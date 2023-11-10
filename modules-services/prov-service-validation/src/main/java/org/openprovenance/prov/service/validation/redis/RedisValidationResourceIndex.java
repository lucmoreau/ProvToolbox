package org.openprovenance.prov.service.validation.redis;


import org.openprovenance.prov.service.validation.ValidationResource;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.Instantiable;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.redis.RedisDocumentResource;
import org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex;
import org.openprovenance.prov.storage.redis.RedisExtendedDocumentResourceIndexFactory;

import java.util.HashMap;
import java.util.Map;

public class RedisValidationResourceIndex extends RedisExtendedDocumentResourceIndexFactory<ValidationResource> implements ResourceIndex<ValidationResource> {

    static final String FIELD_JSON_REPORT_ID = "_jrep_";
    static final String FIELD_MATRIX_PNG_ID = "_mapng";
    static final String FIELD_MATRIX_ID = "_ma_";
    static final String FIELD_COMPLETED = "_co_";
    static final String FIELD_REPORT_ID="_rep_";

    static String[] extra=new String[]{ FIELD_COMPLETED, FIELD_REPORT_ID, FIELD_MATRIX_ID, FIELD_MATRIX_PNG_ID, FIELD_JSON_REPORT_ID};

    public RedisValidationResourceIndex(RedisDocumentResourceIndex dri, Instantiable<ValidationResource> factory) {
        super(dri, factory, extra);
    }

    public static Instantiable<ValidationResource> factory = new Instantiable<>() {
        @Override
        public ValidationResource newResource(DocumentResource dr) {
            return new RedisValidationResource(((RedisDocumentResource) dr).getMap());
        }

        @Override
        public ValidationResource newResource() {
            return new RedisValidationResource(new HashMap<>());
        }
    };

    public static RedisValidationResourceIndex make (ResourceIndex<DocumentResource> ri) {
        return new RedisValidationResourceIndex((RedisDocumentResourceIndex)ri, factory);
    }

    @Override
    public ResourceIndex<DocumentResource> getAncestor() {
        return dri;
    }


    public static void register(Map<String,Instantiable<?>> m) {
        m.put(ValidationResource.getResourceKind(), factory);
    }


}
