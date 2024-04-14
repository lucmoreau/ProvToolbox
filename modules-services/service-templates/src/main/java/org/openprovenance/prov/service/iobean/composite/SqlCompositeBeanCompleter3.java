package org.openprovenance.prov.service.iobean.composite;


import org.openprovenance.prov.template.library.plead.client.integrator.BeanCompleter2;
import org.openprovenance.prov.template.library.plead.client.integrator.Plead_transformingOutputs;
import org.openprovenance.prov.template.library.plead.client.integrator.Plead_transforming_compositeOutputs;

import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlCompositeBeanCompleter3 extends BeanCompleter2 {

    private final Object[] extra;
    private final ResultSet rs;

    public SqlCompositeBeanCompleter3(ResultSet rs) {
        this(rs,null);
    }
    public SqlCompositeBeanCompleter3(ResultSet rs, Object[] extra) {
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
        this.rs=rs;
        this.extra=extra;
    }


    @Override
    public boolean next() {
        try {
            return rs.next();  // ResultSet next() has already been called in generic_enact
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Plead_transformingOutputs process(Plead_transformingOutputs bean) {
        super.process(bean);
        extraProcess(bean);
        return bean;
    }

    public void extraProcess(Plead_transformingOutputs bean) {
        if (extra==null) return;
        int parent=getter.get(Integer.class,"parent");
        extra[0]=parent;
    }


    public Plead_transforming_compositeOutputs process(Plead_transforming_compositeOutputs bean) {
        Plead_transforming_compositeOutputs result=super.process(bean);
        bean.ID= (Integer) extra[0];
        return result;
        /*
        do  {
            Plead_transformingOutputs composee=new Plead_transformingOutputs();
            bean.__addElements(composee);
            process(composee);
            bean.ID= (Integer) extra[0];
        } while (next()) ;
        return bean;
         */
    }

}
