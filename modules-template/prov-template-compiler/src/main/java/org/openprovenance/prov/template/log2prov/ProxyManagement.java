package org.openprovenance.prov.template.log2prov;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Stream;

public class ProxyManagement {

    static boolean debug=false;


    public static class PassthroughInvocationHandler implements InvocationHandler {

        private final Object unknownObject;
        private final Class<?>[] localInterfaces;

        Map<Class<?>, Class<?>> mapper=new HashMap<>();
        Map<Method, Method> mMapper=new HashMap<>();

        public PassthroughInvocationHandler(Object unknownObject, Class<?>[] localInterfaces) {
            this.unknownObject = unknownObject;
            this.localInterfaces=localInterfaces;

            Class<?>[] unknownInterfaces = unknownObject.getClass().getInterfaces();
            for (Class<?> localInterface : localInterfaces) {
                boolean found=false;
                for (Class<?> unknownInterface : unknownInterfaces) {
                    if (unknownInterface.getSimpleName().equals(localInterface.getSimpleName())) {
                        mapper.put(localInterface, unknownInterface);
                        found=true;
                        break;
                    }
                }
                if (!found) { // backup option, use the remote object class as the counterpart for localInterface
                    mapper.put(localInterface, unknownObject.getClass());
                }
            }

            mapper.entrySet().forEach(e -> {
                Class<?> localClass=e.getKey();
                Class<?> remoteClass=e.getValue();
                for (Method lMethod: localClass.getDeclaredMethods()) {
                    for (Method rMethod: remoteClass.getDeclaredMethods()) {
                        if (lMethod.getName().equals(rMethod.getName())
                                && lMethod.getParameterCount()==rMethod.getParameterCount() ) { // TODO extend to full signature if required
                            mMapper.put(lMethod,rMethod);
                        }
                    }
                }
            });
        }

        @Override
        public Object invoke(Object proxy, Method lMethod, Object[] args) throws Throwable {
            if (debug) System.out.println("Calling invoke with lMethod " + lMethod);
            final Method rMethod = mMapper.get(lMethod);
            if (debug) System.out.println("Calling invoke with rMethod " + rMethod);
            return rMethod.invoke(unknownObject, args);
        }
    }

    public static  <T> T simpleProxy(Class iface, InvocationHandler handler, Class...otherIfaces) {
        Class[] allInterfaces = Stream.concat(
                Stream.of(iface),
                Stream.of(otherIfaces))
                .distinct()
                .toArray(Class[]::new);

        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                allInterfaces,
                handler);
    }
    public   <T> T facadeProxy(Class<T> iface, Object remoteObject) {
        return simpleProxy(iface, new PassthroughInvocationHandler(remoteObject, new Class[]{iface}));
    }
}
