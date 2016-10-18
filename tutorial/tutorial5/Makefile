
test.zip:
	cd target; unzip ProvToolbox-Tutorial1-0.5.1-SNAPSHOT-src.zip; cd ProvToolbox-Tutorial1-0.5.1-SNAPSHOT; mvn clean install


PROVMAN=~/workspace/prov-scala/provman/target/appassembler/bin/provmanagement


do.comp:
	$(PROVMAN) compare --infile target/pc1.provn  --withfile target/pc2.provn --outfile -
