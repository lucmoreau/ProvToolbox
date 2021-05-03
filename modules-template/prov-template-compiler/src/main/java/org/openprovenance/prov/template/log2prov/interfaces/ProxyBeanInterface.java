package org.openprovenance.prov.template.log2prov.interfaces;

// This interface is useful to invoke method on generated classes, by means of the ProxyManagement class, without having to share any package/classes.
public interface ProxyBeanInterface {
   Object process(Object processor);

}
