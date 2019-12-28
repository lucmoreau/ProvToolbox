package org.openprovenance.prov.service.translation;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ServiceUtils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class ActionTranslate implements ActionPerformer {

    private final ServiceUtils utils;

    public ActionTranslate(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.TRANSLATE;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException {

        ServiceUtils.Destination destination = utils.getDestination(formData);
        String location = "documents/" + vr.visibleId + "."
                + destination;

        return utils.composeResponseSeeOther(location).header("Expires",
                date)
                .build();
    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}
