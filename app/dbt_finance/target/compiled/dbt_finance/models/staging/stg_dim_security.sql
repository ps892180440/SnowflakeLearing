select
    security_key,
    upper(trim(symbol)) as symbol,
    security_name,
    exchange,
    sector,
    industry,
    list_date,
    is_active,
    datediff('year', list_date, current_date()) as years_since_listing,
    etl_updated_at
from TEST_SNOWFLAKE_LEANING.SCHM_F_SNOWLEARN_01.DIM_SECURITY