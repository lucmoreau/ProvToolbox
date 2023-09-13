package org.openprovenance.prov.storage.redis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.Instantiable;
import org.openprovenance.prov.storage.api.ResourceIndex;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE extends DocumentResource>  implements ResourceIndex<EXTENDED_RESOURCE> {
    private static final Logger logger = LogManager.getLogger(RedisExtendedDocumentResourceIndexFactory.class);

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }



    protected  final String[] extra;
    protected  final String[] myKeysArray;


    public String[] myKeys() {
        return myKeysArray;
    }

    protected final Instantiable<EXTENDED_RESOURCE> factory;
    protected final RedisDocumentResourceIndex dri;

    public RedisExtendedDocumentResourceIndexFactory(RedisDocumentResourceIndex dri, Instantiable<EXTENDED_RESOURCE> factory, String [] extra) {
        this.dri=dri;
        this.factory=factory;
        this.extra=extra;
        this.myKeysArray=concat(dri.myKeys(),extra);
    }

    protected  RedisExtendedDocumentResourceIndexFactory(RedisDocumentResourceIndex dri, Instantiable<EXTENDED_RESOURCE> factory, String [] extra, String[] myKeysArray) {
        this.dri=dri;
        this.factory=factory;
        this.extra=extra;
        this.myKeysArray=myKeysArray;
    }

    public EXTENDED_RESOURCE newResource(DocumentResource dr) {
        final EXTENDED_RESOURCE extended_resource = factory.newResource(dr);
        return extended_resource;
    }


    @Override
    public EXTENDED_RESOURCE get(String key) {
        logger.debug("get " + key );
        logger.debug("get " + Arrays.toString(myKeys()));
        logger.debug("get " + dri.client );
        List<String> values=dri.client.hmget(key, myKeys());
        logger.debug("get " + key + values);
        Map<String,String> m=new HashMap<>();
        for (int i=0; i<myKeys().length; i++) {
            final String value = values.get(i);
            if (value!=null) m.put(myKeys()[i], value);
        }
        if (m.isEmpty()) {
            return null;
        }
        EXTENDED_RESOURCE er=factory.newResource(new RedisDocumentResource(m));
        logger.debug("get " + key + " " + m);
        return er;
    }

    @Override
    public void put(String key, EXTENDED_RESOURCE er) {
        RedisDocumentResource rdr=(RedisDocumentResource)er;
        dri.client.hmset(key,rdr.getMap());
        logger.debug("put done " + key + " " + rdr.getMap());
    }


    @Override
    public void remove(String key) {
        long n=dri.client.del(key);
        logger.debug("remove done " + key + " " + n);

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

    /**
     * Returns a thread safe instance.
     */
    @Override
    public ResourceIndex<EXTENDED_RESOURCE> getIndex() {
        return new RedisExtendedDocumentResourceIndexFactory(dri.getIndex(),factory,extra,myKeysArray);
    }

    @Override
    public void close() {
        dri.close();
    }

    /*
    @Override
    public <R2 extends EXTENDED_RESOURCE> RedisExtendedDocumentResourceIndexFactory<R2> getExtender(Instantiable<R2> factory2) {
        logger.info("Need to specify extras ... ");
        return new RedisExtendedDocumentResourceIndexFactory(dri,factory2,null);
    }

     */

}
