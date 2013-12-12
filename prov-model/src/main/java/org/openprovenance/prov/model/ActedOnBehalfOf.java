package org.openprovenance.prov.model;

/**
 * <p>Java interface for the PROV Delegation association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-delegation">PROV-DM Definition for Delegation</a>: 
 * Delegation is the assignment of authority and responsibility to an agent (by itself or by another agent)
 *  to carry out a specific activity as a delegate or representative, while the agent it acts on behalf of 
 *  retains some responsibility for the outcome of the delegated work.
 *  
 *  <p>
 *  
 * @author lavm
 *
 */

public interface ActedOnBehalfOf extends Identifiable, HasLabel, HasType, HasOther, Influence {

    QualifiedName getDelegate();

    void setActivity(QualifiedName eid2);

    void setDelegate(QualifiedName delegate);

    void setResponsible(QualifiedName responsible);

    QualifiedName getResponsible();

    QualifiedName getActivity();

}
