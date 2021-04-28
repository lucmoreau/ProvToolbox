
DROP DATABASE  templatedb;

CREATE DATABASE templatedb
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'C'
       LC_CTYPE = 'C'
       CONNECTION LIMIT = -1;

\c wamdb

CREATE TABLE template_block
(
    operation text,
    operation_type text,
    agent text,
    consumed1 text,
    consumed_value1 text,
    consumed2 text,
    consumed_value2 int,
    produced text,
    produced_type text,
    produced_value int
);

