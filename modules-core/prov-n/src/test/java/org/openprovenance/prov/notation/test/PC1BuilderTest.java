package org.openprovenance.prov.notation.test;


import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.notation.ProvSerialiser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PC1BuilderTest extends org.openprovenance.prov.model.test.PC1BuilderTest {


    @Override
    public void testBuild() throws IOException {
        Document doc=makePC1();

        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/pc1-builder.provn").toPath()), doc, true);
    }
}
