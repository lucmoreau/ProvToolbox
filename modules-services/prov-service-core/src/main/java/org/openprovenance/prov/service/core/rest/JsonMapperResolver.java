package org.openprovenance.prov.service.core.rest;

import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;
import tools.jackson.databind.json.JsonMapper;

/**
 * Supplies the mapper used by Jackson's JAX-RS JSON provider
 * ({@code tools.jackson.jakarta.rs.json.JacksonJsonProvider}), which is registered automatically
 * (it comes with swagger) or explicitly by some applications.
 *
 * <p>Without this resolver, the Jackson 3 provider uses a mapper with Jackson 3 defaults
 * (e.g. properties sorted alphabetically, dates as ISO strings), whereas the Jackson 2 provider
 * used Jackson 2 defaults. This resolver keeps the JSON written by the services as it was
 * (JACKSON3_MIGRATION.md, D2). Applications register it as a singleton.</p>
 */
@Provider
public class JsonMapperResolver implements ContextResolver<JsonMapper> {

    private static final JsonMapper MAPPER = JsonMapper.builderWithJackson2Defaults().build();

    @Override
    public JsonMapper getContext(Class<?> type) {
        return MAPPER;
    }
}
