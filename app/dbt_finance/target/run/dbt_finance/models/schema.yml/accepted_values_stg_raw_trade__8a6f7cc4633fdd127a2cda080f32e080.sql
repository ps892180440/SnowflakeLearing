select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    

with all_values as (

    select
        status as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_trade_orders
    group by status

)

select *
from all_values
where value_field not in (
    'FILLED','PARTIAL','CANCELLED'
)



      
    ) dbt_internal_test