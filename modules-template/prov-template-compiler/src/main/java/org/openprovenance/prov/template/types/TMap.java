package org.openprovenance.prov.template.types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.openprovenance.prov.template.types.TypesRecordProcessor.mergeMapsOfLists;

public  class TMap {
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
    public Map<Integer, Integer> features;

    public Map<Integer, List<List<Integer>>> allLevels;
    public Map<Integer, Map<List<Integer>, Long>> allLevelsCompact;
    public Map<Integer, List<String>> descriptors;

    public void assign(String s, int level, Map<Integer, List<List<Integer>>> m) {

        switch (level) {
            case 1: level1=m; return;
            case 2: level2=m; return;
            case 3: level3=m; return;
            case 4: level4=m; return;
            case 5: level5=m; return;
            case 6: level6=m; return;
            case 7: level7=m; return;
            case 8: level8=m; return;
            default:
                throw new IllegalStateException("Unexpected value: " + level);
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

        allLevelsCompact= allLevels.keySet().stream().collect(Collectors.toMap((Integer k)-> k,(Integer k)->allLevels.get(k).stream().collect(Collectors.groupingBy((List<Integer> v) -> v, Collectors.counting()))));


    }
}
