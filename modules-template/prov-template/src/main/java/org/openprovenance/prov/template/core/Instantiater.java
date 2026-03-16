package org.openprovenance.prov.template.core;

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
import org.openprovenance.prov.model.exception.UncheckedException;
import org.openprovenance.prov.template.core.Using.UsingIterator;
import org.openprovenance.prov.model.ProvUtilities;
import org.openprovenance.prov.template.json.*;

public class Instantiater {
    static Logger logger = LogManager.getLogger(Instantiater.class);


    final private boolean addOrderp;
    final private boolean allUpdatedRequired;
    private final boolean preserveUnboundVariables;

    boolean displayUnusedVariables=true;
    boolean displayUnusedBindings=true;

    public Instantiater(ProvFactory pf, boolean addOrderp, boolean allUpdatedRequired) {
        this.pf = pf;
        this.addOrderp = addOrderp;
        this.allUpdatedRequired = allUpdatedRequired;
        this.preserveUnboundVariables=false;
        this.displayUnusedVariables=true;
    }
    public Instantiater(ProvFactory pf, boolean addOrderp, boolean allUpdatedRequired, boolean preserveUnboundVariables, boolean displayUnusedVariables, boolean displayUnusedBindings) {
        this.pf = pf;
        this.addOrderp = addOrderp;
        this.allUpdatedRequired = allUpdatedRequired;
        this.preserveUnboundVariables=preserveUnboundVariables;
        this.displayUnusedVariables=displayUnusedVariables;
        this.displayUnusedBindings=displayUnusedBindings;
    }
    public Instantiater(ProvFactory pf) {
        this(pf, false, false);
    }

    public Document instantiate(Document docIn, Bindings bindings) {
        return instantiate(docIn, bindings, null, null);
    }

    public Document instantiate(Document docIn, Bindings bindings, String bindingsFilename, String templateFilename) {

        
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

    /*
    public List<StatementOrBundle> expand(Statement statement,  Bindings bindings, Groupings grp1) {
        Using us1 = ExpandUtil.usedGroups(statement, grp1, bindings);
        return expand(statement,  bindings, grp1, us1);
    }

     */
    
    boolean allExpanded=true;
    public boolean getAllExpanded() {
    	return allExpanded;
    }



    public List<StatementOrBundle> expand(Bundle bun, Bindings bindings, Groupings grp1, String bindingsFilename, String templateFilename) {

        Map<QualifiedName, QDescriptor> env= new HashMap<>();
        Map<QualifiedName, SingleDescriptors> env2= new HashMap<>();

        InstantiateAction action = new InstantiateAction(pf,
                                               u,
                                               this,
                                               env,
                                               env2,
                                         null,
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
                                          Bindings bindings,
                                          Groupings grp1,
                                          Using us1) {
        List<StatementOrBundle> results = new LinkedList<>();
        Iterator<List<Integer>> iter = us1.iterator();

        Set<QualifiedName> freeAttributeVariables = InstantiateUtil.freeAttributeVariables(statement, pf);

        while (iter.hasNext()) {
            List<Integer> index = iter.next();
            Map<QualifiedName, QDescriptor> env=us1.get(bindings, grp1, index);

            Map<QualifiedName, SingleDescriptors> env2=us1.getAttr(freeAttributeVariables,
                    bindings,
                    (UsingIterator) iter);

            InstantiateAction action = new InstantiateAction(pf,
                                                               u,
                                                               this,
                                                                env,
                                                               env2,
                                                               index,
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


}
