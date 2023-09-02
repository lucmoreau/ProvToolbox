package org.openprovenance.prov.dot;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.vanilla.ProvFactory;
import org.xml.sax.SAXException;


/**
 * 
 */


public class PC1FullTest extends org.openprovenance.prov.model.PC1FullTest {

    @Override
    public void subtestCopyPC1Full() throws java.io.FileNotFoundException,  java.io.IOException   {
    }

    @Override
    public void testPC1() throws  java.io.IOException   {
        super.testPC1();
        subtestDoToDot();
    }
    
    void subtestDoToDot() throws FileNotFoundException, IOException {
	/** Produces a dot representation
	 * of created graph. */

        ProvToDot toDot=new ProvToDot(new ProvFactory()); // with roles

	if (graph1==null) System.out.println("doToDot with null ");
        
        toDot.convert(graph1,"target/pc1-full.dot", "target/pc1-full.pdf", "PC1 Full");
    }

}
