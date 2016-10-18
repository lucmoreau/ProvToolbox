package org.openprovenance.prov.tutorial.tutorial5;

import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.notation.Utility;
import org.openprovenance.prov.template.Bindings;
import org.openprovenance.prov.template.Expand;
import org.openprovenance.prov.template.Groupings;

import static org.openprovenance.prov.template.Expand.VAR_NS;
import static org.openprovenance.prov.template.Expand.TMPL_NS;


/**
 * 
 * ProvToolbox Tutorial 5: provenance challenge workflow in Java and serializing
 * it to SVG (in a file) and to PROVN (on the console).
 * 
 * @author lucmoreau
 * @see <a
 *      href="http://twiki.ipaw.info/bin/view/Challenge/FirstProvenanceChallenge">provenance
 *      challenge</a>
 */
public class ProvenanceChallenge1Template  extends ChallengeUtil implements Challenge<Bindings>, Variables {



    public ProvenanceChallenge1Template(ProvFactory pFactory) {
        super(pFactory);
    }

    /** Binding variable */
    public QualifiedName b_var(String name) {
        return pFactory.newQualifiedName(VAR_NS, name, "var");
    }
    /** Template Variable */
    public QualifiedName t_var(String name) {
        return pFactory.newQualifiedName(VAR_NS, name, "var");
    }

    

    public List<TypedValue> makeAttributeValue(String s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pFactory.newOther(pFactory.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.XSD_STRING));
        return ll;
    }
    
    public List<TypedValue> makeAttributeValue(QualifiedName s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pFactory.newOther(pFactory.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.PROV_QUALIFIED_NAME));
        return ll;
    }
    
    
    /** These attributes "partially" instantiate a template for align, with a set of constants */
    void align_static(Bindings bindings1) {
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_IMG)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_HDR)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_IMG_REF)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_HDR_REF)));     
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), makeAttributeValue(prim_val(FILE)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), makeAttributeValue(prim_val(FILE)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), makeAttributeValue(prim_val(FILE)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), makeAttributeValue(prim_val(FILE)));
        
        bindings1.addAttribute(b_var(VAR_BLOCK_TYPE),    makeAttributeValue(prim_val(ALIGN_WARP)));
        bindings1.addAttribute(b_var(VAR_PRODUCED_TYPE), makeAttributeValue(prim_val(FILE)));
        bindings1.addAttribute(b_var(VAR_PRODUCED_NAME), makeAttributeValue(prim_val(ROLE_OUT)));


        
    }

    public Bindings align(String imgfile1, String imglabel, 
                          String hdrfile1, String hdrlabel,
                          String imgreffile1, String imgreflabel, 
                          String hdrreffile1, String hdrreflabel, 
                          String activity, 
                          String warpfile, String warplabel,
                          String workflow, String agent)  {
	
        Bindings bindings1=new Bindings(pFactory);
        align_static(bindings1);
        
        bindings1.addVariable(b_var(VAR_CONSUMED),       q(imgfile1));
        bindings1.addVariable(b_var(VAR_CONSUMED),       q(hdrfile1));      
        bindings1.addVariable(b_var(VAR_CONSUMED),       q(imgreffile1));
        bindings1.addVariable(b_var(VAR_CONSUMED),       q(hdrreffile1));      

        bindings1.addAttribute(t_var(VAR_CONSUMED_LABEL),makeAttributeValue(imglabel));
        bindings1.addAttribute(t_var(VAR_CONSUMED_LABEL),makeAttributeValue(hdrlabel));
        bindings1.addAttribute(t_var(VAR_CONSUMED_LABEL),makeAttributeValue(imgreflabel));
        bindings1.addAttribute(t_var(VAR_CONSUMED_LABEL),makeAttributeValue(hdrreflabel));
        
        bindings1.addVariable(b_var(VAR_BLOCK_INSTANCE), q(activity));      
   
        bindings1.addVariable(b_var(VAR_PRODUCED),       q(warpfile));      
        bindings1.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(warplabel));
        
      //  if (workflow!=null) bindings1.addVariable(b_var(VAR_PARENT),         prim_val(workflow));      
      //  if (agent!=null)    bindings1.addVariable(b_var(VAR_AGENT),          prim_val(agent));      
        
        return bindings1;
    }
    
    public Bindings reslice(String warp, 
                            String activity, 
                            String imgfile, String imglabel,
                            String hdrfile, String hdrlabel,
                            String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        
        bindings.addVariable(b_var(VAR_CONSUMED),       q(warp));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_IN)));
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), makeAttributeValue(prim_val(FILE)));
        
        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), q(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    makeAttributeValue(prim_val(RESLICE)));
        
        bindings.addVariable(b_var(VAR_PRODUCED),       q(imgfile));      
        bindings.addVariable(b_var(VAR_PRODUCED),       q(hdrfile));      
        bindings.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(imglabel));
        bindings.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(hdrlabel));
        bindings.addAttribute(t_var(VAR_PRODUCED_NAME),makeAttributeValue(prim_val(ROLE_IMG)));
        bindings.addAttribute(t_var(VAR_PRODUCED_NAME),makeAttributeValue(prim_val(ROLE_HDR)));
        bindings.addAttribute(t_var(VAR_PRODUCED_TYPE),makeAttributeValue(prim_val(FILE)));
        bindings.addAttribute(t_var(VAR_PRODUCED_TYPE),makeAttributeValue(prim_val(FILE)));
        
       // if (workflow!=null) bindings.addVariable(b_var(VAR_PARENT),         prim_val(workflow));      
       // if (agent!=null)    bindings.addVariable(b_var(VAR_AGENT),          prim_val(agent));      

        return bindings;
    }
    
    public Bindings softmean(String imgfile1, String hdrfile1,
                             String imgfile2, String hdrfile2,
                             String imgfile3, String hdrfile3,
                             String imgfile4, String hdrfile4,
                             String activity, 
                             String imgatlas, String imglabel,
                             String hdratlas, String hdrlabel,
                             String workflow, String agent)  {

        Bindings bindings=new Bindings(pFactory);
        
        bindings.addVariable(b_var(VAR_CONSUMED),       q(imgfile1));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(hdrfile1));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(imgfile2));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(hdrfile2));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(imgfile3));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(hdrfile3));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(imgfile4));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(hdrfile4));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_I1)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_H1)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_I2)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_H2)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_I3)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_H3)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_I4)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_H4)));
        
        
        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), q(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    makeAttributeValue(prim_val(SOFTMEAN)));

        bindings.addVariable(b_var(VAR_PRODUCED),       q(imgatlas));      
        bindings.addVariable(b_var(VAR_PRODUCED),       q(hdratlas));      
        bindings.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(imglabel));
        bindings.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(hdrlabel));
        bindings.addAttribute(t_var(VAR_PRODUCED_NAME),makeAttributeValue(prim_val(ROLE_IMG)));
        bindings.addAttribute(t_var(VAR_PRODUCED_NAME),makeAttributeValue(prim_val(ROLE_HDR)));
        bindings.addAttribute(t_var(VAR_PRODUCED_TYPE),makeAttributeValue(prim_val(FILE)));
        bindings.addAttribute(t_var(VAR_PRODUCED_TYPE),makeAttributeValue(prim_val(FILE)));

       // if (workflow!=null) bindings.addVariable(b_var(VAR_PARENT),         prim_val(workflow));      
       // if (agent!=null)    bindings.addVariable(b_var(VAR_AGENT),          prim_val(agent));      
        
        
        return bindings;
    }
    
    public Bindings slice(String imgatlas, 
                          String hdratlas, 
                          String params, String paramslabel, String paramsvalue,
                          String activity, 
                          String pgmfile, String pgmlabel,
                          String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        bindings.addVariable(b_var(VAR_CONSUMED),       q(imgatlas));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(hdratlas));
        bindings.addVariable(b_var(VAR_CONSUMED),       q(params));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_IMG)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_HDR)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_PARAM))); 
        bindings.addAttribute(b_var(VAR_CONSUMED_LABEL), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_LABEL), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_LABEL), makeAttributeValue(paramslabel));  
        bindings.addAttribute(b_var(VAR_CONSUMED_VALUE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_VALUE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_VALUE), makeAttributeValue(paramsvalue));
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), makeAttributeValue(prim_val(STRING)));

        
        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), q(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    makeAttributeValue(prim_val(SLICER)));
        
        bindings.addVariable(b_var(VAR_PRODUCED),       q(pgmfile));      
        bindings.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(pgmlabel));
        bindings.addAttribute(t_var(VAR_PRODUCED_NAME),makeAttributeValue(prim_val(ROLE_OUT)));
        bindings.addAttribute(t_var(VAR_PRODUCED_TYPE),makeAttributeValue(prim_val(FILE)));
        
       // if (workflow!=null) bindings.addVariable(b_var(VAR_PARENT),         prim_val(workflow));      
       // if (agent!=null)    bindings.addVariable(b_var(VAR_AGENT),          prim_val(agent));      
        
        return bindings;
    }
    
    
    public Bindings convert(String pgmfile, 
                            String activity, 
                            String giffile, String giflabel,
                            String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        
        bindings.addVariable(b_var(VAR_CONSUMED),       q(pgmfile));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), makeAttributeValue(prim_val(ROLE_IN)));

        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), q(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    makeAttributeValue(prim_val(CONVERT)));

        bindings.addVariable(b_var(VAR_PRODUCED),       q(giffile));      
        bindings.addAttribute(t_var(VAR_PRODUCED_LABEL),makeAttributeValue(giflabel));
        bindings.addAttribute(t_var(VAR_PRODUCED_NAME),makeAttributeValue(prim_val(ROLE_OUT)));
        bindings.addAttribute(t_var(VAR_PRODUCED_TYPE),makeAttributeValue(prim_val(FILE)));

       // if (workflow!=null) bindings.addVariable(b_var(VAR_PARENT),         prim_val(workflow));      
       // if (agent!=null)    bindings.addVariable(b_var(VAR_AGENT),          prim_val(agent));              
        
        return bindings;
    }
    
    
    public List<Bindings> makeBindings() {
        List<Bindings> res=new LinkedList<Bindings>();
        res.add(align("anatomy1.img", "Anatomy I1", "anatomy1.hdr", "Anatomy H1", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp1","warp1.warp", "Warp Params1", "a#pcworkflow","ag1"));
        res.add(align("anatomy2.img", "Anatomy I2", "anatomy2.hdr", "Anatomy H2", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp2","warp2.warp", "Warp Params2", "a#pcworkflow","ag1"));
        res.add(align("anatomy3.img", "Anatomy I3", "anatomy3.hdr", "Anatomy H3", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp3","warp3.warp", "Warp Params3", "a#pcworkflow","ag1"));
        res.add(align("anatomy4.img", "Anatomy I4", "anatomy4.hdr", "Anatomy H4", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp4","warp4.warp", "Warp Params4", "a#pcworkflow","ag1"));

        res.add(reslice("warp1.warp", "a#reslice1", "resliced1.img", "Resliced I1", "resliced1.hdr", "Resliced H1", "a#pcworkflow","ag1"));
        res.add(reslice("warp2.warp", "a#reslice2", "resliced2.img", "Resliced I2", "resliced2.hdr", "Resliced H2", "a#pcworkflow","ag1"));
        res.add(reslice("warp3.warp", "a#reslice3", "resliced3.img", "Resliced I3", "resliced3.hdr", "Resliced H3", "a#pcworkflow","ag1"));
        res.add(reslice("warp4.warp", "a#reslice4", "resliced4.img", "Resliced I4", "resliced4.hdr", "Resliced H4", "a#pcworkflow","ag1"));

        res.add(softmean("resliced1.img", "resliced1.hdr", "resliced2.img", "resliced2.hdr", "resliced3.img", "resliced3.hdr", "resliced4.img", "resliced4.hdr", "a#softmean", "atlas.img", "Atlas Image", "atlas.hdr", "Atlas Header", "a#pcworkflow","ag1"));

        res.add(slice("atlas.img", "atlas.hdr",  "params#slicer1", "slicer param 1", "-x .5", "a#slicer1", "atlas-x.pgm", "Atlas X Slice", "a#pcworkflow","ag1"));
        res.add(slice("atlas.img", "atlas.hdr",  "params#slicer2", "slicer param 2", "-y .5", "a#slicer2", "atlas-y.pgm", "Atlas Y Slice", "a#pcworkflow","ag1"));
        res.add(slice("atlas.img", "atlas.hdr",  "params#slicer3", "slicer param 3", "-z .5", "a#slicer3", "atlas-z.pgm", "Atlas Z Slice", "a#pcworkflow","ag1"));

        
        res.add(convert("atlas-x.pgm", "a#convert1", "atlas-x.gif", "Atlas X Graphic", "a#pcworkflow","ag1"));
        res.add(convert("atlas-y.pgm", "a#convert2", "atlas-y.gif", "Atlas Y Graphic", "a#pcworkflow","ag1"));
        res.add(convert("atlas-z.pgm", "a#convert3", "atlas-z.gif", "Atlas Z Graphic", "a#pcworkflow","ag1"));
        
        return res;
    }

    

    
    public Document expander(Document docIn, Bindings bindings1) {
        
        Expand expand=new Expand(pFactory, false, true);

        Bundle bun = (Bundle) docIn.getStatementOrBundle().get(0);


        Groupings grp1 = Groupings.fromDocument(docIn);

        Bundle bun1 = (Bundle) expand.expand(bun, bindings1, grp1).get(0);
        Document doc1 = pFactory.newDocument();
        doc1.getStatementOrBundle().add(bun1);

        bun1.setNamespace(Namespace.gatherNamespaces(bun1));

        // doc1.setNamespace(bun1.getNamespace());
        doc1.setNamespace(new Namespace());

        return doc1;
    }


    public Document makeDocument() {
        
        System.out.println("******************");
        
        IndexedDocument iDoc=new IndexedDocument(pFactory, pFactory.newDocument(), true);

        //return makePC1FullGraph(pFactory, URL_LOCATION, URL_LOCATION);
        try {
        
            Document doc= new Utility().readDocument("src/main/resources/template_block.provn", pFactory);
            
            
            for (Bindings bindings : makeBindings()) {
            
                Document eDoc=expander(doc, bindings);
                iDoc.merge(eDoc);
            }
            return iDoc.toDocument();
            
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
            
        }


    }
    public static void main(String[] args) {
        if (args.length != 2)
            throw new UnsupportedOperationException("main to be called with filename");
        String file1 = args[0];
        String file2 = args[1];

        ProvenanceChallenge1Template pc1 = new ProvenanceChallenge1Template(InteropFramework.newXMLProvFactory());
        pc1.openingBanner();
        Document document = pc1.makeDocument();
        pc1.doConversions(document, file1);
        pc1.doConversions(document, file2);
        pc1.closingBanner();

    }

}
