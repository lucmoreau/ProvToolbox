# Jackson 2 → Jackson 3 migration plan

Living document: **whoever works on the migration updates the status columns and the
progress log after every step, and commits it with the work.** A new session should be able
to resume from this file alone.

- Branch: `jackson3-port`, off `scala3-port` (which is now Scala 3 only, see SCALA3_MIGRATION.md).
- Started: 2026-11-03.

## ▶ Current status and next steps

*(updated as work progresses, see the progress log at the end)*

**Phase A (analysis) is done.** The answer to "do all libraries support Jackson 3?" is
**no**: pac4j (+ scribejava) has no Jackson 3 release, and Spring Boot 2.7 needs a
major upgrade (to 4.x) first. All the others have one (json-schema-validator 3.x,
zjsonpatch 0.6 under new coordinates, swagger-core 3.0, mongojack 6), but usually as a new
major version with its own API changes. See section 3.

**Decisions D1–D6 confirmed by the maintainer (2026-11-03)**, all as proposed (section 4).
**Phase B is done** (JDK 17, Jackson 3 BOM, baseline; B1 verified identical to the baseline).
C1 (prov-model) needs no change.

**C2 is DONE (2026-11-03).** prov-jsonld is on Jackson 3, together with the modules its change
broke (see the C2 table in section 5). Some are fully migrated, some partly or with a marked
temporary bridge, to be completed in C5 and C7.
Verification (full comparison build): **test results identical to the baseline for every module**;
outputs identical except known noise, plus 6 provman query-result JSON files that were
**truncated (invalid JSON) with Jackson 2** and are now complete and valid (see 5b #11).

**C3: prov-jsonld-xml is retired** and **PROV-XML support is dropped** (step 1 done; step 2,
the services' advertised formats, still TODO: see the C3 row).

**C5 is DONE (2026-11-03).** prov-template, prov-template-compiler (incl. generated code and
poms), template library, and the Jackson 2 left in prov-interop, prov-service-translation,
prov-storage-mongodb's test and prov-toolbox-plugin. Comparison build: tests identical to the
baseline (except the expected PROV-XML removals: prov-jsonld-xml, and roundtrip, whose only test
was the XML one); outputs as after C2. **On the maintainer's machine: `modules-template` builds
(incl. the generated `templates_cli`), and prov-storage-mongodb passes 151/151 with MongoDB**
(after the maintainer's fix e901e85, catalogue #21).

**C4 is DONE (2026-11-03)**: prov-model-scala (its Java `jsonld11` serialisers), prov-scala-misc,
plus benchmarks and roundtrip. Comparison build: tests identical to the baseline (except the
PROV-XML removals), outputs as after C5.

Still on Jackson 2 (files using the API): prov-nf 21, nlg-wrapper 19, service-templates-core 12,
nlg-spec-transformer 8, prov-service-core 7, service-metrics 5, prov-service-xplain 3,
prov-explanations 3, and a few 1–2 file modules (services, provman, prov-summarisation, prov-query).

**Next:** C6 (storage: only redis/filesystem/api done in C2, check), C7 (prov-nf, summarisation,
typemap, NLG modules, query, explanations, provman; remove the xplain and summary bridges), C8
(services), PROV-XML step 2.

## 1. What changes in Jackson 3 (summary of what matters here)

| Topic | Jackson 2 | Jackson 3 |
|---|---|---|
| Maven groupId | `com.fasterxml.jackson.core` (core, databind), `com.fasterxml.jackson.dataformat`, `com.fasterxml.jackson.module`, `com.fasterxml.jackson.jakarta.rs`, `com.fasterxml.jackson.jaxrs` | `tools.jackson.core`, `tools.jackson.dataformat`, `tools.jackson.module`, `tools.jackson.jakarta.rs`, `tools.jackson.jaxrs`; BOM `tools.jackson:jackson-bom` |
| Annotations | `com.fasterxml.jackson.core:jackson-annotations` 2.x | **unchanged**: same artifact and package (`com.fasterxml.jackson.annotation`), versions 2.20+ without a patch number (2.21, 2.22). Jackson 2 and 3 share it. |
| Java packages | `com.fasterxml.jackson.{core,databind,dataformat,module}` | `tools.jackson.{core,databind,dataformat,module}`. `com.fasterxml.jackson.annotation` is unchanged |
| JPMS module names | `com.fasterxml.jackson.core`, `...databind`, `...dataformat.xml`, `...dataformat.yaml` | `tools.jackson.core`, `tools.jackson.databind`, `tools.jackson.dataformat.xml`, `tools.jackson.dataformat.yaml` (annotation module unchanged) |
| JDK | 8+ | **17+** |
| Mapper configuration | mutable `ObjectMapper` (`registerModule`, `configure`, `enable`, `setSerializationInclusion`...) | **immutable**: `JsonMapper.builder()...build()`, `XmlMapper.builder()`, `mapper.rebuild()` |
| Exceptions | checked `JsonProcessingException extends IOException` | **unchecked** `JacksonException extends RuntimeException`. `readValue`/`writeValue` no longer throw `IOException`, so `catch (IOException e)` around pure Jackson calls is a **compile error** (unreachable catch) |
| Custom (de)serializers | `JsonSerializer`, `JsonDeserializer`, `SerializerProvider` | `ValueSerializer`, `ValueDeserializer`, `SerializationContext` (`StdSerializer`/`StdDeserializer` kept) |
| Streaming API renames | `writeStringField`, `writeFieldName`, `writeObject`, `getCurrentName`, `getText` | `writeStringProperty`, `writeName`, `writePOJO`, `currentName`, `getString` |
| Tree model renames | `JsonNode.fields()`, `asText()`, `textValue()`, `isTextual()`, `TextNode` | `properties()`, `asString()`, `stringValue()`, `isString()`, `StringNode` |
| **Default settings changed** | e.g. properties in declaration order, dates as timestamps | e.g. `SORT_PROPERTIES_ALPHABETICALLY` on, `WRITE_DATES_AS_TIMESTAMPS` off, `FAIL_ON_NULL_FOR_PRIMITIVES` on, `FAIL_ON_TRAILING_TOKENS` on. **This can change the bytes ProvToolbox writes** (PROV-JSON / JSON-LD / XML output, template bindings), see decision D2 |
| Java 8 date/time, `Optional` | separate modules (jsr310, jdk8) | built into databind |
| Scala module | `jackson-module-scala_3` 2.x (`com.fasterxml...`) | `tools.jackson.module:jackson-module-scala_3` 3.x, Java 17+ |

Tooling: OpenRewrite has a recipe (`org.openrewrite.java.jackson.UpgradeJackson_2_3`) for poms
and Java sources. It **does not handle Scala**, Scala strings, or the Java source text that
our template compiler generates.

## 2. Inventory of Jackson in ProvToolbox

### 2.1 Direct Jackson artifacts (poms)

| Jackson 2 artifact | # poms | Jackson 3 artifact | Available? |
|---|---|---|---|
| `jackson-databind` | 11 | `tools.jackson.core:jackson-databind` | ✅ |
| `jackson-core` | 4 | `tools.jackson.core:jackson-core` | ✅ |
| `jackson-annotations` | 6 | unchanged (`com.fasterxml.jackson.core:jackson-annotations` 2.22) | ✅ |
| `jackson-dataformat-xml` | 4 | `tools.jackson.dataformat:jackson-dataformat-xml` | ✅ 3.2.4 |
| `jackson-dataformat-yaml` | 1 (prov-template) | `tools.jackson.dataformat:jackson-dataformat-yaml` | ✅ 3.2.4 |
| `jackson-module-scala_3` | 4 | `tools.jackson.module:jackson-module-scala_3` | ✅ 3.2.4 |
| `jackson-module-jakarta-xmlbind-annotations` | 1 (prov-nf) | `tools.jackson.module:jackson-module-jakarta-xmlbind-annotations` | ✅ 3.2.4 |
| `jackson-jakarta-rs-json-provider` | 2 (service-xplain, service-summary) | `tools.jackson.jakarta.rs:jackson-jakarta-rs-json-provider` | ✅ 3.2.4 |
| `jackson-jaxrs-json-provider` (javax) | 6 (service-metrics, prov-service-security-pac, service-translator, service-templates-core, service-templates, archetype) | `tools.jackson.jaxrs:jackson-jaxrs-json-provider` | ✅ 3.2.4 (but see note: should these be the jakarta one?) |

### 2.2 Source footprint (tracked `.java` + `.scala`)

About **520 files in 38 modules** import Jackson. Of these, ~120 only use annotations
(`com.fasterxml.jackson.annotation`, **unchanged** in Jackson 3); the rest use the API.

| Module | annotation-only files | API files |
|---|---|---|
| modules-core/prov-jsonld | 16 | 53 |
| modules-core/prov-jsonld-xml | 11 | 48 |
| modules-core/prov-model-scala (Scala+Java) | 17 | 40 |
| modules-template/prov-template-compiler | 22 | 21 |
| modules-template/prov-template | 6 | 20 |
| modules-nf/prov-nf (Scala) | 1 | 21 |
| modules-explanations/nlg-wrapper (Scala) | – | 19 |
| modules-misc/prov-scala-misc (Scala) | – | 18 |
| modules-services2/service-templates-core | 2 | 12 |
| modules-storage/prov-storage-mongodb | 2 | 7 |
| modules-services/prov-service-core | – | 7 |
| modules-validation/prov-validation | 3 | 6 |
| modules-services-metrics/service-metrics | 1 | 6 |
| ~25 other modules | | 1–8 each |

API features used (files / occurrences): mapper construction 103/139; **mapper mutation
(`registerModule`, `configure`, ...) 27/159**; `JsonProcessingException` 34/88; custom
`JsonSerializer`/`JsonDeserializer` 31/66; `SerializerProvider` 28/60;
`StdSerializer`/`StdDeserializer` 34/65; `JsonNode` accessors to rename 26/118; streaming
generator/parser renames ~32 files; `TypeReference` 7/31; XML (`XmlMapper`,
`@JacksonXml...`) 72/252; jackson-module-scala 4/17.

### 2.3 Non-obvious places

- **`module-info.java`** (JPMS) in prov-model, prov-jsonld, prov-jsonld-xml, prov-interop,
  prov-service-core, prov-storage-api, prov-storage-filesystem, prov-template,
  prov-template-compiler (+ test module-infos): `requires com.fasterxml.jackson.{core,databind,dataformat.xml,dataformat.yaml}`
  → `tools.jackson.*`. `requires com.fasterxml.jackson.annotation` stays.
- **Code generation in prov-template-compiler.** It emits Java code that uses Jackson
  (e.g. `CompilerClientTest.java`: `ClassName.get("ObjectMapper", "com.fasterxml.jackson.databind")`),
  and generates Maven poms with a Jackson dependency (`CompilerMaven.java`:
  `dep.setGroupId("com.fasterxml.jackson.core")`). Generated projects and the
  **archetype** (`modules-tools/prov-template-archetype`) must move too. Code that
  references Jackson classes via `ClassName.get(X.class)` follows the import change.
- **jsweet candy** (`prov-jsweet-candy-java`): only uses annotations → unaffected.
- `DEPENDENCIES.md`, `src/license/tree.txt` files: regenerate at the end.

## 3. Third-party libraries and Jackson 3 support (the key question)

**Short answer: not all of them.** Every third-party library that uses Jackson has a
Jackson 3 version **except pac4j** (and scribejava, which pac4j pulls in). Spring Boot
only supports Jackson 3 from 4.x, and this project is on 2.7.

The list below is complete for the whole reactor: it comes from
`mvn dependency:tree -Dverbose=true` over all 72 resolvable modules (A2). It shows every
non-ProvToolbox artifact that declares a Jackson dependency.

| Library (current) | Brings (Jackson 2) | ProvToolbox modules affected | Jackson 3 support | Action |
|---|---|---|---|---|
| networknt `json-schema-validator` **1.5.9** | databind, dataformat-yaml 2.18.3 | **direct**: prov-template and prov-template-compiler (compile scope), prov-jsonld and prov-storage-mongodb (test scope only). **Transitively in 22 modules (compile/provided)** via prov-template, mostly through prov-interop (6 Java files use its API: `SchemaCheck` in prov-template; `CompilerJsonSchema`, `CompilerDocumentation`, `JsonSchemaTesting` in prov-template-compiler; tests in prov-jsonld and the compiler) | ✅ **3.x line** (3.0.8; Java 17 + Jackson 3). Its 2.x line is still Jackson 2 | upgrade 1.5.9 → 3.0.x in **two steps**: first 1.5.9 → **2.0.x** (new API, still Jackson 2, so it can be done and tested before any Jackson change), then 2.0.x → 3.0.x (same API, Jackson 3). API mapping (from the project's `doc/migration-2.0.0.md`): `JsonSchemaFactory` → `SchemaRegistry` (`SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7)`), `JsonSchema` → `Schema`, `ValidationMessage` → `Error` (`getMessage()` no longer contains the instance location; `validate` returns a `List`), `SpecVersion.VersionFlag` → `SpecificationVersion`, `SchemaValidatorsConfig` → `SchemaRegistryConfig` (+ `WalkConfig`, `ExecutionConfig`) |
| flipkart `zjsonpatch` **0.4.14** | core, databind 2.14 | prov-template | ✅ **moved to `io.github.vishwakarma:zjsonpatch` 0.6.x** (0.6.4): `Jackson3JsonPatch`/`Jackson3JsonDiff`, Jackson an optional dependency | change coordinates, use the Jackson3* classes, declare databind 3 |
| swagger-core **2.2.41** (`swagger-jaxrs2-jakarta`, `-core-jakarta`, `-models-jakarta`) | databind, yaml, jsr310, jakarta-rs-json-provider 2.19.2 | 13 service modules (prov-service-*, service-*) | ✅ **swagger-core 3.0.0** (released 2026-10-28): Jackson 3 only, Java 17, **`swagger-jaxrs2-jakarta` renamed `swagger-rest`**. 2.2.x stays on Jackson 2 | upgrade to 3.0.x. Very new, so check maturity; can be done last (services) |
| `mongojack` **5.0.3** | databind, jsr310 2.16.1 | prov-storage-mongodb + 9 service modules | ✅ **6.0.0**: Jackson 3, Java 17, **MongoDB driver 5.x only** | upgrade to 6.0.x (+ Mongo driver 5, check `MIGRATING.md`) |
| pac4j **5.7.9** (`pac4j-http`, `-oauth`, `-oidc`) | databind 2.14 | prov-service-security-pac, service-metrics, service-templates, service-templates-core | ❌ **no Jackson 3 release.** pac4j master (6.6.1-SNAPSHOT) uses Jackson **2.22.4**. pac4j 6 needs JDK 17 + jakartaee-pac4j 8 | **Jackson 2 island** (decision D3); upgrade pac4j 5.7 → 6.x separately |
| scribejava **8.3.3** (via pac4j-oauth) | databind 2.14 | same 4 modules | ❌ Jackson 2 (8.3.3 is the latest release, from 2022; the project looks inactive) | goes with pac4j |
| Spring Boot **2.7.6** (`spring-boot-starter-json`) | databind, jdk8, jsr310, parameter-names 2.13.4 | service-templates (+ the archetype's generated `-service` project) | ✅ only in **Spring Boot 4.x** (Jackson 3 default, Jackson 2 deprecated). 2.7 → 4 is a large separate upgrade (Spring 6/7, Boot 3 → 4) | **Jackson 2 island** until Spring Boot is upgraded (D3) |

Checked and **not** using Jackson: titanium-json-ld (Jakarta JSON-P), Apache Jena 5.x,
RESTEasy 6.2 core (JSON goes through Jackson's own JAX-RS provider), jedis, xmlsec,
SimpleNLG/lexaccess, graalvm polyglot, netty, quartz, javapoet, antlr, jsweet, izpack,
commons-*, Maven libraries. log4j-core's JSON layout (optional) is not used.

Coexistence: Jackson 2 (`com.fasterxml.jackson.{core,databind}`) and Jackson 3
(`tools.jackson.*`) have different packages and artifacts, so both can be on one classpath.
They **share** `jackson-annotations` (2.20+), which works with both. A library on Jackson 2
keeps its own `ObjectMapper` and its own Jackson 2 jars. So "Jackson 2 islands" are possible,
but Jackson 2 isn't fully gone until pac4j and Spring Boot are dealt with.

### 3.1 Jackson 2 artifacts currently on the classpaths (dependency trees, 2026-11-03)

| Artifact | Versions seen | Origin |
|---|---|---|
| jackson-annotations | 2.21 | ProvToolbox (kept in Jackson 3) |
| jackson-core, jackson-databind | 2.21.2 | ProvToolbox (managed) |
| jackson-dataformat-xml | 2.21.2 | ProvToolbox |
| jackson-dataformat-yaml | 2.18.3, 2.19.2 | json-schema-validator, swagger (ProvToolbox's own prov-template declares it too) |
| jackson-datatype-jsr310 | 2.16.1, 2.19.2 | mongojack, swagger (built into databind 3) |
| jackson-datatype-jdk8, jackson-module-parameter-names | 2.13.4 | Spring Boot 2.7 |
| jackson-jakarta-rs-base / -json-provider | 2.19.2, 2.21.2 | swagger, ProvToolbox services |
| jackson-jaxrs-base / -json-provider (javax), jackson-module-jaxb-annotations | 2.21.2 | ProvToolbox (6 poms) |
| jackson-module-jakarta-xmlbind-annotations | 2.19.2, 2.21.2 | swagger, prov-nf |
| jackson-module-scala_3 | 2.21.2 | ProvToolbox Scala modules |

## 4. Decisions (confirmed by the maintainer on 2026-11-03, all as proposed)

- **D1 – One step or incremental?** ✅ Confirmed: incremental, module by module in dependency
  order, as for Scala 3. Jackson 2 and 3 can coexist on one classpath (different packages,
  shared annotations), so the build stays green between steps. Unlike Scala 3, no
  side-by-side copies are needed.
- **D2 – Output compatibility.** ✅ Confirmed: keep Jackson 2 output (`builderWithJackson2Defaults()`). Jackson 3's new defaults (alphabetical property order, ISO
  dates...) can change serialized PROV. Proposal: build every mapper with
  **`JsonMapper.builderWithJackson2Defaults()`** (or the equivalent `XmlMapper`
  builder) to keep byte-identical output, and verify with round-trip tests and output diffs.
  Moving to the Jackson 3 defaults can be a separate, deliberate change later.
- **D3 – Libraries without Jackson 3 support** ✅ Confirmed: leave Jackson 2 there for now, migrate those modules' own code. (pac4j; Spring Boot 2.7 until upgraded):
  keep Jackson 2 *only* in those modules (coexistence), and record them as "remaining
  Jackson 2" in this file.
- **D5 – JDK 17 everywhere.** ✅ Done (B0). Jackson 3 jars are Java 17 class files. Java modules still
  compile with `release 15` (root pom, maven-compiler-plugin), and javac `--release 15`
  cannot read them. So step B0 raises the root `release`/`source` to 17. The Scala modules
  already use 17. The JSweet candies (`release 11`) only use the shared annotations, so
  they are not affected.
- **D6 – ProvToolbox's own public API changes.** ✅ Confirmed: break it, new major version. About 57 public methods in ProvToolbox
  (prov-jsonld 12, prov-jsonld-xml 12, prov-storage-mongodb 8, prov-template 5,
  nlg-wrapper 5, service-templates-core 4, prov-model-scala 4, ...) take or return Jackson
  types (`ObjectMapper`, `JsonNode`, `JsonGenerator`...), and custom serializers are
  public classes. Several follow the "pass an `ObjectMapper` and mutate it" pattern (e.g.
  `ProvMixin` calls `mapper.addMixIn(...)`, and `ProvDeserialiser` calls `registerModule`), which
  must become "configure a `MapperBuilder`". Users of these APIs will see a breaking
  change. Proposal: accept it and release the result as a new major version of ProvToolbox
  (together with Scala 3 and JDK 17), and keep method names where possible
  (e.g. `ProvMixin.addProvMixin(MapperBuilder<?,?> b)` next to `addProvMixin(ObjectMapper)`).
- **D4 – Tooling.** ✅ Confirmed (with D1): OpenRewrite first for Java, Scala by hand. Use OpenRewrite `UpgradeJackson_2_3` for Java modules where it helps,
  then fix by hand. Scala (6 modules) by hand / sed.

## 5. Plan (phases and steps)

| Step | What | Status |
|---|---|---|
| A1 | Inventory of direct Jackson use (poms, sources, module-info, generators) | DONE |
| A2 | Dependency trees of the whole reactor: which third-party libs pull Jackson 2 | DONE (section 3, 3.1) |
| A3 | Check each library's Jackson 3 release (versions, API changes) | DONE (section 3). Library-specific API migration notes are written when each module is ported |
| B0 | Root pom: compiler `release`/`source` 15 → 17 (D5); full build + tests to check nothing else breaks | **DONE**: the whole reactor compiles with release 17; test results as in 5c |
| B1 | Root pom: add `tools.jackson:jackson-bom` (dependencyManagement) and a `jackson3.version` property; keep the Jackson 2 properties while modules migrate | **DONE**: `jackson3.version=3.2.4`, BOM imported. The BOM manages the shared `jackson-annotations` at 2.22, so `jackson.annotation.version` moved 2.21 → 2.22. Verified: full test run **identical** to the baseline (`test-summary.py --diff`), and test outputs identical apart from known noise (see 5a step 5) |
| B2 | Baseline: record current test results (`jackson3-migration/test-summary.py` over the build logs → `jackson3-migration/baseline-tests.tsv`) and sample outputs for later comparison | **DONE (cloud)**: `jackson3-migration/baseline-tests.tsv`, 31 modules with tests (unit tests only, `-DskipITs`; prov-storage-mongodb not run). Pre-existing failures listed in 5c. Caveat: the baseline was run in segments. Modules from service-translator onwards (last segment, `-rf :service-translator`) already ran with the B1 root pom (annotations 2.22 + BOM); the others ran before B1. Snapshot of 5,793 files written by tests under `target/` (json/jsonld/provn/xml/dot/svg/...): kept outside the repo; regenerate by running the baseline command on commit 3606168 |
| C1 | prov-model (annotations only: check, module-info) | **DONE, no change needed**: only `com.fasterxml.jackson.annotation` (2 files), which Jackson 3 keeps; `module-info` `requires com.fasterxml.jackson.annotation` stays |
| C2 | prov-jsonld (+ json-schema-validator 3.x), and the modules its change breaks | **DONE** — tests identical to baseline; see the detail table below |
| C3 | prov-jsonld-xml | **RETIRED by the maintainer (2026-11-03)**: commented out of `modules-core/pom.xml`. (Its Jackson 3 migration had been done, tests 147/147, commit 0332a1a; the sources stay in the tree.) **PROV-XML support dropped** (maintainer's decision): no module depends on it any more. Step 1 (done): dependency removed from prov-interop, roundtrip, benchmarks, prov-service-core, prov-storage-mongodb; PROVX reader/writer and the PROVX format registration (`.provx`/`.xml` extensions, `application/provenance+xml`) removed from prov-interop; `RoundTripFromJavaXMLViaLegacyTest` deleted; the validation service no longer stores an (unserved) XML report, and `ServiceUtilsConfig.serialiser`/`ObjectSerialiser` (only used for it) are removed; PROV-XML removed from the `TranslateIT`/`DateIT` integration tests (incl. archetype copies). `Formats.ProvFormat.PROVX` and the media-type constants stay in prov-model. **Step 2 (TODO)**: remove PROV-XML from what the services advertise (content negotiation and `@Produces` lists, swagger `@Content`, `ALLOWABLE_*_EXTENSIONS`, `UploadForm` enums, `ServletConfig` filter, message body writers, `ServiceUtils` cases) |
| C4 | prov-model-scala (Scala), prov-n, prov-scala-misc | **DONE** (full comparison build: tests identical to baseline except the PROV-XML removals; outputs as after C5): prov-model-scala **done** (its Java `jsonld11` copy of the JSON-LD serialisers: builder-based `ProvSerialiser(MapperBuilder, boolean)` and `customize2(MapperBuilder)`, `ProvMixin`/`ProvMixin2.addProvMixin(MapperBuilder)`, `AnySetterAttributesModule` reused from prov-jsonld; JUnit 149/149, ScalaTest 16/16, 406 outputs identical). prov-n: no Jackson. prov-scala-misc **done** (17 XML bean classes with `@JacksonXmlRootElement` + `BindingsJson`, all Java: OpenRewrite; JUnit 18/18, ScalaTest 6/6, 291 outputs identical). benchmarks and roundtrip (had Jackson 2 only transitively) migrated |
| C5 | prov-template (+ YAML, zjsonpatch), prov-template-compiler (+ generated code, generated poms), template library, archetype | **DONE** (archetype's service sources: with C8): prov-template migrated (tests 31 run, same 2 timezone failures as baseline; outputs identical; json-schema-validator 3.0.8; zjsonpatch → `io.github.vishwakarma:zjsonpatch` 0.6.4 `Jackson3JsonDiff` in tests). prov-template-compiler migrated (tests 158 run, same 1 error as baseline: nested `templates_cli` build needs the jsweet candy; outputs identical). The compiler's **generated** code now uses `JsonMapper.builderWithJackson2Defaults().build()` (`CompilerClientTest`) and generated poms depend on `tools.jackson.core:jackson-databind` 3.2.4 (`CompilerMaven`). **To verify on the maintainer's machine: the generated `templates_cli` project builds and its tests pass** (not possible in the cloud). Dependants migrated (compile): prov-interop (`InteropFramework`; no Jackson 2 left, module-info only `tools.jackson.databind`), prov-service-translation (`TemplateService`, `ActionExpand`), prov-storage-mongodb test, prov-template-library tests, prov-toolbox-plugin (`WebjarPreparer`). Comparison build: see progress log |
| C6 | prov-interop, storage (api, filesystem, redis, mongodb + mongojack 6) | **DONE**: migrated during C2/C5; C6 removed the last Jackson 2 (prov-storage-mongodb's test-scope json-schema-validator 1.5.9 → 3.x). `dependency:tree` of the 4 storage modules: no Jackson 2 |
| C7 | validation, nf, signature, summarisation, typemap, explanations (nlg-*), query, provman | **IN PROGRESS**: prov-validation (C2), **prov-nf** (JUnit 1, ScalaTest 94, 280 outputs identical), **prov-summarisation** (32/32) + **prov-typemap** (565 outputs identical), **provman** (2/2) done; prov-signature has no Jackson code. `TypePropagator.om` is now an immutable Jackson 3 mapper with the Scala module; callers that enabled `INDENT_OUTPUT` on it now use `writerWithDefaultPrettyPrinter()`. The prov-service-summary **bridge is removed** (back to `ndStorage.serializeObjectToStore(TypePropagator.om(), …)`). TODO: nlg-wrapper, nlg-spec-transformer, prov-query, prov-explanations; then remove the prov-service-xplain bridge |
| C8 | Services (prov-service-*, service-*): JAX-RS provider 3.x, swagger-core 3 | TODO |
| C9 | Remaining Jackson 2 islands (pac4j, Spring Boot) decided per D3 | TODO |
| D | Clean-up: remove Jackson 2 properties/dependencies, check `mvn dependency:tree` has no `com.fasterxml.jackson.core:jackson-databind`, regenerate DEPENDENCIES.md / license trees | TODO |

### C2 detail (done 2026-11-03)

| Module | State | Verification |
|---|---|---|
| prov-jsonld | **migrated** (OpenRewrite + manual): builder-based `ProvSerialiser(MapperBuilder, boolean)`, `ProvDeserialiser(MapperBuilder[, DateTimeOption[, TimeZone]])`, `customize(MapperBuilder)`, `ProvMixin.addProvMixin(MapperBuilder)`; `CustomNamespaceSerializer(boolean)` (no mapper); new `AnySetterAttributesModule` (fix #2); json-schema-validator 3.0.8 (test scope) | tests 660/660 as baseline; outputs identical except known noise |
| prov-validation | **migrated** (`Mapper.getValidationReportMapper()` builds from one builder) | tests 232/232 as baseline |
| prov-interop | **partly**: JSON/JSON-LD (de)serialiser factories on Jackson 3; `InteropFramework` still Jackson 2 for prov-template's `Bindings` (until C5); module-info requires both | compiles |
| roundtrip (Benchmarks test) | ProvSerialiser call updated | compiles |
| prov-storage-api, prov-storage-filesystem | **migrated** (`ObjectMapper` in the API is now Jackson 3) | compile |
| prov-storage-mongodb | **migrated**, **mongojack 5.0.3 → 6.0.0** (MongoDB driver 5.6.2); `MongoJackModule.configure(mapper)` replaces `registerModule`; test still reads `Bindings` with Jackson 2 | compiles; **tests need MongoDB** (not run) |
| prov-service-xplain | **temporary bridge** in `PhraseEnvironmentReader`: PROV document read with Jackson 3, the rest with nlg-spec-transformer's Jackson 2 Scala mapper (remove in C7). Behaviour change: the PROV deserialisers are no longer added to the *shared* `SpecLoader.mapper()` (check with the xplain ITs) | compiles |
| service-translator, service-metrics, service-templates (test `ValidationReportMessageBodyReader`), archetype copy | mix-in added on `mapper.rebuild()` | compile |
| provman | `UtilsXplain.exportToJson` builds prov-jsonld's serialiser from a Jackson 3 builder + `tools.jackson.module:jackson-module-scala_3` (rest of provman still Jackson 2) | tests 2/2 |
| prov-storage-index-redis | **migrated** (1 file; it only had Jackson 2 transitively) | compiles |
| prov-service-validation | **migrated** where it meets the storage API (`StorageSetup`: `MongoJackModule.configure(Mapper.getValidationReportMapper())`) | compiles |
| prov-service-translation | **partly**: `StorageSetup`, `BindingsResourceStorageFileSystem`, `TranslationService` on Jackson 3; `TemplateService` and `ActionExpand` deliberately **left on Jackson 2** (they only deal with prov-template's `Bindings`/templates; move with C5) | compiles |
| prov-service-summary | **temporary bridge** in `SummaryService` (`storeWithJackson2`/`loadWithJackson2`): summaries are prov-summarisation's Scala objects, written with its Jackson 2 mapper, stored as bytes (same bytes as before with the filesystem storage; the MongoDB storage does not support object storage). Remove in C7 | compiles |

## 5a. Recipe for migrating one module (repeat per module, in the order of section 5)

1. **Before**: make sure the module's baseline test line is in
   `jackson3-migration/baseline-tests.tsv` (step B2).
2. **pom**: replace each Jackson 2 dependency by its Jackson 3 equivalent (section 2.1).
   Drop `<version>`: the root pom imports `tools.jackson:jackson-bom` (B1). Keep
   `com.fasterxml.jackson.core:jackson-annotations` unchanged. Upgrade the third-party
   library to its Jackson 3 version at the same time (section 3).
3. **module-info.java** (main and test): `requires com.fasterxml.jackson.core` →
   `tools.jackson.core`, `...databind` → `tools.jackson.databind`,
   `...dataformat.xml` → `tools.jackson.dataformat.xml`, `...dataformat.yaml` →
   `tools.jackson.dataformat.yaml`. `opens ... to com.fasterxml.jackson.databind` → `to tools.jackson.databind`.
4. **Sources**:
   - Java: try OpenRewrite first (only on this module):
     `mvn -B -pl <module> org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.activeRecipes=org.openrewrite.java.jackson.UpgradeJackson_2_3 -Drewrite.recipeArtifactCoordinates=org.openrewrite.recipe:rewrite-jackson:RELEASE`,
     then review the diff and fix the rest by hand.
   - Scala: by hand (sed for the import renames, then the compiler errors).
   - Typical manual fixes: mutable mapper set-up → builder (`JsonMapper.builderWithJackson2Defaults()`,
     per D2); `catch (IOException)` around pure Jackson calls → `catch (JacksonException)`
     or remove; `JsonSerializer`/`JsonDeserializer`/`SerializerProvider` → `ValueSerializer`/
     `ValueDeserializer`/`SerializationContext`; streaming and tree-model renames (section 1).
     Add each new kind of fix to the catalogue (section 5b).
5. **Build and test** the module and its dependants. Compare with the baseline:
   `python3 jackson3-migration/test-summary.py <log> > after.tsv` then
   `python3 jackson3-migration/test-summary.py --diff jackson3-migration/baseline-tests.tsv after.tsv`.
   Diff the files the tests write (`target/`) against the B2 snapshot with
   `python3 jackson3-migration/compare-outputs.py <snapshot-dir> <file-list>` (it ignores
   run-to-run noise: timestamps, UUIDs, order of JSON arrays/lines).
   **Known noise** that differs even between two identical runs: prov-generator's random
   graphs (51 files); the `*8.via*.json[ld]` files of prov-jsonld (two "now" timestamps are one
   or two set elements depending on whether they fall in the same millisecond);
   `provconvert.txt`/`provmanagement.txt`/`staging/Readme.txt` (man-page text from the OS). The
   script already ignores timestamps, UUIDs, `[B@...` byte-array hashes (prov-n writes
   base64Binary values as `byte[].toString()`, a pre-existing bug), DOT blank-node ids and element
   order. Expected change: `prov-toolbox-plugin/target/plugin-enhanced.xml` lists the dependencies.
6. Check that the module no longer has Jackson 2 on its classpath (unless it is a D3 island):
   `mvn -B dependency:tree -pl <module> | grep -E "com.fasterxml.jackson.(core:jackson-(core|databind)|dataformat|module|datatype|jaxrs|jakarta)"`.
7. Update the status table and progress log, commit, push.

## 5b. Catalogue of fixes found so far

1. **OpenRewrite** (`UpgradeJackson_2_3`) does most of the mechanical work in Java: imports,
   groupIds, `JsonProcessingException`/`IOException` catches → `JacksonException`,
   `JsonSerializer` → `ValueSerializer`, `getText()` → `getString()`, `isTextual()` → `isString()`,
   etc. Use goal **`runNoFork`** when the module no longer compiles (e.g. because an upstream
   module or library is already on Jackson 3): `run` compiles first and fails. Its output needs a
   review:
   - it turns `new ObjectMapper()` into `new JsonMapper()` (**Jackson 3 defaults**). Replace it with
     `JsonMapper.builderWithJackson2Defaults().build()` (D2);
   - it removes `throws IOException` from methods but leaves a `throw new IOException(...)` in the
     body. Use `throw DatabindException.from(ctxt, msg)` in (de)serializers;
   - it leaves mutations of mappers (`registerModule`, `enable`, `addMixIn`, `getFactory().setCodec`)
     that no longer compile → builder (see #3).
2. **`@JsonIgnore` + `@JsonAnySetter` (silent data loss!).** Jackson 3 adds every `@JsonIgnore`d
   property name to the ignorable set **unconditionally** (databind #5952). Jackson 2 did it only for
   classes without an any-setter. ProvToolbox's JSON-LD mix-ins ignore `getType()`,
   `getLabel()`, `getLocation()`, `getRole()`, `getValue()` and route those keys to the
   any-setter `setIndexedAttributes`, so with Jackson 3 these **attributes were silently dropped**
   on reading (71 test failures in prov-jsonld). Fix: `AnySetterAttributesModule` (prov-jsonld,
   `...jsonld11.serialization.deserial`) — a deserializer modifier that rebuilds bean
   deserializers having an any-setter with `withByNameInclusion(ignored minus PROV keys, null)`.
   Registered by the JSON-LD `ProvDeserialiser.customize`. **Reuse it** for the other copies of the
   mix-ins (prov-model-scala's `jsonld11`, prov-jsonld-xml) when they migrate.
3. **Mutable mapper → builder.** Public methods that took an `ObjectMapper` and mutated it now take
   a `MapperBuilder<?, ?>` (D6). Where a mapper must be derived from an existing one:
   `mapper.rebuild()...build()`. `MapperBuilder.typeFactory()` replaces `mapper.getTypeFactory()`
   during configuration. `BeanSerializerModifier` → `ValueSerializerModifier`
   (`SetupContext.addSerializerModifier`, `modifySerializer(config, BeanDescription.Supplier, ser)`).
4. **No codec on generators**: `jsonGenerator.setCodec(mapper); jsonGenerator.writePOJO(x)` →
   `serializationContext.writeValue(jsonGenerator, x)`.
5. **json-schema-validator 1.x → 3.x**: see section 3. `Error.getMessage()` has no instance location
   any more, but `Error.toString()` has it (same text as 1.x `getMessage()`). `validate` returns a
   `List` (1.x: a `Set`), so use `distinct()`.
6. **mongojack 6**: `MongoJackModule.configure(mapper)` returns a configured copy (was
   `mapper.registerModule(MongoJackModule.DEFAULT_MODULE_INSTANCE)`).
7. **Classes with both Jackson 2 and 3 users** (e.g. prov-template's `Bindings` while prov-template is
   still Jackson 2): pass a Jackson 2 mapper explicitly
   (`new com.fasterxml.jackson.databind.ObjectMapper()`), with a comment pointing to the step that
   removes it.
8. A module that compiled against Jackson 2 **transitively** (prov-validation, redis via prov-jsonld)
   breaks when the upstream module moves to Jackson 3. Declare/migrate its own Jackson dependency.
10. **jackson-module-scala for Scala 3 under Jackson 3**: `tools.jackson.module:jackson-module-scala_3`
    (`DefaultScalaModule` in `tools.jackson.module.scala`). The Jackson 3 BOM manages only the
    `_2.11/_2.12/_2.13` artifacts, so give `${jackson3.version}` explicitly.
11. **Behaviour change (improvement) found by the output comparison**: provman's
    `UtilsXplain.exportToJson` wrote truncated, invalid JSON with Jackson 2 (it failed silently at
    the Scala `enumType` property). With Jackson 3 and its Scala module, the 6 affected files
    (`q.*.json`, `16.json`, `loan2-result.json`) are complete and valid.
13. **XML (jackson-dataformat-xml 3)**: `new XmlMapper(xmlFactory)` → `XmlMapper.builder(XmlFactory.builderWithJackson2Defaults().xmlInputFactory(..).xmlOutputFactory(..).build()).configureForJackson2()`
    (D2 for a custom factory; `XmlMapper.builderWithJackson2Defaults()` otherwise).
    `SetupContext.addDeserializationProblemHandler` → `addHandler`; `JsonGenerator.writeObject` → `writePOJO`;
    the StAX helpers' checked `IOException` → `JacksonIOException.construct(new IOException(e))`.
14. **Token-skipping that relied on Jackson 2 internals**: `nextValue(); readValueAsTree(); readValueAsTree();`
    worked in Jackson 2 only because `readValueAsTree()` cleared the current token. In Jackson 3 the
    parser stayed inside the element, and the rest of the document was misread (89 errors in
    prov-jsonld-xml: inner `<agent prov:ref=.../>` became top-level statements). Use
    `jsonParser.skipChildren()` to end on the element's END_OBJECT. Grep for `readValueAsTree`
    in the other modules. Also prefer `ctxt.readValue(p, type)` to `p.readValueAs(type)` in handlers.
16. **`asText()` → `asString()` changes behaviour (OpenRewrite rename)**: Jackson 2 `asText()` returned
    `""` for containers and missing nodes. Jackson 3 `asString()` **throws** (`JsonNodeException: 'ArrayNode'
    method asString() cannot coerce`). The faithful translation is **`asString("")`**. Likewise Jackson 2
    `textValue()` returned `null` for non-text nodes, and Jackson 3 `stringValue()` throws: use
    **`stringValue(null)`**. Found in prov-template's `SchemaCheck` (`@context` as an array); applied to all
    54 converted call sites of prov-jsonld (11 in `ProvJsonReader`) as well. **Apply to every module after OpenRewrite.**
17. **`JsonParser.readValueAsTree()` returns a `TreeNode`** in Jackson 3; `ctxt.readTreeAsValue` needs a
    `JsonNode`, so use `JsonNode node = ctxt.readTree(parser)`. `ObjectMapper.getFactory()` is gone
    (`tokenStreamFactory()`; for a format test use `mapper instanceof YAMLMapper`). `YAMLMapper` has no
    `builderWithJackson2Defaults()`: use `YAMLMapper.builder().configureForJackson2()`.
18. **try-with-resources + `catch (JacksonException)`**: OpenRewrite turns `catch (IOException e)` into
    `catch (JacksonException e)`, but `close()` of a resource still throws `IOException`:
    `catch (JacksonException | IOException e)`.
19. **Shared static mapper mutated in a constructor** (prov-template-compiler's
    `ConfigProcessor.objectMapper` + `DescriptorUtils.setupDeserializer(om)`): build the static mapper
    once with the module (`setupDeserializer` now takes and returns a `MapperBuilder`).
20. OpenRewrite leaves **`new ObjectMapper()`** (Jackson 3 defaults) when the variable's type is
    `ObjectMapper`: replace by `JsonMapper.builderWithJackson2Defaults().build()` too (D2). It also
    produces **`tools.jackson.core.JsonProcessingException`, which does not exist**: use `JacksonException`.
    `JsonNode.fields()` (iterator) → `properties()` (a `Set`): add `.iterator()` where needed.
23. When a module had Jackson 2 **only transitively** and it is already gone from the classpath,
    OpenRewrite cannot resolve the types and misses some changes (e.g. `catch (IOException)` around
    `writeValue`). Compile and fix by hand (benchmarks, roundtrip).
22. **Java sources under `src/main/scala`** (prov-model-scala's `jsonld11`): OpenRewrite's `runNoFork` does
    not see them. Use the `run` goal (it runs scala-maven-plugin's `add-source` first) while the
    module still compiles on Jackson 2, i.e. **before** changing its pom. OpenRewrite turns
    `jp.getCodec().readTree(jp)` into `jp.objectReadContext().readTree(jp)`: use `ctxt.readTree(jp)` (#21).
21. **`p.readValueAs(type)` inside a deserializer** (maintainer's fix, e901e85): it goes through the
    parser's own ObjectReadContext. mongojack 6 builds its BSON parser on a context that is not bound
    to a parser, so reading from MongoDB failed with an NPE (`_readCapabilities` is null; 143 errors in
    prov-storage-mongodb). Use **`ctxt.readValue(p, type)`** (the ongoing deserialization's context)
    everywhere, also in the Scala modules.
24. **Global mapper mutated by callers** (`TypePropagator.om.enable(INDENT_OUTPUT)` in prov-summarisation
    and provman): with Jackson 2 this made *every later use* of the shared mapper indented. Jackson 3:
    `om.writerWithDefaultPrettyPrinter().writeValue(...)` at those call sites only.
25. **jackson-dataformat-xml 3**: `JacksonXmlModule` no longer exists. `new XmlMapper(module)` with
    `module.setDefaultUseWrapper(false)` → `XmlMapper.builderWithJackson2Defaults().defaultUseWrapper(false)`;
    `setAnnotationIntrospector(pair)` → `.annotationIntrospector(pair)` on the builder;
    `new JakartaXmlBindAnnotationIntrospector(TypeFactory.defaultInstance())` → `new JakartaXmlBindAnnotationIntrospector()`.
15. **Whitespace text in XML**: Jackson 3's XML parser reports the whitespace-only content of an
    element without children (an empty pretty-printed `<document>`) as a property `""`
    (`UnrecognizedPropertyException`). No `XmlReadFeature` controls it; prov-jsonld-xml's
    `StatementsHandler` problem handler now accepts a blank `""` property.
12. **Reverting OpenRewrite where the code talks to a Jackson 2 module**: OpenRewrite converts
    everything in a module. Where a class only deals with a module that is still on Jackson 2
    (e.g. prov-service-translation's `TemplateService`/`ActionExpand` with prov-template), restore
    the file (`git checkout -- <file>`) and migrate it with that module later.
9. To test a module against an upstream module changed in the same step, the upstream one must be
   **installed**. prov-jsonld's install fails in the cloud (w3.org check), so use
   `mvn install -DskipTests -Dexec.skip=true -pl modules-core/prov-jsonld` (its only exec is that check).

## 5c. Baseline: pre-existing test failures (before any Jackson change)

These fail in the baseline for environmental reasons, so they are **not** regressions:

| Module | Baseline | Cause |
|---|---|---|
| prov-interop | 1 error | test reads `/Users/luc/git-papers/papers/book-ptm/...` (hard-coded path on the maintainer's machine) |
| prov-template | 2 failures | **timezone-dependent**: bindings10.json expects `2023-09-05T20:31:56.570Z` but parsing gives `21:31:56.570+01:00` (same instant, different representation). Date handling in bindings is exactly where Jackson 3 defaults differ, so watch this test closely |
| prov-template-compiler | 1 error | missing `target/libs/templates/templates_cli/target/example_template_block.json` (nested `templates_cli` build could not resolve its dependencies in the cloud) |
| service-templates | 1 error | needs PostgreSQL (`conn` is null) |
| nlg-wrapper, nlg-spec-transformer, prov-explanations | ScalaTest ABORTED | no SimpleNLG lexicon in the cloud |
| prov-toolbox-js-embedding | surefire fork fails | needs a GraalVM JDK (JVMCI options rejected by OpenJDK 21) |

## 6. Build environment notes (what a fresh machine/container needs)

- prov-model runs `dot --version`. Graphviz 2.43 (Ubuntu 24.04) only knows `dot -V`;
  a shim in `/usr/local/bin/dot` that maps `--version` to `-V` works.
- **Full test run used for the baseline and the comparisons** (cloud):
  `mvn -B install -DskipTests -fn -pl modules-storage/prov-storage-mongodb -am` then
  `mvn -B install -P '!missing-lexicon' -fn -DskipITs -Dmaven.test.failure.ignore=true -Dtest.failure.ignore=true -pl '!modules-storage/prov-storage-mongodb'`.
  `test.failure.ignore` is the property that nlg-wrapper's scalatest configuration reads.
  `-fn` (not `-fae`) is needed because prov-jsonld's `schema.check` fails without www.w3.org,
  and with `-fae` everything downstream would be skipped. Do **not** exclude tests with
  `-Dtest='!pattern'`: it replaces surefire's default includes, so `module-info` and helper
  classes get run as tests and most counts become 0.
- **Integration tests (`*IT`, failsafe) are not run in the cloud** (`-DskipITs`). The services'
  ITs (e.g. service-translator `TranslateIT`, `DateIT`) need the service running with its
  storage (MongoDB), and in the cloud each call times out. They must be run on the
  maintainer's machine, before and after.
- PostgreSQL (service-templates), GraalVM (prov-toolbox-js-embedding) and docker
  (docker-* modules) are not available in the cloud container either.
- prov-jsonld's `schema.check` (`make schema.test`) downloads the published schema from
  www.w3.org, which the cloud network policy blocks. Its 660 tests had already run.
- **MongoDB**: prov-storage-mongodb's tests need a MongoDB server on localhost. Without
  one, each test waits for a 30 s timeout, so the module takes hours. In the cloud
  baseline, the run was resumed after it (`-rf :prov-storage-index-redis`), and its tests
  must be compared on a machine with MongoDB. **Redis**: `apt-get install redis-server` then
  `redis-server --daemonize yes` (done in the cloud container).
- Tools used by the build (exec-maven-plugin checks and Makefiles): `ajv` (**ajv-cli 3.x**:
  `npm i -g ajv-cli@3`; ajv-cli 5 rejects the draft-04 schemas), `jq`, graphviz `dot`, `curl`, `make`.
  Without them, prov-model and prov-template-library fail. Do **not** use `-Dexec.skip=true`
  for a full build: it also skips the template compiler runs (prov-template's
  template-library zip, prov-template-library), and the later modules then fail.
- The cloud container could not build: jsweet candies (`repository.jsweet.org` unreachable),
  provconvert's izpack installer, docker-* modules (no docker), and anything needing the
  SimpleNLG lexicon (see SCALA3_MIGRATION.md; nlg-wrapper built with
  `-P '!missing-lexicon' -Dmaven.antrun.skip=true` against a local lexaccess stub).
  All Java/Scala code modules compiled and installed.

## 7. Progress log

| Date | Step | Result / notes |
|---|---|---|
| 2026-11-03 | A1 | Branch `jackson3-port` created off `scala3-port` (66ce97c). Inventory done (section 2). |
| 2026-11-03 | baseline build | `mvn install -DskipTests` of the whole Scala-3-only reactor: all code modules OK (see section 6 for the packaging modules that cannot be built in the cloud). |
| 2026-11-03 | C7 (nf, summarisation, typemap, provman) | Migrated; tests and outputs identical to baseline; summary bridge removed. |
| 2026-11-03 | C6 done | Storage modules checked: only prov-storage-mongodb's test-scope json-schema-validator was still Jackson 2 (upgraded). No Jackson 2 in their dependency trees. |
| 2026-11-03 | C4 done | Full comparison build: tests identical to baseline except the PROV-XML removals; outputs as after C5. |
| 2026-11-03 | C4 (prov-scala-misc) | prov-scala-misc migrated (tests and outputs identical); benchmarks, roundtrip fixed (catalogue #23). Full comparison build next. |
| 2026-11-03 | C4 (prov-model-scala) | Migrated (catalogue #22); tests and outputs identical to baseline. |
| 2026-11-03 | C5 done | Comparison build: tests identical to baseline (except PROV-XML removals), outputs as after C2. Maintainer: modules-template builds; prov-storage-mongodb 151/151 after e901e85 (`readValueAs` → `ctxt.readValue`, catalogue #21). |
| 2026-11-03 | C5 (dependants) | prov-interop, prov-service-translation, mongodb test, template library tests, prov-toolbox-plugin moved off Jackson 2. Everything compiles. |
| 2026-11-03 | C5 (compiler) | prov-template-compiler migrated (json-schema-validator 3 API in `JsonSchemaTesting`/`CompilerJsonSchema`/`CompilerDocumentation`; generated code and poms on Jackson 3). Tests as baseline (158, 1 env error). |
| 2026-11-03 | C5 (prov-template) | prov-template migrated; found catalogue #16 (asText/textValue semantics) and fixed it in prov-jsonld too (660/660 still). Dependants of prov-template not migrated yet (build red for them). |
| 2026-11-03 | PROV-XML dropped (step 1) | Maintainer: remove the dependency on prov-jsonld-xml and drop PROV-XML support. Done in the code (see C3 row); the services' advertised formats are step 2. |
| 2026-11-03 | C3 retired | Maintainer is retiring prov-jsonld-xml: module commented out of `modules-core/pom.xml`. Comparison build for C3 stopped. Its 5 dependants still reference it (open point). |
| 2026-11-03 | C3 (module) | prov-jsonld-xml migrated (OpenRewrite + builder rewrite + `AnySetterAttributesModule` copy + fixes #13–#15). Tests 147/147 (were 89 errors before fix #14), outputs identical. Next: build the dependants, full comparison build. |
| 2026-11-03 | C2 done | Remaining 4 modules fixed (redis migrated; translation partly; validation; summary bridge); provman fixed. Full comparison build: tests identical to baseline everywhere; outputs identical except noise + 6 provman JSON files no longer truncated. `compare-outputs.py` also ignores `[B@` hashes, DOT blank nodes, in-line attribute order. |
| 2026-11-03 | C2 (in progress) | prov-jsonld migrated and verified (660/660; outputs identical except noise). Found and fixed the silent attribute loss (catalogue #2). prov-validation migrated (232/232). prov-interop (partly), storage api/filesystem/mongodb (mongojack 6) migrated, xplain bridged, test readers fixed. Left: redis, prov-service-translation, -validation, -summary (they pass Jackson 2 mappers to the storage API). Build red for those. |
| 2026-11-03 | decisions | Maintainer confirmed D1–D6 as proposed. |
| 2026-11-03 | B1 verified, C1 | B1 full test run: identical to the baseline. Outputs compared with the new `jackson3-migration/compare-outputs.py`: identical except known noise. C1 (prov-model) needs no change. |
| 2026-11-03 | B2 | Baseline recorded (`jackson3-migration/baseline-tests.tsv`, section 5c). It took several attempts: w3.org blocked (use `-fn`), MongoDB timeouts (exclude the module), service ITs (`-DskipITs`), GraalVM. B1 verification build started with the same settings. |
| 2026-11-03 | B0, B1, B2 (in progress) | Root pom: release 17 (B0); Jackson 3 BOM + annotations 2.22 (B1, not built yet). Baseline test run started (prov-storage-mongodb skipped, no MongoDB; see section 6). Added `jackson3-migration/test-summary.py`. Found that json-schema-validator spreads from prov-template (not prov-jsonld, where it is test scope only). Added D6 (ProvToolbox public API) and the per-module recipe (5a). |
| 2026-11-03 | A2, A3 | Verbose dependency trees of 72 modules. Third-party Jackson users: json-schema-validator, zjsonpatch, swagger-core, mongojack, pac4j (+scribejava), Spring Boot 2.7. Jackson 3 status checked for each (section 3). Verified in the jar: databind 3.2.4 has `JsonMapper.builderWithJackson2Defaults()` and `MapperBuilder.configureForJackson2()` (D2 is feasible). Found D5 (JDK 17). |
