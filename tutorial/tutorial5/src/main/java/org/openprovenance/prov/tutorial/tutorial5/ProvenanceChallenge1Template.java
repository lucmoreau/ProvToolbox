package org.openprovenance.prov.tutorial.tutorial5;

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

public class ProvenanceChallenge1Template  extends ChallengeCommon<Collection<Bindings>> implements Variables {   



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
        ll.add(pFactory.newOther(pFactory.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.XSD_STRING));
        return ll;
    }
    
    public List<TypedValue> a_val(QualifiedName s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pFactory.newOther(pFactory.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.PROV_QUALIFIED_NAME));
        return ll;
    }
    
    
    /** These attributes "partially" instantiate a template for align, with a set of constants */
    void align_static(Bindings bindings1) {
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_IMG)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_HDR)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_IMG_REF)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_HDR_REF)));     
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), a_val(prim(FILE)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), a_val(prim(FILE)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), a_val(prim(FILE)));
        bindings1.addAttribute(b_var(VAR_CONSUMED_TYPE), a_val(prim(FILE)));
        
        bindings1.addAttribute(b_var(VAR_BLOCK_TYPE),    a_val(prim(ALIGN_WARP)));
        bindings1.addAttribute(b_var(VAR_PRODUCED_TYPE), a_val(prim(FILE)));
        bindings1.addAttribute(b_var(VAR_PRODUCED_NAME), a_val(prim(ROLE_OUT)));


        
    }

  
    public Collection<Bindings> align(String imgfile1,    String imglabel, 
                                      String hdrfile1,    String hdrlabel,
                                      String imgreffile1, String imgreflabel, 
                                      String hdrreffile1, String hdrreflabel, 
                                      String activity, 
                                      String warpfile,    String warplabel,
                                      String workflow,    String agent)  {
	
        Bindings bindings1=new Bindings(pFactory);
        align_static(bindings1);
        
        bindings1.addVariable(b_var(VAR_CONSUMED),       pc(imgfile1));
        bindings1.addVariable(b_var(VAR_CONSUMED),       pc(hdrfile1));      
        bindings1.addVariable(b_var(VAR_CONSUMED),       pc(imgreffile1));
        bindings1.addVariable(b_var(VAR_CONSUMED),       pc(hdrreffile1));      

        bindings1.addAttribute(b_var(VAR_CONSUMED_LABEL),a_val(imglabel));
        bindings1.addAttribute(b_var(VAR_CONSUMED_LABEL),a_val(hdrlabel));
        bindings1.addAttribute(b_var(VAR_CONSUMED_LABEL),a_val(imgreflabel));
        bindings1.addAttribute(b_var(VAR_CONSUMED_LABEL),a_val(hdrreflabel));
        
        bindings1.addVariable(b_var(VAR_BLOCK_INSTANCE), pc(activity));      
   
        bindings1.addVariable(b_var(VAR_PRODUCED),       pc(warpfile));      
        bindings1.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(warplabel));
        
        bindings1.addVariable(b_var(VAR_PARENT),         pc(workflow));      
        bindings1.addVariable(b_var(VAR_AGENT),          pc(agent));      
        
        return Collections.singleton(bindings1);
    }
    
    public Collection<Bindings> reslice(String warp, 
                                        String activity, 
                                        String imgfile, String imglabel,
                                        String hdrfile, String hdrlabel,
                                        String workflow, String agent)  {
    
        Bindings bindings=new Bindings(pFactory);
        
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(warp));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_IN)));
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), a_val(prim(FILE)));
        
        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), pc(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    a_val(prim(RESLICE)));
        
        bindings.addVariable(b_var(VAR_PRODUCED),       pc(imgfile));      
        bindings.addVariable(b_var(VAR_PRODUCED),       pc(hdrfile));      
        bindings.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(imglabel));
        bindings.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(hdrlabel));
        bindings.addAttribute(b_var(VAR_PRODUCED_NAME),a_val(prim(ROLE_IMG)));
        bindings.addAttribute(b_var(VAR_PRODUCED_NAME),a_val(prim(ROLE_HDR)));
        bindings.addAttribute(b_var(VAR_PRODUCED_TYPE),a_val(prim(FILE)));
        bindings.addAttribute(b_var(VAR_PRODUCED_TYPE),a_val(prim(FILE)));
        
        bindings.addVariable(b_var(VAR_PARENT),         pc(workflow));      
        bindings.addVariable(b_var(VAR_AGENT),          pc(agent));      

        return Collections.singleton(bindings);
    }
    
    public Collection<Bindings> softmean(String imgfile1, String hdrfile1,
                                         String imgfile2, String hdrfile2,
                                         String imgfile3, String hdrfile3,
                                         String imgfile4, String hdrfile4,
                                         String activity, 
                                         String imgatlas, String imglabel,
                                         String hdratlas, String hdrlabel,
                                         String workflow, String agent)  {

        Bindings bindings=new Bindings(pFactory);
        
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(imgfile1));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(hdrfile1));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(imgfile2));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(hdrfile2));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(imgfile3));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(hdrfile3));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(imgfile4));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(hdrfile4));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_I1)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_H1)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_I2)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_H2)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_I3)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_H3)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_I4)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_H4)));
        
        
        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), pc(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    a_val(prim(SOFTMEAN)));

        bindings.addVariable(b_var(VAR_PRODUCED),       pc(imgatlas));      
        bindings.addVariable(b_var(VAR_PRODUCED),       pc(hdratlas));      
        bindings.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(imglabel));
        bindings.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(hdrlabel));
        bindings.addAttribute(b_var(VAR_PRODUCED_NAME),a_val(prim(ROLE_IMG)));
        bindings.addAttribute(b_var(VAR_PRODUCED_NAME),a_val(prim(ROLE_HDR)));
        bindings.addAttribute(b_var(VAR_PRODUCED_TYPE),a_val(prim(FILE)));
        bindings.addAttribute(b_var(VAR_PRODUCED_TYPE),a_val(prim(FILE)));

        bindings.addVariable(b_var(VAR_PARENT),         pc(workflow));      
        bindings.addVariable(b_var(VAR_AGENT),          pc(agent));      
        
        
        return Collections.singleton(bindings);
    }
    
    public Collection<Bindings> slice(String imgatlas, 
                                      String hdratlas, 
                                      String params, String paramslabel, String paramsvalue,
                                      String activity, 
                                      String pgmfile, String pgmlabel,
                                      String workflow, String agent)  {

        Bindings bindings=new Bindings(pFactory);
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(imgatlas));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(hdratlas));
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(params));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_IMG)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_HDR)));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_PARAM))); 
        bindings.addAttribute(b_var(VAR_CONSUMED_LABEL), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_LABEL), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_LABEL), a_val(paramslabel));  
        bindings.addAttribute(b_var(VAR_CONSUMED_VALUE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_VALUE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_VALUE), a_val(paramsvalue));
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), new LinkedList<TypedValue>());
        bindings.addAttribute(b_var(VAR_CONSUMED_TYPE), a_val(prim(STRING)));

        
        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), pc(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    a_val(prim(SLICER)));
        
        bindings.addVariable(b_var(VAR_PRODUCED),       pc(pgmfile));      
        bindings.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(pgmlabel));
        bindings.addAttribute(b_var(VAR_PRODUCED_NAME),a_val(prim(ROLE_OUT)));
        bindings.addAttribute(b_var(VAR_PRODUCED_TYPE),a_val(prim(FILE)));
        
        bindings.addVariable(b_var(VAR_PARENT),         pc(workflow));      
        bindings.addVariable(b_var(VAR_AGENT),          pc(agent));      
        
        return Collections.singleton(bindings);
    }
    
    
    public Collection<Bindings> convert(String pgmfile, 
                                        String activity, 
                                        String giffile, String giflabel,
                                        String workflow, String agent)  {

        Bindings bindings=new Bindings(pFactory);
        
        bindings.addVariable(b_var(VAR_CONSUMED),       pc(pgmfile));
        bindings.addAttribute(b_var(VAR_CONSUMED_NAME), a_val(prim(ROLE_IN)));

        bindings.addVariable(b_var(VAR_BLOCK_INSTANCE), pc(activity));      
        bindings.addAttribute(b_var(VAR_BLOCK_TYPE),    a_val(prim(CONVERT)));

        bindings.addVariable(b_var(VAR_PRODUCED),       pc(giffile));      
        bindings.addAttribute(b_var(VAR_PRODUCED_LABEL),a_val(giflabel));
        bindings.addAttribute(b_var(VAR_PRODUCED_NAME), a_val(prim(ROLE_OUT)));
        bindings.addAttribute(b_var(VAR_PRODUCED_TYPE), a_val(prim(FILE)));

        bindings.addVariable(b_var(VAR_PARENT),         pc(workflow));      
        bindings.addVariable(b_var(VAR_AGENT),          pc(agent));              
        
        return Collections.singleton(bindings);
    }
    
    
    public List<Bindings> makeBindings() {
        List<Bindings> res=new LinkedList<Bindings>();
        List<Collection<Bindings>> acc=new LinkedList<Collection<Bindings>>();
        overallWorkflow(acc);
        for (Collection<Bindings> col:acc) res.addAll(col);
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
            InputStream is=getClass().getClassLoader().getResourceAsStream("template_block.provn");
            Document doc= new Utility().readDocument(is, pFactory);
            
            int count=0;
            for (Bindings bindings : makeBindings()) {
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
