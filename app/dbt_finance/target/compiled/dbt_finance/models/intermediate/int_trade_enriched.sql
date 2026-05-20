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

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.stg_raw_trade_orders t
left join TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.stg_dim_account a
    on t.account_id = a.account_id
left join TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.stg_dim_security s
    on t.symbol = s.symbol