package org.openprovenance.prov.templates.library;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_instantiatingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_instantiatingBuilder;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mexpandingBean;
import org.openprovenance.prov.template.library.ptm_copy.client.common.Ptm_mexpandingBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import static org.openprovenance.prov.template.library.ptm_copy.logger.Logger.logPtm_instantiating;

public class PtmTest extends TestCase {

    ProvFactory pf=ProvFactory.getFactory();
    public void testPtm1() throws JacksonException {
        String res=logPtm_instantiating("doc123.prov", "prov-doc123.prov-csv", "tmpl/plead-filtering.provn", "bindings/ex1223.json",
                1, null, "luc.moreau@kcl.ac.uk", pf.newTimeNow().toString()) ;
        System.out.println(res);

        Ptm_mexpandingBean mbean=Ptm_mexpandingBuilder.examplar();
        System.out.println(mbean.process(new Ptm_mexpandingBuilder().args2csv()));
        System.out.println(tools.jackson.databind.json.JsonMapper.builderWithJackson2Defaults().build().writeValueAsString(mbean));

        Ptm_instantiatingBean bean=Ptm_instantiatingBuilder.examplar();
        System.out.println(bean.process(new Ptm_instantiatingBuilder().args2csv()));
        System.out.println(tools.jackson.databind.json.JsonMapper.builderWithJackson2Defaults().build().writeValueAsString(bean));

    }
}
