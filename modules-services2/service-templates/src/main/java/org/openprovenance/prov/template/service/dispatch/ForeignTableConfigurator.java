package org.openprovenance.prov.template.service.dispatch;

import org.openprovenance.prov.client.Builder;
import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.configurator.BuilderProcessorConfigurator;
import org.openprovenance.prov.template.library.plead.configurator.TableConfigurator;

import java.util.function.Function;

public class ForeignTableConfigurator extends BuilderProcessorConfigurator<String[]> implements TableConfigurator<String[]> {

    public ForeignTableConfigurator(Function<Builder, String[]> _processor) {
        super(_processor);
    }
    public ForeignTableConfigurator() {
        this(Builder::getForeign);
    }

}
