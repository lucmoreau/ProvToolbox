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

    public Expand(ProvFactory pf, boolean addOrderp, boolean allUpdatedRequired) {
        this.pf = pf;
        this.addOrderp = addOrderp;
        this.allUpdatedRequired = allUpdatedRequired;
    }


    public Document expander(Document docIn, Bindings bindings) {

        
        Bundle bun;
        logger.info("expander: expander " + bindings);

        try {
            bun = u.getBundle(docIn).get(0);
        } catch (RuntimeException e) {
            throw new UncheckedException("Bundle missing in template", e);
        }

        Groupings grp1 = Groupings.fromDocument(docIn, bindings, pf);
        //logger.debug("expander: Found groupings " + grp1);

        Bundle bun1 = (Bundle) expand(bun, bindings, grp1).get(0);
        Document doc1 = pf.newDocument();
        doc1.getStatementOrBundle().add(bun1);

        logger.info("expander: id uri " + bun1.getId().getNamespaceURI());
        logger.info("expander: id pre " + bun1.getId().getPrefix());
        logger.info("expander: id loc " + bun1.getId().getLocalPart());

        bun1.setNamespace(Namespace.gatherNamespaces(bun1));

        doc1.setNamespace(new Namespace());

        return doc1;
    }



    private final ProvFactory pf;

    static ProvUtilities u = new ProvUtilities();

    public List<StatementOrBundle> expand(Statement statement, OldBindings oldBindings, Groupings grp1) {
        Using us1 = ExpandUtil.usedGroups(statement, grp1, oldBindings);
        return expand(statement, oldBindings, grp1, us1);
    }
    
    boolean allExpanded=true;
    public boolean getAllExpanded() {
    	return allExpanded;
    }

    public List<StatementOrBundle> expand(Bundle bun, Bindings bindings, Groupings grp1) {

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
                                               grp1,
                                               addOrderp,
                                               allUpdatedRequired);
        u.doAction(bun, action);
        allExpanded=allExpanded && action.getAllExpanded();
        return action.getList();
    }

    public List<StatementOrBundle> expand(Statement statement,
                                          OldBindings bindings1,
                                          Groupings grp1,
                                          Using us1) {
        List<StatementOrBundle> results = new LinkedList<>();
        Iterator<List<Integer>> iter = us1.iterator();
        /*
         * System.out.println(" --------------------- " );
         * System.out.println(" Statement " + statement);
         * System.out.println(" Using " + us1); System.out.println(" Groupings "
         * + grp1);
         */
        while (iter.hasNext()) {
            List<Integer> index = iter.next();
            // System.out.println(" Index " + index);

            Map<QualifiedName, QualifiedName> env = us1.get(bindings1, grp1, index);
            Map<QualifiedName, List<TypedValue>> env2;

            env2 = us1.getAttr(ExpandUtil.freeAttributeVariables(statement, pf),
                               bindings1,
                               (UsingIterator) iter);

            ExpandAction action = new ExpandAction(pf,
                                                   u,
                                                   this,
                                                   env,
                                                   env2,
                                                   index,
                                                   bindings1,
                                                   grp1,
                                                   addOrderp,
                                                   allUpdatedRequired);
            u.doAction(statement, action);
            allExpanded=allExpanded && action.getAllExpanded();

            results.addAll(action.getList());

        }
        return results;

    }



}
