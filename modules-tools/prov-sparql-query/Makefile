
PROV_SPARQL=target/appassembler/bin/prov-sparql

q1:
	$(PROV_SPARQL) -infile "src/test/resources/pc1-full.jsonld" -query "src/test/resources/q1.rq" -outfile "target/result1.json"
	jq . target/result1.json


q2:
	$(PROV_SPARQL) -infile "src/test/resources/pc1-full.jsonld" -query "src/test/resources/q2.rq" -outformat TEXT