package org.openprovenance.prov.template.compiler.past.annotations;

import java.util.Map;

public class AnnotationConverter {

    static Map<String, PastAnnotation> annotationMap=getAnnotationMap();

    static  Map<String, PastAnnotation> getAnnotationMap() {
        Map<String, PastAnnotation> map= new java.util.HashMap<>();
        map.put(ClassInitialiser.NAME, new ClassInitialiser());
        map.put(Ignore.NAME, new Ignore());
        map.put(ClassMethod.NAME, new ClassMethod());
        map.put(RegisterMethod.NAME, new RegisterMethod());
        map.put(SingleDispatchMethod.NAME, new SingleDispatchMethod());
        return map;
    }

    static public PastAnnotation toAnnotation(String name) {
        if ( name==null) {
            throw new IllegalArgumentException("Name name cannot be null");
        }
        return annotationMap.get(name);
    }
}
