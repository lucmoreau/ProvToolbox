
FILES    := $(shell find target -name '*.jsonld11')

FILES_TO_CHECK    := $(patsubst %, %.check, $(FILES))



echo:
	echo $(FILES)
	echo $(FILES_TO_CHECK)

check:
	ajv -s src/main/resources/jsonldschema.json -d target/pc1-full.json

check.file:
	ajv -s src/main/resources/jsonldschema.json -d $(FILE)



target/%.jsonld11.check: target/%.jsonld11
	-$(MAKE) FILE=$< check.file


go: $(FILES_TO_CHECK)
