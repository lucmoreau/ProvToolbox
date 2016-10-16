package org.openprovenance.prov.tutorial.tutorial5;

import java.net.URI;
import java.util.Arrays;
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
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
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
public class ProvenanceChallenge1 {

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

    public ProvenanceChallenge1(ProvFactory pFactory) {
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

        Agent ag1 = pFactory.newAgent(q("ag1"), "John Doe");

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

        Used u1 = newUsed(a1, "img", e3);
        Used u2 = newUsed(a1, "hdr", e4);
        Used u3 = newUsed(a1, "imgRef", e1);
        Used u4 = newUsed(a1, "hdrRef", e2);
        Used u5 = newUsed(a2, "img", e5);
        Used u6 = newUsed(a2, "hdr", e6);
        Used u7 = newUsed(a2, "imgRef", e1);
        Used u8 = newUsed(a2, "hdrRef", e2);
        Used u9 = newUsed(a3, "img", e7);
        Used u10 = newUsed(a3, "hdr", e8);
        Used u11 = newUsed(a3, "imgRef", e1);
        Used u12 = newUsed(a3, "hdrRef", e2);
        Used u13 = newUsed(a4, "img", e9);
        Used u14 = newUsed(a4, "hdr", e10);
        Used u15 = newUsed(a4, "imgRef", e1);
        Used u16 = newUsed(a4, "hdrRef", e2);

        Used u17 = newUsed(a5, "in", e11);
        Used u18 = newUsed(a6, "in", e12);
        Used u19 = newUsed(a7, "in", e13);
        Used u20 = newUsed(a8, "in", e14);

        Used u21 = newUsed(a9, "i1", e15);
        Used u22 = newUsed(a9, "h1", e16);
        Used u23 = newUsed(a9, "i2", e17);
        Used u24 = newUsed(a9, "h2", e18);
        Used u25 = newUsed(a9, "i3", e19);
        Used u26 = newUsed(a9, "h3", e20);
        Used u27 = newUsed(a9, "i4", e21);
        Used u28 = newUsed(a9, "h4", e22);

        Used u29 = newUsed(a10, "img", e23);
        Used u30 = newUsed(a10, "hdr", e24);
        Used u30p = newUsed(a10, "param", e25p);
        Used u31 = newUsed(a11, "img", e23);
        Used u32 = newUsed(a11, "hdr", e24);
        Used u32p = newUsed(a11, "param", e26p);
        Used u33 = newUsed(a12, "img", e23);
        Used u34 = newUsed(a12, "hdr", e24);
        Used u34p = newUsed(a12, "param", e27p);

        Used u35 = newUsed(a13, "in", e25);
        Used u36 = newUsed(a14, "in", e26);
        Used u37 = newUsed(a15, "in", e27);

        WasGeneratedBy wg1 = pFactory.newWasGeneratedBy(e11, "out", a1);
        WasGeneratedBy wg2 = pFactory.newWasGeneratedBy(e12, "out", a2);
        WasGeneratedBy wg3 = pFactory.newWasGeneratedBy(e13, "out", a3);
        WasGeneratedBy wg4 = pFactory.newWasGeneratedBy(e14, "out", a4);

        WasGeneratedBy wg5 = pFactory.newWasGeneratedBy(e15, "img", a5);
        WasGeneratedBy wg6 = pFactory.newWasGeneratedBy(e16, "hdr", a5);
        WasGeneratedBy wg7 = pFactory.newWasGeneratedBy(e17, "img", a6);
        WasGeneratedBy wg8 = pFactory.newWasGeneratedBy(e18, "hdr", a6);
        WasGeneratedBy wg9 = pFactory.newWasGeneratedBy(e19, "img", a7);
        WasGeneratedBy wg10 = pFactory.newWasGeneratedBy(e20, "hdr", a7);
        WasGeneratedBy wg11 = pFactory.newWasGeneratedBy(e21, "img", a8);
        WasGeneratedBy wg12 = pFactory.newWasGeneratedBy(e22, "hdr", a8);

        WasGeneratedBy wg13 = pFactory.newWasGeneratedBy(e23, "img", a9);
        WasGeneratedBy wg14 = pFactory.newWasGeneratedBy(e24, "hdr", a9);

        WasGeneratedBy wg15 = pFactory.newWasGeneratedBy(e25, "out", a10);
        WasGeneratedBy wg16 = pFactory.newWasGeneratedBy(e26, "out", a11);
        WasGeneratedBy wg17 = pFactory.newWasGeneratedBy(e27, "out", a12);

        WasGeneratedBy wg18 = pFactory.newWasGeneratedBy(e28, "out", a13);
        WasGeneratedBy wg19 = pFactory.newWasGeneratedBy(e29, "out", a14);
        WasGeneratedBy wg20 = pFactory.newWasGeneratedBy(e30, "out", a15);
        wg18.setTime(pFactory.newTimeNow());
        wg19.setTime(pFactory.newTimeNow());
        wg20.setTime(pFactory.newTimeNow());

        WasDerivedFrom wd1 = newWasDerivedFrom(e11, e1);
        WasDerivedFrom wd2 = newWasDerivedFrom(e11, e2);
        WasDerivedFrom wd3 = newWasDerivedFrom(e11, e3);
        WasDerivedFrom wd4 = newWasDerivedFrom(e11, e4);
        WasDerivedFrom wd5 = newWasDerivedFrom(e12, e1);
        WasDerivedFrom wd6 = newWasDerivedFrom(e12, e2);
        WasDerivedFrom wd7 = newWasDerivedFrom(e12, e5);
        WasDerivedFrom wd8 = newWasDerivedFrom(e12, e6);
        WasDerivedFrom wd9 = newWasDerivedFrom(e13, e1);
        WasDerivedFrom wd10 = newWasDerivedFrom(e13, e2);
        WasDerivedFrom wd11 = newWasDerivedFrom(e13, e7);
        WasDerivedFrom wd12 = newWasDerivedFrom(e13, e8);
        WasDerivedFrom wd13 = newWasDerivedFrom(e14, e1);
        WasDerivedFrom wd14 = newWasDerivedFrom(e14, e2);
        WasDerivedFrom wd15 = newWasDerivedFrom(e14, e9);
        WasDerivedFrom wd16 = newWasDerivedFrom(e14, e10);

        WasDerivedFrom wd17 = newWasDerivedFrom(e15, e11);
        WasDerivedFrom wd18 = newWasDerivedFrom(e16, e11);
        WasDerivedFrom wd19 = newWasDerivedFrom(e17, e12);
        WasDerivedFrom wd20 = newWasDerivedFrom(e18, e12);
        WasDerivedFrom wd21 = newWasDerivedFrom(e19, e13);
        WasDerivedFrom wd22 = newWasDerivedFrom(e20, e13);
        WasDerivedFrom wd23 = newWasDerivedFrom(e21, e14);
        WasDerivedFrom wd24 = newWasDerivedFrom(e22, e14);

        WasDerivedFrom wd25 = newWasDerivedFrom(e23, e15);
        WasDerivedFrom wd26 = newWasDerivedFrom(e23, e16);
        WasDerivedFrom wd27 = newWasDerivedFrom(e23, e17);
        WasDerivedFrom wd28 = newWasDerivedFrom(e23, e18);
        WasDerivedFrom wd29 = newWasDerivedFrom(e23, e19);
        WasDerivedFrom wd30 = newWasDerivedFrom(e23, e20);
        WasDerivedFrom wd31 = newWasDerivedFrom(e23, e21);
        WasDerivedFrom wd32 = newWasDerivedFrom(e23, e22);

        WasDerivedFrom wd33 = newWasDerivedFrom(e24, e15);
        WasDerivedFrom wd34 = newWasDerivedFrom(e24, e16);
        WasDerivedFrom wd35 = newWasDerivedFrom(e24, e17);
        WasDerivedFrom wd36 = newWasDerivedFrom(e24, e18);
        WasDerivedFrom wd37 = newWasDerivedFrom(e24, e19);
        WasDerivedFrom wd38 = newWasDerivedFrom(e24, e20);
        WasDerivedFrom wd39 = newWasDerivedFrom(e24, e21);
        WasDerivedFrom wd40 = newWasDerivedFrom(e24, e22);

        WasDerivedFrom wd41 = newWasDerivedFrom(e25, e23);
        WasDerivedFrom wd42 = newWasDerivedFrom(e25, e24);
        WasDerivedFrom wd43 = newWasDerivedFrom(e26, e23);
        WasDerivedFrom wd44 = newWasDerivedFrom(e26, e24);
        WasDerivedFrom wd45 = newWasDerivedFrom(e27, e23);
        WasDerivedFrom wd46 = newWasDerivedFrom(e27, e24);

        WasDerivedFrom wd47 = newWasDerivedFrom(e28, e25);
        WasDerivedFrom wd48 = newWasDerivedFrom(e29, e26);
        WasDerivedFrom wd49 = newWasDerivedFrom(e30, e27);

        WasAssociatedWith waw1 = pFactory.newWasAssociatedWith(q("waw1"), a1.getId(), ag1.getId());

        Document graph = pFactory.newDocument(new Activity[] { a1, a2, a3, a4, a5, a6, a7, a8, a9,
                a10, a11, a12, a13, a14, a15 }, new Entity[] { e1, e2, e5, e6, e3, e4, e7, e8, e9,
                e10, e11, e12, e13, e14, e15, e16, e17, e18, e19, e20, e21, e22, e23, e24, e25,
                e25p, e26, e26p, e27, e27p, e28, e29, e30 }, new Agent[] { ag1 }, new Statement[] {
                u1, u2, u3, u4, u5, u6, u7, u8, u9, u10, u11, u12, u13, u14, u15, u16, u17, u18,
                u19, u20, u21, u22, u23, u24, u25, u26, u27, u28, u29, u30, u31, u32, u33, u34,
                u30p, u32p, u34p, u35, u36, u37, wg1, wg2, wg3, wg4, wg5, wg6, wg7, wg8, wg9, wg10,
                wg11, wg12, wg13, wg14, wg15, wg16, wg17, wg18, wg19, wg20, wd1, wd2, wd3, wd4,
                wd5, wd6, wd7, wd8, wd9, wd10, wd11, wd12, wd13, wd14, wd15, wd16, wd17, wd18,
                wd19, wd20, wd21, wd22, wd23, wd24, wd25, wd26, wd27, wd28, wd29, wd30, wd31, wd32,
                wd33, wd34, wd35, wd36, wd37, wd38, wd39, wd40, wd41, wd42, wd43, wd44, wd45, wd46,
                wd47, wd48, wd49, waw1 });

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

        return makePC1FullGraph(pFactory, URL_LOCATION, URL_LOCATION);

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

        ProvenanceChallenge1 little = new ProvenanceChallenge1(InteropFramework.newXMLProvFactory());
        little.openingBanner();
        Document document = little.makeDocument();
        little.doConversions(document, file);
        little.closingBanner();

    }

}
