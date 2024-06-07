package org.openprovenance.prov.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.openprovenance.prov.service.TemplateService.NO_SECURITY_CONFIG;

public class Utils {
    static RSAPublicKey getRsaPublicKey(String publicKeyString) {
        KeyFactory kf = null;
        RSAPublicKey pubKey = null;
        try {
            kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));
            pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        return pubKey;
    }

    ObjectMapper om = new ObjectMapper();

    public SecurityConfiguration readSecurityConfiguration(String configFileName) {
        try {
            if (NO_SECURITY_CONFIG.equals(configFileName)) {
                return null;
            }
            return om.readValue(new File(configFileName), SecurityConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
