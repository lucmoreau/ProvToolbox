package org.openprovenance.prov.service.dispatch;

import org.openprovenance.prov.template.library.plead.client.common.*;
import org.openprovenance.prov.template.library.plead.client.configurator.TableConfigurator;

public class ForeignTableConfigurator implements TableConfigurator<String[]> {

    @Override
    public String[] plead_transforming(Plead_transformingBuilder builder) {
        return Plead_transformingBuilder.foreignTables;
    }

    @Override
    public String[] plead_filtering(Plead_filteringBuilder builder) {
        return Plead_filteringBuilder.foreignTables;
    }

    @Override
    public String[] plead_training(Plead_trainingBuilder builder) {
        return Plead_trainingBuilder.foreignTables;
    }

    @Override
    public String[] plead_validating(Plead_validatingBuilder builder) {
        return Plead_validatingBuilder.foreignTables;
    }

    @Override
    public String[] plead_approving(Plead_approvingBuilder builder) {
        return Plead_approvingBuilder.foreignTables;
    }

    @Override
    public String[] plead_splitting(Plead_splittingBuilder builder) {
        return Plead_splittingBuilder.foreignTables;
    }


    @Override
    public String[] plead_transforming_composite(Plead_transforming_compositeBuilder builder) {
        return null;
    }

}
