
FILES    := $(shell ls target/*.jsonld)

FILES_TO_CHECK    := $(patsubst %, %.check, $(FILES))

echo:
	echo $(FILES)
	echo $(FILES_TO_CHECK)

JSONLD_SCHEMA=src/main/resources/2024-08-25/jsonldschema.json
JSONLD_CONTEXT=src/main/resources/2024-08-25/jsonldcontext.jsonld

PUBLIC_JSONLD_SCHEMA=https://openprovenance.org/prov-jsonld/schema.json
PUBLIC_JSONLD_CONTEXT=https://openprovenance.org/prov-jsonld/context.jsonld


check.file:
	@ajv -s $(JSONLD_SCHEMA) -d $(FILE)



target/%.jsonld.check: target/%.jsonld
	@-$(MAKE) FILE=$< check.file


go: check.schema.spec check.context.spec $(FILES_TO_CHECK)

check.schema.spec:
	curl -s -o target/public-schema.json $(PUBLIC_JSONLD_SCHEMA)
	diff -q target/public-schema.json $(JSONLD_SCHEMA)
	rm target/public-schema.json


check.context.spec:
	curl -s -o target/public-context.jsonld $(PUBLIC_JSONLD_CONTEXT)
	diff -q target/public-context.jsonld $(JSONLD_CONTEXT)
	rm target/public-context.jsonld