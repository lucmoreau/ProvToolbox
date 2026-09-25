package org.openprovenance.prov.service.security.pac;

import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;

import java.util.Map;
import java.util.Optional;

/**
 * GitHub users are identified as {@code github:<numeric id>}: the numeric id is stable across
 * renames and does not disclose the login. The profile carries no username, so that the
 * servlet principal (pac4j's Pac4JPrincipal uses the username when present, the id otherwise)
 * is that identifier; the login is kept as the {@code login} attribute for the allowlist.
 */
public class GitHubProfiles {
    public static final String PREFIX = "github:";
    public static final String LOGIN = "login";

    static public CommonProfile profile(String githubId, String login, Map<String, Object> attributes, String clientName) {
        CommonProfile profile = new CommonProfile();
        profile.setId(PREFIX + githubId);
        if (attributes != null) {
            profile.addAttributes(attributes);
        }
        profile.removeAttribute("id");
        profile.addAttribute(LOGIN, login);
        profile.setClientName(clientName);
        return profile;
    }

    /** Rewrites the profile produced by pac4j's GitHubClient into the form above. */
    public static class PrincipalGenerator implements AuthorizationGenerator {
        @Override
        public Optional<UserProfile> generate(org.pac4j.core.context.WebContext context,
                                              org.pac4j.core.context.session.SessionStore sessionStore,
                                              UserProfile gitHubProfile) {
            String login = (String) gitHubProfile.getAttribute(LOGIN);
            return Optional.of(profile(gitHubProfile.getId(), login, gitHubProfile.getAttributes(), gitHubProfile.getClientName()));
        }
    }
}
