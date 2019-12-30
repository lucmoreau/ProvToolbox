package org.openprovenance.prov.redis;

import org.openprovenance.prov.service.core.*;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.util.*;

import org.apache.log4j.Logger;

public class RedisDocumentResourceIndex implements ResourceIndex<DocumentResource> {

    static Logger logger = Logger.getLogger(RedisDocumentResourceIndex.class);

     static final String FIELD_VISIBLE_ID ="_vid_";
     static final String FIELD_STORE_ID ="_id_";
     static final String FIELD_EXPIRES ="_expires_";
     static final String FIELD_KIND ="_kind_";

    final Locale loc = new Locale("en", "UK");
    final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);


    private final Jedis client;

    public RedisDocumentResourceIndex() {
        client=new Jedis();
    }
    public RedisDocumentResourceIndex(String host) {
        client = new Jedis(host);
    }
    public RedisDocumentResourceIndex(String host, int port) {
        client = new Jedis(host, port);
    }

    public String[] myKeys()  { return new String[] {FIELD_STORE_ID, FIELD_EXPIRES, FIELD_KIND} ; }


    @Override
    public DocumentResource get(String key) {
        logger.info("get " + key );
        List<String> values=client.hmget(key, FIELD_STORE_ID, FIELD_EXPIRES, FIELD_KIND);
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

    @Override
    public <EXTENDED_RESOURCE extends DocumentResource> ExtendedDocumentResourceIndexFactory<EXTENDED_RESOURCE> getExtender(Instantiable<EXTENDED_RESOURCE> factory) {
        return new ExtendedDocumentResourceIndexFactory(this,factory);
    }

    @Override
    public void remove(String key) {

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
}
