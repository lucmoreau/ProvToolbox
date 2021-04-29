package org.example;

import junit.framework.TestCase;
import org.example.local.InterfaceX;
import org.openprovenance.prov.template.log2prov.ProxyManagement;

public class ProxyTest extends TestCase {

    static class RemoteA implements org.example.remote.InterfaceX {
        @Override
        public String make(float x) {
            System.out.println("RemoteA being called");
            return String.valueOf(x);
        }
    }

    public void testProxy1() {
        testProxy1(new RemoteA());
    }



    public void testProxy1(Object o) {

        System.out.println("Found object " + o);

        ProxyManagement pm=new ProxyManagement();
        System.out.println("Proxy being called");

        InterfaceX proxy=pm.facadeProxy(InterfaceX.class,o);

        String v=proxy.make((float) 18.9);
        System.out.println("Proxy returning " + v);


    }

}
