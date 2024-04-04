package org.openprovenance.prov.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.csv.CSVParser;

import java.util.List;
import java.util.Map;

public class JsonOrCsv {

    @JsonIgnore
    CSVParser csv;
    @JsonIgnore
    List<Map<String,Object>> json;
}
