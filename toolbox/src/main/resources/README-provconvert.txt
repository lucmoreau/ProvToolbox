
provconvert
--------

An executable to:
- convert PROV representations from ASN into RDF Turtle, XML, and ASN
- convert PROV representations from XML into RDF Turtle, XML, and ASN



 
USAGE:

     provconvert -asn2rdf fileIn fileOut
     provconvert -asn2xml fileIn fileOut
     provconvert -asn2asn fileIn fileOut
     provconvert -xml2xml fileIn fileOut
     provconvert -xml2asn fileIn fileOut


The arguments are the following:

 fileIn:  the name of a file from which to read the input representation
 fileOut: the name of a file in which to write an output representation




----------------------------------------------------------------------


EXAMPLE

 bin/provconvert -xml2rdf  examples/bad-cake.xml bad-cake2.ttl


----------------------------------------------------------------------

NOTES

- This is purely experimental, and relying on XML schemas, OWL
  ontologies, and ASN grammars that are still evolving.

- The conversions do not support all the PROV terms yet

- There are a number of assumptions underpinning the ASN parser and converter:
   - Entities, agents, activities always have to be declared
     In other words, if one writes 
       wasGeneratedBy(e2,a1)
     there must be
       entity(e2)
       activity(a1)

   - Declarations should occur before use (due to a 1 pass conversion)



