package org.openprovenance.prov.notation.test;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.notation.Utility;

/** Compares documents: their namespaces, their statements and bundles, and what they are written as in PROV-N. */
public class DocumentComparison {

    private final ProvFactory pFactory;
    private final Utility u = new Utility(DateTimeOption.PRESERVE, null);

    public DocumentComparison(ProvFactory pFactory) {
        this.pFactory = pFactory;
    }

    /** A description of the first difference between two documents, or null if they are equal. */
    public String difference(Document d1, Document d2) {
        String nsDifference = namespaceDifference("document", d1.getNamespace(), d2.getNamespace());
        if (nsDifference != null) return nsDifference;

        List<StatementOrBundle> l1 = d1.getStatementOrBundle();
        List<StatementOrBundle> l2 = d2.getStatementOrBundle();
        if (l1.size() != l2.size()) {
            return "number of statements and bundles: " + l1.size() + " vs " + l2.size();
        }
        for (int i = 0; i < l1.size(); i++) {
            StatementOrBundle s1 = l1.get(i);
            StatementOrBundle s2 = l2.get(i);
            if (s1 instanceof Bundle && s2 instanceof Bundle) {
                Bundle b1 = (Bundle) s1, b2 = (Bundle) s2;
                if (!Objects.equals(b1.getId(), b2.getId())) return "bundle identifier: " + b1.getId() + " vs " + b2.getId();
                nsDifference = namespaceDifference("bundle " + b1.getId(), b1.getNamespace(), b2.getNamespace());
                if (nsDifference != null) return nsDifference;
                List<Statement> bl1 = b1.getStatement(), bl2 = b2.getStatement();
                if (bl1.size() != bl2.size()) return "number of statements in bundle " + b1.getId() + ": " + bl1.size() + " vs " + bl2.size();
                for (int j = 0; j < bl1.size(); j++) {
                    if (!statementEquals(bl1.get(j), bl2.get(j))) return "statement " + j + " in bundle " + b1.getId() + ": " + bl1.get(j) + " vs " + bl2.get(j);
                }
            } else if (!statementEquals(s1, s2)) {
                return "statement " + i + ": " + s1 + " vs " + s2;
            }
        }

        // last, the documents as written in PROV-N, which also shows the prefixes of qualified names; the order of the
        // lines is left out, since that of the prefix declarations does not matter, and that of statements is checked above
        String w1 = write(d1), w2 = write(d2);
        if (!sortedLines(w1).equals(sortedLines(w2))) return "written as PROV-N:\n" + w1 + "\nvs\n" + w2;
        return null;
    }

    private static List<String> sortedLines(String s) {
        return s.lines().sorted().collect(Collectors.toList());
    }

    private boolean statementEquals(StatementOrBundle s1, StatementOrBundle s2) {
        if (s1.getKind() != s2.getKind() || !s1.equals(s2)) return false;
        Collection<Attribute> a1 = pFactory.getAttributes((Statement) s1), a2 = pFactory.getAttributes((Statement) s2);
        return a1.size() == a2.size() && new HashSet<>(a1).equals(new HashSet<>(a2));
    }

    private static String namespaceDifference(String where, Namespace n1, Namespace n2) {
        if (n1 == null || n2 == null) {
            return (n1 == n2) ? null : "namespace of " + where + ": " + n1 + " vs " + n2;
        }
        Map<String, String> p1 = n1.getPrefixes(), p2 = n2.getPrefixes();
        if (!p1.equals(p2)) return "prefixes of " + where + ": " + p1 + " vs " + p2;
        if (!Objects.equals(n1.getDefaultNamespace(), n2.getDefaultNamespace())) {
            return "default namespace of " + where + ": " + n1.getDefaultNamespace() + " vs " + n2.getDefaultNamespace();
        }
        return null;
    }

    private String write(Document doc) {
        try {
            return u.convertBeanToSyntaxTree(doc, pFactory);
        } catch (Throwable t) {
            return "cannot be written: " + describe(t);
        }
    }

    static String describe(Throwable t) {
        String s = t.getClass().getSimpleName() + ": " + t.getMessage();
        return s.length() > 400 ? s.substring(0, 400) + "..." : s;
    }
}
