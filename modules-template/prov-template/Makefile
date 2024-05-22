

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
	$(PTM_EXPAND) src/main/resources/meta/config/config-ptm.json


test.schema:
	ajv -s src/main/resources/jsonschema/bindings-schema.json -d $(FILE)

test.schema1:
	$(MAKE) FILE=src/test/resources/bindings/b21.json test.schema
