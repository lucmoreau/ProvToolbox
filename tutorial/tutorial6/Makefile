
test.zip:
	cd target; unzip ProvToolbox-Tutorial1-0.5.1-SNAPSHOT-src.zip; cd ProvToolbox-Tutorial1-0.5.1-SNAPSHOT; mvn clean install


PROVMAN=~/Documents/workspace/prov-scala/provman/target/appassembler/bin/provmanagement
PROVMAN=~/workspaces/prov-scala/provman/target/appassembler/bin/provmanagement
PROVMAN=~/workspace/prov-scala/provman/target/appassembler/bin/provmanagement


do.comp:
	$(PROVMAN) compare --infile target/pc1.provn  --withfile target/pc2.provn --outfile -
	$(PROVMAN) compare --infile target/pc2.provn  --withfile target/pc3.provn --outfile -

do.expand:
	provconvert -infile src/main/resources/template_block.provn -bindings src/main/resources/bind-align.json -bindver 3 -outfile target/template_align.provn
