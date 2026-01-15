package org.openprovenance.prov.template.compiler.past;

public class PastFactory {
    public Class CLASS(String name) {
        return new Class(name);
    }
    public Class CLASS(String name, boolean isInterface) {
        return new Class(name, isInterface);
    }

    public Class INTERFACE(String interfaceName) {
        Class intfce = new Class(interfaceName, true);
        return intfce;
    }
}
