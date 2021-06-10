FROM maven:3.6-jdk-11-slim

RUN apt-get update && apt-get -y install graphviz man rpm python rubygems make && gem install json-ld

WORKDIR /src

COPY pom.xml /src/

COPY modules-core/pom.xml /src/modules-core/
COPY modules-core/prov-model-scala/pom.xml /src/modules-core/prov-model-scala/
COPY modules-core/prov-model/pom.xml /src/modules-core/prov-model/
COPY modules-core/prov-jsonld-xml/pom.xml /src/modules-core/prov-jsonld-xml/

COPY modules-misc/pom.xml /src/modules-misc/
COPY modules-misc/prov-dot/pom.xml /src/modules-misc/prov-dot/
COPY modules-misc/prov-generator/pom.xml /src/modules-misc/prov-generator/

COPY modules-legacy/pom.xml /src/modules-legacy/
COPY modules-legacy/prov-json/pom.xml /src/modules-legacy/prov-json/
COPY modules-legacy/prov-xml/pom.xml /src/modules-legacy/prov-xml/
COPY modules-legacy/roundtrip/pom.xml /src/modules-legacy/roundtrip/
COPY modules-legacy/prov-n/pom.xml /src/modules-legacy/prov-n/
COPY modules-legacy/prov-sql/pom.xml /src/modules-legacy/prov-sql/
COPY modules-legacy/prov-rdf/pom.xml /src/modules-legacy/prov-rdf/

COPY modules-template/pom.xml /src/modules-template/
COPY modules-template/prov-template/pom.xml /src/modules-template/prov-template/
COPY modules-template/prov-template-compiler/pom.xml /src/modules-template/prov-template-compiler/

COPY modules-executable/pom.xml /src/modules-executable/
COPY modules-executable/prov-interop/pom.xml /src/modules-executable/prov-interop/
COPY modules-executable/toolbox/pom.xml /src/modules-executable/toolbox/

COPY modules-tutorial/pom.xml /src/modules-tutorial/pom.xml
COPY modules-tutorial/tutorial1/pom.xml /src/modules-tutorial/tutorial1/pom.xml
COPY modules-tutorial/tutorial2/pom.xml /src/modules-tutorial/tutorial2/pom.xml
COPY modules-tutorial/tutorial3/pom.xml /src/modules-tutorial/tutorial3/pom.xml
COPY modules-tutorial/tutorial4/pom.xml /src/modules-tutorial/tutorial4/pom.xml
COPY modules-tutorial/tutorial5/pom.xml /src/modules-tutorial/tutorial5/pom.xml
COPY modules-tutorial/tutorial6/pom.xml /src/modules-tutorial/tutorial6/pom.xml

COPY modules-services/pom.xml /src/modules-services/pom.xml
COPY modules-services/prov-service-translation/pom.xml /src/modules-services/prov-service-translation/pom.xml
COPY modules-services/prov-service-core/pom.xml /src/modules-services/prov-service-core/pom.xml
COPY modules-services/service-translator/pom.xml /src/modules-services/service-translator/pom.xml
COPY modules-services/prov-log/pom.xml /src/modules-services/prov-log/pom.xml
COPY modules-services/docker-service-translator/pom.xml /src/modules-services/docker-service-translator/pom.xml

RUN mvn dependency:go-offline --fail-never

COPY . /src

RUN sed -i '/docker-service-translator/d' modules-services/pom.xml
RUN mvn -f pom.xml package

