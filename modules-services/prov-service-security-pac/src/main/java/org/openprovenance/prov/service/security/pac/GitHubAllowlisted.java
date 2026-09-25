package org.openprovenance.prov.service.security.pac;

import java.util.List;

/**
 * A client configuration whose users are admitted by an allowlist. An entry is a GitHub
 * login (case-insensitive), {@code github:<numeric id>} (survives account renames), or
 * {@code *} for any GitHub user. A missing or empty allowlist admits nobody.
 */
public interface GitHubAllowlisted {
    List<String> getAllowlist();

    /** name of the pac4j client whose profiles this allowlist applies to */
    String clientName();
}
