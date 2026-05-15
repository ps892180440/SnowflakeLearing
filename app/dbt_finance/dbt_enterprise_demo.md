# dbt 企业级案例 — 证券交易平台数据仓库

## 一、项目概述

### 1.1 业务背景

本项目模拟一个**证券交易平台**的数据仓库建设。数据库 `TEST_SNOWFLAKE_LEANING` 中已有三层数据：

| Schema | 层级 | 说明 |
|--------|------|------|
| `SCHM_L_SNOWLEARN_01` | Landing（着陆层） | 从外部系统（S3）原始导入的数据 |
| `SCHM_F_SNOWLEARN_01` | Foundation（基础层） | 清洗后的维度表和事实表 |
| `SCHM_M_SNOWLEARN_01` | Mart（集市层） | 面向业务的报表 |

### 1.2 项目目标

使用 dbt 构建 **四层数据管道**（Staging → Intermediate → Marts），实现：
1. **数据清洗标准化**：字段大小写统一、空值过滤、去重处理
2. **维度关联**：将交易订单与客户信息、证券信息进行关联
3. **业务分析**：生成客户交易汇总、证券行情分析、风险敞口报告

### 1.3 数据流向全景图

```
Landing 层 (原始数据)           Staging 层 (清洗)               Intermediate 层 (关联)          Marts 层 (业务)
─────────────────────         ──────────────────              ──────────────────────          ──────────────────
RAW_TRADE_ORDERS  ────→  stg_raw_trade_orders  ──────┐
                                                      │
DIM_ACCOUNT       ────→  stg_dim_account (去重) ─────┼──→  int_trade_enriched  ──┬──→ mart_client_trading_summary
                                                      │                          │
DIM_SECURITY      ────→  stg_dim_security  ──────────┘                          ├──→ mart_risk_exposure_report
                                                                                 │
RAW_STOCK_QUOTES  ────→  stg_raw_stock_quotes  ──────────────────────────────────┼──→ mart_security_daily_performance
                                                                                 │
RAW_FUND_NAV      ────→  stg_raw_fund_nav  ─────────────────────────────────────┘  (预留扩展)
```

---

## 二、源表结构

### 2.1 Landing 层 — 原始数据

#### RAW_TRADE_ORDERS（原始交易订单）

| 字段 | 类型 | 说明 |
|------|------|------|
| ORDER_ID | VARCHAR(50) | 订单唯一标识 |
| ACCOUNT_ID | VARCHAR(30) | 客户账户ID |
| SYMBOL | VARCHAR(20) | 证券代码 |
| ORDER_TYPE | VARCHAR(10) | 订单类型：LIMIT（限价）/ MARKET（市价） |
| DIRECTION | VARCHAR(4) | 方向：BUY（买入）/ SELL（卖出） |
| QUANTITY | NUMBER(18,4) | 数量（股数） |
| PRICE | NUMBER(18,4) | 价格 |
| ORDER_TIME | TIMESTAMP | 下单时间 |
| STATUS | VARCHAR(20) | 状态：FILLED / PARTIAL / CANCELLED |
| SOURCE_FILE | VARCHAR(500) | S3 源文件路径 |
| INGESTION_TS | TIMESTAMP | 数据摄入时间戳 |

#### RAW_STOCK_QUOTES（原始股票行情）

| 字段 | 类型 | 说明 |
|------|------|------|
| SYMBOL | VARCHAR(20) | 证券代码 |
| TRADE_DATE | DATE | 交易日期 |
| OPEN_PRICE | NUMBER(18,4) | 开盘价 |
| HIGH_PRICE | NUMBER(18,4) | 最高价 |
| LOW_PRICE | NUMBER(18,4) | 最低价 |
| CLOSE_PRICE | NUMBER(18,4) | 收盘价 |
| VOLUME | NUMBER(20,0) | 成交量 |

#### RAW_FUND_NAV（原始基金净值）

| 字段 | 类型 | 说明 |
|------|------|------|
| FUND_CODE | VARCHAR(20) | 基金代码 |
| NAV_DATE | DATE | 净值日期 |
| UNIT_NAV | NUMBER(18,6) | 单位净值 |
| ACCUMULATED_NAV | NUMBER(18,6) | 累计净值 |
| DAILY_RETURN | NUMBER(10,6) | 日收益率 |

### 2.2 Foundation 层 — 维度表

#### DIM_ACCOUNT（账户维度）

| 字段 | 类型 | 说明 |
|------|------|------|
| ACCOUNT_KEY | NUMBER(38,0) | 代理键（自增） |
| ACCOUNT_ID | VARCHAR(30) | 业务账户ID |
| CLIENT_NAME | VARCHAR(200) | 客户名称 |
| RISK_LEVEL | VARCHAR(20) | 风险等级：LOW / MEDIUM / HIGH |
| ACCOUNT_TYPE | VARCHAR(50) | 账户类型：INDIVIDUAL / INSTITUTIONAL |
| OPEN_DATE | DATE | 开户日期 |
| IS_ACTIVE | BOOLEAN | 是否活跃 |

#### DIM_SECURITY（证券维度）

| 字段 | 类型 | 说明 |
|------|------|------|
| SECURITY_KEY | NUMBER(38,0) | 代理键（自增） |
| SYMBOL | VARCHAR(20) | 证券代码 |
| SECURITY_NAME | VARCHAR(200) | 证券全称 |
| EXCHANGE | VARCHAR(50) | 交易所 |
| SECTOR | VARCHAR(100) | 行业板块 |
| INDUSTRY | VARCHAR(100) | 细分行业 |
| LIST_DATE | DATE | 上市日期 |
| IS_ACTIVE | BOOLEAN | 是否在市 |

---

## 三、项目文件结构

```
dbt_finance/
├── dbt_project.yml                          # 项目总配置
├── profiles.yml                              # Snowflake 连接配置
├── models/
│   ├── sources.yml                           # 数据源声明（5 张源表）
│   ├── schema.yml                            # 数据测试定义（33 个测试）
│   ├── staging/                              # Staging 层（5 个 view）
│   │   ├── stg_raw_trade_orders.sql
│   │   ├── stg_raw_stock_quotes.sql
│   │   ├── stg_raw_fund_nav.sql
│   │   ├── stg_dim_account.sql
│   │   └── stg_dim_security.sql
│   ├── intermediate/                         # Intermediate 层（1 个 view）
│   │   └── int_trade_enriched.sql
│   └── marts/                                # Marts 层（3 个 table）
│       ├── mart_client_trading_summary.sql
│       ├── mart_security_daily_performance.sql
│       └── mart_risk_exposure_report.sql
└── logs/
    └── dbt.log                               # 运行日志（自动生成）
```

---

## 四、配置文件详解

### 4.1 dbt_project.yml

**中文说明：** 项目总配置文件，定义项目名称、文件路径，以及各层模型的默认物化策略。

```yaml
name: 'dbt_finance'
version: '1.0.0'
profile: 'dbt_finance'

model-paths: ["models"]
test-paths: ["tests"]
seed-paths: ["seeds"]

models:
  dbt_finance:
    staging:
      +materialized: view          # staging 和 intermediate 用 view（轻量、不占存储）
    intermediate:
      +materialized: view
    marts:
      +materialized: table         # marts 用 table（持久化，查询性能好）
```

**物化策略选择理由：**

| 层级 | 物化方式 | 理由 |
|------|---------|------|
| Staging | view | 只是透传清洗，不需要持久化存储，节省成本 |
| Intermediate | view | 关联逻辑中间层，不直接被终端用户查询 |
| Marts | table | 业务报表层，需要快速查询，数据持久化 |

### 4.2 profiles.yml

**中文说明：** Snowflake 连接配置。在 Workspace 中 `account` 和 `user` 留空，直接使用当前会话身份。

```yaml
dbt_finance:
  target: dev
  outputs:
    dev:
      type: snowflake
      account: ""
      user: ""
      role: "ACCOUNTADMIN"
      database: "TEST_SNOWFLAKE_LEANING"
      warehouse: "COMPUTE_WH"
      schema: "SCHM_F_SNOWLEARN_01"
      threads: 4
```

---

## 五、Staging 模型详解

### 5.1 stg_raw_trade_orders.sql — 交易订单清洗

**物化方式：** view
**中文说明：** 从原始交易订单表 `RAW_TRADE_ORDERS` 中读取数据，做以下清洗处理：
1. 使用 `upper(trim(...))` 统一字段为大写并去除首尾空格（symbol、order_type、direction、status）
2. 计算 `computed_total_amount = quantity × price`（订单总金额）
3. 从 `order_time` 中提取 `order_date`（交易日期，方便按日汇总）
4. 过滤掉 `order_id` 为空的无效记录

```sql
select
    order_id,
    account_id,
    upper(trim(symbol)) as symbol,
    upper(trim(order_type)) as order_type,
    upper(trim(direction)) as direction,
    quantity,
    price,
    round(quantity * price, 4) as computed_total_amount,
    order_time,
    order_time::date as order_date,
    upper(trim(status)) as status,
    source_file,
    ingestion_ts
from {{ source('landing', 'RAW_TRADE_ORDERS') }}
where order_id is not null
```

### 5.2 stg_raw_stock_quotes.sql — 股票行情清洗

**物化方式：** view
**中文说明：** 从原始股票行情表 `RAW_STOCK_QUOTES` 中读取数据，新增三个计算字段：
1. `daily_change`：当日涨跌额（收盘价 - 开盘价）
2. `daily_change_pct`：当日涨跌百分比
3. `intraday_range`：日内波动幅度（最高价 - 最低价）

```sql
select
    upper(trim(symbol)) as symbol,
    trade_date,
    open_price,
    high_price,
    low_price,
    close_price,
    volume,
    close_price - open_price as daily_change,
    case
        when open_price > 0 then round((close_price - open_price) / open_price * 100, 4)
        else 0
    end as daily_change_pct,
    high_price - low_price as intraday_range,
    ingestion_ts
from {{ source('landing', 'RAW_STOCK_QUOTES') }}
where symbol is not null and trade_date is not null
```

### 5.3 stg_raw_fund_nav.sql — 基金净值清洗

**物化方式：** view
**中文说明：** 从原始基金净值表 `RAW_FUND_NAV` 中读取数据，将 `daily_return`（小数形式，如 0.0123）转换为百分比形式 `daily_return_pct`（如 1.23%），方便业务人员理解。

```sql
select
    upper(trim(fund_code)) as fund_code,
    nav_date,
    unit_nav,
    accumulated_nav,
    daily_return,
    round(daily_return * 100, 4) as daily_return_pct,
    source_file,
    ingestion_ts
from {{ source('landing', 'RAW_FUND_NAV') }}
where fund_code is not null and nav_date is not null
```

### 5.4 stg_dim_account.sql — 账户维度清洗（含去重）

**物化方式：** view
**中文说明：** 从账户维度表 `DIM_ACCOUNT` 中读取数据。因为源表中同一个 `account_id` 可能存在多条记录（不同批次导入），这里使用 `row_number()` 窗口函数按 `etl_updated_at` 降序排列，只保留每个账户的最新一条记录。同时计算 `account_age_days`（开户至今天数）。

```sql
with ranked as (
    select
        account_key,
        account_id,
        client_name,
        upper(trim(risk_level)) as risk_level,
        upper(trim(account_type)) as account_type,
        open_date,
        is_active,
        datediff('day', open_date, current_date()) as account_age_days,
        etl_updated_at,
        row_number() over (partition by account_id order by etl_updated_at desc) as rn
    from {{ source('foundation', 'DIM_ACCOUNT') }}
)

select
    account_key,
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    is_active,
    account_age_days,
    etl_updated_at
from ranked
where rn = 1
```

**企业级要点：** 这是一个常见的 **SCD（缓慢变化维度）去重模式**——当源系统可能重复推送数据时，在 staging 层做去重是最佳实践。

### 5.5 stg_dim_security.sql — 证券维度清洗

**物化方式：** view
**中文说明：** 从证券维度表 `DIM_SECURITY` 中读取数据，统一 symbol 为大写，并计算 `years_since_listing`（上市至今年数）。

```sql
select
    security_key,
    upper(trim(symbol)) as symbol,
    security_name,
    exchange,
    sector,
    industry,
    list_date,
    is_active,
    datediff('year', list_date, current_date()) as years_since_listing,
    etl_updated_at
from {{ source('foundation', 'DIM_SECURITY') }}
```

---

## 六、Intermediate 模型详解

### 6.1 int_trade_enriched.sql — 交易明细维度关联

**物化方式：** view
**中文说明：** 这是整个项目的**核心关联层**。将清洗后的交易订单与账户维度、证券维度进行 `LEFT JOIN`，产出一张"宽表"，每条交易记录都附带了完整的客户信息（姓名、风险等级、账户类型）和证券信息（名称、交易所、板块、行业）。下游的三个 Marts 模型都基于这张宽表构建。

```sql
select
    t.order_id,
    t.order_date,
    t.order_time,
    t.direction,
    t.order_type,
    t.status,
    t.quantity,
    t.price,
    t.computed_total_amount,

    a.account_key,
    a.account_id,
    a.client_name,
    a.risk_level,
    a.account_type,
    a.is_active as account_is_active,

    s.security_key,
    s.symbol,
    s.security_name,
    s.exchange,
    s.sector,
    s.industry

from {{ ref('stg_raw_trade_orders') }} t
left join {{ ref('stg_dim_account') }} a
    on t.account_id = a.account_id
left join {{ ref('stg_dim_security') }} s
    on t.symbol = s.symbol
```

**企业级要点：** Intermediate 层的职责是**关联和重塑**，不做业务聚合。这样做的好处是：如果多个 Mart 都需要同样的关联逻辑，只需维护一处。

---

## 七、Marts 模型详解

### 7.1 mart_client_trading_summary.sql — 客户交易汇总报表

**物化方式：** table
**中文说明：** 按客户维度汇总交易活动。为每位客户计算：
1. **订单统计**：总订单数、已成交数、部分成交数、已取消数
2. **成交率**：`fill_rate_pct = 已成交 / 总订单 × 100%`
3. **资金统计**：买入总额、卖出总额、交易总额
4. **股数统计**：买入总股数、卖出总股数
5. **多样性**：交易了多少只不同证券、涉及多少个板块
6. **活跃度**：首次交易日、最后交易日、活跃交易天数跨度

```sql
select
    account_id,
    client_name,
    risk_level,
    account_type,

    count(distinct order_id) as total_orders,
    count(distinct case when status = 'FILLED' then order_id end) as filled_orders,
    count(distinct case when status = 'PARTIAL' then order_id end) as partial_orders,
    count(distinct case when status = 'CANCELLED' then order_id end) as cancelled_orders,

    round(
        count(distinct case when status = 'FILLED' then order_id end) * 100.0
        / nullif(count(distinct order_id), 0),
    2) as fill_rate_pct,

    sum(case when direction = 'BUY' then computed_total_amount else 0 end) as total_buy_amount,
    sum(case when direction = 'SELL' then computed_total_amount else 0 end) as total_sell_amount,
    sum(computed_total_amount) as total_traded_amount,

    sum(case when direction = 'BUY' then quantity else 0 end) as total_shares_bought,
    sum(case when direction = 'SELL' then quantity else 0 end) as total_shares_sold,

    count(distinct symbol) as distinct_securities_traded,
    count(distinct sector) as distinct_sectors,

    min(order_date) as first_trade_date,
    max(order_date) as last_trade_date,
    datediff('day', min(order_date), max(order_date)) as active_trading_days_span

from {{ ref('int_trade_enriched') }}
where account_id is not null
group by account_id, client_name, risk_level, account_type
```

**业务用途：** 客户经理可以通过此表了解每位客户的交易活跃度、投资偏好和成交质量。

### 7.2 mart_security_daily_performance.sql — 证券每日行情分析

**物化方式：** table
**中文说明：** 将股票行情数据与证券维度关联，并为每条行情记录添加两个业务标签：
1. **price_trend（价格趋势）**：根据日涨跌幅分为 STRONG_UP（>3%）、UP、FLAT、DOWN、STRONG_DOWN（<-3%）
2. **volume_tier（成交量等级）**：根据成交量分为 VERY_HIGH（>5000万）、HIGH（>3000万）、MEDIUM（>1000万）、LOW

```sql
select
    q.symbol,
    s.security_name,
    s.sector,
    s.industry,
    s.exchange,
    q.trade_date,

    q.open_price,
    q.high_price,
    q.low_price,
    q.close_price,
    q.volume,
    q.daily_change,
    q.daily_change_pct,
    q.intraday_range,

    case
        when q.daily_change_pct > 3 then 'STRONG_UP'
        when q.daily_change_pct > 0 then 'UP'
        when q.daily_change_pct = 0 then 'FLAT'
        when q.daily_change_pct > -3 then 'DOWN'
        else 'STRONG_DOWN'
    end as price_trend,

    case
        when q.volume > 50000000 then 'VERY_HIGH'
        when q.volume > 30000000 then 'HIGH'
        when q.volume > 10000000 then 'MEDIUM'
        else 'LOW'
    end as volume_tier

from {{ ref('stg_raw_stock_quotes') }} q
left join {{ ref('stg_dim_security') }} s
    on q.symbol = s.symbol
```

**业务用途：** 交易员和分析师可以快速筛选当日"放量大涨"或"缩量下跌"的证券，辅助投资决策。

### 7.3 mart_risk_exposure_report.sql — 风险敞口报告

**物化方式：** table
**中文说明：** 这是**风控部门**最关心的报表。按客户 × 证券维度计算：
1. **净持仓数量**：`net_position_qty = 买入总量 - 卖出总量`
2. **净风险敞口**：`net_exposure_amount = 买入总额 - 卖出总额`
3. **敞口等级**：>5万为 HIGH，>2万为 MEDIUM，其余为 LOW
4. **风险预警标志**：当高风险客户的单只证券净敞口超过 5 万时，标记 `risk_alert_flag = true`
5. 仅统计已成交（FILLED）和部分成交（PARTIAL）的订单

```sql
select
    t.account_id,
    t.client_name,
    t.risk_level,
    t.account_type,
    t.sector,
    t.symbol,
    t.security_name,

    sum(case when t.direction = 'BUY' then t.quantity else 0 end)
    - sum(case when t.direction = 'SELL' then t.quantity else 0 end) as net_position_qty,

    sum(case when t.direction = 'BUY' then t.computed_total_amount else 0 end)
    - sum(case when t.direction = 'SELL' then t.computed_total_amount else 0 end) as net_exposure_amount,

    case
        when (...) > 50000 then 'HIGH'
        when (...) > 20000 then 'MEDIUM'
        else 'LOW'
    end as exposure_level,

    case
        when t.risk_level = 'HIGH' and (...) > 50000 then true
        else false
    end as risk_alert_flag,

    count(distinct t.order_id) as trade_count,
    max(t.order_date) as last_trade_date

from {{ ref('int_trade_enriched') }} t
where t.status in ('FILLED', 'PARTIAL')
group by ...
```

**业务用途：** 风控部门可以通过 `risk_alert_flag = true` 快速定位需要关注的高风险客户持仓。

---

## 八、数据测试详解

本项目定义了 **33 个数据测试**，覆盖四种测试类型：

### 8.1 测试类型总览

| 测试类型 | 数量 | 说明 |
|----------|------|------|
| `unique` | 7 | 确保字段值唯一，无重复 |
| `not_null` | 16 | 确保字段不为空 |
| `accepted_values` | 9 | 确保字段值在指定范围内 |
| `relationships` | 1 | 确保外键引用完整性 |

### 8.2 测试分布

**Staging 层测试（数据质量守门）：**
- `stg_raw_trade_orders`：order_id 唯一+非空、direction 只能是 BUY/SELL、order_type 只能是 LIMIT/MARKET、status 只能是 FILLED/PARTIAL/CANCELLED、quantity 和 price 非空
- `stg_dim_account`：account_key 唯一+非空、account_id 唯一+非空、risk_level 只能是 LOW/MEDIUM/HIGH
- `stg_dim_security`：security_key 唯一+非空、symbol 唯一+非空
- `stg_raw_stock_quotes`：symbol 和 trade_date 非空
- `stg_raw_fund_nav`：fund_code 和 nav_date 非空

**Intermediate 层测试（关联完整性）：**
- `int_trade_enriched`：order_id 唯一+非空、client_name 非空、symbol 非空且必须在 `stg_dim_security` 中存在（relationships 测试）

**Marts 层测试（业务逻辑正确性）：**
- `mart_client_trading_summary`：account_id 和 fill_rate_pct 非空
- `mart_security_daily_performance`：symbol 和 trade_date 非空、price_trend 和 volume_tier 值域校验
- `mart_risk_exposure_report`：account_id 非空、exposure_level 值域校验

---

## 九、执行命令与结果

### 9.1 编译验证

```bash
dbt compile --project-dir /app/dbt_finance
```

**结果：** Found 9 models, 33 data tests, 5 sources — 编译通过

### 9.2 运行模型

```bash
dbt run --project-dir /app/dbt_finance
```

**执行顺序与结果：**（dbt 自动按依赖关系确定顺序）

| 顺序 | 模型 | 类型 | 结果 | 耗时 |
|------|------|------|------|------|
| 1 | stg_dim_account | view | SUCCESS | 0.80s |
| 2 | stg_dim_security | view | SUCCESS | 0.80s |
| 3 | stg_raw_fund_nav | view | SUCCESS | 0.81s |
| 4 | stg_raw_stock_quotes | view | SUCCESS | 0.80s |
| 5 | stg_raw_trade_orders | view | SUCCESS | 0.77s |
| 6 | mart_security_daily_performance | table | SUCCESS | 1.83s |
| 7 | int_trade_enriched | view | SUCCESS | 0.94s |
| 8 | mart_client_trading_summary | table | SUCCESS | 1.70s |
| 9 | mart_risk_exposure_report | table | SUCCESS | 1.46s |

**总计：PASS=9, ERROR=0**

### 9.3 运行测试

```bash
dbt test --project-dir /app/dbt_finance
```

**结果：PASS=33, WARN=0, ERROR=0 — 全部通过**

---

## 十、生成的 Snowflake 对象

| 对象名 | 类型 | 完整路径 |
|--------|------|----------|
| STG_RAW_TRADE_ORDERS | VIEW | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_RAW_TRADE_ORDERS` |
| STG_RAW_STOCK_QUOTES | VIEW | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_RAW_STOCK_QUOTES` |
| STG_RAW_FUND_NAV | VIEW | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_RAW_FUND_NAV` |
| STG_DIM_ACCOUNT | VIEW | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_DIM_ACCOUNT` |
| STG_DIM_SECURITY | VIEW | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.STG_DIM_SECURITY` |
| INT_TRADE_ENRICHED | VIEW | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.INT_TRADE_ENRICHED` |
| MART_CLIENT_TRADING_SUMMARY | TABLE | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.MART_CLIENT_TRADING_SUMMARY` |
| MART_SECURITY_DAILY_PERFORMANCE | TABLE | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.MART_SECURITY_DAILY_PERFORMANCE` |
| MART_RISK_EXPOSURE_REPORT | TABLE | `TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.MART_RISK_EXPOSURE_REPORT` |

---

## 十一、企业级特性总结

| 特性 | 本项目中的体现 |
|------|--------------|
| **四层架构** | Source → Staging → Intermediate → Marts，职责清晰 |
| **数据去重** | `stg_dim_account` 使用 `row_number()` 窗口函数处理重复账户 |
| **标准化清洗** | 所有 staging 模型统一使用 `upper(trim(...))` 规范化字符串 |
| **空值防御** | staging 层 WHERE 过滤无效记录，SQL 中使用 `nullif()` 防除零错误 |
| **维度关联** | intermediate 层将事实与维度 JOIN，避免 marts 层重复关联 |
| **业务标签** | 价格趋势、成交量等级、敞口等级、风险预警等 CASE WHEN 分类 |
| **引用完整性测试** | `relationships` 测试确保交易记录中的 symbol 都能在证券维度表中找到 |
| **多种测试类型** | unique、not_null、accepted_values、relationships 四种测试全覆盖 |
| **物化策略分层** | staging/intermediate 用 view（省存储），marts 用 table（快查询） |
| **ref() 依赖管理** | dbt 自动识别模型间依赖关系，按正确顺序执行 |
