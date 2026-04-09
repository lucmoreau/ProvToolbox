select *
from activity
WHERE created_at IS NOT NULL ORDER BY created_at DESC LIMIT 5;
