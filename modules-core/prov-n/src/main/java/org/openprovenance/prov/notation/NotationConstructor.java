package org.openprovenance.prov.notation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.ProvUtilities.BuildFlag;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.model.extension.QualifiedAlternateOf;
import org.openprovenance.prov.model.extension.QualifiedHadMember;
import org.openprovenance.prov.model.extension.QualifiedSpecializationOf;

/** For testing purpose, conversion back to ASN. */

public class NotationConstructor implements ModelConstructor, ModelConstructorExtension {
    public static final String MARKER = "-";
    boolean abbrev = false;
    final private BufferedWriter buffer;


    public boolean standaloneExpression = false;

    public NotationConstructor(Writer writer) {
        this.buffer = new BufferedWriter(writer);
    }

    public String breakline() {
        return "\n";
    }

    public void close() {
        try {
            buffer.close();
        } catch (IOException e) {
            throw new UncheckedException(
                                         "convertBeanToNotation: closing writer failed",
                                         e);
        }
    }

    public Object convertDefaultNamespace(Object iri) {
        return keyword("default") + " " + showuri((String) iri);
    }

    // TODO
    public Object convertExtension(Object name, QualifiedName id, Object args,
                                   Object dAttrs) {
        System.out.println("Name @" + name);
        System.out.println("Name @" + id);
        System.out.println("Name @" + args);
        System.out.println("Name @" + dAttrs);
        // String s=keyword((String)name) + "(" + oldOptionalId(id) + args +
        // oldOptionalAttributes(dAttrs) + ")";
        // return s;
        return null;
    }

    public Object convertNamespace(Object pre, Object iri) {
        return keyword("prefix") + " " + showprefix((String) pre) + " "
                + showuri((String) iri);
    }

    public void flush() {
        try {
            buffer.flush();
        } catch (IOException e) {
            throw new UncheckedException(
                                         "convertBeanToNotation: closing writer failed",
                                         e);
        }
    }

    public String idOrMarker(QualifiedName qn) {
        return ((qn == null) ? MARKER : Namespace.getThreadNamespace().qualifiedNameToString(qn));
    }

    private String keyEntitySet(List<Entry> kes) {
        String s = "{";
        if (kes != null) {
            boolean first = true;
            for (Entry p : kes) {
                if (!first)
                    s = s + ", ";
                first = false;
                s = s
                        + "("
                        + ProvUtilities.valueToNotationString(p.getKey())
                        + ", " + idOrMarker(p.getEntity()) + ")";
            }
        }
        s = s + "}";
        return s;
    }

    private String keySet(List<org.openprovenance.prov.model.Key> ks) {
        String s = "{";
        if (ks != null) {
            boolean first = true;
            for (org.openprovenance.prov.model.Key k : ks) {
                if (!first)
                    s = s + ", ";
                first = false;
                s = s
                        + ProvUtilities.valueToNotationString(k);
            }
        }
        s = s + "}";
        return s;
    }

    public String keyword(String s) {
        return s;
    }

    @Override
    public ActedOnBehalfOf newActedOnBehalfOf(QualifiedName id,
                                              QualifiedName ag2,
                                              QualifiedName ag1,
                                              QualifiedName a,
                                              Collection<Attribute> attributes) {
        String s = keyword("actedOnBehalfOf") + "(" + optionalId(id)
                + idOrMarker(ag2) + "," + idOrMarker(ag1) + "," + idOrMarker(a)
                + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public Activity newActivity(QualifiedName id,
                                XMLGregorianCalendar startTime,
                                XMLGregorianCalendar endTime,
                                Collection<Attribute> attributes) {
        final String s = keyword("activity") + "(" + idOrMarker(id) + ","
                + timeOrMarker(startTime) + "," + timeOrMarker(endTime);
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public Agent newAgent(QualifiedName id, Collection<Attribute> attributes) {
        final String s = keyword("agent") + "(" + idOrMarker(id);
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public AlternateOf newAlternateOf(QualifiedName e1, QualifiedName e2) {
        writeln("alternateOf(" + idOrMarker(e1) + "," + idOrMarker(e2) + ")");
        return null;
    }

    @Override
    public DerivedByInsertionFrom newDerivedByInsertionFrom(QualifiedName id,
                                                            QualifiedName after,
                                                            QualifiedName before,
                                                            List<Entry> kes,
                                                            Collection<Attribute> attributes) {

        String s = "prov:derivedByInsertionFrom(" + optionalId(id)
                + idOrMarker(after) + "," + idOrMarker(before) + ","
                + keyEntitySet(kes) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public DerivedByRemovalFrom newDerivedByRemovalFrom(QualifiedName id,
                                                        QualifiedName after,
                                                        QualifiedName before,
                                                        List<org.openprovenance.prov.model.Key> keys,
                                                        Collection<Attribute> attributes) {
        String s = "prov:derivedByRemovalFrom(" + optionalId(id)
                + idOrMarker(after) + "," + idOrMarker(before) + ","
                + keySet(keys) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public DictionaryMembership newDictionaryMembership(QualifiedName dict,
                                                        List<Entry> keyEntitySet) {
        if (abbrev) {
            String s = "provx:hadDictionaryMember(" + idOrMarker(dict) + ","
                    + keyEntitySet(keyEntitySet) + ")";
            writeln(s);
        } else {
            for (Entry entry : keyEntitySet) {

                String s = "prov:hadDictionaryMember("
                        + idOrMarker(dict)
                        + ","
                        + idOrMarker(entry.getEntity())
                        + ","
                        + ProvUtilities.valueToNotationString(entry.getKey())
                        + ")";
                writeln(s);
            }
        }
        return null;
    }

    @Override
    public DictionaryMembership newDictionaryMembership(QualifiedName id, QualifiedName dict, List<Entry> keyEntitySet, Collection<Attribute> attributes) {
        if (abbrev) {
            String s = "provx:hadDictionaryMember(" + idOrMarker(dict) + ","
                    + keyEntitySet(keyEntitySet) + ")";
            writeln(s);
        } else {
            for (Entry entry : keyEntitySet) {

                String s = "prov:hadDictionaryMember("
                        + idOrMarker(dict)
                        + ","
                        + idOrMarker(entry.getEntity())
                        + ","
                        + ProvUtilities.valueToNotationString(entry.getKey())
                        + ")";
                writeln(s);
            }
        }
        return null;
    }

    @Override
    public Document newDocument(Namespace namespaces,
                                Collection<Statement> statements,
                                Collection<Bundle> bundles) {
        String s = "";
        s = s + keyword("endDocument");
        writeln(s);
        return null;
    }

    @Override
    public Document newDocument(Namespace namespace, List<StatementOrBundle> statementsOrBundles) {
        String s = "";
        s = s + keyword("endDocument");
        writeln(s);
        return null;
    }

    @Override
    public Entity newEntity(QualifiedName id, Collection<Attribute> attributes) {
        final String s = keyword("entity") + "(" + idOrMarker(id);
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public HadMember newHadMember(QualifiedName c, Collection<QualifiedName> ll) {
        if ((ll == null) || (ll.size() == 0)) {
            // strictly speaking it is not a syntactically correct expression,
            // but we print something to support scruffiness
            String s = keyword("hadMember") + "(" + idOrMarker(c) + ","
                    + idOrMarker((QualifiedName) null) + ")";
            writeln(s);
        } else {
            for (QualifiedName e : ll) {
                String s = keyword("hadMember") + "(" + idOrMarker(c) + ","
                        + idOrMarker(e) + ")";
                writeln(s);
            }
        }
        return null;
    }

    @Override
    public MentionOf newMentionOf(QualifiedName e2, QualifiedName e1,
                                  QualifiedName b) {
        String s = "mentionOf(" + idOrMarker(e2) + ", " + idOrMarker(e1) + ", "
                + idOrMarker(b) + ")";
        writeln(s);
        return null;
    }

    @Override
    public Bundle newNamedBundle(QualifiedName id, Namespace namespace,
                                      Collection<Statement> statements) {
        String s = "";
        s = s + keyword("endBundle");
        writeln(s);
      
        return null;
    }

    @Override
    public SpecializationOf newSpecializationOf(QualifiedName e2,
                                                QualifiedName e1) {
        writeln("specializationOf(" + idOrMarker(e2) + "," + idOrMarker(e1)
                + ")");
        return null;
    }

    @Override
    public Used newUsed(QualifiedName id, QualifiedName activity,
                        QualifiedName entity, XMLGregorianCalendar time,
                        Collection<Attribute> attributes) {
        final String s = keyword("used") + "(" + optionalId(id)
                + idOrMarker(activity) + "," + idOrMarker(entity) + ","
                + timeOrMarker(time);
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
                                                  QualifiedName a,
                                                  QualifiedName ag,
                                                  QualifiedName plan,
                                                  Collection<Attribute> attributes) {
        final String s = keyword("wasAssociatedWith") + "(" + optionalId(id)
                + idOrMarker(a) + "," + idOrMarker(ag) + "," + idOrMarker(plan);
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QualifiedName id,
                                              QualifiedName e,
                                              QualifiedName ag,
                                              Collection<Attribute> attributes) {
        final String s = keyword("wasAttributedTo") + "(" + optionalId(id)
                + idOrMarker(e) + ", " + idOrMarker(ag);
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public WasDerivedFrom newWasDerivedFrom(QualifiedName id, 
                                            QualifiedName e2,
                                            QualifiedName e1,
                                            QualifiedName activity,
                                            QualifiedName generation,
                                            QualifiedName usage,
                                            Collection<Attribute> attributes) {
        final String s = keyword("wasDerivedFrom")
                + "("
                + optionalId(id)
                + idOrMarker(e2)
                + ", "
                + idOrMarker(e1)
                + ((activity == null && generation == null && usage == null) ? ""
                        : ", " + idOrMarker(activity) + ", "
                                + idOrMarker(generation) + ", "
                                + idOrMarker(usage));
        write(s);
        writeOptionalAttributes(attributes);
        writeln("");
        return null;
    }

    @Override
    public WasEndedBy newWasEndedBy(QualifiedName id, 
                                    QualifiedName activity,
                                    QualifiedName trigger, 
                                    QualifiedName ender,
                                    XMLGregorianCalendar time,
                                    Collection<Attribute> attributes) {
        String s = "wasEndedBy(" + optionalId(id) + idOrMarker(activity) + ","
                + idOrMarker(trigger) + "," + idOrMarker(ender) + ","
                + timeOrMarker(time) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasGeneratedBy newWasGeneratedBy(QualifiedName id,
                                            QualifiedName entity,
                                            QualifiedName activity,
                                            XMLGregorianCalendar time,
                                            Collection<Attribute> attributes) {
        String s = keyword("wasGeneratedBy") + "(" + optionalId(id)
                + idOrMarker(entity) + "," + idOrMarker(activity) + ","
                + timeOrMarker(time) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasInfluencedBy newWasInfluencedBy(QualifiedName id,
                                              QualifiedName a2,
                                              QualifiedName a1,
                                              Collection<Attribute> attributes) {
        String s = "wasInfluencedBy(" + optionalId(id) + idOrMarker(a2) + ","
                + idOrMarker(a1) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasInformedBy newWasInformedBy(QualifiedName id, QualifiedName a2,
                                          QualifiedName a1,
                                          Collection<Attribute> attributes) {
        String s = "wasInformedBy(" + optionalId(id) + idOrMarker(a2) + ","
                + idOrMarker(a1) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasInvalidatedBy newWasInvalidatedBy(QualifiedName id,
                                                QualifiedName entity,
                                                QualifiedName activity,
                                                XMLGregorianCalendar time,
                                                Collection<Attribute> attributes) {
        String s = keyword("wasInvalidatedBy") + "(" + optionalId(id)
                + idOrMarker(entity) + "," + idOrMarker(activity) + ","
                + timeOrMarker(time) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasStartedBy newWasStartedBy(QualifiedName id,
                                        QualifiedName activity,
                                        QualifiedName trigger,
                                        QualifiedName starter,
                                        XMLGregorianCalendar time,
                                        Collection<Attribute> attributes) {
        String s = "wasStartedBy(" + optionalId(id) + idOrMarker(activity)
                + "," + idOrMarker(trigger) + "," + idOrMarker(starter) + ","
                + timeOrMarker(time) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    public String optionalAttributes(Collection<Attribute> attrs) {
        if ((attrs == null) || (attrs.isEmpty())) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (Attribute attr : attrs) {
            if (first) {
                sb.append(symbol(",[") + attr.toNotationString());
                first = false;
            } else {
                sb.append(symbol(",") + " " + attr.toNotationString());
            }
        }
        if (!first)
            sb.append(symbol("]"));
        return sb.toString();
    }

    final String closing=")";

    public void writeOptionalAttributes(Collection<Attribute> attrs) {
        if ((attrs == null) || (attrs.isEmpty())) {
        } else {
            boolean first = true;
            for (Attribute attr : attrs) {
                if (first) {
                    write(symbol(",["));
                    write(attr.toNotationString());
                    first = false;
                } else {
                    write(symbol(",") + " ");
                    write(attr.toNotationString());
                }
            }
            if (!first)
                write(symbol("]"));
        }
        write (closing);
    }


    private String optionalId(QualifiedName id) {
        return ((id == null) ? "" : (Namespace.getThreadNamespace().qualifiedNameToString(id) + ";"));
    }

    public String processNamespaces(Namespace namespace) {
        String s = "";

        Map<String, String> nss = namespace.getPrefixes();
        String def;
        if ((def = namespace.getDefaultNamespace()) != null) {
            s = s + convertDefaultNamespace("<" + def + ">") + breakline();
        }

        for (String key : nss.keySet()) {
            String uri = nss.get(key);
            if ((key.equals("_") 
        	    || (key.equals("prov"))
        	    || (key.equals("xsd") && NamespacePrefixMapper.XSD_NS.equals(uri)))) {
                // IGNORE, we have just handled it
            } else {
                s = s + convertNamespace(key, "<" + uri + ">") + breakline();
            }
        }

        return s;
    }

    public String showprefix(String s) {
        return s;
    }

    public String showuri(String s) {
        return s;
    }

    @Override
    public void startBundle(QualifiedName bundleId, Namespace namespaces) {
   
        String s = keyword("bundle") + " " + namespaces.qualifiedNameToString(bundleId)
                + breakline();
        s = s + processNamespaces(namespaces);
        writeln(s);


    }

    @Override
    public void startDocument(Namespace namespaces) {
        String s = keyword("document") + breakline();
        s = s + processNamespaces(namespaces);
        write(s);
    }

    public String symbol(String s) {
        return s;
    }

    public String timeOrMarker(XMLGregorianCalendar time) {
        return ((time == null) ? MARKER : time.toString());
    }

    final public void write(String s) {
        try {
            buffer.write(s);
        } catch (IOException e) {
            throw new UncheckedException("NotationConstructor.write() failed", e);
        }
    }

    final public void writeln(String s) {
        try {
            buffer.write(s);
            if (!standaloneExpression)
                buffer.newLine();
        } catch (IOException e) {
            throw new UncheckedException("NotationConstructor.write() failed", e);
        }
    }


    @Override
    public QualifiedName newQualifiedName(String namespace,
                                          String local,
                                          String prefix) {
        return null;
    }

    @Override
    public QualifiedName newQualifiedName(String namespace,
                                          String local,
                                          String prefix,
                                          BuildFlag flag) {
        return null;
    }

    @Override
    public QualifiedAlternateOf newQualifiedAlternateOf(QualifiedName id,
                                                        QualifiedName e2,
                                                        QualifiedName e1,
                                                        Collection<Attribute> attributes) {
        if ((id==null) && (attributes==null || attributes.isEmpty())) {
             newAlternateOf(e2,e1);
             return null;
        }
        String s = keyword("provext:alternateOf") + "(" + optionalId(id)
                + idOrMarker(e2) + "," + idOrMarker(e1) +  optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public QualifiedSpecializationOf newQualifiedSpecializationOf(QualifiedName id,
                                                                  QualifiedName e2,
                                                                  QualifiedName e1,
                                                                  Collection<Attribute> attributes) {
        if ((id==null) && (attributes==null || attributes.isEmpty())) {
            newSpecializationOf(e2,e1);
            return null;
        }
       
        String s = keyword("provext:specializationOf") + "(" + optionalId(id)
                + idOrMarker(e2) + "," + idOrMarker(e1) +  optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public QualifiedHadMember newQualifiedHadMember(QualifiedName id,
                                                    QualifiedName c,
                                                    Collection<QualifiedName> ll,
                                                    Collection<Attribute> attributes) {
        if ((id==null) && (attributes==null || attributes.isEmpty())) {
            newHadMember(c,ll);
            return null;
        }
        if ((ll == null) || (ll.size() == 0)) {
            // strictly speaking it is not a syntactically correct expression,
            // but we print something to support scruffiness
            String s = keyword("provext:hadMember") + "(" + optionalId(id) + idOrMarker(c) + ","
                    + idOrMarker((QualifiedName) null)+  optionalAttributes(attributes) + ")";
            writeln(s);
        } else {
            for (QualifiedName e : ll) {
                String s = keyword("provext:hadMember") + "(" + optionalId(id) + idOrMarker(c) + ","
                        + idOrMarker(e) +  optionalAttributes(attributes) + ")";
                writeln(s);
            }
        }
        // TODO Auto-generated method stub
        return null;
    }

}
