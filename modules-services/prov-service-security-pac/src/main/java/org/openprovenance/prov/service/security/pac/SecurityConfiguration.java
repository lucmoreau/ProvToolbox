package org.openprovenance.prov.service.security.pac;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;

public class SecurityConfiguration {

    private String callbackUrl;

    /**
     * Principals that may read every record, whoever wrote it (the access control on records — a record visible to the
     * principal that wrote it, or through an access_control grant — does not apply to them). For a client authenticated
     * with client credentials the principal is its service account, e.g. {@code service-account-<client id>}. Empty by
     * default: no principal reads beyond its own records and grants.
     */
    private List<String> readers = List.of();

    public List<String> getReaders() {
        return readers;
    }

    public void setReaders(List<String> readers) {
        this.readers = readers == null ? List.of() : List.copyOf(readers);
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
    
    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "@type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DirectBearerAuthClientConfiguration.class, name = ClientConfiguration.DIRECT_BEARER_AUTH_CLIENT),
            @JsonSubTypes.Type(value = KeycloakOidcClientConfiguration.class, name = ClientConfiguration.KEYCLOAK_OIDC_CLIENT),
            @JsonSubTypes.Type(value = GitHubClientConfiguration.class, name = ClientConfiguration.GITHUB_CLIENT),
            @JsonSubTypes.Type(value = GitHubBearerAuthClientConfiguration.class, name = ClientConfiguration.GITHUB_BEARER_AUTH_CLIENT)
    })
    public Map<String,ClientConfiguration> configurations;

    @Override
    public String toString() {
        return "SecurityConfiguration{" +
                "callbackUrl='" + callbackUrl + '\'' +
                ", readers=" + readers +
                ", configurations=" + configurations +
                '}';
    }
}
