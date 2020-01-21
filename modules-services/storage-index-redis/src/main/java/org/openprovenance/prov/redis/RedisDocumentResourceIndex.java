package org.openprovenance.prov.redis;

import org.openprovenance.prov.service.core.*;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.util.*;

import org.apache.log4j.Logger;

public class RedisDocumentResourceIndex implements ResourceIndex<DocumentResource> {

    private static Logger logger = Logger.getLogger(RedisDocumentResourceIndex.class);

    static final String FIELD_VISIBLE_ID ="_vid_";
    static final String FIELD_STORE_ID ="_id_";
    static final String FIELD_EXPIRES ="_expires_";
    static final String FIELD_KIND ="_kind_";
    static final String FIELD_BINDINGS_ID="_bid_";
    static final String FIELD_TEMPLATE_ID="_tid_";

    private final String[] myKeyArray = {FIELD_STORE_ID, FIELD_EXPIRES, FIELD_KIND};


    final Locale loc = new Locale("en", "UK");
    final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);


    protected final Jedis client;

    public RedisDocumentResourceIndex() {
        client=new Jedis();
    }
    public RedisDocumentResourceIndex(String host) {
        client = new Jedis(host);
    }
    public RedisDocumentResourceIndex(String host, int port) {
        client = new Jedis(host, port);
    }

    public String[] myKeys()  { return myKeyArray; }


    public static Instantiable<DocumentResource> factory =new Instantiable<DocumentResource>() {
        @Override
        public DocumentResource newResource(DocumentResource dr) {
            throw new UnsupportedOperationException("Should not attempt to create a Document Resource from a Document Resource");
        }

        @Override
        public DocumentResource newResource() {
            return new RedisDocumentResource(new HashMap<>());
        }
    };

    @Override
    public DocumentResource get(String key) {
        logger.info("get " + key );
        //List<String> values=client.hmget(key, FIELD_STORE_ID, FIELD_EXPIRES, FIELD_KIND);
        List<String> values=client.hmget(key, myKeys());
        logger.info("get " + key + values);
        Map<String,String> m=new HashMap<>();
        for (int i=0; i<myKeys().length; i++) {
            m.put(myKeys()[i],values.get(i));
        }
        DocumentResource dr=new RedisDocumentResource(m);
        logger.info("get " + key + " " + m);
        return dr;
    }

    @Override
    public void put(String key, DocumentResource dr) {
        logger.info("put " + key );
        RedisDocumentResource rdr=(RedisDocumentResource)dr;
        client.hmset(key,rdr.getMap());
        logger.info("put done " + key );

    }
/*
    @Override
    public <EXTENDED_RESOURCE extends DocumentResource> ExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE> getExtender(Instantiable<EXTENDED_RESOURCE> f) {
        return new ExtendedDocumentResourceIndexFactory(this,f);
    }

 */

    @Override
    public void remove(String key) {
        client.del(key);
    }

    @Override
    public String newId() {
        Long count=client.hincrBy(FIELD_VISIBLE_ID, FIELD_VISIBLE_ID,1);
        return "r"+count;
    }

    @Override
    public DocumentResource newResource() {
        String id=newId();
        DocumentResource rdr=new RedisDocumentResource();
        rdr.setVisibleId(id);
        return rdr;
    }

    @Override
    public DocumentResource newResource(DocumentResource dr) {
        return new RedisDocumentResource( ((RedisDocumentResource)dr).m);
    }

    @Override
    public StorageKind kind() {
        return StorageKind.RE;
    }

    @Override
    public ResourceIndex<DocumentResource> getAncestor() {
        throw new UnsupportedOperationException("No ancestor to Root Index");
    }
}
