package org.openprovenance.prov.validation;

import java.util.HashSet;
import java.util.Set;

public class EntityEntry {
    static int count = 0;

    private final int myCount;

    private final Set<String> generationKey = new HashSet<>();

    public void addGenerationKey(String key) {
        generationKey.add(key);
    }

    public Set<String> getGenerationKey() {
        return generationKey;
    }

    private final Set<String> invalidationKey = new HashSet<>();;

    public void addInvalidationKey(String key) {
        invalidationKey.add(key);
    }

    public Set<String> getInvalidationKey() {
        return invalidationKey;
    }

    public EntityEntry() {
        count++;
        myCount = count;
    }

    public String toString() {
        return "<<" + myCount + "," + generationKey + "," + invalidationKey
                + ">>";

    }
}
