package org.openprovenance.prov.service.security.pac;

import org.pac4j.core.client.Client;

import java.util.List;

import static org.openprovenance.prov.service.security.pac.Utils.configureGitHubBearerAuthClient;

/**
 * Programmatic access with a GitHub token (personal access token or OAuth token) sent as
 * {@code Authorization: Bearer <token>}; the token is resolved to a GitHub user by the
 * GitHub /user API, and the result cached for {@link #cacheTtlSeconds}.
 */
public class GitHubBearerAuthClientConfiguration implements ClientConfiguration, GitHubAllowlisted {
    public static final long DEFAULT_CACHE_TTL_SECONDS = 300;

    private String realm = "github";
    private long cacheTtlSeconds = DEFAULT_CACHE_TTL_SECONDS;
    private List<String> allowlist;

    public String getType() {
        return GITHUB_BEARER_AUTH_CLIENT;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public long getCacheTtlSeconds() {
        return cacheTtlSeconds;
    }

    public void setCacheTtlSeconds(long cacheTtlSeconds) {
        this.cacheTtlSeconds = cacheTtlSeconds;
    }

    @Override
    public List<String> getAllowlist() {
        return allowlist;
    }

    public void setAllowlist(List<String> allowlist) {
        this.allowlist = allowlist;
    }

    @Override
    public String clientName() {
        return GITHUB_BEARER_AUTH_CLIENT;
    }

    @Override
    public Client configureClient() {
        return configureGitHubBearerAuthClient(this);
    }

    @Override
    public String toString() {
        return "GitHubBearerAuthClientConfiguration{" +
                "realm='" + realm + '\'' +
                ", cacheTtlSeconds=" + cacheTtlSeconds +
                ", allowlist=" + allowlist +
                '}';
    }
}
