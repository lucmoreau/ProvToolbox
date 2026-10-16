package org.openprovenance.prov.service.security.pac;

/**
 * A client configuration whose users are admitted by an allowlist. The allowlist is not held in
 * the configuration file: {@link #getAllowlistEnv()} names a system property or environment
 * variable whose value is a comma-separated list of entries. An entry is a GitHub login
 * (case-insensitive), {@code github:<numeric id>} (survives account renames), or {@code *} for any
 * GitHub user. The variable must be set; an empty value admits nobody.
 */
public interface GitHubAllowlisted {
    String getAllowlistEnv();

    /** name of the pac4j client whose profiles this allowlist applies to */
    String clientName();
}
