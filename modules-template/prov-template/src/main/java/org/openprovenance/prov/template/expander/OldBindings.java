package org.openprovenance.prov.template.expander;

import java.util.*;

import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_NS;
import static org.openprovenance.prov.template.expander.ExpandUtil.TMPL_PREFIX;

import java.util.Map.Entry;

import org.openprovenance.prov.model.Attribute;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Name;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.Other;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.TypedValue;

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
        return pf.newQualifiedName(ExpandUtil.VAR_NS, name, ExpandUtil.VAR_PREFIX);
    }
    
    /** Attribute value */
    public List<TypedValue> a_val(String s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
        ll.add(pf.newAttribute(pf.newQualifiedName(TMPL_NS, "ignore", "app"), s, name.XSD_STRING));
        return ll;
    }
    
    public List<TypedValue> a_val(QualifiedName s) {
        List<TypedValue> ll=new LinkedList<TypedValue>();
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
                for (Entry<Integer, QualifiedName> entry: map.entrySet()) {
                    set(entry.getKey(),ll,entry.getValue());
                }
                result.getVariables().put(entity.getId(),ll);
            } 
            
            int size2=map_v2.entrySet().size();
            if (size2>0) {
                List<List<TypedValue>> allvalues= new LinkedList<>();

                for (Entry<Integer, Set<TypedValue>> entry1: map_v2.entrySet()) {
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
        BindingsBean bb=BindingsJson.toBean(this);
        BindingsJson.exportBean(filename,bb,true);
    }
	
}
