package org.openprovenance.prov.service.security;

import org.pac4j.core.client.Client;

import static org.openprovenance.prov.service.security.SecurityConfigFactory.configureKeycloakOidcClient;

public class KeycloakOidcClientConfiguration implements ClientConfiguration {
    private String clientId;
    private String realm;
    private String baseUri;
    private String callbackUrl;

    public String getType() {
        return KEYCLOAK_OIDC_CLIENT;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Override
    public Client configureClient() {
        return configureKeycloakOidcClient(new KeycloakOidcClientConfiguration());
    }

    @Override
    public String toString() {
        return "KeycloakOidcClientConfiguration{" +
                "clientId='" + clientId + '\'' +
                ", realm='" + realm + '\'' +
                ", baseUri='" + baseUri + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                '}';
    }
}
