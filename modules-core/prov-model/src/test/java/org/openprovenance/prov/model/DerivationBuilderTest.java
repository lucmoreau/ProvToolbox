package org.openprovenance.prov.model;

import junit.framework.TestCase;
import org.openprovenance.prov.bookptm.DerivationBuilder;
import org.openprovenance.prov.model.builder.Builder;
import org.openprovenance.prov.model.builder.Prefix;

import java.io.IOException;


public class DerivationBuilderTest extends TestCase {


    public static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();
    public static Name name=pFactory.getName();

    public void testBuild() throws IOException {

        Document doc = new DerivationBuilder().makeDerivation();

        assertEquals(24,doc.getStatementOrBundle().size());

    }


}
