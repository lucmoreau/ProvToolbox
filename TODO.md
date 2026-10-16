# ProvToolbox TODO — Outstanding Work

> **🔢 LAST ALLOCATED TASK NUMBER: T28** — the highest task id ever assigned (whether still open here or retired to
> `DONE.md`). **When allocating a new task, take the NEXT number (T29) and bump this line.** Do NOT reuse a number
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
