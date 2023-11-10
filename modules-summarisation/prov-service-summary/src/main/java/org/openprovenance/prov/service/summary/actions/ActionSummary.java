package org.openprovenance.prov.service.summary.actions;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.log.ProvLevel;
import org.openprovenance.prov.scala.immutable.Document;
import org.openprovenance.prov.scala.immutable.Indexer;
import org.openprovenance.prov.scala.summary.SummaryAPI;
import org.openprovenance.prov.scala.summary.SummaryConfig;
import org.openprovenance.prov.scala.summary.SummaryIndex;
import org.openprovenance.prov.scala.summary.TypePropagator;
import org.openprovenance.prov.service.core.ActionPerformer;
import org.openprovenance.prov.service.core.ServiceUtils;
import org.openprovenance.prov.service.core.jobs.JobManagement;
import org.openprovenance.prov.service.summary.SummaryDocumentResource;
import org.openprovenance.prov.service.summary.SummaryDocumentResourceInMemory;
import org.openprovenance.prov.service.summary.SummaryService;
import org.openprovenance.prov.storage.api.DocumentResource;
import scala.Tuple2;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public final class ActionSummary implements ActionPerformer {
    static Logger logger = LogManager.getLogger(ActionSummary.class);

    private final ServiceUtils utils;

    public ActionSummary(ServiceUtils utils) {
        this.utils=utils;
    }

    @Override
    public ServiceUtils.Action getAction() {
        return ServiceUtils.Action.SUMMARISE;
    }


    @Override
    public Response doAction(Map<String, List<InputPart>> formData, DocumentResource dr, Date date) throws IOException {
        SummaryDocumentResource sdr=new SummaryDocumentResourceInMemory(dr);
        utils.getDocumentResourceIndex().put(dr.getVisibleId(), sdr);


        ServiceUtils.Destination destination = utils.getDestination(formData);

        String location= "documents/" + dr.getVisibleId() + "." + destination;

        //List<InputPart> inputParts = formData.get("statements");
        //String bindings = inputParts.get(0).getBodyAsString();
        //System.out.println("bindings " + bindings);



       // if (sdr.getsIndex()==null) {
      //      sdr.setsIndex(getSummary(sdr));
     //   }


       // Document doc=sdr.getsIndex().document();

        logger.info(" TODO TODO TODO: sdr.setDocument(doc)");
        // sdr.setDocument(doc);

        if (true) throw new UnsupportedOperationException("Code has not been updated to work with summary/config stored in storage");

    //    doLogSummary(sdr,doc);
        return utils.composeResponseSeeOther(location).header("Expires", date).build();
    }


    public void doLogSummary(SummaryDocumentResource vr, Document doc) {

        try {
            final SummaryDocumentResource vr2=(SummaryDocumentResource)((SummaryDocumentResourceInMemory)vr).clone();

            Runnable run= () -> {
                logger.debug("========================> " + JobManagement.logJobp);
                if (!JobManagement.logJobp) return;

                InteropFramework interop = new InteropFramework();
                String myfile=vr2.getStorageId()+"_summary.provn";
                vr2.setStorageId(myfile);
               // vr2.url=vr2.url+"_summary";
                interop.writeDocument(myfile, doc);

                doLog(SummaryService.SUMMARISATION,vr2);
            };

            run.run();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }


    public  void doLog(String action, SummaryDocumentResource vr) {
        logger.log(ProvLevel.PROV,
                "" + action + ","
                        + vr.getVisibleId() + ","
                        + vr.getStorageId());
    }



    public SummaryIndex getSummary(DocumentResource vr) throws IOException {
        org.openprovenance.prov.model.Document doc=utils.getDocumentFromCacheOrStore(vr.getStorageId());
        org.openprovenance.prov.model.Document d2=org.openprovenance.prov.scala.immutable.ProvFactory.pf().newDocument(doc);
        Document d3=(Document)d2;
        SummaryConfig config=new SummaryConfig(1);
        Tuple2<Indexer, TypePropagator> pair= SummaryAPI.sum(d3,config,null);
        Indexer ind=pair._1;
        TypePropagator tp=pair._2;
        SummaryIndex indexed= SummaryAPI.makeSummaryIndex(config,tp,ind,1,null, null);

        return indexed;
    }


    @Override public String toString () {
        return "<<performer:" + getAction() + ">>";
    }
}
