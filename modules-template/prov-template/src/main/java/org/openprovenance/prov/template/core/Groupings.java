package org.openprovenance.prov.template.core;

import static org.openprovenance.prov.template.core.InstantiateUtil.LINKED_URI;
import static org.openprovenance.prov.template.core.InstantiateUtil.newVariable;

import java.util.*;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.json.Bindings;

public class Groupings {
    final private static ProvUtilities u= new ProvUtilities();
    final private ArrayList<ArrayList<QualifiedName>> variables;
    private QualifiedName[] freeVariables;

    public Groupings() {
        variables= new ArrayList<>();
    }

    public ArrayList<QualifiedName> get(int group) {
        return variables.get(group);
    }

    public int size() {
        return variables.size();
    }

    public int addVariable(QualifiedName name) {
        ArrayList<QualifiedName> ll= new ArrayList<>();
        ll.add(name);
        int len=variables.size();
        variables.add(ll);
        return len;
    }


    public void addVariable(int group, QualifiedName name) {
        List<QualifiedName> v=variables.get(group);
        v.add(name);
    }




    @Override
    public String toString () {
        return "" + variables;
    }

    static public Groupings fromDocument(Document doc, Bindings bindings, ProvFactory pf) {
        Map<QualifiedName,Set<QualifiedName>> linked= new Hashtable<>();
        Map<QualifiedName,Integer> linkedGroups= new Hashtable<>();

        Bundle bun=u.getBundle(doc).get(0);
        Groupings grps=new Groupings();
        Set<QualifiedName> allVars= new HashSet<>();
        for (Statement statement: bun.getStatement()) {
            Set<QualifiedName> vars= InstantiateUtil.freeVariables(statement);
            allVars.addAll(vars);
            if (statement instanceof HasOther) {
                HasOther stmt2=(HasOther)statement;
                for (Other other: stmt2.getOther()) {
                    if (LINKED_URI.equals(other.getElementName().getUri())) {
                        QualifiedName id=((Identifiable)statement).getId();
                        QualifiedName otherId=(QualifiedName) other.getValue();
                        addEntry(linked, otherId, id);
                        addEntry(linked, id, otherId);
                    }
                }
            }
        }

        if (bindings!=null) {
            Map<String, String> blinked = bindings.linked;
            if (blinked !=null) {
                for (String key: blinked.keySet()) {
                    QualifiedName q1 = newVariable(key, pf);
                    QualifiedName q2 = newVariable(blinked.get(key), pf);
                    addEntry(linked, q1, q2);
                    addEntry(linked, q2, q1);
                }
            }
        }


        // Compute transitive closure
        for(QualifiedName visit: linked.keySet()) {
            Stack<QualifiedName> toVisit = new Stack<>();
            toVisit.push(visit);
            Set<QualifiedName> reachable = new HashSet<>();
            while(!toVisit.isEmpty()) {
                QualifiedName local_qn = toVisit.pop();
                for(QualifiedName neighbour: linked.get(local_qn)) {
                    if (!reachable.contains(neighbour)) {
                        reachable.add(neighbour);
                        toVisit.push(neighbour);
                    }
                }
            }
            linked.get(visit).addAll(reachable);
        }

        QualifiedName [] sorted=allVars.toArray(new QualifiedName[0]);
        Arrays.sort(sorted, Comparator.comparing(QualifiedName::getUri));;

        // currentGroup has the same value as size()
        int currentGroup=0;
        for (QualifiedName qn: sorted) {
            Set<QualifiedName> links=linked.get(qn);
            if (links==null || links.isEmpty()) {
                int g=grps.addVariable(qn);
                if (g!=currentGroup) {
                    throw new IllegalStateException("Unexpected group index");
                }
                currentGroup++;
            } else {
                Integer aGroup=linkedGroups.get(qn);
                if (aGroup!=null) {
                    grps.addVariable(aGroup,qn);
                } else {
                    int g=grps.addVariable(qn);
                    if (g!=currentGroup) {
                        throw new IllegalStateException("Unexpected group index");
                    }
                    for (QualifiedName otherQn: links) {
                        linkedGroups.put(otherQn,currentGroup);
                    }
                    currentGroup++;
                }
            }
        }

        // also look for all free variables in attributes
        Set<QualifiedName> vars= new HashSet<>();
        for (Statement statement: bun.getStatement()) {
            vars.addAll(InstantiateUtil.freeAttributeVariables(statement));
        }
        Collections.addAll(vars, sorted);
        QualifiedName [] sorted2=vars.toArray(new QualifiedName[0]);
        grps.setFreeVariables(sorted2);
        return grps;
    }

    private void setFreeVariables(QualifiedName[] freeVariables) {
        this.freeVariables=freeVariables;
    }

    public QualifiedName[] getFreeVariables() {
        return freeVariables;
    }

    static  private void addEntry(Map<QualifiedName, Set<QualifiedName>> linked,
                          QualifiedName id,
                          QualifiedName otherId) {
        linked.computeIfAbsent(otherId, k -> new HashSet<>());
        linked.get(otherId).add(id);
    }

}
