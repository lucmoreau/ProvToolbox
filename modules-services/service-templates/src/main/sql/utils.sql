

CREATE TABLE IF NOT EXISTS plead_transforming_composite_linker
(
    ID        SERIAL,
    --  The first file resulting from the split
    composite INT,
    --  The composite record
    simple    INT
--  The simple record
);




CREATE OR REPLACE FUNCTION insert_plead_transforming_composite_inserters(_records plead_transforming_type [])
    RETURNS table(ID INT,
                  transformed_file INT,
                  transforming INT,
                  parent INT)
AS $$
WITH
    inserted_consistsOf  AS (SELECT * from insert_plead_transforming_composite_array (_records)),

    the_record AS (INSERT INTO plead_transforming_composite (bean, count, type)
        VALUES (null, (SELECT count(ID) FROM inserted_consistsOf), 'plead_transforming')
        RETURNING id),

    the_product AS (SELECT (SELECT id AS composite FROM the_record), ID as simple
                    FROM inserted_consistsOf),

    tablexxx AS (INSERT INTO plead_transforming_composite_linker(composite,simple)
        SELECT * FROM the_product)

SELECT ID,transformed_file, transforming, (select id as parent from the_record)
FROM inserted_consistsOf

$$ LANGUAGE sql;

select * from insert_plead_transforming_composite_inserters (ARRAY[
    ( -1,'ff'::TEXT,0,null,null,null,1,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type,
    ( -1,'gggg'::TEXT,-1,null,null,null,1,null,null,''::TEXT,NULL,NULL,NULL) :: plead_transforming_type]);







