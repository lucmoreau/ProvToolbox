package org.openprovenance.prov.service.signature;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.storage.api.DocumentResource;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class ActionSign implements ActionPerformer {

    private final ServiceUtils utils;

    public ActionSign(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.SIGN;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException {
        return utils.composeResponseSeeOther("documents/" + vr.getVisibleId() + "/signed").header("Expires",date).build();
    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}
