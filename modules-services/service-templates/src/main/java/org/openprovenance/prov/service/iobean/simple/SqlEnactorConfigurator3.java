package org.openprovenance.prov.service.iobean.simple;

import org.openprovenance.prov.service.Storage;
import org.openprovenance.prov.template.library.plead.client.configurator.EnactorConfigurator2;

import java.sql.Connection;

public class SqlEnactorConfigurator3 extends EnactorConfigurator2 {
    public SqlEnactorConfigurator3(Storage storage, Connection conn) {
        super(new SqlBeanEnactor3(storage, conn));
    }

}
