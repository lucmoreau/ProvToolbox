package org.openprovenance.prov.service.iobean.composite;


import org.openprovenance.prov.template.library.plead.client.configurator2.CompositeEnactorConfigurator2;

import java.sql.ResultSet;
import java.util.function.Function;

public class SqlCompositeEnactorConfigurator3 extends CompositeEnactorConfigurator2 {

    public SqlCompositeEnactorConfigurator3(Function<String, ResultSet> querier) {
        super(new SqlCompositeBeanEnactor3(querier));
    }

}
