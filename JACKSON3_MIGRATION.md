# Jackson 2 → Jackson 3 migration plan

Living document: **whoever works on the migration updates the status columns and the
progress log after every step, and commits it with the work.** A new session should be able
to resume from this file alone.

- Branch: `jackson3-port`, off `scala3-port` (which is now Scala 3 only, see SCALA3_MIGRATION.md).
- Started: 2026-11-03.

## ▶ Current status and next steps

*(updated as work progresses, see the progress log at the end)*

Phase A (analysis) in progress.

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

Libraries that use Jackson themselves have to be on a Jackson 3 version, or Jackson 2 stays on
the classpath next to Jackson 3. That's allowed (different packages, so no clash), but the
two don't share configuration, modules, or annotations other than `com.fasterxml.jackson.annotation`.

| Library (current version) | Used in | Uses Jackson? | Jackson 3 support | Action |
|---|---|---|---|---|
| networknt `json-schema-validator` 1.5.9 | prov-jsonld, prov-storage-mongodb, prov-template, prov-template-compiler | yes (Jackson 2 `JsonNode` API) | ✅ **3.x line** (3.0.8): Java 17 + Jackson 3. 2.x line = Jackson 2. Major API change from 1.x | upgrade 1.5.9 → 3.x (API migration) |
| flipkart `zjsonpatch` 0.4.14 | prov-template | yes | ✅ since 0.6.x: separate Jackson 3 API set; Jackson becomes an optional dependency | upgrade, use the Jackson 3 API, declare databind |
| swagger-core `swagger-jaxrs2-jakarta` 2.2.41 | 13 service modules | yes | ✅ **swagger-core 3.0.0** (released 2026-10-28): Jackson 3 only, Java 17, artifact renamed **`swagger-rest`**. 2.2.x stays on Jackson 2 | upgrade to 3.0.x (very new: check maturity) |
| `mongojack` 5.0.3 | prov-storage-mongodb | yes (core of the library) | ✅ **6.x**: Jackson 3, Java 17, Mongo driver 5.x only | upgrade to 6.x (+ Mongo driver 5) |
| pac4j 5.7.9 | prov-service-security-pac | yes (pac4j-core) | ❓ no Jackson 3 release found: pac4j 6.3.4 is on Jackson 2.21. pac4j 6 needs JDK 17 + jakartaee-pac4j 8 | **blocker candidate**: keep Jackson 2 for this module only, or wait |
| Spring Boot 2.7.6 | service-templates, archetype | yes | ✅ Spring Boot **4.x** (Jackson 3 by default, Jackson 2 deprecated). 2.7 → 4 is a large jump (Spring 7, Jakarta) | separate sub-project; keep Jackson 2 meanwhile |
| RESTEasy 6.2.14 | services | not in core; the JSON provider is Jackson's own `jackson-jakarta-rs-json-provider` | ✅ Jackson publishes `tools.jackson.jakarta.rs` 3.x (RESTEasy's own Jackson 3 provider is still beta, `dev.resteasy.providers`) | switch provider artifact |
| titanium-json-ld 1.4.1 | prov-jsonld, mongodb | no (Jakarta JSON-P) | n/a | none |
| Apache Jena 5.x | prov-template-compiler, prov-sparql-query | to check (dependency tree) | | |
| jedis, xmlsec, SimpleNLG, graalvm, netty, quartz, log4j, commons-*, javapoet, antlr, jsweet, izpack, maven libs | various | no (log4j: optional JSON layout only) | n/a | none |

*(To be confirmed and completed with `mvn dependency:tree` over the whole reactor, section 3.1.)*

### 3.1 Transitive Jackson 2 on the classpath (from dependency trees)

*(filled in step A2)*

## 4. Decisions to take (proposals; the maintainer to confirm)

- **D1 – One step or incremental?** Proposal: incremental, module by module in dependency
  order, as for Scala 3. Jackson 2 and 3 can coexist on one classpath (different packages,
  shared annotations), so the build stays green between steps. Unlike Scala 3, no
  side-by-side copies are needed.
- **D2 – Output compatibility.** Jackson 3's new defaults (alphabetical property order, ISO
  dates...) can change serialized PROV. Proposal: build every mapper with
  **`JsonMapper.builderWithJackson2Defaults()`** (or the equivalent `XmlMapper`
  builder) to keep byte-identical output, and verify with round-trip tests and output diffs.
  Moving to the Jackson 3 defaults can be a separate, deliberate change later.
- **D3 – Libraries without Jackson 3 support** (pac4j; Spring Boot 2.7 until upgraded):
  keep Jackson 2 *only* in those modules (coexistence), and record them as "remaining
  Jackson 2" in this file.
- **D4 – Tooling.** Use OpenRewrite `UpgradeJackson_2_3` for Java modules where it helps,
  then fix by hand. Scala (6 modules) by hand / sed.

## 5. Plan (phases and steps)

| Step | What | Status |
|---|---|---|
| A1 | Inventory of direct Jackson use (poms, sources, module-info, generators) | DONE |
| A2 | Dependency trees of the whole reactor: which third-party libs pull Jackson 2 | TODO |
| A3 | Check each library's Jackson 3 release (versions, API changes) | partly done (section 3) |
| B1 | Root pom: add `tools.jackson:jackson-bom` (dependencyManagement) and a `jackson3.version` property; keep the Jackson 2 properties while modules migrate | TODO |
| B2 | Baseline: record current test results and sample outputs (PROV-JSON, JSON-LD, XML, template bindings, provman outputs) for later comparison | TODO |
| C1 | prov-model (annotations only: check, module-info) | TODO |
| C2 | prov-jsonld (+ json-schema-validator 3.x) | TODO |
| C3 | prov-jsonld-xml | TODO |
| C4 | prov-model-scala (Scala), prov-n, prov-scala-misc | TODO |
| C5 | prov-template (+ YAML, zjsonpatch), prov-template-compiler (+ generated code, generated poms), template library, archetype | TODO |
| C6 | prov-interop, storage (api, filesystem, redis, mongodb + mongojack 6) | TODO |
| C7 | validation, nf, signature, summarisation, typemap, explanations (nlg-*), query, provman | TODO |
| C8 | Services (prov-service-*, service-*): JAX-RS provider 3.x, swagger-core 3 | TODO |
| C9 | Remaining Jackson 2 islands (pac4j, Spring Boot) decided per D3 | TODO |
| D | Clean-up: remove Jackson 2 properties/dependencies, check `mvn dependency:tree` has no `com.fasterxml.jackson.core:jackson-databind`, regenerate DEPENDENCIES.md / license trees | TODO |

## 6. Progress log

| Date | Step | Result / notes |
|---|---|---|
| 2026-11-03 | A1 | Branch `jackson3-port` created off `scala3-port` (66ce97c). Inventory done (section 2). |
