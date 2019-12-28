package org.openprovenance.prov.redis;

import org.openprovenance.prov.service.core.DocumentResource;
import org.openprovenance.prov.service.core.ResourceIndex;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import org.apache.log4j.Logger;

public class RedisIndex implements ResourceIndex {

    static Logger logger = Logger.getLogger(RedisIndex.class);

    private static final String FIELD_KEY ="_key_";
    private static final String FIELD_STORE_ID ="_id_";
    private static final String FIELD_EXPIRES ="_expires_";
    private static final String FIELD_KIND ="_kind_";

    final Locale loc = new Locale("en", "UK");
    final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);


    private final Jedis client;

    public RedisIndex() {
        client=new Jedis();
    }
    public RedisIndex(String host) {
        client = new Jedis(host);
    }
    public RedisIndex(String host, int port) {
        client = new Jedis(host, port);
    }


    @Override
    public DocumentResource get(String key) {
        logger.info("get " + key );
        List<String> values=client.hmget(key, FIELD_STORE_ID, FIELD_EXPIRES, FIELD_KIND);
        logger.info("get " + key + values);
        DocumentResource dr=new DocumentResource();
        dr.visibleId =key;
        dr.storageId =values.get(0);
        try {
            String dateString = values.get(1);
            if (dateString!=null) dr.expires= dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            dr.expires=null;
        }
        String kindString = values.get(2);
        if (kindString!=null) dr.kind=StorageKind.valueOf(kindString);
        logger.info("get " + key + " " + dr);
        return dr;
    }

    @Override
    public void put(String key, DocumentResource dr) {
        logger.info("put " + key );
        Map<String,String> m=new HashMap<>();
        m.put(FIELD_STORE_ID,dr.storageId);
        if (dr.expires!=null) m.put(FIELD_EXPIRES,dateFormat.format(dr.expires));
        if (dr.kind!=null) m.put(FIELD_KIND,dr.kind.name());
        client.hmset(key,m);
        logger.info("put done " + key );

    }

    @Override
    public void remove(String key) {

    }

    @Override
    public String newId() {
        Long count=client.hincrBy(FIELD_KEY, FIELD_KEY,1);
        return "r"+count;
    }

    @Override
    public StorageKind kind() {
        return StorageKind.RE;
    }
}
