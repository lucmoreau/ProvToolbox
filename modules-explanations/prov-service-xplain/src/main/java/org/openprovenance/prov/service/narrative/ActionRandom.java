package org.openprovenance.prov.service.narrative;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.storage.api.DocumentResource;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActionRandom implements ActionPerformer {


    private final ServiceUtils utils;

    public ActionRandom(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.RANDOM;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource dr, Date date) throws IOException {

        return  utils.composeResponseSeeOther("documents/" + dr.getVisibleId() + "/random_narrative").header("Expires",date).build();
    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}


