package org.openprovenance.prov.template.compiler.past.annotations;

import java.util.Map;

public class AnnotationConverter {

    static Map<String, PastAnnotation> annotationMap=getAnnotationMap();

    static  Map<String, PastAnnotation> getAnnotationMap() {
        Map<String, PastAnnotation> map= new java.util.HashMap<>();
        map.put(ClassInitialiser.NAME, new ClassInitialiser());
        map.put(Ignore.NAME, new Ignore());
        return map;
    }

    static public PastAnnotation toAnnotation(String name) {
        if ( name==null) {
            throw new IllegalArgumentException("Name name cannot be null");
        }
        return annotationMap.get(name);
    }
}
