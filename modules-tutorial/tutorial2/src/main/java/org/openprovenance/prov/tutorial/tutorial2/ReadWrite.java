package org.openprovenance.prov.tutorial.tutorial2;

import java.util.Arrays;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.interop.Formats.ProvFormat;
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
 * ProvToolbox Tutorial 1: Reading, Converting and Saving PROV Documents.
 * 
 * @author lucmoreau
 * @see <a href="https://lucmoreau.wordpress.com/2015/07/05/provtoolbox-tutorial-2-reading-converting-and-saving-prov-documents/">tutorial-2-reading-converting-and-saving-prov-documents blog post</a>
 */
public class ReadWrite {

    
    public static final String PROVBOOK_NS = "http://www.provbook.org";
    public static final String PROVBOOK_PREFIX = "provbook";
    
    public static final String JIM_PREFIX = "jim";
    public static final String JIM_NS = "http://www.cs.rpi.edu/~hendler/";

    private final ProvFactory pFactory;
    private final Namespace ns;
    public ReadWrite(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(PROVBOOK_PREFIX, PROVBOOK_NS);
        ns.register(JIM_PREFIX, JIM_NS);
    }

    
    public void doConversions(String filein, String fileout) {
        InteropFramework intF=new InteropFramework();
        Document document=intF.readDocumentFromFile(filein);
        intF.writeDocument(fileout, document);     
//       / intF.writeDocument(System.out, ProvFormat.XML, document);
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
        if (args.length!=2) throw new UnsupportedOperationException("main to be called with two filenames");
        String filein=args[0];
        String fileout=args[1];
        
        ReadWrite tutorial=new ReadWrite(InteropFramework.getDefaultFactory());
        tutorial.openingBanner();
        tutorial.doConversions(filein, fileout);
        tutorial.closingBanner();

    }

}
