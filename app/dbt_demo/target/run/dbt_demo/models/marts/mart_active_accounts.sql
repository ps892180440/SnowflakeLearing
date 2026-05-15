
  
    

        create or replace transient table TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.mart_active_accounts
         as
        (select
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    datediff('day', open_date, current_date()) as account_age_days,
    case
        when datediff('day', open_date, current_date()) > 365 then 'Mature'
        when datediff('day', open_date, current_date()) > 90 then 'Established'
        else 'New'
    end as account_maturity
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_account
where is_active = true
        );
      
  