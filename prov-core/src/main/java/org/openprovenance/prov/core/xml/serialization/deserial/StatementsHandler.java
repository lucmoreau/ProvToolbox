package org.openprovenance.prov.core.xml.serialization.deserial;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.openprovenance.prov.core.vanilla.*;
import org.openprovenance.prov.core.xml.serialization.Constants;
import org.openprovenance.prov.model.StatementOrBundle;

import java.io.IOException;
import java.util.HashMap;
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


    static Map<String,Class<? extends StatementOrBundle>> setupTable() {
        Map<String, Class<? extends StatementOrBundle>> result = new HashMap<>();
        result.put(Constants.PROPERTY_PROV_END, WasEndedBy.class);
        result.put(Constants.PROPERTY_PROV_START, WasStartedBy.class);
        result.put(Constants.PROPERTY_PROV_INVALIDATION, WasInvalidatedBy.class);
        result.put(Constants.PROPERTY_PROV_MEMBERSHIP, HadMember.class);
        result.put(Constants.PROPERTY_PROV_INFLUENCE, WasInfluencedBy.class);
        result.put( Constants.PROPERTY_PROV_COMMUNICATION, WasInformedBy.class);
        result.put( Constants.PROPERTY_PROV_DERIVATION, WasDerivedFrom.class);
        result.put( Constants.PROPERTY_PROV_ALTERNATE, AlternateOf.class);
        result.put( Constants.PROPERTY_PROV_SPECIALIZATION, SpecializationOf.class);
        result.put( Constants.PROPERTY_PROV_ATTRIBUTION, WasAttributedTo.class);
        result.put( Constants.PROPERTY_PROV_ASSOCIATION, WasAssociatedWith.class);
        result.put( Constants.PROPERTY_PROV_GENERATION, WasGeneratedBy.class);
        result.put( Constants.PROPERTY_PROV_USED, Used.class);
        result.put( Constants.PROPERTY_PROV_ACTIVITY, Activity.class);
        result.put( Constants.PROPERTY_PROV_AGENT, Agent.class);
        result.put(Constants.PROPERTY_PROV_ENTITY, Entity.class);
        result.put(Constants.PROPERTY_PROV_DELEGATION, ActedOnBehalfOf.class);
        return result;
    }
    final static Map<String,Class<? extends StatementOrBundle>> statementMap=setupTable();

    /*
    static boolean aProvExpression(String s) {
        switch (s) {
            case Constants.PROPERTY_PROV_END:
            case Constants.PROPERTY_PROV_START:
            case Constants.PROPERTY_PROV_INVALIDATION:
            case Constants.PROPERTY_PROV_MEMBERSHIP:
            case Constants.PROPERTY_PROV_INFLUENCE:
            case Constants.PROPERTY_PROV_COMMUNICATION:
            case Constants.PROPERTY_PROV_DERIVATION:
            case Constants.PROPERTY_PROV_ALTERNATE:
            case Constants.PROPERTY_PROV_SPECIALIZATION:
            case Constants.PROPERTY_PROV_ATTRIBUTION:
            case Constants.PROPERTY_PROV_ASSOCIATION:
            case Constants.PROPERTY_PROV_GENERATION:
            case Constants.PROPERTY_PROV_USED:
            case Constants.PROPERTY_PROV_ACTIVITY:
            case Constants.PROPERTY_PROV_AGENT:
            case Constants.PROPERTY_PROV_ENTITY:
            case Constants.PROPERTY_PROV_DELEGATION:
                return true;
            default:
            return false;
        }
    }
    private static final Object STATEMENT_LIST = "STATEMENT_LIST";
    */


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
            if ((beanOrClass instanceof Document) && ((cl=statementMap.get(propertyName))!=null)) {
                StatementOrBundle s=jp.readValueAs(cl);
                Document doc=(Document) beanOrClass;
                doc.getStatementOrBundle().add(s);
                return true;
            }
            return super.handleUnknownProperty(ctxt, jp, deserializer, beanOrClass, propertyName);
        }
    }

}
