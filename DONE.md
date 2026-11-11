# ProvToolbox DONE — Completed Tasks

> Tasks retired from `TODO.md`. Each entry records the task id, its completion date and what was done.
> When a task is completed, remove it from the TODO index, move its section here and add a row to the index below.
> Dates written "by YYYY-MM-DD" are the first commit of `TODO.md` that lists the task as done.

---

## Update history

- **2026-11-11** — T35 (`pt:erp-ref` renamed `pt:ext-ref`) retired here.
- **2026-11-11** — T31 (`@transient` and `@pseudonym` on a template's variables) retired here.
- **2026-11-11** — T32 (log2prov cannot read a simple template's records) retired here.
- **2026-10-24** — T29 (ttf execution is slow) retired here; T30 filed for the `dot` runs that remain.
- **2026-10-16** — `TODO.md` and `DONE.md` restructured on the pattern of nextgen's `ODOO-TODO.md` / `ODOO-DONE.md`;
  the Done section of `TODO.md` (T27, T1, T14, T9, T19, T20) moved here. T28 filed.

---

## Completed Task Index

| T# | Description | Completed | Section |
|----|-------------|-----------|---------|
| T35 | T31's pseudonym method `pt:erp-ref` renamed `pt:ext-ref`, an external system's identifier, not necessarily an ERP's; no alias | 2026-11-11 | [T35 section](#-t35-pt-erp-ref-renamed-pt-ext-ref-2026-11-11) |
| T31 | `@transient` and `@pseudonym("method")` on a template's variables, decided by each catalogue's privacy policy: a transient value never written (column kept, log field empty, no expanded attribute), a pseudonymised one checked for its method's form in the database and in the generated Java, Python and JavaScript clients; Rulings 1–9 | 2026-11-11 | [T31 section](#-t31-transient-and-pseudonym-on-a-templates-variables-2026-11-11) |
| T32 | log2prov cannot read a simple template's records (fully qualified name vs short name key, no `toBean` in the client builder, `log2prov.interfaces` not exported) — fixed; round trip tested on class path and module path | 2026-11-11 | [T32 section](#-t32-log2prov-cannot-read-a-simple-templates-records-2026-11-11) |
| T29 | ttf execution (`execute-ttf`): the merge rewrote every format once per input, every task re-read and rewrote `index.json`, the `dot` renderings ran one at a time — fixed; nextgen/template `make ttf.execute` 2 min 22 s → 15.3 s | 2026-10-24 | [T29 section](#-t29-ttf-execution-is-slow-2026-10-24) |
| T27 | ptm templates: an expanded/merged document derived from one input only — now from both (template and bindings; template1 and template2) | 2026-10-09 | [T27 section](#-t27-ptm-templates-a-document-derived-from-one-input-only-2026-10-09) |
| T9 | Rust code generation: `bean.count + 1` on an `Option<i32>` | by 2026-05-24 | [T9 section](#-t9-rust-code-generation-arithmetic-on-an-option-by-2026-05-24) |
| T19 | The navigation display visualises atype icons, falling back on template icons | by 2026-05-24 | [T19 section](#-t19-atype-icons-in-the-navigation-display-by-2026-05-24) |
| T20 | Streaming progress for `/templates/viz` (SSE endpoint, per-stage panel in the navigator) | 2026-05-19 | [T20 section](#-t20-streaming-progress-for-templatesviz-2026-05-19) |
| T1 | template-compiler: CompilerCommon uses the new code generation pattern for the Common Bean | by 2026-05-24 | [T1 section](#-t1-template-compiler-the-common-bean-code-generation-by-2026-05-24) |
| T14 | Template expansion keeps an association without agent but with a plan | by 2026-05-24 | [T14 section](#-t14-an-association-without-agent-but-with-a-plan-by-2026-05-24) |

---

## ✅ T35: `pt:erp-ref` renamed `pt:ext-ref` (2026-11-11)

**Retired 2026-11-11 by Luc ("Retire and merge")**; merged into `development2_0`.

**Filed 2026-11-11** from nextgen's T-610, on Luc's word: ""erp-ref" is not a good choice of name. "ext-ref" would be better, to
mean an external system, not necessarily an ERP". The method T31 introduced names the identifier of the system of record for a
value (an ERP, an HR system, an identity realm); the name said ERP only.

**Built 2026-11-11** on branch `t35-ext-ref`: the method's key in `privacy/methods.json` and its documentation (an external
system's identifier, that system being the system of record), the javadoc examples in `RegisterEntry` and `EffectivePrivacy`,
T31's cases (`src/test/resources/t31/cases.json`) and the tests reading them. No alias: T31 was merged the same day and nothing
used the old name. `DONE.md`'s T31 section keeps the name it was built with. The specification is renamed alongside
(provenance-templates, `prov-template/index.html`).

**Done when.** No `erp-ref` outside `DONE.md`; prov-template-compiler's tests pass; the specification says `pt:ext-ref`.

---

---

## ✅ T31: `@transient` and `@pseudonym` on a template's variables (2026-11-11)

**Completed 2026-11-11**, retired by Luc. Built on `t31-transient-pseudonym`, merged into `development2_0` (81316ef54); the specification changes are on provenance-templates `main`. Its findings led to T32 (retired), T33 and T34.


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
    `org.openprovenance.prov.template.privacy.Pseudonyms` (`org.openprovenance.prov.utils.Pseudonyms` since Ruling 8).
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
- **Java:** `Pseudonyms` goes in `prov-model`, beside `StringEscapeUtils`, using `java.util.regex` (package
  `org.openprovenance.prov.utils` since Ruling 8).
  `CompilerMaven.makeSubPom`'s condition for adding `prov-model` to the client project becomes "`@escape` or a
  pseudonym is used".
- **JavaScript:** a jsweet candy in `prov-jsweet-candy-java`, implemented with JavaScript `RegExp` and the `u` flag.
- **Python:** a port at `prov-template-library/src/main/python/org/openprovenance/prov/utils/Pseudonyms.py` (Ruling 8),
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

- **Ruling 8 (Luc, 2026-11-10), on where `Pseudonyms` lives.** Luc first asked for it to move from `prov-model` to
  the compiler module. Told that the generated client project would then depend on `prov-template-compiler` whenever
  a pseudonym is used (it depends only on `prov-model`, for `@escape`), he ruled: "then it's ok to be prov-model, but
  make it appear under org.openprovenance.prov.utils." Applied:
  - Java: `org.openprovenance.prov.utils.Pseudonyms` in `prov-model`, which exports the package;
  - the jsweet candy (`prov-jsweet-candy-java`, re-transpiled into `dist`) and the Python port
    (`prov-template-library/src/main/python/org/openprovenance/prov/utils/Pseudonyms.py`) follow, since the generated
    code calls the same fully qualified name in every language;
  - the compiler's `Constants.PRIVACY_RUNTIME_PACKAGE`, the tests and the conformance vectors follow.

- **Ruling 9 (Luc, 2026-11-10), on F4.** "Document it only." Applied: the specification's `#template-compiler`
  section says a writer that bypasses the generated client is refused by the constraint, whose error may repeat the
  row with the clear value, and that a store holding references to people should set `log_error_verbosity = terse`
  and be written only by the generated client. The same paragraph now says, after Ruling 7, that the client check is
  generated for Java, Python and JavaScript, and that a catalogue generating Rust is warned. No code changes.

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
  - The jsweet candy, transpiled into `prov-jsweet-candy-java/dist` by packaging that module (`mvn -pl
    modules-template/prov-jsweet-candy-java package`; the copy to `dist` runs at `package`): run in node,
    `org.openprovenance.prov.utils.Pseudonyms.matches` accepts a reference and `null`, refuses a clear name, and
    `check` refuses without the value.
  - End to end, 2026-11-11, on A6 (one transient, three pseudonyms), each generated client run with a valid record,
    absent optional values, and a clear value for each pseudonym, in `logApproval` and `args2bean`:
    - Python: the generated `ApprovalBuilder.py`, with the port `org.openprovenance.prov.utils.Pseudonyms` from
      `prov-template-library`: the valid record is written with `approver_name`'s column empty; a clear
      `approver_ref` and an unkeyed `customer_hash` are refused, naming template, variable and method, never the
      value; plain text in `order_note` passes, `pt:inline-refs`' known limit (A4).
    - JavaScript: A6 regenerated with `jsweet: true`, the client transpiled by jsweet 3.1.0 against the candy (from a
      copy-on-write clone of `~/.m2`, `-Dmaven.repo.local`, into which `prov-model` and the candy were installed), and
      run in node with the candy's bundle: the same answers, and absent values written as empty fields.
    - Not exercised: the generated Java client (covered by the compiler's tests and the Java runtime's vectors).
  - Built without `-am`, the compiler module's tests compile against the installed `prov-model`, which may predate
    T31: `T31CasesTest` then looks `Pseudonyms` up at run time and skips the Java conformance, saying so in the
    report. Verified both ways.
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
    - Options: document it, or have the insert function check first and raise without the value. Ruling 9: document
      it only.
  - F5, reported by the code survey, confirmed on 2026-11-11 by a round trip (A0's generated client and log2prov
    code, compiled and run in a JVM on a plain class path): `log2prov` cannot read a simple template's records.
    - The generated client writes the fully qualified name first (`t31.Approval,1,4711,…`, since the PAST rewrite,
      af3fa56ec); `FileBuilder.register` keys `registry` and `clientRegistry` by `getName()` (`approval`).
    - The record as written doesn't reach the "unknown method" skip: `clientRegistry.get` returns null and
      `pm.facadeProxy(…, null)` throws a `NullPointerException` (`FileBuilder.java:93`), ending the read.
    - Keyed by the short name, the same record makes its document.
    - The SQL path (`processRecordForSql`, with a `RecordProcessor`) then throws a `NullPointerException`: the client
      builder has no `toBean` (`FileBuilder.java:126`, through the proxy's missing method).
    - Also seen: on the module path, the generated log2prov builder can't load, since `prov.template.compiler`
      doesn't export `org.openprovenance.prov.template.log2prov.interfaces`, which it implements.
    - Not caused by T31, and outside it: filed as T32 (Luc, 2026-11-11). T26 (composites) reports the same name /
      fully-qualified-name mismatch, but says simple records carry the name, which the round trip contradicts.
  - F6, seen running the generated Python client end to end (not caused by T31; filed as T33): the Python emitter turns Java's
    `x == null` into `x == 0`, so an absent value (`None`) is never treated as absent. The log writes the string
    `None` for an absent field, and an absent `@escape` field crashes in the port's `escapeCsv`, a placeholder that
    always quotes (`'"' + x + '"'`), where Java's quotes only when needed. The pseudonym check itself accepts `None`.
  - F7, seen transpiling a generated client with jsweet (not caused by T31; filed as T34): the jsweet build the compiler generates
    for a client project (`CompilerMaven`: `addJsweetCall`, `jsweet/jsweet-pom.xml`, `jsweet/jsweet.sh`) predates the
    way jsweet is now invoked, and transpiles nothing as generated. Luc (2026-11-11): the current way is
    provenance-templates-library's, whose `pom.xml` runs `mvn -f pom-jsweet.xml jsweet:jsweet` from antrun with
    `MAVEN_OPTS` opening `jdk.compiler`'s packages (`--add-opens …javac.api`, `.code`, `.file`, `.model`, `.parser`,
    `.tree`, `.util`), and whose `pom-jsweet.xml`, at the project's root, names its sources
    (`compileSourceRootsOverride`) and bundles (`bundle`, `declaration`, `dtsOut`); `prov-jsweet-candy-java` does the
    same. The generated one instead:
    - runs `jsweet/jsweet.sh`, which sets `JAVA_HOME` to a JDK 12 (`java12.home`, here left as the literal
      `${java12.home}`, a bash error) and passes no `--add-opens`, so jsweet 3.1.0 fails on a recent JDK;
    - keeps `jsweet-pom.xml` in `jsweet/`, so jsweet looks for `jsweet/src/main/java` and finds no sources;
    - gates the run on `if="${java12Exists}"`, true even when the property is unset;
    - doesn't pass the caller's `-Dmaven.repo.local` to the nested `mvn`.
    Run the library's way (the same POM from `t31_cli/`, with those `MAVEN_OPTS`), it transpiles cleanly: that is how
    the end-to-end check above ran.
- *Not done.*
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

## ✅ T32: log2prov cannot read a simple template's records (2026-11-11)

**Filed 2026-11-11** (Luc), from T31's finding F5, confirmed by a round trip.

**What happens.** A round trip on the code generated for T31's case A0 (`t31.Approval`): the generated client
(`ApprovalBuilder.logApproval`) and log2prov builder, compiled together and run in a JVM on a plain class path, the
record written by the client and read back with `FileBuilder.reader`.
1. **The key.** The generated client writes the fully qualified name in the record's first column
   (`t31.Approval,1,4711,…`), as it has since the PAST rewrite (af3fa56ec, `CompilerCommon.generateLoggerMethod`).
   `FileBuilder.register` keys `registry` and `clientRegistry` by `getName()` (`approval`). The read doesn't reach
   the "unknown method" skip: `clientRegistry.get` returns null, and `pm.facadeProxy(ProxySQLInterface.class, null)`
   throws a `NullPointerException` (`FileBuilder.java:93`), which ends the read at the first record. With `approval` in
   the first column, the same record makes its document.
2. **The SQL path.** With a `RecordProcessor` (`Runner`, `ProvenanceKernels`), `processRecordForSql` calls
   `clientBuilder.toBean(args)` (`FileBuilder.java:126`). No generated client builder has `toBean`; only
   `ProxySQLInterface` declares it. The proxy finds no method of that name and throws a `NullPointerException`
   (`ProxyManagement.java:60`).
3. **The module path.** Loaded as a module, the generated log2prov builder can't be defined:
   `prov.template.compiler` doesn't export `org.openprovenance.prov.template.log2prov.interfaces`, which it implements
   (`ProxyClientAccessorInterface`). On a plain class path, as above, it loads.

T26 reports the same name / fully-qualified-name mismatch for composite records, but says simple records carry the
name, which the round trip contradicts. The two should be fixed together.

**Fix (proposed).**
- Key the registries by the fully qualified name, the record's first column (or register under both, for logs written
  before af3fa56ec); and look the builder up before `facadeProxy`, so that an unknown record is skipped, not fatal.
- Generate `toBean(Object[])` in the client builder (from `record2bean`, which exists), or have `processRecordForSql`
  build the bean another way.
- Export `org.openprovenance.prov.template.log2prov.interfaces` from `prov.template.compiler`.

**DoD.** A generator test generates a catalogue, writes records with its generated client, and reads them back with
`FileBuilder.reader`: the documents equal the template's expansions of the same values, and the SQL path yields the
inserts. An unknown record is skipped with a message. The round trip also passes on the module path.

**Fixed (2026-11-11, Claude, in a session started from T31's); retired 2026-11-11 (Luc).**
All in `modules-template/prov-template-compiler`.
- **The key** (`log2prov/FileBuilder`). `register` puts a builder under its fully qualified name, the record's first
  column, and under its short name, so logs written before af3fa56ec still read (`FileBuilder.keys()`; the instance
  `register(Map)` does the same). A short name that is already another template's key is left to it, with a warning; a
  fully qualified name replaces, with a warning. `processRecord` looks the builder up first and skips an unknown record
  with a warning (`unknown record <name>: skipped (known: …)`) before any proxy is made; the client builder's proxy is
  made only for the SQL path. *Decided by Claude:* both keys rather than the fully qualified name alone (the ticket's
  alternative), and warning level so the skip shows under log4j's default configuration.
- **The SQL path** (`compiler/common/CompilerCommon.generateToBeanMethod`). A simple template's client builder now has
  `toBean(Object[])`, delegating to `record2bean` (generated in Java and Python), as a composite's builder already had.
  *Decided by Claude:* generate the method (the ticket's first option), so `ProxySQLInterface` and the composites'
  `toBean` stay as they are. Client libraries generated before this fix still lack it, and need regenerating for the SQL path.
- **The module path** (`module-info.java`). `org.openprovenance.prov.template.log2prov.interfaces` is exported.
- **T26, item 1, the trivial part.** `FileBuilder.registerBuilders` skips a null slot of the generated `Init` with a
  warning instead of `Class.forName(null)`. T26's other items (a FileBuilder for composites, the composite `toBean`'s
  `List<Object[]>` parameter, which the proxy would call with an `Object[]`, the literal `\n` in composite records) are
  untouched; the two-key registry covers item 2 once composites have a builder.
- **Test** `T32Log2ProvTest` (resources `src/test/resources/t32/`: one simple template, `t32.Review`, with an `xsd:int`
  and an escaped attribute). It generates the catalogue with `ConfigProcessor.main` in a child JVM, compiles the
  generated cli and l2p code with a driver (javax.tools), and runs the driver in a child JVM twice: on a plain class
  path, and with the compiler and its dependencies on the module path (`--add-modules prov.template.compiler`, checked
  to be the named module) and the generated code on the class path. The driver writes two records with
  `ReviewBuilder.logReview`, a record of an unknown template, and one keyed by the short name, and reads them back with
  `FileBuilder.reader` and a `RecordSQLProcessor`. Asserted: the unknown record is skipped with the message; the three
  documents equal the `Instantiater`'s expansions of the template with the same values (statements compared, the
  bundle's generated name aside); the SQL holds the client's own insert head and tuples. With each of the three fixes
  reverted in turn, the test fails with the defect as described above (the null-proxy NPE, the missing `toBean` NPE,
  the `IllegalAccessError` on the module path).
- **The suite.** The compiler's 168 tests pass (`mvn -o -pl modules-template/prov-template-compiler -am test` restricted to
  the module's tests). The generation runs in a child JVM because a generation in the test JVM breaks a later
  `CompilerTest` (the Python emitter's NPE, `Python.typeSimpleName`, noted in T31's phase 1). That is pre-existing: at
  HEAD, `T31CasesTest` run before `CompilerTest` (`-Dsurefire.runOrder=reversealphabetical`) fails the same way; the
  default order runs `CompilerTest` first.
- **Seen, not fixed (pre-existing, for Luc to file or not).** In the SQL the round trip produces, a null optional
  value is written `''`, also for the `xsd:int` column (`('r2','4712','John Doe','p10','','')`), which PostgreSQL refuses
  for an integer; and a comment `good, "clear"` is written `'good, \"clear\"'`, with backslashes a standard SQL string
  keeps.

---

## ✅ T29: ttf execution is slow (2026-10-24)

**Filed 2026-10-22** (Luc: `mvn test` in provenance-templates-library takes minutes executing its ttfs;
`modules-tools/prov-toolbox-plugin/.../ttf/BatchExecutor.java` → `modules-template/prov-template/.../core/ttf`).

**Measured** (2026-10-22, the eight `execute-ttf` executions of `provenance-templates-library/pom.xml`, 131 tasks — 110
instantiate, 21 merge — run through a harness timing each task and each (de)serialiser, Linux, 4 cores, graphviz 2.43).
Before: 25.8 s in the executor (29.6 s wall), of which 17.9 s serialisation, 0.9 s deserialisation, 5–7 s the rest
(instantiation, index files, schema checks, csv records). Text formats are negligible (provn 0.5–6 ms, jsonld 5–27 ms per
document); every png, svg, qualified.png, qualified.svg output is one `dot` process: png ~27–105 ms, svg ~17–28 ms
(micro-benchmark: `dot -V` 5 ms, layout 15 ms, png render 45 ms, svg 16 ms; png+svg from one run 40 ms). The 21 merge tasks
took 13.4 s of the 25.8 s: `MergeTask` serialised the merged document once per (input other than the first) × format,
so the 327 image outputs of the library were rendered 529 times. The index of the output directory was re-read by every
task (two or three `index.json` reads, ~4 ms each) and rewritten by every `addEntry` (~2 ms, one per format and one per
provenance record) — ~20 ms per task, ~3 s. Java-side CPU is small: a JFR profile of the 62 triangle tasks sampled 40
execution samples in 8 s, the JVM waiting on `dot` the rest. On macOS, where a `dot` process start is far dearer, the
count of `dot` runs is what the minutes are made of.

**Done** (commit 62915e0, merged 2026-10-24). `MergeTask` writes each format once (the merging records per input kept as
before); `BatchExecutor` holds one `TemplateIndex` per directory for its life (`index`, `indexPath`), the tasks `putEntry`
and `persist()` once per task (`TemplateIndex` gained the deferred methods, its methods synchronized);
`BatchExecutor.serializeAll` writes the text formats in turn, then the renderings (png, svg, pdf and qualified variants)
concurrently through `parallelStream` (off with `-Dorg.openprovenance.prov.template.ttf.parallel=false`); output streams
closed. `MergeTaskTest`.

**Results.** Library here: 11.9 s in the executor (17.9 s wall), merges 3.0 s; the 671 generated files identical to before
but for the vargen UUIDs, `index.json` equal as parsed JSON. nextgen/template, `make ttf.execute` on Luc's Mac
(2026-10-24): 2 min 22 s before; 1 min 07 s with the renderings sequential (the merge and index fixes alone, 2.1×);
15.3 s with them concurrent (9.3×); the 1229 generated files, indexes included, byte-identical to the old code's.

What remains — one `dot` run per layout, one `ajv` run per batch, `dot`'s stderr — is T30 in `TODO.md`.

---

## ✅ T27: ptm templates: a document derived from one input only (2026-10-09)

ptm templates: the document derived from one input only. ttf-ptm.json now builds ptm-expanding
    and ptm-merging from generic/product2-2 instead of parallel2 (same variables, plus the cross derivations), so
    the document and the provenance record each derive from both inputs: template and bindings (expanding),
    template1 and template2 (merging). provenance-templates' library: all 559 output documents derive from every
    input their activity used (438 from bindings). ptm-mexpanding also from product2-2 (template and provenance
    each derive from mtemplate and bindings).

---

## ✅ T9: Rust code generation: arithmetic on an Option (by 2026-05-24)

rust code generation:
```text
   --> src/org/openprovenance/templates/catalogue/transport/integrator/bean_completer2.rs:510:42
    |
510 |             bean.count = Some(bean.count + 1);
    |                               ---------- ^ - {integer}
    |                               |
    |                               std::option::Option<i32>
    |
note: the foreign item type `std::option::Option<i32>` doesn't implement `Add<{integer}>`
   --> /Users/luc/.rustup/toolchains/stable-aarch64-apple-darwin/lib/rustlib/src/rust/library/core/src/option.rs:599:1
    |

```

---

## ✅ T19: Atype icons in the navigation display (by 2026-05-24)

update navigation display to visualise atype icons, and fallback on template icons if atype icons not available

(Listed in the Done section of `TODO.md`, under T9, without a ✅.)

---

## ✅ T20: Streaming progress for /templates/viz (2026-05-19)

The viz request used to run three stages sequentially behind a single
synchronous `POST /provapi/templates/viz` returning `image/svg+xml`,
with the browser showing a single "Loading…" spinner for the full span.
Now decomposed into observable stages and exposed via an SSE endpoint,
so the browser shows per-stage progress as work happens.

#### What landed

**Server-side** (`service-templates-core`):
- New package `org.openprovenance.prov.service.core.progress` with
  transport-agnostic listener machinery:
  - `ProgressListener` — interface with `started`, `done`, `failed`,
    `detail`, `progress` methods (last three defaulted to no-op).
  - `NoOpProgressListener.INSTANCE` — singleton default.
  - `LoggingProgressListener` — emits one log4j line per callback.
  - `VizStages.SQL` / `PROV_BUILD` / `RENDER` — hierarchical name constants.
- `TemplateService.getTemplatesViz` → `TemplateLogic.generateViz` →
  `TemplateQuery.generateViz` → `TemplatesToDot.convert` now take a
  `ProgressListener` parameter. Three bracket points (around
  `recursiveTraversal`, around the DOT-file write, around `dot -Tsvg`)
  emit `started`/`done`/`detail`, with `detail` carrying connection count,
  DOT byte size, and SVG byte size respectively.
- New sub-package `progress.sse` with `SseProgressListener`, which turns
  each callback into a JSON `stage` event written to a JAX-RS
  `SseEventSink`. Adds two terminal methods: `result(mediaType, byte[])`
  (base64 inline) and `error(Throwable)`.
- New endpoint `POST /templates/viz/stream` returning `text/event-stream`
  with the same `TemplatesVizConfig` JSON body as the legacy endpoint.
  Runs synchronously on the request thread — async-executor handoff was
  tried first but RESTEasy 6.2.14 (or the pac4j filter in front of it)
  closed the underlying Jetty response after the resource method returned
  `void`, leading to `EofException: Closed` on every send after the
  first. Sync execution keeps the response open for the full ~600 ms.
  `SseProgressListener.send` blocks on the returned `CompletionStage` to
  guarantee ordering and prevent close/flush races.
- The synchronous `/templates/viz` endpoint stays as-is for back-compat;
  it now uses `LoggingProgressListener` so per-stage timings appear in
  the server log even without a streaming client.

**Client-side** (`forms.html:submitNavigate`):
- `XMLHttpRequest` replaced with `fetch` + `ReadableStream` reader that
  splits on `\n\n`, extracts `event:` / `data:` lines, JSON-parses each
  payload, and dispatches on event name (`stage`, `result`, `error`).
- New three-row stage panel rendered into `#navigator_div` showing SQL /
  Provenance build / Graphviz render. Each row flips spinner → tick +
  duration on `stage:done`, or cross + error on `stage:failed`. Detail
  messages (connection count, byte sizes) sit alongside the duration.
- On `result`: base64-decoded via `atob` + `Uint8Array` + `TextDecoder`,
  injected into `#navigator_div`, then `rewriteImageHrefs`,
  `wireNavigatorNodeEvents`, `updateMinimap`, and `navigator_save`
  enabling fire — same wiring as the old `xhr.onload`.
- `AbortController` stashed in `_currentVizStream`; back-to-back clicks
  abort the previous request so stale events don't paint the fresh panel.

#### Measured outcome (AP-60 dataset, `goods_transporting:4129`)

| Stage | Time | Detail |
|---|---:|---|
| `viz.sql` | ~270 ms | 1 079 connections |
| `viz.prov-build` | ~27 ms | DOT 2.2 MB |
| `viz.render` | ~345 ms | SVG 5.1 MB |
| **Total** | **~640 ms** | |

After the index work in odoodemo T-15, SQL is no longer dominant —
Graphviz is the heaviest single stage, which is precisely the kind of
graph for which staged progress changes how the wait feels.

#### Reusability beyond viz

The listener interface and SSE transport are deliberately generic. Other
long-running endpoints (document conversion, template expansion, bulk
import, large search, export pipelines, anything currently fronted by
Quartz) can adopt the same pattern by accepting an optional listener and
exposing a `/stream` sibling. Suggested future adapters: `MetricsProgressListener`
(Micrometer/Prometheus) and `CompositeProgressListener` (fan-out).

---

## ✅ T1: template-compiler: the Common Bean code generation (by 2026-05-24)

template-compiler: updated CompilerCommon to use the new code generation pattern for the Common Bean, and removed the old code.

---

## ✅ T14: An association without agent but with a plan (by 2026-05-24)

template expansion with association without agent but with plan, does not include association.

---
