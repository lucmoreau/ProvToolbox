
jetty:
	cd demo.webapp; make jetty

run:
	target/libs/bin/example_run -infile src/test/resources/templates/example.csv -outfile target/example.png
	target/libs/bin/example_run kernel -infile src/test/resources/templates/example.csv -outfile target/kernel.json -levelOffset 1000000 -relationOffset 2000 -setOffset 100000 -levelNumber 4 -knownTypes src/test/resources/templates/knownTypes.json -knownRelations src/test/resources/templates/knownRelations.json -translation src/test/resources/templates/prettyRelations.json

cli.all:
	jq -c ."tmap"."allLevels" target/kernel.json 
cli.allc:
	jq -c ."tmap"."allLevelsCompact" target/kernel.json
cli.descriptors:
	jq --sort-keys ."tmap"."descriptors" target/kernel.json

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

JSWEET_CLASSPATH=/Users/luc/IdeaProjects/ProvToolbox/modules-template/prov-template-compiler/target/libs/templates/templates_cli/jsweet_extension:/Users/luc/.m2/repository/org/openprovenance/prov/prov-model/2.0.4-SNAPSHOT/prov-model-2.0.4-SNAPSHOT.jar:/Users/luc/.m2/repository/org/apache/commons/commons-lang3/3.9/commons-lang3-3.9.jar:/Users/luc/.m2/repository/org/apache/commons/commons-collections4/4.4/commons-collections4-4.4.jar:/Users/luc/.m2/repository/commons-io/commons-io/2.8.0/commons-io-2.8.0.jar:/Users/luc/.m2/repository/jakarta/xml/bind/jakarta.xml.bind-api/4.0.0/jakarta.xml.bind-api-4.0.0.jar:/Users/luc/.m2/repository/jakarta/activation/jakarta.activation-api/2.1.0/jakarta.activation-api-2.1.0.jar:/Users/luc/.m2/repository/org/apache/logging/log4j/log4j-core/2.20.0/log4j-core-2.20.0.jar:/Users/luc/.m2/repository/org/apache/logging/log4j/log4j-api/2.20.0/log4j-api-2.20.0.jar:/Users/luc/.m2/repository/org/openprovenance/prov/prov-jsweet-candy-java/2.0.4-SNAPSHOT/prov-jsweet-candy-java-2.0.4-SNAPSHOT.jar:/Users/luc/.m2/repository/org/jsweet/jsweet-core/6.3.1/jsweet-core-6.3.1.jar

jsc:
	 cd target/libs/templates/templates_cli; java -jar   ~/.m2/repository/org/jsweet/jsweet-transpiler/4.0.0-SNAPSHOT/jsweet-transpiler-4.0.0-SNAPSHOT-jar-with-dependencies.jar  -i src/main/java --classpath $(JSWEET_CLASSPATH)  --targetVersion ES6 --jsout target/js --tsout target/ts --moduleResolution classic --candiesJsOut target/webapp/candies  --module none --verbose


py:
	export PYTHONPATH=target/py; python3 test.py
