package org.openprovenance.prov.service.translation.actions;

import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.storage.api.DocumentResource;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class ActionMetrics implements ActionPerformer {

    private final ServiceUtils utils;

    public ActionMetrics(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.METRICS;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException {

        String location = "metrics/" + vr.getVisibleId() + "." + "json";

        return utils.composeResponseSeeOther(location).header("Expires", date).build();
    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}
