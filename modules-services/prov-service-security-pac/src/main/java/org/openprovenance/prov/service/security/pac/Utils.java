package org.openprovenance.prov.service.security.pac;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


import org.pac4j.core.credentials.extractor.BearerAuthExtractor;
import org.pac4j.http.client.direct.DirectBearerAuthClient;
import org.pac4j.jwt.config.signature.RSASignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.oidc.client.KeycloakOidcClient;
import org.pac4j.oidc.config.KeycloakOidcConfiguration;


public class Utils {

    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Utils.class);

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
            return om.readValue(new File(configFileName), SecurityConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*
    public SecurityConfiguration readSecurityConfiguration(String configFileName) {
        try {
            if (TemplateService.NO_SECURITY_CONFIG.equals(configFileName)) {
                return null;
            }
            return om.readValue(new File(configFileName), SecurityConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

     */


    static public DirectBearerAuthClient configureDirectBearerAuthClient(DirectBearerAuthClientConfiguration config) {
        DirectBearerAuthClient client = new DirectBearerAuthClient();
        JwtAuthenticator tokenAuthenticator = new JwtAuthenticator();
        tokenAuthenticator.setRealmName(config.getRealm());
        RSASignatureConfiguration signatureConfiguration;
        signatureConfiguration= new ClaimAndSignatureConfiguration(config.getClientId(), config.getRealmUri());
        signatureConfiguration.setPublicKey(Utils.getRsaPublicKey(config.getSignaturePublicKey()));
        signatureConfiguration.setPrivateKey(null);
        tokenAuthenticator.addSignatureConfiguration(signatureConfiguration);
        client.setRealmName(config.getRealm());
        client.setAuthenticator(tokenAuthenticator);
        client.setCredentialsExtractor(new BearerAuthExtractor());
        client.addAuthorizationGenerator(new RoleAuthorizationGenerator(config.getRole()));

        logger.info("DirectBearerAuthClient created: " + client);
        return client;
    }



    static public KeycloakOidcClient configureKeycloakOidcClient(KeycloakOidcClientConfiguration config) {
        KeycloakOidcConfiguration keycloakOidcConfiguration = new KeycloakOidcConfiguration();
        //keycloakOidcConfiguration.setDiscoveryURI("https://openprovenance.org/auth/realms/xplain/.well-known/openid-configuration");
        //keycloakOidcConfiguration.setDiscoveryURI(tplKeycloak);

        keycloakOidcConfiguration.setClientId(config.getClientId());
        keycloakOidcConfiguration.setRealm(config.getRealm());
        keycloakOidcConfiguration.setBaseUri(config.getBaseUri());

        KeycloakOidcClient client = new KeycloakOidcClient(keycloakOidcConfiguration);
        client.setCallbackUrl(config.getCallbackUrl());


        logger.info("KeycloakOidcClient created " + keycloakOidcConfiguration);

        return client;
    }

}
