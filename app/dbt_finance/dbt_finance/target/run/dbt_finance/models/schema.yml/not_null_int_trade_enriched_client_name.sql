select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select client_name
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.int_trade_enriched
where client_name is null



      
    ) dbt_internal_test