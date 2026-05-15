select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select fill_rate_pct
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_client_trading_summary
where fill_rate_pct is null



      
    ) dbt_internal_test