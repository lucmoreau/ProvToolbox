

CREATE TABLE IF NOT EXISTS plead_transforming_composite_linker
(
    ID        SERIAL,
    --  The first file resulting from the split
    composite INT,
    --  The composite record
    simple    INT
--  The simple record
);

CREATE TABLE if not exists predecessor_table  (template text, output text, input text);

truncate predecessor_table;

insert into predecessor_table (template, output, input)
values
    ('plead_validating', 'score', 'testing_dataset'),
    ('plead_filtering', 'filtered_file', 'file'),
    ('plead_approving', 'approval_record,', 'pipeline'),
    ('plead_approving', 'approval_record', 'score'),
    ('plead_approving', 'approved_pipeline', 'pipeline'),
    ('plead_approving', 'approved_pipeline', 'score'),
    ('plead_splitting', 'split_file1', 'file'),
    ('plead_splitting', 'split_file2', 'file'),
    ('plead_transforming', 'transformed_file', 'file'),
    ('plead_training', 'pipeline', 'training_dataset')
;

/*

select * from backwardTraversal(8, 'plead_approving', 'pipeline')
union
select * from backwardTraversal(18, 'plead_training', 'training_dataset')


select edges.*, predecessor_table.input
from
(select * from backwardTraversal(8, 'plead_approving', 'pipeline')
union
select * from backwardTraversal(18, 'plead_training', 'training_dataset')) as edges
left join predecessor_table on edges.out_template=predecessor_table.template
and edges.out_property=predecessor_table.output

 */

/*

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

 */

/*
CREATE OR REPLACE FUNCTION search_records(from_date timestamptz)
    RETURNS table(key integer,
                  table_name text,
                  created_at timestamptz)
AS $$
SELECT
    COALESCE(plead_transforming.transforming, plead_filtering.filtering) AS key,
    CASE
        WHEN plead_transforming.transforming IS NOT NULL THEN 'plead_transforming'
        WHEN plead_filtering.filtering IS NOT NULL THEN 'plead_filtering'
        END AS table_name,
    created_at
FROM
    activity
        LEFT JOIN
    plead_transforming ON plead_transforming.transforming = activity.ID
        LEFT JOIN
    plead_filtering ON plead_filtering.filtering = activity.ID
WHERE
    activity.created_at > from_date
  AND ((plead_transforming.transforming is not NULL) OR (plead_filtering.filtering is not NULL))
ORDER BY created_at DESC
limit 50
$$ language sql;

select * from search_records('2024-04-15')

*/




