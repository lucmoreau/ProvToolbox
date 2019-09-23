# Use latest alpine/tomcat image as the base
FROM tomcat:8.5-jre8-alpine
#FROM tomcat:9.0.22-jdk12-openjdk-oracle 


MAINTAINER Luc Moreau "luc.moreau@kcl.ac.uk"

#RUN apk add --no-cache java-1.8.0-openjdk-devel

# install dependencies
RUN apk add --no-cache graphviz

# install dependencies
RUN apk add --no-cache curl

# Expose the port we're interested in
EXPOSE 8080

# Directories used by the service
RUN mkdir -p /home/pservice/log/ /home/pservice/files/ /home/pservice/database/

#
VOLUME /home/pservice


RUN rm -r -f /usr/local/tomcat/webapps/ROOT

ADD service-translator-0.8.1-SNAPSHOT-docker.war /usr/local/tomcat/webapps/ROOT.war


CMD ["catalina.sh", "run"]
