package org.openprovenance.prov.template.core.ttf;

import java.io.IOException;
import java.util.Map;

public interface ConfigTask {
    void execute(TemplateTasksBatch templateTasksBatch, BatchExecutor be,  Map<String, String> variableMap, String inputBasedir) throws IOException;
    String hasProvenance();
}
