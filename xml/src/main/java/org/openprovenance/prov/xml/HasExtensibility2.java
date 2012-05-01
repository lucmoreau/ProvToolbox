package org.openprovenance.prov.xml;
import java.util.List;

/** Over time, when all patches applied to classes, HasExtensibility2 should replace HasExtensibility. */

public interface HasExtensibility2 extends HasExtensibility {
    public java.util.Hashtable<String,List<Object>> attributesWithNamespace(String namespace);
} 
