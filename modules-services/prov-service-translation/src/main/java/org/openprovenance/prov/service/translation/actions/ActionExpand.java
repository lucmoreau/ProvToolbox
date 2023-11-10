package org.openprovenance.prov.service.translation.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.service.translation.JobDeleteTemplateResource;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.NonDocumentGenericResourceStorage;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;
import org.openprovenance.prov.template.expander.deprecated.BindingsBean;
import org.openprovenance.prov.template.expander.Expand;
import org.openprovenance.prov.template.json.Bindings;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import jakarta.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActionExpand implements ActionPerformer {
    public static final String BINDINGS_KEY = "bindings";
    private static final Logger logger = LogManager.getLogger(ActionExpand.class);

    private final ServiceUtils utils;

    private final ResourceIndex<TemplateResource> resourceIndex;
    private final ObjectMapper om;

    public ActionExpand(ServiceUtils utils) {
        this.utils= utils;
        this.om=new ObjectMapper();

        ResourceIndex<?> indexer=utils.getExtensionMap().get(TemplateResource.getResourceKind());
        this.resourceIndex=(ResourceIndex<TemplateResource>) indexer;

    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.EXPAND;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource dr, Date date) throws IOException {


        ResourceIndex<TemplateResource> index=resourceIndex.getIndex();
        try {
            TemplateResource tr = index.newResource(dr);


            Document templateDocument = utils.getDocumentFromCacheOrStore(tr.getStorageId());

            ServiceUtils.Destination destination = utils.getDestination(formData);

            List<InputPart> inputParts = formData.get("statements");
            String bindings = inputParts.get(0).getBodyAsString();
            logger.debug("bindings " + bindings);

            Document expanded = expandTemplateWithBindings(templateDocument, tr, bindings);

            DocumentResource theTemplate = index.getAncestor().newResource();  // This is also thread safe!
            final String originalStorageId = tr.getStorageId();

            utils.getJobManager().scheduleJob(theTemplate.getVisibleId());
            scheduleNewJob(tr.getVisibleId());

            theTemplate.setStorageId(originalStorageId);
            logger.info("is it necessary for template to be exposed as " + theTemplate.getVisibleId() + " " + theTemplate.getStorageId());  // TODO: Should not if the template already existed in store

            String expandedStoreId = storeExpandedDocument(expanded);
            tr.setStorageId(expandedStoreId);
            tr.setTemplateStorageId(originalStorageId);
            index.put(tr.getVisibleId(), tr);


            doLog(tr);

            String location = "documents/" + tr.getVisibleId() + "." + destination;
            return utils.composeResponseSeeOther(location).header("Expires", date).build();
        } finally {
            index.close();
        }
    }

    private Document expandTemplateWithBindings(Document templateDocument, TemplateResource tr, String bindingsString) throws IOException {
        boolean allExpanded=false;
        ProvFactory pFactory=utils.getProvFactory();
        boolean addOrderp=false;

        Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);

        InputStream stream = new ByteArrayInputStream(bindingsString.getBytes(StandardCharsets.UTF_8));
        Bindings bindings = om.readValue(stream, Bindings.class);



        Document expanded = myExpand.expander(templateDocument, bindings);


        storeBindings(tr, bindings);
        //storeBindings(tr, bean);


        return expanded;
    }

    private void storeBindings(TemplateResource tr, BindingsBean bean) throws IOException {

        final NonDocumentGenericResourceStorage<BindingsBean> bindingsStorage = (NonDocumentGenericResourceStorage<BindingsBean> ) utils.getGenericResourceStorageMap().get(BINDINGS_KEY);
        String bindingsStoreId= bindingsStorage.newStore("json", "application/json");

        bindingsStorage.serializeObjectToStore(bean,bindingsStoreId);

        logger.debug("saving bindings " + " " + bindingsStoreId);
        tr.setBindingsStorageId(bindingsStoreId);

    }

    private void storeBindings(TemplateResource tr, Bindings bean) throws IOException {

        final NonDocumentGenericResourceStorage<Bindings> bindingsStorage = (NonDocumentGenericResourceStorage<Bindings> ) utils.getGenericResourceStorageMap().get(BINDINGS_KEY);
        String bindingsStoreId= bindingsStorage.newStore("json", "application/json");

        bindingsStorage.serializeObjectToStore(bean,bindingsStoreId);

        logger.debug("saving bindings " + " " + bindingsStoreId);
        tr.setBindingsStorageId(bindingsStoreId);

    }



    private String storeExpandedDocument(Document doc) throws IOException {
        InteropFramework interop = new InteropFramework(utils.getProvFactory());
        String storeId=utils.getStorageManager().newStore(Formats.ProvFormat.PROVN);

        synchronized (utils.documentCache) {
            utils.documentCache.put(storeId,doc);
        }
        utils.getStorageManager().writeDocument(storeId, doc, Formats.ProvFormat.PROVN);
        return storeId;
    }

    public Date scheduleNewJob(String visibleId) {
        try {
            // remove previous job, and replace it
            JobManagement.getScheduler().deleteJob(new JobKey(visibleId,"graph"));
        } catch (SchedulerException e) {
            logger.throwing(e);
        }
        return utils.getJobManager().scheduleJob(JobDeleteTemplateResource.class,visibleId,"-template", "graph");
    }


    private void doLog(TemplateResource tr) {
        logger.log(ProvLevel.PROV,
                "" + Constants.TEMPLATE_EXPANSION + ","
                        + tr.getVisibleId() + ","
                        + tr.getStorageId());
    }

}
