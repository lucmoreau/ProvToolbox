
CREATE OR REPLACE FUNCTION count_rows_in_tables(table_names text[])
RETURNS TABLE(table_name text, row_count bigint) AS
$$
DECLARE
tbl text;
BEGIN
    FOREACH tbl IN ARRAY table_names
    LOOP
        RETURN QUERY EXECUTE format(
            'SELECT %L, COUNT(*) FROM %I',
            tbl, tbl
        );
END LOOP;
END;
$$ LANGUAGE plpgsql;


SELECT * FROM count_rows_in_tables(ARRAY['activity', 'agent', 'file_transforming', 'file_init' ]);