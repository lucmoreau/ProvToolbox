
FILES    := $(shell find target -name '*.jsonld')

FILES_TO_CHECK    := $(patsubst %, %.check, $(FILES))

echo:
	echo $(FILES)
	echo $(FILES_TO_CHECK)

#JSONLD_SCHEMA=src/main/resources/2024-06-11/jsonldschema.json
JSONLD_SCHEMA=src/main/resources/2024-08-25/jsonldschema.json

check.file:
	@ajv -s $(JSONLD_SCHEMA) -d $(FILE)



target/%.jsonld.check: target/%.jsonld
	@-$(MAKE) FILE=$< check.file


go: $(FILES_TO_CHECK)
