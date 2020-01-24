package org.openprovenance.prov.storage.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.storage.api.DocumentResource;
import org.openprovenance.prov.storage.api.ResourceIndex;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RedisDocumentResource implements DocumentResource {

    final Locale loc = new Locale("en", "UK");
    final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, loc);


    final Map<String,String> m;
    private Document doc;
    private Throwable thrown;

    public RedisDocumentResource(Map<String, String> m) {
        this.m = m;
    }

    public RedisDocumentResource() {
        this.m=new HashMap<>();
    }

    @Override
    public String getVisibleId() {
        return m.get(RedisDocumentResourceIndex.FIELD_VISIBLE_ID);
    }

    @Override
    public void setVisibleId(String visibleId) {
        m.put(RedisDocumentResourceIndex.FIELD_VISIBLE_ID,visibleId);
    }

    @Override
    public String getStorageId() {
        return m.get(RedisDocumentResourceIndex.FIELD_STORE_ID);
    }

    @Override
    public void setStorageId(String storageId) {
        m.put(RedisDocumentResourceIndex.FIELD_STORE_ID, storageId);
    }

    @Override
    public Date getExpires() {
        try {
            String dateString = m.get(RedisDocumentResourceIndex.FIELD_EXPIRES);
            if (dateString!=null) return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setExpires(Date expires) {
        if (expires!=null) m.put(RedisDocumentResourceIndex.FIELD_EXPIRES,dateFormat.format(expires));
    }

    @Override
    public ResourceIndex.StorageKind getKind(){
        return ResourceIndex.StorageKind.RE;
    }

    @Override
    public void setKind(ResourceIndex.StorageKind kind) {
    }

    @Override
    public Throwable getThrown() {
        return thrown;
    }

    @Override
    public void setThrown(Throwable thrown) {
        this.thrown=thrown;
    }

    ObjectMapper om=new ObjectMapper();

    Map<String, Object> extension=null;

    @Override
    public Map<String, Object> getExtension() {
        if (extension!=null) return extension;
        String s= m.get(RedisDocumentResourceIndex.FIELD_EXTENSION);
        if (s==null) {
            extension=new HashMap<>();
            return extension;
        }
        Map<String, Object> m=null;
        try {
            m = om.readValue(s, Map.class);
            extension=m;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public void setExtension(Map<String, Object> extension) {
        if (extension!=null) {
            String s = null;
            try {
                s = om.writeValueAsString(extension);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            if (s != null) {
                m.put(RedisDocumentResourceIndex.FIELD_EXTENSION, s);
                this.extension=extension;
            }
        }
    }

    public Map<String, String> getMap() {
        return m;
    }
}
