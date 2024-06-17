package org.openprovenance.prov.service.iobean.simple;

import org.openprovenance.prov.template.library.plead.client.configurator2.EnactorConfigurator2;

import java.sql.ResultSet;
import java.util.function.Function;

public class SqlEnactorConfigurator3 extends EnactorConfigurator2 {
    public SqlEnactorConfigurator3(Function<String, ResultSet> querier) {
        super(new SqlBeanEnactor3(querier));
    }

}
