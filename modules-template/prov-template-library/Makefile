

test.node:
	npm test


test.py:
	export PYTHONPATH=target/generated-python:src/main/python; python3 src/test/python/test.py


compile.plead.templates:
	 mvn exec:exec@compile-template-plead

transpile.code:
	 export JAVA_HOME=/usr/local/java/jdk-12.0.1.jdk/Contents/Home/; mvn -f pom12.xml jsweet:jsweet

SCHEMA_FILE=../prov-template/src/main/resources/jsonschema/bindings-schema.json

test.schema:
	@ajv -s $(SCHEMA_FILE) -d $(FILE)

BINDINGS_DIR=src/main/resources/bindings/org/openprovenance/prov/templates

ALL_BINDINGS = $(wildcard $(BINDINGS_DIR)/plead/*.json $(BINDINGS_DIR)/pg/*.json $(BINDINGS_DIR)/ptm/*.json)

test.schema.example:
	$(MAKE) FILE=$(BINDINGS_DIR)/plead/plead-transforming.json test.schema

# test.schema.all to test schema for all bindings in $(ALL_BINDINGS)
test.schema.all:
	@$(foreach FILE, $(ALL_BINDINGS), $(MAKE) FILE=$(FILE) test.schema;)

go:
	echo $(SOURCES)

ALL_TEMPLATE_PROJECTS_DIR=src/main/resources/config
ALL_TEMPLATE_PROJECTS = $(wildcard $(ALL_TEMPLATE_PROJECTS_DIR)/*.json)


test.schema.projects:
	@$(foreach FILE, $(ALL_TEMPLATE_PROJECTS), $(MAKE) FILE=$(FILE) SCHEMA_FILE=../prov-template-compiler/src/main/resources/schema/template-project-schema.json test.schema;)

