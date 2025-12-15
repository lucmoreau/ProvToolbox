package org.openprovenance.prov.template.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public final class TemplateIndex {

    private static final TypeReference<Map<String, TemplateLocator>> TYPE = new TypeReference<>() {};
    private final Map<String, TemplateLocator> index;
    private final Path templateDir;

    public TemplateIndex(String templateDir) {
        try {
            this.templateDir=Paths.get(templateDir);
            this.index=readIndex(this.templateDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TemplateIndex(String templateDir, boolean buildIfMissing) {
        try {
            this.templateDir=Paths.get(templateDir);
            if (buildIfMissing) {
                this.index=readOrBuildIndex(this.templateDir);
            } else {
                this.index = readIndex(this.templateDir);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void addEntry(String templateId, String format, String filepath)  {
        try {
            this.index.putIfAbsent(templateId, new TemplateLocator());
            TemplateLocator locator=this.index.get(templateId);
            locator.set(format, filepath);
            persist(this.templateDir, this.index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void addProvenanceEntry(String templateId, String format, String filepath)  {

        try {
            this.index.putIfAbsent(templateId, new TemplateLocator());
            TemplateLocator locator=this.index.get(templateId);
            locator.setProvenance(TemplateExtension.from(format), filepath);
            persist(this.templateDir, this.index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the template location for the given id and preferred extensions.
     *
     * @param templateId template identifier
     * @param extensions list of preferred extensions in order
     * @return template location or null if not found
     */
    public String get(String templateId, List<TemplateExtension> extensions) {
        return extensions
                .stream()
                .map(ext -> {
                    TemplateLocator templateLocator = index.get(templateId);
                    if (templateLocator == null) return null;
                    return templateLocator.get(ext);
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public String getAbsolute(String templateId, List<TemplateExtension> extensions) {
        return extensions
                .stream()
                .map(ext -> {
                    TemplateLocator templateLocator = index.get(templateId);
                    if (templateLocator == null) return null;
                    String relativeLocation = templateLocator.get(ext);
                    if (relativeLocation == null) return null;
                    String absoluteLocation = templateDir.resolve(relativeLocation).toString();
                    return absoluteLocation;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }



    /**
     * Read index from the given templates directory.
     * Looks for `index.json`, then `index.yaml`, then `index.yml`.
     *
     * @param templatesDir directory containing the index file
     * @return map of template id -> TemplateLocator
     * @throws IOException if nothing found or read/parse fails
     */
    public static Map<String, TemplateLocator> readIndex(Path templatesDir) throws IOException {
        Path json = templatesDir.resolve("index.json");
        Path yaml = templatesDir.resolve("index.yaml");
        Path yml  = templatesDir.resolve("index.yml");

        Path source = Stream.of(yaml, json, yml)
                .filter(Files::exists)
                .findFirst()
                .orElseThrow(() -> new NoSuchFileException("No index.yaml/index.json/index.yml found in " + templatesDir));

        ObjectMapper mapper = createMapperFor(source.getFileName().toString());

        try (BufferedReader r = Files.newBufferedReader(source)) {
            return mapper.readValue(r, TYPE);
        }
    }

    /**
     * Read index from the given templates directory, and if not available, build it.
     * Looks for `index.json`, then `index.yaml`, then `index.yml`.
     *
     * @param templatesDir directory containing the index file
     * @return map of template id -> TemplateLocator
     * @throws IOException if nothing found or read/parse fails
     */
    public static Map<String, TemplateLocator> readOrBuildIndex(Path templatesDir) throws IOException {
        Path json = templatesDir.resolve("index.json");
        Path yaml = templatesDir.resolve("index.yaml");
        Path yml  = templatesDir.resolve("index.yml");

        Optional<Path> source = Stream.of(json) //yaml, yml
                .filter(Files::exists)
                .findFirst();

        if (source.isEmpty()) {
            return buildIndex(templatesDir);
        } else {
            Path path = source.get();
            ObjectMapper mapper = createMapperFor(path.getFileName().toString());
            try (BufferedReader r = Files.newBufferedReader(path)) {
                return mapper.readValue(r, TYPE);
            }
        }
    }



    private static ObjectMapper createMapperFor(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".json")) {
            return new ObjectMapper();
        } else {
            return new ObjectMapper(new YAMLFactory());
        }
    }


    /**
     * Build an index by traversing the templatesDir and collecting files matching
     * known TemplateExtension values. Template id is the relative path from
     * templatesDir with the extension removed and path separators normalized to '/'.
            *
            * @param templatesDir directory to scan
     * @return map of template id -> TemplateLocator
     * @throws IOException if templatesDir does not exist or IO error occurs
     */
    public static Map<String, TemplateLocator> buildIndex(Path templatesDir) throws IOException {
        if (templatesDir == null) throw new IllegalArgumentException("templatesDir is null");
        if (!Files.exists(templatesDir) || !Files.isDirectory(templatesDir)) {
            throw new NoSuchFileException("Templates directory not found: " + templatesDir);
        }

        Map<String, TemplateLocator> index = new HashMap<>();
        Set<String> indexFilenames = Set.of("index.json", "index.yaml", "index.yml");

        try (Stream<Path> stream = Files.walk(templatesDir)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> {
                        String fileName = p.getFileName().toString();
                        int dot = fileName.lastIndexOf('.');
                        if (dot < 0) return false;
                        String ext = fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
                        //System.out.println("Found file with extension: " + TemplateExtension.extensions);
                        return TemplateExtension.extensions.stream().anyMatch(e -> e.equals(ext));
                    })
                    .forEach(p -> {
                        String fileName = p.getFileName().toString();
                        if (indexFilenames.contains(fileName)) return;

                        int dot = fileName.lastIndexOf('.');
                        if (dot < 0) return;
                        String ext = fileName.substring(dot + 1).toLowerCase(Locale.ROOT);

                        TemplateExtension extension = TemplateExtension.from(ext);

                        Path rel = templatesDir.relativize(p);
                        String relStr = rel.toString().replace('\\', '/');
                        // remove the dot + extension from the end to get templateId
                        String templateId = relStr.substring(0, relStr.length() - (ext.length() + 1));

                        index.putIfAbsent(templateId, new TemplateLocator());
                        index.get(templateId).set(extension, relStr);
                    });
        }

        TemplateIndex.persist(templatesDir, index);

        return index;
    }

    /**
     * Add or update an entry in the index:
     * - templatesDir: directory containing `index.json`/`index.yaml`/`index.yml` (e.g. `src/main/resources/templates`)
     * - index: map of template id -> TemplateLocator
     */
    public static void persist(Path templatesDir, Map<String, TemplateLocator> index) throws IOException {
        if (templatesDir == null) throw new IllegalArgumentException("templatesDir is null");
        Files.createDirectories(templatesDir);

        Path json = templatesDir.resolve("index.json");
        Path yaml = templatesDir.resolve("index.yaml");
        Path yml  = templatesDir.resolve("index.yml");

        // choose existing index file or default to json
        Path source = Stream.of( json)
                .filter(Files::exists)
                .findFirst()
                .orElse(json);

        ObjectMapper mapper = createMapperFor(source.getFileName().toString());


        // write back using same format (pretty for JSON)
        try (BufferedWriter w = Files.newBufferedWriter(source, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            if (mapper.getFactory() instanceof YAMLFactory) {
                mapper.writeValue(w, index);
            } else {
                mapper.writerWithDefaultPrettyPrinter().writeValue(w, index);
            }
        }
    }

    @Override
    public String toString() {
        return "<<indx::" + templateDir.toString() + ">>";
    }

    public Path relativize(Path documentPath) {
        return templateDir.relativize(documentPath);
    }
}
