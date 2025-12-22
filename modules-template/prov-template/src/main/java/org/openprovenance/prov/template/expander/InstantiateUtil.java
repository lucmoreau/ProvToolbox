package org.openprovenance.prov.template.expander;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.exception.InvalidCaseException;
import org.openprovenance.prov.template.json.Bindings;
import org.openprovenance.prov.template.json.Descriptor;
import org.openprovenance.prov.template.json.Descriptors;

public class InstantiateUtil {
    static Logger logger = LogManager.getLogger(InstantiateUtil.class);

    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    public static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    public static final String TMPL_PREFIX = "tmpl";
    public static final String VAR_PREFIX = "var";

    public static final String LINKED = "linked";
    public static final String LINKED_URI = TMPL_NS + LINKED;
    public static final String LABEL = "label";
    public static final String TIME = "time";
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String LABEL_URI = TMPL_NS + LABEL;
    public static final String TIME_URI = TMPL_NS + TIME;
    public static final String STARTTIME_URI = TMPL_NS + STARTTIME;
    public static final String ENDTIME_URI = TMPL_NS + ENDTIME;
    public static final String IFVAR = "if";
    public static final String IFVAR_URI = TMPL_NS + IFVAR;
    public static final String IDVAR = "ID";
    public static final String IDVAR_URI = TMPL_NS + IDVAR;


    public static final String ACTIVITY_TYPE = "activityType";
    public static final String ACTIVITY_TYPE_URI = TMPL_NS + ACTIVITY_TYPE;
    public static final String ACTIVITY = "activity";
    public static final String TMPL_ACTIVITY_URI = TMPL_NS + ACTIVITY;

    public static final String UUID_PREFIX = "uuid";
    public static final String URN_UUID_NS = "urn:uuid:";
    static ProvUtilities u = new ProvUtilities();


    static   public int getFirstTimeIndex(Statement s)  {
        final Kind kind = s.getKind();
        return switch (kind) {
            case PROV_ACTIVITY -> 1;
            case PROV_AGENT -> 1;
            case PROV_ALTERNATE -> 2;
            case PROV_ASSOCIATION -> 4;
            case PROV_ATTRIBUTION -> 3;
            case PROV_BUNDLE -> throw new InvalidCaseException("ExpandUtil.getFirstTimeIndex() for " + kind);
            case PROV_COMMUNICATION -> 3;
            case PROV_DELEGATION -> 4;
            case PROV_DERIVATION -> 6;
            case PROV_DICTIONARY_INSERTION ->
                    throw new InvalidCaseException("ExpandUtil.getFirstTimeIndex() for " + kind);
            case PROV_DICTIONARY_MEMBERSHIP ->
                    throw new InvalidCaseException("ExpandUtil.getFirstTimeIndex() for " + kind);
            case PROV_DICTIONARY_REMOVAL ->
                    throw new InvalidCaseException("ExpandUtil.getFirstTimeIndex() for " + kind);
            case PROV_END -> 4;
            case PROV_ENTITY -> 1;
            case PROV_GENERATION -> 3;
            case PROV_INFLUENCE -> 3;
            case PROV_INVALIDATION -> 3;
            case PROV_MEMBERSHIP -> (s instanceof QualifiedRelation) ? 3 : 2;
            case PROV_MENTION -> 3;
            case PROV_SPECIALIZATION -> (s instanceof QualifiedRelation) ? 3 : 2;
            case PROV_START -> 4;
            case PROV_USAGE -> 3;
            default -> throw new InvalidCaseException("ExpandUtil.getFirstTimeIndex() for " + kind);
        };
    }

    static public Set<QualifiedName> freeVariables(Statement statement) {
        Set<QualifiedName> result = new HashSet<>();
        for (int i = 0; i < getFirstTimeIndex(statement); i++) {
            Object o = u.getter(statement, i);
            if (o instanceof QualifiedName) {
                QualifiedName name = (QualifiedName) o;
                if (isVariable(name))
                    result.add(name);
            } else {
                if (o instanceof List) {
                    List<QualifiedName> ll = (List<QualifiedName>) o;
                    for (QualifiedName name : ll) {
                        if (isVariable(name))
                            result.add(name);
                    }
                }
            }
        }
        return result;
    }

    static public Set<QualifiedName> freeAttributeVariables(Statement statement) {
        Set<QualifiedName> result = new HashSet<>();
        if (statement instanceof HasOther) {
            List<Other> ll = ((HasOther) statement).getOther();
            for (Other other : ll) {
                QualifiedName elementName = other.getElementName();
                if (isVariable(elementName)) {
                    result.add(elementName);
                }
                Object value = other.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName qn = (QualifiedName) value;
                    if (isVariable(qn))  result.add(qn);
                }
            }
        }
        if (statement instanceof HasValue) {
            Value attr = ((HasValue) statement).getValue();
            if (attr != null) {
                Object value = attr.getValue();
                if (value instanceof QualifiedName) {
                    QualifiedName qn = (QualifiedName) value;
                    if (isVariable(qn))  result.add(qn);
                }
            }
        }
        if (statement instanceof HasType) {
            List<Type> types = ((HasType) statement).getType();
            if (types != null) {
                for (Attribute attr : types) {
                    Object value = attr.getValue();
                    if (value instanceof QualifiedName) {
                        QualifiedName qn = (QualifiedName) value;
                        if (isVariable(qn))  result.add(qn);
                    }
                }
            }
        }
        if (statement instanceof HasLocation) {
            List<Location> locations = ((HasLocation) statement).getLocation();
            if (locations != null) {
                for (Attribute attr : locations) {
                    Object value = attr.getValue();
                    if (value instanceof QualifiedName) {
                        QualifiedName qn = (QualifiedName) value;
                        if (isVariable(qn))  result.add(qn);
                    }
                }
            }
        }
        if (statement instanceof HasRole) {
            List<Role> roles = ((HasRole) statement).getRole();
            if (roles != null) {
                for (Attribute attr : roles) {
                    Object value = attr.getValue();
                    if (value instanceof QualifiedName) {
                        QualifiedName qn = (QualifiedName) value;
                        if (isVariable(qn))  result.add(qn);
                    }
                }
            }
        }

        return result;
    }


    static public Set<QualifiedName> freeVariables(Bundle statement) {
        Set<QualifiedName> result = new HashSet<>();
        QualifiedName name = statement.getId();
        if (isVariable(name)) {
            result.add(name);
        }
        return result;
    }


    static public Set<QualifiedName> freeAttributeVariables(Statement statement, ProvFactory pf) {
        Set<QualifiedName> result = new HashSet<>();
        Collection<Attribute> ll = pf.getAttributes(statement);
        Name name = pf.getName();
        for (Attribute attr : ll) {
            QualifiedName elementName = attr.getElementName();
            if (name.PROV_QUALIFIED_NAME.equals(attr.getType())) {
                Object o = attr.getValue();
                if (o instanceof QualifiedName) { // if attribute is constructed
                    // properly, this test should
                    // always return true
                    QualifiedName qn = (QualifiedName) o;
                    if (isVariable(qn)  && (!elementName.getUri().equals(LINKED_URI)))  // ignore occurrence of linked variables
                        result.add(qn);
                }
            }

            if (InstantiateUtil.isVariable(elementName)) {
                //System.out.println("Free variable: found attrib variable " + attr.getElementName());
                result.add(elementName);
            }
        }
        return result;
    }




    public static Using usedGroups(Statement statement, Groupings groupings, Bindings bindings) {
        Set<QualifiedName> vars = freeVariables(statement);
        Set<Integer> groups = new HashSet<>();
        for (QualifiedName var : vars) {
            for (int grp = 0; grp < groupings.size(); grp++) {
                List<QualifiedName> names = groupings.get(grp);
                if (names.contains(var)) {
                    groups.add(grp);
                }
            }
        }

        Using u = new Using();
        Integer[] sorted = groups.toArray(new Integer[0]);
        Arrays.sort(sorted);


        for (Integer g : sorted) {
            List<QualifiedName> vs = groupings.get(g);
            QualifiedName qn = vs.get(0);

            Descriptors descriptors=(bindings.var==null)?null:bindings.var.get(qn.getLocalPart());
            Descriptors gen_descriptors=(bindings.vargen==null)?null:bindings.vargen.get(qn.getLocalPart());

            List<Descriptor> descriptor_list=new java.util.ArrayList<>();
            if (descriptors!=null && descriptors.values!=null) descriptor_list.addAll(descriptors.values);
            if (gen_descriptors!=null && gen_descriptors.values!=null) descriptor_list.addAll(gen_descriptors.values);


            if (!descriptor_list.isEmpty()) {
                u.addGroup(g, descriptor_list.size());
            } else {
                if (isGensymVariable(qn)) {
                    u.addGroup(g, 1); // uuid to be generated for this gensym
                    // variable
                } else {

                }
            }
        }

        return u;
    }

    static public boolean isVariable(QualifiedName id) {
        if (id == null) return false;
        final String namespaceURI = id.getNamespaceURI();
        return (VAR_NS.equals(namespaceURI) || VARGEN_NS.equals(namespaceURI));
    }

    static public boolean isGensymVariable(QualifiedName id) {
        if (id == null) return false;
        final String namespaceURI = id.getNamespaceURI();
        return VARGEN_NS.equals(namespaceURI);
    }

    static public QualifiedName newVariable(String name, ProvFactory pf) {
        return pf.newQualifiedName(VAR_NS,name,VAR_PREFIX);
    }

}

