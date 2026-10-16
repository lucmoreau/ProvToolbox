
ORIGIN=${origin.project}

SERVICE=${project.artifactId}

BUILD_PLATFORM=linux/amd64

TARGET_IMAGE=$(SERVICE).${docker.java.platform}
LOCATION_IMAGE=$(TARGET_IMAGE)


EXPORT_IMAGE=provenance/templates

WAR_VERSION=${version}
WAR_FILE=$(ORIGIN)-$(WAR_VERSION)-secure
GITHUB_WAR_FILE=$(ORIGIN)-$(WAR_VERSION)-github
UNSECURE_WAR_FILE=$(ORIGIN)-$(WAR_VERSION)-unsecure



DOCKER=docker

# both images: $(TARGET_IMAGE) for linux/amd64 and $(TARGET_IMAGE)-mac for linux/arm64
build.all: mk.dir NO.generate.docker.war copy.war copy.dictionary dck.file build build.mac

# only the image for the platform of this machine, as the security tests run it
build.native: mk.dir copy.war copy.dictionary dck.file
	if [ "`uname -m`" = arm64 ] || [ "`uname -m`" = aarch64 ]; then $(MAKE) -f classes/Makefile build.mac; else $(MAKE) -f classes/Makefile build; fi

mk.dir:
	mkdir -p $(LOCATION_IMAGE)/security

generate.docker.war:
	rm -f $(LOCATION_IMAGE)/*.war
	cd ${project.basedir}/../$(ORIGIN) ; mvn clean install -Pdocker,jetty

NO.generate.docker.war:
	@echo "No need to regenerate docker war"

# the secure war, and the deployment descriptor of each security mode, which the
# entry point installs at start-up according to TPL_SECURITY_MODE
copy.war:
	cp ${project.build.directory}/$(WAR_FILE).war $(LOCATION_IMAGE)/$(WAR_FILE).war
	unzip -p ${project.build.directory}/$(WAR_FILE).war WEB-INF/web.xml > $(LOCATION_IMAGE)/security/web-keycloak.xml
	unzip -p ${project.build.directory}/$(GITHUB_WAR_FILE).war WEB-INF/web.xml > $(LOCATION_IMAGE)/security/web-github.xml
	unzip -p ${project.build.directory}/$(UNSECURE_WAR_FILE).war WEB-INF/web.xml > $(LOCATION_IMAGE)/security/web-none.xml

# the lexicon of the explanations, unzipped into target/dictionary by the pom
copy.dictionary:
	cp ${deployed.dictionary.folder}/lexAccess2013.data   $(LOCATION_IMAGE)/lexAccess2013.data
	cp ${deployed.dictionary.folder}/lexAccess2013.script $(LOCATION_IMAGE)/lexAccess2013.script

dck.file:
	cp ${project.basedir}/target/classes/Dockerfile $(LOCATION_IMAGE)
	cp ${project.basedir}/src/main/docker/docker-entrypoint.sh $(LOCATION_IMAGE)

build:
	$(DOCKER) build --platform $(BUILD_PLATFORM) -t ${TARGET_IMAGE} ${LOCATION_IMAGE}
	$(DOCKER) images

build.mac:
	$(DOCKER) build --platform linux/arm64/v8 -t ${TARGET_IMAGE}-mac ${LOCATION_IMAGE}
	$(DOCKER) images


NOW=`date "+%Y-%m-%d-%H-%M"`

tag.now:
	$(DOCKER) tag ${TARGET_IMAGE} $(EXPORT_IMAGE):$(NOW)

tag:
	$(DOCKER) tag ${TARGET_IMAGE} $(EXPORT_IMAGE):${project.version}

push:
	$(DOCKER) push $(EXPORT_IMAGE):${project.version}
