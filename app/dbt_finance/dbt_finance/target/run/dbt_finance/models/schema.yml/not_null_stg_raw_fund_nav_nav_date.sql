select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select nav_date
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_fund_nav
where nav_date is null



      
    ) dbt_internal_test