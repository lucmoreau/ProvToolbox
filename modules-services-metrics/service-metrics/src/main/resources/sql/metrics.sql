CREATE TABLE IF NOT EXISTS metrics
(
    ID        SERIAL,
    artifact  TEXT,
    url       TEXT,
    hash      JSON,
    features  JSON,
    counts    JSON,
    validity  JSON,
    traffic   JSON,
    created_at timestamp with time zone NOT NULL DEFAULT NOW()
);


CREATE TABLE IF NOT EXISTS typemap
(
    ID    SERIAL UNIQUE,
    map   JSON,
    set   JSON,
    list  JSON,
    created_at timestamp with time zone NOT NULL DEFAULT NOW()
);

-- Insert a default typemap if typemap is empty
INSERT INTO typemap (map, set, list)
SELECT '{}'::json, '[]'::json, '[]'::json
WHERE NOT EXISTS (SELECT * FROM typemap);



