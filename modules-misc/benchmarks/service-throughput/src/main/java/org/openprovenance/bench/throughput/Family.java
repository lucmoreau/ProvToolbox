package org.openprovenance.bench.throughput;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * A template family: the templates of one catalogue package (one service instance), their inputs as the generated
 * beans name them (provenance-templates 0.1.0-SNAPSHOT), and a realistic mix of messages, a composite among them.
 *
 * <p>Values are unique (a run-wide counter in every text), times are now, and an input referring to an entity, an
 * activity or an agent takes an id the service returned for an earlier record (an output of the family: the records
 * link up as a real workload's would), or a small number before there is any. The store has no foreign key from the
 * template tables, so the choice affects what is read later, not what a write costs.</p>
 */
final class Family {

    enum Kind { REF, TEXT, TIME, INT, DOUBLE, BOOL, CONTAINER_TYPE,
        /** sent as null: transporting's ilocation and weighing's unit are written into their INSERT unquoted by the generated QueryInvoker4 (a text there is invalid SQL) */
        NULL }

    static final class Field {
        final String name;
        final Kind kind;

        Field(String name, Kind kind) {
            this.name = name;
            this.kind = kind;
        }
    }

    static final class Template {
        final String isA;
        final String table;
        final List<Field> fields;
        /** the element template of a composite, null for a simple one */
        final Template element;
        /** a composite's shared variables: the same token in every element, one new id for them all */
        final List<String> shared;
        final int weight;

        Template(String isA, String table, int weight, Template element, Field... fields) {
            this(isA, table, weight, element, List.of(), fields);
        }

        Template(String isA, String table, int weight, Template element, List<String> shared, Field... fields) {
            this.isA = isA;
            this.table = table;
            this.weight = weight;
            this.element = element;
            this.shared = shared;
            this.fields = List.of(fields);
        }

        boolean composite() {
            return element != null;
        }
    }

    final String name;
    final String cataloguePackage;
    final List<Template> templates;
    private final int totalWeight;
    private final int compositeSize;
    private final String runTag;
    private final AtomicLong counter = new AtomicLong();
    /** recent ids returned by the service (outputs): what later records refer to */
    private final AtomicLongArray recentIds = new AtomicLongArray(4096);
    private final AtomicLong recentCount = new AtomicLong();

    private Family(String name, String cataloguePackage, int compositeSize, List<Template> templates) {
        this.name = name;
        this.cataloguePackage = cataloguePackage;
        this.templates = templates;
        this.compositeSize = compositeSize;
        this.totalWeight = templates.stream().mapToInt(t -> t.weight).sum();
        this.runTag = Long.toString(System.currentTimeMillis(), 36);
    }

    private static Field f(String name, Kind kind) {
        return new Field(name, kind);
    }

    private static final Field[] PROCESS_TIMES = {f("time", Kind.TIME), f("start", Kind.TIME), f("end", Kind.TIME)};

    private static Field[] with(Field[] times, Field... fields) {
        Field[] all = new Field[fields.length + times.length];
        System.arraycopy(fields, 0, all, 0, fields.length);
        System.arraycopy(times, 0, all, fields.length, times.length);
        return all;
    }

    static Family named(String family, int compositeSize) {
        switch (family) {
            case "fs": return fs(compositeSize);
            case "tr": return tr(compositeSize);
            default: throw new IllegalArgumentException("FAMILY is fs or tr, not " + family);
        }
    }

    /** org.openprovenance.templates.catalogue.fs: the file-system templates, composite file_transforming_composite. */
    static Family fs(int compositeSize) {
        String p = "org.openprovenance.templates.fs.";
        Template transforming = new Template(p + "FileTransforming", "file_transforming", 3, null,
                with(PROCESS_TIMES, f("filename", Kind.TEXT), f("file", Kind.REF), f("method", Kind.REF), f("engineer", Kind.REF), f("path", Kind.TEXT)));
        List<Template> t = new ArrayList<>();
        t.add(new Template(p + "FileInit", "file_init", 2, null, f("time", Kind.TIME), f("type", Kind.TEXT), f("location", Kind.TEXT)));
        t.add(transforming);
        t.add(new Template(p + "FileFiltering", "file_filtering", 2, null,
                with(PROCESS_TIMES, f("filename", Kind.TEXT), f("file", Kind.REF), f("method", Kind.REF), f("engineer", Kind.REF),
                        f("n_rows", Kind.INT), f("n_cols", Kind.INT), f("path", Kind.TEXT))));
        t.add(new Template(p + "FileTraining", "file_training", 1, null,
                with(PROCESS_TIMES, f("filename", Kind.TEXT), f("training_dataset", Kind.REF), f("method", Kind.REF), f("engineer", Kind.REF), f("path", Kind.TEXT))));
        t.add(new Template(p + "FileValidating", "file_validating", 1, null,
                with(PROCESS_TIMES, f("testing_dataset", Kind.REF), f("method", Kind.REF), f("engineer", Kind.REF), f("path", Kind.TEXT), f("score_value", Kind.DOUBLE))));
        t.add(new Template(p + "FileApproving", "file_approving", 1, null,
                with(PROCESS_TIMES, f("filename", Kind.TEXT), f("pipeline", Kind.REF), f("score", Kind.REF), f("approval_method", Kind.REF),
                        f("manager", Kind.REF), f("signature", Kind.TEXT), f("path", Kind.TEXT))));
        t.add(new Template(p + "FileSplitting", "file_splitting", 1, null,
                with(PROCESS_TIMES, f("filename1", Kind.TEXT), f("filename2", Kind.TEXT), f("file", Kind.REF), f("method", Kind.REF), f("engineer", Kind.REF),
                        f("path1", Kind.TEXT), f("path2", Kind.TEXT))));
        t.add(new Template(p + "FileTransformingComposite", "file_transforming_composite", 1, transforming,
                List.of("transformed_file", "transforming")));
        return new Family("fs", "org.openprovenance.templates.catalogue.fs", compositeSize, t);
    }

    /** org.openprovenance.templates.catalogue.transport: the transport templates, composites packing/unpacking_composite. */
    static Family tr(int compositeSize) {
        String p = "org.openprovenance.templates.transport.";
        Template packing = new Template(p + "Packing", "packing", 1, null,
                f("container0", Kind.REF), f("item0", Kind.REF), f("container", Kind.REF), f("item", Kind.REF), f("packer", Kind.REF),
                f("sealed", Kind.BOOL), f("containerType", Kind.CONTAINER_TYPE), f("time", Kind.TIME));
        Template unpacking = new Template(p + "Unpacking", "unpacking", 1, null,
                f("container0", Kind.REF), f("container", Kind.REF), f("item", Kind.REF), f("unpacker", Kind.REF), f("time", Kind.TIME));
        List<Template> t = new ArrayList<>();
        t.add(new Template(p + "EntityInit", "item_init", 2, null, f("time", Kind.TIME), f("type", Kind.TEXT), f("location", Kind.TEXT)));
        t.add(new Template("org.openprovenance.templates.responsibility.AgentInit", "agent_init", 1, null,
                f("time", Kind.TIME), f("type", Kind.TEXT), f("location", Kind.TEXT)));
        t.add(new Template(p + "Transporting", "transporting", 3, null,
                f("item0", Kind.REF), f("item", Kind.REF), f("transporter", Kind.REF), f("schedule", Kind.REF), f("ilocation", Kind.NULL), f("time", Kind.TIME)));
        t.add(new Template(p + "HandingOver", "handingover", 2, null,
                f("item0", Kind.REF), f("item", Kind.REF), f("receiver", Kind.REF), f("giver", Kind.REF), f("time", Kind.TIME)));
        t.add(new Template(p + "Weighing", "weighing", 2, null,
                f("item0", Kind.REF), f("item", Kind.REF), f("agent", Kind.REF), f("scale", Kind.REF), f("weight", Kind.DOUBLE), f("unit", Kind.NULL), f("time", Kind.TIME)));
        t.add(packing);
        t.add(unpacking);
        t.add(new Template(p + "PackingComposite", "packing_composite", 1, packing, List.of("container1", "adding")));
        t.add(new Template(p + "UnpackingComposite", "unpacking_composite", 1, unpacking, List.of("container1", "removing")));
        return new Family("tr", "org.openprovenance.templates.catalogue.transport", compositeSize, t);
    }

    Template pick() {
        int r = ThreadLocalRandom.current().nextInt(totalWeight);
        for (Template t : templates) {
            r -= t.weight;
            if (r < 0) return t;
        }
        return templates.get(templates.size() - 1);
    }

    /** One entry of a message: a simple record's bindings, or a composite's with its elements. */
    Map<String, Object> entry(Template t) {
        if (!t.composite()) return simple(t);
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("isA", t.isA);
        m.put("count", compositeSize);
        m.put("type", t.element.isA);
        List<Map<String, Object>> elements = new ArrayList<>(compositeSize);
        for (int i = 0; i < compositeSize; i++) {
            Map<String, Object> e = simple(t.element);
            int token = -1;
            for (String shared : t.shared) e.put(shared, token--);
            elements.add(e);
        }
        m.put("__elements", elements);
        return m;
    }

    /** The elements of each composite. */
    int compositeSize() {
        return compositeSize;
    }

    /** The template records an entry writes: 1, or a composite's own and its elements'. */
    int records(Template t) {
        return t.composite() ? 1 + compositeSize : 1;
    }

    private Map<String, Object> simple(Template t) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        long n = counter.incrementAndGet();
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("isA", t.isA);
        for (Field f : t.fields) {
            switch (f.kind) {
                case REF: m.put(f.name, ref(rnd)); break;
                case TEXT: m.put(f.name, f.name + "-" + runTag + "-" + n); break;
                case TIME: m.put(f.name, Instant.now().toString()); break;
                case INT: m.put(f.name, 1 + rnd.nextInt(100_000)); break;
                case DOUBLE: m.put(f.name, Math.round(rnd.nextDouble() * 1e6) / 1e3); break;
                case BOOL: m.put(f.name, rnd.nextBoolean()); break;
                case CONTAINER_TYPE: m.put(f.name, rnd.nextBoolean() ? "box" : "pallet"); break;
                case NULL: m.put(f.name, null); break;
                default: throw new IllegalStateException(f.kind.toString());
            }
        }
        return m;
    }

    private long ref(ThreadLocalRandom rnd) {
        long known = Math.min(recentCount.get(), recentIds.length());
        if (known == 0) return 1 + rnd.nextInt(16);
        return recentIds.get(rnd.nextInt((int) known));
    }

    /** An id the service returned (an output of a record): later records may refer to it. */
    void remember(long id) {
        long i = recentCount.getAndIncrement();
        recentIds.set((int) (i % recentIds.length()), id);
    }
}
