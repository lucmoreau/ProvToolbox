# ProvToolbox TODO — Outstanding Work

> **🔢 LAST ALLOCATED TASK NUMBER: T48** — the highest task id ever assigned (whether still open here or retired to
> `DONE.md`). **When allocating a new task, take the NEXT number (T49) and bump this line.** Do NOT reuse a number
> freed by retirement. T22–T25 were placeholders never filled (removed 2026-10-16); their numbers stay taken.
>
> Completed tasks are in `DONE.md`. Paths are relative to the repository root unless stated.
> Restructured 2026-10-16 on the pattern of nextgen's `ODOO-TODO.md`: an index of one-line rows, the narrative in
> each task's section. Priorities other than T28 and T26 were not set before; they are listed ⬜ BACKLOG until ruled.

---

## Task Index

When a task is selected and completed: (1) remove it from this index, (2) move the task
section to `DONE.md` with a completion date, and add it to `DONE.md`'s index.

**Index format — one short sentence per task, narrative in the body.** Each row in this index is a
title / brief one-line summary *only*. ALL narrative — diagnosis, findings, design, evidence,
attempt history — belongs in the task's `###` section under `## Tasks` below, never in the index row.

| # | Task | Priority | Category | Section |
|---|---|---|---|---|
| T48 | The python tests of `prov-template-library` (`test.py`, `test2.py`) fail, with the original compiler and with this one: the Python emitter writes Java's static calls literally (`int.valueOf(...)`, 205 times in the library's generated Python, `str.concat`, `str.valueOf`, `float.valueOf`), and the tests are stale (a commented-out `Logger` import; `processorConverter`, which the overloading rename gives other names). Nothing runs them (the pom's `python.test` is commented out). The compiler module's two python tests also have a stale import in one. | ⬜ BACKLOG | Template compiler / Python / Tests | [T48 section](#-t48-the-python-tests-of-the-template-library-fail) |
| T41 | The `flow` entry of T36 expresses one request that makes one record on a previous record of the same template. Extend it: a flow with nothing that stood, a stood record made by another template, and several records in one flow; and the compiler findings of the first application. | ⬜ BACKLOG | Template compiler / Flow | [T41 section](#-t41-the-flow-entry-expresses-more-requests) |
| T36 | `@inJson` declares the values a template keeps inside a JSON text variable, and a `subtype` of a template gets a typed bean generated from PAST for each target language; the Turtle says how each subtype relates to its general template. | ⬜ BACKLOG | Template compiler | [T36 section](#-t36-injson-values-and-template-subtypes) |
| T30 | ttf execution: the `dot` runs left after T29 — one run per layout for the png and svg of a document; `validate-ttf` one `ajv` run per batch; `dot`'s stderr drained. | ⬜ BACKLOG | Template / Build | [T30 section](#-t30-ttf-execution-one-dot-run-per-layout) |
| T28 | The Browser tab checks every listed record's hash at once — with ~2,000 records the browser drops ~400 checks and rows on screen can show no verdict. | 🟡 MEDIUM | Service / Dashboard | [T28 section](#-t28-the-browser-tab-checks-every-records-hash-at-once) |
| T26 | log2prov cannot read composite template records (null builder slots in the generated Init, FQN vs name key, literal `\n` between the composite's records). | 🟡 MEDIUM | Template compiler / log2prov | [T26 section](#-t26-log2prov-cannot-read-composite-template-records) |
| T21 | Accept bindings files in yaml format: schema validation, a `-bindingsformat` option, the service instantiation page, the book's chapter 5. | ⬜ BACKLOG | Templates / Bindings | [T21 section](#-t21-bindings-files-in-yaml-format) |
| T2 | template-compiler: `TableConfigurator` generic per method rather than per class, removing unnecessary casts. | ⬜ BACKLOG | Template compiler | [T2 section](#-t2-tableconfigurator-generic-per-method) |
| T3 | `script.sh` in prov-template-compiler points to the proper library. | ⬜ BACKLOG | Template compiler / Build | [T3 section](#-t3-scriptsh-points-to-the-proper-library) |
| T4 | No delegation inserted when delegate/responsible are empty. | ⬜ BACKLOG | Template service / Store | [T4 section](#-t4-no-delegation-without-delegate-and-responsible) |
| T5 | `PC1FullTest.java` in prov-dot no longer works. | ⬜ BACKLOG | prov-dot / Tests | [T5 section](#-t5-pc1fulltest-in-prov-dot) |
| T6 | Upgrade the Scala version. | ⬜ BACKLOG | Build | [T6 section](#-t6-upgrade-scala) |
| T7 | `templateIT` fails when the service uses Mongo. | ⬜ BACKLOG | Template service / Tests | [T7 section](#-t7-templateit-fails-with-mongo) |
| T8 | DeleteJob/Redis: template expansion fails to clear one of the provn files (the original template). | ⬜ BACKLOG | Service / Storage | [T8 section](#-t8-deletejob-and-redis) |

---

## Tasks

### ⬜ T48: The python tests of the template library fail

**Priority**: ⬜ BACKLOG.  **Category**: Template compiler / Python / Tests.  **Found 2026-11-19** (Luc: "The provenance template library has got some tests in python, they will need to be tried").

**What was run.** `modules-template/prov-template-library/src/test/python/test.py` and `test2.py` (Makefile targets `test.py` and `test.py2`: `PYTHONPATH=target/generated-python:src/main/python`), against the Python the library's own build generates (`tp_plead`, `tp_ptm`, `tp_pg`: 309 files), generated twice in a scratch copy of the module: with the compiler installed before T38–T47 (the original `2.6.0-SNAPSHOT` jar) and with this branch's compiler. **Both fail identically in both**, so none of it is caused by this branch's changes. The two generated trees differ only in the examplar timestamps and in three lines (below). Also run: the compiler module's `src/test/python/test.py` and `test2.py` (Makefile `py`, `py2`; `PYTHONPATH=target/libs/python:src/test/python:../prov-template-library/src/main/python`, the last for the `past` package, which the Makefile does not say): `test.py` passes; `test2.py` fails at its first import.

**The failures.**
1. `test.py` (library): `NameError: name 'Logger' is not defined`: line 2 is `#from ….plead.client.logger.Logger import Logger`, commented out, and the body calls `Logger.logPlead_approving`. With the import enabled (a scratch copy) it passes.
2. `test2.py` (library): `AttributeError: type object 'int' has no attribute 'valueOf'` in `Plead_approvingBuilder.examplar()`: the Python emitter writes a Java static call as written. In the library's generated Python: `int.valueOf(` 205 times, `str.concat(` 16, `str.replace(` 12 (works as `str.replace(x, a, b)`), `float.valueOf(` 4, `str.valueOf(` 3. With `int(`/`float(` substituted (a scratch copy) the test goes on to fail at: `'Plead_approvingBuilder' object has no attribute 'processorConverter'`: Python has no overloading, so the emitter names the overloads `processorConverter_function` and `processorConverter_plead_approving_processor`; the test calls the Java name.
3. compiler module `test2.py`: `ModuleNotFoundError: org.example.templates.block.logger`: the generated Logger is `org.example.templates.catalogue.client.logger.Logger`; with the path corrected (a scratch copy) it passes.

**Found on the way, and fixed by T38**: with the original compiler the library's three `LocalEnactor.py` (pg, ptm, plead) had `super().unknown(BeanLocalEnactor3(...), List())`; they now have `super().__init__(...)`. No other difference in the generated Python.

**Not run by the build**: the library pom's `python.test` execution is commented out, so nothing ever ran them since the PAST emitter replaced the earlier Python writer. **Proposed**: (a) map the Java static calls the library's templates use in the Python emitter (`Integer.valueOf` → `int(...)`, `Double.valueOf`/`Float.valueOf` → `float(...)`, `String.valueOf` → `str(...)`, `String.concat` → `+`), with a test that compiles and imports the generated Python; (b) update the tests (the imports, the overload names), or retire them; (c) decide whether `python.test` runs in the library's build. Related: T33 (fixed): the shape is now `None if record[5] is None else int.valueOf(...)`. **Not done**: any of it.

---







### ⬜ T41: The flow entry expresses more requests

**Priority**: ⬜ BACKLOG.  **Category**: Template compiler / Flow.  **Opened 2026-11-16**, at Luc's word relayed by the session that started this one ("Delegate the extension to the T36 session"; ticket chosen by him: "New ticket T41 (Recommended)"). Branch `claude/t41-flow-extension`, from `origin/development2_0` at `78e095819`.

T36's `flow` entry (design §13) came from one example: one request, one previous record of the same template as the one made, one record made. The first application tried it on all of its 46 commands: 2 are expressible; 44 are not, for four reasons: **NS** (12 commands: nothing stood: `stood` may not be empty), **XT** (18: the record that stood was made by another template), **MR** (10: one request makes several records, some of them a number that depends on the request), **LD** (4: statements written through a ledger enactor, not a document template).

**Design**: `docs/T36-INJSON-SUBTYPES-DESIGN.md`, part F (§14); rulings G1 to G5 and the findings recorded there (§14.7), what was built in §14.10.

**DoD.**
- A flow with nothing that stood (NS), a stood record of another template (XT) and a flow of several records in order (MR) can be declared, each with a real case in the test fixture (made-up names).
- Every input of every record is still accounted for exactly once, the six kinds stay, each assignment marked with its kind in every language.
- Compatibility: the stage 9 declarations compile to the same classes (or to classes shown equivalent); a catalogue without a flow is unchanged.
- LD: what it would take is reported to Luc; it is not built unless he says so (G5: not built; the application declares its ledger statements as templates).
- The compiler findings of the first application are put to Luc as tickets or fixes (not fixed silently): done: finding (2) fixed in T41, (1), (3), (4) are T42, T43, T44.

**Built** (branch `claude/t41-flow-extension`, from `origin/development2_0` at `78e095819`, 2026-11-16; never pushed):
- **Stage A, NS**: a flow may stand on nothing (G1). **Stage B, XT**: a stood record is typed by its own template, `of` any subtype or template (G2). **Stage C, MR**: `records`, chaining from an earlier record's bean and outputs, one method for each record, replies naming their record (G3); the stage 9 members stay as the shorthand. **Stage D**: the type checker knows the JSON text runtime's types.
- Tests: `T36FlowTest` (26), `T36FlowCompatTest` (1: the stage 9 declaration compiles to the same Java, Python and JavaScript, against goldens), the fixture with three more templates (`exchanging`, `registering`) and a case for each gap with made-up names.
- **Continuation (2026-11-17)**: a request that gives nothing has no request class and `ask` takes none; `rest: absent` narrows a shared subtype (G7a); the composite record (G6): `composite`, `elements`, `shared`, three methods, the application loops (design §14.12 to §14.15). `spread` is T45.
- **Not verified**: the Python and JavaScript are parsed, not run; the first application's own 44 commands are not run through the new shapes; two flows of one request tied by the application (G4) are the application's.

---





### ⬜ T36: `@inJson` values and template subtypes

**Priority**: ⬜ BACKLOG.  **Category**: Template compiler.  **Branch**: `claude/t36-injson-subtypes-d38753` (off `development2_0`; the app named it).
**Filed 2026-11-16** (Luc), from the first application's design of user-interaction records. **Design**: `docs/T36-INJSON-SUBTYPES-DESIGN.md`, proposed
and ruled 2026-11-16 (R1–R11, below and design §8).
**Checked against the code 2026-11-16** (design §11): one real correction. The `subtype` entry cannot live in
`configs.templates`: 47 loops in 31 files take a non-simple entry for a composite and 13 cast it. Luc ruled (R11) that it is a third
class that every loop skips, not split out.

**The need.** Applications use one compiled template for many kinds of act (nextgen: 73 templates for 213 act types)
and put what differs in text variables holding a JSON object (`attributes`, `pattributes`), because a new key there
changes no table and no bean. Nothing declares those keys, and every caller writes them by hand.

**Asked** (Luc, 2026-11-15 and 2026-11-16):
- "we could introduce new annotations, @inJson, indicating which pattributes it gets encoded in, under which
  variable. These annotations would not change the java api to the template or its sql annotation, but it would
  allows to encode all the values encoded in a template."
- "can a compiler generate the user beans like we do with the prov-template-compiler, using the roster supplementary
  definitions. The ttl files should also define the subtypes and their relation to the general template."
- "The compiler extension would have to generate PAST expressions so that generated beans can be compiled to
  multiple target languages."

**What it comes to.**
- **`@inJson`** on a value's descriptor names the text variable (the carrier) the value is encoded in. Such a value is
  no bean field and no column: the template's beans and SQL are what they were. It is listed in the documentation and
  the JSON schema.
- **A third kind of template, `subtype`**, in the project configuration: it names the template it refines, the inputs
  it fixes (the act type) and the in-JSON values it carries, required or optional. An application writes these entries
  from its own roster of act types; the roster is not ProvToolbox's.
- **A typed bean per subtype**, built as a PAST class and emitted for each target language: one typed field per
  carried value, a conversion to the general template's input bean (which writes the carrier's JSON text) and one
  back. The JSON text is written through one small runtime class per language, as `@escape` reaches `escapeCsv`.
- **No server side for a subtype**: no table, no insert function, no builder. Its record is the general template's.
- **The descriptions**: documentation, JSON schema, and a Turtle file declaring each subtype as a subclass of its
  general template with what it fixes and carries.

**Touches**: T33 (Python wrote `null` as `0`: an absent optional value; fixed and retired 2026-11-19) and T34 (the generated jsweet build; fixed and retired 2026-11-19); Rust
has no mapping for a static runtime call.

**DoD.**
- A catalogue that uses neither `@inJson` nor `subtype` compiles to exactly what it compiled to before.
- Adding in-JSON values to a template changes its documentation and schema and no other generated file.
- For a case table of subtypes: the typed bean compiles in each language emitted, converts to the general bean and
  back without loss, refuses a missing required value, and the carrier's text is the same from each language.
- The provenance-templates specification says what `@inJson` and a subtype are (stage 6, with parts D and E: committed `fb01ff2` on the branch `t36-injson-subtypes` of the specification's repository, 2026-11-16, at Luc's word; never pushed).

**Rulings** (Luc, in his words, dated, as given):
- **R3, in effect (2026-11-16)**, written while R1–R3 and R11 were being asked: "It should be our expectation that
  @InJson does not declarate all variables. Additionally, we have to allow for extensibility, for attributes that had
  not been anticipated as design type." So a carrier is open: the typed bean keeps the other keys in a map per carrier
  and writes them back (design §4.1, §5.2, §5.3). Three details are put back to him (the design's R3).
- **R1, R2, R4–R11, given 2026-11-16** (answers to the questions put to him, quoted as the option he picked):
  R1 "Separate list at loading"; R2 "Allow @inJson.key"; R3's three details "All three as stated"; R4 "Fields only,
  conversions not in Rust"; R5 "Accept, with RFC 8785 numbers"; R6 "Own ticket after T36"; R7 "Compiler writes it; name
  from subtype's name"; R8 "Refuse the combination"; R9 "Generate it per catalogue now" (a dispatcher is in T36);
  R10 "Accept as proposed"; R11 "Third class, all loops skip it" (47 loops in 31 files taught to skip it). R9 and R11
  are the two where he did not take the proposal. Table in the design, §8.
- **R12–R16, ruled 2026-11-16**: part D of the design (§12), added at Luc's word ("Add the asking
  and the answer classes to the T36 design. Does it require change to the declarations?"). Two more generated classes
  per subtype: the request (only what the caller gives) and the reply (only what the service returns), each copied into
  and out of the typed bean. It needs one optional annotation on the values concerned; no existing declaration has to
  change. **R14, Luc: "go for performative"**: the annotation is `@performative` (first proposed as
  `@side`), and with it, as proposed to him and not worded by him, the values `request` and `reply`, the classes
  `<Name>Request` and `<Name>Reply`, and `tpl:performative` in the Turtle. **R16, Luc: "and then request
  the subsession to implement"**: part D is stage 7 of T36, built on its branch by its own session. Then, asked in this
  session: **R12 "Annotation on the variable (Recommended)"**; **R13 "Map only for all-request carriers (Recommended)"**
  (the request class has `<carrier>Other` where every value the template declares in the carrier is a request value);
  **R15 "Yes, a term for it (Recommended)"** (an ordinary value that has a performative has a Turtle term); **the methods
  "into and of (Recommended)"**. Table and text in the design, §12.8.

**Built** (branch `claude/t36-injson-subtypes-d38753`):
- **Stage 1, `@inJson`, 2026-11-16** (R1, R2, R3 given; nothing else was needed):
  - Descriptor: `@inJson` and `@inJson.key` on `AttributeDescriptor`; `@inJson` is also read on `NameDescriptor`, only to refuse it by name.
  - Split at loading (R1): `InJsonSplitter.split`, called by `CompilerUtil.getBindingsSchema(String)` right after the file is read and
    before the privacy policy. `TemplateBindingsSchema.getVar()` keeps the ordinary variables, so no generator changed;
    `getInJson()` and `getInJsonOf(carrier)` hold the in-JSON values in the order of the file (`InJsonValue`).
  - Refusals, each naming the file, the template and the variable (`InJsonException`, "T36: …"): carrier absent, a node,
    itself in-JSON, or not `xsd:string`/`json`; `@inJson` on a node; no `@type` or one `getPastClassForType` does not know; an
    empty `@inJson` or `@inJson.key`; `@inJson.key` alone; a repeated carrier and key; and `@inJson` with `@sql.type`,
    `@sql.foreign`, `@unique`, `@escape`, `@output`, `@transient` (R8) or `@pseudonym` (R8). The same key in two carriers is accepted.
  - Listing: the documentation page lists each carrier's keys under it, saying other keys are allowed; the generated JSON
    schema adds them to the carrier's description (its type stays a string, and no property is added). A machine-readable
    form waits for the Turtle (stage 5).
  - The bindings schema of `prov-template` accepts the two members (see T37 for what else it lacks).
  - Tests: `T36InJsonTest`, 7 cases. The whole module: 175 tests, 0 failures (168 before).
- **Stage 2, the `subtype` kind, 2026-11-16 (R11: a third class that every loop skips, committed `f60c1c077`)**:
  - `SubtypeTemplateCompilerConfig` (`refines`, `fixed`, `carries`), `@type: subtype` in `TemplateCompilerConfig` and
    `TemplatesProjectConfiguration`, `TemplateConfigurationEnum.isSubtype`, and `SubtypeTemplateCompilerConfig` in the project
    schema `template-project-schema.json`.
  - All 47 loops over `configs.templates` (31 files) start with a skip of a subtype entry; the two other uses (the number of
    builders in `CompilerBuilderInit`, the template names in `CompilerLogger`) filter it. Lookups by name are safe because the
    checker refuses a subtype whose name or fully qualified name is already used.
  - `SubtypeChecker`, called by `ConfigProcessor.processTemplateGenerationConfig` after `setupPrivacy` (`SubtypeException`,
    "T36: subtype …"): refines nothing or a non-simple template; a fixed variable that is unknown, a node, an output, not an input,
    a carrier, an in-JSON value, or whose constant does not fit its type (or is null); carries a name that is no in-JSON value,
    or a mode other than `required`/`optional`; two subtypes of one template fixing the same values; a reused name; a missing member.
  - Tests: `T36SubtypeTest`, 5 cases (parsing, 19 refusals, and generation through the whole pipeline). The whole module: 180 tests, 0 failures.
- **Stage 3, the typed bean in Java, 2026-11-16 (committed `f60c1c077`)**:
  - Runtime `org.openprovenance.prov.utils.JsonText` in prov-model (R5: RFC 8785 numbers; objects written in the order of the map, declared keys first):
    `write`, `read`, `parse`, `toText`, `newObject`, `merge`, `others`, `getString/Integer/Long/Double/Float/Boolean/Json`, `same`. Its own parser, no dependency.
    The conformance vectors (29 canonical, 14 refused, 15 getters) are `src/test/resources/t36/json-text-vectors.json`, to be run by every port.
  - `CompilerSubtype`, all PAST: per subtype a class in the package of the refined template's input bean (`Approval_bigInputs`), with `isA`, the constant
    `refines`, a field per ordinary input (not the fixed ones, not a carrier it carries in), a field per carried value, a `<carrier>Other` map per such carrier, `toGeneral()`
    and static `fromGeneral(general)`; and the per-catalogue dispatcher `Subtypes` (R9) with `which<Template>` and `fromGeneral<Template>` (the subtype with most fixed
    values first). Generated before the client POM, which then depends on prov-model (only when there is a subtype).
  - Rust (R4): the typed fields and `isA`, no conversion, no constant, no other-keys map, no dispatcher; the file was checked with `cargo check` alone with serde (not in the generated project, which has no `Cargo.toml`).
  - The checker also refuses a subtype for a catalogue with `"integrator": false` (the input beans exist only for an integrator catalogue) and one carrying values in a carrier that is not an input.
  - Tests: `T36JsonTextTest` (4), `T36SubtypeTest` (now 9: the generated beans are compiled with `javax.tools` against the generated tree and run by reflection: conversions, round trip with an undeclared key,
    required values, clash, fixed-value mismatch, a `json` value nested, the dispatcher). The module: 188 tests, 0 failures, prov-model's tests too.
- **Verified, stage 3**: catalogues without a subtype are unchanged (T31's 25 cases, 2410 files, and `CompilerTest`'s 93 source files equal the base commit's, as above, run with `CompilerTest` passing).
- **Stage 4, Python and JavaScript, 2026-11-16 (committed `f60c1c077`)**:
  - Python: the port `org/openprovenance/prov/utils/JsonText.py` in `prov-template-library/src/main/python` (with `JsonObject`, an ordered `dict` with the `put`/`containsKey` the emitted code calls). It gives the 58 vectors
    their answers (`run_json_text_vectors.py`).
  - JavaScript: the candy `JsonText.java` in `prov-jsweet-candy-java` (JavaScript's own number text is RFC 8785's: `"" + d`), transpiled by the module's own build into `dist/` (533 lines added, nothing else changed). It gives the
    58 vectors their answers in node (`run_json_text_vectors.js`). Limit: JSweet keeps a string-keyed map as a JavaScript object, so a key that reads as an array index (`"1"`) is written before the others.
  - Emitters (PAST, for every emitter that needed it): the Python and JavaScript emitters no longer import the class being emitted (a typed bean that makes one, or whose static method calls its own class,
    imported itself: a circular import in Python, a redeclared `const` in JavaScript). Nothing else changes: see the comparison below.
  - The emitted Python of the two typed beans and the dispatcher, run with python3 against the port (`check_typed_beans.py`): conversions, round trip with an undeclared key, nested `json`, clash, fixed-value mismatch, wrong type, dispatcher.
    **Was blocked by T33, fixed 2026-11-19 (no `BLOCKED` line is printed now)**, said by the test (`[T36] BLOCKED T33 …`, not a failure): an absent required value is not detected (`x == 0`), an absent optional value is written as `null`, and the dispatcher's "none" is `0`.
    **Blocked by T38**: the whole pipeline cannot write Python for a subtype catalogue with assertions on (an integrator catalogue is needed for subtypes), so the test emits the generated PAST classes with the Python emitter directly.
  - The emitted JavaScript of the typed bean parses (`node --check`). Its `require` of `org/openprovenance/prov/utils/JsonText.js` is provided by nothing, as for any static call of the PAST JavaScript route (`escapeCsv` is no different).
  - The jsweet route, checked by hand (not a test): the generated Java of the typed beans and the dispatcher, with the candy and a stand-in general bean, transpiled by jsweet 3.1.0 in a scratch project (the candy's own `pom-jsweet.xml`, with
    the same `MAVEN_OPTS`), and run in node: conversions, round trip, other keys, nested `json`, required, clash, fixed-value mismatch, wrong type, dispatcher (22 checks, 0 failed). **T34** blocks doing this through the generated client project.
  - Tests: `T36SubtypeTest` is 13 cases (the four ports' tests skip with a message when python3 or node is absent). The module: 192 tests, 0 failures.
- **Stage 5, the descriptions, 2026-11-16 (committed `f60c1c077`)**:
  - Documentation: under each template that has subtypes, a list of them: the fully qualified name, the typed bean, what each fixes (`name = value`) and carries (key, carrier, type, `required`/`optional`, documentation), HTML-escaped. Nothing for a template without.
  - JSON schema: for each subtype a definition `<subtype fqn>` for its typed bean (`CompilerJsonSchema.generateSubtypeJSonSchema`): `isA` of its own, the inputs kept as fields of their own (not the fixed ones, not the carriers),
    each carried value as a typed property (a `json` value as a string, said to be JSON text; its key and carrier said in the description), each carrier's other keys as an object with free members, `required` = `isA`, compulsory inputs and
    required values, `additionalProperties: false`, and the `if isA then $ref` block. The general template's schema is as it was.
  - Turtle (R7, ruled): two new catalogue members, `turtle` (the file, written beside the JSON schema) and `turtleNamespace` (the IRI of the terms; refused when absent; names mine: R10 gave none for these). `CompilerTurtle` writes the `tpl:` vocabulary
    (`http://openprovenance.org/ns/tpl#`: `fullyQualifiedName`, `inJson`, `key`, `fixes`, `variable`, `value`, `requires`, `mayCarry`), then for each simple template with in-JSON values or subtypes an `owl:Class` named from its `name`, each in-JSON value as a datatype
    property `<template>-<value>` (domain, range, `tpl:inJson`, `tpl:key` when it differs), and each subtype as an `owl:Class` named from its `name`, `rdfs:subClassOf` its template, with `tpl:fixes [ tpl:variable ; tpl:value ]`, `tpl:requires` and `tpl:mayCarry`.
    A property named after the template and the value, not the value alone, so that two templates may use one name (a property with two domains would belong to both).
  - Tests: `T36DescriptionsTest` (6): the documentation (taking the subtypes out gives the page without), the schema (its properties, types, required, and records valid and invalid against the subtype's definition with networknt), the Turtle (text, and read by rdflib when installed, 28 checks).
    The module: 198 tests, 0 failures. The test fixture moved to `T36Fixture`.
  - The project schema accepts `turtle` and `turtleNamespace`.
- **Verified, stage 5**: catalogues without subtypes, `turtle` or `turtleNamespace` are unchanged (T31's 25 cases, 2410 files, and `CompilerTest`'s 93 files, against the base commit, as above).
- **Not verified, stage 5**: the Turtle with another parser than rdflib; the documentation page in a browser; `ajv` on the project schema; the JSON schema of a subtype in jsonform (the schema is used by a form generator).
- **Committed on 2026-11-16 at Luc's word, on my branch only (never pushed)**: `9f19ddcf9` the loop-skip lines, the config class and `isSubtype` (32 files, compiled alone in a clean worktree); `f60c1c077` the rest of stages 2 to 5 (30 files); the next commit, this file and the design. Stage 6 is in the specification's repository, uncommitted.
- **Stage 6, the specification, 2026-11-16 (committed `fb01ff2`, with the text of parts D and E, on the branch `t36-injson-subtypes` of the specification's own repository, provenance-templates; never pushed)**:
  - `provenance-templates-specification/prov-template/index.html`: two rows in the table of annotations (`@inJson`, `@inJson.key`); a section **In-JSON values** (`#in-json`: what an in-JSON value and a carrier are, the refusals as MUST NOT, a carrier is open, the text of a value by RFC 8785's number rule with
    declared keys first); a section **Subtypes** in the template compiler's description (`#template-subtypes`: the catalogue entry and its rules, the typed beans and their two conversions, the `Subtypes` class, the JSON text class, the documentation, the JSON schema, the Turtle and its `tpl:` vocabulary);
    a sentence in the compiler's introduction; a line in "Changes since PROV-TEMPLATE 2014-06-07".
  - `prov-template/schemas/bindings-schema.json` (the specification's copy): `@inJson` and `@inJson.key` on the value descriptor.
  - Editor's notes on the page, as T31 did, say what disagrees: the schema's `@type` list (five types) is narrower than the compiler's (it rejects `xsd:double`, `xsd:long`, `xsd:date`, `json`); in Rust the typed bean has fields and no conversions; Python writes an absent optional value as 0; the generated JavaScript build transpiles nothing.
- **Verified, stage 6**: the page's sections, tables and lists are balanced, its ids are unique and every `#` link on it resolves; the schema parses; `ajv` accepts a bindings file with `@inJson` and `@inJson.key` against the new schema, rejects it against the schema before (additional property), and rejects `"@inJson": 3`.
- **Not verified, stage 6**: the page rendered by ReSpec (it loads ReSpec from w3.org; not opened); the bibliography entry `[[RFC8785]]` resolving; `ajv` on T36's own fixture files (they use `xsd:double`, which the schema's type list rejects, as above, and `@escape`-style members the specification's note already lists); the text of the specification read by anyone but me.
- **Stage 7, the request and the reply (part D), 2026-11-16 (committed, the commit after `97f4b566f`)**:
  - `@performative` (`request` or `reply`) on `AttributeDescriptor` (read on `NameDescriptor` only to refuse it); `PerformativeChecker` refuses, naming file, template and variable: a node, a word other than the two, an output, a value that is no input
    (a refusal the design did not list: such a value would be in neither class), a carrier that holds in-JSON values. It is allowed on an ordinary input value and on an in-JSON value. Added to `bindings-schema.json` of `prov-template`.
  - Per subtype, `<Name>Request` and `<Name>Reply` (`CompilerSubtype.generateRequestReplyClasses`), PAST classes through the existing emitters: a field per value, `into(typed)` and static `of(typed)`, no runtime class. The request has `<carrier>Other` for a carrier all of
    whose template-declared values are request values (R13); `into` copies it when present. A class is generated only when the subtype has a value of that kind.
  - Descriptions: the documentation says each value's performative and groups a subtype's values (request, reply, the service's own); the JSON schema has the definitions `<subtype fqn>Request` and `Reply` (no `isA`), and says the performative in a value's description;
    the Turtle declares `tpl:performative` and gives it to the properties of in-JSON values and, R15, of ordinary values (which are then described, their template too). Nothing is added for a catalogue without `@performative`.
  - Rust: tried, and not possible without extending the Rust emitter, which was not done: it writes `typed.f = Some(self.f.to_string())` (does not compile for an `Option` field; `cargo check`). The Rust classes are fields only, with a comment saying why.
  - Tests: `T36PerformativeTest` (12), `check_request_reply.py` and `.js`. Module: 209 tests, 0 failures (run with `-am`, so that `JsonText` is on the class path: without it the tests that compile the generated Java are skipped with a message).
  - The specification (in its own repository, committed with stage 6): `@performative` in the table and the schema, a section Performatives (`#performative`), a section Requests and replies (`#requests-replies`), a line in the changes.
- **Verified, stage 7**: T31's 25 cases (2410 files) and `CompilerTest`'s catalogue (93 files) equal the base commit's (a worktree at 0a8d9f4b1, same normalisations and method as above); the same catalogue with and without the annotation differs in exactly the four classes, `doc.html`, `schema.json` and the copy of the bindings schema;
  the Java classes compile and run (the Jackson body of the design's §12.1 cannot set a value the caller does not give); the emitted Python and JavaScript run against a stand-in typed bean; a request body is valid and invalid against the request definition (networknt); the Turtle is read by rdflib.
- **Not verified, stage 7**: the Turtle with another parser than rdflib; the request schema in jsonform; the classes in a real application's build (the first application's worked example was read, not compiled with the new classes); T33 (Python `into` copies `None` over the typed bean's map of other keys when the request has none) and T34 (no JavaScript client is built) are not fixed;
  the specification page rendered by ReSpec; `ajv` on the specification schema was run on one file with the member, accepted, and one with a wrong word, refused.
- **Stage 8, two findings of the first application, 2026-11-16 (rulings F1 and F2, Luc: "Yes, fix a node's local part (Recommended)", "Honour the declared package (Recommended)")**:
  - A subtype may fix a node input by its local part (a text without prefix): `SubtypeChecker` accepts it (an output or a non-input is still refused, with the right message), the typed bean has no field for it, `toGeneral`/`fromGeneral`/the dispatcher and the Turtle treat it as a fixed text.
  - The typed bean, the request and the reply are generated in the package the subtype declares (under `.client.integrator`), no longer in the general template's; `ConfigProcessor` registers the subtype's package, the dispatcher imports from it.
  - Tests: `T36NodeAndPackageTest` (3); T36's fixture subtypes now declare the general template's package, so the existing tests keep their paths.
  - Part E of the design (§13, the flow) is written, its rulings E1 to E6 recorded (§13.8). The flow itself is stage 9.
- **Stage 9, the flow (part E of the design), 2026-11-16 (rulings E1 to E6)**:
  - A new kind of catalogue entry, `"@type": "flow"`, a `ClientSideTemplateCompilerConfig` like the subtype (the 49 skip sites now call `TemplateConfigurationEnum.isClientSide`). It names the user subtype (`from`) and the backend subtype (`to`), the things that stood
    (`stood`: node, madeBy, addressed, identity, of), and says for each input which of six kinds it is (as asked, chained, carried over, constant, supplied, as made; and absent). `FlowModel` refuses a declaration that leaves an input out or names one twice, with the flow, the value and the places named.
  - `CompilerFlow` writes PAST: a class with `ask`, `record`, `answer`, `refused`, `failed`, one assignment per origin each preceded by a COMMENT statement naming its kind, the two guards, and an interface `<Flow>Rules` with a method per supplied value (the beans in scope as arguments). PAST needed nothing added. Java, Python, JavaScript; Rust none (E6).
  - `JsonText.text(value)` (a number or a boolean as the carrier writes it, a string as itself, null as null) in Java, the Python port and the candy (dist rebuilt), with 13 vectors (71 in all); `2.50` is `"2.5"` until a decimal type (R6).
  - The documentation lists each flow as a table of where each value comes from; the Turtle describes it (`tpl:Flow`, `tpl:from`, `tpl:to`, `tpl:origin` with `tpl:step`, `tpl:target`, `tpl:kind`, `tpl:source`; the names are mine); nothing in the JSON schema. The project schema has the entry.
  - Tests: `T36FlowTest` (12) and `T36FlowFixture`: the generated Java compiled and run against stand-in rules and previous records (each kind, the open keys copied and the act's not, the guards, the replies, a changed price changes exactly the two values that came from it), the kinds beside the assignments in Java, Python and JavaScript, the Python and JavaScript parsed, the documentation, the Turtle read by rdflib, a catalogue without the entry unchanged but for the documentation, 37 refusals. `T36NodeAndPackageTest` has a fixed `xsd:int` node.
  - The real case: the first application's flow, written in this declaration against a copy of its catalogue in a scratch directory, generates the 31 assignments of the hand pattern and compiles against the beans generated for it.
- **Verified, stage 9**: T31's 25 cases (2410 files) and `CompilerTest`'s catalogue (93 files) equal the base commit's (a worktree at 0a8d9f4b1, same normalisations and method as above); the module's 226 tests pass.
- **Not verified, stage 9**: the emitted Python and JavaScript flows are parsed, not run (T33 spoiled the Python guards and T34 stopped a JavaScript client: both fixed and retired 2026-11-19; still parsed, not run); the flow was not run against a store; `of` on a stood thing, two flows in one catalogue and a user subtype with no reply value are not tested; the Turtle with another parser than rdflib; the table in a browser; the specification page rendered by ReSpec.
- **Verified, stage 4**: catalogues without a subtype are unchanged, Python files included (T31's 25 cases, 2410 files, and `CompilerTest`'s 93 source and Python files, against the base commit, as above).
- **Not verified, stage 4**: the generated jsweet client project built as a whole (T34); numbers beyond 2^53 (the vectors avoid them: Java and Python read them exactly, JavaScript cannot); a JavaScript or Python `float` for `xsd:float` (they have one number type; Java's `Float`
  writes its own shortest digits).
- **Not verified, stage 3** (now partly checked, see stage 4): the Python and JavaScript output of the typed bean (stage 4: it shows `.put(` on a map, `== 0` for null (T33), and an import of its own class, which are emitter matters), the generated project built by Maven
  with the new prov-model (not installed to `~/.m2`: the tests run in one reactor with prov-model), Rust beyond the single file.
- **Verified, stage 2**: a catalogue with two subtype entries (before and after the simple template, and with a composite beside
  them) generates the same tree as without them, file for file. Taking the skip out of one loop makes that test fail. And a
  catalogue using none of T36 compiles as before: T31's 25 generated cases (2410 files) and `CompilerTest`'s catalogue (93 source
  files) equal the base commit's after the earlier normalisations and the generators' own "at line N" comments, which my inserted
  lines shift.
- **Not verified, stage 2**: the project schema was only checked as JSON, not run through `ajv` (the plugin's `validate-catalogue`).
- **Verified, 2026-11-16** (stage 1): with in-JSON values added to a carrier of T31's approval template, the generated tree differs from
  the tree without them in exactly `doc.html` and `schema.json` (apart from the verbatim copy of the bindings file), and no
  other generated file mentions an in-JSON value's name. And a catalogue using none of T36 compiles as before: T31's 25
  generated cases (2410 files) and `CompilerTest`'s catalogue (93 source files) are identical to the base commit's, run
  in a second worktree at 0a8d9f4b1 and compared after the normalisations of T31's R14 (build date, `tmp_Conv`
  counters, JSON key order) and the absolute path of the run.
- **Not verified**: the Python, JavaScript and Rust trees of the second comparison beyond what the first two lists cover
  (the Python files of `CompilerTest`'s catalogue are in it; Rust and jsweet output are not generated by either).
  A reader of a `cbindings` file outside the compiler (the webjar copies it verbatim, with the `@inJson` entries).

---



### ⬜ T30: ttf execution: one dot run per layout

**Priority**: ⬜ BACKLOG.  **Category**: Template / Build.
**Filed 2026-10-24**, what T29 (`DONE.md`) left: after it, the time of a template tasks file is the `dot` runs that remain,
one per image output, overlapping where the cores allow.

- One `dot` run per layout: png and svg (and qualified.png/qualified.svg) of a document share a layout; `dot -Tpng -o a.png
  -Tsvg -o b.svg f.dot` lays out once — `ProvToDot` would gain a multi-output `convert`, and the tasks a way to group the
  formats sharing a `ProvToDot` configuration (prov-template does not depend on prov-dot: a small interface in prov-model,
  or a `layoutKey` on the serialiser). In provenance-templates-library ~265 pairs, so half the process starts; the gain is
  the process start and the layout, dearer on macOS, smaller when the renderings already run concurrently.
- `validate-ttf` (`TtbSchemaValidator`) starts one `ajv` (node) process per ttf, 26 per build in the library's first
  execution: validate every ttf in one `ajv` run (`-d` repeated), or in the JVM with networknt, which `SchemaCheck` already uses.
- `ProvToDot.convert(Document, OutputStream, type, title)` neither drains nor waits on `dot`'s stderr: a chatty `dot` could
  block on a full pipe, more so now that several run at once; redirect stderr or read it on a thread, and `waitFor`.
- `MergeTask` re-reads its inputs from disk although the tasks that generated them left them in the executor's document cache;
  reading them is cheap (~1 s in all) and `IndexedDocument.merge` may mutate the statements it is given, so leave as is unless profiled.
- The library's `pom.xml` also runs 47 `provconvert` executions and 24 `exec` ones at `test`; they are outside the ttf path
  and were not measured.

**DoD.** The png and svg of one document come from one `dot` run; `validate-ttf` starts one process per batch; the timing
harness of T29 (a `BatchExecutor` subclass timing tasks and serialisers) shows the ttf time as the necessary `dot` runs.

---

### 🟡 T28: The Browser tab checks every record's hash at once

**Priority**: 🟡 MEDIUM.  **Category**: Service / Dashboard.
**Filed 2026-10-16** (found retaking the book's Browser screenshots, `book-ptm/project/service-book/src/test/js/fs.screenshotter.js`, Luc: "add this task in the provtoolbox todo").

**What happens.** `forms.html` (`modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/webapp.filter/forms.html`),
`submitBrowse()`: the records come back from `POST provapi/templates/records` and fill a DataTable whose hash column renders
through `cleanSignature()`, which calls `getRecordAsCsv()` the first time it sees a signature — one `GET provapi/template/<template>/<id>.csv`
per record, the page re-hashing the first CSV line and appending a green tick or a red cross to the signature's `div`.
DataTables renders every row when the table is built (no `deferRender`), so every record of the result is fetched at once,
not only the ten on the page.

**Measured** on the book service (`localhost:7075/book`, 2026-10-16), base relation `activity`, limit 10000, To date
2026-10-15: 1,959 rows, ~1,800 CSV requests sent in one go; headless Chromium completed ~1,400 and failed ~400 with
`net::ERR_INSUFFICIENT_RESOURCES`. The failed ones included the rows on screen, which stayed with no tick or cross for good:
`xhr.onerror` only logs, `seen[sig]` stays undefined and nothing retries. A desktop Chrome showed the ticks for the same query,
so whether the rows on screen survive depends on the browser and the order the requests come back.

**Also seen in the same session** (to be confirmed, not yet diagnosed):
- With a limit, the records list keeps the *oldest* N records from the From date, while the table sorts newest first — with
  limit 100 or 1000 the first page showed records from March 2026, not the latest ones.
- Records before 2026-05-01 do not reproduce their hash (red crosses); those of 2026-05-01 and later do.
- Composite rows (`org.openprovenance.templates.fs.FileTransformingComposite`) have no hash and widen the `table_name` column.

**Workaround in place** (book scripts only): `fs.screenshotter.js` / `fs.browser.screenshotter.js` wait for the verdicts and,
after 15 s, rerun the page's own `getRecordAsCsv` for the rows on screen still without one (`waitForHashVerdicts`).

**Fix (proposed).** Check only what is drawn: compute the verdicts on the table's `draw` event for `rows({page: 'current'})`
(with `deferRender: true`), keeping `seen` as the cache; or queue the checks with a small number in flight. Either way, a
failed check should be shown (and retried), not left blank.

**DoD.** With ~2,000 records listed, every row on screen gets its tick or cross in headless Chromium without the book
script's recheck, the page sends at most a page's worth of checks per draw, and a failed check is visible as such.

---

### 🟡 T26: log2prov cannot read composite template records

**Priority**: 🟡 MEDIUM.  **Category**: Template compiler / log2prov.
**Filed 2026-10-09** (found from provenance-templates T-5).

1. Generated `Init` (`CompilerBuilderInit.generateInitializer`) sizes `builders`/`typeManagers` for every
   catalogue entry but fills only the simple templates; composite slots stay `null`, and
   `FileBuilder.registerBuilders` calls `Class.forName(null)` → `NullPointerException` (`FileBuilder.java:178`).
   Repro: `provconvert -infile x.csv -log2prov org.openprovenance.templates.catalogue.transport.Init -outfile x.provn`
   with provenance-templates-library on the classpath (transport catalogue: 9 slots, 7 filled,
   `packing_composite`/`unpacking_composite` null) dies before reading a line. Fix: skip null slots at least,
   and emit a FileBuilder for composites.
2. A composite record's first field is the fully qualified name (`logPacking_composite` writes
   `org.openprovenance.templates.transport.PackingComposite`), but `FileBuilder.registry` is keyed by
   `getName()` (`packing_composite`); simple records use the name. Make them agree.
3. Composite `args2csv` (`CompilerCommon.generateCommonCSVConverterMethod`) joins header and element records
   with a literal two-character `\n` (`sb.append("\\n")` in the generated source), so `FileBuilder.reader`'s
   `CSVParser` sees one record with fields run together (`type\npacking`, ...). Emit real line breaks
   (or one record per element tied to the header) and have the reader regroup them.
**From T32 (2026-11-11, retired):** `FileBuilder.registerBuilders` skips a null slot with a warning (item 1's
trivial part), and the registry is keyed by fully qualified name and short name (item 2, once composites have a
builder). Still open here: a FileBuilder for composites, the composite `toBean`'s `List<Object[]>` parameter (log2prov's
proxy calls it with an `Object[]`), the literal `\n` in composite records.
DoD: a log of `packing_composite` records round-trips through `-log2prov` to the same PROV as the composite's expansion.

---

### ⬜ T21: Bindings files in yaml format

**Priority**: ⬜ BACKLOG.  **Category**: Templates / Bindings.
**In TODO.md by 2026-05-24.**

Accept bindings files in yaml format, update schema validation, and add -bindingsformat option to template instantiation, update service instantiation page, change chapter 5 book entry.

---

### ⬜ T2: TableConfigurator generic per method

**Priority**: ⬜ BACKLOG.  **Category**: Template compiler.
**In TODO.md by 2026-04-15.**

template-compiler: Revisit TableConfigurator so that generic type is per method, rather than class level, allowing unnecessary casts.

```
public interface TableConfigurator {
  <T>T pg_capturing(Pg_capturingBuilder builder);
}

public class CsvConfigurator4Outputs implements TableConfigurator {
/**
* Gets configuration
* @param builder builder for template pg_capturing
* @return String[]
  */
  public final Function<Pg_capturingOutputs, String> pg_capturing(Pg_capturingBuilder builder) {
  // Generated by class org.openprovenance.prov.template.compiler.CompilerConfigurations, method generateConfigurator;
  // in file CompilerConfigurations.java, at line 114;
  return o -> o.process(builder.aArgs2CsVConverter);
  }
  ...
}
```

---

### ⬜ T3: script.sh points to the proper library

**Priority**: ⬜ BACKLOG.  **Category**: Template compiler / Build.
**In TODO.md by 2026-04-15.**

script.sh in prov-template-compiler needs to point to the proper library

---

### ⬜ T4: No delegation without delegate and responsible

**Priority**: ⬜ BACKLOG.  **Category**: Template service / Store.
**In TODO.md by 2026-04-15.**

should not insert delegation if delegate/responsible empty

```sql
INSERT INTO
__PROV_DELEGATION (delegate, delegate_rel, responsible, responsible_rel, activity, activity_rel, template, template_id, rel) VALUES
(,'agent',,'agent',1517,'activity','plead_filtering',152,'--0--');
```

---

### ⬜ T5: PC1FullTest in prov-dot

**Priority**: ⬜ BACKLOG.  **Category**: prov-dot / Tests.
**In TODO.md by 2026-04-15.**

PC1FullTest.java in prov-dot no longer working

---

### ⬜ T6: Upgrade Scala

**Priority**: ⬜ BACKLOG.  **Category**: Build.
**In TODO.md by 2026-04-15.**

upgrade scala version

---

### ⬜ T7: templateIT fails with Mongo

**Priority**: ⬜ BACKLOG.  **Category**: Template service / Tests.
**In TODO.md by 2026-04-15.**

templateIT fails when service is usign mongo.

---

### ⬜ T8: DeleteJob and Redis

**Priority**: ⬜ BACKLOG.  **Category**: Service / Storage.
**In TODO.md by 2026-04-15.**

Template expansion fails to clear one of the provn files (the original template)

```agsl
06:01:18.861 [main] INFO  org.openprovenance.prov.service.translator.StorageConfiguration - Configuration --- {PSERVICE_DEL_PERIOD=600, PSERVICE_REDIS_PORT=6379, PSERVICE_DBNAME=prov, PSERVICE_STORAGE=fs, PSERVICE_INDEX=redis, PSERVICE_AUTODELETE=true, PSERVICE_CACHE=200, PSERVICE_REDIS_HOST=localhost}
```
```
6:21:41.389 [DefaultQuartzScheduler_Worker-6] ERROR org.quartz.core.JobRunShell - Job graph.r26572 threw an unhandled Exception: 
redis.clients.jedis.exceptions.JedisConnectionException: java.net.SocketTimeoutException: Read timed out
    at redis.clients.util.RedisInputStream.ensureFill(RedisInputStream.java:201) ~[jedis-2.8.1.jar:?]
    at redis.clients.util.RedisInputStream.readByte(RedisInputStream.java:40) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Protocol.process(Protocol.java:141) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Protocol.read(Protocol.java:205) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Connection.readProtocolWithCheckingBroken(Connection.java:297) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Connection.getBinaryMultiBulkReply(Connection.java:233) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Connection.getMultiBulkReply(Connection.java:226) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Jedis.hmget(Jedis.java:671) ~[jedis-2.8.1.jar:?]
    at org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex.get(RedisDocumentResourceIndex.java:85) ~[prov-storage-index-redis-2.0.0-SNAPSHOT.jar:?]
    at org.openprovenance.prov.service.core.jobs.JobDeleteDocumentResource.execute(JobDeleteDocumentResource.java:37) ~[prov-service-core-2.0.0-SNAPSHOT.jar:?]
    at org.quartz.core.JobRunShell.run(JobRunShell.java:202) [quartz-2.3.2.jar:?]
    at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573) [quartz-2.3.2.jar:?]
Caused by: java.net.SocketTimeoutException: Read timed out
    at java.net.SocketInputStream.socketRead0(Native Method) ~[?:?]
    at java.net.SocketInputStream.socketRead(SocketInputStream.java:115) ~[?:?]
    at java.net.SocketInputStream.read(SocketInputStream.java:168) ~[?:?]
    at java.net.SocketInputStream.read(SocketInputStream.java:140) ~[?:?]
    at java.net.SocketInputStream.read(SocketInputStream.java:126) ~[?:?]
    at redis.clients.util.RedisInputStream.ensureFill(RedisInputStream.java:195) ~[jedis-2.8.1.jar:?]
    ... 11 more

```


```agsl
org.quartz.SchedulerException: Job threw an unhandled exception.
    at org.quartz.core.JobRunShell.run(JobRunShell.java:213) [quartz-2.3.2.jar:?]
    at org.quartz.simpl.SimpleThreadPool$WorkerThread.run(SimpleThreadPool.java:573) [quartz-2.3.2.jar:?]
Caused by: java.lang.ClassCastException: class java.lang.Long cannot be cast to class java.util.List (java.lang.Long and java.util.List are in module java.base of loader 'bootstrap')
    at redis.clients.jedis.Connection.getBinaryMultiBulkReply(Connection.java:233) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Connection.getMultiBulkReply(Connection.java:226) ~[jedis-2.8.1.jar:?]
    at redis.clients.jedis.Jedis.hmget(Jedis.java:671) ~[jedis-2.8.1.jar:?]
    at org.openprovenance.prov.storage.redis.RedisDocumentResourceIndex.get(RedisDocumentResourceIndex.java:85) ~[prov-storage-index-redis-2.0.0-SNAPSHOT.jar:?]
    at org.openprovenance.prov.service.core.jobs.JobDeleteDocumentResource.execute(JobDeleteDocumentResource.java:37) ~[prov-service-core-2.0.0-SNAPSHOT.jar:?]
    at org.quartz.core.JobRunShell.run(JobRunShell.java:202) ~[quartz-2.3.2.jar:?]

```
