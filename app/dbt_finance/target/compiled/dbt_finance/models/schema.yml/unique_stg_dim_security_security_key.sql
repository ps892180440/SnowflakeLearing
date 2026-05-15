
    
    

select
    security_key as unique_field,
    count(*) as n_records

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_security
where security_key is not null
group by security_key
having count(*) > 1


