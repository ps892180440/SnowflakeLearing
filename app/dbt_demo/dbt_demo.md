# dbt Demo 学习文档

## 一、基础概念

### 1.1 什么是 dbt

dbt（Data Build Tool）是一个数据转换工具，让数据分析师和工程师通过编写 SQL SELECT 语句来转换数据仓库中的数据。

核心特点：
- **只做 T（Transform）**：在 ELT 流程中负责转换层
- **SQL 优先**：用 SELECT 语句定义模型，dbt 自动处理 DDL/DML
- **内置版本控制、测试、文档功能**

### 1.2 什么是 ELT

ELT 是 **Extract, Load, Transform** 的缩写，是一种数据集成流程：

| 步骤 | 说明 |
|------|------|
| **Extract（提取）** | 从源系统（数据库、API、文件等）抽取原始数据 |
| **Load（加载）** | 将原始数据直接加载到目标数据仓库（如 Snowflake） |
| **Transform（转换）** | 在数据仓库内部对数据进行清洗、建模和转换 |

与传统 ETL 的区别：ETL 在加载前转换数据，ELT 先加载原始数据再利用仓库算力就地转换。dbt 负责的就是其中 **T（Transform）** 这一步。

---

## 二、项目总览

### 2.1 项目目标

基于源表 `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DIM_ACCOUNT`（账户维度表），通过 dbt 的分层架构，构建出一张「活跃账户 + 成熟度分类」的业务分析表。

### 2.2 数据流向

```
源表 DIM_ACCOUNT
      │
      ▼
Staging 层：stg_dim_account（view）
  → 从源表透传所有字段，作为清洗中转层
      │
      ▼
Marts 层：mart_active_accounts（table）
  → 筛选活跃账户、计算账龄、划分成熟度等级
```

### 2.3 分层设计理念

```
Source（源）     →  原始数据，不做任何修改
  ↓
Staging（暂存）  →  轻量清洗/字段选择，解耦源表（改源表只需改这一层）
  ↓
Marts（集市）    →  面向业务的加工逻辑，供分析师直接查询使用
```

这是 dbt 推荐的经典三层架构，核心好处是**关注点分离**——源表变了只改 staging，业务逻辑变了只改 marts。

### 2.4 源表结构

源表 `DIM_ACCOUNT` 包含以下字段：

| 字段名 | 类型 | 说明 |
|--------|------|------|
| ACCOUNT_KEY | NUMBER(38,0) | 自增代理键（IDENTITY） |
| ACCOUNT_ID | VARCHAR(30) | 业务账户标识 |
| CLIENT_NAME | VARCHAR(200) | 客户名称 |
| RISK_LEVEL | VARCHAR(20) | 风险等级（如 Low, Medium, High） |
| ACCOUNT_TYPE | VARCHAR(50) | 账户类型 |
| OPEN_DATE | DATE | 开户日期 |
| IS_ACTIVE | BOOLEAN | 是否活跃（默认 TRUE） |
| ETL_UPDATED_AT | TIMESTAMP_NTZ(9) | ETL 最后更新时间戳 |

---

## 三、项目文件结构

```
dbt_demo/
├── dbt_project.yml               # 项目总配置文件
├── profiles.yml                   # Snowflake 连接配置文件
├── models/
│   ├── sources.yml                # 数据源声明文件
│   ├── schema.yml                 # 数据测试定义文件
│   ├── staging/
│   │   └── stg_dim_account.sql    # Staging 模型（物化为 view）
│   └── marts/
│       └── mart_active_accounts.sql  # 业务模型（物化为 table）
└── logs/
    └── dbt.log                    # dbt 运行日志（自动生成）
```

---

## 四、配置文件详解

### 4.1 dbt_project.yml — 项目总配置

**作用：** 定义项目名称、版本、文件路径，以及各层模型的默认物化方式。

```yaml
name: 'dbt_demo'
version: '1.0.0'
profile: 'dbt_demo'

model-paths: ["models"]
test-paths: ["tests"]
seed-paths: ["seeds"]

models:
  dbt_demo:
    staging:
      +materialized: view        # staging 层的模型默认物化为视图（view）
    marts:
      +materialized: table       # marts 层的模型默认物化为物理表（table）
```

**关键配置说明：**

| 配置项 | 说明 |
|--------|------|
| `name` | 项目名称，必须与 profiles.yml 中的 profile 名一致 |
| `profile` | 指向 profiles.yml 中的连接配置 |
| `model-paths` | 模型 SQL 文件的存放目录 |
| `+materialized: view` | staging 层用视图，不占存储，每次查询时实时计算 |
| `+materialized: table` | marts 层用物理表，数据持久化存储，查询性能更好 |

### 4.2 profiles.yml — Snowflake 连接配置

**作用：** 告诉 dbt 如何连接 Snowflake，包括数据库、仓库、角色等信息。

```yaml
dbt_demo:
  target: dev
  outputs:
    dev:
      type: snowflake
      account: ""                # 留空，Workspace 中使用当前会话身份
      user: ""                   # 留空，Workspace 中使用当前会话身份
      role: "ACCOUNTADMIN"       # 使用的角色
      database: "TEST_SNOWFLAKE_LEANING"   # 目标数据库
      warehouse: "COMPUTE_WH"   # 计算仓库
      schema: "SCHM_F_SNOWLEARN_01"        # 目标 schema
      threads: 4                 # 并行线程数
```

**注意事项：**
- 在 Snowflake Workspace 中运行 dbt 时，`account` 和 `user` 留空即可，会自动使用当前登录会话
- **不要**使用 `env_var()`、`password`、`authenticator` 等字段

---

## 五、模型文件详解

### 5.1 sources.yml — 数据源声明

**作用：** 声明原始数据源的位置和元数据。告诉 dbt "我的原始数据在哪里"，并为每个字段添加文档描述。声明后，模型中可通过 `{{ source('snowlearn', 'DIM_ACCOUNT') }}` 引用源表。

```yaml
version: 2

sources:
  - name: snowlearn
    database: TEST_SNOWFLAKE_LEANING
    schema: SCHM_F_SNOWLEARN_01
    description: "Snowflake learning demo source data"
    tables:
      - name: DIM_ACCOUNT
        description: "Account dimension table with client and risk info"
        columns:
          - name: ACCOUNT_KEY
            description: "Auto-increment surrogate key"
          - name: ACCOUNT_ID
            description: "Business account identifier"
          - name: CLIENT_NAME
            description: "Client name"
          - name: RISK_LEVEL
            description: "Risk classification (e.g. Low, Medium, High)"
          - name: ACCOUNT_TYPE
            description: "Type of account"
          - name: OPEN_DATE
            description: "Date when the account was opened"
          - name: IS_ACTIVE
            description: "Whether the account is currently active"
          - name: ETL_UPDATED_AT
            description: "ETL last update timestamp"
```

### 5.2 stg_dim_account.sql — Staging 模型

**所在目录：** `models/staging/`
**物化方式：** view（视图）
**在 Snowflake 中生成的对象：** `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_DIM_ACCOUNT`（视图）

**SQL 作用说明：** 这是一个 Staging（暂存）层模型。它从源表 `DIM_ACCOUNT` 中选取所有需要的字段，原样透传到 staging 层，不做任何业务逻辑处理。其目的是作为源表与业务层之间的"隔离层"——当源表结构发生变化时，只需要修改这一个文件，下游所有 marts 模型不受影响。

```sql
select
    account_key,
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    is_active,
    etl_updated_at
from {{ source('snowlearn', 'DIM_ACCOUNT') }}
```

**语法说明：**
- `{{ source('snowlearn', 'DIM_ACCOUNT') }}` 是 dbt 的 Jinja 语法，引用 `sources.yml` 中声明的源表，dbt 会自动解析为完整的表名 `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DIM_ACCOUNT`

### 5.3 mart_active_accounts.sql — 业务模型

**所在目录：** `models/marts/`
**物化方式：** table（物理表）
**在 Snowflake 中生成的对象：** `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.MART_ACTIVE_ACCOUNTS`（表）

**SQL 作用说明：** 这是一个 Marts（业务集市）层模型，包含三个核心业务逻辑：
1. **筛选活跃账户**：通过 `where is_active = true` 只保留当前活跃的账户
2. **计算账户年龄**：通过 `datediff` 函数计算从开户日期到今天的天数，生成 `account_age_days` 字段
3. **划分成熟度等级**：根据账龄将账户分为三个等级——`Mature`（成熟，>365天）、`Established`（稳定，90-365天）、`New`（新户，≤90天）

```sql
select
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    datediff('day', open_date, current_date()) as account_age_days,
    case
        when datediff('day', open_date, current_date()) > 365 then 'Mature'
        when datediff('day', open_date, current_date()) > 90 then 'Established'
        else 'New'
    end as account_maturity
from {{ ref('stg_dim_account') }}
where is_active = true
```

**语法说明：**
- `{{ ref('stg_dim_account') }}` 是 dbt 的模型引用语法，引用上游的 staging 模型。dbt 会自动处理依赖顺序，确保 `stg_dim_account` 先于本模型执行
- `datediff('day', open_date, current_date())` 计算两个日期之间的天数差
- `case...when...end` 根据账龄条件判断，输出对应的成熟度标签

### 5.4 schema.yml — 数据测试定义

**作用：** 为模型字段定义数据质量测试。运行 `dbt test` 时，dbt 会自动检查这些规则是否满足。

```yaml
version: 2

models:
  - name: stg_dim_account
    description: "Staging model for DIM_ACCOUNT - light cleanup pass"
    columns:
      - name: account_key
        tests:
          - unique           # 测试：account_key 值必须唯一
          - not_null         # 测试：account_key 不能为空
      - name: account_id
        tests:
          - not_null         # 测试：account_id 不能为空

  - name: mart_active_accounts
    description: "Active accounts with maturity classification"
    columns:
      - name: account_id
        tests:
          - not_null         # 测试：account_id 不能为空
      - name: account_maturity
        tests:
          - accepted_values:                          # 测试：值只能是以下三个之一
              values: ['New', 'Established', 'Mature']
```

**测试类型说明：**

| 测试类型 | 说明 |
|----------|------|
| `unique` | 确保该字段的值在整个表中唯一，没有重复 |
| `not_null` | 确保该字段不包含 NULL 值 |
| `accepted_values` | 确保该字段的值只出现在指定的列表中 |

---

## 六、执行命令与结果

### 6.1 编译验证

```bash
dbt compile --project-dir /app/dbt_demo
```

**作用：** 检查项目语法是否正确，将 Jinja 模板编译为可执行的 SQL，但不实际运行。
**结果：** Found 2 models, 6 data tests, 1 source — 编译通过。

### 6.2 运行模型

```bash
dbt run --project-dir /app/dbt_demo
```

**作用：** 实际执行所有模型，在 Snowflake 中创建对应的视图和表。
**结果：**
- `stg_dim_account` → 创建视图 SUCCESS
- `mart_active_accounts` → 创建表 SUCCESS
- 总计：PASS=2, ERROR=0

### 6.3 运行测试

```bash
dbt test --project-dir /app/dbt_demo
```

**作用：** 执行 `schema.yml` 中定义的所有数据质量测试。
**结果：** PASS=5, ERROR=0 — 全部通过。

---

## 七、生成的 Snowflake 对象

| 对象名 | 类型 | 完整路径 |
|--------|------|----------|
| STG_DIM_ACCOUNT | 视图（VIEW） | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_DIM_ACCOUNT` |
| MART_ACTIVE_ACCOUNTS | 表（TABLE） | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.MART_ACTIVE_ACCOUNTS` |

---

## 八、核心概念速查

| dbt 概念 | 本项目中的体现 |
|----------|--------------|
| **Source（源）** | 用 `sources.yml` 声明 `DIM_ACCOUNT` 为数据源 |
| **Staging 模型** | `stg_dim_account` — 轻量透传层，物化为 view |
| **Marts 模型** | `mart_active_accounts` — 业务逻辑层，物化为 table |
| **ref() 引用** | Marts 通过 `{{ ref('stg_dim_account') }}` 引用 staging，dbt 自动管理依赖顺序 |
| **source() 引用** | Staging 通过 `{{ source('snowlearn', 'DIM_ACCOUNT') }}` 引用源表 |
| **测试** | 包含 `unique`、`not_null`、`accepted_values` 三种数据质量测试 |
| **物化策略** | staging 用 view（轻量、实时），marts 用 table（持久化、高性能） |
