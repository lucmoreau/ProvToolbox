package org.openprovenance.prov.service.validation;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;import org.openprovenance.prov.storage.api.ResourceStorage;
import org.openprovenance.prov.storage.api.NonDocumentResourceStorage;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;
import org.openprovenance.prov.validation.report.ValidationReport;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import static org.openprovenance.prov.service.validation.ActionValidate.*;


public class JobDeleteValidationResource implements Job {
    static Logger logger = LogManager.getLogger(JobDeleteValidationResource.class);

    public JobDeleteValidationResource() {
    }

    @Override
    public void execute(JobExecutionContext context){
        logger.info("delete job called " + context.getTrigger());

        ValidationServiceUtils utils=null;
        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            utils=(ValidationServiceUtils)schedulerContext.getOrDefault(VALIDATION_UTILS_KEY,utils);
        } catch (SchedulerException e) {
            logger.throwing(e);
        }

        String visibleId = context.getJobDetail().getKey().getName();
        if (visibleId == null) {
            logger.error("no visibleId " + context);
        } else {
            ValidationResource vr = utils.getValidationResourceIndex().get(visibleId);

            if (vr == null) {
                logger.info("no VR entry for visibleId " + visibleId);
            } else {
                vr.getJsonReportStorageId();

                NonDocumentResourceStorage nonDocumentResourceStorage=utils.getNonDocumentResourceStorage();
                NonDocumentGenericResourceStorage<ValidationReport> reportStorage= (NonDocumentGenericResourceStorage<ValidationReport>) utils.getGenericResourceStorageMap().get(REPORT_KEY);
                NonDocumentGenericResourceStorage<String> matrixStorage = (NonDocumentGenericResourceStorage<String>) utils.getGenericResourceStorageMap().get(MATRIX_KEY);
                ResourceStorage documentStorage= utils.getStorageManager();

                logger.info("deleting ValidationResource... " + visibleId);
                utils.getValidationResourceIndex().remove(visibleId);
                utils.deleteFromCache(visibleId);

                if (vr.getJsonReportStorageId()!=null) reportStorage.delete(vr.getJsonReportStorageId());
                if (vr.getReport()!=null)              nonDocumentResourceStorage.delete(vr.getReport());
                if (vr.getMatrix()!=null)              matrixStorage.delete(vr.getMatrix());
                if (vr.getPngMatrix()!=null)           matrixStorage.delete(vr.getPngMatrix());
                if (vr.getStorageId()!=null)           documentStorage.delete(vr.getStorageId());

                vr.setReport(null);
                vr.setJsonReport(null);
                vr.setMatrix(null);
                vr.setPngMatrix(null);
                vr.setStorageId(null);
            }
        }
    }
}
