package org.openprovenance.prov.template.compiler.past.emitter;

import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.template.compiler.past.Class;

import java.util.List;

public class Emitters implements Emitter{

    final private List<Emitter> emitters;

    public Emitters(List<Emitter> emitters) {
        this.emitters = emitters;
    }

    @Override
    public TypeSpec emit(Class clazz) {
        emitters.forEach(emitter->emitter.emit(clazz));
        return null;
    }

    @Override
    public WritableObject toWritableObject(Class clazz, String templateName, String packge, StackTraceElement stackTraceElement) {
        return null;
    }
}
