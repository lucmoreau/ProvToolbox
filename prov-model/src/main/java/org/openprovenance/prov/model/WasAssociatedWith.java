package org.openprovenance.prov.model;



/**
 * <p>Java interface for the PROV Association association.
 * 
 * <p><a href="http://www.w3.org/TR/prov-dm/#concept-association">PROV-DM Definition for Association</a>: 
 * An activity association is an assignment of responsibility to an agent for an activity, indicating that
 * the agent had a role in the activity. It further allows for a plan to be specified, which is the plan intended 
 * by the agent to achieve some goals in the context of this activity.
 *  <p>
 *  
 * @author lavm
 *
 */

public interface WasAssociatedWith extends Identifiable,  HasLabel, HasType, HasRole, HasOther, Influence {

    void setActivity(QualifiedName eid2);

    void setAgent(QualifiedName eid1);

    void setPlan(QualifiedName eid);

    QualifiedName getActivity();

    QualifiedName getAgent();

    QualifiedName getPlan();

}
