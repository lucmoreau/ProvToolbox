package org.openprovenance.prov.benchmarks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvDeserialiser;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Benchmarks {

    ProvFactory pf=ProvFactory.getFactory();

    class JsonDeserialiser implements ProvDeserialiser {
        ObjectMapper om=new ObjectMapper();
        @Override
        public Document deserialiseDocument(InputStream in) throws IOException {
            om.readValue(in, Map.class);
            return null;
        }
    }
    class JsonSerialiser implements ProvSerialiser {
        ObjectMapper om=new ObjectMapper();
        @Override
        public void serialiseDocument(OutputStream out, Document document, boolean formatted) {
            try {
                om.writeValue(out,document);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void serialiseDocument(OutputStream out, Document document, String mediaType, boolean formatted) {
            serialiseDocument(out,document,false);
        }

        @Override
        public Collection<String> mediaTypes() {
            return null;
        }
    }

    ProvSerialiser   jsonldSerialiser   = new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser(false);
    ProvDeserialiser jsonldDeserialiser = new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser();
    ProvSerialiser   provnSerialiser    = new org.openprovenance.prov.notation.ProvSerialiser(pf);
    ProvDeserialiser provnDeserialiser  = new org.openprovenance.prov.notation.ProvDeserialiser(pf);
    ProvDeserialiser jsonDeserialiser   = new JsonDeserialiser();
    ProvSerialiser   jsonSerialiser     = new JsonSerialiser();
    ProvDeserialiser scalaDeserialiser  = new org.openprovenance.prov.scala.immutable.ProvDeserialiser();

    public static void main(String [] args) throws IOException {
        new Benchmarks().doTask(args[0], args[1]);
    }

    public void doTask(String arg0, String arg1) throws IOException {
        Document doc= provnDeserialiser.deserialiseDocument(new FileInputStream("src/test/resources/primer.provn"));
        int count0=Integer.valueOf(arg0);
        int count1=Integer.valueOf(arg1);
        System.out.println(count0);
        System.out.println(count1);

        System.out.println("warmup");
        doBenchmark(doc, count0);
        System.out.println("run");
        doBenchmark(doc, count1);
    }

    public void output(List ll) {
        String s="";
        boolean first=true;
        for (Object o: ll) {
            if (first) {
                first=false;
            } else {
                s=s+", ";
            }
            s=s+ o;
        }
        System.out.println(s);
    }

    public double average(List<Long> l) {
        return l.stream().mapToLong(Long::longValue).summaryStatistics().getAverage();
    }

    public void doBenchmark(Document doc, int count) throws IOException {
        System.out.println("write jsonld");
        List<Long> result1 = repeatSerialize(jsonldSerialiser, count, doc);
        output(result1);
        System.out.println("write provn");
        List<Long> result2 = repeatSerialize(provnSerialiser, count, doc);
        output(result2);
        System.out.println("deep copy");
        List<Long> result3 = repeatCopy(count, doc, ProvFactory.getFactory());
        output(result3);
        System.out.println("read jsonld");
        List<Long> result4 = repeatDeserialize(jsonldDeserialiser, count, provjsonldPrimer);
        output(result4);
        System.out.println("read provn");
        List<Long> result5 = repeatDeserialize(provnDeserialiser, count, provnPrimer);
        output(result5);
        System.out.println("read json");
        List<Long> result6 = repeatDeserialize(jsonDeserialiser, count, provjsonldPrimer);
        output(result6);
        System.out.println("read scala");
        List<Long> result7 = repeatDeserialize(scalaDeserialiser, count, provnPrimer);
        output(result7);
        System.out.println("deep copy scala");
        List<Long> result8 = repeatCopy(count, doc,new org.openprovenance.prov.scala.immutable.ProvFactory());
        output(result8);

        final double average3 = average(result3);
        final double average4 = average(result4);
        final double average5 = average(result5);
        final double average6 = average(result6);
        final double average7 = average(result7);
        final double average8 = average(result8);

        System.out.println( "w:jsonld, " + average(result1));
        System.out.println( "w:provn, " + average(result2));
        System.out.println( "deep copy, " + average3);
        System.out.println( "r:jsonld, " + average4 + ", " + (average4/average3) + ", " + (average4/(average3+average6)));
        System.out.println( "r:provn, " + average5);
        System.out.println( "r:json, " + average6);
        System.out.println( "r:scala, " + average7);
        System.out.println( "deep copy sc, " + average8);
    }

    public List<Long> repeatSerialize(ProvSerialiser serialiser, int count, Document doc) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(100000);
        List<Long> result=new LinkedList<>();

        for(int i=0; i< count; i++) {
            long start=System.nanoTime();
            for (int j=0; j<100; j++) {
                baos.reset();
                serialiser.serialiseDocument(baos, doc, false);
            }
            long end=System.nanoTime();
            result.add(end-start);
        }
        return result;
    }

    public List<Long> repeatDeserialize(ProvDeserialiser deserialiser, int count, String doc) throws IOException {
        List<Long> result=new LinkedList<>();

        byte[] bytes=doc.getBytes();
        for(int i=0; i< count; i++) {
            long start=System.nanoTime();
            for (int j=0; j<100; j++) {
                deserialiser.deserialiseDocument(new ByteArrayInputStream(bytes));
            }
            long end=System.nanoTime();
            result.add(end-start);
        }
        return result;
    }

    public List<Long> repeatCopy(int count, Document doc, org.openprovenance.prov.model.ProvFactory factory) {
        ByteArrayOutputStream baos=new ByteArrayOutputStream(100000);
        List<Long> result=new LinkedList<>();

        BeanTraversal bc=new BeanTraversal(factory, factory);

        for(int i=0; i< count; i++) {
            long start=System.nanoTime();
            for (int j=0; j<100; j++) {
                Document doc2=bc.doAction(doc);
            }
            long end=System.nanoTime();
            result.add(end-start);
        }
        return result;
    }

    static String provjsonldPrimer ="{\n" +
            "  \"@context\" : [ {\n" +
            "    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",\n" +
            "    \"dcterms\" : \"http://purl.org/dc/terms/\",\n" +
            "    \"ex\" : \"http://example/\",\n" +
            "    \"prov\" : \"http://www.w3.org/ns/prov#\",\n" +
            "    \"foaf\" : \"http://xmlns.com/foaf/0.1/\"\n" +
            "  }, \"http://openprovenance.org/prov-json.ld\" ],\n" +
            "  \"@graph\" : [ {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:article\",\n" +
            "    \"dcterms:title\" : [ {\n" +
            "      \"@value\" : \"Crime rises in cities\"\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:articleV1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:articleV2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:dataSet2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:regionList\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:composition\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:chart1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:chart2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:blogEntry\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:compile\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:compile2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:compose\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:correct\",\n" +
            "    \"startTime\" : \"2012-03-31T09:21:00.000+01:00\",\n" +
            "    \"endTime\" : \"2012-04-01T15:21:00.000+01:00\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:illustrate\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:regionList\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:composition\",\n" +
            "    \"activity\" : \"ex:compose\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:illustrate\",\n" +
            "    \"entity\" : \"ex:composition\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:chart1\",\n" +
            "    \"activity\" : \"ex:illustrate\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:chart1\",\n" +
            "    \"activity\" : \"ex:compile\",\n" +
            "    \"time\" : \"2012-03-02T10:30:00.000Z\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:chart2\",\n" +
            "    \"activity\" : \"ex:compile2\",\n" +
            "    \"time\" : \"2012-04-01T15:21:00.000+01:00\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Agent\",\n" +
            "    \"@id\" : \"ex:derek\",\n" +
            "    \"foaf:mbox\" : [ {\n" +
            "      \"@value\" : \"<mailto:derek@example.org>\"\n" +
            "    } ],\n" +
            "    \"prov:type\" : [ \"prov:Person\" ],\n" +
            "    \"foaf:givenName\" : [ {\n" +
            "      \"@value\" : \"Derek\"\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Association\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"agent\" : \"ex:derek\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Association\",\n" +
            "    \"activity\" : \"ex:illustrate\",\n" +
            "    \"agent\" : \"ex:derek\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Agent\",\n" +
            "    \"@id\" : \"ex:chartgen\",\n" +
            "    \"foaf:name\" : [ {\n" +
            "      \"@value\" : \"Chart Generators Inc\"\n" +
            "    } ],\n" +
            "    \"prov:type\" : [ \"prov:Organization\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Delegation\",\n" +
            "    \"delegate\" : \"ex:derek\",\n" +
            "    \"responsible\" : \"ex:chartgen\",\n" +
            "    \"activity\" : \"ex:compose\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Attribution\",\n" +
            "    \"entity\" : \"ex:chart1\",\n" +
            "    \"agent\" : \"ex:derek\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:dataSet1\",\n" +
            "    \"prov:role\" : [ \"ex:dataToCompose\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:regionList\",\n" +
            "    \"prov:role\" : [ \"ex:regionsToAggregateBy\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:dataSet2\",\n" +
            "    \"activity\" : \"ex:correct\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:correct\",\n" +
            "    \"entity\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:dataSet2\",\n" +
            "    \"usedEntity\" : \"ex:dataSet1\",\n" +
            "    \"prov:type\" : [ \"prov:Revision\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:chart2\",\n" +
            "    \"usedEntity\" : \"ex:dataSet2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:blogEntry\",\n" +
            "    \"usedEntity\" : \"ex:article\",\n" +
            "    \"prov:type\" : [ \"prov:Quotation\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Specialization\",\n" +
            "    \"specificEntity\" : \"ex:articleV1\",\n" +
            "    \"generalEntity\" : \"ex:article\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:articleV1\",\n" +
            "    \"usedEntity\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Specialization\",\n" +
            "    \"specificEntity\" : \"ex:articleV2\",\n" +
            "    \"generalEntity\" : \"ex:article\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:articleV2\",\n" +
            "    \"usedEntity\" : \"ex:dataSet2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Alternate\",\n" +
            "    \"alternate1\" : \"ex:articleV2\",\n" +
            "    \"alternate2\" : \"ex:articleV1\"\n" +
            "  } ]\n" +
            "}";

    static String provnPrimer="\n" +
            "\n" +
            "document\n" +
            "\n" +
            "   prefix ex <http://example/>\n" +
            "   prefix dcterms <http://purl.org/dc/terms/>\n" +
            "   prefix foaf <http://xmlns.com/foaf/0.1/>\n" +
            "\n" +
            "\n" +
            "   entity(ex:article, [dcterms:title=\"Crime rises in cities\"])\n" +
            "   entity(ex:articleV1)\n" +
            "   entity(ex:articleV2)\n" +
            "   entity(ex:dataSet1)\n" +
            "   entity(ex:dataSet2)\n" +
            "   entity(ex:regionList)\n" +
            "   entity(ex:composition)\n" +
            "   entity(ex:chart1)\n" +
            "   entity(ex:chart2)\n" +
            "   entity(ex:blogEntry)\n" +
            "\n" +
            "\n" +
            "   activity(ex:compile)\n" +
            "   activity(ex:compile2)\n" +
            "   activity(ex:compose)\n" +
            "   activity(ex:correct, 2012-03-31T09:21:00, 2012-04-01T15:21:00)\n" +
            "   activity(ex:illustrate)\n" +
            "\n" +
            "\n" +
            "   used(ex:compose, ex:dataSet1, -)\n" +
            "   used(ex:compose, ex:regionList, -)\n" +
            "   wasGeneratedBy(ex:composition, ex:compose, -)\n" +
            "\n" +
            "   used(ex:illustrate, ex:composition, -)\n" +
            "   wasGeneratedBy(ex:chart1, ex:illustrate, -)\n" +
            "\n" +
            "   wasGeneratedBy(ex:chart1, ex:compile,  2012-03-02T10:30:00)\n" +
            "   wasGeneratedBy(ex:chart2, ex:compile2, 2012-04-01T15:21:00)\n" +
            "    \n" +
            "\n" +
            "   agent(ex:derek, [ prov:type='prov:Person', foaf:givenName = \"Derek\", \n" +
            "          foaf:mbox= \"<mailto:derek@example.org>\"])\n" +
            "   wasAssociatedWith(ex:compose, ex:derek, -)\n" +
            "   wasAssociatedWith(ex:illustrate, ex:derek, -)\n" +
            "\n" +
            "   agent(ex:chartgen, [ prov:type='prov:Organization',\n" +
            "          foaf:name = \"Chart Generators Inc\"])\n" +
            "   actedOnBehalfOf(ex:derek, ex:chartgen, ex:compose)\n" +
            "\n" +
            "   wasAttributedTo(ex:chart1, ex:derek)\n" +
            "\n" +
            "\n" +
            "   used(ex:compose, ex:dataSet1, -,   [ prov:role = 'ex:dataToCompose'])\n" +
            "   used(ex:compose, ex:regionList, -, [ prov:role = 'ex:regionsToAggregateBy'])\n" +
            "\n" +
            "   wasGeneratedBy(ex:dataSet2, ex:correct, -)\n" +
            "   used(ex:correct, ex:dataSet1, -)\n" +
            "   wasDerivedFrom(ex:dataSet2, ex:dataSet1, [prov:type='prov:Revision'])\n" +
            "   wasDerivedFrom(ex:chart2, ex:dataSet2)\n" +
            "\n" +
            "   wasDerivedFrom(ex:blogEntry, ex:article, [prov:type='prov:Quotation'])\n" +
            "   specializationOf(ex:articleV1, ex:article)\n" +
            "   wasDerivedFrom(ex:articleV1, ex:dataSet1)\n" +
            "\n" +
            "   specializationOf(ex:articleV2, ex:article)\n" +
            "   wasDerivedFrom(ex:articleV2, ex:dataSet2)\n" +
            "\n" +
            "   alternateOf(ex:articleV2, ex:articleV1)\n" +
            "\n" +
            "\n" +
            "endDocument\n" +
            "\n";

}
