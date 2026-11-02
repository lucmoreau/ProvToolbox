# Scala 2.13 → Scala 3 migration plan

This is a living document. **Whoever works on the migration (human or Claude session)
updates the "Progress log" and the status column after every step and commits it
with the work.** A new session should be able to resume from this file alone.

## Decisions (agreed with the maintainer, 2026-11-02)

| Topic | Decision |
|---|---|
| Branch | `scala3-port`, branched off `development2_0`. All migration work goes there. |
| Scope | **Every** module containing Scala sources (12 modules, listed below). |
| Strategy | Side-by-side copies. Each Scala 2 module `X` gets a sibling directory `X-scala3` with artifactId `X-scala3`. The 2.13 module is left **untouched** until every copy works; then the 2.13 versions are retired (Phase R). |
| Packages | The copies keep the **same Scala/Java packages** (`org.openprovenance.prov.scala...`). So a 2.13 module and its `-scala3` twin must never be on the same classpath. Renaming the copies back at retirement is then trivial. |
| Dependencies | A `-scala3` module depends only on other `-scala3` modules (plus Java modules). Java modules (e.g. prov-validation, the services) keep depending on the 2.13 artifacts until Phase R. |
| Scala version | Latest **LTS = 3.9.0** (released Oct 2026; replaces 3.3 LTS). Property `scala3.version` in the root pom. |
| JDK | Scala ≥ 3.8 needs JDK 17+. `-scala3` modules compile with `release 17` (the rest of the build stays on 15). |
| Source style | **Minimal** migration: keep brace syntax, keep `implicit`s where they still compile, change only what Scala 3 needs (or what removes errors and deprecation-as-error). No idiomatic rewrite (given/using, enums, indentation syntax). |

## Scala modules and dependency layers

Only the Scala-module dependencies are shown (Java modules such as prov-model,
prov-n, prov-jsonld and prov-validation are left out). Port in layer order.

| Layer | Module (2.13 path) | Scala3 copy | Depends on (Scala) | Main LOC / test LOC | Status |
|---|---|---|---|---|---|
| 0 | `modules-core/prov-model-scala` | `modules-core/prov-model-scala-scala3` | – (parboiled2) | 6564 / 446 | **DONE** |
| 0 | `modules-explanations/nlg-wrapper` | `modules-explanations/nlg-wrapper-scala3` | – (jackson-module-scala) | 1039 / 629 | TODO (needs clojars, see notes) |
| 1 | `modules-misc/prov-scala-misc` | `modules-misc/prov-scala-misc-scala3` | prov-model-scala | 4397 / 941 | TODO |
| 1 | `modules-explanations/nlg-spec-transformer` | `modules-explanations/nlg-spec-transformer-scala3` | prov-model-scala, nlg-wrapper | 3146 / 1293 | TODO |
| 2 | `modules-nf/prov-nf` | `modules-nf/prov-nf-scala3` | prov-scala-misc | 4899 / 1407 | TODO |
| 2 | `modules-summarisation/prov-summarisation` | `modules-summarisation/prov-summarisation-scala3` | prov-scala-misc (jackson-module-scala, parallel collections) | 2562 / 1753 | TODO |
| 3 | `modules-nf/prov-signature` | `modules-nf/prov-signature-scala3` | prov-nf | 239 / 213 | TODO |
| 3 | `modules-summarisation/prov-typemap` | `modules-summarisation/prov-typemap-scala3` | prov-scala-misc, prov-summarisation | 466 / 47 | TODO |
| 3 | `modules-explanations/prov-query` | `modules-explanations/prov-query-scala3` | prov-scala-misc, nlg-spec-transformer, prov-nf, prov-summarisation (parser-combinators, parboiled2) | 1377 / 905 | TODO |
| 4 | `modules-explanations/prov-explanations` | `modules-explanations/prov-explanations-scala3` | nlg-wrapper, prov-query, prov-nf, prov-summarisation | 1515 / 1160 | TODO |
| 5 | `modules-explanations/prov-summary-explanations` | `modules-explanations/prov-summary-explanations-scala3` | prov-query, prov-explanations, prov-summarisation | 85 / 0 | TODO |
| 6 | `modules-executable-provman` | `modules-executable-provman-scala3` | everything above (scopt) | 1674 / 32 | TODO |

Note: `prov-validation` (Java) depends on `prov-scala-misc` only in **test** scope,
so `prov-nf-scala3` / `prov-explanations-scala3` can depend on prov-validation
without pulling Scala 2.13 classes in. **Always check** with
`mvn dependency:tree -pl <module>` that no `_2.13` artifact and no 2.13
`prov-*` Scala artifact ends up on a `-scala3` module's classpath.

### Third-party Scala libraries (Scala 3 artifacts checked on Maven Central)

| Library | 2.13 artifact | Scala 3 artifact |
|---|---|---|
| scala-library | `scala-library 2.13.18` | `scala3-library_3 ${scala3.version}` |
| scalatest | `scalatest_2.13 3.2.19` | `scalatest_3 3.2.19` ✅ |
| parboiled2 | `parboiled_2.13 2.5.1` | `parboiled_3 2.5.1` (macro lib; Scala 3 supported since 2.5.0; check rule syntax differences) |
| parser-combinators | `scala-parser-combinators_2.13 2.4.0` | `scala-parser-combinators_3 2.4.0` ✅ |
| parallel collections | `scala-parallel-collections_2.13 1.0.4` | `scala-parallel-collections_3 1.0.4` ✅ |
| scala-xml | `scala-xml_2.13` | `scala-xml_3 2.x` (latest 2.5.0) |
| jackson-module-scala | `jackson-module-scala_2.13 ${jackson.version}` | `jackson-module-scala_3 ${jackson.version}` ✅ (2.21.2 exists). Scala 3 has no `ScalaObjectMapper`/`ClassTagExtensions` with Manifest — check usages. |
| scopt | `scopt_2.13` | `scopt_3 4.1.0` |

## Build environment notes

- Always pass `-Dexec.skip=true`: `prov-model` runs `exec-maven-plugin` checks for
  `ajv`, `dot`, `jq`... which are not installed everywhere (they are only presence checks).
- First build of the Java upstream modules (once per machine):
  `mvn -B install -DskipTests -Dmaven.javadoc.skip=true -Dexec.skip=true -pl modules-core/prov-model-scala,modules-misc/prov-scala-misc -am`
- **Cloud session limitation (2026-11-02)**: the Claude cloud environment could not
  reach `repo.clojars.org`, which serves `gov.nih.nlm.nls.lexaccess:lexaccess-dist`
  (needed by `nlg-wrapper`). So `nlg-wrapper` and everything downstream of it
  (`nlg-spec-transformer`, `prov-query`, `prov-explanations`,
  `prov-summary-explanations`, provman) could not be built there, not even in 2.13.
  These have to be done on a machine with clojars access (or with
  `repo.clojars.org` allowed in the environment's network settings).

## Recipe for porting one module `X` (repeat per module)

**Use the script**: `python3 scala3-migration/copy-module.py <dir>/X` does steps 1–3
(copy without `target/` and `*.iml`, rewrite the pom, register in the parent pom).
Then review `diff <dir>/X/pom.xml <dir>/X-scala3/pom.xml` before building.
If a 2.13 module uses a Scala library not covered by `_${scala.binary.version}`
(e.g. a hard-coded `_2.13`), fix that by hand.

1. **Copy**: `cp -r <dir>/X <dir>/X-scala3` and then `rm -rf <dir>/X-scala3/target`.
   Delete IDE files (`*.iml`) in the copy.
2. **pom.xml of the copy**:
   - `<artifactId>X-scala3</artifactId>`, `<name>` gets the suffix ` (Scala 3)`.
   - Add `<properties>`: `<scala.binary.version>3</scala.binary.version>`
     (this turns every `_${scala.binary.version}` dependency into `_3`).
   - Replace the `scala-library` dependency with
     `org.scala-lang:scala3-library_3:${scala3.version}` (add it if it was missing).
   - Replace each dependency on a 2.13 Scala module `Y` with `Y-scala3`.
   - Add `scalaVersion` `${scala3.version}` to the `scala-maven-plugin`
     `<configuration>` and set the args to the Scala 3 set:
     `-deprecation -feature -no-indent -release ${scala3.release}`.
     `-no-indent` turns off significant indentation. We keep brace syntax, and the
     sources mix tabs and spaces, which the indentation checks reject.
     If a module throws many errors, adding `-source:3.0-migration` temporarily
     can help to see them as warnings.
   - Override `maven-compiler-plugin` with `<release>17</release>` (needed when the
     module has Java sources that reference Scala 3 classes).
   - The `attach-javadocs` (`doc-jar`) execution is disabled (`<phase>none</phase>`):
     scala-maven-plugin 4.9.7 passes Scala 2 scaladoc options (`-doc-format:html`,
     `-doc-title`) and the Scala 3 scaladoc fails. **TODO (Phase R)**: try
     scala-maven-plugin 4.10.x, or configure the Scala 3 scaladoc options, to get doc jars back.
3. **Register** the copy in the parent aggregator pom `<modules>` right after `X`.
4. **Compile**: `mvn -B install -DskipTests -Dexec.skip=true -pl <dir>/X-scala3` (upstream
   `-scala3` modules must already be installed in `~/.m2`). Fix the errors (see the
   catalogue below), then build with tests: `mvn -B install -Dexec.skip=true -pl <dir>/X-scala3`.
5. **Compare** test results with the 2.13 module (`mvn -B test -pl <dir>/X`):
   the same tests must pass. Record the numbers in the progress log.
6. **Check the classpath**: `mvn -B dependency:tree -pl <dir>/X-scala3 | grep -E "_2\.1[23]|scala-library:2"`
   must print nothing.
7. Update this file (status + log), commit, push.

## Catalogue of Scala 2 → 3 fixes found so far

(Add to this list as you find them, so later modules go faster.)

1. **parboiled2 actions (`~>`) must be lambdas.** The Scala 3 parboiled2 macro crashes
   (`scala.MatchError: Select(This(Ident(...)),makeX)` "Exception occurred while
   executing macro expansion") on `~> makeX` when `makeX` is a function-typed `val`
   (or any non-lambda). Rewrite it to a lambda with the right arity:
   `~> ((a1, a2) => makeX(a1, a2))`, `~> (a1 => makeX(a1))`, `~> (() => makeX())`.
   `~> (makeX _)` does **not** help for vals. In prov-model-scala this was done for
   all 103 actions in `ProvnParser.scala` with a small script. Apply the same to
   `prov-query/.../ProvQLParser.scala` and to any other `rule { ... ~> f }`.
2. **Same-package definitions shadow explicit imports.** `ProvnParser.scala` (package
   `...scala.immutable`) imported `org.openprovenance.prov.model.StatementOrBundle.Kind`,
   but Scala 3 resolved `Kind` to the same-package `object Kind` (an `Enumeration`), giving
   "value PROV_ATTRIBUTION is not a member of object ...immutable.Kind".
   Fix: rename on import (`import ...StatementOrBundle.{Kind => JKind}`) and use `JKind.X`.
3. **`@BeanProperty override val` on a constructor parameter**, when the parent class
   already has a `@BeanProperty val` of the same name, generates a `getX` that "needs
   `override` modifier". Fix: drop `@BeanProperty` from the subclass (the inherited
   getter returns the overridden val anyway). Seen in `OrderedDocument`.
4. **Tabs/spaces indentation errors** ("Incompatible combinations of tabs and spaces in
   indentation prefixes"): fixed globally by the `-no-indent` compiler option, no code change.
5. Remaining **warnings** (not errors, left as is under the minimal-change policy):
   `_` wildcard in types (use `?`), `with` as a type operator (use `&`), identifiers with `$`.
   They can be rewritten automatically later with `-rewrite -source 3.4-migration`.

## Phase R — retirement (only after all 12 copies are DONE)

1. Switch the Java consumers to the `-scala3` artifacts: prov-validation (test
   scope), roundtrip, benchmarks, service-signature, prov-service-summary,
   service-summary, prov-service-xplain, service-xplain, prov-service-signature,
   modules-services* (grep all poms for `prov-model-scala`, `prov-scala-misc`,
   `prov-nf`, `prov-summarisation`, `prov-explanations`, `prov-query`, `nlg-`,
   `prov-signature`, `prov-typemap`, `prov-summary-explanations`).
   Their compiler `release` probably has to go to 17.
2. Run the full build `mvn -B install` and the full test suite.
3. Delete the 2.13 modules, rename `X-scala3` → `X` (directory and artifactId), drop the
   suffix from all dependency references, and remove the 2.13 properties from the root pom
   (`scala.library.version`, keeping `scala.binary.version=3` at the root).
4. Update README/RELEASES/DEPENDENCIES.md.

## Progress log

| Date | Step | Result / notes |
|---|---|---|
| 2026-11-02 | Plan written, branch `scala3-port` created | Root pom: properties `scala3.version=3.9.0`, `scala3.release=17`. Helper script `scala3-migration/copy-module.py`. |
| 2026-11-02 | prov-model-scala-scala3 | Builds with tests. Fixes #1–#4 of the catalogue. Tests same as 2.13: JUnit 149/149, ScalaTest 16/16. `dependency:tree` clean (only `_3` + scala-library 3.9.0). |
