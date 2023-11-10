package org.openprovenance.prov.service.summary.actions;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.summary.SummaryService;
import org.openprovenance.prov.storage.api.DocumentResource;

import jakarta.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class ActionUpload implements ActionPerformer {
    private static Logger logger = LogManager.getLogger(ActionUpload.class);

    private final ServiceUtils utils;

    public ActionUpload(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.UPLOAD;
    }


    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource vr, Date date) {
      //  DocumentResource.table.put(vr.graphId, vr);


        String location= "documents/" + vr.getVisibleId();

        doLog(SummaryService.SUMMARISATION,vr);

        return utils.composeResponseSeeOther(location).header("Expires",
                date)
                .build();
    }


    private  void doLog(String action, DocumentResource vr) {
        logger.log(ProvLevel.PROV,
                "" + action + ","
                        + vr.getVisibleId() + ","
                        + vr.getStorageId());
    }




    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}
