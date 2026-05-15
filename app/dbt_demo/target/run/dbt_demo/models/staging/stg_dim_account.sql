
  create or replace   view TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_dim_account
  
   as (
    select
    account_key,
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    is_active,
    etl_updated_at
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DIM_ACCOUNT
  );

