package org.openprovenance.prov.service.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class NoStorageConfiguration extends StorageConfiguration {
    private final static Logger logger = LogManager.getLogger(NoStorageConfiguration.class);

    public String dbname = "none";
    public String index = "none";
    public String storage = "none";
    public int cache = -1;
    public boolean autodelete = true;
    public int delete_period = 1200;

    public String redis_host = "nohost";
    public int redis_port = -1;
    public String mongo_host  = "nohost";
    public int mongo_port = -1;
    public String uploaded_filepath="nowhere";

    public NoStorageConfiguration() {
        logger.info("NoStorageConfiguration");
    }


    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
