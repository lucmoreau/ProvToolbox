package org.openprovenance.prov.dot;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.openprovenance.prov.model.Element;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasOther;
import org.openprovenance.prov.model.HasRole;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.HasLabel;
import org.openprovenance.prov.model.HasValue;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.QualifiedRelation;
import org.openprovenance.prov.model.Relation;
import org.openprovenance.prov.model.Role;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.Value;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.xml.ProvDeserialiser;
import org.openprovenance.prov.xml.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInfluencedBy;
import org.openprovenance.prov.model.WasInformedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;


/** Serialisation of  Prov representation to DOT format. */
public class ProvToDot {
    public final static String DEFAULT_CONFIGURATION_FILE="defaultConfig.xml";
    public final static String DEFAULT_CONFIGURATION_FILE_WITH_ROLE="defaultConfigWithRole.xml";
    public final static String DEFAULT_CONFIGURATION_FILE_WITH_ROLE_NO_LABEL="defaultConfigWithRoleNoLabel.xml";

    public final static String USAGE="prov2dot provFile.xml out.dot out.pdf [configuration.xml]";
    public int MAX_TOOLTIP_LENGTH = 2000;

    ProvUtilities u=new ProvUtilities();
    ProvFactory pf=new ProvFactory();

    QualifiedName SUM_SIZE=pf.newQualifiedName(NamespacePrefixMapper.SUMMARY_NS, "size", NamespacePrefixMapper.SUMMARY_PREFIX);

    public String qualifiedNameToString(QualifiedName qName) {
        return qName.getNamespaceURI()+qName.getLocalPart();
    }

    public String localnameToString(QualifiedName qName) {
        //return qName.getLocalPart();
        return nonEmptyLocalName(qName);
    }

    public enum Config { DEFAULT, ROLE, ROLE_NO_LABEL };

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

        converter.convert(opmFile,outDot,outPdf,null);
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
    public ProvToDot(Config config) {
        InputStream is=null;
        switch (config) {
            case DEFAULT:
                System.out.println("ProvToDot DEFAULT");
                is=this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE);
                break;
            case ROLE:
                System.out.println("ProvToDot role");
                is=this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE_WITH_ROLE);
                break;

            case ROLE_NO_LABEL:
                //System.out.println("ProvToDot role no label");
            default:
                //System.out.println("ProvToDot role no label (by default)");
                is=this.getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIGURATION_FILE_WITH_ROLE_NO_LABEL);
                break;
        }
        init(is);
    }

    public ProvToDot(String configurationFile) {
        this();
        init(configurationFile);
    }

    public ProvToDot(String configurationFile, String other) {
        InputStream is=this.getClass().getClassLoader().getResourceAsStream(configurationFile);
        init(is);
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

    public void convert(String opmFile, String dotFile, String pdfFile, String title)
            throws java.io.FileNotFoundException, java.io.IOException, JAXBException {
        convert (ProvDeserialiser.getThreadProvDeserialiser().deserialiseDocument(new File(opmFile)),dotFile,pdfFile,title);
    }

    public void convert(Document graph, String dotFile, String pdfFile, String title)
            throws java.io.FileNotFoundException, java.io.IOException {
        convert(graph,new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        @SuppressWarnings("unused")
        java.lang.Process proc = runtime.exec("dot -o " + pdfFile + " -Tpdf " + dotFile);
    }

    public void convert(Document graph, String dotFile, OutputStream pdfStream, String title)
            throws java.io.FileNotFoundException, java.io.IOException {
        convert(graph,new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process proc = runtime.exec("dot  -Tpdf " + dotFile);
        InputStream is=proc.getInputStream();
        org.apache.commons.io.IOUtils.copy(is, pdfStream);
    }
    public void convert(Document graph, String dotFile, String title)
            throws java.io.FileNotFoundException, java.io.IOException {
        convert(graph,new File(dotFile),title);
    }

    public void convert(Document graph, String dotFile, String aFile, String type, String title)
            throws java.io.FileNotFoundException, java.io.IOException {
        convert(graph,new File(dotFile),title);
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process proc = runtime.exec("dot -o " + aFile + " -T" + type + " " + dotFile);
        try {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String s_error=errorReader.readLine();
            if (s_error!=null) {
                System.out.println("Error:  " + s_error);
            }
            proc.waitFor();
            //System.err.println("exit value " + proc.exitValue());
        } catch (InterruptedException e){};
    }

    public void convert(Document graph, OutputStream os, String type, String title) {

        try {
            File dotFile=File.createTempFile("temp", ".dot");
            convert(graph, dotFile ,title);
            Runtime runtime = Runtime.getRuntime();
            java.lang.Process proc = runtime.exec("dot  -T" + type + " " + dotFile);
            InputStream is=proc.getInputStream();
            org.apache.commons.io.IOUtils.copy(is, os);
            dotFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException(e);
        }



    }

    public void convert(Document graph, String dotFile, OutputStream os, String type, String title)
            throws java.io.FileNotFoundException, java.io.IOException {
        convert(graph,new File(dotFile),title);
        Runtime runtime = Runtime.getRuntime();

        java.lang.Process proc = runtime.exec("dot  -T" + type + " " + dotFile);
        InputStream is=proc.getInputStream();
        org.apache.commons.io.IOUtils.copy(is, os);

    }

    public void convert(Document graph, File file, String title) throws java.io.FileNotFoundException{
        OutputStream os=new FileOutputStream(file);
        convert(graph, new PrintStream(os), title);
    }

    public void convert(Document graph, OutputStream os, String title) {

        convert(graph, new PrintStream(os), title);
    }

    public void convert(Document doc, PrintStream out, String title) {
        if (title!=null) name=title;
        List<Relation> edges=u.getRelations(doc);

        prelude(doc, out);

        if (u.getActivity(doc)!=null) {
            for (Activity p: u.getActivity(doc)) {
                emitActivity(p,out);
            }
        }

        if (u.getEntity(doc)!=null) {
            for (Entity p: u.getEntity(doc)) {
                emitEntity(p,out);
            }
        }

        if (u.getAgent(doc)!=null) {
            for (Agent p: u.getAgent(doc)) {
                emitAgent(p,out);
            }
        }

        if (u.getBundle(doc)!=null) {
            for (Bundle bun: u.getBundle(doc)) {
                convert(bun,out);
            }
        }

        for (Relation e: edges) {
            emitDependency(e,out);
        }


        postlude(doc, out);

    }

    public void convert(Bundle bun, PrintStream out) {
        List<Relation> edges=u.getRelations(bun);

        prelude(bun, out);

        if (u.getActivity(bun)!=null) {
            for (Activity p: u.getActivity(bun)) {
                emitActivity(p,out);
            }
        }

        if (u.getEntity(bun)!=null) {
            for (Entity p: u.getEntity(bun)) {
                emitEntity(p,out);
            }
        }

        if (u.getAgent(bun)!=null) {
            for (Agent p: u.getAgent(bun)) {
                emitAgent(p,out);
            }
        }

        for (Relation e: edges) {
            emitDependency(e,out);
        }


        postlude(bun, out);

    }

    boolean collapseAnnotations=true;

    static int embeddedAnnotationCounter=0;
    public void emitAnnotations(HasOther node, PrintStream out) {

    }



    //////////////////////////////////////////////////////////////////////
    ///
    ///                              ELEMENTS
    ///
    //////////////////////////////////////////////////////////////////////

    public void emitActivity(Activity p, PrintStream out) {
        HashMap<String,String> properties=new HashMap<String, String>();

        emitElement(p.getId(),
                addURL(p.getId(), addActivityShape(p,addActivityLabel(p, addActivityColor(p,properties)))),
                out);

        emitAnnotations("", p,out);
    }


    public void emitEntity(Entity a, PrintStream out) {
        HashMap<String,String> properties=new HashMap<String, String>();

        emitElement(a.getId(),
                addURL(a.getId(), addEntityShape(a,addEntityLabel(a, addEntityColor(a,properties)))),
                out);

        emitAnnotations("", a,out);
    }

    public void emitAgent(Agent ag, PrintStream out) {
        HashMap<String,String> properties=new HashMap<String, String>();

        emitElement(ag.getId(),
                addURL(ag.getId(), addAgentShape(ag,addAgentLabel(ag, addAgentColor(ag,properties)))),
                out);

        emitAnnotations("", ag,out);

    }

    public void emitAnnotations(String id, HasOther statement, PrintStream out) {

        if (((statement.getOther()==null)
                || (statement.getOther().isEmpty())
                || (countOthers(statement)==0))
                &&
                (((HasType)statement).getType().isEmpty())
                &&
                (! (statement instanceof HasValue) || ((HasValue)statement).getValue()==null)
                &&
                (! (statement instanceof HasRole) || ((HasRole)statement).getRole().isEmpty())
                &&
                (((HasLabel)statement).getLabel().isEmpty())
        ) return;

        HashMap<String,String> properties=new HashMap<String, String>();
        QualifiedName newId=annotationId(((Identifiable)statement).getId(),id);
        emitElement(newId,
                addAnnotationShape(statement,addAnnotationColor(statement,addAnnotationLabel(statement,properties))),
                out);
        HashMap<String,String> linkProperties=new HashMap<String, String>();
        emitRelation(qualifiedNameToString(newId),
                qualifiedNameToString(((Identifiable)statement).getId()),
                addAnnotationLinkProperties(statement,linkProperties),out,true);
    }



    int annotationCount=0;
    @SuppressWarnings("unused")
    public QualifiedName annotationId(QualifiedName id,String node) {

        if (true || id==null) {
            return pf.newQualifiedName("-","attrs" + node + (annotationCount++),null);
        } else {
            return id;
        }
    }

    public HashMap<String, String> addURL(QualifiedName id,
                                          HashMap<String, String> properties) {
        if (id!=null) properties.put("URL", htmlify(id.getNamespaceURI()+id.getLocalPart()));
        return properties;
    }


    public HashMap<String,String> addAnnotationLinkProperties(HasOther ann, HashMap<String,String> properties) {
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


    public HashMap<String,String> addBlankNodeShape(HashMap<String,String> properties) {
        properties.put("shape","point");
        properties.put("label","");
        return properties;
    }

    public HashMap<String,String> addActivityLabel(Activity p, HashMap<String,String> properties) {
        properties.put("label",activityLabel(p) + displaySize(p));
        return properties;
    }

    public HashMap<String,String> addActivityColor(Activity p, HashMap<String,String> properties) {
        if (displayActivityColor) {
            properties.put("color",processColor(p));
            properties.put("fontcolor",processColor(p));
        } else {
            properties.put("fillcolor","#9FB1FC"); //blue
            properties.put("color","#0000FF"); //blue
            properties.put("style", "filled");
        }
        addColors(p,properties);
        return properties;
    }

    public  HashMap<String,String> addColors(HasOther object, HashMap<String,String> properties) {
        Hashtable<String,List<Other>> table=u.attributesWithNamespace(object,NamespacePrefixMapper.DOT_NS);

        List<Other> o=table.get("fillcolor");
        if (o!=null && !o.isEmpty()) {
            properties.put("fillcolor", o.get(0).getValue().toString());
            properties.put("style", "filled");
        }
        o=table.get("color");
        if (o!=null && !o.isEmpty()) {
            properties.put("color", o.get(0).getValue().toString());
        }
        o=table.get("url");
        if (o!=null && !o.isEmpty()) {
            properties.put("URL", htmlify(o.get(0).getValue().toString()));
        }
        o=table.get("size");
        if (o!=null && !o.isEmpty()) {
            if ((object instanceof QualifiedRelation)) {
                String val=o.get(0).getValue().toString();
                properties.put("penwidth", val);
            } else {
                if (object instanceof Element) {
                    properties.put("width", "" + Double.valueOf(o.get(0).getValue().toString()) * 0.75);
                }
            }
        }
        o=table.get("tooltip");
        if (o!=null && !o.isEmpty()) {
            String val=o.get(0).getValue().toString();
            if (val.length()>MAX_TOOLTIP_LENGTH) {
                val=val.substring(0,Math.min(val.length(), MAX_TOOLTIP_LENGTH))+" ...";
            }
            properties.put("tooltip", val);
        }
        return properties;
    }



    public HashMap<String,String> addEntityShape(Entity p, HashMap<String,String> properties) {
        // default is good for entity
        List<Type> types=p.getType();
        for (Type type: types) {
            if (type.getValue() instanceof QualifiedName) {
                QualifiedName name=(QualifiedName) type.getValue();
                if (("Dictionary".equals(name.getLocalPart()))
                        ||
                        ("EmptyDictionary".equals(name.getLocalPart()))) {
                    properties.put("shape","folder");
                }
            }
        }
        return properties;
    }

    public HashMap<String,String> addEntityColor(Entity a, HashMap<String,String> properties) {
        if (displayEntityColor) {
            properties.put("color",entityColor(a));
            properties.put("fontcolor",entityColor(a));
        } else {
            properties.put("fillcolor","#FFFC87");//yellow
            properties.put("color","#808080"); //gray
            properties.put("style", "filled");
        }
        addColors(a,properties);
        return properties;
    }

    public HashMap<String,String> addEntityLabel(Entity p, HashMap<String,String> properties) {
        properties.put("label",entityLabel(p) + displaySize(p));
        return properties;
    }

    public String displaySize(HasOther p) {
        for (Other o: p.getOther()) {
            if (SUM_SIZE.equals(o.getElementName())) {
                return " (" + o.getConvertedValue() + ")";
            }
        }
        return "";
    }

    public HashMap<String,String> addAgentShape(Agent p, HashMap<String,String> properties) {
        properties.put("shape","house");
        //properties.put("sides","8");
        return properties;
    }

    public HashMap<String,String> addAgentLabel(Agent p, HashMap<String,String> properties) {
        properties.put("label",agentLabel(p) + displaySize(p));
        return properties;
    }

    public HashMap<String,String> addAgentColor(Agent a, HashMap<String,String> properties) {
        if (displayAgentColor) {
            properties.put("color",agentColor(a));
            properties.put("fontcolor",agentColor(a));
        } else {
            properties.put("fillcolor","#FDB266"); //orange
            properties.put("style", "filled");
        }
        addColors(a,properties);
        return properties;
    }

    public HashMap<String,String> addAnnotationShape(HasOther ann, HashMap<String,String> properties) {
        properties.put("shape","note");
        return properties;
    }

    public HashMap<String,String> addAnnotationLabel(HasOther ann, HashMap<String,String> properties) {

        String label="";
        label=label+"<<TABLE cellpadding=\"0\" border=\"0\">\n";
        for (Type type: ((HasType)ann).getType()) {
            label=label+"	<TR>\n";
            label=label+"	    <TD align=\"left\">" + "type" + ":</TD>\n";
            label=label+"	    <TD align=\"left\">" + getPropertyValueWithUrl(type) + "</TD>\n";  //FIXME: could we have URL in <a></a>?
            label=label+"	</TR>\n";
        }
        for (LangString lab: ((HasLabel)ann).getLabel()) {
            label=label+"	<TR>\n";
            label=label+"	    <TD align=\"left\">" + "label" + ":</TD>\n";
            label=label+"	    <TD align=\"left\">" + htmlify(lab.getValue()) + "</TD>\n";
            label=label+"	</TR>\n";
        }
        if (ann instanceof HasValue) {
            Value val=((HasValue)ann).getValue();
            if (val!=null) {
                label=label+"	<TR>\n";
                label=label+"	    <TD align=\"left\">" + "value" + ":</TD>\n";
                label=label+"	    <TD align=\"left\">" + getPropertyValueWithUrl(val) + "</TD>\n";
                label=label+"	</TR>\n";
            }

        }
        if (ann instanceof HasRole) {
            for (Role role: ((HasRole)ann).getRole()) {
                label=label+"	<TR>\n";
                label=label+"	    <TD align=\"left\">" + "role" + ":</TD>\n";
                label=label+"	    <TD align=\"left\">" + getPropertyValueWithUrl(role) + "</TD>\n";
                label=label+"	</TR>\n";
            }
        }
        for (Other prop: ann.getOther()) {

            if (prop.getElementName().getNamespaceURI().startsWith(NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX)) {
                // no need to display this attribute
                continue;
            }
		
		/*
		if ("fillcolor".equals(prop.getElementName().getLocalPart())) {
		    // no need to display this attribute
		    continue;
		}
		
		if ("size".equals(prop.getElementName().getLocalPart())) {
		    // no need to display this attribute
		    continue;
		}
		if ("tooltip".equals(prop.getElementName().getLocalPart())) {
		    // no need to display this attribute
		    continue;
		}
		*/

            label=label+"	<TR>\n";
            label=label+"	    <TD align=\"left\">" + convertProperty((Attribute)prop) + ":</TD>\n";
            label=label+"	    <TD align=\"left\">" + getPropertyValueWithUrl((Attribute)prop) + "</TD>\n";
            label=label+"	</TR>\n";
        }
        label=label+"    </TABLE>>\n";
        properties.put("label",label);
        properties.put("fontsize","10");

        return properties;
    }

    public int countOthers(HasOther ann) {
        int count=0;
        for (Other obj: ann.getOther()) {
            if (!(obj.getElementName().getNamespaceURI().startsWith(NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX))) {
                count++;
            }
        }
        return count;
    }

    public String convertValue(Attribute v) {
        if (v.getValue() instanceof QualifiedName) {
            QualifiedName name=(QualifiedName) v.getValue();
            return htmlify(nonEmptyLocalName(name));
        }
        String label=getPropertyValueFromAny(v);
        int i=label.lastIndexOf("#");
        int j=label.lastIndexOf("/");
        return htmlify(label.substring(Math.max(i,j)+1, label.length()));
    }

    public String nonEmptyLocalName(QualifiedName name) {
        final String localPart = name.getLocalPart();
        if ("".equals(localPart)) {
            // we are in this case for url finishing with /
            String uri=name.getNamespaceURI();
            String label=uri.substring(0, uri.length()-1);
            int i=label.lastIndexOf("#");
            int j=label.lastIndexOf("/");
            return uri.substring(Math.max(i,j)+1, uri.length());

        } else {
            return localPart;
        }
    }


    public String convertProperty(Attribute oLabel) {
        String label=getPropertyFromAny(oLabel);
        int i=label.lastIndexOf("#");
        int j=label.lastIndexOf("/");
        return label.substring(Math.max(i,j)+1, label.length());
    }

    public String getPropertyFromAny (Attribute o) {
        return o.getElementName().getUri();
    }

    public String getPropertyValueFromAny (Type t) {
        Object val=t.getValue();
        if (val instanceof QualifiedName) {
            QualifiedName q=(QualifiedName)val;
            return q.getNamespaceURI() + q.getLocalPart();
        } else {
            return "" +  val;
        }
    }

    public String getPropertyValueWithUrl (Attribute t) {
        Object val=t.getValue();
        if (val instanceof QualifiedName) {
            QualifiedName q=(QualifiedName)val;
            return htmlify(q.getPrefix() +  ":" + q.getLocalPart());
            //return "<a xlink:href='" + q.getNamespaceURI() + q.getLocalPart() + "'>" + q.getLocalPart() + "</a>";
            //return "&lt;a href=\"" + q.getPrefix() + ":" + q.getLocalPart() + "\"&gt;" + q.getLocalPart() + "&lt;/a&gt;";
        } else {
            return htmlify(""+val);
        }
    }
    public String getPropertyValueFromAny (Attribute o) {
        Object val=o.getValue();
        if (val instanceof QualifiedName) {
            QualifiedName q=(QualifiedName)val;
            return q.getNamespaceURI() + q.getLocalPart();
        } else {
            return "" +  val;
        }
    }


    public HashMap<String,String> addAnnotationColor(HasOther ann, HashMap<String,String> properties) {
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

    public String activityLabel(Activity p) {
        if (displayActivityValue) {
            return convertActivityName(""+pf.getLabel(p));
        } else {
            return localnameToString(p.getId());
        }
    }
    public String processColor(Activity p) {
        // Note, I should compute effective account membership
        List<String> colors=new LinkedList<String>();
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
            return convertEntityName(""+pf.getLabel(p));
        } else {
            return localnameToString(p.getId());
        }
    }

    public String entityColor(Entity p) {
        // Note, I should compute effective account membership
        List<String> colors=new LinkedList<String>();
        // for (AccountRef acc: p.getAccount()) {
        //     String accountLabel=((Account)acc.getRef()).getId();
        //     String colour=convertAccount(accountLabel);
        //     colors.add(colour);
        // }
        return selectColor(colors);
    }
    public String agentColor(Agent p) {
        // Note, I should compute effective account membership
        List<String> colors=new LinkedList<String>();
        // for (AccountRef acc: p.getAccount()) {
        //     String accountLabel=((Account)acc.getRef()).getId();
        //     String colour=convertAccount(accountLabel);
        //     colors.add(colour);
        // }
        return selectColor(colors);
    }


    public String annotationColor(HasOther ann) {
        List<String> colors=new LinkedList<String>();
        colors.add("gray");
        return selectColor(colors);
    }

    public String agentLabel(Agent p) {
        if (displayAgentValue) {
            return convertAgentName(""+pf.getLabel(p));
        } else {
            return localnameToString(p.getId());
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


    int bncounter=0;

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              EDGES
    ///
    //////////////////////////////////////////////////////////////////////

    public void emitDependency(Relation e, PrintStream out) {

        HashMap<String,String> properties=new HashMap<String, String>();

        List<QualifiedName> others=u.getOtherCauses(e);
        if (others !=null) { // n-ary case
            String bnid="bn" + (bncounter++);


            emitBlankNode(dotify(bnid), addBlankNodeShape(properties), out);

            HashMap<String,String> properties2=new HashMap<String, String>();
            properties2.put("arrowhead","none");

            String arrowTail=getArrowShapeForRelation(e);
            if (arrowTail!=null) {
                properties2.put("arrowtail",arrowTail);
                properties2.put("dir","back");
            }
            if (e instanceof HasOther) {
                addColors((HasOther)e,properties2);
            }
            HashMap<String,String> properties3=new HashMap<String, String>();

            QualifiedName effect=u.getEffect(e);
            if (effect!=null) {
                emitRelation( qualifiedNameToString(effect),
                        bnid,
                        properties2,
                        out,
                        true);
            }

            relationName(e, properties3);
            if (e instanceof HasOther) {
                addColors((HasOther)e,properties3);
            }

            if (e instanceof DerivedByInsertionFrom) {
                properties3.put("arrowhead","onormal");
            }

            if (u.getCause(e)!=null) {
                emitRelation( bnid,
                        qualifiedNameToString(u.getCause(e)),
                        properties3,
                        out,
                        true);
            }

            HashMap<String,String> properties4=new HashMap<String, String>();
            if (e instanceof HasOther) {
                addColors((HasOther)e,properties4);
            }
            for (QualifiedName other: others) {
                if (other!=null) {
                    emitRelation( bnid,
                            qualifiedNameToString(other),
                            properties4,
                            out,
                            true);
                }
            }

        } else { // binary case
            if (u.getCause(e)!=null) { // make sure there is a cuase
                relationName(e, properties);
                if (e instanceof QualifiedRelation) {
                    addColors((QualifiedRelation)e,properties);
                }

                String arrowTail=getArrowShapeForRelation(e);
                if (arrowTail!=null) {
                    properties.put("arrowtail",arrowTail);
                    properties.put("dir","both");
                }

                QualifiedName effect=u.getEffect(e);
                QualifiedName cause=u.getCause(e);
                if (effect!=null && cause!=null) {
                    emitRelation( qualifiedNameToString(effect),
                            qualifiedNameToString(cause),
                            properties,
                            out,
                            true);
                }
            }
        }
    }

    void relationName(Relation e, HashMap<String,String> properties) {
        String l=getShortLabelForRelation(e);
        if (l!=null) {
            properties.put("taillabel",l);
            properties.put("labelangle", "60.0");
            properties.put("labeldistance", "1.5");
            properties.put("rotation", "20");
            properties.put("labelfontsize", "8");
        }
    }

    String getArrowShapeForRelation(Relation e) {
        if (e instanceof WasStartedBy)      return "oinv";
        if (e instanceof WasEndedBy)        return "odiamond";
        if (e instanceof WasInvalidatedBy)  return "odiamond";
        return null;
    }


    String getLabelForRelation(Relation e) {
        if (e instanceof Used)              return "used";
        if (e instanceof WasGeneratedBy)    return "wasGeneratedBy";
        if (e instanceof WasDerivedFrom)    return "wasDerivedFrom";
        if (e instanceof WasStartedBy)      return "wasStartedBy";
        if (e instanceof WasEndedBy)        return "wasEndedBy";
        if (e instanceof WasInvalidatedBy)  return "wasInvalidatedBy";
        if (e instanceof WasInformedBy)     return "wasInformedBy";
        if (e instanceof WasAssociatedWith) return "wasAssociatedWith";
        if (e instanceof WasAttributedTo)   return "wasAttributedTo";
        if (e instanceof WasInfluencedBy)   return "wasInfluencedBy";
        if (e instanceof ActedOnBehalfOf)   return "actedOnBehalfOf";
        if (e instanceof SpecializationOf)  return "specializationOf";
        if (e instanceof AlternateOf)       return "alternateOf";
        return null;
    }
    String getShortLabelForRelation(Relation e) {
        if (e instanceof Used)              return "use";
        if (e instanceof WasGeneratedBy)    return "gen";
        if (e instanceof WasDerivedFrom)    return "der";
        if (e instanceof WasStartedBy)      return "start";
        if (e instanceof WasEndedBy)        return "end";
        if (e instanceof WasInvalidatedBy)  return "inv";
        if (e instanceof WasInformedBy)     return "inf";
        if (e instanceof WasAssociatedWith) return "assoc";
        if (e instanceof WasAttributedTo)   return "att";
        if (e instanceof WasInfluencedBy)   return "inf";
        if (e instanceof ActedOnBehalfOf)   return "del";
        if (e instanceof SpecializationOf)  return "spe";
        if (e instanceof AlternateOf)       return "alt";
        return null;
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
    public void addRelationLabel(Relation e0, HashMap<String,String> properties) {
        String label=null;
        if (!(e0 instanceof QualifiedRelation)) return;
        QualifiedRelation e=(QualifiedRelation)e0;
        List<Type> type=pf.getType(e);
        if ((type!=null) && (!type.isEmpty())) {
            label=type.get(0).getValue().toString();
        } else if (getRelationPrintRole(e)) {
            String role=pf.getRole(e);
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
    private String layout;

    /* make name compatible with dot notation*/

    public String OLDdotify(String name) {
        return "n" + name.replace('-','_').replace('.','_').replace('/','_').replace(':','_').replace('#','_').replace('~','_').replace('&','_').replace('=','_').replace('?','_');
    }
    public String dotify(String name) {
        //return name.replace('-','_').replace('.','_').replace('/','_').replace(':','_').replace('#','_').replace('~','_').replace("&","&amp;").replace('=','_').replace('?','_');
        //return htmlify(name);
        return "\"" + name + "\"";
    }

    public String htmlify(String name) {
        return name.replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;");
    }

    public void emitElement(QualifiedName name, HashMap<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(""+dotify(qualifiedNameToString(name)));
        emitProperties(sb,properties);
        out.println(sb.toString());
    }


    public void emitBlankNode(String bnid, HashMap<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(bnid);
        emitProperties(sb,properties);
        out.println(sb.toString());
    }


    public void emitRelation(String src, String dest, HashMap<String,String> properties, PrintStream out, boolean directional) {
        StringBuffer sb=new StringBuffer();
        sb.append(dotify(src));
        if (directional) {
            sb.append(" -> ");
        } else {
            sb.append(" -- ");
        }
        sb.append(dotify(dest));
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

    void prelude(Document doc, PrintStream out) {
        out.println("digraph \"" + name + "\" { size=\"16,12\"; rankdir=\"BT\"; ");
        if (layout!=null) {
            out.println("layout=\"" + layout + "\"; ");
        }
    }

    void postlude(Document doc, PrintStream out) {
        out.println("}");
        out.close();
    }

    void prelude(Bundle doc, PrintStream out) {
        out.println("subgraph " + dotify("cluster" + qualifiedNameToString(doc.getId())) + " { ");
        out.println("  label=\"" + localnameToString(doc.getId()) + "\";");
        out.println("  URL=\"" + qualifiedNameToString(doc.getId()) + "\";");
    }

    void postlude(Bundle doc, PrintStream out) {
        out.println("}");
    }

    public void setLayout(String layout) {
        this.layout=layout;

    }






}


