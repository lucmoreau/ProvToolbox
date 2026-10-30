package org.openprovenance.prov.service.core;

import org.openprovenance.prov.model.interop.CatalogueDispatcherInterface;
import org.openprovenance.prov.template.log2prov.FileBuilder;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * A catalogue of two templates for tests, without generated code: {@code t.Making} (table {@code making}), which
 * generates {@code thing1} by the activity {@code making} from {@code thing0}, and {@code t.MakingComposite} (table
 * {@code making_composite}), a composite of makings.
 */
final class FakeCatalogue {
    static final String MAKING = "t.Making";
    static final String COMPOSITE = "t.MakingComposite";
    static final String[] MAKING_PROPERTIES = {"isA", "ID", "thing0", "thing1", "making"};

    @SuppressWarnings("unchecked")
    static CatalogueDispatcherInterface<FileBuilder> create() {
        Map<String, Object> answers = new HashMap<>();
        answers.put("getShortNames", "{\"" + MAKING + "\":\"making\",\"" + COMPOSITE + "\":\"making_composite\"}");
        answers.put("getIoMap", "{\"output\":{\"" + MAKING + "\":{\"thing1\":\"thing\",\"making\":\"activity\"}},"
                + "\"input\":{\"" + MAKING + "\":{\"thing0\":\"thing\"}}}");
        answers.put("getUniqueMap", "{}");
        answers.put("getLinkers", "{\"" + COMPOSITE + "\":{\"table\":\"making_composite_linker\",\"linked\":\"making\"}}");
        answers.put("getPropertyOrder", Map.of(MAKING, MAKING_PROPERTIES, COMPOSITE, new String[]{"isA", "ID"}));
        Function<Object[], String> csv = r -> String.join(",", java.util.Arrays.stream(r).map(String::valueOf).toArray(String[]::new));
        answers.put("getCsvConverter", Map.of(MAKING, csv));
        // wasGeneratedBy(thing1, making), used(making, thing0): the record's positions 3, 4 and 4, 2
        answers.put("getRelation0", Map.of(MAKING, Map.of(
                "PROV_GENERATION", Map.of("gen1", new int[]{3, 4}),
                "PROV_USAGE", Map.of("use1", new int[]{4, 2}))));
        answers.put("getForeignTables", Map.of(MAKING, new String[]{null, null, "thing", "thing", "activity"}));
        return (CatalogueDispatcherInterface<FileBuilder>) Proxy.newProxyInstance(FakeCatalogue.class.getClassLoader(),
                new Class<?>[]{CatalogueDispatcherInterface.class},
                (proxy, method, args) -> {
                    if (answers.containsKey(method.getName())) return answers.get(method.getName());
                    if (method.getReturnType() == Map.class) return new HashMap<>();
                    if (method.getReturnType() == List.class) return List.of();
                    switch (method.getName()) {
                        case "hashCode": return System.identityHashCode(proxy);
                        case "equals": return proxy == args[0];
                        case "toString": return "FakeCatalogue";
                        default: return null;
                    }
                });
    }

    /** The row of a making as the database returns it. */
    static Map<String, Object> makingRow(int id) {
        return Map.of("ID", id, "thing0", 100 + id, "thing1", 200 + id, "making", 300 + id);
    }
}
