package org.openprovenance.prov.template.types;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.openprovenance.prov.vanilla.WasEndedBy;
import org.openprovenance.prov.vanilla.WasStartedBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.types.TypesRecordProcessor.mergeMapsOfLists;

public  class TMap {
    public static final int MAX_SIZE = 50;
    public Map<Integer, String> level0;
    public Map<Integer, Set<Integer>> level0S;
    public Map<Integer, String> allRelations;
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
    public Map<Integer, List<String>> descriptors2;


    public void assign(String s, int level, Map<Integer, List<List<Integer>>> m) {
        levelArray[level]=m;
        switch (level) {
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

        for (int i =11; i<MAX_SIZE; i++) {
            if (levelArray[i]!=null) {
                allLevels=mergeMapsOfLists(allLevels,levelArray[i]);
            } else {
                break;
            }
        }

        allLevelsCompact= allLevels.keySet().stream().collect(Collectors.toMap((Integer k)-> k,(Integer k)->allLevels.get(k).stream().collect(Collectors.groupingBy((List<Integer> v) -> v, Collectors.counting()))));


    }
}
