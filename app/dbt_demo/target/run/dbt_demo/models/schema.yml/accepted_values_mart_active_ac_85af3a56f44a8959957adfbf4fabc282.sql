select
      count(*) as failures,
      count(*) != 0 as should_warn,
      count(*) != 0 as should_error
    from (
      
    
    

with all_values as (

    select
        account_maturity as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_active_accounts
    group by account_maturity

)

select *
from all_values
where value_field not in (
    'New','Established','Mature'
)



      
    ) dbt_internal_test