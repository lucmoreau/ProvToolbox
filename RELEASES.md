See also GitHub releases https://github.com/lucmoreau/ProvToolbox/releases

==0.9.20==
* prov-tempalte-compiler:  allow for collections of level0 application specific types

==0.9.19==
* further upgrade to log4j 2.17.0 (following log4shell vulnerability)
* prov-template-compiler: allows for level0 type to be added to all levels (option -addLevel0)
* prov-tempalte-compiler: allows for domain specific types to be added from properties (option -propertyConverters)


==0.9.18==
* further upgrade to log4j 2.16.0 (following log4shell vulnerability)
* prov-template-compiler: -levelNumber option to determine type inference level
* prov-template-compiler: traversal of further relations (specialization and membership)


==0.9.17==
* upgraded to log4j 2.15.0 (following log4shell vulnerability)
* prov-template-compiler: support for type inference (variation of provenance kernel approach for templates)


==0.9.16==
* prov-n: support for qualified specialization/alternate/membership in prov-n parser with provext: namespace prefix
* prov-template-compiler: conditional link tmpl:if='var:xxx' (support for hadMember)
* prov-template-compiler: fixed script generation (class path definition)
* removed dependency CamelCase (apache common text instead of google guava, as it was already present)

==0.9.15==
* support for qualified specialization/alternate/membership in prov-json
* prov-template-compiler: conditional link tmpl:if='var:xxx' (currently for invalidation only)

==0.9.5==
* TODO: changelog

==0.9.4==
* TODO: changelog

==0.9.3==
* TODO: changelog

==0.9.2==
* multi-threading issue with jedis

==0.9.1==
* prov-model now contains a vanilla implementation of prov interfaces
* new serialization (using jackson) to prov-json, prov-jsonld, prov-xml
* prov-model-scala: alternate implementation of the prov data model in scala
* refactoring of translator/template expansion service
* storage component (used by service) to store prov documents in filesystem or mongodb
* indexing of documents: in memory or using redis


==0.8.0 ==

* port to JDK 12
* restructuring of prov-template
* preliminary compiler of template to java code
* fixed some javadoc comments
* prov-json: closing buffers when reading files
* prov-xml: changed QualifiedNameUtils instance to be statically declared in QualifiedName
* prov-n, prov-xml, prov-json: Escaping in qualified names: a couple of corner cases handled, and in particular, after gaining a better understanding of qualified name in prov-json
* prov-n, prov-xml, prov-json: Escaping in strings.

==0.7.3==

* prov-model: in extension namespace, support for Qualified versions of alternate, specialization, membership, to allow summariser to annotate these relations with weight information. Serialization to xml and provn is work in progress, not supported in prov-json and prov-sql yet. No parsing yet.
* prov-template: support for JSON bindings, bindings bean generator, new tutorials partial template extension. Support functionality in paper submission.
* bug fixes
  ** #149: dependency update
  ** printing to html

==0.7.2==

* prov-json: fixed prov:InternationalizedString and refactoring
* test suite generation for SSI interoperability harness
* fixed bug with rdf:XMLLiteral
* prov-template: option to filter out statements in which (non-attributes) variables have not been expanded
* prov-template: added return code for unexpanded variables (when using -allpanded option)
* changed name of artifact: toolbox -> provconvert
* installer for `provconvert` on macosx

==0.7.1==

* Debian package .deb binary release
* prov-dot: update of visualization to support summaries size of nodes/edges
* prov-dot: updated provtoolbox namespace
* prov-xml: updated sql persistence, xml local name encoding

==0.7.0==

* comparator function exposed in provconvert
* better support for syntax checking of qualified names and QNames
* made the xsd schema URI as known by RDF (with a hash) the default namespace
* tests for unicode and internationalization
* prov-dot: support for visualization of some prov attributes
* provconvert -compare and -outcompare options
* fixed issues with prov:QUALIFIED_NAME, and remove QNames except for xml import/export
* tutorial 4
* bug fixes
  ** prov:InternationalizedString https://github.com/lucmoreau/ProvToolbox/issues/133
  ** incorrect prefix declaration in export https://github.com/lucmoreau/ProvToolbox/issues/132
  ** parsing relative uris with input stream in ttl https://github.com/lucmoreau/ProvToolbox/issues/122
  ** prov-n qualified names written as "ex:foo" %% prov:QUALIFIED_NAME https://github.com/lucmoreau/ProvToolbox/issues/109
  ** warning for prov:label non-string value https://github.com/lucmoreau/ProvToolbox/issues/104
  ** escaping of characters in Qualifed Names and QNames https://github.com/lucmoreau/ProvToolbox/issues/120
  ** visualisation of prov:value  https://github.com/lucmoreau/ProvToolbox/issues/71
  ** conversion to dot https://github.com/lucmoreau/ProvToolbox/issues/67

==0.6.2==
* tutorial2 and tutorial3
* toolbox: provconvert -version option now showing version!
* toolbox: provconvert now supports merging (and optional flattening) of multiple documents, with options -merge (and -flatten)
* toolbox: provconvert use - as a filename to use unix-style stdio (use in conjunction with specifying format)
* toolbox: provconvert option to specify format of input/output files without relying on extension
* toolbox: provconvert supports indexing and flattening of Document (options: -index -flatten)
* toolbox: now rpm generation
* toolbox: provconvert now supports conversion to png,jpeg,pdf
* toolbox: provconvert -formats reports supported formats
* prov-model: introduced IndexedDocument as a mechanism to index the content of a document (testing in prov-xml)
* prov-n: issue #112: adding newline at the end of provn file.
* prov-dot: misc bug fixing in ProvToDot and minor improvements
* prov-json: fixed bug in json parser for bundle id and namespaces
* prov-template: properly computes transitive closure for link relation
* prov-template: test for tmpl:time, etc
* prov-template: by default, prov-template no longer adds tmpl:order attributes in expanded document. Introduced -genorder option in provconvert
* prov-xml: introduced non-standard convention to serialize local names starting with digit tmpl:2dvalue -> tmpl:_2dvalue
* refreshed some maven dependencies

==0.6.1==

* support for maven central (Sonatype OSS)
* updated tutorial (removed repository declaration)
* removed dependency on jaxb2_common
* renamed NamedBundle to Bundle (fairly unused Bundle was renamed to BundleEntity)

==0.6.0==

* First tutorial
* Fixed collection of issues #61, #86, #88, #89, #90, #91, #94, #95, 96, #99, #101.
* Cleaned up dependency management
* Embedded jvnet.Equals/ToString interfaces and implementation in prov-xml to avoid nasty artifact class.
* Fixed a bug with xsd namespace when parsing rdf.
* Made sure exceptions in Interop Framework were properly chained.
* Integrated Jamal's random generator code. Can be called from command line with -generator option
* First version of prov-template
* Support for interoperability across representation: cfr writeDocument, readDocument, and content negotiation support in prov-convert
* Extensive work on prov-sql, in particular roundtrip testing
* Testing through validator and translator at https://provenance.ecs.soton.ac.uk/validator/


==0.5.0==

* Continued refactorization of Beans
  ** Introduced class QualifiedName
  ** All beans in prov-model now refer to QualifiedName (no reference to IDRef anymore)
* New "visitor" interface StatementAction (and StatementActionValue) replacing RecordAction( and Record Value)
* Chaining of Namespace objects to support nesting of bundle inside document
* Reimplemented utility getter/setter without reflection. Moved them to prov-model
* Replaced old 'visitor' interface (RecordAction) by new one (StatementAction)
* prov-n/prov-json/prov-rdf/prov-dot depend only of prov-model
* Documentation: see http://openprovenance.org/java/site/0_5_0/apidocs/
* Systematic Testing of Keys in dictionary insertions
* Updated JSON Schema (thanks Dong!) to support compact version of dictionaries

==0.4.0==
* Extensive refactorization of Beans
  ** moved away from default jaxb serialization of attributes since it didn't handle qnames properly. Custom serializer used instead.
  ** Introduced explicit classes for all attributes Location, Role, Value, Type, Other
  ** Changed the beans to handle serializations of attributes: it now relies on a sorted list of all attributes.
  ** extensive testing of attributes
  ** support for rdf 1.1 data types
  ** beans no longer generated automatically
  ** removed getAny() replaced by getOther()
  ** Defined method getKind on all beans

* Bug fixes
  ** Support for multi-line strings in provn
  ** issue #82
  ** issue #83
  ** issue #81

* Namespace management
  ** Introduced Namespace class for management of namespaces
  ** Defined function to collect all namespaces (and associated prefixes) in a document

* Introduced a class DOMProcessing grouping all dom-related processing
* Defined enumerated type for all types of statements
* Defined new interface StatementAction to define statement specific processing. Classes implementing this interfaces can be  used by ProvUtilities to operate on any statement.
* prov-sql (preliminary release)
  ** Added prov-sql package, an ORM mapping generated by JAXB.
  ** Currently, no support for dictionary, and extensions.
  ** Feedback welcome on the SQL Schema and mapping.



==0.3.0==
* significant refactoring in view of ORM based persistence
* introduced package prov-model specifying the interface of beans, and core factory functionality
* prov-xml (and future prov-sql) to implement these interfaces
* redefined the interface to location/value/role/typ. Instead of returning arbitrary objects, they return instances of Location/Value/Role/Type, all subclasses of TypedValue
* minimized patching of auto-generated beans by configuring bindings.xjc
* nice accessors to statements in bundles/documents
* better alignment with w3c schema (though no support for extensions)
* using IDRef instead of XXRef

==0.2.3==

* display of bundles in prov-dot
* title option in prov-dot

==0.2.2==

* aligned schema for prov-dictionary

==0.2.1==

* bug fix

==0.2.0==

* support for latest prov-xml schema
* support for prov-dictionary in Java, prov-n, prov-json, and prov-xml
* conversion of prov-dictionary from Java to rdf
* Various bug fixes

==0.1.3==

* in prov-convert, support for namespace prefix declaration
* in prov-convert, pdf output
* prov-dot improvements

==0.1.2 (CR implementation release)==

* support for ProvValidator http://provenance.ecs.soton.ac.uk/

==0.1.1 (Xmas release)==

* Refactor converters to/from Rdf
* Aligned xml schema for specialization/alternate
* Membership support in rdf


==0.1.0==

* Refactored converters
* Deprecated old traversal classes. They will be deleted in next release.

`0.3.0`

* Version included in prov-translator service, launched on Dec 17th