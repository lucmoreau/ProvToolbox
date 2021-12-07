
jetty:
	cd demo.webapp; make jetty

run:
	target/libs/bin/example_run -infile src/test/resources/templates/example.csv -outfile target/example.png
	target/libs/bin/example_run kernel -infile src/test/resources/templates/example.csv -outfile target/kernel.json -levelOffset 1000000 -relationOffset 2000 -setOffset 100000 -knownTypes src/test/resources/templates/knownTypes.json -knownRelations src/test/resources/templates/knownRelations.json

cli.all:
	jq -c ."tmap"."allLevels" target/kernel.json 

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

clash:
	cd target/output;javac org/issue/ClashBindingsBean.java -cp ../../../prov-template/target/prov-template-0.9.1-SNAPSHOT.jar:../../../../modules-core/prov-model/target/prov-model-0.9.1-SNAPSHOT.jar 

HERE=$(HOME)/IdeaProjects/ProvToolbox/modules-template/prov-template-compiler/

comp2:
	cd target/libs/templates/templates_cli/src/main/java/;javac org/example/templates/block/client/*.java -cp $(HERE)/../prov-template/target/prov-template-0.9.10-SNAPSHOT.jar:$(HERE)/../../modules-core/prov-model/target/prov-model-0.9.10-SNAPSHOT.jar 
