package org.openprovenance.prov.service.translation;

import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.JobManagement;
import org.openprovenance.prov.service.core.ServiceUtils;
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
    static Logger logger = Logger.getLogger(ActionExpand.class);

    private final TranslationServiceUtils utils;

    public ActionExpand(ServiceUtils utils) {
        this.utils=(TranslationServiceUtils) utils;
    }

    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.EXPAND;
    }

    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource vr, Date date) throws IOException {
        TemplateResource vr2=new TemplateResource(vr);
        DocumentResource.table.put(vr.graphId, vr2);


        ServiceUtils.Destination destination = utils.getDestination(formData);

        String location= "documents/" + vr.graphId + "." + destination;
        List<InputPart> inputParts = formData.get("statements");
        String bindings = inputParts.get(0).getBodyAsString();
        System.out.println("bindings " + bindings);


        expandTemplateWithBindings(vr2, bindings);

        doLogExpansion(vr2);
        return utils.composeResponseSeeOther(location).header("Expires",
                date)
                .build();
    }

    public void expandTemplateWithBindings(DocumentResource vr, String bindings) {
        boolean allExpanded=false;
        ProvFactory pFactory=org.openprovenance.prov.xml.ProvFactory.getFactory();
        boolean addOrderp=false;

        Expand myExpand=new Expand(pFactory, addOrderp,allExpanded);
        Document expanded;

        InputStream stream = new ByteArrayInputStream(bindings.getBytes(StandardCharsets.UTF_8));


        Bindings bb= BindingsJson.fromBean(BindingsJson.importBean(stream),pFactory);
        ((TemplateResource)vr).bindings=bb;

        expanded = myExpand.expander(vr.document,
                bb);

        vr.document=(org.openprovenance.prov.xml.Document)expanded;
    }


    public void doLogExpansion(DocumentResource vr) {

        try {
            final TemplateResource vr2=(TemplateResource)((TemplateResource)vr).clone();

            Runnable run=new Runnable() {

                @Override
                public void run() {
                    logger.debug("========================> " + JobManagement.logJobp);
                    if (!JobManagement.logJobp) return;

                    InteropFramework interop = new InteropFramework();
                    String myfile=vr2.filepath+"_expanded.provn";
                    vr2.filepath=myfile;
                    vr2.url=vr2.url+"_expanded";
                    interop.writeDocument(myfile, vr2.document);

                    doLog(TemplateService.TEMPLATE_EXPANSION,vr2);
                }

            };

            run.run();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public  void doLog(String action, TemplateResource vr) {
        logger.log(ProvLevel.PROV,
                "" + action + ","
                        + vr.graphId + ","
                        + vr.filepath + ","
                        + vr.format);
    }




}
