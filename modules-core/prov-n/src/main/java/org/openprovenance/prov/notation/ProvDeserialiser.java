package org.openprovenance.prov.notation;

import org.openprovenance.prov.model.DateTimeOption;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.ProvFactory;

import java.io.InputStream;
import java.util.TimeZone;

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
    public Document deserialiseDocument(InputStream in){
        return u.readDocument(in,pFactory) ;
    }
}
