package org.openprovenance.prov.service.core.dispatch;

import org.openprovenance.prov.client.Builder;
import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.BuilderProcessorConfigurator;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;

import java.util.function.Function;

public class ForeignTableConfigurator extends BuilderProcessorConfigurator<String[]> implements TableConfigurator<String[]> {

    public ForeignTableConfigurator(Function<Builder, String[]> _processor) {
        super(_processor);
    }
    public ForeignTableConfigurator() {
        this(Builder::getForeign);
    }

}
