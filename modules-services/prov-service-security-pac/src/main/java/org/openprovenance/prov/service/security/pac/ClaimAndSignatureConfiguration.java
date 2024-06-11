package org.openprovenance.prov.service.security.pac;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.text.ParseException;
import java.util.Objects;

class ClaimAndSignatureConfiguration extends RSASignatureConfiguration {

    // logger
    private static final Logger logger = LogManager.getLogger(ClaimAndSignatureConfiguration.class);

    private final String clientId;
    private final String realmUri;

    public ClaimAndSignatureConfiguration(String clientId, String realmUri)  {
        this.clientId=clientId;
        this.realmUri=realmUri;
    }

    @Override
    public boolean verify(SignedJWT signedJWT) {
        try {
            logger.info("verifying " + signedJWT.getJWTClaimsSet());
            JWTClaimsSet claims=signedJWT.getJWTClaimsSet();
            String azp = claims.getStringClaim("azp");
            if (!Objects.equals(azp, clientId)) {
                logger.info("azp claim is not direct client: " + azp);
                return false;
            } else {
                logger.info("azp claim is correct ");
            }
            String issuer = claims.getIssuer();
            if (!Objects.equals(issuer, realmUri)) {
                logger.info("issuer is not the same as expected baseUri: " + issuer);
                return false;
            } else {
                logger.info("issuer claim is correct ");
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}