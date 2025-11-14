package org.openprovenance.prov.template.compiler.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Ordered collection of TemplateIndex instances. get(...) queries each index
 * in insertion order and returns the first non-null TemplateLocator.
 */
public final class TemplateIndexPath {
    Logger logger = LogManager.getLogger(TemplateIndexPath.class);

    private final List<TemplateIndex> indices = new ArrayList<>();

    public TemplateIndexPath() {
    }

    public TemplateIndexPath(Collection<TemplateIndex> initial) {
        if (initial != null) {
            this.indices.addAll(initial);
        }
        logger.debug("TemplateIndexPath initial: " + this.indices);
    }

    public void add(TemplateIndex index) {
        Objects.requireNonNull(index, "index is null");
        indices.add(index);
    }

    public void addAll(Collection<TemplateIndex> more) {
        if (more != null) {
            for (TemplateIndex idx : more) {
                Objects.requireNonNull(idx, "one of the indices is null");
            }
            indices.addAll(more);
        }
    }

    public int size() {
        return indices.size();
    }

    public void clear() {
        indices.clear();
    }

    /**
     * Return the first location found by querying each TemplateIndex
     * in insertion order. Returns null if none of the indices contain the id.
     * @param templateId template identifier
     * @param extensions list of preferred extensions in order
     * @return template location or null if not found
     */
    public String get(String templateId, List<TemplateExtension> extensions) {
        Objects.requireNonNull(templateId, "templateId is null");

        logger.debug("TemplateIndexPath.get: indices=" + indices + ", templateId=" + templateId + ", extensions=" + extensions);

        return indices
                .stream()
                .map(index -> index.get(templateId, extensions))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

    }

    /**
     * Return the first location found by querying each TemplateIndex
     * in insertion order. Returns null if none of the indices contain the id.
     * @param templateId template identifier
     * @param extensions list of preferred extensions in order
     * @return template location
     * @throws IllegalArgumentException if the templateId is not found in any index
     */
    public String getStrict(String templateId, List<TemplateExtension> extensions) {
        Objects.requireNonNull(templateId, "templateId is null");

        logger.debug("TemplateIndexPath.getStrict: indices=" + indices + ", templateId=" + templateId + ", extensions=" + extensions);
        return indices
                .stream()
                .map(index -> index.getAbsolute(templateId, extensions))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Template ID not found in any template library index: " + templateId));
    }
}
