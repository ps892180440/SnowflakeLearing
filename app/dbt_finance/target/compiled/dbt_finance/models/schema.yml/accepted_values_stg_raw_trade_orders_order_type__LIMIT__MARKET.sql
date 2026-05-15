
    
    

with all_values as (

    select
        order_type as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_trade_orders
    group by order_type

)

select *
from all_values
where value_field not in (
    'LIMIT','MARKET'
)


