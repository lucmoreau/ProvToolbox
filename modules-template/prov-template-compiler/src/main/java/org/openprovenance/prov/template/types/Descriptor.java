package org.openprovenance.prov.template.types;

import java.util.function.Function;

public interface Descriptor extends Comparable<Descriptor>{
    String getCategory();
    String toText(Function<String,String> relationTranslator);
}
