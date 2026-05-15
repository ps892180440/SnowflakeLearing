
    
    

select
    account_id as unique_field,
    count(*) as n_records

from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_account
where account_id is not null
group by account_id
having count(*) > 1


