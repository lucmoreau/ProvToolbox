package org.openprovenance.prov.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;

import static org.openprovenance.prov.service.TemplateService.tplKeycloak;
import static org.openprovenance.prov.service.TemplateService.tplKeycloakBaseuri;

public class SecurityConfigFactory implements ConfigFactory {

    static Logger logger = LogManager.getLogger(SecurityConfigFactory.class);

    @Override
    public Config build(final Object... parameters) {
        OidcClient oidcClient = configureKeycloakOidcClient();
        Clients clients = new Clients("http://localhost:7072/ptl/callback", oidcClient);
        Config config = new Config(clients);
        logger.info("Config created" + config);
        return config;
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
