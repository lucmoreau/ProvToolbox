package org.openprovenance.prov.template.expander;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.model.exception.InvalidCaseException;
import org.openprovenance.prov.model.ProvUtilities;

public class ExpandUtil {
    static Logger logger = Logger.getLogger(ExpandUtil.class);

    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    public static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    public static final String TMPL_PREFIX = "tmpl";
    public static final String VAR_PREFIX = "var";
    public static final String VARGEN_PREFIX = "vargen";

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


    //private final ProvFactory pf;

    static ProvUtilities u = new ProvUtilities();


    static   public int getFirstTimeIndex(Statement s)  {
    	final Kind kind = s.getKind();
    	switch (kind) {
    	case PROV_ACTIVITY: return 1;
    	case PROV_AGENT: return 1;
    	case PROV_ALTERNATE: return 2;
    	case PROV_ASSOCIATION: return 4;
    	case PROV_ATTRIBUTION: return 3;
    	case PROV_BUNDLE:
    		throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
    	case PROV_COMMUNICATION: return 3;
    	case PROV_DELEGATION: return 4;
    	case PROV_DERIVATION: return 6;
    	case PROV_DICTIONARY_INSERTION:
    		throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
    	case PROV_DICTIONARY_MEMBERSHIP:
    		throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
    	case PROV_DICTIONARY_REMOVAL:
    		throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);
    	case PROV_END: return 4;
    	case PROV_ENTITY: return 1;
    	case PROV_GENERATION: return 3;
    	case PROV_INFLUENCE: return 3;
    	case PROV_INVALIDATION: return 3;
    	case PROV_MEMBERSHIP: return 2;
    	case PROV_MENTION: return 3;
    	case PROV_SPECIALIZATION: return 2;
    	case PROV_START: return 4;
    	case PROV_USAGE: return 3;
    	default:
    		throw new InvalidCaseException("ProvUtilities.getFirstTimeIndex() for " + kind);     
    	}
    }

    static public Set<QualifiedName> freeVariables(Statement statement) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        for (int i = 0; i < getFirstTimeIndex(statement); i++) {
            Object o = u.getter(statement, i);
            if (o instanceof QualifiedName) {
                QualifiedName name = (QualifiedName) o;
                if (name != null && isVariable(name))
                    result.add(name);
            } else {
                if (o instanceof List) {
                    List<QualifiedName> ll = (List<QualifiedName>) o;
                    for (QualifiedName name : ll) {
                        if (name != null && isVariable(name))
                            result.add(name);
                    }
                }
            }
        }
        return result;
    }

    static public Set<QualifiedName> freeVariables(Bundle statement) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        QualifiedName name = statement.getId();
        if (name != null && isVariable(name)) {
            result.add(name);
        }
        return result;
    }


    static public HashSet<QualifiedName> freeAttributeVariables(Statement statement, ProvFactory pf) {
        HashSet<QualifiedName> result = new HashSet<QualifiedName>();
        Collection<Attribute> ll = pf.getAttributes(statement);
        for (Attribute attr : ll) {
            if (pf.getName().PROV_QUALIFIED_NAME.equals(attr.getType())) {
                Object o = attr.getValue();
                if (o instanceof QualifiedName) { // if attribute is constructed
                                                  // properly, this test should
                                                  // always return true
                    QualifiedName qn = (QualifiedName) o;
                    if (isVariable(qn)  && (!attr.getElementName().getUri().equals(LINKED_URI)))  // ignore occurrence of linked variables
                        result.add(qn);
                }
            }
        }
        return result;
    }


   public static Using usedGroups(Statement statement, Groupings groupings, Bindings bindings) {
        Set<QualifiedName> vars = freeVariables(statement);
        Set<Integer> groups = new HashSet<Integer>();
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
            List<QualifiedName> vals = bindings.getVariables().get(qn);
            if (vals != null) {
                u.addGroup(g, vals.size());

            } else {
                if (isGensymVariable(qn)) {
                    u.addGroup(g, 1); // uuid to be generated for this gensym
                                      // variable
                } else {
                    List<List<TypedValue>> attrs = bindings.getAttributes().get(vs.get(0));
                    if (attrs != null) {
                        u.addGroup(g, attrs.size());
                    }
                }

            }
        }

        return u;
    }

    static public boolean isVariable(QualifiedName id) {
        if (id == null)
            return false;
        final String namespaceURI = id.getNamespaceURI();
        return (VAR_NS.equals(namespaceURI) || VARGEN_NS.equals(namespaceURI));
    }
    static public boolean isVGensymariable(QualifiedName id) {
        if (id == null)
            return false;
        final String namespaceURI = id.getNamespaceURI();
        return VARGEN_NS.equals(namespaceURI);
    }
    
    static public boolean isGensymVariable(QualifiedName id) {
        if (id == null)
            return false;
        final String namespaceURI = id.getNamespaceURI();
        return VARGEN_NS.equals(namespaceURI);
    }

}

