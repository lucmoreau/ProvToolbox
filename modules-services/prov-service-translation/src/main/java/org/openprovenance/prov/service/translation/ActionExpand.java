package org.openprovenance.prov.service.translation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsJson;
import org.openprovenance.prov.template.expander.Expand;

import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ActionExpand implements ActionPerformer {
    private static Logger logger = Logger.getLogger(ActionExpand.class);

    private final ServiceUtils utils;
    private final ResourceIndex<TemplateResource> resourceIndex;

    ActionExpand(ServiceUtils utils) {
        this.utils= utils;

        Instantiable<?> expander=utils.getExtensionMap().get(TemplateResource.getResourceKind());
        Instantiable<TemplateResource> expander2=(Instantiable<TemplateResource>) expander;

        //ExtendedDocumentResourceIndexFactory<TemplateResource> tmp=utils.getDocumentResourceIndex().getExtender(expander2);


        logger.info("FIXING FIXME FIXME: template resource index in memory must be discovered dynamically");

        this.resourceIndex=utils.getDocumentResourceIndex().getExtender(expander2);
        //this.resourceIndex=new TemplateResourceIndexInMemory(utils.getDocumentResourceIndex(),TemplateResourceIndexInMemory.factory);
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
        ProvFactory pFactory=org.openprovenance.prov.xml.ProvFactory.getFactory();
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
        final NonDocumentResourceIndex<NonDocumentResource> ndIndex = utils.getNonDocumentResourceIndex();
        NonDocumentResource ndr= ndIndex.newResource();
        ndr.setMediaType("application/json");
        final NonDocumentResourceStorage nonDocumentResourceStorage = utils.getNonDocumentResourceStorage();
        String bindingsStoreId= nonDocumentResourceStorage.newStore("json", ndr.getMediaType());
        ndr.setStorageId(bindingsStoreId);
        nonDocumentResourceStorage.serializeObjectToStore(new ObjectMapper(),bean,bindingsStoreId);
        ndIndex.put(ndr.getVisibleId(),ndr);
        logger.info("saving bindings " + ndr.getVisibleId() + " " + bindingsStoreId);
        tr.setBindingsStorageId(ndr.getVisibleId());
        utils.getJobManager().scheduleJob(ndr.getVisibleId());
    }


    private String storeExpandedDocument(Document doc) throws IOException {
        InteropFramework interop = new InteropFramework();
        String storeId=utils.getStorageManager().newStore(Formats.ProvFormat.PROVN);
        interop.writeDocument(storeId, doc);
        return storeId;

    }

    private void doLog(TemplateResource tr) {
        logger.log(ProvLevel.PROV,
                "" + Constants.TEMPLATE_EXPANSION + ","
                        + tr.getVisibleId() + ","
                        + tr.getStorageId());
    }

}
