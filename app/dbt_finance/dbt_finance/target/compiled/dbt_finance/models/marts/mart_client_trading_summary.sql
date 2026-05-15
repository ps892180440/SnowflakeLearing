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

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.int_trade_enriched
where account_id is not null
group by account_id, client_name, risk_level, account_type