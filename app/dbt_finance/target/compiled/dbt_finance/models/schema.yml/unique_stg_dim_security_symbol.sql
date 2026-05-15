
    
    

select
    symbol as unique_field,
    count(*) as n_records

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_security
where symbol is not null
group by symbol
having count(*) > 1


