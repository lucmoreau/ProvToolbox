
ORIGIN=service-translator

SERVICE=docker-service-translator



TARGET_IMAGE=$(SERVICE).temurin
LOCATION_IMAGE=$(TARGET_IMAGE)


EXPORT_IMAGE=provenance/provapi

DOCKER_RUN_DIR=$(HOME)/docker/log-dir

WAR_VERSION=0.9.1-SNAPSHOT
WAR_FILE=$(ORIGIN)-$(WAR_VERSION)-docker



# see http://www.projectatomic.io/blog/2015/08/why-we-dont-let-non-root-users-run-docker-in-centos-fedora-or-rhel/
# to run docker as lavm
DOCKER=sudo /usr/bin/docker
DOCKER=docker

build.all: mk.dir generate.docker.war copy.war dck.file build

mk.dir:
	mkdir -p $(LOCATION_IMAGE)

generate.docker.war:
	rm -f $(LOCATION_IMAGE)/*.war
	cd /Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-translator/../$(ORIGIN) ; mvn clean install -Pdocker

copy.war:
	cp /Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-translator/../$(ORIGIN)/target/$(WAR_FILE).war $(LOCATION_IMAGE)

dck.file:
	cp /Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-translator/tomcat.open9j.prov/Dockerfile $(LOCATION_IMAGE)

build:
	$(DOCKER) build -t ${TARGET_IMAGE} ${LOCATION_IMAGE}
	$(DOCKER) images


NOW=`date "+%Y-%m-%d-%H-%M"`

tag.now:
	$(DOCKER) tag ${TARGET_IMAGE} $(EXPORT_IMAGE):$(NOW)

tag:
	$(MAKE) -f target/classes/Makefile tag

push:
	$(MAKE) -f target/classes/Makefile push


run.8080:
	$(MAKE) setup.dir
	$(DOCKER) run -ti  -p 8080:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE}
run.9090:
	$(MAKE) setup.dir
	$(DOCKER) run -ti  -p 9090:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE}

setup.dir:
	mkdir -p $(DOCKER_RUN_DIR)/database $(DOCKER_RUN_DIR)/files $(DOCKER_RUN_DIR)/log



run.bash:
	$(DOCKER) run -ti  -p 9090:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE} /bin/bash

cli1:
	curl -D - -H "content-type: text/provenance-notation" -H "accept: application/json" -X POST --data-binary @src/test/resources/sculpture.provn http://localhost:9090/service/provapi/documents2


DOCKER_PROJECT=prov1
docker.up:
	docker-compose --project-name $(DOCKER_PROJECT) -f docker.conf/docker-compose.yml up -d

docker.ps:
	docker-compose --project-name $(DOCKER_PROJECT) -f docker.conf/docker-compose.yml ps

docker.stop:
	docker-compose --project-name $(DOCKER_PROJECT) -f docker.conf/docker-compose.yml stop

docker.rm:
	docker-compose --project-name $(DOCKER_PROJECT) -f docker.conf/docker-compose.yml rm

docker.logs:
	docker-compose --project-name $(DOCKER_PROJECT) -f docker.conf/docker-compose.yml logs

docker.restart:
	docker-compose --project-name $(DOCKER_PROJECT) -f docker.conf/docker-compose.yml restart prov-wrt

