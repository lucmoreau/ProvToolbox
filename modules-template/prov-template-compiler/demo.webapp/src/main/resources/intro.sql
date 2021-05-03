-- Database: demodb

DROP DATABASE demodb;

CREATE DATABASE demodb
    WITH OWNER = postgres
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    LC_COLLATE = 'C'
    LC_CTYPE = 'C'
    CONNECTION LIMIT = -1;

\c demodb


