package org.openprovenance.prov.tutorial.tutorial6;

import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
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
import org.openprovenance.prov.template.BindingsBean;
import org.openprovenance.prov.template.Expand;
import org.openprovenance.prov.template.Groupings;

import static org.openprovenance.prov.template.ExpandUtil.VAR_NS;
import static org.openprovenance.prov.template.ExpandUtil.TMPL_NS;


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

public class ProvenanceChallenge1Template  extends ChallengeCommon<Collection<BindingsBean>> implements Variables {   



    public ProvenanceChallenge1Template(ProvFactory pFactory) {
        super(pFactory);
    }

    /** Binding variable */
    public QualifiedName b_var(String name) {
        return pFactory.newQualifiedName(VAR_NS, name, "var");
    }

    /** Attribute value */
    public List<TypedValue> a_val(String s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pFactory.newAttribute(pFactory.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.XSD_STRING));
        return ll;
    }
    
    public List<TypedValue> a_val(QualifiedName s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pFactory.newAttribute(pFactory.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.PROV_QUALIFIED_NAME));
        return ll;
    }
    
    
    /** These attributes "partially" instantiate a template for align, with a set of constants */


    public Collection<BindingsBean> align(String imgfile1,    String imglabel, 
                                          String hdrfile1,    String hdrlabel,
                                          String imgreffile1, String imgreflabel, 
                                          String hdrreffile1, String hdrreflabel, 
                                          String activity, 
                                          String warpfile,    String warplabel,
                                          String workflow,    String agent)  {

        AlignBindingsBean bean=new AlignBindingsBean(pFactory);

        bean.addConsumed1(pc(imgfile1));
        bean.addConsumed2(pc(hdrfile1));
        bean.addConsumed3(pc(imgreffile1));
        bean.addConsumed4(pc(hdrreffile1));

        bean.addConsumedLabel1(imglabel);
        bean.addConsumedLabel2(hdrlabel);
        bean.addConsumedLabel3(imgreflabel);
        bean.addConsumedLabel4(hdrreflabel);

        bean.addBlockInstance(pc(activity));

        bean.addProduced(pc(warpfile));
        bean.addProducedLabel(warplabel);

        bean.addParent(pc(workflow));
        bean.addAgent(pc(agent));

        BindingsBean bb=bean;
        return Collections.singleton(bb);
    }

    public Collection<BindingsBean> reslice(String warp, 
                                            String activity, 
                                            String imgfile, String imglabel,
                                            String hdrfile, String hdrlabel,
                                            String workflow, String agent)  {
        ResliceBindingsBean bean=new ResliceBindingsBean(pFactory);

        bean.addConsumed(pc(warp));

        bean.addBlockInstance(pc(activity));

        bean.addProduced1(pc(imgfile));
        bean.addProduced2(pc(hdrfile));
        bean.addProducedLabel1(imglabel);
        bean.addProducedLabel2(hdrlabel);
        bean.addParent(pc(workflow));

        BindingsBean bb=bean;
        return Collections.singleton(bb);

    }
    
    public Collection<BindingsBean> softmean(String imgfile1, String hdrfile1,
                                             String imgfile2, String hdrfile2,
                                             String imgfile3, String hdrfile3,
                                             String imgfile4, String hdrfile4,
                                             String activity, 
                                             String imgatlas, String imglabel,
                                             String hdratlas, String hdrlabel,
                                             String workflow, String agent)  {
        SoftmeanBindingsBean bean=new SoftmeanBindingsBean(pFactory);
        
        bean.addConsumed1(pc(imgfile1));
        bean.addConsumed2(pc(hdrfile1));
        bean.addConsumed3(pc(imgfile2));
        bean.addConsumed4(pc(hdrfile2));
        bean.addConsumed5(pc(imgfile3));
        bean.addConsumed6(pc(hdrfile3));
        bean.addConsumed7(pc(imgfile4));
        bean.addConsumed8(pc(hdrfile4));
        
        bean.addBlockInstance(pc(activity));
        
        bean.addProduced1(pc(imgatlas));
        bean.addProduced2(pc(hdratlas));
        bean.addProducedLabel1(imglabel);
        bean.addProducedLabel2(hdrlabel);
        bean.addParent(pc(workflow));
        
        BindingsBean bb=bean;
        
        return Collections.singleton(bb);

    }
    
    public Collection<BindingsBean> slice(String imgatlas, 
                                          String hdratlas, 
                                          String params, String paramslabel, String paramsvalue,
                                          String activity, 
                                          String pgmfile, String pgmlabel,
                                          String workflow, String agent)  {
        
        SlicerBindingsBean bean=new SlicerBindingsBean(pFactory);
        
        bean.addConsumed1(pc(imgatlas));
        bean.addConsumed2(pc(hdratlas));
        bean.addConsumed3(pc(params));
        
        bean.addConsumedLabel3(paramslabel);
        bean.addConsumedValue3(paramsvalue);

        bean.addBlockInstance(pc(activity));
        
        bean.addProduced(pc(pgmfile));
        bean.addProducedLabel(pgmlabel);
 
        bean.addParent(pc(workflow));
        
        BindingsBean bb=bean;      
        return Collections.singleton(bb);
    }
    
    
    public Collection<BindingsBean> convert(String pgmfile, 
                                        String activity, 
                                        String giffile, String giflabel,
                                        String workflow, String agent)  {
        ConvertBindingsBean bean=new ConvertBindingsBean(pFactory);
        
        bean.addConsumed(pc(pgmfile));

        bean.addBlockInstance(pc(activity));
        
        bean.addProduced(pc(giffile));
        bean.addProducedLabel(giflabel);
 
        bean.addParent(pc(workflow));
        
        BindingsBean bb=bean;      
        return Collections.singleton(bb);
    }
    
    
    public List<BindingsBean> makeBindingsBean() {
        List<BindingsBean> res=new LinkedList<BindingsBean>();
        List<Collection<BindingsBean>> acc=new LinkedList<Collection<BindingsBean>>();
        overallWorkflow(acc);
        for (Collection<BindingsBean> col:acc) {
            if (col!=null) { //TODO: temporary till all beans are created
                res.addAll(col);
            }
        }
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

        doc1.setNamespace(new Namespace());

        return doc1;
    }


    public Document makeDocument(String bind) {
        
        System.out.println("******************");
        
        IndexedDocument iDoc=new IndexedDocument(pFactory, pFactory.newDocument(), true);

        //return makePC1FullGraph(pFactory, URL_LOCATION, URL_LOCATION);
        try {
            
            int count=0;
            for (BindingsBean bb : makeBindingsBean()) {
                InputStream is=getClass().getClassLoader().getResourceAsStream(bb.getTemplate());
                Document doc= new Utility().readDocument(is, pFactory);
                Bindings bindings=bb.getBindings();

                bindings.addVariableBindingsAsAttributeBindings();
                bindings.exportToJson(bind + count + ".json");
                count++;
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
        if (args.length != 3)
            throw new UnsupportedOperationException("main to be called with filename");
        String file1 = args[0];
        String file2 = args[1];
        String bind = args[2];

        ProvenanceChallenge1Template pc1 = new ProvenanceChallenge1Template(InteropFramework.newXMLProvFactory());
        pc1.openingBanner();
        Document document = pc1.makeDocument(bind);
        pc1.doConversions(document, file1);
        pc1.doConversions(document, file2);
        pc1.closingBanner();

    }

}
