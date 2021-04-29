package org.openprovenance.prov.template.log2prov.interfaces;

import java.util.HashMap;

// This interface is useful to invoke method on generated classes, by means of the ProxyManagement class, without having to share any package/classes.
public interface ProxyClientInterface {

    int[] getNodes();

    HashMap<Integer, int[]> getSuccessors();

    String getName();

    String make(Object[] objects);

}
