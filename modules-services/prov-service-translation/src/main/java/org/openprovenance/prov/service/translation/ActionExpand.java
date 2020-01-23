package org.openprovenance.prov.service.translation;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.openprovenance.prov.template.expander.Expand;
import org.quartz.JobKey;
import org.quartz.SchedulerException;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActionExpand implements ActionPerformer {
    public static final String BINDINGS_KEY = "bindings";
    private static Logger logger = Logger.getLogger(ActionExpand.class);

    private final ServiceUtils utils;

    private final ResourceIndex<TemplateResource> resourceIndex;

    ActionExpand(ServiceUtils utils) {
        this.utils= utils;

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
        TemplateResource tr=resourceIndex.newResource(dr);

        Document templateDocument=utils.getDocumentFromCacheOrStore(tr.getStorageId());

        ServiceUtils.Destination destination = utils.getDestination(formData);

        List<InputPart> inputParts = formData.get("statements");
        String bindings = inputParts.get(0).getBodyAsString();
        logger.debug("bindings " + bindings);

        Document expanded=expandTemplateWithBindings(templateDocument, tr, bindings);

        DocumentResource theTemplate=resourceIndex.getAncestor().newResource();
        final String originalStorageId = tr.getStorageId();

        utils.getJobManager().scheduleJob(theTemplate.getVisibleId());
        scheduleNewJob(tr.getVisibleId());

        theTemplate.setStorageId(originalStorageId);
        logger.info("is it necessary for template to be exposed as " + theTemplate.getVisibleId() + " " + theTemplate.getStorageId());  // TODO: Should not if the template already existed in store

        String expandedStoreId=storeExpandedDocument(expanded);
        tr.setStorageId(expandedStoreId);
        tr.setTemplateStorageId(originalStorageId);
        resourceIndex.put(tr.getVisibleId(),tr);



        doLog(tr);

        String location= "documents/" + tr.getVisibleId() + "." + destination;
        return utils.composeResponseSeeOther(location).header("Expires", date).build();
    }

    private Document expandTemplateWithBindings(Document templateDocument, TemplateResource tr, String bindings) throws IOException {
        boolean allExpanded=false;
        ProvFactory pFactory=utils.getProvFactory();
        boolean addOrderp=false;

        Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);

        InputStream stream = new ByteArrayInputStream(bindings.getBytes(StandardCharsets.UTF_8));


        final BindingsJson.BindingsBean bean = BindingsJson.importBean(stream);
        Bindings bb= BindingsJson.fromBean(bean,pFactory);

        Document expanded = myExpand.expander(templateDocument, bb);


        storeBindings(tr, bean);


        return expanded;
    }

    private void storeBindings(TemplateResource tr, BindingsJson.BindingsBean bean) throws IOException {
        //final NonDocumentResourceIndex<NonDocumentResource> ndIndex = utils.getNonDocumentResourceIndex();
        //NonDocumentResource ndr= ndIndex.newResource();
        //ndr.setMediaType("application/json");
        final NonDocumentGenericResourceStorage<BindingsJson.BindingsBean> bindingsStorage = (NonDocumentGenericResourceStorage<BindingsJson.BindingsBean> ) utils.getGenericResourceStorageMap().get(BINDINGS_KEY);
        String bindingsStoreId= bindingsStorage.newStore("json", "application/json");
        //ndr.setStorageId(bindingsStoreId);
        bindingsStorage.serializeObjectToStore(bean,bindingsStoreId);
        //ndIndex.put(ndr.getVisibleId(),ndr);
        logger.info("saving bindings " + " " + bindingsStoreId);
        tr.setBindingsStorageId(bindingsStoreId);
        //utils.getJobManager().scheduleJob(ndr.getVisibleId());
    }

    /*
    private void storeBindings_ORIGINAL(TemplateResource tr, BindingsJson.BindingsBean bean) throws IOException {
        final NonDocumentResourceIndex<NonDocumentResource> ndIndex = utils.getNonDocumentResourceIndex();
        NonDocumentResource ndr= ndIndex.newResource();
        ndr.setMediaType("application/json");
        final NonDocumentGenericResourceStorage<BindingsJson.BindingsBean> bindingsStorage = (NonDocumentGenericResourceStorage<BindingsJson.BindingsBean> ) utils.getGenericResourceStorageMap().get(BINDINGS_KEY);
        String bindingsStoreId= bindingsStorage.newStore("json", ndr.getMediaType());
        ndr.setStorageId(bindingsStoreId);
        bindingsStorage.serializeObjectToStore(bean,bindingsStoreId);
        ndIndex.put(ndr.getVisibleId(),ndr);
        logger.info("saving bindings " + ndr.getVisibleId() + " " + bindingsStoreId);
        tr.setBindingsStorageId(ndr.getVisibleId());
        utils.getJobManager().scheduleJob(ndr.getVisibleId());
    }

     */


    private String storeExpandedDocument(Document doc) throws IOException {
        InteropFramework interop = new InteropFramework(utils.getProvFactory());
        String storeId=utils.getStorageManager().newStore(Formats.ProvFormat.PROVN);

        synchronized (utils.documentCache) {
            utils.documentCache.put(storeId,doc);
        }
        utils.getStorageManager().writeDocument(storeId, Formats.ProvFormat.PROVN,doc);
       // interop.writeDocument(storeId, doc);
        return storeId;
    }

    public Date scheduleNewJob(String visibleId) {
        try {
            // remove previous job, and replace it
            JobManagement.getScheduler().deleteJob(new JobKey(visibleId,"graph"));
        } catch (SchedulerException e) {
            e.printStackTrace();
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
