package org.openprovenance.prov.service.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryConfiguration {
    static Logger logger = LogManager.getLogger(QueryConfiguration.class);
    public boolean enableExternalQueries;
    public List<String> externalQueryPath;

    @Override
    public String toString() {
        return "QueryConfiguration{" +
                "enableExternalQueries=" + enableExternalQueries +
                ", externalQueryPath=" + externalQueryPath +
                '}';
    }

    public static QueryConfiguration DisabledConfig() {
        QueryConfiguration disabledConfiguration = new QueryConfiguration();
        disabledConfiguration.enableExternalQueries = false;
        disabledConfiguration.externalQueryPath = new ArrayList<>();
        logger.info("QueryConfiguration disabled");
        return disabledConfiguration;
    }


    public static QueryConfiguration newQueryConfiguration(String queryPath) {
        if (queryPath==null || queryPath.isEmpty()) return DisabledConfig();
        String[] path=queryPath.split(":");
        QueryConfiguration queryConfiguration = new QueryConfiguration();
        queryConfiguration.enableExternalQueries=true;
        queryConfiguration.externalQueryPath=new ArrayList<>();
        queryConfiguration.externalQueryPath.addAll(Arrays.asList(path));
        logger.debug("QueryConfiguration: " + queryConfiguration);
        return queryConfiguration;
    }


    public static QueryConfiguration newQueryConfiguration(QueryConfiguration queryConfiguration) {
        if (queryConfiguration==null
                || !queryConfiguration.enableExternalQueries
                || queryConfiguration.externalQueryPath == null
                || queryConfiguration.externalQueryPath.isEmpty()) return DisabledConfig();
        QueryConfiguration newqueryConfiguration = new QueryConfiguration();
        newqueryConfiguration.enableExternalQueries=true;
        newqueryConfiguration.externalQueryPath=new ArrayList<>();
        newqueryConfiguration.externalQueryPath.addAll(queryConfiguration.externalQueryPath);
        logger.info("QueryConfiguration: " + newqueryConfiguration);
        return newqueryConfiguration;
    }
}
