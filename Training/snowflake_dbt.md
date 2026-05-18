# Snowflake dbt 全面知识指南

---

## 目录

- [1. dbt 概述](#1-dbt-概述)
  - [1.1 什么是 dbt](#11-什么是-dbt)
  - [1.2 dbt 的核心价值](#12-dbt-的核心价值)
  - [1.3 dbt 与 Snowflake 的关系](#13-dbt-与-snowflake-的关系)
  - [1.4 dbt Core vs dbt Cloud](#14-dbt-core-vs-dbt-cloud)
- [2. 环境搭建与安装](#2-环境搭建与安装)
  - [2.1 安装 dbt-snowflake](#21-安装-dbt-snowflake)
  - [2.2 配置 profiles.yml](#22-配置-profilesyml)
  - [2.3 初始化项目](#23-初始化项目)
- [3. 项目结构](#3-项目结构)
  - [3.1 标准目录结构](#31-标准目录结构)
  - [3.2 dbt_project.yml 详解](#32-dbt_projectyml-详解)
  - [3.3 关键目录说明](#33-关键目录说明)
- [4. Models（模型）](#4-models模型)
  - [4.1 模型概述](#41-模型概述)
  - [4.2 SQL 模型语法](#42-sql-模型语法)
  - [4.3 Python 模型语法](#43-python-模型语法)
  - [4.4 模型命名规范](#44-模型命名规范)
  - [4.5 模型层级设计（Staging → Marts）](#45-模型层级设计staging--marts)
- [5. Materializations（物化策略）](#5-materializations物化策略)
  - [5.1 View](#51-view)
  - [5.2 Table](#52-table)
  - [5.3 Incremental](#53-incremental)
  - [5.4 Ephemeral](#54-ephemeral)
  - [5.5 Materialized View（Snowflake 专属）](#55-materialized-viewsnowflake-专属)
  - [5.6 各策略对比与选择](#56-各策略对比与选择)
- [6. Sources（数据源）](#6-sources数据源)
  - [6.1 定义 Sources](#61-定义-sources)
  - [6.2 引用 Sources](#62-引用-sources)
  - [6.3 Source Freshness（数据新鲜度）](#63-source-freshness数据新鲜度)
- [7. Seeds（种子数据）](#7-seeds种子数据)
- [8. Tests（测试）](#8-tests测试)
  - [8.1 通用测试（Generic Tests）](#81-通用测试generic-tests)
  - [8.2 单一测试（Singular Tests）](#82-单一测试singular-tests)
  - [8.3 自定义通用测试](#83-自定义通用测试)
  - [8.4 测试配置与运行](#84-测试配置与运行)
- [9. Jinja & Macros（宏）](#9-jinja--macros宏)
  - [9.1 Jinja 模板语法基础](#91-jinja-模板语法基础)
  - [9.2 dbt 内置 Jinja 函数](#92-dbt-内置-jinja-函数)
  - [9.3 自定义 Macros](#93-自定义-macros)
  - [9.4 常用 Macros 模式](#94-常用-macros-模式)
- [10. Snapshots（快照/缓慢变化维度）](#10-snapshots快照缓慢变化维度)
  - [10.1 SCD Type 2 实现](#101-scd-type-2-实现)
  - [10.2 Snapshot 策略](#102-snapshot-策略)
- [11. Variables（变量）与环境](#11-variables变量与环境)
- [12. Hooks 与 Operations](#12-hooks-与-operations)
- [13. Analyses 与 Exposures](#13-analyses-与-exposures)
- [14. Packages（包管理）](#14-packages包管理)
- [15. Documentation（文档生成）](#15-documentation文档生成)
- [16. dbt 命令行参考](#16-dbt-命令行参考)
- [17. Snowflake 专属特性与最佳实践](#17-snowflake-专属特性与最佳实践)
  - [17.1 Snowflake 适配的物化策略](#171-snowflake-适配的物化策略)
  - [17.2  Warehouse 管理](#172-warehouse-管理)
  - [17.3 权限与 RBAC](#173-权限与-rbac)
  - [17.4 零拷贝克隆（Zero-Copy Cloning）](#174-零拷贝克隆zero-copy-cloning)
  - [17.5 动态表（Dynamic Tables）](#175-动态表dynamic-tables)
- [18. 示例一：简单入门案例](#18-示例一简单入门案例)
  - [18.1 项目需求](#181-项目需求)
  - [18.2 项目结构](#182-项目结构)
  - [18.3 配置文件](#183-配置文件)
  - [18.4 模型代码](#184-模型代码)
  - [18.5 测试代码](#185-测试代码)
  - [18.6 运行与验证](#186-运行与验证)
  - [18.7 生成文档](#187-生成文档)
- [19. 示例二：企业级电商数据平台](#19-示例二企业级电商数据平台)
  - [19.1 业务背景与需求](#191-业务背景与需求)
  - [19.2 项目结构](#192-项目结构)
  - [19.3 配置文件详解](#193-配置文件详解)
  - [19.4 Sources 定义](#194-sources-定义)
  - [19.5 Staging 层模型](#195-staging-层模型)
  - [19.6 Intermediate 层模型](#196-intermediate-层模型)
  - [19.7 Marts 层模型（核心业务表）](#197-marts-层模型核心业务表)
  - [19.8 自定义 Macros](#198-自定义-macros)
  - [19.9 增量策略模型](#199-增量策略模型)
  - [19.10 Snapshots 缓慢变化维度](#1910-snapshots-缓慢变化维度)
  - [19.11 测试体系](#1911-测试体系)
  - [19.12 CI/CD 与生产部署](#1912-cicd-与生产部署)
  - [19.13 文档与数据血缘](#1913-文档与数据血缘)

---

## 1. dbt 概述

### 1.1 什么是 dbt

**dbt (data build tool)** 是一个基于命令行的数据转换工具，它使数据分析师和工程师能够通过软件工程的最佳实践来转换数据库中的数据。dbt 的核心思想是 **"ELT 中的 T"**——即专注于数据仓库中的 **Transform（转换）** 环节。

dbt 的工作方式：
- 你编写 **SELECT 语句**（模型），dbt 负责将它们物化为数据库中的视图或表
- dbt 管理模型之间的**依赖关系**，并按照正确的顺序执行它们
- dbt 支持**测试**、**文档**、**版本控制**和**CI/CD** 集成

### 1.2 dbt 的核心价值

| 能力 | 描述 |
|------|------|
| **可维护性** | 使用版本控制（Git）管理所有数据转换逻辑 |
| **可测试性** | 内建测试框架：唯一性、非空、引用完整性、自定义测试 |
| **文档化** | 自动生成数据血缘图和列级文档 |
| **可复用性** | 通过 Macros（宏）消除重复 SQL |
| **依赖管理** | 自动解析模型依赖 DAG，按序执行 |
| **环境管理** | 通过变量和目标实现 dev/staging/prod 环境分离 |

### 1.3 dbt 与 Snowflake 的关系

Snowflake 是 dbt 的**第一类适配器**（first-class adapter），dbt-snowflake 包提供了对 Snowflake 专属特性的深度支持：

- **零拷贝克隆（Zero-Copy Cloning）**：`dbt run` 可自动使用克隆创建开发环境
- **物化视图**：支持 Snowflake 的 Materialized View
- **动态表**：支持 Dynamic Tables
- **Warehouse 管理**：可为不同模型指定不同的 Warehouse
- **Query Tag**：自动为 dbt 执行的查询打标签，便于监控和成本追踪
- **信息架构**：与 Snowflake 的 ACCESS_HISTORY 和 QUERY_HISTORY 集成

### 1.4 dbt Core vs dbt Cloud

| 特性 | dbt Core | dbt Cloud |
|------|----------|-----------|
| **性质** | 开源 CLI 工具 | SaaS 平台 |
| **费用** | 免费 | 免费层 + 付费计划 |
| **编辑器** | 任意编辑器 | Web IDE |
| **调度** | 需自行配置（Airflow 等） | 内建调度器 |
| **CI/CD** | 需自行集成 GitHub Actions 等 | 内建 CI |
| **API** | 无 | 丰富的管理 API |
| **适用场景** | 技术团队，需要灵活定制 | 团队需要开箱即用方案 |

---

## 2. 环境搭建与安装

### 2.1 安装 dbt-snowflake

```bash
# 使用 pip 安装
pip install dbt-snowflake

# 验证安装
dbt --version

# 安装特定版本
pip install dbt-snowflake==1.7.0
```

### 2.2 配置 profiles.yml

`profiles.yml` 是 dbt 的**数据库连接配置文件**，默认位置：
- **Linux/Mac**：`~/.dbt/profiles.yml`
- **Windows**：`%USERPROFILE%\.dbt\profiles.yml`

```yaml
# ~/.dbt/profiles.yml

dbt_ecommerce:                          # 项目名称（对应 dbt_project.yml 中的 profile）
  target: dev                           # 默认目标环境
  outputs:
    dev:                                # 开发环境
      type: snowflake
      account: "ab12345.us-east-1"      # Snowflake 账号标识符
      user: "dbt_dev_user"
      password: "your_password"
      role: "dbt_dev_role"
      warehouse: "dbt_dev_wh"
      database: "ecommerce_dev"
      schema: "dbt_dev"
      threads: 8                        # 并行线程数
      client_session_keep_alive: true   # 保持连接活跃（可选）
      query_tag: "dbt_dev"              # 查询标签，便于追踪

    staging:                             # 预发布环境
      type: snowflake
      account: "ab12345.us-east-1"
      user: "dbt_staging_user"
      password: "your_password"
      role: "dbt_staging_role"
      warehouse: "dbt_staging_wh"
      database: "ecommerce_staging"
      schema: "dbt_staging"
      threads: 4
      query_tag: "dbt_staging"

    prod:                                # 生产环境
      type: snowflake
      account: "ab12345.us-east-1"
      user: "dbt_prod_user"
      private_key_path: "/path/to/rsa_key.p8"  # 推荐：使用 key-pair 认证
      role: "dbt_prod_role"
      warehouse: "dbt_prod_wh"
      database: "ecommerce_prod"
      schema: "dbt_prod"
      threads: 16
      query_tag: "dbt_prod"

  # 可选：使用环境变量替代（生产环境推荐）
  # user: "{{ env_var('DBT_USER') }}"
  # password: "{{ env_var('DBT_PASSWORD') }}"
```

**认证方式对比**：

| 方式 | 配置字段 | 安全级别 | 推荐场景 |
|------|----------|---------|---------|
| 用户名/密码 | `user` + `password` | 低 | 本地开发 |
| Key-Pair | `private_key_path` + `private_key_passphrase` | 高 | 生产环境 CI/CD |
| SSO (Browser) | `authenticator: externalbrowser` | 中 | 交互式本地开发 |
| OAuth | `authenticator: oauth` + `token` | 高 | dbt Cloud 集成 |

### 2.3 初始化项目

```bash
# 初始化一个新的 dbt 项目
dbt init ecommerce_analytics

# 进入项目目录
cd ecommerce_analytics

# 验证数据库连接
dbt debug

# 调试连接（输出更详细信息）
dbt debug --config-dir
```

---

## 3. 项目结构

### 3.1 标准目录结构

```
my_dbt_project/
├── dbt_project.yml              # 项目主配置文件（必需）
├── packages.yml                 # 依赖包声明
├── package-lock.yml             # 依赖锁定文件（自动生成）
├── profiles.yml                 # （可选）数据库连接配置
├── .gitignore                   # Git 忽略规则
├── .user.yml                    # 用户级覆盖配置（可选）
│
├── models/                      # 核心：所有数据模型
│   ├── staging/                 # Staging 层：原始数据清洗重命名
│   │   ├── sources.yml          # 数据源定义
│   │   ├── stg_customers.sql
│   │   ├── stg_orders.sql
│   │   └── stg_products.sql
│   ├── intermediate/            # 中间层：跨源聚合与业务逻辑
│   │   ├── int_order_items.sql
│   │   └── int_customer_orders.sql
│   └── marts/                   # 集市层：面向业务的数据产品
│       ├── finance/
│       │   ├── fct_revenue.sql
│       │   └── dim_customers.sql
│       └── marketing/
│           └── fct_campaign_performance.sql
│
├── snapshots/                   # 缓慢变化维度（SCD）
│   └── products_snapshot.sql
│
├── macros/                      # 可复用的 Jinja 宏
│   ├── generate_schema_name.sql
│   ├── custom_tests/
│   │   └── test_not_null_multiple.sql
│   └── utils.sql
│
├── seeds/                       # 静态 CSV 参考数据
│   ├── country_codes.csv
│   └── product_categories.csv
│
├── tests/                       # 单一测试 SQL
│   ├── assert_total_orders_positive.sql
│   └── assert_no_duplicate_customers.sql
│
├── analyses/                    # 一次性分析查询（不物化）
│   └── churn_analysis.sql
│
├── data/                        # 数据导出（可选）
│
└── logs/                        # dbt 运行日志（自动生成）
    └── dbt.log
```

### 3.2 dbt_project.yml 详解

```yaml
# dbt_project.yml

# 项目名称：必须与 profiles.yml 中的配置项名称一致
name: 'ecommerce_analytics'
version: '1.0.0'
config-version: 2

# 使用的 dbt profile（必须匹配 profiles.yml 中的顶层 key）
profile: 'dbt_ecommerce'

# 文档中的模型路径前缀（项目名作为 namespace）
require-dbt-version: ">=1.6.0"

# 模型路径：告诉 dbt 从哪些目录查找 SQL/Python 模型
model-paths: ["models"]
analysis-paths: ["analyses"]
test-paths: ["tests"]
seed-paths: ["seeds"]
macro-paths: ["macros"]
snapshot-paths: ["snapshots"]

# dbt 编译前清理的目标目录
clean-targets:
  - "target"
  - "dbt_packages"
  - "logs"

# 查询注释：在执行时附加元数据（可选，可以在 Snowflake QUERY_HISTORY 中看到）
query-comment:
  comment: "{{ query_comment(node) }}"
  append: true  # 追加到已有注释后面

# 模型全局配置（可被模型级别的 config() 覆盖）
models:
  ecommerce_analytics:            # 项目名作为命名空间
    staging:                      # 目录路径段
      +materialized: view         # + 前缀表示应用于此路径下所有模型
      +schema: staging
      +tags: ["staging"]

    intermediate:
      +materialized: table
      +schema: intermediate
      +tags: ["intermediate"]

    marts:
      +materialized: table
      +schema: marts
      +tags: ["marts", "production"]
      finance:
        +schema: marts_finance
      marketing:
        +schema: marts_marketing

# Seeds 配置
seeds:
  ecommerce_analytics:
    +quote_columns: false         # 不使用引号包裹列名
    +schema: reference

# Snapshots 配置
snapshots:
  ecommerce_analytics:
    +target_schema: snapshots
    +strategy: timestamp
    +updated_at: updated_at

# 测试配置
tests:
  ecommerce_analytics:
    +store_failures: true         # 将失败的测试记录存储为表
    +severity: warn               # 失败严重级别：warn / error

# 变量：全局变量，可在模型中通过 {{ var('name') }} 访问
vars:
  start_date: '2023-01-01'
  business_days_only: true
  max_days_lookback: 90

# 日志级别
log-path: logs

# 发送匿名使用数据（可关闭）
send_anonymous_usage_stats: false

# 模型限制：确保不意外物化过多模型
# fail_if_models_exceed: 500
```

### 3.3 关键目录说明

| 目录 | 用途 | 是否物化到 DB | 说明 |
|------|------|-------------|------|
| `models/` | 核心数据模型 | 是 | SELECT 语句，dbt 负责将其编译为 CREATE VIEW/TABLE |
| `snapshots/` | SCD Type 2 | 是 | 追踪维度表随时间的变化 |
| `seeds/` | 参考数据 | 是 | CSV 文件加载到数据库中 |
| `macros/` | Jinja 宏 | 否 | 可复用的模板函数 |
| `tests/` | 自定义测试 | 否 | SQL 查询，返回 0 行即通过 |
| `analyses/` | 分析查询 | 否 | 编译但不物化，用于临时分析 |

---

## 4. Models（模型）

### 4.1 模型概述

**模型（Model）** 是 dbt 的核心概念。一个模型就是一个 **SELECT 语句**，dbt 根据配置将其物化为数据库中的视图（View）或表（Table）。

每个 `.sql` 文件代表一个模型，**文件名 = 视图/表名称**。

### 4.2 SQL 模型语法

#### 基础模型

```sql
-- models/staging/stg_customers.sql

WITH source AS (
    SELECT * FROM {{ source('raw_data', 'customers') }}
),

renamed AS (
    SELECT
        id AS customer_id,
        first_name,
        last_name,
        first_name || ' ' || last_name AS full_name,
        email,
        LOWER(email) AS email_normalized,
        created_at,
        updated_at,
        -- 条件逻辑
        CASE
            WHEN status = 1 THEN 'active'
            WHEN status = 0 THEN 'inactive'
            ELSE 'unknown'
        END AS customer_status,
        -- 使用变量
        '{{ var("start_date") }}' AS report_start_date
    FROM source
)

SELECT * FROM renamed
```

#### 模型配置参数

在模型文件的顶部通过 `{{ config() }}` 块配置物化策略和其他参数：

```sql
-- models/marts/finance/fct_orders.sql

{{
    config(
        materialized='table',           -- 物化策略
        unique_key='order_id',          -- 唯一键（增量模式必需）
        partition_by={'field': 'order_date', 'data_type': 'date'},  -- Snowflake 分区
        cluster_by=['customer_id'],     -- 聚簇键
        tags=['finance', 'daily'],      -- 标签
        schema='marts_finance',         -- 目标 Schema
        alias='orders_fact',            -- 别名（覆盖文件名）
        pre_hook="DELETE FROM {{ this }} WHERE order_date < '2020-01-01'",
        post_hook="GRANT SELECT ON {{ this }} TO ROLE analyst_role",
        snowflake_warehouse='transform_wh',  -- Snowflake 专属
        copy_grants=true,               -- Snowflake 专属：CREATE OR REPLACE 时保留权限
        transient=false                 -- Snowflake 专属：是否为瞬态表
    )
}}

-- 模型 SQL 内容
SELECT ...
```

**所有配置项说明**：

| 配置项 | 类型 | 说明 |
|--------|------|------|
| `materialized` | string | 物化策略：view/table/incremental/ephemeral/materialized_view |
| `unique_key` | string | 增量更新时的唯一键 |
| `partition_by` | dict | Snowflake 分区配置 |
| `cluster_by` | list/string | 聚簇键 |
| `tags` | list | 模型标签，用于筛选运行 |
| `schema` | string | 覆盖默认目标 Schema |
| `alias` | string | 覆盖默认模型名（默认等于文件名） |
| `database` | string | 覆盖目标数据库 |
| `pre_hook` | string/list | 模型执行前执行的 SQL |
| `post_hook` | string/list | 模型执行后执行的 SQL |
| `enabled` | bool | 是否启用该模型 |
| `full_refresh` | bool | 强制全量刷新（增量模型） |
| `snowflake_warehouse` | string | 指定 Snowflake Warehouse |
| `copy_grants` | bool | CREATE OR REPLACE 时保留权限 |
| `transient` | bool | 使用 TRANSIENT TABLE（不保留 Time Travel 数据） |
| `persist_docs` | dict | 将列/表描述持久化到数据库 COMMENT |
| `grants` | dict | 权限授予配置 |

#### 模型间的引用：ref() 函数

`{{ ref() }}` 是 dbt 最重要的函数，用于在模型之间建立**引用关系**：

```sql
-- models/marts/finance/fct_orders.sql

-- ref() 会自动建立依赖关系，dbt 会先执行被引用的模型
-- 同时 ref() 会自动处理 Schema 和数据库引用
SELECT
    o.order_id,
    o.order_date,
    c.customer_id,
    c.full_name AS customer_name,
    p.product_name,
    oi.quantity,
    oi.unit_price,
    oi.quantity * oi.unit_price AS line_total
FROM {{ ref('stg_orders') }} AS o                              -- 引用 staging 模型
LEFT JOIN {{ ref('stg_customers') }} AS c                       -- 引用另一个 staging 模型
    ON o.customer_id = c.customer_id
LEFT JOIN {{ ref('stg_products') }} AS p
    ON o.product_id = p.product_id
LEFT JOIN {{ ref('int_order_items') }} AS oi                    -- 引用 intermediate 模型
    ON o.order_id = oi.order_id
```

**ref() 的特性**：

```sql
-- 1. 基本引用（同项目模型）
{{ ref('model_name') }}

-- 2. 带版本引用（模型分版本时）
{{ ref('model_name', v='1') }}

-- 3. 跨项目引用（dbt Mesh / dbt 1.6+）
{{ ref('other_project', 'model_name') }}

-- 4. 打包引用（跳过当前项目的解析，直接引用依赖包的模型）
{{ ref('model_name', package='dbt_utils', version='1.0.0') }}
```

### 4.3 Python 模型语法

dbt 1.3+ 支持使用 Python 编写模型（需 Snowflake 的 Python UDF/存储过程支持）：

```python
# models/marts/finance/fct_customer_ltv.py

import pandas as pd

def model(dbt, session):
    # 设置模型配置
    dbt.config(
        materialized="table",
        snowflake_warehouse="transform_wh",
        tags=["python", "finance"]
    )

    # 使用 ref() 引用上游模型，返回 DataFrame
    orders_df = dbt.ref("stg_orders").to_df()
    customers_df = dbt.ref("stg_customers").to_df()

    # Pandas 数据处理
    orders_summary = orders_df.groupby("customer_id").agg(
        total_orders=("order_id", "count"),
        total_revenue=("order_total", "sum"),
        first_order_date=("order_date", "min"),
        last_order_date=("order_date", "max"),
        avg_order_value=("order_total", "mean")
    ).reset_index()

    # 关联客户信息
    result = customers_df.merge(orders_summary, on="customer_id", how="left")

    # 计算客户终身价值
    result["customer_ltv"] = result["total_revenue"] * 1.2  # 预测系数

    return result
```

### 4.4 模型命名规范

| 层级 | 前缀 | 示例 | 说明 |
|------|------|------|------|
| Staging | `stg_` | `stg_orders` | 一对一映射源表 |
| Intermediate | `int_` | `int_order_items` | 跨源聚合中间表 |
| Fact | `fct_` | `fct_daily_revenue` | 事实表（度量） |
| Dimension | `dim_` | `dim_customers` | 维度表 |
| Base | `base_` | `base_crm_companies` | 基础视图 |

### 4.5 模型层级设计（Staging → Marts）

```
                     ┌────────────────────────────────────┐
   Raw Sources       │         Staging Layer               │
  ──────────────────►│  stg_customers, stg_orders, etc.    │
  (sources.yml)      │  重命名、类型转换、基础清洗           │
                     └──────────────┬─────────────────────┘
                                    │
                     ┌──────────────▼─────────────────────┐
                     │      Intermediate Layer            │
                     │  int_order_items, int_sessions     │
                     │  跨源 Join、复杂聚合、业务逻辑        │
                     └──────────────┬─────────────────────┘
                                    │
              ┌─────────────────────┼─────────────────────┐
              │                                            │
  ┌───────────▼──────────┐              ┌─────────────────▼──────────┐
  │   Marts - Finance    │              │   Marts - Marketing        │
  │  fct_revenue         │              │  fct_campaign_performance  │
  │  dim_customers       │              │  dim_campaigns             │
  └──────────────────────┘              └────────────────────────────┘
```

---

## 5. Materializations（物化策略）

### 5.1 View

每次查询时重新执行 SQL，不存储物理数据。

```sql
{{
    config(
        materialized='view'
    )
}}

SELECT
    customer_id,
    SUM(order_total) AS lifetime_revenue
FROM {{ ref('stg_orders') }}
GROUP BY customer_id
```

**适用场景**：
- 数据量小（< 1000 行）
- 需要始终看到最新数据
- 下游模型很少引用
- 计算成本低，查询频率低

### 5.2 Table

创建物理表，每次运行时 **DROP + CREATE**（全量重建）。

```sql
{{
    config(
        materialized='table',
        cluster_by=['order_date'],
        partition_by={
            'field': 'order_date',
            'data_type': 'date'
        }
    )
}}

SELECT ... -- 全量数据
```

**适用场景**：
- 被多个下游模型频繁引用
- 数据量中等（< 100万行）
- 查询性能要求高
- 全量刷新时间可接受

### 5.3 Incremental

**增量更新模式**：首次全量创建，后续只插入/更新新数据。这是大型事实表的标准策略。

```sql
{{
    config(
        materialized='incremental',
        unique_key='order_id',                  -- 唯一键
        on_schema_change='sync_all_columns',    -- Schema 变化策略
        incremental_strategy='merge',           -- Snowflake 推荐：merge
        cluster_by=['order_date']
    )
}}

SELECT
    order_id,
    customer_id,
    order_date,
    order_total,
    status
FROM {{ source('raw_data', 'orders') }}

{% if is_incremental() %}
-- 增量逻辑：只处理自上次运行以来的新记录
-- 这个 WHERE 条件只在增量运行时生效
WHERE order_date > (SELECT MAX(order_date) FROM {{ this }})
{% endif %}
```

**增量策略详解（`incremental_strategy`）**：

| 策略 | 说明 | 适用数据库 |
|------|------|-----------|
| `merge` | 使用 MERGE 语句 upsert | Snowflake（推荐） |
| `delete+insert` | 先 DELETE 再 INSERT | 默认策略 |
| `append` | 仅追加，不更新 | 仅追加日志 |
| `insert_overwrite` | 覆盖指定分区 | 分区表 |

**`is_incremental()` 详解**：

```sql
-- 首次运行：is_incremental() 返回 False → 执行全量
-- 后续运行：is_incremental() 返回 True → 只处理增量

-- 也可以指定强制全量刷新
-- dbt run --full-refresh  → 忽略 is_incremental() 条件
```

**`on_schema_change` 选项**：

| 值 | 行为 |
|----|------|
| `ignore` | 新列被忽略（默认） |
| `fail` | 检测到 Schema 变化时失败 |
| `append_new_columns` | 自动添加新列 |
| `sync_all_columns` | 同步所有列（包括删除不存在的列） |

### 5.4 Ephemeral

**临时模型**：不会在数据库中创建对象，而是将 SQL 作为 CTE（Common Table Expression）注入到引用它的模型中。

```sql
-- models/intermediate/int_customer_stats.sql
{{
    config(
        materialized='ephemeral'
    )
}}

SELECT
    customer_id,
    COUNT(*) AS order_count,
    SUM(order_total) AS total_spent
FROM {{ ref('stg_orders') }}
GROUP BY customer_id


-- 引用 ephemeral 模型时，其 SQL 会被注入为 CTE
-- models/marts/dim_customers.sql
SELECT
    c.*,
    cs.order_count,
    cs.total_spent
FROM {{ ref('stg_customers') }} AS c
LEFT JOIN {{ ref('int_customer_stats') }} AS cs
    ON c.customer_id = cs.customer_id
-- 编译后的 SQL：
-- WITH __dbt__cte__int_customer_stats AS (
--     SELECT customer_id, COUNT(*) AS order_count, ...
--     FROM ...
--     GROUP BY customer_id
-- )
-- SELECT c.*, cs.order_count, ...
-- FROM stg_customers AS c
-- LEFT JOIN __dbt__cte__int_customer_stats AS cs ...
```

**适用场景**：
- 轻量级转换，不希望创建额外的数据库对象
- 被少量模型引用（每个引用都会复制一次 SQL）

### 5.5 Materialized View（Snowflake 专属）

Snowflake 的物化视图，自动维护结果集。

```sql
{{
    config(
        materialized='materialized_view',
        secure=true,          -- 安全视图
        copy_grants=true
    )
}}

SELECT
    customer_id,
    DATE_TRUNC('month', order_date) AS order_month,
    SUM(order_total) AS monthly_revenue,
    COUNT(DISTINCT order_id) AS order_count
FROM {{ ref('stg_orders') }}
GROUP BY 1, 2
```

**注意事项**：
- Snowflake 物化视图有严格的 SQL 限制（只支持部分聚合函数）
- 不支持 LEFT JOIN
- 维护成本计入 Warehouse 费用

### 5.6 各策略对比与选择

```
数据量 & 查询频率
      ↑
      │
 高   │   Incremental          Table
      │   (Merge策略)          (全量重建)
      │
 中   │   Table                View / EVT
      │   (小表全量)
      │
 低   │   View / EVT           View
      │
      └────────────────────────────→
          低        中        高       刷新频率需求
```

**决策矩阵**：

| 场景 | 数据量 | 刷新需求 | 推荐策略 |
|------|--------|----------|---------|
| 原始数据清洗 | 大 | 近实时 | View |
| 维度表 | 中 | 每日 | Table |
| 大型事实表 | 极大 | 每小时 | Incremental (merge) |
| 预聚合汇总 | 中 | 每日 | Table |
| 中间计算 CTE | 小 | 按需 | Ephemeral |
| 实时仪表板 | 中 | 自动 | Materialized View |

---

## 6. Sources（数据源）

### 6.1 定义 Sources

Sources 用于定义和文档化 dbt 项目引用的原始数据表：

```yaml
# models/staging/sources.yml

version: 2

sources:
  - name: raw_data                     # Source 名称（命名空间）
    description: "Raw data loaded from production OLTP database via Fivetran"
    database: ecommerce_raw            # 源数据库
    schema: public                     # 源 Schema
    loader: fivetran                   # 加载工具
    freshness:                         # 数据新鲜度检查（可选）
      warn_after: {count: 12, period: hour}      # 12小时旧数据触发警告
      error_after: {count: 24, period: hour}     # 24小时旧数据触发错误
      filter: "id % 2 = 0"            # 新鲜度检查的过滤条件（可选）
    loaded_at_field: _fivetran_synced  # 用于判断新鲜度的时间戳字段

    tags: ["source", "raw"]

    tables:
      - name: customers
        description: "Customer master data from CRM"
        columns:
          - name: id
            description: "Primary key, auto-increment"
            tests:
              - not_null
              - unique
          - name: email
            description: "Customer email address"
            tests:
              - not_null
          - name: created_at
            description: "Account creation timestamp"
            tests:
              - not_null

      - name: orders
        description: "Order transactions"
        freshness:
          warn_after: {count: 4, period: hour}
        columns:
          - name: order_id
            tests:
              - not_null
              - unique
          - name: customer_id
            tests:
              - not_null
              - relationships:
                  to: source('raw_data', 'customers')
                  field: id
          - name: order_total
            tests:
              - not_null
              - dbt_utils.accepted_range:
                  min_value: 0
                  max_value: 100000

      - name: products
        description: "Product catalog"

  - name: marketing_data                # 第二个 Source
    database: ecommerce_raw
    schema: marketing
    loader: stitch
    tables:
      - name: campaigns
      - name: ad_impressions
```

### 6.2 引用 Sources

```sql
-- 引用方式一：source() 函数
SELECT * FROM {{ source('raw_data', 'customers') }}
--                 ^^^^^^^^^^    ^^^^^^^^^^^
--                 source 名称  表 名称

-- 引用方式二：带限定列名
SELECT
    {{ source('raw_data', 'customers') }}.id,
    {{ source('raw_data', 'customers') }}.email
FROM {{ source('raw_data', 'customers') }}

-- 典型使用模式：CTE 中转
WITH source AS (
    SELECT * FROM {{ source('raw_data', 'orders') }}
)
SELECT ...
FROM source
```

### 6.3 Source Freshness（数据新鲜度）

```bash
# 检查所有 Sources 的数据新鲜度
dbt source freshness

# 指定检查特定 Source
dbt source freshness --select source:raw_data.customers

# 只输出有问题的 Source
dbt source freshness --select source:raw_data.orders
```

**新鲜度配置进阶**：

```yaml
sources:
  - name: raw_data
    tables:
      - name: real_time_events
        freshness:
          # 过滤掉已知加载延迟的分区
          filter: "event_date >= CURRENT_DATE - INTERVAL '2 days'"
        loaded_at_field: ingested_at
```

---

## 7. Seeds（种子数据）

Seeds 是存储在 `seeds/` 目录中的 CSV 文件，dbt 会将其加载为数据库中的表。

```csv
-- seeds/country_codes.csv
country_code,country_name,region,iso_alpha3
US,United States,North America,USA
CN,China,Asia,CHN
JP,Japan,Asia,JPN
GB,United Kingdom,Europe,GBR
DE,Germany,Europe,DEU
```

**使用 Seeds**：

```sql
-- 在模型中引用 Seed
SELECT
    c.customer_id,
    c.country_code,
    cc.country_name,
    cc.region
FROM {{ ref('stg_customers') }} AS c
LEFT JOIN {{ ref('country_codes') }} AS cc       -- seed 也通过 ref() 引用
    ON c.country_code = cc.country_code
```

**Seeds 配置**：

```yaml
# dbt_project.yml
seeds:
  ecommerce_analytics:
    +quote_columns: false       # 列名不加引号
    +column_types:              # 指定列的数据类型
      country_code: varchar(3)
      iso_alpha3: varchar(3)
    +delimiter: ","             # 分隔符（默认逗号）
    +full_refresh: false

# 或者在单个 seed 文件顶部用 config block
-- seeds/country_codes.sql
{{
    config(
        column_types={'country_code': 'varchar(3)', 'iso_alpha3': 'varchar(3)'}
    )
}}
```

**Seeds 命令**：

```bash
# 加载所有 Seeds
dbt seed

# 选择性加载
dbt seed --select country_codes

# 强制全量刷新
dbt seed --full-refresh
```

---

## 8. Tests（测试）

### 8.1 通用测试（Generic Tests）

dbt 内置四种通用测试，可直接在 YAML 中声明：

```yaml
# models/staging/schema.yml

version: 2

models:
  - name: stg_customers
    description: "Staged customer data"
    columns:
      - name: customer_id
        description: "Unique customer identifier"
        tests:
          - not_null                     # 非空测试
          - unique                       # 唯一性测试

      - name: email
        tests:
          - not_null
          - unique

      - name: customer_status
        tests:
          - accepted_values:             # 可接受值测试
              values: ['active', 'inactive', 'churned']
              quote: true                # 值加引号
              severity: warn             # 失败时仅警告
              config:
                where: "created_at > '2023-01-01'"  # 只测试 2023 年后的数据

      - name: country_code
        tests:
          - relationships:               # 引用完整性测试
              to: ref('country_codes')   # 引用模型
              field: country_code        # 目标字段
              severity: error
```

**四个内置通用测试**：

| 测试 | 语法 | 说明 |
|------|------|------|
| `not_null` | 直接声明 | 列值不能为 NULL |
| `unique` | 直接声明 | 列值必须唯一 |
| `accepted_values` | `values: [...]` | 列值必须在指定列表中 |
| `relationships` | `to:` + `field:` | 外键引用完整性 |

### 8.2 单一测试（Singular Tests）

在 `tests/` 目录下编写返回 **0 行 = 通过** 的 SQL：

```sql
-- tests/assert_total_orders_positive.sql

-- 如果查询返回任何行，测试失败
-- 返回 0 行 = 通过，返回 > 0 行 = 失败

SELECT
    order_id,
    SUM(line_total) AS total_amount
FROM {{ ref('fct_order_items') }}
GROUP BY order_id
HAVING SUM(line_total) <= 0   -- 找出总额 <= 0 的订单
```

```sql
-- tests/assert_no_duplicate_customers.sql

SELECT
    email,
    COUNT(*) AS occurrences
FROM {{ ref('stg_customers') }}
WHERE email IS NOT NULL
GROUP BY email
HAVING COUNT(*) > 1
```

### 8.3 自定义通用测试

在 `macros/` 目录下创建可复用的测试宏：

```sql
-- macros/custom_tests/test_not_null_multiple.sql

{% macro test_not_null_multiple(model, columns) %}
    -- 同时检查多列非空
    -- 用法：在 YAML 中声明
    --   tests:
    --     - dbt_utils.not_null_multiple:
    --         columns: [first_name, last_name, email]

    SELECT *
    FROM {{ model }}
    WHERE
        {% for column in columns %}
            {{ column }} IS NULL
            {% if not loop.last %}OR{% endif %}
        {% endfor %}
{% endmacro %}
```

```sql
-- macros/custom_tests/test_positive_values.sql

{% macro test_positive_values(model, column_name) %}
    -- 检查列值都为正数

    SELECT
        {{ column_name }}
    FROM {{ model }}
    WHERE {{ column_name }} <= 0
{% endmacro %}
```

```sql
-- macros/custom_tests/test_not_null_proportion.sql

{% macro test_not_null_proportion(model, column_name, at_least=0.95) %}
    -- 检查非空比例不低于指定阈值
    -- 适用于允许部分 NULL 但要求大部分有值的场景

    WITH total AS (
        SELECT COUNT(*) AS total_rows FROM {{ model }}
    ),
    not_null_count AS (
        SELECT COUNT(*) AS filled_rows FROM {{ model }}
        WHERE {{ column_name }} IS NOT NULL
    )
    SELECT *
    FROM total, not_null_count
    WHERE (filled_rows::FLOAT / total_rows) < {{ at_least }}
{% endmacro %}
```

在 YAML 中使用自定义测试：

```yaml
models:
  - name: stg_customers
    columns:
      - name: customer_id
        tests:
          - positive_values                          # 自定义通用测试
          - not_null_proportion:
              at_least: 0.99                         # 期望 99% 非空

      - name: email
        tests:
          - not_null_multiple:
              columns: [email, first_name, last_name]
```

### 8.4 测试配置与运行

```bash
# 运行所有测试
dbt test

# 运行特定模型的测试
dbt test --select stg_customers

# 运行特定目录的测试
dbt test --select staging

# 运行标记为 "production" 的测试
dbt test --select tag:production

# 运行 source 级别的测试
dbt test --select source:raw_data

# 保存测试失败记录（需配置 store_failures: true）
# 会在数据库中创建表记录每次失败的测试结果
dbt test --store-failures
```

**测试严重级别配置**：

```yaml
# dbt_project.yml
tests:
  ecommerce_analytics:
    +severity: warn          # 全局默认为警告
    staging:
      +severity: error       # staging 层测试失败为错误

# 在模型级别覆盖
models:
  - name: stg_orders
    columns:
      - name: order_total
        tests:
          - not_null:
              severity: error        # 此测试失败 = error
          - accepted_range:
              min_value: 0
              severity: warn         # 此测试失败 = warn
              config:
                enabled: true
                store_failures: true
                limit: 1000          # 测试最多扫描 1000 行
                where: "order_date >= CURRENT_DATE - 30"
```

---

## 9. Jinja & Macros（宏）

### 9.1 Jinja 模板语法基础

dbt 使用 **Jinja2** 模板引擎来编写动态 SQL。所有 dbt 模型都可以包含 Jinja 语法。

#### 表达式：`{{ ... }}`

```sql
-- 输出变量或函数结果
SELECT
    '{{ var("start_date") }}' AS report_start,          -- 输出变量值
    '{{ target.name }}' AS environment,                  -- 输出当前环境名
    '{{ run_started_at }}' AS execution_time,             -- 输出运行开始时间
    {{ target.schema }}.my_table                          -- 输出目标 Schema
```

#### 语句：`{% ... %}`

```sql
{% set payment_methods = ["credit_card", "bank_transfer", "paypal"] %}

SELECT
    order_id,
    {% for method in payment_methods %}
        SUM(CASE WHEN payment_method = '{{ method }}' THEN amount ELSE 0 END)
            AS {{ method }}_amount
        {% if not loop.last %},{% endif %}
    {% endfor %}
FROM {{ ref('stg_payments') }}
GROUP BY order_id
```

#### 注释：`{# ... #}`

```sql
{# 这是 Jinja 注释，不会出现在编译后的 SQL 中 #}
-- 这是 SQL 注释，会出现在编译后的 SQL 中

SELECT * FROM {{ ref('stg_orders') }}
{# TODO: 优化这个查询的性能 #}
WHERE order_date >= '2023-01-01'
```

#### 变量赋值与作用域

```sql
-- set：声明变量
{% set my_var = 42 %}
{% set my_text = "hello world" %}
{% set my_list = [1, 2, 3] %}
{% set my_dict = {"key": "value"} %}

-- 变量在整个模型范围内可用
-- 注意：变量赋值只在当前模型/宏内有效
```

#### 条件语句

```sql
{% if target.name == 'prod' %}
    -- 生产环境逻辑
    SELECT * FROM {{ source('raw_data', 'orders') }}
    WHERE order_date >= CURRENT_DATE - INTERVAL '7 days'
{% elif target.name == 'staging' %}
    -- 预发布环境逻辑
    SELECT * FROM {{ source('raw_data', 'orders') }}
    WHERE order_date >= CURRENT_DATE - INTERVAL '1 day'
{% else %}
    -- 开发环境逻辑（仅取少量数据以加快迭代）
    SELECT * FROM {{ source('raw_data', 'orders') }}
    WHERE order_date >= CURRENT_DATE - INTERVAL '3 days'
    LIMIT 1000
{% endif %}
```

#### 循环语句

```sql
-- for 循环
{% set categories = dbt_utils.get_column_values(
    table=ref('stg_products'),
    column='category'
) %}

SELECT
    order_id,
    {% for category in categories %}
        SUM(CASE WHEN p.category = '{{ category }}' THEN oi.quantity ELSE 0 END)
            AS qty_{{ category | lower | replace(' ', '_') }}
        {% if not loop.last %},{% endif %}
    {% endfor %}
FROM {{ ref('fct_order_items') }} AS oi
LEFT JOIN {{ ref('stg_products') }} AS p ON oi.product_id = p.product_id
GROUP BY order_id
```

#### 过滤器

```sql
-- 常用 Jinja 过滤器
{{ "text" | upper }}                    -- TEXT
{{ "text" | lower }}                    -- text
{{ "hello world" | replace(" ", "_") }} -- hello_world
{{ [1, 2, 3] | length }}               -- 3
{{ my_var | default("default_value") }} -- 默认值
{{ my_list | join(", ") }}             -- "1, 2, 3"
{{ my_string | trim }}                 -- 去除首尾空格
```

### 9.2 dbt 内置 Jinja 函数

#### ref() 和 source()

```sql
{{ ref('model_name') }}                    -- 引用模型
{{ source('source_name', 'table_name') }}   -- 引用数据源
```

#### config()

```sql
{{ config(materialized='table', tags=['finance']) }}
```

#### var()

```sql
{{ var('variable_name') }}                 -- 获取变量
{{ var('variable_name', 'default_value') }} -- 带默认值
```

#### 其他关键函数

```sql
-- 当前模型信息
{{ this }}                          -- 当前模型完整路径：database.schema.table
{{ this.schema }}                   -- 当前模型 Schema
{{ this.table }}                    -- 当前模型表名
{{ this.database }}                 -- 当前模型数据库

-- 运行上下文信息
{{ target.name }}                   -- 当前环境名（dev/staging/prod）
{{ target.schema }}                 -- 目标 Schema
{{ target.database }}               -- 目标数据库
{{ run_started_at }}                -- 运行开始时间戳

-- 递增模型条件
{% if is_incremental() %}
    -- 只在增量运行时执行
{% endif %}

-- Schema 生成
{{ generate_schema_name(custom_schema_name, node) }}
{{ generate_database_name(custom_database_name, node) }}
{{ generate_alias_name(custom_alias_name, node) }}

-- 日志函数
{{ log("Processing model: " ~ this, info=True) }}
{{ exceptions.warn("Warning: large table scan") }}
{{ exceptions.raise_compiler_error("Invalid configuration") }}
```

#### 调用其他宏

```sql
-- 调用自定义宏
{{ my_macro(arg1, arg2) }}

-- 调用 dbt_utils 包中的宏
{{ dbt_utils.star(from=ref('stg_orders'), except=['created_at']) }}

-- 调用带命名参数的宏
{{ dbt_utils.date_spine(
    datepart="day",
    start_date="cast('2023-01-01' as date)",
    end_date="cast('2023-12-31' as date)"
) }}
```

### 9.3 自定义 Macros

Macros 是定义在 `macros/` 目录下的可复用 Jinja 函数：

```sql
-- macros/utils.sql

-- 示例 1：货币金额格式化
{% macro format_currency(column_name, precision=2) %}
    ROUND({{ column_name }}::NUMERIC(18, {{ precision }}), {{ precision }})
{% endmacro %}

-- 使用时：
-- SELECT {{ format_currency('order_total') }} FROM ...


-- 示例 2：安全除以零
{% macro safe_divide(numerator, denominator) %}
    CASE
        WHEN {{ denominator }} = 0 OR {{ denominator }} IS NULL THEN NULL
        ELSE {{ numerator }} / {{ denominator }}
    END
{% endmacro %}


-- 示例 3：对指定列集进行数据脱敏
{% macro mask_columns(columns) %}
    {% for col in columns %}
        CASE
            WHEN current_role() IN ('ANALYST_ADMIN', 'DATA_ENGINEER')
                THEN {{ col }}
            ELSE '***MASKED***'
        END AS {{ col }}
        {% if not loop.last %},{% endif %}
    {% endfor %}
{% endmacro %}


-- 示例 4：生成日期范围
{% macro date_range(start_date, end_date) %}
    SELECT
        DATEADD(day, seq4(), '{{ start_date }}'::DATE) AS date_day
    FROM TABLE(GENERATOR(ROWCOUNT => DATEDIFF(day, '{{ start_date }}', '{{ end_date }}') + 1))
{% endmacro %}


-- 示例 5：审计列
{% macro audit_columns() %}
    current_timestamp() AS dbt_updated_at,
    '{{ invocation_id }}' AS dbt_invocation_id,
    '{{ target.name }}' AS dbt_environment
{% endmacro %}

-- 使用时：SELECT *, {{ audit_columns() }} FROM ...
```

```sql
-- macros/generate_schema_name.sql
-- 覆盖默认的 Schema 命名规则

{% macro generate_schema_name(custom_schema_name, node) -%}

    {%- set default_schema = target.schema -%}

    {%- if custom_schema_name is none -%}
        -- 没有自定义 Schema → 使用默认 Schema
        {{ default_schema }}

    {%- elif target.name == 'prod' -%}
        -- 生产环境：直接使用自定义 Schema（不加前缀）
        {{ custom_schema_name | trim }}

    {%- else -%}
        -- 非生产环境：添加环境前缀，避免混淆
        {{ default_schema }}_{{ custom_schema_name | trim }}

    {%- endif -%}

{%- endmacro %}
```

### 9.4 常用 Macros 模式

#### Pivot（行转列）

```sql
-- macros/pivot.sql

{% macro pivot(column_name, values, table_ref, agg='SUM', then_column='value') %}
    {% for value in values %}
        {{ agg }}(
            CASE
                WHEN {{ column_name }} = '{{ value }}'
                THEN {{ then_column }}
                ELSE 0
            END
        ) AS {{ value | lower | replace(' ', '_') }}
        {% if not loop.last %},{% endif %}
    {% endfor %}
{% endmacro %}

-- 使用示例：
-- SELECT
--     product_id,
--     {{ pivot('payment_method', ['credit_card', 'paypal', 'bank_transfer'], ref('stg_payments')) }}
-- FROM {{ ref('stg_payments') }}
-- GROUP BY product_id
```

#### 通用表元数据生成

```sql
-- macros/get_table_columns.sql

{% macro get_table_columns(table_name, schema_name=none, database_name=none) %}
    {# 动态获取表的列名列表 #}
    {% if schema_name is none %}{% set schema_name = target.schema %}{% endif %}
    {% if database_name is none %}{% set database_name = target.database %}{% endif %}

    {% set query %}
        SELECT column_name
        FROM {{ database_name }}.information_schema.columns
        WHERE table_schema = UPPER('{{ schema_name }}')
          AND table_name = UPPER('{{ table_name }}')
        ORDER BY ordinal_position
    {% endset %}

    {% set results = run_query(query) %}
    {% if execute %}
        {% set results_list = results.columns[0].values() %}
    {% else %}
        {% set results_list = [] %}
    {% endif %}

    {{ return(results_list) }}
{% endmacro %}
```

---

## 10. Snapshots（快照/缓慢变化维度）

### 10.1 SCD Type 2 实现

Snapshots 实现 **Type 2 Slowly Changing Dimension（SCD2）**——当源数据变化时，不是在原地更新，而是插入一条新记录并标记有效期。

```sql
-- snapshots/products_snapshot.sql

{% snapshot products_snapshot %}

{{
    config(
        target_schema='snapshots',
        unique_key='product_id',            -- 业务主键
        strategy='timestamp',               -- 策略：timestamp 或 check
        updated_at='last_modified_at',      -- 用于判断变更的时间戳列
        invalidate_hard_deletes=true,       -- 是否检测物理删除
    )
}}

SELECT
    product_id,
    product_name,
    category,
    unit_price,
    cost,
    supplier_id,
    is_active,
    last_modified_at
FROM {{ source('raw_data', 'products') }}

{% endsnapshot %}
```

**执行后生成的表结构**：

```
| product_id | product_name | unit_price | dbt_scd_id | dbt_updated_at | dbt_valid_from | dbt_valid_to |
|------------|-------------|------------|------------|----------------|----------------|--------------|
| P001       | Widget A    | 10.00      | hash_A1    | 2023-01-01     | 2023-01-01     | 2023-06-15   |
| P001       | Widget A    | 12.00      | hash_A2    | 2023-06-15     | 2023-06-15     | NULL         |
| P002       | Widget B    | 15.00      | hash_B1    | 2023-01-01     | 2023-01-01     | NULL         |
```

**快照元数据列说明**：

| 列名 | 说明 |
|------|------|
| `dbt_scd_id` | SCD 记录的唯一标识（hash） |
| `dbt_updated_at` | 该版本记录的创建时间 |
| `dbt_valid_from` | 该版本生效开始时间 |
| `dbt_valid_to` | 该版本失效时间（NULL = 当前有效） |

### 10.2 Snapshot 策略

**Timestamp 策略**（推荐）：使用 `updated_at` 时间戳列检测变化。

```sql
{% snapshot orders_status_snapshot %}

{{
    config(
        target_schema='snapshots',
        unique_key='order_id',
        strategy='timestamp',
        updated_at='status_updated_at',
    )
}}

SELECT * FROM {{ source('raw_data', 'orders') }}

{% endsnapshot %}
```

**Check 策略**：对比指定列的值是否变化（适用于没有时间戳列的表）。

```sql
{% snapshot customers_snapshot %}

{{
    config(
        target_schema='snapshots',
        unique_key='customer_id',
        strategy='check',
        check_cols=['email', 'phone', 'address', 'tier'],  -- 监测这些列的变化
    )
}}

SELECT * FROM {{ source('raw_data', 'customers') }}

{% endsnapshot %}
```

**Snapshot 命令**：

```bash
# 执行所有 Snapshot
dbt snapshot

# 执行特定 Snapshot
dbt snapshot --select products_snapshot

# 快照通常只在新记录时执行变更检测
# 无新数据时几乎零开销
```

**使用 Snapshot 数据查询当前版本**：

```sql
-- 获取所有产品的最新版本
SELECT *
FROM {{ ref('products_snapshot') }}
WHERE dbt_valid_to IS NULL

-- 获取某个时间点的历史快照（时间旅行查询）
SELECT *
FROM {{ ref('products_snapshot') }}
WHERE dbt_valid_from <= '2023-06-01'
  AND (dbt_valid_to > '2023-06-01' OR dbt_valid_to IS NULL)
```

---

## 11. Variables（变量）与环境

```yaml
# dbt_project.yml
vars:
  start_date: '2023-01-01'
  business_days_only: true

  # 嵌套变量
  finance:
    tax_rate: 0.08
    currency: 'USD'
```

**使用变量**：

```sql
-- 在模型中使用变量
SELECT *
FROM {{ ref('stg_orders') }}
WHERE order_date >= '{{ var("start_date") }}'

-- 带默认值
SELECT *
FROM {{ ref('stg_orders') }}
WHERE order_date >= '{{ var("start_date", "2023-06-01") }}'

-- 访问嵌套变量
SELECT {{ var("finance")["tax_rate"] }} AS tax_rate
```

**命令行传递变量**：

```bash
# 运行时覆盖变量
dbt run --vars '{"start_date": "2023-06-01", "business_days_only": false}'

# 使用 JSON 文件
dbt run --vars "$(cat vars/prod_vars.json)"
```

**多环境变量管理**：

```sql
-- 使用 target.name 区分环境
{% if target.name == 'prod' %}
    {% set days_back = 7 %}
{% elif target.name == 'staging' %}
    {% set days_back = 3 %}
{% else %}
    {% set days_back = 30 %}
{% endif %}

SELECT * FROM {{ ref('stg_orders') }}
WHERE order_date >= CURRENT_DATE - INTERVAL '{{ days_back }} days'
```

---

## 12. Hooks 与 Operations

### Hooks

Hooks 在模型执行前后运行 SQL：

```sql
-- 模型级 pre/post hook
{{
    config(
        materialized='table',
        pre_hook=[
            "DELETE FROM {{ this }} WHERE order_date < '2020-01-01'",
            "ALTER SESSION SET TIMEZONE = 'UTC'"
        ],
        post_hook=[
            "GRANT SELECT ON {{ this }} TO ROLE reporter",
            "{{ log_audit_event(this.name, 'model_refreshed') }}"
        ]
    )
}}
```

```yaml
# 项目级 hooks（dbt_project.yml）
on-run-start:
  - "CREATE SCHEMA IF NOT EXISTS {{ target.schema }}_audit"
  - "ALTER WAREHOUSE {{ target.warehouse }} SET WAREHOUSE_SIZE = 'XLARGE'"

on-run-end:
  - "ALTER WAREHOUSE {{ target.warehouse }} SET WAREHOUSE_SIZE = 'XSMALL'"
  - "GRANT SELECT ON ALL TABLES IN SCHEMA {{ target.schema }} TO ROLE analyst"
```

### Operations

Operations 是定义在宏中、通过 `dbt run-operation` 执行的 SQL 操作：

```sql
-- macros/operations.sql

{% macro vacuum_analysis() %}
    -- 收集所有 dbt 模型的统计信息（特定于某些数据库）
    {% set models_to_analyze = [
        'fct_orders', 'fct_revenue', 'dim_customers'
    ] %}

    {% for model in models_to_analyze %}
        ANALYZE TABLE {{ ref(model) }};
        {{ log("Analyzed: " ~ model, info=True) }}
    {% endfor %}
{% endmacro %}
```

```bash
# 执行 operation
dbt run-operation vacuum_analysis
```

---

## 13. Analyses 与 Exposures

### Analyses

`analyses/` 目录中的文件会被编译但**不物化**到数据库：

```sql
-- analyses/churn_analysis.sql

WITH customer_orders AS (
    SELECT
        customer_id,
        MIN(order_date) AS first_order,
        MAX(order_date) AS last_order,
        COUNT(*) AS total_orders
    FROM {{ ref('fct_orders') }}
    GROUP BY customer_id
),

churned AS (
    SELECT
        *,
        DATEDIFF('day', last_order, CURRENT_DATE) AS days_since_last_order
    FROM customer_orders
    WHERE days_since_last_order > 90
)

SELECT
    COUNT(*) AS churned_customers,
    AVG(total_orders) AS avg_orders_before_churn
FROM churned
```

```bash
# 编译 analysis（生成可执行的 SQL）
dbt compile --select churn_analysis
```

### Exposures

Exposures 定义下游对 dbt 模型的依赖（仪表板、报告等）：

```yaml
# models/marts/exposures.yml

version: 2

exposures:
  - name: executive_dashboard
    type: dashboard
    description: "Executive KPI dashboard in Tableau"
    maturity: high
    url: https://tableau.company.com/dashboards/executive
    owner:
      name: "Data Analytics Team"
      email: analytics@company.com

    depends_on:
      - ref('fct_daily_revenue')
      - ref('dim_customers')
      - ref('fct_order_items')

  - name: weekly_finance_report
    type: notebook
    description: "Weekly financial summary generated in Jupyter"
    maturity: medium
    owner:
      name: "Finance Team"
    depends_on:
      - ref('fct_revenue')
```

---

## 14. Packages（包管理）

### packages.yml

```yaml
# packages.yml

packages:
  # dbt Labs 官方包
  - package: dbt-labs/dbt_utils
    version: [">=1.1.0", "<2.0.0"]

  # Snowflake 特性包
  - package: dbt-labs/dbt_external_tables
    version: 0.8.0

  # 审计日志
  - package: dbt-labs/audit_helper
    version: 0.9.0

  # 从 Git 安装
  - git: "https://github.com/my-org/dbt_custom_package.git"
    revision: v1.2.0

  # 从本地路径安装
  - local: submodules/my_local_package
```

### 常用 dbt 包

| 包名 | 用途 |
|------|------|
| `dbt-labs/dbt_utils` | 通用 SQL 工具宏（star, date_spine, pivot 等） |
| `dbt-labs/codegen` | 代码生成器 |
| `dbt-labs/audit_helper` | 数据审计和比较 |
| `dbt-labs/dbt_external_tables` | 外部表管理（Snowflake stages） |
| `dbt-labs/redshift` | Redshift 专属特性 |
| `calogica/dbt_expectations` | 扩展测试库（类似 Great Expectations） |
| `entechlog/dbt_snow_mask` | Snowflake 数据脱敏 |
| `Snowflake-Labs/dbt_constraints` | Snowflake 约束管理 |
| `getdbt/dbt_snowflake_monitoring` | Snowflake 成本监控 |

```bash
# 安装依赖
dbt deps
```

---

## 15. Documentation（文档生成）

### 文档声明

```yaml
# models/schema.yml

version: 2

models:
  - name: fct_orders
    description: |
      ## Order Fact Table

      This table contains one row per order with denormalized customer
      and product information for fast querying.

      ### Business Rules
      - Orders with status 'cancelled' are excluded from revenue calculations
      - Order totals include tax but exclude shipping

    config:
      meta:
        owner: "Data Engineering"
        sla: "Runs every 2 hours, max 10 min"

    columns:
      - name: order_id
        description: "Unique order identifier from source system"
        tests:
          - not_null
          - unique

      - name: customer_id
        description: "Foreign key to dim_customers"

      - name: order_total
        description: "Total order amount including tax (USD)"

      - name: status
        description: |
          Order processing status.
          Possible values: pending, confirmed, shipped, delivered, cancelled

      - name: order_date
        description: "Date the order was placed (in UTC timezone)"
```

### 生成与查看文档

```bash
# 生成文档 HTML
dbt docs generate

# 启动本地文档服务器（默认端口 8080）
dbt docs serve

# 指定端口
dbt docs serve --port 8001

# 无浏览器启动
dbt docs serve --no-browser
```

---

## 16. dbt 命令行参考

### 核心命令

```bash
# ---------- 项目初始化与配置 ----------
dbt init <project_name>           # 初始化新项目
dbt debug                         # 验证连接和配置
dbt debug --config-dir            # 显示配置目录
dbt clean                         # 清理编译缓存和日志

# ---------- 依赖管理 ----------
dbt deps                          # 安装 packages.yml 中的依赖

# ---------- 运行模型 ----------
dbt run                           # 运行所有模型
dbt run --select <model_name>     # 运行指定模型
dbt run --select +<model_name>    # 运行指定模型及其上游依赖
dbt run --select <model_name>+    # 运行指定模型及其下游
dbt run --select path:staging     # 按路径选择
dbt run --select tag:finance      # 按标签选择
dbt run --exclude <model_name>    # 排除指定模型
dbt run --full-refresh            # 强制全量刷新（即使增量模型）

# ---------- 测试 ----------
dbt test                          # 运行所有测试
dbt test --select <model_name>    # 运行指定模型的测试
dbt test --select source:*        # 运行所有 Source 测试

# ---------- 快照 ----------
dbt snapshot                      # 执行所有 Snapshots
dbt snapshot --select <snapshot_name>

# ---------- 种子数据 ----------
dbt seed                          # 加载所有 Seeds
dbt seed --select <seed_name>     # 加载指定 Seed

# ---------- 新鲜度检查 ----------
dbt source freshness              # 检查所有 Source 新鲜度
dbt source freshness --select source:raw_data.orders

# ---------- 编译 ----------
dbt compile                       # 编译所有模型（不执行）
dbt compile --select <model_name> # 预览编译后的 SQL

# ---------- 文档 ----------
dbt docs generate                 # 生成文档
dbt docs serve                    # 启动文档服务器

# ---------- 构建（run + test + seed + snapshot）----------
dbt build                         # 运行所有模型、测试、种子和快照
dbt build --select +fct_orders    # 构建指定模型及其依赖链

# ---------- 其他 ----------
dbt ls                            # 列出所有资源
dbt run-operation <operation>     # 执行 operation
dbt parse                         # 解析项目（验证语法）
dbt show --select <model_name>    # 预览模型查询结果（前5行）
dbt list --resource-type model    # 列出所有模型
dbt list --output json            # JSON 格式输出
```

### 选择语法（Node Selection）

```bash
# dbt 强大的模型选择语法

# 按名称选择
dbt run --select stg_customers

# 按路径选择
dbt run --select path:marts/finance

# 按标签选择
dbt run --select tag:production

# 图运算符
dbt run --select +fct_orders        # fct_orders 及其所有上游
dbt run --select fct_orders+        # fct_orders 及其所有下游
dbt run --select +fct_orders+       # fct_orders 及其上下游（整个依赖链）
dbt run --select 1+fct_orders       # fct_orders 的第 1 级上游
dbt run --select fct_orders+2       # fct_orders 的第 2 级下游

# 组合选择
dbt run --select staging finance.monthly  # 多个选择
dbt run --select tag:nightly,tag:critical # 并集
dbt run --select +fct_orders,stg_customers

# 交集与排除
dbt run --select marts,tag:daily          # marts 且标记为 daily
dbt run --select marts --exclude tag:deprecated  # marts 但排除已废弃

# Source 选择
dbt run --select source:raw_data+         # Source 及其下游

# 测试选择
dbt test --select stg_customers,test_type:unique  # 特定测试类型

# 其他
dbt run --select state:modified+          # 只运行已修改的模型
```

---

## 17. Snowflake 专属特性与最佳实践

### 17.1 Snowflake 适配的物化策略

```sql
-- Snowflake 特有的 TRANSIENT 表（不保留 Time Travel 数据，但成本更低）
{{
    config(
        materialized='table',
        transient=true                    -- TRANSIENT TABLE
    )
}}

-- Snowflake 的 COPY GRANTS
{{
    config(
        materialized='table',
        copy_grants=true                  -- 替换表时保留原有权限
    )
}}

-- 指定 Warehouse
{{
    config(
        materialized='table',
        snowflake_warehouse='transform_xl' -- 大模型使用大 Warehouse
    )
}}

-- 安全视图（Secure View）
{{
    config(
        materialized='view',
        secure=true                       -- SECURE VIEW：隐藏 DDL 和查询逻辑
    )
}}
```

### 17.2 Warehouse 管理

```yaml
# 全局设置（dbt_project.yml）
models:
  ecommerce_analytics:
    +snowflake_warehouse: dbt_default_wh   # 默认
    marts:
      +snowflake_warehouse: dbt_marts_wh   # 集市层使用更大 Warehouse
    staging:
      +snowflake_warehouse: dbt_staging_wh # Staging 使用独立 Warehouse
```

```sql
-- 模型级别覆盖
{{
    config(
        snowflake_warehouse='transform_xl'
    )
}}
```

```yaml
# 在运行开始时自动调整 Warehouse 大小
on-run-start:
  - "ALTER WAREHOUSE {{ target.warehouse }} SET WAREHOUSE_SIZE = 'XLARGE' WAIT_FOR_COMPLETION = TRUE"

on-run-end:
  - "ALTER WAREHOUSE {{ target.warehouse }} SET WAREHOUSE_SIZE = 'XSMALL'"
```

### 17.3 权限与 RBAC

```yaml
# dbt_project.yml 中的 grants 配置
models:
  ecommerce_analytics:
    +grants:
      select: ['reporter', 'analyst']
    marts:
      +grants:
        select: ['reporter', 'analyst', 'bi_tool_role']
    staging:
      +grants:
        select: ['analyst']
```

```sql
-- 模型级别的 post_hook 权限管理
{{
    config(
        materialized='table',
        post_hook=[
            "GRANT SELECT ON {{ this }} TO ROLE analyst",
            "GRANT SELECT ON {{ this }} TO ROLE reporter",
            "GRANT ALL ON {{ this }} TO ROLE data_engineer"
        ]
    )
}}
```

### 17.4 零拷贝克隆（Zero-Copy Cloning）

dbt-snowflake 支持在开发环境中使用零拷贝克隆：

```bash
# 在 profiles.yml 中配置 clone 连接
# Snowflake 会在执行 dbt run 前自动克隆 prod 数据库
# 创建独立的开发环境，几乎不占用额外存储

# profiles.yml 配置示例：
# dev:
#   type: snowflake
#   ...
#   database: ecommerce_prod_clone
#   clone_from: ecommerce_prod    # 从哪个数据库克隆
```

### 17.5 动态表（Dynamic Tables）

Snowflake 的动态表可替代 dbt 调度：

```sql
-- Snowflake 原生 DDL（在 dbt 外部创建）
CREATE OR REPLACE DYNAMIC TABLE customer_orders_summary
    TARGET_LAG = '5 minutes'
    WAREHOUSE = transform_wh
AS
SELECT
    customer_id,
    COUNT(*) AS total_orders,
    SUM(order_total) AS total_revenue
FROM ecommerce_prod.marts.orders
GROUP BY customer_id;

-- 在 dbt 中引用动态表
-- Snowflake 自动维护数据新鲜度
-- dbt 只需要处理不适用动态表的复杂转换
```

---

## 18. 示例一：简单入门案例

### 18.1 项目需求

构建一个简单的销售报表系统，包含：

1. **客户维度表** — 从业务系统清洗客户数据
2. **订单事实表** — 关联客户信息并计算总金额
3. **数据测试** — 确保数据质量
4. **自动文档** — 生成数据血缘和字段说明

**数据源**（假设已在 Snowflake 中）：

| Source 表 | 字段 |
|-----------|------|
| `raw.customers` | id, name, email, signup_date, country |
| `raw.orders` | order_id, customer_id, product_name, quantity, unit_price, order_date |

### 18.2 项目结构

```
simple_sales/
├── dbt_project.yml
├── profiles.yml
├── .gitignore
├── models/
│   └── sales/
│       ├── sources.yml
│       ├── stg_customers.sql
│       ├── stg_orders.sql
│       ├── dim_customers.sql
│       ├── fct_orders.sql
│       └── schema.yml
├── macros/
│   └── currency_format.sql
├── tests/
│   └── assert_positive_revenue.sql
└── seeds/
    └── discount_tiers.csv
```

### 18.3 配置文件

```yaml
# dbt_project.yml

name: 'simple_sales'
version: '1.0.0'
config-version: 2

profile: 'simple_sales'

model-paths: ["models"]
analysis-paths: ["analyses"]
test-paths: ["tests"]
seed-paths: ["seeds"]
macro-paths: ["macros"]
snapshot-paths: ["snapshots"]

clean-targets:
  - "target"
  - "dbt_packages"

models:
  simple_sales:
    sales:
      +materialized: table
      +schema: analytics

seeds:
  simple_sales:
    +schema: reference
```

```yaml
# profiles.yml

simple_sales:
  target: dev
  outputs:
    dev:
      type: snowflake
      account: "your_account.us-east-1"
      user: "your_username"
      password: "your_password"
      role: "dbt_dev"
      warehouse: "dbt_wh"
      database: "simple_sales_dev"
      schema: "dbt"
      threads: 4
```

### 18.4 模型代码

```yaml
# models/sales/sources.yml

version: 2

sources:
  - name: raw
    database: simple_sales_dev
    schema: raw
    tables:
      - name: customers
        description: "Raw customer data from CRM"
        columns:
          - name: id
            tests:
              - not_null
              - unique
          - name: email
            tests:
              - not_null

      - name: orders
        description: "Raw order transactions"
        columns:
          - name: order_id
            tests:
              - not_null
              - unique
          - name: customer_id
            tests:
              - not_null
              - relationships:
                  to: source('raw', 'customers')
                  field: id
          - name: unit_price
            tests:
              - not_null
          - name: quantity
            tests:
              - not_null
        freshness:
          warn_after: {count: 1, period: day}
          loaded_at_field: order_date
```

```sql
-- models/sales/stg_customers.sql
{{
    config(
        materialized='table',
        tags=['staging']
    )
}}

WITH source AS (
    SELECT * FROM {{ source('raw', 'customers') }}
),

renamed AS (
    SELECT
        id AS customer_id,
        name AS customer_name,
        email,
        LOWER(email) AS email_normalized,
        country,
        signup_date,
        -- 派生字段：客户注册年份
        EXTRACT(YEAR FROM signup_date) AS signup_year,
        CURRENT_TIMESTAMP() AS dbt_loaded_at
    FROM source
)

SELECT * FROM renamed
```

```sql
-- models/sales/stg_orders.sql
{{
    config(
        materialized='table',
        tags=['staging']
    )
}}

WITH source AS (
    SELECT * FROM {{ source('raw', 'orders') }}
),

renamed AS (
    SELECT
        order_id,
        customer_id,
        product_name,
        quantity,
        unit_price,
        -- 计算行金额
        quantity * unit_price AS line_total,
        order_date,
        EXTRACT(YEAR FROM order_date) AS order_year,
        EXTRACT(MONTH FROM order_date) AS order_month,
        CURRENT_TIMESTAMP() AS dbt_loaded_at
    FROM source
)

SELECT * FROM renamed
```

```sql
-- models/sales/dim_customers.sql
{{
    config(
        materialized='table',
        tags=['dimension', 'production']
    )
}}

SELECT
    customer_id,
    customer_name,
    email_normalized AS email,
    country,
    signup_date,
    signup_year,
    -- 添加业务分组
    CASE
        WHEN signup_year >= 2024 THEN 'New'
        WHEN signup_year >= 2023 THEN 'Recent'
        WHEN signup_year >= 2020 THEN 'Established'
        ELSE 'Legacy'
    END AS customer_segment,
    dbt_loaded_at
FROM {{ ref('stg_customers') }}
```

```sql
-- models/sales/fct_orders.sql
{{
    config(
        materialized='table',
        tags=['fact', 'production']
    )
}}

SELECT
    o.order_id,
    o.customer_id,
    c.customer_name,
    c.customer_segment,
    c.country,
    o.product_name,
    o.quantity,
    o.unit_price,
    o.line_total,
    o.order_date,
    o.order_year,
    o.order_month
FROM {{ ref('stg_orders') }} AS o
LEFT JOIN {{ ref('dim_customers') }} AS c
    ON o.customer_id = c.customer_id
```

### 18.5 测试代码

```yaml
# models/sales/schema.yml

version: 2

models:
  - name: stg_customers
    description: "Cleaned customer staging data"
    columns:
      - name: customer_id
        description: "Unique customer identifier"
        tests:
          - not_null
          - unique
      - name: email_normalized
        description: "Lowercase normalized email"
        tests:
          - not_null

  - name: dim_customers
    description: "Customer dimension with segmentation"
    columns:
      - name: customer_id
        tests:
          - not_null
          - unique
      - name: customer_segment
        tests:
          - accepted_values:
              values: ['New', 'Recent', 'Established', 'Legacy']

  - name: fct_orders
    description: |
      ## Order Fact Table
      One row per order. Includes denormalized customer and product info.
    columns:
      - name: order_id
        tests:
          - not_null
          - unique
      - name: customer_id
        tests:
          - not_null
          - relationships:
              to: ref('dim_customers')
              field: customer_id
      - name: line_total
        tests:
          - not_null
      - name: quantity
        tests:
          - not_null
          - dbt_utils.at_least_one  # 需要安装 dbt_utils 包
```

```sql
-- tests/assert_positive_revenue.sql

-- 确保所有订单的金额都是正数
-- 空结果 = 测试通过

SELECT
    order_id,
    line_total
FROM {{ ref('fct_orders') }}
WHERE line_total <= 0
```

### 18.6 运行与验证

```bash
# 1. 安装依赖（如果使用了 dbt_utils）
dbt deps

# 2. 验证项目配置和连接
dbt debug

# 3. 编译项目（检查 SQL 语法）
dbt compile

# 4. 运行所有模型
dbt run

# 预期输出：
# 17:00:00  1 of 4 START table model analytics.stg_customers .......... [RUN]
# 17:00:02  1 of 4 OK created table model analytics.stg_customers ..... [SUCCESS in 2s]
# 17:00:02  2 of 4 START table model analytics.stg_orders ............. [RUN]
# 17:00:04  2 of 4 OK created table model analytics.stg_orders ........ [SUCCESS in 2s]
# 17:00:04  3 of 4 START table model analytics.dim_customers .......... [RUN]
# 17:00:06  3 of 4 OK created table model analytics.dim_customers ..... [SUCCESS in 2s]
# 17:00:06  4 of 4 START table model analytics.fct_orders ............. [RUN]
# 17:00:08  4 of 4 OK created table model analytics.fct_orders ........ [SUCCESS in 2s]
# 17:00:08
# 17:00:08 Finished running 4 table models in 0 hours 0 minutes and 8.00 seconds (8.00s).

# 5. 运行测试
dbt test

# 6. 运行 Source 新鲜度检查
dbt source freshness

# 7. 查看模型依赖关系
dbt ls

# 8. 显示 DAG 关系
dbt ls --select +fct_orders
```

### 18.7 生成文档

```bash
# 生成文档
dbt docs generate

# 启动文档服务器
dbt docs serve --port 8080

# 在浏览器中查看：
# - 数据血缘图（DAG）：所有模型的上下游依赖关系
# - 模型描述：来自 YAML 中的 description
# - 列级文档：每个字段的含义和测试
# - Source 信息：原始数据的来源和新鲜度状态
```

---

## 19. 示例二：企业级电商数据平台

### 19.1 业务背景与需求

**场景**：某中大型电商公司需要构建统一的数据分析平台。

**业务需求**：

| 模块 | 需求 | 数据量 |
|------|------|--------|
| **订单分析** | 日/周/月订单汇总，支持品牌/品类/区域下钻 | 日均 50 万订单 |
| **客户 360** | 客户行为画像、分层、生命周期分析 | 1000 万+ 客户 |
| **库存管理** | 实时库存、动销率、补货预警 | 10 万 SKU |
| **营销分析** | 活动效果评估、ROI 计算、渠道归因 | 日均 200 万事件 |

**技术架构**：

```
OLTP (PostgreSQL) ──┐
Clickstream (Kafka) ─┼──► Fivetran/Airbyte ──► Snowflake Raw ──► dbt Transform ──► BI/ML
CRM (Salesforce) ────┘                                                         │
                                                                 ┌──────────────┘
                                                                 ▼
                                                     Tableau / Looker / Streamlit
```

### 19.2 项目结构

```
ecommerce_analytics/
├── dbt_project.yml
├── packages.yml
├── package-lock.yml
├── profiles.yml
├── .gitignore
├── .pre-commit-config.yaml
│
├── models/
│   ├── staging/
│   │   ├── sources.yml
│   │   ├── crm/
│   │   │   ├── stg_crm__customers.sql
│   │   │   ├── stg_crm__leads.sql
│   │   │   └── stg_crm__opportunities.sql
│   │   ├── erp/
│   │   │   ├── stg_erp__orders.sql
│   │   │   ├── stg_erp__order_items.sql
│   │   │   ├── stg_erp__products.sql
│   │   │   └── stg_erp__inventory.sql
│   │   └── events/
│   │       ├── stg_events__page_views.sql
│   │       └── stg_events__transactions.sql
│   │
│   ├── intermediate/
│   │   ├── financial/
│   │   │   ├── int_order_enriched.sql
│   │   │   ├── int_order_payments.sql
│   │   │   └── int_daily_revenue.sql
│   │   ├── customer/
│   │   │   ├── int_customer_orders.sql
│   │   │   ├── int_customer_sessions.sql
│   │   │   └── int_customer_lifecycle.sql
│   │   └── inventory/
│   │       ├── int_inventory_daily.sql
│   │       └── int_sell_through.sql
│   │
│   └── marts/
│       ├── finance/
│       │   ├── fct_revenue_daily.sql
│       │   ├── fct_orders.sql
│       │   ├── fct_order_items.sql
│       │   ├── dim_products.sql
│       │   └── dim_dates.sql
│       ├── customers/
│       │   ├── dim_customers.sql
│       │   └── fct_customer_metrics.sql
│       ├── inventory/
│       │   ├── fct_inventory_levels.sql
│       │   └── dim_warehouses.sql
│       └── marketing/
│           ├── fct_campaign_performance.sql
│           └── dim_campaigns.sql
│
├── snapshots/
│   ├── products_snapshot.sql
│   ├── customers_snapshot.sql
│   └── inventory_snapshot.sql
│
├── macros/
│   ├── generate_schema_name.sql
│   ├── financial/
│   │   ├── currency_conversion.sql
│   │   └── margin_calculation.sql
│   ├── customer/
│   │   └── customer_segmentation.sql
│   ├── testing/
│   │   ├── test_not_null_multiple.sql
│   │   ├── test_positive_values.sql
│   │   └── test_referential_integrity.sql
│   ├── utils/
│   │   ├── safe_divide.sql
│   │   ├── date_spine.sql
│   │   └── audit_columns.sql
│   └── snowflake/
│       ├── warehouse_scale.sql
│       └── query_tag.sql
│
├── tests/
│   ├── integration/
│   │   ├── assert_order_item_totals_match.sql
│   │   └── assert_inventory_non_negative.sql
│   └── business_rules/
│       ├── assert_revenue_matches_payments.sql
│       └── assert_customer_countries_exist.sql
│
├── analyses/
│   ├── cohort_retention.sql
│   ├── customer_ltv_projection.sql
│   └── ab_test_significance.sql
│
├── seeds/
│   ├── country_mapping.csv
│   ├── currency_exchange_rates.csv
│   ├── product_categories.csv
│   └── discount_tiers.csv
│
├── profiles/
│   ├── profiles.yml
│   └── profiles.prod.yml
│
├── scripts/
│   ├── ci_run.sh
│   ├── prod_deploy.sh
│   └── schema_diff.sh
│
└── .github/
    └── workflows/
        ├── ci_dbt.yml
        └── deploy_prod.yml
```

### 19.3 配置文件详解

```yaml
# dbt_project.yml

name: 'ecommerce_analytics'
version: '2.0.0'
config-version: 2

profile: 'ecommerce_analytics'

require-dbt-version: ">=1.7.0"

model-paths: ["models"]
analysis-paths: ["analyses"]
test-paths: ["tests"]
seed-paths: ["seeds"]
macro-paths: ["macros"]
snapshot-paths: ["snapshots"]

clean-targets:
  - "target"
  - "dbt_packages"

# 查询注释：在 Snowflake QUERY_HISTORY 中追踪每个查询
query-comment:
  comment: |
    {
        "dbt_invocation_id": "{{ invocation_id }}",
        "node": "{{ node.name }}",
        "materialized": "{{ node.config.materialized }}"
    }
  append: true

# ---------------------------------------------------------------------------
# 模型配置
# ---------------------------------------------------------------------------
models:
  ecommerce_analytics:

    # ---- Staging 层 ----
    staging:
      +materialized: view                          # 轻量视图，不存储冗余数据
      +schema: staging
      +tags: ["staging"]
      +snowflake_warehouse: staging_wh             # 小 Warehouse 即可
      +grants:
        select: ['analyst_role']
      crm:
        +schema: staging_crm
      erp:
        +schema: staging_erp
      events:
        +schema: staging_events

    # ---- Intermediate 层 ----
    intermediate:
      +materialized: ephemeral                     # 中间层使用 ephemeral 减少存储
      +tags: ["intermediate"]
      financial:
        +materialized: table                       # 部分复杂度高的中间表物化为 table
        +schema: intermediate_finance
      customer:
        +schema: intermediate_customer

    # ---- Marts 层 ----
    marts:
      +materialized: table
      +tags: ["marts", "production"]
      +snowflake_warehouse: marts_wh               # 大 Warehouse，关注性能
      +post-hook: "GRANT SELECT ON {{ this }} TO ROLE bi_viewer"
      +persist_docs:
        relation: true
        columns: true

      finance:
        +schema: marts_finance
        +tags: ["finance", "p0"]                    # P0 = 最高优先级
        +post-hook:
          - "GRANT SELECT ON {{ this }} TO ROLE finance_analyst"
          - "GRANT SELECT ON {{ this }} TO ROLE executive_dashboard"

      customers:
        +schema: marts_customers
        +tags: ["customers", "p1"]
        +post-hook:
          - "GRANT SELECT ON {{ this }} TO ROLE marketing_analyst"

      marketing:
        +schema: marts_marketing
        +tags: ["marketing", "p1"]

# ---------------------------------------------------------------------------
# Snapshots
# ---------------------------------------------------------------------------
snapshots:
  ecommerce_analytics:
    +target_schema: snapshots
    +strategy: timestamp
    +updated_at: dbt_updated_at
    +tags: ["snapshot"]
    +post-hook: "GRANT SELECT ON {{ this }} TO ROLE analyst_role"

# ---------------------------------------------------------------------------
# 测试配置
# ---------------------------------------------------------------------------
tests:
  ecommerce_analytics:
    +store_failures: true
    +schema: test_failures
    staging:
      +severity: error
    intermediate:
      +severity: warn
    marts:
      +severity: error                              # 集市层测试失败 = 阻断
      finance:
        +severity: error
        +store_failures: true

# ---------------------------------------------------------------------------
# 变量
# ---------------------------------------------------------------------------
vars:
  # 业务配置
  fiscal_year_start_month: 2                       # 财年从 2 月开始
  default_currency: 'USD'
  high_value_threshold: 500

  # 数据窗口配置
  days_of_history_for_ltv: 365
  churn_days_threshold: 90

  # 环境特定配置覆盖（可通过 --vars 覆盖）
  pricing:
    tax_rate: 0.08
    shipping_base_rate: 5.99

  # dbt_utils 配置
  dbt_utils_dispatch_list: ["ecommerce_analytics"]

# ---------------------------------------------------------------------------
# 调度配置（配合 dbt Cloud 或 Airflow）
# ---------------------------------------------------------------------------
on-run-start:
  - "ALTER WAREHOUSE marts_wh SET WAREHOUSE_SIZE = 'XLARGE' WAIT_FOR_COMPLETION = TRUE"
  - "USE ROLE dbt_executor"

on-run-end:
  - "ALTER WAREHOUSE marts_wh SET WAREHOUSE_SIZE = 'XSMALL'"
  - |
    {% if target.name == 'prod' %}
      GRANT SELECT ON ALL TABLES IN SCHEMA marts_finance TO ROLE bi_viewer;
      GRANT SELECT ON ALL TABLES IN SCHEMA marts_customers TO ROLE bi_viewer;
    {% endif %}
```

```yaml
# packages.yml

packages:
  - package: dbt-labs/dbt_utils
    version: [">=1.1.0", "<2.0.0"]

  - package: dbt-labs/codegen
    version: 0.12.0

  - package: calogica/dbt_expectations
    version: [">=0.10.0", "<0.11.0"]

  - package: dbt-labs/audit_helper
    version: 0.11.0

  - package: entechlog/dbt_snow_mask
    version: 0.4.0
```

### 19.4 Sources 定义

```yaml
# models/staging/sources.yml

version: 2

sources:
  # =========================================================================
  # Source 1: CRM 系统（客户关系管理）
  # =========================================================================
  - name: crm
    description: "Salesforce CRM data synced via Fivetran every 15 minutes"
    database: raw_data
    schema: salesforce
    loader: fivetran
    freshness:
      warn_after: {count: 2, period: hour}
      error_after: {count: 6, period: hour}

    tables:
      - name: accounts
        description: "Customer account master data"
        freshness:
          warn_after: {count: 1, period: hour}
        loaded_at_field: _fivetran_synced
        columns:
          - name: id
            description: "Salesforce Account ID (18-char)"
            tests:
              - not_null
              - unique
          - name: name
            tests:
              - not_null
          - name: industry
            description: "Industry classification"
          - name: annual_revenue
            description: "Reported annual revenue (USD)"
          - name: created_date
            tests:
              - not_null

      - name: leads
        description: "Marketing leads"
        columns:
          - name: id
            tests: &pk_tests
              - not_null
              - unique
          - name: email
            tests:
              - not_null
          - name: status
            tests:
              - accepted_values:
                  values: ['open', 'contacted', 'qualified', 'converted', 'disqualified']

      - name: opportunities
        description: "Sales opportunities pipeline"

  # =========================================================================
  # Source 2: ERP 系统（企业资源计划）
  # =========================================================================
  - name: erp
    description: "Order and inventory data from ERP (PostgreSQL CDC via Airbyte)"
    database: raw_data
    schema: erp
    loader: airbyte
    freshness:
      warn_after: {count: 30, period: minute}
      error_after: {count: 2, period: hour}

    tables:
      - name: orders
        description: "Order header records"
        loaded_at_field: _airbyte_emitted_at
        columns:
          - name: order_id
            tests: *pk_tests
          - name: customer_id
            tests:
              - not_null
          - name: order_date
            tests:
              - not_null
          - name: order_status
            tests:
              - accepted_values:
                  values: ['pending', 'confirmed', 'shipped', 'delivered', 'cancelled', 'returned']
                  quote: true
          - name: total_amount
            tests:
              - not_null

      - name: order_items
        description: "Order line items"

      - name: products
        description: "Product master data"

      - name: inventory
        description: "Warehouse inventory levels"

  # =========================================================================
  # Source 3: 事件追踪
  # =========================================================================
  - name: events
    description: "Clickstream and transactional events from Kafka via Snowpipe"
    database: raw_data
    schema: events
    loader: snowpipe
    freshness:
      warn_after: {count: 10, period: minute}
      error_after: {count: 30, period: minute}

    tables:
      - name: page_views
      - name: transactions
```

### 19.5 Staging 层模型

```sql
-- models/staging/erp/stg_erp__orders.sql

{{
    config(
        materialized='view',
        tags=['staging', 'erp', 'orders'],
        snowflake_warehouse='staging_wh'
    )
}}

WITH source AS (
    SELECT *
    FROM {{ source('erp', 'orders') }}
    -- 在 dev 环境限制数据量
    {% if target.name != 'prod' %}
    WHERE order_date >= CURRENT_DATE - INTERVAL '30 days'
    {% endif %}
),

renamed AS (
    SELECT
        -- 主键
        order_id,

        -- 外键
        customer_id,

        -- 业务字段（统一命名规范：小写 + 下划线）
        order_date,
        order_status,
        total_amount AS order_total,
        shipping_amount,
        tax_amount,
        discount_amount,
        payment_method,
        shipping_address_city,
        shipping_address_country,
        currency_code,

        -- 计算字段
        total_amount - tax_amount - shipping_amount + discount_amount
            AS net_revenue,

        -- 审计列
        _airbyte_emitted_at AS source_updated_at,
        CURRENT_TIMESTAMP() AS dbt_loaded_at,

        -- 元数据
        '{{ invocation_id }}'::VARCHAR AS dbt_job_id

    FROM source
)

SELECT * FROM renamed
```

```sql
-- models/staging/crm/stg_crm__customers.sql

{{
    config(
        materialized='view',
        tags=['staging', 'crm', 'customers']
    )
}}

WITH source AS (
    SELECT * FROM {{ source('crm', 'accounts') }}
),

renamed AS (
    SELECT
        id AS customer_id,
        name AS company_name,
        industry,
        annual_revenue,
        number_of_employees,
        billing_country,
        billing_city,
        -- Salesforce 层级
        type AS account_type,
        owner_id AS account_owner_id,
        -- 日期
        created_date AS customer_since,
        last_activity_date,
        -- 状态
        is_active__c AS is_active,
        customer_tier__c AS customer_tier,       -- 自定义字段：客户等级
        -- 审计
        _fivetran_synced AS source_synced_at,
        CURRENT_TIMESTAMP() AS dbt_loaded_at
    FROM source
)

SELECT * FROM renamed
```

```sql
-- models/staging/events/stg_events__page_views.sql

{{
    config(
        materialized='view',
        tags=['staging', 'events', 'page_views']
    )
}}

WITH source AS (
    SELECT * FROM {{ source('events', 'page_views') }}
),

enriched AS (
    SELECT
        event_id,
        session_id,
        user_id,
        page_url,
        page_title,
        -- 从 URL 解析 UTM 参数
        PARSE_URL(page_url):parameters:utm_source::STRING AS utm_source,
        PARSE_URL(page_url):parameters:utm_medium::STRING AS utm_medium,
        PARSE_URL(page_url):parameters:utm_campaign::STRING AS utm_campaign,
        -- 设备信息
        device_type,
        browser,
        -- 行为度量
        time_on_page_seconds,
        scroll_depth_percent,
        -- 时间
        event_timestamp,
        DATE(event_timestamp) AS event_date,
        HOUR(event_timestamp) AS event_hour,
        CURRENT_TIMESTAMP() AS dbt_loaded_at
    FROM source
)

SELECT * FROM enriched
```

### 19.6 Intermediate 层模型

```sql
-- models/intermediate/financial/int_order_enriched.sql

{{
    config(
        materialized='ephemeral',
        tags=['intermediate', 'financial']
    )
}}

WITH orders AS (
    SELECT * FROM {{ ref('stg_erp__orders') }}
),

order_items AS (
    SELECT * FROM {{ ref('stg_erp__order_items') }}
),

products AS (
    SELECT * FROM {{ ref('stg_erp__products') }}
),

-- 聚合订单行项目
order_aggregates AS (
    SELECT
        order_id,
        COUNT(DISTINCT product_id) AS unique_products,
        SUM(quantity) AS total_items,
        SUM(line_total) AS items_total,
        -- 使用了哪些产品类别
        LISTAGG(DISTINCT product_category, ', ') AS categories_in_order
    FROM order_items
    LEFT JOIN products USING (product_id)
    GROUP BY order_id
)

SELECT
    o.*,
    oa.unique_products,
    oa.total_items,
    oa.items_total,
    oa.categories_in_order,
    -- 金额一致性检查
    CASE
        WHEN ABS(o.order_total - oa.items_total) < 0.01 THEN TRUE
        ELSE FALSE
    END AS amounts_match
FROM orders AS o
LEFT JOIN order_aggregates AS oa
    ON o.order_id = oa.order_id
```

```sql
-- models/intermediate/customer/int_customer_orders.sql

{{
    config(
        materialized='table',
        tags=['intermediate', 'customer'],
        snowflake_warehouse='transform_wh'
    )
}}

WITH orders AS (
    SELECT * FROM {{ ref('stg_erp__orders') }}
    WHERE order_status NOT IN ('cancelled', 'returned')
),

customer_metrics AS (
    SELECT
        customer_id,

        -- 订单统计
        COUNT(DISTINCT order_id) AS total_orders,
        COUNT(DISTINCT DATE_TRUNC('month', order_date)) AS active_months,

        -- 金额统计
        SUM(order_total) AS total_revenue,
        AVG(order_total) AS avg_order_value,
        MIN(order_total) AS min_order_value,
        MAX(order_total) AS max_order_value,

        -- 时间统计
        MIN(order_date) AS first_order_date,
        MAX(order_date) AS last_order_date,
        DATEDIFF('day', MIN(order_date), MAX(order_date)) AS customer_lifetime_days,

        -- 最近订单日期
        DATEDIFF('day', MAX(order_date), CURRENT_DATE()) AS days_since_last_order,

        -- 消费分布
        SUM(CASE WHEN order_total >= {{ var('high_value_threshold') }}
            THEN 1 ELSE 0 END) AS high_value_orders,

        -- 首选支付方式
        MODE(payment_method) AS preferred_payment_method,

        -- 平均下单间隔
        CASE
            WHEN COUNT(DISTINCT order_id) > 1
            THEN DATEDIFF('day', MIN(order_date), MAX(order_date))
                 / (COUNT(DISTINCT order_id) - 1)
            ELSE NULL
        END AS avg_days_between_orders

    FROM orders
    GROUP BY customer_id
)

SELECT
    *,
    -- 客户分层
    CASE
        WHEN total_revenue >= 10000 THEN 'VIP'
        WHEN total_revenue >= 5000  THEN 'Gold'
        WHEN total_revenue >= 1000  THEN 'Silver'
        WHEN total_revenue >= 100   THEN 'Bronze'
        ELSE 'New'
    END AS revenue_tier,

    -- 活跃度分类
    CASE
        WHEN days_since_last_order <= 30  THEN 'Active'
        WHEN days_since_last_order <= 90  THEN 'At Risk'
        WHEN days_since_last_order <= 180 THEN 'Dormant'
        ELSE 'Churned'
    END AS activity_status,

    CURRENT_TIMESTAMP() AS dbt_updated_at

FROM customer_metrics
```

### 19.7 Marts 层模型（核心业务表）

```sql
-- models/marts/finance/fct_orders.sql

{{
    config(
        materialized='incremental',
        unique_key='order_id',
        incremental_strategy='merge',
        cluster_by=['order_date'],
        partition_by={'field': 'order_date', 'data_type': 'date'},
        tags=['marts', 'finance', 'fact', 'p0'],
        snowflake_warehouse='marts_wh'
    )
}}

WITH enriched_orders AS (
    SELECT * FROM {{ ref('int_order_enriched') }}
),

customers AS (
    SELECT
        customer_id,
        company_name,
        industry,
        customer_tier,
        billing_country
    FROM {{ ref('stg_crm__customers') }}
)

SELECT
    -- 主键
    eo.order_id,

    -- 外键（维度引用）
    eo.customer_id,
    eo.order_date,
    {{ dbt_utils.generate_surrogate_key(['eo.order_date']) }} AS date_key,

    -- 客户维度
    c.company_name AS customer_name,
    c.industry AS customer_industry,
    c.customer_tier,
    c.billing_country,

    -- 订单度量
    eo.order_total,
    eo.tax_amount,
    eo.shipping_amount,
    eo.discount_amount,
    eo.net_revenue,
    eo.items_total,

    -- 订单属性
    eo.order_status,
    eo.payment_method,
    eo.currency_code,
    eo.unique_products,
    eo.total_items,
    eo.categories_in_order,

    -- 数据质量标记
    eo.amounts_match,

    -- 审计
    eo.source_updated_at,
    CURRENT_TIMESTAMP() AS dbt_updated_at,
    '{{ invocation_id }}' AS dbt_job_id

FROM enriched_orders AS eo
LEFT JOIN customers AS c
    ON eo.customer_id = c.customer_id

{% if is_incremental() %}
-- 增量逻辑：只处理上次运行以来新增/更新的订单
WHERE eo.order_date >= (
    SELECT COALESCE(
        DATEADD(day, -3, MAX(order_date)),  -- 3 天重叠窗口防止数据延迟
        '2020-01-01'
    )
    FROM {{ this }}
)
{% endif %}
```

```sql
-- models/marts/finance/fct_revenue_daily.sql

{{
    config(
        materialized='incremental',
        unique_key=['date_day', 'customer_tier', 'billing_country'],
        incremental_strategy='merge',
        cluster_by=['date_day'],
        tags=['marts', 'finance', 'p0'],
        snowflake_warehouse='marts_wh'
    )
}}

WITH orders AS (
    SELECT * FROM {{ ref('fct_orders') }}
    WHERE order_status NOT IN ('cancelled', 'returned')
),

daily_revenue AS (
    SELECT
        order_date AS date_day,
        customer_tier,
        billing_country,

        -- 收入指标
        SUM(net_revenue) AS net_revenue,
        SUM(order_total) AS gross_revenue,
        SUM(discount_amount) AS total_discounts,
        SUM(tax_amount) AS total_tax,
        SUM(shipping_amount) AS total_shipping,

        -- 订单指标
        COUNT(DISTINCT order_id) AS total_orders,
        COUNT(DISTINCT customer_id) AS unique_customers,
        SUM(total_items) AS total_items_sold,

        -- 平均指标
        ROUND(AVG(order_total), 2) AS avg_order_value,
        ROUND(SUM(net_revenue) / NULLIF(COUNT(DISTINCT customer_id), 0), 2)
            AS avg_revenue_per_customer,

        -- 毛利率（假设成本占收入 60%，实际应从成本表获取）
        ROUND(SUM(net_revenue) * 0.4, 2) AS estimated_gross_margin,

        -- 计数
        COUNT(DISTINCT CASE WHEN payment_method = 'credit_card'
            THEN order_id END) AS credit_card_orders,
        COUNT(DISTINCT CASE WHEN payment_method = 'paypal'
            THEN order_id END) AS paypal_orders,

        -- 7 天移动平均
        ROUND(AVG(SUM(net_revenue)) OVER (
            PARTITION BY customer_tier, billing_country
            ORDER BY order_date
            ROWS BETWEEN 6 PRECEDING AND CURRENT ROW
        ), 2) AS revenue_7d_moving_avg

    FROM orders
    GROUP BY 1, 2, 3
)

SELECT * FROM daily_revenue
{% if is_incremental() %}
WHERE date_day >= (SELECT MAX(date_day) FROM {{ this }})
{% endif %}
```

```sql
-- models/marts/customers/dim_customers.sql

{{
    config(
        materialized='table',
        tags=['marts', 'customers', 'dimension', 'p1'],
        cluster_by=['customer_tier'],
        snowflake_warehouse='marts_wh'
    )
}}

WITH customers AS (
    SELECT * FROM {{ ref('stg_crm__customers') }}
),

customer_orders AS (
    SELECT * FROM {{ ref('int_customer_orders') }}
),

sessions AS (
    SELECT * FROM {{ ref('int_customer_sessions') }}
)

SELECT
    -- 基础信息
    c.customer_id,
    c.company_name,
    c.industry,
    c.annual_revenue,
    c.number_of_employees,
    c.billing_country,
    c.billing_city,
    c.account_type,
    c.customer_tier,
    c.customer_since,

    -- 订单行为指标
    co.total_orders,
    co.total_revenue,
    co.avg_order_value,
    co.first_order_date,
    co.last_order_date,
    co.days_since_last_order,
    co.avg_days_between_orders,
    co.preferred_payment_method,
    co.high_value_orders,

    -- 分层与状态
    co.revenue_tier,
    co.activity_status,

    -- 会话行为指标
    s.total_sessions,
    s.avg_session_duration_minutes,
    s.total_page_views,
    s.last_session_date,

    -- 客户的完整生命周期价值（当前 + 预测）
    co.total_revenue AS historical_revenue,
    ROUND(
        co.total_revenue
        / NULLIF(DATEDIFF('month', co.first_order_date, CURRENT_DATE()), 0)
        * 12,
        2
    ) AS annualized_revenue_run_rate,

    -- 流失风险评分（简单模型：RFM 组合）
    CASE
        WHEN co.revenue_tier = 'VIP' AND co.days_since_last_order <= 30 THEN 1
        WHEN co.days_since_last_order <= 30 THEN 2
        WHEN co.days_since_last_order <= 60 THEN 3
        WHEN co.days_since_last_order <= 90 THEN 4
        ELSE 5
    END AS churn_risk_score,

    -- 元数据
    CURRENT_TIMESTAMP() AS dbt_updated_at

FROM customers AS c
LEFT JOIN customer_orders AS co
    ON c.customer_id = co.customer_id
LEFT JOIN sessions AS s
    ON c.customer_id = s.customer_id
```

### 19.8 自定义 Macros

```sql
-- macros/financial/currency_conversion.sql

{% macro convert_currency(amount_column, from_currency, to_currency='USD') %}
    {# 币种转换宏 —— 需要 currency_exchange_rates seed 表 #}

    CASE
        WHEN UPPER('{{ from_currency }}') = UPPER('{{ to_currency }}')
            THEN {{ amount_column }}
        ELSE
            {{ amount_column }} * (
                SELECT conversion_rate
                FROM {{ ref('currency_exchange_rates') }}
                WHERE from_currency = UPPER('{{ from_currency }}')
                  AND to_currency = UPPER('{{ to_currency }}')
            )
    END
{% endmacro %}
```

```sql
-- macros/financial/margin_calculation.sql

{% macro calculate_margin(revenue, cost, precision=4) %}
    {# 计算利润率，安全处理除零 #}

    CASE
        WHEN {{ revenue }} IS NULL OR {{ cost }} IS NULL THEN NULL
        WHEN {{ revenue }} = 0 THEN 0
        ELSE ROUND(({{ revenue }} - {{ cost }}) / {{ revenue }}, {{ precision }})
    END
{% endmacro %}
```

```sql
-- macros/utils/audit_columns.sql

{% macro audit_columns(include_invocation_id=true) %}
    {# 标准审计列——每个模型都应包含 #}

    CURRENT_TIMESTAMP() AS dbt_updated_at
    {% if include_invocation_id %}
        ,'{{ invocation_id }}'::VARCHAR AS dbt_invocation_id
    {% endif %}
    ,'{{ target.name }}'::VARCHAR AS dbt_environment
{% endmacro %}
```

```sql
-- macros/customer/customer_segmentation.sql

{% macro segment_customers(revenue_column, recency_column, frequency_column) %}
    {# 基于 RFM 的客户分群宏 #}

    CASE
        -- VIP：高价值 + 近期活跃 + 高频
        WHEN {{ revenue_column }} >= 10000
            AND {{ recency_column }} <= 30
            AND {{ frequency_column }} >= 10
            THEN 'VIP'

        -- 忠诚客户：中高价值 + 高频
        WHEN {{ revenue_column }} >= 5000
            AND {{ frequency_column }} >= 5
            THEN 'Loyal'

        -- 有潜力：近期活跃 但消费不高
        WHEN {{ recency_column }} <= 30
            AND {{ revenue_column }} < 5000
            THEN 'Potential'

        -- 需挽回：曾经高价值 但近期不活跃
        WHEN {{ revenue_column }} >= 5000
            AND {{ recency_column }} > 60
            THEN 'At Risk - High Value'

        -- 流失风险
        WHEN {{ recency_column }} > 90
            THEN 'Churn Risk'

        -- 新客户
        WHEN {{ frequency_column }} = 1
            THEN 'New'

        ELSE 'Regular'
    END
{% endmacro %}
```

### 19.9 增量策略模型

```sql
-- models/marts/marketing/fct_campaign_performance.sql

{{
    config(
        materialized='incremental',
        unique_key=['campaign_id', 'date_day'],
        incremental_strategy='merge',
        on_schema_change='sync_all_columns',
        cluster_by=['date_day'],
        tags=['marts', 'marketing', 'p1'],
        snowflake_warehouse='marts_wh'
    )
}}

WITH campaign_events AS (
    SELECT
        campaign_id,
        event_date AS date_day,
        event_type,
        user_id,
        revenue_impact
    FROM {{ ref('stg_marketing__campaign_events') }}

    {% if is_incremental() %}
    WHERE event_date >= (
        SELECT COALESCE(DATEADD(day, -7, MAX(date_day)), '2023-01-01')
        FROM {{ this }}
    )
    {% endif %}
),

daily_metrics AS (
    SELECT
        campaign_id,
        date_day,

        -- 曝光与点击
        COUNT(DISTINCT CASE WHEN event_type = 'impression'
            THEN user_id END) AS impressions,
        COUNT(DISTINCT CASE WHEN event_type = 'click'
            THEN user_id END) AS clicks,

        -- 转化
        COUNT(DISTINCT CASE WHEN event_type = 'conversion'
            THEN user_id END) AS conversions,
        COUNT(DISTINCT CASE WHEN event_type = 'purchase'
            THEN user_id END) AS purchases,

        -- 收入
        SUM(CASE WHEN event_type = 'purchase'
            THEN revenue_impact ELSE 0 END) AS attributed_revenue,
        SUM(CASE WHEN event_type = 'purchase'
            THEN revenue_impact ELSE 0 END) * 0.3 AS estimated_margin,

        -- 花费（需要从广告平台同步）
        0 AS ad_spend  -- 由另一个流程更新

    FROM campaign_events
    GROUP BY 1, 2
),

with_metrics AS (
    SELECT
        *,

        -- CTR（点击率）
        {{ safe_divide('clicks', 'impressions') }} AS ctr,

        -- CVR（转化率）
        {{ safe_divide('conversions', 'clicks') }} AS cvr,

        -- CPA（每次获客成本）
        {{ safe_divide('ad_spend', 'conversions') }} AS cpa,

        -- ROAS（广告支出回报率）
        {{ safe_divide('attributed_revenue', 'ad_spend') }} AS roas,

        -- CPI（每次展示成本）
        {{ safe_divide('ad_spend', 'impressions') }} * 1000 AS cpm

    FROM daily_metrics
)

SELECT
    *,
    CURRENT_TIMESTAMP() AS dbt_updated_at,
    '{{ invocation_id }}' AS dbt_job_id
FROM with_metrics
```

### 19.10 Snapshots 缓慢变化维度

```sql
-- snapshots/products_snapshot.sql

{% snapshot products_snapshot %}

{{
    config(
        target_schema='snapshots',
        unique_key='product_id',
        strategy='check',
        check_cols=[
            'product_name',
            'category',
            'unit_price',
            'cost',
            'supplier_id',
            'is_active'
        ],
        hard_deletes='invalidate',   -- 检测到物理删除时标记为失效
        tags=['snapshot', 'products']
    )
}}

SELECT
    product_id,
    product_name,
    category,
    unit_price,
    cost,
    supplier_id,
    is_active,
    CURRENT_TIMESTAMP() AS dbt_updated_at
FROM {{ source('erp', 'products') }}

{% endsnapshot %}
```

```sql
-- snapshots/customers_snapshot.sql

{% snapshot customers_snapshot %}

{{
    config(
        target_schema='snapshots',
        unique_key='customer_id',
        strategy='check',
        check_cols=[
            'industry',
            'annual_revenue',
            'customer_tier',
            'billing_country',
            'is_active'
        ],
        hard_deletes='invalidate',
        tags=['snapshot', 'customers']
    )
}}

SELECT * FROM {{ ref('stg_crm__customers') }}

{% endsnapshot %}
```

### 19.11 测试体系

```yaml
# models/marts/finance/schema.yml

version: 2

models:
  - name: fct_orders
    description: |
      ## Order Fact Table
      Core fact table for the entire analytics platform.

      ### Granularity
      One row per order. Each order links to a single customer and a date.

      ### Key Business Rules
      1. Cancelled and returned orders are kept in the table but flagged
      2. Revenue calculations should exclude cancelled/returned orders
      3. Currency conversion to USD happens upstream in staging

    config:
      meta:
        owner: "Data Engineering - Finance Pod"
        sla: "Daily by 6:00 AM UTC"
        pii_fields: ["customer_name", "billing_country"]

    columns:
      - name: order_id
        description: "Primary key. Source: ERP orders.order_id"
        tests:
          - not_null
          - unique
          - dbt_expectations.expect_column_values_to_be_of_type:
              column_type: VARCHAR

      - name: customer_id
        description: "Foreign key to dim_customers"
        tests:
          - not_null
          - relationships:
              to: ref('dim_customers')
              field: customer_id
              severity: error

      - name: order_date
        description: "Order placement date in UTC"
        tests:
          - not_null
          - dbt_expectations.expect_column_values_to_be_between:
              min_value: "2020-01-01"
              max_value: "2030-12-31"
              row_condition: "order_id IS NOT NULL"

      - name: order_total
        description: "Total order amount including tax and shipping"
        tests:
          - not_null
          - dbt_expectations.expect_column_values_to_be_between:
              min_value: 0
              max_value: 1000000
              severity: warn

      - name: net_revenue
        description: "Net revenue = total - tax - shipping + discounts"
        tests:
          - not_null
          - positive_values

      - name: order_status
        tests:
          - not_null
          - accepted_values:
              values: ['pending','confirmed','shipped','delivered','cancelled','returned']
              quote: true

      - name: amounts_match
        description: "TRUE when order_total matches sum of line items"
        tests:
          - not_null

    # 模型级别的额外测试
    tests:
      - dbt_utils.unique_combination_of_columns:
          combination_of_columns:
            - order_id
            - order_date
      - dbt_utils.equal_rowcount:
          compare_model: ref('stg_erp__orders')

  - name: fct_revenue_daily
    description: |
      ## Daily Revenue Aggregation
      Pre-aggregated daily revenue metrics by customer tier and country.

    columns:
      - name: date_day
        tests:
          - not_null

      - name: net_revenue
        tests:
          - not_null
          - positive_values

      - name: total_orders
        tests:
          - not_null
          - dbt_expectations.expect_column_values_to_be_between:
              min_value: 0

      - name: avg_order_value
        tests:
          - dbt_expectations.expect_column_values_to_be_between:
              min_value: 0
              max_value: 50000

      - name: revenue_7d_moving_avg
        tests:
          - dbt_expectations.expect_column_values_to_be_between:
              min_value: 0
```

```sql
-- tests/business_rules/assert_revenue_matches_payments.sql

{#
  业务规则验证：每日收入汇总是否与支付系统记录匹配。
  允许 ±1% 的差异（四舍五入、时区差异等）。
#}

WITH revenue_per_day AS (
    SELECT
        order_date AS date_day,
        SUM(net_revenue) AS total_revenue
    FROM {{ ref('fct_orders') }}
    WHERE order_status NOT IN ('cancelled', 'returned')
    GROUP BY order_date
),

payments_per_day AS (
    SELECT
        DATE(payment_timestamp) AS date_day,
        SUM(amount) AS total_payments
    FROM {{ source('erp', 'payment_transactions') }}
    WHERE status = 'settled'
    GROUP BY DATE(payment_timestamp)
)

SELECT
    r.date_day,
    r.total_revenue,
    p.total_payments,
    ABS(r.total_revenue - p.total_payments) AS difference,
    ABS(r.total_revenue - p.total_payments) / NULLIF(r.total_revenue, 0) AS pct_diff
FROM revenue_per_day AS r
LEFT JOIN payments_per_day AS p
    ON r.date_day = p.date_day
WHERE ABS(r.total_revenue - p.total_payments) / NULLIF(r.total_revenue, 0) > 0.01
```

### 19.12 CI/CD 与生产部署

**GitHub Actions CI 流水线**：

```yaml
# .github/workflows/ci_dbt.yml

name: dbt CI

on:
  pull_request:
    branches: [main]
    paths:
      - 'models/**'
      - 'macros/**'
      - 'snapshots/**'
      - 'tests/**'
      - 'dbt_project.yml'
      - 'packages.yml'

env:
  DBT_PROFILES_DIR: ./profiles
  DBT_TARGET: ci

jobs:
  dbt-test:
    runs-on: ubuntu-latest
    timeout-minutes: 60

    steps:
      - uses: actions/checkout@v4

      - name: Setup Python
        uses: actions/setup-python@v5
        with:
          python-version: '3.11'

      - name: Install dbt
        run: |
          pip install dbt-snowflake==1.7.0
          dbt deps

      - name: Setup Snowflake credentials
        env:
          SNOWFLAKE_PRIVATE_KEY: ${{ secrets.SNOWFLAKE_PRIVATE_KEY }}
        run: |
          echo "$SNOWFLAKE_PRIVATE_KEY" > /tmp/snowflake_key.p8

      - name: Run dbt in CI environment
        run: |
          # 使用零拷贝克隆创建 CI Schema
          # 编译检查所有模型
          dbt compile --target ci

          # 只运行修改过的模型及其下游
          dbt run --target ci --select state:modified+

          # 运行测试
          dbt test --target ci --select state:modified+
        env:
          DBT_ACCOUNT: ${{ secrets.SNOWFLAKE_ACCOUNT }}
          DBT_USER: ${{ secrets.SNOWFLAKE_USER }}
          DBT_ROLE: ${{ secrets.SNOWFLAKE_CI_ROLE }}
```

**生产部署脚本**：

```bash
#!/bin/bash
# scripts/prod_deploy.sh

set -e  # 遇到任何错误立即退出

echo "========================================"
echo "  dbt Production Deployment"
echo "  Started at: $(date)"
echo "========================================"

# 设置环境
export DBT_PROFILES_DIR=./profiles
export DBT_TARGET=prod

# 1. 安装依赖
echo "[1/7] Installing dbt packages..."
dbt deps

# 2. 编译检查
echo "[2/7] Compiling all models..."
dbt compile --target prod

# 3. 运行 Staging 层
echo "[3/7] Building staging layer..."
dbt run --select staging --target prod

# 4. 运行 Intermediate 层
echo "[4/7] Building intermediate layer..."
dbt run --select intermediate --target prod

# 5. 运行 Marts 层
echo "[5/7] Building marts layer..."
dbt run --select marts --target prod

# 6. 执行 Snapshots
echo "[6/7] Running snapshots..."
dbt snapshot --target prod

# 7. 运行测试
echo "[7/7] Running tests..."
dbt test --target prod --select marts

# 8. 生成文档
echo "Generating documentation..."
dbt docs generate --target prod

# 部署状态通知
if [ $? -eq 0 ]; then
    echo "========================================"
    echo "  Deployment SUCCESSFUL"
    echo "  Completed at: $(date)"
    echo "========================================"
else
    echo "========================================"
    echo "  Deployment FAILED"
    echo "  Completed at: $(date)"
    echo "========================================"
    exit 1
fi
```

### 19.13 文档与数据血缘

```bash
# 生成完整的项目文档（包含所有模型描述、列注释、测试、血缘）
dbt docs generate

# 在 dbt Cloud 中可以直接查看交互式 DAG 血缘图
# 或者本地启动文档服务器
dbt docs serve --port 8080
```

**数据血缘示意图（通过 dbt docs 自动生成）**：

```
raw_data.salesforce.accounts ──────► stg_crm__customers ──────► dim_customers
                                                                      │
raw_data.erp.orders ──────────────► stg_erp__orders ───┐              │
                                                        ├──► int_order_enriched ──► fct_orders
raw_data.erp.order_items ─────────► stg_erp__order_items┘         │
                                                        │              │
raw_data.erp.products ────────────► stg_erp__products ──┘              ▼
                                                                  fct_revenue_daily
raw_data.events.page_views ───────► stg_events__page_views ──┐
                                                               ├──► int_customer_sessions
raw_data.events.transactions ─────► stg_events__transactions ─┘
```

---

## 附录：常用资源

### 推荐学习路径

1. **入门**：dbt 官方文档 [docs.getdbt.com](https://docs.getdbt.com)
2. **实践**：dbt Learn 免费课程
3. **进阶**：《dbt 最佳实践指南》（dbt 官方博客）
4. **社区**：dbt Community Slack（#snowflake 频道）
5. **高级**：dbt Coalesce 会议演讲视频

### Snowflake + dbt 优化清单

| 方向 | 实践 |
|------|------|
| **成本控制** | 使用 `query_tag` 追踪成本；dev 环境限制数据扫描量；关闭 `send_anonymous_usage_stats` |
| **权限安全** | 使用 Key-Pair 认证；最小权限原则；`copy_grants: true` 保留权限 |
| **性能优化** | 合理使用 `cluster_by`；大表使用 `incremental` 策略；`transient` 表减少存储成本 |
| **代码质量** | 模型按层组织；命名规范统一；每个模型包含审计列；关键模型配置 `store_failures` |
| **协作规范** | PR 模板包含测试结果截图；生产部署使用 CI/CD；文档即代码原则 |

---

> **文档版本**：v1.0 | **最后更新**：2024年 | **适用 dbt 版本**：1.6 ~ 1.8 | **适用 Snowflake 适配器**：dbt-snowflake 1.6+
