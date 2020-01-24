package org.openprovenance.prov.service.core;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.storage.api.DocumentResource;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ActionPerformer {
    public ServiceUtils.Action getAction();
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException;


}
