package org.openprovenance.prov.service.security.pac;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/** The direct bearer client's verification of a Keycloak access token: the claims ({@code azp} one of the configured
 *  direct clients, the issuer the realm) AND the signature, against the realm's published signing keys (its JWKS
 *  endpoint, {@code <realmUri>/protocol/openid-connect/certs}), cached by key id and refreshed once when a token
 *  names a key the cache does not hold (a rotation). 2026-10-15: before this the override checked the claims only
 *  and never the signature, and the public key pasted into the configuration had gone stale against the realm's
 *  current key without anyone noticing. The configured key, when present, is the fallback for a token without a
 *  key id, or when the realm's endpoint cannot be read. */
public class ClaimAndSignatureConfiguration extends RSASignatureConfiguration {

    // logger
    private static final Logger logger = LogManager.getLogger(ClaimAndSignatureConfiguration.class);

    public static final String JWKS_PATH = "/protocol/openid-connect/certs";
    /** The least time between two reads of the realm's keys: an unknown key id is a rotation at most this often. */
    public static final long MIN_REFRESH_MILLIS = 60_000L;
    private static final int CONNECT_TIMEOUT_MS = 5_000, READ_TIMEOUT_MS = 5_000, SIZE_LIMIT_BYTES = 256 * 1024;

    /** The direct clients whose tokens this service accepts: the configured {@code clientId}, one id or a
     *  comma-separated list (a service with several client identities names each of them). */
    private final Set<String> clientIds;
    private final String realmUri;
    private final Map<String, RSAPublicKey> keysById = new ConcurrentHashMap<>();
    private volatile long lastRefresh = 0L;

    public ClaimAndSignatureConfiguration(String clientId, String realmUri)  {
        this.clientIds=parseClientIds(clientId);
        this.realmUri=realmUri;
    }

    /** {@code "a"} → {a}; {@code "a, b"} → {a, b}; blanks and empty items ignored; null → empty (nothing accepted). */
    public static Set<String> parseClientIds(String clientId) {
        if (clientId == null) return new LinkedHashSet<>();
        return Arrays.stream(clientId.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<String> getClientIds() {
        return clientIds;
    }

    /** The claims, then the signature. */
    @Override
    public boolean verify(SignedJWT signedJWT) {
        try {
            logger.debug("verifying " + signedJWT.getJWTClaimsSet());
            JWTClaimsSet claims=signedJWT.getJWTClaimsSet();
            String azp = claims.getStringClaim("azp");
            if (azp == null || !clientIds.contains(azp)) {
                logger.info("azp claim is not a direct client: " + azp + " (accepted: " + clientIds + ")");
                return false;
            } else {
                logger.debug("azp claim is correct ");
            }
            String issuer = claims.getIssuer();
            if (!Objects.equals(issuer, realmUri)) {
                logger.info("issuer is not the same as expected baseUri: " + issuer);
                return false;
            } else {
                logger.debug("issuer claim is correct ");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return verifySignature(signedJWT);
    }

    /** The signature against the realm's key named by the token's {@code kid}; the configured key when the token
     *  names none or the realm's keys cannot be read; refused when neither verifies. */
    boolean verifySignature(SignedJWT signedJWT) {
        String kid = signedJWT.getHeader().getKeyID();
        RSAPublicKey key = kid == null ? null : realmKey(kid);
        if (key != null) {
            boolean ok = verifyWith(signedJWT, key);
            if (!ok) logger.info("signature does not verify against the realm's key " + kid);
            return ok;
        }
        RSAPublicKey configured = getPublicKey();
        if (configured == null) {
            logger.info("no key to verify the signature with (kid=" + kid + ", realm keys: " + keysById.keySet() + ", no configured key)");
            return false;
        }
        boolean ok = verifyWith(signedJWT, configured);
        if (!ok) logger.info("signature does not verify against the configured key (kid=" + kid + ")");
        return ok;
    }

    private static boolean verifyWith(SignedJWT signedJWT, RSAPublicKey key) {
        try {
            return signedJWT.verify(new RSASSAVerifier(key));
        } catch (JOSEException e) {
            logger.info("signature verification failed: " + e.getMessage());
            return false;
        }
    }

    /** The realm's signing key with this id: from the cache, else after one refresh (rate-limited). */
    RSAPublicKey realmKey(String kid) {
        RSAPublicKey key = keysById.get(kid);
        if (key != null) return key;
        synchronized (this) {
            key = keysById.get(kid);
            if (key != null) return key;
            long now = System.currentTimeMillis();
            if (now - lastRefresh < MIN_REFRESH_MILLIS && lastRefresh != 0L) {
                logger.info("unknown key id " + kid + " and the realm's keys were read less than " + (MIN_REFRESH_MILLIS / 1000) + " s ago");
                return null;
            }
            lastRefresh = now;
            refreshRealmKeys();
            return keysById.get(kid);
        }
    }

    /** Reads the realm's JWKS; a failure leaves the cache as it was and is logged. */
    void refreshRealmKeys() {
        if (realmUri == null) return;
        String url = realmUri + JWKS_PATH;
        try {
            JWKSet set = JWKSet.load(new URL(url), CONNECT_TIMEOUT_MS, READ_TIMEOUT_MS, SIZE_LIMIT_BYTES);
            int n = 0;
            for (JWK jwk : set.getKeys()) {
                if (!(jwk instanceof RSAKey) || jwk.getKeyID() == null) continue;
                if (jwk.getKeyUse() != null && !KeyUse.SIGNATURE.equals(jwk.getKeyUse())) continue;
                keysById.put(jwk.getKeyID(), ((RSAKey) jwk).toRSAPublicKey());
                n++;
            }
            logger.info("realm signing keys read from " + url + ": " + n + " (" + keysById.keySet() + ")");
        } catch (IOException | ParseException | JOSEException e) {
            logger.warn("could not read the realm's keys from " + url + ": " + e.getMessage());
        }
    }
}
