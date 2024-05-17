package org.openprovenance.prov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.credentials.extractor.BearerAuthExtractor;
import org.pac4j.http.client.direct.DirectBearerAuthClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.oidc.credentials.authenticator.OidcAuthenticator;
import org.pac4j.oidc.credentials.extractor.OidcExtractor;
import org.pac4j.oidc.profile.creator.OidcProfileCreator;


import static org.openprovenance.prov.service.TemplateService.*;

public class SecurityConfigFactory implements ConfigFactory {

    public static final String CALLBACK_URL = provHost + "/callback";
    static Logger logger = LogManager.getLogger(SecurityConfigFactory.class);



    @Override
    public Config build(final Object... parameters) {

        //System.setProperty("javax.net.ssl.keyStore", "/Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-templates/docker.conf/certs/server.truststore");
       // System.setProperty("javax.net.ssl.keyStorePassword", "luc@pass");
      //  System.setProperty("javax.net.ssl.trustStore", "/Users/luc/IdeaProjects/ProvToolbox/modules-services/docker-service-templates/docker.conf/certs/server.truststore");
        //System.setProperty("javax.net.ssl.trustStorePassword", "luc@pass");
        //System.setProperty("javax.net.debug", "ssl");

        // first one for BearerAuth, second one for Oidc
        KeycloakOidcClient oidcClient1 = configureKeycloakOidcClient();
        OidcClient oidcClient2 = configureKeycloakOidcClient();
        OidcConfiguration oidcConfiguration1=oidcClient1.getConfiguration();
        //System.out.println("OIDC Configuration: " + oidcClient2.getConfiguration().getSSLFactory());
        //oidcClient2.getConfiguration().setSSLFactory(null);
         oidcConfiguration1.setWithState(false);
        //oidcConfiguration1.setUseNonce(true);
        //ParameterClient parameterClient = configureParameterClient();
        DirectBearerAuthClient directBearerAuthClient = configureDirectBearerAuthClient(oidcClient1, oidcClient1.getConfiguration());
       // System.out.println("preferred algo " + oidcConfiguration1.getPreferredJwsAlgorithm());
        Clients clients = new Clients(CALLBACK_URL,  directBearerAuthClient, oidcClient2); //directBearerAuthClient,oidcClient2
        Config config = new Config(clients);
        logger.info("Config created" + config);
        return config;
    }

    public DirectBearerAuthClient configureDirectBearerAuthClient(OidcClient oidcClient, OidcConfiguration oidcConfiguration) {
        DirectBearerAuthClient client = new DirectBearerAuthClient();
        JwtAuthenticator tokenAuthenticator = new JwtAuthenticator();  //Collections.singletonList(new RSASignatureConfiguration())
        tokenAuthenticator.setRealmName(tplKeycloakRealm);

        client.setRealmName(tplKeycloakRealm);
        //client.setAuthenticator(tokenAuthenticator);
        //client.setCredentialsExtractor(new BearerAuthExtractor());
        client.setCredentialsExtractor(new OidcExtractor(oidcConfiguration, oidcClient));
        client.setAuthenticator(new OidcAuthenticator(oidcConfiguration, oidcClient));
        client.setProfileCreator(new OidcProfileCreator(oidcConfiguration, oidcClient));

        logger.info("DirectBearerAuthClient created: " + client);
        return client;
    }

    public ParameterClient configureParameterClient() {
        JwtAuthenticator tokenAuthenticator = new JwtAuthenticator();
        tokenAuthenticator.setRealmName(tplKeycloakRealm);


        ParameterClient client = new ParameterClient("AUTHORIZATION", tokenAuthenticator);
        client.setSupportGetRequest(true);
        client.setSupportPostRequest(true);
        client.setCredentialsExtractor(new BearerAuthExtractor());

        logger.info("ParameterClient created: " + client);




        return client;
    }

    public KeycloakOidcClient configureKeycloakOidcClient() {



        KeycloakOidcConfiguration config = new KeycloakOidcConfiguration();
        //config.setDiscoveryURI("https://openprovenance.org/auth/realms/xplain/.well-known/openid-configuration");
        //config.setDiscoveryURI(tplKeycloak);

        config.setClientId(tplKeycloakClientId);
        config.setRealm(tplKeycloakRealm);
        config.setBaseUri(tplKeycloakBaseuri);

        KeycloakOidcClient client = new KeycloakOidcClient(config);


        client.setCallbackUrl(CALLBACK_URL);

        logger.info("KeycloakOidcClient created " + tplKeycloakClientId + " " + tplKeycloakRealm + " " + tplKeycloakBaseuri + " " + CALLBACK_URL +  client.getConfiguration().getDiscoveryURI() );

        return client;
    }

}
