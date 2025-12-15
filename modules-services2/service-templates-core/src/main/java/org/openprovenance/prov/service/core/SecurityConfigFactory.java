package org.openprovenance.prov.service.core;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.stream.Collectors;

import static org.openprovenance.prov.service.core.TemplateService.*;

import org.openprovenance.prov.service.security.pac.SecurityConfiguration;
import org.openprovenance.prov.service.security.pac.Utils;
import org.pac4j.core.authorization.authorizer.CsrfAuthorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;

public class SecurityConfigFactory implements ConfigFactory {

    static Logger logger = LogManager.getLogger(SecurityConfigFactory.class);
    final private Utils utils = new Utils();

    public SecurityConfigFactory() {
        logger.info("########### SecurityConfigFactory constructor ###########");
    }


    @Override
    public Config build(final Object... parameters) {

        SecurityConfiguration securityConfiguration= TemplateService.securityConfiguration;
        if (securityConfiguration==null) {
            logger.info("No SecurityConfiguration: " + tplSecurityConfig);
            return new Config();
        }
        logger.info("SecurityConfiguration read " + securityConfiguration);
        List<Client> clients=securityConfiguration.configurations.keySet().stream().map(key -> securityConfiguration.configurations.get(key).configureClient()).collect(Collectors.toList());
        Config config = new Config(securityConfiguration.getCallbackUrl(), clients);

        /*
        System.setProperty("javax.net.ssl.keyStore", "/Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-templates/docker.conf/certs/server.truststore");
        System.setProperty("javax.net.ssl.keyStorePassword", "luc@pass");
        System.setProperty("javax.net.ssl.trustStore", "/Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-templates/docker.conf/certs/server.truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "luc@pass");
        System.setProperty("javax.net.debug", "ssl");

         */

        /*
        OidcClient oidcClient = configureKeycloakOidcClient(new KeycloakOidcClientConfiguration());
        DirectBearerAuthClient directBearerAuthClient = configureDirectBearerAuthClient(new DirectBearerAuthClientConfiguration());
        Config config = new Config(CALLBACK_URL,  directBearerAuthClient, oidcClient);
        */

        config.addAuthorizer("defaultAuthorizer", new RequireAnyRoleAuthorizer("provwriter"));
        config.addAuthorizer("csrf", new CsrfAuthorizer());
        logger.info("Config created " + config.getAuthorizers());
        //System.out.println("--- Config created " + config);
        return config;
    }



}
