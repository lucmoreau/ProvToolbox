package org.openprovenance.prov.tutorial.tutorial1;

import java.util.Arrays;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;

/**
 * A little provenance goes a long way. 
 * ProvToolbox Tutorial 1: creating a provenance document in Java and serializing it
 * to SVG (in a file) and to PROVN (on the console).
 * 
 * @author lucmoreau
 * @see <a href="http://blog.provbook.org/2013/10/11/a-little-provenance-goes-a-long-way/">a-little-provenance-goes-a-long-way blog post</a>
 */
public class Little {

    
    public static final String PROVBOOK_NS = "http://www.provbook.org";
    public static final String PROVBOOK_PREFIX = "provbook";
    
    public static final String JIM_PREFIX = "jim";
    public static final String JIM_NS = "http://www.cs.rpi.edu/~hendler/";

    private final ProvFactory pFactory;
    private final Namespace ns;
    public Little(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(PROVBOOK_PREFIX, PROVBOOK_NS);
        ns.register(JIM_PREFIX, JIM_NS);
    }

    public QualifiedName qn(String n) {
        return ns.qualifiedName(PROVBOOK_PREFIX, n, pFactory);
    }

    public Document makeLittle() {     
       
        Entity quote = pFactory.newEntity(qn("a-little-provenance-goes-a-long-way"));
        quote.setValue(pFactory.newValue("A little provenance goes a long way",
                                         pFactory.getName().XSD_STRING));

        Agent paul = pFactory.newAgent(qn("Paul"), "Paul Groth");
        Agent luc = pFactory.newAgent(qn("Luc"), "Luc Moreau");

        WasAttributedTo attr1 = pFactory.newWasAttributedTo(null,
                                                            quote.getId(),
                                                            paul.getId());
        WasAttributedTo attr2 = pFactory.newWasAttributedTo(null,
                                                            quote.getId(),
                                                            luc.getId());

        Entity original = pFactory.newEntity(ns.qualifiedName(JIM_PREFIX,"LittleSemanticsWeb.html",pFactory));

        WasDerivedFrom wdf = pFactory.newWasDerivedFrom(quote.getId(),
                                                        original.getId());

        Document document = pFactory.newDocument();
        document.getStatementOrBundle()
                .addAll(Arrays.asList(new StatementOrBundle[] { quote, 
                                                                paul,
                                                                luc, 
                                                                attr1,
                                                                attr2, 
                                                                original,
                                                                wdf }));

        document.setNamespace(ns);

        return document;
    }
    
    public void doConversions(Document document, String file) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file, document);     
        intF.writeDocument(System.out, ProvFormat.PROVN, document);

    }

    public void closingBanner() {
        System.out.println("");
        System.out.println("*************************");
    }

    public void openingBanner() {
        System.out.println("*************************");
        System.out.println("* Converting document  ");
        System.out.println("*************************");
    }
    
    public static void main(String [] args) {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file=args[0];
        
        Little little=new Little(org.openprovenance.prov.xml.ProvFactory.getFactory());
        little.openingBanner();
        little.doConversions(little.makeLittle(), file);
        little.closingBanner();

    }

}
