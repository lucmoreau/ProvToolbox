package nlg.wrapper;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatementsHandler extends SimpleModule{


    public StatementsHandler(String customSerializaer, Version version) {
        super(customSerializaer,version);
    }

    @Override
    public void setupModule(SetupContext context) {
        // Required, as documented in the Javadoc of SimpleModule
        super.setupModule(context);
        context.addDeserializationProblemHandler(new MyDeserializationProblemHandler());
    }


    static Map<String,Class<? extends Object>> setupStatementTable() {
        Map<String, Class<? extends Object>> result = new HashMap<>();
        result.put("field", Field.class);
        result.put("value", Value.class);
        result.put("block", Block.class);
        result.put("statement", Statement.class);
        result.put("next", Next.class);
        result.put("mutation", Mutation.class);
        return result;
    }
    final static Map<String,Class<? extends Object>> statementMap = setupStatementTable();



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


            Class<? extends Object> cl2;
            if ((cl2 = statementMap.get(propertyName)) != null) {
                System.out.println("########################" + propertyName);
                Object s = jp.readValueAs(cl2);
                if (beanOrClass instanceof Block) {
                    Block b = (Block) beanOrClass;
                    System.out.println("Bock context " +b.type + " " + s);
                    b.getBody().add((BlocklyContents) s);
                } else if (beanOrClass instanceof Value) {
                    Value v=(Value) beanOrClass;
                    System.out.println("Value context " +v.getName() + " " + s);
                    v.getBlock().add((Block) s);
                } else if (beanOrClass instanceof Root) {
                    Root v=(Root) beanOrClass;
                    System.out.println("Root context " + " " + s);
                    v.getBlock().add((Block) s);
                }
                return true;
            }

            return super.handleUnknownProperty(ctxt, jp, deserializer, beanOrClass, propertyName);
        }
    }

}
