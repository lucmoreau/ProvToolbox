CREATE TABLE IF NOT EXISTS metrics
(
    ID        SERIAL,
    artifact  TEXT,
    url       TEXT,
    features  JSON,
    counts    JSON
);


CREATE TABLE IF NOT EXISTS typemap
(
    ID    SERIAL UNIQUE,
    map   JSON,
    set   JSON,
    list  JSON
);

INSERT INTO typemap
(id, map, set, list)
SELECT 1, '{}', '[]', '[]'
ON CONFLICT DO NOTHING;

