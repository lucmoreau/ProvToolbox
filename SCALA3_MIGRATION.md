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
| 0 | `modules-explanations/nlg-wrapper` | `modules-explanations/nlg-wrapper-scala3` | – (jackson-module-scala) | 1039 / 629 | **COMPILES** — full test run pending (needs lexicon) |
| 1 | `modules-misc/prov-scala-misc` | `modules-misc/prov-scala-misc-scala3` | prov-model-scala | 4397 / 941 | **DONE** |
| 1 | `modules-explanations/nlg-spec-transformer` | `modules-explanations/nlg-spec-transformer-scala3` | prov-model-scala, nlg-wrapper | 3146 / 1293 | **COMPILES** — full test run pending (needs lexicon) |
| 2 | `modules-nf/prov-nf` | `modules-nf/prov-nf-scala3` | prov-scala-misc | 4899 / 1407 | **DONE** |
| 2 | `modules-summarisation/prov-summarisation` | `modules-summarisation/prov-summarisation-scala3` | prov-scala-misc (jackson-module-scala, parallel collections) | 2562 / 1753 | **DONE** |
| 3 | `modules-nf/prov-signature` | `modules-nf/prov-signature-scala3` | prov-nf | 239 / 213 | **DONE** |
| 3 | `modules-summarisation/prov-typemap` | `modules-summarisation/prov-typemap-scala3` | prov-scala-misc, prov-summarisation | 466 / 47 | **DONE** |
| 3 | `modules-explanations/prov-query` | `modules-explanations/prov-query-scala3` | prov-scala-misc, nlg-spec-transformer, prov-nf, prov-summarisation (parser-combinators, parboiled2) | 1377 / 905 | **DONE** (built with the lexaccess stub; re-run once with the real lexicon) |
| 4 | `modules-explanations/prov-explanations` | `modules-explanations/prov-explanations-scala3` | nlg-wrapper, prov-query, prov-nf, prov-summarisation | 1515 / 1160 | **COMPILES** — full test run pending (needs lexicon) |
| 5 | `modules-explanations/prov-summary-explanations` | `modules-explanations/prov-summary-explanations-scala3` | prov-query, prov-explanations, prov-summarisation | 85 / 0 | **DONE** (no tests) |
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
- **Graphviz `dot` must be installed** (`apt-get install graphviz`), otherwise 3 tests
  of prov-summarisation fail (in 2.13 as well) with `Cannot run program "dot"`.
- Extra check worth doing for modules that write files in tests: run the 2.13 and the
  Scala 3 tests, then `diff -rq X/target X-scala3/target -x classes -x test-classes -x "*.jar" -x "maven-*" -x surefire-reports -x "generated*" -x analysis`.
  It should report only `site`.
- First build of the Java upstream modules (once per machine):
  `mvn -B install -DskipTests -Dmaven.javadoc.skip=true -Dexec.skip=true -pl modules-core/prov-model-scala,modules-misc/prov-scala-misc -am`
- **Cloud session limitation (2026-11-02)**: the Claude cloud environment could not
  reach `repo.clojars.org`, which serves `gov.nih.nlm.nls.lexaccess:lexaccess-dist`
  (needed by `nlg-wrapper`). So `nlg-wrapper` and everything downstream of it
  (`nlg-spec-transformer`, `prov-query`, `prov-explanations`,
  `prov-summary-explanations`, provman) could not be built there, not even in 2.13.
  The lexicon data (`lexAccess2013.tgz` from `data.lhncbc.nlm.nih.gov`) was also blocked.
  **Workaround used in the cloud session (compile only)**: an *empty stub*
  `lexaccess-dist-2013.jar` installed into the local `~/.m2` (never committed), with
  `mvn ... -Dexec.skip=true -Dmaven.antrun.skip=true -P '!missing-lexicon'`. That
  compiles main + test code; tests that touch SimpleNLG's lexicon abort with
  `NoClassDefFoundError: gov/nih/nlm/nls/lexAccess/Api/LexAccessApi`.
  **On a machine with real access, delete any such stub first**
  (`rm -rf ~/.m2/repository/gov/nih/nlm/nls/lexaccess`), then build normally and run
  the full test suites of the "COMPILES" modules against their 2.13 twins.

## Recipe for porting one module `X` (repeat per module)

**Use the script**: `python3 scala3-migration/copy-module.py <dir>/X` does steps 1–3
(copy without `target/` and `*.iml`, rewrite the pom, register in the parent pom),
and also applies fix #8 (lambda parentheses) to all `.scala` files of the copy.
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
   all 103 actions in `ProvnParser.scala`, and for the 25 in prov-query's `ProvQLParser.scala`
   with **`python3 scala3-migration/parboiled-actions.py Parser.scala <files declaring the actions>`**
   (it reads each action's arity from its `val/def f: (A, B) => R` declaration).
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
5. **Inherited members beat imports, even when inaccessible.** In `mutable/Model.scala`,
   `class ProvFactory extends org.openprovenance.prov.model.ProvFactory` used
   `new util.LinkedList` (with `import java.util`), but the Java parent has a
   package-private field `util`. Scala 2 skipped it, Scala 3 picks it and reports
   "variable util in class ProvFactory cannot be accessed". Fix: fully qualify
   (`java.util.LinkedList`); done with a regex `(?<![.\w])util\.` → `java.util.`.
6. **`@BeanProperty` getters/setters are not visible from Scala code** in Scala 3
   (they are generated after type checking). Calls like `a.getLocation()`,
   `a.setLocation(x)`, `b.setStatement(x)` fail with "value setLocation is not a member",
   *unless* a Java interface the class implements declares the method. Fix: use the
   field directly (`a.location`, `a.location = x`, `b.statement = x`).
   This also holds **across modules**: bean accessors are in the class file but not in
   TASTy, so a downstream Scala 3 module (or test) cannot call `x.getKind()`/`b.getNamespace`
   on a Scala-defined class either → use `x.kind`, `b.namespace`. Java callers are unaffected.
7. **A `lazy val` cannot implement an abstract `val`** ("lazy value X may not override a
   non-lazy value"). Fix: declare the abstract member as `def` in the trait
   (`trait Indexing` in `immutable/Indexer.scala`).
8. **Lambda with a typed parameter needs parentheses**: `map{v:T => ...}` →
   `map{(v:T) => ...}`. Regex used on a whole file:
   `\{(\s*)([a-z]\w*)\s*:\s*([A-Z][\w\[\], .]*?)\s*=>` → `{\1(\2:\3) =>`.
9. **`using` is a (soft) keyword**: Java annotation arguments named `using` need backticks:
   `@JsonDeserialize(using = classOf[X])` → ``@JsonDeserialize(`using` = classOf[X])``
   ("expression expected but = found" + "Missing arguments for constructor JsonDeserialize").
10. **jackson-module-scala: `ScalaObjectMapper` does not exist for Scala 3.** Replace the
    mixin with `ClassTagExtensions` (`new ObjectMapper with ClassTagExtensions`).
    Behaviour caveat: `ClassTagExtensions.readValue[T]` uses a `ClassTag`, so the type
    arguments of a generic `T` (e.g. `Map[String, List[X]]`) are erased, unlike the
    `Manifest`-based `ScalaObjectMapper`. Where the code only calls the Java API
    `readValue(in, classOf[X])` (as in nlg-wrapper and nlg-spec-transformer, checked) this
    changes nothing. Check each `readValue[...]`/`convertValue[...]` call with a generic type
    argument in later modules.
11. **No `ClassTag` for an intersection with an abstract type.** `x.transform[Phrase with F](e)`
    with `def transform[F <: Phrase](e)(implicit c: ClassTag[F])` → "No ClassTag available
    for Phrase & F". Scala 2 used the erasure (`Phrase`). Same behaviour, made explicit:
    `x.transform[Phrase with F](e)(ClassTag[Phrase with F](classOf[Phrase]))` (SpecLoader.scala).
12. **`case class C private (...)` also makes the synthetic `apply` private** in Scala 3.
    `C(...)` called from elsewhere fails with "object C ... does not take parameters".
    Fix: widen to `private[pkg]` (here `case class Result private[primitive]`, Primitive.scala).
13. **View bounds `T <% X` are gone.** Replace with an implicit conversion parameter and
    apply it explicitly: `def convert[T](t0: T)(implicit ev: T => X) = { val t = ev(t0); ... }`
    (QFactory.scala). The existing `implicit def` conversions (`OrType.aToOr3`...) still satisfy `ev`.
14. **No type ascription after a tuple pattern**: `val (a, b): (A, B) = e` → `val (a, b) = e`
    (the type is inferred from `e`).
15. **Lambdas with a typed parameter outside braces** need parentheses too, e.g.
    `case None => _: Record =>` → `case None => (_: Record) =>`; `x: Record => ...` →
    `(x: Record) => ...`. The automatic regex (#8) only covers the `{x: T =>` form.
16. Remaining **warnings** (not errors, left as is under the minimal-change policy):
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
| 2026-11-02 | prov-summary-explanations-scala3 | Compiled with no source change; no tests in 2.13 either. `dependency:tree` clean. |
| 2026-11-02 | prov-explanations-scala3 | Compiled with **no source change**. Both suites (`NlgSpec`, `ExplanationSpec`, 27 tests) abort in **both** versions on the missing lexaccess class. **TODO with the lexicon**: full test run of both, compare. `dependency:tree` clean. |
| 2026-11-02 | prov-query-scala3 | Fixes #1 (25 actions, new script `parboiled-actions.py`), #6 (test), #13, #14, #15. Tests same as 2.13: ScalaTest 11/11 (these suites do not need the lexicon). `target/` outputs identical. `dependency:tree` clean. |
| 2026-11-02 | nlg-spec-transformer-scala3 | Built with the lexaccess stub. Fixes #8 (auto), #9, #10, #11, #12. Main + test compile. `xmlSpec` (no lexicon): 2/2 in both versions. `TransformSpecsSpec`, `SentencesFromJsonSpec`, `LoadPhraseSpec` abort in **both** versions on the missing lexaccess class. **TODO with the lexicon**: full test run of both (2.13 expects 24 tests), compare. |
| 2026-11-02 | nlg-wrapper-scala3 | Built with the lexaccess stub (see notes). Fixes #9, #10. Main + test compile. `-Dsuites=nlg.wrapper.xmlSpec` (no lexicon needed): 3/3 in both 2.13 and Scala 3. Other suites abort for lack of the lexicon in **both** versions. **TODO on a machine with the lexicon**: full `mvn test` of both, compare (2.13 expects 30 tests). |
| 2026-11-02 | prov-signature-scala3 | One fix #8. Tests same as 2.13: JUnit 1/1, ScalaTest 0 (the ScalaTest runner finds no suites in 2.13 either). `dependency:tree` clean. Fix #8 is now automatic in `copy-module.py`. |
| 2026-11-02 | prov-typemap-scala3 | Compiled unchanged. The ScalaTest run prints no summary line in 2.13 either ("Expected test count is: 2"). The test output (type maps, JSON) is identical to 2.13, apart from a trailing space in a suite name. `dependency:tree` clean. |
| 2026-11-02 | prov-summarisation-scala3 | Main compiled unchanged. One test needed fix #8. Tests same as 2.13: ScalaTest 32/32 (once graphviz was installed). All JSON/DOT/PROV-N/SVG outputs in `target/` are byte-identical to the 2.13 run. `dependency:tree` clean (jackson-module-scala_3, parallel-collections_3). |
| 2026-11-02 | prov-nf-scala3 | Builds with tests. Fixes #6 (main + `UnificationSpec` `getKind` → `kind`) and #8. Tests same as 2.13: JUnit 1/1, ScalaTest 94/94. `dependency:tree` clean (prov-validation brings no 2.13 classes). |
| 2026-11-02 | prov-scala-misc-scala3 | Builds with tests. Fixes #5–#8. Tests same as 2.13: JUnit 18/18, ScalaTest 6/6. `dependency:tree` clean. |
| 2026-11-02 | prov-model-scala-scala3 | Builds with tests. Fixes #1–#4 of the catalogue. Tests same as 2.13: JUnit 149/149, ScalaTest 16/16. `dependency:tree` clean (only `_3` + scala-library 3.9.0). |
