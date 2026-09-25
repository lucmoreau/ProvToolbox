package org.openprovenance.prov.service.security.pac;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admits a profile produced by a GitHub client when it matches that client's allowlist
 * (see {@link GitHubAllowlisted}). Profiles from any other client are refused, so this
 * authorizer is only meaningful behind GitHub clients.
 */
public class GitHubAllowlistAuthorizer implements Authorizer {
    public static final String NAME = "githubAllowlist";
    public static final String ANY = "*";

    static Logger logger = LogManager.getLogger(GitHubAllowlistAuthorizer.class);

    private final Map<String, List<String>> allowlists = new HashMap<>();

    public GitHubAllowlistAuthorizer(SecurityConfiguration securityConfiguration) {
        for (ClientConfiguration configuration : securityConfiguration.configurations.values()) {
            if (configuration instanceof GitHubAllowlisted) {
                GitHubAllowlisted allowlisted = (GitHubAllowlisted) configuration;
                List<String> allowlist = allowlisted.getAllowlist() == null ? List.of() : allowlisted.getAllowlist();
                if (allowlist.isEmpty()) {
                    logger.warn("Empty allowlist for " + allowlisted.clientName() + ": no GitHub user is admitted");
                }
                allowlists.put(allowlisted.clientName(), allowlist);
            }
        }
    }

    @Override
    public boolean isAuthorized(WebContext context, SessionStore sessionStore, List<UserProfile> profiles) {
        for (UserProfile profile : profiles) {
            List<String> allowlist = allowlists.get(profile.getClientName());
            if (allowlist != null && matches(allowlist, profile)) {
                return true;
            }
        }
        logger.debug("GitHub allowlist refuses " + profiles);
        return false;
    }

    static boolean matches(List<String> allowlist, UserProfile profile) {
        String id = profile.getId();
        Object login = profile.getAttribute(GitHubProfiles.LOGIN);
        for (String entry : allowlist) {
            if (ANY.equals(entry)) return true;
            if (entry.startsWith(GitHubProfiles.PREFIX)) {
                if (entry.equals(id)) return true;
            } else if (login instanceof String && entry.equalsIgnoreCase((String) login)) {
                return true;
            }
        }
        return false;
    }
}
