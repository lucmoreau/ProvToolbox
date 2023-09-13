
ORIGIN=${origin.project}

SERVICE=${project.artifactId}



TARGET_IMAGE=$(SERVICE).${docker.java.platform}
LOCATION_IMAGE=$(TARGET_IMAGE)

DOCKER_RUN_DIR=$(HOME)/docker/log-dir

WAR_VERSION=${project.version}
WAR_FILE_ORIGINAL=$(ORIGIN)-$(WAR_VERSION)-jetty
WAR_FILE=$(ORIGIN)-$(WAR_VERSION)-docker



# see http://www.projectatomic.io/blog/2015/08/why-we-dont-let-non-root-users-run-docker-in-centos-fedora-or-rhel/
# to run docker as lavm
DOCKER=sudo /usr/bin/docker
DOCKER=docker

build.all: mk.dir NO.generate.docker.war alt.copy.war dck.file build

mk.dir:
	mkdir -p $(LOCATION_IMAGE)

generate.docker.war:
	rm -f $(LOCATION_IMAGE)/*.war
	cd ${project.basedir}/../$(ORIGIN) ; mvn clean install -Pdocker,jetty

NO.generate.docker.war:
	@echo "No need to generate docker war"

NO.copy.war:
	@echo "No need to copy war"

alt.copy.war:
	cp ${project.build.directory}/$(WAR_FILE).war $(LOCATION_IMAGE)/$(WAR_FILE).war

copy.war:
	cp ${project.basedir}/../$(ORIGIN)/target/$(WAR_FILE_ORIGINAL).war $(LOCATION_IMAGE)/$(WAR_FILE).war

dck.file:
	cp ${project.basedir}/target/classes/Dockerfile $(LOCATION_IMAGE)

build:
	$(DOCKER) build -t ${TARGET_IMAGE} ${LOCATION_IMAGE}
	$(DOCKER) images

run.8080:
	$(MAKE) setup.dir
	$(DOCKER) run -ti  -p 8080:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE}

setup.dir:
	mkdir -p $(DOCKER_RUN_DIR)/database $(DOCKER_RUN_DIR)/files $(DOCKER_RUN_DIR)/log



run.bash:
	$(DOCKER) run -ti  -p 9090:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE} /bin/bash


