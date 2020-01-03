package org.openprovenance.prov.redis;

import org.apache.log4j.Logger;
import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.Instantiable;
import org.openprovenance.prov.service.core.ResourceIndex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE extends DocumentResource>  implements ResourceIndex<EXTENDED_RESOURCE> {
    private static Logger logger = Logger.getLogger(RedisExtendedDocumentResourceIndexFactory.class);

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }



    private  final String[] extra;
    private  final String[] myKeysArray;


    public String[] myKeys() {
        return myKeysArray;
    }

    private final Instantiable<EXTENDED_RESOURCE> factory;
    protected final RedisDocumentResourceIndex dri;

    public RedisExtendedDocumentResourceIndexFactory(RedisDocumentResourceIndex dri, Instantiable<EXTENDED_RESOURCE> factory, String [] extra) {
        this.dri=dri;
        this.factory=factory;
        this.extra=extra;
        this.myKeysArray=concat(dri.myKeys(),extra);
    }

    public EXTENDED_RESOURCE newResource(DocumentResource dr) {
        final EXTENDED_RESOURCE extended_resource = factory.newResource(dr);
        return extended_resource;
    }


    @Override
    public EXTENDED_RESOURCE get(String key) {
        logger.info("get " + key );
        List<String> values=dri.client.hmget(key, myKeys());
        logger.info("get " + key + values);
        Map<String,String> m=new HashMap<>();
        for (int i=0; i<myKeys().length; i++) {
            m.put(myKeys()[i],values.get(i));
        }
        EXTENDED_RESOURCE er=factory.newResource(new RedisDocumentResource(m));
        logger.info("get " + key + " " + m);
        return er;
    }

    @Override
    public void put(String key, EXTENDED_RESOURCE er) {
        logger.info("put " + key );
        RedisDocumentResource rdr=(RedisDocumentResource)er;
        dri.client.hmset(key,rdr.getMap());
        logger.info("put done " + key + " " + rdr.getMap());
    }


    @Override
    public void remove(String key) {
        logger.info("TODO: remove()");
    }

    @Override
    public String newId() {
        return dri.newId();
    }

    @Override
    public EXTENDED_RESOURCE newResource() {
        return factory.newResource();
    }


    @Override
    public StorageKind kind() {
        return dri.kind();
    }

    @Override
    public ResourceIndex<DocumentResource> getAncestor() {
        return dri;
    }

    /*
    @Override
    public <R2 extends EXTENDED_RESOURCE> RedisExtendedDocumentResourceIndexFactory<R2> getExtender(Instantiable<R2> factory2) {
        logger.info("Need to specify extras ... ");
        return new RedisExtendedDocumentResourceIndexFactory(dri,factory2,null);
    }

     */

}
