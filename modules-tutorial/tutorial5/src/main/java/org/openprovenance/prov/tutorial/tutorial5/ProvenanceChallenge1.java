package org.openprovenance.prov.tutorial.tutorial5;

import java.util.Arrays;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.StatementOrBundle;
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
public class ProvenanceChallenge1 extends ChallengeUtil implements ChallengeConstants{



    QualifiedName PRIMITIVE_ALIGN_WARP;
    QualifiedName PRIMITIVE_CONVERT;
    QualifiedName PRIMITIVE_SLICER;
    QualifiedName PRIMITIVE_RESLICE;
    QualifiedName PRIMITIVE_SOFTMEAN;
    

    public ProvenanceChallenge1(ProvFactory pFactory) {
        super(pFactory);

        PRIMITIVE_ALIGN_WARP = pFactory.newQualifiedName(PRIM_NS, ALIGN_WARP, PRIM_PREFIX);
        PRIMITIVE_CONVERT    = pFactory.newQualifiedName(PRIM_NS, CONVERT, PRIM_PREFIX);
        PRIMITIVE_SLICER     = pFactory.newQualifiedName(PRIM_NS, SLICER, PRIM_PREFIX);
        PRIMITIVE_RESLICE    = pFactory.newQualifiedName(PRIM_NS, RESLICE, PRIM_PREFIX);
        PRIMITIVE_SOFTMEAN   = pFactory.newQualifiedName(PRIM_NS, SOFTMEAN, PRIM_PREFIX);

    }

    public void addUrl(HasOther p1, String val) {
        p1.getOther().add(pFactory.newOther(PC1_NS, "url", PC1_PREFIX, val, name.XSD_STRING));
    }
    
    public Document makePC1FullGraph(ProvFactory pFactory) {

        Document graph = pFactory.newDocument();
        List<StatementOrBundle> ll=graph.getStatementOrBundle();
        

        Activity a1 = pFactory.newActivity(pc("a#align_warp1"));
        pFactory.addType(a1, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a2 = pFactory.newActivity(pc("a#align_warp2"));
        pFactory.addType(a2, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a3 = pFactory.newActivity(pc("a#align_warp3"));
        pFactory.addType(a3, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a4 = pFactory.newActivity(pc("a#align_warp4"));
        pFactory.addType(a4, PRIMITIVE_ALIGN_WARP, name.PROV_QUALIFIED_NAME);

        Activity a5 = pFactory.newActivity(pc("a#reslice1"));
        pFactory.addType(a5, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a6 = pFactory.newActivity(pc("a#reslice2"));
        pFactory.addType(a6, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a7 = pFactory.newActivity(pc("a#reslice3"));
        pFactory.addType(a7, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a8 = pFactory.newActivity(pc("a#reslice4"));
        pFactory.addType(a8, PRIMITIVE_RESLICE, name.PROV_QUALIFIED_NAME);

        Activity a9 = pFactory.newActivity(pc("a#softmean"));
        pFactory.addType(a9, PRIMITIVE_SOFTMEAN, name.PROV_QUALIFIED_NAME);

        Activity a10 = pFactory.newActivity(pc("a#slicer1"));
        pFactory.addType(a10, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);

        Activity a11 = pFactory.newActivity(pc("a#slicer2"));
        pFactory.addType(a11, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);

        Activity a12 = pFactory.newActivity(pc("a#slicer3"));
        pFactory.addType(a12, PRIMITIVE_SLICER, name.PROV_QUALIFIED_NAME);

        Activity a13 = pFactory.newActivity(pc("a#convert1"));
        pFactory.addType(a13, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);

        Activity a14 = pFactory.newActivity(pc("a#convert2"));
        pFactory.addType(a14, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);

        Activity a15 = pFactory.newActivity(pc("a#convert3"));
        pFactory.addType(a15, PRIMITIVE_CONVERT, name.PROV_QUALIFIED_NAME);
        
        ll.addAll(Arrays.asList(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15));

//        Agent ag1 = pFactory.newAgent(q("ag1"), "John Doe");
        
        //ll.add(ag1);

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
        Entity e25p = newParameter(pFactory, "params#slicer1", "slicer param 1", "-x .5");
        Entity e26 = newFile(pFactory, "atlas-y.pgm", "Atlas Y Slice");
        Entity e26p = newParameter(pFactory, "params#slicer2", "slicer param 2", "-y .5");
        Entity e27 = newFile(pFactory, "atlas-z.pgm", "Atlas Z Slice");
        Entity e27p = newParameter(pFactory, "params#slicer3", "slicer param 3", "-z .5");

        Entity e28 = newFile(pFactory, "atlas-x.gif", "Atlas X Graphic");
        Entity e29 = newFile(pFactory, "atlas-y.gif", "Atlas Y Graphic");
        Entity e30 = newFile(pFactory, "atlas-z.gif", "Atlas Z Graphic");
        
        ll.addAll(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10));
        ll.addAll(Arrays.asList(e11, e12, e13, e14, e15, e16, e17, e18, e19, e20));
        ll.addAll(Arrays.asList(e21, e22, e23, e24, e25, e25p, e26, e26p, e27, e27p, e28, e29, e30));

        ll.add(newUsed(a1, ROLE_IMG, e3));
        ll.add(newUsed(a1, ROLE_HDR, e4));
        ll.add(newUsed(a1, ROLE_IMG_REF, e1));
        ll.add(newUsed(a1, ROLE_HDR_REF, e2));
        ll.add(newUsed(a2, ROLE_IMG, e5));
        ll.add(newUsed(a2, ROLE_HDR, e6));
        ll.add(newUsed(a2, ROLE_IMG_REF, e1));
        ll.add(newUsed(a2, ROLE_HDR_REF, e2));
        ll.add(newUsed(a3, ROLE_IMG, e7));
        ll.add(newUsed(a3, ROLE_HDR, e8));
        ll.add(newUsed(a3, ROLE_IMG_REF, e1));
        ll.add(newUsed(a3, ROLE_HDR_REF, e2));
        ll.add(newUsed(a4, ROLE_IMG, e9));
        ll.add(newUsed(a4, ROLE_HDR, e10));
        ll.add(newUsed(a4, ROLE_IMG_REF, e1));
        ll.add(newUsed(a4, ROLE_HDR_REF, e2));

        ll.add(newUsed(a5, ROLE_IN, e11));
        ll.add(newUsed(a6, ROLE_IN, e12));
        ll.add(newUsed(a7, ROLE_IN, e13));
        ll.add(newUsed(a8, ROLE_IN, e14));

        ll.add(newUsed(a9, ROLE_I1, e15));
        ll.add(newUsed(a9, ROLE_H1, e16));
        ll.add(newUsed(a9, ROLE_I2, e17));
        ll.add(newUsed(a9, ROLE_H2, e18));
        ll.add(newUsed(a9, ROLE_I3, e19));
        ll.add(newUsed(a9, ROLE_H3, e20));
        ll.add(newUsed(a9, ROLE_I4, e21));
        ll.add(newUsed(a9, ROLE_H4, e22));

        ll.add(newUsed(a10, ROLE_IMG, e23));
        ll.add(newUsed(a10, ROLE_HDR, e24));
        ll.add(newUsed(a10, ROLE_PARAM, e25p));
        ll.add(newUsed(a11, ROLE_IMG, e23));
        ll.add(newUsed(a11, ROLE_HDR, e24));
        ll.add(newUsed(a11, ROLE_PARAM, e26p));
        ll.add(newUsed(a12, ROLE_IMG, e23));
        ll.add(newUsed(a12, ROLE_HDR, e24));
        ll.add(newUsed(a12, ROLE_PARAM, e27p));

        ll.add(newUsed(a13, ROLE_IN, e25));
        ll.add(newUsed(a14, ROLE_IN, e26));
        ll.add(newUsed(a15, ROLE_IN, e27));

        ll.add(newWasGeneratedBy(e11, ROLE_OUT, a1));
        ll.add(newWasGeneratedBy(e12, ROLE_OUT, a2));
        ll.add(newWasGeneratedBy(e13, ROLE_OUT, a3));
        ll.add(newWasGeneratedBy(e14, ROLE_OUT, a4));

        ll.add(newWasGeneratedBy(e15, ROLE_IMG, a5));
        ll.add(newWasGeneratedBy(e16, ROLE_HDR, a5));
        ll.add(newWasGeneratedBy(e17, ROLE_IMG, a6));
        ll.add(newWasGeneratedBy(e18, ROLE_HDR, a6));
        ll.add(newWasGeneratedBy(e19, ROLE_IMG, a7));
        ll.add(newWasGeneratedBy(e20, ROLE_HDR, a7));
        ll.add(newWasGeneratedBy(e21, ROLE_IMG, a8));
        ll.add(newWasGeneratedBy(e22, ROLE_HDR, a8));

        ll.add(newWasGeneratedBy(e23, ROLE_IMG, a9));
        ll.add(newWasGeneratedBy(e24, ROLE_HDR, a9));

        ll.add(newWasGeneratedBy(e25, ROLE_OUT, a10));
        ll.add(newWasGeneratedBy(e26, ROLE_OUT, a11));
        ll.add(newWasGeneratedBy(e27, ROLE_OUT, a12));

        WasGeneratedBy wg18 = newWasGeneratedBy(e28, ROLE_OUT, a13);
        WasGeneratedBy wg19 = newWasGeneratedBy(e29, ROLE_OUT, a14);
        WasGeneratedBy wg20 = newWasGeneratedBy(e30, ROLE_OUT, a15);
        //wg18.setTime(pFactory.newTimeNow());
        //wg19.setTime(pFactory.newTimeNow());
        //wg20.setTime(pFactory.newTimeNow());
        
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
        ll.add(newWasDerivedFrom(e25, e25p));
        ll.add(newWasDerivedFrom(e26, e23));
        ll.add(newWasDerivedFrom(e26, e24));
        ll.add(newWasDerivedFrom(e26, e26p));
        ll.add(newWasDerivedFrom(e27, e23));
        ll.add(newWasDerivedFrom(e27, e24));
        ll.add(newWasDerivedFrom(e27, e27p));
        ll.add(newWasDerivedFrom(e28, e25));
        ll.add(newWasDerivedFrom(e29, e26));
        ll.add(newWasDerivedFrom(e30, e27));


        
        ll.add(pFactory.newAgent(pc("ag1")));
        ll.add(pFactory.newActivity(pc("a#pcworkflow")));
        ll.add(pFactory.newWasAssociatedWith(null,pc("a#pcworkflow"), pc("ag1")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#align_warp1"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#align_warp2"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#align_warp3"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#align_warp4"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#reslice1"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#reslice2"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#reslice3"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#reslice4"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#softmean"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#slicer1"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#slicer2"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#slicer3"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#convert1"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#convert2"), null, pc("a#pcworkflow")));
        ll.add(pFactory.newWasStartedBy(null, pc("a#convert3"), null, pc("a#pcworkflow")));
        
       

        graph.setNamespace(Namespace.gatherNamespaces(graph));

        return graph;
    }

    public Document makeDocument() {

        return makePC1FullGraph(pFactory);

    }

    public static void main(String[] args) {
        if (args.length != 2)
            throw new UnsupportedOperationException("main to be called with filename");
        String file1 = args[0];
        String file2 = args[1];

        ProvenanceChallenge1 pc1 = new ProvenanceChallenge1(InteropFramework.newXMLProvFactory());
        pc1.openingBanner();
        Document document = pc1.makeDocument();
        pc1.doConversions(document, file1);
        pc1.doConversions(document, file2);
        pc1.closingBanner();
        
    }

}
