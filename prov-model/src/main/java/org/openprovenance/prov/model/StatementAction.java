package org.openprovenance.prov.model;


/** Interface to a visitor like functionality operating over PROV statements and bundles.  
 * This visitor returns void.  For a visitor that returns an object, see {@link StatementActionValue}.
 *  
 * <p><span class="strong">Relevant Factory Methods:</span>
 * <ul>
 * <li> {@link ProvUtilities#doAction(org.openprovenance.prov.model.StatementOrBundle, org.openprovenance.prov.model.StatementActionValue)}
 * <li> {@link ProvUtilities#doAction(org.openprovenance.prov.model.StatementOrBundle, org.openprovenance.prov.model.StatementAction)}
 * <li> {@link ProvUtilities#forAllStatement(java.util.List, org.openprovenance.prov.model.StatementAction)}
 * <li> {@link ProvUtilities#forAllStatementOrBundle(java.util.List, org.openprovenance.prov.model.StatementAction)}
 * </ul>
 * 
 * @author lavm
 *
 */
public interface StatementAction {

    void doAction(Activity s);

    void doAction(Used s);

    void doAction(WasStartedBy s);

    void doAction(Agent s);

    void doAction(AlternateOf s);

    void doAction(WasAssociatedWith s);

    void doAction(WasAttributedTo s);

    void doAction(WasInfluencedBy s);

    void doAction(ActedOnBehalfOf s);

    void doAction(WasDerivedFrom s);

    void doAction(DictionaryMembership s);

    void doAction(DerivedByRemovalFrom s);

    void doAction(WasEndedBy s);

    void doAction(Entity s);

    void doAction(WasGeneratedBy s);

    void doAction(WasInvalidatedBy s);

    void doAction(HadMember s);

    void doAction(MentionOf s);

    void doAction(SpecializationOf s);

    void doAction(DerivedByInsertionFrom s);

    void doAction(WasInformedBy s);

    void doAction(NamedBundle s, ProvUtilities provUtilities);

}
