FILE=/home/lavm/workspace/validation-project/prov-validation/src/test/resources/pc1-full.xml
URL=https://raw.github.com/lucmoreau/ProvToolbox/master/prov-n/src/test/resources/prov/primer.pn
OUT=target/primer

PROV_CREDENTIALS=~/.kclprov

SERVICE_PORT=${servicePort}
SERVICE_CONTEXT=${serviceContext}
SERVICE_HOST=localhost
SERVICE_PROTOCOL=http

SERVICE_SECURITY_CONFIG=/Users/luc/git-papers/papers/book-ptm/project/service-book/src/test/config/security/openprovenance-auth2-security-config.json
TEMPLATE_CONFIG=target/test-classes/template-config.json

display.config:
	@echo "PROV_STORAGE_CONFIG_BOOKPTM: ${PROV_STORAGE_CONFIG_BOOKPTM}"

install.sec:
	@$(MAKE) display.config
	export PROV_DB_USER=luc; export PROV_DB_PASS=admin; export PROV_HOST=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT); export PROV_API=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi; export PROV_STORAGE_CONFIG=${PROV_STORAGE_CONFIG_BOOKPTM}; export TPL_SECURITY_CONFIG=$(SERVICE_SECURITY_CONFIG);  export TEMPLATE_CONFIG=$(TEMPLATE_CONFIG); mvn -Pjetty,security clean install

jetty:
	 export PROV_DB_USER=luc; export PROV_DB_PASS=admin; export PROV_HOST=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT); export PROV_API=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi; export PROV_STORAGE_CONFIG=${PROV_STORAGE_CONFIG_BOOKPTM}; export TPL_SECURITY_CONFIG=$(SERVICE_SECURITY_CONFIG); export XPLAIN_LEXICON=${XPLAIN_LEXICON}; export TEMPLATE_CONFIG=$(TEMPLATE_CONFIG); mvn -Pjetty,security jetty:run-war

jetty.nosec:
	 export PROV_DB_USER=luc; export PROV_DB_PASS=admin; export PROV_HOST=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT); export PROV_API=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi; export PROV_STORAGE_CONFIG=${PROV_STORAGE_CONFIG_BOOKPTM}; unset TPL_SECURITY_CONFIG;  export TEMPLATE_CONFIG=$(TEMPLATE_CONFIG); mvn -Pjetty,nosecurity jetty:run-war


jetty.post:
	 mvn -Ppostgres jetty:run


SERVICE=$(SERVICE_HOST):8080

TEMPLATE_URL=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/webjars/prov-template-library/2.0.8-SNAPSHOT/templates/org/openprovenance/prov/templates/generic/binaryop.provn

WEBSERVICE=https://openprovenance.org/services/provapi/documents_form/
WEBSERVICE=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi/documents_form/

OUTPUT_FORMAT=svg
test.luc:
	bin/keycloak-get-token.sh
	curl -k -s --cookie-jar target/cookie-jar.txt -i -X POST  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Content-Type: multipart/form-data" -Fstatements=@src/test/resources/bindings/b1.json -Furl=$(TEMPLATE_URL) -Fexpand=$(OUTPUT_FORMAT) -Ftype=$(OUTPUT_FORMAT) $(WEBSERVICE)| grep Location | awk '{ print $$2 }' > target/location1.txt
	@echo "#!/bin/sh" > target/shell1.sh
	@cat target/location1.txt | tr -d '\r' | awk '{printf("/usr/bin/curl -s -H \"Authorization: Bearer `cat /Users/luc/.keycloak_token`\" -D target/headers1.txt -o target/payload1.$(OUTPUT_FORMAT) --cookie-jar target/cookie-jar.txt -X GET \"%s\"\n", $$1) }' >> target/shell1.sh
	sh target/shell1.sh

test2:
	bin/get-with-keycloak-standard-flow.sh $(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/forms.html -o target/payload.html -D target/header.txt
	@echo "Now, using cookie only, to access resources"
	curl -s -o target/payload.html -D - --cookie-jar target/cookie.jar.txt --cookie target/cookie.jar.txt $(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/forms.html


test.bob:
	bin/keycloak-get-token.sh
	curl --cookie-jar target/cookie-jar.txt -i -X POST  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Content-Type: multipart/form-data" -Fstatements=@src/test/resources/bindings/b1.json -Furl=$(TEMPLATE_URL) -Fexpand=jsonld  $(WEBSERVICE) | grep Location | awk '{ print $$2 }' > target/location1.txt
	curl --cookie-jar target/cookie-jar.txt -i -X GET -O < target/location1.txt

test.syntax1:
#	echo "document prefix ex <$(SERVICE_PROTOCOL)://foo> entity(ex:e)endDocument" > target/contents.provn
	curl -i -X POST  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Content-Type: multipart/form-data"  -Ftype=provn -Fstatements=@target/contents.provn -Ftranslate=turtle  $(WEBSERVICE) #| grep Location | awk '{ print $$2 }' > target/location.txt
#--trace-ascii target/output.txt
	curl -X GET -i `cat target/location.txt`


#WEBSERVICE=https://openprovenance.org/services/provapi/documents
#WEBSERVICE=$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_PREFIX)/provapi/templates

test.ptl1:
	curl -i -X POST  -H "Content-Type: application/json"  -d @src/test/resources/json/table-key1.json  $(WEBSERVICE).svg



test.template:
	bin/keycloak-get-token.sh
	curl -i -X GET  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`"   $(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi/template/plead_transforming/480.jsonld


get.template:
	@export VERBOSE=false;bin/keycloak-get-token.sh
	@curl -s -X GET  -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`"   $(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi/template/${TEMPLATE}/${ID}.${EXTENSION}

hash.template:
	@$(MAKE) get.template TEMPLATE=plead_transforming ID=$(ID) EXTENSION=csv | grep $(TEMPLATE) | sed 's/$(TEMPLATE)/$(ID),$(TEMPLATE)/g' > target/977.csv
	@perl -p -i -e 'chomp if eof' target/$(ID).csv
	@openssl dgst -sha3-512 target/$(ID).csv

ex.h.1:
	@$(MAKE) hash.template ID=977 TEMPLATE=plead_transforming

ex.h.2:
	@$(MAKE) get.template TEMPLATE=plead_transforming ID=977 EXTENSION=csv | grep "plead" | sed 's/plead/977,plead/g' > target/977.csv
	@perl -p -i -e 'chomp if eof' target/977.csv
	@openssl dgst -sha3-512 target/977.csv


post.template:
	bin/keycloak-get-token.sh
	curl -i -X POST -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" \
					-H "Accept-PROV-Hash: SHA3-512" \
					-d @src/test/resources/json/bindings.json \
					-H "Content-type: application/vnd.kcl.prov-template+json" \
					-H "Accept: application/vnd.kcl.prov-template+json" \
					$(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/$(SERVICE_CONTEXT)/provapi/statements

post.template2:
	bin/keycloak-get-token.sh
	curl -i -X POST -H "Authorization: Bearer `cat $(HOME)/.keycloak_token`" -H "Accept-PROV-Hash: SHA3-512" -d @src/test/resources/json/bindings2.json -H "Content-type: application/vnd.kcl.prov-template+json" -H "Accept: application/vnd.kcl.prov-template+json" $(SERVICE_PROTOCOL)://$(SERVICE_HOST):$(SERVICE_PORT)/ptl/provapi/statements



create.archetype:
	mvn archetype:create-from-project   

from.archetype:
	cd target/generated-sources/archetype; mvn install; cd target; mvn archetype:generate -DarchetypeCatalog=local
