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
import org.openprovenance.prov.template.json.Bindings;

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


        Map<QualifiedName, QualifiedName> env0 = new HashMap<>();
        Map<QualifiedName, List<TypedValue>> env1 = new HashMap<>();


        ExpandAction action = new ExpandAction(pf,
                                               u,
                                               this,
                                               env0,
                                               env1,
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
        warnAboutUnboundTemplateVariables(bindings, templateFilename, freeVars);
        warnAboutUnusedBindingsVariables(bindings, bindingsFilename, freeVars);
        return action.getList();
    }

    private void warnAboutUnusedBindingsVariables(Bindings bindings, String bindingsFilename, Set<String> freeVariables) {
        if (!displayUnusedBindings) return;
        Set<String> allBindingsVars= bindings.var.keySet();
        allBindingsVars.removeAll(freeVariables);
        if (!allBindingsVars.isEmpty()) {
            // get the suffix of bindingsFilename following last slash


            System.out.println("Unused bindings variables: " + allBindingsVars + ((bindingsFilename ==null)?"": ": in bindings file " + filenameSuffix(bindingsFilename)));
        }
    }

    private void warnAboutUnboundTemplateVariables(Bindings bindings, String templateFilename, Set<String> freeVariables) {
        if (!displayUnusedVariables) return;
        List<String> unboundVariables=new LinkedList<>();
        Set<String> allBindingsVars=bindings.var.keySet();
        for (String var: freeVariables) {
            if (!allBindingsVars.contains(var)) {
                unboundVariables.add(var);
            }
        }
        if (!unboundVariables.isEmpty()) {
            System.out.println("Unbound template variables: " + unboundVariables + ((templateFilename ==null)?"": " in template " + filenameSuffix(templateFilename)));
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


        while (iter.hasNext()) {
            List<Integer> index = iter.next();
            // System.out.println(" Index " + index);

            Map<QualifiedName, QualifiedName> env = us1.get(oldBindings, grp1, index);
        // testing
            us1.newGet(bindings, grp1, index);
            Map<QualifiedName, List<TypedValue>> env2;

            env2 = us1.getAttr(ExpandUtil.freeAttributeVariables(statement, pf),
                               oldBindings,
                               (UsingIterator) iter);

            ExpandAction action = new ExpandAction(pf,
                                                   u,
                                                   this,
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



}
