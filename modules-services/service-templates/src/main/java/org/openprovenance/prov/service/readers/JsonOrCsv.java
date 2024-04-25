package org.openprovenance.prov.service.readers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.csv.CSVParser;

import java.util.List;
import java.util.Map;

public class JsonOrCsv {

    @JsonIgnore
    public CSVParser csv;
    @JsonIgnore
    public List<Map<String,Object>> json;
}
