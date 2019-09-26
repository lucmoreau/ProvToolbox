
TARGET_IMAGE=tomcat.open9j.prov

DOCKER_RUN_DIR=$(HOME)/docker/log-dir



# see http://www.projectatomic.io/blog/2015/08/why-we-dont-let-non-root-users-run-docker-in-centos-fedora-or-rhel/
# to run docker as lavm
DOCKER=sudo /usr/bin/docker
DOCKER=docker

build.all: copy.war build

generate.docker.war:
	rm $(TARGET_IMAGE)/service*.war
	cd ../service-translator ; mvn clean install -Pdocker

copy.war:
	$(MAKE) generate.docker.war
	cp ../service-translator/target/service-translator-0.8.1-SNAPSHOT-docker.war $(TARGET_IMAGE)


build:
	$(DOCKER) build -t ${TARGET_IMAGE} ${TARGET_IMAGE}
	$(DOCKER) images

run.8080:
	$(MAKE) setup.dir
	$(DOCKER) run -ti  -p 8080:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE}

setup.dir:
	mkdir -p $(DOCKER_RUN_DIR)/database $(DOCKER_RUN_DIR)/files $(DOCKER_RUN_DIR)/log



run.bash:
	$(DOCKER) run -ti  -p 9090:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t ${TARGET_IMAGE} /bin/bash


run.bash2:
	$(DOCKER) run -ti  -p 9090:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t tomcat:9-jdk11-openjdk-slim /bin/bash

run.bash3:
	$(DOCKER) run -ti  -p 9090:8080  -i -v $(DOCKER_RUN_DIR):/home/pservice -t tomcat:9-jdk12-adoptopenjdk-openj9 /bin/bash


