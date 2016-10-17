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
public class ProvenanceChallenge1Template  extends ChallengeUtil implements Challenge<Bindings> {


    
    private final Namespace ns;

    public ProvenanceChallenge1Template(ProvFactory pFactory) {
        super(pFactory);
        ns = new Namespace();
        ns.addKnownNamespaces();

    }

    /** Binding variable */
    public QualifiedName b_var(String name) {
        return pFactory.newQualifiedName(VAR_NS, name, "var");
    }
    /** Template Variable */
    public QualifiedName t_var(String name) {
        return pFactory.newQualifiedName(VAR_NS, name, "var");
    }
    
    public QualifiedName file_val(String name) {
        return pFactory.newQualifiedName(PC1_NS, name, PC1_PREFIX);
    }
    public QualifiedName prim_val(String name) {
        return pFactory.newQualifiedName(PRIM_NS, name, PRIM_PREFIX);
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
    

    public Bindings align(String imgfile1, String imglabel, 
                                  String hdrfile1, String hdrlabel,
                                  String imgreffile1, String imgreflabel, 
                                  String hdrreffile1, String hdrreflabel, 
                                  String activity, 
                                  String warpfile, String warplabel,
                                  String workflow, String agent)  {
	
        Bindings bindings1=new Bindings(pFactory);
        bindings1.addVariable(b_var("consumed"),       file_val(imgfile1));
        bindings1.addVariable(b_var("consumed"),       file_val(hdrfile1));      
        bindings1.addVariable(b_var("consumed"),       file_val(imgreffile1));
        bindings1.addVariable(b_var("consumed"),       file_val(hdrreffile1));      
        bindings1.addVariable(b_var("block_instance"), prim_val(activity));      
        bindings1.addVariable(b_var("produced"),       file_val(warpfile));      

        if (workflow!=null) bindings1.addVariable(b_var("parent"),         prim_val(workflow));      
        if (agent!=null)    bindings1.addVariable(b_var("agent"),          prim_val(agent));      
        
        bindings1.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Img")));
        bindings1.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Hdr")));
        bindings1.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("ImgRef")));
        bindings1.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("HdrRef")));
        bindings1.addAttribute(b_var("block_type"),    makeAttributeValue(prim_val("Align_warp")));
        bindings1.addAttribute(b_var("consumed_type"), makeAttributeValue(prim_val("File")));
        bindings1.addAttribute(b_var("consumed_type"), makeAttributeValue(prim_val("File")));
        bindings1.addAttribute(b_var("consumed_type"), makeAttributeValue(prim_val("File")));
        bindings1.addAttribute(b_var("consumed_type"), makeAttributeValue(prim_val("File")));
        bindings1.addAttribute(b_var("produced_type"), makeAttributeValue(prim_val("File")));
        bindings1.addAttribute(t_var("produced_label"),makeAttributeValue(warplabel));
        bindings1.addAttribute(t_var("consumed_label"),makeAttributeValue(imglabel));
        bindings1.addAttribute(t_var("consumed_label"),makeAttributeValue(hdrlabel));
        bindings1.addAttribute(t_var("consumed_label"),makeAttributeValue(hdrreflabel));
        bindings1.addAttribute(t_var("consumed_label"),makeAttributeValue(hdrreflabel));
        
        return bindings1;
    }
    
    public Bindings reslice(String warp, 
                            String activity, 
                            String imgfile, String imglabel,
                            String hdrfile, String hdrlabel,
                            String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        bindings.addVariable(b_var("consumed"),       file_val(warp));
        bindings.addVariable(b_var("block_instance"), prim_val(activity));      
        bindings.addVariable(b_var("produced"),       file_val(imgfile));      
        bindings.addVariable(b_var("produced"),       file_val(hdrfile));      

        if (workflow!=null) bindings.addVariable(b_var("parent"),         prim_val(workflow));      
        if (agent!=null)    bindings.addVariable(b_var("agent"),          prim_val(agent));      
        
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Warp")));
        bindings.addAttribute(b_var("consumed_type"), makeAttributeValue(prim_val("File")));
        bindings.addAttribute(b_var("block_type"),    makeAttributeValue(prim_val("Align_warp")));
        bindings.addAttribute(t_var("produced_label"),makeAttributeValue(imglabel));
        bindings.addAttribute(t_var("produced_label"),makeAttributeValue(hdrlabel));
        bindings.addAttribute(t_var("produced_name"),makeAttributeValue(prim_val("Img")));
        bindings.addAttribute(t_var("produced_name"),makeAttributeValue(prim_val("Hdr")));
        bindings.addAttribute(t_var("produced_type"),makeAttributeValue(prim_val("File")));
        bindings.addAttribute(t_var("produced_type"),makeAttributeValue(prim_val("File")));
        
        
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
        bindings.addVariable(b_var("consumed"),       file_val(imgfile1));
        bindings.addVariable(b_var("consumed"),       file_val(hdrfile1));
        bindings.addVariable(b_var("consumed"),       file_val(imgfile2));
        bindings.addVariable(b_var("consumed"),       file_val(hdrfile2));
        bindings.addVariable(b_var("consumed"),       file_val(imgfile3));
        bindings.addVariable(b_var("consumed"),       file_val(hdrfile3));
        bindings.addVariable(b_var("consumed"),       file_val(imgfile4));
        bindings.addVariable(b_var("consumed"),       file_val(hdrfile4));
        bindings.addVariable(b_var("block_instance"), prim_val(activity));      
        bindings.addVariable(b_var("produced"),       file_val(imgatlas));      
        bindings.addVariable(b_var("produced"),       file_val(hdratlas));      

        if (workflow!=null) bindings.addVariable(b_var("parent"),         prim_val(workflow));      
        if (agent!=null)    bindings.addVariable(b_var("agent"),          prim_val(agent));      
        
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("i1")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("h1")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("i2")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("h3")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("i3")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("h3")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("i4")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("h4")));
        bindings.addAttribute(b_var("block_type"),    makeAttributeValue(prim_val("Align_warp")));
        bindings.addAttribute(t_var("produced_label"),makeAttributeValue(imglabel));
        bindings.addAttribute(t_var("produced_label"),makeAttributeValue(hdrlabel));
        bindings.addAttribute(t_var("produced_name"),makeAttributeValue(prim_val("Img")));
        bindings.addAttribute(t_var("produced_name"),makeAttributeValue(prim_val("Hdr")));
        bindings.addAttribute(t_var("produced_type"),makeAttributeValue(prim_val("File")));
        bindings.addAttribute(t_var("produced_type"),makeAttributeValue(prim_val("File")));
        
        
        return bindings;
    }
    
    public Bindings slice(String imgatlas, 
                          String hdratlas, 
                          String params, String paramslabel, String paramsvalue,
                          String activity, 
                          String pgmfile, String pgmlabel,
                          String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        bindings.addVariable(b_var("consumed"),       file_val(imgatlas));
        bindings.addVariable(b_var("consumed"),       file_val(hdratlas));
        bindings.addVariable(b_var("consumed"),       file_val(params));
        bindings.addVariable(b_var("block_instance"), prim_val(activity));      
        bindings.addVariable(b_var("produced"),       file_val(pgmfile));      

        if (workflow!=null) bindings.addVariable(b_var("parent"),         prim_val(workflow));      
        if (agent!=null)    bindings.addVariable(b_var("agent"),          prim_val(agent));      
        
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Img")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Hdr")));
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Param"))); 
        bindings.addAttribute(b_var("consumed_label"), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var("consumed_label"), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var("consumed_label"), makeAttributeValue(paramslabel));  
        bindings.addAttribute(b_var("consumed_value"), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var("consumed_value"), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var("consumed_value"), makeAttributeValue(paramsvalue));  
        bindings.addAttribute(b_var("block_type"),    makeAttributeValue(prim_val("Reslice")));
        bindings.addAttribute(t_var("produced_label"),makeAttributeValue(pgmlabel));
        bindings.addAttribute(t_var("produced_name"),makeAttributeValue(prim_val("Out")));
        bindings.addAttribute(t_var("produced_type"),makeAttributeValue(prim_val("File")));
        
        
        return bindings;
    }
    
    
    public Bindings convert(String pgmfile, 
                            String activity, 
                            String giffile, String giflabel,
                            String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        bindings.addVariable(b_var("consumed"),       file_val(pgmfile));
        bindings.addVariable(b_var("block_instance"), prim_val(activity));      
        bindings.addVariable(b_var("produced"),       file_val(giffile));      

        if (workflow!=null) bindings.addVariable(b_var("parent"),         prim_val(workflow));      
        if (agent!=null)    bindings.addVariable(b_var("agent"),          prim_val(agent));      
        
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("In")));
        bindings.addAttribute(b_var("block_type"),    makeAttributeValue(prim_val("Convert")));
        bindings.addAttribute(t_var("produced_label"),makeAttributeValue(giflabel));
        bindings.addAttribute(t_var("produced_name"),makeAttributeValue(prim_val("Out")));
        bindings.addAttribute(t_var("produced_type"),makeAttributeValue(prim_val("File")));
        
        
        return bindings;
    }
    
    
    public List<Bindings> makeBindings() {
        List<Bindings> res=new LinkedList<Bindings>();
        res.add(align("anatomy1.img", "Anatomy I1", "anatomy1.hdr", "Anatomy H1", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp1","warp1.warp", "Warp Params1", "a#pcworkflow","ag1"));
        res.add(align("anatomy2.img", "Anatomy I2", "anatomy2.hdr", "Anatomy H2", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp2","warp2.warp", "Warp Params2", "a#pcworkflow","ag1"));
        res.add(align("anatomy3.img", "Anatomy I3", "anatomy3.hdr", "Anatomy H3", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp3","warp3.warp", "Warp Params3", "a#pcworkflow","ag1"));
        res.add(align("anatomy4.img", "Anatomy I4", "anatomy4.hdr", "Anatomy H4", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp4","warp4.warp", "Warp Params4", "a#pcworkflow","ag1"));

        
        res.add(reslice("warp1.warp", "a#reslice1", "reslice1.img", "Resliced I1", "reslice1.hdr", "Resliced H1", "a#pcworkflow","ag1"));
        res.add(reslice("warp2.warp", "a#reslice2", "reslice2.img", "Resliced I2", "reslice2.hdr", "Resliced H2", "a#pcworkflow","ag1"));
        res.add(reslice("warp3.warp", "a#reslice3", "reslice3.img", "Resliced I3", "reslice3.hdr", "Resliced H3", "a#pcworkflow","ag1"));
        res.add(reslice("warp4.warp", "a#reslice4", "reslice4.img", "Resliced I4", "reslice4.hdr", "Resliced H4", "a#pcworkflow","ag1"));

        
        
        res.add(softmean("reslice1.img", "reslice1.hdr", "reslice2.img", "reslice2.hdr", "reslice3.img", "reslice3.hdr", "reslice4.img", "reslice4.hdr", "a#softmean", "atlas.img", "Atlas Image", "atlas.hdr", "Atlas Header", "a#pcworkflow","ag1"));

        
        res.add(slice("atlas.img", "atlas.hdr",  "params#slicer1", "slicer param 1", "-x .5", "a#slice1", "atlas-x.pgm", "Atlas X slice", "a#pcworkflow","ag1"));
        res.add(slice("atlas.img", "atlas.hdr",  "params#slicer2", "slicer param 2", "-y .5", "a#slice2", "atlas-y.pgm", "Atlas Y slice", "a#pcworkflow","ag1"));
        res.add(slice("atlas.img", "atlas.hdr",  "params#slicer3", "slicer param 3", "-z .5", "a#slice3", "atlas-z.pgm", "Atlas Z slice", "a#pcworkflow","ag1"));

        
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
        if (args.length != 1)
            throw new UnsupportedOperationException("main to be called with filename");
        String file = args[0];

        ProvenanceChallenge1Template little = new ProvenanceChallenge1Template(InteropFramework.newXMLProvFactory());
        little.openingBanner();
        Document document = little.makeDocument();
        little.doConversions(document, file);
        little.closingBanner();

    }

}
