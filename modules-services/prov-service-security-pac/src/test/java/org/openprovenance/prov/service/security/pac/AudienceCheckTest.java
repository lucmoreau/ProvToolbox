package org.openprovenance.prov.service.security.pac;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/** D5 (nextgen deployment design B2): a bearer token must be addressed to this service (its {@code aud}) when the
 *  configuration names an audience; without one the audience is not checked, as before. */
public class AudienceCheckTest {

    private static final String REALM = "https://id.example/realms/provmind";
    private final KeyPair kp;

    public AudienceCheckTest() throws Exception {
        KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
        g.initialize(2048);
        kp = g.generateKeyPair();
    }

    private SignedJWT token(String azp, String... aud) throws Exception {
        JWTClaimsSet claims = new JWTClaimsSet.Builder().issuer(REALM).claim("azp", azp).audience(Arrays.asList(aud))
                .expirationTime(new Date(System.currentTimeMillis() + 60_000)).build();
        SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);   // no kid: the configured key verifies
        jwt.sign(new RSASSASigner(kp.getPrivate()));
        return jwt;
    }

    private ClaimAndSignatureConfiguration config(String audience) {
        ClaimAndSignatureConfiguration c = new ClaimAndSignatureConfiguration("chronicle-weaver, chronicle-gateway", REALM, audience);
        c.setPublicKey((RSAPublicKey) kp.getPublic());
        return c;
    }

    @Test
    public void aTokenAddressedToTheStoreIsAccepted() throws Exception {
        assertTrue(config("provenance-store").verify(token("chronicle-weaver", "provenance-store", "account")));
    }

    @Test
    public void aTokenForAnotherServiceIsRefused() throws Exception {
        // an accepted client, the right realm and signature, but the token was minted for the agent gateway
        assertFalse(config("provenance-store").verify(token("chronicle-gateway", "agent-gateway")));
        assertFalse(config("provenance-store").verify(token("chronicle-weaver")));
    }

    @Test
    public void withoutAnAudienceConfiguredItIsNotChecked() throws Exception {
        assertTrue(config(null).verify(token("chronicle-weaver", "agent-gateway")));
        assertTrue(config(" ").verify(token("chronicle-weaver")));
    }

    @Test
    public void theOtherChecksStillApply() throws Exception {
        assertFalse("a client not accepted", config("provenance-store").verify(token("ptm-direct", "provenance-store")));
    }
}
