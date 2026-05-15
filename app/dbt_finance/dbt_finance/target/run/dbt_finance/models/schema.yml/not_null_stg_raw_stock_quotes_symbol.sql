select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select symbol
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_stock_quotes
where symbol is null



      
    ) dbt_internal_test