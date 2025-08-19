
PROV_SPARQL=target/appassembler/bin/prov-sparql
RIOT=/usr/local/java/apache-jena-5.1.0/bin/riot

q1:
	$(PROV_SPARQL) -infile "src/test/resources/pc1-full.jsonld" \
                   -query "src/test/resources/q1.rq" \
                   -outfile "target/result1.json"
	jq . target/result1.json


q2:
	$(PROV_SPARQL) -infile "src/test/resources/pc1-full.jsonld" -query "src/test/resources/q2.rq" -outformat TEXT



r.q1:
	$(RIOT) --output=TTL src/test/resources/pc1-full.jsonld > target/pc1-full.tll
	$(RIOT) --output=JSON-LD --syntax=TTL target/pc1-full.tll > target/pc1-full.jsonld
