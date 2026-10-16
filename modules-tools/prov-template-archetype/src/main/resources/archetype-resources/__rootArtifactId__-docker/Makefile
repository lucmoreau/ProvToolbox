#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
${symbol_pound} The Docker image of the template service, its compose stack and its security tests.
${symbol_pound} See README.md: every confidential or site-specific value is supplied at run time,
${symbol_pound} from the environment or docker.conf/.env (copied from docker.conf/env.example).

SERVICE=${rootArtifactId}-docker
TPL_CONTEXT=${serviceContext}
EXPORT_IMAGE=provenance/templates

ENV_FILE=docker.conf/.env
COMPOSE_FILE=docker.conf/docker-compose.yml
DOCKER_PROJECT=tpl

${symbol_pound} the image built for this machine: linux/arm64 images carry the suffix -mac
ifneq (,$(filter arm64 aarch64,$(shell uname -m)))
NATIVE_IMAGE=$(SERVICE).temurin-mac
else
NATIVE_IMAGE=$(SERVICE).temurin
endif

${symbol_pound} the security mode of config.check and docker.up: the environment, else docker.conf/.env, else keycloak
MODE ?= $(or $(TPL_SECURITY_MODE),$(shell sed -n 's/^TPL_SECURITY_MODE=//p' $(ENV_FILE) 2>/dev/null | tail -1),keycloak)

${symbol_pound} the image of docker.up: the environment, else docker.conf/.env, else the image built for this machine
IMAGE ?= $(or $(TPL_IMAGE),$(shell sed -n 's/^TPL_IMAGE=//p' $(ENV_FILE) 2>/dev/null | tail -1),$(NATIVE_IMAGE))

SCRIPT_ENV=ENV_FILE=$(ENV_FILE) COMPOSE_FILE=$(COMPOSE_FILE) TPL_CONTEXT=$(TPL_CONTEXT) DEFAULT_IMAGE=$(NATIVE_IMAGE)

COMPOSE=docker compose --project-name $(DOCKER_PROJECT) -f $(COMPOSE_FILE) $(if $(wildcard $(ENV_FILE)),--env-file $(ENV_FILE))

DOCKER=docker

${symbol_pound} the service's scripts that obtain bearer tokens
SERVICE_BIN=../${rootArtifactId}-service/bin


help:
	@echo "make image                 build the image for this machine ($(NATIVE_IMAGE))"
	@echo "make image.all             build the linux/amd64 and linux/arm64 images"
	@echo "make config.check          check the configuration of the security mode (MODE=$(MODE))"
	@echo "make token.keycloak        a Keycloak access token into ~/.keycloak_token (lasts minutes)"
	@echo "make token.github          a GitHub token into ~/.github_token"
	@echo "make test.keycloak         security test of the container with Keycloak"
	@echo "make test.github           security test of the container with GitHub login"
	@echo "make test.security         both security tests"
	@echo "make docker.up | docker.ps | docker.stop | docker.rm | docker.down | docker.logs | docker.restart"
	@echo "                           the compose stack (project $(DOCKER_PROJECT)), configured by $(ENV_FILE)"


${symbol_pound} the service's WARs must be in the local repository (mvn install in the service folder); the root pom is installed here
image:
	mvn -N -q -f ../pom.xml install
	mvn install -Ddocker.make.target=build.native

image.all:
	mvn -N -q -f ../pom.xml install
	mvn install -Ddocker.make.target=build.all


${symbol_pound} configuration checks: what a deployment in the mode needs
config.check:
	@$(SCRIPT_ENV) bash src/test/sh/check-config.sh $(MODE)

${symbol_pound} what the security tests need, besides the deployment's configuration: the image and a token
config.check.keycloak:
	@$(SCRIPT_ENV) bash src/test/sh/check-config.sh keycloak --for-test

config.check.github:
	@$(SCRIPT_ENV) bash src/test/sh/check-config.sh github --for-test


${symbol_pound} bearer tokens for the security tests and for scripts, by the service's bin/ scripts, which read
${symbol_pound} their settings from the environment and ~/.openprovenance.credentials.sh (see README.md):
${symbol_pound} TPL_KEYCLOAK (the realm's token endpoint), TPL_KEYCLOAK_CLIENTID_DIRECT, TPL_KEYCLOAK_USERNAME and
${symbol_pound} TPL_KEYCLOAK_PASSWORD (asked for when empty); TPL_GITHUB_TOKEN, or else TPL_GITHUB_CLIENTID for the
${symbol_pound} device flow. A Keycloak token lasts minutes: make token.keycloak test.keycloak
token.keycloak:
	@bash $(SERVICE_BIN)/keycloak-get-token.sh
	@test -n "`find $(HOME)/.keycloak_token -mmin -1 2>/dev/null`" \
	  && echo "token.keycloak: a fresh token is in ~/.keycloak_token" \
	  || { echo "token.keycloak: no token obtained; check TPL_KEYCLOAK, TPL_KEYCLOAK_CLIENTID_DIRECT, TPL_KEYCLOAK_USERNAME and the password"; exit 1; }

token.github:
	@bash $(SERVICE_BIN)/github-get-token.sh
	@test -n "`find $(HOME)/.github_token -mmin -1 2>/dev/null`" \
	  && echo "token.github: a fresh token is in ~/.github_token" \
	  || { echo "token.github: no token obtained; set TPL_GITHUB_TOKEN, or TPL_GITHUB_CLIENTID for the device flow"; exit 1; }


${symbol_pound} security tests: each runs its own stack (port TEST_PORT, 18080 by default) and removes it
${symbol_pound} afterwards (KEEP=true leaves it running); TOKEN_FILE is the bearer token the service
${symbol_pound} should accept (default ~/.keycloak_token, ~/.github_token)
test.keycloak: config.check.keycloak
	@$(SCRIPT_ENV) bash src/test/sh/test-security.sh keycloak

test.github: config.check.github
	@$(SCRIPT_ENV) bash src/test/sh/test-security.sh github

test.security:
	$(MAKE) test.keycloak
	$(MAKE) test.github

${symbol_pound} stop a stack left by KEEP=true
test.down:
	docker compose --project-name tpl-security-test-$(MODE) -f $(COMPOSE_FILE) down -v


${symbol_pound} the deployment, configured by $(ENV_FILE); docker.up checks the configuration first
docker.up: config.check
	TPL_IMAGE=$(IMAGE) $(COMPOSE) up -d

docker.ps:
	$(COMPOSE) ps

docker.stop:
	$(COMPOSE) stop

docker.rm:
	$(COMPOSE) rm

docker.down:
	$(COMPOSE) down

docker.logs:
	$(COMPOSE) logs provtemplates

docker.restart:
	$(COMPOSE) restart provtemplates


NOW=`date "+%Y-%m-%d-%H-%M"`

tag.now:
	$(DOCKER) tag $(SERVICE).temurin $(EXPORT_IMAGE):$(NOW)

tag:
	$(MAKE) -f target/classes/Makefile tag

push:
	$(MAKE) -f target/classes/Makefile push


${symbol_pound} a self-signed certificate, for a local Keycloak served over TLS; docker.conf/certs is
${symbol_pound} git-ignored and the keystore password comes from KEYSTORE_PASS
gen.key:
	mkdir -p docker.conf/certs
	openssl req -newkey rsa:2048 -nodes -keyout docker.conf/certs/server.key.pem -x509 -days 3650 -out docker.conf/certs/server.crt.pem -subj '/CN=localhost'

pkcs12:
	openssl pkcs12 -export -in docker.conf/certs/server.crt.pem -inkey docker.conf/certs/server.key.pem -name localhost -out docker.conf/certs/server-PKCS-12.p12

trusted.store:
	@test -n "${symbol_dollar}${symbol_dollar}KEYSTORE_PASS" || { echo "KEYSTORE_PASS is not set"; exit 1; }
	keytool -import -deststorepass:env KEYSTORE_PASS -alias localhost.pub -file docker.conf/certs/server.crt.pem -storetype JKS -keystore docker.conf/certs/server.truststore
	keytool -importkeystore -deststorepass:env KEYSTORE_PASS -destkeystore docker.conf/certs/server.truststore -srckeystore docker.conf/certs/server-PKCS-12.p12 -srcstoretype PKCS12

list.store:
	keytool -list -keystore docker.conf/certs/server.truststore

.PHONY: help image image.all token.keycloak token.github config.check config.check.keycloak config.check.github test.keycloak test.github test.security test.down docker.up docker.ps docker.stop docker.rm docker.down docker.logs docker.restart tag.now tag push gen.key pkcs12 trusted.store list.store
