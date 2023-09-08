package org.openprovenance.prov.benchmarks;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvDeserialiser;
import org.openprovenance.prov.model.ProvSerialiser;
import org.openprovenance.prov.vanilla.ProvFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static org.openprovenance.prov.benchmarks.Examples.*;

@State(Scope.Thread)
public class SerialisationBenchmarks {

    final ProvFactory pf=ProvFactory.getFactory();
    final ProvFactory spf=new org.openprovenance.prov.scala.immutable.ProvFactory();
    
    ProvSerialiser   jsonldSerialiser   ;
    ProvDeserialiser provnDeserialiser  = new org.openprovenance.prov.notation.ProvDeserialiser(pf);
    ProvDeserialiser jsonldDeserialiser ;
    ProvSerialiser   provnSerialiser    ;
    ProvDeserialiser rawJsonDeserialiser;
    ProvSerialiser rawJsonSerialiser;
    ProvDeserialiser scalaDeserialiser  ;
    ProvSerialiser   pjsonSerialiser    ;
    ProvDeserialiser pjsonDeserialiser  ;

    ByteArrayOutputStream baos;
    Document doc;

    private ByteArrayInputStream provjsonldPrimerStream;
    private BeanTraversal beanTraveral;
    private BeanTraversal beanTraveralScala;
    private ByteArrayInputStream pjsonPrimerStream;
    private ByteArrayInputStream provnPrimerStream;

    @Setup(Level.Trial)
    public void doSetup() throws IOException {
        jsonldSerialiser=new org.openprovenance.prov.core.jsonld11.serialization.ProvSerialiser();
        jsonldDeserialiser=new org.openprovenance.prov.core.jsonld11.serialization.ProvDeserialiser();
        provnSerialiser    = new org.openprovenance.prov.notation.ProvSerialiser(pf);
        provnDeserialiser  = new org.openprovenance.prov.notation.ProvDeserialiser(pf);
        rawJsonDeserialiser = new RawJsonDeserialiser();
        rawJsonSerialiser = new RawJsonSerialiser();
        scalaDeserialiser  = new org.openprovenance.prov.scala.immutable.ProvDeserialiser();
        pjsonSerialiser    = new org.openprovenance.prov.core.json.serialization.ProvSerialiser();
        pjsonDeserialiser  = new org.openprovenance.prov.core.json.serialization.ProvDeserialiser();

        baos=new ByteArrayOutputStream(100000);
        doc= provnDeserialiser.deserialiseDocument(new FileInputStream("src/test/resources/primer.provn"));
        provjsonldPrimerStream=new ByteArrayInputStream(provjsonldPrimer.getBytes());
        pjsonPrimerStream=new ByteArrayInputStream(pjsonPrimer.getBytes());
        provnPrimerStream=new ByteArrayInputStream(provnPrimer.getBytes());
        beanTraveral=new BeanTraversal(pf, pf);
        beanTraveralScala=new BeanTraversal(spf, spf);
    }

    @TearDown(Level.Trial)
    public void doTearDown() {
    }

    @Benchmark()
    public void testSerialiseProvJsonLD(Blackhole blackhole)  {
        baos.reset();
        jsonldSerialiser.serialiseDocument(baos, doc, false);
        blackhole.consume(baos);
    }

    @Benchmark()
    public void testDeserialiseProvJsonLD(Blackhole blackhole) throws IOException {
        provjsonldPrimerStream.reset();
        doc=jsonldDeserialiser.deserialiseDocument(provjsonldPrimerStream);
        blackhole.consume(doc);
    }

    @Benchmark
    public void testDeepCopyJava(Blackhole blackhole) {
        Document doc2=beanTraveral.doAction(doc);
        blackhole.consume(doc2);
    }

    @Benchmark
    public void testDeepCopyScala(Blackhole blackhole) {
        Document doc2=beanTraveralScala.doAction(doc);
        blackhole.consume(doc2);
    }

    @Benchmark()
    public void testDeserialisePJson(Blackhole blackhole) throws IOException {
        pjsonPrimerStream.reset();
        doc= pjsonDeserialiser.deserialiseDocument(pjsonPrimerStream);
        blackhole.consume(doc);
    }
    @Benchmark()
    public void testSerialisePJson(Blackhole blackhole) throws IOException {
        baos.reset();
        pjsonSerialiser.serialiseDocument(baos,doc,false);
        blackhole.consume(baos);
    }

    @Benchmark()
    public void testDeserialiseJson(Blackhole blackhole) throws IOException {
        provjsonldPrimerStream.reset();
        doc=rawJsonDeserialiser.deserialiseDocument(provjsonldPrimerStream);
        blackhole.consume(doc);
    }

    @Benchmark()
    public void testDeserialiseProvN(Blackhole blackhole) throws IOException {
        provnPrimerStream.reset();
        doc= provnDeserialiser.deserialiseDocument(provnPrimerStream);
        blackhole.consume(doc);
    }

    @Benchmark()
    public void testSerialiseProvN(Blackhole blackhole) throws IOException {
        baos.reset();
        provnSerialiser.serialiseDocument(baos,doc,false);
        blackhole.consume(baos);
    }

    @Benchmark
    public void testReadScala(Blackhole blackhole) throws IOException {
        provnPrimerStream.reset();
        doc= scalaDeserialiser.deserialiseDocument(provnPrimerStream);
        blackhole.consume(doc);
    }


}
