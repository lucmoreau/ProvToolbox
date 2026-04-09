

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
    ('plead_training', 'pipeline', 'training_dataset'),

    ('transporting', 'item1', 'item0'),
    ('weighing', 'item1', 'item0'),
    ('handover', 'item1', 'item0')
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
