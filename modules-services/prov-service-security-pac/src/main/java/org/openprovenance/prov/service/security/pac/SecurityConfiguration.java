package org.openprovenance.prov.service.security.pac;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

public class SecurityConfiguration {

    private String callbackUrl;

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "@type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DirectBearerAuthClientConfiguration.class, name = ClientConfiguration.DIRECT_BEARER_AUTH_CLIENT),
            @JsonSubTypes.Type(value = KeycloakOidcClientConfiguration.class, name = ClientConfiguration.KEYCLOAK_OIDC_CLIENT)
    })
    public Map<String,ClientConfiguration> configurations;

    @Override
    public String toString() {
        return "SecurityConfiguration{" +
                "callbackUrl='" + callbackUrl + '\'' +
                ", configurations=" + configurations +
                '}';
    }
}
