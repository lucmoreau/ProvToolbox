package org.openprovenance.prov.notation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.model.Entry;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.AlternateOf;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.ModelConstructor;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HadMember;
import org.openprovenance.prov.model.MentionOf;
import org.openprovenance.prov.model.NamedBundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedNameExport;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.SpecializationOf;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.DictionaryMembership;
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
import org.openprovenance.prov.model.exception.UncheckedException;

/** For testing purpose, conversion back to ASN. */

public class NotationConstructor implements ModelConstructor {

    public static final String MARKER = "-";
    boolean abbrev = false;
    final private BufferedWriter buffer;

    final private QualifiedNameExport qnExport;

    public boolean standaloneExpression = false;

    public NotationConstructor(Writer writer, QualifiedNameExport qnExport) {
        this.buffer = new BufferedWriter(writer);
        this.qnExport = qnExport;
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
        return ((qn == null) ? MARKER : qnExport.qualifiedNameToString(qn));
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
        String s = keyword("activity") + "(" + idOrMarker(id) + ","
                + timeOrMarker(startTime) + "," + timeOrMarker(endTime)
                + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public Agent newAgent(QualifiedName id, Collection<Attribute> attributes) {
        String s = keyword("agent") + "(" + idOrMarker(id)
                + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public AlternateOf newAlternateOf(QualifiedName e2, QualifiedName e1) {
        writeln("alternateOf(" + idOrMarker(e2) + "," + idOrMarker(e1) + ")");
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
    public Document newDocument(Namespace namespaces,
                                Collection<Statement> statements,
                                Collection<NamedBundle> bundles) {
        String s = "";

        s = s + keyword("endDocument");

        write(s);
        return null;
    }

    @Override
    public Entity newEntity(QualifiedName id, Collection<Attribute> attributes) {
        String s = keyword("entity") + "(" + idOrMarker(id)
                + optionalAttributes(attributes) + ")";
        writeln(s);
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
    public NamedBundle newNamedBundle(QualifiedName id, Namespace namespace,
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
        String s = keyword("used") + "(" + optionalId(id)
                + idOrMarker(activity) + "," + idOrMarker(entity) + ","
                + timeOrMarker(time) + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasAssociatedWith newWasAssociatedWith(QualifiedName id,
                                                  QualifiedName a,
                                                  QualifiedName ag,
                                                  QualifiedName plan,
                                                  Collection<Attribute> attributes) {
        String s = keyword("wasAssociatedWith") + "(" + optionalId(id)
                + idOrMarker(a) + "," + idOrMarker(ag) + "," + idOrMarker(plan)
                + optionalAttributes(attributes) + ")";
        writeln(s);
        return null;
    }

    @Override
    public WasAttributedTo newWasAttributedTo(QualifiedName id,
                                              QualifiedName e,
                                              QualifiedName ag,
                                              Collection<Attribute> attributes) {
        String s = keyword("wasAttributedTo") + "(" + optionalId(id)
                + idOrMarker(e) + ", " + idOrMarker(ag)
                + optionalAttributes(attributes) + ")";
        writeln(s);
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
        String s = keyword("wasDerivedFrom")
                + "("
                + optionalId(id)
                + idOrMarker(e2)
                + ", "
                + idOrMarker(e1)
                + ((activity == null && generation == null && usage == null) ? ""
                        : ", " + idOrMarker(activity) + ", "
                                + idOrMarker(generation) + ", "
                                + idOrMarker(usage))
                + optionalAttributes(attributes) + ")";
        writeln(s);
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

    private String optionalId(QualifiedName id) {
        return ((id == null) ? "" : (qnExport.qualifiedNameToString(id) + ";"));
    }

    public String processNamespaces(Namespace namespace) {
        String s = "";

        Hashtable<String, String> nss = namespace.getPrefixes();
        String def;
        if ((def = namespace.getDefaultNamespace()) != null) {
            s = s + convertDefaultNamespace("<" + def + ">") + breakline();
        }

        for (String key : nss.keySet()) {
            String uri = nss.get(key);
            if ((key.equals("_") || (key.equals("prov")))) {
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
        String s = keyword("bundle") + " " + qnExport.qualifiedNameToString(bundleId)
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

    public void write(String s) {
        try {
            buffer.write(s);
        } catch (IOException e) {
            throw new UncheckedException("NotationConstructor.write() failed",
                                         e);
        }
    }

    public void writeln(String s) {
        try {
            buffer.write(s);
            if (!standaloneExpression)
                buffer.newLine();
        } catch (IOException e) {
            throw new UncheckedException("NotationConstructor.write() failed",
                                         e);
        }
    }

    @Override
    public QualifiedName newQualifiedName(String namespace, String local,
					  String prefix) {
	return null;
    }

}
