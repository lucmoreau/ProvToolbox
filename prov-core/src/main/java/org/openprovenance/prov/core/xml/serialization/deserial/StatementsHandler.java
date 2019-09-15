package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.openprovenance.prov.core.vanilla.*;
import org.openprovenance.prov.core.xml.serialization.Constants;
import org.openprovenance.prov.model.Statement;
import org.openprovenance.prov.model.StatementOrBundle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StatementsHandler extends SimpleModule{


    public StatementsHandler(String customKindSerializer, Version version) {
        super(customKindSerializer,version);
    }

    @Override
    public void setupModule(SetupContext context) {
        // Required, as documented in the Javadoc of SimpleModule
        super.setupModule(context);
        context.addDeserializationProblemHandler(new MyDeserializationProblemHandler());
    }


    static Map<String,Class<? extends StatementOrBundle>> setupStatementOrBundleTable() {
        Map<String, Class<? extends StatementOrBundle>> result = new HashMap<>();
        result.put(Constants.PROPERTY_PROV_END, WasEndedBy.class);
        result.put(Constants.PROPERTY_PROV_START, WasStartedBy.class);
        result.put(Constants.PROPERTY_PROV_INVALIDATION, WasInvalidatedBy.class);
        result.put(Constants.PROPERTY_PROV_MEMBERSHIP, HadMember.class);
        result.put(Constants.PROPERTY_PROV_INFLUENCE, WasInfluencedBy.class);
        result.put(Constants.PROPERTY_PROV_COMMUNICATION, WasInformedBy.class);
        result.put(Constants.PROPERTY_PROV_DERIVATION, WasDerivedFrom.class);
        result.put(Constants.PROPERTY_PROV_ALTERNATE, AlternateOf.class);
        result.put(Constants.PROPERTY_PROV_SPECIALIZATION, SpecializationOf.class);
        result.put(Constants.PROPERTY_PROV_ATTRIBUTION, WasAttributedTo.class);
        result.put(Constants.PROPERTY_PROV_ASSOCIATION, WasAssociatedWith.class);
        result.put(Constants.PROPERTY_PROV_GENERATION, WasGeneratedBy.class);
        result.put(Constants.PROPERTY_PROV_USED, Used.class);
        result.put(Constants.PROPERTY_PROV_ACTIVITY, Activity.class);
        result.put(Constants.PROPERTY_PROV_AGENT, Agent.class);
        result.put(Constants.PROPERTY_PROV_ENTITY, Entity.class);
        result.put(Constants.PROPERTY_PROV_DELEGATION, ActedOnBehalfOf.class);
        result.put(Constants.PROPERTY_PROV_BUNDLE, Bundle.class);
        result.put(Constants.PROPERTY_PROV_QUALIFIED_ALTERNATE, QualifiedAlternateOf.class);
        result.put(Constants.PROPERTY_PROV_QUALIFIED_SPECIALIZATION, QualifiedSpecializationOf.class);
        result.put(Constants.PROPERTY_PROV_QUALIFIED_MEMBERSHIP, QualifiedHadMember.class);
        return result;
    }
    static Map<String,Class<? extends Statement>> setupStatementTable() {
        Map<String, Class<? extends Statement>> result = new HashMap<>();
        result.put(Constants.PROPERTY_PROV_END, WasEndedBy.class);
        result.put(Constants.PROPERTY_PROV_START, WasStartedBy.class);
        result.put(Constants.PROPERTY_PROV_INVALIDATION, WasInvalidatedBy.class);
        result.put(Constants.PROPERTY_PROV_MEMBERSHIP, HadMember.class);
        result.put(Constants.PROPERTY_PROV_INFLUENCE, WasInfluencedBy.class);
        result.put(Constants.PROPERTY_PROV_COMMUNICATION, WasInformedBy.class);
        result.put(Constants.PROPERTY_PROV_DERIVATION, WasDerivedFrom.class);
        result.put(Constants.PROPERTY_PROV_ALTERNATE, AlternateOf.class);
        result.put(Constants.PROPERTY_PROV_SPECIALIZATION, SpecializationOf.class);
        result.put(Constants.PROPERTY_PROV_ATTRIBUTION, WasAttributedTo.class);
        result.put(Constants.PROPERTY_PROV_ASSOCIATION, WasAssociatedWith.class);
        result.put(Constants.PROPERTY_PROV_GENERATION, WasGeneratedBy.class);
        result.put(Constants.PROPERTY_PROV_USED, Used.class);
        result.put(Constants.PROPERTY_PROV_ACTIVITY, Activity.class);
        result.put(Constants.PROPERTY_PROV_AGENT, Agent.class);
        result.put(Constants.PROPERTY_PROV_ENTITY, Entity.class);
        result.put(Constants.PROPERTY_PROV_DELEGATION, ActedOnBehalfOf.class);
        result.put(Constants.PROPERTY_PROV_QUALIFIED_ALTERNATE, QualifiedAlternateOf.class);
        result.put(Constants.PROPERTY_PROV_QUALIFIED_SPECIALIZATION, QualifiedSpecializationOf.class);
        result.put(Constants.PROPERTY_PROV_QUALIFIED_MEMBERSHIP, QualifiedHadMember.class);
        return result;
    }
    final static Map<String,Class<? extends StatementOrBundle>> statementOrBundleMap = setupStatementOrBundleTable();
    final static Map<String,Class<? extends Statement>> statementMap = setupStatementTable();



    private static class MyDeserializationProblemHandler extends DeserializationProblemHandler {

        /**
         * Method called to deal with a property that did not map to a known
         * Bean property. Method can deal with the problem as it sees fit (ignore,
         * throw exception); but if it does return, it has to skip the matching
         * Json content parser has.
         *<p>
         * NOTE: method signature was changed in version 1.5; explicit JsonParser
         * <b>must</b> be passed since it may be something other than what
         * context has. Prior versions did not include the first parameter.
         *
         * @param jp Parser that points to value of the unknown property
         * @param ctxt Context for deserialization; allows access to the parser,
         *    error reporting functionality
         * @param beanOrClass Instance that is being populated by this
         *   deserializer, or if not known, Class that would be instantiated.
         *   If null, will assume type is what  returns.
         * @param propertyName Name of the property that can not be mapped
         */
        @Override
        public boolean handleUnknownProperty(DeserializationContext ctxt,
                                             JsonParser jp,
                                             JsonDeserializer<?> deserializer,
                                             Object beanOrClass,
                                             String propertyName)
                throws IOException {

            Class<? extends StatementOrBundle> cl;
            if ((beanOrClass instanceof Document) && ((cl= statementOrBundleMap.get(propertyName))!=null)) {
                StatementOrBundle s=jp.readValueAs(cl);
                Document doc=(Document) beanOrClass;
                doc.getStatementOrBundle().add(s);
                return true;
            } else {
                Class<? extends Statement> cl2;
                if ((beanOrClass instanceof Bundle) && ((cl2 = statementMap.get(propertyName)) != null)) {
                    System.out.println("########################" + propertyName);
                    Statement s = jp.readValueAs(cl2);
                    Bundle bun = (Bundle) beanOrClass;
                    bun.getStatement().add(s);
                    return true;
                }
            }
            return super.handleUnknownProperty(ctxt, jp, deserializer, beanOrClass, propertyName);
        }
    }

}
