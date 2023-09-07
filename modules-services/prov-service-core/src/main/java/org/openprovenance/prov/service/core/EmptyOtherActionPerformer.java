package org.openprovenance.prov.service.core;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.storage.api.DocumentResource;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class EmptyOtherActionPerformer implements OtherActionPerformer{

    @Override
    public boolean otherAction(ServiceUtils.Action action, Map<String, List<InputPart>> formData) {
        return true;
    }

    @Override
    public Response doAction(ServiceUtils.Action action, Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException {
        throw new UnsupportedOperationException("Action not supported by service " + action);
    }

}
