package org.openprovenance.prov.template.types;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.ProxyManagement;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyClientInterface;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyMakerInterface;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TypesRecordProcessor  {
    static final ObjectMapper om=new ObjectMapper();

    final Map<Integer,Map<String, Integer>> levelTypeIndex;
    final Map<Integer,Map<Set<Integer>, Integer>> levelTypeSetIndex;
    private final List<Pair<String, Object[]>> records;
    private final Map<String, Integer> knownRelations;
    private final Map<String, Integer> allRelations;
    private final int relationOffset;
    private final int levelOffset;
    private int relationCount;

    final Map<Integer,Map<List<List<Integer>>, Integer>> levelRelTypeSetIndex;


    public TypesRecordProcessor(Map<String, Integer> knownLevel0TypeIndex, Map<Set<Integer>, Integer> knownTypesSets, Map<String, Integer> knownRelations, int relationOffset, int levelOffset, List<Pair<String, Object[]>> records) {
        this.levelTypeIndex=new HashMap<>();
        this.levelTypeIndex.put(0, new HashMap<>(knownLevel0TypeIndex));

        this.levelTypeSetIndex=new HashMap<>();
        this.levelTypeSetIndex.put(0, new HashMap<>(knownTypesSets));
        
        this.knownRelations=knownRelations;
        this.allRelations=new HashMap<>();
        this.allRelations.putAll(knownRelations);
        this.relationCount=newPossibleIndex(allRelations.values())+100;

        this.levelRelTypeSetIndex=new HashMap<>();

        this.relationOffset=relationOffset;
        this.levelOffset=levelOffset;

        this.records=records;

    }

    public void process(String methodName, Object [] args) {
        records.add(Pair.of(methodName,args));
    }

    final Map<String, Object> result=new HashMap<>();



    public Map<String, Integer> levelN(HashMap<String, FileBuilder> registry, HashMap<String, Object> clientRegistry, ProxyManagement pm, Map<String, Integer> mapLevelN, int levelNext) {
        final Map<String, Collection<int[]>> mapLevelNP1 = new HashMap<>();
        final Map<String, Collection<List<Integer>>> mapLevelNP1pretty = new HashMap<>();

        for (Pair<String, Object[]> pair: records) {
            String methodName=pair.getKey();
            Object[] record=pair.getRight();
            FileBuilder builder=registry.get(methodName);
            Object remoteClientBuilder=clientRegistry.get(methodName);

            ProxyMakerInterface makerBuilder=pm.facadeProxy(ProxyMakerInterface.class,builder);
            final ProxyClientInterface clientBuilder=pm.facadeProxy(ProxyClientInterface.class,remoteClientBuilder);

            Object [] typedRecord= (Object[]) makerBuilder.make(record,makerBuilder.getTypedRecord());

            final Map<String, Collection<int[]>> mapLevelNP1_tmp = new HashMap<>();

            makerBuilder.propagateTypes(typedRecord, mapLevelN, mapLevelNP1_tmp);
            Map<String, Collection<List<Integer>>> mapLevelNP1pretty_tmp=mapLevelNP1_tmp.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s)-> mapLevelNP1_tmp.get(s).stream().map(a -> constructType(a,clientBuilder)).collect(Collectors.toList())));

            mapLevelNP1_tmp.keySet().forEach(key -> {
                mapLevelNP1.computeIfAbsent(key, s->new LinkedList<>());
                mapLevelNP1.get(key).addAll(mapLevelNP1_tmp.get(key));
            });

            mapLevelNP1pretty_tmp.keySet().forEach(key -> {
                mapLevelNP1pretty.computeIfAbsent(key, s->new LinkedList<>());
                mapLevelNP1pretty.get(key).addAll(mapLevelNP1pretty_tmp.get(key));
            });


        }

        result.put("allRelations", allRelations);
        result.put("mapLevel"+levelNext, mapLevelNP1);


        Map<String, List<List<Integer>>> sortedMapLevelNP1pretty=mapLevelNP1pretty.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s) -> mapLevelNP1pretty.get(s).stream().sorted(collectionComparator).collect(Collectors.toList())));

        result.put("level"+levelNext, sortedMapLevelNP1pretty);


        this.levelRelTypeSetIndex.computeIfAbsent(levelNext, n -> new HashMap<>());
        Map<List<List<Integer>>, Integer> levelNRelTypeSetIndex=levelRelTypeSetIndex.get(levelNext);
        int count=newPossibleIndex(levelNRelTypeSetIndex.values(), levelOffset * levelNext+ relationOffset);

        for (List<List<Integer>> coll: sortedMapLevelNP1pretty.values()) {
           if (levelNRelTypeSetIndex.get(coll)==null) {
               levelNRelTypeSetIndex.put(coll,count++);
           }
        }

        result.put("levelNRelTypeSetIndex" + levelNext, levelNRelTypeSetIndex);

        Map<String, Integer> levelNS=sortedMapLevelNP1pretty.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s) -> levelNRelTypeSetIndex.get(sortedMapLevelNP1pretty.get(s))));

        result.put("level"+levelNext+"S", levelNS);

        return levelNS;


    }

    public Map<String, Collection<Integer>> computeTypesPerNode(int bound) {
        Map<String,Collection<Integer>> types=new HashMap<>();
        for (int count=0; count<bound; count++) {
            Map<String, Integer> levelNS= (Map<String, Integer>) result.get("level"+count+"S");
            levelNS.keySet().forEach(k -> {
                types.computeIfAbsent(k, n -> new HashSet<>());
                types.get(k).add(levelNS.get(k));
                });
        }
        result.put("allTypes",types);
        return types;
    }


    public Map<Integer, Integer> computeFeatureVector(Map<String, Collection<Integer>> types) {
        Map<Integer,Integer> counts=new HashMap<>();
        final Collection<Collection<Integer>> allCollections = types.values();
        for (Collection<Integer> coll: allCollections) {
            for (int value: coll) {
                counts.computeIfAbsent(value, k -> 0);
                counts.put(value,1+counts.get(value));
            }
        }

        result.put("counts", counts);
        return counts;

    }



    public List<Integer> constructType(int[] typeRecord, ProxyClientInterface clientBuilder) {
        StringBuffer sb=new StringBuffer();

        // [ 8, 9, 100001, 4 ]
        int out=typeRecord[0];
        int outType=typeRecord[1];
        int in=typeRecord[3];
        int inType=typeRecord[2];

        sb.append(clientBuilder.getName());
        sb.append(".");
        sb.append(clientBuilder.getPropertyOrder()[out]);
        sb.append(".");
        sb.append(niceRelationName(StatementOrBundle.Kind.values()[outType]));
        sb.append(".");
        sb.append(clientBuilder.getPropertyOrder()[in]);

        String rel=sb.toString();
        if (allRelations.get(rel)==null) {
            allRelations.put(rel,relationCount);
            relationCount++;
        }

        return List.of(allRelations.get(rel),inType);

    }

    private static String niceRelationName(StatementOrBundle.Kind value) {
        switch (value) {
            case PROV_ENTITY:
                return "ent";
            case PROV_ACTIVITY:
                return "act";
            case PROV_AGENT:
                return "ag";
            case PROV_USAGE:
                return "usd";
            case PROV_GENERATION:
                return "wgb";
            case PROV_INVALIDATION:
                return "wib";
            case PROV_START:
                return "wsb";
            case PROV_END:
                return "web";
            case PROV_COMMUNICATION:
                return "winf";
            case PROV_DERIVATION:
                return "wdf";
            case PROV_ASSOCIATION:
                return "waw";
            case PROV_ATTRIBUTION:
                return "wat";
            case PROV_DELEGATION:
                return "aobo";
            case PROV_INFLUENCE:
                return "winfl";
            case PROV_ALTERNATE:
                return "alt";
            case PROV_SPECIALIZATION:
                return "spec";
            case PROV_MENTION:
                return "mention";
            case PROV_MEMBERSHIP:
                return "mem";
            case PROV_BUNDLE:
                return "bun";
            case PROV_DICTIONARY_INSERTION:
            case PROV_DICTIONARY_REMOVAL:
            case PROV_DICTIONARY_MEMBERSHIP:
            default:
                throw new IllegalStateException("Unexpected value: " + value);
        }
    }

    public Map<String, Integer> level0(Map<QualifiedName, Set<String>> knownTypeMap, Map<QualifiedName, Set<String>> unknownTypeMap) {
        Map<String, Set<String>> knownTypeMap2  =  knownTypeMap.keySet().stream().collect(Collectors.toMap(QualifiedName::getUri, knownTypeMap::get));
        Map<String, Set<String>> unknownTypeMap2=unknownTypeMap.keySet().stream().collect(Collectors.toMap(QualifiedName::getUri, unknownTypeMap::get));

        final Map<String, Integer> level0TypeIndex = levelTypeIndex.get(0);
        final Collection<Integer> level0Values = level0TypeIndex.values();

        Set<String> allValues = flattenAllValues(knownTypeMap2, unknownTypeMap2);
        Set<String> newValues = findNewValues(allValues, level0TypeIndex);


        int nextIndex= newPossibleIndex(level0Values);

        List<String> ordered=newValues.stream().sorted().collect(Collectors.toList());
        for (String s: ordered) {
            level0TypeIndex.put(s,nextIndex);
            nextIndex++;
        };

        Map<String, Set<Integer>>   knownTypeMap3=  knownTypeMap2.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s)->   knownTypeMap2.get(s).stream().map(level0TypeIndex::get).collect(Collectors.toSet())));  //.stream().map(v -> level0TypeIndex.get(v)).collect(Collectors.toSet()))
        Map<String, Set<Integer>> unknownTypeMap3=unknownTypeMap2.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s)-> unknownTypeMap2.get(s).stream().map(level0TypeIndex::get).collect(Collectors.toSet())));  //.stream().map(v -> level0TypeIndex.get(v)).collect(Collectors.toSet()))

        Map<String, Set<Integer>> level0 = mergeTypeMaps(knownTypeMap3, unknownTypeMap3);


        result.put("level0TypeIndex", level0TypeIndex);
        result.put("knownTypeMap2",knownTypeMap2);
        result.put("knownTypeMap3",knownTypeMap3);
        result.put("unknownTypeMap2",unknownTypeMap2);
        result.put("unknownTypeMap3",unknownTypeMap3);
        result.put("level0",level0);


        Set<Set<Integer>> allSetValues= new HashSet<>(level0.values());
        final Map<Set<Integer>, Integer> level0TypeSetIndex = levelTypeSetIndex.get(0);

        Set<Set<Integer>> knownTypeSets=level0TypeSetIndex.keySet();

        Set<Set<Integer>> newTypeSets=new HashSet<>(allSetValues);
        newTypeSets.removeAll(knownTypeSets);

        final Collection<Integer> level0SValues = level0TypeSetIndex.values();

        int nextIndexS= newPossibleIndex(level0SValues);



        List<Set<Integer>> orderedS=newTypeSets.stream().sorted(collectionComparator).collect(Collectors.toList());
        for (Set<Integer> s: orderedS) {
            level0TypeSetIndex.put(s,nextIndexS);
            nextIndexS++;
        }


        result.put("level0STypeIndex",level0TypeSetIndex);


        Map<String, Integer> level0S=level0.keySet().stream().collect(Collectors.toMap((String s)->s, (String s)->level0TypeSetIndex.get(level0.get(s))));

        result.put("level0S",level0S);


        return level0S;

    }
    static Comparator<Collection<Integer>> collectionComparator = (o1, o2) -> {
        String s1=o1.toString();
        String s2=o2.toString();
        return s1.compareTo(s2);
    };


    public Map<String, Object> getResult() {
        return result;

    }

    public void computeLevels(HashMap<String,FileBuilder> registry, HashMap<String,Object> clientRegistry, ProxyManagement pm, Map<QualifiedName, Set<String>> knownTypeMap, Map<QualifiedName, Set<String>> unknownTypeMap, int bound) {
        Map<String, Integer> level0= level0(knownTypeMap, unknownTypeMap);

        Map<String, Integer> level=level0;
        for (int i=1; i <bound; i++) {
            level= levelN(registry, clientRegistry, pm, level, i);
        }
    }

    private Map<String, Set<Integer>> mergeTypeMaps(Map<String, Set<Integer>> knownTypeMap3, Map<String, Set<Integer>> unknownTypeMap3) {
        Map<String, Set<Integer>> level0=new HashMap<>(unknownTypeMap3);

        knownTypeMap3.forEach((k, v) -> level0.merge(k, v, (Set<Integer> v1, Set<Integer> v2) -> {
            Set<Integer> set = new TreeSet<>(v1);
            set.addAll(v2);
            return set;
        }));
        return level0;
    }

    private int newPossibleIndex(Collection<Integer> level0Values, int defaultValue) {
        return (level0Values.isEmpty() ? defaultValue : Collections.max(level0Values)) + 1;
    }
    private int newPossibleIndex(Collection<Integer> level0Values) {
        return (level0Values.isEmpty() ? 0 : Collections.max(level0Values)) + 1;
    }

    private Set<String> findNewValues(Set<String> allValues, Map<String, Integer> level0TypeIndex) {
        Set<String> newValues = new HashSet<>(allValues);
        newValues.removeAll(level0TypeIndex.keySet());
        return newValues;
    }

    private Set<String> flattenAllValues(Map<String, Set<String>> knownTypeMap2, Map<String, Set<String>> unknownTypeMap2) {
        Set<String> allValues1=  knownTypeMap2.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        Set<String> allValues2= unknownTypeMap2.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        Set<String> allValues=new HashSet<>();
        allValues.addAll(allValues1);
        allValues.addAll(allValues2);
        return allValues;
    }


}
