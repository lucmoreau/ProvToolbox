package org.openprovenance.prov.storage.redis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.Instantiable;
import org.openprovenance.prov.storage.api.ResourceIndex;
import org.openprovenance.prov.storage.api.TemplateResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RedisTemplateResourceIndex extends RedisExtendedDocumentResourceIndexFactory<TemplateResource> implements ResourceIndex<TemplateResource> {

    private static Logger logger = LogManager.getLogger(RedisExtendedDocumentResourceIndexFactory.class);

    static String[] extra=new String[]{ RedisDocumentResourceIndex.FIELD_BINDINGS_ID, RedisDocumentResourceIndex.FIELD_TEMPLATE_ID};
    public RedisTemplateResourceIndex(RedisDocumentResourceIndex dri, Instantiable<TemplateResource> factory) {
        super(dri, factory,extra);
    }

    private final String[] myKeyArray = concat(dri.myKeys(),new String[] {RedisDocumentResourceIndex.FIELD_BINDINGS_ID, RedisDocumentResourceIndex.FIELD_TEMPLATE_ID});

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static Instantiable<TemplateResource> factory =new Instantiable<TemplateResource>() {
        @Override
        public TemplateResource newResource(DocumentResource dr) {
            return new RedisTemplateResource(((RedisDocumentResource)dr).getMap());
        }

        @Override
        public TemplateResource newResource() {
            return new RedisTemplateResource(new HashMap<>());
        }
    };

    public static RedisTemplateResourceIndex make (ResourceIndex<DocumentResource> ri) {
        return new RedisTemplateResourceIndex((RedisDocumentResourceIndex)ri, factory);
    }

    /*
    @Override
    public <EXTENDED_RESOURCE extends TemplateResource> RedisExtendedDocumentResourceIndexFactory getExtender(Instantiable<EXTENDED_RESOURCE> f) {
        logger.info("need to specify extra");
        return new RedisExtendedDocumentResourceIndexFactory(this.dri,f,null);
    }


     */
    @Override
    public ResourceIndex<DocumentResource> getAncestor() {
        return dri;
    }


    public static void register(Map<String,Instantiable<?>> m) {
        m.put(TemplateResource.getResourceKind(), factory);
    }


    /**
     * Returns a thread safe instance.
     */
    @Override
    public ResourceIndex<TemplateResource> getIndex() {
        return new RedisTemplateResourceIndex(dri.getIndex(),factory,extra,myKeysArray);
    }

    private RedisTemplateResourceIndex(RedisDocumentResourceIndex dri, Instantiable<TemplateResource> factory,String [] extra, String[] myKeysArray) {
        super(dri,factory,extra,myKeysArray);
    }

}
