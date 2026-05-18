# dbt + Snowflake CI/CD 完整实施指南

> 以 `dbt_finance`（证券交易平台数据仓库）项目为实战案例

---

## 一、什么是 dbt CI/CD

### 1.1 概念说明

| 术语 | 全称 | 含义 |
|------|------|------|
| **CI** | Continuous Integration（持续集成） | 每次提交代码后，自动编译、运行、测试 dbt 模型，确保新代码不会破坏现有逻辑 |
| **CD** | Continuous Deployment（持续部署） | 代码合并到主分支后，自动将变更部署到生产环境，无需人工干预 |
| **Git** | 版本控制系统 | 管理 dbt 项目的所有代码变更历史，支持多人协作 |

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

## 二、案例项目：dbt_finance

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

### 2.2 项目统计

| 指标 | 数据 |
|------|------|
| 模型总数 | 9 个（5 staging + 1 intermediate + 3 marts） |
| 数据源 | 5 张表，跨 2 个 schema |
| 测试总数 | 33 个（unique / not_null / accepted_values / relationships） |
| 物化策略 | staging + intermediate → view，marts → table |

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

.github/
└── workflows/
    ├── ci.yml                                  # CI 工作流（PR 触发）
    └── cd.yml                                  # CD 工作流（合并到 main 触发）
```

---

## 三、环境分离

CI/CD 的基础是**环境分离**——开发/测试在 DEV 环境运行，不影响生产数据。

### 3.1 双环境配置

| 环境 | Schema | 用途 | 何时使用 |
|------|--------|------|----------|
| **dev** | `SCHM_F_SNOWLEARN_DEV` | 开发和 CI 测试 | 本地开发 + Pull Request 自动验证 |
| **prod** | `SCHM_F_SNOWLEARN_01` | 生产数据 | 合并到 main 后自动部署 |

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

## 四、CI 工作流详解

### 4.1 文件：`.github/workflows/ci.yml`

**触发条件：** 当对 `main` 分支发起 Pull Request，且修改了 `dbt_finance/` 目录下的文件时自动触发。

**完整文件内容及逐行注释：**

```yaml
name: dbt CI - Pull Request Validation       # 工作流名称，显示在 GitHub Actions 页面

on:
  pull_request:                               # 触发事件：Pull Request
    branches: [main]                          # 目标分支：main
    paths:
      - 'app/dbt_finance/**'                      # 只有 dbt_finance 目录有变更才触发

env:                                          # 全局环境变量，从 GitHub Secrets 注入
  SNOWFLAKE_ACCOUNT: ${{ secrets.SNOWFLAKE_ACCOUNT }}
  SNOWFLAKE_USER: ${{ secrets.SNOWFLAKE_USER }}
  SNOWFLAKE_PASSWORD: ${{ secrets.SNOWFLAKE_PASSWORD }}
  SNOWFLAKE_ROLE: ACCOUNTADMIN
  SNOWFLAKE_WAREHOUSE: COMPUTE_WH
  SNOWFLAKE_DATABASE: TEST_SNOWFLAKE_LEANING
  DBT_PROFILES_DIR: ./app/dbt_finance

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

## 五、CD 工作流详解

### 5.1 文件：`.github/workflows/cd.yml`

**触发条件：** 当代码被合并（push）到 `main` 分支，且修改了 `dbt_finance/` 目录下的文件时自动触发。

**完整文件内容及逐行注释：**

```yaml
name: dbt CD - Deploy to Production          # 工作流名称

on:
  push:                                        # 触发事件：push（包括 PR 合并）
    branches: [main]                           # 目标分支：main
    paths:
      - 'app/dbt_finance/**'                       # 只有 dbt_finance 目录有变更才触发

env:
  SNOWFLAKE_ACCOUNT: ${{ secrets.SNOWFLAKE_ACCOUNT }}
  SNOWFLAKE_USER: ${{ secrets.SNOWFLAKE_USER }}
  SNOWFLAKE_PASSWORD: ${{ secrets.SNOWFLAKE_PASSWORD }}
  SNOWFLAKE_ROLE: ACCOUNTADMIN
  SNOWFLAKE_WAREHOUSE: COMPUTE_WH
  SNOWFLAKE_DATABASE: TEST_SNOWFLAKE_LEANING
  DBT_PROFILES_DIR: ./app/dbt_finance
 
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

### 5.2 CI 与 CD 的关键区别

| 对比项 | CI（ci.yml） | CD（cd.yml） |
|--------|-------------|-------------|
| 触发事件 | `pull_request` | `push`（合并到 main） |
| 目标环境 | dev（`SCHM_F_SNOWLEARN_DEV`） | prod（`SCHM_F_SNOWLEARN_01`） |
| 是否执行 compile | 是 | 否（CI 已验证过） |
| environment 保护 | 无 | `production`（可设人工审批） |
| 失败影响 | 阻止 PR 合并 | 需手动回滚或重新部署 |

---

## 六、定时调度（Snowflake TASK）

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

## 七、GitHub 端配置步骤

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

### 7.2 第二步：创建 Production 环境（推荐）

路径：GitHub 仓库 → **Settings** → **Environments** → **New environment**

1. 环境名输入：`production`
2. 勾选 **Required reviewers**，添加至少 1 位审批人
3. 效果：CD 部署前会暂停，等待审批人点击"Approve"后才继续

### 7.3 第三步：配置分支保护规则（推荐）

路径：GitHub 仓库 → **Settings** → **Branches** → **Add branch protection rule**

1. Branch name pattern：`main`
2. 勾选：
   - ✅ **Require a pull request before merging**（必须通过 PR 合并）
   - ✅ **Require status checks to pass before merging**（CI 必须通过）
   - ✅ **Require approvals**（至少 1 人审核）
3. 效果：没有人能直接 push 到 main，所有变更必须走 PR → CI → Review → 合并

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

## 九、故障排查

### 9.1 CI 失败常见原因

| 错误 | 原因 | 解决方式 |
|------|------|---------|
| `Compilation Error` | SQL 语法错误或 ref/source 引用不存在 | 检查模型 SQL，确认引用的模型名拼写正确 |
| `Database Error` | Snowflake 连接失败 | 检查 GitHub Secrets 是否正确配置 |
| `Test Failure` | 数据质量测试未通过 | 查看失败的测试，修复数据或调整测试规则 |
| `Permission Error` | 角色权限不足 | 确认服务账号有目标 schema 的 CREATE 权限 |

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

## 十、配置清单总览

### Snowflake 侧

| 配置项 | 状态 | 说明 |
|--------|------|------|
| DEV Schema | ✅ 已创建 | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV` |
| PROD Schema | ✅ 已存在 | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01` |
| profiles.yml | ✅ 已更新 | dev/prod 双环境配置 |
| schedules.sql | ✅ 已创建 | Snowflake TASK 每日 6:00 定时调度 |

### GitHub 侧

| 配置项 | 状态 | 操作 |
|--------|------|------|
| ci.yml | ✅ 已创建 | `.github/workflows/ci.yml` |
| cd.yml | ✅ 已创建 | `.github/workflows/cd.yml` |
| Secrets | ⏳ 需手动配置 | 添加 SNOWFLAKE_ACCOUNT / USER / PASSWORD |
| production 环境 | ⏳ 推荐配置 | 添加部署审批规则 |
| 分支保护规则 | ⏳ 推荐配置 | main 分支要求 PR + CI 通过 + Review |
