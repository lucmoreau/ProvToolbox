package org.openprovenance.prov.template.expander;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Bundle;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;
import org.openprovenance.prov.model.TypedValue;
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.template.expander.Using.UsingIterator;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.template.json.*;

public class Expand {
    static Logger logger = LogManager.getLogger(Expand.class);


    final private boolean addOrderp;
    final private boolean allUpdatedRequired;
    private final boolean preserveUnboundVariables;

    boolean displayUnusedVariables=true;
    boolean displayUnusedBindings=true;

    public Expand(ProvFactory pf, boolean addOrderp, boolean allUpdatedRequired) {
        this.pf = pf;
        this.addOrderp = addOrderp;
        this.allUpdatedRequired = allUpdatedRequired;
        this.preserveUnboundVariables=false;
        this.displayUnusedVariables=true;
    }
    public Expand(ProvFactory pf, boolean addOrderp, boolean allUpdatedRequired, boolean preserveUnboundVariables, boolean displayUnusedVariables, boolean displayUnusedBindings) {
        this.pf = pf;
        this.addOrderp = addOrderp;
        this.allUpdatedRequired = allUpdatedRequired;
        this.preserveUnboundVariables=preserveUnboundVariables;
        this.displayUnusedVariables=displayUnusedVariables;
        this.displayUnusedBindings=displayUnusedBindings;
    }
    public Expand(ProvFactory pf) {
        this(pf, false, false);
    }

    public Document expander(Document docIn, Bindings bindings) {
        return expander(docIn, bindings, null, null);
    }

    public Document expander(Document docIn, Bindings bindings, String bindingsFilename, String templateFilename) {

        
        Bundle bun;
        try {
            bun = u.getBundle(docIn).get(0);
        } catch (RuntimeException e) {
            throw new UncheckedException("Bundle missing in template", e);
        }

        Groupings grp1 = Groupings.fromDocument(docIn, bindings, pf);

        Bundle bun1 = (Bundle) expand(bun, bindings, grp1, bindingsFilename, templateFilename).get(0);
        Document doc1 = pf.newDocument();
        doc1.getStatementOrBundle().add(bun1);


        bun1.setNamespace(Namespace.gatherNamespaces(bun1));

        doc1.setNamespace(new Namespace());

        return doc1;
    }



    private final ProvFactory pf;

    static ProvUtilities u = new ProvUtilities();

    public List<StatementOrBundle> expand(Statement statement, OldBindings oldBindings, Bindings bindings, Groupings grp1) {
        Using us1 = ExpandUtil.usedGroups(statement, grp1, oldBindings);
        return expand(statement, oldBindings, bindings, grp1, us1);
    }
    
    boolean allExpanded=true;
    public boolean getAllExpanded() {
    	return allExpanded;
    }



    public List<StatementOrBundle> expand(Bundle bun, Bindings bindings, Groupings grp1, String bindingsFilename, String templateFilename) {

        OldBindings legacyBindings=BindingsJson.fromBean(bindings,pf);
        // TODO: make ExpandAction to take Bindings directly


        Map<QualifiedName, QualifiedName> oldEnv = new HashMap<>();
        Map<QualifiedName, List<TypedValue>> oldEnv2 = new HashMap<>();

        Map<QualifiedName, QDescriptor> env= new HashMap<>();
        Map<QualifiedName, SingleDescriptors> env2= new HashMap<>();


        ExpandAction action = new ExpandAction(pf,
                                               u,
                                               this,
                                               oldEnv,
                                               oldEnv2,
                                               env,
                                               env2,
                                         null,
                                               legacyBindings,
                                               bindings,
                                               grp1,
                                               addOrderp,
                                               allUpdatedRequired,
                                               preserveUnboundVariables);
        u.doAction(bun, action);
        allExpanded=allExpanded && action.getAllExpanded();
        Set<String> freeVars=Arrays.stream(grp1.getFreeVariables()).map(QualifiedName::getLocalPart).collect(java.util.stream.Collectors.toSet());
        warnAboutUnboundTemplateVariables(bindings, templateFilename, bindingsFilename, freeVars);
        warnAboutUnusedBindingsVariables(bindings, templateFilename, bindingsFilename, freeVars);
        return action.getList();
    }

    private void warnAboutUnusedBindingsVariables(Bindings bindings, String templateFilename, String bindingsFilename, Set<String> freeVariables) {
        if (!displayUnusedBindings) return;
        Set<String> allBindingsVars= bindings.var.keySet();
        allBindingsVars.removeAll(freeVariables);
        if (!allBindingsVars.isEmpty()) {
            List<String> sorted=new LinkedList<>(allBindingsVars);
            Collections.sort(sorted);
            System.out.println("Unknown bindings variables: " + sorted + ((bindingsFilename ==null)?"": ": in bindings file " + filenameSuffix(bindingsFilename) + ((templateFilename ==null)?"": " (template file: " + filenameSuffix(templateFilename) + ")")));
        }
    }

    private void warnAboutUnboundTemplateVariables(Bindings bindings, String templateFilename, String bindingsFilename, Set<String> freeVariables) {
        if (!displayUnusedVariables) return;
        List<String> unboundVariables=new LinkedList<>();
        Set<String> allBindingsVars=bindings.var.keySet();
        for (String var: freeVariables) {
            if (!allBindingsVars.contains(var)) {
                unboundVariables.add(var);
            }
        }
        if (!unboundVariables.isEmpty()) {
            // sort unboundVariables
            Collections.sort(unboundVariables);
            System.out.println("Unbound template variables: " + unboundVariables + ((templateFilename ==null)?"": " in template " + filenameSuffix(templateFilename) + ((bindingsFilename ==null)?"": " (bindings file: " + filenameSuffix(bindingsFilename) + ")")));
        }
    }

    private String filenameSuffix(String path) {
        if (path == null) return "";
        int idx = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        if (idx >= 0 && idx < path.length() - 1) return path.substring(idx + 1);
        return path;
    }

    public List<StatementOrBundle> expand(Statement statement,
                                          OldBindings oldBindings,
                                          Bindings bindings,
                                          Groupings grp1,
                                          Using us1) {
        List<StatementOrBundle> results = new LinkedList<>();
        Iterator<List<Integer>> iter = us1.iterator();

        Set<QualifiedName> freeAttributeVariables = ExpandUtil.freeAttributeVariables(statement, pf);

        while (iter.hasNext()) {
            List<Integer> index = iter.next();
            // System.out.println(" Index " + index);

            Map<QualifiedName, QualifiedName> oldEnv = us1.get(oldBindings, grp1, index);
        // testing replacement of oldBindings by new bindings
            Map<QualifiedName, QDescriptor> env=us1.newGet(bindings, grp1, index);

            assert env.size()==oldEnv.size();


            Map<QualifiedName, List<TypedValue>> oldEnv2;

            oldEnv2 = us1.getAttr(freeAttributeVariables,
                               oldBindings,
                               (UsingIterator) iter);
            Map<QualifiedName, SingleDescriptors> env2=us1.newGetAttr(freeAttributeVariables,
                    bindings,
                    (UsingIterator) iter);
            if (env2.size()!=oldEnv2.size()) {
                System.out.println("Mismatch in attribute environment sizes: env2.size()=" + env2.size() + " vs oldEnv2.size()=" + oldEnv2.size());
                System.out.println("testEnv2=" + env2);
                System.out.println("env2=" + oldEnv2);
            }
            assert env2.size()==oldEnv2.size();
            assert compareTests(env2,oldEnv2);


            ExpandAction action = new ExpandAction(pf,
                                                   u,
                                                   this,
                                                   oldEnv,
                                                   oldEnv2,
                                                   env,
                                                   env2,
                                                   index,
                                                   oldBindings,
                                                   bindings,
                                                   grp1,
                                                   addOrderp,
                                                   allUpdatedRequired,
                                                   preserveUnboundVariables);
            u.doAction(statement, action);
            allExpanded=allExpanded && action.getAllExpanded();

            results.addAll(action.getList());

        }
        return results;

    }

    private boolean compareTests(Map<QualifiedName, SingleDescriptors> testEnv2, Map<QualifiedName, List<TypedValue>> env2) {
        for (QualifiedName key: testEnv2.keySet()) {
            SingleDescriptors sd=testEnv2.get(key);
            List<TypedValue> tvs=env2.get(key);
            if (sd.values.size()!=tvs.size()) {
                System.out.println("Mismatch in sizes for key " + key + ": sd.values.size()=" + sd.values.size() + " vs tvs.size()=" + tvs.size());
                System.out.println("sd.values=" + sd.values);
                System.out.println("tvs=" + tvs);
                return false;
            }
            for (int i=0;i<sd.values.size();i++) {
                SingleDescriptor sdi=sd.values.get(i);
                TypedValue tvi=tvs.get(i);
                if (sdi instanceof QDescriptor) {
                    QDescriptor qdi=(QDescriptor)sdi;
                    if (tvi.getValue() instanceof QualifiedName) {
                        QualifiedName qn=(QualifiedName)tvi.getValue();
                        if (!qdi.id.equals(qn.getPrefix()+":"+qn.getLocalPart())) {
                            System.out.println("Mismatch in id values for key " + key + " at index " + i + ": qdi.id=" + qdi.id + " vs qn.getLocalPart()=" + qn.getLocalPart());
                            System.out.println("sdi=" + sdi);
                            System.out.println("tvi=" + tvi);
                            return false;
                        }
                        continue;
                    } else {
                        System.out.println("Expected QualifiedName value for key " + key + " at index " + i + ": tvi.getValue()=" + tvi.getValue());
                        System.out.println("sdi=" + sdi);
                        System.out.println("tvi=" + tvi);
                        return false;
                    }
                } else if (sdi instanceof VDescriptor) {
                    Object value = tvi.getValue();
                    if ( (value instanceof String) | (value instanceof org.openprovenance.prov.vanilla.LangString) ) {
                        String str=(value instanceof String)? (String)value : ((org.openprovenance.prov.vanilla.LangString)value).getValue();
                        VDescriptor vdi=(VDescriptor)sdi;
                        if (!vdi.value.equals(str)) {
                            System.out.println("Mismatch in value for key " + key + " at index " + i + ": vdi.value=" + vdi.value + " vs str=" + str);
                            System.out.println("sdi=" + sdi);
                            System.out.println("tvi=" + tvi);
                            return false;
                        }
                        continue;
                    } else {
                        System.out.println("Expected String value for key " + key + " at index " + i + ": tvi.getValue()=" + value);
                        System.out.println("sdi=" + sdi);
                        System.out.println("tvi=" + tvi);
                        return false;
                    }
                } else {
                    System.out.println("Unexpected descriptor type for key " + key + " at index " + i + ": sdi.getClass()=" + sdi.getClass());
                    System.out.println("sdi=" + sdi);
                    System.out.println("tvi=" + tvi);
                    return false;
                }
            }
        }
        return true;
    }


}
