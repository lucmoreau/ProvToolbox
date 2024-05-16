FILE=/home/lavm/workspace/validation-project/prov-validation/src/test/resources/pc1-full.xml
URL=https://raw.github.com/lucmoreau/ProvToolbox/master/prov-n/src/test/resources/prov/primer.pn
OUT=target/primer

PROV_CREDENTIALS=~/.kclprov

tomcat:
	mvn tomcat7:run -Ptomcat  -X


build.docker:
	mvn clean install -Ptomcat,hsqldb,docker


jetty:
	 export PROV_DB_USER=luc; export PROV_DB_PASS=admin; export PROV_HOST=http://localhost:7072/ptl; export PROV_API=http://localhost:7072/ptl/provapi; mvn jetty:run-war -Pjetty


jetty.post:
	 mvn -Ppostgres jetty:run

#SERVICE=localhost:8080
#SERVICE=localhost:7000/prov-vservice-0.5.2-SNAPSHOT
SERVICE=localhost:8080

TEMPLATE_URL=http://localhost:7072/ptl/webjars/prov-template-library/2.0.6-SNAPSHOT/templates/org/openprovenance/prov/templates/generic/binaryop.provn

WEBSERVICE=https://openprovenance.org/services/provapi/documents_form/
WEBSERVICE=http://localhost:7072/ptl/provapi/documents_form/

OUTPUT_FORMAT=svg
test.luc:
	bin/keycloak-get-token.sh
	curl --cookie-jar target/cookie-jar.txt -i -X POST  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Content-Type: multipart/form-data" -Fstatements=@src/test/resources/bindings/b1.json -Furl=$(TEMPLATE_URL) -Fexpand=$(OUTPUT_FORMAT) -Ftype=$(OUTPUT_FORMAT) $(WEBSERVICE)| grep Location | awk '{ print $$2 }' > target/location1.txt
	echo "#!/bin/sh" > target/shell1.sh
	cat target/location1.txt | tr -d '\r' | awk '{printf("/usr/bin/curl -D target/headers1.txt -o target/payload1.$(OUTPUT_FORMAT) --cookie-jar target/cookie-jar.txt -X GET \"%s\"\n", $$1) }' >> target/shell1.sh
	sh target/shell1.sh


test.bob:
	bin/keycloak-get-token.sh
	curl --cookie-jar target/cookie-jar.txt -i -X POST  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Content-Type: multipart/form-data" -Fstatements=@src/test/resources/bindings/b1.json -Furl=$(TEMPLATE_URL) -Fexpand=jsonld  $(WEBSERVICE) | grep Location | awk '{ print $$2 }' > target/location1.txt
	curl --cookie-jar target/cookie-jar.txt -i -X GET -O < target/location1.txt

test.syntax1:
#	echo "document prefix ex <http://foo> entity(ex:e)endDocument" > target/contents.provn
	curl -i -X POST  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Content-Type: multipart/form-data"  -Ftype=provn -Fstatements=@target/contents.provn -Ftranslate=turtle  $(WEBSERVICE) #| grep Location | awk '{ print $$2 }' > target/location.txt
#--trace-ascii target/output.txt
	curl -X GET -i `cat target/location.txt`


#WEBSERVICE=https://openprovenance.org/services/provapi/documents
#WEBSERVICE=http://localhost:7072/ptl/provapi/templates

test.ptl1:
	curl -i -X POST  -H "Content-Type: application/json"  -d @src/test/resources/json/table-key1.json  $(WEBSERVICE).svg