package org.openprovenance.prov.service.core.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import static org.openprovenance.prov.service.core.ServiceUtils.getSystemOrEnvironmentVariableOrDefault;

public class StorageConfiguration implements EnvironmentVariables {
    public String dbname = "prov";
    public String index = "redis";
    public String storage = "mongodb";
    public int cache = 200;
    public boolean autodelete = true;
    public int delete_period = 1200;

    public String redis_host = "localhost";
    public int redis_port = 6379;
    public String mongo_host  = "localhost";
    public int mongo_port = 27017;
    public String uploaded_filepath="/somewhere/to/upload";

    public static final String provStorageConfigurationFile=getSystemOrEnvironmentVariableOrDefault(PROV_STORAGE_CONFIG, "somefile-somewhere");
    public static StorageConfiguration loadConfiguration() {
        try {
            return new ObjectMapper().readValue(new File(provStorageConfigurationFile), StorageConfiguration.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
