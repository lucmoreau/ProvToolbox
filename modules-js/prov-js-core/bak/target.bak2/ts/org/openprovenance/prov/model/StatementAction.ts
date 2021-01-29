/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * Interface to a visitor like functionality operating over PROV statements and bundles.
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
     * @class
     */
    export interface StatementAction {
        doAction(s?: any, provUtilities?: any);
    }
}

