package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.InputStream;
import java.util.TimeZone;

/**
 * Deserialises a PROV-N document.
 * <p>
 * It reads with the ANTLR 3 parser (via {@link Utility}), which recovers from syntax errors, logging them. In the
 * release after 2.3.0, it will read with the ANTLR 4 parser ({@link ProvNReader}), which is available now, and which
 * differs as follows:
 * <ul>
 *     <li>a document that is not valid PROV-N is rejected with a {@link ProvNSyntaxException}, listing all errors;</li>
 *     <li>nothing but whitespace and comments may follow endDocument;</li>
 *     <li>wasRevisionOf, wasQuotedFrom and hadPrimarySource, absent from the PROV-N Recommendation, are not keywords;</li>
 *     <li>statements that the ANTLR 3 parser failed to read are read: an extension right after the namespace
 *     declarations, a provext statement, wasAssociatedWith without agent and plan, dictionary keys without datatype.</li>
 * </ul>
 * See ProvN.g4 for the grammar, and its notes on the choices made.
 */
public class ProvDeserialiser implements org.openprovenance.prov.model.ProvDeserialiser {
    private final ProvFactory pFactory;
    private final Utility u;
    private final DateTimeOption dateTimeOption;
    private final TimeZone optionalTimeZone;

    public ProvDeserialiser(ProvFactory pFactory) {
        this.pFactory=pFactory;
        this.dateTimeOption = DateTimeOption.PRESERVE;
        this.optionalTimeZone = null;
        this.u=new Utility(dateTimeOption, optionalTimeZone);
    }
    public ProvDeserialiser(ProvFactory pFactory, DateTimeOption dateTimeOption) {
        this.pFactory=pFactory;
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = null;
        this.u=new Utility(dateTimeOption, optionalTimeZone);
    }
    public ProvDeserialiser(ProvFactory pFactory, DateTimeOption dateTimeOption, TimeZone optionalTimeZone) {
        this.pFactory=pFactory;
        this.dateTimeOption = dateTimeOption;
        this.optionalTimeZone = optionalTimeZone;
        this.u=new Utility(dateTimeOption, optionalTimeZone);
    }

    /**
     * Deerializes a document from a stream
     *
     * @param in an {@link InputStream}
     * @return org.openprovenance.prov.model.Document
     */
    @Override
    @SuppressWarnings("deprecation") // the ANTLR 3 parser, until the release after 2.3.0
    public Document deserialiseDocument(InputStream in){
        return u.readDocument(in,pFactory) ;
    }
}
