
opm2dot
--------

An executable to convert OPM graphs into dot files, and from dot files
into pdf format. The latter conversion requires the Graphviz executable
`dot' to be in the PATH.

 
USAGE:
  opm2dot opmFile.xml out.dot out.pdf [configuration.xml]

The arguments are the following:

 opmFile.xml: an XML representation (version ${project.version}) of an
              OPM graph

 out.dot: the name of the dot file to be produced by this utility

 out.pdf: the name of the pdf file to be produced by this utility
          (Note, to successfully be produced, the dot executable should
           be in the class path)

 configuration.xml: the optional configuration file to parameterize
                    the conversion process

----------------------------------------------------------------------


EXAMPLES

 bin/opm2dot examples/bad-cake.xml bad-cake.dot bad-cake.pdf
 bin/opm2dot examples/bad-cake.xml bad-cake-red.dot bad-cake-red.pdf examples/redConfig.xml
 bin/opm2dot examples/bad-cake.xml bad-cake-black.dot bad-cake-black.pdf examples/blackConfig.xml


