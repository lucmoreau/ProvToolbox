package org.openprovenance.prov.xml;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Hashtable;
import java.net.URI;
import javax.xml.bind.JAXBException;


import junit.framework.TestCase;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.QualifiedName;

/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class ContextualizationPC1Test extends TestCase {

    public static final String PC1_NS = "http://www.ipaw.info/pc1/";
    public static final String PC1_PREFIX = "pc1";
    public static final String PRIM_NS = "http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX = "prim";
    public static final String DOT_NS = "http://openprovenance.org/Toolbox/dot#";
    public static final String DOT_PREFIX = "dot";
    public static final String EX_NS = "http://example.com/";
    public static final String EX_PREFIX = "ex";


    public static ProvFactory pFactory=new ProvFactory();
    public static Name name=pFactory.getName();
    static final ProvUtilities util=new ProvUtilities();
    public static ValueConverter vconv;

    /**
     * Create the test case
     * 
     * @param testName
     *            name of the test case
     */
    public ContextualizationPC1Test(String testName) {
	super(testName);
    }

    public boolean urlFlag = true;

    /**
     * @return the suite of tests being tested
     */

    public static Document graph1;
    public static Document graph2;

    public void testPC1SpecFull() throws JAXBException {
	Document graph = makePC1GraphAndSpecialization(pFactory);

	ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
        Namespace.withThreadNamespace(graph.getNamespace());

	serial.serialiseDocument(new File("target/pc1-spec.xml"), graph, true);

	graph1 = graph;
	//System.out.println("PC1Full Test asserting True");
	assertTrue(true);

    }

    static String PATH_PROPERTY = "http://openprovenance.org/primitives#path";
    static String URL_PROPERTY = "http://openprovenance.org/primitives#url";
    static String PRIMITIVE_PROPERTY = "http://openprovenance.org/primitives#primitive";
    static String FILE_LOCATION = "/shomewhere/pc1/";
    static String URL_LOCATION = "http://www.ipaw.info/challenge/";

    static QualifiedName PRIMITIVE_ALIGN_WARP = pFactory.newQualifiedName(PRIM_NS, "align_warp",null);
    static URI PRIMITIVE_RESLICE = URI
	    .create("http://openprovenance.org/primitives#reslice");
    static URI PRIMITIVE_SOFTMEAN = URI
	    .create("http://openprovenance.org/primitives#softmean");
    static URI PRIMITIVE_CONVERT = URI
	    .create("http://openprovenance.org/primitives#convert");
    static URI PRIMITIVE_SLICER = URI
	    .create("http://openprovenance.org/primitives#slicer");


    public QualifiedName q(String n) {
	return new org.openprovenance.prov.xml.QualifiedName(EX_NS, n, EX_PREFIX);
    }

    
    public Entity newFile(ProvFactory pFactory, String id, String label,
	                  String file, String location) {

	org.openprovenance.prov.model.Entity a = pFactory.newEntity(q(id), label);
	pFactory.addType(a, URI
	        .create("http://openprovenance.org/primitives#File"));

	addUrl(a, location + file);

	return a;
    }

    public Entity newParameter(ProvFactory pFactory, String id, String label,
	                       String value) {

	Entity a = pFactory.newEntity(q(id), label);
	pFactory.addType(a, URI.create("http://openprovenance.org/primitives#String"));

	addValue(a, value);

	return a;
    }

    static Entity globalA1 = null;

    public Document makePC1GraphAndSpecialization(ProvFactory pFactory) {
	Document graph = pFactory.newDocument();

	String bName = "b123"; // needs to be generated

	NamedBundle bun = makePC1FullGraph(pFactory, bName);
	graph.getStatementOrBundle().add(bun);
	
	
	bun.setNamespace(Namespace.gatherNamespaces(bun));

	Hashtable<String, String> namespaces = new Hashtable<String, String>();
	// currently, no prefix used, all qnames map to PC1_NS
	namespaces.put("_", EX_NS); // new default namespace
	namespaces.put("xsd", NamespacePrefixMapper.XSD_NS);
	namespaces.put(EX_PREFIX, EX_NS);
	namespaces.put(DOT_PREFIX, DOT_NS);

	Entity bunEntity = pFactory.newEntity(bun.getId());
	Entity a = pFactory.newEntity(new org.openprovenance.prov.xml.QualifiedName(EX_NS, globalA1.getId().getLocalPart(),null));
	MentionOf ctx = pFactory.newMentionOf(a.getId(), globalA1.getId(), bunEntity.getId());
	a.getOther().add(pFactory.newOther(DOT_NS, DOT_PREFIX, "color", "blue", name.XSD_STRING));

	graph.getStatementOrBundle().add(bunEntity);
	graph.getStatementOrBundle().add(a);
	graph.getStatementOrBundle().add(ctx);
	
	
	graph.setNamespace(Namespace.gatherNamespaces(graph));
	graph.getNamespace().extendWith(bun.getNamespace());
	
	return graph;
    }

    public NamedBundle makePC1FullGraph(ProvFactory pFactory, String bName) {
	if (urlFlag) {
	    return makePC1FullGraph(pFactory, URL_LOCATION, URL_LOCATION, bName);
	} else {
	    return makePC1FullGraph(pFactory, FILE_LOCATION, "./", bName);
	}
    }
    
    public void addValue(HasOther p1, String val) {
	p1.getOther().add(pFactory.newOther(PC1_NS, "value", PC1_PREFIX, val, 
	                                     name.XSD_STRING));
    }

    public void addUrl(HasOther p1, String val) {
	p1.getOther().add(pFactory.newOther(PC1_NS, "url", PC1_PREFIX, val, 
	                                     name.XSD_STRING));
    }

    
    public Used newUsed(Activity activity, String role, Entity entity){
	return newUsed(activity.getId(),role,entity.getId());
    }
    
    public Used newUsed(QualifiedName activity, String role, QualifiedName entity){
	Used u1 = pFactory.newUsed(activity, entity);
	u1.getRole().add(pFactory.newRole(role,name.XSD_STRING));
	return u1;
	
    }
    
    public WasDerivedFrom newWasDerivedFrom(Entity entity2, Entity entity1){
	WasDerivedFrom wdf=pFactory.newWasDerivedFrom(null, entity2.getId(), entity1.getId());
	return wdf;
    }
    

    public NamedBundle makePC1FullGraph(ProvFactory pFactory,
	                                String inputLocation,
	                                String outputLocation, String bName) {

	//Activity p0 = pFactory.newActivity("p0", "PC1Full Workflow");

	Activity p1 = pFactory.newActivity(q("p1"), "align_warp 1");
	List<Type> o = p1.getType();

	o.add(pFactory.newType(PRIMITIVE_ALIGN_WARP,name.XSD_ANY_URI));

	pFactory.addType(p1, PRIMITIVE_ALIGN_WARP, name.XSD_ANY_URI);
	pFactory.addType(p1, 10, name.XSD_INT);
	pFactory.addType(p1, -10, name.XSD_INT);
	pFactory.addType(p1, -10.55, name.XSD_FLOAT);
	pFactory.addType(p1, "abc", name.XSD_STRING);
	pFactory.addType(p1, true, name.XSD_BOOLEAN);
	pFactory.addType(p1, URI.create("http://www.example.com/hi"));

	Activity p2 = pFactory.newActivity(q("p2"), "align_warp 2");
	pFactory.addType(p2, PRIMITIVE_ALIGN_WARP, name.XSD_ANY_URI);

	Activity p3 = pFactory.newActivity(q("p3"), "align_warp 3");
	pFactory.addType(p3, PRIMITIVE_ALIGN_WARP, name.XSD_ANY_URI);

	Activity p4 = pFactory.newActivity(q("p4"), "align_warp 4");

	pFactory.addType(p4, PRIMITIVE_ALIGN_WARP, name.XSD_ANY_URI);

	Activity p5 = pFactory.newActivity(q("p5"), "Reslice 1");
	pFactory.addType(p5, PRIMITIVE_RESLICE, name.XSD_ANY_URI);

	Activity p6 = pFactory.newActivity(q("p6"), "Reslice 2");
	pFactory.addType(p6, PRIMITIVE_RESLICE);

	Activity p7 = pFactory.newActivity(q("p7"), "Reslice 3");
	pFactory.addType(p7, PRIMITIVE_RESLICE);

	Activity p8 = pFactory.newActivity(q("p8"), "Reslice 4");
	pFactory.addType(p8, PRIMITIVE_RESLICE);

	Activity p9 = pFactory.newActivity(q("p9"), "Softmean");
	pFactory.addType(p9, PRIMITIVE_SOFTMEAN);

	Activity p10 = pFactory.newActivity(q("p10"), "Slicer 1");

	pFactory.addType(p10, PRIMITIVE_SLICER);

	Activity p11 = pFactory.newActivity(q("p11"), "Slicer 2");
	pFactory.addType(p11, PRIMITIVE_SLICER);

	Activity p12 = pFactory.newActivity(q("p12"), "Slicer 3");
	pFactory.addType(p12, PRIMITIVE_SLICER);

	Activity p13 = pFactory.newActivity(q("p13"), "Convert 1");

	pFactory.addType(p13, PRIMITIVE_CONVERT);

	Activity p14 = pFactory.newActivity(q("p14"), "Convert 2");

	pFactory.addType(p14, PRIMITIVE_CONVERT);

	Activity p15 = pFactory.newActivity(q("p15"), "Convert 3");

	pFactory.addType(p15, PRIMITIVE_CONVERT);

	Agent ag1 = pFactory.newAgent(q("ag1"), "John Doe");

	Entity a1 = newFile(pFactory, "a1", "Reference Image", "reference.img",
	                    inputLocation);

	globalA1 = a1;

	Entity a2 = newFile(pFactory, "a2", "Reference Header",
	                    "reference.hdr", inputLocation);

	Entity a3 = newFile(pFactory, "a3", "Anatomy I1", "anatomy1.img",
	                    inputLocation);

	Entity a4 = newFile(pFactory, "a4", "Anatomy H1", "anatomy1.hdr",
	                    inputLocation);

	Entity a5 = newFile(pFactory, "a5", "Anatomy I2", "anatomy2.img",
	                    inputLocation);

	Entity a6 = newFile(pFactory, "a6", "Anatomy H2", "anatomy2.hdr",
	                    inputLocation);

	Entity a7 = newFile(pFactory, "a7", "Anatomy I3", "anatomy3.img",
	                    inputLocation);

	Entity a8 = newFile(pFactory, "a8", "Anatomy H3", "anatomy3.hdr",
	                    inputLocation);

	Entity a9 = newFile(pFactory, "a9", "Anatomy I4", "anatomy4.img",
	                    inputLocation);

	Entity a10 = newFile(pFactory, "a10", "Anatomy H4", "anatomy4.hdr",
	                     inputLocation);

	Entity a11 = newFile(pFactory, "a11", "Warp Params1", "warp1.warp",
	                     outputLocation);

	Entity a12 = newFile(pFactory, "a12", "Warp Params2", "warp2.warp",
	                     outputLocation);

	Entity a13 = newFile(pFactory, "a13", "Warp Params3", "warp3.warp",
	                     outputLocation);

	Entity a14 = newFile(pFactory, "a14", "Warp Params4", "warp4.warp",
	                     outputLocation);

	Entity a15 = newFile(pFactory, "a15", "Resliced I1", "resliced1.img",
	                     outputLocation);

	Entity a16 = newFile(pFactory, "a16", "Resliced H1", "resliced1.hdr",
	                     outputLocation);
	Entity a17 = newFile(pFactory, "a17", "Resliced I2", "resliced2.img",
	                     outputLocation);
	Entity a18 = newFile(pFactory, "a18", "Resliced H2", "resliced2.hdr",
	                     outputLocation);
	Entity a19 = newFile(pFactory, "a19", "Resliced I3", "resliced3.img",
	                     outputLocation);
	Entity a20 = newFile(pFactory, "a20", "Resliced H3", "resliced3.hdr",
	                     outputLocation);
	Entity a21 = newFile(pFactory, "a21", "Resliced I4", "resliced4.img",
	                     outputLocation);
	Entity a22 = newFile(pFactory, "a22", "Resliced H4", "resliced4.hdr",
	                     outputLocation);

	Entity a23 = newFile(pFactory, "a23", "Atlas Image", "atlas.img",
	                     outputLocation);
	Entity a24 = newFile(pFactory, "a24", "Atlas Header", "atlas.hdr",
	                     outputLocation);

	Entity a25 = newFile(pFactory, "a25", "Atlas X Slice", "atlas-x.pgm",
	                     outputLocation);
	Entity a25p = newParameter(pFactory, "a25p", "slicer param 1", "-x .5");

	Entity a26 = newFile(pFactory, "a26", "Atlas Y Slice", "atlas-y.pgm",
	                     outputLocation);
	Entity a26p = newParameter(pFactory, "a26p", "slicer param 2", "-y .5");
	Entity a27 = newFile(pFactory, "a27", "Atlas Z Slice", "atlas-z.pgm",
	                     outputLocation);
	Entity a27p = newParameter(pFactory, "a27p", "slicer param 3", "-z .5");
	Entity a28 = newFile(pFactory, "a28", "Atlas X Graphic", "atlas-x.gif",
	                     outputLocation);
	Entity a29 = newFile(pFactory, "a29", "Atlas Y Graphic", "atlas-y.gif",
	                     outputLocation);
	Entity a30 = newFile(pFactory, "a30", "Atlas Z Graphic", "atlas-z.gif",
	                     outputLocation);

	Used u1 = newUsed(p1, "img", a3);
	Used u2 = newUsed(p1, "hdr", a4);
	Used u3 = newUsed(p1, "imgRef", a1);u3.setId(q("u3"));
	Used u4 = newUsed(p1, "hdrRef", a2);
	Used u5 = newUsed(p2, "img", a5);
	Used u6 = newUsed(p2, "hdr", a6);
	Used u7 = newUsed(p2, "imgRef", a1);
	Used u8 = newUsed(p2, "hdrRef", a2);
	Used u9 = newUsed(p3, "img", a7);
	Used u10 = newUsed(p3, "hdr", a8);
	Used u11 = newUsed(p3, "imgRef", a1);
	Used u12 = newUsed(p3, "hdrRef", a2);
	Used u13 = newUsed(p4, "img", a9);
	Used u14 = newUsed(p4, "hdr", a10);
	Used u15 = newUsed(p4, "imgRef", a1);
	Used u16 = newUsed(p4, "hdrRef", a2);

	Used u17 = newUsed(p5, "in", a11);
	Used u18 = newUsed(p6, "in", a12);
	Used u19 = newUsed(p7, "in", a13);
	Used u20 = newUsed(p8, "in", a14);

	Used u21 = newUsed(p9, "i1", a15);
	Used u22 = newUsed(p9, "h1", a16);
	Used u23 = newUsed(p9, "i2", a17);
	Used u24 = newUsed(p9, "h2", a18);
	Used u25 = newUsed(p9, "i3", a19);
	Used u26 = newUsed(p9, "h3", a20);
	Used u27 = newUsed(p9, "i4", a21);
	Used u28 = newUsed(p9, "h4", a22);

	Used u29 = newUsed(p10, "img", a23);
	Used u30 = newUsed(p10, "hdr", a24);
	Used u30p = newUsed(p10, "param", a25p);
	Used u31 = newUsed(p11, "img", a23);
	Used u32 = newUsed(p11, "hdr", a24);
	Used u32p = newUsed(p11, "param", a26p);
	Used u33 = newUsed(p12, "img", a23);
	Used u34 = newUsed(p12, "hdr", a24);
	Used u34p = newUsed(p12, "param", a27p);

	Used u35 = newUsed(p13, "in", a25);
	Used u36 = newUsed(p14, "in", a26);
	Used u37 = newUsed(p15, "in", a27);

	WasGeneratedBy wg1 = pFactory.newWasGeneratedBy(a11, "out", p1);wg1.setId(q("wgb1"));
	WasGeneratedBy wg2 = pFactory.newWasGeneratedBy(a12, "out", p2);
	WasGeneratedBy wg3 = pFactory.newWasGeneratedBy(a13, "out", p3);
	WasGeneratedBy wg4 = pFactory.newWasGeneratedBy(a14, "out", p4);

	WasGeneratedBy wg5 = pFactory.newWasGeneratedBy(a15, "img", p5);
	WasGeneratedBy wg6 = pFactory.newWasGeneratedBy(a16, "hdr", p5);
	WasGeneratedBy wg7 = pFactory.newWasGeneratedBy(a17, "img", p6);
	WasGeneratedBy wg8 = pFactory.newWasGeneratedBy(a18, "hdr", p6);
	WasGeneratedBy wg9 = pFactory.newWasGeneratedBy(a19, "img", p7);
	WasGeneratedBy wg10 = pFactory.newWasGeneratedBy(a20, "hdr", p7);
	WasGeneratedBy wg11 = pFactory.newWasGeneratedBy(a21, "img", p8);
	WasGeneratedBy wg12 = pFactory.newWasGeneratedBy(a22, "hdr", p8);

	WasGeneratedBy wg13 = pFactory.newWasGeneratedBy(a23, "img", p9);
	WasGeneratedBy wg14 = pFactory.newWasGeneratedBy(a24, "hdr", p9);

	WasGeneratedBy wg15 = pFactory.newWasGeneratedBy(a25, "out", p10);
	WasGeneratedBy wg16 = pFactory.newWasGeneratedBy(a26, "out", p11);
	WasGeneratedBy wg17 = pFactory.newWasGeneratedBy(a27, "out", p12);

	WasGeneratedBy wg18 = pFactory.newWasGeneratedBy(a28, "out", p13);
	WasGeneratedBy wg19 = pFactory.newWasGeneratedBy(a29, "out", p14);
	WasGeneratedBy wg20 = pFactory.newWasGeneratedBy(a30, "out", p15);
	wg18.setTime(pFactory.newTimeNow());
	wg19.setTime(pFactory.newTimeNow());
	wg20.setTime(pFactory.newTimeNow());

	//WasDerivedFrom wd1 = pFactory.newWasDerivedFrom(a11, a1, p1, wg1, u3);
	WasDerivedFrom wd1 = newWasDerivedFrom(a11,a1);
	wd1.setActivity(p1.getId());
	wd1.setGeneration(wg1.getId());
	wd1.setUsage(u3.getId());
	WasDerivedFrom wd2 = newWasDerivedFrom(a11, a2);
	WasDerivedFrom wd3 = newWasDerivedFrom(a11, a3);
	WasDerivedFrom wd4 = newWasDerivedFrom(a11, a4);
	WasDerivedFrom wd5 = newWasDerivedFrom(a12, a1);
	WasDerivedFrom wd6 = newWasDerivedFrom(a12, a2);
	WasDerivedFrom wd7 = newWasDerivedFrom(a12, a5);
	WasDerivedFrom wd8 = newWasDerivedFrom(a12, a6);
	WasDerivedFrom wd9 = newWasDerivedFrom(a13, a1);
	WasDerivedFrom wd10 = newWasDerivedFrom(a13, a2);
	WasDerivedFrom wd11 = newWasDerivedFrom(a13, a7);
	WasDerivedFrom wd12 = newWasDerivedFrom(a13, a8);
	WasDerivedFrom wd13 = newWasDerivedFrom(a14, a1);
	WasDerivedFrom wd14 = newWasDerivedFrom(a14, a2);
	WasDerivedFrom wd15 = newWasDerivedFrom(a14, a9);
	WasDerivedFrom wd16 = newWasDerivedFrom(a14, a10);

	WasDerivedFrom wd17 = newWasDerivedFrom(a15, a11);
	WasDerivedFrom wd18 = newWasDerivedFrom(a16, a11);
	WasDerivedFrom wd19 = newWasDerivedFrom(a17, a12);
	WasDerivedFrom wd20 = newWasDerivedFrom(a18, a12);
	WasDerivedFrom wd21 = newWasDerivedFrom(a19, a13);
	WasDerivedFrom wd22 = newWasDerivedFrom(a20, a13);
	WasDerivedFrom wd23 = newWasDerivedFrom(a21, a14);
	WasDerivedFrom wd24 = newWasDerivedFrom(a22, a14);

	WasDerivedFrom wd25 = newWasDerivedFrom(a23, a15);
	WasDerivedFrom wd26 = newWasDerivedFrom(a23, a16);
	WasDerivedFrom wd27 = newWasDerivedFrom(a23, a17);
	WasDerivedFrom wd28 = newWasDerivedFrom(a23, a18);
	WasDerivedFrom wd29 = newWasDerivedFrom(a23, a19);
	WasDerivedFrom wd30 = newWasDerivedFrom(a23, a20);
	WasDerivedFrom wd31 = newWasDerivedFrom(a23, a21);
	WasDerivedFrom wd32 = newWasDerivedFrom(a23, a22);

	WasDerivedFrom wd33 = newWasDerivedFrom(a24, a15);
	WasDerivedFrom wd34 = newWasDerivedFrom(a24, a16);
	WasDerivedFrom wd35 = newWasDerivedFrom(a24, a17);
	WasDerivedFrom wd36 = newWasDerivedFrom(a24, a18);
	WasDerivedFrom wd37 = newWasDerivedFrom(a24, a19);
	WasDerivedFrom wd38 = newWasDerivedFrom(a24, a20);
	WasDerivedFrom wd39 = newWasDerivedFrom(a24, a21);
	WasDerivedFrom wd40 = newWasDerivedFrom(a24, a22);

	WasDerivedFrom wd41 = newWasDerivedFrom(a25, a23);
	WasDerivedFrom wd42 = newWasDerivedFrom(a25, a24);
	WasDerivedFrom wd43 = newWasDerivedFrom(a26, a23);
	WasDerivedFrom wd44 = newWasDerivedFrom(a26, a24);
	WasDerivedFrom wd45 = newWasDerivedFrom(a27, a23);
	WasDerivedFrom wd46 = newWasDerivedFrom(a27, a24);

	WasDerivedFrom wd47 = newWasDerivedFrom(a28, a25);
	WasDerivedFrom wd48 = newWasDerivedFrom(a29, a26);
	WasDerivedFrom wd49 = newWasDerivedFrom(a30, a27);

	WasAssociatedWith waw1 = pFactory.newWasAssociatedWith(q("waw1"), p1.getId(), ag1.getId());

	NamedBundle graph = pFactory.newNamedBundle(q(bName),
	                                            Arrays.asList( p1, p2,
	                                                            p3, p4, p5,
	                                                            p6, p7, p8,
	                                                            p9, p10,
	                                                            p11, p12,
	                                                            p13, p14,
	                                                            p15 ),
	                                            Arrays.asList( a1, a2, a5,
	                                                          a6, a3, a4,
	                                                          a7, a8, a9,
	                                                          a10, a11,
	                                                          a12, a13,
	                                                          a14, a15,
	                                                          a16, a17,
	                                                          a18, a19,
	                                                          a20, a21,
	                                                          a22, a23,
	                                                          a24, a25,
	                                                          a25p, a26,
	                                                          a26p, a27,
	                                                          a27p, a28,
	                                                          a29, a30 ),
	                                            Arrays.asList( ag1 ),
	                                            Arrays.asList(new Statement[] { u1, u2, u3,
	                                                          u4, u5, u6,
	                                                          u7, u8, u9,
	                                                          u10, u11,
	                                                          u12, u13,
	                                                          u14, u15,
	                                                          u16, u17,
	                                                          u18, u19,
	                                                          u20, u21,
	                                                          u22, u23,
	                                                          u24, u25,
	                                                          u26, u27,
	                                                          u28, u29,
	                                                          u30, u31,
	                                                          u32, u33,
	                                                          u34, u30p,
	                                                          u32p, u34p,
	                                                          u35, u36,
	                                                          u37, wg1,
	                                                          wg2, wg3,
	                                                          wg4, wg5,
	                                                          wg6, wg7,
	                                                          wg8, wg9,
	                                                          wg10, wg11,
	                                                          wg12, wg13,
	                                                          wg14, wg15,
	                                                          wg16, wg17,
	                                                          wg18, wg19,
	                                                          wg20, wd1,
	                                                          wd2, wd3,
	                                                          wd4, wd5,
	                                                          wd6, wd7,
	                                                          wd8, wd9,
	                                                          wd10, wd11,
	                                                          wd12, wd13,
	                                                          wd14, wd15,
	                                                          wd16, wd17,
	                                                          wd18, wd19,
	                                                          wd20, wd21,
	                                                          wd22, wd23,
	                                                          wd24, wd25,
	                                                          wd26, wd27,
	                                                          wd28, wd29,
	                                                          wd30, wd31,
	                                                          wd32, wd33,
	                                                          wd34, wd35,
	                                                          wd36, wd37,
	                                                          wd38, wd39,
	                                                          wd40, wd41,
	                                                          wd42, wd43,
	                                                          wd44, wd45,
	                                                          wd46, wd47,
	                                                          wd48, wd49,
	                                                          waw1 }));
	
	graph.setNamespace(Namespace.gatherNamespaces(graph));


	return graph;
    }

    public void NOtestCopyPC1SpecFull() throws java.io.FileNotFoundException,
	    java.io.IOException {
	ProvFactory pFactory = new ProvFactory();

	Document c = pFactory.newDocument(graph1);

	assertTrue("self graph1 differ", graph1.equals(graph1));

	assertTrue("self c differ", c.equals(c));

	assertTrue("graph1 c differ", graph1.equals(c));

    }

    public void NOtestReadXMLGraph2() throws javax.xml.bind.JAXBException {

	ProvDeserialiser deserial = ProvDeserialiser
	        .getThreadProvDeserialiser();

	Document c = deserial.deserialiseDocument(new File("target/pc1-spec.xml"));
	graph2 = c;

	graph2.setNamespace(graph1.getNamespace());
	ProvSerialiser serial = ProvSerialiser.getThreadProvSerialiser();
        Namespace.withThreadNamespace(graph2.getNamespace());

	serial.serialiseDocument(new File("target/pc1-spec2.xml"), graph2, true);

	// System.out.println("a0" + graph1.getRecords().getActivity().get(0));
	// System.out.println("a0" + graph2.getRecords().getActivity().get(0));

	assertTrue("graph1 a* and graph2 a* differ", util.getActivity(graph1)
	        .equals(util.getActivity(graph2)));

	// failing because of comparison of Elements <pc1:url>...</pc1:url>
	assertFalse("graph1 e* and graph2 e* differ", util.getEntity(graph1)
	        .equals(util.getEntity(graph2)));

	assertFalse("graph1 and graph2 differ", graph1.equals(graph2));

	Document c2 = deserial.deserialiseDocument(new File("target/pc1-spec.xml"));
	c2.setNamespace(graph1.getNamespace());

	assertFalse("c e* and c2 e* differ",
	            util.getEntity(c)
	                    .equals(util.getEntity(c2)));
	assertFalse("c and c2 differ", c.equals(c2));

    }

    public void NOtestSchemaValidateXML2() throws javax.xml.bind.JAXBException,
	    org.xml.sax.SAXException, java.io.IOException {

	ProvDeserialiser deserial = ProvDeserialiser
	        .getThreadProvDeserialiser();

	String[] schemaFiles = new String[1];
	schemaFiles[0] = "src/test/resources/pc1.xsd";
	deserial.validateDocument(schemaFiles, new File("target/pc1-spec.xml"));

    }

    public void NOtestSchemaFailValidateXML2() {

	ProvDeserialiser deserial = ProvDeserialiser
	        .getThreadProvDeserialiser();

	String[] schemaFiles = new String[1];
	schemaFiles[0] = "src/test/resources/pc1.xsd";

	try {
	    deserial.validateDocument(schemaFiles,
		                    new File("target/pc1-spec.xml"));
	} catch (Exception e) {
	    e.printStackTrace();
	    assertTrue(true);
	    return;
	}
	assertTrue(false);

    }

}
