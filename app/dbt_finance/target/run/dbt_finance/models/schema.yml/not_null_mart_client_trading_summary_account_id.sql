select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select account_id
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_client_trading_summary
where account_id is null



      
    ) dbt_internal_test