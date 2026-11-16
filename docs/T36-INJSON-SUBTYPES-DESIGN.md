# T36 — `@inJson` values and template subtypes: design

Status: **rulings R1–R11 given 2026-11-16 (§8); stages 1–6 built (see `TODO.md`, T36, for what is committed and what is not)**; checked against the code the same day (§11). Ticket: `TODO.md`, T36.
**Part D (§12), the request and the reply classes: proposed 2026-11-16; R12 to R16 ruled by Luc 2026-11-16 (§12.8); built as stage 7 (committed on the T36 branch).**
Grounded in `development2_0` at 0a8d9f4b1. The rulings asked of Luc are numbered R1–R11 in §8 (R11 added by the check); each says what is proposed and, where there is a real
alternative, what it is. Nothing after stage 1 should be built before the rulings it depends on.

Paths are relative to `modules-template/prov-template-compiler/src/main/java/org/openprovenance/prov/template/`
unless they start at the repository root.

## 1. Why

Applications built on a template library use one compiled template for many kinds of act. In nextgen's library, 73
templates serve 213 act types; `document_creating` alone serves 39. What differs from one act type to the next is put
in text variables that hold a JSON object (`attributes`, `pattributes`), because adding a key there changes no table
and no bean. The price is that nothing declares those keys: their names, their types and which act type carries which
are in the code that writes them and the SQL that reads them, and they have drifted.

Luc's requirements, in his words:

- 2026-11-15: "The provenance template compiler can be modified to support new annotations. […] we could introduce
  new annotations, @inJson, indicating which pattributes it gets encoded in, under which variable. These annotations
  would not change the java api to the template or its sql annotation, but it would allows to encode all the values
  encoded in a template."
- 2026-11-16: "the @inJson annotation is exactly what i had in mind. now can a compiler generate the user beans like
  we do with the prov-template-compiler, using the roster supplementary definitions. The ttl files should also define
  the subtypes and their relation to the general template."
- 2026-11-16: "The compiler extension would have to generate PAST expressions so that generated beans can be
  compiled to multiple target languages."

The first application is the first application, whose design note
(`the first application's design note`) records each request a person makes on one template,
`user_interacting`, with one subtype per action. ProvToolbox must not depend on it: everything here is stated for any
library.

## 2. Words

- **general template** — a `simple` template as compiled today: one table, one set of beans.
- **carrier** — a text variable of a general template that holds a JSON object.
- **in-JSON value** — a declared value encoded under a key of a carrier (`@inJson`).
- **subtype** — a general template with some inputs fixed and a stated set of in-JSON values. A record of a subtype
  is a record of the general template: same table, same row, same insert function.
- **typed bean** — the generated class of a subtype ("the user beans"), with one typed field per in-JSON value it
  carries, and conversions to and from the general template's input bean.
- **roster** — an application's own file listing its act types (nextgen's `acts.yaml`, the first application's `actions.yaml`). A
  roster is not ProvToolbox's: the application turns it into the configuration entries of §5.1.

## 3. What the code imposes

Read on 2026-11-16. Items marked (T31) are taken from T31's findings in `DONE.md` and were not read again.

- **A variable's annotations are a closed list.** `descriptors/AttributeDescriptor` (a value) takes `@value`,
  `@type`, `@documentation`, `@escape`, `@examplar`, `@input`, `@output`, `@sql.type`, `@sql.foreign`, `@unique`,
  `@transient`, `@pseudonym`. `descriptors/NameDescriptor` (a node) takes `@id`, `@type`, `@escape`, `@role`,
  `@documentation`, `@examplar`, `@input`, `@output`, `@sql.type`, `@sql.table`, `@sql.new.inputs`,
  `@sql.also.outputs`. The descriptors are read by `ConfigProcessor.objectMapper`, a Jackson 3 mapper built with `builderWithJackson2Defaults()`
  (`ConfigProcessor.java:63`), whose defaults fail on an unknown member, so a compiler older than T36 refuses a library
  that carries `@inJson`: it fails closed (T31). A value is an array of one `AttributeDescriptor`, a node an object
  (`DescriptorUtils.setupDeserializer`); member names are flat and dotted (`@sql.type`), so `@inJson.key` fits.
- **Every generator gets a template's variables from one place.** `compiler/CompilerUtil.getBindingsSchema(String)` is the
  only code that reads a `cbindings` file into a `TemplateBindingsSchema` (it applies T31's privacy policy right after
  reading); the two overloads taking a `SimpleTemplateCompilerConfig` cache its result by the `cbindings` path as the
  configuration gives it. The compiler calls them in about thirty places. `DescriptorUtils.fieldNames(schema)`
  lists the variables in order and 21 generators use it (T31; counted again on 2026-11-16: 22 files mention it). 22 files
  read `schema.getVar()` directly (`CompilerJsonSchema`, `CompilerBeanGenerator`, `CompilerCommon` at 17 places,
  `CompilerLogger`, `PrivacyPolicy`, …). Nothing else in the repository reads the descriptor classes: the services and
  plugins only name the `cbindings` file or copy it.
- **Two kinds of template, and no single place that tells them apart.** `compiler/configuration/TemplateCompilerConfig`
  has two subclasses, `SimpleTemplateCompilerConfig` (`@type: simple`; `template`, `cbindings`, `interfaces`) and
  `CompositeTemplateCompilerConfig` (`@type: composite`; `consistsOf`, `sharing`), named in `@JsonSubTypes` and in
  `src/main/resources/schema/template-project-schema.json`. Everything iterates one array, `configs.templates`: 47
  loops in 31 generator files. Where a loop distinguishes, "not simple" means composite: 23 tests of that form, and
  13 unconditional casts to the composite class (`ConfigProcessor` four times, then the bean, enactor, completer,
  merger, history, checker and query-invoker generators). A third kind placed in that array would be taken for a
  composite by all of them and fail on the cast. This is a larger change than the design first said (R11).
- **A bean is a PAST class.** `CompilerBeanGenerator.generateBean` builds a
  `compiler/past/Class` (`CLASS`, `FIELD`, `METHOD`, …) and returns a `SpecificationFile` of four generators: Java
  (`past/emitter/Poet`), Python, JavaScript and Rust. It already makes extra input beans over one template
  (`variantTable`, `generateSimpleConfigsWithVariants`), for templates used inside a composite.
- **What PAST has.** Statements and expressions: `Assignment`, `IfStatement`, `IfExpression`, `Return`,
  `ThrowStatement`, `TryCatch`, `ForLoop`, `MethodCall`, `CastExpression`, `InstanceOf`, `Constant`, `Variable`,
  `LambdaExpression`. Types (`past/type/ClassName`): `past.lang` `String`, `Integer`, `Long`, `Float`, `Double`,
  `Boolean`, `Object`; `past.util` `Map`, `HashMap`, `List`; `past.exception` `IllegalArgumentException` and others.
  The Python and JavaScript emitters treat `past.util.Map` and `HashMap` specially (`isMap`). PAST's annotations
  include `StaticMethod` and `NotInRust`. PAST classes are type-checked (`configuration/TypeCheckCoordinator`,
  `past/checker`).
- **The declared types.** `CompilerUtil.getPastClassForType`: `xsd:int`, `xsd:long`, `xsd:string`, `xsd:boolean`,
  `xsd:float`, `xsd:double`; `xsd:dateTime`, `xsd:date` and `json` are all a `String`. There is no decimal.
- **A runtime call reaches each language by one class per language** (T31, from `@escape`): `CompilerCommon` emits a
  PAST `METHOD_CALL` to `org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeCsv`; the class exists in
  Java (`prov-model`), in JavaScript through the jsweet candy (`prov-jsweet-candy-java`), in Python
  (`prov-template-library/src/main/python`). `CompilerMaven.makeSubPom` adds the dependency only when it is used.
  Rust has no mapping for a static call to such a class: `rust-registry.json` maps types and instance methods only.
- **Open tickets in the way.** T33: the Python emitter writes `null` as `0`, so an absent optional value is not
  absent. T34: the jsweet build the compiler generates transpiles nothing.
- **Outputs of the configuration.** `TemplatesProjectConfiguration` names the files the compiler writes for a
  catalogue: `jsonschema`, `documentation`, `sqlFile`. A Turtle file would be a fourth.

## 4. Part A — `@inJson`

### 4.1 The annotation

On a value's descriptor, in a general template's `cbindings`:

```json
"priceBefore": [[{ "@value": "*", "@type": "xsd:double",
                   "@documentation": "The line's unit price before the act",
                   "@input": "optional", "@inJson": "pattributes" }]]
```

- `@inJson` names the carrier. The key is the variable's name.
- `@inJson.key` (optional) gives the key when it differs from the variable's name. It is needed when one key is in
  two carriers of the same template, which happens today (`priceUnit` in `attributes` and in `pattributes`): two
  variables cannot share a name.

Refused at compile time, with the template and the variable named:

- the carrier is not a variable of the template, is not a value of type `xsd:string` or `json`, or is itself
  `@inJson`;
- two in-JSON values with the same carrier and key;
- `@inJson` on a node (`NameDescriptor`), or together with `@sql.type`, `@sql.foreign`, `@unique`, `@escape`,
  `@output`;
- `@inJson` together with `@transient` or `@pseudonym` (R8).

An in-JSON value is not a variable of the PROV template document: nothing in the template's graph refers to it.

**A carrier is open** (Luc, 2026-11-16: "It should be our expectation that @InJson does not declare all variables.
Additionally, we have to allow for extensibility, for attributes that had not been anticipated as design type."). `@inJson`
declares some of a carrier's keys, never all of them, and nothing here closes a carrier: what the compiler generates must
accept, keep and return keys nobody declared. This is a permanent property, not a stage of migration (R3).

### 4.2 Effect on what is generated

| generated | effect |
|---|---|
| the general template's beans, builders, invokers, enactors, CSV and log records, type converters | none |
| the SQL table, insert function and privacy constraints | none |
| the documentation page | the values are listed under their carrier |
| the JSON schema | recorded as an annotation of the carrier, whose type stays a string |
| the typed beans of subtypes (part B) | one field each |

**How (proposed, R1).** Split once, where the schema is loaded and cached: `TemplateBindingsSchema.getVar()` and
`DescriptorUtils.fieldNames` keep returning the ordinary variables only, and the in-JSON values are held beside them
(`getInJson()`). Every existing generator then sees exactly what it saw before, whichever of the two ways it reads
the variables, and only the documentation, the schema and part B read the new list. The alternative is to leave them
in `var` and make each generator skip them: 21 users of `fieldNames` and the direct readers of `getVar()` to change
and keep right.

### 4.3 The test of part A

A catalogue compiled without `@inJson`, and the same catalogue with in-JSON values added to its templates, give the
same generated tree, file for file, except the documentation and the JSON schema. And a catalogue that uses none of
T36 compiles to exactly what it compiled to before.

## 5. Part B — subtypes

### 5.1 A third kind of template in the configuration

```json
{ "@type": "subtype",
  "name": "repricing_rfq_line",
  "fullyQualifiedName": "com.example.actions.RepricingRfqLine",
  "package": "com.example.actions",
  "refines": "com.example.actions.UserInteracting",
  "fixed":   { "atype": "ex:RepricingRfqLine" },
  "carries": { "unitPrice": "required", "note": "optional" } }
```

- `refines`: a `simple` template of the same catalogue, by its fully qualified name (as `consistsOf` names one).
- `fixed`: inputs of the general template given a constant for every record of the subtype.
- `carries`: the in-JSON values of the general template this subtype has, each `required` or `optional`.

Refused at compile time: `refines` naming no simple template; a fixed variable that is not an input value of that
template (a node, an output, a carrier, an in-JSON value), or whose constant does not fit its type; a carried name
that is not an in-JSON value of that template; two subtypes of one template with the same fixed values.

`template-project-schema.json` gains the kind. **R11, ruled by Luc, 2026-11-16: "Third class, all loops skip it"**: the
entry is written in the catalogue's `templates` array, as `simple` and `composite` are, and is a third class,
`SubtypeTemplateCompilerConfig`, in `configs.templates`. Every one of the 47 loops over that array in 31 files is
taught to skip it (or, where it means to find a template by name, to find only simple ones), and the 13 casts and 23
"not simple" tests are made to name the kind they mean. This is the largest part of stage 2. It is guarded by one test:
a catalogue with a subtype entry compiles to the same tree, but for the subtype's own files, as the same catalogue
without it. Only the subtype generators read the third class. (The design had proposed moving the entry out of the
array at loading, which would have left those 47 loops alone; Luc chose the other.)

A subtype adds nothing to the server side: no table, no insert function, no builder, no entry in the catalogue's
dispatch, no log2prov reader. A store that loads the library sees the general template only.

### 5.2 The typed bean

One PAST class per subtype, in the package the general template's input bean is in, named as beans are
(`Repricing_rfq_lineInputs`):

- a constant `isA`, the subtype's fully qualified name, and a constant `refines`, the general template's;
- a field for each input of the general template, except the fixed ones and except each carrier this subtype carries
  values in;
- a field for each carried value, of the type `getPastClassForType` gives its `@type`;
- for each carrier the subtype carries values in, one more field, a `Map<String,Object>` of **the other keys** of that
  carrier (named `<carrier>Other`; R10), whose values are JSON values: string, integer, double, boolean, null, or a map
  or list of these. It is empty when there are none; it is what makes an unanticipated attribute reach the record;
- a carrier the subtype carries nothing in stays an ordinary field, passed through as it is (it is open by being text).

There is no typed bean for the outputs: a subtype's record returns what the general template's returns.

### 5.3 The two conversions, as PAST

Both are methods of the typed bean, built from PAST statements and expressions, never from text in one language.

`toGeneral()` returns the general template's input bean:

1. copies each ordinary field;
2. assigns each fixed constant;
3. for each required carried value that is absent, throws `IllegalArgumentException` naming the subtype and the value;
4. for each carrier with carried values, makes a map, puts each carried value that is present under its key, in
   the order the general template declares them, then the entries of the carrier's other-keys field, and assigns the
   carrier the text the runtime call of §5.4 returns. An other-keys entry whose key is one the template declares for
   that carrier is refused (`IllegalArgumentException`), so that a declared value cannot be shadowed unseen.

`fromGeneral(general)`, static, returns the typed bean:

1. throws when a fixed variable of `general` does not hold the subtype's constant;
2. copies each ordinary field;
3. for each carrier with carried values, reads it with the runtime call, assigns each carried field from its key,
   converted to the field's type, and puts every other key in the carrier's other-keys field;
4. applies the same check of required values.

### 5.4 The runtime class

Turning a map into JSON text and back needs a library in each language, so it cannot be PAST alone. It follows
`@escape`: the generated code makes a PAST `METHOD_CALL` to one small class that exists once per language, added to a
client project only when the catalogue has a subtype.

- `write(Map<String,Object>) → String` and `read(String) → Map<String,Object>`, and nothing else.
- **One text per value, in every language.** `write` keeps the order of the map, puts no space between tokens, and
  writes each type one way (R5). A record made from the same values by the Java client and by the Python client must
  carry the same text.
- A table of values with the text each must give is kept once and run by every language's port, as T31's conformance
  vectors are.

### 5.5 The languages

- **Java**: first, and what the first application needs.
- **Python**: emitted with Java. An absent optional value is right only once T33 is fixed, since the conversions
  test for `null`.
- **JavaScript**: emitted with Java. It cannot be built into a client until T34 is fixed.
- **Built (stage 4)**: the Python port is `prov-template-library/src/main/python/org/openprovenance/prov/utils/JsonText.py`; the JavaScript one is a jsweet
  candy, `prov-jsweet-candy-java`'s `JsonText.java`, with the number text JavaScript gives (RFC 8785's). Both run the shared vectors
  `src/test/resources/t36/json-text-vectors.json`. The Python and JavaScript emitters were given one rule, that a class does not import itself.
  Python is right for present values; an absent one waits for T33.
- **Rust**: has no mapping for the static runtime call (§3). Proposed (R4): mark the two conversions `NotInRust`, so
  that the Rust bean has the typed fields and no conversions, and file the mapping as its own ticket.

### 5.7 The dispatcher (R9)

One PAST class per catalogue that has subtypes, in the catalogue's common package: from a general template's input bean
it returns the typed bean of the subtype whose fixed values the bean holds, as PAST (a chain of tests of the fixed
variables, each calling that subtype's `fromGeneral`), and a refusal when none matches. When one general template has
subtypes that fix different variables, the tests are made in the order of the number of fixed values, most first. A
catalogue with no subtype has none. It reads the third class of §5.1 and emits through the same emitters; Rust has it
marked `NotInRust`, like the conversions it calls.

### 5.6 The test of part B

For a case table of subtypes (in the manner of `T31CasesTest`): the typed bean compiles in each language emitted;
`fromGeneral(x.toGeneral())` gives back `x`, other keys included (a case with an undeclared key, nested value
included); `toGeneral(fromGeneral(g))` gives back `g` as a JSON object; a missing required value is refused; a general bean of another subtype is
refused; the carrier's text is the expected one and the same from each language.

## 6. Part C — the descriptions

- **Documentation.** Under each general template, its carriers with their in-JSON values, and its subtypes, each with
  what it fixes and carries.
- **JSON schema.** For each subtype, the schema of its typed bean: the fixed values absent, the carried values as
  typed properties, the required ones required, and each carrier's other keys as an object with free properties
  (the carrier is open, §4.1). The general template's own schema stays closed on its variables, since the carrier is
  one string there.
- **Turtle** (new; the configuration gains a member naming the file, as `jsonschema` and `documentation` do). For a
  catalogue:

```turtle
ex:UserInteracting                      # a general template: the class of its records
    rdf:type          owl:Class ;
    rdfs:label        "User Interacting"@en .

ex:unitPrice                            # an in-JSON value: a key of a carrier
    rdf:type          owl:DatatypeProperty ;
    rdfs:domain       ex:UserInteracting ;
    rdfs:range        xsd:double ;
    tpl:inJson        "attributes" .

ex:UserInteracting-RepricingRfqLine     # a subtype
    rdf:type          owl:Class ;
    rdfs:subClassOf   ex:UserInteracting ;
    tpl:fixes         [ tpl:variable "atype" ; tpl:value "ex:RepricingRfqLine" ] ;
    tpl:requires      ex:unitPrice ;
    tpl:mayCarry      ex:note .
```

The relation of a subtype to its general template is `rdfs:subClassOf`, which says what holds: every record of the
subtype is a record of the general template. `tpl:` is a small vocabulary this ticket would define (R7). The
application's act types (their labels, their comments) stay the application's to declare; the compiler writes what it
knows and has checked.

**Built (stage 5)**: the three descriptions are as written above, with these choices of names: the catalogue members `turtle` (the file) and
`turtleNamespace` (the IRI of the terms); the term of an in-JSON value is `<template name>-<value name>` and not the value's name alone (a
property with two domains belongs to both). A template is described in the Turtle when it keeps in-JSON values or has subtypes. See `TODO.md`, T36.

## 7. Stages

Each stage ends with the module's tests green and can be judged before the next.

1. **`@inJson`** (part A): the descriptor member, the split at loading, the compile-time refusals, the listing in the
   documentation and the schema; the test of §4.3. This stage needs no ruling beyond R1 and R2.
2. **The `subtype` kind** (R11): the third configuration class, the project schema, every loop over `configs.templates` taught to skip it, the compile-time refusals, and the test that an added subtype entry changes only its own files.
3. **The typed bean in Java**: the PAST class and its two conversions, the dispatcher (R9), the Java runtime class, the test of §5.6 in
   Java.
4. **Python and JavaScript**: the ports of the runtime class, the shared table of values; what T33 and T34 block is
   said in the tests, not hidden.
5. **The descriptions** (part C).
6. **The specification.** T31 changed the provenance-templates specification for its annotations; `@inJson` and the
   subtype kind need the same.
   **Built (stage 6)**: in the specification repository (`provenance-templates-specification/prov-template/index.html` and
   `schemas/bindings-schema.json`): a section on in-JSON values, a section on subtypes in the compiler's description, the two
   annotations in the table and the schema, a line in the changes, and editor's notes where the page and the compiler disagree.
7. **The request and the reply** (part D, §12): built (R16), the rulings R12 to R15 and the method names given 2026-11-16 (§12.8). See §12.10 for what the code changed in the section.

## 8. Rulings

**Given by Luc, 2026-11-16** (answers to the questions put to him; where he picked a listed option its name is quoted):

| | ruling |
|---|---|
| R1 | "Separate list at loading": split at the one place the schema is loaded. |
| R2 | "Allow @inJson.key". |
| R3 | His message ("@InJson does not declarate all variables … extensibility, for attributes that had not been anticipated"), then the three details "as stated": other keys kept per carrier; a clash with a declared key refused; JSON values; same content, declared keys first. |
| R4 | "Fields only, conversions not in Rust"; the Rust registry's static call is a ticket of its own. |
| R5 | "Accept, with RFC 8785 numbers": integers as JSON integers, booleans, strings, dates and date-times as strings, a `json` value nested as JSON, doubles by RFC 8785's number rule (5.0 is written 5). |
| R6 | "Own ticket after T36". |
| R7 | "Compiler writes it; name from subtype's name": `rdfs:subClassOf`, a `tpl:` vocabulary, the namespace in the catalogue, the term named from the subtype's `name`. |
| R8 | "Refuse the combination". |
| R9 | **"Generate it per catalogue now"**: the dispatcher is in T36 (§5.7). My proposal was to leave it out. |
| R10 | "Accept as proposed": `subtype`, `refines`, `fixed`, `carries`, `toGeneral`, `fromGeneral`, `<carrier>Other`, `org.openprovenance.prov.utils.JsonText` in prov-model with a jsweet candy and a Python port. |
| R11 | **"Third class, all loops skip it"** (§5.1). My proposal was to move subtypes out at loading. |

R12–R16 are ruled in §12.8, for part D (Luc, 2026-11-16).

The text of each ruling below is what was asked; where it differs from the table, the table is what Luc ruled.

- **R1. Where an in-JSON value is kept after loading.** Proposed: split at the one place the schema is loaded, so
  existing generators are untouched (§4.2). Alternative: keep them in `var` and filter in each generator.
- **R2. `@inJson.key`.** Proposed: allowed, for a key that differs from the variable's name. Alternative: the key is
  always the name, and a library renames one of two clashing keys in its data.
- **R3. A carrier with keys nobody declared. Ruled by Luc, 2026-11-16, in effect** (his message while R1–R3 and R11
  were being asked): "It should be our expectation that @InJson does not declarate all variables. Additionally, we
  have to allow for extensibility, for attributes that had not been anticipated as design type." So the alternative
  the design listed is the rule, for every subtype and not as an opt-in: the typed bean keeps a map of the other keys
  (§5.2), `fromGeneral` fills it, `toGeneral` writes it back. What is still open in it, for him to confirm: the other
  keys are kept for every carrier the subtype carries values in; a clash between an other key and a declared key is
  refused (§5.3); `fromGeneral` then `toGeneral` gives the same JSON object but not always the same text, since
  declared keys are written first (§5.6 compares JSON values, not strings).
- **R4. Rust.** Proposed: typed fields in Rust, the conversions marked `NotInRust`, the mapping of a static runtime
  call filed as its own ticket. Alternative: T36 extends the Rust registry first.
- **R5. The text of a value.** Proposed: an `xsd:int` or `xsd:long` as a JSON integer; an `xsd:double` or `xsd:float`
  as the shortest text that reads back to the same number; a boolean as `true` or `false`; a string, a date and a
  date-time as a JSON string; a `json` value as the JSON it is (an object or an array nested in the carrier, not a
  string holding JSON). The last changes what `json` means for an in-JSON value compared with a column, where it is a
  string.
- **R6. A decimal type.** Money is `xsd:double` in the libraries today. `xsd:decimal` needs a PAST type and its
  rendering in four languages. Proposed: its own ticket after T36. Alternative: in T36, for Java and Python first.
- **R7. The Turtle.** Four choices: the compiler writes the file (alternative: each application's roster compiler
  does, as nextgen's does for act types today); `rdfs:subClassOf` for the relation; a subtype as a term of its own,
  named `<Template>-<fixed act type>` (alternative: named by the subtype's `name`); the vocabulary `tpl:` and the
  namespace of a catalogue's terms, which the configuration would have to give.
- **R8. Privacy annotations on an in-JSON value.** T31's `@transient` and `@pseudonym` act where a value leaves the
  process as a column or a log field. An in-JSON value leaves inside its carrier. Proposed: refuse the combination in
  T36 and state that a carrier's own annotations cover its whole content. Alternative: `toGeneral` applies them to the
  value before it is written.
- **R9. A dispatcher.** From a general bean, give the typed bean of the subtype its fixed values name. Proposed: not
  in T36; `fromGeneral` per subtype is enough for a first application. Alternative: one generated class per catalogue.
- **R10. The names.** `subtype`, `refines`, `fixed`, `carries`, `toGeneral`, `fromGeneral`, and the runtime class's
  name and module.

- **R11. Where a subtype entry lives after loading** (added by the check, §11). Proposed: written in `templates`,
  moved at loading into `configs.subtypes` (§5.1). Alternatives: a member of its own in the catalogue
  (`"subtypes": [...]`), which needs no moving and reads as plainly; or a third class in `templates` with every one of
  the 47 loops taught to skip it, which is the risk the check found.

## 9. Out of scope

- An application's roster and the program that turns it into `cbindings` and subtype entries.
- Declaring the keys of any existing library's carriers.
- A view or typed columns in SQL over a carrier.
- A check, in the store, that a stored carrier holds only declared keys.
- Any change to what a general template generates for a catalogue that has no `@inJson` and no subtype.

## 10. Not verified

- The emitters were not read in full. Whether the two conversions, written with the PAST nodes of §3, come out right
  in Python and JavaScript without a new node or a new emitter rule is for stage 3 and 4 to show. The type checker may
  need the runtime class registered as an external type.
- The Rust registry (`rust-registry.json`) and the claim that it maps no static call are T31's finding, not read again.
- The items marked (T31) in §3 are T31's findings as written in `DONE.md`.
- The figures of §1 are nextgen's roster as read on 2026-11-16.
- The specification repository (`provenance-templates-specification`) was read for stage 6 (§7); its page was not rendered.

## 11. Check against the code (2026-11-16)

Done on the worktree branch of `development2_0` at 0a8d9f4b1 by reading `descriptors/*`, `CompilerUtil`, `ConfigProcessor`,
`configuration/*`, `CompilerJsonSchema`, `CompilerDocumentation`, `PrivacyPolicy` and the loops over `configs.templates`.
Nothing was built for this check. What changed in the design, and why:

1. **§3 and §5.1, the third kind of template** (the one real correction). The design said `ConfigProcessor`'s main loop
   casts anything not simple to the composite class. It is 47 loops in 31 files, 23 of which treat "not simple" as
   composite and 13 of which cast. A subtype placed in `templates` would break most generators. §5.1 now proposes
   splitting subtypes out at loading, and R11 asks it.
2. **§3, how the descriptors are read.** It is Jackson 3 with Jackson-2 defaults, not "Jackson 2"; the behaviour the
   design relies on (an unknown member fails) is unchanged. Not run: stage 1's test will show it.
3. **§3, where bindings are read.** There is one reader, `getBindingsSchema(String)`; the cache sits in two thin
   overloads keyed by the configured `cbindings` path; the privacy policy is applied right after reading. R1's split
   goes in that reader, **before** the policy is applied, so a policy scope naming an in-JSON value is refused as an
   unknown variable (a consequence of R8, now stated: that message says "unknown variable").
4. **§3, the counts.** 22 files use `fieldNames` (the design said 21, from T31), 22 read `getVar()` directly.
5. **§10, two items answered.** Nothing in the compiler cross-checks a `cbindings` file's variables against the
   template document: the parameters of every generated class come from the bindings (`fieldNames`), never from the
   document. That is the reason the split of R1 is needed (an in-JSON value left in `var` would become a bean field
   and a column), and it means an in-JSON value absent from the document raises nothing. And no code in the
   repository other than the compiler reads the descriptor classes (§3). A tool that parses a `cbindings` file itself
   was looked for in the services and the Maven plugin and not found; applications' own tools are not in this repository.

6. **§4.1, §5.2, §5.3, §5.6, §6, R3 — Luc's answer to the rulings, 2026-11-16.** While R1–R3 and R11 were asked, Luc
   wrote: "It should be our expectation that @InJson does not declarate all variables. Additionally, we have to allow
   for extensibility, for attributes that had not been anticipated as design type." The design had treated undeclared
   keys as a transition and proposed ignoring them. They are permanent, so the typed bean now keeps them in a map per
   carrier, `toGeneral` writes them back, and the tests round-trip an undeclared key. R3 is recorded as ruled in
   effect, with three details put back to him.

What the check left as written: §4's refusals and test, §5.4–5.5, §7, and R1, R2, R4–R11 as proposed.

## 12. Part D — the request and the reply (proposed 2026-11-16; not built)

Luc, 2026-11-16: "Add the asking and the answer classes to the T36 design. Does it require change to the
declarations?" And, on the name of the annotation, the same day: "What about @performative", then "go for
performative".

**The word.** A performative is the kind of act a message performs: a request, a reply, a refusal. The term comes
from the languages agents use to talk to one another. Here it names the two messages of one exchange between a caller
and a service: **the request** (what Luc first called the asking) and **the reply** (the answer).

### 12.1 Why

The typed bean of a subtype holds every input of a record. Where the record is of a request that a program served,
those inputs come from three hands:

- **the caller**: what was asked (a line, a price);
- **the service, before it acts**: who asked and about what, as nodes of the store, and the time;
- **the service, after it acted**: what became of the request (an outcome, the version made, a refusal's code).

The typed bean does not tell them apart. Two things were found with the first application (the first application, a trial
on 2026-11-16 with the beans of stage 3):

- Its JSON reader fills a typed bean straight from the body of a request, so a body
  `{"unitPrice": 2, "agent": 99, "outcome": "accepted"}` sets `agent` and `outcome`, which are not the caller's to set.
- It writes by hand one class for the body of each request and one for each response. They say again what the
  declaration says, and nothing holds the two together.

Part D adds two generated classes per subtype: **the request**, with only what the caller gives, and **the reply**,
with only what the service returns. Both feed the one record through the typed bean. The typed bean, the general
template's beans and the SQL are unchanged.

### 12.2 Does it need a change to the declarations

**Yes: one optional annotation on the values concerned. No existing declaration has to change**, and a declaration
without it generates exactly what it generates today.

It is needed because nothing in a declaration says which hand a value comes from. `@input` says whether the record
must be given the value, not who gives it.

- **The request can be guessed only for some templates.** Where a template keeps nothing but the caller's values in
  its carriers, the request is what the subtype carries. That is true of the first application's interaction template by design. It
  is false of nextgen's templates: in the record of one repricing, 17 of the 19 values in the two carriers are the
  service's.
- **The reply cannot be guessed at all.** `outcome`, `result` and `code` are ordinary inputs, declared like `agent`
  and `time`.

Three places it could be said:

| | where | for | against |
|---|---|---|---|
| (a) | an annotation on the variable, in the general template's declaration | said once per template, every subtype follows; the template's page, schema and Turtle can show it; nothing is generated where it is absent | a new annotation: a compiler without part D refuses the file |
| (b) | two lists on each subtype entry of the catalogue | the declarations are untouched | repeated in every subtype, the reply being the same list each time; a template's own page cannot show it |
| (c) | a rule: the request is what the subtype carries, and only the reply is said, by (a) or (b) | less to write | wrong, and silently, for a template whose carriers hold the service's values |

**Proposed: (a)** (R12). Luc's ruling on the annotation's name (R14) takes (a) as given; he was not asked to choose
between the three in so many words.

### 12.3 The annotation

On a value's descriptor (`AttributeDescriptor`), beside `@input`:

```json
"unitPrice": [[{ "@value": "*", "@type": "xsd:double",
                 "@documentation": "The unit price the person asks for",
                 "@input": "optional", "@inJson": "attributes", "@performative": "request" }]],

"outcome":   [[{ "@value": "*", "@type": "xsd:string",
                 "@documentation": "accepted, refused or failed",
                 "@input": "compulsory", "@performative": "reply" }]]
```

- `"request"`: the value is content of the request: the caller gives it.
- `"reply"`: the value is content of the reply: the service returns it.
- Absent: neither. The value is the service's own.
- Allowed on an ordinary input value and on an in-JSON value. One performative per value.

A performative is a property of a message, and the annotation is written on a value. It is to be read as "this value
is content of the message that performs this act".

Refused at compile time, with the template and the variable named:

- a value other than the two;
- `@performative` on a node (`NameDescriptor`): a caller gives names, and the service finds the nodes;
- `@performative` on an output;
- `@performative` on a carrier that holds in-JSON values: its values each say their own.

Effect on what a general template generates: none, as for `@inJson` (§4.2), except that its documentation page and its
schema say the performative of each value. The annotation is read on the descriptor and kept there. Only part D's
generator and the descriptions read it, so no existing generator changes.

**What the annotation does not mean.** The compiler sorts values into two classes and does nothing else with the
word. It does not check that a reply follows a request. And the reply is not one act: whether it is an agreement, a
refusal or a failure is said by a value inside it (`outcome`, in the first application), which the compiler treats as
any other value.

### 12.4 The two classes

For a subtype, the request class is generated when the subtype has at least one request value, and the reply class
when it has at least one reply value. A subtype has a value when it is an ordinary input the subtype does not fix, or
an in-JSON value the subtype carries. Both are PAST classes in the typed bean's package, written by the same emitters.

`<Name>Request`:

- one field per request value of the subtype, of the type it has in the typed bean;
- `into(typed)`: copies its fields into the typed bean it is given and returns it. It touches no other field;
- static `of(typed)`: the request of a typed bean.

`<Name>Reply`: the same, with the reply values.

A variable the subtype fixes is in neither: the subtype sets it. Neither class checks anything: the check of required
values stays in `toGeneral()` (§5.3), which every record passes through. Neither calls the runtime class of §5.4, since
they only copy fields.

As they would read in Java (written by hand here; nothing is generated yet):

```java
public class Repricing_rfq_lineRequest {
  public Integer line;
  public Double unitPrice;
  public String reason;
  public Repricing_rfq_lineInputs into(Repricing_rfq_lineInputs typed) { … }
  public static Repricing_rfq_lineRequest of(Repricing_rfq_lineInputs typed) { … }
}

public class Repricing_rfq_lineReply {
  public String outcome;
  public String result;
  public String code;
  public Repricing_rfq_lineInputs into(Repricing_rfq_lineInputs typed) { … }
  public static Repricing_rfq_lineReply of(Repricing_rfq_lineInputs typed) { … }
}
```

And how an application would use them (its JSON reader and its store are its own):

```java
Repricing_rfq_lineRequest asked = read(body, Repricing_rfq_lineRequest.class);
Repricing_rfq_lineInputs in = asked.into(new Repricing_rfq_lineInputs());
in.agent = …; in.subject0 = …; in.time = …;        // the service's own, before it acts
…                                                    // the service acts
in.outcome = "accepted"; in.result = version;        // the reply
store.process(in.toGeneral(), key);
return Repricing_rfq_lineReply.of(in);
```

A body that names `agent` or `outcome` then cannot set them: the request class has no such field.

**Keys nobody declared (R3).** A carrier is open, and an attribute nobody anticipated has to be able to reach the
record. Proposed (R13): the request class has the other-keys map (`<carrier>Other`) of each carrier whose declared
values, in the template, are all request values, and `into` copies it to the typed bean. For a carrier that also holds
values of the service, the request class has no such map, and an undeclared key reaches the record only if the
application puts it in the typed bean itself. Alternative: no map in the request class at all.

**The languages.** As the typed bean: Java, Python (an absent optional value is right only once T33 is fixed),
JavaScript (emitted; built only once T34 is fixed). Rust: the fields. R4 left the conversions out of Rust because they
call a runtime class; `into` and `of` call none, so they may be possible there. Not tried.

### 12.5 The descriptions

- **Documentation.** For each subtype, its values in three groups: of the request, of the reply, the service's own.
- **JSON schema.** Two more definitions per subtype, for the two classes. An application can publish the first as the
  schema of its request body. They have no `isA`, so T40 does not touch them.
- **Turtle.** `tpl:performative "request"` or `"reply"` on the value's property. Today only an in-JSON value has a
  term (§6). An ordinary value that has a performative would need one too, named the same way
  (`<template name>-<value name>`), without `tpl:inJson` (R15).
- **Specification.** The annotation in the table and in the bindings schema, as stage 6 did for `@inJson`.

### 12.6 What it does not do

- It reads and writes no JSON and knows nothing of HTTP. Values an application takes from an address or a header
  (which document, which line, who is calling) are copied by the application.
- No decimal type (R6): a requested price is a double until that ticket.
- The template's outputs bean is unchanged, and the reply class does not hold the identifiers the store mints.
- Nothing changes for a catalogue that uses no `@performative`.

### 12.7 The test of part D

- For the fixture of §5.6 with performatives added: `of(asked.into(t))` gives back `asked`, and the same for the
  reply; `into` leaves every other field of the typed bean as it was; the request class has no field for a reply value
  or for a value of the service, and the reply class none for a request value.
- The same catalogue without `@performative` gives the same generated tree, but for the two classes and the
  descriptions. A catalogue that uses none of T36 compiles to what it compiled to before.
- Each refusal of §12.3.

### 12.8 Rulings

- **R12. Where the performative of a value is said. Ruled by Luc, 2026-11-16: "Annotation on the variable (Recommended)"**,
  the option that read: `@performative: request|reply` in the general template's declaration; said once, every subtype
  follows; no existing declaration changes. Proposed (a) of §12.2; the alternatives were two lists on each subtype entry (b), and the rule that the
  request is what the subtype carries (c).
- **R13. Keys nobody declared, in the request class. Ruled by Luc, 2026-11-16: "Map only for all-request carriers (Recommended)"**,
  the option that read: the request class has `<carrier>Other` where every value the template declares in that carrier is a
  request value; `into` copies it to the typed bean. Alternative: no map in the request class.
- **R14. The names. Ruled by Luc, 2026-11-16: "go for performative".** The annotation is `@performative`. It
  had first been proposed as `@side`, with the values `asking` and `answer`; Luc asked "why did you call this @side?",
  proposed `@inSubtype`, then `@performative`. His ruling answers a proposal that put with it the values `request`
  and `reply`, the class names `<Name>Request` and `<Name>Reply`, and the Turtle property `tpl:performative`; those
  three are taken as accepted with it and he did not word them himself. **The methods, ruled 2026-11-16: "into and of (Recommended)"**:
  `typed = request.into(new XInputs())` and `XRequest.of(typed)`.
- **R15. The Turtle. Ruled by Luc, 2026-11-16: "Yes, a term for it (Recommended)"**, the option that read: `<template>-<value>` without
  `tpl:inJson`, with `tpl:performative`; the template becomes a class when any value has a performative. The property
  `tpl:performative` came with R14.
- **R16. Where it is built. Ruled by Luc, 2026-11-16**, asked whether to build it now as stage 7 or keep it as
  design until T36 is merged: "and then request the subsession to implement". So it is stage 7 of T36, on its branch,
  built by T36's own session.

### 12.9 Not verified

- The Rust classes are fields only; the methods were tried and do not compile (§12.10).
- The Turtle with a parser other than rdflib; the documentation page in a browser; the request schema in jsonform; the
  classes inside a real application's build. The trial of §12.1 is now a test (Jackson 3 with its defaults reading a body into the
  typed bean and into the request class), but a reader other than Jackson was not tried.
- The agent communication languages the word comes from were not consulted for this section; `request` and `reply`
  are chosen as plain names of the two messages, not taken from one of them.

### 12.10 Built (stage 7) and what the code changed in this section

Checked against the code on 2026-11-16 while building:

1. **§12.3, one more refusal.** `@performative` on a value that is no input of the template is refused: such a value is in neither
   class (the typed bean keeps inputs only), so the annotation would say nothing. The refusals are in
   `descriptors/PerformativeChecker`, called by `CompilerUtil.getBindingsSchema` after the `@inJson` split, so it can tell a carrier;
   each names the file, the template and the variable. `NameDescriptor` reads the member only to refuse it.
2. **§12.4, the map of other keys.** The request's map has no initial value, and `into` copies it only when it is present, so an
   absent map leaves the typed bean's own (empty) one; `of` copies the reference. The test of "present" is the only comparison with
   null in the classes, and the Python emitter writes it `!= 0` (T33): in Python `into` then copies `None`.
3. **§12.4, Rust: tried, it cannot.** The Rust emitter writes the copy of one bean's field into another's as `Some(x.to_string())`
   or `Some(x)`, which does not compile for an `Option` field (`cargo check` on the generated file: "`Option<String>` doesn't
   implement `Display`", "expected `i32`, found `Option<i32>`"). The classes are fields only in Rust, with the derive, no map of other
   keys, and a comment saying why. The Rust emitter was not extended (Luc's word is needed).
4. **§12.5, names.** The schema definitions are `<subtype fqn>Request` and `<subtype fqn>Reply` (`t31.repriceRequest`), with no
   `isA`, so no `if isA then $ref` block. The general template's page and schema say the performative of each value (`[@performative: request]`
   in a description; `key (type, request)` in the list of a carrier's keys), and add nothing without it. The Turtle declares
   `tpl:performative` only when some value has one, so a catalogue without it gets the same Turtle as before.
5. **§12.7, the tests** are `T36PerformativeTest` (12): each refusal; the same catalogue with and without the annotation differing in exactly
   the four classes, `doc.html`, `schema.json` and the copy of the bindings schema; the classes compiled and run (fields, `into`, `of`, the
   carrier's text written from them); the Jackson body of §12.1; the documentation; the schema, with a request body checked against the
   request definition; the Turtle, read by rdflib; Python and JavaScript run against a stand-in typed bean (`check_request_reply.py`, `.js`); Rust.
   What T33 spoils is printed (`BLOCKED T33`), and T34 stops the JavaScript from being built into a client.

