
DEPLOY_DIR=target/foo


run:
	@mkdir -p $(DEPLOY_DIR)
	@cd $(DEPLOY_DIR); make -f ../../Makefile archetype.generate
	@ls -lR $(DEPLOY_DIR)

GROUP_ID=org.example
ARTIFACT_ID=demo
VERSION=0.1-SNAPSHOT

archetype.generate:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@mvn archetype:generate \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype
