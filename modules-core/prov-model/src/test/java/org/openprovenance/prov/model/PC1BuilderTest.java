package org.openprovenance.prov.model;

import junit.framework.TestCase;
import org.openprovenance.prov.model.builder.Builder;

import java.io.IOException;
import java.util.List;


public class PC1BuilderTest extends TestCase {

    static String URL_LOCATION = "https://www.ipaw.info/challenge/";

    public static final String PC1_NS = "http://www.ipaw.info/pc1/";
    public static final String PC1_PREFIX = "pc1";
    public static final String PRIM_NS = "http://openprovenance.org/primitives#";
    public static final String PRIM_PREFIX = "prim";

    public static ProvFactory pFactory=new org.openprovenance.prov.vanilla.ProvFactory();
    final public static QualifiedName PRIMITIVE_ALIGN_WARP = pFactory.newQualifiedName(PRIM_NS, "align_warp", PRIM_PREFIX);
    final public static QualifiedName PRIMITIVE_RESLICE = pFactory.newQualifiedName(PRIM_NS, "reslice", PRIM_PREFIX);
    final public static QualifiedName PRIMITIVE_SOFTMEAN = pFactory.newQualifiedName(PRIM_NS, "softmean", PRIM_PREFIX);
    final public static QualifiedName PRIMITIVE_CONVERT = pFactory.newQualifiedName(PRIM_NS, "convert", PRIM_PREFIX);
    final public static QualifiedName PRIMITIVE_SLICER = pFactory.newQualifiedName(PRIM_NS, "slicer", PRIM_PREFIX);

    public static Name name=pFactory.getName();


    public void testBuild() throws IOException {

        Document doc = makePC1();

        assertEquals(25,doc.getStatementOrBundle().size());
        System.out.println(doc.getStatementOrBundle());

    }

    public Document makePC1() {
        Builder builder=new Builder(pFactory,pFactory);
        Document doc =
                builder .prefix(PC1_PREFIX, PC1_NS)
                        .prefix(PRIM_PREFIX, PRIM_NS)

                        .forEach(List.of("a1","a2","a3","a4"), (a) ->
                                builder .activity()
                                        .id(PC1_PREFIX, a).knownAsLocal()
                                        .type(PRIMITIVE_ALIGN_WARP)
                                        .label("align_warp " + a)
                                        .build())

                        .forEach(List.of("a5","a6","a7","a8"), (a,count) ->
                                builder .activity()
                                        .id(PC1_PREFIX, a).knownAsLocal()
                                        .type(PRIMITIVE_RESLICE)
                                        .label("Reslice " + (count+1))
                                        .build())

                        .activity()
                        .id(PC1_PREFIX, "a9").knownAsLocal()
                        .type(PRIMITIVE_SOFTMEAN)
                        .label("Softmean")
                        .build()

                        .forEach(List.of("a10","a11","a12"), (a, count) ->
                                builder .activity()
                                        .id(PC1_PREFIX, a).knownAsLocal()
                                        .type(PRIMITIVE_SLICER)
                                        .label("Slicer " + (count+1))
                                        .build())

                        .forEach(List.of("a13","a14", "a15"), (a, count) ->
                                builder .activity()
                                        .id(PC1_PREFIX, a).knownAsLocal()
                                        .type(PRIMITIVE_CONVERT)
                                        .label("Convert " + (count+1))
                                        .build())

                        .entity()
                        .id(PC1_PREFIX, "e1").knownAsLocal()
                        .label("Reference Image")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "reference.img")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e2").knownAsLocal()
                        .label("Reference Header")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "reference.hdr")
                        .build()

                        .forEach(List.of(1,2,3,4), (a) -> {
                            int a1 = 2*a+1;
                            int a2 = 2*a+2;
                            builder.entity()
                                    .id(PC1_PREFIX, "e" + a1).knownAsLocal()
                                    .label("Anatomy I" + a1)
                                    .type(PRIM_PREFIX, "FILE")
                                    .attr(name.PROV_LOCATION, URL_LOCATION + "anatomy" + a1 + ".img")
                                    .build();
                            builder.entity()
                                    .id(PC1_PREFIX, "e" + a2).knownAsLocal()
                                    .label("Anatomy H" + a2)
                                    .type(PRIM_PREFIX, "FILE")
                                    .attr(name.PROV_LOCATION, URL_LOCATION + "anatomy" + a2 + ".hdr")
                                    .build();
                            return builder;
                        })

                        .forEach(List.of(1,2,3,4), (a) ->
                                builder.entity()
                                        .id(PC1_PREFIX, "e" + (10+a)).knownAsLocal()
                                        .label("Warp Params" + a)
                                        .type(PRIM_PREFIX, "FILE")
                                        .attr(name.PROV_LOCATION, URL_LOCATION + "warp" + a + ".warp")
                                        .build())

                        .forEach(List.of(1,2,3,4), (a) -> {
                            int a1 = 2*a+13;
                            int a2 = 2*a+14;
                            builder.entity()
                                    .id(PC1_PREFIX, "e" + a1).knownAsLocal()
                                    .label("Resliced I" + a1)
                                    .type(PRIM_PREFIX, "FILE")
                                    .attr(name.PROV_LOCATION, URL_LOCATION + "resliced" + a1 + ".img")
                                    .build();
                            builder.entity()
                                    .id(PC1_PREFIX, "e" + a2).knownAsLocal()
                                    .label("Resliced H" + a2)
                                    .type(PRIM_PREFIX, "FILE")
                                    .attr(name.PROV_LOCATION, URL_LOCATION + "resliced" + a2 + ".hdr")
                                    .build();
                            return builder;
                        })

                        .entity()
                        .id(PC1_PREFIX, "e23").knownAsLocal()
                        .label("Atlas Image")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas.img")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e24").knownAsLocal()
                        .label("Atlas Header")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas.hdr")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e25").knownAsLocal()
                        .label("Atlas X Slice")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas-x.pgm")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e26").knownAsLocal()
                        .label("Atlas Y Slice")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas-y.pgm")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e27").knownAsLocal()
                        .label("Atlas Z Slice")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas-z.pgm")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e28").knownAsLocal()
                        .label("Atlas X Graphic")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas-x.gif")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e29").knownAsLocal()
                        .label("Atlas Y Graphic")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas-y.gif")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e30").knownAsLocal()
                        .label("Atlas Z Graphic")
                        .type(PRIM_PREFIX, "FILE")
                        .attr(name.PROV_LOCATION, URL_LOCATION + "atlas-z.gif")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e25p").knownAsLocal()
                        .label("slicer param 1")
                        .attr(name.PROV_VALUE, "-x .5")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e26p").knownAsLocal()
                        .label("slicer param 2")
                        .attr(name.PROV_VALUE, "-y .5")
                        .build()

                        .entity()
                        .id(PC1_PREFIX, "e27p").knownAsLocal()
                        .label("slicer param 3")
                        .attr(name.PROV_VALUE, "-z .5")
                        .build()

                        .wasDerivedFrom().e2("e11").e1("e2").build()
                        .wasDerivedFrom().e2("e11").e1("e3").build()
                        .wasDerivedFrom().e2("e11").e1("e4").build()
                        .wasDerivedFrom().e2("e12").e1("e1").build()
                        .wasDerivedFrom().e2("e12").e1("e2").build()
                        .wasDerivedFrom().e2("e12").e1("e5").build()
                        .wasDerivedFrom().e2("e12").e1("e6").build()
                        .wasDerivedFrom().e2("e13").e1("e1").build()
                        .wasDerivedFrom().e2("e13").e1("e2").build()
                        .wasDerivedFrom().e2("e13").e1("e7").build()
                        .wasDerivedFrom().e2("e13").e1("e8").build()
                        .wasDerivedFrom().e2("e14").e1("e1").build()
                        .wasDerivedFrom().e2("e14").e1("e10").build()
                        .wasDerivedFrom().e2("e14").e1("e2").build()
                        .wasDerivedFrom().e2("e14").e1("e9").build()
                        .wasDerivedFrom().e2("e15").e1("e11").build()
                        .wasDerivedFrom().e2("e16").e1("e11").build()
                        .wasDerivedFrom().e2("e17").e1("e12").build()
                        .wasDerivedFrom().e2("e18").e1("e12").build()
                        .wasDerivedFrom().e2("e19").e1("e13").build()
                        .wasDerivedFrom().e2("e20").e1("e13").build()
                        .wasDerivedFrom().e2("e21").e1("e14").build()
                        .wasDerivedFrom().e2("e22").e1("e14").build()
                        .wasDerivedFrom().e2("e23").e1("e15").build()
                        .wasDerivedFrom().e2("e23").e1("e16").build()
                        .wasDerivedFrom().e2("e23").e1("e17").build()
                        .wasDerivedFrom().e2("e23").e1("e18").build()
                        .wasDerivedFrom().e2("e23").e1("e19").build()
                        .wasDerivedFrom().e2("e23").e1("e20").build()
                        .wasDerivedFrom().e2("e23").e1("e21").build()
                        .wasDerivedFrom().e2("e23").e1("e22").build()
                        .wasDerivedFrom().e2("e24").e1("e15").build()
                        .wasDerivedFrom().e2("e24").e1("e16").build()
                        .wasDerivedFrom().e2("e24").e1("e17").build()
                        .wasDerivedFrom().e2("e24").e1("e18").build()
                        .wasDerivedFrom().e2("e24").e1("e19").build()
                        .wasDerivedFrom().e2("e24").e1("e20").build()
                        .wasDerivedFrom().e2("e24").e1("e21").build()
                        .wasDerivedFrom().e2("e24").e1("e22").build()
                        .wasDerivedFrom().e2("e25").e1("e23").build()
                        .wasDerivedFrom().e2("e25").e1("e24").build()
                        .wasDerivedFrom().e2("e26").e1("e23").build()
                        .wasDerivedFrom().e2("e26").e1("e24").build()
                        .wasDerivedFrom().e2("e27").e1("e23").build()
                        .wasDerivedFrom().e2("e27").e1("e24").build()
                        .wasDerivedFrom().e2("e28").e1("e25").build()
                        .wasDerivedFrom().e2("e29").e1("e26").build()
                        .wasDerivedFrom().e2("e30").e1("e27").build()

                        .used().a("a1").role("hdr").e("e4").build()
                        .used().a("a1").role("hdrRef").e("e2").build()
                        .used().a("a1").role("img").e("e3").build()
                        .used().a("a1").role("imgRef").e("e1").build()
                        .used().a("a10").role("hdr").e("e24").build()
                        .used().a("a10").role("img").e("e23").build()
                        .used().a("a10").role("param").e("e25p").build()
                        .used().a("a11").role("hdr").e("e24").build()
                        .used().a("a11").role("img").e("e23").build()
                        .used().a("a11").role("param").e("e26p").build()
                        .used().a("a12").role("hdr").e("e24").build()
                        .used().a("a12").role("img").e("e23").build()
                        .used().a("a12").role("param").e("e27p").build()
                        .used().a("a13").role("in").e("e25").build()
                        .used().a("a14").role("in").e("e26").build()
                        .used().a("a15").role("in").e("e27").build()
                        .used().a("a2").role("hdr").e("e6").build()
                        .used().a("a2").role("hdrRef").e("e2").build()
                        .used().a("a2").role("img").e("e5").build()
                        .used().a("a2").role("imgRef").e("e1").build()
                        .used().a("a3").role("hdr").e("e8").build()
                        .used().a("a3").role("hdrRef").e("e2").build()
                        .used().a("a3").role("img").e("e7").build()
                        .used().a("a3").role("imgRef").e("e1").build()
                        .used().a("a4").role("hdr").e("e10").build()
                        .used().a("a4").role("hdrRef").e("e2").build()
                        .used().a("a4").role("img").e("e9").build()
                        .used().a("a4").role("imgRef").e("e1").build()
                        .used().a("a5").role("in").e("e11").build()
                        .used().a("a6").role("in").e("e12").build()
                        .used().a("a7").role("in").e("e13").build()
                        .used().a("a8").role("in").e("e14").build()
                        .used().a("a9").role("h1").e("e16").build()
                        .used().a("a9").role("h2").e("e18").build()
                        .used().a("a9").role("h3").e("e20").build()
                        .used().a("a9").role("h4").e("e22").build()
                        .used().a("a9").role("i1").e("e15").build()
                        .used().a("a9").role("i2").e("e17").build()
                        .used().a("a9").role("i3").e("e19").build()
                        .used().a("a9").role("i4").e("e21").build()

                        .wasGeneratedBy().e("e11").role("out").a("a1").now().build()
                        .wasGeneratedBy().e("e12").role("out").a("a2").now().build()
                        .wasGeneratedBy().e("e13").role("out").a("a3").now().build()
                        .wasGeneratedBy().e("e14").role("out").a("a4").build()
                        .wasGeneratedBy().e("e15").role("img").a("a5").build()
                        .wasGeneratedBy().e("e16").role("hdr").a("a5").build()
                        .wasGeneratedBy().e("e17").role("img").a("a6").build()
                        .wasGeneratedBy().e("e18").role("hdr").a("a6").build()
                        .wasGeneratedBy().e("e19").role("img").a("a7").build()
                        .wasGeneratedBy().e("e20").role("hdr").a("a7").build()
                        .wasGeneratedBy().e("e21").role("img").a("a8").build()
                        .wasGeneratedBy().e("e22").role("hdr").a("a8").build()

                        .wasGeneratedBy().e("e23").role("img").a("a9").build()
                        .wasGeneratedBy().e("e24").role("hdr").a("a9").build()

                        .wasGeneratedBy().e("e25").role("out").a("a10").build()
                        .wasGeneratedBy().e("e26").role("out").a("a11").build()
                        .wasGeneratedBy().e("e27").role("out").a("a12").build()

                        .wasGeneratedBy().e("e28").role("out").a("a13").build()
                        .wasGeneratedBy().e("e29").role("out").a("a14").build()
                        .wasGeneratedBy().e("e30").role("out").a("a15").build()



                        .build();
        return doc;
    }


}
