
    
    

select
    order_id as unique_field,
    count(*) as n_records

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.stg_raw_trade_orders
where order_id is not null
group by order_id
having count(*) > 1


