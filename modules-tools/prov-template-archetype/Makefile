FILE=./target/classes/provtoolbox.version
PROVTOOLBOX_VERSION=$(shell cat $(FILE))

TPL_VERSION=0.1.0-SNAPSHOT

DEPLOY_DIR=target

NLG_BASIC_XPLAN_LIBRARY=/xplain/nlg/provbasic/provbasic.json
NLG_BASIC_XPLAN_SELECTION='["actedonbehalfof1","activity1","agent1","alternate1","association1","attribution1","derivation1","end1","entity1","generation1","invalidation1","specialization1","start1","usage1","member1"]'

NLG_FS_XPLAN_LIBRARY=/xplain/nlg/fs/fs-xplan-library.json
NLG_FS_XPLAN_SELECTION='["fs-awk-column-selection", "fs-file-init", "fs-file-splitting","fs-file-derivation", "fs-generic-responsibility", "fs-file-validating", "fs-file-approving"]'

NLG_TR_XPLAN_LIBRARY=/xplain/nlg/tr/tr-xplan-library.json
NLG_TR_XPLAN_SELECTION='["tr-transporting", "tr-packing", "tr-unpacking", "tr-examining", "tr-examining-no-instrument", "tr-agent-init", "tr-assigning", "tr-handingover"]'


run.book.tr:
	@echo "run.book.tr: Using provtoolbox version $(PROVTOOLBOX_VERSION)"
	@mkdir -p $(DEPLOY_DIR)
	@$(MAKE) archetype.generate.book.tr

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

archetype.generate.book.tr:
	@echo "Generating project $(GROUP_ID):$(ARTIFACT_ID):$(VERSION)"
	@cd $(DEPLOY_DIR); mvn archetype:generate -B \
        -DgroupId=$(GROUP_ID) \
        -DartifactId=$(ARTIFACT_ID) \
        -Dversion=$(VERSION) \
        -DProjectArtifactId=$(ARTIFACT_ID) \
        -DarchetypeGroupId=org.openprovenance.prov \
        -DarchetypeArtifactId=prov-template-archetype \
		-DarchetypeVersion=$(PROVTOOLBOX_VERSION) \
		-DtemplateLibraryGroupId=org.openprovenance.templates \
		-DtemplateLibraryArtifactId=provenance-templates-library \
		-DtemplateLibraryVersion=$(TPL_VERSION) \
		-DtemplateExplanationsGroupId=org.openprovenance.templates \
		-DtemplateExplanationsArtifactId=provenance-templates-explanations \
		-DtemplateExplanationsVersion=$(TPL_VERSION) \
		-DtemplateLibraryServiceName=ServiceBookPTM \
		-DtemplatesButtonLabel="Book Templates" \
		-DcataloguePackage=org.openprovenance.templates.catalogue.transport \
		-DtemplateSubLibrary=-transport \
		-DloggerConstructor=org.openprovenance.templates.catalogue.transport.client.logger.Logger \
		-DjdbcURL="jdbc:postgresql://localhost:5432/bookptm?sslmode=disable" \
		-Dimage.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-Dtemplate.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-Dcbindings.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-DnlgXplanLibrary=$(NLG_TR_XPLAN_LIBRARY) \
		-DnlgXplanSelection=$(NLG_TR_XPLAN_SELECTION) \
		-DtheIconSvgLibrary=transport-icons.svg \
		-DserviceContext=/book \
		-DservicePort=7075 \
		-DdashboardTitle="PTM Book Transport Dashboard"

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
		-DtemplateLibraryGroupId=org.openprovenance.templates \
		-DtemplateLibraryArtifactId=provenance-templates-library \
		-DtemplateLibraryVersion=$(TPL_VERSION) \
		-DtemplateExplanationsGroupId=org.openprovenance.templates \
		-DtemplateExplanationsArtifactId=provenance-templates-explanations \
		-DtemplateExplanationsVersion=$(TPL_VERSION) \
		-DtemplateLibraryServiceName=ServiceBookFS \
		-DdashboardTitle="PTM Book FS Dashboard" \
		-DtemplatesButtonLabel="Book Templates" \
		-DcataloguePackage=org.openprovenance.templates.catalogue.fs \
		-DtemplateSubLibrary=-fs \
		-DloggerConstructor=org.openprovenance.templates.catalogue.fs.client.logger.Logger \
		-DjdbcURL="jdbc:postgresql://localhost:5432/bookptm?sslmode=disable" \
		-Dimage.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-Dtemplate.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-Dcbindings.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-DnlgXplanLibrary=$(NLG_FS_XPLAN_LIBRARY) \
		-DnlgXplanSelection=$(NLG_FS_XPLAN_SELECTION) \
		-DtheIconSvgLibrary=file-icons.svg \
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
		-DtemplateLibraryVersion=$(TPL_VERSION) \
		-DtemplateExplanationsGroupId=org.openprovenance.prov \
		-DtemplateExplanationsArtifactId=prov-template-explanations \
		-DtemplateExplanationsVersion=$(TPL_VERSION) \
		-DtemplateLibraryServiceName=ServiceTemplateLibary \
		-DtemplatesButtonLabel="PLEAD Templates" \
		-DcataloguePackage=org.openprovenance.prov.template.library.plead \
		-DtemplateSubLibrary=-plead \
		-DloggerConstructor=org.openprovenance.prov.template.library.plead.client.logger.Logger \
		-DjdbcURL="jdbc:postgresql://localhost:5432/templates?sslmode=disable" \
		-Dimage.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-Dtemplate.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-Dcbindings.url.prefix=webjars/$(ARTIFACT_ID)/$(VERSION)/ \
		-DnlgXplanLibrary=$(NLG_BASIC_XPLAN_LIBRARY) \
		-DnlgXplanSelection=$(NLG_BASIC_XPLAN_SELECTION) \
		-DtheIconSvgLibrary=file-icons.svg \
		-DserviceContext=/ptl \
		-DservicePort=7076 \
		-DdashboardTitle="PTL Dashboard"



book.install:
	mvn clean install


go:
	$(MAKE) book.install
	$(MAKE) run.book.tr
	cd $(DEPLOY_DIR)/artif/artif-service; mvn clean install
	cd $(DEPLOY_DIR)/artif/artif-service; make jetty

go2:
	$(MAKE) book.install
	$(MAKE) run.book.fs
	cd $(DEPLOY_DIR)/artif/artif-service; mvn clean install
	cd $(DEPLOY_DIR)/artif/artif-service; make jetty
