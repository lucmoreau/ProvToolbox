#!/bin/sh

# Creates the service's database account (TPM_DB_USER, TPM_DB_PASS) and its
# event table. The postgres image runs this once, on the first start of an
# empty data folder. It can be run again on an existing database, for instance
# after a failed first start or to apply a new TPL_DB_PASS:
#   docker exec tpl-postgres16-1 sh /docker-entrypoint-initdb.d/01-init.sh
# The values are read by psql from the environment (\getenv) and quoted by it,
# so they appear neither on a command line nor in the log.

set -e

echo "Database $TPM_DB_NAME: account $TPM_DB_USER"

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$TPM_DB_NAME" <<-'EOSQL'
  \getenv db TPM_DB_NAME
  \getenv user TPM_DB_USER
  \getenv pass TPM_DB_PASS
  SELECT format('CREATE ROLE %I LOGIN', :'user')
   WHERE NOT EXISTS (SELECT FROM pg_roles WHERE rolname = :'user') \gexec
  ALTER ROLE :"user" WITH LOGIN PASSWORD :'pass';
  GRANT ALL PRIVILEGES ON DATABASE :"db" TO :"user";
  ALTER DATABASE :"db" OWNER TO :"user";

  \connect :db :user
  GRANT ALL ON SCHEMA public TO :"user";
  CREATE TABLE IF NOT EXISTS event (
    id CHAR(26) NOT NULL CHECK (CHAR_LENGTH(id) = 26) PRIMARY KEY,
    aggregate_id CHAR(26) NOT NULL CHECK (CHAR_LENGTH(aggregate_id) = 26),
    event_data JSON NOT NULL,
    version INT,
    UNIQUE(aggregate_id, version)
  );
  CREATE INDEX IF NOT EXISTS idx_event_aggregate_id ON event (aggregate_id);
EOSQL

echo "Database $TPM_DB_NAME ready for $TPM_DB_USER"
