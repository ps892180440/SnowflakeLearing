
    
    

with all_values as (

    select
        exposure_level as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_risk_exposure_report
    group by exposure_level

)

select *
from all_values
where value_field not in (
    'HIGH','MEDIUM','LOW'
)


