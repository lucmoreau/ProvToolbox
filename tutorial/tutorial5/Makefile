
test.zip:
	cd target; unzip ProvToolbox-Tutorial5-0.7.3-SNAPSHOT-src.zip; cd ProvToolbox-Tutorial5-0.7.3-SNAPSHOT; mvn clean install


PROVMAN=~/Documents/workspace/prov-scala/provman/target/appassembler/bin/provmanagement
PROVMAN=~/workspaces/prov-scala/provman/target/appassembler/bin/provmanagement
PROVMAN=~/workspace/prov-scala/provman/target/appassembler/bin/provmanagement


do.comp:
	$(PROVMAN) compare --infile target/pc1.provn  --withfile target/pc2.provn --outfile -
	$(PROVMAN) compare --infile target/pc2.provn  --withfile target/pc3.provn --outfile -

do.expand:
	provconvert -infile src/main/resources/template_block.provn -bindings target/bind0.json -bindver 3 -outfile target/align1.pdf
	provconvert -infile src/main/resources/template_block.provn -bindings target/bind0.json -bindver 3 -outfile target/align1.svg
	provconvert -infile src/main/resources/template_block.provn -bindings target/bind0.json -bindver 3 -outfile target/align1.provn
