# Use latest alpine/tomcat image as the base
#FROM tomcat:8.5-jre8-alpine
#FROM tomcat:9-jdk13-openjdk-oracle
#FROM tomcat:9-jdk11-openjdk-slim
FROM tomcat:9-jdk12-adoptopenjdk-openj9


MAINTAINER Luc Moreau "luc.moreau@kcl.ac.uk"


# install dependencies
RUN apt-get update; \
	apt-get install -y --no-install-recommends curl graphviz ; \
        apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false



# install dependencies
#RUN apk add --no-cache curl

# Expose the port we're interested in
EXPOSE 8080

# Directories used by the service
RUN mkdir -p /home/pservice/log/ /home/pservice/files/ /home/pservice/database/

#
VOLUME /home/pservice

RUN rm -r -f /usr/local/tomcat/webapps/ROOT

ADD service-translator-0.8.1-SNAPSHOT-docker.war /usr/local/tomcat/webapps/ROOT.war


CMD ["catalina.sh", "run"]
