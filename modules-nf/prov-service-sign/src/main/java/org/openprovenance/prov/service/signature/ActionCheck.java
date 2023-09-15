package org.openprovenance.prov.service.signature;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.storage.api.DocumentResource;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class ActionCheck implements ActionPerformer {
    static Logger logger = LogManager.getLogger(ActionCheck.class);

    private final ServiceUtils utils;

    public ActionCheck(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.CHECK;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource dr, Date date) throws IOException {
        DocumentResource dr2 = null;
        List<InputPart> inputParts = formData.get("s_file");
        if (inputParts != null) {
            dr2 = utils.doProcessFileForm(inputParts);
        } else {
            inputParts = formData.get("s_statements");
            List<InputPart> type = formData.get("s_type");
            if (inputParts != null) {
                dr2 = utils.doProcessStatementsForm(inputParts,type);
            }
        }
        String filepath=dr2.getStorageId();

        SignedDocumentResource sdr = new SignedDocumentResourceInMemory(dr);
        sdr.setSignedfilepath(filepath);
        utils.getDocumentResourceIndex().put(dr.getVisibleId(), sdr);


        return utils.composeResponseSeeOther("documents/" + dr.getVisibleId() + "/check").header("Expires",date).build();
    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}
