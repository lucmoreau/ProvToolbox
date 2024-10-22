package org.openprovenance.prov.service.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



public class NoStorageConfiguration extends StorageConfiguration {
    private final static Logger logger = LogManager.getLogger(NoStorageConfiguration.class);

    public NoStorageConfiguration() {
        logger.info("NoStorageConfiguration");
        dbname = "none";
        index = "none";
        storage = "none";
        cache = -1;
        autodelete = true;
        delete_period = 1200;

        redis_host = "nohost";
        redis_port = -1;
        mongo_host  = "nohost";
        mongo_port = -1;
        uploaded_filepath="nowhere";
    }

}
