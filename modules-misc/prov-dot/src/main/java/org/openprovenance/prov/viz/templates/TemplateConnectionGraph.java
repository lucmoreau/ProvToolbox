package org.openprovenance.prov.viz.templates;

import java.util.*;

/**
 * What a template-connection drawing needs, free of any service or database: the connections
 * between template records, and the tables describing the templates' ports.
 */
public class TemplateConnectionGraph {

    /** A connection: the {@code in} record's property refers to the {@code out} record's property. */
    public static class Connection {
        public Integer inId;
        public String inTemplate;
        public String inProperty;
        public String inType;
        public Integer outId;
        public String outTemplate;
        public String outProperty;
        public String outType;

        public static Connection of(String inTemplate, Integer inId, String inProperty, String inType,
                                    String outTemplate, Integer outId, String outProperty, String outType) {
            Connection c = new Connection();
            c.inTemplate = inTemplate;
            c.inId = inId;
            c.inProperty = inProperty;
            c.inType = inType;
            c.outTemplate = outTemplate;
            c.outId = outId;
            c.outProperty = outProperty;
            c.outType = outType;
            return c;
        }

        @Override
        public String toString() {
            return "Connection{" + inTemplate + "/" + inId + "." + inProperty + " -> " + outTemplate + "/" + outId + "." + outProperty + "}";
        }
    }

    public final List<Connection> connections = new ArrayList<>();
    /** Fully qualified template name → short name (the table name). */
    public final Map<String, String> shortNames = new HashMap<>();
    /** Short template name → input property → its type, as declared by the templates. */
    public final Map<String, Map<String, String>> inputs = new HashMap<>();
    /** Short template name → output property → its type. */
    public final Map<String, Map<String, String>> outputs = new HashMap<>();
    /** Fully qualified template name → property → PROV type URI (entity, activity, agent). */
    public final Map<String, Map<String, String>> baseTypes = new HashMap<>();
    /** Fully qualified template name → property → the properties that follow it, for the entities style. */
    public final Map<String, Map<String, List<String>>> selectedSuccessors = new HashMap<>();

    /** The record the drawing was ASKED for, drawn even when no connection mentions it; null when the caller did not say. */
    public String startTemplate;
    public Integer startTemplateId;
    public String startSemanticType;

    /** Base of the URLs nodes and ports link to. */
    public String provAPI;
}
