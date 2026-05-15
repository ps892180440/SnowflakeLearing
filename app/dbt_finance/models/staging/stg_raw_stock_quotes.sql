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
