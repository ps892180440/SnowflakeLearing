select
    upper(trim(fund_code)) as fund_code,
    nav_date,
    unit_nav,
    accumulated_nav,
    daily_return,
    round(daily_return * 100, 4) as daily_return_pct,
    source_file,
    ingestion_ts
from {{ source('landing', 'RAW_FUND_NAV') }}
where fund_code is not null and nav_date is not null
