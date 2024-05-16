#!/bin/bash

#From
#https://graspingtech.com/docker-compose-postgresql/

set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
  CREATE USER $TPM_DB_USER WITH PASSWORD '$TPM_DB_PASS';
  CREATE DATABASE $TPM_DB_NAME;
  GRANT ALL PRIVILEGES ON DATABASE $TPM_DB_NAME TO $TPM_DB_USER;
  \connect $TPM_DB_NAME $TPM_DB_USER
  BEGIN;
    CREATE TABLE IF NOT EXISTS event (
	  id CHAR(26) NOT NULL CHECK (CHAR_LENGTH(id) = 26) PRIMARY KEY,
	  aggregate_id CHAR(26) NOT NULL CHECK (CHAR_LENGTH(aggregate_id) = 26),
	  event_data JSON NOT NULL,
	  version INT,
	  UNIQUE(aggregate_id, version)
	);
	CREATE INDEX idx_event_aggregate_id ON event (aggregate_id);
  COMMIT;
EOSQL
echo "Database $TPM_DB_NAME created successfully"
echo "User $TPM_DB_USER"
echo "Password $TPM_DB_PASS"
