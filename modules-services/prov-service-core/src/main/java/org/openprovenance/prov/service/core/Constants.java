package org.openprovenance.prov.service.core;

import java.util.Arrays;
import java.util.List;

public interface Constants {
	String DOCUMENT_NOT_FOUND = "Document not found";
	String WILDCARD = "*";
	String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
	String HEADER_PARAM_ACCEPT = "Accept";
    String PROVAPI = "provapi";
    

    String ALLOWABLE_INPUT_DOCUMENT_EXTENSIONS = "ttl,provn,provx,json,rdf,trig,jsonld";
    String ALLOWABLE_OUTPUT_DOCUMENT_EXTENSIONS = "ttl,provn,provx,json,rdf,trig,svg,png,pdf,jpg,jpeg,jsonld";
    List<String> translationExtensions = Arrays.asList(ALLOWABLE_OUTPUT_DOCUMENT_EXTENSIONS.split(","));


    String EXPANSION = "expansion";
    String TEMPLATE_EXPANSION = "templateExpansion";
    String SUMMARISATION = "summarisation";


    String GITHUB_CLIENT_SECRET = "GITHUB_CLIENT_SECRET";
    String GITHUB_CLIENT_ID = "GITHUB_CLIENT_ID";
}
