select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    

with all_values as (

    select
        volume_tier as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_security_daily_performance
    group by volume_tier

)

select *
from all_values
where value_field not in (
    'VERY_HIGH','HIGH','MEDIUM','LOW'
)



      
    ) dbt_internal_test