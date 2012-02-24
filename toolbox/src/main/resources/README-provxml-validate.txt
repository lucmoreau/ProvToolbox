

opmxml-validate
---------------

An executable to validate OPM graphs represented in the xml format.

 
USAGE:
  opmxml-validate opmFile.xml {schemaFile.xsd}*

The arguments are the following:

 opmFile.xml: an XML representation (version ${project.version}) of an
              OPM graph

 schemaFile.xsd: 0, 1 or more schema files 

----------------------------------------------------------------------


EXAMPLES

 bin/opmxml-validate examples/bad-cake.xml



