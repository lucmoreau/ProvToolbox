
test.zip:
	cd target; unzip ProvToolbox-Tutorial6-0.7.3-SNAPSHOT-src.zip; cd ProvToolbox-Tutorial6-0.7.3-SNAPSHOT; mvn clean install


PROVMAN=~/Documents/workspace/prov-scala/provman/target/appassembler/bin/provmanagement
PROVMAN=~/workspaces/prov-scala/provman/target/appassembler/bin/provmanagement
PROVMAN=~/workspace/prov-scala/provman/target/appassembler/bin/provmanagement


do.comp:
	$(PROVMAN) compare --infile ../tutorial5/target/pc3.provn  --withfile target/pc3.provn --outfile -
	$(PROVMAN) compare --infile ../tutorial5/target/pc2.provn  --withfile target/pc3.provn --outfile -

do.expand:
	provconvert -infile src/main/resources/template_block.provn -bindings src/main/resources/bind-align.json -bindver 3 -outfile target/template_align.provn
	provconvert -infile target/template_align.provn -template Align -package org.example -outfile target/

