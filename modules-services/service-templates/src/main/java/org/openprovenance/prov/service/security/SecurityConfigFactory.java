package org.openprovenance.prov.service.security;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.TemplateService;
import org.pac4j.core.authorization.authorizer.CsrfAuthorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Client;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.credentials.extractor.BearerAuthExtractor;
import org.pac4j.http.client.direct.DirectBearerAuthClient;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;


import java.util.List;
import java.util.stream.Collectors;

import static org.openprovenance.prov.service.TemplateService.*;
import static org.openprovenance.prov.service.security.Utils.getRsaPublicKey;

public class SecurityConfigFactory implements ConfigFactory {

    static Logger logger = LogManager.getLogger(SecurityConfigFactory.class);
    final private Utils utils = new Utils();



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
        System.out.println("--- Config created " + config);
        return config;
    }




    static public DirectBearerAuthClient configureDirectBearerAuthClient(DirectBearerAuthClientConfiguration config) {
        DirectBearerAuthClient client = new DirectBearerAuthClient();
        JwtAuthenticator tokenAuthenticator = new JwtAuthenticator();
        tokenAuthenticator.setRealmName(config.getRealm());
        RSASignatureConfiguration signatureConfiguration;
        signatureConfiguration= new ClaimAndSignatureConfiguration(config.getClientId(), config.getRealmUri());
        signatureConfiguration.setPublicKey(getRsaPublicKey(config.getSignaturePublicKey()));
        signatureConfiguration.setPrivateKey(null);
        tokenAuthenticator.addSignatureConfiguration(signatureConfiguration);
        client.setRealmName(config.getRealm());
        client.setAuthenticator(tokenAuthenticator);
        client.setCredentialsExtractor(new BearerAuthExtractor());
        client.addAuthorizationGenerator(new RoleAuthorizationGenerator(config.getRole()));

        logger.info("DirectBearerAuthClient created: " + client);
        return client;
    }



    static public KeycloakOidcClient configureKeycloakOidcClient(KeycloakOidcClientConfiguration config) {
        KeycloakOidcConfiguration keycloakOidcConfiguration = new KeycloakOidcConfiguration();
        //keycloakOidcConfiguration.setDiscoveryURI("https://openprovenance.org/auth/realms/xplain/.well-known/openid-configuration");
        //keycloakOidcConfiguration.setDiscoveryURI(tplKeycloak);

        keycloakOidcConfiguration.setClientId(config.getClientId());
        keycloakOidcConfiguration.setRealm(config.getRealm());
        keycloakOidcConfiguration.setBaseUri(config.getBaseUri());

        KeycloakOidcClient client = new KeycloakOidcClient(keycloakOidcConfiguration);
        client.setCallbackUrl(config.getCallbackUrl());


        logger.info("KeycloakOidcClient created " + keycloakOidcConfiguration);

        return client;
    }

}
