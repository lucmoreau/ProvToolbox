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

import java.util.Collections;

import static org.openprovenance.prov.service.TemplateService.tplKeycloakBaseuri;

public class SecurityConfigFactory implements ConfigFactory {

    static Logger logger = LogManager.getLogger(SecurityConfigFactory.class);

    @Override
    public Config build(final Object... parameters) {
        OidcClient oidcClient = configureKeycloakOidcClient();
        OidcConfiguration oidcConfiguration=oidcClient.getConfiguration();
        oidcConfiguration.setWithState(false);
        //ParameterClient parameterClient = configureParameterClient();
        DirectBearerAuthClient directBearerAuthClient = configureDirectBearerAuthClient(oidcClient, oidcConfiguration);
        Clients clients = new Clients("http://localhost:7072/ptl/callback", directBearerAuthClient, oidcClient);
        Config config = new Config(clients);
        logger.info("Config created" + config);
        return config;
    }

    public DirectBearerAuthClient configureDirectBearerAuthClient(OidcClient oidcClient, OidcConfiguration oidcConfiguration) {
        DirectBearerAuthClient client = new DirectBearerAuthClient();
        JwtAuthenticator tokenAuthenticator = new JwtAuthenticator();  //Collections.singletonList(new RSASignatureConfiguration())
        tokenAuthenticator.setRealmName("xplain");


        //client.setCredentialsExtractor(new BearerAuthExtractor());
        client.setCredentialsExtractor(new OidcExtractor(oidcConfiguration, oidcClient));
        //client.setAuthenticator(tokenAuthenticator);
        client.setAuthenticator(new OidcAuthenticator(oidcConfiguration, oidcClient));
        client.setRealmName("xplain");
        client.setProfileCreator(new OidcProfileCreator(oidcConfiguration, oidcClient));

        logger.info("DirectBearerAuthClient created: " + client);
        return client;
    }

    public ParameterClient configureParameterClient() {
        JwtAuthenticator tokenAuthenticator = new JwtAuthenticator();
        tokenAuthenticator.setRealmName("xplain");


        ParameterClient client = new ParameterClient("AUTHORIZATION", tokenAuthenticator);
        client.setSupportGetRequest(true);
        client.setSupportPostRequest(true);
        client.setCredentialsExtractor(new BearerAuthExtractor());

        logger.info("ParameterClient created: " + client);




        return client;
    }

    public OidcClient configureKeycloakOidcClient() {



        KeycloakOidcConfiguration config = new KeycloakOidcConfiguration();
        //config.setDiscoveryURI("https://openprovenance.org/auth/realms/xplain/.well-known/openid-configuration");
        //config.setDiscoveryURI(tplKeycloak);

        config.setClientId("assistant");
        config.setRealm("xplain");
        config.setBaseUri(tplKeycloakBaseuri);

        KeycloakOidcClient client = new KeycloakOidcClient(config);
        client.setCallbackUrl("http://localhost:7072/ptl/callback");
        logger.info("KeycloakOidcClient created");

        return client;
    }

}
