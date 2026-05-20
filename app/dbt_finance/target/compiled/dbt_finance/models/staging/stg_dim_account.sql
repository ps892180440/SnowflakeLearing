with ranked as (
    select
        account_key,
        account_id,
        client_name,
        upper(trim(risk_level)) as risk_level,
        upper(trim(account_type)) as account_type,
        open_date,
        is_active,
        datediff('day', open_date, current_date()) as account_age_days,
        etl_updated_at,
        row_number() over (partition by account_id order by etl_updated_at desc) as rn
    from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DIM_ACCOUNT
)

select
    account_key,
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    is_active,
    account_age_days,
    etl_updated_at
from ranked
where rn = 1