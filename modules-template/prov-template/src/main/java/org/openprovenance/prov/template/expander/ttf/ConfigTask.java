package org.openprovenance.prov.template.expander.ttf;

import org.openprovenance.prov.template.utils.TemplateIndexPath;

import java.io.IOException;
import java.util.Map;

public interface ConfigTask {
    void execute(TemplateTasksBatch templateTasksBatch, BatchExecutor be,  Map<String, String> variableMap, String inputBasedir) throws IOException;
    String hasProvenance();
}
