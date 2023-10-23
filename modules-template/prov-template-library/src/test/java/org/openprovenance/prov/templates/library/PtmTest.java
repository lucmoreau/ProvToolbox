package org.openprovenance.prov.templates.library;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openprovenance.prov.template.library.ptm.client.common.Ptm_expandingBean;
import org.openprovenance.prov.template.library.ptm.client.common.Ptm_expandingBuilder;
import org.openprovenance.prov.template.library.ptm.client.common.Ptm_mexpandingBean;
import org.openprovenance.prov.template.library.ptm.client.common.Ptm_mexpandingBuilder;
import org.openprovenance.prov.vanilla.ProvFactory;

import static org.openprovenance.prov.template.library.ptm.logger.Logger.logPtm_expanding;

public class PtmTest extends TestCase {

    ProvFactory pf=ProvFactory.getFactory();
    public void testPtm1() throws JsonProcessingException {
        String res=logPtm_expanding("doc123.prov", "tmpl/plead-filtering.provn", "bindings/ex1223.json",
                1, null, "luc.moreau@kcl.ac.uk", pf.newTimeNow().toString()) ;
        System.out.println(res);

        Ptm_mexpandingBean mbean=Ptm_mexpandingBuilder.examplar();
        System.out.println(mbean.process(new Ptm_mexpandingBuilder().args2csv()));
        System.out.println(new ObjectMapper().writeValueAsString(mbean));

        Ptm_expandingBean bean=Ptm_expandingBuilder.examplar();
        System.out.println(bean.process(new Ptm_expandingBuilder().args2csv()));
        System.out.println(new ObjectMapper().writeValueAsString(bean));

    }
}
