package org.openprovenance.prov.storage.redis;

import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;

import java.util.Map;

public class RedisTemplateResource extends RedisDocumentResource implements TemplateResource {
    public RedisTemplateResource(Map<String, String> m) {
        super(m);
    }

    @Override
    public String getBindingsStorageId() {
        return m.get(RedisDocumentResourceIndex.FIELD_BINDINGS_ID);
    }

    @Override
    public void setBindingsStorageId(String bindingsStorageId) {
        m.put(RedisDocumentResourceIndex.FIELD_BINDINGS_ID,bindingsStorageId);

    }

    @Override
    public String getTemplateStorageId() {
        return m.get(RedisDocumentResourceIndex.FIELD_TEMPLATE_ID);
    }

    @Override
    public void setTemplateStorageId(String templateStorageId) {
        m.put(RedisDocumentResourceIndex.FIELD_TEMPLATE_ID,templateStorageId);
    }


}
