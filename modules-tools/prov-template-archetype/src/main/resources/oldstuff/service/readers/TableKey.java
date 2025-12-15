#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package org.openprovenance.prov.service.readers;

public class TableKey {
    public String isA;
    public Integer ID;

    @Override
    public String toString() {
        return "TableKey{" +
                "isA='" + isA + '${symbol_escape}'' +
                ", ID=" + ID +
                '}';
    }
}
