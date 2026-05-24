package org.openprovenance.prov.service.core.readers;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Typed body for {@code POST /query/{name}/post}.
 *
 * <p>Serialises / deserialises as a flat JSON object whose keys are parameter
 * names and whose values are strings (or any JSON scalar, coerced to
 * {@code String}).  Example wire format:</p>
 *
 * <pre>{@code
 * { "searchTerm": "N50-2025-002", "domain": "goods" }
 * }</pre>
 *
 * <p>The corresponding SQL file uses {@code :paramName} placeholders that are
 * substituted by {@link org.openprovenance.prov.service.core.QueryService}
 * before the query is executed.</p>
 */
public class QueryParameters {

    private final Map<String, String> params = new LinkedHashMap<>();

    // ── Jackson binding ───────────────────────────────────────────────────────

    /** Accepts any JSON key/value pair from the body into the parameter map. */
    @JsonAnySetter
    public void put(String key, Object value) {
        params.put(key, value == null ? null : value.toString());
    }

    /** Serialises back to a flat JSON object (mirrors the wire format). */
    @JsonAnyGetter
    public Map<String, String> all() {
        return Collections.unmodifiableMap(params);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** Returns the value for {@code key}, or {@code null} if absent. */
    public String get(String key) {
        return params.get(key);
    }

    /** Returns all parameter entries. */
    public Set<Map.Entry<String, String>> entrySet() {
        return Collections.unmodifiableSet(params.entrySet());
    }

    /** Returns {@code true} if no parameters were supplied. */
    public boolean isEmpty() {
        return params.isEmpty();
    }

    @Override
    public String toString() {
        return "QueryParameters" + params;
    }
}
