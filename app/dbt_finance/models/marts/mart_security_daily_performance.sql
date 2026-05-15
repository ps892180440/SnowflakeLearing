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
