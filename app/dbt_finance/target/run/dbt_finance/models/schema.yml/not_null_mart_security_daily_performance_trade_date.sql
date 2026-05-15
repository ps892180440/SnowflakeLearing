select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select trade_date
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_security_daily_performance
where trade_date is null



      
    ) dbt_internal_test