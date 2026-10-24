# ProvToolbox TODO — Outstanding Work

> **🔢 LAST ALLOCATED TASK NUMBER: T29** — the highest task id ever assigned (whether still open here or retired to
> `DONE.md`). **When allocating a new task, take the NEXT number (T30) and bump this line.** Do NOT reuse a number
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
| T29 | ttf execution (`execute-ttf`) is slow: its time is the `dot` renderings — the merge rewrote every format once per input (fixed), the renderings now run concurrently; one `dot` run per layout and one `ajv` run per batch remain. | 🟡 MEDIUM | Template / Build | [T29 section](#-t29-ttf-execution-is-slow) |
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

### 🟡 T29: ttf execution is slow

**Priority**: 🟡 MEDIUM.  **Category**: Template / Build.
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

**Done 2026-10-22.** `MergeTask` writes each format once (the merging records per input kept as before); `BatchExecutor`
holds one `TemplateIndex` per directory for its life (`index`, `indexPath`), the tasks `putEntry` and `persist()` once per
task (`TemplateIndex` gained the deferred methods, its methods synchronized); `BatchExecutor.serializeAll` writes the text
formats in turn, then the renderings (png, svg, pdf and qualified variants) concurrently through `parallelStream` (off with
`-Dorg.openprovenance.prov.template.ttf.parallel=false`); output streams closed. After: 11.9 s in the executor (17.9 s wall),
merges 3.0 s; the 671 generated files identical to before but for the vargen UUIDs. `MergeTaskTest`.

**Remaining (proposed).**
- One `dot` run per layout: png and svg (and qualified.png/qualified.svg) of a document share a layout; `dot -Tpng -o a.png
  -Tsvg -o b.svg f.dot` lays out once — `ProvToDot` would gain a multi-output `convert`, and the tasks a way to group the
  formats sharing a `ProvToDot` configuration (prov-template does not depend on prov-dot: a small interface in prov-model,
  or a `layoutKey` on the serialiser). ~265 pairs × ~20 ms saved, more on macOS (one process start instead of two).
- `validate-ttf` (`TtbSchemaValidator`) starts one `ajv` (node) process per ttf, 26 per build in the library's first
  execution: validate every ttf in one `ajv` run (`-d` repeated), or in the JVM with networknt, which `SchemaCheck` already uses.
- `ProvToDot.convert(Document, OutputStream, type, title)` neither drains nor waits on `dot`'s stderr: a chatty `dot` could
  block on a full pipe (more so now that several run at once); redirect stderr or read it on a thread, and `waitFor`.
- `MergeTask` re-reads its inputs from disk although the tasks that generated them left them in the executor's document cache;
  reading them is cheap (~1 s in all) and `IndexedDocument.merge` may mutate the statements it is given, so leave as is unless profiled.
- The library's `pom.xml` also runs 47 `provconvert` executions and 24 `exec` ones at `test`; they are outside the ttf path
  and were not measured.

**DoD.** `mvn test` of provenance-templates-library spends its ttf time in `dot` runs that are each necessary, one per
layout; the harness (a `BatchExecutor` subclass timing tasks and serialisers) shows no task-side overhead above the renderings.

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
