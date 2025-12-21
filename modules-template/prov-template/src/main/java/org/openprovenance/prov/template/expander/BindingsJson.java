package org.openprovenance.prov.template.expander;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.model.exception.UncheckedException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openprovenance.prov.template.expander.deprecated.BindingsBean;
import org.openprovenance.prov.template.json.*;

public class BindingsJson {
    static final ObjectMapper mapper = new ObjectMapper();



    public static Object[] convertBeanToValue(Descriptor v, Map<String, String> context, Namespace ns, ProvFactory pf) {
        if (v instanceof QDescriptor) {
            QDescriptor qd=(QDescriptor)v;
            return new Object[] {ns.stringToQualifiedName(qd.id,pf),pf.getName().PROV_QUALIFIED_NAME};
        }
        if (v instanceof VDescriptor) {

            VDescriptor vd=(VDescriptor)v;
            if (vd.language!=null) {
                return new Object[]{pf.newInternationalizedString(vd.value,vd.language),pf.getName().XSD_STRING};
            }
            if (vd.type!=null) {
                String[] strings = vd.type.split(":");
                String prefix = "";
                String local = vd.type;
                if (strings.length > 1) {
                    prefix = strings[0];
                    local = strings[1];
                }
                String namespace = "http://foo/";
                namespace = context.getOrDefault(prefix, namespace);
                if ("xsd".equals(prefix)) {
                    namespace = NamespacePrefixMapper.XSD_NS;
                }
                return new Object[]{vd.value, pf.newQualifiedName(namespace, local, prefix)};
            } else {
                //System.out.println("===> vd " + vd); //new RuntimeException().printStackTrace();
                return new Object[]{vd.value,pf.getName().XSD_STRING};
            }
        }
        else throw new UnsupportedOperationException("bean is " + v.getClass());
    }


    public static OldBindings fromBean(Bindings bean, ProvFactory pf) {
        OldBindings result=new OldBindings(pf);
        Namespace ns=new Namespace(bean.context);

        if (bean.var!=null) fromBeanForVariables(bean, pf, bean.var, ns, result, VAR_KIND.IS_VAR);
        if (bean.vargen!=null) fromBeanForVariables(bean, pf, bean.vargen, ns, result, VAR_KIND.IS_VARGEN);


        return result;
    }

    enum VAR_KIND { IS_VAR, IS_VARGEN };
    private static void fromBeanForVariables(Bindings bean,
                                             ProvFactory pf,
                                             Map<String, Descriptors> var,
                                             Namespace ns,
                                             OldBindings result,
                                             VAR_KIND varKind) {
        for (String key: var.keySet()) {
            int i=0;
            List<List<TypedValue>> allvalues= new LinkedList<>();

            boolean single_value=true;

            Descriptors descriptors = var.get(key);
            if (descriptors==null || descriptors.values==null) {
                //throw new IllegalArgumentException("Missing variable " + key + " in bindings " + var);
                continue;
            };
            for (Descriptor descriptor: descriptors.values) {

                SingleDescriptors wrapped;
                if (descriptor instanceof SingleDescriptor) {
                    wrapped=new SingleDescriptors();
                    wrapped.values=new LinkedList<>();
                    wrapped.values.add((SingleDescriptor)descriptor);
                } else if (descriptor instanceof SingleDescriptors) {
                    wrapped=((SingleDescriptors) descriptor);
                    single_value=false;
                } else {
                    throw new IllegalArgumentException("Unexpected type " + descriptor.getClass());
                }
                int count=0;



                List<TypedValue> values= new LinkedList<>();

                for (SingleDescriptor o: wrapped.values) {
                    //System.out.println("Converting " + o);
                    Object[] conversion=convertBeanToValue(o, bean.context, ns, pf);
                    Object theValue=conversion[0];
                    QualifiedName type=(QualifiedName)conversion[1];
                    if (!(type.equals(pf.getName().PROV_QUALIFIED_NAME))) single_value=false; // interested only in qualified names
                    values.add(pf.newAttribute(ExpandUtil.TMPL_NS, OldBindings.VALUE2 + i + "_" + count, ExpandUtil.TMPL_PREFIX, theValue, type));
                    count++;
                }

                allvalues.add(values);

                i++;
            }

            QualifiedName myvar;
            if (varKind.equals(VAR_KIND.IS_VAR)) {
                 myvar = pf.newQualifiedName(ExpandUtil.VAR_NS, key, "var");
            } else {
                 myvar = pf.newQualifiedName(ExpandUtil.VARGEN_NS, key, "vargen");
            }

            result.getAttributes().put(myvar, allvalues);


            if (single_value) {
                List<QualifiedName> ll = new LinkedList<>();
                for (Descriptor o : descriptors.values) {
                    Object[] conversion = convertBeanToValue(o, bean.context, ns, pf);
                    Object theValue = conversion[0];
                    ll.add((QualifiedName) theValue);
                }
                result.getVariables().put(myvar, ll);
            }



        }


    }



    public static Bindings importBindings(JsonNode json) {

        try {
            return mapper.treeToValue(json,Bindings.class);

        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON conversion to bean failed", e);
        }
    }

    /*
    public static OldBindings getBindingsFromSchema(JsonNode bindings_schema, ProvFactory provFactory) {
        return BindingsJson.fromBean(BindingsJson.importBindings(bindings_schema), provFactory);
    }
     */
}
