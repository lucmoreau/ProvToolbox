package org.openprovenance.prov.service.obsolete;

import org.openprovenance.prov.template.library.plead.client.common.BeanCompleter;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlBeanCompleter extends BeanCompleter {

    public SqlBeanCompleter(ResultSet rs) {
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
