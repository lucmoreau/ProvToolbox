
do:
	export XJC_OPTS="-Dxml.catalog.verbosity=999";	xjc -b src/main/resources/bindings.xjb -catalog src/main/resources/catalog.cat  src/main/resources/validation.xsd src/main/resources/collection.xsd  src/main/resources/prov-20120920.xsd 


xerces:
	src/main/resources/xerces.sh -a src/main/resources/ex.xsd -i target/attr_entity0.xml

xmllint:
	xmllint --noout --schema src/main/resources/ex.xsd target/attr_entity0.xml 


xerces.all:
	src/main/resources/xerces.sh -a src/main/resources/ex.xsd -i target/*.xml
