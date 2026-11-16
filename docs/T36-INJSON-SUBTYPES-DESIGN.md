# T36 — `@inJson` values and template subtypes: design

Status: **proposed, nothing built** (2026-11-16); checked against the code the same day (§11). Ticket: `TODO.md`, T36.
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

`template-project-schema.json` gains the kind. Where its entry lives is R11: proposed, the entry is written in the
catalogue's `templates` array, as `simple` and `composite` are, but is taken out of `configs.templates` as soon as the
configuration is read, into `configs.subtypes`. The 47 loops then never see a subtype, which is the same device as R1:
split once at the point of loading, so existing code is untouched. Only the subtype generators read `configs.subtypes`.

A subtype adds nothing to the server side: no table, no insert function, no builder, no entry in the catalogue's
dispatch, no log2prov reader. A store that loads the library sees the general template only.

### 5.2 The typed bean

One PAST class per subtype, in the package the general template's input bean is in, named as beans are
(`Repricing_rfq_lineInputs`):

- a constant `isA`, the subtype's fully qualified name, and a constant `refines`, the general template's;
- a field for each input of the general template, except the fixed ones and except each carrier this subtype carries
  values in;
- a field for each carried value, of the type `getPastClassForType` gives its `@type`;
- a carrier the subtype carries nothing in stays an ordinary field, passed through as it is.

There is no typed bean for the outputs: a subtype's record returns what the general template's returns.

### 5.3 The two conversions, as PAST

Both are methods of the typed bean, built from PAST statements and expressions, never from text in one language.

`toGeneral()` returns the general template's input bean:

1. copies each ordinary field;
2. assigns each fixed constant;
3. for each required carried value that is absent, throws `IllegalArgumentException` naming the subtype and the value;
4. for each carrier with carried values, makes a `HashMap`, puts each carried value that is present under its key, in
   the order the general template declares them, and assigns the carrier the text the runtime call of §5.4 returns.

`fromGeneral(general)`, static, returns the typed bean:

1. throws when a fixed variable of `general` does not hold the subtype's constant;
2. copies each ordinary field;
3. for each carrier with carried values, reads it with the runtime call and assigns each carried field from its key,
   converted to the field's type;
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
- **Rust**: has no mapping for the static runtime call (§3). Proposed (R4): mark the two conversions `NotInRust`, so
  that the Rust bean has the typed fields and no conversions, and file the mapping as its own ticket.

### 5.6 The test of part B

For a case table of subtypes (in the manner of `T31CasesTest`): the typed bean compiles in each language emitted;
`fromGeneral(x.toGeneral())` gives back `x`; a missing required value is refused; a general bean of another subtype is
refused; the carrier's text is the expected one and the same from each language.

## 6. Part C — the descriptions

- **Documentation.** Under each general template, its carriers with their in-JSON values, and its subtypes, each with
  what it fixes and carries.
- **JSON schema.** For each subtype, the schema of its typed bean: the fixed values absent, the carried values as
  typed properties, the required ones required.
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

## 7. Stages

Each stage ends with the module's tests green and can be judged before the next.

1. **`@inJson`** (part A): the descriptor member, the split at loading, the compile-time refusals, the listing in the
   documentation and the schema; the test of §4.3. This stage needs no ruling beyond R1 and R2.
2. **The `subtype` kind**: the configuration class, the enum, the project schema, the loop, the compile-time refusals.
3. **The typed bean in Java**: the PAST class and its two conversions, the Java runtime class, the test of §5.6 in
   Java.
4. **Python and JavaScript**: the ports of the runtime class, the shared table of values; what T33 and T34 block is
   said in the tests, not hidden.
5. **The descriptions** (part C).
6. **The specification.** T31 changed the provenance-templates specification for its annotations; `@inJson` and the
   subtype kind need the same.

## 8. Rulings asked

- **R1. Where an in-JSON value is kept after loading.** Proposed: split at the one place the schema is loaded, so
  existing generators are untouched (§4.2). Alternative: keep them in `var` and filter in each generator.
- **R2. `@inJson.key`.** Proposed: allowed, for a key that differs from the variable's name. Alternative: the key is
  always the name, and a library renames one of two clashing keys in its data.
- **R3. A carrier with keys nobody declared.** A library moving to `@inJson` step by step has carriers holding both
  declared and undeclared keys. Proposed: the typed bean writes only what is declared, and `fromGeneral` ignores other
  keys; a carrier a subtype must also fill freely stays out of that subtype's `carries`. Alternative: an `open`
  subtype keeps a map of the other keys beside the typed fields.
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
- The specification repository (`provenance-templates-specification`), which holds the bindings schema, was not read.

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

What the check left as written: §4 (the annotation, the refusals, the test), §5.2–5.6, §6, and R1–R10.
