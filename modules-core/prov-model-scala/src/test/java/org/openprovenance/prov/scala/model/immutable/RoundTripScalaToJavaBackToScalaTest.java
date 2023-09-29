package org.openprovenance.prov.scala.model.immutable;

import org.openprovenance.prov.model.RoundTripFromJavaTest;
import org.openprovenance.prov.vanilla.ProvFactory;
import org.openprovenance.prov.model.BeanTraversal;
import org.openprovenance.prov.model.Document;

/**
 * Unit test for PROV roundtrip conversion between Java represenations
 */
abstract public class RoundTripScalaToJavaBackToScalaTest extends RoundTripFromJavaTest {

    public RoundTripScalaToJavaBackToScalaTest() {
        test = false;
        pFactory = new org.openprovenance.prov.scala.immutable.ProvFactory();
    }


    ProvFactory pFactoryJ = new ProvFactory();

    @Override
    public void compareDocAndFile(Document doc, String file, boolean check) {
        BeanTraversal bc = new BeanTraversal(pFactoryJ, pFactoryJ);
        Document doc2 = bc.doAction(doc);

        BeanTraversal bc2 = new BeanTraversal(pFactory, pFactory);
        Document doc3 = bc2.doAction(doc2);

        Document doc4 = bc2.doAction(doc3);

        documentComparator.compareDocuments(doc, doc3, check && checkTest(file));
    }

    public boolean checkSchema(String name) {
        return false;
    }

    public boolean checkTest(String name) {
        if (name.endsWith("entity101" + extension())
                || name.endsWith("bundle6" + extension())) {
            System.out.println(escapeRed("########## Skipping testing for " + name + " in " + extension()));
            return false;
        }
        return true;
    }

}
