
provconvert
--------

An executable to:
- convert PROV representations represented as ASN into RDF Turtle, 
- convert PROV representations represented as XML into RDF Turtl, 

The program uses Sesame's elmo to create or naviguate the RDF representation of PROV graphs.

The program assumes that all XML IDs in the XML representation can be
converted into a URI by providing a namespace NS, and vice-versa that
all URIs identifying PROV entities in the RDF representation have a
common namespace.
 
USAGE:

  provconvert -xml2rdf fileIn fileOut NS [yes]
  provconvert -xml2n3  fileIn fileOut NS [yes]
  provconvert -rdf2xml fileIn fileOut NS [gid]



The arguments are the following:

 fileIn:  the name of a file from which to read the input representation
          of an PROV graph
 fileOut: the name of a file in which to write an output representation
          of an PROV graph
 NS: namespace



----------------------------------------------------------------------


EXAMPLE

 bin/provconvert -rdf2xml examples/bad-cake.rdf bad-cake2.xml
 bin/provconvert -xml2n3  examples/bad-cake.xml bad-cake2.n3 http://example.com/



