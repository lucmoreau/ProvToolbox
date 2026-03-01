FILE=./target/classes/provtoolbox.version
PROVTOOLBOX_VERSION=$(shell cat $(FILE))

DEPLOY_DIR=target

run.book:
	@echo "run.book: Using provtoolbox version $(PROVTOOLBOX_VERSION)"
	@mkdir -p $(DEPLOY_DIR)
	@$(MAKE) archetype.generate.book

run.book.fs:
	@echo "run.book.fs: Using provtoolbox version $(PROVTOOLBOX_VERSION)"
	@mkdir -p $(DEPLOY_DIR)
	@$(MAKE) archetype.generate.book.fs

run.ptl:
	@echo "run.ptl: Using provtoolbox version $(PROVTOOLBOX_VERSION)"
	@mkdir -p $(DEPLOY_DIR)
	@$(MAKE) ARTIFACT_ID=artif2 archetype.generate.ptl


GROUP_ID=org.example
ARTIFACT_ID=artif
VERSION=0.1.0-SNAPSHOT

archetype.generate.book:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@cd $(DEPLOY_DIR); mvn archetype:generate -B \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DProjectArtifactId=$(ARTIFACT_ID) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype \
	-DarchetypeVersion=$(PROVTOOLBOX_VERSION) \
	-DtemplateLibraryGroupId=org.openprovenance.bookptm \
	-DtemplateLibraryArtifactId=template-intro1 \
	-DtemplateLibraryVersion=0.1.0-SNAPSHOT \
	-DtemplateLibraryServiceName=ServiceBookPTM \
	-DcataloguePackage=org.openprovenance.templates.catalogue.transport \
	-DtemplateSubLibrary=-transport \
	-DloggerConstructor=org.openprovenance.templates.catalogue.transport.client.logger.Logger \
	-DjdbcURL="jdbc:postgresql://localhost:5432/bookptm?sslmode=disable" \
	-Dimage.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
	-Dtemplate.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
	-Dcbindings.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
	-DserviceContext=/book \
	-DservicePort=7075

archetype.generate.book.fs:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@cd $(DEPLOY_DIR); mvn archetype:generate -B \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DProjectArtifactId=$(ARTIFACT_ID) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype \
	-DarchetypeVersion=$(PROVTOOLBOX_VERSION) \
	-DtemplateLibraryGroupId=org.openprovenance.bookptm \
	-DtemplateLibraryArtifactId=template-intro1 \
	-DtemplateLibraryVersion=0.1.0-SNAPSHOT \
	-DtemplateLibraryServiceName=ServiceBookFS \
	-DcataloguePackage=org.openprovenance.templates.catalogue.fs \
	-DtemplateSubLibrary=-fs \
	-DloggerConstructor=org.openprovenance.templates.catalogue.fs.client.logger.Logger \
	-DjdbcURL="jdbc:postgresql://localhost:5432/bookptm?sslmode=disable" \
	-Dimage.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
	-Dtemplate.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
	-Dcbindings.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
	-DserviceContext=/book \
	-DservicePort=7075


archetype.generate.ptl:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@cd $(DEPLOY_DIR); mvn archetype:generate \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype \
	-DarchetypeVersion=$(PROVTOOLBOX_VERSION) \
	-DtemplateLibraryGroupId=org.openprovenance.prov \
	-DtemplateLibraryArtifactId=prov-template-library \
	-DtemplateLibraryVersion=$(PROVTOOLBOX_VERSION) \
	-DtemplateLibraryServiceName=ServiceTemplateLibary \
	-DcataloguePackage=org.openprovenance.prov.template.library.plead \
	-DtemplateSubLibrary=-plead \
	-DloggerConstructor=org.openprovenance.prov.template.library.plead.client.logger.Logger \
	-DjdbcURL="jdbc:postgresql://localhost:5432/templates?sslmode=disable" \
	-DserviceContext=/ptl \
	-DservicePort=7076





book.install:
	mvn clean install


go:
	$(MAKE) book.install
	$(MAKE) run.book
	cd $(DEPLOY_DIR)/artif/artif-service; mvn clean install
	cd $(DEPLOY_DIR)/artif/artif-service; make jetty

go2:
	$(MAKE) book.install
	$(MAKE) run.book.fs
	cd $(DEPLOY_DIR)/artif/artif-service; mvn clean install
	cd $(DEPLOY_DIR)/artif/artif-service; make jetty

