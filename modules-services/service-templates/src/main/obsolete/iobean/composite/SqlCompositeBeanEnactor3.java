package org.openprovenance.prov.service.iobean.composite;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanChecker2;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanEnactor2Composite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.function.Function;


public class SqlCompositeBeanEnactor3 extends BeanEnactor2Composite<ResultSet> {

    public SqlCompositeBeanEnactor3(Function<String,ResultSet> querier) {
        super(new SqlCompositeEnactorImplementation3(querier), new BeanChecker2());
    }

}


