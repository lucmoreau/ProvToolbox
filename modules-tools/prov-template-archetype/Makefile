FILE=./target/classes/provtoolbox.version
PROVTOOLBOX_VERSION=$(shell cat $(FILE))

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
		-DtemplateLibraryGroupId=org.openprovenance.bookptm \
		-DtemplateLibraryArtifactId=template-intro1 \
		-DtemplateLibraryVersion=0.1.0-SNAPSHOT \
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
		-DtemplateLibraryGroupId=org.openprovenance.bookptm \
		-DtemplateLibraryArtifactId=template-intro1 \
		-DtemplateLibraryVersion=0.1.0-SNAPSHOT \
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
		-DtemplateLibraryVersion=$(PROVTOOLBOX_VERSION) \
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



PROVTOOLBOX_DIR=/Users/luc/IdeaProjects/ProvToolbox
JAVA_OPTS="-Dlog4j.configurationFile=src/test/resources/ptm/config/log4j2.xml -Djava.util.logging.config.file=src/test/resources/ptm/config/logging.properties"

# provman uses its own copy of the lexical database: the deployed service (make jetty
# in the generated project) holds an exclusive hsqldb lock on the nlg-wrapper copy,
# and hsqldb allows a single process per database.
LEXICON_SOURCE=$(PROVTOOLBOX_DIR)/modules-explanations/nlg-wrapper/target/dictionary
LEXICON_DIR=$(CURDIR)/target/dictionary
JAVA_OPTS2=-DXPLAIN_LEXICON=$(LEXICON_DIR)/lexAccess2013.data

$(LEXICON_DIR)/lexAccess2013.data:
	@echo "Copying lexical database to $(LEXICON_DIR) ..."
	@mkdir -p $(LEXICON_DIR)
	@cp $(LEXICON_SOURCE)/lexAccess2013.data $(LEXICON_DIR)/
	@cp $(LEXICON_SOURCE)/lexAccess2013.properties $(LEXICON_DIR)/
	@cp $(LEXICON_SOURCE)/lexAccess2013.script $(LEXICON_DIR)/

PROVMAN=export JAVA_OPTS=$(JAVA_OPTS2); $(PROVTOOLBOX_DIR)/modules-executable-provman/target/appassembler/bin/provmanagement
NLG_XPLAIN=src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/resources/xplain/nlg

XPLAN_SCHEMA=src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/resources/schema/xplan-schema.json
AJV=ajv

# X-plan template files to validate (excludes dictionaries, profiles, config, and
# provbasic2.json which intentionally contains // comments and is not strict JSON)
XPLAN_FILES = \
    $(NLG_XPLAIN)/fs/fs-xplan-library.json \
    $(NLG_XPLAIN)/fs/fs-awk-column-selection.json \
    $(NLG_XPLAIN)/fs/fs-file-derivation.json \
    $(NLG_XPLAIN)/fs/fs-file-init.json \
    $(NLG_XPLAIN)/fs/fs-file-splitting.json \
    $(NLG_XPLAIN)/fs/fs-file-validating.json \
    $(NLG_XPLAIN)/fs/fs-file-approving.json \
    $(NLG_XPLAIN)/fs/fs-generic-responsibility.json \
    $(NLG_XPLAIN)/tr/tr-xplan-library.json \
    $(NLG_XPLAIN)/tr/tr-transporting.json \
    $(NLG_XPLAIN)/tr/tr-packing.json \
    $(NLG_XPLAIN)/tr/tr-unpacking.json \
    $(NLG_XPLAIN)/tr/tr-examining.json \
    $(NLG_XPLAIN)/tr/tr-examining-no-instrument.json \
    $(NLG_XPLAIN)/tr/tr-agent-init.json \
    $(NLG_XPLAIN)/tr/tr-assigning.json \
    $(NLG_XPLAIN)/tr/tr-handingover.json \
    $(NLG_XPLAIN)/provbasic/provbasic.json \
    $(NLG_XPLAIN)/provbasic/actedonbehalfof1.json \
    $(NLG_XPLAIN)/provbasic/activity1.json \
    $(NLG_XPLAIN)/provbasic/agent1.json \
    $(NLG_XPLAIN)/provbasic/alternate1.json \
    $(NLG_XPLAIN)/provbasic/association1.json \
    $(NLG_XPLAIN)/provbasic/attribution1.json \
    $(NLG_XPLAIN)/provbasic/derivation1.json \
    $(NLG_XPLAIN)/provbasic/end1.json \
    $(NLG_XPLAIN)/provbasic/end2.json \
    $(NLG_XPLAIN)/provbasic/end3.json \
    $(NLG_XPLAIN)/provbasic/entity1.json \
    $(NLG_XPLAIN)/provbasic/generation1.json \
    $(NLG_XPLAIN)/provbasic/invalidation1.json \
    $(NLG_XPLAIN)/provbasic/member1.json \
    $(NLG_XPLAIN)/provbasic/specialization1.json \
    $(NLG_XPLAIN)/provbasic/start1.json \
    $(NLG_XPLAIN)/provbasic/start2.json \
    $(NLG_XPLAIN)/provbasic/start3.json \
    $(NLG_XPLAIN)/provbasic/usage1.json

schema.validate:
	@echo "Validating x-plan files against $(XPLAN_SCHEMA) ..."
	@failed=0; \
	for f in $(XPLAN_FILES); do \
	    result=$$($(AJV) validate -s $(XPLAN_SCHEMA) -d "$$f" --errors=text 2>&1); \
	    rc=$$?; \
	    fname=$$(basename "$$f"); \
	    if [ $$rc -eq 0 ]; then \
	        echo "  OK   $$fname"; \
	    else \
	        echo "  FAIL $$fname"; \
	        echo "$$result"; \
	        failed=$$((failed+1)); \
	    fi; \
	done; \
	if [ $$failed -eq 0 ]; then \
	    echo "All x-plan files are valid."; \
	else \
	    echo "$$failed file(s) failed validation."; \
	    exit 1; \
	fi
DOCUMENTS=/Users/luc/IdeaProjects/ProvToolbox/modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/src/test/prov/

# SOURCE_NAME/PROPERTY_NAME rename the key of a shared x-plan in the generated json,
# so that its sentences can be labelled by scenario (e.g. fs-file-fitting,
# fs-file-filtering, tr-weighing). By default, the key is left unchanged.
SOURCE_NAME=fs-file-derivation
PROPERTY_NAME=$(SOURCE_NAME)

# x-plan library used by batch.explain; tr targets override it
XPLAN_LIB=$(NLG_XPLAIN)/fs/fs-xplan-library.json
TR_XPLAN_LIB=$(NLG_XPLAIN)/tr/tr-xplan-library.json

batch.explain: $(LEXICON_DIR)/lexAccess2013.data
	@$(PROVMAN) explain --infile $(DOCUMENTS)/$(FILE).provn --outfile target/narrative-loan.provn --text target/narrative-loan.json --language $(XPLAN_LIB) -s target/simplenlg2.txt --profile=$(PROFILE)  --batch-templates=$(BATCH)
	@jq 'with_entries(if .key == "$(SOURCE_NAME)" then .key = "$(PROPERTY_NAME)" else . end) | with_entries(select(.value != ""))' target/narrative-loan.json



xp.filtering:
	@$(MAKE) batch.explain FILE=file_filtering BATCH=[fs-awk-column-selection]

# the filtering variants share the fs-file-derivation x-plan (see xp.fitting.on.filtering):
# verb and object qualifier come from the dictionary, keyed on prov:type
xp.filtering.all: xp.filtering xp.fitting.on.filtering xp.filtering.no.method xp.filtering.no.agent xp.filtering.no.association xp.filtering.no.date

xp.filtering.no.method:
	@$(MAKE) batch.explain FILE=file_filtering_no_method BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-filtering
xp.filtering.no.agent:
	@$(MAKE) batch.explain FILE=file_filtering_no_agent BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-filtering
xp.filtering.no.association:
	@$(MAKE) batch.explain FILE=file_filtering_no_association BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-filtering
xp.filtering.no.date:
	@$(MAKE) batch.explain FILE=file_filtering_no_date BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-filtering

xp.transforming.all: xp.transforming xp.transforming.no.method xp.transforming.no.agent xp.transforming.no.association

xp.init.all: xp.init xp.init.no.agent xp.init.no.date

xp.transforming:
	@$(MAKE) batch.explain FILE=file_transforming1 BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-transforming
xp.transforming.no.method:
	@$(MAKE) batch.explain FILE=file_transforming1_no_method BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-transforming
xp.transforming.no.agent:
	@$(MAKE) batch.explain FILE=file_transforming1_no_agent BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-transforming
xp.transforming.no.association:
	@$(MAKE) batch.explain FILE=file_transforming1_no_association BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-transforming
xp.init:
	@$(MAKE) batch.explain FILE=file_init BATCH=[fs-file-init]
xp.init.no.agent:
	@$(MAKE) batch.explain FILE=file_init_no_agent BATCH=[fs-file-init]
xp.init.no.date:
	@$(MAKE) batch.explain FILE=file_init_no_date BATCH=[fs-file-init]

xp.validating.all: xp.validating xp.validating.method xp.validating.no.agent xp.validating.no.date
xp.validating:
	@$(MAKE) batch.explain FILE=file_validating BATCH=[fs-file-validating]
xp.validating.method:
	@$(MAKE) batch.explain FILE=file_validating_method BATCH=[fs-file-validating]
xp.validating.no.agent:
	@$(MAKE) batch.explain FILE=file_validating_no_agent BATCH=[fs-file-validating]
xp.validating.no.date:
	@$(MAKE) batch.explain FILE=file_validating_no_date BATCH=[fs-file-validating]

xp.approving.all: xp.approving xp.approving.no.agent xp.approving.no.score
xp.approving:
	@$(MAKE) batch.explain FILE=file_approving BATCH=[fs-file-approving]
xp.approving.no.agent:
	@$(MAKE) batch.explain FILE=file_approving_no_agent BATCH=[fs-file-approving]
xp.approving.no.score:
	@$(MAKE) batch.explain FILE=file_approving_no_score BATCH=[fs-file-approving]

xp.splitting.all: xp.splitting xp.splitting.four.way

xp.splitting:
	@$(MAKE) batch.explain FILE=file_splitting1 BATCH=[fs-file-splitting]
xp.splitting.four.way:
	@$(MAKE) batch.explain FILE=file_splitting_four_way BATCH=[fs-file-splitting]

xp.fitting.all: xp.fitting xp.fitting.no.method xp.fitting.untyped.method xp.fitting.untyped.entities xp.fitting.no.agent xp.fitting.no.association xp.fitting.no.date

# same x-plan as xp.fitting: verb and object qualifier come from the dictionary, keyed on prov:type
xp.fitting.on.filtering:
	@$(MAKE) batch.explain FILE=file_filtering BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-filtering

xp.fitting:
	@$(MAKE) batch.explain FILE=file_training BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting
xp.fitting.no.method:
	@$(MAKE) batch.explain FILE=file_training_no_method BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting
xp.fitting.untyped.method:
	@$(MAKE) batch.explain FILE=file_training_untyped_method BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting
xp.fitting.untyped.entities:
	@$(MAKE) batch.explain FILE=file_training_untyped_entities BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting
xp.fitting.no.agent:
	@$(MAKE) batch.explain FILE=file_training_no_agent BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting
xp.fitting.no.association:
	@$(MAKE) batch.explain FILE=file_training_no_association BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting
xp.fitting.no.date:
	@$(MAKE) batch.explain FILE=file_training_no_date BATCH=[fs-file-derivation] PROPERTY_NAME=fs-file-fitting

# --- Transport book (tr): x-plans for the physical and responsibility template packages.
# Test documents are based on recent records from the bookptm database (see each
# document's header comment for its record of origin).

tr.explain:
	@$(MAKE) batch.explain XPLAN_LIB=$(TR_XPLAN_LIB) FILE=$(FILE) BATCH=$(BATCH) SOURCE_NAME=$(SOURCE_NAME) PROPERTY_NAME=$(PROPERTY_NAME)

xp.tr.all: xp.tr.transporting.all xp.tr.packing.all xp.tr.unpacking xp.tr.weighing.all xp.tr.examining xp.tr.agent.init.all xp.tr.assigning.all xp.tr.handingover.all

xp.tr.transporting.all: xp.tr.transporting xp.tr.transporting.full xp.tr.transporting.no.agent
xp.tr.transporting:
	@$(MAKE) tr.explain FILE=tr_transporting BATCH=[tr-transporting]
xp.tr.transporting.full:
	@$(MAKE) tr.explain FILE=tr_transporting_full BATCH=[tr-transporting]
xp.tr.transporting.no.agent:
	@$(MAKE) tr.explain FILE=tr_transporting_no_agent BATCH=[tr-transporting]

xp.tr.packing.all: xp.tr.packing xp.tr.packing.no.agent
xp.tr.packing:
	@$(MAKE) tr.explain FILE=tr_packing BATCH=[tr-packing]
xp.tr.packing.no.agent:
	@$(MAKE) tr.explain FILE=tr_packing_no_agent BATCH=[tr-packing]

xp.tr.unpacking:
	@$(MAKE) tr.explain FILE=tr_unpacking BATCH=[tr-unpacking]

# weighing shares the tr-examining x-plan: the verb (weigh/examine) comes from the
# dictionary, keyed on the activity type; the weight phrase is optional
xp.tr.weighing.all: xp.tr.weighing xp.tr.weighing.unit xp.tr.weighing.no.delegation xp.tr.weighing.no.scale
xp.tr.weighing:
	@$(MAKE) tr.explain FILE=tr_weighing BATCH=[tr-examining][tr-examining-no-instrument] SOURCE_NAME=tr-examining PROPERTY_NAME=tr-weighing
xp.tr.weighing.unit:
	@$(MAKE) tr.explain FILE=tr_weighing_unit BATCH=[tr-examining][tr-examining-no-instrument] SOURCE_NAME=tr-examining PROPERTY_NAME=tr-weighing
xp.tr.weighing.no.delegation:
	@$(MAKE) tr.explain FILE=tr_weighing_no_delegation BATCH=[tr-examining][tr-examining-no-instrument] SOURCE_NAME=tr-examining PROPERTY_NAME=tr-weighing
xp.tr.weighing.no.scale:
	@$(MAKE) tr.explain FILE=tr_weighing_no_scale BATCH=[tr-examining][tr-examining-no-instrument] SOURCE_NAME=tr-examining-no-instrument PROPERTY_NAME=tr-weighing
xp.tr.examining:
	@$(MAKE) tr.explain FILE=tr_examining BATCH=[tr-examining][tr-examining-no-instrument]

xp.tr.agent.init.all: xp.tr.agent.init xp.tr.agent.init.scale xp.tr.agent.init.no.location
xp.tr.agent.init:
	@$(MAKE) tr.explain FILE=tr_agent_init BATCH=[tr-agent-init]
xp.tr.agent.init.scale:
	@$(MAKE) tr.explain FILE=tr_agent_init_scale BATCH=[tr-agent-init]
xp.tr.agent.init.no.location:
	@$(MAKE) tr.explain FILE=tr_agent_init_no_location BATCH=[tr-agent-init]

xp.tr.assigning.all: xp.tr.assigning xp.tr.assigning.no.agent
xp.tr.assigning:
	@$(MAKE) tr.explain FILE=tr_assigning BATCH=[tr-assigning]
xp.tr.assigning.no.agent:
	@$(MAKE) tr.explain FILE=tr_assigning_no_agent BATCH=[tr-assigning]

xp.tr.handingover.all: xp.tr.handingover xp.tr.handingover.no.giver
xp.tr.handingover:
	@$(MAKE) tr.explain FILE=tr_handingover BATCH=[tr-handingover]
xp.tr.handingover.no.giver:
	@$(MAKE) tr.explain FILE=tr_handingover_no_giver BATCH=[tr-handingover]

#| xgroup_by(.)
xp.all:
	@$(MAKE) xp.transforming.all xp.filtering.all xp.init.all xp.splitting.all xp.fitting.all xp.validating.all xp.approving.all | jq -s '.  '
