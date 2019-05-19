FROM maven:3.6-alpine

RUN apk update && apk add graphviz man rpm python cdrkit

WORKDIR /src

COPY pom.xml /src/
COPY prov-dot/pom.xml /src/prov-dot/
COPY prov-generator/pom.xml /src/prov-generator/
COPY prov-interop/pom.xml /src/prov-interop/
COPY prov-json/pom.xml /src/prov-json/
COPY prov-model/pom.xml /src/prov-model/
COPY prov-n/pom.xml /src/prov-n/
COPY prov-rdf/pom.xml /src/prov-rdf/
COPY prov-sql/pom.xml /src/prov-sql/
COPY prov-template/pom.xml /src/prov-template/
COPY prov-xml/pom.xml /src/prov-xml/
COPY toolbox/pom.xml /src/toolbox/

COPY ./tutorial/tutorial1/pom.xml /src/tutorial/tutorial1/pom.xml
COPY ./tutorial/tutorial2/pom.xml /src/tutorial/tutorial2/pom.xml
COPY ./tutorial/tutorial3/pom.xml /src/tutorial/tutorial3/pom.xml
COPY ./tutorial/tutorial4/pom.xml /src/tutorial/tutorial4/pom.xml
COPY ./tutorial/tutorial5/pom.xml /src/tutorial/tutorial5/pom.xml
COPY ./tutorial/tutorial6/pom.xml /src/tutorial/tutorial6/pom.xml

RUN mvn -f pom.xml dependency:resolve --fail-never

COPY . /src

RUN mvn package

