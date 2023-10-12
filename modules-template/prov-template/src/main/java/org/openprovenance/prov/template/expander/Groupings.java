package org.openprovenance.prov.template.expander;

import static org.openprovenance.prov.template.expander.ExpandUtil.LINKED_URI;
import static org.openprovenance.prov.template.expander.ExpandUtil.newVariable;

import java.util.*;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.json.Bindings;

public class Groupings {
    final private List<List<QualifiedName>> variables;

    static ProvUtilities u= new ProvUtilities();

    public Groupings() {
        variables= new LinkedList<>();
    }

    public List<QualifiedName> get(int group) {
        return variables.get(group);
    }

    public int size() {
        return variables.size();
    }

    public void addVariable(QualifiedName name) {
        List<QualifiedName> ll= new LinkedList<>();
        ll.add(name);
        variables.add(ll);
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
            Set<QualifiedName> vars=ExpandUtil.freeVariables(statement);
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
        //TODO
        if (bindings!=null) {
            Map<String, String> blinked = bindings.linked;
            if (blinked !=null) {
                for (String key: blinked.keySet()) {
                    addEntry(linked, newVariable(key, pf), newVariable(blinked.get(key), pf));
                    addEntry(linked, newVariable(blinked.get(key),pf), newVariable(key, pf));

                }
            }
        }
        //Luc add information for bindings file here

        // Compute transitive closure
        for(QualifiedName visit: linked.keySet()) {
            Stack<QualifiedName> toVisit = new Stack<>();
            toVisit.push(visit);
            Collection<QualifiedName> reachable = new HashSet<>();
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

        int currentGroup=0;
        for (QualifiedName qn: sorted) {
            Set<QualifiedName> links=linked.get(qn);
            if (links==null || links.isEmpty()) {
                grps.addVariable(qn);
            } else {
                Integer aGroup=linkedGroups.get(qn);
                if (aGroup!=null) {
                    grps.addVariable(aGroup,qn);
                } else {
                    grps.addVariable(qn);
                    for (QualifiedName otherQn: links) {
                        linkedGroups.put(otherQn,currentGroup);
                    }
                }
            }
            currentGroup++;
        }
        return grps;
    }

    static  void addEntry(Map<QualifiedName, Set<QualifiedName>> linked,
                          QualifiedName id, QualifiedName otherId) {
        linked.computeIfAbsent(otherId, k -> new HashSet<>());
        linked.get(otherId).add(id);
    }

}
