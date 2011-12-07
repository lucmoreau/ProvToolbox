package org.openprovenance.prov.xml;
import java.util.List;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.namespace.QName;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.JAXBElement;
//import org.w3c.dom.Element;

import org.openprovenance.prov.printer.ProvPrinterConfiguration;
import org.openprovenance.prov.printer.AgentMapEntry;
import org.openprovenance.prov.printer.AccountColorMapEntry;
import org.openprovenance.prov.printer.EntityMapEntry;
import org.openprovenance.prov.printer.ActivityMapEntry;
import org.openprovenance.prov.printer.RelationStyleMapEntry;
import org.openprovenance.prov.printer.ProvPrinterConfigDeserialiser;

/** Serialisation of  Prov representation to DOT format. */
public class ProvToDot {
    public final static String DEFAULT_CONFIGURATION_FILE="defaultConfig.xml";
    public final static String DEFAULT_CONFIGURATION_FILE_WITH_ROLE="defaultConfigWithRole.xml";
    public final static String USAGE="prov2dot provFile.xml out.dot out.pdf [configuration.xml]";

    ProvUtilities u=new ProvUtilities();
    ProvFactory of=new ProvFactory();

    public String qnameToString(QName qName) {
        return qName.getLocalPart();
    }

    public static void main(String [] args) throws Exception {
        if ((args==null) || (args.length==0) || (args.length>4)) {
            System.out.println(USAGE);
            return;
        }

        String opmFile=args[0];
        String outDot=args[1];
        String outPdf=args[2];
        String configFile=((args.length==4) ? args[3] : null);

        ProvToDot converter=((configFile==null) ? new ProvToDot() : new ProvToDot(configFile));

        converter.convert(opmFile,outDot,outPdf);
    }


    public ProvToDot() {
        InputStream is=this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE);
        init(is);
    }
    public ProvToDot(boolean withRoleFlag) {
        InputStream is;
        if (withRoleFlag) {
            is=this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE_WITH_ROLE);
        } else {
            is=this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE);
        }
        init(is);
    }

    public ProvToDot(String configurationFile) {
        this();
        init(configurationFile);
    }

    public ProvPrinterConfigDeserialiser getDeserialiser() {
        return ProvPrinterConfigDeserialiser.getThreadProvPrinterConfigDeserialiser();
    }
    
    public void init(String configurationFile) {
        ProvPrinterConfigDeserialiser printerDeserial=getDeserialiser();
        try {
            ProvPrinterConfiguration opc=printerDeserial.deserialiseProvPrinterConfiguration(new File(configurationFile));
            init(opc);
        } catch (JAXBException je) {
            je.printStackTrace();
        }
    }

    public void init(InputStream is) {
        ProvPrinterConfigDeserialiser printerDeserial=getDeserialiser();
        try {
            ProvPrinterConfiguration opc=printerDeserial.deserialiseProvPrinterConfiguration(is);
            init(opc);
        } catch (JAXBException je) {
            je.printStackTrace();
        }
    }

    public void init(ProvPrinterConfiguration configuration) {
        if (configuration==null) return;

        if (configuration.getRelations()!=null) {
            if (configuration.getRelations().getDefault()!=null) {
                defaultRelationStyle=configuration.getRelations().getDefault();
            }
            
            for (RelationStyleMapEntry edge: configuration.getRelations().getRelation()) {
                edgeStyleMap.put(edge.getType(),edge);
            }
        }

        if (configuration.getActivities()!=null) {
            if (configuration.getActivities().isDisplayValue()!=null) {
                this.displayActivityValue=configuration.getActivities().isDisplayValue();
            }
            if (configuration.getActivities().isColoredAsAccount()!=null) {
                this.displayActivityColor=configuration.getActivities().isColoredAsAccount();
            }
            for (ActivityMapEntry process: configuration.getActivities().getActivity()) {
                processNameMap.put(process.getValue(),process.getDisplay());
            }
        }

        if (configuration.getEntities()!=null) {
            if (configuration.getEntities().isDisplayValue()!=null) {
                this.displayEntityValue=configuration.getEntities().isDisplayValue();
            }
            if (configuration.getEntities().isColoredAsAccount()!=null) {
                this.displayEntityColor=configuration.getEntities().isColoredAsAccount();
            }
            for (EntityMapEntry artifact: configuration.getEntities().getEntity()) {
                artifactNameMap.put(artifact.getValue(),artifact.getDisplay());
            }
        }

        if (configuration.getAgents()!=null) {
            if (configuration.getAgents().isDisplayValue()!=null) {
                this.displayAgentValue=configuration.getAgents().isDisplayValue();
            }
            if (configuration.getAgents().isColoredAsAccount()!=null) {
                this.displayAgentColor=configuration.getAgents().isColoredAsAccount();
            }
            for (AgentMapEntry agent: configuration.getAgents().getAgent()) {
                agentNameMap.put(agent.getValue(),agent.getDisplay());
            }
        }

        if (configuration.getAccounts()!=null) {
            if (configuration.getAccounts().getDefaultAccount()!=null) {
                this.defaultAccountLabel=configuration.getAccounts().getDefaultAccount();
            }
            if (configuration.getAccounts().getDefaultColor()!=null) {
                this.defaultAccountColor=configuration.getAccounts().getDefaultColor();
            }
            for (AccountColorMapEntry account: configuration.getAccounts().getAccount()) {
                accountColourMap.put(account.getName(),account.getColor());
            }
        }

        if (configuration.getGraphName()!=null) {
            this.name=configuration.getGraphName();
        }

    }

    public void convert(String opmFile, String dotFile, String pdfFile)
        throws java.io.FileNotFoundException, java.io.IOException, JAXBException {
        convert (ProvDeserialiser.getThreadProvDeserialiser().deserialiseContainer(new File(opmFile)),dotFile,pdfFile);
    }

    public void convert(Container graph, String dotFile, String pdfFile)
        throws java.io.FileNotFoundException, java.io.IOException {
        convert(graph,new File(dotFile));
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process proc = runtime.exec("dot -o " + pdfFile + " -Tpdf " + dotFile);
    }

    public void convert(Container graph, File file) throws java.io.FileNotFoundException{
        OutputStream os=new FileOutputStream(file);
        convert(graph, new PrintStream(os));
    }

    public void convert(Container graph, PrintStream out) {
        List<Relation> edges=u.getRelations(graph);

        prelude(out);

        if (graph.getRecords().getActivity()!=null) {
            for (Activity p: graph.getRecords().getActivity()) {
                emitActivity(p,out);
            }
        }

        if (graph.getRecords().getEntity()!=null) {
            for (Entity p: graph.getRecords().getEntity()) {
                emitEntity(p,out);
            }
        }

        if (graph.getRecords().getAgent()!=null) {
            for (Agent p: graph.getRecords().getAgent()) {
                emitAgent(p,out);
            }
        }

        for (Relation e: edges) {
            if (!(e instanceof HasAnnotation)) 
            emitDependency(e,out);
        }
        

        postlude(out);
       
    }

    boolean collapseAnnotations=true;

    static int embeddedAnnotationCounter=0;

    public void emitAnnotations(HasExtensibility node, PrintStream out) {

    }


    //////////////////////////////////////////////////////////////////////
    ///
    ///                              ELEMENTS
    ///
    //////////////////////////////////////////////////////////////////////

    public void emitActivity(Activity p, PrintStream out) {
        HashMap<String,String> properties=new HashMap();

        emitElement(p.getId(),
                 addActivityShape(p,addActivityLabel(p, addActivityColor(p,properties))),
                 out);

        emitAnnotations(p,out);
    }

    public void emitEntity(Entity a, PrintStream out) {
        HashMap<String,String> properties=new HashMap();

        emitElement(a.getId(),
                 addEntityShape(a,addEntityLabel(a, addEntityColor(a,properties))),
                 out);

        emitAnnotations(a,out);
    }

    public void emitAgent(Agent ag, PrintStream out) {
        HashMap<String,String> properties=new HashMap();

        emitElement(ag.getId(),
                 addAgentShape(ag,addAgentLabel(ag, addAgentColor(ag,properties))),
                 out);

        emitAnnotations(ag,out);

    }

    public void emitAnnotation(String id, Object ann, PrintStream out) {
        // HashMap<String,String> properties=new HashMap();
        // String newId=annotationId(ann.getId(),id);
        // emitElement(newId,
        //          addAnnotationShape(ann,addAnnotationColor(ann,addAnnotationLabel(ann,properties))),
        //          out);
        // HashMap<String,String> linkProperties=new HashMap();
        // emitRelation(newId,id,addAnnotationLinkProperties(ann,linkProperties),out,true);
    }



    int annotationCount=0;
    public String annotationId(String id,String node) {
        if (id==null) {
            return "ann" + node + (annotationCount++);
        } else {
            return id;
        }
    }

    public HashMap<String,String> addAnnotationLinkProperties(Note ann, HashMap<String,String> properties) {
        properties.put("arrowhead","none");
        properties.put("style","dashed");
        properties.put("color","gray");
        return properties;
    }
    
    public HashMap<String,String> addActivityShape(Activity p, HashMap<String,String> properties) {
        properties.put("shape","polygon");
        properties.put("sides","4");
        return properties;
    }

    public HashMap<String,String> addActivityLabel(Activity p, HashMap<String,String> properties) {
        properties.put("label",processLabel(p));
        return properties;
    }

    public HashMap<String,String> addActivityColor(Activity p, HashMap<String,String> properties) {
        if (displayActivityColor) {
            properties.put("color",processColor(p));
            properties.put("fontcolor",processColor(p));
        }
        return properties;
    }

    public HashMap<String,String> addEntityShape(Entity p, HashMap<String,String> properties) {
        // default is good for entity
        return properties;
    }

    public HashMap<String,String> addEntityColor(Entity a, HashMap<String,String> properties) {
        if (displayEntityColor) {
            properties.put("color",entityColor(a));
            properties.put("fontcolor",entityColor(a));
        }
        return properties;
    }

    public HashMap<String,String> addEntityLabel(Entity p, HashMap<String,String> properties) {
        properties.put("label",entityLabel(p));
        return properties;
    }

    public HashMap<String,String> addAgentShape(Agent p, HashMap<String,String> properties) {
        properties.put("shape","polygon");
        properties.put("sides","8");
        return properties;
    }

    public HashMap<String,String> addAgentLabel(Agent p, HashMap<String,String> properties) {
        properties.put("label",agentLabel(p));
        return properties;
    }

    public HashMap<String,String> addAgentColor(Agent a, HashMap<String,String> properties) {
        if (displayAgentColor) {
            properties.put("color",agentColor(a));
            properties.put("fontcolor",agentColor(a));
        }
        return properties;
    }

    public HashMap<String,String> addAnnotationShape(Note ann, HashMap<String,String> properties) {
        properties.put("shape","note");
        return properties;
    }
    public HashMap<String,String> addAnnotationLabel(Note ann, HashMap<String,String> properties) {
        String label="";
        label=label+"<<TABLE cellpadding=\"0\" border=\"0\">\n";
        for (Object prop: ann.getAny()) {
            label=label+"	<TR>\n";
            label=label+"	    <TD align=\"left\">" + convertProperty(prop) + ":</TD>\n";
            label=label+"	    <TD align=\"left\">" + convertValue(prop) + "</TD>\n";
            label=label+"	</TR>\n";
        }
        label=label+"    </TABLE>>\n";
        properties.put("label",label);
        return properties;
    }

   public String convertValue(Object v) {
         String label=""+v;
         int i=label.lastIndexOf("#");
         int j=label.lastIndexOf("/");
         return label.substring(Math.max(i,j)+1, label.length());
     }

    public String convertProperty(Object oLabel) {
        String label=(String)oLabel;
        int i=label.lastIndexOf("#");
        int j=label.lastIndexOf("/");
        return label.substring(Math.max(i,j)+1, label.length());
    }


    public HashMap<String,String> addAnnotationColor(Note ann, HashMap<String,String> properties) {
        if (displayAnnotationColor) {
            properties.put("color",annotationColor(ann));
            properties.put("fontcolor","black");
            //properties.put("style","filled");
        }
        return properties;
    }


    boolean displayActivityValue=false;
    boolean displayActivityColor=false;
    boolean displayEntityValue=false;
    boolean displayEntityColor=false;
    boolean displayAgentColor=false;
    boolean displayAgentValue=false;
    boolean displayAnnotationColor=true;

    public String processLabel(Activity p) {
        if (displayActivityValue) {
            return convertActivityName(""+of.getLabel(p));
        } else {
            return qnameToString(p.getId());
        }
    }
    public String processColor(Activity p) {
        // Note, I should compute effective account membership
        List<String> colors=new LinkedList();
        // for (AccountRef acc: p.getAccount()) {
        //     String accountLabel=((Account)acc.getRef()).getId();
        //     String colour=convertAccount(accountLabel);
        //     colors.add(colour);
        // }

        return selectColor(colors);
    }

    // returns the first non transparent color
    public String selectColor(List<String> colors) {
        String tr="transparent";
        for (String c: colors) {
            if (!(c.equals(tr))) return c;
        }
        return tr;
    }
        
    public String entityLabel(Entity p) {
        if (displayEntityValue) {
            return convertEntityName(""+of.getLabel(p));
        } else {
            return qnameToString(p.getId());
        }
    }
    public String entityColor(Entity p) {
        // Note, I should compute effective account membership
        List<String> colors=new LinkedList();
        // for (AccountRef acc: p.getAccount()) {
        //     String accountLabel=((Account)acc.getRef()).getId();
        //     String colour=convertAccount(accountLabel);
        //     colors.add(colour);
        // }
        return selectColor(colors);
    }
    public String agentColor(Agent p) {
        // Note, I should compute effective account membership
        List<String> colors=new LinkedList();
        // for (AccountRef acc: p.getAccount()) {
        //     String accountLabel=((Account)acc.getRef()).getId();
        //     String colour=convertAccount(accountLabel);
        //     colors.add(colour);
        // }
        return selectColor(colors);
    }


    public String annotationColor(Note ann) {
        List<String> colors=new LinkedList();
        colors.add("gray");
        return selectColor(colors);
    }

    public String agentLabel(Agent p) {
        if (displayAgentValue) {
            return convertAgentName(""+of.getLabel(p));
        } else {
            return qnameToString(p.getId());
        }
    }

    HashMap<String,String> processNameMap=new HashMap<String,String>();
    public String convertActivityName(String process) {
        String name=processNameMap.get(process);
        if (name!=null) return name;
        return process;
    }
    HashMap<String,String> artifactNameMap=new HashMap<String,String>();
    public String convertEntityName(String artifact) {
        String name=artifactNameMap.get(artifact);
        if (name!=null) return name;
        return artifact;
    }
    HashMap<String,String> agentNameMap=new HashMap<String,String>();
    public String convertAgentName(String agent) {
        String name=agentNameMap.get(agent);
        if (name!=null) return name;
        return agent;
    }


    //////////////////////////////////////////////////////////////////////
    ///
    ///                              EDGES
    ///
    //////////////////////////////////////////////////////////////////////

    public void emitDependency(Relation e, PrintStream out) {
        HashMap<String,String> properties=new HashMap();

        // List<AccountRef> accounts=e.getAccount();
        // if (accounts.isEmpty()) {
        //     accounts=new LinkedList();
        //     accounts.add(of.newAccountRef(of.newAccount(defaultAccountLabel)));
        // }
            
        // for (AccountRef acc: accounts) {
        //     String accountLabel=((Account)acc.getRef()).getId();
        //     addRelationAttributes(accountLabel,e,properties);
        emitRelation( qnameToString(u.getEffect(e)),
                      qnameToString(u.getCause(e)),
                      properties,
                      out,
                      true);
            //        }
    }

    public HashMap<String,String> addRelationAttributes(String accountLabel,
                                                    Relation e,
                                                    HashMap<String,String> properties) {
        String colour=convertAccount(accountLabel);
        properties.put("color",colour);
        properties.put("fontcolor",colour);
        properties.put("style",getRelationStyle(e));
        addRelationLabel(e,properties);
        return properties;
    }


    /* Displays type if any, role otherwise. */
    public void addRelationLabel(Relation e, HashMap<String,String> properties) {
        String label=null;
        String type=of.getType(e);
        if (type!=null) {
            label=type;
        } else if (getRelationPrintRole(e)) {
            String role=of.getRole(e);
            if (role!=null) {
                label=displayRole(role);
                properties.put("fontsize","8");
            }
        }
        if (label!=null) {
            properties.put("label",convertRelationLabel(label));
            if (properties.get("fontsize")==null) {
                properties.put("fontsize","10");
            }
        }
    }

    public String displayRole(String role) {
        return "(" + role + ")";
    }

    public String convertRelationLabel(String label) {
        return label.substring(label.indexOf("#")+1, label.length());
    }


    HashMap<String,String> accountColourMap=new HashMap<String,String>();
    public String convertAccount(String account) {
        String colour=accountColourMap.get(account);
        if (colour!=null) return colour;
        return defaultAccountColor;
    }

    String defaultRelationStyle;
    HashMap<String,RelationStyleMapEntry> edgeStyleMap=new HashMap<String,RelationStyleMapEntry>();
    
    public String getRelationStyle(Relation edge) {
        String name=edge.getClass().getName();
        RelationStyleMapEntry style=edgeStyleMap.get(name);
        if (style!=null) return style.getStyle();
        return defaultRelationStyle;
    }

    public boolean getRelationPrintRole(Relation edge) {
        String name=edge.getClass().getName();
        RelationStyleMapEntry style=edgeStyleMap.get(name);
        if (style!=null) {
            Boolean flag=style.isPrintRole();
            if (flag==null) return false;
            return flag;
        } else {
            return false;
        }
    }

    
    //////////////////////////////////////////////////////////////////////
    ///
    ///                              DOT FORMAT GENERATION
    ///
    //////////////////////////////////////////////////////////////////////
    
    
    String name;
    String defaultAccountLabel;
    String defaultAccountColor;


    public void emitElement(QName name, HashMap<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(""+qnameToString(name));
        emitProperties(sb,properties);
        out.println(sb.toString());
    }


    public void emitRelation(String src, String dest, HashMap<String,String> properties, PrintStream out, boolean directional) {
        StringBuffer sb=new StringBuffer();
        sb.append(src);
        if (directional) {
            sb.append(" -> ");
        } else {
            sb.append(" -- ");
        }
        sb.append(dest);
        emitProperties(sb,properties);
        out.println(sb.toString());
    }

    public void emitProperties(StringBuffer sb,HashMap<String,String> properties) {
        sb.append(" [");
        boolean first=true;
        for (String key: properties.keySet()) {
            if (first) {
                first=false;
            } else {
                sb.append(",");
            }
            String value=properties.get(key);
            sb.append(key);
            if (value.startsWith("<")) {
                sb.append("=");
                sb.append(value);
            } else {
                sb.append("=\"");
                sb.append(value);
                sb.append("\"");
            }


        }
        sb.append("]");
    }

    void prelude(PrintStream out) {
        out.println("digraph " + name + " { rankdir=\"BT\"; ");
    }

    void postlude(PrintStream out) {
        out.println("}");
        out.close();
    }


    


    
}


