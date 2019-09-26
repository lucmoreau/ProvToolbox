package org.openprovenance.prov.service.core;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OtherActionPerformer {
    public boolean otherAction(ServiceUtils.Action action, Map<String, List<InputPart>> formData);

    public Response doAction(ServiceUtils.Action action, Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException;
}
