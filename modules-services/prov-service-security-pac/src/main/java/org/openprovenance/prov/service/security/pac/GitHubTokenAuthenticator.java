package org.openprovenance.prov.service.security.pac;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.profile.CommonProfile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Validates a GitHub token by calling GitHub's /user API with it, and builds the profile of
 * the user it belongs to. Successful lookups are cached, keyed by a SHA-256 digest of the
 * token, for a configurable time: a token revoked on GitHub is still accepted until its
 * entry expires. Failed lookups are not cached.
 */
public class GitHubTokenAuthenticator implements Authenticator {

    static Logger logger = LogManager.getLogger(GitHubTokenAuthenticator.class);

    static final String GITHUB_USER_API = "https://api.github.com/user";
    static final int MAX_CACHE_ENTRIES = 10000;

    private final String clientName;
    private final long ttlMillis;
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    private final ObjectMapper om = new ObjectMapper();
    private final Map<String, Entry> cache = new ConcurrentHashMap<>();

    static class Entry {
        final CommonProfile profile;
        final long expiresAt;

        Entry(CommonProfile profile, long expiresAt) {
            this.profile = profile;
            this.expiresAt = expiresAt;
        }
    }

    public GitHubTokenAuthenticator(String clientName, long ttlSeconds) {
        this.clientName = clientName;
        this.ttlMillis = ttlSeconds * 1000;
    }

    @Override
    public void validate(Credentials credentials, WebContext context, SessionStore sessionStore) {
        TokenCredentials tokenCredentials = (TokenCredentials) credentials;
        String token = tokenCredentials.getToken();
        if (token == null || token.isBlank()) {
            throw new CredentialsException("empty GitHub token");
        }
        String key = digest(token);
        long now = System.currentTimeMillis();
        Entry entry = cache.get(key);
        if (entry == null || entry.expiresAt <= now) {
            entry = new Entry(lookup(token), now + ttlMillis);
            if (ttlMillis > 0) {
                if (cache.size() >= MAX_CACHE_ENTRIES) {
                    cache.values().removeIf(e -> e.expiresAt <= now);
                }
                if (cache.size() < MAX_CACHE_ENTRIES) {
                    cache.put(key, entry);
                }
            }
        }
        tokenCredentials.setUserProfile(entry.profile);
    }

    CommonProfile lookup(String token) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(GITHUB_USER_API))
                .timeout(Duration.ofSeconds(10))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/vnd.github+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET()
                .build();
        HttpResponse<String> response;
        try {
            response = http.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            logger.warn("GitHub /user lookup failed: " + e.getMessage());
            throw new CredentialsException("GitHub /user lookup failed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new CredentialsException("GitHub /user lookup interrupted");
        }
        if (response.statusCode() != 200) {
            if (response.statusCode() != 401) {
                logger.warn("GitHub /user lookup returned " + response.statusCode());
            }
            throw new CredentialsException("GitHub token rejected: " + response.statusCode());
        }
        try {
            JsonNode user = om.readTree(response.body());
            JsonNode id = user.get("id");
            JsonNode login = user.get(GitHubProfiles.LOGIN);
            if (id == null || !id.canConvertToLong() || login == null) {
                throw new CredentialsException("GitHub /user response has no id or login");
            }
            return GitHubProfiles.profile(id.asText(), login.asText(), null, clientName);
        } catch (IOException e) {
            throw new CredentialsException("GitHub /user response is not JSON");
        }
    }

    static String digest(String token) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(token.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
