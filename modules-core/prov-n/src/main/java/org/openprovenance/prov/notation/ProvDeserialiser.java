package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.InputStream;
import java.util.TimeZone;

/**
 * Deserialises a PROV-N document, with the ANTLR 4 parser ({@link ProvNReader}).
 * <p>
 * Since 2.4.0, the ANTLR 3 parser, which 2.3.0 and earlier used, is no longer used; it differs as follows:
 * <ul>
 *     <li>a document that is not valid PROV-N is rejected with a {@link ProvNSyntaxException}, listing all errors,
 *     where the ANTLR 3 parser recovered from errors, and only logged them;</li>
 *     <li>nothing but whitespace and comments may follow endDocument;</li>
 *     <li>wasRevisionOf, wasQuotedFrom and hadPrimarySource, absent from the PROV-N Recommendation, are not keywords;</li>
 *     <li>statements that the ANTLR 3 parser failed to read are read: an extension right after the namespace
 *     declarations, a provext statement, wasAssociatedWith without agent and plan, dictionary keys without datatype.</li>
 * </ul>
 * The input is decoded as UTF-8. See ProvN.g4 for the grammar, and its notes on the choices made.
 */
public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {
    private final ProvNReader reader;

    public ProvDeserialiser(ProvFactory pFactory) {
        this(pFactory, DateTimeOption.PRESERVE, null);
    }

    public ProvDeserialiser(ProvFactory pFactory, DateTimeOption dateTimeOption) {
        this(pFactory, dateTimeOption, null);
    }

    public ProvDeserialiser(ProvFactory pFactory, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.reader = new ProvNReader(pFactory, dateTimeOption, optionalTimeZone);
    }

    /**
     * Deserialises a document from a stream.
     *
     * @param in an {@link InputStream}, holding a single PROV-N document
     * @return org.openprovenance.prov.model.Document
     * @throws ProvNSyntaxException if the stream does not hold a syntactically valid PROV-N document
     */
    @Override
    public Document deserialiseDocument(InputStream in) {
        return reader.readDocument(in);
    }
}
