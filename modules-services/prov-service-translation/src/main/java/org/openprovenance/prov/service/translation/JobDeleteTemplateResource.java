package org.openprovenance.prov.service.translation;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.ResourceStorage;
import org.openprovenance.prov.storage.api.TemplateResource;
import org.openprovenance.prov.template.expander.BindingsBean;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import static org.openprovenance.prov.service.core.jobs.JobManagement.UTILS_KEY;
import static org.openprovenance.prov.service.translation.ActionExpand.BINDINGS_KEY;


public class JobDeleteTemplateResource implements Job {
    static Logger logger = LogManager.getLogger(JobDeleteTemplateResource.class);

    public JobDeleteTemplateResource() {
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
        ResourceIndex<TemplateResource> resourceIndex=(ResourceIndex<TemplateResource>) utils.getExtensionMap().get(TemplateResource.getResourceKind());
        final NonDocumentGenericResourceStorage<BindingsBean> bindingsStorage = (NonDocumentGenericResourceStorage<BindingsBean> ) utils.getGenericResourceStorageMap().get(BINDINGS_KEY);

        String visibleId = context.getJobDetail().getKey().getName();
        if (visibleId == null) {
            logger.error("no visibleId " + context);
        } else {
            TemplateResource vr = resourceIndex.get(visibleId);

            if (vr == null) {
                logger.info("no TR entry for visibleId " + visibleId);
            } else {

                ResourceStorage documentStorage= utils.getStorageManager();

                logger.info("deleting TemplateResource... " + visibleId);

                resourceIndex.remove(visibleId);


                utils.deleteFromCache(visibleId);
                if (vr.getStorageId()!=null)           documentStorage.delete(vr.getStorageId());
                if (vr.getBindingsStorageId()!=null)   bindingsStorage.delete(vr.getBindingsStorageId());

                vr.setStorageId(null);
                vr.setBindingsStorageId(null);
            }
        }
    }
}
