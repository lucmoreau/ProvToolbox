package org.openprovenance.prov.template.expander.deprecated;

import java.util.*;

import static org.openprovenance.prov.template.expander.InstantiateUtil.TMPL_NS;
import static org.openprovenance.prov.template.expander.InstantiateUtil.TMPL_PREFIX;

import java.util.Map.Entry;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.InstantiateUtil;
import org.openprovenance.prov.template.json.*;

@Deprecated
public class OldBindings {
    
    public static final String VALUE = "value_";
    public static final String VALUE2 = "2dvalue_";
    public static final String APP_VALUE = TMPL_NS+VALUE;
    public static final String VALUE_v2 = "binding_";
    public static final String APP_VALUE_v2 = TMPL_NS+VALUE_v2;
    public static final String APP_VALUE2 = TMPL_NS+VALUE2;
    
    final private Map<QualifiedName, List<QualifiedName>> variables;
    final private Map<QualifiedName, List<List<TypedValue>>> attributes;

    final private ProvFactory pf;
    final private Name name;
    static ProvUtilities u= new ProvUtilities();

    public OldBindings(ProvFactory pf) {
        this(new HashMap<>(), new HashMap<>(), pf);
    }

    public OldBindings(HashMap<QualifiedName, List<QualifiedName>> variables,
                       HashMap<QualifiedName, List<List<TypedValue>>> attributes,
                       ProvFactory pf) {
        this.variables=variables;
        this.attributes=attributes;
        this.pf=pf;
        this.name=pf.getName();
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

    enum VAR_KIND { IS_VAR, IS_VARGEN };

    public static OldBindings fromBean(Bindings bean, ProvFactory pf) {
        OldBindings result=new OldBindings(pf);
        Namespace ns=new Namespace(bean.context);

        if (bean.var!=null) fromBeanForVariables(bean, pf, bean.var, ns, result, VAR_KIND.IS_VAR);
        if (bean.vargen!=null) fromBeanForVariables(bean, pf, bean.vargen, ns, result, VAR_KIND.IS_VARGEN);


        return result;
    }

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
                    Object[] conversion= convertBeanToValue(o, bean.context, ns, pf);
                    Object theValue=conversion[0];
                    QualifiedName type=(QualifiedName)conversion[1];
                    if (!(type.equals(pf.getName().PROV_QUALIFIED_NAME))) single_value=false; // interested only in qualified names
                    values.add(pf.newAttribute(TMPL_NS, VALUE2 + i + "_" + count, TMPL_PREFIX, theValue, type));
                    count++;
                }

                allvalues.add(values);

                i++;
            }

            QualifiedName myvar;
            if (varKind.equals(VAR_KIND.IS_VAR)) {
                 myvar = pf.newQualifiedName(InstantiateUtil.VAR_NS, key, "var");
            } else {
                 myvar = pf.newQualifiedName(InstantiateUtil.VARGEN_NS, key, "vargen");
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

    @Override
    public boolean equals (Object o) {
        if (!(o instanceof OldBindings)) return false;
        OldBindings b=(OldBindings)o;
        return b.variables.equals(this.variables)
                && b.attributes.equals(this.attributes);

    }


    public Map<QualifiedName, List<QualifiedName>> getVariables() {
        return variables;
    }



    public Map<QualifiedName, List<List<TypedValue>>> getAttributes() {
        return attributes;
    }


    public void addVariable(QualifiedName name, QualifiedName val) {
        variables.computeIfAbsent(name, k -> new LinkedList<>());
        variables.get(name).add(val);
    }
    
    public void addVariable(String name, QualifiedName val) {
        addVariable(b_var(name),val);
    }


    public void addAttribute(QualifiedName name, List<TypedValue> values) {
        attributes.computeIfAbsent(name, k -> new LinkedList<>());
        attributes.get(name).add(values);
    }    
    public void addAttribute(String name, QualifiedName value) {
        addAttribute(b_var(name),a_val(value));
    }    
    
    public void addAttribute(String name, String value) {
        addAttribute(b_var(name),a_val(value));
    }
    
    /* binding variable */
    public QualifiedName b_var(String name) {
        return pf.newQualifiedName(InstantiateUtil.VAR_NS, name, InstantiateUtil.VAR_PREFIX);
    }
    
    /* Attribute value */
    public List<TypedValue> a_val(String s) {
        List<TypedValue> ll= new LinkedList<>();
        ll.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.XSD_STRING));
        return ll;
    }
    
    public List<TypedValue> a_val(QualifiedName s) {
        List<TypedValue> ll= new LinkedList<>();
        ll.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.PROV_QUALIFIED_NAME));
        return ll;
    }
    
    

    @Override
    public String toString () {
	return "[" + getVariables() + " -- " + getAttributes() + "]";
    }



    @Deprecated
    public Document toDocument_v1() {
        List<Statement> ll= new LinkedList<>();
        add1DValues_v1(ll,variables);
        add2Dvalues_v1(ll,attributes);
        Document dummy=pf.newDocument(null,ll, new LinkedList<>());
        return pf.newDocument(pf.newNamespace(Namespace.gatherNamespaces(dummy)),ll, new LinkedList<>());
    }
    
    public Document toDocument_v2 () {
        List<Statement> ll= new LinkedList<>();
        add1DValues_v2(ll,variables);  
        add2Dvalues_v2(ll,attributes);
        Document dummy=pf.newDocument(null,ll, new LinkedList<>());
        return pf.newDocument(pf.newNamespace(Namespace.gatherNamespaces(dummy)),ll, new LinkedList<>());
    }

    public void add2Dvalues_v1(List<Statement> ll, Map<QualifiedName, List<List<TypedValue>>> attributes) {
        for (Entry<QualifiedName, List<List<TypedValue>>> entry: attributes.entrySet()) {
            int count1=0;
            List<Attribute> attrs= new LinkedList<>();
            for (List<TypedValue> vals: entry.getValue()) {
                int count2=0;
                for (TypedValue val: vals) {
                    attrs.add(pf.newAttribute(TMPL_NS, VALUE2+count1+"_"+count2, TMPL_PREFIX, val.getValue(), val.getType()));
                    count2++;
                }
                count1++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);
            ll.add(e);       
        }
    }
    
    public void add2Dvalues_v2(List<Statement> ll, Map<QualifiedName, List<List<TypedValue>>> attributes) {
        for (Entry<QualifiedName, List<List<TypedValue>>> entry: attributes.entrySet()) {
            int count1=0;
            List<Attribute> attrs= new LinkedList<>();
            for (List<TypedValue> vals: entry.getValue()) {
                //int count2=0;
                for (TypedValue val: vals) {
                    attrs.add(pf.newAttribute(TMPL_NS, VALUE_v2+count1, TMPL_PREFIX, val.getValue(), val.getType()));
                    //count2++;
                }
                count1++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);
            ll.add(e);       
        }
    }


    public void add1DValues_v1(List<Statement> ll, Map<QualifiedName, List<QualifiedName>> variables) {
        add1DValues(ll, variables, VALUE);
    }
    public void add1DValues_v2(List<Statement> ll, Map<QualifiedName, List<QualifiedName>> variables) {
        add1DValues(ll, variables, VALUE_v2);
    }

    private void add1DValues(List<Statement> ll, Map<QualifiedName, List<QualifiedName>> variables, String valuev2) {
        for (Entry<QualifiedName, List<QualifiedName>> entry: variables.entrySet()) {
            int count=0;
            List<Attribute> attrs= new LinkedList<>();
            for (QualifiedName qn: entry.getValue()) {
                attrs.add(pf.newAttribute(TMPL_NS, valuev2 +count, TMPL_PREFIX, qn, pf.getName().PROV_QUALIFIED_NAME));
                count++;
            }
            Entity e=pf.newEntity(entry.getKey(),attrs);
            ll.add(e);
        }
    }


    public static OldBindings fromDocument_v1(Document doc, ProvFactory pf) {
        OldBindings result=new OldBindings(pf);
        
        List<Entity> entities=u.getEntity(doc);
        for (Entity entity: entities) {
            Map<Integer,QualifiedName> map= new HashMap<>();
            Map<Integer, Map<Integer,TypedValue>> map2= new HashMap<>();
            for (Other attr: entity.getOther()) {
                String uri = attr.getElementName().getUri();
                if (uri.startsWith(APP_VALUE)) {
                    Integer i=Integer.valueOf(uri.substring(APP_VALUE.length()));
                    if (attr.getValue() instanceof QualifiedName) {
                        map.put(i, (QualifiedName) attr.getValue());
                    }
                } else {
                    if (uri.startsWith(APP_VALUE2)) {
                        String index=uri.substring(APP_VALUE2.length());
                        String [] nums=index.split("_");
                        Integer i=Integer.valueOf(nums[0]);
                        Integer j=Integer.valueOf(nums[1]);
                        map2.computeIfAbsent(i, k -> new HashMap<>());
                        map2.get(i).put(j,attr);
                    }
                }
            }
            List<QualifiedName> ll= new ArrayList<>();
            int size=map.entrySet().size();
            if (size>0) {
                for (Entry<Integer, QualifiedName> entry: map.entrySet()) {
                    set(entry.getKey(),ll,entry.getValue());
                }
                result.getVariables().put(entity.getId(),ll);
            } 
            
            int size2=map2.entrySet().size();
            if (size2>0) {
                List<List<TypedValue>> allvalues= new LinkedList<>();
                for (Entry<Integer, Map<Integer, TypedValue>> entry1: map2.entrySet()) {
                    List<TypedValue> values= new LinkedList<>();
                    for (Entry<Integer, TypedValue> entry2: entry1.getValue().entrySet()) {
                        set(entry2.getKey(),values,entry2.getValue());
                    }
                    set(entry1.getKey(),allvalues,values);
                }
                result.getAttributes().put(entity.getId(),allvalues);
            }
        }
        return result;
    }
    

    public void addVariableBindingsAsAttributeBindings() {
    	Map<QualifiedName, List<QualifiedName>> vb=getVariables();
    	for(Entry<QualifiedName,List<QualifiedName>> entry: vb.entrySet()) {
    		int count=0;
    		for (QualifiedName qn: entry.getValue()) {
    			List<TypedValue> ll= new LinkedList<>();
    			ll.add(pf.newAttribute(TMPL_NS, VALUE2+count+"_"+0, TMPL_PREFIX, qn, pf.getName().PROV_QUALIFIED_NAME));
    			count++;
    			addAttribute(entry.getKey(),ll);
    		}
    	}
    	
    }

    static public <E> void set(int pos, List<E> ll, E val) {
        int size=ll.size();
        for (int i=size; i<=pos; i++) {
            ll.add(null);
        }
        ll.set(pos,val);
    }
    
    public void exportToJson(String filename) {
        BindingsBean bb= Conversions.toBean(this);
        Conversions.exportBean(filename,bb,true);
    }

       /*
    public static OldBindings getBindingsFromSchema(JsonNode bindings_schema, ProvFactory provFactory) {
        return BindingsJson.fromBean(BindingsJson.importBindings(bindings_schema), provFactory);
    }
     */
	
}
