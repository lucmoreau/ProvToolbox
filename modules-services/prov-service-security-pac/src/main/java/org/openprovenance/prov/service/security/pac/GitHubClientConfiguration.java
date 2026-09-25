package org.openprovenance.prov.service.security.pac;

import org.pac4j.core.client.Client;

import java.util.List;

import static org.openprovenance.prov.service.security.pac.Utils.configureGitHubClient;

/**
 * Interactive (browser) login through a GitHub OAuth App. The client secret is never
 * held in the configuration file: {@link #clientSecretEnv} names a system property or
 * environment variable holding it.
 */
public class GitHubClientConfiguration implements ClientConfiguration, GitHubAllowlisted {
    public static final String DEFAULT_SCOPE = "read:user";

    private String clientId;
    private String clientSecretEnv;
    private String callbackUrl;
    private String scope = DEFAULT_SCOPE;
    private List<String> allowlist;

    public String getType() {
        return GITHUB_CLIENT;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecretEnv() {
        return clientSecretEnv;
    }

    public void setClientSecretEnv(String clientSecretEnv) {
        this.clientSecretEnv = clientSecretEnv;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
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
        return GITHUB_CLIENT;
    }

    @Override
    public Client configureClient() {
        return configureGitHubClient(this);
    }

    @Override
    public String toString() {
        return "GitHubClientConfiguration{" +
                "clientId='" + clientId + '\'' +
                ", clientSecretEnv='" + clientSecretEnv + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", scope='" + scope + '\'' +
                ", allowlist=" + allowlist +
                '}';
    }
}
