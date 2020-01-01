package org.openprovenance.prov.service.translation;

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


        logger.info("FIXING FIXME FIXME: teplate resource index in memory must be discovered dynamically");

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


        ServiceUtils.Destination destination = utils.getDestination(formData);

        List<InputPart> inputParts = formData.get("statements");
        String bindings = inputParts.get(0).getBodyAsString();
        logger.debug("bindings " + bindings);

        Document expanded=expandTemplateWithBindings(tr, bindings);

        DocumentResource theTemplate=resourceIndex.getAncestor().newResource();
        final String originalStorageId = tr.getStorageId();
        utils.getJobManager().scheduleJob(theTemplate.getVisibleId());

        theTemplate.setStorageId(originalStorageId);
        logger.info("is it necessary for template to be exposed as " + theTemplate.getVisibleId() + " " + theTemplate.getStorageId());  // TODO: Should not if the template already existed in store

        String expandedStoreId=storeExpandedDocument(expanded);
        tr.setStorageId(expandedStoreId);
        tr.setTemplateStorageId(originalStorageId);
        resourceIndex.put(tr.getVisibleId(),tr);

        // TODO TODO: store bindings in a resource file


        doLog(tr);

        String location= "documents/" + tr.getVisibleId() + "." + destination;
        return utils.composeResponseSeeOther(location).header("Expires", date).build();
    }

    private Document expandTemplateWithBindings(TemplateResource tr, String bindings) {
        boolean allExpanded=false;
        ProvFactory pFactory=org.openprovenance.prov.xml.ProvFactory.getFactory();
        boolean addOrderp=false;

        Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);

        InputStream stream = new ByteArrayInputStream(bindings.getBytes(StandardCharsets.UTF_8));


        Bindings bb= BindingsJson.fromBean(BindingsJson.importBean(stream),pFactory);
        tr.setBindings(bb);

        Document expanded = myExpand.expander(tr.document(), bb);

        return expanded;
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
