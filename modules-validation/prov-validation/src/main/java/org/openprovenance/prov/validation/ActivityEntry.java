package org.openprovenance.prov.validation;

import java.util.HashSet;
import java.util.Set;

public class ActivityEntry {
    static int count = 0;

    private final int myCount;

    private final Set<String> startKey = new HashSet<>();

    public void addStartKey(String key) {
        this.startKey.add(key);
    }

    public Set<String> getStartKey() {
        return startKey;
    }

    private final Set<String> endKey = new HashSet<>();

    public void addEndKey(String key) {
        this.endKey.add(key);
    }

    public Set<String> getEndKey() {
        return endKey;
    }

    public ActivityEntry() {
        count++;
        myCount = count;
    }

    public String toString() {
        return "<<" + myCount + "," + startKey + "," + endKey + ">>";
    }
}
