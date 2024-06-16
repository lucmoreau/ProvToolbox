package org.openprovenance.prov.service.iobean.composite;


import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.configurator2.CompositeEnactorConfigurator2;

import java.sql.Connection;

public class SqlCompositeEnactorConfigurator3 extends CompositeEnactorConfigurator2 {

    public SqlCompositeEnactorConfigurator3(Storage storage, Connection conn) {
        super(new SqlCompositeBeanEnactor3(storage, conn));
    }

}
