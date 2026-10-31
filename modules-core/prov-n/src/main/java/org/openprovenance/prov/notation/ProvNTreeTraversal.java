package org.openprovenance.prov.notation;

import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.XMLGregorianCalendar;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.model.StatementOrBundle.Kind;
import org.openprovenance.prov.notation.ProvNParser.*;

/**
 * Traverses the parse tree that the ANTLR 4 parser {@link ProvNParser} builds for a PROV-N document, and
 * constructs its PROV representation by invoking the methods of a {@link ModelConstructor}.
 * <p>
 * This is the counterpart of {@link TreeTraversal}, which does the same for the tree built by the ANTLR 3
 * parser ({@link PROV_NParser}), and it constructs the same representation.
 * The visit of a statement returns the object built by the {@link ModelConstructor}; the visit of a literal,
 * its Java value: a {@link String}, a {@link LangString}, an {@link Integer}, or a pair (an {@code Object[]})
 * value and datatype, for a typed literal.
 * <p>
 * Qualified names are resolved against the namespace declarations in scope, so an instance of this class
 * is stateful: use a new one for each document.
 */
public class ProvNTreeTraversal extends ProvNBaseVisitor<Object> {

    final private ModelConstructor c;
    final private ProvFactory pFactory;
    final private Name name;
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;

    /** The namespace in scope: the document's, or a bundle's, whose parent is the document's. */
    private Namespace namespace;

    public ProvNTreeTraversal(ModelConstructor c, ProvFactory pFactory, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.c = c;
        this.pFactory = pFactory;
        this.name = pFactory.getName();
        this.namespace = pFactory.newNamespace();
        this.namespace.addKnownNamespaces();
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
    }

    /* ------------------------------------------------------------------
       Documents, bundles, namespaces
       ------------------------------------------------------------------ */

    @Override
    public Document visitDocument(DocumentContext ctx) {
        declareNamespaces(ctx.namespaceDeclarations());
        Namespace documentNamespace = namespace;
        List<Statement> statements = statements(ctx.expression());
        List<Bundle> bundles = new LinkedList<>();
        for (BundleContext b : ctx.bundle()) {
            bundles.add(visitBundle(b));
        }
        return c.newDocument(documentNamespace, statements, bundles);
    }

    @Override
    public Bundle visitBundle(BundleContext ctx) {
        Namespace localNamespace = pFactory.newNamespace();
        localNamespace.addKnownNamespaces();
        localNamespace.setParent(namespace);

        // the local namespace becomes the current namespace
        namespace = localNamespace;
        try {
            declareNamespaces(ctx.namespaceDeclarations());
            // the bundle identifier is resolved after the bundle's namespace declarations
            QualifiedName bundleId = visitIdentifier(ctx.identifier());
            List<Statement> statements = statements(ctx.expression());
            return c.newNamedBundle(bundleId, localNamespace, statements);
        } finally {
            // restore the parent namespace
            namespace = localNamespace.getParent();
        }
    }

    private void declareNamespaces(NamespaceDeclarationsContext ctx) {
        if (ctx == null) return;
        // declarations are processed in document order, the default namespace first
        for (ParseTree child : ctx.children) {
            visit(child);
        }
    }

    @Override
    public Object visitNamespaceDeclaration(NamespaceDeclarationContext ctx) {
        namespace.register(ctx.PREFX().getText(), unwrap(ctx.IRI_REF().getText()));
        return null;
    }

    @Override
    public Object visitDefaultNamespaceDeclaration(DefaultNamespaceDeclarationContext ctx) {
        namespace.registerDefault(unwrap(ctx.IRI_REF().getText()));
        return null;
    }

    /** The statements built for these expressions, leaving out those for which none is built (e.g., unknown extensions). */
    private List<Statement> statements(List<ExpressionContext> expressions) {
        List<Statement> statements = new LinkedList<>();
        for (ExpressionContext e : expressions) {
            Object o = visitExpression(e);
            if (o != null) statements.add((Statement) o);
        }
        return statements;
    }

    @Override
    public Object visitExpression(ExpressionContext ctx) {
        return visit(ctx.getChild(0));
    }

    /* ------------------------------------------------------------------
       Component 1: entities and activities
       ------------------------------------------------------------------ */

    @Override
    public Object visitEntityExpression(EntityExpressionContext ctx) {
        return c.newEntity(identifierOrMarker(ctx.identifierOrMarker()),
                           attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitActivityExpression(ActivityExpressionContext ctx) {
        return c.newActivity(identifierOrMarker(ctx.identifierOrMarker()),
                             time(ctx.startTime),
                             time(ctx.endTime),
                             attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitGenerationExpression(GenerationExpressionContext ctx) {
        return c.newWasGeneratedBy(optionalIdentifier(ctx.optionalIdentifier()),
                                   identifierOrMarker(ctx.entity),
                                   identifierOrMarker(ctx.activity),
                                   time(ctx.timeOrMarker()),
                                   attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitUsageExpression(UsageExpressionContext ctx) {
        return c.newUsed(optionalIdentifier(ctx.optionalIdentifier()),
                         identifierOrMarker(ctx.activity),
                         identifierOrMarker(ctx.entity),
                         time(ctx.timeOrMarker()),
                         attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitStartExpression(StartExpressionContext ctx) {
        return c.newWasStartedBy(optionalIdentifier(ctx.optionalIdentifier()),
                                 identifierOrMarker(ctx.activity),
                                 identifierOrMarker(ctx.trigger),
                                 identifierOrMarker(ctx.starter),
                                 time(ctx.timeOrMarker()),
                                 OpenprovTerms.canonical(Kind.PROV_START, attributes(ctx.optionalAttributeValuePairs()), pFactory));
    }

    @Override
    public Object visitEndExpression(EndExpressionContext ctx) {
        return c.newWasEndedBy(optionalIdentifier(ctx.optionalIdentifier()),
                               identifierOrMarker(ctx.activity),
                               identifierOrMarker(ctx.trigger),
                               identifierOrMarker(ctx.ender),
                               time(ctx.timeOrMarker()),
                               OpenprovTerms.canonical(Kind.PROV_END, attributes(ctx.optionalAttributeValuePairs()), pFactory));
    }

    @Override
    public Object visitInvalidationExpression(InvalidationExpressionContext ctx) {
        return c.newWasInvalidatedBy(optionalIdentifier(ctx.optionalIdentifier()),
                                     identifierOrMarker(ctx.entity),
                                     identifierOrMarker(ctx.activity),
                                     time(ctx.timeOrMarker()),
                                     attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitCommunicationExpression(CommunicationExpressionContext ctx) {
        return c.newWasInformedBy(optionalIdentifier(ctx.optionalIdentifier()),
                                  identifierOrMarker(ctx.informed),
                                  identifierOrMarker(ctx.informant),
                                  OpenprovTerms.canonical(Kind.PROV_COMMUNICATION, attributes(ctx.optionalAttributeValuePairs()), pFactory));
    }

    /* ------------------------------------------------------------------
       Component 2: agents and responsibility
       ------------------------------------------------------------------ */

    @Override
    public Object visitAgentExpression(AgentExpressionContext ctx) {
        return c.newAgent(identifierOrMarker(ctx.identifierOrMarker()),
                          attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitAttributionExpression(AttributionExpressionContext ctx) {
        return c.newWasAttributedTo(optionalIdentifier(ctx.optionalIdentifier()),
                                    identifierOrMarker(ctx.entity),
                                    identifierOrMarker(ctx.agent),
                                    OpenprovTerms.canonical(Kind.PROV_ATTRIBUTION, attributes(ctx.optionalAttributeValuePairs()), pFactory));
    }

    @Override
    public Object visitAssociationExpression(AssociationExpressionContext ctx) {
        return c.newWasAssociatedWith(optionalIdentifier(ctx.optionalIdentifier()),
                                      identifierOrMarker(ctx.activity),
                                      identifierOrMarker(ctx.agent),
                                      identifierOrMarker(ctx.plan),
                                      attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitResponsibilityExpression(ResponsibilityExpressionContext ctx) {
        return c.newActedOnBehalfOf(optionalIdentifier(ctx.optionalIdentifier()),
                                    identifierOrMarker(ctx.delegate),
                                    identifierOrMarker(ctx.responsible),
                                    identifierOrMarker(ctx.activity),
                                    attributes(ctx.optionalAttributeValuePairs()));
    }

    /* ------------------------------------------------------------------
       Component 3: derivations
       ------------------------------------------------------------------ */

    @Override
    public Object visitDerivationExpression(DerivationExpressionContext ctx) {
        return derivation(ctx.optionalIdentifier(), ctx.derivationArguments(), null);
    }

    /** A revision is a derivation of type prov:Revision (PROV-DM, Section 5.2.2). */
    @Override
    public Object visitRevisionExpression(RevisionExpressionContext ctx) {
        return derivation(ctx.optionalIdentifier(), ctx.derivationArguments(), name.PROV_REVISION);
    }

    /** A quotation is a derivation of type prov:Quotation (PROV-DM, Section 5.2.3). */
    @Override
    public Object visitQuotationExpression(QuotationExpressionContext ctx) {
        return derivation(ctx.optionalIdentifier(), ctx.derivationArguments(), name.PROV_QUOTATION);
    }

    /** A primary source is a derivation of type prov:PrimarySource (PROV-DM, Section 5.2.4). */
    @Override
    public Object visitHadPrimarySourceExpression(HadPrimarySourceExpressionContext ctx) {
        return derivation(ctx.optionalIdentifier(), ctx.derivationArguments(), name.PROV_PRIMARY_SOURCE);
    }

    private Object derivation(OptionalIdentifierContext id, DerivationArgumentsContext ctx, QualifiedName derivationType) {
        List<Attribute> attributes = attributes(ctx.optionalAttributeValuePairs());
        if (derivationType != null) {
            attributes.add(pFactory.newAttribute(name.PROV_TYPE, derivationType, name.PROV_QUALIFIED_NAME));
        }
        return c.newWasDerivedFrom(optionalIdentifier(id),
                                   identifierOrMarker(ctx.generated),
                                   identifierOrMarker(ctx.used),
                                   identifierOrMarker(ctx.activity),
                                   identifierOrMarker(ctx.generation),
                                   identifierOrMarker(ctx.usage),
                                   attributes);
    }

    @Override
    public Object visitInfluenceExpression(InfluenceExpressionContext ctx) {
        return c.newWasInfluencedBy(optionalIdentifier(ctx.optionalIdentifier()),
                                    identifierOrMarker(ctx.influencee),
                                    identifierOrMarker(ctx.influencer),
                                    attributes(ctx.optionalAttributeValuePairs()));
    }

    /* ------------------------------------------------------------------
       Component 5: alternate entities
       ------------------------------------------------------------------ */

    @Override
    public Object visitAlternateExpression(AlternateExpressionContext ctx) {
        return c.newAlternateOf(identifierOrMarker(ctx.alternate1), identifierOrMarker(ctx.alternate2));
    }

    @Override
    public Object visitSpecializationExpression(SpecializationExpressionContext ctx) {
        return c.newSpecializationOf(identifierOrMarker(ctx.specific), identifierOrMarker(ctx.general));
    }

    @Override
    public Object visitMentionExpression(MentionExpressionContext ctx) {
        return c.newMentionOf(identifierOrMarker(ctx.specific), identifierOrMarker(ctx.general), identifierOrMarker(ctx.bundleId));
    }

    /* ------------------------------------------------------------------
       Component 6: collections and dictionaries
       ------------------------------------------------------------------ */

    @Override
    public Object visitCollectionMembershipExpression(CollectionMembershipExpressionContext ctx) {
        List<QualifiedName> entities = new LinkedList<>();
        QualifiedName entity = identifierOrMarker(ctx.entity);
        if (entity != null) entities.add(entity);
        return c.newHadMember(identifierOrMarker(ctx.collection), entities);
    }

    @Override
    public Object visitInsertionExpression(InsertionExpressionContext ctx) {
        return c.newDerivedByInsertionFrom(optionalIdentifier(ctx.optionalIdentifier()),
                                           identifierOrMarker(ctx.after),
                                           identifierOrMarker(ctx.before),
                                           visitKeyEntitySet(ctx.keyEntitySet()),
                                           attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitRemovalExpression(RemovalExpressionContext ctx) {
        List<Key> keys = new LinkedList<>();
        for (LiteralContext l : ctx.literal()) {
            keys.add(key(l));
        }
        return c.newDerivedByRemovalFrom(optionalIdentifier(ctx.optionalIdentifier()),
                                         identifierOrMarker(ctx.after),
                                         identifierOrMarker(ctx.before),
                                         keys,
                                         attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitDictionaryMembershipExpression(DictionaryMembershipExpressionContext ctx) {
        List<Entry> entries = new LinkedList<>();
        entries.add(pFactory.newEntry(key(ctx.literal()), identifierOrMarker(ctx.entity)));
        return c.newDictionaryMembership(identifierOrMarker(ctx.dictionary), entries);
    }

    @Override
    public List<Entry> visitKeyEntitySet(KeyEntitySetContext ctx) {
        List<Entry> entries = new LinkedList<>();
        for (KeyEntityPairContext pair : ctx.keyEntityPair()) {
            entries.add(pFactory.newEntry(key(pair.literal()), identifierOrMarker(pair.identifierOrMarker())));
        }
        return entries;
    }

    private Key key(LiteralContext ctx) {
        Object[] valueAndType = valueAndType(visit(ctx));
        return pFactory.newKey(valueAndType[0], (QualifiedName) valueAndType[1]);
    }

    /* ------------------------------------------------------------------
       Extensibility
       ------------------------------------------------------------------ */

    @Override
    public Object visitExtensibilityExpression(ExtensibilityExpressionContext ctx) {
        QualifiedName extensionName = visitQualifiedName(ctx.qualifiedName());
        QualifiedName id = optionalIdentifier(ctx.optionalIdentifier());
        List<Object> args = new LinkedList<>();
        for (ExtensibilityArgumentContext arg : ctx.extensibilityArgument()) {
            args.add(visitExtensibilityArgument(arg));
        }
        return newExtension(extensionName, id, args, attributes(ctx.optionalAttributeValuePairs()));
    }

    @Override
    public Object visitExtensibilityArgument(ExtensibilityArgumentContext ctx) {
        if (ctx.identifierOrMarker() != null) return identifierOrMarker(ctx.identifierOrMarker());
        return visit(ctx.getChild(0));
    }

    @Override
    public List<Object> visitExtensibilityRecord(ExtensibilityRecordContext ctx) {
        List<Object> args = new LinkedList<>();
        for (ExtensibilityArgumentContext arg : ctx.extensibilityArgument()) {
            args.add(visitExtensibilityArgument(arg));
        }
        return args;
    }

    /**
     * Constructs the statement for an extension: the qualified forms of hadMember, specializationOf and
     * alternateOf in the provext namespace. Other extensions are ignored: null is returned.
     */
    @SuppressWarnings("unchecked")
    public Object newExtension(QualifiedName extensionName, QualifiedName id, List<Object> args, List<Attribute> attributes) {
        if (!NamespacePrefixMapper.isProvExt(extensionName.getNamespaceURI())) {
            return null;
        }
        ModelConstructorExtension mce = (ModelConstructorExtension) c;
        switch (extensionName.getLocalPart()) {
            case "hadMember":
                checkArguments(extensionName, args, 2);
                return mce.newQualifiedHadMember(id, (QualifiedName) args.get(0), List.of((QualifiedName) args.get(1)),
                                                 OpenprovTerms.canonical(Kind.PROV_MEMBERSHIP, attributes, pFactory));
            case "specializationOf":
                checkArguments(extensionName, args, 2);
                return mce.newQualifiedSpecializationOf(id, (QualifiedName) args.get(0), (QualifiedName) args.get(1),
                                                        OpenprovTerms.canonical(Kind.PROV_SPECIALIZATION, attributes, pFactory));
            case "alternateOf":
                checkArguments(extensionName, args, 2);
                return mce.newQualifiedAlternateOf(id, (QualifiedName) args.get(0), (QualifiedName) args.get(1), attributes);
        }
        throw new UnsupportedOperationException("unknown " + extensionName);
    }

    private void checkArguments(QualifiedName extensionName, List<Object> args, int expected) {
        if (args.size() != expected) {
            throw new IllegalArgumentException(extensionName + " expects " + expected + " arguments, found " + args.size());
        }
        for (Object arg : args) {
            if (!(arg instanceof QualifiedName)) {
                throw new IllegalArgumentException(extensionName + " expects qualified names as arguments, found " + arg);
            }
        }
    }

    /* ------------------------------------------------------------------
       Identifiers, attributes, literals, time
       ------------------------------------------------------------------ */

    @Override
    public QualifiedName visitIdentifier(IdentifierContext ctx) {
        if (ctx == null) return null;
        return visitQualifiedName(ctx.qualifiedName());
    }

    @Override
    public QualifiedName visitQualifiedName(QualifiedNameContext ctx) {
        return stringToQualifiedName(ctx.getText());
    }

    /** The identifier, or null, for a marker '-' or an absent identifier. */
    private QualifiedName identifierOrMarker(IdentifierOrMarkerContext ctx) {
        if (ctx == null) return null;
        return visitIdentifier(ctx.identifier());
    }

    private QualifiedName optionalIdentifier(OptionalIdentifierContext ctx) {
        if (ctx == null) return null;
        return identifierOrMarker(ctx.identifierOrMarker());
    }

    /** The time, or null, for a marker '-' or an absent time. */
    private XMLGregorianCalendar time(TimeOrMarkerContext ctx) {
        if (ctx == null || ctx.time() == null) return null;
        return visitTime(ctx.time());
    }

    @Override
    public XMLGregorianCalendar visitTime(TimeContext ctx) {
        return pFactory.newISOTime(ctx.DATETIME().getText(), dateTimeOption, optionalTimeZone);
    }

    /** The attributes, in document order, in a mutable list. */
    private List<Attribute> attributes(OptionalAttributeValuePairsContext ctx) {
        List<Attribute> attributes = new LinkedList<>();
        if (ctx != null && ctx.attributeValuePairs() != null) {
            for (AttributeValuePairContext pair : ctx.attributeValuePairs().attributeValuePair()) {
                attributes.add(visitAttributeValuePair(pair));
            }
        }
        return attributes;
    }

    @Override
    public Attribute visitAttributeValuePair(AttributeValuePairContext ctx) {
        Object[] valueAndType = valueAndType(visit(ctx.literal()));
        return pFactory.newAttribute(visitQualifiedName(ctx.qualifiedName()), valueAndType[0], (QualifiedName) valueAndType[1]);
    }

    /** The value and datatype of a literal: those of a typed literal, or the datatype implied by the Java value of the literal. */
    private Object[] valueAndType(Object literal) {
        if (literal instanceof Object[]) return (Object[]) literal;
        if (literal instanceof LangString) return new Object[]{literal, name.PROV_LANG_STRING};
        if (literal instanceof Integer) return new Object[]{literal, name.XSD_INT};
        return new Object[]{literal, name.XSD_STRING};
    }

    /**
     * A string literal: a {@link String}, a {@link LangString} if it has a language tag, or, if it has a datatype, a
     * pair value and datatype, where the value is a {@link QualifiedName} if the datatype is prov:QUALIFIED_NAME.
     */
    @Override
    public Object visitStringLiteral(StringLiteralContext ctx) {
        String value = (ctx.STRING_LITERAL() != null)
                ? unescape(unwrap(ctx.STRING_LITERAL().getText()))
                : unescape(unquoteTrice(ctx.STRING_LITERAL_LONG2().getText()));
        TerminalNode langtag = ctx.LANGTAG();
        if (ctx.datatype() == null) {
            if (langtag == null) return value;
            return pFactory.newInternationalizedString(value, stripAt(langtag.getText()));
        }
        if (langtag != null) {
            // a language tag takes precedence over the datatype
            LangString lstring = pFactory.newInternationalizedString(value, stripAt(langtag.getText()));
            return new Object[]{lstring, name.PROV_LANG_STRING};
        }
        return typedLiteral(value, visitQualifiedName(ctx.datatype().qualifiedName()));
    }

    @Override
    public Integer visitIntLiteral(IntLiteralContext ctx) {
        return Integer.valueOf(ctx.getText());
    }

    /** A qualified name literal 'ex:foo': the pair of the qualified name ex:foo and prov:QUALIFIED_NAME. */
    @Override
    public Object[] visitQualifiedNameLiteral(QualifiedNameLiteralContext ctx) {
        return typedLiteral(unwrap(ctx.QUALIFIED_NAME_LITERAL().getText()), name.PROV_QUALIFIED_NAME);
    }

    private Object[] typedLiteral(String literal, QualifiedName datatype) {
        Object value = literal;
        if (datatype.equals(name.PROV_QUALIFIED_NAME)) {
            value = stringToQualifiedName(literal);
        }
        return new Object[]{value, datatype};
    }

    /* ------------------------------------------------------------------
       Utilities
       ------------------------------------------------------------------ */

    private QualifiedName stringToQualifiedName(String s) {
        return namespace.stringToQualifiedName(s, pFactory);
    }

    private static String unescape(String s) {
        return ProvUtilities.unescape(s);
    }

    private static String unwrap(String s) {
        return s.substring(1, s.length() - 1);
    }

    private static String unquoteTrice(String s) {
        return s.substring(3, s.length() - 3);
    }

    private static String stripAt(String langtag) {
        return langtag.substring(1);
    }

    @Override
    protected Object defaultResult() {
        return null;
    }
}
