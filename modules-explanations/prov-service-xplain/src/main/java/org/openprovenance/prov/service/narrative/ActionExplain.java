package org.openprovenance.prov.service.narrative;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionExplain implements ActionPerformer {

    static Logger logger = LogManager.getLogger(ActionExplain.class);


    private final NarrativeServiceUtils utils;

    public ActionExplain(NarrativeServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.EXPLANATION;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource dr, Date date) throws IOException {

        List<InputPart> templates = formData.get("xplain-template");
        String templates_str=null;
        List<InputPart> profiles = formData.get("xplain-profile");
        String profiles_str=null;

        if (templates != null) {
            templates_str=templates.get(0).getBodyAsString();
        }
        if (profiles != null) {
            profiles_str=profiles.get(0).getBodyAsString();
        }

        logger.debug(NarrativeServiceUtils.KEY_TEMPLATES + " " + templates_str);
        logger.debug(NarrativeServiceUtils.KEY_PROFILES  + " " + profiles_str);

        final ResourceIndex<DocumentResource> index = utils.getDocumentResourceIndex().getIndex();

        String keyId = utils.createEntryForTemplateAndProfile(index, dr.getVisibleId(), dr, templates_str, profiles_str);

        index.put(dr.getVisibleId(),dr);

        return  utils.composeResponseSeeOther("documents/" + dr.getVisibleId() + "/explanation/" + keyId).header("Expires",date).build();
    }


    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}


