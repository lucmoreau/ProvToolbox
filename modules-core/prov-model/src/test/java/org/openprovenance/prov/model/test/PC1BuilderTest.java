package org.openprovenance.prov.model.test;

import junit.framework.TestCase;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.builder.Builder;
import org.openprovenance.prov.model.builder.Prefix;

import java.io.IOException;
import java.util.List;


public class PC1BuilderTest extends TestCase {

    static String URL_LOCATION = "https://www.ipaw.info/challenge/";

    public static final String PC1_NS = "http://www.ipaw.info/pc1/";
    public static final String PRIM_NS = "http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX = "prim";

    public static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();

    public static Name name=pFactory.getName();


    public void testBuild() throws IOException {

        Document doc = makePC1();

        assertEquals(156,doc.getStatementOrBundle().size());
       // System.out.println(doc.getStatementOrBundle());

    }

    public Document makePC1() {
        Builder builder=new Builder(pFactory,pFactory,pFactory);

        /* some prelude */

        Prefix PC1  = builder.prefix("pc1");
        Prefix PRIM = builder.prefix("prim");
        builder .prefix(PC1, PC1_NS)
                .prefix(PRIM, PRIM_NS);

        QualifiedName PC1_URL=builder.qn(PC1, "url");
        QualifiedName PRIMITIVE_ALIGN_WARP=builder.qn(PRIM, "align_warp");
        QualifiedName PRIMITIVE_RESLICE=builder.qn(PRIM, "reslice");
        QualifiedName PRIMITIVE_SOFTMEAN=builder.qn(PRIM, "softmean");
        QualifiedName PRIMITIVE_CONVERT=builder.qn(PRIM, "convert");
        QualifiedName PRIMITIVE_SLICER=builder.qn(PRIM, "slicer");

        /* creation of a document */


                builder .forEach(List.of("a1","a2","a3","a4"), (a) ->
                                builder .activity()
                                        .id(PC1, a).aka()
                                        .type(PRIMITIVE_ALIGN_WARP)
                                        .label("align_warp " + a)
                                        .build())

                        .forEach(List.of("a5","a6","a7","a8"), (a,count) ->
                                builder .activity()
                                        .id(PC1, a).aka()
                                        .type(PRIMITIVE_RESLICE)
                                        .label("Reslice " + (count+1))
                                        .build())

                        .activity()
                        .id(PC1, "a9").aka()
                        .type(PRIMITIVE_SOFTMEAN)
                        .label("Softmean")
                        .build()

                        .forEach(List.of("a10","a11","a12"), (a, count) ->
                                builder .activity()
                                        .id(PC1, a).aka()
                                        .type(PRIMITIVE_SLICER)
                                        .label("Slicer " + (count+1))
                                        .build())

                        .forEach(List.of("a13","a14", "a15"), (a, count) ->
                                builder .activity()
                                        .id(PC1, a).aka()
                                        .type(PRIMITIVE_CONVERT)
                                        .label("Convert " + (count+1))
                                        .build())

                        .entity()
                        .id(PC1, "e1").aka()
                        .label("Reference Image")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "reference.img")
                        .build()

                        .entity()
                        .id(PC1, "e2").aka()
                        .label("Reference Header")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "reference.hdr")
                        .build()

                        .forEach(List.of(1,2,3,4), (a) -> {
                            int a1 = 2*a+1;
                            int a2 = 2*a+2;
                            builder.entity()
                                    .id(PC1, "e" + a1).aka()
                                    .label("Anatomy I" + a1)
                                    .type(PRIM, "FILE")
                                    .attr(PC1_URL, URL_LOCATION + "anatomy" + a1 + ".img")
                                    .build();
                            builder.entity()
                                    .id(PC1, "e" + a2).aka()
                                    .label("Anatomy H" + a2)
                                    .type(PRIM, "FILE")
                                    .attr(PC1_URL, URL_LOCATION + "anatomy" + a2 + ".hdr")
                                    .build();
                            return builder;
                        })

                        .forEach(List.of(1,2,3,4), (a) ->
                                builder.entity()
                                        .id(PC1, "e" + (10+a)).aka()
                                        .label("Warp Params" + a)
                                        .type(PRIM, "FILE")
                                        .attr(PC1_URL, URL_LOCATION + "warp" + a + ".warp")
                                        .build())

                        .forEach(List.of(1,2,3,4), (a) -> {
                            int a1 = 2*a+13;
                            int a2 = 2*a+14;
                            builder.entity()
                                    .id(PC1, "e" + a1).aka()
                                    .label("Resliced I" + a1)
                                    .type(PRIM, "FILE")
                                    .attr(PC1_URL, URL_LOCATION + "resliced" + a1 + ".img")
                                    .build();
                            builder.entity()
                                    .id(PC1, "e" + a2).aka()
                                    .label("Resliced H" + a2)
                                    .type(PRIM, "FILE")
                                    .attr(PC1_URL, URL_LOCATION + "resliced" + a2 + ".hdr")
                                    .build();
                            return builder;
                        }).build();

                builder
                        .entity()
                        .id(PC1, "e23").aka()
                        .label("Atlas Image")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas.img")
                        .build()

                        .entity()
                        .id(PC1, "e24").aka()
                        .label("Atlas Header")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas.hdr")
                        .build()

                        .entity()
                        .id(PC1, "e25").aka()
                        .label("Atlas X Slice")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas-x.pgm")
                        .build()

                        .entity()
                        .id(PC1, "e26").aka()
                        .label("Atlas Y Slice")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas-y.pgm")
                        .build()

                        .entity()
                        .id(PC1, "e27").aka()
                        .label("Atlas Z Slice")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas-z.pgm")
                        .build()

                        .entity()
                        .id(PC1, "e28").aka()
                        .label("Atlas X Graphic")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas-x.gif")
                        .build()

                        .entity()
                        .id(PC1, "e29").aka()
                        .label("Atlas Y Graphic")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas-y.gif")
                        .build()

                        .entity()
                        .id(PC1, "e30").aka()
                        .label("Atlas Z Graphic")
                        .type(PRIM, "FILE")
                        .attr(PC1_URL, URL_LOCATION + "atlas-z.gif")
                        .build()

                        .entity()
                        .id(PC1, "e25p").aka()
                        .label("slicer param 1")
                        .value( "-x .5")
                        .build()

                        .entity()
                        .id(PC1, "e26p").aka()
                        .label("slicer param 2")
                        .value( "-y .5")
                        .build()

                        .entity()
                        .id(PC1, "e27p").aka()
                        .label("slicer param 3")
                        .value("-z .5")
                        .build();

// need to break the chain here, as we need to use the same builder for the rest of the document
        builder

                        .wasDerivedFrom().generatedEntity("e11").usedEntity("e2").build()
                        .wasDerivedFrom().generatedEntity("e11").usedEntity("e3").build()
                        .wasDerivedFrom().generatedEntity("e11").usedEntity("e4").build()
                        .wasDerivedFrom().generatedEntity("e12").usedEntity("e1").build()
                        .wasDerivedFrom().generatedEntity("e12").usedEntity("e2").build()
                        .wasDerivedFrom().generatedEntity("e12").usedEntity("e5").build()
                        .wasDerivedFrom().generatedEntity("e12").usedEntity("e6").build()
                        .wasDerivedFrom().generatedEntity("e13").usedEntity("e1").build()
                        .wasDerivedFrom().generatedEntity("e13").usedEntity("e2").build()
                        .wasDerivedFrom().generatedEntity("e13").usedEntity("e7").build()
                        .wasDerivedFrom().generatedEntity("e13").usedEntity("e8").build()
                        .wasDerivedFrom().generatedEntity("e14").usedEntity("e1").build()
                        .wasDerivedFrom().generatedEntity("e14").usedEntity("e10").build()
                        .wasDerivedFrom().generatedEntity("e14").usedEntity("e2").build()
                        .wasDerivedFrom().generatedEntity("e14").usedEntity("e9").build()
                        .wasDerivedFrom().generatedEntity("e15").usedEntity("e11").build()
                        .wasDerivedFrom().generatedEntity("e16").usedEntity("e11").build()
                        .wasDerivedFrom().generatedEntity("e17").usedEntity("e12").build()
                        .wasDerivedFrom().generatedEntity("e18").usedEntity("e12").build()
                        .wasDerivedFrom().generatedEntity("e19").usedEntity("e13").build()
                        .wasDerivedFrom().generatedEntity("e20").usedEntity("e13").build()
                        .wasDerivedFrom().generatedEntity("e21").usedEntity("e14").build()
                        .wasDerivedFrom().generatedEntity("e22").usedEntity("e14").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e15").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e16").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e17").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e18").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e19").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e20").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e21").build()
                        .wasDerivedFrom().generatedEntity("e23").usedEntity("e22").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e15").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e16").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e17").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e18").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e19").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e20").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e21").build()
                        .wasDerivedFrom().generatedEntity("e24").usedEntity("e22").build()
                        .wasDerivedFrom().generatedEntity("e25").usedEntity("e23").build()
                        .wasDerivedFrom().generatedEntity("e25").usedEntity("e24").build()
                        .wasDerivedFrom().generatedEntity("e26").usedEntity("e23").build()
                        .wasDerivedFrom().generatedEntity("e26").usedEntity("e24").build()
                        .wasDerivedFrom().generatedEntity("e27").usedEntity("e23").build()
                        .wasDerivedFrom().generatedEntity("e27").usedEntity("e24").build()
                        .wasDerivedFrom().generatedEntity("e28").usedEntity("e25").build()
                        .wasDerivedFrom().generatedEntity("e29").usedEntity("e26").build()
                        .wasDerivedFrom().generatedEntity("e30").usedEntity("e27").build()
                .build();

        builder

                        .used().activity("a1").role("hdr").entity("e4").build()
                        .used().activity("a1").role("hdrRef").entity("e2").build()
                        .used().activity("a1").role("img").entity("e3").build()
                        .used().activity("a1").role("imgRef").entity("e1").build()
                        .used().activity("a10").role("hdr").entity("e24").build()
                        .used().activity("a10").role("img").entity("e23").build()
                        .used().activity("a10").role("param").entity("e25p").build()
                        .used().activity("a11").role("hdr").entity("e24").build()
                        .used().activity("a11").role("img").entity("e23").build()
                        .used().activity("a11").role("param").entity("e26p").build()
                        .used().activity("a12").role("hdr").entity("e24").build()
                        .used().activity("a12").role("img").entity("e23").build()
                        .used().activity("a12").role("param").entity("e27p").build()
                        .used().activity("a13").role("in").entity("e25").build()
                        .used().activity("a14").role("in").entity("e26").build()
                        .used().activity("a15").role("in").entity("e27").build()
                        .used().activity("a2").role("hdr").entity("e6").build()
                        .used().activity("a2").role("hdrRef").entity("e2").build()
                        .used().activity("a2").role("img").entity("e5").build()
                        .used().activity("a2").role("imgRef").entity("e1").build()
                        .used().activity("a3").role("hdr").entity("e8").build()
                        .used().activity("a3").role("hdrRef").entity("e2").build()
                        .used().activity("a3").role("img").entity("e7").build()
                        .used().activity("a3").role("imgRef").entity("e1").build()
                        .used().activity("a4").role("hdr").entity("e10").build()
                        .used().activity("a4").role("hdrRef").entity("e2").build()
                        .used().activity("a4").role("img").entity("e9").build()
                        .used().activity("a4").role("imgRef").entity("e1").build()
                        .used().activity("a5").role("in").entity("e11").build()
                        .used().activity("a6").role("in").entity("e12").build()
                        .used().activity("a7").role("in").entity("e13").build()
                        .used().activity("a8").role("in").entity("e14").build()
                        .used().activity("a9").role("h1").entity("e16").build()
                        .used().activity("a9").role("h2").entity("e18").build()
                        .used().activity("a9").role("h3").entity("e20").build()
                        .used().activity("a9").role("h4").entity("e22").build()
                        .used().activity("a9").role("i1").entity("e15").build()
                        .used().activity("a9").role("i2").entity("e17").build()
                        .used().activity("a9").role("i3").entity("e19").build()
                        .used().activity("a9").role("i4").entity("e21").build();

        Document doc =
                builder

                        .wasGeneratedBy().entity("e11").role("out").activity("a1").now().build()
                        .wasGeneratedBy().entity("e12").role("out").activity("a2").now().build()
                        .wasGeneratedBy().entity("e13").role("out").activity("a3").now().build()
                        .wasGeneratedBy().entity("e14").role("out").activity("a4").build()
                        .wasGeneratedBy().entity("e15").role("img").activity("a5").build()
                        .wasGeneratedBy().entity("e16").role("hdr").activity("a5").build()
                        .wasGeneratedBy().entity("e17").role("img").activity("a6").build()
                        .wasGeneratedBy().entity("e18").role("hdr").activity("a6").build()
                        .wasGeneratedBy().entity("e19").role("img").activity("a7").build()
                        .wasGeneratedBy().entity("e20").role("hdr").activity("a7").build()
                        .wasGeneratedBy().entity("e21").role("img").activity("a8").build()
                        .wasGeneratedBy().entity("e22").role("hdr").activity("a8").build()

                        .wasGeneratedBy().entity("e23").role("img").activity("a9").build()
                        .wasGeneratedBy().entity("e24").role("hdr").activity("a9").build()

                        .wasGeneratedBy().entity("e25").role("out").activity("a10").build()
                        .wasGeneratedBy().entity("e26").role("out").activity("a11").build()
                        .wasGeneratedBy().entity("e27").role("out").activity("a12").build()

                        .wasGeneratedBy().entity("e28").role("out").activity("a13").build()
                        .wasGeneratedBy().entity("e29").role("out").activity("a14").build()
                        .wasGeneratedBy().entity("e30").role("out").activity("a15").build()



                        .build();
        return doc;
    }



}
