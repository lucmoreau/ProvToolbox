package org.openprovenance.prov.template.types;


import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.apache.commons.lang.ArrayUtils;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.template.log2prov.FileBuilder;
import org.openprovenance.prov.template.log2prov.ProxyManagement;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyClientInterface;
import org.openprovenance.prov.template.log2prov.interfaces.ProxyMakerInterface;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TypesRecordProcessor  {

    private final Map<Integer,Map<String, Integer>> levelTypeIndex;
    private final Map<Integer,Map<Set<Integer>, Integer>> levelTypeSetIndex;
    private final List<Pair<String, Object[]>> records;
    private final Map<String, Integer> knownRelations;
    private final Map<String, Integer> allRelations;
    private final int relationOffset;
    private final int levelOffset;
    private final Map<String,String> translation;
    private final Map<Integer,Map<List<List<Integer>>, Integer>> levelRelTypeSetIndex;
    private final int levelNumber;
    private final boolean addLevel0ToAllLevels;
    private final Map<String, Map<String, BiFunction<Object,String,Collection<String>>>> propertyConverters;

    // stateful
    private int relationCount;
    private final Collection<String> rejectedTypes;


    public TypesRecordProcessor(Map<String, Integer> knownLevel0TypeIndex, Map<Set<Integer>, Integer> knownTypesSets, Map<String, Integer> knownRelations, int relationOffset, int levelOffset, Map<String, String> translation, int levelNumber, boolean addLevel0ToAllLevels, Map<String, Map<String, List<String>>> propertyConverters, Collection<String> rejectedTypes, List<Pair<String, Object[]>> records) {
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
        this.translation=translation;

        this.records=records;
        this.rejectedTypes=rejectedTypes;

        this.levelNumber=levelNumber;
        this.addLevel0ToAllLevels=addLevel0ToAllLevels;
        this.propertyConverters=(propertyConverters==null)? null:initialisePropertyConverters(propertyConverters);

    }


    public void process(String methodName, Object [] args) {
        records.add(Pair.of(methodName,args));
    }

    final Map<String, Object> result=new HashMap<>();

    final TMap tMap=new TMap();


    public Map<String, Integer> levelN(HashMap<String, FileBuilder> registry, HashMap<String, Object> clientRegistry, ProxyManagement pm, Map<String, Integer> mapLevelN, int levelNext, Map<String, Integer> mapLevel0) {
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

            makerBuilder.propagateTypes(typedRecord, mapLevelN, mapLevelNP1_tmp, mapLevel0);
            final Map<String, Collection<int[]>> mapLevelNP1_tmp2=mapLevelNP1_tmp.keySet().stream().collect(Collectors.toMap((k)->k, (k) -> filterTypes(mapLevelNP1_tmp.get(k),clientBuilder,tMap)));
            Map<String, Collection<List<Integer>>> mapLevelNP1pretty_tmp=mapLevelNP1_tmp2.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s)-> mapLevelNP1_tmp2.get(s).stream().map(a -> constructType(a,clientBuilder,tMap)).collect(Collectors.toList())));

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
        tMap.allRelations=swap(allRelations);
        result.put("tmap",tMap);

        result.put("mapLevel"+levelNext, mapLevelNP1);


        Map<String, List<List<Integer>>>    sortedMapLevelNP1pretty=mapLevelNP1pretty.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s) -> mapLevelNP1pretty.get(s).stream().sorted(collectionComparator).collect(Collectors.toList())));


        // optionally add level0 type (if -addLevel0 is set)
        final Map<String, List<List<Integer>>> tmp_finalSortedMapLevelNP1pretty = sortedMapLevelNP1pretty;
        sortedMapLevelNP1pretty=(!addLevel0ToAllLevels)?tmp_finalSortedMapLevelNP1pretty:tmp_finalSortedMapLevelNP1pretty.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s) -> {
            List<List<Integer>> ll=tmp_finalSortedMapLevelNP1pretty.get(s);
            ll.add(0, List.of(-1,mapLevel0.get(s)));
            return ll;
        }));



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

        tMap.assign("levelNRelTypeSetIndex", levelNext, swap(levelNRelTypeSetIndex));

        Map<String, List<List<Integer>>> finalSortedMapLevelNP1pretty1 = sortedMapLevelNP1pretty;
        Map<String, Integer> levelNS=sortedMapLevelNP1pretty.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s) -> levelNRelTypeSetIndex.get(finalSortedMapLevelNP1pretty1.get(s))));

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
        tMap.features=counts;

        tMap.merge();

        //Map<Integer,List<String>> descriptors= tMap.allLevelsCompact.keySet().stream().collect(Collectors.toMap((Integer k) -> k, this::getDescriptors));
        Map<Integer,List<Descriptor>> structuredDescriptors= tMap.allLevelsCompact.keySet().stream().collect(Collectors.toMap((Integer k) -> k, this::getStructuredDescriptors));
        Map<Integer,List<String>> descriptors=structuredDescriptors.keySet().stream().collect(Collectors.toMap((Integer k) -> k, (Integer k) -> structuredDescriptors.get(k).stream().map(d -> d.toText(this::translateRelation)).collect(Collectors.toList())));

        tMap.descriptors=descriptors;
        tMap.structuredDescriptors =structuredDescriptors;

        return counts;

    }

    /*
    public List<String> getDescriptors(Integer k) {
        final Map<List<Integer>, Long> m=tMap.allLevelsCompact.get(k);
        return m.keySet().stream().map(ss->convertToDescriptor(ss,m.get(ss)).toText()).collect(Collectors.toList());
    }

    private String convertToTextOLD(List<Integer> ll, Long count) {
        if (ll.get(0)==-1) {  // was added when -addLevel0, the next element is a level0 type value
            return getDescriptorS0(ll.get(1));
        } else {
            return numeral(count) + translateRelation(tMap.allRelations.get(ll.get(0))) + "(" + ((ll.get(1) < levelOffset) ? getDescriptorS0(ll.get(1)) : joinStrings(getDescriptors(ll.get(1)))) + ")";
        }
    }

    private String convertToText(List<Integer> ll, Long count) {
        return convertToDescriptor(ll,count).toText();
    }
   */

    public List<Descriptor> getStructuredDescriptors(Integer k) {
        return (k < levelOffset) ? getStructuredDescriptors0(k) : getStructuredDescriptorsN(k);
    }

    public List<Descriptor> getStructuredDescriptorsN(Integer k) {
        Map<List<Integer>, Long> m=tMap.allLevelsCompact.get(k);
        return m.keySet().stream().map(ss->convertToDescriptor(ss,m.get(ss))).sorted().collect(Collectors.toList());
    }

    public List<Descriptor> getStructuredDescriptors0(Integer k) {
        //return tMap.level0S.get(k).stream().map(d -> new DescriptorAtom(getDescriptor0(d))).collect(Collectors.toList());
        return Collections.singletonList(new DescriptorAtom(k, tMap.level0S.get(k).stream().map(this::getDescriptor0).collect(Collectors.toList())));
    }

    public List<String> getStructuredDescriptorString0(Integer k) {
        return tMap.level0S.get(k).stream().map(this::getDescriptor0).collect(Collectors.toList());
    }

    private Descriptor convertToDescriptor(List<Integer> ll, Long count) {
        if (ll.get(0)==-1) {  // when -addLevel0 option is provided, the next element is a level0 type value
            return new DescriptorAtom(ll.get(1),getStructuredDescriptorString0(ll.get(1)));
        } else {
            return new DescriptorTree(ll.get(1), count, tMap.allRelations.get(ll.get(0)), getStructuredDescriptors(ll.get(1)));
        }
    }

    static String numeral(Long count) {
        if (1L==count) return "";
        return  count + " ";
    }

    //Map<String,String> translation=Map.of("template_block.produced.wdf.consumed1", "wdf1", "template_block.produced.wdf.consumed2", "wdf2");

    private String translateRelation(String s) {
        if (translation!=null) {
            return translation.getOrDefault(s,s);
        }
        return s;
    }

    /*
    public String getDescriptorS0(Integer k) {
        return tMap.level0S.get(k).stream().map(this::getDescriptor0).collect(Collectors.joining(",", "", ""));
    }

    public String joinStrings(List<String> ll) {
        return ll.stream().collect(Collectors.joining(",", "", ""));
    }


     */
    public String getDescriptor0(Integer k) {
        return  localName(tMap.level0.get(k));
    }

    public static String localName(String s) {
        int pos=s.lastIndexOf("#");
        if (pos>0) return s.substring(pos+1);
        pos=s.lastIndexOf("/");
        if (pos>0) return s.substring(pos+1);
        return s;
    }


    public Collection<int[]> filterTypes(Collection<int[]> types, ProxyClientInterface clientBuilder, TMap tMap) {
        return types.stream().filter((r) -> filterTypeRecord(r,clientBuilder,tMap)).collect(Collectors.toList());
    }

    public boolean filterTypeRecord(int[] typeRecord, ProxyClientInterface clientBuilder, TMap tMap) {

        if ((rejectedTypes==null) || rejectedTypes.isEmpty()) return true;

        // [ 8, 9, 100001, 4 ]
        int out=typeRecord[0];
        int outType=typeRecord[1];
        int relType=typeRecord[2];
        int inType=typeRecord[3];
        int in=typeRecord[4];

        String rel = prettifyType(clientBuilder, tMap, out, outType, relType, in);



        final boolean result = !rejectedTypes.contains(rel);
        if (result) System.out.println("filter Type Record: " + rel);
        return result;
    }

    public List<Integer> constructType(int[] typeRecord, ProxyClientInterface clientBuilder, TMap tMap) {

        // [ 8, 9, 100001, 4 ]
        int out=typeRecord[0];
        int outType=typeRecord[1];
        int relType=typeRecord[2];
        int inType=typeRecord[3];
        int in=typeRecord[4];

        String rel = prettifyType(clientBuilder, tMap, out, outType, relType, in);

        if (allRelations.get(rel)==null) {
            allRelations.put(rel,relationCount);
            relationCount++;
        }

        return List.of(allRelations.get(rel),inType);

    }

    private String prettifyType(ProxyClientInterface clientBuilder, TMap tMap, int out, int outType, int relType, int in) {
        StringBuffer sb=new StringBuffer();
        sb.append(clientBuilder.getName());
        sb.append(".");
        sb.append(clientBuilder.getPropertyOrder()[out]);
        sb.append(".");
        sb.append(niceRelationName(StatementOrBundle.Kind.values()[outType]));
        sb.append(".");
        sb.append(clientBuilder.getPropertyOrder()[in]);
        if (relType !=-1) {
            sb.append("[");
            sb.append(tMap.level0S.get(relType).stream().map(i -> localName(tMap.level0.get(i))).collect(Collectors.joining(",", "", "")));
            sb.append("]");
        }
        String rel=sb.toString();
        return rel;
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
                return "spe";
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

    public <ALPHA,BETA>  Map<BETA,ALPHA> swap (Map<ALPHA,BETA> m) {
        return m.keySet().stream().collect(Collectors.toMap(m::get, a->a));
    }

    public Map<String, Integer> level0(Map<QualifiedName, Set<String>> knownTypeMap, Map<QualifiedName, Set<String>> unknownTypeMap, Map<String, Map<String, BiFunction<Object, String, Collection<String>>>> propertyConverters) {
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

        Map<String, Set<Integer>> level0 = mergeMapsOfSets(knownTypeMap3, unknownTypeMap3);


        result.put("level0TypeIndex", level0TypeIndex);
        //result.put("knownTypeMap2",knownTypeMap2);
        //result.put("knownTypeMap3",knownTypeMap3);
        //result.put("unknownTypeMap2",unknownTypeMap2);
        //result.put("unknownTypeMap3",unknownTypeMap3);
        result.put("level0",level0);

        tMap.level0=swap(level0TypeIndex);


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
        tMap.level0S=swap(level0TypeSetIndex);


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

    public void computeLevels(HashMap<String,FileBuilder> registry, HashMap<String,Object> clientRegistry, ProxyManagement pm, Map<QualifiedName, Set<String>> knownTypeMap, Map<QualifiedName, Set<String>> unknownTypeMap, Map<String, Map<String, BiFunction<Object, String, Collection<String>>>> propertyConverters, int bound) {
        Map<String, Integer> level0= level0(knownTypeMap, unknownTypeMap,propertyConverters);
        Map<String, Integer> level=level0;
        for (int i=1; i <bound; i++) {
            level= levelN(registry, clientRegistry, pm, level, i, level0);
        }
    }

    static public <ALPHA,BETA> Map<ALPHA, Set<BETA>> mergeMapsOfSets(Map<ALPHA, Set<BETA>> map1, Map<ALPHA, Set<BETA>> map2) {
        Map<ALPHA, Set<BETA>> result=new HashMap<>(map2);
        map1.forEach((k, v) -> result.merge(k, v, (Set<BETA> v1, Set<BETA> v2) -> {
            Set<BETA> set = new TreeSet<>(v1);
            set.addAll(v2);
            return set;
        }));
        return result;
    }
    static public <ALPHA,BETA> Map<ALPHA, List<BETA>> mergeMapsOfLists(Map<ALPHA, List<BETA>> map1, Map<ALPHA, List<BETA>> map2) {
        Map<ALPHA, List<BETA>> result=new HashMap<>(map2);
        map1.forEach((k, v) -> result.merge(k, v, (List<BETA> v1, List<BETA> v2) -> {
            List<BETA> set = new LinkedList<>(v1);
            set.addAll(v2);
            return set;
        }));
        return result;
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


    public int getLevelNumber() {
        return levelNumber;
    }

    public Map<String, Map<String, BiFunction<Object, String, Collection<String>>>> getPropertyConverters() {
        return propertyConverters;
    }

    public Map<String, Map<String, BiFunction<Object, String, Collection<String>>>> initialisePropertyConverters(Map<String, Map<String, List<String>>> converters) {
        return converters.keySet().stream().collect(Collectors.toMap((String s)-> s, (String s) -> initialise(converters.get(s))));
    }

    public Map<String, BiFunction<Object, String, Collection<String>>> initialise(Map<String, List<String>> m) {
        return m.keySet().stream().collect(Collectors.toMap((String s)-> s, (String s) -> m.get(s).stream().map(fname -> initialize(s, fname)).reduce(biidentity,bicompose)));
    }

    final private Function<Object, Collection<String>> identity= o->List.of();
    final private BiFunction<Object, String,Collection<String>> biidentity= (o,s)->List.of();

    final private BinaryOperator<Function<Object, Collection<String>>> compose= (f1, f2) -> o -> {
        List<String> ll=new LinkedList<>();
        final Collection<String> c1 = f1.apply(o);
        if (c1!=null) ll.addAll(c1);
        final Collection<String> c2 = f2.apply(o);
        if (c2!=null) ll.addAll(c2);
        return ll;
    };

    final private BinaryOperator<BiFunction<Object, String, Collection<String>>> bicompose= (f1, f2) -> (o,s) -> {
        List<String> ll=new LinkedList<>();
        final Collection<String> c1 = f1.apply(o,s);
        if (c1!=null) ll.addAll(c1);
        final Collection<String> c2 = f2.apply(o,s);
        if (c2!=null) ll.addAll(c2);
        return ll;
    };

    private BiFunction<Object, String, Collection<String>> initialize(String property, String classname) {
        try {
            return (BiFunction<Object,String, Collection<String>>) Class.forName(classname).getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("fail to initialize property converter " + classname + " for property " + property);
        }
    }
}
