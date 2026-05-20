
    
    

with all_values as (

    select
        volume_tier as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.mart_security_daily_performance
    group by volume_tier

)

select *
from all_values
where value_field not in (
    'VERY_HIGH','HIGH','MEDIUM','LOW'
)


