select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    

with all_values as (

    select
        risk_level as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_account
    group by risk_level

)

select *
from all_values
where value_field not in (
    'LOW','MEDIUM','HIGH'
)



      
    ) dbt_internal_test