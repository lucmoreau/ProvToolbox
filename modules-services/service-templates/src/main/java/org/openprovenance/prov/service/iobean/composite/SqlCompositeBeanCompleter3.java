package org.openprovenance.prov.service.iobean.composite;


import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;
import org.openprovenance.prov.template.library.plead.client.integrator.Plead_transformingOutputs;
import org.openprovenance.prov.template.library.plead.client.integrator.Plead_transforming_compositeOutputs;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.openprovenance.prov.service.iobean.simple.SqlBeanCompleter3.newGetter;


public class SqlCompositeBeanCompleter3 extends BeanCompleter2 {

    public static final String PARENT_COLUMN = "parent";
    private final Object[] extra;
    private final ResultSet rs;

    public SqlCompositeBeanCompleter3(ResultSet rs) {
        this(rs,null);
    }

    public SqlCompositeBeanCompleter3(ResultSet rs, Object[] extra) {
        super(newGetter(rs));
        this.rs=rs;
        this.extra=extra;
    }

    private void setLocation(int parent) {
        extra[0]= parent;
    }
    public Integer getLocation() {
        return (Integer) extra[0];
    }
    private boolean locationNotExists() {
        return extra == null;
    }

    @Override
    public boolean next() {
        try {
            return rs.next();  // ResultSet next() has already been called in generic_enact
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public Plead_transforming_compositeOutputs process(Plead_transforming_compositeOutputs bean) {
        Plead_transforming_compositeOutputs result=super.process(bean);
        bean.ID= getLocation();
        return result;
    }

    public Plead_transformingOutputs process(Plead_transformingOutputs bean) {
        super.process(bean);
        extraProcessComposee(bean);
        return bean;
    }

    public void extraProcessComposee(Plead_transformingOutputs bean) {
        if (locationNotExists()) return;
        int parent=getter.get(Integer.class, PARENT_COLUMN);
        setLocation(parent);
    }

}
