DOCUMENTS=src/test/resources/queries/prov/loan
QUERIES=src/test/resources/queries/queries-loan
JAVA_OPTS=-Djava.util.logging.config.file=src/main/resources/config/logging.properties
JAVA_OPTS2="-Dlog4j.configurationFile=src/test/resources/ptm/config/log4j2.xml -Djava.util.logging.config.file=src/test/resources/ptm/config/logging.properties"

PROVMAN=export JAVA_OPTS=$(JAVA_OPTS); target/appassembler/bin/provmanagement
PROVMAN2=export JAVA_OPTS=$(JAVA_OPTS2); target/appassembler/bin/provmanagement

mvn.test: test.batch.queries xp.all.file xp.aggregate

test.batch.queries:
	$(PROVMAN) batch --infile src/test/resources/batch/queries.txt --outfile ignore --parallel true

test.queries: q.automatic.decision1 q.automatic.decision2 q.automatic.decision3 q.automation1 q.automation2 q.automation3 q.counterfactual1 q.counterfactual1b q.datasource1 q.datasource2 q.datasource2b q.exclusion1 q.inclusion1 q.inclusion3 q.performance1 q.relevancy2 q.responsibility1 q.responsibility2 q.responsibility3 q.responsibility4 q.responsibility5 q.responsibility6

q.responsibility1:
	@echo "q.responsibility1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/loan2.provn  --query $(QUERIES)/q.responsibility1.txt --outfiles target/loan2.provn --queryResult target/loan2-result.json

q.responsibility2:
	@echo "q.responsibility2"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/loan2.provn  --query $(QUERIES)/q.responsibility2.txt --outfiles target/q-responsibility2.provn,target/q-responsibility2.pdf

q.responsibility3:
	@echo "q.responsibility3"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/training.provn  --query $(QUERIES)/q.responsibility3.txt --outfiles target/q.responsibility3-tr.provn,target/q.responsibility3-tr.pdf

q.responsibility4:
	@echo "q.responsibility4"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/loan2-modified.provn  --query $(QUERIES)/q.responsibility4.txt --outfiles target/q-responsibility4.provn,target/q-responsibility4.pdf

q.responsibility5:
	@echo "q.responsibility5"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/loan2-modified.provn  --query $(QUERIES)/q.responsibility5.txt --queryResult target/q.responsibility5.json --outfiles target/q-responsibility5.provn,target/q-responsibility5.pdf

q.responsibility6:
	@echo "q.responsibility6"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/training-1.provn  --query $(QUERIES)/q.responsibility6.txt --queryResult target/q.responsibility6.json --outfiles target/q-responsibility6.provn,target/q-responsibility6.pdf

q.automation1:
	@echo "q.automation1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.automation1.txt --outfiles target/q.automation1-128.provn,target/q.automation1-128.pdf

q.automation2:
	@echo "q.automation2"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.automation2.txt --outfiles target/q.automation2-128.provn,target/q.automation2-128.pdf

q.automation3:
	@echo "q.automation3"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/130910225.provn  --query $(QUERIES)/q.automation3.txt --outfiles target/q.automation3-130.provn,target/q.automation3-130.pdf

q.automatic.decision1:
	@echo "q.automatic.decision1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/130910225-modified.provn  --query $(QUERIES)/q.automatic.decision1.txt --queryResult target/q.automatic.decision1-130.json --outfiles target/q.automatic.decision1-130.provn,target/q.automatic.decision1-130.pdf

q.automatic.decision2:
	@echo "q.automatic.decision2"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/130910225-modified.provn  --query $(QUERIES)/q.automatic.decision2.txt --queryResult target/q.automatic.decision2-130.json --outfiles target/q.automatic.decision2-130.provn,target/q.automatic.decision2-130.pdf

q.automatic.decision3:
	@echo "q.automatic.decision3"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/16.provn  --query $(QUERIES)/q.automatic.decision3.txt --queryResult target/16.json --outfiles target/16.provn,target/16.pdf

q.datasource1:
	@echo "q.datasource1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.datasource1.txt --outfiles target/q.datasource1-128.provn,target/q.datasource1-128.pdf

q.datasource2:
	@echo "q.datasource2"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/16.provn  --query $(QUERIES)/q.datasource2.txt --outfiles target/q.datasource2-16.provn,target/q.datasource2-16.pdf

q.datasource2b:
	@echo "q.datasource2b"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.datasource2.txt --outfiles target/q.datasource2-128.provn,target/q.datasource2-128.pdf

q.performance1:
	@echo "q.performance1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/training.provn  --query $(QUERIES)/q.performance1.txt --outfiles target/q.performance1-train.provn,target/q.performance1-train.pdf

q.inclusion1:
	@echo "q.inclusion1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.inclusion1.txt --outfiles target/q.inclusion1-128.provn,target/q.inclusion1-128.pdf

q.inclusion3:
	@echo "q.inclusion3"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.inclusion3.txt --outfiles target/q.inclusion3-128.provn,target/q.inclusion3-128.pdf

q.exclusion1:
	@echo "q.exclusion1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.exclusion1.txt --outfiles target/q.exclusion1-128.provn,target/q.exclusion1-128.pdf

q.relevancy2:
	@echo "q.relevancy2"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.relevancy2.txt --queryResult target/q.relevancy2.json --outfiles target/q-relevancy2.provn,target/q-relevancy2.pdf

q.counterfactual1:
	@echo "q.counterfactual1"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/cf_merged_hd.provn  --query $(QUERIES)/q.counterfactual1.txt --outfiles target/q.counterfactual1-hd.provn,target/q.counterfactual1-hd.pdf

q.counterfactual1b:
	@echo "q.counterfactual1b"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/cf_merged_ad.provn  --query $(QUERIES)/q.counterfactual1.txt --outfiles target/q.counterfactual1-ad.provn,target/q.counterfactual1-ad.pdf


q.responsibility6b:
	@echo "q.responsibility6"
	@$(PROVMAN) translate --infile $(DOCUMENTS)/128350251.provn  --query $(QUERIES)/q.responsibility6.txt --outfiles target/q-responsibility6.provn,target/q-responsibility6.pdf

xp.all.file:
	@$(MAKE) batch.explain  FILE=128350251 BATCH=[data.sources2],[automatic.decision3,human.decision1],[responsibility6],[inclusion3],[performance4],[relevancy2],[exclusion1]

PROFILE=ln:borrower-noun

batch.explain:
	@$(PROVMAN) explain --infile $(DOCUMENTS)/$(FILE).provn --outfile target/narrative-loan.provn --text target/narrative-loan.json --language src/test/resources/xplans/loan/template-library.json -s target/simplenlg2.txt --profile=$(PROFILE)  --batch-templates=$(BATCH)
	@jq . target/narrative-loan.json

xp.aggregate: aggregate-count

aggregate-count:
	$(MAKE) batch.explain  FILE=16 BATCH=[aggregate-count]

aggregate-count2:
	$(MAKE) batch.explain  FILE=16 BATCH=[aggregate-count2]


kern:
	$(PROVMAN) kernelize --infile ../modules-nf/prov-nf/src/test/resources/prov/pc1.provn -t 2 -A true --features target/pc1-feature.json
kern2:
	$(PROVMAN) kernelize --infile ../modules-nf/prov-nf/src/test/resources/prov/pc1.provn -t 2 -A false --features target/pc1-feature2.json


batch.explain.ptm:
	@$(PROVMAN2) explain --infile src/test/resources/ptm/prov/$(FILE).provn --outfile target/narrative-ptm.provn --text target/narrative-ptm.json --language src/test/resources/ptm/plead/template-library.json -s target/simplenlg2.txt --profile=$(PROFILE)  --batch-templates=$(BATCH)
	@jq . target/narrative-ptm.json

ptm.xp.file1:
	@$(MAKE) batch.explain.ptm  FILE=transforming BATCH=[plead-generic-responsibility][book-action1]

ptm.xp.file2:
	@$(MAKE) batch.explain.ptm  FILE=transforming-composite-1agent BATCH=[plead-generic-responsibility][plead-composite-responsibility][plead-composite-responsibility2][plead-composite-responsibility3][book-action1]
	@$(MAKE) batch.explain.ptm  FILE=transforming-composite-1agent2orgs BATCH=[plead-generic-responsibility][plead-composite-responsibility][plead-composite-responsibility2][plead-composite-responsibility3][book-action1]
