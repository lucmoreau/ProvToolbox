package org.openprovenance.prov.service.security.pac;

import org.pac4j.core.client.Client;

public interface ClientConfiguration {
    String DIRECT_BEARER_AUTH_CLIENT = "DirectBearerAuthClient";
    String KEYCLOAK_OIDC_CLIENT = "KeycloakOidcClient";

    Client configureClient();
}
