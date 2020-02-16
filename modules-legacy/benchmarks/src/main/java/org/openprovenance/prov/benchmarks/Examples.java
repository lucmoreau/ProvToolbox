package org.openprovenance.prov.benchmarks;

public final class Examples {

    static String provjsonldPrimer ="{\n" +
            "  \"@context\" : [ {\n" +
            "    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",\n" +
            "    \"dcterms\" : \"http://purl.org/dc/terms/\",\n" +
            "    \"ex\" : \"http://example/\",\n" +
            "    \"prov\" : \"http://www.w3.org/ns/prov#\",\n" +
            "    \"foaf\" : \"http://xmlns.com/foaf/0.1/\"\n" +
            "  }, \"http://openprovenance.org/prov-json.ld\" ],\n" +
            "  \"@graph\" : [ {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:article\",\n" +
            "    \"dcterms:title\" : [ {\n" +
            "      \"@value\" : \"Crime rises in cities\"\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:articleV1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:articleV2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:dataSet2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:regionList\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:composition\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:chart1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:chart2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Entity\",\n" +
            "    \"@id\" : \"ex:blogEntry\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:compile\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:compile2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:compose\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:correct\",\n" +
            "    \"startTime\" : \"2012-03-31T09:21:00.000+01:00\",\n" +
            "    \"endTime\" : \"2012-04-01T15:21:00.000+01:00\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Activity\",\n" +
            "    \"@id\" : \"ex:illustrate\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:regionList\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:composition\",\n" +
            "    \"activity\" : \"ex:compose\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:illustrate\",\n" +
            "    \"entity\" : \"ex:composition\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:chart1\",\n" +
            "    \"activity\" : \"ex:illustrate\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:chart1\",\n" +
            "    \"activity\" : \"ex:compile\",\n" +
            "    \"time\" : \"2012-03-02T10:30:00.000Z\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:chart2\",\n" +
            "    \"activity\" : \"ex:compile2\",\n" +
            "    \"time\" : \"2012-04-01T15:21:00.000+01:00\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Agent\",\n" +
            "    \"@id\" : \"ex:derek\",\n" +
            "    \"foaf:mbox\" : [ {\n" +
            "      \"@value\" : \"<mailto:derek@example.org>\"\n" +
            "    } ],\n" +
            "    \"prov:type\" : [ \"prov:Person\" ],\n" +
            "    \"foaf:givenName\" : [ {\n" +
            "      \"@value\" : \"Derek\"\n" +
            "    } ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Association\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"agent\" : \"ex:derek\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Association\",\n" +
            "    \"activity\" : \"ex:illustrate\",\n" +
            "    \"agent\" : \"ex:derek\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Agent\",\n" +
            "    \"@id\" : \"ex:chartgen\",\n" +
            "    \"foaf:name\" : [ {\n" +
            "      \"@value\" : \"Chart Generators Inc\"\n" +
            "    } ],\n" +
            "    \"prov:type\" : [ \"prov:Organization\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Delegation\",\n" +
            "    \"delegate\" : \"ex:derek\",\n" +
            "    \"responsible\" : \"ex:chartgen\",\n" +
            "    \"activity\" : \"ex:compose\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Attribution\",\n" +
            "    \"entity\" : \"ex:chart1\",\n" +
            "    \"agent\" : \"ex:derek\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:dataSet1\",\n" +
            "    \"prov:role\" : [ \"ex:dataToCompose\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:compose\",\n" +
            "    \"entity\" : \"ex:regionList\",\n" +
            "    \"prov:role\" : [ \"ex:regionsToAggregateBy\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Generation\",\n" +
            "    \"entity\" : \"ex:dataSet2\",\n" +
            "    \"activity\" : \"ex:correct\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Usage\",\n" +
            "    \"activity\" : \"ex:correct\",\n" +
            "    \"entity\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:dataSet2\",\n" +
            "    \"usedEntity\" : \"ex:dataSet1\",\n" +
            "    \"prov:type\" : [ \"prov:Revision\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:chart2\",\n" +
            "    \"usedEntity\" : \"ex:dataSet2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:blogEntry\",\n" +
            "    \"usedEntity\" : \"ex:article\",\n" +
            "    \"prov:type\" : [ \"prov:Quotation\" ]\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Specialization\",\n" +
            "    \"specificEntity\" : \"ex:articleV1\",\n" +
            "    \"generalEntity\" : \"ex:article\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:articleV1\",\n" +
            "    \"usedEntity\" : \"ex:dataSet1\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Specialization\",\n" +
            "    \"specificEntity\" : \"ex:articleV2\",\n" +
            "    \"generalEntity\" : \"ex:article\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Derivation\",\n" +
            "    \"generatedEntity\" : \"ex:articleV2\",\n" +
            "    \"usedEntity\" : \"ex:dataSet2\"\n" +
            "  }, {\n" +
            "    \"@type\" : \"prov:Alternate\",\n" +
            "    \"alternate1\" : \"ex:articleV2\",\n" +
            "    \"alternate2\" : \"ex:articleV1\"\n" +
            "  } ]\n" +
            "}";

    static String provnPrimer="\n" +
            "\n" +
            "document\n" +
            "\n" +
            "   prefix ex <http://example/>\n" +
            "   prefix dcterms <http://purl.org/dc/terms/>\n" +
            "   prefix foaf <http://xmlns.com/foaf/0.1/>\n" +
            "\n" +
            "\n" +
            "   entity(ex:article, [dcterms:title=\"Crime rises in cities\"])\n" +
            "   entity(ex:articleV1)\n" +
            "   entity(ex:articleV2)\n" +
            "   entity(ex:dataSet1)\n" +
            "   entity(ex:dataSet2)\n" +
            "   entity(ex:regionList)\n" +
            "   entity(ex:composition)\n" +
            "   entity(ex:chart1)\n" +
            "   entity(ex:chart2)\n" +
            "   entity(ex:blogEntry)\n" +
            "\n" +
            "\n" +
            "   activity(ex:compile)\n" +
            "   activity(ex:compile2)\n" +
            "   activity(ex:compose)\n" +
            "   activity(ex:correct, 2012-03-31T09:21:00, 2012-04-01T15:21:00)\n" +
            "   activity(ex:illustrate)\n" +
            "\n" +
            "\n" +
            "   used(ex:compose, ex:dataSet1, -)\n" +
            "   used(ex:compose, ex:regionList, -)\n" +
            "   wasGeneratedBy(ex:composition, ex:compose, -)\n" +
            "\n" +
            "   used(ex:illustrate, ex:composition, -)\n" +
            "   wasGeneratedBy(ex:chart1, ex:illustrate, -)\n" +
            "\n" +
            "   wasGeneratedBy(ex:chart1, ex:compile,  2012-03-02T10:30:00)\n" +
            "   wasGeneratedBy(ex:chart2, ex:compile2, 2012-04-01T15:21:00)\n" +
            "    \n" +
            "\n" +
            "   agent(ex:derek, [ prov:type='prov:Person', foaf:givenName = \"Derek\", \n" +
            "          foaf:mbox= \"<mailto:derek@example.org>\"])\n" +
            "   wasAssociatedWith(ex:compose, ex:derek, -)\n" +
            "   wasAssociatedWith(ex:illustrate, ex:derek, -)\n" +
            "\n" +
            "   agent(ex:chartgen, [ prov:type='prov:Organization',\n" +
            "          foaf:name = \"Chart Generators Inc\"])\n" +
            "   actedOnBehalfOf(ex:derek, ex:chartgen, ex:compose)\n" +
            "\n" +
            "   wasAttributedTo(ex:chart1, ex:derek)\n" +
            "\n" +
            "\n" +
            "   used(ex:compose, ex:dataSet1, -,   [ prov:role = 'ex:dataToCompose'])\n" +
            "   used(ex:compose, ex:regionList, -, [ prov:role = 'ex:regionsToAggregateBy'])\n" +
            "\n" +
            "   wasGeneratedBy(ex:dataSet2, ex:correct, -)\n" +
            "   used(ex:correct, ex:dataSet1, -)\n" +
            "   wasDerivedFrom(ex:dataSet2, ex:dataSet1, [prov:type='prov:Revision'])\n" +
            "   wasDerivedFrom(ex:chart2, ex:dataSet2)\n" +
            "\n" +
            "   wasDerivedFrom(ex:blogEntry, ex:article, [prov:type='prov:Quotation'])\n" +
            "   specializationOf(ex:articleV1, ex:article)\n" +
            "   wasDerivedFrom(ex:articleV1, ex:dataSet1)\n" +
            "\n" +
            "   specializationOf(ex:articleV2, ex:article)\n" +
            "   wasDerivedFrom(ex:articleV2, ex:dataSet2)\n" +
            "\n" +
            "   alternateOf(ex:articleV2, ex:articleV1)\n" +
            "\n" +
            "\n" +
            "endDocument\n" +
            "\n";

    public static String pjsonPrimer="{\n" +
            "  \"prefix\" : {\n" +
            "    \"foaf\" : \"http://xmlns.com/foaf/0.1/\",\n" +
            "    \"xsd\" : \"http://www.w3.org/2001/XMLSchema#\",\n" +
            "    \"_\" : \"https://openprovenance.org/blank#\",\n" +
            "    \"dcterms\" : \"http://purl.org/dc/terms/\",\n" +
            "    \"ex\" : \"http://example/\",\n" +
            "    \"prov\" : \"http://www.w3.org/ns/prov#\"\n" +
            "  },\n" +
            "  \"entity\" : {\n" +
            "    \"ex:chart1\" : { },\n" +
            "    \"ex:regionList\" : { },\n" +
            "    \"ex:composition\" : { },\n" +
            "    \"ex:chart2\" : { },\n" +
            "    \"ex:article\" : {\n" +
            "      \"dcterms:title\" : [ \"Crime rises in cities\" ]\n" +
            "    },\n" +
            "    \"ex:articleV1\" : { },\n" +
            "    \"ex:dataSet2\" : { },\n" +
            "    \"ex:blogEntry\" : { },\n" +
            "    \"ex:articleV2\" : { },\n" +
            "    \"ex:dataSet1\" : { }\n" +
            "  },\n" +
            "  \"activity\" : {\n" +
            "    \"ex:compile\" : { },\n" +
            "    \"ex:correct\" : {\n" +
            "      \"prov:startTime\" : \"2012-03-31T09:21:00.000+01:00\",\n" +
            "      \"prov:endTime\" : \"2012-04-01T15:21:00.000+01:00\"\n" +
            "    },\n" +
            "    \"ex:compose\" : { },\n" +
            "    \"ex:illustrate\" : { },\n" +
            "    \"ex:compile2\" : { }\n" +
            "  },\n" +
            "  \"agent\" : {\n" +
            "    \"ex:derek\" : {\n" +
            "      \"foaf:mbox\" : [ \"<mailto:derek@example.org>\" ],\n" +
            "      \"prov:type\" : [ {\n" +
            "        \"type\" : \"prov:QUALIFIED_NAME\",\n" +
            "        \"$\" : \"prov:Person\"\n" +
            "      } ],\n" +
            "      \"foaf:givenName\" : [ \"Derek\" ]\n" +
            "    },\n" +
            "    \"ex:chartgen\" : {\n" +
            "      \"foaf:name\" : [ \"Chart Generators Inc\" ],\n" +
            "      \"prov:type\" : [ {\n" +
            "        \"type\" : \"prov:QUALIFIED_NAME\",\n" +
            "        \"$\" : \"prov:Organization\"\n" +
            "      } ]\n" +
            "    }\n" +
            "  },\n" +
            "  \"used\" : {\n" +
            "    \"_:n11\" : {\n" +
            "      \"prov:activity\" : \"ex:compose\",\n" +
            "      \"prov:entity\" : \"ex:dataSet1\",\n" +
            "      \"prov:role\" : [ {\n" +
            "        \"type\" : \"prov:QUALIFIED_NAME\",\n" +
            "        \"$\" : \"ex:dataToCompose\"\n" +
            "      } ]\n" +
            "    },\n" +
            "    \"_:n12\" : {\n" +
            "      \"prov:activity\" : \"ex:compose\",\n" +
            "      \"prov:entity\" : \"ex:regionList\",\n" +
            "      \"prov:role\" : [ {\n" +
            "        \"type\" : \"prov:QUALIFIED_NAME\",\n" +
            "        \"$\" : \"ex:regionsToAggregateBy\"\n" +
            "      } ]\n" +
            "    },\n" +
            "    \"_:n14\" : {\n" +
            "      \"prov:activity\" : \"ex:correct\",\n" +
            "      \"prov:entity\" : \"ex:dataSet1\"\n" +
            "    },\n" +
            "    \"_:n1\" : {\n" +
            "      \"prov:activity\" : \"ex:compose\",\n" +
            "      \"prov:entity\" : \"ex:regionList\"\n" +
            "    },\n" +
            "    \"_:n0\" : {\n" +
            "      \"prov:activity\" : \"ex:compose\",\n" +
            "      \"prov:entity\" : \"ex:dataSet1\"\n" +
            "    },\n" +
            "    \"_:n3\" : {\n" +
            "      \"prov:activity\" : \"ex:illustrate\",\n" +
            "      \"prov:entity\" : \"ex:composition\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"wasAssociatedWith\" : {\n" +
            "    \"_:n7\" : {\n" +
            "      \"prov:activity\" : \"ex:compose\",\n" +
            "      \"prov:agent\" : \"ex:derek\"\n" +
            "    },\n" +
            "    \"_:n8\" : {\n" +
            "      \"prov:activity\" : \"ex:illustrate\",\n" +
            "      \"prov:agent\" : \"ex:derek\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"wasAttributedTo\" : {\n" +
            "    \"_:n10\" : {\n" +
            "      \"prov:entity\" : \"ex:chart1\",\n" +
            "      \"prov:agent\" : \"ex:derek\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"actedOnBehalfOf\" : {\n" +
            "    \"_:n9\" : {\n" +
            "      \"prov:delegate\" : \"ex:derek\",\n" +
            "      \"prov:responsible\" : \"ex:chartgen\",\n" +
            "      \"prov:activity\" : \"ex:compose\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"alternateOf\" : {\n" +
            "    \"_:n22\" : {\n" +
            "      \"prov:alternate1\" : \"ex:articleV2\",\n" +
            "      \"prov:alternate2\" : \"ex:articleV1\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"specializationOf\" : {\n" +
            "    \"_:n20\" : {\n" +
            "      \"prov:specificEntity\" : \"ex:articleV2\",\n" +
            "      \"prov:generalEntity\" : \"ex:article\"\n" +
            "    },\n" +
            "    \"_:n18\" : {\n" +
            "      \"prov:specificEntity\" : \"ex:articleV1\",\n" +
            "      \"prov:generalEntity\" : \"ex:article\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"wasDerivedFrom\" : {\n" +
            "    \"_:n21\" : {\n" +
            "      \"prov:generatedEntity\" : \"ex:articleV2\",\n" +
            "      \"prov:usedEntity\" : \"ex:dataSet2\"\n" +
            "    },\n" +
            "    \"_:n15\" : {\n" +
            "      \"prov:generatedEntity\" : \"ex:dataSet2\",\n" +
            "      \"prov:usedEntity\" : \"ex:dataSet1\",\n" +
            "      \"prov:type\" : [ {\n" +
            "        \"type\" : \"prov:QUALIFIED_NAME\",\n" +
            "        \"$\" : \"prov:Revision\"\n" +
            "      } ]\n" +
            "    },\n" +
            "    \"_:n16\" : {\n" +
            "      \"prov:generatedEntity\" : \"ex:chart2\",\n" +
            "      \"prov:usedEntity\" : \"ex:dataSet2\"\n" +
            "    },\n" +
            "    \"_:n17\" : {\n" +
            "      \"prov:generatedEntity\" : \"ex:blogEntry\",\n" +
            "      \"prov:usedEntity\" : \"ex:article\",\n" +
            "      \"prov:type\" : [ {\n" +
            "        \"type\" : \"prov:QUALIFIED_NAME\",\n" +
            "        \"$\" : \"prov:Quotation\"\n" +
            "      } ]\n" +
            "    },\n" +
            "    \"_:n19\" : {\n" +
            "      \"prov:generatedEntity\" : \"ex:articleV1\",\n" +
            "      \"prov:usedEntity\" : \"ex:dataSet1\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"wasGeneratedBy\" : {\n" +
            "    \"_:n6\" : {\n" +
            "      \"prov:entity\" : \"ex:chart2\",\n" +
            "      \"prov:activity\" : \"ex:compile2\",\n" +
            "      \"prov:time\" : \"2012-04-01T15:21:00.000+01:00\"\n" +
            "    },\n" +
            "    \"_:n13\" : {\n" +
            "      \"prov:entity\" : \"ex:dataSet2\",\n" +
            "      \"prov:activity\" : \"ex:correct\"\n" +
            "    },\n" +
            "    \"_:n2\" : {\n" +
            "      \"prov:entity\" : \"ex:composition\",\n" +
            "      \"prov:activity\" : \"ex:compose\"\n" +
            "    },\n" +
            "    \"_:n5\" : {\n" +
            "      \"prov:entity\" : \"ex:chart1\",\n" +
            "      \"prov:activity\" : \"ex:compile\",\n" +
            "      \"prov:time\" : \"2012-03-02T10:30:00.000Z\"\n" +
            "    },\n" +
            "    \"_:n4\" : {\n" +
            "      \"prov:entity\" : \"ex:chart1\",\n" +
            "      \"prov:activity\" : \"ex:illustrate\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
