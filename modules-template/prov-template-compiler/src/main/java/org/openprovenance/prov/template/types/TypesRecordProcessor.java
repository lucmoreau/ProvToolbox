package org.openprovenance.prov.template.types;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.QualifiedName;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TypesRecordProcessor  {
    static final ObjectMapper om=new ObjectMapper();

    final Map<Integer,Map<String, Integer>> levelTypeIndex;
    final Map<Integer,Map<Set<Integer>, Integer>> levelTypeSetIndex;
    private final List<Pair<String, Object[]>> records;

    public TypesRecordProcessor(Map<String, Integer> knownLevel0TypeIndex, Map<Set<Integer>, Integer> knownTypesSets, List<Pair<String, Object[]>> records) {
        this.levelTypeIndex=new HashMap<>();
        this.levelTypeIndex.put(0, new HashMap<>(knownLevel0TypeIndex));

        this.levelTypeSetIndex=new HashMap<>();
        this.levelTypeSetIndex.put(0, new HashMap<>(knownTypesSets));

        this.records=records;

    }

    public void process(String methodName, Object [] args) {
        records.add(Pair.of(methodName,args));
    }

    public void end(Map<QualifiedName, Set<String>> knownTypeMap, Map<QualifiedName, Set<String>> unknownTypeMap) {
        Map<String, Set<String>> knownTypeMap2  =  knownTypeMap.keySet().stream().collect(Collectors.toMap(QualifiedName::getUri, knownTypeMap::get));
        Map<String, Set<String>> unknownTypeMap2=unknownTypeMap.keySet().stream().collect(Collectors.toMap(QualifiedName::getUri, unknownTypeMap::get));

        Set<String> allValues1=  knownTypeMap2.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        Set<String> allValues2=unknownTypeMap2.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
        Set<String> allValues=new HashSet<>();
        allValues.addAll(allValues1);
        allValues.addAll(allValues2);

        Set<String> newValues = new HashSet<>(allValues);
        final Map<String, Integer> level0TypeIndex = levelTypeIndex.get(0);
        newValues.removeAll(level0TypeIndex.keySet());
        final Collection<Integer> level0Values = level0TypeIndex.values();

        int nextIndex=(level0Values.isEmpty()?0:Collections.max(level0Values))+1;

        List<String> ordered=newValues.stream().sorted().collect(Collectors.toList());
        for (String s: ordered) {
            level0TypeIndex.put(s,nextIndex);
            nextIndex++;
        };

        Map<String, Set<Integer>>   knownTypeMap3=  knownTypeMap2.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s)->   knownTypeMap2.get(s).stream().map(level0TypeIndex::get).collect(Collectors.toSet())));  //.stream().map(v -> level0TypeIndex.get(v)).collect(Collectors.toSet()))
        Map<String, Set<Integer>> unknownTypeMap3=unknownTypeMap2.keySet().stream().collect(Collectors.toMap((String s) -> s, (String s)-> unknownTypeMap2.get(s).stream().map(level0TypeIndex::get).collect(Collectors.toSet())));  //.stream().map(v -> level0TypeIndex.get(v)).collect(Collectors.toSet()))

        Map<String, Set<Integer>> level0=new HashMap<>(unknownTypeMap3);

        knownTypeMap3.forEach((k, v) -> level0.merge(k, v, (Set<Integer> v1, Set<Integer> v2) -> {
            Set<Integer> set = new TreeSet<>(v1);
            set.addAll(v2);
            return set;
        }));

        Map<String, Object> result=new HashMap<>();

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

        int nextIndexS=(level0SValues.isEmpty()?0:Collections.max(level0SValues))+1;

        Comparator<Set<Integer>> comp= (o1, o2) -> {
            String s1=o1.toString();
            String s2=o2.toString();
            return s1.compareTo(s2);
        };

        List<Set<Integer>> orderedS=newTypeSets.stream().sorted(comp).collect(Collectors.toList());
        for (Set<Integer> s: orderedS) {
            level0TypeSetIndex.put(s,nextIndexS);
            nextIndexS++;
        };


        result.put("level0STypeIndex",level0TypeSetIndex);


        Map<String, Integer> level0S=level0.keySet().stream().collect(Collectors.toMap((String s)->s, (String s)->level0TypeSetIndex.get(level0.get(s))));

        result.put("level0S",level0S);



        try {
            om.writerWithDefaultPrettyPrinter().writeValue(System.out, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
