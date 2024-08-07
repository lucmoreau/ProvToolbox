package org.openprovenance.prov.template.types;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.types.TypesRecordProcessor.mergeMapsOfLists;

public  class TMap {
    public static final int MAX_SIZE = 50;
    /* A Mapping from integer to the corresponding type. */
    public Map<Integer, String> primitive;
    /* A mapping from integer to a set of primitive types, denoted by their integers */
    public Map<Integer, Set<Integer>> primitive_Set;
    public Map<Integer, String> allRelations;

    public Map<Integer, List<List<Integer>>> level0;
    public Map<Integer, List<List<Integer>>> level1;
    public Map<Integer, List<List<Integer>>> level2;
    public Map<Integer, List<List<Integer>>> level3;
    public Map<Integer, List<List<Integer>>> level4;
    public Map<Integer, List<List<Integer>>> level5;
    public Map<Integer, List<List<Integer>>> level6;
    public Map<Integer, List<List<Integer>>> level7;
    public Map<Integer, List<List<Integer>>> level8;
    public Map<Integer, List<List<Integer>>> level9;
    public Map<Integer, List<List<Integer>>> level10;
    public Map<Integer, Integer> features;

    public Map<Integer, List<List<Integer>>> allLevels;
    public Map<Integer, Map<List<Integer>, Long>> allLevelsCompact;
    public Map<Integer, List<String>> descriptors;

    public Map<Integer, List<List<Integer>>> [] levelArray=new Map[MAX_SIZE];

    /*

    @JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property = "@type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = DescriptorAtom.class, name = "atom"),
            @JsonSubTypes.Type(value = DescriptorTree.class, name = "tree")
    })

     */
    public Map<Integer, List<Descriptor>> structuredDescriptors;


    public void assign(String s, int level, Map<Integer, List<List<Integer>>> m) {
        levelArray[level]=m;
        switch (level) {
            case 0: level0 =m; return;
            case 1: level1=m; return;
            case 2: level2=m; return;
            case 3: level3=m; return;
            case 4: level4=m; return;
            case 5: level5=m; return;
            case 6: level6=m; return;
            case 7: level7=m; return;
            case 8: level8=m; return;
            case 9: level9=m; return;
            case 10: level10=m; return;
            default:
                // nothing to be done, we have assigned the value in levelArray already
        }
    }

    public void merge() {
        if (level1!=null) allLevels=level1;
        if (level2!=null) allLevels=mergeMapsOfLists(allLevels,level2);
        if (level3!=null) allLevels=mergeMapsOfLists(allLevels,level3);
        if (level4!=null) allLevels=mergeMapsOfLists(allLevels,level4);
        if (level5!=null) allLevels=mergeMapsOfLists(allLevels,level5);
        if (level6!=null) allLevels=mergeMapsOfLists(allLevels,level6);
        if (level7!=null) allLevels=mergeMapsOfLists(allLevels,level7);
        if (level8!=null) allLevels=mergeMapsOfLists(allLevels,level8);
        if (level9!=null) allLevels=mergeMapsOfLists(allLevels,level9);
        if (level10!=null) allLevels=mergeMapsOfLists(allLevels,level10);
        if (level0 !=null) allLevels=mergeMapsOfLists(allLevels, level0);

        for (int i =11; i<MAX_SIZE; i++) {
            if (levelArray[i]!=null) {
                allLevels=mergeMapsOfLists(allLevels,levelArray[i]);
            } else {
                break;
            }
        }

        allLevelsCompact= allLevels.keySet().stream().collect(Collectors.toMap((Integer k)-> k,(Integer k)->allLevels.get(k).stream().collect(Collectors.groupingBy((List<Integer> v) -> v, Collectors.counting()))));


    }
    public TMap() {}

    public TMap(TMap tmap) {
        this.primitive = tmap.primitive;
        this.primitive_Set = tmap.primitive_Set;
        this.allRelations = tmap.allRelations;
        this.level0 = tmap.level0;
        this.level1 = tmap.level1;
        this.level2 = tmap.level2;
        this.level3 = tmap.level3;
        this.level4 = tmap.level4;
        this.level5 = tmap.level5;
        this.level6 = tmap.level6;
        this.level7 = tmap.level7;
        this.level8 = tmap.level8;
        this.level9 = tmap.level9;
        this.level10 = tmap.level10;
        this.features = tmap.features;
        this.allLevels = tmap.allLevels;
        this.allLevelsCompact = tmap.allLevelsCompact;
        this.descriptors = tmap.descriptors;
        this.levelArray = tmap.levelArray;
        this.structuredDescriptors = tmap.structuredDescriptors;
    }
}
