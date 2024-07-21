package org.openprovenance.prov.service.client;

import org.openprovenance.prov.configuration.Configuration;
import org.openprovenance.prov.model.interop.ApiUriFragments;

import java.util.Objects;
import java.util.Properties;

public class ClientConfig implements ApiUriFragments {


    public final Properties properties;
    public final  String port ;
    public final  String context;
    public final  String host ;
    public final  String protocol;
    public final  String hostURLprefix;
    public final  String hostURLprefixContext;
    public final  String postURL;
    public final  String formURL;
    public final  String resourcesURLprefix;
    public final  String htmlURL ;


    public ClientConfig(Class<?> clazz) {  //
        properties = Objects.requireNonNull(Configuration.getPropertiesFromClasspath(clazz, "config.properties"));
        port = properties.getProperty("service.port");
        context = properties.getProperty("service.context");
        host = properties.getProperty("service.host");
        protocol = properties.getProperty("service.protocol");

        hostURLprefix = protocol + "://" + host + ":" + port;
        hostURLprefixContext = protocol + "://" + host + ":" + port + context;
        postURL = hostURLprefixContext + FRAGMENT_PROVAPI + FRAGMENT_DOCUMENTS;
        formURL = hostURLprefixContext + FRAGMENT_PROVAPI + FRAGMENT_DOCUMENTS_FORM;
        resourcesURLprefix = hostURLprefixContext + FRAGMENT_PROVAPI + FRAGMENT_RESOURCES;
        htmlURL = hostURLprefixContext + "/contact.html";
    }

}