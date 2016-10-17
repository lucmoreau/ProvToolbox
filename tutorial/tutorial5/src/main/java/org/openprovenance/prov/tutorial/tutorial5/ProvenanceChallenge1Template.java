package org.openprovenance.prov.tutorial.tutorial5;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.IndexedDocument;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;
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
public class ProvenanceChallenge1Template {

    public static final String PC1_NS = "http://www.ipaw.info/challenge/";
    public static final String PC1_PREFIX = "pc1";
    public static final String PRIM_NS = "http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX = "prim";

    static String PATH_PROPERTY = "http://openprovenance.org/primitives#path";
    static String URL_PROPERTY = "http://openprovenance.org/primitives#url";
    static String PRIMITIVE_PROPERTY = "http://openprovenance.org/primitives#primitive";
    static String FILE_LOCATION = "/shomewhere/pc1/";
    static String URL_LOCATION = "http://www.ipaw.info/challenge/";

 //   static URI PRIMITIVE_RESLICE = URI.create("http://openprovenance.org/primitives#Reslice");
 //   static URI PRIMITIVE_SOFTMEAN = URI.create("http://openprovenance.org/primitives#Softmean");
 //   static URI PRIMITIVE_CONVERT = URI.create("http://openprovenance.org/primitives#Convert");
    //static URI PRIMITIVE_SLICER = URI.create("http://openprovenance.org/primitives#Slicer");

    QualifiedName PRIMITIVE_ALIGN_WARP;
    QualifiedName PRIMITIVE_CONVERT;
    QualifiedName PRIMITIVE_SLICER;
    QualifiedName PRIMITIVE_RESLICE;
    QualifiedName PRIMITIVE_SOFTMEAN;
    
    Name name;

    private final ProvFactory pFactory;
    private final Namespace ns;

    public ProvenanceChallenge1Template(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns = new Namespace();
        ns.addKnownNamespaces();

        PRIMITIVE_ALIGN_WARP = pFactory.newQualifiedName(PRIM_NS, "Align_warp", PRIM_PREFIX);
        PRIMITIVE_CONVERT    = pFactory.newQualifiedName(PRIM_NS, "Convert", PRIM_PREFIX);
        PRIMITIVE_SLICER     = pFactory.newQualifiedName(PRIM_NS, "Slicer", PRIM_PREFIX);
        PRIMITIVE_RESLICE    = pFactory.newQualifiedName(PRIM_NS, "Reslice", PRIM_PREFIX);
        PRIMITIVE_SOFTMEAN   = pFactory.newQualifiedName(PRIM_NS, "Softmean", PRIM_PREFIX);
        name = pFactory.getName();

    }

    public void addUrl(HasOther p1, String val) {
        p1.getOther().add(pFactory.newOther(PC1_NS, "url", PC1_PREFIX, val, name.XSD_STRING));
    }
    
    public Entity newFile(ProvFactory pFactory,
                             String id,
                             String label) {

           Entity a = pFactory.newEntity(q(id), label);
           pFactory.addType(a, pFactory.newType(pFactory.newQualifiedName(PRIM_NS, "FILE", PRIM_PREFIX),name.PROV_QUALIFIED_NAME));
           return a;
    }

    Entity newParameter(ProvFactory pFactory, String id, String label, String value) {

        Entity a = pFactory.newEntity(q(id), label);
        //pFactory.addType(a, URI.create("http://openprovenance.org/primitives#String"));
        pFactory.addType(a, pFactory.newType(pFactory.newQualifiedName(PRIM_NS, "String", PRIM_PREFIX),name.PROV_QUALIFIED_NAME));


        addValue(a, value);

        return a;
    }

    public void addValue(HasOther p1, String val) {
        p1.getOther().add(pFactory.newOther(PC1_NS, "value", PC1_PREFIX, val, name.XSD_STRING));
    }

    public QualifiedName q(String n) {
        // return new org.openprovenance.prov.xml.QualifiedName(PC1_NS, n,
        // PC1_PREFIX);
        return pFactory.newQualifiedName(PC1_NS, n, PC1_PREFIX);
    }

    // public QualifiedName qn(String n) {
    // return ns.qualifiedName(PROVBOOK_PREFIX, n, pFactory);
    // }

    public Document makePC1FullGraph(ProvFactory pFactory,
                                     String inputLocation,
                                     String outputLocation) {

        Document graph = pFactory.newDocument();
        List<StatementOrBundle> ll=graph.getStatementOrBundle();
        

        Activity a1 = pFactory.newActivity(q("a#align_warp1"));
        pFactory.addType(a1, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a2 = pFactory.newActivity(q("a#align_warp2"));
        pFactory.addType(a2, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a3 = pFactory.newActivity(q("a#align_warp3"));
        pFactory.addType(a3, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a4 = pFactory.newActivity(q("a#align_warp4"));
        pFactory.addType(a4, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a5 = pFactory.newActivity(q("a#reslice1"));
        pFactory.addType(a5, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a6 = pFactory.newActivity(q("a#reslice2"));
        pFactory.addType(a6, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a7 = pFactory.newActivity(q("a#reslice3"));
        pFactory.addType(a7, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a8 = pFactory.newActivity(q("a#reslice4"));
        pFactory.addType(a8, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a9 = pFactory.newActivity(q("a#softmean"));
        pFactory.addType(a9, PRIMITIVE_SOFTMEAN, name.PROV_QUALIFIED_NAME);

        Activity a10 = pFactory.newActivity(q("a#slicer1"));
        pFactory.addType(a10, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);

        Activity a11 = pFactory.newActivity(q("a#slicer2"));
        pFactory.addType(a11, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);

        Activity a12 = pFactory.newActivity(q("a#slicer3"));
        pFactory.addType(a12, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);

        Activity a13 = pFactory.newActivity(q("a#convert1"));
        pFactory.addType(a13, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);

        Activity a14 = pFactory.newActivity(q("a#convert2"));
        pFactory.addType(a14, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);

        Activity a15 = pFactory.newActivity(q("a#convert3"));
        pFactory.addType(a15, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);
        
        ll.addAll(Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15));

        Agent ag1 = pFactory.newAgent(q("ag1"), "John Doe");
        
        ll.add(ag1);

        Entity e1 = newFile(pFactory, "reference.img", "Reference Image");

        Entity e2 = newFile(pFactory, "reference.hdr", "Reference Header");

        Entity e3 = newFile(pFactory, "anatomy1.img", "Anatomy I1");

        Entity e4 = newFile(pFactory, "anatomy1.hdr", "Anatomy H1");

        Entity e5 = newFile(pFactory, "anatomy2.img", "Anatomy I2");

        Entity e6 = newFile(pFactory, "anatomy2.hdr", "Anatomy H2");

        Entity e7 = newFile(pFactory, "anatomy3.img", "Anatomy I3");

        Entity e8 = newFile(pFactory, "anatomy3.hdr", "Anatomy H3");

        Entity e9 = newFile(pFactory, "anatomy4.img", "Anatomy I4");

        Entity e10 = newFile(pFactory, "anatomy4.hdr", "Anatomy H4");

        Entity e11 = newFile(pFactory, "warp1.warp", "Warp Params1");

        Entity e12 = newFile(pFactory, "warp2.warp", "Warp Params2");

        Entity e13 = newFile(pFactory, "warp3.warp", "Warp Params3");

        Entity e14 = newFile(pFactory, "warp4.warp", "Warp Params4");

        Entity e15 = newFile(pFactory, "resliced1.img", "Resliced I1");
        Entity e16 = newFile(pFactory, "resliced1.hdr", "Resliced H1");
        Entity e17 = newFile(pFactory, "resliced2.img", "Resliced I2");
        Entity e18 = newFile(pFactory, "resliced2.hdr", "Resliced H2");
        Entity e19 = newFile(pFactory, "resliced3.img", "Resliced I3");
        Entity e20 = newFile(pFactory, "resliced3.hdr", "Resliced H3");
        Entity e21 = newFile(pFactory, "resliced4.img", "Resliced I4");
        Entity e22 = newFile(pFactory, "resliced4.hdr", "Resliced H4");

        Entity e23 = newFile(pFactory, "atlas.img", "Atlas Image");
        Entity e24 = newFile(pFactory, "atlas.hdr", "Atlas Header");

        Entity e25 = newFile(pFactory, "atlas-x.pgm", "Atlas X Slice");
        Entity e25p = newParameter(pFactory, "params#slider1", "slicer param 1", "-x .5");
        Entity e26 = newFile(pFactory, "atlas-y.pgm", "Atlas Y Slice");
        Entity e26p = newParameter(pFactory, "params#slider2", "slicer param 2", "-y .5");
        Entity e27 = newFile(pFactory, "atlas-z.pgm", "Atlas Z Slice");
        Entity e27p = newParameter(pFactory, "params#slider3", "slicer param 3", "-z .5");

        Entity e28 = newFile(pFactory, "atlas-x.gif", "Atlas X Graphic");
        Entity e29 = newFile(pFactory, "atlas-y.gif", "Atlas Y Graphic");
        Entity e30 = newFile(pFactory, "atlas-z.gif", "Atlas Z Graphic");
        
        ll.addAll(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
        ll.addAll(Arrays.asList(e11, e12, e13, e14, e15, e16, e17, e18, e19, e20));
        ll.addAll(Arrays.asList(e21, e22, e23, e24, e25, e25p, e26, e26p, e27, e27p, e28, e29, e30));

        ll.add(newUsed(a1, "img", e3));
        ll.add(newUsed(a1, "hdr", e4));
        ll.add(newUsed(a1, "imgRef", e1));
        ll.add(newUsed(a1, "hdrRef", e2));
        ll.add(newUsed(a2, "img", e5));
        ll.add(newUsed(a2, "hdr", e6));
        ll.add(newUsed(a2, "imgRef", e1));
        ll.add(newUsed(a2, "hdrRef", e2));
        ll.add(newUsed(a3, "img", e7));
        ll.add(newUsed(a3, "hdr", e8));
        ll.add(newUsed(a3, "imgRef", e1));
        ll.add(newUsed(a3, "hdrRef", e2));
        ll.add(newUsed(a4, "img", e9));
        ll.add(newUsed(a4, "hdr", e10));
        ll.add(newUsed(a4, "imgRef", e1));
        ll.add(newUsed(a4, "hdrRef", e2));

        ll.add(newUsed(a5, "in", e11));
        ll.add(newUsed(a6, "in", e12));
        ll.add(newUsed(a7, "in", e13));
        ll.add(newUsed(a8, "in", e14));

        ll.add(newUsed(a9, "i1", e15));
        ll.add(newUsed(a9, "h1", e16));
        ll.add(newUsed(a9, "i2", e17));
        ll.add(newUsed(a9, "h2", e18));
        ll.add(newUsed(a9, "i3", e19));
        ll.add(newUsed(a9, "h3", e20));
        ll.add(newUsed(a9, "i4", e21));
        ll.add(newUsed(a9, "h4", e22));

        ll.add(newUsed(a10, "img", e23));
        ll.add(newUsed(a10, "hdr", e24));
        ll.add(newUsed(a10, "param", e25p));
        ll.add(newUsed(a11, "img", e23));
        ll.add(newUsed(a11, "hdr", e24));
        ll.add(newUsed(a11, "param", e26p));
        ll.add(newUsed(a12, "img", e23));
        ll.add(newUsed(a12, "hdr", e24));
        ll.add(newUsed(a12, "param", e27p));

        ll.add(newUsed(a13, "in", e25));
        ll.add(newUsed(a14, "in", e26));
        ll.add(newUsed(a15, "in", e27));

        ll.add(newWasGeneratedBy(e11, "out", a1));
        ll.add(newWasGeneratedBy(e12, "out", a2));
        ll.add(newWasGeneratedBy(e13, "out", a3));
        ll.add(newWasGeneratedBy(e14, "out", a4));

        ll.add(newWasGeneratedBy(e15, "img", a5));
        ll.add(newWasGeneratedBy(e16, "hdr", a5));
        ll.add(newWasGeneratedBy(e17, "img", a6));
        ll.add(newWasGeneratedBy(e18, "hdr", a6));
        ll.add(newWasGeneratedBy(e19, "img", a7));
        ll.add(newWasGeneratedBy(e20, "hdr", a7));
        ll.add(newWasGeneratedBy(e21, "img", a8));
        ll.add(newWasGeneratedBy(e22, "hdr", a8));

        ll.add(newWasGeneratedBy(e23, "img", a9));
        ll.add(newWasGeneratedBy(e24, "hdr", a9));

        ll.add(newWasGeneratedBy(e25, "out", a10));
        ll.add(newWasGeneratedBy(e26, "out", a11));
        ll.add(newWasGeneratedBy(e27, "out", a12));

        WasGeneratedBy wg18 = newWasGeneratedBy(e28, "out", a13);
        WasGeneratedBy wg19 = newWasGeneratedBy(e29, "out", a14);
        WasGeneratedBy wg20 = newWasGeneratedBy(e30, "out", a15);
        wg18.setTime(pFactory.newTimeNow());
        wg19.setTime(pFactory.newTimeNow());
        wg20.setTime(pFactory.newTimeNow());
        ll.addAll(Arrays.asList(wg18,wg19,wg20));

        ll.add(newWasDerivedFrom(e11, e1));
        ll.add(newWasDerivedFrom(e11, e2));
        ll.add(newWasDerivedFrom(e11, e3));
        ll.add(newWasDerivedFrom(e11, e4));
        ll.add(newWasDerivedFrom(e12, e1));
        ll.add(newWasDerivedFrom(e12, e2));
        ll.add(newWasDerivedFrom(e12, e5));
        ll.add(newWasDerivedFrom(e12, e6));
        ll.add(newWasDerivedFrom(e13, e1));
        ll.add(newWasDerivedFrom(e13, e2));
        ll.add(newWasDerivedFrom(e13, e7));
        ll.add(newWasDerivedFrom(e13, e8));
        ll.add(newWasDerivedFrom(e14, e1));
        ll.add(newWasDerivedFrom(e14, e2));
        ll.add(newWasDerivedFrom(e14, e9));
        ll.add(newWasDerivedFrom(e14, e10));
        ll.add(newWasDerivedFrom(e15, e11));
        ll.add(newWasDerivedFrom(e16, e11));
        ll.add(newWasDerivedFrom(e17, e12));
        ll.add(newWasDerivedFrom(e18, e12));
        ll.add(newWasDerivedFrom(e19, e13));
        ll.add(newWasDerivedFrom(e20, e13));
        ll.add(newWasDerivedFrom(e21, e14));
        ll.add(newWasDerivedFrom(e22, e14));
        ll.add(newWasDerivedFrom(e23, e15));
        ll.add(newWasDerivedFrom(e23, e16));
        ll.add(newWasDerivedFrom(e23, e17));
        ll.add(newWasDerivedFrom(e23, e18));
        ll.add(newWasDerivedFrom(e23, e19));
        ll.add(newWasDerivedFrom(e23, e20));
        ll.add(newWasDerivedFrom(e23, e21));
        ll.add(newWasDerivedFrom(e23, e22));
        ll.add(newWasDerivedFrom(e24, e15));
        ll.add(newWasDerivedFrom(e24, e16));
        ll.add(newWasDerivedFrom(e24, e17));
        ll.add(newWasDerivedFrom(e24, e18));
        ll.add(newWasDerivedFrom(e24, e19));
        ll.add(newWasDerivedFrom(e24, e20));
        ll.add(newWasDerivedFrom(e24, e21));
        ll.add(newWasDerivedFrom(e24, e22));
        ll.add(newWasDerivedFrom(e25, e23));
        ll.add(newWasDerivedFrom(e25, e24));
        ll.add(newWasDerivedFrom(e26, e23));
        ll.add(newWasDerivedFrom(e26, e24));
        ll.add(newWasDerivedFrom(e27, e23));
        ll.add(newWasDerivedFrom(e27, e24));
        ll.add(newWasDerivedFrom(e28, e25));
        ll.add(newWasDerivedFrom(e29, e26));
        ll.add(newWasDerivedFrom(e30, e27));

        WasAssociatedWith waw1 = pFactory.newWasAssociatedWith(q("waw1"), a1.getId(), ag1.getId());
        ll.add(waw1);

        graph.setNamespace(Namespace.gatherNamespaces(graph));

        return graph;
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
    

    public Bindings makeBindings1(String imgfile1, String imglabel, 
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
    
    public Bindings makeBindings2(String warp, 
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
    
    public Bindings makeBindings3(String imgfile1, String hdrfile1,
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
    
    public Bindings makeBindings4(String imgatlas, 
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
        bindings.addAttribute(b_var("consumed_name"), makeAttributeValue(prim_val("Param")));  //TODO: VALUE
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
    
    
    public Bindings makeBindings5(String pgmfile, 
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
        res.add(makeBindings1("anatomy1.img", "Anatomy I1", "anatomy1.hdr", "Anatomy H1", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp1","warp1.warp", "Warp Params1", "a#pcworkflow","ag1"));
        res.add(makeBindings1("anatomy2.img", "Anatomy I2", "anatomy2.hdr", "Anatomy H2", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp2","warp2.warp", "Warp Params2", "a#pcworkflow","ag1"));
        res.add(makeBindings1("anatomy3.img", "Anatomy I3", "anatomy3.hdr", "Anatomy H3", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp3","warp3.warp", "Warp Params3", "a#pcworkflow","ag1"));
        res.add(makeBindings1("anatomy4.img", "Anatomy I4", "anatomy4.hdr", "Anatomy H4", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp4","warp4.warp", "Warp Params4", "a#pcworkflow","ag1"));

        
        res.add(makeBindings2("warp1.warp", "a#reslice1", "reslice1.img", "Resliced I1", "reslice1.hdr", "Resliced H1", "a#pcworkflow","ag1"));
        res.add(makeBindings2("warp2.warp", "a#reslice2", "reslice2.img", "Resliced I2", "reslice2.hdr", "Resliced H2", "a#pcworkflow","ag1"));
        res.add(makeBindings2("warp3.warp", "a#reslice3", "reslice3.img", "Resliced I3", "reslice3.hdr", "Resliced H3", "a#pcworkflow","ag1"));
        res.add(makeBindings2("warp4.warp", "a#reslice4", "reslice4.img", "Resliced I4", "reslice4.hdr", "Resliced H4", "a#pcworkflow","ag1"));

        
        
        res.add(makeBindings3("reslice1.img", "reslice1.hdr", "reslice2.img", "reslice2.hdr", "reslice3.img", "reslice3.hdr", "reslice4.img", "reslice4.hdr", "a#softmean", "atlas.img", "Atlas Image", "atlas.hdr", "Atlas Header", "a#pcworkflow","ag1"));

        
        res.add(makeBindings4("atlas.img", "atlas.hdr",  "params#slicer1", "slicer param 1", "-x .5", "a#slice1", "atlas-x.pgm", "Atlas X slice", "a#pcworkflow","ag1"));
        res.add(makeBindings4("atlas.img", "atlas.hdr",  "params#slicer2", "slicer param 2", "-y .5", "a#slice2", "atlas-y.pgm", "Atlas Y slice", "a#pcworkflow","ag1"));
        res.add(makeBindings4("atlas.img", "atlas.hdr",  "params#slicer3", "slicer param 3", "-z .5", "a#slice3", "atlas-z.pgm", "Atlas Z slice", "a#pcworkflow","ag1"));

        
        res.add(makeBindings5("atlas-x.pgm", "a#convert1", "atlas-x.gif", "Atlas X Graphic", "a#pcworkflow","ag1"));
        res.add(makeBindings5("atlas-y.pgm", "a#convert2", "atlas-y.gif", "Atlas Y Graphic", "a#pcworkflow","ag1"));
        res.add(makeBindings5("atlas-z.pgm", "a#convert3", "atlas-z.gif", "Atlas Z Graphic", "a#pcworkflow","ag1"));
        
        
        return res;
    }

    



    public Used newUsed(Activity activity, String role, Entity entity) {
        return newUsed(activity.getId(), role, entity.getId());
    }

    public Used newUsed(org.openprovenance.prov.model.QualifiedName activity,
                        String role,
                        org.openprovenance.prov.model.QualifiedName entity) {
        Used u1 = pFactory.newUsed(activity, entity);
        u1.getRole().add(pFactory.newRole(role, name.XSD_STRING));
        return u1;

    }

    public WasGeneratedBy newWasGeneratedBy(Entity entity, String role, Activity activity) {
        return newWasGeneratedBy(entity.getId(), role, activity.getId());
    }

    public WasGeneratedBy newWasGeneratedBy(org.openprovenance.prov.model.QualifiedName entity,
                                            String role,
                                            org.openprovenance.prov.model.QualifiedName activity) {
        WasGeneratedBy u1 = pFactory.newWasGeneratedBy(null, entity, activity);
        u1.getRole().add(pFactory.newRole(role, name.XSD_STRING));
        return u1;

    }

    public WasDerivedFrom newWasDerivedFrom(Entity entity2, Entity entity1) {
        WasDerivedFrom wdf = pFactory.newWasDerivedFrom(null, entity2.getId(), entity1.getId());
        return wdf;
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

    public void doConversions(Document document, String file) {
        InteropFramework intF = new InteropFramework();
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
