
  create or replace   view TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_trade_orders
  
   as (
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
from TEST_SNOWFLAKE_LEANING.SCHM_L_SNOWLEARN_01.RAW_TRADE_ORDERS
where order_id is not null
  );

