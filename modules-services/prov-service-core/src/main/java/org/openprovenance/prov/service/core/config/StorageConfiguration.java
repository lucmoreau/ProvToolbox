package org.openprovenance.prov.service.core.config;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.io.File;

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

    public static final String provStorageConfigurationFile=getSystemOrEnvironmentVariableOrDefault(PROV_STORAGE_CONFIG, "somefile-somewhere-NOT-DEFINED-in-PROV_STORAGE_CONFIG");

    public static StorageConfiguration loadConfiguration() {
        try {
            return JsonMapper.builderWithJackson2Defaults().build().readValue(new File(provStorageConfigurationFile), StorageConfiguration.class);
        } catch (JacksonException e) { // Jackson 3: unchecked, also for I/O errors (was IOException)
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        try {
            return JsonMapper.builderWithJackson2Defaults().build().writeValueAsString(this);
        } catch (JacksonException e) {
            throw new RuntimeException(e);
        }
    }
}
