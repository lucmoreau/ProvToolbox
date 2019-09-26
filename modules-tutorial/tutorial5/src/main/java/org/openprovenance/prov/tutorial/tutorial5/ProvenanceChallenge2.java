package org.openprovenance.prov.tutorial.tutorial5;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
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
public class ProvenanceChallenge2 extends ChallengeCommon<Collection<StatementOrBundle>> { 



    public ProvenanceChallenge2(ProvFactory pFactory) {
        super(pFactory);
    

    }

   
    
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
        Activity a1 = pFactory.newActivity(pc(activity));
        pFactory.addType(a1,prim(ALIGN_WARP), name.PROV_QUALIFIED_NAME);
        
        Entity e1 = newFile(pFactory, imgreffile1, imgreflabel);
        Entity e2 = newFile(pFactory, hdrreffile1, hdrreflabel);
        Entity e3 = newFile(pFactory, imgfile, imglabel);
        Entity e4 = newFile(pFactory, hdrfile, hdrlabel);
        
        ll.addAll(Arrays.asList(a1,e1,e2,e3,e4));

        ll.add(newUsed(a1, ROLE_IMG, e3));
        ll.add(newUsed(a1, ROLE_HDR, e4));
        ll.add(newUsed(a1, ROLE_IMG_REF, e1));
        ll.add(newUsed(a1, ROLE_HDR_REF, e2));
        
        Entity e11 = newFile(pFactory, warpfile, warplabel);
        
        ll.add(e11);

        ll.add(newWasGeneratedBy(e11, ROLE_OUT, a1));
        
        ll.add(newWasDerivedFrom(e11, e1));
        ll.add(newWasDerivedFrom(e11, e2));
        ll.add(newWasDerivedFrom(e11, e3));
        ll.add(newWasDerivedFrom(e11, e4));
        
        ll.add(pFactory.newAgent(pc(agent)));
        ll.add(pFactory.newActivity(pc(workflow)));
        ll.add(pFactory.newWasAssociatedWith(null,pc(workflow), pc(agent)));
        ll.add(pFactory.newWasStartedBy(null, pc(activity), null, pc(workflow)));
        
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
        
        Activity a5 = pFactory.newActivity(pc(activity));
        pFactory.addType(a5, pFactory.newQualifiedName(PRIM_NS, RESLICE,    PRIM_PREFIX), name.PROV_QUALIFIED_NAME);

        Entity e15 = newFile(pFactory, imgfile, imglabel);
        Entity e16 = newFile(pFactory, hdrfile, hdrlabel);
        
        Entity e11 = pFactory.newEntity(pc(warp));
        ll.add(newUsed(a5, ROLE_IN, e11));
        ll.add(newWasGeneratedBy(e15, ROLE_IMG, a5));
        ll.add(newWasGeneratedBy(e16, ROLE_HDR, a5));
        
        ll.add(newWasDerivedFrom(e15, e11));
        ll.add(newWasDerivedFrom(e16, e11));
        
        ll.addAll(Arrays.asList(a5,e15,e16,e11));

        ll.add(newUsed(a5, ROLE_IN, e11));
        
        ll.add(pFactory.newAgent(pc(agent)));
        ll.add(pFactory.newActivity(pc(workflow)));
        ll.add(pFactory.newWasAssociatedWith(null,pc(workflow), pc(agent)));
        ll.add(pFactory.newWasStartedBy(null, pc(activity), null, pc(workflow)));
        
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

        
        Activity a9 = pFactory.newActivity(pc(activity));
        pFactory.addType(a9, pFactory.newQualifiedName(PRIM_NS, SOFTMEAN,    PRIM_PREFIX), name.PROV_QUALIFIED_NAME);
        
        Entity e15 = pFactory.newEntity(pc(imgfile1));
        Entity e16 = pFactory.newEntity(pc(hdrfile1));
        Entity e17 = pFactory.newEntity(pc(imgfile2));
        Entity e18 = pFactory.newEntity(pc(hdrfile2));
        Entity e19 = pFactory.newEntity(pc(imgfile3));
        Entity e20 = pFactory.newEntity(pc(hdrfile3));
        Entity e21 = pFactory.newEntity(pc(imgfile4));
        Entity e22 = pFactory.newEntity(pc(hdrfile4));

        
        ll.add(newUsed(a9, ROLE_I1, e15));
        ll.add(newUsed(a9, ROLE_H1, e16));
        ll.add(newUsed(a9, ROLE_I2, e17));
        ll.add(newUsed(a9, ROLE_H2, e18));
        ll.add(newUsed(a9, ROLE_I3, e19));
        ll.add(newUsed(a9, ROLE_H3, e20));
        ll.add(newUsed(a9, ROLE_I4, e21));
        ll.add(newUsed(a9, ROLE_H4, e22));
        
        Entity e23 = newFile(pFactory, imgatlas, imglabel);
        Entity e24 = newFile(pFactory, hdratlas, hdrlabel);
        
        ll.addAll(Arrays.asList(a9,e15,e16,e17,e18,e19,e20,e21,e22,e23,e24));

        
        ll.add(newWasGeneratedBy(e23, ROLE_IMG, a9));
        ll.add(newWasGeneratedBy(e24, ROLE_HDR, a9));
        
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
        
        ll.add(pFactory.newAgent(pc(agent)));
        ll.add(pFactory.newActivity(pc(workflow)));
        ll.add(pFactory.newWasAssociatedWith(null,pc(workflow), pc(agent)));
        ll.add(pFactory.newWasStartedBy(null, pc(activity), null, pc(workflow)));
        
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
        
        Activity a10 = pFactory.newActivity(pc(activity));
        pFactory.addType(a10, pFactory.newQualifiedName(PRIM_NS, SLICER,    PRIM_PREFIX), name.PROV_QUALIFIED_NAME);
        
        Entity e23 = pFactory.newEntity(pc(imgatlas));
        Entity e24 = pFactory.newEntity(pc(hdratlas));
        Entity e25p = newParameter(pFactory, params, paramslabel, paramsvalue);
        
        Entity e25 = newFile(pFactory, pgmfile, pgmlabel);
        
        ll.addAll(Arrays.asList(a10,e23,e24,e25p,e25));

        
        ll.add(newUsed(a10, ROLE_IMG, e23));
        ll.add(newUsed(a10, ROLE_HDR, e24));
        ll.add(newUsed(a10, ROLE_PARAM, e25p));
        
        ll.add(newWasGeneratedBy(e25, ROLE_OUT, a10));
        
        ll.add(newWasDerivedFrom(e25, e23));
        ll.add(newWasDerivedFrom(e25, e24));
        ll.add(newWasDerivedFrom(e25, e25p));
        
        ll.add(pFactory.newAgent(pc(agent)));
        ll.add(pFactory.newActivity(pc(workflow)));
        ll.add(pFactory.newWasAssociatedWith(null,pc(workflow), pc(agent)));
        ll.add(pFactory.newWasStartedBy(null, pc(activity), null, pc(workflow)));
        
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

        Activity a13 = pFactory.newActivity(pc(activity));
        pFactory.addType(a13, pFactory.newQualifiedName(PRIM_NS, CONVERT,    PRIM_PREFIX), name.PROV_QUALIFIED_NAME);
        
        Entity e25 = pFactory.newEntity(pc(pgmfile));

        Entity e28 = newFile(pFactory, giffile, giflabel);
        
        ll.add(newUsed(a13, ROLE_IN, e25));
        
        WasGeneratedBy wg18 = newWasGeneratedBy(e28, ROLE_OUT, a13);
        //wg18.setTime(pFactory.newTimeNow());
        
        ll.add(newWasDerivedFrom(e28, e25));
        
        ll.add(pFactory.newAgent(pc(agent)));
        ll.add(pFactory.newActivity(pc(workflow)));
        ll.add(pFactory.newWasAssociatedWith(null,pc(workflow), pc(agent)));
        ll.add(pFactory.newWasStartedBy(null, pc(activity), null, pc(workflow)));
        
        ll.addAll(Arrays.asList(a13,e25,e28,wg18));
        return ll;
        
    }
        
    
    
    public Document makeDocument() {
        Document graph = pFactory.newDocument();
        List<StatementOrBundle> ll=graph.getStatementOrBundle();
        Collection<Collection<StatementOrBundle>> acc=new LinkedList<Collection<StatementOrBundle>>();
        overallWorkflow(acc);      
        for (Collection<StatementOrBundle> col: acc) ll.addAll(col);
        graph.setNamespace(Namespace.gatherNamespaces(graph));
        return graph;
    }
    


    public static void main(String[] args) {
        if (args.length != 2)
            throw new UnsupportedOperationException("main to be called with 2 filenames");
        String file1 = args[0];
        String file2 = args[1];

        ProvenanceChallenge2 pc1 = new ProvenanceChallenge2(InteropFramework.newXMLProvFactory());
        pc1.openingBanner();
        Document document = pc1.makeDocument();
        pc1.doConversions(document, file1);
        pc1.doConversions(document, file2);
        pc1.closingBanner();

    }

}
