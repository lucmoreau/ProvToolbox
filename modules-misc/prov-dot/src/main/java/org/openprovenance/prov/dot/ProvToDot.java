package org.openprovenance.prov.dot;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

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
import org.openprovenance.prov.model.ProvFactory;
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

import static java.lang.Math.min;


/** Serialisation of  Prov representation to DOT format. */
public class ProvToDot {

    public int MAX_TOOLTIP_LENGTH = 2000;

    ProvUtilities u=new ProvUtilities();
    final ProvFactory pf;
    QualifiedName makeSumSize(){
        return pf.newQualifiedName(NamespacePrefixMapper.SUMMARY_NS, "size", NamespacePrefixMapper.SUMMARY_PREFIX);
    }

    final QualifiedName SUM_SIZE;

    public String qualifiedNameToString(QualifiedName qName) {
        return qName.getNamespaceURI()+qName.getLocalPart();
    }

    public String localnameToString(QualifiedName qName) {
        return nonEmptyLocalName(qName);
    }


    private Integer maxStringLength=null;

    public void setMaxStringLength(Integer maxStringLength) {
        this.maxStringLength = maxStringLength;
    }


    public ProvToDot(ProvFactory pf) {
        this.pf=pf;
        SUM_SIZE=makeSumSize();
    }


    public void convert(Document graph, String dotFile, String pdfFile, String title)
            throws java.io.IOException {
        convert(graph,new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        @SuppressWarnings("unused")
        java.lang.Process proc = runtime.exec("dot -o " + pdfFile + " -Tpdf " + dotFile);
    }

    public void convert(Document graph, String dotFile, OutputStream pdfStream, String title)
            throws java.io.IOException {
        convert(graph,new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        java.lang.Process proc = runtime.exec("dot  -Tpdf " + dotFile);
        InputStream is=proc.getInputStream();
        org.apache.commons.io.IOUtils.copy(is, pdfStream);
    }
    public void convert(Document graph, String dotFile, String title)
            throws java.io.IOException {
        convert(graph,new File(dotFile),title);
    }

    public void convert(Document graph, String dotFile, String aFile, String type, String title)
            throws java.io.IOException {
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
        } catch (InterruptedException e){}
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
            throws java.io.IOException {
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

        if (true) {
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
        properties.put("fillcolor", "#9FB1FC"); //blue
        properties.put("color","#0000FF"); //blue
        properties.put("style", "filled");
        addColors(p,properties);
        return properties;
    }

    public String getStringValue(Other o) {
        Object v=o.getValue();
        if (v instanceof LangString) {
            return ((LangString) v).getValue();
        } else {
            return v.toString();
        }
    }

    public  HashMap<String,String> addColors(HasOther object, HashMap<String,String> properties) {
        Hashtable<String,List<Other>> table=u.attributesWithNamespace(object,NamespacePrefixMapper.DOT_NS);

        List<Other> o=table.get("fillcolor");
        if (o!=null && !o.isEmpty()) {
            properties.put("fillcolor", getStringValue(o.get(0)));
            properties.put("style", "filled");
        }
        o=table.get("color");
        if (o!=null && !o.isEmpty()) {
            properties.put("color", getStringValue(o.get(0)));
        }
        o=table.get("url");
        if (o!=null && !o.isEmpty()) {
            properties.put("URL", htmlify(getStringValue(o.get(0))));
        }
        o=table.get("size");
        if (o!=null && !o.isEmpty()) {
            if ((object instanceof QualifiedRelation)) {
                String val=o.get(0).getValue().toString();
                properties.put("penwidth", val);
            } else {
                if (object instanceof Element) {
                    properties.put("width", "" + Double.valueOf(getStringValue(o.get(0))) * 0.75);
                }
            }
        }
        o=table.get("tooltip");
        if (o!=null && !o.isEmpty()) {
            String val=getStringValue(o.get(0));
            if (val.length()>MAX_TOOLTIP_LENGTH) {
                val=val.substring(0, min(val.length(), MAX_TOOLTIP_LENGTH))+" ...";
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
        properties.put("fillcolor", "#FFFC87");//yellow
        properties.put("color","#808080"); //gray
        properties.put("style", "filled");
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
        return properties;
    }

    public HashMap<String,String> addAgentLabel(Agent p, HashMap<String,String> properties) {
        properties.put("label",agentLabel(p) + displaySize(p));
        return properties;
    }

    public HashMap<String,String> addAgentColor(Agent a, HashMap<String,String> properties) {
        properties.put("fillcolor", "#FDB266"); //orange
        properties.put("style", "filled");
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
            label=label+"	    <TD align=\"left\">" + htmlify(lab.getValue(), true) + "</TD>\n";
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


            label=label+"	<TR>\n";
            label=label+"	    <TD align=\"left\">" + convertProperty(prop) + ":</TD>\n";
            label=label+"	    <TD align=\"left\">" + getPropertyValueWithUrl(prop) + "</TD>\n";
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



    public String nonEmptyLocalName(QualifiedName name) {
        final String localPart = name.getLocalPart();
        if ("".equals(localPart)) {
            // we are in this case for url finishing with /
            String uri=name.getNamespaceURI();
            String label=uri.substring(0, uri.length()-1);
            int i=label.lastIndexOf("#");
            int j=label.lastIndexOf("/");
            return uri.substring(Math.max(i,j)+1);

        } else {
            return localPart;
        }
    }


    public String convertProperty(Attribute oLabel) {
        String label=getPropertyFromAny(oLabel);
        int i=label.lastIndexOf("#");
        int j=label.lastIndexOf("/");
        return label.substring(Math.max(i,j)+1);
    }

    public String getPropertyFromAny (Attribute o) {
        return o.getElementName().getUri();
    }


    public String getPropertyValueWithUrl (Attribute t) {
        Object val=t.getValue();
        if (val instanceof QualifiedName) {
            QualifiedName q=(QualifiedName)val;
            return htmlify(q.getPrefix() +  ":" + q.getLocalPart(), true);
        } if (val instanceof LangString) {
            LangString ls=(LangString)val;
            if (ls.getLang()==null) {
                return htmlify(ls.getValue(), true);
            } else {
                return htmlify(ls.getValue(), true) + "@" + ls.getLang();

            }
        } else {
            return htmlify(""+val, true);
        }
    }



    public HashMap<String,String> addAnnotationColor(HasOther ann, HashMap<String,String> properties) {
        if (displayAnnotationColor) {
            properties.put("color",annotationColor(ann));
            properties.put("fontcolor","black");
        }
        return properties;
    }



    boolean displayAnnotationColor=true;

    public String activityLabel(Activity p) {
        return localnameToString(p.getId());
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
        return localnameToString(p.getId());
    }


    public String agentColor(Agent p) {
        List<String> colors= new LinkedList<>();
        return selectColor(colors);
    }


    public String annotationColor(HasOther ann) {
        List<String> colors=new LinkedList<String>();
        colors.add("gray");
        return selectColor(colors);
    }

    public String agentLabel(Agent p) {
        return localnameToString(p.getId());
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


    //////////////////////////////////////////////////////////////////////
    ///
    ///                              DOT FORMAT GENERATION
    ///
    //////////////////////////////////////////////////////////////////////


    String name;
    private String layout;

    /* make name compatible with dot notation*/

    public String dotify(String name) {
        //return name.replace('-','_').replace('.','_').replace('/','_').replace(':','_').replace('#','_').replace('~','_').replace("&","&amp;").replace('=','_').replace('?','_');
        //return htmlify(name);
        return "\"" + name + "\"";
    }

    boolean ellipsis=true;

    public String htmlify(String name) {
        return htmlify(name,false);
    }

    public String htmlify(String name, boolean truncate) {
        if (truncate && (maxStringLength != null) && (name.length() > maxStringLength)) {
            name = name.substring(0, maxStringLength);
            if (ellipsis) {
                name = name + "...";
            }
        }
        return name
                .replace("&","&amp;")
                .replace("<","&lt;")
                .replace(">","&gt;");
    }

    public void emitElement(QualifiedName name, HashMap<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(dotify(qualifiedNameToString(name)));
        emitProperties(sb,properties);
        out.println(sb);
    }


    public void emitBlankNode(String bnid, HashMap<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(bnid);
        emitProperties(sb,properties);
        out.println(sb);
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
        out.println(sb);
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
        out.println("digraph \"" + name + "\" { rankdir=\"BT\"; ");  //size="16,12"; 
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


