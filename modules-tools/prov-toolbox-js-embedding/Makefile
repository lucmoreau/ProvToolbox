# note, currently jar file is not automatically build
# call mvn clean install -Passembly


PROVTOOLBOX_DIR=$(HOME)/IdeaProjects/ProvToolbox
PROVTOOLBOX_JS_DIR=$(PROVTOOLBOX_DIR)/modules-tools/prov-toolbox-js-embedding/


JSPROV.JAR=$(PROVTOOLBOX_JS_DIR)/target/prov-toolbox-js-embedding-2.2.2-SNAPSHOT-jar-with-dependencies.jar

FILE.JS=src/test/resources/org/openprovenance/prov/polyglot/js/builder.js

jsprov:
	java -jar $(JSPROV.JAR)  $(FILE.JS)
