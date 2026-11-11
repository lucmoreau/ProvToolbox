# ProvToolbox DONE — Completed Tasks

> Tasks retired from `TODO.md`. Each entry records the task id, its completion date and what was done.
> When a task is completed, remove it from the TODO index, move its section here and add a row to the index below.
> Dates written "by YYYY-MM-DD" are the first commit of `TODO.md` that lists the task as done.

---

## Update history

- **2026-11-11** — T32 (log2prov cannot read a simple template's records) retired here.
- **2026-10-24** — T29 (ttf execution is slow) retired here; T30 filed for the `dot` runs that remain.
- **2026-10-16** — `TODO.md` and `DONE.md` restructured on the pattern of nextgen's `ODOO-TODO.md` / `ODOO-DONE.md`;
  the Done section of `TODO.md` (T27, T1, T14, T9, T19, T20) moved here. T28 filed.

---

## Completed Task Index

| T# | Description | Completed | Section |
|----|-------------|-----------|---------|
| T32 | log2prov cannot read a simple template's records (fully qualified name vs short name key, no `toBean` in the client builder, `log2prov.interfaces` not exported) — fixed; round trip tested on class path and module path | 2026-11-11 | [T32 section](#-t32-log2prov-cannot-read-a-simple-templates-records-2026-11-11) |
| T29 | ttf execution (`execute-ttf`): the merge rewrote every format once per input, every task re-read and rewrote `index.json`, the `dot` renderings ran one at a time — fixed; nextgen/template `make ttf.execute` 2 min 22 s → 15.3 s | 2026-10-24 | [T29 section](#-t29-ttf-execution-is-slow-2026-10-24) |
| T27 | ptm templates: an expanded/merged document derived from one input only — now from both (template and bindings; template1 and template2) | 2026-10-09 | [T27 section](#-t27-ptm-templates-a-document-derived-from-one-input-only-2026-10-09) |
| T9 | Rust code generation: `bean.count + 1` on an `Option<i32>` | by 2026-05-24 | [T9 section](#-t9-rust-code-generation-arithmetic-on-an-option-by-2026-05-24) |
| T19 | The navigation display visualises atype icons, falling back on template icons | by 2026-05-24 | [T19 section](#-t19-atype-icons-in-the-navigation-display-by-2026-05-24) |
| T20 | Streaming progress for `/templates/viz` (SSE endpoint, per-stage panel in the navigator) | 2026-05-19 | [T20 section](#-t20-streaming-progress-for-templatesviz-2026-05-19) |
| T1 | template-compiler: CompilerCommon uses the new code generation pattern for the Common Bean | by 2026-05-24 | [T1 section](#-t1-template-compiler-the-common-bean-code-generation-by-2026-05-24) |
| T14 | Template expansion keeps an association without agent but with a plan | by 2026-05-24 | [T14 section](#-t14-an-association-without-agent-but-with-a-plan-by-2026-05-24) |

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
