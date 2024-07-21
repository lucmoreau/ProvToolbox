
print('*** Hello ProvToolbox!');

var doc = builder
    .entity()
    .id(defs.XID,"e1")
    .build()
    .build();


serializer.serialize(doc, 'target/doc.jsonld');

