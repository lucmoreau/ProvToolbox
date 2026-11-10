# ProvToolbox TODO — Outstanding Work

> **🔢 LAST ALLOCATED TASK NUMBER: T31** — the highest task id ever assigned (whether still open here or retired to
> `DONE.md`). **When allocating a new task, take the NEXT number (T32) and bump this line.** Do NOT reuse a number
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
| T31 | `@transient` and `@pseudonym("method")` on a template's variables: a transient value is never persisted (no column, no expansion, no log), a pseudonymised one is checked for its method's form at write time. | 🟡 MEDIUM | Template compiler / Data protection | [T31 section](#-t31-transient-and-pseudonym-on-a-templates-variables) |
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

---

### 🟡 T31: `@transient` and `@pseudonym` on a template's variables

**Priority**: 🟡 MEDIUM.  **Category**: Template compiler / Data protection.  **Branch**: `t31-transient-pseudonym` (off `development2_0`).
**Filed 2026-11-10** (Luc). The rule behind it: a provenance store built from templates should hold references to people,
never their names. A name is resolved, when shown, from the system of record for people. A name-to-reference mapping
is never kept, so erasing a person is that system's job alone. The annotations make the rule part of a template's
declaration, so the compiler enforces it, instead of every application remembering it.

**The two annotations** (on a variable's descriptor in the bindings configuration, beside `@input`, `@output`,
`@sql.type`, `@sql.table`, `@escape`, `@unique`):
- **`@transient`:** the value exists while the template is filled in (an enactor may match on it, a caller may log it
  locally) and is never persisted.
- **`@pseudonym("method")`:** the value arrives already pseudonymised. The annotation declares the method, the form a
  valid value takes, and the resolver readers use. Proposed methods:
  - `erp-ref`: the system of record's identifier, resolved and erased there.
  - `inline-refs`: free text in which people appear only as `erp-ref` references, e.g. `⟨person <id>⟩`.
  - `keyed-hash`: an HMAC with a tenant key, where no system of record exists. Erasure destroys the key, for the whole tenant.

  A random token kept with a lookup table is excluded, because it needs the persistent mapping the rule forbids.

**Requirements added 2026-11-10 (Luc).**
- **A library declares; an application decides.** A template library may carry the annotations. An application using
  that library must be able to ignore them, or change them, without copying the library: from its own project
  configuration, never by editing the library's bindings.
- **Methods are extensible.** The initial methods are a starting set. New methods must be addable over time, by
  ProvToolbox or by an application, without changing the compiler.
- **`@transient` takes a method too.** It becomes `@transient("method")` (for example, drop the value entirely, or keep
  it only in a local log), with a default when no method is given.
- **Implementations can be overridden.** An application can replace a method's implementation with its own, under the
  same method name.

The design comes first: written into this section and ruled by Luc before the compiler changes below are built.
They will change to fit it.

**Design (proposed 2026-11-10, ruled by Luc the same day: Rulings 1–5 below).** Grounded in
`development2_0` at eca0e021a. The rulings asked for are numbered; each says what is proposed and, where there is a
real alternative, what it is.

*What the code imposes.*
- Every generator gets a template's variables from one place. `CompilerUtil.getBindingsSchema(config[, baseDir])`
  reads each `cbindings` file once and caches it. `ConfigProcessor.findAllPackagesInConfigurations` loads them all
  before any generation. A policy applied there is seen by every generator, with no generator reading it again.
- `DescriptorUtils.fieldNames(schema)` lists the variables in order. 21 generators use it: beans, builders, the bean
  checker, invokers, SQL, expansion, JSON schema and documentation. The beans must keep a transient field, since the
  enactor needs it. So `fieldNames` can't simply drop transient variables. Each place where a value leaves the
  process needs its own list.
- The generated client project (`cli`) is emitted from PAST into Java, Python, JavaScript (jsweet) and Rust. `@escape`
  already shows how a runtime call reaches all of them, and R9 follows it:
  - `CompilerCommon` emits a PAST `METHOD_CALL` to
    `org.openprovenance.apache.commons.lang.StringEscapeUtils.escapeCsv`;
  - the class exists once per language: Java in `prov-model`, JavaScript through the jsweet candy in
    `prov-jsweet-candy-java`, Python in `prov-template-library/src/main/python`;
  - `CompilerMaven.makeSubPom` adds `prov-model` to the client project only when `@escape` is used.
- Ports can drift. The Python `escapeCsv` port is a placeholder ("PLACEHOLDER TO FIX" in its docstring) that doesn't
  double embedded quotes as the Java one does. Rust has no mapping for a static call to such a class:
  `rust-registry.json` maps types and instance methods only. A run-time registry looked up by name stays out (Ruling 1).
- The insert function is `LANGUAGE SQL` (a CTE chain), or plpgsql only in the `@unique` idempotent case. Tables are
  created `IF NOT EXISTS`, so a constraint written inside `CREATE TABLE` never reaches a table that already exists.
- The descriptors are read with Jackson 2 defaults, which fail on unknown members. So a compiler older than T31
  refuses a library that carries the annotations. That fails closed, as wanted.
- A library is consumed in place: the `compile-catalogue` goal takes `templateLibraries` and `cbindingsBaseDir`, and
  the application's own catalogue names the library's `cbindings`. Nothing is copied.

*R1. Two layers: the library declares, the application decides.*
- The annotations in a bindings schema state the library's intent.
- The application's catalogue (`TemplatesProjectConfiguration`) gains a member `privacy`: the path of a policy file,
  like `variableCheck`. The policy states the application's decisions.
- Where the two disagree, the policy wins.
- With no `privacy` member, every annotation is honoured as declared.
- One policy file can be shared by all of an application's catalogues.
- Alternative: the policy inline in the catalogue. Rejected because several catalogues would repeat it.

*R2. Syntax in a bindings schema.*
- `"@transient": "pt:local-log"`, or `"@transient": true` for the default method, `pt:drop`.
- `"@pseudonym": "pt:erp-ref"`.
- A method name without a prefix means `pt:`, so `"erp-ref"` is `pt:erp-ref`.
- Both annotations on one variable are refused, unless the policy resolves the variable to one of them.
- The notation `@transient("m")` / `@pseudonym("m")` used elsewhere in this ticket stands for these members.

*R3. Method names are namespaced.*
- `pt:` is reserved for ProvToolbox. Any other prefix belongs to whoever defines it, e.g. `acme:staff-no`.
- An application may redefine a `pt:` method (R4) but not invent a new one under `pt:`. That is a compile error, so
  a later ProvToolbox method can't collide with an application's.

*R4. Methods are data first, Java second; a same-name definition overrides.*
- A method is a JSON definition:
  - `kind`: `transient` or `pseudonym`;
  - for a transient method, `keeps` (R5);
  - for a pseudonym method, `pattern`: a regular expression in the subset that Java and PostgreSQL `~` read alike
    (POSIX ERE: classes, `{n}`, `*`, `+`, `?`, `|`, anchors; no look-around, no `\d`);
  - `sql`: optional. A boolean SQL expression over `v` that replaces the pattern test in the database when a regular
    expression is not enough;
  - `past`: optional. A PAST method `{package, class, method}` that replaces the default PAST call in the client
    code (R9, R16, R17);
  - `resolver`: a hint naming the system readers resolve a value against, e.g. `hr`;
  - `documentation`.
- ProvToolbox's built-ins ship as a resource of the compiler (`privacy/methods.json`), in the same format an
  application uses.
- An application defines methods under `methods` in its policy file. A definition with a built-in's name replaces the
  built-in (requirement 4). The audit record says so (R12).
- For a method that can't be stated as data, an interface `PrivacyMethod` (name, kind, keeps, SQL check, JSON-schema
  pattern, resolver, documentation) is found by `ServiceLoader` on the compiler's classpath. For the Maven goal, that
  means a dependency of the plugin. The policy can also name a class: `"acme:x": {"class": "com.acme.X"}`.
- Precedence for one name: the policy's definition, then a class found by `ServiceLoader`, then the built-in.
- A method is resolved once, when the code is generated, and its meaning is then fixed (Ruling 1). A `PrivacyMethod`
  class runs inside the compiler only. What it returns is written into the generated artefacts, and the class is
  never called while the application runs.
- Each resolved definition has a *digest*: the SHA-256 of its canonical JSON (kind; `keeps`, or the `pattern` / `sql`
  actually used). It doesn't matter whether the definition came from the built-ins, the policy or a class. The digest
  identifies what the method meant when the code was generated.
- Requirement 2 holds: a new method is a few lines of JSON, or a jar on the plugin's classpath. The compiler is unchanged.

*R5. A transient method says where the value may go.*
- A value can go to four places:
  - `store`: the SQL column, the composite SQL type, the insert parameter, `sqlTuple`, what `QueryInvoker` /
    `TableConfigurator` send;
  - `log`: the CSV record written by `CompilerLogger`;
  - `expansion`: the attribute in the expanded PROV document;
  - the bean, which always keeps it, since the enactor needs it.
- The JSON schema and the documentation always describe the variable, since a caller still supplies it.
- A transient method lists the places it keeps the value, among `store`, `log`, `expansion`:
  - `pt:drop` (the default) keeps none;
  - `pt:local-log` keeps `log` only. The value stays in the caller's CSV log and never reaches the store or an
    expanded document. A replay of that log (`log2prov`) still leaves it unbound.
- An application can add methods by choosing another subset.
- The honest limit: a new *place* (a new kind of output) needs a compiler change. A new *method* doesn't.
- Keeping the value in the log is safe only while the log stays local. An application that ships its CSV log to a
  server should use `pt:drop`. The documentation says so.

*R6. The starting pseudonym methods and their forms.*

| Method | Form (pattern) | Resolver / erasure |
|---|---|---|
| `pt:erp-ref` | `^[a-z][a-z0-9_.-]*:[A-Za-z0-9_./-]+$`, a system prefix and an identifier with no spaces, e.g. `hr:person/4711` | the system of record |
| `pt:inline-refs` | `^([^⟨⟩]\|⟨person [a-z][a-z0-9_.-]*:[A-Za-z0-9_./-]+⟩)*$`: free text whose every `⟨…⟩` is a well-formed reference | as `pt:erp-ref`, per reference |
| `pt:keyed-hash` | `^[a-z0-9_-]+:[0-9a-f]{64}$`, a key identifier and an HMAC-SHA256 in hex | destroy the key: erases the whole tenant |

- A form check recognises a clear value by its shape, not its meaning:
  - `John Smith` fails `pt:erp-ref` (space, no prefix), but `smith:john` passes.
  - `pt:inline-refs` checks only that references are well-formed. It can't prove that the text around them names nobody.
- Both limits are stated in the documentation. An application is expected to override `pt:erp-ref` with its own
  system's exact form (R4). That is requirement 4's main use.
- The key identifier in `pt:keyed-hash` is there so a rotated or destroyed key is traceable from the stored values.
- Alternative: no key identifier, the bare 64 hex digits.

*R7. What the policy can say.*
- It can define methods (R4).
- `overrides` maps a scope to a decision. Scopes, from most specific to least:
  - `variable:<template fqn>#<variable>`;
  - `template:<template fqn>`;
  - `method:<method>`, which applies wherever that method is declared;
  - `*`.
- The most specific matching scope decides.
- Decisions:
  - `"honour"` (the default; a bare string);
  - `{"decision": "ignore", "reason": …}`: the variable is treated as unannotated;
  - a replacement, such as `{"@pseudonym": "acme:staff-no", "reason": …}` or `{"@transient": "pt:drop", "reason": …}`.
- At `variable:` scope, a replacement may also *add* an annotation the library didn't declare. An application can
  protect a library variable without copying the library.
- At `method:` scope, a replacement substitutes one method for another everywhere. This is also how an application
  changes the default `@transient` method: `"method:pt:drop": {"@transient": "acme:x"}`.
- `reason` is required on every decision other than `honour`. The audit record keeps it.
- Example:
  ```json
  { "methods":   { "pt:erp-ref":   { "kind": "pseudonym", "pattern": "^hr:person/[0-9]+$", "resolver": "hr" },
                   "acme:staff-no": { "kind": "pseudonym", "pattern": "^S[0-9]{6}$", "resolver": "hr" } },
    "overrides": { "method:pt:local-log":              { "@transient": "pt:drop", "reason": "our CSV logs are shipped" },
                   "template:org.example.Cleaning":    { "decision": "ignore", "reason": "test fixtures only" },
                   "variable:org.example.Ordering#clerk": { "@pseudonym": "acme:staff-no", "reason": "staff numbers, not ERP" } } }
  ```

*R8. Where it is applied in the compiler.*
- A `PrivacyPolicy` is built once in `ConfigProcessor.processTemplateGenerationConfig`, from the built-ins, the
  `ServiceLoader` classes and the catalogue's `privacy` file.
- It is applied to each bindings schema as it enters `CompilerUtil`'s cache. It resolves each descriptor to its
  *effective* annotation: fields on `NameDescriptor` / `AttributeDescriptor`, set from the declared members and the
  policy. It also records an audit entry.
- `DescriptorUtils` gains:
  - `getTransient(key, schema)` and `getPseudonym(key, schema)`, returning the effective method;
  - `keeps(key, schema, Place)`;
  - `storedFieldNames`, `loggedFieldNames`, `expandedFieldNames`.
- The places that send a value out switch from `fieldNames` to the right list. The list was checked against the
  generated code (see *Test configurations* below):
  - `CompilerSQL`:
    - `generateSQL`, the table column;
    - `generateSqlTypeDeclaration`, the composite type;
    - `generateSQLInsertStatement`, the JDBC `_sqlInsert1` in the client builder;
    - `generateSqlTupleMethod`;
  - `sql/CompilerSqlComposer`: insert, array insert, composite/linker insert, search functions;
  - `CompilerQueryInvoker` and `CompilerQueryInvokerWithPrincipal`, in the generated Java and Python `QueryInvoker`;
    also the processor / `TableConfigurator`;
  - the CSV record. Its writer (`logApproval(sb, …)` in the client builder, from
    `CompilerCommon.generateCommonCSVConverterMethod`), its reader (`record2bean`), `propertyOrder` and the `log2prov`
    reader all take `loggedFieldNames`. The record is positional, so all of them must agree. `CompilerLogger`'s
    `logX(…)` keeps every parameter: the caller still passes the value, which goes no further;
  - `expansion/CompilerExpansionBuilder`: the factory methods leave a transient variable unbound. The generated
    builder already adds an attribute only when its value is non-null (`if (x != null) attrs.add(…)`), so this
    means not binding it, not new expansion logic.
- The generated Python is emitted from the same PAST and follows with no separate work. The JavaScript (jsweet)
  and Rust outputs are checked in phase 2.
- `fieldNames` is unchanged for the beans, builders' parameters, the JSON schema and the documentation.
- A composite inherits its component's effective annotations.

*R9. A pseudonym is checked in the client code through PAST, and in the database (Ruling 3).*
- In the client, each pseudonym method maps by default to a PAST method call. Each emitter maps that call to its
  target language (R16).
  - The default call is `Pseudonyms.matches(value, PATTERN)` on a runtime class
    `org.openprovenance.prov.template.privacy.Pseudonyms`.
  - `PATTERN` is a `static final` constant generated into the client builder, holding the method's pattern. The
    meaning stays in the generated code, fixed at generation (Ruling 1). The runtime class only matches a regular
    expression, which every target language does the same way within R4's subset.
  - The client builder gains a generated `checkPrivacy(…)`, called from `args2csv` and `args2bean`. Those are the two
    conversions every value from the caller passes through on its way to a CSV record, a bean, the `QueryInvoker` or
    the JDBC insert.
  - A refusal throws `IllegalArgumentException` naming the template, the variable and the method, never the value.
    An error message must not carry the clear value into a log.
  - A transient method needs no call: what it does is decided by what the code generates (R5).
- In the database, the check stays as below. It is the authority for any writer that bypasses the generated client,
  e.g. a direct call of the insert function.
- For each pseudonymised column, the SQL file gains a constraint whose check is written inline, from the method's
  definition fixed at generation (Ruling 1):
  `ALTER TABLE t ADD CONSTRAINT t_<col>_p<digest12> CHECK (<col> IS NULL OR <col> ~ '<pattern>') NOT VALID;`,
  or the method's `sql` expression instead of the pattern test. Then
  `COMMENT ON CONSTRAINT t_<col>_p<digest12> ON t IS '<method> sha256:<digest>'`.
- Before adding it, a `DO` block drops any other `t_<col>_p…` constraint, the trace of an earlier definition.
  Constraint names stay under PostgreSQL's 63 characters: the method name goes in the comment, not the name.
- There is no shared check function. `CREATE OR REPLACE FUNCTION` would let anyone with rights on the schema change
  what a method means for every table at once, without touching a table or leaving a trace in the constraints. An
  inline check can change only by DDL on the table itself, and only under a new name.
- The constraint reaches a table that already exists. `NOT VALID` means rows written before are not re-checked:
  migrating them is not in this task. Every new write is checked, through both insert function forms. A clear value
  is a failed write.
- `CompilerJsonSchema` adds `"pattern"` to a pseudonymised property, so a form refuses a clear value before sending it.
- So a clear value is refused three times: by a form (JSON-schema `pattern`), by the generated client in every
  language (PAST), and by the database (constraint). All three test the same pattern, fixed at generation.

*R16. How the PAST call reaches each target language (Ruling 3).*
- **Java:** `Pseudonyms` goes in `prov-model`, beside `StringEscapeUtils`, using `java.util.regex`.
  `CompilerMaven.makeSubPom`'s condition for adding `prov-model` to the client project becomes "`@escape` or a
  pseudonym is used".
- **JavaScript:** a jsweet candy in `prov-jsweet-candy-java`, implemented with JavaScript `RegExp` and the `u` flag.
- **Python:** a port at `prov-template-library/src/main/python/org/openprovenance/prov/template/privacy/Pseudonyms.py`,
  using `re.fullmatch`. The Python emitter imports it as it imports `StringEscapeUtils`.
- **Rust:** an entry in `rust-registry.json` mapping the static call to a function using the `regex` crate, added to
  the generated `Cargo.toml`. That needs the registry to accept static calls, a JSON-level extension of
  `RustMethodRegistry`.
  - Until it does, generating Rust for a catalogue with a pseudonym is a compile error. That fails closed rather than
    leaving the Rust client without the check. *Superseded by Ruling 7: the Rust client leaves the check out, with a
    warning.*
- **Conformance:** one set of test vectors runs against all four ports and against PostgreSQL's `~`, and all must give
  the same answer. The vectors are the `refused` / `accepted` lists of `t31/cases.json`, plus edge cases (anchors,
  empty string, non-ASCII `⟨⟩`). This is what keeps the ports from drifting as `escapeCsv`'s have.
- **Unannotated templates:** no import, no constant, no `checkPrivacy`, no new dependency (R14).

*R17. An application's own PAST method (requirement 4 through PAST).*
- A method definition may give `past` instead of the default call, e.g.
  `"acme:staff-no": {"kind": "pseudonym", "pattern": "^S[0-9]{6}$", "past": {"package": "com.acme.privacy",
  "class": "Checks", "method": "staffNo"}, "targets": ["java", "python"]}`.
  - `targets` lists the languages for which the application provides that class.
  - Generating for a language not listed is a compile error: the compiler can't check a Python or Rust class exists,
    so the application declares it.
  - `pattern` remains compulsory. It drives the database check and the JSON schema.
- Auditability, in view of Ruling 1: with a custom `past`, the client check's meaning lives in the application's
  runtime code, outside the generated code and outside the digest. The digest then covers only the reference
  (package, class, method). The register marks the row "client check defined in application code".
- Allowed, and marked as such in the register (Ruling 4).

*R10. Everything is fixed when the code is generated (Ruling 1).*
- Fixed when the code is generated, like a `final` variable, for auditability:
  - whether a variable is transient, and where its value goes (columns, insert parameters, CSV fields, expansion
    bindings);
  - which method a variable uses;
  - what each method means: its form, its places, its digest;
  - the SQL checks, the client checks (the PAST call and its `final` pattern constant), the JSON-schema pattern, the
    documentation, the audit record.
- Nothing about a method is changeable at run time. There is no registry, no lookup by name while running, and no
  replaceable SQL function.
- The generated client builder of an annotated template carries the effective policy as a constant,
  `public static final String[] privacy`, one `"<variable>=<method>@sha256:<digest>"` per annotated variable (the
  Python `Final` equivalent). A running application can report which definitions it was built with, and nothing can
  rebind them. A template without annotations doesn't get the constant (R14).
- A method's meaning can change only through a new generation from a changed definition. That gives a new digest, a
  new constraint name and comment in the database, a new constant in the code and a new row in the register, so every
  change is visible.
- The resolver a reader calls is the application's code, not generated. The method fixes only its name (`resolver`).

*R11. Fail closed: compile errors, each naming the template, the variable and the method.*
- A method with no definition, unless the policy defines it or ignores the annotation.
- Both annotations on one variable.
- `@transient` on a name variable. A name variable is in an identifier position: unbound, it would suppress the node.
- Either annotation on a variable that isn't an input, whether declared by the library or added by a policy. An input
  is `@input` `compulsory` or `optional`, with `@output` absent or `false` (Ruling 2).
  - Only an input carries a value from the caller, which is the only way a name enters a record. An output gets its
    value from the store: `approving` comes from `INSERT INTO approving DEFAULT VALUES RETURNING ID`.
  - `@transient` on an output contradicts itself: the store makes the value and keeps it.
  - `@pseudonym` on an output would test the store's own integer ID against a reference pattern, and so refuse every
    write.
  - `@output: "optional"` counts as an output (`OutputFieldValue.isOutput`).
  - An `@input: "false"` variable that isn't an output is never supplied by a caller, so there is nothing to protect.
- This replaces the earlier `@output` and `@sql.table` refusals. The `@sql.table` one was wrong for inputs: the
  generated SQL inserts an input with `@sql.table` straight into the template's own column (`order` as
  `input__order` into `_order`). `@sql.table` redirects only outputs. `@pseudonym` on such an input is therefore
  allowed, and checked in that column.
- `@transient` on a variable that is also `@unique`, named in `@sql.new.inputs` / `@sql.also.outputs`, or shared in
  a composite.
- A transient method whose `keeps` names an unknown place. A pseudonym method without `pattern` (a `class` must
  supply one).
- A custom `past` for a target language not in its `targets`. A pseudonym in a catalogue that generates Rust, until
  the Rust registry maps the call (R16). *Ruling 7: Rust is no longer refused, for either.*
- A new method under `pt:`.
- A non-`honour` decision without `reason`. A policy scope naming an unknown template or variable.

*R12. The effective policy is in the output: the audit record.*
- `generateDocumentationEnd` writes, beside the documentation, `privacy-register.json` and a page `privacy-register.html`.
- They are written only when the catalogue has at least one annotation, declared or added.
- One row per annotated variable:
  - template, variable;
  - declared annotation and method (or none);
  - effective annotation and method (or ignored);
  - decided by: the library's bindings file, or the policy scope that decided;
  - reason;
  - where the method's definition came from (built-in, the policy, or a class), and its digest, the one found in
    the database constraint's comment and in the code's `privacy` constant;
  - for a transient method, the places it keeps the value; for a pseudonym method, its form and resolver.
- This is the record a data-protection audit asks for.
- Each template's own documentation page shows the *effective* annotation only. An ignored annotation leaves the page
  as it was (R14).

*R13. Two ways an application uses a library.*
- (a) Its catalogue names the library's `cbindings` in place (`templateLibraries`, `cbindingsBaseDir`). The policy
  then applies fully: tables, records, checks.
- (b) It depends on code the library generated and published. The library's choices, method meanings included,
  are then fixed in that code, and nothing can be replaced (R10).
- Neither way copies the library. An application that needs to change the shape of a library template's table uses (a).

*R14. "No change for others", stated exactly.*
- When no variable of a catalogue has an effective annotation, every file the compiler wrote before T31 is
  byte-identical, and no new file appears. That covers no annotations at all, and all of them ignored.
- Measured: today's compiler is not deterministic on its own. Two runs on the same input differ in two places:
  - the generated logger's `buildDate`, a timestamp;
  - the key order of the generated `schema.json`, which differs between JVMs; the content is the same.
- So "byte-identical" means after normalising those two: `buildDate` blanked, JSON compared with sorted keys. The
  test does this (`T31BaselineTest.normalise`).
- Alternative: make the order of `CompilerJsonSchema` stable as part of T31. It would change `schema.json`'s bytes
  once for every catalogue, so it belongs in its own ticket.
- New output (the SQL privacy section, the register) appears only with annotations.
- One exception holds by construction. The webjar's `bindings/` copy is the library's file copied verbatim
  (`ConfigProcessor`, `FileUtils.copyFileToDirectory`), so it carries the library's declared annotations even when
  the application ignores them.
- A run-time consumer must therefore never act on annotations read from a raw bindings file. The effective policy is
  in the register.
- Alternative: write the effective bindings instead of the verbatim copy whenever the policy changed something. That
  re-serialises the file, so its bytes change whenever a policy applies.

*R15. Tests, and "Done when" as the design amends it.*
- Tests sit in `prov-template-compiler/src/test`, beside `IdempotentInsertFunctionTest`. There are two kinds:
  - unit tests in its style: build a `TemplateBindingsSchema` from JSON and inspect the generated text;
  - end-to-end tests over the matrix of test configurations below (`t31/cases.json`), generated by the compiler as
    `T31BaselineTest` does. Each case's `expect` becomes assertions.
- Each generation runs in a fresh JVM, because a second generation in one JVM fails today (see below).
- Parsing and validation: both annotations, `true` for the default method, the unprefixed name, every R11 refusal.
- Policy: each scope and decision, precedence, an override of `pt:erp-ref`, an application method, a
  `ServiceLoader` method found from a test resource.
- Transient (`pt:drop`): no column, no SQL type field, no insert parameter, no `sqlTuple` element, no CSV field, no
  expanded attribute. `pt:local-log`: the CSV field kept, the rest absent.
- Pseudonym, for each method:
  - the inline `NOT VALID` constraint is generated, named by digest and commented with method and digest;
  - no `CREATE FUNCTION` appears in the SQL;
  - the `privacy` constant is in the client builder;
  - the JSON-schema pattern is present;
  - the pattern, run in Java, refuses a clear value and accepts a valid one.
- Digests: two generations from the same definitions give the same digests. A changed definition (P2 against A6)
  gives a new digest, constraint name and constant.
- Client checks (R9, R16):
  - the generated Java and Python builders contain `checkPrivacy`, its calls from `args2csv` and `args2bean`, and the
    `final` pattern constants;
  - a template without annotations has none of them;
  - the conformance vectors give the same answers in the Java, Python, JavaScript and Rust ports and in PostgreSQL.
    The Python, JavaScript and PostgreSQL runs are skipped when their interpreter or database is absent;
  - the refusal message names template, variable and method, and doesn't contain the value.
- A live PostgreSQL test runs the generated SQL and shows the insert refused and accepted. It runs only when
  `PROV_T31_PG_URL` is set, so the build doesn't need a database.
- Byte identity: one catalogue, generated (i) with its annotations stripped and (ii) with them all ignored by policy,
  gives identical trees. A test catalogue is also generated before and after, against `development2_0`'s output.
- The register lists declared, effective and decided-by, as in R12.
- The specification and the bindings schema carry the annotations (below).

*The specification (`provenance-templates-specification`, not edited in phase 1).*
- `prov-template/index.html` §`bindings-schemas`:
  - two rows in the members table, `@transient` and `@pseudonym`;
  - a short subsection on what they mean. A transient attribute variable is left unbound, so its attribute is
    absent: the existing rule for unbound attribute variables in `prov-template-algorithms` needs no change. A name
    variable can't be transient, since unbound it suppresses its node. Method names are namespaced, `pt:` reserved.
- `prov-template/schemas/bindings-schema.json`: the descriptor objects accept `@transient` (string or `true`) and
  `@pseudonym` (string). Today they are `additionalProperties: false`. The ednote listing the members the schema
  rejects stays as it is.
- `prov-template/index.html` §`template-compiler`, informative:
  - the catalogue's `privacy` policy, its scopes and precedence, method definitions and overrides;
  - what is fixed at generation and what isn't;
  - the register.
- `prov-template-library` §`organisation` or §`conventions`: library `cbindings` may carry the annotations as intent,
  and each application decides through its policy.
- The method definition format and the built-in methods' forms go in the specification too, as a table, so a reader
  in another language can check a value the same way.

*Test configurations, and what today's compiler does with them (measured 2026-11-10 at eca0e021a).*
- The fixtures are in `prov-template-compiler/src/test/resources/t31/`:
  - `approval.provn`: an approval activity with a clear name, its agent with a reference, the approved order with
    a note and a customer hash;
  - `approval-base.json`: its bindings, unannotated;
  - `cases.json`: the 36 configurations. Each gives annotations, optionally a base case and a policy, and the
    design's expectation.
- `T31BaselineTest` runs today's compiler on each case into `target/t31/<case>/`. It writes `target/t31/baseline.md`,
  which records:
  - whether the catalogue and the bindings are read;
  - whether generation succeeds;
  - which files differ from A0's;
  - for A0, every generated file that mentions each variable.
- It asserts only that A0 generates and that generation, once normalised, is deterministic.

| Case | Configuration | Design expects | Today |
|---|---|---|---|
| A0 | no annotation | the baseline | generates |
| A1 | `approver_name` `@transient: true` | no column, SQL type field, insert parameter, `_sqlInsert1`/`QueryInvoker` element, CSV field or expanded attribute; bean, schema, doc keep it | bindings refused: `UnrecognizedPropertyException "@transient"` |
| A2 | `@transient: "pt:local-log"` | as A1, but the CSV field kept | refused, as A1 |
| A3 | `approver_ref` `@pseudonym: "erp-ref"` (unprefixed) | inline `NOT VALID` constraint `approval_approver_ref_p<digest>`, `privacy` constant, client `checkPrivacy` calling `Pseudonyms.matches` in Java and Python, schema `pattern`; refuses `Jane Smith`, accepts `hr:person/4711` | refused: `"@pseudonym"` |
| A4 | `order_note` `pt:inline-refs` | refuses `⟨person Jane Smith⟩`; accepts text with well-formed references (a bare name in the text passes: the known limit) | refused |
| A5 | `customer_hash` `pt:keyed-hash` | refuses `ACME Ltd`, accepts `k1:` + 64 hex | refused |
| A6 | the library: A1+A3+A4+A5 | the union; the register has 4 rows | refused |
| A7 | `@transient: false` | same as A0 | refused |
| P1 | A6, policy `*` ignore | same as A0, plus the register | catalogue refused: `UnrecognizedPropertyException "privacy"`; without it, bindings refused |
| P2 | A6, policy redefines `pt:erp-ref` | as A6, inline check `^hr:person/[0-9]+$`, a digest different from A6's; register: defined by the policy | refused, as P1 |
| P3 | A2, `method:pt:local-log` → `pt:drop` | generated code same as A1 | refused, as P1 |
| P4 | A0, policy *adds* `@pseudonym` on `approver_ref` | generated code same as A3; register: declared none | catalogue refused; without the policy, same as A0 |
| P5 | A6, `template:` ignore | same as A0, plus the register | refused, as P1 |
| P6 | A6, `acme:staff-no` on `approver_ref` | inline check `^S[0-9]{6}$`, comment `acme:staff-no` | refused, as P1 |
| P7 | A6, `variable:` ignore on `approver_name` | A3+A4+A5 only | refused, as P1 |
| P8 | A1, `method:pt:drop` → `acme:keep-log` | generated code same as A2 | refused, as P1 |
| E1–E8 (not E5) | both annotations; `@transient` on a name variable, an `@output`, with `@unique`; unknown `pt:` methods; an undefined `acme:` method | compile errors (R11), each naming template, variable and method | bindings refused by Jackson, the same message for all, never the reason |
| E5 | `@pseudonym` on an input with `@sql.table` (`order`) | allowed: checked in the template's `_order` column (R11 as amended) | refused by Jackson |
| P9 | A6, `acme:staff-no` with its own PAST method for Java and Python | client check calls `com.acme.privacy.Checks.staffNo`; inline database check `^S[0-9]{6}$`; register: "client check defined in application code" (R17) | refused, as P1 |
| E18 | A3 in a catalogue that generates Rust | compile error until the Rust registry maps the call (R16) | refused by Jackson |
| E19 | P9 with `targets: ["java"]`, the catalogue generating Python | compile error (R17) | refused, as P1 |
| E15–E17 | `@pseudonym` on `@input: "false"`; on an input with `@output: "optional"`; a policy adding `@pseudonym` to the output `approving` | compile errors (R11: inputs only) | the first two refused by Jackson; the third: catalogue refused |
| E9 | E8, policy ignores `acme:staff-no` | same as A0 | refused, as P1 |
| E10, E13, E14 | A0, policy invents `pt:new-thing` / a method with no form / `keeps: ["cloud"]` | compile errors (R3, R11) | catalogue refused; without the policy, same as A0 |
| E11, E12 | A6, a decision without `reason` / a scope naming an unknown variable | compile errors (R11) | refused, as P1 |

- The cases still missing need a composite catalogue: a composite sharing a transient variable (R11), and a
  composite inheriting its component's annotations (R8). `doGenerateServerForEntry1` doesn't cover composites, so
  phase 2 runs them through `ConfigProcessor.processTemplateGenerationConfig`.

*What the measurement showed.*
1. **Today's compiler fails closed**, as the design assumed. A bindings schema with either annotation is refused,
   and so is a catalogue with `privacy`. A library that adopts the annotations can't be compiled by an older
   compiler into a store that silently persists the values.
2. **Where a value lands today.** Shown for `approver_name` in A0, 25 files. This is the list R5 and R8 must cover:
   - the store: `template.sql` (column, type, insert parameter), `_sqlInsert1` in the client builder, the Java and
     Python `QueryInvoker`;
   - the log: the CSV writer and reader in the client builder, and the Java and Python `Logger` (parameters only);
   - the expansion: the `l2p` `ApprovalBuilder`, `…TypeManagement`, `…TypedRecord`;
   - kept: the bean, processor, builder interface, `BeanChecker`, `TypeConverter`, `schema.json`, `doc.html`;
   - verbatim copies: the webjar copy of the bindings and of the template.

   R8 lists these places, corrected from this run.
3. **Generation is not deterministic** in `buildDate` and in `schema.json` key order. R14 is amended for it.
4. **A second generation in one JVM fails**, with an NPE in `past/emitter/Python.typeSimpleName`. *Corrected in
   phase 2:* the harness, copied from `CompilerTest`, skipped the `SpecificationFile.resetTypeCheckCoordinator()` /
   `resetCodeGenCoordinator()` that `processTemplateGenerationConfig` calls before each generation. With them,
   repeated in-process generation works. It is not a compiler bug.

**Rulings.** Each ruling is recorded here, with who made it and when.
- **Ruling 1 (Luc, 2026-11-10), on R10.** A method's meaning must not change while the application runs: "not
  something that can be updated, but should be fixed, as a final variable", for auditability. Applied:
  - R4: a method is resolved once, at generation, and gets a digest;
  - R9: an inline check in a digest-named constraint, no shared replaceable SQL function;
  - R10: everything fixed at generation, and a `final` constant of the effective policy in the generated client
    builder;
  - R12: the digest in the register;
  - R13: nothing replaceable in pre-generated code;
  - R15 and the cases: digests tested.
- **Ruling 2 (Luc, 2026-11-10), on R11.** `@transient` and `@pseudonym` are restricted to inputs: `@input`
  `compulsory` or `optional`, and `@output` absent or `false`. This holds whether the library declares the annotation
  or a policy adds it. Luc asked "restricted to @input properties?" and ruled the proposal as written.
  - This replaces the earlier `@output` and `@sql.table` refusals. An input with `@sql.table` may be `@pseudonym`
    (case E5).
  - E15–E17 are the refusals.
  - R11's other refusals stand with it: both annotations; `@transient` on a name variable, with `@unique`, in
    `@sql.new.inputs` / `@sql.also.outputs`, or shared in a composite; unknown methods; a new `pt:` method; a decision
    without `reason`; an unknown scope.
- **Ruling 3 (Luc, 2026-11-10), on R9.** "As prov-template-compiler generates PAST expressions, there should be a
  default mapping to past methods, each of them being mapped to their corresponding target language java, js, etc."
  Applied:
  - R9: a client check through a default PAST call, beside the database check;
  - R16: how the call reaches Java, JavaScript, Python and Rust;
  - R17: an application's own PAST method;
  - R4, R10, R11, R15: amended to match.
  - This replaces R9's earlier "no check in the client code", whose premise was wrong: `@escape` already maps a PAST
    call to per-language runtime classes.
- **Ruling 4 (Luc, 2026-11-10), on R17.** An application may give its own PAST method for a pseudonym method's
  client check ("Allow, marked").
  - It declares the languages it provides the method for in `targets`.
  - `pattern` stays compulsory, for the database check and the JSON schema.
  - The register marks the row "client check defined in application code". The digest covers only the reference.
  - Cases P9 (allowed) and E19 (a generated language not in `targets`) test it.
- **Ruling 5 (Luc, 2026-11-10), on the rest.** "Accept the rest": R1–R9 (as amended by Rulings 1 and 3), R12–R16, as
  proposed. The alternatives listed in them are rejected:
  - R1: the policy is a file named by the catalogue's `privacy` member, not inline;
  - R6: `pt:keyed-hash` keeps its key identifier;
  - R14: byte identity is measured after normalising `buildDate` and JSON key order, and the webjar's bindings copy
    stays verbatim;
  - R16: Rust is a compile error for a pseudonym until the Rust registry maps the call.
- All rulings are made. Phase 2 builds the design. "Done when" holds as R15 amends it.
- **Ruling 6 (Luc, 2026-11-10), on the build's departures.** D1–D4 are approved ("approved"); D5 was agreed before
  them. They are recorded under *Build (phase 2)* below:
  - D1: the CSV log keeps a transient variable's column, empty;
  - D2: the client check is one `Pseudonyms.check` call per pseudonymised variable, in `logX` and `args2bean`;
  - D3: patterns must be anchored;
  - D4: the register sits beside `doc.html`, written whether or not the catalogue configures documentation;
  - D5: the store keeps a transient variable's column, never written and guarded by `CHECK (… IS NULL)`, and the old
    insert function is dropped on upgrade.
- **Ruling 7 (Luc, 2026-11-10), on Rust (R16).** "For Rust, can we make it ignore these annotations (but show a
  warning)." Applied:
  - a catalogue that generates Rust is no longer refused for a pseudonym. The compiler prints a warning per
    pseudonymised variable (`[privacy] WARNING: T31: template …, variable …, method …: the generated Rust client
    doesn't check this pseudonym …`), and the register's row notes "not checked by the generated Rust client";
  - the client check's PAST call carries a `NotInRust` annotation; the Rust emitter writes a comment with the reason
    in its place. Java, Python and JavaScript keep the call, and the database constraint checks every writer;
  - an application's own PAST method (R17) is likewise left out of the Rust client: `targets` must cover the other
    languages generated, and `rust` in it changes nothing until the Rust registry maps a static call;
  - `@transient` needs nothing: it is honoured in PAST (the write paths and the log), which the Rust output shares.
  - Today the Rust client emits only the beans, processors and record classes, not the builder that holds `logX`
    and `args2bean`, so no Rust code holds a check yet; the annotation takes effect when the builder is emitted.

**Build (phase 2, 2026-11-10): committed on `t31-transient-pseudonym` after Luc's review; the composite work below
followed.**
- *What is built.*
  - Descriptors: `@transient` and `@pseudonym` on `NameDescriptor` / `AttributeDescriptor`, and the effective annotation
    `EffectivePrivacy`. `DescriptorUtils` gains `getPrivacy`, `keeps` and the stored / logged / expanded field lists.
  - The catalogue: `privacy` in `TemplatesProjectConfiguration` and `template-project-schema.json`.
  - Package `compiler/privacy`:
    - `PrivacyPolicy`: methods, scopes, decisions, digests, every R11 / Ruling 2 / R17 refusal, the register, and
      Ruling 7's Rust warnings (`warnings()`);
    - `MethodDefinition`, `PrivacyMethod` (the SPI, `uses` in `module-info`), `PolicyFile`, `RegisterEntry`,
      `PrivacyRegisterWriter`;
    - the built-ins in `privacy/methods.json`.
  - Wiring: `ConfigProcessor.setupPrivacy` (per generation) and `CompilerUtil.getBindingsSchema` (applies the policy
    wherever bindings are read).
  - Composites:
    - R11: `PrivacyPolicy.checkComposite`, called by `setupPrivacy` for each composite of the catalogue, refuses a
      variable that is transient after the policy and shared by the composite;
    - R8 holds without new code: a composite's elements use the component's bindings file, read once and resolved by
      the policy, so the composite's insert function, its array form and its CSV writer (which writes each element
      through the component's `args2csv`, and so its checks) take the component's effective annotations.
  - Generators:
    - `CompilerSQL`: the column, type, `_sqlInsert1`, `?` count and `sqlTuple` body take the stored variables; the
      inline digest-named `NOT VALID` constraints are added;
    - `CompilerSqlComposer`: the stored variables;
    - `CompilerQueryInvoker`: the stored variables;
    - the CSV writer (`CompilerCommon.generateLoggerMethod`);
    - `CompilerExpansionBuilder`: `make(…)` and the continuation pass `null` for a transient value;
    - `CompilerJsonSchema`: `pattern` and a mark;
    - `CompilerDocumentation`: a mark;
    - the client check through PAST, and the `privacy` constant;
    - `CompilerMaven`: `prov-model` when a pseudonym is used.
  - Runtime `Pseudonyms`, the same in each language:
    - Java: in `prov-model`, exported;
    - JavaScript: a jsweet candy in `prov-jsweet-candy-java`;
    - Python: a port in `prov-template-library`.
  - Tests: `T31CasesTest` (renamed from `T31BaselineTest`) generates the 40 cases of `t31/cases.json` and asserts each
    `expect`. A case with `composite` adds a composite of the template, sharing the variables it names, and is
    generated by the whole pipeline, `ConfigProcessor.processTemplateGenerationConfig`:
    - C0: A0 with a composite sharing `approving`: every input stored by the composite's insert, no register;
    - C1: A6 with the same composite: the composite's insert leaves out `approver_name`, the constraints and the
      empty-column guard as A6, the register as A6's (the composite adds no row);
    - E20: A1 with a composite sharing `approver_name`: refused.
    - Ruling 7: R1 (was E18), A3 generating Rust: generated, with the warning, the register's note, no Rust call to a
      check, the Java and Python checks and the constraint unchanged; R2, A6 generating Rust: a warning per pseudonym,
      D5's empty column for the transient one. `RustEmitterTest.notInRust_leavesTheCallOutWithAComment` checks the
      emitter writes the comment in place of the call.
  - The specification, edited in its own repository and committed there:
    - `prov-template/index.html`: members table rows, a section on the data-protection annotations, the policy in the
      compiler section;
    - `prov-template/schemas/bindings-schema.json`: `@pseudonym` on both descriptors, `@transient` on attribute
      descriptors only;
    - `prov-template-library/index.html`: a convention.
- *Verified.*
  - All 40 cases pass, including every refusal message, with D5's expectations on A1, A2, A6, C1 and R2.
  - Conformance of every `refused` / `accepted` vector: Java runtime, Python port, JavaScript `RegExp`, PostgreSQL `~`.
  - The generated SQL in a live PostgreSQL 14 (`PROV_T31_PG_URL`):
    - A6's valid record is accepted, and each clear value is refused by its constraint;
    - P2's changed definition replaces the constraint under its new digest, `NOT VALID`.
  - The compiler module's full suite passes: 165 tests.
  - Byte identity against the pre-build compiler, after the R14 normalisations plus generator line numbers (finding
    3): A0, A7, P1, P5 and E9 against pre-build A0; `CompilerTest`'s `config1` catalogue as well. C0, the catalogue
    with a composite, through the whole pipeline, against the compiler at 67fd38a36 (before the build): identical but
    for the bindings path, given absolute to the old run.
- *Departures from the rulings (approved by Luc: Ruling 6).*
  - D1 (R5/R8, the log):
    - a transient value its method doesn't log keeps its CSV column, left empty: the separator is written, never the
      value. The ruled design dropped the column (`loggedFieldNames` for writer and reader alike);
    - the record is positional, and about 15 generators re-derive its index (`record2bean`, `processorConverter`,
      `make(Object[])`, `TypedRecord`, `propertyOrder`, `__nodes` / `__successors` / `__relations`,
      `foreignTables`, …), as does the `log2prov` runtime (`FileBuilder`);
    - an empty column gives the same protection, since the value is never written and a replay never learns it,
      without touching any of them.
  - D2 (R9, the client check):
    - no separate generated `checkPrivacy(…)`: one `Pseudonyms.check(value, pattern, template, variable, method)`
      call per pseudonymised variable, at the start of `logX` and of `args2bean`;
    - the runtime throws `IllegalArgumentException` naming template, variable and method, never the value;
    - the pattern is a literal of the generated code, fixed at generation, not a separate constant per variable; the
      `privacy` constant carries method and digest;
    - an application's own PAST method (R17) takes the same five parameters.
  - D3 (R11): a pattern must be anchored, `^...$`. Without anchors, Java `matches()` and Python `fullmatch` match the
    whole value while JavaScript `test` and PostgreSQL `~` search within it.
  - D4 (R12):
    - the register goes to the webjar's `html/` directory beside `doc.html`;
    - it is written from `ConfigProcessor.generatePrivacyRegister`, even when the catalogue has no `documentation`;
      the audit record shouldn't depend on that setting.
  - D5 (R5, the store), agreed by Luc on 2026-11-10: "it's better for legacy store, there is an upgrade path".
    - A transient value its method doesn't store keeps its column, in the table and in the composite type, and is
      never written there. Only the write paths leave it out: the insert function's parameters and values, the JDBC
      `_sqlInsert1` and its `?` count, `sqlTuple`, `QueryInvoker`. Readers are unchanged.
    - Past records in a legacy store keep their values and read back as before. Erasing them stays the application's
      migration.
    - A new store has the column, always `NULL`. Luc noted a further benefit: bindings with and without `@transient`
      map to the same table.
    - `CHECK (col IS NULL) NOT VALID`, named `<table>_<col>_t<digest>` and commented with method and digest, refuses a
      writer that bypasses the generated code. `COMMENT ON COLUMN` says why the column is empty.
    - On a table that catalogues with different decisions share, the stricter decision wins: the guard refuses the
      writes of a catalogue that doesn't honour `@transient`. Drop the guard if such sharing matters more.
    - `CREATE OR REPLACE FUNCTION` with fewer parameters would add a second insert function beside the old one, which
      an old client could still call with the clear value. The SQL file therefore drops the old signature first.
    - A column's earlier constraints are cleared by exact name (`^<table>_<col>_[pt]<12 hex>$`), never by a looser
      prefix that could catch unrelated constraints.
    - Verified live: a legacy A0 store with a past record, upgraded by A6's SQL, keeps the record's name; the old
      insert function is gone; a direct `INSERT` of a name is refused; a new record's column is `NULL`.
- *Findings.*
  - F1: finding 4 above is corrected: the NPE came from the harness, not the compiler.
  - F2: `StatementTypeAction` keeps a static counter (`tmp_Conv<n>`) running across generations in one JVM.
    `compile-catalogue` with several catalogues therefore makes one catalogue's output depend on those generated before
    it.
  - F3: the generated code carries the generator's own source line numbers (`// in file CompilerCommon.java, at line
    N`). Any edit of the compiler changes every catalogue's output, so R14's byte identity can only hold modulo them.
  - F4: PostgreSQL's refusal `DETAIL` echoes the failing row, the clear value included
    (`Failing row contains (…, Jane Smith, …)`).
    - The generated client refuses first, without the value, but a writer that bypasses it gets the value back in the
      error, and the server log keeps it unless `log_error_verbosity = terse`.
    - Options: document it, or have the insert function check first and raise without the value.
  - F5, reported by the code survey and not checked: `log2prov`'s `FileBuilder.register` keys builders by short name
    while the record's first column holds the fully qualified name; `processRecordForSql` calls `toBean`, which simple
    templates don't generate.
- *Not done.*
  - The jsweet candy compiles but wasn't transpiled. The Python port is exercised only by the conformance vectors.
  - Rust (Ruling 7): the Rust client has no pseudonym check; the registry still doesn't map a static call (R16).

**What the compiler does with them** (as filed; superseded where the design above differs). Each item names the
generator it touches, as found on `development2_0` at 77cd3d1e0:
1. **Descriptors.** `descriptors/NameDescriptor.java` and `AttributeDescriptor.java` gain `@transient` (boolean) and
   `@pseudonym` (a method name). The configuration's JSON schema and `TemplatesProjectConfiguration` read them. A
   variable that is both is refused, as is an unknown method.
2. **SQL.**
   - `CompilerSQL`: no column for a transient variable in the generated `CREATE TABLE`.
   - `sql/CompilerSqlComposer`: no parameter in the insert function, and nothing inserted.
   - A pseudonymised variable keeps its column, and the insert function refuses a value not in the method's form
     (a `CHECK` or a test in the function). A leak is then a failed write, not a silent row.
3. **Beans and invokers.** The beans still carry the field, since the enactor needs it. The generated
   `QueryInvoker` / processor / `TableConfigurator` leave it out of what goes to the store. `CompilerLogger` leaves it
   out of the CSV log record, so a replay never re-learns it.
4. **Expansion.** `expansion/CompilerExpansionBuilder` and the template expansion leave a transient variable unbound
   in the expanded PROV document (an attribute absent, never an empty value). An identifier position cannot be
   transient, and the compiler says so.
5. **JSON schema and documentation.** `CompilerJsonSchema` marks both. `CompilerDocumentation` lists them, and a
   catalogue-wide listing (template, variable, method) gives the record of personal data a data-protection audit asks for.
6. **The specification.** The PROV-TEMPLATE specification (`provenance-templates-specification`) states both, and the
   bindings schema accepts them.

**Not in this task:** an application's use of the annotations — which of its variables it marks, the resolvers its
readers call, honouring `@transient` in its own feeds and logs, and migrating values already in its store.

**Done when.**
- Both annotations parse and are validated.
- Generator tests (in `prov-template-compiler/src/test`, beside `IdempotentInsertFunctionTest`) show, for a transient
  variable: no column, no insert parameter, no log field, no expanded attribute.
- For each method, a pseudonymised variable's clear value is refused at insert, and its valid value is accepted.
- The documentation lists both.
- The specification and the bindings schema carry them.
- A template without either annotation generates byte-identical output (an existing catalogue is unchanged until it opts in).

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
