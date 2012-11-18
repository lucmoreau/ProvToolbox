
provconvert
--------

An executable to convert between PROV representations.


USAGE

 prov-convert [-namespaces file] [-infile file] [-verbose]
       [-version] [-debug] [-help] [-logfile file] [-outfile file]
 -debug               print debugging information
 -help                print this message
 -infile <file>       use given file as input
 -logfile <file>      use given file for log
 -namespaces <file>   use given file as declaration of prefix namespaces
 -outfile <file>      use given file as output
 -verbose             be verbose
 -version             print the version information and exit


RECOGNIZED FILE EXTENSIONS

 - prov-n notation:   .provn
 - prov-o ttl:        .ttl
 - prov-xml:          .provx or .xml
 - pdf:               .pdf
 - svg:               .svg


EXAMPLE

  provconvert -infile  ../prov-xml/target/pc1-full.xml -outfile target/pc1-full.pdf



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



