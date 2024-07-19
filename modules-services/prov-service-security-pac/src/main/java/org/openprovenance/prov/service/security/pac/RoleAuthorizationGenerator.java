package org.openprovenance.prov.service.security.pac;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RoleAuthorizationGenerator implements AuthorizationGenerator {



    static Logger logger = LogManager.getLogger(RoleAuthorizationGenerator.class);
    private final String role;

    static Map<String,UserProfile> profiles= new HashMap<>();

    static public UserProfile getProfile(String id) {
        return profiles.get(id);
    }

    public RoleAuthorizationGenerator(String role) {
        this.role = role;
    }

    @Override
    public Optional<UserProfile> generate(WebContext webContext, SessionStore sessionStore, UserProfile profile) {
        //logger.info("Checking roles claim " +  profile);
        Object roles = profile.getAttribute("roles");
        if (!(roles instanceof List)) {
            logger.info("roles claim is null or not a list");
            throw new RuntimeException("roles claim is null or not a list");
        } else {
            List<String> rolesList=(List<String>)roles;
            if (!rolesList.contains(role)) {
                logger.info("roles claim does not contain editor");
                throw new RuntimeException("roles claim does not contain editor");
            } else {
                logger.info("roles claim is correct");
            }
        }
        profiles.put(profile.getId(),profile);
        logger.info("profiles " + profile.getId() + " " + profile.getAttribute("preferred_username"));
        return Optional.of(profile);
    }


}