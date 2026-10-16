package org.openprovenance.prov.viz;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.DocumentedUnsupportedCaseException;
import org.openprovenance.prov.model.exception.UncheckedException;

import java.io.*;
import java.util.*;
import java.util.function.Supplier;

import static org.openprovenance.prov.model.NamespacePrefixMapper.PROV_EXT_NS;

/**
 * Visualisation of a PROV document. The generic part — which nodes, edges, clusters and attribute
 * boxes a document gives rise to — lives here and produces a {@link VizGraph}; each subclass renders
 * that model in its own notation (graphviz dot, mermaid, ...).
 *
 * <pre>
 *   Document --build--&gt; VizGraph --passes--&gt; VizGraph --render--&gt; text
 * </pre>
 */
public abstract class ProvViz implements RecommendedProvVisualProperties, ProvShorthandNames {

    protected final Logger logger = LogManager.getLogger(getClass());

    public int MAX_TOOLTIP_LENGTH = 2000;

    protected final ProvFactory pf;
    protected final Supplier<ProvUtilities> getProvUtilities;
    protected final ProvUtilities u;
    protected final QualifiedName SUM_SIZE;

    private Integer maxStringLength = null;
    protected boolean ellipsis = true;
    /** Draw the attribute box of n-ary relations (the qualified rendering). */
    protected boolean displayAnnotations = false;
    protected String name;
    protected String layout;
    private final List<VizPass> passes = new ArrayList<>();

    private int annotationCount = 0;
    private int bncounter = 0;

    public ProvViz(ProvFactory pf) {
        this(pf, ProvUtilities::new);
    }

    public ProvViz(ProvFactory pf, Supplier<ProvUtilities> getProvUtilities) {
        this.pf = pf;
        this.SUM_SIZE = pf.newQualifiedName(NamespacePrefixMapper.SUMMARY_NS, "size", NamespacePrefixMapper.SUMMARY_PREFIX);
        this.getProvUtilities = getProvUtilities;
        this.u = getProvUtilities.get();
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              CONFIGURATION
    ///
    //////////////////////////////////////////////////////////////////////

    public Integer getMaxStringLength() {
        return maxStringLength;
    }

    public void setMaxStringLength(Integer maxStringLength) {
        this.maxStringLength = maxStringLength;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public ProvViz displayAnnotations(boolean displayAnnotations) {
        this.displayAnnotations = displayAnnotations;
        return this;
    }

    public boolean displaysAnnotations() {
        return displayAnnotations;
    }

    public ProvViz addPass(VizPass pass) {
        passes.add(pass);
        return this;
    }

    public List<VizPass> getPasses() {
        return passes;
    }

    /** The name a backend gives the graph; also the title the constructor of a bundle-less document sees. */
    public String getName() {
        return name;
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              PIPELINE
    ///
    //////////////////////////////////////////////////////////////////////

    /** Render {@code graph} in this backend's notation. */
    public abstract void render(VizGraph graph, PrintStream out);

    /** The file extension of this backend's notation, without the dot (e.g. {@code dot}, {@code mmd}). */
    public abstract String notationExtension();

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              SHAPES, PER BACKEND
    ///
    //////////////////////////////////////////////////////////////////////

    /** What this backend draws for an entity's ellipse. */
    public abstract String ellipse();
    /** What this backend draws for an activity's rectangle. */
    public abstract String rectangle();
    /** What this backend draws for an agent's house. */
    public abstract String house();
    /** What this backend draws for an attribute box's note. */
    public abstract String note();
    /** What this backend draws for the point of an n-ary relation. */
    public abstract String point();
    /** What this backend draws for a dictionary's folder. */
    public abstract String folder();
    /** What this backend draws for a node that is only its label. */
    public abstract String plaintext();

    /** This backend's token for a conventional shape. */
    public String shapeToken(NodeShape shape) {
        return switch (shape) {
            case ELLIPSE -> ellipse();
            case RECTANGLE -> rectangle();
            case HOUSE -> house();
            case NOTE -> note();
            case POINT -> point();
            case FOLDER -> folder();
            case PLAINTEXT -> plaintext();
        };
    }

    /** The conventional shape of each kind of node. */
    public NodeShape defaultShape(NodeKind kind) {
        return switch (kind) {
            case ENTITY -> NodeShape.ELLIPSE;
            case ACTIVITY -> NodeShape.RECTANGLE;
            case AGENT -> NodeShape.HOUSE;
            case ANNOTATION -> NodeShape.NOTE;
            case BLANK -> NodeShape.POINT;
            case CUSTOM -> NodeShape.PLAINTEXT;
        };
    }

    public VizGraph process(VizGraph graph) {
        for (VizPass pass : passes) {
            graph = pass.apply(graph);
        }
        return graph;
    }

    public void convert(Document doc, PrintStream out, String title) {
        render(process(build(doc, title)), out);
    }

    public void convert(Document doc, OutputStream os, String title) {
        convert(doc, new PrintStream(os), title);
    }

    public void convert(Document doc, File file, String title) throws FileNotFoundException {
        convert(doc, new PrintStream(new FileOutputStream(file)), title);
    }

    public void convert(Document doc, String file, String title) throws FileNotFoundException {
        convert(doc, new File(file), title);
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              BUILDING THE MODEL
    ///
    //////////////////////////////////////////////////////////////////////

    public VizGraph build(Document doc, String title) {
        if (title != null) name = title;
        annotationCount = 0;
        bncounter = 0;
        VizGraph graph = new VizGraph(name);
        graph.layout = layout;
        buildStatements(u.getActivity(doc), u.getEntity(doc), u.getAgent(doc), u.getRelations(doc), graph);
        if (u.getBundle(doc) != null) {
            for (Bundle bun : u.getBundle(doc)) {
                graph.add(buildBundle(bun));
            }
        }
        return graph;
    }

    public VizCluster buildBundle(Bundle bun) {
        VizCluster cluster = new VizCluster(clusterKey(bun.getId()));
        cluster.id = bun.getId();
        cluster.label = localnameToString(bun.getId());
        cluster.url = qualifiedNameToString(bun.getId());
        buildStatements(u.getActivity(bun), u.getEntity(bun), u.getAgent(bun), u.getRelations(bun), cluster);
        return cluster;
    }

    protected void buildStatements(List<Activity> activities, List<Entity> entities, List<Agent> agents, List<Relation> relations, VizScope scope) {
        if (activities != null) for (Activity p : activities) buildActivity(p, scope);
        if (entities != null) for (Entity p : entities) buildEntity(p, scope);
        if (agents != null) for (Agent p : agents) buildAgent(p, scope);
        if (relations != null) for (Relation e : relations) buildDependency(e, scope);
    }

    public String qualifiedNameToString(QualifiedName qName) {
        return qName.getNamespaceURI() + qName.getLocalPart();
    }

    public String localnameToString(QualifiedName qName) {
        return nonEmptyLocalName(qName);
    }

    /** The key of the node standing for an element or an identified relation. */
    public String nodeKey(QualifiedName id) {
        return qualifiedNameToString(id);
    }

    public String clusterKey(QualifiedName id) {
        return "cluster" + qualifiedNameToString(id);
    }

    // ---- elements

    public VizNode buildActivity(Activity p, VizScope scope) {
        VizNode n = newElementNode(p, NodeKind.ACTIVITY);
        n.label = activityLabel(p) + displaySize(p);
        n.style.fill = ACTIVITY_FILL_COLOUR;
        n.style.stroke = ACTIVITY_COLOUR;
        n.style.style(ACTIVITY_STYLE);
        applyHints(p, n);
        scope.add(n);
        buildAnnotation("", p, scope);
        return n;
    }

    public VizNode buildEntity(Entity e, VizScope scope) {
        VizNode n = newElementNode(e, NodeKind.ENTITY);
        n.label = entityLabel(e) + displaySize(e);
        n.style.fill = ENTITY_FILLCOLOUR;
        n.style.stroke = ENTITY_COLOUR;
        n.style.style(ENTITY_STYLE);
        if (isDictionary(e)) n.shape = NodeShape.FOLDER;
        applyHints(e, n);
        scope.add(n);
        buildAnnotation("", e, scope);
        return n;
    }

    public VizNode buildAgent(Agent ag, VizScope scope) {
        VizNode n = newElementNode(ag, NodeKind.AGENT);
        n.label = agentLabel(ag) + displaySize(ag);
        n.style.fill = AGENT_FILLCOLOUR;
        n.style.style(AGENT_STYLE);
        applyHints(ag, n);
        scope.add(n);
        buildAnnotation("", ag, scope);
        return n;
    }

    protected VizNode newElementNode(Element e, NodeKind kind) {
        VizNode n = new VizNode(nodeKey(e.getId()), kind);
        n.shape = defaultShape(kind);
        n.id = e.getId();
        n.provKind = e.getKind();
        n.url = qualifiedNameToString(e.getId());
        return n;
    }

    public boolean isDictionary(Entity p) {
        for (Type type : p.getType()) {
            if (type.getValue() instanceof QualifiedName) {
                QualifiedName qn = (QualifiedName) type.getValue();
                if ("Dictionary".equals(qn.getLocalPart()) || "EmptyDictionary".equals(qn.getLocalPart())) return true;
            }
        }
        return false;
    }

    public String activityLabel(Activity p) {
        return localnameToString(p.getId());
    }

    public String entityLabel(Entity p) {
        return localnameToString(p.getId());
    }

    public String agentLabel(Agent p) {
        return localnameToString(p.getId());
    }

    public String displaySize(HasOther p) {
        for (Other o : p.getOther()) {
            if (SUM_SIZE.equals(o.getElementName())) {
                return " (" + o.getConvertedValue() + ")";
            }
        }
        return "";
    }

    // ---- hints carried by dot:* attributes in the data

    public String getStringValue(Other o) {
        Object v = o.getValue();
        if (v instanceof LangString) {
            return ((LangString) v).getValue();
        } else {
            return v.toString();
        }
    }

    /** {@code dot:fillcolor}, {@code dot:color}, {@code dot:style}, {@code dot:url}, {@code dot:size}, {@code dot:tooltip} override the defaults. */
    public void applyHints(HasOther object, VizStyled target) {
        Hashtable<String, List<Other>> table = u.attributesWithNamespace(object, NamespacePrefixMapper.DOT_NS);
        Style style = target.style();

        List<Other> o = table.get("fillcolor");
        if (o != null && !o.isEmpty()) {
            style.fill = getStringValue(o.get(0));
            style.style("filled");
        }
        o = table.get("color");
        if (o != null && !o.isEmpty()) {
            style.stroke = getStringValue(o.get(0));
        }
        o = table.get("style");
        if (o != null && !o.isEmpty()) {
            style.styles.clear();
            for (String word : getStringValue(o.get(0)).split(",")) {
                if (!word.isBlank()) style.style(word.trim());
            }
        }
        o = table.get("url");
        if (o != null && !o.isEmpty()) {
            target.setUrl(getStringValue(o.get(0)));
        }
        o = table.get("size");
        if (o != null && !o.isEmpty()) {
            if (object instanceof QualifiedRelation) {
                style.penWidth = Double.parseDouble(o.get(0).getValue().toString());
            } else if (object instanceof Element) {
                style.width = Double.parseDouble(getStringValue(o.get(0))) * 0.75;
            }
        }
        o = table.get("tooltip");
        if (o != null && !o.isEmpty()) {
            String val = getStringValue(o.get(0));
            if (val.length() > MAX_TOOLTIP_LENGTH) {
                val = val.substring(0, MAX_TOOLTIP_LENGTH) + " ...";
            }
            target.setTooltip(val);
        }
    }

    // ---- attribute boxes

    /** Toolbox-internal attributes (dot, summary, ...) and provext links to other statements are drawn otherwise, never listed in an attribute box. */
    public boolean isDisplayedAttribute(Other prop) {
        String ns = prop.getElementName().getNamespaceURI();
        return !(ns.startsWith(NamespacePrefixMapper.SHARED_PROV_TOOLBOX_PREFIX) || NamespacePrefixMapper.isProvExt(ns) || ns.equals(NamespacePrefixMapper.OPENPROV_NS));
    }

    public int countOthers(HasOther ann) {
        int count = 0;
        for (Other obj : ann.getOther()) {
            if (isDisplayedAttribute(obj)) count++;
        }
        return count;
    }

    /** Nothing to put in a box: no displayable attribute, and no identifier worth showing (elements show theirs as label). */
    public boolean hasNothingToAnnotate(HasOther statement) {
        return (statement.getOther() == null || countOthers(statement) == 0)
                && ((HasType) statement).getType().isEmpty()
                && (!(statement instanceof HasValue) || ((HasValue) statement).getValue() == null)
                && (!(statement instanceof HasRole) || ((HasRole) statement).getRole().isEmpty())
                && (!(statement instanceof HasLocation) || ((HasLocation) statement).getLocation().isEmpty())
                && ((HasLabel) statement).getLabel().isEmpty()
                && (!(statement instanceof Identifiable) || ((Identifiable) statement).getId() == null || statement instanceof Element);
    }

    /**
     * The attribute box of {@code statement}, tied to its node by a dashed link.
     *
     * @param generatedIdIfUnqualifiedRelation the key of the point standing for an n-ary relation without identifier, "" for elements and identified statements
     * @return the box, or null when there is nothing to show
     */
    public VizNode buildAnnotation(String generatedIdIfUnqualifiedRelation, HasOther statement, VizScope scope) {
        if (hasNothingToAnnotate(statement)) return null;

        QualifiedName statementId = ((Identifiable) statement).getId();
        boolean qualifiedRelationWithId = statement instanceof Relation && statementId != null;

        VizNode box = new VizNode(annotationKey(generatedIdIfUnqualifiedRelation), NodeKind.ANNOTATION);
        box.shape = NodeShape.NOTE;
        if (qualifiedRelationWithId) {
            box.rows.add(new VizNode.Row("id", statementId.getLocalPart()));
        }
        qualifiedRelationWithId = qualifiedRelationWithId || !generatedIdIfUnqualifiedRelation.isEmpty();
        addAnnotationRows(statement, box);
        String colour = qualifiedRelationWithId ? QUALIFIED_COLOUR : annotationColor(statement);
        box.style.stroke = colour;
        box.style.fontColour = qualifiedRelationWithId ? QUALIFIED_COLOUR : ANNOTATION_FONT_COLOUR;
        box.style.fontSize = 10;
        scope.add(box);

        VizEdge link = new VizEdge(box.key, (statementId == null) ? generatedIdIfUnqualifiedRelation : nodeKey(statementId));
        link.role = EdgeRole.ANNOTATION;
        link.head = ArrowKind.NONE;
        link.style.style("dashed");
        link.style.stroke = colour;
        scope.add(link);
        return box;
    }

    public String annotationKey(String node) {
        return "-attrs" + node + (annotationCount++);
    }

    public void addAnnotationRows(HasOther ann, VizNode box) {
        for (Type type : ((HasType) ann).getType()) {
            box.rows.add(new VizNode.Row("type", attributeValueText(type)));
        }
        for (LangString lab : ((HasLabel) ann).getLabel()) {
            box.rows.add(new VizNode.Row("label", lab.getValue()));
        }
        if (ann instanceof HasValue) {
            Value val = ((HasValue) ann).getValue();
            if (val != null) box.rows.add(new VizNode.Row("value", attributeValueText(val)));
        }
        if (ann instanceof HasRole) {
            for (Role role : ((HasRole) ann).getRole()) {
                box.rows.add(new VizNode.Row("role", attributeValueText(role)));
            }
        }
        if (ann instanceof HasLocation) {
            for (Location location : ((HasLocation) ann).getLocation()) {
                box.rows.add(new VizNode.Row("location", attributeValueText(location)));
            }
        }
        for (Other prop : ann.getOther()) {
            if (!isDisplayedAttribute(prop)) continue;
            box.rows.add(new VizNode.Row(convertProperty(prop), attributeValueText(prop)));
        }
    }

    public String convertProperty(Attribute oLabel) {
        String label = oLabel.getElementName().getUri();
        int i = label.lastIndexOf("#");
        int j = label.lastIndexOf("/");
        return label.substring(Math.max(i, j) + 1);
    }

    /** The text of an attribute value: prefixed names for qualified names, the string with its language tag for lang strings. */
    public String attributeValueText(Attribute t) {
        Object val = t.getValue();
        if (val instanceof QualifiedName) {
            QualifiedName q = (QualifiedName) val;
            return q.getPrefix() + ":" + q.getLocalPart();
        }
        if (val instanceof LangString) {
            LangString ls = (LangString) val;
            return (ls.getLang() == null) ? ls.getValue() : ls.getValue() + "@" + ls.getLang();
        }
        return "" + val;
    }

    public String annotationColor(HasOther ignoredAnn) {
        return ANNOTATION_COLOUR;
    }

    // ---- relations

    public void buildDependency(Relation e, VizScope scope) {
        List<QualifiedName> others = u.getOtherCauses(e);
        if (others != null) { // n-ary case
            String id = null;
            if (e instanceof Identifiable && ((Identifiable) e).getId() != null) {
                id = nodeKey(((Identifiable) e).getId());
            }
            String bnid = (id == null) ? "bn" + (bncounter++) : id;

            VizNode bn = new VizNode(bnid, NodeKind.BLANK);
            bn.shape = NodeShape.POINT;
            bn.provKind = e.getKind();
            if (id != null) bn.id = ((Identifiable) e).getId();
            scope.add(bn);

            QualifiedName effect = u.getEffect(e);
            if (effect != null) {
                VizEdge in = new VizEdge(nodeKey(effect), bnid);
                in.provKind = e.getKind();
                in.head = ArrowKind.NONE;
                ArrowKind tail = getArrowShapeForRelation(e);
                if (tail != null) in.tail = tail;
                if (e instanceof HasOther) applyHints((HasOther) e, in);
                if (e instanceof WasInvalidatedBy) in.tailPort = "s";
                scope.add(in);
            }

            if (u.getCause(e) != null) {
                VizEdge out = new VizEdge(bnid, nodeKey(u.getCause(e)));
                out.provKind = e.getKind();
                out.label = getShortLabelForRelation(e);
                if (e instanceof HasOther) applyHints((HasOther) e, out);
                if (e instanceof DerivedByInsertionFrom) out.head = ArrowKind.ONORMAL;
                if (e instanceof WasInvalidatedBy) out.headPort = "s";
                scope.add(out);
            }

            for (QualifiedName other : others) {
                if (other != null) {
                    VizEdge link = new VizEdge(bnid, nodeKey(other));
                    link.provKind = e.getKind();
                    link.role = EdgeRole.LINK;
                    link.head = ArrowKind.NONE;
                    if (e instanceof HasOther) applyHints((HasOther) e, link);
                    link.style.style("dashed");
                    link.style.stroke = QUALIFIED_COLOUR;
                    scope.add(link);
                }
            }
            if (displayAnnotations && e instanceof HasOther) {
                buildAnnotation(bnid, (HasOther) e, scope);
            }
        } else { // binary case
            QualifiedName effect = u.getEffect(e);
            QualifiedName cause = u.getCause(e);
            if (effect != null && cause != null) {
                VizEdge edge = new VizEdge(nodeKey(effect), nodeKey(cause));
                edge.provKind = e.getKind();
                edge.label = getShortLabelForRelation(e);
                if (e instanceof QualifiedRelation) applyHints((QualifiedRelation) e, edge);
                ArrowKind tail = getArrowShapeForRelation(e);
                if (tail != null) edge.tail = tail;
                scope.add(edge);
            }
        }
    }

    public ArrowKind getArrowShapeForRelation(Relation e) {
        if (e instanceof WasStartedBy) return ArrowKind.OINV;
        if (e instanceof WasEndedBy) return ArrowKind.ODIAMOND;
        if (e instanceof WasInvalidatedBy) return ArrowKind.ODIAMOND;
        return null;
    }

    public String getShortLabelForRelation(Relation e) {
        return switch (e.getKind()) {
            case PROV_ENTITY, PROV_ACTIVITY, PROV_AGENT ->
                    throw new IllegalStateException("should not happen: a relation is not an element");
            case PROV_USAGE -> PROV_SHORTHAND_USAGE;
            case PROV_GENERATION -> PROV_SHORTHAND_GENERATION;
            case PROV_INVALIDATION -> PROV_SHORTHAND_INVALIDATION;
            case PROV_START -> PROV_SHORTHAND_START;
            case PROV_END -> PROV_SHORTHAND_END;
            case PROV_COMMUNICATION -> PROV_SHORTHAND_COMMUNICATION;
            case PROV_DERIVATION -> PROV_SHORTHAND_DERIVATION;
            case PROV_ASSOCIATION -> PROV_SHORTHAND_ASSOCIATION;
            case PROV_ATTRIBUTION -> PROV_SHORTHAND_ATTRIBUTION;
            case PROV_DELEGATION -> PROV_SHORTHAND_DELEGATION;
            case PROV_INFLUENCE -> PROV_SHORTHAND_INFLUENCE;
            case PROV_ALTERNATE -> PROV_SHORTHAND_ALTERNATE;
            case PROV_SPECIALIZATION -> PROV_SHORTHAND_SPECIALIZATION;
            case PROV_MENTION -> PROV_SHORTHAND_MENTION;
            case PROV_MEMBERSHIP -> PROV_SHORTHAND_MEMBERSHIP;
            case PROV_BUNDLE -> null;
            case PROV_DICTIONARY_INSERTION, PROV_DICTIONARY_REMOVAL, PROV_DICTIONARY_MEMBERSHIP ->
                    throw new DocumentedUnsupportedCaseException("dictionaries not supported");
        };
    }

    //////////////////////////////////////////////////////////////////////
    ///
    ///                              TEXT HELPERS
    ///
    //////////////////////////////////////////////////////////////////////

    public String nonEmptyLocalName(QualifiedName name) {
        final String localPart = name.getLocalPart();
        if ("".equals(localPart)) {
            // we are in this case for url finishing with /
            String uri = name.getNamespaceURI();
            String label = uri.substring(0, uri.length() - 1);
            int i = label.lastIndexOf("#");
            int j = label.lastIndexOf("/");
            return uri.substring(Math.max(i, j) + 1);
        } else {
            return localPart;
        }
    }

    /** Cut to {@link #getMaxStringLength()} with an ellipsis; the identity when no length is set. */
    public String truncate(String text) {
        if (text == null) return null;
        if (maxStringLength != null && text.length() > maxStringLength) {
            text = text.substring(0, maxStringLength);
            if (ellipsis) text = text + "...";
        }
        return text;
    }

    /** Wraps a checked exception where the pipeline cannot throw one. */
    protected static UncheckedException unchecked(IOException e) {
        return new UncheckedException(e);
    }
}
