package org.openprovenance.prov.service.core;

/**
 * What the direct JDBC mode of the throughput benchmark needs of service-templates-core beyond its public API, in its
 * package: the relation mapping of a {@link TemplateQuery} (package-private there), to give it an
 * {@link AsyncRelationWriter} as {@link TemplateService} does for {@code relations.write=async}.
 */
public final class DirectAccess {
    private DirectAccess() {
    }

    public static void setAsyncWriter(TemplateQuery query, AsyncRelationWriter writer) {
        query.getRelationMapping().setAsyncWriter(writer);
    }
}
