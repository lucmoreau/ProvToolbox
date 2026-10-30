# Throughput benchmark of the template service (service-templates-core). See BENCHMARK.md.
#
# Its own PostgreSQL (Docker postgres:16, 127.0.0.1:55432, database provbench, volume provbench-pgdata, label provbench=1)
# and its own service (http://localhost:7099/provbench). Every target that writes runs the guard first (Guard.java): it
# refuses any other host, port, database or context, a PostgreSQL on 55432 that is not the labelled container, and a
# cluster holding any database but provbench, postgres, template0, template1.
#
# Self-contained: sets every variable it needs, reads nothing from ~/.bashrc; the service's environment is built by the
# harness (no TPL_*, PROV_*, TEMPLATE_CONFIG or JDBC_POOL_SIZE of the caller reaches it). Portable make and sh only
# (macOS/BSD and Linux): the work is done by the Java harness.

JAVA ?= java
MVN ?= mvn

# a run (make bench.run ...)
FAMILY ?= fs
VARIANT ?= baseline
N ?= 1
B ?= 1
POOL ?= 0
WARMUP ?= 10
DURATION ?= 30
WRITERS ?= 2
QUEUE ?= 10000
BATCH ?= 200
COMPOSITE_SIZE ?= 3
JETTY_MAX_THREADS ?= 200
SERVICE_JVM_FLAGS ?= -Xms2g -Xmx2g -XX:+UseG1GC
PLAN ?= full
LABEL ?=
STOP_ON_FAIL ?= 0

# what the generated services are built from (mvn install'ed: ProvToolbox, its archetype, provenance-templates)
PROVTOOLBOX_VERSION ?= 2.3.0-SNAPSHOT
TPL_VERSION ?= 0.1.0-SNAPSHOT

HARNESS_CP = target/service-throughput.jar:target/lib/*
HARNESS = $(JAVA) -Xmx1g -Dbench.home="$(CURDIR)" -cp "$(HARNESS_CP)" org.openprovenance.bench.throughput.Main
SETTINGS = FAMILY=$(FAMILY) VARIANT=$(VARIANT) N=$(N) B=$(B) POOL=$(POOL) WARMUP=$(WARMUP) DURATION=$(DURATION) \
	WRITERS=$(WRITERS) QUEUE=$(QUEUE) BATCH=$(BATCH) COMPOSITE_SIZE=$(COMPOSITE_SIZE) JETTY_MAX_THREADS=$(JETTY_MAX_THREADS) \
	"SERVICE_JVM_FLAGS=$(SERVICE_JVM_FLAGS)" PLAN=$(PLAN) LABEL=$(LABEL) STOP_ON_FAIL=$(STOP_ON_FAIL)

.PHONY: bench.help bench.build bench.prepare bench.service.build bench.guard bench.db.up bench.db.down bench.db.reset \
	bench.db.destroy bench.service.up bench.service.down bench.run bench.matrix bench.plan bench.smoke

bench.help:
	@echo "make bench.prepare                       build the harness (and its guard test), generate and build both services"
	@echo "make bench.db.up | bench.db.down | bench.db.reset | bench.db.destroy"
	@echo "make bench.service.up FAMILY=fs|tr VARIANT=... POOL=... | bench.service.down"
	@echo "make bench.run FAMILY=fs VARIANT=baseline N=1 B=1 POOL=0 WARMUP=10 DURATION=30"
	@echo "make bench.plan | bench.matrix [PLAN=full|smoke] [LABEL=...]"
	@echo "variants: baseline noacl acl-index nohash hash-header async nokey key-noindex nojit combined"

# ---------------------------------------------------------------- build

bench.build: target/service-throughput.jar

target/service-throughput.jar: pom.xml $(shell find src -name '*.java')
	$(MVN) -B -q -f pom.xml package

bench.prepare: bench.build
	$(MAKE) bench.service.build FAMILY=fs
	$(MAKE) bench.service.build FAMILY=tr

# The service of a family, generated from prov-template-archetype as its Makefile's archetype.generate.book.fs/.tr do,
# but on the benchmark's store, port and context; under target/, never committed. Rebuilt when service-templates-core
# changed: make bench.service.build FAMILY=fs FORCE=1.
ifeq ($(FAMILY),tr)
CATALOGUE = org.openprovenance.templates.catalogue.transport
SUBLIBRARY = -transport
ICONS = transport-icons.svg
XPLAN_LIBRARY = /xplain/nlg/tr/tr-xplan-library.json
XPLAN_SELECTION = ["tr-transporting", "tr-packing", "tr-unpacking", "tr-examining", "tr-examining-no-instrument", "tr-agent-init", "tr-assigning", "tr-handingover"]
else
CATALOGUE = org.openprovenance.templates.catalogue.fs
SUBLIBRARY = -fs
ICONS = file-icons.svg
XPLAN_LIBRARY = /xplain/nlg/fs/fs-xplan-library.json
XPLAN_SELECTION = ["fs-awk-column-selection", "fs-file-init", "fs-file-splitting","fs-file-derivation", "fs-generic-responsibility", "fs-file-validating", "fs-file-approving"]
endif
SERVICE_ID = provbench-$(FAMILY)

bench.service.build:
	@mkdir -p target/services
	@if [ -n "$(FORCE)" ]; then rm -rf target/services/$(SERVICE_ID); fi
	@if [ ! -d target/services/$(SERVICE_ID) ]; then \
	  echo "generating $(SERVICE_ID) from prov-template-archetype $(PROVTOOLBOX_VERSION)"; \
	  cd target/services && $(MVN) -B -q archetype:generate -DinteractiveMode=false \
	    -DarchetypeGroupId=org.openprovenance.prov -DarchetypeArtifactId=prov-template-archetype -DarchetypeVersion=$(PROVTOOLBOX_VERSION) \
	    -DgroupId=org.openprovenance.provbench -DartifactId=$(SERVICE_ID) -Dversion=0.1.0-SNAPSHOT -DProjectArtifactId=$(SERVICE_ID) \
	    -DtemplateLibraryGroupId=org.openprovenance.templates -DtemplateLibraryArtifactId=provenance-templates-library -DtemplateLibraryVersion=$(TPL_VERSION) \
	    -DtemplateExplanationsGroupId=org.openprovenance.templates -DtemplateExplanationsArtifactId=provenance-templates-explanations -DtemplateExplanationsVersion=$(TPL_VERSION) \
	    -DtemplateLibraryServiceName=ServiceProvbench -DdashboardTitle="provbench $(FAMILY)" -DtemplatesButtonLabel=Templates \
	    -DcataloguePackage=$(CATALOGUE) -DtemplateSubLibrary=$(SUBLIBRARY) -DloggerConstructor=$(CATALOGUE).client.logger.Logger \
	    -DjdbcURL=jdbc:postgresql://localhost:55432/provbench -DservicePort=7099 -DserviceContext=/provbench \
	    -Dimage.url.prefix=webjars/$(SERVICE_ID)/0.1.0-SNAPSHOT/ -Dtemplate.url.prefix=webjars/$(SERVICE_ID)/0.1.0-SNAPSHOT/ \
	    -Dcbindings.url.prefix=webjars/$(SERVICE_ID)/0.1.0-SNAPSHOT/ \
	    -DnlgXplanLibrary=$(XPLAN_LIBRARY) -DnlgXplanSelection='$(XPLAN_SELECTION)' -DtheIconSvgLibrary=$(ICONS) || exit 1; \
	fi
	cd target/services/$(SERVICE_ID) && $(MVN) -B -q -Pjetty,nosecurity -DskipTests -Dexec.skip=true package -pl $(SERVICE_ID)-service
	@echo "built target/services/$(SERVICE_ID)/$(SERVICE_ID)-service/target/$(SERVICE_ID)-service-0.1.0-SNAPSHOT-unsecure"

# ---------------------------------------------------------------- store, service, runs (the guard first, in the harness)

bench.guard: bench.build
	$(HARNESS) guard $(SETTINGS)

bench.db.up: bench.build
	$(HARNESS) db-up $(SETTINGS)

bench.db.down: bench.build
	$(HARNESS) db-down $(SETTINGS)

bench.db.reset: bench.build
	$(HARNESS) db-reset $(SETTINGS)

bench.db.destroy: bench.build
	$(HARNESS) db-destroy $(SETTINGS)

bench.service.up: bench.build
	$(HARNESS) service-up $(SETTINGS)

bench.service.down: bench.build
	$(HARNESS) service-down $(SETTINGS)

bench.run: bench.build
	$(HARNESS) run $(SETTINGS)

bench.plan: bench.build
	@$(HARNESS) plan $(SETTINGS)

bench.matrix: bench.build
	$(HARNESS) matrix $(SETTINGS)

# the short check this module was proved with: both families, baseline and async, N=2 POOL=4, a few seconds each
bench.smoke: bench.build
	$(HARNESS) matrix $(SETTINGS) PLAN=smoke WARMUP=3 DURATION=8 LABEL=$(if $(LABEL),$(LABEL),smoke)
