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
import org.openprovenance.prov.template.json.*;

public class BindingsJson {
    static final ObjectMapper mapper = new ObjectMapper();


	public static Object convertValueToBean(Object o, QualifiedName type, Map<String, String> context) {
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

        public static Object[] convertBeanToValue(Object v, Map<String, String> context, Namespace ns, ProvFactory pf) {
	   
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


        for (Entry<QualifiedName, List<QualifiedName>> entry:bindings.getVariables().entrySet()) {
            List<Object> l1= new LinkedList<>();
            for (QualifiedName qn: entry.getValue()) {
                l1.add(convertValueToBean(qn, null, bean.context));
                if (entry.getKey().getNamespaceURI().startsWith(ExpandUtil.VARGEN_NS)) {
                    bean.vargen.put(entry.getKey().getLocalPart(), l1);
                } else {
                    bean.var.put(entry.getKey().getLocalPart(), l1);
                }
            }
        }
		
		for (Entry<QualifiedName,List<List<TypedValue>>> entry: bindings.getAttributes().entrySet()) {
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
			if (entry.getKey().getNamespaceURI().startsWith(ExpandUtil.VARGEN_NS)) {
				bean.vargen.put(entry.getKey().getLocalPart(),l1);
			} else {
				bean.var.put(entry.getKey().getLocalPart(),l1);
			}
			
		}
		return bean;
	}
	
	public static OldBindings fromBean(BindingsBean bean, ProvFactory pf) {
        OldBindings result=new OldBindings(pf);
        Namespace ns=new Namespace(bean.context);

        for (Entry<String,List<Object>> bindings: bean.var.entrySet()) {
            String var=bindings.getKey();
            int i=0;
            List<List<TypedValue>> allvalues= new LinkedList<>();
          
            boolean single_value=true;

            for (Object value: bindings.getValue()) {

                List<Object> wrapped;
                if (!(value instanceof List)) {
                    wrapped= new LinkedList<>();
                    wrapped.add(value);
                } else {
                    wrapped=(List<Object>) value;
                    single_value=false;

                }
                int count=0;
                
 
                List<TypedValue> values= new LinkedList<>();

                for (Object o: wrapped) {
                    Object[] conversion=convertBeanToValue(o,bean.context, ns, pf);
                    Object theValue=conversion[0];
                    QualifiedName type=(QualifiedName)conversion[1];
                    if (!(type.equals(pf.getName().PROV_QUALIFIED_NAME))) single_value=false; // interested only in qualified names
                    values.add(pf.newAttribute(ExpandUtil.TMPL_NS, OldBindings.VALUE2+i+"_"+count, ExpandUtil.TMPL_PREFIX, theValue, type));
                    count++;
                }
                
                allvalues.add(values);
                
                i++;
            }

            QualifiedName myvar=pf.newQualifiedName(ExpandUtil.VAR_NS,var,"var");
            
            result.getAttributes().put(myvar,allvalues);

            if (single_value) {
                List<QualifiedName> ll= new LinkedList<>();
                for (Object o: bindings.getValue()) {
                    Object[] conversion=convertBeanToValue(o,bean.context, ns, pf);
                    Object theValue=conversion[0];
                    ll.add((QualifiedName) theValue);
                }
                result.getVariables().put(myvar,ll);
            }
            

        }
        

	    return result;
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
                    values.add(pf.newAttribute(ExpandUtil.TMPL_NS, OldBindings.VALUE2+i+"_"+count, ExpandUtil.TMPL_PREFIX, theValue, type));
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
    
    public static BindingsBean importBean(InputStream input_file1) {

        try {
            return mapper.readValue(input_file1,BindingsBean.class);
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON serialization failed", e);
        }
    }

    private static BindingsBean importBean(JsonNode json) {

        try {
            return mapper.treeToValue(json,BindingsBean.class);
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON conversion to bean failed", e);
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

    public static OldBindings getOldBindingsFromSchema(JsonNode bindings_schema, ProvFactory provFactory) {
        return BindingsJson.fromBean(BindingsJson.importBean(bindings_schema), provFactory);
    }

    public static OldBindings getBindingsFromSchema(JsonNode bindings_schema, ProvFactory provFactory) {
        return BindingsJson.fromBean(BindingsJson.importBindings(bindings_schema), provFactory);
    }

}
