package org.openprovenance.prov.service;

import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.rules.Rules;
import org.openprovenance.prov.scala.typemap.IncrementalProcessor;
import scala.Tuple2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsCalculator extends Rules {



    @Override
    public Object getMetrics(Document document, ProvFactory pFactory) {


        Tuple2<Map<String, Integer>, Map<List<Tuple2<Integer, Integer>>, Integer>> features=IncrementalProcessor.process(document);

        System.out.println("Features: " + features._1);
        System.out.println("Features: " + features._2);



        Object simpleMetrics = super.getMetrics(document, pFactory);
        return new HashMap<String, Object>() {{
            put("artifact", "document");
            put("counts", simpleMetrics);
            put("features", features._2);
        }};
    }

}
