package org.openprovenance.prov.dot;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.DocumentedUnsupportedCaseException;
import org.openprovenance.prov.model.exception.UncheckedException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.vanilla.QualifiedSpecializationOf;

/** Serialisation of  Prov representation to DOT format. */
public class ProvToDot implements DotProperties,  RecommendedProvVisualProperties, ProvShorthandNames {

    Logger logger=LogManager.getLogger(ProvToDot.class);

    public int MAX_TOOLTIP_LENGTH = 2000;


    final Supplier<ProvUtilities> getProvUtilities;
    final ProvUtilities u;



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
        this.getProvUtilities= ProvUtilities::new;
        this.u=this.getProvUtilities.get();
    }
    public ProvToDot(ProvFactory pf, Supplier<ProvUtilities> getProvUtilities) {
        this.pf=pf;
        SUM_SIZE=makeSumSize();
        this.getProvUtilities= getProvUtilities;
        this.u= this.getProvUtilities.get();
    }



    public void convert(Document graph, String dotFile, String pdfFile, String title) throws IOException {
        convert(graph,new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        @SuppressWarnings("unused")
        Process proc = runtime.exec("dot -o " + pdfFile + " -Tpdf " + dotFile);
    }

    public void convert(Document graph, String dotFile, OutputStream pdfStream, String title)
            throws IOException {
        convert(graph,new File(dotFile), title);
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("dot  -Tpdf " + dotFile);
        InputStream is=proc.getInputStream();
        IOUtils.copy(is, pdfStream);
    }
    public void convert(Document graph, String dotFile, String title)
            throws IOException {
        convert(graph,new File(dotFile),title);
    }

    public void convert(Document graph, String dotFile, String aFile, String type, String title)
            throws IOException {
        convert(graph,new File(dotFile),title);
        Runtime runtime = Runtime.getRuntime();
        Process proc = runtime.exec("dot -o " + aFile + " -T" + type + " " + dotFile);
        try {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            String s_error=errorReader.readLine();
            if (s_error!=null) {
                System.out.println("Error:  " + s_error);
            }
            proc.waitFor();
        } catch (InterruptedException ie){
            throw new UncheckedException("convert exception", ie);
        }
    }

    public void convert(Document graph, OutputStream os, String type, String title) {

        try {
            File dotFile=File.createTempFile("temp", ".dot");
            convert(graph, dotFile ,title);
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("dot  -T" + type + " " + dotFile);
            InputStream is=proc.getInputStream();
            IOUtils.copy(is, os);
            @SuppressWarnings("unused")
            boolean resultCode=dotFile.delete();

        } catch (IOException e) {
            logger.throwing(e);
            throw new UncheckedException(e);
        }



    }

    public void convert(Document graph, String dotFile, OutputStream os, String type, String title)
            throws IOException {
        convert(graph,new File(dotFile),title);
        Runtime runtime = Runtime.getRuntime();

        Process proc = runtime.exec("dot  -T" + type + " " + dotFile);
        InputStream is=proc.getInputStream();
        IOUtils.copy(is, os);

    }

    public void convert(Document graph, File file, String title) throws FileNotFoundException{
        OutputStream os=new FileOutputStream(file);
        convert(graph, new PrintStream(os), title);
    }

    public void convert(Document graph, OutputStream os, String title) {

        convert(graph, new PrintStream(os), title);
    }

    public void convert(Document doc, PrintStream out, String title) {
        if (title!=null) name=title;

        prelude(doc, out);

        List<Relation> edges=u.getRelations(doc);

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

        for (Relation e: edges) {
            emitDependency(e,out);
        }

        if (u.getBundle(doc)!=null) {
            for (Bundle bun: u.getBundle(doc)) {
                convert(bun,out);
            }
        }


        postlude(doc, out);

    }

    public void convert(Bundle bun, PrintStream out) {

        prelude(bun, out);

        List<Relation> edges=u.getRelations(bun);

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


    //////////////////////////////////////////////////////////////////////
    ///
    ///                              ELEMENTS
    ///
    //////////////////////////////////////////////////////////////////////

    public void emitActivity(Activity p, PrintStream out) {
        Map<String,String> properties= new HashMap<>();

        emitSpace(p.getId(),out);

        emitElement(p.getId(),
                p.getKind(),
                addURL(p.getId(), addActivityShape(p,addActivityLabel(p, addActivityColor(p,properties)))),
                out);

        emitAnnotations("", p,out);
    }



    public void emitEntity(Entity e, PrintStream out) {
        emitSpace(e.getId(),out);

        Map<String,String> properties= new HashMap<>();

        emitElement(e.getId(),
                e.getKind(),
                addURL(e.getId(), addEntityShape(e,addEntityLabel(e, addEntityColor(e,properties)))),
                out);

        emitAnnotations("", e,out);
    }

    public void emitAgent(Agent ag, PrintStream out) {
        emitSpace(ag.getId(),out);

        Map<String,String> properties= new HashMap<>();

        emitElement(ag.getId(),
                ag.getKind(),
                addURL(ag.getId(), addAgentShape(ag,addAgentLabel(ag, addAgentColor(ag,properties)))),
                out);

        emitAnnotations("", ag,out);

    }

    private void emitSpace(QualifiedName ignoredId, PrintStream out) {
        out.println();
    }

    private void emitComment(QualifiedName id, StatementOrBundle.Kind kind, StringBuffer out) {
        String prefix = (id==null)? "_" : id.getPrefix();
        String localPart =  (id==null)? "_" : id.getLocalPart();
        out.append(" [comment=\"").append(kind).append(" ").append(prefix).append(":").append(localPart).append("\"]");
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
                (! (statement instanceof HasLocation) || ((HasLocation)statement).getLocation().isEmpty())
                &&
                (((HasLabel)statement).getLabel().isEmpty())
        ) return;

        Map<String,String> properties= new HashMap<>();
        QualifiedName newId=annotationId(((Identifiable)statement).getId(),id);
        emitElement(newId,
                null,
                addAnnotationShape(statement,addAnnotationColor(statement,addAnnotationLabel(statement,properties))),
                out);
        Map<String,String> linkProperties= new HashMap<>();
        emitRelation(null,
                qualifiedNameToString(newId),
                qualifiedNameToString(((Identifiable)statement).getId()),
                addAnnotationLinkProperties(statement,linkProperties),out,true);
    }



    int annotationCount=0;

    public QualifiedName annotationId(QualifiedName ignoredId, String node) {
        return pf.newQualifiedName("-", "attrs" + node + (annotationCount++), null);
    }

    public Map<String, String> addURL(QualifiedName id,
                                      Map<String, String> properties) {
        if (id!=null) properties.put("URL", htmlify(id.getNamespaceURI()+id.getLocalPart()));
        return properties;
    }


    public Map<String,String> addAnnotationLinkProperties(HasOther ignoredAnn, Map<String,String> properties) {
        properties.put(DOT_ARROWHEAD,"none");
        properties.put(DOT_STYLE,"dashed");
        properties.put(DOT_COLOUR,"gray");
        return properties;
    }

    public Map<String,String> addActivityShape(Activity ignoredActivity, Map<String,String> properties) {
        properties.put(DOT_SHAPE, ACTIVITY_SHAPE);
        properties.put(DOT_SIDES, ACTIVITY_SIDES);
        return properties;
    }


    public Map<String,String> addBlankNodeShape(Map<String,String> properties) {
        properties.put(DOT_SHAPE,"point");
        properties.put(DOT_LABEL,"");
        return properties;
    }

    public Map<String,String> addActivityLabel(Activity p, Map<String,String> properties) {
        properties.put(DOT_LABEL,activityLabel(p) + displaySize(p));
        return properties;
    }

    public Map<String,String> addActivityColor(Activity p, Map<String,String> properties) {
        properties.put(DOT_FILLCOLOUR, ACTIVITY_FILL_COLOUR);
        properties.put(DOT_COLOUR, ACTIVITY_COLOUR);
        properties.put(DOT_STYLE, ACTIVITY_STYLE);
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

    public void addColors(HasOther object, Map<String,String> properties) {
        Hashtable<String,List<Other>> table=u.attributesWithNamespace(object,NamespacePrefixMapper.DOT_NS);

        List<Other> o=table.get(DOT_FILLCOLOUR);
        if (o!=null && !o.isEmpty()) {
            properties.put(DOT_FILLCOLOUR, getStringValue(o.get(0)));
            properties.put(DOT_STYLE, ACTIVITY_STYLE);
        }
        o=table.get(DOT_COLOUR);
        if (o!=null && !o.isEmpty()) {
            properties.put(DOT_COLOUR, getStringValue(o.get(0)));
        }
        o=table.get(DOT_URL);
        if (o!=null && !o.isEmpty()) {
            properties.put("URL", htmlify(getStringValue(o.get(0))));
        }
        o=table.get("size");
        if (o!=null && !o.isEmpty()) {
            if ((object instanceof QualifiedRelation)) {
                String val=o.get(0).getValue().toString();
                properties.put(DOT_PENWIDTH, val);
            } else {
                if (object instanceof Element) {
                    properties.put(DOT_WIDTH, "" + Double.parseDouble(getStringValue(o.get(0))) * 0.75);
                }
            }
        }
        o=table.get(DOT_TOOLTIP);
        if (o!=null && !o.isEmpty()) {
            String val=getStringValue(o.get(0));
            if (val.length()>MAX_TOOLTIP_LENGTH) {
                val=val.substring(0, MAX_TOOLTIP_LENGTH)+" ...";
            }
            properties.put(DOT_TOOLTIP, val);
        }
    }



    public Map<String,String> addEntityShape(Entity p, Map<String,String> properties) {
        // default is good for entity
        List<Type> types=p.getType();
        for (Type type: types) {
            if (type.getValue() instanceof QualifiedName) {
                QualifiedName name=(QualifiedName) type.getValue();
                if (("Dictionary".equals(name.getLocalPart()))
                        ||
                        ("EmptyDictionary".equals(name.getLocalPart()))) {
                    properties.put(DOT_SHAPE,"folder");
                }
            }
        }
        return properties;
    }

    public Map<String,String> addEntityColor(Entity a, Map<String,String> properties) {
        properties.put(DOT_FILLCOLOUR, ENTITY_FILLCOLOUR);
        properties.put(DOT_COLOUR, ENTITY_COLOUR);
        properties.put(DOT_STYLE, ENTITY_STYLE);
        addColors(a,properties);
        return properties;
    }

    public Map<String,String> addEntityLabel(Entity p, Map<String,String> properties) {
        properties.put(DOT_LABEL,entityLabel(p) + displaySize(p));
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

    public Map<String,String> addAgentShape(Agent ignoredAgent, Map<String,String> properties) {
        properties.put(DOT_SHAPE, AGENT_SHAPE);
        return properties;
    }

    public Map<String,String> addAgentLabel(Agent agent, Map<String,String> properties) {
        properties.put(DOT_LABEL,agentLabel(agent) + displaySize(agent));
        return properties;
    }

    public Map<String,String> addAgentColor(Agent agent, Map<String,String> properties) {
        properties.put(DOT_FILLCOLOUR, AGENT_FILLCOLOUR);
        properties.put(DOT_STYLE, AGENT_STYLE);
        addColors(agent,properties);
        return properties;
    }

    public Map<String,String> addAnnotationShape(HasOther ignoredAnn, Map<String,String> properties) {
        properties.put(DOT_SHAPE, ANNOTATION_SHAPE);
        return properties;
    }

    public Map<String,String> addAnnotationLabel(HasOther ann, Map<String,String> properties) {

        StringBuilder label= new StringBuilder();
        label.append("<<TABLE cellpadding=\"0\" border=\"0\">\n");
        for (Type type: ((HasType)ann).getType()) {
            label.append("	<TR>\n");
            label.append("	    <TD align=\"left\">").append("type").append(":</TD>\n");
            label.append("	    <TD align=\"left\">").append(getPropertyValueWithUrl(type)).append("</TD>\n");  //FIXME: could we have URL in <a></a>?
            label.append("	</TR>\n");
        }
        for (LangString lab: ((HasLabel)ann).getLabel()) {
            label.append("	<TR>\n");
            label.append("	    <TD align=\"left\">").append(DOT_LABEL).append(":</TD>\n");
            label.append("	    <TD align=\"left\">").append(htmlify(lab.getValue(), true)).append("</TD>\n");
            label.append("	</TR>\n");
        }
        if (ann instanceof HasValue) {
            Value val=((HasValue)ann).getValue();
            if (val!=null) {
                label.append("	<TR>\n");
                label.append("	    <TD align=\"left\">").append("value").append(":</TD>\n");
                label.append("	    <TD align=\"left\">").append(getPropertyValueWithUrl(val)).append("</TD>\n");
                label.append("	</TR>\n");
            }

        }
        if (ann instanceof HasRole) {
            for (Role role: ((HasRole)ann).getRole()) {
                label.append("	<TR>\n");
                label.append("	    <TD align=\"left\">").append("role").append(":</TD>\n");
                label.append("	    <TD align=\"left\">").append(getPropertyValueWithUrl(role)).append("</TD>\n");
                label.append("	</TR>\n");
            }
        }
        if (ann instanceof HasLocation) {
            for (Location location: ((HasLocation)ann).getLocation()) {
                label.append("	<TR>\n");
                label.append("	    <TD align=\"left\">").append("location").append(":</TD>\n");
                label.append("	    <TD align=\"left\">").append(getPropertyValueWithUrl(location)).append("</TD>\n");
                label.append("	</TR>\n");
            }
        }
        for (Other prop: ann.getOther()) {

            if (prop.getElementName().getNamespaceURI().startsWith(NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX)) {
                // no need to display this attribute
                continue;
            }


            label.append("	<TR>\n");
            label.append("	    <TD align=\"left\">").append(convertProperty(prop)).append(":</TD>\n");
            label.append("	    <TD align=\"left\">").append(getPropertyValueWithUrl(prop)).append("</TD>\n");
            label.append("	</TR>\n");
        }
        label.append("    </TABLE>>\n");
        properties.put(DOT_LABEL, label.toString());
        properties.put(DOT_FONTSIZE,"10");

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



    public Map<String,String> addAnnotationColor(HasOther ann, Map<String,String> properties) {
        if (displayAnnotationColor) {
            properties.put(DOT_COLOUR,annotationColor(ann));
            properties.put(DOT_FONTCOLOUR,"black");
        }
        return properties;
    }



    boolean displayAnnotationColor=true;

    public String activityLabel(Activity p) {
        return localnameToString(p.getId());
    }


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


    public String annotationColor(HasOther ignoredAnn) {
        List<String> colors= new LinkedList<>();
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


        Map<String,String> properties= new HashMap<>();
        emitSpace(null,out);

        List<QualifiedName> others=u.getOtherCauses(e);
        if (others !=null) { // n-ary case

            // if relation e has id
            String id=null;
            if (e instanceof Identifiable && ((Identifiable)e).getId()!=null) {
                id=qualifiedNameToString(((Identifiable)e).getId());
            }
            String bnid=(id==null)? "bn" + (bncounter++) : id;


            emitBlankNode(dotify(bnid), addBlankNodeShape(properties), out);

            Map<String,String> properties2= new HashMap<>();
            properties2.put(DOT_ARROWHEAD,"none");

            String arrowTail=getArrowShapeForRelation(e);
            if (arrowTail!=null) {
                properties2.put(DOT_ARROWTAIL,arrowTail);
                properties2.put(DOT_DIR,"back");
            }
            if (e instanceof HasOther) {
                addColors((HasOther)e,properties2);
            }
            Map<String,String> properties3= new HashMap<>();

            QualifiedName effect=u.getEffect(e);
            if (effect!=null) {
                emitRelation( e.getKind(),
                        qualifiedNameToString(effect),
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
                properties3.put(DOT_ARROWHEAD,"onormal");
            }

            if (u.getCause(e)!=null) {
                emitRelation(  e.getKind(),
                        bnid,
                        qualifiedNameToString(u.getCause(e)),
                        properties3,
                        out,
                        true);
            }

            HashMap<String,String> properties4= new HashMap<>();
            if (e instanceof HasOther) {
                addColors((HasOther)e,properties4);
            }
            properties4.put(DOT_STYLE,"dashed");
            properties4.put(DOT_ARROWHEAD,"none");
            properties4.put(DOT_COLOUR,"gray");

            for (QualifiedName other: others) {
                if (other!=null) {
                    emitRelation(  e.getKind(),
                            bnid,
                            qualifiedNameToString(other),
                            properties4,
                            out,
                            true);
                }
            }
            emitSpace(null,out);

        } else { // binary case
            if (u.getCause(e)!=null) { // make sure there is a cause
                relationName(e, properties);
                if (e instanceof QualifiedRelation) {
                    addColors((QualifiedRelation)e,properties);
                }

                String arrowTail=getArrowShapeForRelation(e);
                if (arrowTail!=null) {
                    properties.put(DOT_ARROWTAIL,arrowTail);
                    properties.put(DOT_DIR,"both");
                }

                QualifiedName effect=u.getEffect(e);
                QualifiedName cause=u.getCause(e);
                if (effect!=null && cause!=null) {
                    emitRelation(  e.getKind(),
                            qualifiedNameToString(effect),
                            qualifiedNameToString(cause),
                            properties,
                            out,
                            true
                    );
                }
            }
        }
    }

    void relationName(Relation e, Map<String,String> properties) {
        String l=getShortLabelForRelation(e);
        if (l!=null) {
            properties.put(DOT_TAILLABEL,l);
            properties.put(DOT_LABELANGLE, "60.0");
            properties.put(DOT_LABELDISTANCE, "1.5");
            properties.put(DOT_ROTATION, "20");
            properties.put(DOT_LABELFONTSIZE, "8");
        }
    }

    String getArrowShapeForRelation(Relation e) {
        if (e instanceof WasStartedBy)      return DOT_ARROWSHAPE_OINV;
        if (e instanceof WasEndedBy)        return DOT_ARROWSHAPE_ODIAMOND;
        if (e instanceof WasInvalidatedBy)  return DOT_ARROWSHAPE_ODIAMOND;
        return null;
    }


    String getShortLabelForRelation(Relation e) {
        switch (e.getKind()) {
            case PROV_ENTITY:
            case PROV_ACTIVITY:
            case PROV_AGENT:
                throw new IllegalStateException("should not happen: a relation is not an element");
            case PROV_USAGE:
                return PROV_SHORTHAND_USAGE;
            case PROV_GENERATION:
                return PROV_SHORTHAND_GENERATION;
            case PROV_INVALIDATION:
                return PROV_SHORTHAND_INVALIDATION;
            case PROV_START:
                return PROV_SHORTHAND_START;
            case PROV_END:
                return PROV_SHORTHAND_END;
            case PROV_COMMUNICATION:
                return PROV_SHORTHAND_COMMUNICATION;
            case PROV_DERIVATION:
                return PROV_SHORTHAND_DERIVATION;
            case PROV_ASSOCIATION:
                return PROV_SHORTHAND_ASSOCIATION;
            case PROV_ATTRIBUTION:
                return PROV_SHORTHAND_ATTRIBUTION;
            case PROV_DELEGATION:
                return PROV_SHORTHAND_DELEGATION;
            case PROV_INFLUENCE:
                return PROV_SHORTHAND_INFLUENCE;
            case PROV_ALTERNATE:
                return PROV_SHORTHAND_ALTERNATE;
            case PROV_SPECIALIZATION:
                return PROV_SHORTHAND_SPECIALIZATION;
            case PROV_MENTION:
                return PROV_SHORTHAND_MENTION;
            case PROV_MEMBERSHIP:
                return PROV_SHORTHAND_MEMBERSHIP;
            case PROV_BUNDLE:
                return null;
            case PROV_DICTIONARY_INSERTION:
            case PROV_DICTIONARY_REMOVAL:
            case PROV_DICTIONARY_MEMBERSHIP:
                throw new DocumentedUnsupportedCaseException("dictionaries not supported");
        }
        return null;
    }


    //////////////////////////////////////////////////////////////////////
    ///
    ///                              DOT FORMAT GENERATION
    ///
    //////////////////////////////////////////////////////////////////////


    protected String name;
    protected String layout;

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

    public void emitElement(QualifiedName name, StatementOrBundle.Kind kind, Map<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(dotify(qualifiedNameToString(name)));
        if (kind!=null) emitComment(name,kind, sb);
        emitProperties(sb,properties);
        out.println(sb);
    }


    public void emitBlankNode(String bnid, Map<String,String> properties, PrintStream out) {
        StringBuffer sb=new StringBuffer();
        sb.append(bnid);
        emitProperties(sb,properties);
        out.println(sb);
    }


    public void emitRelation(StatementOrBundle.Kind kind, String src, String dest, Map<String,String> properties, PrintStream out, boolean directional) {
        StringBuffer sb=new StringBuffer();
        sb.append(dotify(src));
        if (directional) {
            sb.append(DOT_DIRECTED_EDGE);
        } else {
            sb.append(DOT_UNDIRECTED_EDGE);
        }
        sb.append(dotify(dest));
        emitComment(null,kind, sb);
        emitProperties(sb,properties);
        out.println(sb);
    }

    public void emitProperties(StringBuffer sb, Map<String,String> properties) {
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

    protected void prelude(Document ignoredDoc, PrintStream out) {
        out.println("digraph \"" + name + "\" { rankdir=\"BT\"; ");  //size="16,12"; 
        if (layout!=null) {
            out.println("layout=\"" + layout + "\"; ");
        }
    }

    protected void postlude(Document ignoredDoc, PrintStream out) {
        out.println("}");
        out.close();
    }

    void prelude(Bundle doc, PrintStream out) {
        out.println("subgraph " + dotify("cluster" + qualifiedNameToString(doc.getId())) + " { ");
        out.println("  label=\"" + localnameToString(doc.getId()) + "\";");
        out.println("  URL=\"" + qualifiedNameToString(doc.getId()) + "\";");
    }

    void postlude(Bundle ignoredDoc, PrintStream out) {
        out.println("}");
    }

    public void setLayout(String layout) {
        this.layout=layout;
    }

    static class ProvUtilitiesForTriangle extends ProvUtilities {
        private final List<String> exceptions;


        public ProvUtilitiesForTriangle() {
            super();
            exceptions = List.of();
        }
        public ProvUtilitiesForTriangle(List<String> exceptions) {
            super();
            this.exceptions=exceptions;
        }

        @Override
        public List<QualifiedName> getOtherCauses(Relation r) {
            if (r instanceof Identifiable & ((Identifiable)r).getId()!=null) {
                if (exceptions.contains(((Identifiable)r).getId().getLocalPart())) {
                    return null;
                }
            }
            if (r instanceof WasAttributedTo) {
                WasAttributedTo wat = (WasAttributedTo) r;
                Hashtable<String, List<Other>> attributes=attributesWithNamespace(wat, NamespacePrefixMapper.PROV_EXT_NS);
                List<Other> result=attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                return result.stream().map(x -> (QualifiedName)x.getValue()).collect(Collectors.toList());
            }  else if (r instanceof QualifiedSpecializationOf ) {
                QualifiedSpecializationOf spe = (QualifiedSpecializationOf) r;
                Hashtable<String, List<Other>> attributes=attributesWithNamespace(spe, NamespacePrefixMapper.PROV_EXT_NS);
                List<Other> result=attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                return result.stream().map(x -> (QualifiedName)x.getValue()).collect(Collectors.toList());
            }  else if (r instanceof Used ) {
                Used usd = (Used) r;
                Hashtable<String, List<Other>> attributes=attributesWithNamespace(usd, NamespacePrefixMapper.PROV_EXT_NS);
                List<Other> result=attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                return result.stream().map(x -> (QualifiedName)x.getValue()).collect(Collectors.toList());
            }  else if (r instanceof WasInformedBy ) {
                WasInformedBy usd = (WasInformedBy) r;
                Hashtable<String, List<Other>> attributes=attributesWithNamespace(usd, NamespacePrefixMapper.PROV_EXT_NS);
                List<Other> result=attributes.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                return result.stream().map(x -> (QualifiedName)x.getValue()).collect(Collectors.toList());
            }  else if (r instanceof WasDerivedFrom ) {
                WasDerivedFrom der = (WasDerivedFrom) r;

                List<QualifiedName> result=new LinkedList<>();
                if (der.getGeneration()!=null) {
                    result.add(der.getGeneration());
                }
                if (der.getUsage()!=null) {
                    result.add(der.getUsage());
                }
                if (der.getActivity()!=null) {
                    result.add(der.getActivity());
                }
                return result;
            } else if (r instanceof WasGeneratedBy || r instanceof WasAssociatedWith || r instanceof SpecializationOf | r instanceof WasInvalidatedBy ) {
                return Collections.emptyList();
            } else {
                return super.getOtherCauses(r);
            }
        }
    }

    public static ProvToDot newProvToDot(ProvFactory pf) {
        Supplier<ProvUtilities> utilities= ProvUtilitiesForTriangle::new;
        return new ProvToDot(pf, utilities);
    }
    public static ProvToDot newProvToDot(ProvFactory pf, List<String> exceptions) {
        Supplier<ProvUtilities> utilities= () -> new ProvUtilitiesForTriangle(exceptions);
        return new ProvToDot(pf, utilities);
    }



}


