# Use latest alpine/tomcat image as the base
#FROM tomcat:8.5-jre8-alpine
#FROM tomcat:9-jdk13-openjdk-oracle
#FROM tomcat:9-jdk11-openjdk-slim
#FROM tomcat:9-jdk12-adoptopenjdk-openj9
#FROM tomcat:9-jdk11-temurin-jammy
#FROM tomcat:10.1.13-jdk17-temurin-jammy
FROM tomcat:10.1.24-jdk17-temurin-jammy

# or should it be jre??


MAINTAINER Luc Moreau "luc.moreau@kcl.ac.uk"


# install dependencies
RUN apt-get update; \
	apt-get install -y --no-install-recommends curl graphviz redis; \
        apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false



# install dependencies
#RUN apk add --no-cache curl

# Expose the port we're interested in
EXPOSE 8080

# Directories used by the service
RUN mkdir -p ${upload.directory}/log/ ${upload.directory}/files/ ${upload.directory}/database/

#
VOLUME ${upload.directory}

#RUN rm -r -f /usr/local/tomcat/webapps/ROOT




ADD ${origin.project}-${project.version}.war /usr/local/tomcat/webapps/${service.context}.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
