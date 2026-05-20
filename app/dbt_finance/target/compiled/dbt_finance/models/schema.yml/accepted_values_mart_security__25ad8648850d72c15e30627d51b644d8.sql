
    
    

with all_values as (

    select
        price_trend as value_field,
        count(*) as n_records

    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_DEV.mart_security_daily_performance
    group by price_trend

)

select *
from all_values
where value_field not in (
    'STRONG_UP','UP','FLAT','DOWN','STRONG_DOWN'
)


