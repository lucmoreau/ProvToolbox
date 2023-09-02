package org.openprovenance.prov.template.expander;

import java.util.Hashtable;
import java.util.List;

public class BindingsBean {
    public Hashtable<String, List<Object>> var;
    public Hashtable<String, List<Object>> vargen;
    public Hashtable<String, String> context;
    public String template;
}