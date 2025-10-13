
DEPLOY_DIR=target
PROVTOOLBOX_VERSION=2.2.2-SNAPSHOT

run.book:
	@mkdir -p $(DEPLOY_DIR)
	@cd $(DEPLOY_DIR); make -f ../Makefile archetype.generate.book

run.ptl:
	@mkdir -p $(DEPLOY_DIR)
	@cd $(DEPLOY_DIR); make -f ../Makefile ARTIFACT_ID=artif2 archetype.generate.ptl


GROUP_ID=org.example
ARTIFACT_ID=artif
VERSION=0.1-SNAPSHOT

archetype.generate.book:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@mvn archetype:generate \
		-DprovtoolboxVersion=$(PROVTOOLBOX_VERSION) \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype \
    	-DarchetypeVersion=2.2.2-SNAPSHOT \
    	-DtemplateLibraryGroupId=org.openprovenance.bookptm \
    	-DtemplateLibraryArtifactId=template-intro1 \
    	-DtemplateLibraryVersion=0.1.0-SNAPSHOT \
    	-DtemplateLibraryServiceName=ServiceBookPTM \
    	-DcataloguePackage=org.openprovenance.bk.physical \
    	-DtemplateSubLibrary=-transport \
    	-DloggerConstructor=org.openprovenance.bk.physical.client.logger.Logger \
    	-DjdbcURL="jdbc:postgresql://localhost:5432/bookptm?sslmode=disable" \
    	-DserviceContext=/book \
    	-DservicePort=7075 \



archetype.generate.ptl:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@mvn archetype:generate \
 		-DprovtoolboxVersion=$(PROVTOOLBOX_VERSION) \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype \
    	-DarchetypeVersion=2.2.2-SNAPSHOT \
    	-DtemplateLibraryGroupId=org.openprovenance.prov \
    	-DtemplateLibraryArtifactId=prov-template-library \
    	-DtemplateLibraryVersion=2.2.2-SNAPSHOT \
    	-DtemplateLibraryServiceName=ServiceTemplateLibary \
    	-DcataloguePackage=org.openprovenance.prov.template.library.plead \
    	-DtemplateSubLibrary=-plead \
    	-DloggerConstructor=org.openprovenance.prov.template.library.plead.client.logger.Logger \
	    -DjdbcURL="jdbc:postgresql://localhost:5432/templates?sslmode=disable" \
	    -DserviceContext=/ptl \
	    -DservicePort=7076





book.install:
	mvn clean install
