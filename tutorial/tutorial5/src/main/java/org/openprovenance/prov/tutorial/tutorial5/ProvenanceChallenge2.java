package org.openprovenance.prov.tutorial.tutorial5;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropFramework.ProvFormat;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasGeneratedBy;

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
public class ProvenanceChallenge2 implements Challenge {

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

    public ProvenanceChallenge2(ProvFactory pFactory) {
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
           pFactory.addType(a, pFactory.newType(pFactory.newQualifiedName(PRIM_NS, "File", PRIM_PREFIX),name.PROV_QUALIFIED_NAME));
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
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.tutorial.tutorial5.Challenge#align(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Collection<StatementOrBundle> align(String imgfile, String imglabel, 
                                               String hdrfile, String hdrlabel,
                                               String imgreffile1, String imgreflabel, 
                                               String hdrreffile1, String hdrreflabel, 
                                               String activity,
                                               String warpfile, String warplabel,
                                               String workflow, String agent) {
        
        Collection<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();
        Activity a1 = pFactory.newActivity(q(activity));
        pFactory.addType(a1, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);
        
        Entity e1 = newFile(pFactory, imgreffile1, imgreflabel);
        Entity e2 = newFile(pFactory, hdrreffile1, hdrreflabel);
        Entity e3 = newFile(pFactory, imgfile, imglabel);
        Entity e4 = newFile(pFactory, hdrfile, hdrlabel);
        
        ll.addAll(Arrays.asList(a1,e1,e2,e3,e4));

        ll.add(newUsed(a1, "img", e3));
        ll.add(newUsed(a1, "hdr", e4));
        ll.add(newUsed(a1, "imgRef", e1));
        ll.add(newUsed(a1, "hdrRef", e2));
        
        Entity e11 = newFile(pFactory, warpfile, warplabel);
        
        ll.add(e11);

        ll.add(newWasGeneratedBy(e11, "out", a1));
        
        ll.add(newWasDerivedFrom(e11, e1));
        ll.add(newWasDerivedFrom(e11, e2));
        ll.add(newWasDerivedFrom(e11, e3));
        ll.add(newWasDerivedFrom(e11, e4));
        
        return ll;
        
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.tutorial.tutorial5.Challenge#reslice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Collection<StatementOrBundle> reslice(String warp, 
                                                 String activity, 
                                                 String imgfile, String imglabel,
                                                 String hdrfile, String hdrlabel,
                                                 String workflow, String agent)  {
        
        Collection<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();
        
        Activity a5 = pFactory.newActivity(q(activity));
        pFactory.addType(a5, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Entity e15 = newFile(pFactory, imgfile, imglabel);
        Entity e16 = newFile(pFactory, hdrfile, hdrlabel);
        
        Entity e11 = pFactory.newEntity(q(warp));
        ll.add(newUsed(a5, "in", e11));
        ll.add(newWasGeneratedBy(e15, "img", a5));
        ll.add(newWasGeneratedBy(e16, "hdr", a5));
        
        ll.add(newWasDerivedFrom(e15, e11));
        ll.add(newWasDerivedFrom(e16, e11));
        
        ll.addAll(Arrays.asList(a5,e15,e16,e11));

        ll.add(newUsed(a5, "in", e11));
        
        return ll;

    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.tutorial.tutorial5.Challenge#softmean(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Collection<StatementOrBundle> softmean(String imgfile1, String hdrfile1,
                                                  String imgfile2, String hdrfile2,
                                                  String imgfile3, String hdrfile3,
                                                  String imgfile4, String hdrfile4,
                                                  String activity, 
                                                  String imgatlas, String imglabel,
                                                  String hdratlas, String hdrlabel,
                                                  String workflow, String agent) {
        
        Collection<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();

        
        Activity a9 = pFactory.newActivity(q("a#softmean"));
        pFactory.addType(a9, PRIMITIVE_SOFTMEAN, name.PROV_QUALIFIED_NAME);
        
        Entity e15 = pFactory.newEntity(q(imgfile1));
        Entity e16 = pFactory.newEntity(q(hdrfile1));
        Entity e17 = pFactory.newEntity(q(imgfile2));
        Entity e18 = pFactory.newEntity(q(hdrfile2));
        Entity e19 = pFactory.newEntity(q(imgfile3));
        Entity e20 = pFactory.newEntity(q(hdrfile3));
        Entity e21 = pFactory.newEntity(q(imgfile4));
        Entity e22 = pFactory.newEntity(q(hdrfile4));

        
        ll.add(newUsed(a9, "i1", e15));
        ll.add(newUsed(a9, "h1", e16));
        ll.add(newUsed(a9, "i2", e17));
        ll.add(newUsed(a9, "h2", e18));
        ll.add(newUsed(a9, "i3", e19));
        ll.add(newUsed(a9, "h3", e20));
        ll.add(newUsed(a9, "i4", e21));
        ll.add(newUsed(a9, "h4", e22));
        
        Entity e23 = newFile(pFactory, imgatlas, imglabel);
        Entity e24 = newFile(pFactory, hdratlas, hdrlabel);
        
        ll.addAll(Arrays.asList(a9,e15,e16,e17,e18,e19,e20,e21,e22,e23,e24));

        
        ll.add(newWasGeneratedBy(e23, "img", a9));
        ll.add(newWasGeneratedBy(e24, "hdr", a9));
        
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
        
        return ll;

        
    }
    /* (non-Javadoc)
     * @see org.openprovenance.prov.tutorial.tutorial5.Challenge#slice(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Collection<StatementOrBundle> slice(String imgatlas, 
                                               String hdratlas, 
                                               String params, String paramslabel, String paramsvalue,
                                               String activity, 
                                               String pgmfile, String pgmlabel,
                                               String workflow, String agent)  {
        Collection<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();
        
        Activity a10 = pFactory.newActivity(q(activity));
        pFactory.addType(a10, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);
        
        Entity e23 = pFactory.newEntity(q(imgatlas));
        Entity e24 = pFactory.newEntity(q(hdratlas));
        Entity e25p = newParameter(pFactory, "params#slicer1", "slicer param 1", "-x .5");
        
        Entity e25 = newFile(pFactory, pgmfile, pgmlabel);
        
        ll.addAll(Arrays.asList(a10,e23,e24,e25p,e25));

        
        ll.add(newUsed(a10, "img", e23));
        ll.add(newUsed(a10, "hdr", e24));
        ll.add(newUsed(a10, "param", e25p));
        
        ll.add(newWasGeneratedBy(e25, "out", a10));
        
        ll.add(newWasDerivedFrom(e25, e23));
        ll.add(newWasDerivedFrom(e25, e24));
        return ll;
        
    }
    
    /* (non-Javadoc)
     * @see org.openprovenance.prov.tutorial.tutorial5.Challenge#convert(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public Collection<StatementOrBundle> convert(String pgmfile, 
                                                 String activity, 
                                                 String giffile, String giflabel,
                                                 String workflow, String agent)  {
        
        Collection<StatementOrBundle> ll=new LinkedList<StatementOrBundle>();

        Activity a13 = pFactory.newActivity(q(activity));
        pFactory.addType(a13, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);
        
        Entity e25 = pFactory.newEntity(q(pgmfile));

        Entity e28 = newFile(pFactory, giffile, giflabel);
        
        ll.add(newUsed(a13, "in", e25));
        
        WasGeneratedBy wg18 = newWasGeneratedBy(e28, "out", a13);
        wg18.setTime(pFactory.newTimeNow());
        
        ll.addAll(Arrays.asList(a13,e25,e28,wg18));
        return ll;
        
    }
        
    
    
    public Document makePC1FullGraph(ProvFactory pFactory) {
        Document graph = pFactory.newDocument();
        List<StatementOrBundle> ll=graph.getStatementOrBundle();
        
        ll.addAll(align("anatomy1.img", "Anatomy I1", "anatomy1.hdr", "Anatomy H1", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp1","warp1.warp", "Warp Params1", "a#pcworkflow","ag1"));
        ll.addAll(align("anatomy2.img", "Anatomy I2", "anatomy2.hdr", "Anatomy H2", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp2","warp2.warp", "Warp Params2", "a#pcworkflow","ag1"));
        ll.addAll(align("anatomy3.img", "Anatomy I3", "anatomy3.hdr", "Anatomy H3", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp3","warp3.warp", "Warp Params3", "a#pcworkflow","ag1"));
        ll.addAll(align("anatomy4.img", "Anatomy I4", "anatomy4.hdr", "Anatomy H4", "reference.img", "Reference Image", "reference.hdr", "Reference Header", "a#align_warp4","warp4.warp", "Warp Params4", "a#pcworkflow","ag1"));


        ll.addAll(reslice("warp1.warp", "a#reslice1", "reslice1.img", "Resliced I1", "reslice1.hdr", "Resliced H1", "a#pcworkflow","ag1"));
        ll.addAll(reslice("warp2.warp", "a#reslice2", "reslice2.img", "Resliced I2", "reslice2.hdr", "Resliced H2", "a#pcworkflow","ag1"));
        ll.addAll(reslice("warp3.warp", "a#reslice3", "reslice3.img", "Resliced I3", "reslice3.hdr", "Resliced H3", "a#pcworkflow","ag1"));
        ll.addAll(reslice("warp4.warp", "a#reslice4", "reslice4.img", "Resliced I4", "reslice4.hdr", "Resliced H4", "a#pcworkflow","ag1"));


        ll.addAll(softmean("reslice1.img", "reslice1.hdr", "reslice2.img", "reslice2.hdr", "reslice3.img", "reslice3.hdr", "reslice4.img", "reslice4.hdr", "a#softmean", "atlas.img", "Atlas Image", "atlas.hdr", "Atlas Header", "a#pcworkflow","ag1"));


        ll.addAll(slice("atlas.img", "atlas.hdr",  "params#slicer1", "slicer param 1", "-x .5", "a#slice1", "atlas-x.pgm", "Atlas X slice", "a#pcworkflow","ag1"));
        ll.addAll(slice("atlas.img", "atlas.hdr",  "params#slicer2", "slicer param 2", "-y .5", "a#slice2", "atlas-y.pgm", "Atlas Y slice", "a#pcworkflow","ag1"));
        ll.addAll(slice("atlas.img", "atlas.hdr",  "params#slicer3", "slicer param 3", "-z .5", "a#slice3", "atlas-z.pgm", "Atlas Z slice", "a#pcworkflow","ag1"));

        
        ll.addAll(convert("atlas-x.pgm", "a#convert1", "atlas-x.gif", "Atlas X Graphic", "a#pcworkflow","ag1"));
        ll.addAll(convert("atlas-y.pgm", "a#convert2", "atlas-y.gif", "Atlas Y Graphic", "a#pcworkflow","ag1"));
        ll.addAll(convert("atlas-z.pgm", "a#convert3", "atlas-z.gif", "Atlas Z Graphic", "a#pcworkflow","ag1"));
        
        graph.setNamespace(Namespace.gatherNamespaces(graph));

        return graph;

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

    public Document makeDocument() {

        return makePC1FullGraph(pFactory);

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

        ProvenanceChallenge2 little = new ProvenanceChallenge2(InteropFramework.newXMLProvFactory());
        little.openingBanner();
        Document document = little.makeDocument();
        little.doConversions(document, file);
        little.closingBanner();

    }

}
