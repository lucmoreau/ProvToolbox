PROVMAN=../../modules-executable-provman/target/appassembler/bin/provmanagement


q.count:
	echo "q.count"
	$(PROVMAN) translate --infile src/test/resources/prov/loan/16.provn  --query src/test/resources/query/q.count.txt --outfiles target/q.count.provn,target/q.count.pdf --queryResult target/q.count-result.json
