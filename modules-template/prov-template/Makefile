

expand1:
	../toolbox/target/appassembler/bin/provconvert -infile src/main/resources/template-a.provn -outfile src/main/resources/expanded-a1.provn -bindings src/main/resources/bindings1.provn 

expand2:
	../toolbox/target/appassembler/bin/provconvert -infile src/main/resources/template-a.provn -outfile src/main/resources/expanded-a2.provn -bindings src/main/resources/bindings2.provn 

expand3:
	../toolbox/target/appassembler/bin/provconvert -infile src/main/resources/template-b.provn -outfile src/main/resources/expanded-a3.provn -bindings src/main/resources/bindings3.provn 

expand4:
	../toolbox/target/appassembler/bin/provconvert -infile src/main/resources/template-c.provn -outfile src/main/resources/expanded-a4.provn -bindings src/main/resources/bindings4.provn 

bindings4:
	../toolbox/target/appassembler/bin/provconvert -infile src/main/resources/bindings4.provn -outfile src/main/resources/bindings4.ttl
	../toolbox/target/appassembler/bin/provconvert -infile src/main/resources/bindings4.provn -outfile src/main/resources/bindings4.json

LINE=4

do.csv:
	cat src/test/resources/csv/csv1.csv | awk -v line=$(LINE) -f src/main/resources/awk/tobindings.awk  > target/bindings$(LINE).json
	provconvert -bindver 3 -infile src/test/resources/csv/template_block.provn -bindings target/bindings$(LINE).json -outfile target/block$(LINE).provn


wfl:
	$(MAKE) LINE=3 do.csv
	$(MAKE) LINE=4 do.csv
	$(MAKE) LINE=5 do.csv
	printf "file, target/block3.provn, provn\nfile, target/block4.provn, provn\nfile, target/block5.provn, provn\n" | provconvert -merge - -flatten -outfile target/wfl.png

PTM_EXPAND=../../modules-executable/toolbox/target/appassembler/bin/ptm-expand

test.ptm:
	$(PTM_EXPAND) `pwd`/target -configs src/main/resources/meta/config/ttb-generic.json
	$(PTM_EXPAND) `pwd`/target -configs src/main/resources/meta/config/ttb-plead.json
	$(PTM_EXPAND) `pwd`/target -configs src/main/resources/meta/config/ttb-ptm.json
	$(PTM_EXPAND) `pwd`/target -configs src/main/resources/meta/config/ttb-pg.json

BINDINGS_SCHEMA_FILE=src/main/resources/jsonschema/bindings-schema.json
TTF_SCHEMA_FILE=src/main/resources/jsonschema/ttf-schema.json

#extracted for book
test.schema:
	ajv -s $(SCHEMA_FILE) -d $(FILE)

test.schema1:
	$(MAKE) FILE=src/test/resources/bindings/b21.json SCHEMA_FILE=$(BINDINGS_SCHEMA_FILE) test.schema

test.ttf1:
	$(MAKE) FILE=src/main/resources/meta/config/config-generic.json SCHEMA_FILE=$(TTF_SCHEMA_FILE) test.schema
