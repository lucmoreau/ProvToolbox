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
        map.put(OverrideAnnotation.NAME, new OverrideAnnotation());
        map.put(JsonIgnoreAnnotation.NAME, new JsonIgnoreAnnotation() );
        map.put(StaticMethod.NAME, new StaticMethod());
        map.put(NoSerialization.NAME, new NoSerialization());
        map.put(MutableReceiver.NAME, new MutableReceiver());
        map.put(MutableFirstParam.NAME, new MutableFirstParam());
        map.put(StatefulProcessor.NAME, new StatefulProcessor());
        return map;
    }

    static public PastAnnotation toAnnotation(String name) {
        if ( name==null) {
            throw new IllegalArgumentException("Name name cannot be null");
        }
        return annotationMap.get(name);
    }
}
