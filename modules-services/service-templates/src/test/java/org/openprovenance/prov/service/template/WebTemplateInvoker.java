package org.openprovenance.prov.service.template;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.template.library.plead.client.integrator.TemplateInvoker;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class WebTemplateInvoker extends TemplateInvoker {
    static Logger logger = LogManager.getLogger(WebTemplateInvoker.class);
    final ServiceInvoker si=new ServiceInvoker();


    final static ParameterizedTypeReference<List<Map<String, Object>>> listType= new ParameterizedTypeReference<>() {};
    private final String url;
    private String username;
    private String password;
    private String authoriser;
    private String accessToken;

    public WebTemplateInvoker(String url) {
        super();
        this.url=url;
    }

    public void setAuthoriser(String authoriser, String username, String password) {
        this.authoriser=authoriser;
        this.username=username;
        this.password=password;
        if ((authoriser!=null)&&(username!=null)&&(password!=null)) {
            this.accessToken=si.getAccessTokenValue(authoriser,username, password);
        } else {
            this.accessToken=null;
        }
    }

    static final Map<String,String> environment=System.getenv();

    private String getEnvironmentVariable(Map<String, String> environment, String key) {
        final String value = environment.get(key);
        return value;
    }
    public void setAuthoriser() {
        setAuthoriser(
                getEnvironmentVariable(environment, "EMS_KEYCLOAK"),
                getEnvironmentVariable(environment, "EMS_USERNAME"),
                getEnvironmentVariable(environment, "EMS_PASSWORD"));

    }


    public  <IN, OUT> OUT generic_post_and_return(Class<OUT> clazz,
                                                  IN inputs0,
                                                  BiFunction<Map<String, Object>, OUT, OUT> completer) {
        List<IN> inputs = Collections.singletonList(inputs0);
        Optional<List<Map<String, Object>>> out1 = si.postInstructionsInOut(url, inputs, listType, accessToken);
        if (!out1.isPresent()) {
            logger.warn("out1 is empty for Updating_obligationInputs ");
            System.out.println("out1 is empty for Updating_obligationInputs ");
            throw new IllegalStateException("out1 is empty for Updating_obligationInputs ");
        } else {
            List<Map<String, Object>> result0 = out1.get();
            Map<String, Object> result1 = result0.get(0);
            try {
                return completer.apply(result1,clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
