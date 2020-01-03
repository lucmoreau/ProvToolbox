package org.openprovenance.prov.redis;

import org.apache.log4j.Logger;
import org.openprovenance.apache.commons.lang.ArrayUtils;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.Instantiable;
import org.openprovenance.prov.service.core.ResourceIndex;
import org.openprovenance.prov.service.translation.TemplateResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RedisTemplateResourceIndex extends RedisExtendedDocumentResourceIndexFactory<TemplateResource> implements ResourceIndex<TemplateResource> {

    private static Logger logger = Logger.getLogger(RedisExtendedDocumentResourceIndexFactory.class);

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


}
