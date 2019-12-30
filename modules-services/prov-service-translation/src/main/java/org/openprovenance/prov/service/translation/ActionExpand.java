package org.openprovenance.prov.service.translation;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.*;
import org.openprovenance.prov.service.translation.memory.TemplateResourceInMemory;
import org.openprovenance.prov.service.translation.memory.TemplateResourceIndexInMemory;
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

    public ActionExpand(ServiceUtils utils) {
        this.utils= utils;

        Instantiable<?> expander=utils.getExtensionMap().get(TemplateResource.getResourceKind());
        Instantiable<TemplateResource> expander2=(Instantiable<TemplateResource>) expander;

        ExtendedDocumentResourceIndexFactory<TemplateResource> tmp=utils.getDocumentResourceIndex().getExtender(expander2);



        //this.resourceIndex=utils.getDocumentResourceIndex().getExtender(expander2);
        logger.info("FIXME FIXME FIXME: teplate resource index in memory must be discovered dynamically");
        this.resourceIndex=new TemplateResourceIndexInMemory(utils.getDocumentResourceIndex(),TemplateResourceIndexInMemory.itr);
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

        String location= "documents/" + dr.getVisibleId() + "." + destination;
        List<InputPart> inputParts = formData.get("statements");
        String bindings = inputParts.get(0).getBodyAsString();
        logger.info("bindings " + bindings);

        expandTemplateWithBindings(tr, bindings);
        storeExpandedDocument(tr);

        return utils.composeResponseSeeOther(location).header("Expires", date).build();
    }

    private void expandTemplateWithBindings(TemplateResource tr, String bindings) {
        boolean allExpanded=false;
        ProvFactory pFactory=org.openprovenance.prov.xml.ProvFactory.getFactory();
        boolean addOrderp=false;

        Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);

        InputStream stream = new ByteArrayInputStream(bindings.getBytes(StandardCharsets.UTF_8));


        Bindings bb= BindingsJson.fromBean(BindingsJson.importBean(stream),pFactory);
        tr.setBindings(bb);

        Document expanded = myExpand.expander(tr.document(), bb);

        tr.setDocument(expanded);
    }


    private void storeExpandedDocument(DocumentResource vr) {

        try {
            final TemplateResource tr=(TemplateResource)((TemplateResourceInMemory)vr).clone();

            Runnable run= () -> {
                logger.debug("========================> " + JobManagement.logJobp);
                if (!JobManagement.logJobp) return;

                InteropFramework interop = new InteropFramework();
                String myfile=tr.getStorageId() +"_expanded.provn";
                tr.setStorageId(myfile);
               // tr.url=tr.url+"_expanded";
                interop.writeDocument(myfile, tr.document());

                doLog(tr);
            };

            run.run();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void doLog(TemplateResource tr) {
        logger.log(ProvLevel.PROV,
                "" + Constants.TEMPLATE_EXPANSION + ","
                        + tr.getVisibleId() + ","
                        + tr.getStorageId());
    }

}
