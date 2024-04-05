FILE=/home/lavm/workspace/validation-project/prov-validation/src/test/resources/pc1-full.xml
URL=https://raw.github.com/lucmoreau/ProvToolbox/master/prov-n/src/test/resources/prov/primer.pn
OUT=target/primer

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


verify:
	 mvn -Ppostgres clean verify

translate.all:
	@$(MAKE) URL=$(URL) OUT=$(OUT) translate.svg
	@$(MAKE)  OUT=$(OUT) TYPE=provn get.others
	@$(MAKE)  OUT=$(OUT) TYPE=xml get.others
	@$(MAKE)  OUT=$(OUT) TYPE=json get.others
	@$(MAKE)  OUT=$(OUT) TYPE=ttl get.others


translate.svg:
	@$(MAKE) URL=$(URL) TYPE=svg OUT=$(OUT) translate
translate.provn:
	@$(MAKE) URL=$(URL) TYPE=provn OUT=$(OUT) translate
translate.xml:
	@$(MAKE) URL=$(URL) TYPE=xml OUT=$(OUT) translate
translate.json:
	@$(MAKE) URL=$(URL) TYPE=json OUT=$(OUT) translate
translate.ttl:
	@$(MAKE) URL=$(URL) TYPE=ttl OUT=$(OUT) translate


translate:
	@curl -X POST --trace-ascii target/output.$(TYPE).txt -H "Content-Type: multipart/form-data"  -F $(TYPE)=$(TYPE) -F url=$(URL) -i  $(HTTP_SERVICE)/prov-vservice/validation/submit | grep Location | awk '{ print $$2 }' > target/location.txt
	@curl -X GET --output $(OUT).$(TYPE) `cat target/location.txt`

get.others:
	@curl -X GET --output $(OUT).$(TYPE)  `cat target/location.txt | sed -e 's/svg/$(TYPE)/' `

xslt:
	xsltproc src/main/webapp/xslt/XMLPrettyPrintSimple.xsl src/test/resources/report-start-fail4.xml > target/report-start-fail4.html




# release 0.7.1

## to prepare a war file, postgresql backend, deployable on tomcat (home, smurf, pat)
## run the following build.test.jetty
### and then run deploy.pat (or equivalent on gaston)
build.test.jetty.postgres:
	mvn clean install -Pjetty,postgres,global

build.test.tomcat.postgres:
	mvn clean install -Ptomcat,postgres,global

build.test.jetty.hsqldb:
	mvn clean install -Pjetty,hsqldb,global

build.test.tomcat.hsqldb:
	mvn clean install -Ptomcat,hsqldb,global

build.test.all:
	$(MAKE) build.test.tomcat.hsqldb
	$(MAKE) build.test.tomcat.postgres
	$(MAKE) build.test.jetty.hsqldb
	$(MAKE) build.test.jetty.postgres

WHAT=prov-service-0.7.1-SNAPSHOT
deploy:
	curl --upload-file target/$(WHAT).war "http://prov:prov2012@provenance.ecs.soton.ac.uk:7000/manager/deploy?path=/$(WHAT)&update=true"

deploy.smurf:
	mvn clean install -Ptomcat,postgres,global
	curl --upload-file target/$(WHAT).war "http://prov:prov2012@smurf.ecs.soton.ac.uk:7000/manager/text/deploy?path=/$(WHAT)&update=true"

deploy.gaston:
	curl --upload-file target/$(WHAT).war "http://admin:admin@localhost:7000/manager/text/deploy?path=/$(WHAT)&update=true"

deploy.pat:
	curl --upload-file target/$(WHAT).war "http://prov:prov2012@provenance.ecs.soton.ac.uk:18080/manager/text/deploy?path=/$(WHAT)&update=true"

deploy.dev:
	curl --upload-file target/$(WHAT).war "http://prov:prov2012@prov-dev.ecs.soton.ac.uk:7000/manager/html/deploy?path=/$(WHAT)&update=true"



FILE_TO_POST=$(HOME)/luc-svn/prov-book/application/datajournalism.provn
MIME_TYPE=text/provenance-notation; charset=UTF-8

post:
	curl -D - -H "Content-Type: $(MIME_TYPE)" -X POST --data-binary @$(FILE_TO_POST) $(HTTP_SERVICE)/prov-vservice/provapi/documents/

post2:
	$(MAKE) FILE_TO_POST="$(HOME)/luc-svn/prov-book/application/datajournalism.provx" MIME_TYPE="application/provenance+xml" post


post3:
	$(MAKE) FILE_TO_POST="$(HOME)/luc-svn/prov-book/application/datajournalism.ttl" MIME_TYPE="text/turtle" post

## in target/site/dependencies.html you should see a pretty nice list of all your dependencies.

license:
	mvn license:add-third-party
missing:
	mvn license:add-third-party -Dlicense.useMissingFile
missing.test:
	mvn license:add-third-party -Dlicense.useMissingFile -Dlicense.includedScopes=test

gg0:
	curl -X GET http://localhost:9999/provapi/documents/random/50/4 
gg1:
	curl -X GET  -H "Accept: application/json" http://localhost:9999/provapi/documents/random/50/4 

gg2:
	curl -X GET  -H "Accept: image/svg+xml" --output foo.svg http://localhost:9999/provapi/documents/random/50/4 

gg3:
	curl -X GET  -H "Accept: text/provenance-notation" http://localhost:9999/provapi/documents/random/50/4/-6028442754801246530


post_template:
	@curl $(PASSWD) -D - -H "Content-Type: $(MIME_TYPE)" -X POST --data-binary @$(FILE_TO_POST) $(HTTP_SERVICE)/ptm/templates/
post_bindings:
	@curl $(PASSWD) -D - -H "Content-Type: $(MIME_TYPE)" -X POST --data-binary @$(FILE_TO_POST) $(HTTP_SERVICE)/ptm/bindings/

tmpl1:
	@$(MAKE) -s FILE_TO_POST="resources/template-attribution.provn" MIME_TYPE="text/provenance-notation" post_template > resources/header-t1.txt
	@cat resources/header-t1.txt
	@cat resources/header-t1.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-t1.txt
	@cat resources/location-t1.txt
	@$(MAKE)  SERVICE=$(SERVICE) -s gt1

tmpl2:
	@$(MAKE) -s FILE_TO_POST="resources/test-template.provn" MIME_TYPE="text/provenance-notation" post_template > resources/header-t2.txt
	@cat resources/header-t2.txt
	@cat resources/header-t2.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-t2.txt
	@cat resources/location-t2.txt
	@$(MAKE) SERVICE=$(SERVICE) -s gt2

tmpl3:
	@$(MAKE) SERVICE=$(SERVICE) -s FILE_TO_POST="src/main/resources/templates/put-named-document.provn" MIME_TYPE="text/provenance-notation" post_template > resources/header-t3.txt
	@cat resources/header-t3.txt
	@cat resources/header-t3.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-t3.txt
	@cat resources/location-t3.txt
	@$(MAKE) SERVICE=$(SERVICE) -s gt3

tmpl4:
	@$(MAKE) SERVICE=$(SERVICE) -s FILE_TO_POST="src/main/resources/templates/post-document.provn" MIME_TYPE="text/provenance-notation" post_template > resources/header-t4.txt
	@cat resources/header-t4.txt
	@cat resources/header-t4.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-t4.txt
	@cat resources/location-t4.txt
	@$(MAKE) SERVICE=$(SERVICE) -s gt4


bind1:
	@$(MAKE) SERVICE=$(SERVICE) -s FILE_TO_POST="resources/bindingsCheneyTRENDDB09.provn" MIME_TYPE="text/provenance-notation" post_bindings > resources/header-b1.txt
	@cat resources/header-b1.txt
	@cat resources/header-b1.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-b1.txt
	@cat resources/location-b1.txt
	@$(MAKE) -s gb1

bind2:
	@$(MAKE) -s FILE_TO_POST="resources/bindingsMoreauOPM101.provn" MIME_TYPE="text/provenance-notation" post_bindings > resources/header-b2.txt
	@cat resources/header-b2.txt
	@cat resources/header-b2.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-b2.txt
	@cat resources/location-b2.txt
	@$(MAKE) SERVICE=$(SERVICE) -s gb2


gt1.type:
	@cat resources/location-t1.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-t1.txt` get_document

gt1:
	@cat resources/location-t1.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-t1.txt` DOCUMENT_TYPE=".provn" get_document
gt2:
	@cat resources/location-t2.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-t2.txt` DOCUMENT_TYPE=".provn" get_document
gt3:
	@cat resources/location-t3.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-t3.txt` DOCUMENT_TYPE=".provn" get_document
gt4:
	@cat resources/location-t4.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-t4.txt` DOCUMENT_TYPE=".provn" get_document
gb1:
	@cat resources/location-b1.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-b1.txt` DOCUMENT_TYPE=".provn" get_document
gb2:
	@cat resources/location-b2.txt
	@$(MAKE) SERVICE=$(SERVICE) -s DOCUMENT=`grep http resources/location-b2.txt` DOCUMENT_TYPE=".provn" get_document

expa1:
	$(MAKE) TEMPLATE=`cat resources/location-t1.txt | sed -e 's/http:\/\/$(SERVICE)\/ptm\/templates\///'` BINDING=`cat resources/location-b1.txt | sed -e 's/http:\/\/$(SERVICE)\/ptm\/bindings\///'` MIME_TYPE="text/provenance-notation" expansion

expansion:
	curl -D - -H "Content-Type: $(MIME_TYPE)" -X GET $(HTTP_SERVICE)/ptm/documents/expansion/$(TEMPLATE)/$(BINDING)

TEMPLATE_TO_GET=1

get_template:
	curl $(PASSWD) -D -  -X GET $(HTTP_SERVICE)/ptm/templates/$(TEMPLATE_TO_GET).provn

get_document:
	@curl $(PASSWD) -D -  -X GET $(DOCUMENT)$(DOCUMENT_TYPE)




log1:
	@$(MAKE) -s FILE_TO_POST="resources/plps-log-template.provn" MIME_TYPE="text/provenance-notation" post_template > resources/header-l1.txt
	@cat resources/header-l1.txt
	@cat resources/header-l1.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-l1.txt
	@cat resources/location-l1.txt

log2:
	@$(MAKE) -s FILE_TO_POST="resources/bindings-plps-log1.provn" MIME_TYPE="text/provenance-notation" post_bindings > resources/header-l2.txt
	@cat resources/header-l2.txt
	@cat resources/header-l2.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-l2.txt
	@cat resources/location-l2.txt

exlog1:
	$(MAKE) TEMPLATE=`cat resources/location-l1.txt | sed -e 's/http:\/\/$(SERVICE)\/ptm\/templates\///'` BINDING=`cat resources/location-l2.txt | sed -e 's/http:\/\/$(SERVICE)\/ptm\/bindings\///'` MIME_TYPE="text/provenance-notation" expansion


post_idoc1:
	@curl  $(PASSWD) -D - -H "Content-Type: $(MIME_TYPE)" -X POST $(HTTP_SERVICE)/ptm/idocuments/ > resources/header-i1.txt
	@cat resources/header-i1.txt
	@cat resources/header-i1.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-i1.txt
	@cat resources/location-i1.txt




post_idoc2:
	@curl -D - -H "Content-Type: $(MIME_TYPE)" -X POST $(DOCUMENT) > resources/header-i2.txt
	@cat resources/header-i2.txt
	@cat resources/header-i2.txt |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-i2.txt

pp2:
	$(MAKE) DOCUMENT=`grep http resources/location-i1.txt` post_idoc2


#HTTP_SERVICE=https://$(SERVICE)
HTTP_SERVICE=http://$(SERVICE)

gid1:
	@cat resources/location-i1.txt
	@$(MAKE) -s DOCUMENT=`grep http resources/location-i1.txt` MIME_TYPE="text/provenance-notation" get_idocument

get_idocument.noredirect:
	@curl  $(PASSWD) -D - -H "Accept: $(MIME_TYPE)"  -X GET $(DOCUMENT)

get_idocument:
	@curl  $(PASSWD) -D - -H "Accept: $(MIME_TYPE)"  --location -X GET $(DOCUMENT)


post_idocadd:
	@curl  $(PASSWD) -D - -X POST --data-urlencode template=`cat resources/location-l1.txt` --data-urlencode bindings=`cat resources/location-l2.txt` `grep http resources/location-i1.txt`

gn1:
	@curl $(PASSWD) -D - -X GET $(HTTP_SERVICE)/ptm/templates/name/hello
pn1:
	@curl $(PASSWD) -D - -X PUT  --data-urlencode template=`cat resources/location-l1.txt` $(HTTP_SERVICE)/ptm/templates/name/hello |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-n1.txt

pn3:
	@curl $(PASSWD) -D - -X PUT  --data-urlencode template=`cat resources/location-t3.txt` $(HTTP_SERVICE)/ptm/templates/name/put-named-document |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-n3.txt
gn3:
	@curl $(PASSWD) -D - -X GET $(HTTP_SERVICE)/ptm/templates/name/put-named-document

pn4:
	@curl $(PASSWD) -D - -X PUT  --data-urlencode template=`cat resources/location-t4.txt` $(HTTP_SERVICE)/ptm/templates/name/post-document |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-n4.txt
gn4:
	@curl $(PASSWD) -D - -X GET $(HTTP_SERVICE)/ptm/templates/name/post-document

pn2:
	@curl $(PASSWD) -D - -X PUT  --data-urlencode template=`cat resources/location-t1.txt` $(HTTP_SERVICE)/ptm/templates/name/hello |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-n1.txt

test-suite1:
	@$(MAKE) tmpl3
	@$(MAKE) gt3
	@$(MAKE) pn3
	@$(MAKE) gn3
	@$(MAKE) tmpl4
	@$(MAKE) gt4
	@$(MAKE) pn4
	@$(MAKE) gn4
	@$(MAKE) log1
	@$(MAKE) pn1
	@$(MAKE) gn1
	@$(MAKE) bind1
	@$(MAKE) gb1
	@$(MAKE) bind2
	@$(MAKE) gb2
	@$(MAKE) tmpl1
	@$(MAKE) gt1
	@$(MAKE) DOCUMENT_TYPE=".json" gt1.type
#	@$(MAKE) DOCUMENT_TYPE=".ttl" gt1.type  PROBLEM
	@$(MAKE) post_idoc1
	@curl -D - -X POST --data-urlencode template=`cat resources/location-t1.txt` --data-urlencode bindings=`cat resources/location-b1.txt` `grep http resources/location-i1.txt`
	@curl -D - -X POST --data-urlencode template=`cat resources/location-t1.txt` --data-urlencode bindings=`cat resources/location-b2.txt` `grep http resources/location-i1.txt`
	@curl -D - -X GET `grep http resources/location-i1.txt`/original
	@curl -D - -X GET `grep http resources/location-i1.txt`  
	@curl -D - -X GET `grep http resources/location-i1.txt`  |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-c1.txt
	@curl -D - --location -H "Accept: text/provenance-notation"   -X GET `grep http resources/location-i1.txt`  
	@curl -D - -X GET `grep http resources/location-c1.txt`  | grep Link: | awk '{print $$2 }' | tr -d '<' | tr -d '>' | tr -d ';' > resources/provenance-link-1.txt
	@curl -D - -X GET `grep http resources/provenance-link-1.txt`  
	echo "TODO COMPLETE provenance , missin g variables, and incorrect namespaces"

#HOST=gaston:8080
#HOST=smurf.ecs.soton.ac.uk:7000
HOST=provenance.ecs.soton.ac.uk

test-suite2:
	$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT tmpl3
	$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gt3
	$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT pn3
	$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gn3
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT tmpl4
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gt4
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT pn4
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gn4
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT log1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT pn1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gn1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT bind1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gb1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT bind2
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gb2
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT tmpl1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT gt1
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT DOCUMENT_TYPE=".json" gt1.type
#	@$(MAKE) DOCUMENT_TYPE=".ttl" gt1.type  PROBLEM
	@$(MAKE) SERVICE=$(HOST)/prov-vservice-0.5.2-SNAPSHOT post_idoc1
	@curl -D - -X POST --data-urlencode template=`cat resources/location-t1.txt` --data-urlencode bindings=`cat resources/location-b1.txt` `grep http resources/location-i1.txt`
	@curl -D - -X POST --data-urlencode template=`cat resources/location-t1.txt` --data-urlencode bindings=`cat resources/location-b2.txt` `grep http resources/location-i1.txt`
	@curl -D - -X GET `grep http resources/location-i1.txt`/original
	@curl -D - -X GET `grep http resources/location-i1.txt`  
	@curl -D - -X GET `grep http resources/location-i1.txt`  |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-c1.txt
	@curl -D - --location -H "Accept: text/provenance-notation"   -X GET `grep http resources/location-i1.txt`  
	@curl -D - -X GET `grep http resources/location-c1.txt`  | grep Link: | awk '{print $$2 }' | tr -d '<' | tr -d '>' | tr -d ';' > resources/provenance-link-1.txt
	@curl -D - -X GET `grep http resources/provenance-link-1.txt`  
	echo "TODO COMPLETE provenance , missin g variables, and incorrect namespaces"

PASSWD=-u rideshare:smart2014

test-suite3:
	$(MAKE) SERVICE=$(HOST) tmpl3
	$(MAKE) SERVICE=$(HOST) gt3
	$(MAKE) SERVICE=$(HOST) pn3
	$(MAKE) SERVICE=$(HOST) gn3
	@$(MAKE) SERVICE=$(HOST) tmpl4
	@$(MAKE) SERVICE=$(HOST) gt4
	@$(MAKE) SERVICE=$(HOST) pn4
	@$(MAKE) SERVICE=$(HOST) gn4
	@$(MAKE) SERVICE=$(HOST) log1
	@$(MAKE) SERVICE=$(HOST) pn1
	@$(MAKE) SERVICE=$(HOST) gn1
	@$(MAKE) SERVICE=$(HOST) bind1
	@$(MAKE) SERVICE=$(HOST) gb1
	@$(MAKE) SERVICE=$(HOST) bind2
	@$(MAKE) SERVICE=$(HOST) gb2
	@$(MAKE) SERVICE=$(HOST) tmpl1
	@$(MAKE) SERVICE=$(HOST) gt1
	@$(MAKE) SERVICE=$(HOST) DOCUMENT_TYPE=".json" gt1.type
	@$(MAKE) SERVICE=$(HOST) post_idoc1
	@curl $(PASSWD) -D - -X POST --data-urlencode template=`cat resources/location-t1.txt` --data-urlencode bindings=`cat resources/location-b1.txt` `grep http resources/location-i1.txt`
	@curl $(PASSWD) -D - -X POST --data-urlencode template=`cat resources/location-t1.txt` --data-urlencode bindings=`cat resources/location-b2.txt` `grep http resources/location-i1.txt`
	@curl $(PASSWD) -D - -X GET `grep http resources/location-i1.txt`/original
	@curl $(PASSWD) -D - -X GET `grep http resources/location-i1.txt`  
	@curl $(PASSWD) -D - -X GET `grep http resources/location-i1.txt`  |  tr -d '\015' | grep Location | awk '{print $$2 }' > resources/location-c1.txt
	@curl $(PASSWD) -D - --location -H "Accept: text/provenance-notation"   -X GET `grep http resources/location-i1.txt`  
	@curl $(PASSWD) -D - -X GET `grep http resources/location-c1.txt`  | grep Link: | awk '{print $$2 }' | tr -d '<' | tr -d '>' | tr -d ';' > resources/provenance-link-1.txt
	@curl $(PASSWD) -D - -X GET `grep http resources/provenance-link-1.txt`  
	echo "TODO COMPLETE provenance , missin g variables, and incorrect namespaces"


### create tunnel on pat, to redirect pat port 9339 to smurf port 7000
### allows https://provenance.ecs.soton.ac.uk/ptm/bindings
tunnel:
	ssh lavm@pat.ecs.soton.ac.uk -R 9339:smurf.ecs.soton.ac.uk:7000


MIME_TYPE2=text/provenance-notation

cu:
	curl -D - -u rideshare:smart2014  -H "Accept: $(MIME_TYPE2)"  -X GET --location https://provenance.ecs.soton.ac.uk/ptm/cdocuments/2368



#
coneg1:
	curl -i -X GET "http://localhost:7070/provapi/documents/ff" -H "accept: application/json"

coneg2:
	curl -i -X OPTION "http://localhost:7070/provapi/documents/ff"
coneg3:
	curl -i -X POST "http://localhost:7070/provapi/documents2" -H "accept: text/turtle" -H "Content-Type: text/provenance-notation" -d "document prefix ex <http://foo> entity(ex:e)endDocument"


coneg4:
	curl -i -X POST -d @src/test/resources/small.ttl "http://localhost:7070/provapi/documents2" -H "accept: text/provenance-notation" -H "Content-Type: text/turtle"


coneg5:
	curl -i -X POST -d @src/test/resources/small.ttl "http://localhost:7070/provapi/documents2" -H "accept: xxx/unknown" -H "Content-Type: text/turtle"





tmpl_java:
	provconvert -infile src/main/resources/templates/provapi/translate-document.provn -package org.openprovenance.prov.pservice.bindings -location here/ -template Translate -outfile target/generated-sources/


test.bin1:
	wget "http://localhost:7070/provapi/bindings/b1?operation=times1&agent=ag1&consumed_value1=5&consumed1=input1&consumed_value2=10&consumed2=input2&produced_value=50&produced=ouput&ex=1234"


test.bin2:
	curl -i -H "accept: text/provenance-notation"  "http://localhost:7070/provapi/documents/bindings/b1?operation=times1&agent=ag1&consumed_value1=5&consumed1=input1&consumed_value2=10&consumed2=input2&produced_value=50&produced=ouput&ex=1234"


test.bin1.r:
	wget "https://openprovenance.org/services/provapi/bindings/b1?operation=times1&agent=ag1&consumed_value1=5&consumed1=input1&consumed_value2=10&consumed2=input2&produced_value=50&produced=ouput&ex=1234"

test.bin2.r:
	curl -i -H "accept: text/provenance-notation"  "https://openprovenance.org/services/provapi/documents/bindings/b1?operation=times1&agent=ag1&consumed_value1=5&consumed1=input1&consumed_value2=10&consumed2=input2&produced_value=50&produced=ouput&ex=1234"


WEBSERVICE=https://openprovenance.org/services/provapi/documents
WEBSERVICE=http://localhost:9190/provapi/documents

test.syntax1.ok:
#	echo "document prefix ex <http://foo> entity(ex:e)endDocument" > target/contents.provn
	@curl -i -X POST  -H "Content-Type: multipart/form-data"  -Ftype=provn -Fstatements=@target/contents.provn -Ftranslate=turtle  $(WEBSERVICE)| grep Location | awk '{ print $$2 }' > target/location.txt
#--trace-ascii target/output.txt
	curl -X GET -i `cat target/location.txt`

test.syntax1:
#	echo "document prefix ex <http://foo> entity(ex:e)endDocument" > target/contents.provn
	@curl -i -X POST  -H "Content-Type: multipart/form-data"  -Ftype=provn -Fstatements=@target/contents.provn -Ftranslate=turtle  $(WEBSERVICE) #| grep Location | awk '{ print $$2 }' > target/location.txt
#--trace-ascii target/output.txt
	curl -X GET -i `cat target/location.txt`


WEBSERVICE=https://openprovenance.org/services/provapi/documents
WEBSERVICE=http://localhost:7072/ptl/provapi/templates

test.ptl1:
	curl -i -X POST  -H "Content-Type: application/json"  -d @src/test/resources/json/table-key1.json  $(WEBSERVICE).provn