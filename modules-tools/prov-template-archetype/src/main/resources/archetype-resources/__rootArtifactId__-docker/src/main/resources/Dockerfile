#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
${symbol_pound} The template service on Tomcat. Nothing confidential is built into the image:
${symbol_pound} the security mode, the identity provider and the database credentials are
${symbol_pound} supplied when the container starts (see README.md in the docker folder).

${symbol_pound} The web application, exploded so that the entry point can install the deployment
${symbol_pound} descriptor of the security mode chosen at start-up (TPL_SECURITY_MODE): the three
${symbol_pound} WARs of the build (secure, github, unsecure) differ only by WEB-INF/web.xml.
${symbol_pound} jetty.webapp.config.properties names a folder of the build machine, unused by Tomcat.
FROM tomcat:10.1.48-jdk25-temurin-noble AS webapp
COPY ${origin.project}-${version}-secure.war /tmp/service.war
RUN mkdir /webapp \
    && cd /webapp \
    && jar xf /tmp/service.war \
    && rm -f WEB-INF/classes/properties/jetty.webapp.config.properties


FROM tomcat:10.1.48-jdk25-temurin-noble


LABEL org.opencontainers.image.authors="Luc Moreau <luc.moreau@sussex.ac.uk>"


${symbol_pound} install dependencies
RUN apt-get update; \
	apt-get install -y --no-install-recommends curl graphviz redis; \
        apt-get purge -y --auto-remove -o APT::AutoRemove::RecommendsImportant=false; \
        rm -rf /var/lib/apt/lists/*

EXPOSE 8080

${symbol_pound} Directories used by the service
RUN mkdir -p ${upload.directory}/log/ ${upload.directory}/files/ ${upload.directory}/database/ /etc/dictionary /opt/provtemplates/run

VOLUME ${upload.directory}

ENV TPL_CONTEXT=${serviceContext} \
    TPL_WEBAPP_DIR=/usr/local/tomcat/webapps${serviceContext}

COPY --from=webapp /webapp/ /usr/local/tomcat/webapps${serviceContext}/
${symbol_pound} the lexicon of the explanations (LexAccess 2013), named by XPLAIN_LEXICON
COPY lexAccess2013.data lexAccess2013.script /etc/dictionary/
${symbol_pound} web-keycloak.xml, web-github.xml and web-none.xml, from the three WARs
COPY security/ /opt/provtemplates/security/
COPY docker-entrypoint.sh /usr/local/bin/docker-entrypoint.sh
RUN chmod 755 /usr/local/bin/docker-entrypoint.sh

ENTRYPOINT ["/usr/local/bin/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
