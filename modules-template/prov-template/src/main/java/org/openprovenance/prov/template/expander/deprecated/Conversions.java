package org.openprovenance.prov.template.expander.deprecated;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.template.expander.InstantiateUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.openprovenance.prov.template.expander.InstantiateUtil.TMPL_NS;
import static org.openprovenance.prov.template.expander.InstantiateUtil.TMPL_PREFIX;
import static org.openprovenance.prov.template.expander.deprecated.OldBindings.*;
import static org.openprovenance.prov.template.json.Bindings.mapper;

@Deprecated
public class Conversions {
    /*
    static public OldBindings fromBean(BindingsBean bean, ProvFactory pf) {
        OldBindings result = new OldBindings(pf);
        Namespace ns = new Namespace(bean.context);

        for (Map.Entry<String, List<Object>> bindings : bean.var.entrySet()) {
            String var = bindings.getKey();
            int i = 0;
            List<List<TypedValue>> allvalues = new LinkedList<List<TypedValue>>();

            boolean single_value = true;

            for (Object value : bindings.getValue()) {

                List<Object> wrapped;
                if (!(value instanceof List)) {
                    wrapped = new LinkedList<Object>();
                    wrapped.add(value);
                } else {
                    wrapped = (List<Object>) value;
                    single_value = false;

                }
                int count = 0;


                List<TypedValue> values = new LinkedList<TypedValue>();

                for (Object o : wrapped) {
                    Object[] conversion = convertBeanToValue(o, bean.context, ns, pf);
                    Object theValue = conversion[0];
                    QualifiedName type = (QualifiedName) conversion[1];
                    if (!(type.equals(pf.getName().PROV_QUALIFIED_NAME)))
                        single_value = false; // interested only in qualified names
                    values.add(pf.newAttribute(ExpandUtil.TMPL_NS, OldBindings.VALUE2 + i + "_" + count, ExpandUtil.TMPL_PREFIX, theValue, type));
                    count++;
                }

                allvalues.add(values);

                i++;
            }

            QualifiedName myvar = pf.newQualifiedName(ExpandUtil.VAR_NS, var, "var");

            result.getAttributes().put(myvar, allvalues);

            if (single_value) {
                List<QualifiedName> ll = new LinkedList<QualifiedName>();
                for (Object o : bindings.getValue()) {
                    Object[] conversion = convertBeanToValue(o, bean.context, ns, pf);
                    Object theValue = conversion[0];
                    ll.add((QualifiedName) theValue);
                }
                result.getVariables().put(myvar, ll);
            }


        }


        return result;
    }


     */

    public static void exportBean(String output_file1,
                                  BindingsBean bean,
                                  boolean pretty) {

        try {
            if (pretty) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(new File(output_file1), bean);
            } else {
                mapper.writeValue(new File(output_file1), bean);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON serialization failed", e);
        }
    }


    public static BindingsBean importBean(File input_file1) {

        try {
            return mapper.readValue(input_file1,BindingsBean.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON serialization failed", e);
        }
    }


    public static OldBindings fromDocument_v2(Document doc, ProvFactory pf) {
        OldBindings result=new OldBindings(pf);
        List<Entity> entities=u.getEntity(doc);
        for (Entity entity: entities) {
            Map<Integer,QualifiedName> map= new HashMap<>();
            Map<Integer, Set<TypedValue>> map_v2= new HashMap<>();
            for (Other attr: entity.getOther()) {
                String uri = attr.getElementName().getUri();
                if (uri.startsWith(APP_VALUE_v2)) {
                    String index=uri.substring(APP_VALUE_v2.length());
                    Integer i=Integer.valueOf(index);
                    map_v2.computeIfAbsent(i, k -> new HashSet<>());
                    map_v2.get(i).add(attr);
                } else {
                    if (uri.startsWith(APP_VALUE)) {
                        Integer i=Integer.valueOf(uri.substring(APP_VALUE.length()));
                        map_v2.computeIfAbsent(i, k -> new HashSet<>());
                        map_v2.get(i).add(attr);
                    }
                }
            }

            // reconstructing the map data structure for variables uniquely mapped to qualified names.
            boolean single_map=true;
            for (int i: map_v2.keySet()) {
                single_map=single_map && (map_v2.get(i).size()==1);
                if (single_map) {
                    for (TypedValue tv: map_v2.get(i)) { // only one element to iterate over
                        single_map=(tv.getValue() instanceof QualifiedName) ;
                    }
                }
            }
            if (single_map) {
                for (int i: map_v2.keySet()) {
                    for (TypedValue tv: map_v2.get(i)) { // only one element to iterate over
                        map.put(i, (QualifiedName) tv.getValue());
                    }
                }
            }

            List<QualifiedName> ll= new ArrayList<>();
            int size=map.entrySet().size();
            if (size>0) {
                for (Map.Entry<Integer, QualifiedName> entry: map.entrySet()) {
                    set(entry.getKey(),ll,entry.getValue());
                }
                result.getVariables().put(entity.getId(),ll);
            }

            int size2=map_v2.entrySet().size();
            if (size2>0) {
                List<List<TypedValue>> allvalues= new LinkedList<>();

                for (Map.Entry<Integer, Set<TypedValue>> entry1: map_v2.entrySet()) {
                    List<TypedValue> values= new LinkedList<>();
                    //convert to 2dvalue
                    int count=0;
                    for (TypedValue tv: entry1.getValue()) {
                        if (tv instanceof Other) {
                            Other o=(Other)tv;
                            String uri = o.getElementName().getUri();
                            if (uri.startsWith(APP_VALUE_v2)) {
                                Integer i=Integer.valueOf(uri.substring(APP_VALUE_v2.length()));
                                values.add(pf.newAttribute(TMPL_NS, VALUE2+i+"_"+count, TMPL_PREFIX, o.getValue(), o.getType()));
                            }
                        }
                        count++;
                    }

                    set(entry1.getKey(),allvalues,values);
                }
                result.getAttributes().put(entity.getId(),allvalues);
            }
        }

        return result;
    }


    private static Object[] convertBeanToValue(Object v, Map<String, String> context, Namespace ns, ProvFactory pf) {

        if (v instanceof Integer) return new Object[] {v , pf.getName().XSD_INT};
        if (v instanceof Float)   return new Object[] {v , pf.getName().XSD_FLOAT};
        if (v instanceof Boolean)   return new Object[] {v , pf.getName().XSD_BOOLEAN};
        if (v instanceof QualifiedName) return new Object[] {v , pf.getName().PROV_QUALIFIED_NAME};
        if (v instanceof String) {
            String s=(String)v;
            if (s.startsWith("{")) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    v=mapper.readValue((String)v,Hashtable.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return new Object[] {v , pf.getName().XSD_STRING};
            }

        }
        if (v instanceof Map) {
            Map<String,String> table=(Map<String,String>)v;
            String lang =table.get("@language");
            String value=table.get("@value");
            String type =table.get("@type");
            String id   =table.get("@id");
            if (lang!=null) {
                return new Object[]{pf.newInternationalizedString(value,lang),pf.getName().XSD_STRING};
            }
            if (id!=null) {
                return new Object[]{ns.stringToQualifiedName(id,pf),pf.getName().PROV_QUALIFIED_NAME};
            }
            if (type!=null) {
                String [] strings=type.split(":");
                String prefix="";
                String local=type;
                if (strings.length>1) {
                    prefix=strings[0];
                    local=strings[1];
                }
                String namespace="http://foo/";
                namespace=context.getOrDefault(prefix, namespace);
                if ("xsd".equals(prefix)) {
                    namespace=NamespacePrefixMapper.XSD_NS;
                }
                return new Object[] {value , pf.newQualifiedName(namespace,local,prefix)};
            }
            System.out.println("===> ?? " + table); //new RuntimeException().printStackTrace();
            return new Object[] {value , pf.newQualifiedName("xal","xbl","cxl")};  //TODO
        }
        throw new UnsupportedOperationException("bean is " + v.getClass());
    }

    public static BindingsBean toBean(OldBindings bindings) {
        BindingsBean bean=new BindingsBean();
        bean.var= new HashMap<>();
        bean.vargen= new HashMap<>();
        bean.context= new HashMap<>();


        for (Map.Entry<QualifiedName, List<QualifiedName>> entry:bindings.getVariables().entrySet()) {
            List<Object> l1= new LinkedList<>();
            for (QualifiedName qn: entry.getValue()) {
                l1.add(convertValueToBean(qn, null, bean.context));
                if (entry.getKey().getNamespaceURI().startsWith(InstantiateUtil.VARGEN_NS)) {
                    bean.vargen.put(entry.getKey().getLocalPart(), l1);
                } else {
                    bean.var.put(entry.getKey().getLocalPart(), l1);
                }
            }
        }

        for (Map.Entry<QualifiedName,List<List<TypedValue>>> entry: bindings.getAttributes().entrySet()) {
            List<Object> l1= new LinkedList<>();

            for	 (List<TypedValue> ll: entry.getValue()) {
                if (ll.size()==1) {
                    l1.add(convertValueToBean(ll.get(0).getValue(), ll.get(0).getType(), bean.context));
                } else {
                    List<Object> l2= new LinkedList<>();
                    for (TypedValue tv:ll) {
                        l2.add(convertValueToBean(tv.getValue(), tv.getType(), bean.context));
                    }
                    l1.add(l2);
                }
            }
            if (entry.getKey().getNamespaceURI().startsWith(InstantiateUtil.VARGEN_NS)) {
                bean.vargen.put(entry.getKey().getLocalPart(),l1);
            } else {
                bean.var.put(entry.getKey().getLocalPart(),l1);
            }

        }
        return bean;
    }

    static Object convertValueToBean(Object o, QualifiedName type, Map<String, String> context) {
        if (o instanceof Integer) return o;
        if (o instanceof Float) return o;
        if (o instanceof Boolean) return o;
        if (o instanceof QualifiedName) {
            QualifiedName qn=(QualifiedName)o;
            Map<String,String> table= new HashMap<>();
            table.put("@id",qn.getPrefix() + ":" + qn.getLocalPart());
            context.put(qn.getPrefix(),qn.getNamespaceURI());
            return table;
        }
        if (o instanceof LangString) {
            LangString qn=(LangString)o;
            Map<String,String> table= new HashMap<>();
            if (qn.getLang()!=null)	table.put("@language",qn.getLang());
            table.put("@value",qn.getValue());
            return table;
        }
        if (o instanceof String)  {
            if ((type==null) || (type.getUri().equals(NamespacePrefixMapper.XSD_NS+"string"))) {
                return o;
            } else {
                Map<String,String> table= new HashMap<>();
                table.put("@type",type.getPrefix()+":"+type.getLocalPart());
                table.put("@value",o.toString());
                return table;
            }
        }
        throw new UnsupportedOperationException("type is " + o);
    }
    public static BindingsBean importBean(InputStream input_file1) {

        try {
            return mapper.readValue(input_file1,BindingsBean.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON serialization failed", e);
        }
    }



}