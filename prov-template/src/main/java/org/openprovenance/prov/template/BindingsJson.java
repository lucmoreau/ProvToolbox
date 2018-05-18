package org.openprovenance.prov.template;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.LangString;
import org.openprovenance.prov.model.NamespacePrefixMapper;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.model.exception.UncheckedException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BindingsJson {
	
	// public static ProvFactory pf=new org.openprovenance.prov.scala.immutable.ProvFactory();

	public static class BindingsBean {
		public Hashtable<String, List<Object>> var;
		public Hashtable<String, List<Object>> vargen;
		public Hashtable<String, String> context;
	    public String template;
	}
	
	public static Object convertValueToBean(Object o, QualifiedName type, Hashtable<String, String> context) {
		if (o instanceof Integer) return o;
		if (o instanceof Float) return o;
		if (o instanceof Boolean) return o;
		if (o instanceof QualifiedName) {
			QualifiedName qn=(QualifiedName)o;
			Hashtable<String,String> table=new Hashtable<String,String>();
			table.put("@id",qn.getPrefix() + ":" + qn.getLocalPart());
			context.put(qn.getPrefix(),qn.getNamespaceURI());
			return table;
		}		
		if (o instanceof LangString) {
			LangString qn=(LangString)o;
			Hashtable<String,String> table=new Hashtable<String,String>();
			table.put("@language",qn.getLang());
			table.put("@value",qn.getValue());
			return table;
		}
		if (o instanceof String)  {
			if ((type==null) || (type.getUri().equals(NamespacePrefixMapper.XSD_NS+"string"))) {
				return o;
			} else {
				Hashtable<String,String> table=new Hashtable<String,String>();
				table.put("@type",type.toString());
				table.put("@value",o.toString());
				return table;
			}
		}
		throw new UnsupportedOperationException("type is " + o);
	}
	
	public static Object[] convertBeanToValue(Object v, Hashtable<String, String> context, Namespace ns, ProvFactory pf) {
	   
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
        if (v instanceof HashMap) {
            HashMap<String,String> table=(HashMap<String,String>)v;
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
            return new Object[] {value , pf.newQualifiedName("xa","xb","cx")};  //TODO
        }
        throw new UnsupportedOperationException("bean is " + v.getClass());
	}

	
	public static BindingsBean toBean(Bindings bindings) {
		BindingsBean bean=new BindingsBean();
		bean.var=new Hashtable<String,List<Object>>();
		bean.vargen=new Hashtable<String,List<Object>>();
		bean.context=new Hashtable<String,String>();
		
		for (Entry<QualifiedName,List<List<TypedValue>>> entry: bindings.getAttributes().entrySet()) {
			List<Object> l1=new LinkedList<Object>();
			
			for	 (List<TypedValue> ll: entry.getValue()) {
				if (ll.size()==1) {
					l1.add(convertValueToBean(ll.get(0).getValue(), ll.get(0).getType(), bean.context));
				} else {
					List<Object> l2=new LinkedList<Object>();
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
	
	public static Bindings fromBean(BindingsBean bean, ProvFactory pf) {
        Bindings result=new Bindings(pf);
        Namespace ns=new Namespace(bean.context);

        for (Entry<String,List<Object>> bindings: bean.var.entrySet()) {
            String var=bindings.getKey();
            int i=0;
            List<List<TypedValue>> allvalues= new LinkedList<List<TypedValue>>();
          
            boolean single_value=true;

            for (Object value: bindings.getValue()) {

                List<Object> wrapped;
                if (!(value instanceof List)) {
                    wrapped=new LinkedList<Object>();
                    wrapped.add(value);
                } else {
                    wrapped=(List<Object>) value;
                    single_value=false;

                }
                int count=0;
                
 
                List<TypedValue> values= new LinkedList<TypedValue>();

                for (Object o: wrapped) {
                    Object[] conversion=convertBeanToValue(o,bean.context, ns, pf);
                    Object theValue=conversion[0];
                    QualifiedName type=(QualifiedName)conversion[1];
                    if (!(type.equals(pf.getName().PROV_QUALIFIED_NAME))) single_value=false; // interested only in qualified names
                    values.add(pf.newAttribute(ExpandUtil.TMPL_NS, Bindings.VALUE2+i+"_"+count, ExpandUtil.TMPL_PREFIX, theValue, type));
                    count++;
                }
                
                //System.out.println("Adding values " + values + " to allvalues " + allvalues);
                allvalues.add(values);
                
                i++;
            }

            QualifiedName myvar=pf.newQualifiedName(ExpandUtil.VAR_NS,var,"var");
            
            result.getAttributes().put(myvar,allvalues);

            
            if (single_value) {
                List<QualifiedName> ll=new LinkedList<QualifiedName>();
                for (Object o: bindings.getValue()) {
                    Object[] conversion=convertBeanToValue(o,bean.context, ns, pf);
                    Object theValue=conversion[0];
                    Object type=conversion[1];
                    //System.out.println("Found " + theValue + " " + type);
                    ll.add((QualifiedName) theValue);
                }
                result.getVariables().put(myvar,ll);
            }
            

        }
        

	    return result;
	}



	public static void exportBean(String output_file1,
			                      BindingsBean bean,
			                      boolean pretty) {

		ObjectMapper mapper = new ObjectMapper();
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

        ObjectMapper mapper = new ObjectMapper();
        try {
            BindingsBean bean=mapper.readValue(input_file1,BindingsBean.class);
            return bean;
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON serialization failed", e);
        }
    }
    
    public static BindingsBean importBean(InputStream input_file1) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            BindingsBean bean=mapper.readValue(input_file1,BindingsBean.class);
            return bean;
            
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedException("JSON serialization failed", e);
        }
    }
    



}
