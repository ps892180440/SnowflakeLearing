select
    account_key,
    account_id,
    client_name,
    risk_level,
    account_type,
    open_date,
    is_active,
    etl_updated_at
from {{ source('snowlearn', 'DIM_ACCOUNT') }}
