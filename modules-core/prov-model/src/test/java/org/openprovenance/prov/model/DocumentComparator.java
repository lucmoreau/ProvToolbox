package org.openprovenance.prov.model;

import org.openprovenance.prov.vanilla.HasAttributes;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DocumentComparator {
    private final DocumentEquality documentEquality;

    public DocumentComparator(DocumentEquality documentEquality) {
        this.documentEquality = documentEquality;
    }

    public void compareDocuments(Document doc, Document doc2, boolean check) {
        //     assertTrue("self doc equality", doc.equals(doc));
        //      assertTrue("self doc2 equality", doc2.equals(doc2));

        if (check) {
            boolean result = this.documentEquality.check(doc, doc2);
            if (!result) {
                System.out.println("Pre-write graph: " + doc);
                System.out.println("Read graph: " + doc2);

                System.out.println("test0: " + doc.getStatementOrBundle().get(0));
                System.out.println("test0: " + doc2.getStatementOrBundle().get(0));
                System.out.println("test1: " + doc.getStatementOrBundle().get(0).getClass());
                System.out.println("test1: " + doc2.getStatementOrBundle().get(0).getClass());

                System.out.println("test: " + (doc.getStatementOrBundle().get(0)).equals((doc2.getStatementOrBundle().get(0))));

                if ((doc.getStatementOrBundle().get(0) instanceof HasAttributes)
                        &&
                        (doc2.getStatementOrBundle().get(0) instanceof HasAttributes)) {

                    System.out.println("test2  (attr): " + compareSet(((HasAttributes) doc.getStatementOrBundle().get(0)).getAttributes(), ((HasAttributes) doc2.getStatementOrBundle().get(0)).getAttributes()));

                }

                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.Identifiable)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.Identifiable)){


                    System.out.println("test3  (I): " + compare(((org.openprovenance.prov.model.Identifiable) doc.getStatementOrBundle().get(0)).getId(), ((((org.openprovenance.prov.model.Identifiable) doc2.getStatementOrBundle().get(0)).getId()))));

                }

                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasLabel)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasLabel) ){


                    System.out.println("test4  (L): " + ((org.openprovenance.prov.model.HasLabel) doc.getStatementOrBundle().get(0)).getLabel().equals((((org.openprovenance.prov.model.HasLabel) doc2.getStatementOrBundle().get(0)).getLabel())));
                    System.out.println("test4  (L): " + compareSet(((org.openprovenance.prov.model.HasLabel) doc.getStatementOrBundle().get(0)).getLabel(), ((org.openprovenance.prov.model.HasLabel) doc2.getStatementOrBundle().get(0)).getLabel()));
                    System.out.println("test4a (L): " + ((org.openprovenance.prov.model.HasLabel) doc.getStatementOrBundle().get(0)).getLabel());
                    System.out.println("test4b (L): " + ((org.openprovenance.prov.model.HasLabel) doc2.getStatementOrBundle().get(0)).getLabel());
                }

                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasType)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasType)){

                    System.out.println("test5  (T): " + ((org.openprovenance.prov.model.HasType) doc.getStatementOrBundle().get(0)).getType().equals((((org.openprovenance.prov.model.HasType) doc2.getStatementOrBundle().get(0)).getType())));
                }

                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasOther)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasOther)){

                    System.out.println("test6  (O): " + ((org.openprovenance.prov.model.HasOther) doc2.getStatementOrBundle().get(0)).getOther().equals(((org.openprovenance.prov.model.HasOther) doc2.getStatementOrBundle().get(0)).getOther()));
                    System.out.println("test6b (O): " + ((org.openprovenance.prov.model.HasOther) doc.getStatementOrBundle().get(0)).getOther());
                    System.out.println("test6c (O): " + ((org.openprovenance.prov.model.HasOther) doc2.getStatementOrBundle().get(0)).getOther());
                }

                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasRole)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasRole)){
                    System.out.println("test7  (R): " + compareSet(((org.openprovenance.prov.model.HasRole) doc.getStatementOrBundle().get(0)).getRole(), ((((org.openprovenance.prov.model.HasRole) doc2.getStatementOrBundle().get(0)).getRole()))));
                }
                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasLocation)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasLocation)){
                    System.out.println("test8  (Loc): " + compareSet(((org.openprovenance.prov.model.HasLocation) doc.getStatementOrBundle().get(0)).getLocation(), ((org.openprovenance.prov.model.HasLocation) doc2.getStatementOrBundle().get(0)).getLocation()));
                    System.out.println("test8a (Loc): " + ((org.openprovenance.prov.model.HasLocation) doc.getStatementOrBundle().get(0)).getLocation());
                    System.out.println("test8b (Loc): " + ((org.openprovenance.prov.model.HasLocation) doc2.getStatementOrBundle().get(0)).getLocation());
                }
                if ((doc.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasValue)
                        && (doc2.getStatementOrBundle().get(0) instanceof org.openprovenance.prov.model.HasValue)){
                    System.out.println("test9  (V): " + compare(((org.openprovenance.prov.model.HasValue) doc.getStatementOrBundle().get(0)).getValue(), ((org.openprovenance.prov.model.HasValue) doc2.getStatementOrBundle().get(0)).getValue()));
                    System.out.println("test9a (V): " + ((org.openprovenance.prov.model.HasValue) doc.getStatementOrBundle().get(0)).getValue());
                    System.out.println("test9b (V): " + ((org.openprovenance.prov.model.HasValue) doc2.getStatementOrBundle().get(0)).getValue());
                }




            }
            assertTrue("doc equals doc2", result);
        } else {
            actionIfNoCheck(doc, doc2);
        }


    }

    public void actionIfNoCheck(Document doc, Document doc2) {
        //assertNotEquals("doc distinct from doc2", doc, doc2);
    }


    private <E>boolean compare(E value1, E value2) {
        if (value1==null) return (value2==null);
        if (value2==null) return false;
        return value1.equals(value2);
    }


    public <E> boolean  compareSet(List<E> a1, List<E> a2) {
        Set<E> set1 = new HashSet<>(a1);
        Set<E> set2 = new HashSet(a2);
        return set1.equals(set2);
    }

    public <E> boolean  compareSet(Collection<E> a1, Collection<E> a2) {
        Set<E> set1 = new HashSet<>(a1);
        Set<E> set2 = new HashSet(a2);
        return set1.equals(set2);
    }


}
