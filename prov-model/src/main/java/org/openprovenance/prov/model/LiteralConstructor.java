package org.openprovenance.prov.model;

public interface LiteralConstructor {

    Object newQName(String value);

    Object newGYear(int value);

    Object newGMonth(int value);

    Object newGDay(int value);
    
    Object newGMonthDay(int month, int day);


    Object newISOTime(String value);

}
