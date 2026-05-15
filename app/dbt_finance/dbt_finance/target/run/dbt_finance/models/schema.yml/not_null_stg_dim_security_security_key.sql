select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    



select security_key
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_security
where security_key is null



      
    ) dbt_internal_test