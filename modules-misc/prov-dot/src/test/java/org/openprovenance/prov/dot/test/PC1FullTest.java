package org.openprovenance.prov.dot.test;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


import org.openprovenance.prov.dot.ProvToDot;
import org.openprovenance.prov.viz.ProvToMermaid;
import org.openprovenance.prov.vanilla.ProvFactory;


/**
 * 
 */


public class PC1FullTest extends org.openprovenance.prov.model.test.PC1FullTest {

    @Override
    public void subtestCopyPC1Full() throws java.io.FileNotFoundException,  java.io.IOException   {
    }

    @Override
    public void testPC1() throws  java.io.IOException   {
        super.testPC1();
        subtestDoToDot();
        subtestDoToMermaid();
    }

    void subtestDoToDot() throws IOException {
        ProvToDot toDot=new ProvToDot(new ProvFactory());

        if (graph1==null) System.out.println("doToDot with null ");
        System.out.println("writing target/pc1-full.pdf");
        toDot.convert(graph1,"target/pc1-full.dot", "target/pc1-full.pdf", "PC1 Full");
        System.out.println("writing target/pc1-full_2.svg");
        toDot.convert(graph1,"target/pc1-full_2.dot", "target/pc1-full_2.svg", "svg", "PC1 Full");
    }

    /** The same graph by the mermaid driver, so target/pc1-full.pdf and target/pc1-full.mermaid.pdf compare. */
    void subtestDoToMermaid() throws IOException {
        ProvToMermaid toMermaid=new ProvToMermaid(new ProvFactory());

        System.out.println("writing target/pc1-full.mmd");
        toMermaid.convert(graph1, "target/pc1-full.mmd", "PC1 Full");
        if (!toMermaid.mmdcAvailable()) {
            System.out.println("########## mmdc not found: target/pc1-full.mermaid.pdf not rendered");
            return;
        }
        for (String type : List.of("pdf", "svg")) {
            String file = "target/pc1-full.mermaid." + type;
            System.out.println("writing " + file);
            try (FileOutputStream out = new FileOutputStream(file)) {
                toMermaid.convert(graph1, out, type, "PC1 Full");
            }
        }
    }

}
