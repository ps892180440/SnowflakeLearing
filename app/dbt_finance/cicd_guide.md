# dbt_finance CI/CD 配置指南

## 一、整体架构

```
开发者在 Workspace 修改 dbt 模型
       │
       ▼
  git commit & push → feature 分支
       │
       ▼
  创建 Pull Request → main 分支
       │
       ▼
  ┌─── CI 自动触发 (.github/workflows/ci.yml) ───┐
  │  1. dbt compile  → 语法检查                    │
  │  2. dbt run      → 在 DEV schema 执行          │
  │  3. dbt test     → 数据质量验证                 │
  │  通过 ✅ → 允许合并   失败 ❌ → 阻止合并        │
  └───────────────────────────────────────────────┘
       │ PR 审核通过，合并到 main
       ▼
  ┌─── CD 自动触发 (.github/workflows/cd.yml) ───┐
  │  1. dbt run      → 在 PROD schema 执行         │
  │  2. dbt test     → 生产数据质量验证             │
  └───────────────────────────────────────────────┘
       │
       ▼
  Snowflake TASK 每日定时调度（可选）
```

## 二、环境分离

| 环境 | Schema | 用途 | 触发条件 |
|------|--------|------|----------|
| **dev** | `SCHM_F_SNOWLEARN_DEV` | CI 验证，PR 测试 | Pull Request 创建/更新 |
| **prod** | `SCHM_F_SNOWLEARN_01` | 生产数据 | PR 合并到 main |

## 三、GitHub 端配置步骤

### 3.1 添加 GitHub Secrets

在 GitHub 仓库 → Settings → Secrets and variables → Actions 中添加：

| Secret 名 | 值 | 说明 |
|-----------|-----|------|
| `SNOWFLAKE_ACCOUNT` | `ogb30853` | 你的 Snowflake 账户标识 |
| `SNOWFLAKE_USER` | `PENGB` | 你的 Snowflake 用户名 |
| `SNOWFLAKE_PASSWORD` | `你的密码` | Snowflake 登录密码 |

> **安全建议：** 生产环境建议使用专用的服务账号（Service Account）+ 密钥对认证，而非个人账号密码。

### 3.2 创建 Production 环境（可选但推荐）

在 GitHub 仓库 → Settings → Environments 中：
1. 创建名为 `production` 的环境
2. 添加保护规则：Required reviewers（至少 1 人审批部署）
3. 这样 CD 部署前会等待人工审批

### 3.3 确认文件已提交

确保以下文件已 push 到 Git 仓库：

```
.github/
└── workflows/
    ├── ci.yml          # CI 工作流（PR 触发）
    └── cd.yml          # CD 工作流（合并触发）

dbt_finance/
├── dbt_project.yml
├── profiles.yml        # 已更新为 dev/prod 双环境
├── schedules.sql       # Snowflake TASK 定时调度（可选）
└── models/
    └── ...
```

## 四、CI 工作流详解 (.github/workflows/ci.yml)

**触发条件：** 当对 `main` 分支发起 Pull Request，且修改了 `dbt_finance/` 目录下的文件时触发。

**执行步骤：**

| 步骤 | 命令 | 说明 |
|------|------|------|
| 1 | `pip install dbt-snowflake` | 安装 dbt |
| 2 | 覆写 `profiles.yml` | 注入 CI 环境的 Snowflake 连接信息 |
| 3 | `dbt deps` | 安装 dbt 依赖包 |
| 4 | `dbt compile` | 编译检查语法 |
| 5 | `dbt run --target dev` | 在 **DEV** schema 执行所有模型 |
| 6 | `dbt test --target dev` | 执行所有数据测试 |

**任何步骤失败 → PR 会被标记为检查未通过 → 无法合并。**

## 五、CD 工作流详解 (.github/workflows/cd.yml)

**触发条件：** 当代码被合并到 `main` 分支（push 事件），且修改了 `dbt_finance/` 目录下的文件时触发。

**执行步骤：**

| 步骤 | 命令 | 说明 |
|------|------|------|
| 1 | `pip install dbt-snowflake` | 安装 dbt |
| 2 | 覆写 `profiles.yml` | 注入 PROD 环境的 Snowflake 连接信息 |
| 3 | `dbt deps` | 安装 dbt 依赖包 |
| 4 | `dbt run --target prod` | 在 **PROD** schema 执行所有模型 |
| 5 | `dbt test --target prod` | 执行生产数据测试 |

## 六、日常开发工作流

```
1. 在 Workspace 中创建/修改 dbt 模型
2. 本地验证：dbt compile → dbt run → dbt test
3. git add → git commit → git push (到 feature 分支)
4. 在 GitHub 上创建 Pull Request
5. CI 自动运行，等待检查通过 ✅
6. 团队成员 Code Review
7. 审核通过后合并到 main
8. CD 自动部署到生产环境
```

## 七、定时调度（可选）

如果希望生产环境每天定时重新运行 dbt 模型，可使用 `schedules.sql` 中的 Snowflake TASK：

```sql
-- 先部署 dbt 项目到 Snowflake
CREATE DBT PROJECT TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DBT_FINANCE
  FROM 'snow://workspace/USER$.PUBLIC."SnowflakeLearing"/versions/live/dbt_finance';

-- 然后创建定时任务（每天早上 6 点执行）
CREATE TASK ... SCHEDULE = 'USING CRON 0 6 * * * Asia/Shanghai'
AS EXECUTE DBT PROJECT ... ARGS = 'run';
```

详见 `dbt_finance/schedules.sql`。

## 八、Snowflake 侧已完成的配置

| 配置项 | 状态 | 说明 |
|--------|------|------|
| DEV Schema | ✅ 已创建 | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV` |
| PROD Schema | ✅ 已存在 | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01` |
| profiles.yml | ✅ 已更新 | dev/prod 双环境配置 |
| CI 工作流 | ✅ 已创建 | `.github/workflows/ci.yml` |
| CD 工作流 | ✅ 已创建 | `.github/workflows/cd.yml` |
| 定时调度 SQL | ✅ 已创建 | `dbt_finance/schedules.sql` |

## 九、你还需要做的

1. **在 GitHub 仓库中添加 3 个 Secrets**（见 3.1 节）
2. **将本工作空间的文件 push 到 Git 仓库**
3. **（推荐）创建 production 环境保护规则**（见 3.2 节）
4. **（可选）配置分支保护规则**：Settings → Branches → 要求 PR 通过 CI 检查才能合并
