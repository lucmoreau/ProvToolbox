
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

