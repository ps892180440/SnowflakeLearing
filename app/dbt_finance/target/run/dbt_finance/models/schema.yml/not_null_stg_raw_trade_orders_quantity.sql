select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select quantity
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_trade_orders
where quantity is null



      
    ) dbt_internal_test