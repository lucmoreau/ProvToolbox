package org.openprovenance.prov.storage.redis;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.Instantiable;
import org.openprovenance.prov.storage.api.ResourceIndex;
import redis.clients.jedis.Jedis;

import java.text.DateFormat;
import java.util.*;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDocumentResourceIndex implements ResourceIndex<DocumentResource> {

    private static Logger logger = LogManager.getLogger(RedisDocumentResourceIndex.class);

    public static final String FIELD_VISIBLE_ID  = "_vid_";
    public static final String FIELD_EXTENSION   = "_ext_";
    public static final String FIELD_STORE_ID    = "_id_";
    public static final String FIELD_EXPIRES     = "_exp_";
    public static final String FIELD_KIND        = "_kind_";
    public static final String FIELD_BINDINGS_ID = "_bid_";
    public static final String FIELD_TEMPLATE_ID = "_tid_";

    private final String[] myKeyArray = {FIELD_STORE_ID, FIELD_EXPIRES, FIELD_KIND, FIELD_EXTENSION};


    final Locale loc = new Locale("en", "UK");
    final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);


    protected final Jedis client;
    private final JedisPool pool;

    public RedisDocumentResourceIndex() {
        this(new JedisPool(new JedisPoolConfig(), "localhost"));
    }

    public RedisDocumentResourceIndex(String host) {
        this(new JedisPool(new JedisPoolConfig(), host));
    }
    public RedisDocumentResourceIndex(String host, int port) {
        this(new JedisPool(new JedisPoolConfig(), host, port));
    }

    public RedisDocumentResourceIndex(JedisPool pool) {
        this.pool=pool;
        this.client=pool.getResource();
    }

    private RedisDocumentResourceIndex(JedisPool pool, Jedis client) {
        this.pool=pool;
        this.client=client;
    }

    public RedisDocumentResourceIndex getIndex() {
        return new RedisDocumentResourceIndex(pool, pool.getResource());
    }

    @Override
    public void close() {
        client.close();
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
        List<String> values=client.hmget(key, myKeys());
        Map<String,String> m=new HashMap<>();
        for (int i=0; i<myKeys().length; i++) {
            final String value = values.get(i);
            if (value!=null) m.put(myKeys()[i], value);
        }
        if (m.isEmpty()) {
            return null;
        }
        logger.debug("get " + key + " " + m);
        DocumentResource dr=new RedisDocumentResource(m);
        return dr;
    }

    @Override
    public void put(String key, DocumentResource dr) {
        logger.debug("put " + key );
        RedisDocumentResource rdr=(RedisDocumentResource)dr;
        client.hmset(key,rdr.getMap());

    }


    @Override
    public void remove(String key) {
        //client.hdel(key,myKeys());
        Long n=client.del(key);
        logger.debug("removed " + key + " " + n);
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
        put(id,rdr);
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
