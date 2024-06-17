package org.openprovenance.prov.service.iobean.composite;



import java.sql.ResultSet;
import java.sql.SQLException;

import static  org.openprovenance.prov.template.library.plead.sql.integration.SqlBeanCompleter3.newGetter;
import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter3;


public class SqlCompositeBeanCompleter3 extends BeanCompleter3 {

    public static final String PARENT_COLUMN = "parent";
    private final Object[] extra;
    private final ResultSet rs;

    public SqlCompositeBeanCompleter3(ResultSet rs) {
        this(rs,null);
    }

    public SqlCompositeBeanCompleter3(ResultSet rs, Object[] extra) {
        super(SqlBeanCompleter3.newGetter(rs));
        this.rs=rs;
        this.extra=extra;
    }

    public Integer getValueFromLocation() {
        return (Integer) extra[0];
    }


    @Override
    public boolean next() {
        try {
            return rs.next();  // ResultSet next() has already been called in generic_enact
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setValueInLocation() {
        if (extra != null) {
            int parent = getter.get(Integer.class, PARENT_COLUMN);
            extra[0]= parent;
        }
    }

}
