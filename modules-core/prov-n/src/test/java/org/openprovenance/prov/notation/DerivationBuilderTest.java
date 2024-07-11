package org.openprovenance.prov.notation;


import org.openprovenance.prov.bookptm.DerivationBuilder;
import org.openprovenance.prov.model.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DerivationBuilderTest extends org.openprovenance.prov.model.DerivationBuilderTest {


    @Override
    public void testBuild() throws IOException {
        Document doc=new DerivationBuilder().makeDerivation();
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-derivation.provn").toPath()), doc, true);
    }

    public void testOther() throws IOException {

        DerivationBuilder builder=new DerivationBuilder();
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-transporting.provn").toPath()), builder.makeDocument_Transporting(), true);
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-transporting-full-triangle.provn").toPath()), builder.makeDocument_TransportingFullTriangle(), true);
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-weighing.provn").toPath()), builder.makeDocument_Weighing(), true);
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-transporting-attribution.provn").toPath()), builder.makeDocument_TransportingAttribution(), true);
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-attribution.provn").toPath()), builder.makeDocument_Attribution(), true);
        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/example-specialization.provn").toPath()), builder.makeDocument_Specialization(), true);

    }
}
