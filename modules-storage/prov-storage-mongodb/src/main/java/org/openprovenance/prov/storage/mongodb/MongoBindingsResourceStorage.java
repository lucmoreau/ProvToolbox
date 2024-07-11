package org.openprovenance.prov.storage.mongodb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openprovenance.prov.template.json.Bindings;


public class MongoBindingsResourceStorage extends MongoGenericResourceStorage<Bindings> implements Constants {



    private static Logger logger = LogManager.getLogger(MongoBindingsResourceStorage.class);



    public MongoBindingsResourceStorage(String dbname, ObjectMapper mapper) {

        super(dbname,COLLECTION_BINDINGS, mapper, Bindings.class, BindingsWrapper::new);
    }
    public MongoBindingsResourceStorage(String host, String dbname, ObjectMapper mapper) {

        super(host, dbname,COLLECTION_BINDINGS, mapper, Bindings.class, BindingsWrapper::new);
    }

}
