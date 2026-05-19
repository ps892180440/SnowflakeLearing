# dbt + Snowflake CI/CD 完整实施指南

> 以 `dbt_finance`（证券交易平台数据仓库）项目为实战案例

---

## 目录

- [一、什么是 dbt CI/CD](#sec-1)
  - [1.1 概念说明](#sec-1-1)
  - [1.2 为什么需要 CI/CD](#sec-1-2)
  - [1.3 整体流程图](#sec-1-3)
- [二、案例项目：dbt_finance](#sec-2)
  - [2.1 项目背景](#sec-2-1)
  - [2.2 项目统计](#sec-2-2)
  - [2.3 项目文件结构](#sec-2-3)
- [三、环境分离](#sec-3)
  - [3.1 双环境配置](#sec-3-1)
  - [3.2 profiles.yml 配置](#sec-3-2)
- [四、CI 工作流详解（GitHub Actions）](#sec-4)
  - [4.1 文件：ci.yml](#sec-4-1)
  - [4.2 CI 执行流程图](#sec-4-2)
  - [4.3 CI 具体验证内容](#sec-4-3)
- [五、CD 工作流详解（GitHub Actions）](#sec-5)
  - [5.1 文件：cd.yml](#sec-5-1)
  - [5.2 CI 与 CD 的关键区别](#sec-5-2)
- [六、定时调度（Snowflake TASK）](#sec-6)
  - [6.1 文件：schedules.sql](#sec-6-1)
  - [6.2 TASK 链路关系](#sec-6-2)
- [七、GitHub 端配置步骤](#sec-7)
  - [7.1 添加 GitHub Secrets](#sec-7-1)
  - [7.2 创建 Production 环境](#sec-7-2)
  - [7.3 配置分支保护规则](#sec-7-3)
  - [7.4 Push 文件到仓库](#sec-7-4)
- [八、日常开发工作流实战](#sec-8)
- [九、故障排查](#sec-9)
  - [9.1 CI 失败常见原因](#sec-9-1)
  - [9.2 CD 失败后的回滚](#sec-9-2)
- [十、配置清单总览](#sec-10)
  - [Snowflake 侧](#sec-10-sf)
  - [GitHub 侧](#sec-10-gh)
- [十一、GitLab CI/CD 配置](#sec-11)
  - [11.1 文件：.gitlab-ci.yml](#sec-11-1)
  - [11.2 GitLab CI 执行流程图](#sec-11-2)
  - [11.3 GitLab CD 执行流程图](#sec-11-3)
  - [11.4 CI 与 CD 的区别（GitLab 版）](#sec-11-4)
  - [11.5 GitLab 端配置步骤](#sec-11-5)
  - [11.6 日常开发工作流（GitLab 版）](#sec-11-6)
  - [11.7 GitHub Actions vs GitLab CI/CD 对照表](#sec-11-7)

---

<a id="sec-1"></a>

## 一、什么是 dbt CI/CD

<a id="sec-1-1"></a>

### 1.1 概念说明

| 术语 | 全称 | 含义 |
|------|------|------|
| **CI** | Continuous Integration（持续集成） | 每次提交代码后，自动编译、运行、测试 dbt 模型，确保新代码不会破坏现有逻辑 |
| **CD** | Continuous Deployment（持续部署） | 代码合并到主分支后，自动将变更部署到生产环境，无需人工干预 |
| **Git** | 版本控制系统 | 管理 dbt 项目的所有代码变更历史，支持多人协作 |

<a id="sec-1-2"></a>

### 1.2 为什么需要 CI/CD

**没有 CI/CD 的痛点：**
- 开发者手动登录 Snowflake → 手动 `dbt run` → 容易忘记 `dbt test`
- 多人协作时容易互相覆盖代码
- 生产环境出错后无法快速回滚
- 无法追溯"谁在什么时候改了什么"

**有 CI/CD 之后：**
- 代码一提交就自动验证，有问题立刻发现
- 测试不通过无法合并到主分支，生产环境有保障
- 所有变更有 Git 记录，可追溯可回滚
- 全程自动化，减少人为失误

<a id="sec-1-3"></a>

### 1.3 整体流程图

```
开发者在 Snowflake Workspace 中修改 dbt 模型
       │
       ▼
  git commit & git push → 推送到 feature 分支
       │
       ▼
  在 GitHub 上创建 Pull Request（PR）→ 目标分支 main
       │
       ▼
  ┌─────────── CI 自动触发 ────────────────────────┐
  │                                                  │
  │   GitHub Actions 运行 ci.yml：                   │
  │                                                  │
  │   Step 1: dbt compile                            │
  │           → 检查 SQL 语法和 Jinja 模板是否正确    │
  │                                                  │
  │   Step 2: dbt run --target dev                   │
  │           → 在 DEV 环境（SCHM_F_SNOWLEARN_DEV）  │
  │             执行所有模型，验证 SQL 可运行          │
  │                                                  │
  │   Step 3: dbt test --target dev                  │
  │           → 运行 33 个数据质量测试                │
  │           → unique / not_null / accepted_values   │
  │             / relationships                       │
  │                                                  │
  │   全部通过 ✅ → PR 标记为"检查通过"              │
  │   任一失败 ❌ → PR 标记为"检查未通过"，阻止合并  │
  │                                                  │
  └──────────────────────────────────────────────────┘
       │
       ▼
  团队成员 Code Review（代码审查）
       │
       ▼
  审核通过，点击 "Merge Pull Request" 合并到 main
       │
       ▼
  ┌─────────── CD 自动触发 ────────────────────────┐
  │                                                  │
  │   GitHub Actions 运行 cd.yml：                   │
  │                                                  │
  │   Step 1: dbt run --target prod                  │
  │           → 在 PROD 环境（SCHM_F_SNOWLEARN_01） │
  │             执行所有模型，更新生产表              │
  │                                                  │
  │   Step 2: dbt test --target prod                 │
  │           → 验证生产数据质量                      │
  │                                                  │
  └──────────────────────────────────────────────────┘
       │
       ▼
  生产环境更新完成，Snowflake TASK 可定时复跑
```

---

<a id="sec-2"></a>

## 二、案例项目：dbt_finance

<a id="sec-2-1"></a>

### 2.1 项目背景

`dbt_finance` 是一个证券交易平台数据仓库项目，基于 `TEST_SNOWFLAKE_LEANING` 数据库，包含四层数据架构：

```
Landing 层 (原始数据)             Staging 层 (清洗)               Intermediate 层 (关联)        Marts 层 (业务)
─────────────────────           ──────────────────              ─────────────────────         ──────────────────
RAW_TRADE_ORDERS      ──→  stg_raw_trade_orders  ──┐
RAW_STOCK_QUOTES      ──→  stg_raw_stock_quotes     │
RAW_FUND_NAV          ──→  stg_raw_fund_nav          ├──→  int_trade_enriched  ──┬──→ mart_client_trading_summary
DIM_ACCOUNT           ──→  stg_dim_account  ────────┤                           ├──→ mart_security_daily_performance
DIM_SECURITY          ──→  stg_dim_security  ───────┘                           └──→ mart_risk_exposure_report
```

<a id="sec-2-2"></a>

### 2.2 项目统计

| 指标 | 数据 |
|------|------|
| 模型总数 | 9 个（5 staging + 1 intermediate + 3 marts） |
| 数据源 | 5 张表，跨 2 个 schema |
| 测试总数 | 33 个（unique / not_null / accepted_values / relationships） |
| 物化策略 | staging + intermediate → view，marts → table |

<a id="sec-2-3"></a>

### 2.3 项目文件结构

```
dbt_finance/
├── dbt_project.yml                            # 项目配置
├── profiles.yml                                # 连接配置（dev/prod 双环境）
├── schedules.sql                               # Snowflake TASK 定时调度
├── models/
│   ├── sources.yml                             # 数据源声明
│   ├── schema.yml                              # 测试定义（33 个测试）
│   ├── staging/
│   │   ├── stg_raw_trade_orders.sql
│   │   ├── stg_raw_stock_quotes.sql
│   │   ├── stg_raw_fund_nav.sql
│   │   ├── stg_dim_account.sql
│   │   └── stg_dim_security.sql
│   ├── intermediate/
│   │   └── int_trade_enriched.sql
│   └── marts/
│       ├── mart_client_trading_summary.sql
│       ├── mart_security_daily_performance.sql
│       └── mart_risk_exposure_report.sql
└── logs/

.github/                                      # GitHub Actions（任选其一）
└── workflows/
    ├── ci.yml                                  # CI 工作流（PR 触发）
    └── cd.yml                                  # CD 工作流（合并到 main 触发）
.gitlab-ci.yml                                  # GitLab CI/CD（任选其一）
                                                #   单文件，CI + CD 合并定义
```

---

<a id="sec-3"></a>

## 三、环境分离

CI/CD 的基础是**环境分离**——开发/测试在 DEV 环境运行，不影响生产数据。

<a id="sec-3-1"></a>

### 3.1 双环境配置

| 环境 | Schema | 用途 | 何时使用 |
|------|--------|------|----------|
| **dev** | `SCHM_F_SNOWLEARN_DEV` | 开发和 CI 测试 | 本地开发 + Pull Request 自动验证 |
| **prod** | `SCHM_F_SNOWLEARN_01` | 生产数据 | 合并到 main 后自动部署 |

<a id="sec-3-2"></a>

### 3.2 profiles.yml 配置

**中文说明：** 这是 dbt 的连接配置文件，定义了两个环境。在 Snowflake Workspace 中本地开发时，`account` 和 `user` 留空（使用当前会话）；在 GitHub Actions CI/CD 中，这个文件会被覆写为使用环境变量注入的凭据。

```yaml
dbt_finance:
  target: dev                          # 默认使用 dev 环境
  outputs:
    dev:                                # ── 开发 / CI 环境 ──
      type: snowflake
      account: ""                       # Workspace 中留空，CI 中覆写
      user: ""
      role: "ACCOUNTADMIN"
      database: "TEST_SNOWFLAKE_LEANING"
      warehouse: "COMPUTE_WH"
      schema: "SCHM_F_SNOWLEARN_DEV"    # ← DEV schema
      threads: 4
    prod:                               # ── 生产环境 ──
      type: snowflake
      account: ""
      user: ""
      role: "ACCOUNTADMIN"
      database: "TEST_SNOWFLAKE_LEANING"
      warehouse: "COMPUTE_WH"
      schema: "SCHM_F_SNOWLEARN_01"     # ← PROD schema
      threads: 4
```

**关键点：**
- `target: dev` 表示默认使用 dev 环境，本地开发时 `dbt run` 不会影响生产
- 运行生产环境需要显式指定 `dbt run --target prod`
- CI 工作流用 `--target dev`，CD 工作流用 `--target prod`

---

<a id="sec-4"></a>

## 四、CI 工作流详解

<a id="sec-4-1"></a>

### 4.1 文件：`.github/workflows/ci.yml`

**触发条件：** 当对 `main` 分支发起 Pull Request，且修改了 `dbt_finance/` 目录下的文件时自动触发。

**完整文件内容及逐行注释：**

```yaml
name: dbt CI - Pull Request Validation       # 工作流名称，显示在 GitHub Actions 页面

on:
  pull_request:                               # 触发事件：Pull Request
    branches: [main]                          # 目标分支：main
    paths:
      - 'dbt_finance/**'                      # 只有 dbt_finance 目录有变更才触发

env:                                          # 全局环境变量，从 GitHub Secrets 注入
  SNOWFLAKE_ACCOUNT: ${{ secrets.SNOWFLAKE_ACCOUNT }}
  SNOWFLAKE_USER: ${{ secrets.SNOWFLAKE_USER }}
  SNOWFLAKE_PASSWORD: ${{ secrets.SNOWFLAKE_PASSWORD }}
  SNOWFLAKE_ROLE: ACCOUNTADMIN
  SNOWFLAKE_WAREHOUSE: COMPUTE_WH
  SNOWFLAKE_DATABASE: TEST_SNOWFLAKE_LEANING
  DBT_PROFILES_DIR: ./dbt_finance

jobs:
  dbt-ci:
    name: dbt Compile + Run + Test (Dev)      # Job 名称
    runs-on: ubuntu-latest                     # 运行在 GitHub 托管的 Ubuntu 机器上
    steps:
      - name: Checkout code                    # 步骤1：拉取仓库代码
        uses: actions/checkout@v4

      - name: Setup Python                     # 步骤2：安装 Python 3.11
        uses: actions/setup-python@v5
        with:
          python-version: '3.11'

      - name: Install dbt-snowflake            # 步骤3：安装 dbt-snowflake
        run: pip install dbt-snowflake==1.9.*

      - name: Override profiles.yml for CI     # 步骤4：覆写 profiles.yml
        run: |                                  #   注入 Snowflake 连接信息
          cat > dbt_finance/profiles.yml << 'EOF'
          dbt_finance:
            target: dev
            outputs:
              dev:
                type: snowflake
                account: "{{ env_var('SNOWFLAKE_ACCOUNT') }}"
                user: "{{ env_var('SNOWFLAKE_USER') }}"
                password: "{{ env_var('SNOWFLAKE_PASSWORD') }}"
                role: "{{ env_var('SNOWFLAKE_ROLE') }}"
                database: "{{ env_var('SNOWFLAKE_DATABASE') }}"
                warehouse: "{{ env_var('SNOWFLAKE_WAREHOUSE') }}"
                schema: "SCHM_F_SNOWLEARN_DEV"
                threads: 4
          EOF

      - name: dbt deps                         # 步骤5：安装 dbt 依赖包
        run: dbt deps --project-dir dbt_finance

      - name: dbt compile                      # 步骤6：编译检查
        run: dbt compile --project-dir dbt_finance

      - name: dbt run (dev)                    # 步骤7：在 DEV 环境运行全部模型
        run: dbt run --project-dir dbt_finance --target dev

      - name: dbt test                         # 步骤8：执行全部 33 个数据测试
        run: dbt test --project-dir dbt_finance --target dev

      - name: Post test summary                # 步骤9：输出摘要到 PR 页面
        if: always()
        run: |
          echo "## dbt CI Results" >> $GITHUB_STEP_SUMMARY
          echo "- **Compile**: ✅ Passed" >> $GITHUB_STEP_SUMMARY
          echo "- **Run**: Check step output" >> $GITHUB_STEP_SUMMARY
          echo "- **Test**: Check step output" >> $GITHUB_STEP_SUMMARY
```

<a id="sec-4-2"></a>

### 4.2 CI 执行流程图

```
PR 创建/更新
     │
     ▼
┌─ Step 1: Checkout ──────────────────────────────────┐
│  从 GitHub 拉取最新代码到运行器                       │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ Step 2-3: 环境准备 ───────────────────────────────┐
│  安装 Python 3.11 + dbt-snowflake 1.9              │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ Step 4: 覆写 profiles.yml ────────────────────────┐
│  用 GitHub Secrets 注入 Snowflake 凭据              │
│  目标 schema: SCHM_F_SNOWLEARN_DEV（不碰生产）     │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ Step 5: dbt deps ─────────────────────────────────┐
│  安装 dbt 第三方依赖包（如有）                       │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ Step 6: dbt compile ──────────────────────────────┐
│  编译所有 9 个模型                                   │
│  检查：SQL 语法、Jinja 模板、ref()/source() 引用    │
│  ❌ 失败 → 停止，PR 标记为失败                      │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ Step 7: dbt run --target dev ─────────────────────┐
│  在 SCHM_F_SNOWLEARN_DEV 中执行全部模型：           │
│  → 5 个 view (staging)                              │
│  → 1 个 view (intermediate)                         │
│  → 3 个 table (marts)                               │
│  ❌ 失败 → 停止，PR 标记为失败                      │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ Step 8: dbt test --target dev ────────────────────┐
│  运行 schema.yml 中定义的 33 个数据测试：           │
│  → 7 个 unique 测试                                 │
│  → 16 个 not_null 测试                              │
│  → 9 个 accepted_values 测试                        │
│  → 1 个 relationships 测试                          │
│  ❌ 任何测试失败 → PR 标记为失败                    │
└─────────────────────────────────────────────────────┘
     │
     ▼
  全部通过 ✅ → PR 显示绿色勾号，允许合并
```

<a id="sec-4-3"></a>

### 4.3 CI 在 dbt_finance 项目中具体验证了什么

以本项目为例，CI 执行的 33 个测试：

| 模型 | 测试内容 | 测试数量 |
|------|----------|---------|
| `stg_raw_trade_orders` | order_id 唯一+非空、direction 值域（BUY/SELL）、order_type 值域（LIMIT/MARKET）、status 值域（FILLED/PARTIAL/CANCELLED）、quantity 非空、price 非空 | 8 |
| `stg_raw_stock_quotes` | symbol 非空、trade_date 非空 | 2 |
| `stg_raw_fund_nav` | fund_code 非空、nav_date 非空 | 2 |
| `stg_dim_account` | account_key 唯一+非空、account_id 唯一+非空、risk_level 值域（LOW/MEDIUM/HIGH） | 5 |
| `stg_dim_security` | security_key 唯一+非空、symbol 唯一+非空 | 4 |
| `int_trade_enriched` | order_id 唯一+非空、client_name 非空、symbol 非空+引用完整性 | 4 |
| `mart_client_trading_summary` | account_id 非空、fill_rate_pct 非空 | 2 |
| `mart_security_daily_performance` | symbol 非空、trade_date 非空、price_trend 值域、volume_tier 值域 | 4 |
| `mart_risk_exposure_report` | account_id 非空、exposure_level 值域 | 2 |
| **合计** | | **33** |

---

<a id="sec-5"></a>

## 五、CD 工作流详解

<a id="sec-5-1"></a>

### 5.1 文件：`.github/workflows/cd.yml`

**触发条件：** 当代码被合并（push）到 `main` 分支，且修改了 `dbt_finance/` 目录下的文件时自动触发。

**完整文件内容及逐行注释：**

```yaml
name: dbt CD - Deploy to Production          # 工作流名称

on:
  push:                                        # 触发事件：push（包括 PR 合并）
    branches: [main]                           # 目标分支：main
    paths:
      - 'dbt_finance/**'                       # 只有 dbt_finance 目录有变更才触发

env:
  SNOWFLAKE_ACCOUNT: ${{ secrets.SNOWFLAKE_ACCOUNT }}
  SNOWFLAKE_USER: ${{ secrets.SNOWFLAKE_USER }}
  SNOWFLAKE_PASSWORD: ${{ secrets.SNOWFLAKE_PASSWORD }}
  SNOWFLAKE_ROLE: ACCOUNTADMIN
  SNOWFLAKE_WAREHOUSE: COMPUTE_WH
  SNOWFLAKE_DATABASE: TEST_SNOWFLAKE_LEANING

jobs:
  dbt-cd:
    name: dbt Run + Test (Prod)
    runs-on: ubuntu-latest
    environment: production                    # ← 关键：绑定 GitHub Environment
                                               #   可设置人工审批、部署保护规则
    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: '3.11'

      - name: Install dbt-snowflake
        run: pip install dbt-snowflake==1.9.*

      - name: Override profiles.yml for CD     # 覆写为 PROD 环境
        run: |
          cat > dbt_finance/profiles.yml << 'EOF'
          dbt_finance:
            target: prod                        # ← 目标环境改为 prod
            outputs:
              prod:
                type: snowflake
                account: "{{ env_var('SNOWFLAKE_ACCOUNT') }}"
                user: "{{ env_var('SNOWFLAKE_USER') }}"
                password: "{{ env_var('SNOWFLAKE_PASSWORD') }}"
                role: "{{ env_var('SNOWFLAKE_ROLE') }}"
                database: "{{ env_var('SNOWFLAKE_DATABASE') }}"
                warehouse: "{{ env_var('SNOWFLAKE_WAREHOUSE') }}"
                schema: "SCHM_F_SNOWLEARN_01"   # ← PROD schema
                threads: 4
          EOF

      - name: dbt deps
        run: dbt deps --project-dir dbt_finance

      - name: dbt run (prod)                   # 在生产环境执行所有模型
        run: dbt run --project-dir dbt_finance --target prod

      - name: dbt test (prod)                  # 生产环境数据质量验证
        run: dbt test --project-dir dbt_finance --target prod

      - name: Post deploy summary              # 输出部署摘要
        if: always()
        run: |
          echo "## dbt Production Deployment" >> $GITHUB_STEP_SUMMARY
          echo "- **Branch**: ${{ github.ref_name }}" >> $GITHUB_STEP_SUMMARY
          echo "- **Commit**: ${{ github.sha }}" >> $GITHUB_STEP_SUMMARY
          echo "- **Deployer**: ${{ github.actor }}" >> $GITHUB_STEP_SUMMARY
```

<a id="sec-5-2"></a>

### 5.2 CI 与 CD 的关键区别

| 对比项 | CI（ci.yml） | CD（cd.yml） |
|--------|-------------|-------------|
| 触发事件 | `pull_request` | `push`（合并到 main） |
| 目标环境 | dev（`SCHM_F_SNOWLEARN_DEV`） | prod（`SCHM_F_SNOWLEARN_01`） |
| 是否执行 compile | 是 | 否（CI 已验证过） |
| environment 保护 | 无 | `production`（可设人工审批） |
| 失败影响 | 阻止 PR 合并 | 需手动回滚或重新部署 |

---

<a id="sec-6"></a>

## 六、定时调度（Snowflake TASK）

<a id="sec-6-1"></a>

### 6.1 文件：`dbt_finance/schedules.sql`

除了 CI/CD 自动部署外，生产环境通常还需要**定时调度**——比如每天早上自动重新运行 dbt 模型，确保数据是最新的。

**完整 SQL 及说明：**

```sql
-- 前提：先通过 CREATE DBT PROJECT 将项目部署到 Snowflake
CREATE DBT PROJECT TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DBT_FINANCE
  FROM 'snow://workspace/USER$.PUBLIC."SnowflakeLearing"/versions/live/dbt_finance';

-- 主任务：每天早上 6 点（北京时间）自动运行 dbt run
CREATE OR REPLACE TASK TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.TASK_DBT_FINANCE_DAILY_RUN
  WAREHOUSE = COMPUTE_WH
  SCHEDULE = 'USING CRON 0 6 * * * Asia/Shanghai'
  COMMENT = 'Daily dbt_finance production run at 6:00 AM CST'
AS
  EXECUTE DBT PROJECT TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DBT_FINANCE ARGS = 'run';

-- 子任务：run 完成后自动触发 test
CREATE OR REPLACE TASK TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.TASK_DBT_FINANCE_DAILY_TEST
  WAREHOUSE = COMPUTE_WH
  AFTER TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.TASK_DBT_FINANCE_DAILY_RUN
  COMMENT = 'Daily dbt_finance test after run'
AS
  EXECUTE DBT PROJECT TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DBT_FINANCE ARGS = 'test';

-- 启用任务（注意：子任务必须先启用）
ALTER TASK TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.TASK_DBT_FINANCE_DAILY_TEST RESUME;
ALTER TASK TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.TASK_DBT_FINANCE_DAILY_RUN RESUME;
```

<a id="sec-6-2"></a>

### 6.2 TASK 链路关系

```
每天 06:00 (Asia/Shanghai)
       │
       ▼
  TASK_DBT_FINANCE_DAILY_RUN
  → EXECUTE DBT PROJECT ... ARGS = 'run'
  → 执行 9 个模型，更新生产表
       │ 成功后自动触发
       ▼
  TASK_DBT_FINANCE_DAILY_TEST
  → EXECUTE DBT PROJECT ... ARGS = 'test'
  → 执行 33 个数据测试
```

---

<a id="sec-7"></a>

## 七、GitHub 端配置步骤

<a id="sec-7-1"></a>

### 7.1 第一步：添加 GitHub Secrets

路径：GitHub 仓库 → **Settings** → **Secrets and variables** → **Actions** → **New repository secret**

需要添加 3 个 Secret：

| Secret 名 | 值 | 说明 |
|-----------|-----|------|
| `SNOWFLAKE_ACCOUNT` | `ogb30853` | Snowflake 账户标识符 |
| `SNOWFLAKE_USER` | `PENGB` | Snowflake 用户名 |
| `SNOWFLAKE_PASSWORD` | `（你的密码）` | Snowflake 登录密码 |

> **安全最佳实践：**
> - 生产环境建议创建专用的**服务账号**（Service Account），不使用个人账号
> - 更安全的方式是使用**密钥对认证**（Key Pair Authentication）替代密码
> - 服务账号应授予最小必要权限，而非 ACCOUNTADMIN

<a id="sec-7-2"></a>

### 7.2 第二步：创建 Production 环境（推荐）

路径：GitHub 仓库 → **Settings** → **Environments** → **New environment**

1. 环境名输入：`production`
2. 勾选 **Required reviewers**，添加至少 1 位审批人
3. 效果：CD 部署前会暂停，等待审批人点击"Approve"后才继续

<a id="sec-7-3"></a>

### 7.3 第三步：配置分支保护规则（推荐）

路径：GitHub 仓库 → **Settings** → **Branches** → **Add branch protection rule**

1. Branch name pattern：`main`
2. 勾选：
   - ✅ **Require a pull request before merging**（必须通过 PR 合并）
   - ✅ **Require status checks to pass before merging**（CI 必须通过）
   - ✅ **Require approvals**（至少 1 人审核）
3. 效果：没有人能直接 push 到 main，所有变更必须走 PR → CI → Review → 合并

<a id="sec-7-4"></a>

### 7.4 第四步：Push 文件到 Git 仓库

确保以下文件已提交并推送到 GitHub：

```bash
git add .github/workflows/ci.yml
git add .github/workflows/cd.yml
git add dbt_finance/profiles.yml
git add dbt_finance/schedules.sql
git commit -m "feat: add CI/CD pipeline for dbt_finance"
git push origin main
```

---

<a id="sec-8"></a>

## 八、日常开发工作流实战

以"给 mart_client_trading_summary 添加一个 avg_order_amount 字段"为例：

### 步骤 1：创建 feature 分支

```bash
git checkout -b feature/add-avg-order-amount
```

### 步骤 2：在 Workspace 中修改模型

编辑 `dbt_finance/models/marts/mart_client_trading_summary.sql`，添加：

```sql
round(sum(computed_total_amount) / nullif(count(distinct order_id), 0), 2) as avg_order_amount
```

### 步骤 3：本地验证

```bash
dbt compile --project-dir dbt_finance
dbt run --project-dir dbt_finance --select mart_client_trading_summary
dbt test --project-dir dbt_finance --select mart_client_trading_summary
```

### 步骤 4：提交并推送

```bash
git add dbt_finance/models/marts/mart_client_trading_summary.sql
git commit -m "feat: add avg_order_amount to client trading summary"
git push origin feature/add-avg-order-amount
```

### 步骤 5：创建 Pull Request

在 GitHub 上创建 PR：`feature/add-avg-order-amount` → `main`

### 步骤 6：等待 CI 自动验证

GitHub Actions 自动运行 `ci.yml`：
- dbt compile ✅
- dbt run (dev) ✅
- dbt test (dev) ✅ — 33 个测试全部通过

### 步骤 7：Code Review + 合并

团队成员审核代码 → 批准 → 合并到 main

### 步骤 8：CD 自动部署到生产

GitHub Actions 自动运行 `cd.yml`：
- dbt run (prod) → 生产表 `MART_CLIENT_TRADING_SUMMARY` 自动更新
- dbt test (prod) → 数据质量验证通过

**整个过程中，开发者无需手动登录 Snowflake 执行任何 SQL。**

---

<a id="sec-9"></a>

## 九、故障排查

<a id="sec-9-1"></a>

### 9.1 CI 失败常见原因

| 错误 | 原因 | 解决方式 |
|------|------|---------|
| `Compilation Error` | SQL 语法错误或 ref/source 引用不存在 | 检查模型 SQL，确认引用的模型名拼写正确 |
| `Database Error` | Snowflake 连接失败 | 检查 GitHub Secrets 是否正确配置 |
| `Test Failure` | 数据质量测试未通过 | 查看失败的测试，修复数据或调整测试规则 |
| `Permission Error` | 角色权限不足 | 确认服务账号有目标 schema 的 CREATE 权限 |

<a id="sec-9-2"></a>

### 9.2 CD 失败后的回滚

```bash
# 方式1：Git revert（推荐）
git revert <commit-sha>
git push origin main
# CD 会自动触发，回滚生产环境

# 方式2：手动运行指定版本
git checkout <good-commit-sha>
dbt run --project-dir dbt_finance --target prod
```

---

<a id="sec-10"></a>

## 十、配置清单总览

<a id="sec-10-sf"></a>

### Snowflake 侧

| 配置项 | 状态 | 说明 |
|--------|------|------|
| DEV Schema | ✅ 已创建 | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV` |
| PROD Schema | ✅ 已存在 | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01` |
| profiles.yml | ✅ 已更新 | dev/prod 双环境配置 |
| schedules.sql | ✅ 已创建 | Snowflake TASK 每日 6:00 定时调度 |

<a id="sec-10-gh"></a>

### GitHub 侧

| 配置项 | 状态 | 操作 |
|--------|------|------|
| ci.yml | ✅ 已创建 | `.github/workflows/ci.yml` |
| cd.yml | ✅ 已创建 | `.github/workflows/cd.yml` |
| Secrets | ⏳ 需手动配置 | 添加 SNOWFLAKE_ACCOUNT / USER / PASSWORD |
| production 环境 | ⏳ 推荐配置 | 添加部署审批规则 |
| 分支保护规则 | ⏳ 推荐配置 | main 分支要求 PR + CI 通过 + Review |

---

<a id="sec-11"></a>

## 十一、GitLab CI/CD 配置

> GitLab 版 CI/CD 配置，逻辑与 GitHub Actions 版本一致，适配 GitLab CI 语法和机制。

<a id="sec-11-1"></a>

### 11.1 文件：`.gitlab-ci.yml`

GitLab CI/CD 使用单一配置文件，通过 `rules` 和 `stages` 区分 CI（Merge Request）和 CD（合并到 main）。

**触发条件：**
- **CI job（dbt-ci）：** 当对 `main` 分支发起 Merge Request，且修改了 `dbt_finance/` 目录下的文件时自动触发
- **CD job（dbt-cd）：** 当代码被合并（push）到 `main` 分支，且修改了 `dbt_finance/` 目录下的文件时触发，需手动确认

**完整文件内容及逐行注释：**

```yaml
# ──────────────────── Pipeline 全局定义 ────────────────────
stages:                                        # 定义 Pipeline 阶段，按顺序执行
  - ci                                         #   阶段1：CI（Merge Request 时触发）
  - cd                                         #   阶段2：CD（合并到 main 后触发）

variables:                                     # 全局变量，所有 job 共享
  DBT_PROJECT_DIR: ./dbt_finance               #   dbt 项目目录路径
  SNOWFLAKE_ROLE: ACCOUNTADMIN                 #   Snowflake 角色
  SNOWFLAKE_WAREHOUSE: COMPUTE_WH              #   Snowflake 计算仓库
  SNOWFLAKE_DATABASE: TEST_SNOWFLAKE_LEANING   #   Snowflake 数据库

# ──────────────────── CI Job：Merge Request 验证 ────────────────────
dbt-ci:                                        # Job 名称，显示在 GitLab Pipeline 页面
  stage: ci                                    # 归属 ci 阶段
  image: python:3.11                           # 使用 Python 3.11 Docker 镜像作为运行环境
  rules:                                       # 触发规则（类似 GitHub Actions 的 on: pull_request）
    - if: $CI_PIPELINE_SOURCE == "merge_request_event" && $CI_MERGE_REQUEST_TARGET_BRANCH_NAME == "main"
                                               #   条件：MR 事件 且 目标分支为 main
      changes:                                 #   路径过滤：只有指定目录变更才触发
        - dbt_finance/**/*                     #     监控 dbt_finance 目录下所有文件
  before_script:                               # 前置脚本：在 script 之前执行（环境准备）
    - pip install dbt-snowflake==1.9.*         #   安装 dbt-snowflake 1.9.x
    - |                                        #   覆写 profiles.yml，注入 CI/CD Variables
      cat > dbt_finance/profiles.yml << 'EOF'  #     使用 heredoc 写入 DEV 环境配置
      dbt_finance:
        target: dev                            #     目标环境：dev
        outputs:
          dev:
            type: snowflake
            account: "{{ env_var('SNOWFLAKE_ACCOUNT') }}"
            user: "{{ env_var('SNOWFLAKE_USER') }}"
            password: "{{ env_var('SNOWFLAKE_PASSWORD') }}"
            role: "{{ env_var('SNOWFLAKE_ROLE') }}"
            database: "{{ env_var('SNOWFLAKE_DATABASE') }}"
            warehouse: "{{ env_var('SNOWFLAKE_WAREHOUSE') }}"
            schema: "SCHM_F_SNOWLEARN_DEV"     #     ← DEV schema，不影响生产
            threads: 4
      EOF
    - dbt deps --project-dir dbt_finance       #   安装 dbt 依赖包
  script:                                      # 主脚本：核心 CI 验证步骤
    - dbt compile --project-dir dbt_finance    #   Step 1: 编译检查 SQL + Jinja 语法
    - dbt run --project-dir dbt_finance --target dev
                                               #   Step 2: 在 DEV 环境执行全部模型
    - dbt test --project-dir dbt_finance --target dev
                                               #   Step 3: 运行全部数据质量测试
  after_script:                                # 后置脚本：无论成功失败都执行
    - |                                        #   输出 CI 结果摘要
      echo "## dbt CI Results" >> ci_summary.md
      echo "- **Compile**: ✅ Passed" >> ci_summary.md
      echo "- **Run**: Check job log" >> ci_summary.md
      echo "- **Test**: Check job log" >> ci_summary.md

# ──────────────────── CD Job：生产环境部署 ────────────────────
dbt-cd:                                        # Job 名称
  stage: cd                                    # 归属 cd 阶段（CI 全部通过后才到达此阶段）
  image: python:3.11                           # 使用 Python 3.11 Docker 镜像
  rules:                                       # 触发规则（类似 GitHub Actions 的 on: push: branches: [main]）
    - if: $CI_COMMIT_BRANCH == "main"          #   条件：push 到 main 分支（含 MR 合并）
      changes:                                 #   路径过滤：
        - dbt_finance/**/*                     #     只有 dbt_finance 目录变更才触发
  environment:                                 # 关联 GitLab 环境（用于环境保护和部署追踪）
    name: production                           #   环境名：production
  when: manual                                 # 手动触发：Pipeline 在此暂停，需人工点击 ▶ 按钮
                                               #   作用等同于 GitHub 的 Environment 审批门禁
  before_script:                               # 前置脚本：准备 PROD 环境连接
    - pip install dbt-snowflake==1.9.*
    - |                                        #   覆写 profiles.yml 为 PROD 环境
      cat > dbt_finance/profiles.yml << 'EOF'
      dbt_finance:
        target: prod                           #     目标环境：prod
        outputs:
          prod:
            type: snowflake
            account: "{{ env_var('SNOWFLAKE_ACCOUNT') }}"
            user: "{{ env_var('SNOWFLAKE_USER') }}"
            password: "{{ env_var('SNOWFLAKE_PASSWORD') }}"
            role: "{{ env_var('SNOWFLAKE_ROLE') }}"
            database: "{{ env_var('SNOWFLAKE_DATABASE') }}"
            warehouse: "{{ env_var('SNOWFLAKE_WAREHOUSE') }}"
            schema: "SCHM_F_SNOWLEARN_01"      #     ← PROD schema
            threads: 4
      EOF
    - dbt deps --project-dir dbt_finance
  script:                                      # 主脚本：生产部署
    - dbt run --project-dir dbt_finance --target prod
                                               #   Step 1: 在生产环境执行全部模型
    - dbt test --project-dir dbt_finance --target prod
                                               #   Step 2: 生产环境数据质量验证
  after_script:                                # 后置脚本：输出部署摘要
    - |
      echo "## dbt Production Deployment" >> deploy_summary.md
      echo "- **Branch**: ${CI_COMMIT_BRANCH}" >> deploy_summary.md
      echo "- **Commit**: ${CI_COMMIT_SHA}" >> deploy_summary.md
      echo "- **Deployer**: ${GITLAB_USER_NAME}" >> deploy_summary.md
```

<a id="sec-11-2"></a>

### 11.2 GitLab CI 执行流程图

```
MR 创建/更新（目标 main）
     │
     ▼
┌─ Stage: ci ─────────────────────────────────────────┐
│  Runner 拉取 python:3.11 Docker 镜像                │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ before_script: 环境准备 ───────────────────────────┐
│  安装 dbt-snowflake 1.9 + 覆写 profiles.yml         │
│  用 GitLab CI/CD Variables 注入 Snowflake 凭据       │
│  目标 schema: SCHM_F_SNOWLEARN_DEV（不碰生产）      │
│  dbt deps 安装依赖                                  │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ script Step 1: dbt compile ────────────────────────┐
│  编译所有 9 个模型                                   │
│  检查：SQL 语法、Jinja 模板、ref()/source() 引用    │
│  ❌ 失败 → Pipeline 标记 Failed，阻止 MR 合并       │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ script Step 2: dbt run --target dev ───────────────┐
│  在 SCHM_F_SNOWLEARN_DEV 中执行全部模型：           │
│  → 5 个 view (staging)                              │
│  → 1 个 view (intermediate)                         │
│  → 3 个 table (marts)                               │
│  ❌ 失败 → Pipeline 标记 Failed                     │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ script Step 3: dbt test --target dev ──────────────┐
│  运行 schema.yml 中定义的 33 个数据测试：           │
│  → 7 个 unique + 16 个 not_null                     │
│  → 9 个 accepted_values + 1 个 relationships       │
│  ❌ 任何测试失败 → Pipeline 标记 Failed             │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ after_script: 输出摘要 ────────────────────────────┐
│  生成 ci_summary.md，记录 Compile/Run/Test 结果     │
└─────────────────────────────────────────────────────┘
     │
     ▼
  全部通过 ✅ → MR 显示绿色 "Pipeline passed"，允许合并
```

<a id="sec-11-3"></a>

### 11.3 GitLab CD 执行流程图

```
MR 合并到 main（或直接 push 到 main）
     │
     ▼
┌─ Stage: cd ─────────────────────────────────────────┐
│  Pipeline 到达 cd 阶段，显示 ⏸ "Blocked" 状态       │
│  when: manual → 需人工点击 ▶ 按钮才能继续           │
└─────────────────────────────────────────────────────┘
     │ （审批人点击 ▶）
     ▼
┌─ before_script: 环境准备 ───────────────────────────┐
│  安装 dbt-snowflake + 覆写 profiles.yml 为 prod     │
│  用 GitLab CI/CD Variables 注入生产凭据              │
│  目标 schema: SCHM_F_SNOWLEARN_01（生产环境）       │
│  dbt deps 安装依赖                                  │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ script Step 1: dbt run --target prod ──────────────┐
│  在 SCHM_F_SNOWLEARN_01 中执行全部 9 个模型         │
│  更新生产表                                         │
│  ❌ 失败 → Pipeline 标记 Failed，需排查后重试       │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ script Step 2: dbt test --target prod ─────────────┐
│  运行 33 个数据质量测试，验证生产数据                │
│  ❌ 失败 → 触发告警，需紧急排查                     │
└─────────────────────────────────────────────────────┘
     │
     ▼
┌─ after_script: 部署摘要 ────────────────────────────┐
│  记录 Branch、Commit SHA、部署人                     │
└─────────────────────────────────────────────────────┘
     │
     ▼
  全部通过 ✅ → 生产环境更新完成
```

<a id="sec-11-4"></a>

### 11.4 CI 与 CD 的关键区别（GitLab 版）

| 对比项 | CI（dbt-ci） | CD（dbt-cd） |
|--------|-------------|-------------|
| 触发条件 | Merge Request → main | push 到 main 分支 |
| 规则写法 | `merge_request_event` | `CI_COMMIT_BRANCH == "main"` |
| 目标环境 | dev（`SCHM_F_SNOWLEARN_DEV`） | prod（`SCHM_F_SNOWLEARN_01`） |
| 是否 compile | 是 | 否（CI 已验证过） |
| 审批机制 | 无（自动触发） | `when: manual`（需手动点击运行） |
| environment | 无 | `production`（关联 GitLab 环境） |

<a id="sec-11-5"></a>

### 11.5 GitLab 端配置步骤

#### 第一步：添加 CI/CD Variables

路径：GitLab 项目 → **Settings** → **CI/CD** → **Variables** → **Add variable**

| Variable 名 | 值 | 类型 | 说明 |
|-------------|-----|------|------|
| `SNOWFLAKE_ACCOUNT` | `ogb30853` | Variable | Snowflake 账户标识符 |
| `SNOWFLAKE_USER` | `PENGB` | Variable | Snowflake 用户名 |
| `SNOWFLAKE_PASSWORD` | `（你的密码）` | **Masked + Protected** | Snowflake 登录密码 |

> - **Masked**：密码不会出现在 CI/CD 日志中
> - **Protected**：变量仅对 protected 分支/tag 可用，防止在 feature 分支中泄露
> - 生产环境建议创建专用**服务账号**（Service Account），使用**密钥对认证**替代密码

#### 第二步：配置 Protected Branch（分支保护规则）

路径：GitLab 项目 → **Settings** → **Repository** → **Protected branches** → **Protect a branch**

**基础保护配置：**

| 配置项 | 推荐值 | 说明 |
|--------|--------|------|
| Branch | `main` | 保护主分支 |
| Allowed to merge | Maintainers（或指定用户/群组） | 谁能合并 MR 到该分支 |
| Allowed to push and merge | No one | 禁止直接 push，强制走 MR 流程 |
| Allowed to force push | 关闭 | 禁止 force push，防止历史被篡改 |
| Allowed to unprotect | 关闭 | 仅项目管理员可解除保护 |

**配置效果：**
- 任何人无法直接 `git push` 到 `main` 分支
- 所有代码变更必须通过 Merge Request
- 只有 Maintainers 及以上角色可以点击 Merge 按钮
- Force push 被完全禁止，保护提交历史完整性

**多级保护示例（推荐）：**

| 分支 | 保护等级 | Allowed to merge | Allowed to push | 说明 |
|------|---------|-----------------|----------------|------|
| `main` | 严格 | Maintainers | No one | 生产分支，MR + 审批 |
| `develop` | 中等 | Developers + Maintainers | No one | 集成分支，MR 但不强制审批 |
| `release/*` | 中等 | Maintainers | No one | 发布分支 |
| `feature/*` | 宽松 | 不保护 | 任何人 | 开发分支，自由 push |

---

#### 第三步：配置 Merge Request 审批规则

路径：GitLab 项目 → **Settings** → **Merge requests** → **Merge request approvals**

**3.1 审批规则配置：**

| 配置项 | 推荐值 | 说明 |
|--------|--------|------|
| Prevent approval by author | ✅ 开启 | 提交者不能审批自己的 MR |
| Prevent approvals by users who add commits | ✅ 开启 | 追加 commit 后之前的审批失效 |
| Prevent editing approval rules in merge requests | ✅ 开启 | 禁止在 MR 中绕过项目级审批规则 |
| Require new password for approvals | 可选 | 审批时需输入密码确认 |

**3.2 添加审批规则：**

路径：同页面 → **Approval rules** → **Add approval rule**

| 规则配置 | 值 | 说明 |
|----------|-----|------|
| Name | `dbt-model-review` | 规则名称 |
| Approvals required | `≥ 1` | 至少 1 人审批 |
| Eligible approvers | 选指定用户/群组 | 熟悉 dbt/数仓的团队成员 |
| Protected branches | `main` | 仅对目标为 main 的 MR 生效 |

**3.3 多级审批示例（推荐）：**

```
┌─ MR 创建 → 目标 main ─────────────────────────┐
│                                                 │
│  ┌─ 审批规则 1：dbt-model-review ────────────┐ │
│  │  审批人数：≥ 1                              │ │
│  │  审批人范围：dbt 开发组成员                 │ │
│  │  作用：确保模型变更经同行审核               │ │
│  └────────────────────────────────────────────┘ │
│                    AND                          │
│  ┌─ 审批规则 2：senior-review（可选）─────────┐ │
│  │  审批人数：≥ 1                              │ │
│  │  审批人范围：Tech Lead / 架构师             │ │
│  │  作用：重大变更需资深人员把关               │ │
│  └────────────────────────────────────────────┘ │
│                    AND                          │
│  ┌─ CI Pipeline 必须通过 ────────────────────┐ │
│  │  dbt compile ✅ + dbt run ✅ + dbt test ✅  │ │
│  └────────────────────────────────────────────┘ │
│                                                 │
│  全部满足 → Merge 按钮可用                      │
└─────────────────────────────────────────────────┘
```

**3.4 配置 Pipeline 必须通过（Merge Checks）：**

路径：GitLab 项目 → **Settings** → **Merge requests** → **Merge checks**

| 配置项 | 推荐值 | 说明 |
|--------|--------|------|
| Pipelines must succeed | ✅ 开启 | CI Pipeline 不通过则无法合并 |
| All threads must be resolved | ✅ 开启 | MR 评论线程全部解决后才能合并 |
| All discussions must be resolved | 可选 | 所有讨论（含代码评论）必须解决 |
| Must reset approvals on push | 可选 | 有新 commit 时清除旧审批 |

**配置效果：**
- MR 页面底部 Merge 按钮在所有条件满足前为灰色不可点击
- Pipeline 失败 → 显示红色 "Pipeline must succeed"
- 审批不足 → 显示 "Approvals: 0 of 1"
- 未解决的讨论 → 显示 "Resolve all discussions"

---

#### 第四步：配置 Environment 保护规则（CD 部署保护）

路径：GitLab 项目 → **Settings** → **CI/CD** → **Environments** → **production** → **Edit**

| 配置项 | 推荐值 | 说明 |
|--------|--------|------|
| Protected environment | ✅ 开启 | 启用环境保护 |
| Allowed to deploy | Maintainers only | 谁能触发 CD 部署 |
| Required approvals | 选 ≥ 1 位审批人 | 部署前指定人员审批（与 `when: manual` 配合） |
| Deploy freeze | 可选 | 设置部署冻结窗口（如周五 18:00 后禁止部署） |

**配置效果：**
- CD job 触发后先在 Pipeline 页面显示为 "Blocked" 状态
- 指定审批人收到通知 → 点击 Approve → Pipeline 继续执行
- 未经审批，CD 无法进入 `dbt run --target prod` 阶段

---

#### 第五步：区分 CI/CD 两套凭据（Production 环境专用变量）

路径：GitLab 项目 → **Settings** → **CI/CD** → **Variables** → **Add variable**

第一步已添加了基础变量。这里进一步按环境隔离，为生产 CD 配置独立的高权限凭据：

| Variable 名 | 值 | 类型 | Protected | Masked | 环境 | 说明 |
|-------------|-----|------|-----------|--------|------|------|
| `SNOWFLAKE_ACCOUNT` | `ogb30853` | Variable | ✅ | 否 | All | 账户标识符 |
| `SNOWFLAKE_USER` | `CI_SVC_ACCOUNT` | Variable | ✅ | 否 | All | CI 专用服务账号 |
| `SNOWFLAKE_PASSWORD` | `（密码）` | Variable | ✅ | ✅ | All | 加密存储 |
| `SNOWFLAKE_PROD_USER` | `PROD_SVC_ACCOUNT` | Variable | ✅ | 否 | production | 生产环境专用账号 |
| `SNOWFLAKE_PROD_PASSWORD` | `（密码）` | Variable | ✅ | ✅ | production | 生产环境密码（更严格） |

> **CI 和 CD 使用不同服务账号** — 生产 CD 使用权限更高的专用账号，CI/开发使用权限受限的账号，遵循最小权限原则。

#### 第六步：Push 文件到 GitLab 仓库

```bash
git add .gitlab-ci.yml
git commit -m "feat: add GitLab CI/CD pipeline for dbt_finance"
git push origin main
```

<a id="sec-11-6"></a>

### 11.6 日常开发工作流（GitLab 版）

```
开发者修改 dbt 模型
       │
       ▼
git commit & git push → 推送 feature 分支到 GitLab
       │
       ▼
在 GitLab 上创建 Merge Request → 目标分支 main
       │
       ▼
┌─── CI 自动触发 ──────────────────────────────┐
│   dbt compile → dbt run (dev) → dbt test     │
│   全部通过 ✅ → MR 显示"Pipeline passed"      │
│   任一失败 ❌ → MR 标记为失败，阻止合并       │
└──────────────────────────────────────────────┘
       │
       ▼
Code Review → 审批通过 → 点击 "Merge"
       │
       ▼
┌─── CD 触发（需手动确认）─────────────────────┐
│   在 GitLab Pipelines 页面点击 ▶ 按钮        │
│   dbt run (prod) → dbt test (prod)           │
│   → 生产表 MART_CLIENT_TRADING_SUMMARY 更新  │
└──────────────────────────────────────────────┘
```

<a id="sec-11-7"></a>

### 11.7 GitHub Actions vs GitLab CI/CD 对照表

| 概念 | GitHub Actions | GitLab CI/CD |
|------|---------------|-------------|
| 配置文件 | `.github/workflows/ci.yml` + `cd.yml` | `.gitlab-ci.yml`（单文件） |
| 触发 MR/PR | `on: pull_request` | `rules: if: merge_request_event` |
| 触发合并 | `on: push: branches: [main]` | `rules: if: CI_COMMIT_BRANCH == "main"` |
| 路径过滤 | `paths:` | `changes:` |
| 环境变量 | `${{ secrets.XXX }}` | `$XXX`（CI/CD Variables） |
| 运行环境 | `runs-on: ubuntu-latest` | `image: python:3.11` |
| 审批门禁 | Environment 配置 Required reviewers | `when: manual` 或 Protected environment |
| 环境标记 | `environment: production` | `environment: name: production` |
| 环境变量文件覆写 | `env_var('SNOWFLAKE_ACCOUNT')` | `env_var('SNOWFLAKE_ACCOUNT')`（相同） |
