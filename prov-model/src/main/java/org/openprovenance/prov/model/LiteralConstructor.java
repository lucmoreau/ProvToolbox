package org.openprovenance.prov.model;

public interface LiteralConstructor {

    //Object newQName(String value);

    Object newGYear(int value);

    Object newGMonth(int value);

    Object newGDay(int value);
    
    Object newGMonthDay(int month, int day);


    Object newISOTime(String value);

    Object newDuration(String value);

     byte [] base64Decoding(String s);

    Object hexDecoding(String value);

}
