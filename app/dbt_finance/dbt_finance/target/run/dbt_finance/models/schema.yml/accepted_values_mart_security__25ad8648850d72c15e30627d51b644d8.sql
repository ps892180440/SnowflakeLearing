select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    

with all_values as (

    select
        price_trend as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_security_daily_performance
    group by price_trend

)

select *
from all_values
where value_field not in (
    'STRONG_UP','UP','FLAT','DOWN','STRONG_DOWN'
)



      
    ) dbt_internal_test