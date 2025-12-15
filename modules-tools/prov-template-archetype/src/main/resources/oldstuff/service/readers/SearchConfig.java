#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package org.openprovenance.prov.service.readers;

public class SearchConfig {
    public String base_relation;
    public String from_date;
    public String to_date;
    public Integer limit;

    @Override
    public String toString() {
        return "SearchConfig{" +
                "base_relation='" + base_relation + '${symbol_escape}'' +
                ", from_date='" + from_date + '${symbol_escape}'' +
                ", to_date='" + to_date + '${symbol_escape}'' +
                ", limit=" + limit +
                '}';
    }
}