
  create or replace   view TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.stg_raw_fund_nav
  
   as (
    select
    upper(trim(fund_code)) as fund_code,
    nav_date,
    unit_nav,
    accumulated_nav,
    daily_return,
    round(daily_return * 100, 4) as daily_return_pct,
    source_file,
    ingestion_ts
from TEST_SNOWFLAKE_LEANING.SCHM_L_SNOWLEARN_01.RAW_FUND_NAV
where fund_code is not null and nav_date is not null
  );

