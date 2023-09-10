package org.openprovenance.prov.service.validation;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.NonDocumentResourceStorage;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActionValidate implements ActionPerformer {
    public static final String REPORT_KEY = "report";
    public static final String MATRIX_KEY = "matrix";
    static final String VALIDATION_UTILS_KEY = "VALIDATION_UTILS_KEY";
    static Logger logger = LogManager.getLogger(ActionValidate.class);

    private final ValidationServiceUtils utils;
    private final ResourceIndex<ValidationResource> validationResourceIndex;
    private final NonDocumentGenericResourceStorage<ValidationReport> reportStorage;
    private final NonDocumentGenericResourceStorage<String> matrixStorage;
    private final NonDocumentResourceStorage nonDocumentResourceStorage;

    public ActionValidate (ServiceUtils utils) {
        this.utils=(ValidationServiceUtils) utils;
        validationResourceIndex = this.utils.getValidationResourceIndex();
        nonDocumentResourceStorage=this.utils.getNonDocumentResourceStorage();
        reportStorage= (NonDocumentGenericResourceStorage<ValidationReport>) this.utils.getGenericResourceStorageMap().get(REPORT_KEY);
        matrixStorage = (NonDocumentGenericResourceStorage<String>) this.utils.getGenericResourceStorageMap().get(MATRIX_KEY);

        try {
            this.utils.getJobManager().getScheduler().getContext().put(VALIDATION_UTILS_KEY, this.utils);
        } catch (SchedulerException e) {
            // continue, without utils registered
        }
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.VALIDATE;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource dr, Date date) throws IOException {
        // upgrading resource to Validation Resource

        ResourceIndex<ValidationResource> index=validationResourceIndex.getIndex();
        try {
            final ValidationResource vr = index.newResource(dr);
            index.put(dr.getVisibleId(), vr);

            Document doc = utils.getDocumentFromCacheOrStore(vr.getStorageId());

            utils.performValidation(doc, vr, index, reportStorage, matrixStorage, nonDocumentResourceStorage);

            scheduleNewJob(vr.getVisibleId());

            logger.info("actionValidate: " + date);
            return utils.composeResponseSeeOther("documents/" + vr.getVisibleId() + "/validation/report").header("Expires", date).build();
        } finally {
            index.close();
        }
    }
    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }

    public Date scheduleNewJob(String visibleId) {
        try {
            // remove previous job, and replace it
            JobManagement.getScheduler().deleteJob(new JobKey(visibleId,"graph"));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return utils.getJobManager().scheduleJob(JobDeleteValidationResource.class,visibleId,"-validation", "graph");
    }

}
