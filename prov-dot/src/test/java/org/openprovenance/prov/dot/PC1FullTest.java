package org.openprovenance.prov.dot;
import javax.xml.bind.JAXBException;

/**
 * 
 */


public class PC1FullTest extends org.openprovenance.prov.xml.PC1FullTest {

    public PC1FullTest( String testName ) {
         super(testName);
    }

    @Override
    public void testPC1Full() throws JAXBException {
    }

    @Override
    public void testCopyPC1Full() throws java.io.FileNotFoundException,  java.io.IOException   {
    }

    public void testPC1Full_A() throws JAXBException, java.io.FileNotFoundException,  java.io.IOException   {
        super.testPC1Full();
        super.testCopyPC1Full();

	/** Produces a dot representation
	 * of created graph. */

        ProvToDot toDot=new ProvToDot(true); // with roles
        
        toDot.convert(graph1,"target/pc1-full.dot", "target/pc1-full.pdf");
    }

    @Override
    public void testSchemaValidateXML() {
        // no need to do it here
    }

    @Override
    public void testSchemaFailValidateXML() {
        // no need to do it here
    }

}
