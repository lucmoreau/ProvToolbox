
print('*** Hello ProvToolbox!');

var doc = builder
    .entity()
    .id(defs.XID,"b34/5").aka()
    .build()

    .agent()
    .id(defs.XID, "ag401").aka()
    .type(name.PROV_PERSON)
    .attr(defs.foaf_name, "Alice")
    .build()

    .wasAttributedTo()
    .entity("b34/5")
    .agent("ag401")
    .build()


    .build();


serializer.serialize(doc, 'target/doc.jsonld');

