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
        when (sum(case when t.direction = 'BUY' then t.computed_total_amount else 0 end)
              - sum(case when t.direction = 'SELL' then t.computed_total_amount else 0 end)) > 50000
        then 'HIGH'
        when (sum(case when t.direction = 'BUY' then t.computed_total_amount else 0 end)
              - sum(case when t.direction = 'SELL' then t.computed_total_amount else 0 end)) > 20000
        then 'MEDIUM'
        else 'LOW'
    end as exposure_level,

    case
        when t.risk_level = 'HIGH'
             and (sum(case when t.direction = 'BUY' then t.computed_total_amount else 0 end)
                  - sum(case when t.direction = 'SELL' then t.computed_total_amount else 0 end)) > 50000
        then true
        else false
    end as risk_alert_flag,

    count(distinct t.order_id) as trade_count,
    max(t.order_date) as last_trade_date

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.int_trade_enriched t
where t.status in ('FILLED', 'PARTIAL')
group by
    t.account_id, t.client_name, t.risk_level, t.account_type,
    t.sector, t.symbol, t.security_name