package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.openprovenance.prov.template.expander.BindingsJson.BindingsBean;


public class MongoBindingsResourceStorage extends MongoGenericResourceStorage<BindingsBean> implements Constants {



    private static Logger logger = Logger.getLogger(MongoBindingsResourceStorage.class);



    public MongoBindingsResourceStorage(String dbname, ObjectMapper mapper) {

        super(dbname,COLLECTION_BINDINGS, mapper, BindingsBean.class, () -> new BindingsWrapper());
    }
    public MongoBindingsResourceStorage(String host, String dbname, ObjectMapper mapper) {

        super(host, dbname,COLLECTION_BINDINGS, mapper, BindingsBean.class, () -> new BindingsWrapper());
    }

}
