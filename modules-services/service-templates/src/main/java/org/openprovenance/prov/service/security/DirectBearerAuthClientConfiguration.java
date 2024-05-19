package org.openprovenance.prov.service.security;

import org.pac4j.core.client.Client;

import static org.openprovenance.prov.service.security.SecurityConfigFactory.configureDirectBearerAuthClient;

public class DirectBearerAuthClientConfiguration implements ClientConfiguration {

    public String getType() {
        return DIRECT_BEARER_AUTH_CLIENT;
    }
    private String realm;
    private String signaturePublicKey;
    private String role;
    private String clientId;
    private String baseUri;

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getSignaturePublicKey() {
        return signaturePublicKey;
    }

    public void setSignaturePublicKey(String signaturePublicKey) {
        this.signaturePublicKey = signaturePublicKey;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public Client configureClient() {
        return configureDirectBearerAuthClient(this);
    }

    @Override
    public String toString() {
        return "DirectBearerAuthClientConfiguration{" +
                "realm='" + realm + '\'' +
                ", signaturePublicKey='" + signaturePublicKey + '\'' +
                ", role='" + role + '\'' +
                ", clientId='" + clientId + '\'' +
                ", baseUri='" + baseUri + '\'' +
                '}';
    }
}
