package org.openprovenance.prov.notation;


import org.openprovenance.prov.model.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PC1BuilderTest extends org.openprovenance.prov.model.PC1BuilderTest {


    @Override
    public void testBuild() throws IOException {
        Document doc=makePC1();

        new ProvSerialiser(pFactory).serialiseDocument(Files.newOutputStream(new File("target/pc1-builder.provn").toPath()), doc, true);
    }
}
