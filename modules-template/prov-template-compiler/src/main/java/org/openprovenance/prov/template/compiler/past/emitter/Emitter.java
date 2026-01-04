package org.openprovenance.prov.template.compiler.past.emitter;

import com.squareup.javapoet.TypeSpec;
import org.openprovenance.prov.template.compiler.past.Class;

public interface Emitter  <TypeSpec>  {
    TypeSpec emit(Class clazz);
    public WritableObject toWritableObject(Class clazz, String templateName, String packge, StackTraceElement stackTraceElement);
}
