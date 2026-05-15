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
