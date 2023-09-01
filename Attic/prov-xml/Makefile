
do:
	export XJC_OPTS="-Dxml.catalog.verbosity=999";	xjc -b src/main/resources/bindings.xjb -catalog src/main/resources/catalog.cat  src/main/resources/validation.xsd src/main/resources/collection.xsd  src/main/resources/prov-20120920.xsd 


xerces:
	src/main/resources/xerces.sh -a src/main/resources/ex.xsd -i target/attr_entity0.xml

xmllint:
	xmllint --noout --schema src/main/resources/ex.xsd --schema src/main/resources/ex2.xsd  --schema src/main/resources/w3c/prov.xsd target/entity100.xml 


xerces.all:
	src/main/resources/xerces.sh -a src/main/resources/ex.xsd -i target/*.xml

xmllint.pc1:
	xmllint --noout --schema src/main/resources/ex.xsd --schema src/main/resources/ex2.xsd --schema src/main/resources/w3c/prov.xsd target/pc1-full.xml


xmllint.all:
	xmllint --noout --schema src/main/resources/ex.xsd --schema src/main/resources/ex2.xsd --schema src/main/resources/w3c/prov.xsd target/*.xml
