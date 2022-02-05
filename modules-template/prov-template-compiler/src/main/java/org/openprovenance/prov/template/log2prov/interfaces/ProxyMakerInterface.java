package org.openprovenance.prov.template.log2prov.interfaces;

import org.openprovenance.prov.model.QualifiedName;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

// This interface is useful to invoke method on generated classes, by means of the ProxyManagement class, without having to share any package/classes.
public interface ProxyMakerInterface {
    Object make(Object[] record, Object _processor);   // public <T> T make(Object[] record, Template_blockBuilderInterface<T> _processor) {
    Object getTypeManager(Map<QualifiedName, Set<String>> ktm, Map<QualifiedName, Set<String>> utm, Map<String, Map<String, BiFunction<Object, String, Collection<String>>>> propertyConverters);
    Object getTypedRecord();
    void propagateTypes(Object[] record,
                        Map<String, Integer> mapLevelN,
                        Map<String, Collection<int[]>> mapLevelNP1,
                        Map<String, Integer> mapLevel0);

}
