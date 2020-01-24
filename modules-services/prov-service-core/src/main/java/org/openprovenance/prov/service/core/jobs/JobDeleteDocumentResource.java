package org.openprovenance.prov.service.core.jobs;

import org.apache.log4j.Logger;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.NonDocumentResource;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import static org.openprovenance.prov.service.core.jobs.JobManagement.UTILS_KEY;

public class JobDeleteDocumentResource implements Job {
    static Logger logger = Logger.getLogger(JobDeleteDocumentResource.class);

    public JobDeleteDocumentResource() {
    }

    @Override
    public void execute(JobExecutionContext context){
        logger.info("delete job called " + context.getTrigger());

        ServiceUtils utils=null;
        try {
            SchedulerContext schedulerContext = context.getScheduler().getContext();
            utils=(ServiceUtils)schedulerContext.getOrDefault(UTILS_KEY,utils);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        String visibleId = context.getJobDetail().getKey().getName();
        if (visibleId == null) {
            logger.error("no visibleId " + context);
        } else {
            DocumentResource dr = utils.getDocumentResourceIndex().get(visibleId);


            if (dr == null) {
                NonDocumentResource ndr = utils.getNonDocumentResourceIndex().get(visibleId);
                if (ndr==null) {
                    logger.info("resource no longer exists in DocumentResourceIndex and NonDocumentResourceIndex:  " + visibleId);
                } else {
                    utils.deleteFromCache(visibleId);
                    if (ndr.getStorageId() !=null) {
                        logger.info("deleting NonDocumentResource... " + ndr.getStorageId());
                        utils.getNonDocumentResourceStorage().delete(ndr.getStorageId());
                        ndr.setStorageId(null);
                    }
                }
            } else {


                utils.getDocumentResourceIndex().remove(visibleId);
                utils.deleteFromCache(visibleId);
                if (dr.getStorageId() != null) {
                    logger.info("deleting DocumentResource... " + visibleId);
                    utils.getStorageManager().delete(dr.getStorageId());
                    dr.setStorageId(null);
                }

            }
        }
    }
}
