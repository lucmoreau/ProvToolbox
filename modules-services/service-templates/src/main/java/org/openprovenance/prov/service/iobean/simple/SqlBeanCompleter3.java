package org.openprovenance.prov.service.iobean.simple;


import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlBeanCompleter3 extends BeanCompleter2 {

    public SqlBeanCompleter3(ResultSet rs) {
        super(new Getter() {
            @Override
            public <T> T get(Class<T> cl, String col) {
                try {
                    return rs.getObject(col,cl);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
