
print('*** Hello ProvToolbox!');

var doc = builder
    .entity()
    .id(defs.XID,"e1")
    .build()

    .agent()
    .id(defs.XID, "bob").aka()
    .type(name.PROV_PERSON)
    .attr(defs.foaf_name, "Bob")
    .build()


    .build();


serializer.serialize(doc, 'target/doc.jsonld');

