package org.openprovenance.prov.validation;

import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.model.ActedOnBehalfOf;
import org.openprovenance.prov.model.Activity;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.HasType;
import org.openprovenance.prov.model.Identifiable;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.Type;
import org.openprovenance.prov.model.Used;
import org.openprovenance.prov.model.WasAssociatedWith;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;
import org.openprovenance.prov.model.WasEndedBy;
import org.openprovenance.prov.model.WasGeneratedBy;
import org.openprovenance.prov.model.WasInvalidatedBy;
import org.openprovenance.prov.model.WasStartedBy;

public class Types {
    static Logger logger = LogManager.getLogger(Types.class);


    final ProvUtilities u;
    private final Config config;
    private final Indexer indexer;
    final public static String VAL_TYPE_NS = "http://openprovenance.org/validation/type/";
    final public static String VAL_TYPE_IGNORE_NS = "http://openprovenance.org/validation/type/ignore/";

    public final Hashtable<String, Set<String>> illegalOverlaps;

    public Types(Indexer indexer, ProvUtilities u, Config config) {
        this.indexer=indexer;
        this.illegalOverlaps = new Hashtable<String, Set<String>>();
        this.config=config;
        this.u=u;
        initializeTypes(uriTypeIndex);
        initializeOverlaps(illegalOverlaps);
        theURIs.addAll(uriTypeIndex.values());
    }

    public static String activityURI = VAL_TYPE_NS + "Activity";
    public static String entityURI = VAL_TYPE_NS + "Entity";
    public static String agentURI = VAL_TYPE_NS + "Agent";
    public static String generationURI = VAL_TYPE_NS + "Generation";
    public static String usageURI = VAL_TYPE_NS + "Usage";
    public static String communicationURI = VAL_TYPE_NS + "Communication";
    public static String startURI = VAL_TYPE_NS + "Start";
    public static String endURI = VAL_TYPE_NS + "End";
    public static String invalidationURI = VAL_TYPE_NS + "Invalidation";
    public static String derivationURI = VAL_TYPE_NS + "Derivation";
    public static String revisionURI = VAL_TYPE_NS + "Revision";
    public static String quotationURI = VAL_TYPE_NS + "Quotation";
    public static String primarySourceURI = VAL_TYPE_NS + "PrimarySource";
    public static String attributionURI = VAL_TYPE_NS + "Attribution";
    public static String associationURI = VAL_TYPE_NS + "Association";
    public static String delegationURI = VAL_TYPE_NS + "Delegation";
    public static String influenceURI = VAL_TYPE_NS + "Influence";
    public static String bundleURI = VAL_TYPE_NS + "Bundle";
    public static String collectionURI = VAL_TYPE_NS + "Collection";
    public static String emptyCollectionURI = VAL_TYPE_NS + "EmptyCollection";
    public static String personURI = VAL_TYPE_NS + "Person";
    public static String organizationURI = VAL_TYPE_NS + "Organization";
    public static String softwareAgentURI = VAL_TYPE_NS + "SoftwareAgent";
    public static String nonEmptyCollectionURI = VAL_TYPE_NS + "NonEmptyCollection";

    public final Hashtable<Integer, String> uriTypeIndex = new Hashtable<Integer, String>();
    public final Set<String> theURIs=new HashSet<String>();

    public void initializeTypes(Hashtable<Integer, String> types) {
        types.put(0, entityURI);
        types.put(1, activityURI);
        types.put(2, agentURI);
        types.put(3, generationURI);
        types.put(4, usageURI);
        types.put(5, communicationURI);
        types.put(6, startURI);
        types.put(7, endURI);
        types.put(8, invalidationURI);
        types.put(9, derivationURI);
        types.put(10, revisionURI);
        types.put(11, quotationURI);
        types.put(12, primarySourceURI);
        types.put(13, attributionURI);
        types.put(14, associationURI);
        types.put(15, delegationURI);
        types.put(16, influenceURI);
        types.put(17, bundleURI);
        types.put(18, collectionURI);
        types.put(19, emptyCollectionURI);
        types.put(20, personURI);
        types.put(21, organizationURI);
        types.put(22, softwareAgentURI);
        types.put(23, nonEmptyCollectionURI);
    };


    public Set<String> getTypes(Integer[] indexes) {
        HashSet<String> set = new HashSet<String>();
        for (int i : indexes) {
            set.add(uriTypeIndex.get(i));
        }
        return set;
    }

    //FIXME: remove 9, 10, 11, from primarySource line

    // @formatter:off
    void initializeOverlaps(Hashtable<String, Set<String>> illegal) {
        illegal.put(entityURI,           getTypes(new Integer[] {    1,    3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16                            }));
        illegal.put(activityURI,         getTypes(new Integer[] { 0,       3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19                }));
        illegal.put(agentURI,            getTypes(new Integer[] {          3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16                            }));
        illegal.put(generationURI,       getTypes(new Integer[] { 0, 1, 2,    4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(usageURI,            getTypes(new Integer[] { 0, 1, 2, 3,    5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(communicationURI,    getTypes(new Integer[] { 0, 1, 2, 3, 4,    6, 7, 8, 9, 10, 11, 12, 13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(startURI,            getTypes(new Integer[] { 0, 1, 2, 3, 4, 5,    7, 8, 9, 10, 11, 12, 13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(endURI,              getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6,    8, 9, 10, 11, 12, 13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(invalidationURI,     getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7,    9, 10, 11, 12, 13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(derivationURI,       getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,                13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(revisionURI,         getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,                13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(quotationURI,        getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,                13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(primarySourceURI,    getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8,                13, 14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(attributionURI,      getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,     14, 15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(associationURI,      getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,     15,     17, 18, 19, 20, 21, 22, 23}));
        illegal.put(delegationURI,       getTypes(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,         17, 18, 19, 20, 21, 22, 23}));
        illegal.put(influenceURI,        getTypes(new Integer[] { 0, 1, 2, 3,                                               17, 18, 19, 20, 21, 22, 23}));
        illegal.put(bundleURI,           getTypes(new Integer[] {    1,    3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,                               }));
        illegal.put(collectionURI,       getTypes(new Integer[] {    1,    3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16                            }));
        illegal.put(emptyCollectionURI,  getTypes(new Integer[] {    1,    3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,                         23}));
        illegal.put(personURI,           getTypes(new Integer[] {          3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16                            }));
        illegal.put(organizationURI,     getTypes(new Integer[] {          3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16                            }));
        illegal.put(softwareAgentURI,    getTypes(new Integer[] {          3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16                            }));
        illegal.put(nonEmptyCollectionURI,
                getTypes(new Integer[] {    1,    3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,         19                }));

    }
    //@formatter:on

    public Collection<String> conflictingTypes(Collection<String> types) {
        if (types.isEmpty()) {
            return types;
        }

        Set<String> theConflicts= new HashSet<>();


        for (String s : types) {
            Set<String> illegal=illegalOverlaps.get(s);
            if (illegal!=null) {
                Set<String> illegalForS= new HashSet<>(illegal);
                illegalForS.retainAll(types);
                if (!illegalForS.isEmpty()) {
                    theConflicts.addAll(illegalForS);
                    theConflicts.add(s);
                }
            }
        }
        return theConflicts;
    }

    public void addInferredType(QualifiedName ref, String type) {
        String id=ref.getUri();
        addInferredType(id,type);
    }
    public void addInferredType(String id, String type) {
        if (config.isTrue(Config.CONSTRAINT_TYPING)) {
            Set<String> objects = aggregatedTypes.computeIfAbsent(id, k -> new HashSet<>());
            objects.add(type);
        }
    }

    public Hashtable<String, Set<String>> aggregatedTypes= new Hashtable<>();


    public void addDeclaredTypes() {
        logger.debug("updateTypes: ");
        addDeclaredTypes(Entity.class, indexer.entityTable);
        addDeclaredTypes(Activity.class, indexer.activityTable);
        addDeclaredTypes(Agent.class, indexer.agentTable);

        addDeclaredTypes(WasStartedBy.class, indexer.wasStartedByTable);
        addDeclaredTypes(WasEndedBy.class, indexer.wasEndedByTable);
        addDeclaredTypes(WasGeneratedBy.class, indexer.wasGeneratedByTable);
        addDeclaredTypes(WasInvalidatedBy.class, indexer.wasInvalidatedByTable);

        addDeclaredTypes(WasDerivedFrom.class, indexer.wasDerivedFromTable);
        addDeclaredTypes(WasAttributedTo.class, indexer.wasAttributedToTable);
        addDeclaredTypes(WasAssociatedWith.class, indexer.wasAssociatedWithTable);
        addDeclaredTypes(ActedOnBehalfOf.class, indexer.actedOnBehalfOfTable);
        addDeclaredTypes(Used.class, indexer.usedTable);

    }


    public <Entry extends Statement> void addDeclaredTypes(Class<Entry> class1,
                                                           Hashtable<String, Entry> table1) {
        for (String key : table1.keySet()) {
            Entry entry = table1.get(key);
            if (entry != null) {
                logger.debug("updateTypes <T>  to " + key);
                String entryURI=((Identifiable)entry).getId().getUri();
                Set<Type> set=new HashSet<Type>(indexer.getTypeTable(entry).get(entryURI)); // make a copy
                List<Type> types=((HasType) entry).getType();
                //logger.debug("updateTypes <T>  to " + types);

                for (Type type: types) {
                    set.remove(type);
                }
                types.addAll(set);  //TODO: I am not sure what I am adding types to????

                for (Type o: types) {
                    addAsValidatorType(entryURI, o);
                }
                //logger.debug("updateTypes <T>  to " + types);


            } else {
                logger.debug("updateAttributes  coudln't find entry " + key);
                throw new UnsupportedOperationException();
            }
        }
    }


    public void addAsValidatorType(String entryURI, Type t) {
        Object o=t.getValue();
        //       Object o=t.getConvertedValue();
        //new org.openprovenance.prov.model.ValueConverter(ProvFactory.getFactory()));
        logger.debug("++++++++++++++++++ addAsValidatorType " + o);
        if (o instanceof QualifiedName) {
            QualifiedName qn=(QualifiedName) o;
            addInferredType(entryURI, convertToValidatorURI(qn.getUri()));
        } else if (o instanceof  String) {
            addInferredType(entryURI, convertToValidatorURI(o.toString()));
        } else if (o instanceof URI) {
            addInferredType(entryURI, convertToValidatorURI(o.toString()));
        } else {
            addInferredType(entryURI, "unkwnown:" + o);
        }
    }


    public String convertToValidatorURI(String uri) {
        if (uri.startsWith(NamespacePrefixMapper.PROV_NS)) {
            return VAL_TYPE_NS+uri.substring(NamespacePrefixMapper.PROV_NS.length());
        } else if (uri.startsWith(VAL_TYPE_NS)) {
            return VAL_TYPE_IGNORE_NS+uri.substring(VAL_TYPE_NS.length());
        } else {
            return uri;
        }
    }


}
