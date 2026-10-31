package org.openprovenance.prov.notation.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.DerivedByInsertionFrom;
import org.openprovenance.prov.model.DerivedByRemovalFrom;
import org.openprovenance.prov.model.DictionaryMembership;
import org.openprovenance.prov.model.Entry;
import org.openprovenance.prov.model.Key;
import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.notation.PROV_NLexer;
import org.openprovenance.prov.notation.PROV_NParser;
import org.openprovenance.prov.notation.ProvNReader;
import org.openprovenance.prov.notation.Utility;

/**
 * Reads PROV-N with both the ANTLR 3 parser (via {@link Utility}) and the ANTLR 4 parser (via {@link ProvNReader}),
 * and compares the Java representations they construct.
 * <p>
 * The ANTLR 3 parser recovers from syntax errors, and only logs them: here, they are counted, so that a document
 * that it reads with errors counts as rejected.
 */
public class ParserComparison {

    public enum Verdict {
        /** Both parsers accept the input, and construct equal documents. */
        SAME,
        /** Both parsers accept the input, but construct different documents. */
        DIFFERENT,
        /** Both parsers reject the input. */
        BOTH_REJECT,
        /**
         * Only the old parser reports syntax errors, but it recovers from them, and constructs the same document as the
         * new parser. For instance, for a marker '-' where PROV-N requires an identifier.
         */
        OLD_RECOVERS,
        /** Only the old parser rejects the input: it fails, or it reports syntax errors and constructs a different document. */
        OLD_REJECTS,
        /** Only the new parser rejects the input. */
        NEW_REJECTS
    }

    public static class Outcome {
        public final String source;
        public final Verdict verdict;
        public final Document oldDocument;
        public final Document newDocument;
        public final String oldFailure;
        public final String newFailure;
        public final String difference;

        Outcome(String source, Verdict verdict, Document oldDocument, Document newDocument, String oldFailure, String newFailure, String difference) {
            this.source = source;
            this.verdict = verdict;
            this.oldDocument = oldDocument;
            this.newDocument = newDocument;
            this.oldFailure = oldFailure;
            this.newFailure = newFailure;
            this.difference = difference;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(verdict + " " + source);
            if (oldFailure != null) sb.append("\n    old: ").append(oldFailure);
            if (newFailure != null) sb.append("\n    new: ").append(newFailure);
            if (difference != null) sb.append("\n    difference: ").append(difference);
            return sb.toString();
        }
    }

    private final ProvFactory pFactory;
    private final Utility u = new Utility(DateTimeOption.PRESERVE, null);
    private final ProvNReader reader;

    public ParserComparison(ProvFactory pFactory) {
        this.pFactory = pFactory;
        this.reader = new ProvNReader(pFactory, DateTimeOption.PRESERVE, null);
    }

    public Outcome compareFile(String filename) {
        OldResult old;
        try {
            old = readWithOldParser(new ANTLRFileStream(filename, "UTF-8"));
        } catch (Throwable t) {
            old = new OldResult(null, List.of(), describe(t));
        }
        Document newDoc = null;
        String newFailure = null;
        try {
            newDoc = reader.readDocument(filename);
        } catch (Throwable t) {
            newFailure = describe(t);
        }
        return outcome(filename, old, newDoc, newFailure);
    }

    public Outcome compareString(String provn) {
        OldResult old = readWithOldParser(new ANTLRStringStream(provn));
        Document newDoc = null;
        String newFailure = null;
        try {
            newDoc = reader.readDocumentFromString(provn);
        } catch (Throwable t) {
            newFailure = describe(t);
        }
        return outcome(provn, old, newDoc, newFailure);
    }

    private Outcome outcome(String source, OldResult old, Document newDoc, String newFailure) {
        Document oldDoc = old.document;
        String oldFailure = old.failure();
        boolean oldAccepts = oldDoc != null && old.syntaxErrors.isEmpty();
        if (newDoc == null) {
            return oldAccepts
                    ? new Outcome(source, Verdict.NEW_REJECTS, oldDoc, null, null, newFailure, null)
                    : new Outcome(source, Verdict.BOTH_REJECT, oldDoc, null, oldFailure, newFailure, null);
        }
        if (oldDoc == null) return new Outcome(source, Verdict.OLD_REJECTS, null, newDoc, oldFailure, null, null);
        String difference = difference(oldDoc, newDoc);
        if (oldAccepts) {
            return new Outcome(source, difference == null ? Verdict.SAME : Verdict.DIFFERENT, oldDoc, newDoc, null, null, difference);
        }
        return new Outcome(source, difference == null ? Verdict.OLD_RECOVERS : Verdict.OLD_REJECTS, oldDoc, newDoc, oldFailure, null, difference);
    }

    /** What the old parser read: possibly a document, possibly with syntax errors, from which it recovered. */
    static class OldResult {
        final Document document;
        final List<String> syntaxErrors;
        final String exception;

        OldResult(Document document, List<String> syntaxErrors, String exception) {
            this.document = document;
            this.syntaxErrors = syntaxErrors;
            this.exception = exception;
        }

        String failure() {
            if (exception != null) return exception;
            if (!syntaxErrors.isEmpty()) return "syntax errors: " + syntaxErrors;
            return null;
        }
    }

    /**
     * Reads a file with the new parser, after checking that the old parser, if it accepts the file, reads the same.
     * @param filename a PROV-N file
     * @return the document read by the new parser
     * @throws AssertionError if the parsers disagree, or if the new parser rejects the file
     */
    public Document readCheckingOldParser(String filename) {
        Outcome o = compareFile(filename);
        if (o.verdict != Verdict.SAME && o.verdict != Verdict.OLD_RECOVERS && o.verdict != Verdict.OLD_REJECTS) {
            throw new AssertionError(o.toString());
        }
        return o.newDocument;
    }

    /** The old reading pipeline of {@link Utility#readDocument(String, ProvFactory)}, which also collects syntax errors. */
    OldResult readWithOldParser(CharStream input) {
        // the old parser communicates with its lexer through a static variable, reset in case a previous parse failed
        PROV_NParser.qnameDisabled = false;

        final List<String> syntaxErrors = new ArrayList<>();
        PROV_NLexer lexer = new PROV_NLexer(input) {
            @Override
            public void reportError(RecognitionException e) {
                syntaxErrors.add("line " + e.line + ":" + e.charPositionInLine + " " + getErrorMessage(e, getTokenNames()));
            }
        };
        TokenStream tokens = new CommonTokenStream(lexer);
        PROV_NParser parser = new PROV_NParser(tokens) {
            @Override
            public void reportError(RecognitionException e) {
                syntaxErrors.add("line " + e.line + ":" + e.charPositionInLine + " " + getErrorMessage(e, getTokenNames()));
                super.reportError(e);
            }

            @Override
            public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
                // silent: collected above
            }
        };
        try {
            CommonTree tree = (CommonTree) parser.document().getTree();
            return new OldResult((Document) u.convertTreeToJavaBean(tree, pFactory), syntaxErrors, null);
        } catch (Throwable t) {
            return new OldResult(null, syntaxErrors, describe(t));
        }
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

        // last, the documents as written in PROV-N, which also shows the prefixes of qualified names
        String w1 = write(d1), w2 = write(d2);
        if (!w1.equals(w2)) return "written as PROV-N:\n" + w1 + "\nvs\n" + w2;
        return null;
    }

    private boolean statementEquals(StatementOrBundle s1, StatementOrBundle s2) {
        if (s1.getKind() != s2.getKind()) return false;
        Collection<Attribute> a1 = pFactory.getAttributes((Statement) s1), a2 = pFactory.getAttributes((Statement) s2);
        if (a1.size() != a2.size() || !new HashSet<>(a1).equals(new HashSet<>(a2))) return false;
        switch (s1.getKind()) {
            // the vanilla beans for these statements, and their Entry and Key, do not implement equals: compared field by field
            case PROV_DICTIONARY_INSERTION: {
                DerivedByInsertionFrom d1 = (DerivedByInsertionFrom) s1, d2 = (DerivedByInsertionFrom) s2;
                return Objects.equals(d1.getId(), d2.getId())
                        && Objects.equals(d1.getNewDictionary(), d2.getNewDictionary())
                        && Objects.equals(d1.getOldDictionary(), d2.getOldDictionary())
                        && entriesEqual(d1.getKeyEntityPair(), d2.getKeyEntityPair());
            }
            case PROV_DICTIONARY_REMOVAL: {
                DerivedByRemovalFrom d1 = (DerivedByRemovalFrom) s1, d2 = (DerivedByRemovalFrom) s2;
                if (!(Objects.equals(d1.getId(), d2.getId())
                        && Objects.equals(d1.getNewDictionary(), d2.getNewDictionary())
                        && Objects.equals(d1.getOldDictionary(), d2.getOldDictionary())
                        && d1.getKey().size() == d2.getKey().size())) return false;
                for (int i = 0; i < d1.getKey().size(); i++) {
                    if (!keyEquals(d1.getKey().get(i), d2.getKey().get(i))) return false;
                }
                return true;
            }
            case PROV_DICTIONARY_MEMBERSHIP: {
                DictionaryMembership d1 = (DictionaryMembership) s1, d2 = (DictionaryMembership) s2;
                return Objects.equals(d1.getDictionary(), d2.getDictionary())
                        && entriesEqual(d1.getKeyEntityPair(), d2.getKeyEntityPair());
            }
            default:
                return s1.equals(s2);
        }
    }

    private static boolean entriesEqual(List<Entry> l1, List<Entry> l2) {
        if (l1.size() != l2.size()) return false;
        for (int i = 0; i < l1.size(); i++) {
            if (!keyEquals(l1.get(i).getKey(), l2.get(i).getKey())) return false;
            if (!Objects.equals(l1.get(i).getEntity(), l2.get(i).getEntity())) return false;
        }
        return true;
    }

    private static boolean keyEquals(Key k1, Key k2) {
        return Objects.equals(k1.getValue(), k2.getValue()) && Objects.equals(k1.getType(), k2.getType());
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
