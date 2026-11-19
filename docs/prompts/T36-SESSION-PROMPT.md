Work on ProvToolbox ticket T36: `@inJson` values and template subtypes in the template compiler
(`modules-template/prov-template-compiler`). This session is for the ProvToolbox work only.

## Where

- Repository: `/Users/luc/IdeaProjects/ProvToolbox`. Its main checkout is on `development2_0` and holds uncommitted
  changes that are not yours: `module-info.java` and `StatementCompilerAction2.java` in prov-template-compiler,
  `BatchExecutor.java` in prov-template, the archetype's `Makefile`, and the untracked `TemplateTimeCompilationTest.java`.
  Do not edit, stage, stash or revert them, and do not switch that checkout's branch.
- Work in your own git worktree, on a new branch `t36-injson-subtypes` cut from `development2_0`. If the app has
  already put you in a worktree on a branch of its own, keep that branch and tell Luc its name.
- The ticket (`TODO.md`, section T36) and the design (`docs/T36-INJSON-SUBTYPES-DESIGN.md`) were written in the main
  checkout on 2026-11-16 and may not be committed. If your branch does not have them, copy those two files from the
  main checkout into your worktree (T36 is the only uncommitted change in `TODO.md`) and make them your first commit.
  Do not commit this prompt file.

## Read first

- `TODO.md`, T36, and `docs/T36-INJSON-SUBTYPES-DESIGN.md`, whole.
- `DONE.md`, T31 (`@transient` and `@pseudonym`): the last annotations added to a variable. It shows how a template's
  variables are loaded once and cached, how a runtime call (`@escape` and `escapeCsv`) reaches each target language,
  and how cases are tested (`T31CasesTest`).
- `TODO.md`, T33 and T34: open defects of the Python emitter and of the generated jsweet build.
- The code the design names in its §3.

## What to do, in order

1. **Check the design against the code.** It was written from a reading of the descriptor and configuration classes,
   the bean generator, and PAST's factory and types. Its §10 lists what was not verified. Correct the design where
   the code says otherwise, and say what you changed and why.
2. **Put the rulings R1–R10 to Luc**, each with the design's proposal and your own view now that you have read the
   code. He rules. Do not assume an answer, and record each ruling in T36's section in his words, dated.
3. **Build stage 1, `@inJson`**, once R1 and R2 are answered: the descriptor member, the values kept apart from the
   ordinary variables, the compile-time refusals, the listing in the documentation and the JSON schema, and the test
   of the design's §4.3.
4. **Build the later stages** only once the rulings each depends on are given: the `subtype` kind in the project
   configuration; the typed bean and its two conversions; Python and JavaScript; the descriptions (documentation,
   JSON schema, Turtle); the specification.

## Constraints

- **PAST, not text.** Luc, 2026-11-16: "The compiler extension would have to generate PAST expressions so that
  generated beans can be compiled to multiple target languages." The typed bean and both conversions are PAST classes,
  methods, statements and expressions, written out by the existing emitters. No generated code is assembled as text
  of one language. If PAST lacks a node or an emitter lacks a rule, add it to PAST for all emitters and say so.
- **JSON text needs a library in each language.** Follow `@escape`: one small runtime class per language, reached by
  a PAST `METHOD_CALL`. The same values must give the same text from every language: keep one table of values and
  expected text, and have each language's port run it.
- **Nothing changes for a catalogue that uses neither `@inJson` nor `subtype`.** Show it by comparing the generated
  trees of an existing catalogue before and after.
- **A subtype adds nothing on the server side**: no table, no insert function, no builder.
- **ProvToolbox depends on no application.** nextgen and provmind-procure are the first users. Their rosters of act
  types and their vocabularies are theirs. Do not change those repositories.
- **T33 and T34 are not part of T36.** Do not fix them here unless Luc says so. Say in the tests what they block.

## How to work

- Build with `bash -c 'source ~/.bashrc; mvn …'`: the full build needs the environment `~/.bashrc` sets.
- Commit to your branch as each stage completes with its tests green. No `Co-Authored-By` trailer for Claude or
  Anthropic. Never push, never merge, never commit to `development2_0`: merging is Luc's word.
- Keep T36's section of `TODO.md` current, as T31's was: the rulings as given, what is built, what was found.
- A defect found outside T36 is a new ticket (the next number is T37; bump the line at the top of `TODO.md`), not a
  silent fix.
- Write to Luc in plain language, and say what you did not verify.

## Done, for this session

- The design checked against the code and corrected.
- The rulings put to Luc, and those he gives recorded.
- Stage 1 on the branch with its test green, or the reason it is not.
- A short report: what is on the branch, and what waits for which ruling.
