package org.openprovenance.prov.xml;
import java.io.File;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple Provenance Challenge 1 like workflow.
 */
public class PC1FullTest 
    extends TestCase
{
    public static ProvFactory pFactory=new ProvFactory();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PC1FullTest( String testName )
    {
        super( testName );
    }

    public boolean urlFlag=true;

    /**
     * @return the suite of tests being tested
     */


    static Container graph1;


    public void testPC1Full() throws JAXBException
    {
        Container graph=makePC1FullGraph(pFactory);

        ProvSerialiser serial=ProvSerialiser.getThreadProvSerialiser();
        serial.serialiseContainer(new File("target/pc1-full.xml"),graph,true);

        
        //System.out.println(sw);

        graph1=graph;
        System.out.println("PC1Full Test asserting True");
        assertTrue( true );


    }

    static String PATH_PROPERTY="http://openprovenance.org/primitives#path";
    static String URL_PROPERTY="http://openprovenance.org/primitives#url";
    static String PRIMITIVE_PROPERTY="http://openprovenance.org/primitives#primitive";
    static String FILE_LOCATION="/shomewhere/pc1/";
    static String URL_LOCATION="http://www.ipaw.info/challenge/";

    static String PRIMITIVE_ALIGN_WARP="http://openprovenance.org/primitives#align_warp";
    static String PRIMITIVE_RESLICE="http://openprovenance.org/primitives#reslice";
    static String PRIMITIVE_SOFTMEAN="http://openprovenance.org/primitives#softmean";
    static String PRIMITIVE_CONVERT="http://openprovenance.org/primitives#convert";
    static String PRIMITIVE_SLICER="http://openprovenance.org/primitives#slicer";


    public Entity newFile(ProvFactory pFactory,
                            String id,
                            Collection<Account> accounts,
                            String label,
                            String file,
                            String location) {
                            
        Entity a=pFactory.newEntity(id,
                                          accounts,
                                          label);
        pFactory.addAnnotation(a,
                               pFactory.newType("http://openprovenance.org/primitives#File"));
        pFactory.addAnnotation(a,
                               pFactory.newEmbeddedAnnotation("an1_" + id,
                                                              (urlFlag) ? URL_PROPERTY : PATH_PROPERTY,
                                                              location + file,
                                                              null));
        return a;
    }


    public Entity newParameter(ProvFactory pFactory,
                                 String id,
                                 Collection<Account> accounts,
                                 String label,
                                 String value) {
                            
        Entity a=pFactory.newEntity(id,
                                        accounts,
                                        label);
        pFactory.addAnnotation(a,
                               pFactory.newType("http://openprovenance.org/primitives#String"));
        pFactory.addValue(a,value,"mime:application/text");

        return a;
    }



    public Container makePC1FullGraph(ProvFactory pFactory) {
        if (urlFlag) {
            return makePC1FullGraph(pFactory,URL_LOCATION,URL_LOCATION);
        } else {
            return makePC1FullGraph(pFactory,FILE_LOCATION,"./");
        }
    }

    public Container makePC1FullGraph(ProvFactory pFactory, String inputLocation, String outputLocation)
    {

        Collection<Account> black=Collections.singleton(pFactory.newAccount("black"));
        

        Activity p0=pFactory.newActivity("p0",
                                       black,
                                       "PC1Full Workflow");

        Activity p1=pFactory.newActivity("p1",
                                       black,
                                       "align_warp 1");
        p1.setAttributes(pFactory.newAttributes(PRIMITIVE_PROPERTY,
                                                PRIMITIVE_ALIGN_WARP));

        Activity p2=pFactory.newActivity("p2",
                                       black,
                                       "align_warp 2");
        pFactory.addAnnotation(p2,
                               pFactory.newEmbeddedAnnotation("an1_p2",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_ALIGN_WARP,
                                                              null));

        Activity p3=pFactory.newActivity("p3",
                                       black,
                                       "align_warp 3");
        pFactory.addAnnotation(p3,
                               pFactory.newEmbeddedAnnotation("an1_p3",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_ALIGN_WARP,
                                                              null));
        Activity p4=pFactory.newActivity("p4",
                                       black,
                                       "align_warp 4");
        pFactory.addAnnotation(p4,
                               pFactory.newEmbeddedAnnotation("an1_p4",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_ALIGN_WARP,
                                                              null));

        Activity p5=pFactory.newActivity("p5",
                                       black,
                                       "Reslice 1");
        pFactory.addAnnotation(p5,
                               pFactory.newEmbeddedAnnotation("an1_p5",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_RESLICE,
                                                              null));

        Activity p6=pFactory.newActivity("p6",
                                       black,
                                       "Reslice 2");
        pFactory.addAnnotation(p6,
                               pFactory.newEmbeddedAnnotation("an1_p6",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_RESLICE,
                                                              null));
        Activity p7=pFactory.newActivity("p7",
                                       black,
                                       "Reslice 3");
        pFactory.addAnnotation(p7,
                               pFactory.newEmbeddedAnnotation("an1_p7",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_RESLICE,
                                                              null));
        Activity p8=pFactory.newActivity("p8",
                                       black,
                                       "Reslice 4");
        pFactory.addAnnotation(p8,
                               pFactory.newEmbeddedAnnotation("an1_p8",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_RESLICE,
                                                              null));
        Activity p9=pFactory.newActivity("p9",
                                       black,
                                       "Softmean");
        pFactory.addAnnotation(p9,
                               pFactory.newEmbeddedAnnotation("an1_p9",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_SOFTMEAN,
                                                              null));
        Activity p10=pFactory.newActivity("p10",
                                        black,
                                        "Slicer 1");
        pFactory.addAnnotation(p10,
                               pFactory.newEmbeddedAnnotation("an1_p10",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_SLICER,
                                                              null));

        Activity p11=pFactory.newActivity("p11",
                                        black,
                                        "Slicer 2");
        pFactory.addAnnotation(p11,
                               pFactory.newEmbeddedAnnotation("an1_p11",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_SLICER,
                                                              null));

        Activity p12=pFactory.newActivity("p12",
                                        black,
                                        "Slicer 3");
        pFactory.addAnnotation(p12,
                               pFactory.newEmbeddedAnnotation("an1_p12",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_SLICER,
                                                              null));
        Activity p13=pFactory.newActivity("p13",
                                        black,
                                        "Convert 1");
        pFactory.addAnnotation(p13,
                               pFactory.newEmbeddedAnnotation("an1_p13",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_CONVERT,
                                                              null));

        Activity p14=pFactory.newActivity("p14",
                                        black,
                                        "Convert 2");
        pFactory.addAnnotation(p14,
                               pFactory.newEmbeddedAnnotation("an1_p14",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_CONVERT,
                                                              null));

        Activity p15=pFactory.newActivity("p15",
                                        black,
                                        "Convert 3");
        pFactory.addAnnotation(p15,
                               pFactory.newEmbeddedAnnotation("an1_p15",
                                                              PRIMITIVE_PROPERTY,
                                                              PRIMITIVE_CONVERT,
                                                              null));

        Agent ag1=pFactory.newAgent("ag1",
                                    black,
                                    "John Doe");


        Entity a1=newFile(pFactory,
                             "a1",
                             black,
                             "Reference Image",
                             "reference.img",
                             inputLocation);

        Entity a2=newFile(pFactory,
                             "a2",
                             black,
                             "Reference Header",
                             "reference.hdr",
                             inputLocation);

        Entity a3=newFile(pFactory,
                             "a3",
                             black,
                             "Anatomy I1",
                             "anatomy1.img",
                             inputLocation);

        Entity a4=newFile(pFactory,
                             "a4",
                             black,
                             "Anatomy H1",
                             "anatomy1.hdr",
                             inputLocation);

        Entity a5=newFile(pFactory,
                             "a5",
                             black,
                             "Anatomy I2",
                             "anatomy2.img",
                             inputLocation);

        Entity a6=newFile(pFactory,
                             "a6",
                             black,
                             "Anatomy H2",
                             "anatomy2.hdr",
                             inputLocation);

        Entity a7=newFile(pFactory,
                             "a7",
                             black,
                             "Anatomy I3",
                             "anatomy3.img",
                             inputLocation);

        Entity a8=newFile(pFactory,
                             "a8",
                             black,
                             "Anatomy H3",
                             "anatomy3.hdr",
                             inputLocation);

        Entity a9=newFile(pFactory,
                             "a9",
                             black,
                             "Anatomy I4",
                             "anatomy4.img",
                             inputLocation);

        Entity a10=newFile(pFactory,
                              "a10",
                              black,
                              "Anatomy H4",
                              "anatomy4.hdr",
                              inputLocation);

        Entity a11=newFile(pFactory,
                             "a11",
                             black,
                             "Warp Params1",
                             "warp1.warp",
                             outputLocation);

        Entity a12=newFile(pFactory,
                             "a12",
                             black,
                             "Warp Params2",
                             "warp2.warp",
                             outputLocation);

        Entity a13=newFile(pFactory,
                             "a13",
                             black,
                             "Warp Params3",
                             "warp3.warp",
                             outputLocation);

        Entity a14=newFile(pFactory,
                             "a14",
                             black,
                             "Warp Params4",
                             "warp4.warp",
                             outputLocation);

        Entity a15=newFile(pFactory,
                             "a15",
                             black,
                             "Resliced I1",
                             "resliced1.img",
                             outputLocation);

        Entity a16=newFile(pFactory,
                             "a16",
                             black,
                             "Resliced H1",
                             "resliced1.hdr",
                             outputLocation);
        Entity a17=newFile(pFactory,
                             "a17",
                             black,
                             "Resliced I2",
                             "resliced2.img",
                             outputLocation);
        Entity a18=newFile(pFactory,
                             "a18",
                             black,
                             "Resliced H2",
                             "resliced2.hdr",
                             outputLocation);
        Entity a19=newFile(pFactory,
                             "a19",
                             black,
                             "Resliced I3",
                             "resliced3.img",
                             outputLocation);
        Entity a20=newFile(pFactory,
                             "a20",
                             black,
                             "Resliced H3",
                             "resliced3.hdr",
                             outputLocation);
        Entity a21=newFile(pFactory,
                             "a21",
                             black,
                             "Resliced I4",
                             "resliced4.img",
                             outputLocation);
        Entity a22=newFile(pFactory,
                             "a22",
                             black,
                             "Resliced H4",
                             "resliced4.hdr",
                             outputLocation);


        Entity a23=newFile(pFactory,
                             "a23",
                             black,
                             "Atlas Image",
                             "atlas.img",
                             outputLocation);
        Entity a24=newFile(pFactory,
                             "a24",
                             black,
                             "Atlas Header",
                             "atlas.hdr",
                             outputLocation);


        Entity a25=newFile(pFactory,
                             "a25",
                             black,
                             "Atlas X Slice",
                             "atlas-x.pgm",
                             outputLocation);
        Entity a25p=newParameter(pFactory,
                                   "a25p",
                                   black,
                                   "slicer param 1",
                                   "-x .5");

        Entity a26=newFile(pFactory,
                             "a26",
                             black,
                             "Atlas Y Slice",
                             "atlas-y.pgm",
                             outputLocation);
        Entity a26p=newParameter(pFactory,
                                   "a26p",
                                   black,
                                   "slicer param 2",
                                   "-y .5");
        Entity a27=newFile(pFactory,
                             "a27",
                             black,
                             "Atlas Z Slice",
                             "atlas-z.pgm",
                             outputLocation);
        Entity a27p=newParameter(pFactory,
                                   "a27p",
                                   black,
                                   "slicer param 3",
                                   "-z .5");
        Entity a28=newFile(pFactory,
                             "a28",
                             black,
                             "Atlas X Graphic",
                             "atlas-x.gif",
                             outputLocation);
        Entity a29=newFile(pFactory,
                             "a29",
                             black,
                             "Atlas Y Graphic",
                             "atlas-y.gif",
                             outputLocation);
        Entity a30=newFile(pFactory,
                             "a30",
                             black,
                             "Atlas Z Graphic",
                             "atlas-z.gif",
                             outputLocation);

        Used u1=pFactory.newUsed(p1,pFactory.newRole("img"),a3,black);
        Used u2=pFactory.newUsed(p1,pFactory.newRole("hdr"),a4,black);
        Used u3=pFactory.newUsed(p1,pFactory.newRole("imgRef"),a1,black);
        Used u4=pFactory.newUsed(p1,pFactory.newRole("hdrRef"),a2,black);
        Used u5=pFactory.newUsed(p2,pFactory.newRole("img"),a5,black);
        Used u6=pFactory.newUsed(p2,pFactory.newRole("hdr"),a6,black);
        Used u7=pFactory.newUsed(p2,pFactory.newRole("imgRef"),a1,black);
        Used u8=pFactory.newUsed(p2,pFactory.newRole("hdrRef"),a2,black);
        Used u9=pFactory.newUsed(p3,pFactory.newRole("img"),a7,black);
        Used u10=pFactory.newUsed(p3,pFactory.newRole("hdr"),a8,black);
        Used u11=pFactory.newUsed(p3,pFactory.newRole("imgRef"),a1,black);
        Used u12=pFactory.newUsed(p3,pFactory.newRole("hdrRef"),a2,black);
        Used u13=pFactory.newUsed(p4,pFactory.newRole("img"),a9,black);
        Used u14=pFactory.newUsed(p4,pFactory.newRole("hdr"),a10,black);
        Used u15=pFactory.newUsed(p4,pFactory.newRole("imgRef"),a1,black);
        Used u16=pFactory.newUsed(p4,pFactory.newRole("hdrRef"),a2,black);

        Used u17=pFactory.newUsed(p5,pFactory.newRole("in"),a11,black);
        Used u18=pFactory.newUsed(p6,pFactory.newRole("in"),a12,black);
        Used u19=pFactory.newUsed(p7,pFactory.newRole("in"),a13,black);
        Used u20=pFactory.newUsed(p8,pFactory.newRole("in"),a14,black);

        Used u21=pFactory.newUsed(p9,pFactory.newRole("i1"),a15,black);
        Used u22=pFactory.newUsed(p9,pFactory.newRole("h1"),a16,black);
        Used u23=pFactory.newUsed(p9,pFactory.newRole("i2"),a17,black);
        Used u24=pFactory.newUsed(p9,pFactory.newRole("h2"),a18,black);
        Used u25=pFactory.newUsed(p9,pFactory.newRole("i3"),a19,black);
        Used u26=pFactory.newUsed(p9,pFactory.newRole("h3"),a20,black);
        Used u27=pFactory.newUsed(p9,pFactory.newRole("i4"),a21,black);
        Used u28=pFactory.newUsed(p9,pFactory.newRole("h4"),a22,black);

        Used u29=pFactory.newUsed(p10,pFactory.newRole("img"),a23,black);
        Used u30=pFactory.newUsed(p10,pFactory.newRole("hdr"),a24,black);
        Used u30p=pFactory.newUsed(p10,pFactory.newRole("param"),a25p,black);
        Used u31=pFactory.newUsed(p11,pFactory.newRole("img"),a23,black);
        Used u32=pFactory.newUsed(p11,pFactory.newRole("hdr"),a24,black);
        Used u32p=pFactory.newUsed(p11,pFactory.newRole("param"),a26p,black);
        Used u33=pFactory.newUsed(p12,pFactory.newRole("img"),a23,black);
        Used u34=pFactory.newUsed(p12,pFactory.newRole("hdr"),a24,black);
        Used u34p=pFactory.newUsed(p12,pFactory.newRole("param"),a27p,black);

        Used u35=pFactory.newUsed(p13,pFactory.newRole("in"),a25,black);
        Used u36=pFactory.newUsed(p14,pFactory.newRole("in"),a26,black);
        Used u37=pFactory.newUsed(p15,pFactory.newRole("in"),a27,black);




        WasGeneratedBy wg1=pFactory.newWasGeneratedBy(a11,pFactory.newRole("out"),p1,black);
        WasGeneratedBy wg2=pFactory.newWasGeneratedBy(a12,pFactory.newRole("out"),p2,black);
        WasGeneratedBy wg3=pFactory.newWasGeneratedBy(a13,pFactory.newRole("out"),p3,black);
        WasGeneratedBy wg4=pFactory.newWasGeneratedBy(a14,pFactory.newRole("out"),p4,black);

        WasGeneratedBy wg5=pFactory.newWasGeneratedBy(a15,pFactory.newRole("img"),p5,black);
        WasGeneratedBy wg6=pFactory.newWasGeneratedBy(a16,pFactory.newRole("hdr"),p5,black);
        WasGeneratedBy wg7=pFactory.newWasGeneratedBy(a17,pFactory.newRole("img"),p6,black);
        WasGeneratedBy wg8=pFactory.newWasGeneratedBy(a18,pFactory.newRole("hdr"),p6,black);
        WasGeneratedBy wg9=pFactory.newWasGeneratedBy(a19,pFactory.newRole("img"),p7,black);
        WasGeneratedBy wg10=pFactory.newWasGeneratedBy(a20,pFactory.newRole("hdr"),p7,black);
        WasGeneratedBy wg11=pFactory.newWasGeneratedBy(a21,pFactory.newRole("img"),p8,black);
        WasGeneratedBy wg12=pFactory.newWasGeneratedBy(a22,pFactory.newRole("hdr"),p8,black);

        WasGeneratedBy wg13=pFactory.newWasGeneratedBy(a23,pFactory.newRole("img"),p9,black);
        WasGeneratedBy wg14=pFactory.newWasGeneratedBy(a24,pFactory.newRole("hdr"),p9,black);

        WasGeneratedBy wg15=pFactory.newWasGeneratedBy(a25,pFactory.newRole("out"),p10,black);
        WasGeneratedBy wg16=pFactory.newWasGeneratedBy(a26,pFactory.newRole("out"),p11,black);
        WasGeneratedBy wg17=pFactory.newWasGeneratedBy(a27,pFactory.newRole("out"),p12,black);

        WasGeneratedBy wg18=pFactory.newWasGeneratedBy(a28,pFactory.newRole("out"),p13,black);
        WasGeneratedBy wg19=pFactory.newWasGeneratedBy(a29,pFactory.newRole("out"),p14,black);
        WasGeneratedBy wg20=pFactory.newWasGeneratedBy(a30,pFactory.newRole("out"),p15,black);


        WasDerivedFrom wd1=pFactory.newWasDerivedFrom(a11,a1,black);
        WasDerivedFrom wd2=pFactory.newWasDerivedFrom(a11,a2,black);
        WasDerivedFrom wd3=pFactory.newWasDerivedFrom(a11,a3,black);
        WasDerivedFrom wd4=pFactory.newWasDerivedFrom(a11,a4,black);
        WasDerivedFrom wd5=pFactory.newWasDerivedFrom(a12,a1,black);
        WasDerivedFrom wd6=pFactory.newWasDerivedFrom(a12,a2,black);
        WasDerivedFrom wd7=pFactory.newWasDerivedFrom(a12,a5,black);
        WasDerivedFrom wd8=pFactory.newWasDerivedFrom(a12,a6,black);
        WasDerivedFrom wd9=pFactory.newWasDerivedFrom(a13,a1,black);
        WasDerivedFrom wd10=pFactory.newWasDerivedFrom(a13,a2,black);
        WasDerivedFrom wd11=pFactory.newWasDerivedFrom(a13,a7,black);
        WasDerivedFrom wd12=pFactory.newWasDerivedFrom(a13,a8,black);
        WasDerivedFrom wd13=pFactory.newWasDerivedFrom(a14,a1,black);
        WasDerivedFrom wd14=pFactory.newWasDerivedFrom(a14,a2,black);
        WasDerivedFrom wd15=pFactory.newWasDerivedFrom(a14,a9,black);
        WasDerivedFrom wd16=pFactory.newWasDerivedFrom(a14,a10,black);


        WasDerivedFrom wd17=pFactory.newWasDerivedFrom(a15,a11,black);
        WasDerivedFrom wd18=pFactory.newWasDerivedFrom(a16,a11,black);
        WasDerivedFrom wd19=pFactory.newWasDerivedFrom(a17,a12,black);
        WasDerivedFrom wd20=pFactory.newWasDerivedFrom(a18,a12,black);
        WasDerivedFrom wd21=pFactory.newWasDerivedFrom(a19,a13,black);
        WasDerivedFrom wd22=pFactory.newWasDerivedFrom(a20,a13,black);
        WasDerivedFrom wd23=pFactory.newWasDerivedFrom(a21,a14,black);
        WasDerivedFrom wd24=pFactory.newWasDerivedFrom(a22,a14,black);


        WasDerivedFrom wd25=pFactory.newWasDerivedFrom(a23,a15,black);
        WasDerivedFrom wd26=pFactory.newWasDerivedFrom(a23,a16,black);
        WasDerivedFrom wd27=pFactory.newWasDerivedFrom(a23,a17,black);
        WasDerivedFrom wd28=pFactory.newWasDerivedFrom(a23,a18,black);
        WasDerivedFrom wd29=pFactory.newWasDerivedFrom(a23,a19,black);
        WasDerivedFrom wd30=pFactory.newWasDerivedFrom(a23,a20,black);
        WasDerivedFrom wd31=pFactory.newWasDerivedFrom(a23,a21,black);
        WasDerivedFrom wd32=pFactory.newWasDerivedFrom(a23,a22,black);

        WasDerivedFrom wd33=pFactory.newWasDerivedFrom(a24,a15,black);
        WasDerivedFrom wd34=pFactory.newWasDerivedFrom(a24,a16,black);
        WasDerivedFrom wd35=pFactory.newWasDerivedFrom(a24,a17,black);
        WasDerivedFrom wd36=pFactory.newWasDerivedFrom(a24,a18,black);
        WasDerivedFrom wd37=pFactory.newWasDerivedFrom(a24,a19,black);
        WasDerivedFrom wd38=pFactory.newWasDerivedFrom(a24,a20,black);
        WasDerivedFrom wd39=pFactory.newWasDerivedFrom(a24,a21,black);
        WasDerivedFrom wd40=pFactory.newWasDerivedFrom(a24,a22,black);

        WasDerivedFrom wd41=pFactory.newWasDerivedFrom(a25,a23,black);
        WasDerivedFrom wd42=pFactory.newWasDerivedFrom(a25,a24,black);
        WasDerivedFrom wd43=pFactory.newWasDerivedFrom(a26,a23,black);
        WasDerivedFrom wd44=pFactory.newWasDerivedFrom(a26,a24,black);
        WasDerivedFrom wd45=pFactory.newWasDerivedFrom(a27,a23,black);
        WasDerivedFrom wd46=pFactory.newWasDerivedFrom(a27,a24,black);

        WasDerivedFrom wd47=pFactory.newWasDerivedFrom(a28,a25,black);
        WasDerivedFrom wd48=pFactory.newWasDerivedFrom(a29,a26,black);
        WasDerivedFrom wd49=pFactory.newWasDerivedFrom(a30,a27,black);



        WasControlledBy wc1=pFactory.newWasControlledBy(p1,pFactory.newRole("user"),ag1,black);



        Container graph=pFactory.newContainer(black,
                                            new Overlaps[] { },
                                            new Activity[] {p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15},
                                            new Entity[] {a1,a2,a5,a6,a3,a4,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,
                                                            a21,a22,a23,a24,a25,a25p,a26,a26p,a27,a27p,a28,a29,a30},
                                            new Agent[] { //ag1
                                                        },
                                            new Object[] {u1,u2,u3,u4,u5,u6,u7,u8,u9,u10,u11,u12,u13,u14,u15,u16,u17,u18,u19,u20,
                                                          u21,u22,u23,u24,u25,u26,u27,u28,u29,u30,u31,u32,u33,u34,u30p,u32p,u34p,u35,u36,u37,
                                                          wg1,wg2,wg3,wg4,wg5,wg6,wg7,wg8,wg9,wg10,wg11,wg12,wg13,wg14,
                                                          wg15,wg16,wg17,wg18,wg19,wg20,
                                                          wd1,wd2,wd3,wd4,wd5,wd6,wd7,wd8,wd9,wd10,wd11,wd12,wd13,wd14,wd15,wd16,
                                                          wd17,wd18,wd19,wd20,wd21,wd22,wd23,wd24,wd25,wd26,wd27,wd28,wd29,wd30,wd31,
                                                          wd32,wd33,wd34,wd35,wd36,wd37,wd38,wd39,wd40,wd41,wd42,wd43,wd44,wd45,wd46,
                                                          wd47,wd48,wd49
                                                          //wc1,
                                            } );



        return graph;
    }
    




    public void testCopyPC1Full() throws java.io.FileNotFoundException,  java.io.IOException   {
        ProvFactory pFactory=new ProvFactory();

        Container graph2=pFactory.newContainer(graph1);

        assertTrue( "self graph1 differ", graph1.equals(graph1) );        

        assertTrue( "self graph2 differ", graph2.equals(graph2) );        

        assertTrue( "graph1 graph2 differ", graph1.equals(graph2) );        
        
    }

}
