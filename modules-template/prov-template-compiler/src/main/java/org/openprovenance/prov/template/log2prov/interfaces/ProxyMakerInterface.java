package org.openprovenance.prov.template.log2prov.interfaces;

import org.openprovenance.prov.model.QualifiedName;

import java.util.Map;
import java.util.Set;

// This interface is useful to invoke method on generated classes, by means of the ProxyManagement class, without having to share any package/classes.
public interface ProxyMakerInterface {
    String make(Object[] record, Object _processor);   // public <T> T make(Object[] record, Template_blockBuilderInterface<T> _processor) {
    Object getTypeManager(Map<QualifiedName, Set<String>> ktm, Map<QualifiedName, Set<String>> utm);

}
