package org.openprovenance.prov.template.compiler;

import org.apache.commons.lang3.tuple.Pair;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.template.compiler.configuration.Locations;
import org.openprovenance.prov.template.compiler.configuration.TemplatesProjectConfiguration;
import org.openprovenance.prov.template.compiler.past.Class;

import java.util.Map;

public interface GeneratorInvoker {
    Pair<Class,StackTraceElement> generate(ProvFactory provFactory, TemplatesProjectConfiguration configs, Locations locations, String id, Map<String, Object> parameters);
}
