

CREATE TABLE IF NOT EXISTS plead_transforming_composite_linker
(
    ID        SERIAL,
    --  The first file resulting from the split
    composite INT,
    --  The composite record
    simple    INT
--  The simple record
);

CREATE TABLE IF NOT EXISTS __PROV_DERIVATION
(
    ID SERIAL,
    generatedEntity INT,
    generatedEntity_rel TEXT,
    usedEntity INT,
    usedEntity_rel TEXT,
    activity INT,
    activity_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);

CREATE TABLE IF NOT EXISTS __PROV_ASSOCIATION
(
    ID SERIAL,
    activity INT,
    activity_rel TEXT,
    agent INT,
    agent_rel TEXT,
    plan INT,
    plan_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);

CREATE TABLE IF NOT EXISTS __PROV_GENERATION
(
    ID SERIAL,
    entity INT,
    entity_rel TEXT,
    activity INT,
    activity_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);

CREATE TABLE IF NOT EXISTS __PROV_USAGE
(
    ID SERIAL,
    activity INT,
    activity_rel TEXT,
    entity INT,
    entity_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);

CREATE TABLE IF NOT EXISTS __PROV_DELEGATION
(
    ID SERIAL,
    delegate INT,
    delegate_rel TEXT,
    responsible INT,
    responsible_rel TEXT,
    activity INT,
    activity_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);

CREATE TABLE IF NOT EXISTS __PROV_SPECIALIZATION
(
    ID SERIAL,
    specificEntity INT,
    specificEntity_rel TEXT,
    generalEntity INT,
    generalEntity_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);


CREATE TABLE IF NOT EXISTS __PROV_MEMBERSHIP
(
    ID SERIAL,
    collection INT,
    collection_rel TEXT,
    entity INT,
    entity_rel TEXT,
    template_id INT,
    template TEXT,
    rel TEXT
);


CREATE TABLE if not exists predecessor_table  (template text, output text, input text);

truncate predecessor_table;

insert into predecessor_table (template, output, input)
values
    ('plead_validating', 'score', 'testing_dataset'),
    ('plead_filtering', 'filtered_file', 'file'),
    ('plead_approving', 'approval_record', 'pipeline'),
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


