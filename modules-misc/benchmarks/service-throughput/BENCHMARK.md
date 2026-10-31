# Throughput benchmark of the provenance store (template service)

What the template service built on `modules-services2/service-templates-core` sustains, and what each part of its
per-record write path costs. The harness posts realistic messages to `POST provapi/statements` over plain HTTP (no
dependency on the service's classes: it measures what a client sees), reads the store over JDBC, and turns parts of
the write path off or defers them through opt-in switches of the service. Every switch defaults to the service's
behaviour as it was: nothing changes for an existing deployment.

- [Running it](#running-it)
- [The guard: never a deployed store](#the-guard-never-a-deployed-store)
- [The write path, and what each variant switches off](#the-write-path-and-what-each-variant-switches-off)
- [The switches (service-templates-core)](#the-switches-service-templates-core)
- [What is measured, and how](#what-is-measured-and-how)
- [The plan and its budget](#the-plan-and-its-budget)
- [Caveats and findings from building it](#caveats-and-findings-from-building-it)
- [Smoke run (cloud session, smoke only)](#smoke-run-cloud-session-smoke-only)
- [Results on the M4 Max](#results-on-the-m4-max)
- [Direct JDBC mode](#direct-jdbc-mode)
- [JSONL mode](#jsonl-mode)
- [For developers: how to write provenance with the toolbox](#for-developers-how-to-write-provenance-with-the-toolbox)

## Running it

Layout: a self-contained Maven project in `modules-misc/benchmarks/service-throughput` (not a module of the ProvToolbox
reactor — `modules-misc/benchmarks` is a jar-packaged JMH module, not an aggregator — so the default build is
unaffected), a `Makefile`, `src/main/java/org/openprovenance/bench/throughput` (harness, guard, embedded Jetty
launcher), `results/` (CSV and Markdown, as the JMH benchmarks next door keep theirs). Generated services live under
`target/services/` and are never committed.

Everything below runs unattended on macOS (BSD userland) and Linux, with Java 17–19 (the harness compiles at
`release 15`, no Java 21 API), Maven and Docker. The targets set every variable they need and read nothing from
`~/.bashrc`; the service's environment is built by the harness (none of the caller's `TPL_*`, `PROV_*`,
`TEMPLATE_CONFIG`, `JDBC_POOL_SIZE` reaches it; its credentials are `provbench`/`provbench`, set explicitly).

Once (the Maven builds of ProvToolbox and of the template library may need `source ~/.bashrc` for other modules'
integration tests; the benchmark's own targets do not):

```bash
mvn -pl modules-services2/service-templates-core,modules-tools/prov-template-archetype -am -DskipTests -Dexec.skip=true install
```

```bash
cd ../provenance-templates && mvn -DskipTests install
```

```bash
cd modules-misc/benchmarks/service-throughput && make bench.prepare
```

`bench.prepare` builds the harness (running the guard's test) and generates both services from
`prov-template-archetype`, as its `Makefile`'s `archetype.generate.book.fs`/`.tr` do but with
`-DjdbcURL=jdbc:postgresql://localhost:55432/provbench -DservicePort=7099 -DserviceContext=/provbench`, then builds their
webapps (`-Pjetty,nosecurity`). After a change to `service-templates-core`, `mvn install` it and
`make bench.service.build FAMILY=fs` (and `tr`) again; `FORCE=1` regenerates the project.

Then:

```bash
make bench.db.up
```

```bash
make bench.matrix
```

`bench.db.up` creates (first time) or starts the PostgreSQL container and records `pg_test_fsync` once per machine (`results/pg_test_fsync-<host>.txt`).
`bench.matrix` runs the whole plan and writes `results/<date>.csv` (one row per run, appended),
`results/<date>.md` (machine, software, plan, table; rewritten after every run), `results/<date>-statements.md` (top
statements of each run) and `results/<date>-environment.md`. `LABEL=x` names them `<date>-x.*`. A failed run is
recorded as such (status FAIL and its reasons in the CSV, listed at the top of the Markdown) and the matrix goes on
(`STOP_ON_FAIL=1` stops it); the command's exit status is non-zero if any run failed.

The other targets:

| target | what it does |
|---|---|
| `bench.db.up` / `bench.db.down` / `bench.db.destroy` | start (create) / stop (volume kept) / remove container and volume |
| `bench.db.reset` | drop and recreate `provbench` (the service must be down) |
| `bench.service.up FAMILY=fs\|tr VARIANT=… POOL=…` / `bench.service.down` | the service, left running (for poking at it) |
| `bench.run FAMILY=… VARIANT=… N=… B=… POOL=… WARMUP=… DURATION=… [WRITERS= QUEUE= BATCH=]` | one run, appended to `results/<date>.csv/.md` |
| `bench.plan` | the plan and its time budget, nothing run |
| `bench.smoke` | both families, baseline and async, N=2, POOL=4, 3 s + 8 s each (`results/<date>-smoke.*`) |
| `bench.guard` | the guard alone |

One run, in this order: service down → `provbench` dropped and recreated (and a variant's pre-start DDL) → service up
with the run's `template-config.json` (switches, `jdbc.pool.size`) → the variant's DDL, once the service has created the
schema (`sql.initializer` + `utils.sql`) → warm-up → settle → snapshot → measured window → settle → snapshot and
collection → service down. Every run starts on an empty store.

The service runs on an **embedded Jetty 11** (`ServiceLauncher`: the generated webapp's exploded `-unsecure`
directory, annotation scanning on, `JETTY_MAX_THREADS`, default 200, must be ≥ N + 8) in a JVM of its own
(`SERVICE_JVM_FLAGS`, default `-Xms2g -Xmx2g -XX:+UseG1GC`; recorded in the report). Measured in the cloud session: the
embedded launcher answers 3.3–4.1 s after the JVM starts, schema creation included; `mvn -Pjetty,nosecurity
jetty:run-war` takes 13.7 s (warm) only to start Jetty — and is unusable for the benchmark anyway: the generated pom's
`jetty` profile sets `PROV_STORAGE_CONFIG` to `storage-configuration-mongodb-redis.json` as a system property, so the
webapp connects to Redis on `localhost:6379` and MongoDB on `27017`, live containers on the target machine. The
launcher's storage configuration is `index: memory`, `storage: fs`, Redis and MongoDB ports set to 1.

## The guard: never a deployed store

The benchmark writes only to its own PostgreSQL (Docker `postgres:16`, `127.0.0.1:55432`, database `provbench`,
named volume `provbench-pgdata`, container `provbench-pg` with label `provbench=1`) and its own service
(`http://localhost:7099/provbench`). `Guard.java` is an **allow-list**, run before every command that writes
(`bench.db.*` but `up`'s creation step, `bench.service.up`, `bench.run`, `bench.matrix`; the harness refuses with exit
status 3 and a line starting `GUARD: refused:`):

1. the JDBC URL's host is `localhost` or `127.0.0.1`, its port `55432` (no port — PostgreSQL's 5432 by default — is
   refused), its database exactly `provbench`; several hosts, or URL parameters that could redirect it (`host`, `port`,
   `PGPORT`, `service`, …), are refused;
2. the service URL is `http`, host local, port `7099`, context exactly `/provbench`, nothing after it;
3. exactly one running container carries the label `provbench=1`, and it publishes `55432`; the PostgreSQL answering on
   `55432` over JDBC is that container's cluster (the `system_identifier` of `pg_control_system()` over JDBC equals the
   one `docker exec <container> psql` reads inside it);
4. that cluster holds no database other than `provbench`, `postgres`, `template0`, `template1`.

`bench.db.up` refuses to create its container when `55432` is taken by anything else, or a container named
`provbench-pg` exists without the label. The service's JVM refuses a URL outside the allow-list too
(`ServiceLauncher` checks it), and the harness refuses to start it when `JAVA_TOOL_OPTIONS`/`JDK_JAVA_OPTIONS` carry a
`-DTPL_*`, `-DPROV_*`, `-DTEMPLATE_CONFIG` or `-DJDBC_POOL_SIZE`. Nothing names the deployed stores: their port (5432),
databases and services appear only in `GuardTest`, which proves they are refused (5432, `bookptm`, `templates`, a
`chronicle_*` database, port 7075, a foreign context on 7099, a cluster holding `bookptm`, a container on 5432).

## The write path, and what each variant switches off

One simple record posted to `POST provapi/statements` (`TemplateService.submitStatements` →
`TemplateLogic.processIncomingJson` → the generated enactor → `TemplateLogic.submitPostProcessing`):

1. `WITH insertion_result AS (SELECT * FROM insert_<template>(…)) INSERT INTO record_index … RETURNING id`
   (`CompilerQueryInvokerWithPrincipal`): the plpgsql function allocates the output entities/activities and inserts the
   template row; with an `Idempotency-Key` the `record_index` row carries `submission_key` under the partial unique
   index `record_index_submission_key` (a duplicate is answered from the store, `TemplateQuery.recoverBySubmissionKey`).
2. Read-back of the record just written: `TemplateQuery.querySimple`/`queryComposite`, `LEFT JOIN record_index …
   LEFT JOIN access_control` (`access_control.record` has no index).
3. Hash: `computeHash` (SHA3-512 of the record's CSV) and `updateHash` (`UPDATE record_index SET hash … WHERE key AND
   principal AND table_name`, indexed by `ix_record_index_key_table_principal`).
4. Relations: `RelationMapping.mapGraphToRelations`, one multi-row `INSERT` per PROV relation kind into `__PROV_*`.
   Inside a submission's transaction (`jdbc.pool.size` > 0) on its connection, committed with it; **without a pool on
   a 3-thread executor, fire-and-forget, sharing the service's single connection** (so at `POOL=0` the relations are
   already deferred, without back-pressure or flush).

Composites: the generated composite completer post-processes **each element as a simple record** (read-back, hash and
its `UPDATE`, the element template's `__PROV_*` rows), then the composite itself (`records.size() > 1`): the linker read
and the elements' reads, the composite's hash and `UPDATE`, no relations of its own. Established from the generated
code (`BeanCompleter2.process(<Element>Outputs)` calls `postEnactmentProcessing` per element, `BeanCompleter3` once
more for the composite) and checked by every run: `record_index` grows by 1 + k per composite of k elements. (A
composite of exactly one element reaches the simple branch under the composite's name, which has no relations: same
result.)

Constraints that shape the plan: an `Idempotency-Key` is refused (400) on a message of several entries, on CSV, and
without `Accept: application/vnd.kcl.prov-template+json` — so B > 1 runs are keyless and idempotency is measured at
B = 1; a message's template is its first entry's, so all B entries of a message are of one template and the mix is a
mix of messages; each entry is its own transaction (`templateQuery.inTransaction`), so B > 1 saves HTTP round trips,
not commits.

| variant | service switches | harness / DDL (in `provbench` only) | what it removes |
|---|---|---|---|
| `baseline` | none | `Idempotency-Key` sent (B=1) | nothing: the service as deployed |
| `noacl` | `submission.readback.access_control=off` | | the access-control joins of step 2 |
| `acl-index` | none | `CREATE INDEX … ON access_control (record)` | what the missing index costs step 2 |
| `nohash` | `submission.hash=off` | | step 3 entirely (no header, `record_index.hash` NULL) |
| `hash-header` | `submission.hash=header` | | step 3's `UPDATE` (hash computed and sent) |
| `async` | `relations.write=async` | | step 4 leaves the submission: batched after its commit |
| `nokey` | none | no `Idempotency-Key` | the submission key and its unique index check |
| `key-noindex` | none | key sent, `DROP INDEX record_index_submission_key` | the partial unique index alone |
| `nojit` | none | `ALTER DATABASE provbench SET jit = off`, before the service starts | PostgreSQL's JIT compilation (see findings) |
| `combined` | `noacl` + `nohash` + `async` | no key | all the code switches together |

The harness sends `Accept-PROV-hash`, so the service sends `PROV-Hash` exactly when it computes a hash; every run
checks the header is on every response, or on none (`nohash`).

## The switches (service-templates-core)

Read once in `TemplateService`'s constructor from `template-config.json`, each with an environment (or system property)
fallback, the configuration first; logged at start-up in one line (`write path: …`, with `jdbc.pool.size`);
an unknown value, or an unknown key under `submission.`/`relations.`, is fatal at start-up (one `FATAL` line, as a
failed init SQL is). `GET provapi/submission/status.json` reports the effective switches, the pool size and the
relation queue. `SubmissionSwitches.java` documents them.

| key | environment | values (default first) | effect |
|---|---|---|---|
| `submission.readback.access_control` | `TPL_READBACK_ACCESS_CONTROL` | `on`, `off` | off: `submitPostProcessing` reads the record it just wrote with `TemplateQuery.queryJustWritten`, without the `record_index`/`access_control` join. Every other reading (`query`, `querySimple`, `queryComposite`: GET, search, traversals) keeps it — the switch lives in `TemplateLogic`, not in `TemplateQuery` (`WritePathSwitchesTest`). |
| `submission.hash` | `TPL_SUBMISSION_HASH` | `update`, `header`, `off` | `header`: computed and sent, no `UPDATE`; `off`: neither, no header (a stale one of the thread's previous request is removed), `record_index.hash` NULL. |
| `relations.write` | `TPL_RELATIONS_WRITE` | `inline`, `async` | `async`: a record's `__PROV_*` rows are queued once its submission has committed (`Querier.afterCommit`: dropped when it rolls back; at once without a pool, where every statement autocommits) to `AsyncRelationWriter`. |
| `relations.async.writers` | `TPL_RELATIONS_ASYNC_WRITERS` | `2` | writer threads, each on a connection of its own (a pool of their own: never the service's shared connection nor the requests' pool) |
| `relations.async.queue` | `TPL_RELATIONS_ASYNC_QUEUE` | `10000` | queue capacity, in records; full, the submitting thread waits (back-pressure, after its commit, its connection returned) |
| `relations.async.batch` | `TPL_RELATIONS_ASYNC_BATCH` | `200` | records per batch: one multi-row `INSERT` per relation table per batch, all committed together; a failed batch is retried record by record |

The writer drains its queue on shutdown (the webapp's `ServiceShutdownListener`, and a JVM shutdown hook; up to
120 s). `status.json` reports `depth`, `capacity`, `queued`, `written`, `failed`, `batches`, `blocked` (submissions
that waited) and the commit-to-visible lag (`lag_ms_mean`, `lag_ms_max`, `lag_ms_last`), measured from the commit to
the commit of the batch holding the record's rows.

`jdbc.pool.size` (and `JDBC_POOL_SIZE`) is left as it was: a non-numeric value only warns and means no pool. I would
make it fatal like the switches (a typo silently benchmarks, or deploys, the single-connection service); the harness
guards against it by checking the pool size `status.json` reports against the run's `POOL`.

## What is measured, and how

Clients: N threads, each posting messages of B entries in turn (`java.net.http`, HTTP/1.1, keep-alive), template
chosen per message by weight; a composite (3 elements, `COMPOSITE_SIZE`) in each family. Values are unique (a run-wide
counter in every text), times are now, references to entities/activities/agents are ids the service returned
earlier (outputs), so records link up. fs: `file_init` 2, `file_transforming` 3, `file_filtering` 2,
`file_training`/`file_validating`/`file_approving`/`file_splitting`/`file_transforming_composite` 1 each. tr:
`item_init` 2, `agent_init` 1, `transporting` 3, `handingover` 2, `weighing` 2, `packing`/`unpacking`/
`packing_composite`/`unpacking_composite` 1 each.

Per run (CSV columns):

- **messages/s, records/s** (template records written: a composite of k counts 1 + k), entries per message B;
  cross-checked against `activity_log` (`ps.statements` = messages, `ps.records` = entries, `ps.statements.failed` = 0;
  read after `GET provapi/activity/summary.json` has flushed the counters; the service runs with
  `TPL_ACTIVITY_FLUSH_SECONDS=1`) and against `record_index` (+ records);
- **rows written/s** (`pg_stat_user_tables`, `n_tup_ins + n_tup_upd`, all tables, deltas) and rows per record;
- **WAL bytes/s** and per record (`pg_current_wal_lsn()` deltas);
- **latency per message** p50/p95/p99/max;
- **service CPU** (the service JVM's process CPU time, from `OperatingSystemMXBean` through the launcher's
  `/provbench/__bench/jvm.json`: no `/proc`, no `ps` needed; `/proc` or `ps -o time=` as a fallback) and
  **PostgreSQL CPU** (the container's cgroup: `cpu.stat` `usage_usec` on cgroup v2 — Docker Desktop — else
  `cpuacct.usage`), as % of one core and ms per record;
- **top statements** by total time (`pg_stat_statements`, reset at the window's start) in `-statements.md`;
- **relation lag** (every run): every 200 ms a just-answered simple record is probed — `__PROV_*` polled every 5 ms
  until one of its rows is visible — the lag from its response; with `async`, also the service's own lag figures;
  `drain_s`: how long the queue took to empty after the window;
- **failures**: any non-2xx response, any count mismatch, a probed record whose relations never appear, a relation
  the writers failed to write, a queue not drained: the run is FAIL, loudly (console banner, CSV, top of the
  Markdown).

Settling: when a phase's clients stop, the harness waits for the relation queue to drain (async) and then 2.5 s, so
that the statistics PostgreSQL backends flush after their transactions (at most once a second each) are in before the
snapshot. What the window's submissions cause after it (the drained relations) is counted with it; rates are per
measured second. The probe's queries (a few per second, sequential scans of `__PROV_*`: they have no index) are the
harness's own load on the store.

## The plan and its budget

`make bench.plan` prints it; `PLAN=full` (the default), all at warm-up 10 s, measure 30 s:

| phase | runs | what varies |
|---|---:|---|
| A. each variant alone, fs, N=1 B=1 POOL=0 | 10 | the variant (baseline and the 9 others) |
| B. under concurrency, fs, N=8 B=1 POOL=8 | 7 | baseline, noacl, nohash, async, nokey, nojit, combined |
| C. clients, fs, POOL=16 | 12 | N = 1, 2, 4, 8, 16, 32 × baseline, combined |
| D. pool size, fs, N=16 | 8 | POOL = 0, 4, 8, 32 × baseline, combined (16 is in C) |
| E. records per message, fs, N=8 POOL=8 | 4 | B = 10, 100 × baseline, combined (keyless) |
| F. transport family | 4 | baseline, combined × (N=1 POOL=0, N=16 POOL=16) |
| G. relation writers, fs, N=16 POOL=16, combined | 2 | WRITERS = 1, 4 (2 in C) |

47 runs × (10 s warm-up + 30 s measured + start-up + ~12 s for stop, reset, settles and collection). Start-up was
3.3–4.1 s in the cloud session; budgeted at 10 s: 47 × 62 s ≈ 48.6 min. At 20 s start-up it would be 56 min, still
under the hour. Measured in the cloud session: the restart-to-measure overhead (stop, reset, start, warm-up, settle)
was warm-up + 6–7 s, the stop 1.1–1.5 s. A dry run of the whole plan (`make bench.matrix WARMUP=1 DURATION=2`) ran
all 47 runs unattended, every check passed, in 11.6 min: ~11.8 s per run beyond its 3 s of load, start-up included —
so on that machine the real plan would take 47 × (40 + 11.8) s ≈ 41 min.

## Caveats and findings from building it

- **Each run starts on an empty store**: tables of thousands of rows, all in cache. The figures are those of a young
  store; a store of millions of rows (index depth, the unindexed `access_control.record` and `__PROV_*` scans of
  other readers) is not in this plan.
- **JIT compiled every composite insert** (fixed on this branch, see [The JIT fix](#the-jit-fix)). The statements
  inside the generated composite functions (`insert_<t>_composite_and_linker`, `…_array`) join set-returning
  functions, which the planner estimates at 1000 rows each — 25 million rows joined — over `jit_above_cost`, so
  PostgreSQL 16 (JIT on by default) JIT-compiled them at every call: ~20 ms of the composite's ~23 ms in the cloud
  session (EXPLAIN ANALYZE: `JIT … Emission 19.2 ms, Total 21.9 ms`), 83 % of the database's time in the smoke runs,
  though a composite is 1 message in 12 (fs). Hence the `nojit` variant. The calling statement (the query invoker) is
  not the one compiled: a plain `EXPLAIN` of it costs 84, far under `jit_above_cost` — the compilation happens in the
  functions' own statements, planned at each call.
- **A shared digest corrupted concurrent hashes** (fixed on this branch): `TemplateQuery` hashed every request with one
  `MessageDigest`; with a pool, two submissions hashing at once failed (500) or could get a wrong hash. Now one digest
  per thread (`ConcurrentHashTest`).
- **Unquoted text in two generated invokers**: `transporting.ilocation` and `weighing.unit` are appended to their
  `INSERT` unquoted by the generated `QueryInvoker4` (template library / compiler), so a text value is invalid SQL (and
  an injection). The harness sends them as null; reported as an open issue.
- `ps.records` counts entries (a composite counts 1), the harness's records count template records (1 + k).
- The cloud session's machine (4 vCPU Xeon VM, cgroup v1 Docker) is not the target: its numbers are smoke only.

## Smoke run (cloud session, smoke only)

Linux VM, 4 vCPU Intel Xeon @ 2.10 GHz, 16 GB, Docker 29.9 (cgroup v1), PostgreSQL 16.15 in Docker on a named
volume (pg_test_fsync fdatasync 2292 ops/s, 436 µs/op; `results/pg_test_fsync-vm.txt`), Java 21 (the harness and the service compile at release 15;
Java 17–19 is the target), branch `bench/service-throughput`. Warm-up 3 s, measured 8 s: **smoke figures, to show the
harness works, not results.** Full tables: `results/2026-10-30-smoke-cloud.md`.

| fam | variant | N | B | pool | key | msg/s | records/s | rows/rec | WAL B/rec | p50 ms | p99 ms | svc ms/rec | PG ms/rec | lag p50 ms | lag p95 ms | status |
|---|---|---:|---:|---:|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| fs | baseline | 2 | 1 | 4 | yes | 226.6 | 282.5 | 8.56 | 1986 | 5.69 | 44.76 | 3.40 | 3.06 | 1.4 | 3.6 | ok |
| fs | async | 2 | 1 | 4 | yes | 234.3 | 293.5 | 8.29 | 1991 | 5.41 | 47.0 | 3.76 | 3.37 | 6.2 | 9.5 | ok |
| tr | baseline | 2 | 1 | 4 | yes | 206.4 | 285.1 | 12.54 | 2589 | 5.72 | 44.53 | 3.15 | 3.58 | 1.7 | 4.4 | ok |
| tr | async | 2 | 1 | 4 | yes | 230.1 | 332.7 | 12.85 | 2501 | 4.5 | 41.16 | 2.55 | 3.59 | 9.0 | 12.1 | ok |
| fs | baseline | 1 | 1 | 0 | yes | 121.7 | 148.7 | 9.03 | 2213 | 5.33 | 50.18 | 4.69 | 3.40 | 1.5 | 3.9 | ok |
| fs | combined | 1 | 1 | 0 | no | 192.3 | 238.5 | 7.82 | 1479 | 3.06 | 30.71 | 3.35 | 2.43 | 2.0 | 8.9 | ok |
| fs | nojit | 2 | 1 | 4 | yes | 294.5 | 372.4 | 8.45 | 1827 | 5.47 | 19.62 | 2.81 | 1.70 | 1.6 | 4.6 | ok |

Against the prior estimate (~5 ms per record, 150–250 records/s sequential on one connection, 5–7 statements and
8–14 rows per record): sequential on one connection the smoke run gave 149 records/s (122 messages/s, p50 5.3 ms per
message) with 9 rows per record — in range on this slow VM; but the time is not spread over the statements as
assumed: the composite's JIT compilation dominates the database's time. The 1,000–2,000 records/s with a pool and
parallel clients needs the target machine (4 vCPU here, shared by clients, service and database).

## Results on the M4 Max

`make bench.matrix` on 2026-10-31: 47 runs in 38.4 min, **47 ok, 0 failed**, warm-up 10 s, measured 30 s each, every
run on an empty store. Full tables: `results/2026-10-31.md` and `.csv`, top statements `results/2026-10-31-statements.md`,
environment `results/2026-10-31-environment.md`. The smoke run before it (`results/2026-10-31-smoke-mac.*`): 4/4 ok.
Every figure below is copied from `results/2026-10-31.csv`; each is a single run.

### Machine and software

| | |
|---|---|
| CPU, cores, RAM | Apple M4 Max, 16 cores (16 logical), 48 GiB; macOS 26.5.2 (aarch64) |
| storage | internal SSD (APFS, Apple Fabric) |
| Docker VM (CPUs, RAM) | Docker Desktop 29.7.2, kernel 6.12.76-linuxkit, 16 CPUs, 8,216,018,944 bytes (7.65 GiB), cgroup v2 |
| pg_test_fsync (fdatasync, one 8 kB write) | 10918.572 ops/s, 92 µs/op, on the named volume inside the Docker VM (`results/pg_test_fsync-Mac.txt`) |
| PostgreSQL | 16.15 (Debian 16.15-1.pgdg13+2, aarch64) in Docker; `shared_buffers` 1310728kB, `max_connections` 200, `pg_stat_statements`, `track_io_timing` on; `jit` at its default (on) |
| Java, service JVM flags | Temurin 19.0.1 (service and harness); `-Xms2g -Xmx2g -XX:+UseG1GC`; embedded Jetty 11, 200 threads |
| ProvToolbox commit | `bench/service-throughput` fa0551de5; template library provenance-templates 0cf246b (0.1.0-SNAPSHOT), composite size 3 |

### A. Each variant alone (fs, N=1, B=1, POOL=0)

| variant | msg/s | records/s | rows/rec | WAL B/rec | p50 ms | p99 ms | svc ms/rec | PG ms/rec | lag p95 ms | vs baseline |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| baseline | 286.4 | 358.8 | 9.02 | 2019 | 2.15 | 20.06 | 0.765 | 1.179 | 8.2 | — |
| noacl | 294.6 | 368.9 | 9.0 | 2002 | 2.08 | 19.4 | 0.79 | 1.094 | 9.1 | +2.8 % |
| acl-index | 284.9 | 358.9 | 8.98 | 2000 | 2.1 | 19.96 | 0.839 | 1.186 | 8.6 | +0.0 % |
| nohash | 331.4 | 414.3 | 8.01 | 1740 | 1.79 | 18.07 | 0.659 | 1.093 | 9.9 | +15.5 % |
| hash-header | 300.8 | 376.1 | 7.98 | 1701 | 1.93 | 20.42 | 0.726 | 1.141 | 9.5 | +4.8 % |
| async | 376.3 | 470.6 | 9.05 | 1940 | 1.26 | 17.98 | 0.521 | 1.196 | 12.3 | +31.2 % |
| nokey | 270.1 | 336.7 | 9.0 | 1854 | 2.25 | 21.24 | 0.841 | 1.196 | 9.6 | -6.2 % |
| key-noindex | 274.5 | 344.1 | 9.01 | 1893 | 2.18 | 20.73 | 0.818 | 1.201 | 9.1 | -4.1 % |
| nojit | 349.6 | 435.6 | 9.01 | 2004 | 2.33 | 10.79 | 0.673 | 0.566 | 9.8 | +21.4 % |
| combined | 516.0 | 648.4 | 8.0 | 1463 | 0.91 | 13.13 | 0.438 | 1.037 | 13.9 | +80.7 % |

(svc and PG ms/rec: CPU time per record; lag: the harness's probe, from the response to the first visible `__PROV_*`
row.)

### B. Under concurrency (fs, N=8, B=1, POOL=8)

| variant | msg/s | records/s | p50 ms | p99 ms | svc CPU % | PG CPU % | svc ms/rec | PG ms/rec | lag p95 ms | vs baseline |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| baseline | 1532.9 | 1918.8 | 3.59 | 23.78 | 53 | 280 | 0.275 | 1.458 | 4.7 | — |
| noacl | 1556.7 | 1943.5 | 3.53 | 23.64 | 52 | 263 | 0.268 | 1.355 | 5.4 | +1.3 % |
| nohash | 1594.1 | 1989.8 | 3.26 | 22.68 | 49 | 273 | 0.247 | 1.372 | 4.6 | +3.7 % |
| async | 1871.2 | 2340.9 | 2.77 | 20.96 | 62 | 348 | 0.265 | 1.487 | 31.0 | +22.0 % |
| nokey | 1523.6 | 1901.7 | 3.64 | 24.4 | 54 | 275 | 0.285 | 1.448 | 5.6 | -0.9 % |
| nojit | 1828.7 | 2291.3 | 3.7 | 12.11 | 58 | 153 | 0.253 | 0.669 | 8.0 | +19.4 % |
| combined | 2076.3 | 2587.3 | 2.44 | 20.37 | 61 | 357 | 0.234 | 1.38 | 40.2 | +34.8 % |

(CPU %: of one core.)

### C. Clients (fs, POOL=16)

| N | baseline records/s | baseline p99 ms | PG CPU % (baseline) | combined records/s | combined p99 ms | PG CPU % (combined) | svc CPU % (combined) |
|---:|---:|---:|---:|---:|---:|---:|---:|
| 1 | 351.2 | 18.43 | 41 | 539.2 | 13.99 | 59 | 22 |
| 2 | 719.6 | 17.92 | 88 | 1014.0 | 14.86 | 117 | 27 |
| 4 | 1215.7 | 20.41 | 160 | 1715.5 | 16.31 | 212 | 41 |
| 8 | 1870.8 | 24.85 | 274 | 2871.1 | 17.74 | 354 | 61 |
| 16 | 2533.2 | 31.73 | 426 | 4006.5 | 22.06 | 543 | 84 |
| 32 | 2428.8 | 46.67 | 415 | 3874.7 | 30.26 | 562 | 94 |

### D. Pool size (fs, N=16)

| POOL | baseline records/s | baseline p99 ms | baseline lag p50 ms | baseline lag max ms | combined records/s | combined p99 ms |
|---:|---:|---:|---:|---:|---:|---:|
| 0 | 435.5 | 204.42 | 6175.0 | 15323.2 | 780.0 | 122.92 |
| 4 | 1178.0 | 38.43 | 1.9 | 9.9 | 1770.0 | 28.17 |
| 8 | 1882.6 | 30.6 | 3.1 | 7.7 | 2789.7 | 23.2 |
| 16 | 2533.2 | 31.73 | 4.1 | 11.3 | 4006.5 | 22.06 |
| 32 | 2530.7 | 31.29 | 3.8 | 12.8 | 3872.9 | 21.66 |

(POOL=16 rows are runs of C.)

### E. Records per message (fs, N=8, POOL=8)

| B | baseline records/s | p50 ms per message | svc ms/rec | combined records/s | p50 ms per message | svc ms/rec |
|---:|---:|---:|---:|---:|---:|---:|
| 1 | 1901.7 | 3.64 | 0.285 | 2587.3 | 2.44 | 0.234 |
| 10 | 2039.7 | 34.1 | 0.235 | 3059.8 | 20.57 | 0.167 |
| 100 | 2080.5 | 341.84 | 0.199 | 3121.6 | 206.12 | 0.135 |

(B > 1 is keyless, so the B=1 baseline row is B's `nokey` run; the B=1 combined row is B's `combined`.)

### F. Transport family

| variant | N | POOL | msg/s | records/s | p99 ms | rows/rec | WAL B/rec | svc ms/rec | PG ms/rec |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| baseline | 1 | 0 | 207.8 | 294.9 | 22.9 | 13.19 | 2688 | 0.916 | 1.461 |
| combined | 1 | 0 | 392.0 | 557.6 | 13.4 | 12.18 | 2138 | 0.517 | 1.295 |
| baseline | 16 | 16 | 1745.9 | 2498.4 | 33.05 | 13.27 | 2485 | 0.317 | 2.002 |
| combined | 16 | 16 | 2540.3 | 3638.7 | 23.66 | 12.52 | 2114 | 0.198 | 1.68 |

### G. Relation writers (fs, combined, N=16, POOL=16)

| writers | records/s | p99 ms | lag p50 ms | lag p95 ms | service lag mean ms | service lag max ms | submissions blocked | drain s |
|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| 1 | 4290.7 | 20.79 | 33.1 | 50.9 | 4.3 | 63.9 | 0 | 0.0 |
| 2 | 4006.5 | 22.06 | 32.2 | 52.2 | 3.9 | 56.5 | 0 | 0.01 |
| 4 | 3835.0 | 23.23 | 33.4 | 56.9 | 3.5 | 46.1 | 0 | 0.0 |

(WRITERS=2 is C's N=16 combined run.)

### What each part costs

Read with two cautions: each figure is one run, and the N=1 runs differ by up to about 6 % where nothing should change
(`nokey` −6.2 %, `key-noindex` −4.1 %), so a difference of that size is noise; and every run starts on an empty store.

**Against the prior estimate.** Sequential on one connection (A, baseline) the store wrote 358.8 records/s, 2.8 ms of
wall time per record (0.77 ms of service CPU, 1.18 ms of PostgreSQL CPU), against the estimate's 150–250 records/s and
~5 ms; rows per record are 9.0 (fs) and 13.2 (tr), inside the estimate's 8–14. With a pool and parallel clients the
baseline reached 2,533 records/s at N=16 (C), PostgreSQL using 4.3 cores: at or above the estimate's 1,000–2,000 on 4
cores.

**Each variant against the baseline** (A at N=1, B at N=8):

- *JIT* (`nojit`) is the largest single cost: +21.4 % records/s at N=1 and +19.4 % at N=8; PostgreSQL CPU per record
  1.179 → 0.566 ms (−52 %) and 1.458 → 0.669 ms (−54 %), PostgreSQL CPU at N=8 280 % → 153 %, p99 halved (20.06 → 10.79
  ms, 23.78 → 12.11 ms). It is all the composite: `file_transforming_composite`'s insert is one message in twelve but
  86.3 % of the statements' time at N=1 (9.3 ms per call) and 82.3 % at N=16 (12.4 ms per call), and 0.28 ms per call
  with JIT off (`-statements.md`, runs 01, 09, 22).
- *Asynchronous relations* (`async`): +31.2 % at N=1 (service CPU per record 0.765 → 0.521 ms, p50 2.15 → 1.26 ms) and
  +22.0 % at N=8. At POOL=0 the relations were already off the request (today's fire-and-forget executor), yet the
  bounded writer is faster: its writers have connections of their own and batch, where the executor's inserts queue on
  the requests' single connection.
- *Hash*: `nohash` +15.5 % at N=1, but +3.7 % at N=8; it removes one row per record (9.02 → 8.01) and 279 B of WAL
  (2019 → 1740). `hash-header` (hash computed, no `UPDATE`) +4.8 %: the `UPDATE` itself is 0.009 ms per call in
  PostgreSQL. Under concurrency the hash is at the noise level.
- *Access control on the read-back* (`noacl`): +2.8 % at N=1, +1.3 % at N=8; an index on `access_control(record)`
  (`acl-index`) changes nothing (+0.0 %). Noise, on an empty store.
- *Idempotency* (`nokey`, `key-noindex`): −6.2 % and −4.1 % at N=1, −0.9 % at N=8: no measurable cost; the key saves
  165 B of WAL per record when absent (2019 → 1854).
- *All code switches together* (`combined` = noacl + nohash + async, keyless): +80.7 % at N=1 (648.4 records/s;
  service 0.438, PostgreSQL 1.037 ms per record) and +34.8 % at N=8 (2,587 records/s). `combined` keeps JIT on; with
  JIT off as well it was not measured.

**Where throughput saturates.** With POOL=16 (C), records/s grows with the clients up to N=16 (baseline 351 → 2,533,
×7.2; combined 539 → 4,007, ×7.4) and falls slightly at N=32 (2,429; 3,875), where p50 latency doubles (5.82 → 13.53
ms baseline): 32 clients wait for 16 connections. The pool (D) caps throughput below N — POOL 0, 4, 8, 16 give 436,
1,178, 1,883, 2,533 records/s at N=16 — and POOL=32 for 16 clients adds nothing (2,531). At the top nothing on the
machine is saturated: PostgreSQL uses 4.3 cores (baseline) to 5.6 (combined) of the VM's 16, the service JVM under one
core (84–94 %), WAL ~4.6 MB/s, against an fdatasync of 92 µs. **The ceiling measured is the plan's concurrency (16
connections), not the hardware**: each submission's latency, dominated by the composite's JIT, times 16. Where the
machine's own ceiling lies (N=32 with POOL=32, harness on another host) this plan does not show.

**No pool.** At POOL=0 (D) 16 clients get 436 records/s with p99 204 ms, and the relations — on today's 3-thread
executor sharing the single connection — were visible 6.2 s (p50) to 15.3 s (max) after the response: an unbounded
backlog, lost if the service stops. Any concurrent load needs a pool.

**What B saves.** B > 1 saves HTTP and per-message service work, not commits (each entry is its own transaction): at
N=8, POOL=8, B = 1, 10, 100 give 1,902, 2,040, 2,081 records/s keyless (+7 %, +9 %) and 2,587, 3,060, 3,122 combined
(+18 %, +21 %); service CPU per record falls 0.285 → 0.199 ms (−30 %) and 0.234 → 0.135 ms (−42 %), PostgreSQL's
barely moves. A message's latency grows with B (p50 34 ms at B=10, 342 ms at B=100, baseline).

**Async lag and drain.** The service's own commit-to-visible lag averaged 1.0–4.7 ms in every async run, its maximum
12.7–86.9 ms except in two runs: 795 ms (tr, N=16) and 1,834 ms (combined, POOL=32), the latter with a 1,851 ms maximum
request latency in the same run — a stall of the whole store (GC, checkpoint or the VM), not of the queue. The
harness's probe, from the response with 5 ms polling over unindexed `__PROV_*`, saw p50 8.5–55.8 ms, p95 12.3–82.7 ms.
No submission ever waited for the queue (blocked 0), and it drained within 0.01 s of the window's end. One writer keeps
up (G: 1, 2, 4 writers 4,291, 4,007, 3,835 records/s, lag alike); the highest rate of the matrix, **4,291 records/s,
p99 20.8 ms**, is combined with one writer at N=16, POOL=16.

**Transport family** (F): 13.2 rows per record, more PostgreSQL time per record (1.46 ms at N=1, 2.00 at N=16); baseline
295 and 2,498 records/s, combined 558 (+89 %) and 3,639 (+46 %).

**JIT in the deployed PostgreSQL.** The deployed PostgreSQL (5432) answers `SHOW jit` = `on` (read 2026-10-31, the only
statement run against it). Whether it compiles also needs a server built with LLVM (`pg_jit_available()`, not checked);
if it does, its composite inserts pay the cost measured here.

**What I would change in the store**, in order of what it buys:

1. Take JIT off the composite inserts, in the store alone: `ROWS` estimates on the generated set-returning functions
   (`CompilerSQL`), or `SET jit = off` as an attribute of the generated composite functions. From A and B: −52 % of
   PostgreSQL's CPU per record, p99 halved, +20 % records/s. **Done on this branch** (`SET jit = off`), measured below.
2. Never run a store under concurrent load without a pool sized to its concurrent writers; and replace the no-pool
   relation path (fire-and-forget, unbounded, seconds behind) by the bounded writer.
3. Enable the asynchronous relation writer where readers accept a few milliseconds' lag: +22–31 %, one writer enough.
4. Keep the hash, the access-controlled read-back and idempotency: on this store each is at the noise level under
   concurrency, not worth the guarantee it gives up.
5. Measure next: N=32 with POOL=32 (and the harness on another host) for the machine's ceiling; `combined` with JIT
   off; a store pre-filled with millions of rows, where the unindexed `access_control.record` and `__PROV_*` reads may
   start to cost (the probe's own `__PROV_*` lookup already took 14 ms per call at N=16, run 28, on a store 30 s old).

### The JIT fix

The compiler now declares the composite insert functions `SET jit = off` (`CompilerSqlComposer.COMPOSITE_FUNCTION_SETTINGS`,
on `insert_<t>_composite_array` and `insert_<t>_composite_and_linker`; `CompositeFunctionJitTest`): JIT is off while
they run, nowhere else. The template library regenerated with it carries the setting on 2 fs and 4 transport
functions (`pg_proc.proconfig` = `{jit=off}` in `provbench`). Re-measured on 2026-10-31 with the same harness, warm-up
10 s, measured 30 s, 10 runs, all ok (`results/2026-10-31-jitfix.*`, `make bench.run LABEL=jitfix …`); "before" is
the matrix above (for N=32, POOL=32 there is none: the plan had stopped at 16 connections).

| fam | variant | N | POOL | records/s before | records/s after | change | p99 ms before | p99 ms after | PG ms/rec after | PG CPU % after |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| fs | baseline | 1 | 0 | 358.8 | 435.0 | +21.2 % | 20.06 | 10.81 | 0.606 | 26 |
| fs | combined | 1 | 0 | 648.4 | 1378.8 | +112.6 % | 13.13 | 2.98 | 0.42 | 58 |
| fs | baseline | 8 | 8 | 1918.8 | 2278.1 | +18.7 % | 23.78 | 12.32 | 0.672 | 153 |
| fs | combined | 8 | 8 | 2587.3 | 3677.4 | +42.1 % | 20.37 | 6.8 | 0.501 | 184 |
| fs | baseline | 16 | 16 | 2533.2 | 2961.0 | +16.9 % | 31.73 | 18.65 | 0.818 | 242 |
| fs | combined | 16 | 16 | 4006.5 | 4907.3 | +22.5 % | 22.06 | 10.03 | 0.565 | 277 |
| fs | baseline | 32 | 32 | — | 3795.7 | | — | 28.83 | 0.96 | 364 |
| fs | combined | 32 | 32 | — | 6579.0 | | — | 15.41 | 0.617 | 406 |
| tr | baseline | 16 | 16 | 2498.4 | 2838.6 | +13.6 % | 33.05 | 21.44 | 0.889 | 252 |
| tr | combined | 16 | 16 | 3638.7 | 5024.2 | +38.1 % | 23.66 | 11.28 | 0.644 | 324 |

Reading:

- The composite insert costs 0.30–0.45 ms per call instead of 9.3–12.4 ms, and 8–16 % of the statements' time
  instead of 82–86 % (`-jitfix-statements.md`). The baseline at N=1 (435.0 records/s, PostgreSQL 0.606 ms per record)
  is the matrix's `nojit` run (435.6, 0.566): the fix removes exactly what `jit = off` for the whole database removed.
- With the fix the switches matter more than before: `combined` is now +217 % over the baseline at N=1 (1,378.8 against
  435.0) and +66 % at N=16 (4,907.3 against 2,961.0); p99 falls to 3–15 ms.
- 32 parallel writers (N=32, POOL=32) still scale: baseline 2,961 → 3,796 records/s (+28 %), combined 4,907 → 6,579
  (+34 %), with PostgreSQL at 3.6–4.1 cores of the VM's 16 and the service JVM at 1.2 cores. **The highest rate
  measured is now 6,579 records/s, p99 15.4 ms** (fs, combined, N=32, POOL=32): the machine's ceiling is still above
  it.

## Direct JDBC mode

The same records written to the same store **without the web service**: no HTTP, no JSON, no Jetty, no per-request
work of the service — what the database (and the generated SQL) sustains alone, and so, against the HTTP runs, what
the service costs.

### Design

- **The same SQL, composed by the generated code.** The harness loads the template library's generated
  `<catalogue.package>.CatalogueDispatcher` (fs: `org.openprovenance.templates.catalogue.fs`, tr:
  `org.openprovenance.templates.catalogue.transport`) by name, as `TemplateService` does, and calls
  `initEnactorConverter`/`initCompositeEnactorConverter` with a `queryExecutor` of its own. A record goes through the
  catalogue's enactor converter exactly as a posted entry does: the record built in the template's property order
  (`getPropertyOrder()`, as `TemplateLogic.convertToArray` builds it from JSON), turned into the generated `…Inputs`
  bean by the generated converter, checked by the generated `BeanChecker2`, its SQL composed by the generated
  `QueryInvoker4` (`WITH insertion_result AS (SELECT * FROM insert_<t>(…)) INSERT INTO record_index … RETURNING id`;
  composites `insert_<t>_composite_and_linker(ARRAY[…])`), run by the `queryExecutor` on the transaction's
  connection (`Storage.executeQuery`, as the service's), the result read by the generated completer.
- **The post-processing reused, not re-implemented** (service-templates-core, in-process, unchanged): `TemplateQuery`
  (read-back, hash, `updateHash`; its constructor installs the same traversal functions as at the service's start),
  `TemplateLogic.submitPostProcessing` as the completers' post-processing callback, `RelationMapping` (inline
  `__PROV_*` inserts), `AsyncRelationWriter` (attached through `org.openprovenance.prov.service.core.DirectAccess`, a
  one-method class of the direct sources in that package: `TemplateQuery.getRelationMapping()` is package-private),
  `SubmissionSwitches` (the run's READBACK/HASH/RELATIONS, read as the service reads its configuration), `Querier` and
  `PooledConnections` (a transaction per `Querier.inTransaction`), `Storage`, `SubmissionKeyManager` (the
  Idempotency-Key the invoker writes into `record_index.submission_key`), `PrincipalManager` (principal `nil`, the
  service's without security).
- **The schema as the service makes it**: after the reset, `sql.initializer` (the library's SQL, with the JIT fix),
  `utils.sql` (from the generated webapp's classes), then `TemplateQuery`'s constructor. No `activity_log` (no service):
  `record_index` is the cross-check.
- **The records** are the HTTP mode's (`Family`: same templates, weights, composites of 3, unique values, references to
  ids returned earlier).
- **Connections**: N writer threads, a pool of N (`PooledConnections`): each transaction on a connection of its own;
  async relations on the writer's own pool, as in the service.
- **Build and classpath**: `src/direct/java` and `src/direct-test/java`, compiled only with `mvn -Pdirect` into
  `target/direct` against service-templates-core (`provided`). It runs on the generated fs webapp's `WEB-INF/classes`
  and `WEB-INF/lib` — the jars the service runs on, both catalogues being in the one library jar — then the harness's
  libraries. The default build (the HTTP mode) is unchanged: it has no service class and no service dependency.
- **Environment**: the service's classes read `TPL_*`, `PROV_*`, `TEMPLATE_CONFIG`, `JDBC_POOL_SIZE` (class
  initialisation of `TemplateService`, whose `headerInfo` the post-processing uses); when any is set, `DirectMain` runs
  itself again in a JVM without them, so nothing of the caller's environment reaches them.

### What each layer includes

| setting | values | what a record costs |
|---|---|---|
| `LAYER=insert` | | the insert function (template row, the entities/activities it allocates) and the `record_index` row, in one statement; no post-processing |
| `LAYER=post` (default) | | + the service's post-processing per record: read-back, hash and its `UPDATE`, `__PROV_*` relations — a composite's elements each, then the composite (as in the service) |
| `READBACK` | `on`, `off` | post: the read-back's `record_index`/`access_control` join (`submission.readback.access_control`) |
| `HASH` | `update`, `header`, `off` | post: `submission.hash` |
| `RELATIONS` | `inline`, `async` | post: `relations.write` (with `WRITERS`, `QUEUE`, `BATCH`) |
| `KEY` | `on` (default), `off` | an Idempotency-Key per record (allowed at any B here: the 400 on several entries is the service's) |
| `B` | 1, 10, 100 | records per **transaction** (the service commits each entry on its own; here B records commit together) |
| `JDBC_BATCH` | `0`, `1` | with `LAYER=insert`: the B composed statements sent in one round trip (one `Statement.execute` of the B statements, their results read in turn). The SQL carries its values inline (no `PreparedStatement` to `addBatch`), and the post-processing needs each record's id before its next statement, so a batch is for the insert layer only (refused with `LAYER=post`). |

Presets of the plan: `insert` (KEY=off), `insert+key`, `post` (every switch at the service's default, KEY=off),
`post+key` (the HTTP baseline's work), `combined` (READBACK=off HASH=off RELATIONS=async KEY=off: the HTTP combined's).

### What is measured

As the HTTP mode, minus the service: transactions/s and records/s; rows written/s and per record (n_tup_ins +
n_tup_upd); WAL/s and per record; latency per **transaction** p50/p95/p99/max; the client JVM's CPU (the harness
writing directly: its writers, the probe and the sampler) and PostgreSQL's (cgroup); top statements; relation lag
(probed, async only); `record_index` + records; and, every 100 ms of the window, the **wait events** of the writers'
backends (`pg_stat_activity`: `CPU` when active without one, `type:event` otherwise, `Client:IdleInTransaction`
between a transaction's statements — the client's own time inside it), the top five in the CSV with the mean number of
busy backends.

Checks (a run that fails one is FAIL, loudly): no failed transaction; `record_index` grew by the records written;
**rows written equal exactly what the catalogue says these records write** — per simple record its template row, one
row per output variable, its `record_index` row, with `LAYER=post` one row per relation tuple of the seven stored
kinds and the hash `UPDATE`; per composite of k its k element rows, its shared outputs once and the others per
element, k + 1 `record_index` rows, its own row, k linker rows, and with `LAYER=post` the elements' relations and k + 1
`UPDATE`s. With everything on, that is 9.0 rows per record (fs) and 13.2 (tr) on the mix, as in the HTTP runs. For this
check the settle before each snapshot is 10.5 s, not the HTTP mode's 2.5 s: PostgreSQL 16 keeps an idle backend's
pending table statistics up to 10 s (`PGSTAT_IDLE_INTERVAL`) when it flushed less than a second before. (So the HTTP
mode's rows/s may miss or gain up to about a second of each backend's rows at the window's edges — the two edges
partly cancel; under 3 % at 30 s.)

### Running it

```bash
make bench.prepare
```

```bash
make bench.db.up
```

```bash
make bench.direct.run FAMILY=fs LAYER=post KEY=on N=16 B=1
```

```bash
make bench.direct.matrix
```

```bash
make bench.direct.compare HTTP_RESULTS=results/2026-10-31.csv,results/2026-10-31-jitfix.csv
```

`bench.direct.*` build `-Pdirect` (running `DirectGuardTest` and `GuardTest`) and need the generated services
(`bench.prepare`; after a change to service-templates-core or the template library: `mvn install` them and
`make bench.service.build FAMILY=fs`, and `tr`). The HTTP service must be down (the run refuses otherwise). Results:
`results/<date>-direct.csv` and `.md` (the same machine and software header, a "direct client JVM" row), `-statements.md`,
`-environment.md`. `bench.direct.compare` writes `results/<date>-direct-compare.md`: each direct run beside the HTTP run
doing the same work — direct `post+key` and HTTP `baseline` with its key (B=1); direct `post` and HTTP `nokey`, or the
keyless `baseline` at B>1; direct `combined` and HTTP `combined`; same family, N and B; HTTP runs with POOL ≥ N only (at
POOL=0 the service shares one connection: another store). By default the HTTP files are those whose environment file
names the same CPU (never another machine's); `HTTP_RESULTS` names them instead. `bench.direct.plan` prints the plan;
`bench.direct.smoke` is the short check of this section. Defaults: warm-up 5 s, measured 20 s (one JVM stays warm across
the matrix), `DIRECT_JVM_FLAGS` `-Xms2g -Xmx2g -XX:+UseG1GC`.

The guard: `DirectRun` runs `Store.guard()` (the allow-list above: URLs, the labelled container by system identifier,
the cluster's databases) before anything is opened, reset or written, and refuses while the HTTP service is up; every
connection of the mode (writers, relation writers, probe, sampler, schema set-up) comes from `Store.connect`, which
checks the JDBC URL against the allow-list. `DirectGuardTest`: a refusing guard stops a run before any connection or
reset; connections to `bookptm`, `templates`, a `chronicle_*` database are refused before any network; the service
classes see none of `TPL_DB_USER`, `TPL_DB_PASS`, `TPL_SECURITY_CONFIG`, `PROV_STORAGE_CONFIG`, `TEMPLATE_CONFIG`,
`JDBC_POOL_SIZE`; a JDBC batch with post-processing is refused before any connection; the settings' values.

### The plan and its budget

`make bench.direct.plan` (`PLAN=full`, warm-up 5 s, measured 20 s):

| phase | runs | what varies |
|---|---:|---|
| D1. layers, fs, N=1 B=1 | 9 | insert, insert+key, post, post+key, combined; post with READBACK=off, HASH=header, HASH=off, RELATIONS=async alone |
| D2. writers, fs, B=1 | 18 | N = 2, 4, 8, 16, 32, 64 × insert, post+key, combined (N=1 in D1) |
| D3. records per transaction, fs, N=16 | 8 | B = 10, 100 × insert, post+key, combined; insert as one JDBC batch at B = 10, 100 |
| D4. transport family, B=1 | 6 | N = 1, 16 × insert, post+key, combined |
| D5. relation writers, fs, combined, N=32 | 2 | WRITERS = 1, 4 (2 in D2) |

43 runs × (5 s warm-up + 20 s measured + ~25 s for the reset, the schema and catalogue set-up — ~1.2 s here — the two
10.5 s settles and the collection) ≈ 43 × 50 s ≈ 36 min: under the 45 minutes. In the cloud smoke run the 8 direct
runs (3 s + 8 s each) took 4.7 min: ~24 s per run beyond its 11 s of load, within the 25 s budgeted.

### Smoke run (cloud session, smoke only)

Linux VM, 4 vCPU Intel Xeon @ 2.80 GHz (another host than the HTTP smoke run of 2026-10-30), 16 GB, Docker 29.9 (cgroup v1), PostgreSQL 16.15, Java 21 (release 15 bytecode),
the template library rebuilt with the JIT fix; warm-up 3 s, measured 8 s, N=2: **smoke figures, to show the mode works, not
results** — on 4 vCPUs the client, the service (HTTP) and PostgreSQL share the cores. `make bench.direct.smoke LABEL=smoke-cloud`:
8 runs, 8 ok (`results/2026-10-31-direct-smoke-cloud.*`); every run's rows equal the catalogue's count exactly.

| fam | layer | key | readback | hash | relations | N | B | JDBC batch | records/s | tx/s | rows/rec | p50 ms/tx | p99 ms/tx | client ms/rec | PG ms/rec | busy | top waits | status |
|---|---|---|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|---|
| fs | insert | off |  |  |  | 2 | 1 | 0 | 748.4 | 601.9 | 4.08 | 3.05 | 7.33 | 0.947 | 1.102 | 1.3 | CPU 33%; IO:WALSync 33%; Client:IdleInTransaction 21%; LWLock:WALWrite 10%; Client:ClientRead 2% | ok |
| fs | post | on | on | update | inline | 2 | 1 | 0 | 288.4 | 227.4 | 8.96 | 7.36 | 27.05 | 3.9 | 2.956 | 1.8 | Client:IdleInTransaction 42%; CPU 36%; IO:WALSync 17%; LWLock:WALWrite 3%; Client:ClientRead 2% | ok |
| fs | post | off | off | off | async | 2 | 1 | 0 | 419.7 | 331.3 | 7.96 | 5.19 | 16.93 | 2.905 | 2.359 | 2.2 | Client:IdleInTransaction 35%; CPU 29%; IO:WALSync 20%; LWLock:WALWrite 10%; Client:ClientRead 5% | ok |
| tr | insert | off |  |  |  | 2 | 1 | 0 | 787.2 | 554.1 | 4.45 | 3.22 | 8.4 | 0.568 | 1.071 | 1.4 | CPU 36%; IO:WALSync 27%; Client:IdleInTransaction 24%; LWLock:WALWrite 8%; Client:ClientRead 3% | ok |
| tr | post | on | on | update | inline | 2 | 1 | 0 | 328.8 | 226.2 | 13.21 | 6.83 | 25.62 | 1.223 | 3.045 | 1.7 | Client:IdleInTransaction 49%; CPU 37%; IO:WALSync 10%; Client:ClientRead 2%; LWLock:WALWrite 1% | ok |
| tr | post | off | off | off | async | 2 | 1 | 0 | 559.7 | 393.2 | 12.23 | 4.26 | 13.03 | 0.871 | 2.216 | 2.5 | CPU 37%; Client:IdleInTransaction 34%; IO:WALSync 18%; LWLock:WALWrite 6%; Client:ClientRead 4% | ok |
| fs | insert | off |  |  |  | 2 | 10 | 1 | 2628.9 | 209.3 | 4.06 | 8.17 | 28.83 | 0.204 | 0.489 | 1.5 | CPU 75%; Client:IdleInTransaction 11%; IO:WALSync 8%; Client:ClientRead 2%; IO:DataFileExtend 2% | ok |
| fs | post | off | on | update | inline | 2 | 10 | 0 | 458.5 | 36.9 | 9.0 | 44.6 | 203.04 | 1.092 | 2.299 | 2.0 | Client:IdleInTransaction 63%; CPU 30%; Client:ClientRead 5%; IO:WALSync 2% | ok |

Against HTTP on the same VM (`make bench.direct.compare LABEL=smoke-cloud`, the HTTP runs `results/2026-10-31-smoke-cloud-jitfix.*`,
N=2, POOL=4, same durations; `results/2026-10-31-direct-smoke-cloud-compare.md`):

| fam | direct | HTTP | B | direct records/s | HTTP records/s | HTTP/direct | direct rows/rec | HTTP rows/rec |
|---|---|---|---:|---:|---:|---:|---:|---:|
| fs | post+key | baseline | 1 | 288.4 | 145.2 | 0.50 | 8.96 | 8.42 |
| fs | combined | combined | 1 | 419.7 | 179.0 | 0.43 | 7.96 | 7.33 |
| tr | post+key | baseline | 1 | 328.8 | 144.1 | 0.44 | 13.21 | 12.36 |
| tr | combined | combined | 1 | 559.7 | 206.3 | 0.37 | 12.23 | 11.25 |
| fs | post | baseline | 10 | 458.5 | 237.3 | 0.52 | 9.0 | 9.51 |

Read as smoke: the HTTP mode reached 0.37–0.52 of the direct rate here, where the service's JVM competes with
PostgreSQL and the client for 4 cores. The HTTP rows/record differ from the exact counts (8.42 against 8.96, 9.51
against 9.00 at B=10) because of the HTTP mode's 2.5 s settle on 8-second windows (see above); at 30 s on the M4 Max
they were 9.0. With B=10 records per transaction, `post` keeps 9.00 rows/record. The insert layer as one JDBC batch of
10 wrote 2,629 records/s, against 748 at one record per transaction. With the post-processing on, the writers'
backends spend 35–63 % of their samples `Client:IdleInTransaction`: inside a transaction, waiting for the client's
Java work between statements.

### Results on the M4 Max (direct)

`make bench.direct.matrix` on 2026-10-31: 43 runs in 33.8 min, **43 ok, 0 failed**, warm-up 5 s, measured 20 s,
every run on an empty store, the template library with the JIT fix; every run's rows equal the catalogue's count
exactly. Machine and software as in [Results on the M4 Max](#results-on-the-m4-max) (Temurin 19.0.1, client JVM
`-Xms2g -Xmx2g -XX:+UseG1GC`, branch at fca84b7da). Full tables: `results/2026-10-31-direct.md` and `.csv`, top
statements `-direct-statements.md`, environment `-direct-environment.md`, the comparison `-direct-compare.md`; the smoke
run before it: `results/2026-10-31-direct-smoke-mac.*` (8/8 ok). Each figure is one run, copied from the CSV.

#### D1. Layers (fs, N=1, B=1)

| layer | key | readback | hash | relations | records/s | tx/s | rows/rec | WAL B/rec | p50 ms/tx | p99 ms/tx | client ms/rec | PG ms/rec | top waits |
|---|---|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---|
| insert | off |  |  |  | 1197.6 | 959.0 | 4.07 | 813 | 0.98 | 2.2 | 0.129 | 0.18 | IO:WALSync 42%; CPU 32%; Client:IdleInTransaction 23%; Client:ClientRead 3% |
| insert | on |  |  |  | 1169.8 | 941.6 | 4.07 | 954 | 1.0 | 2.0 | 0.109 | 0.191 | CPU 41%; Client:IdleInTransaction 33%; IO:WALSync 24%; Client:ClientRead 2% |
| post | off | on | update | inline | 494.4 | 391.8 | 8.95 | 1710 | 2.06 | 8.2 | 0.237 | 0.541 | Client:IdleInTransaction 66%; CPU 19%; IO:WALSync 9%; Client:ClientRead 5% |
| post | on | on | update | inline | 509.1 | 408.6 | 9.01 | 1825 | 2.01 | 7.57 | 0.227 | 0.52 | Client:IdleInTransaction 67%; CPU 24%; IO:WALSync 6%; Client:ClientRead 3% |
| post | off | off | off | async | 932.1 | 742.8 | 7.99 | 1456 | 1.14 | 3.41 | 0.169 | 0.441 | Client:IdleInTransaction 33%; CPU 29%; IO:WALSync 24%; Client:ClientRead 12%; LWLock:WALWrite 1% |
| post | off | off | update | inline | 537.8 | 437.3 | 9.05 | 1724 | 1.94 | 6.59 | 0.163 | 0.472 | Client:IdleInTransaction 72%; CPU 13%; IO:WALSync 9%; Client:ClientRead 5% |
| post | off | on | header | inline | 557.9 | 444.2 | 8.0 | 1426 | 1.89 | 6.47 | 0.221 | 0.447 | Client:IdleInTransaction 68%; CPU 21%; IO:WALSync 9%; Client:ClientRead 3%; IO:WALWrite 1% |
| post | off | on | off | inline | 562.4 | 446.0 | 7.99 | 1425 | 1.85 | 6.85 | 0.19 | 0.466 | Client:IdleInTransaction 68%; CPU 23%; Client:ClientRead 6%; IO:WALSync 4% |
| post | off | on | update | async | 660.3 | 532.2 | 9.03 | 1746 | 1.49 | 6.35 | 0.209 | 0.575 | Client:IdleInTransaction 36%; CPU 34%; IO:WALSync 21%; Client:ClientRead 6%; LWLock:WALWrite 3% |

#### D2. Writers (fs, B=1)

| N | insert records/s | post+key records/s | combined records/s | PG CPU % (insert / post+key / combined) | busy backends (combined) | p99 ms/tx (combined) | top waits (combined) |
|---:|---:|---:|---:|---|---:|---:|---|
| 1 | 1197.6 | 509.1 | 932.1 | 22 / 26 / 41 | 0.9 | 3.41 | Client:IdleInTransaction 33%; CPU 29%; IO:WALSync 24%; Client:ClientRead 12%; LWLock:WALWrite 1% |
| 2 | 529.6 | 965.1 | 1738.9 | 13 / 52 / 74 | 1.9 | 3.46 | Client:IdleInTransaction 46%; CPU 24%; IO:WALSync 18%; Client:ClientRead 8%; LWLock:WALWrite 4% |
| 4 | 3201.3 | 1404.3 | 2676.6 | 71 / 91 / 119 | 3.2 | 4.55 | Client:IdleInTransaction 49%; CPU 24%; IO:WALSync 17%; LWLock:WALWrite 6%; Client:ClientRead 4% |
| 8 | 6049.7 | 2294.3 | 3330.0 | 143 / 155 / 173 | 5.6 | 11.56 | Client:IdleInTransaction 55%; CPU 18%; LWLock:WALWrite 11%; IO:WALSync 10%; Client:ClientRead 5% |
| 16 | 7987.4 | 2227.5 | 5496.4 | 220 / 175 / 269 | 10.4 | 9.09 | Client:IdleInTransaction 51%; LWLock:WALWrite 18%; CPU 18%; IO:WALSync 7%; Client:ClientRead 6% |
| 32 | 9371.1 | 3767.7 | 7530.4 | 342 / 383 / 409 | 20.1 | 13.55 | Client:IdleInTransaction 56%; LWLock:WALWrite 19%; CPU 15%; Client:ClientRead 6%; IO:WALSync 4% |
| 64 | 12589.0 | 5272.8 | 9363.2 | 485 / 542 / 573 | 38.4 | 24.29 | Client:IdleInTransaction 58%; LWLock:WALWrite 21%; CPU 12%; Client:ClientRead 4%; IO:WALSync 2% |

(N=1 is D1's runs: insert, post+key, combined.)

#### D3. Records per transaction (fs, N=16)

| B | insert | insert, JDBC batch | post+key | combined | p50 ms/tx (combined) | PG CPU % (insert batch / combined) |
|---:|---:|---:|---:|---:|---:|---|
| 1 | 7987.4 | — | 2227.5 | 5496.4 | 3.12 | — / 269 |
| 10 | 24178.3 | 30626.8 | 3586.2 | 8053.9 | 18.71 | 504 / 417 |
| 100 | 28786.8 | 63233.5 | 3354.2 | 9372.8 | 153.25 | 818 / 421 |

(B=1 is D2's N=16 runs.)

#### D4. Transport family (B=1)

| preset | N | records/s | rows/rec | PG ms/rec | p99 ms/tx |
|---|---:|---:|---:|---:|---:|
| insert | 1 | 1160.9 | 4.45 | 0.22 | 3.87 |
| post+key | 1 | 444.2 | 13.19 | 0.615 | 10.28 |
| combined | 1 | 762.3 | 12.2 | 0.585 | 6.87 |
| insert | 16 | 7270.7 | 4.45 | 0.291 | 7.35 |
| post+key | 16 | 2693.4 | 13.2 | 0.917 | 25.68 |
| combined | 16 | 5183.5 | 12.21 | 0.581 | 11.84 |

#### D5. Relation writers (fs, combined, N=32)

| writers | records/s | lag p50 ms | lag p95 ms | writer lag mean ms | writer lag max ms | drain s |
|---:|---:|---:|---:|---:|---:|---:|
| 1 | 7394.2 | 37.2 | 63.0 | 7.5 | 84.3 | 0.0 |
| 2 | 7530.4 | 39.2 | 55.9 | 5.8 | 69.6 | 0.0 |
| 4 | 6300.2 | 36.5 | 49.9 | 6.3 | 47.3 | 0.0 |

(WRITERS=2 is D2's N=32 combined run.)

#### Direct against HTTP

`make bench.direct.compare HTTP_RESULTS=results/2026-10-31-jitfix.csv`: paired with the HTTP runs made after the JIT
fix only (the matrix's HTTP runs had the composites JIT-compiled: another store). Same family, N, B = 1 and work;
the HTTP pool equals N.

| fam | direct | HTTP | N | direct records/s | HTTP records/s | HTTP/direct | direct rows/rec | HTTP rows/rec | direct PG ms/rec | HTTP PG ms/rec | direct p50 ms/tx | HTTP p50 ms/msg | HTTP service ms/rec |
|---|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| fs | post+key | baseline | 8 | 2294.3 | 2278.1 | 0.99 | 9.0 | 9.02 | 0.674 | 0.672 | 3.65 | 3.72 | 0.261 |
| fs | post+key | baseline | 16 | 2227.5 | 2961.0 | 1.33 | 9.03 | 9.01 | 0.787 | 0.818 | 9.28 | 5.78 | 0.296 |
| fs | post+key | baseline | 32 | 3767.7 | 3795.7 | 1.01 | 9.01 | 9.04 | 1.018 | 0.96 | 8.65 | 9.03 | 0.317 |
| fs | combined | combined | 8 | 3330.0 | 3677.4 | 1.10 | 8.01 | 7.96 | 0.52 | 0.501 | 2.36 | 2.36 | 0.184 |
| fs | combined | combined | 16 | 5496.4 | 4907.3 | 0.89 | 8.0 | 8.01 | 0.49 | 0.565 | 3.12 | 3.53 | 0.188 |
| fs | combined | combined | 32 | 7530.4 | 6579.0 | 0.87 | 8.0 | 8.02 | 0.544 | 0.617 | 4.61 | 5.3 | 0.185 |
| tr | post+key | baseline | 16 | 2693.4 | 2838.6 | 1.05 | 13.2 | 13.28 | 0.917 | 0.889 | 6.6 | 6.47 | 0.309 |
| tr | combined | combined | 16 | 5183.5 | 5024.2 | 0.97 | 12.21 | 12.21 | 0.581 | 0.644 | 3.56 | 3.76 | 0.187 |

(`-direct-compare.md` also pairs D5's one- and four-writer runs with the HTTP combined run at two writers; left out
here.)

#### What the database sustains alone

Read with the same cautions as the HTTP results: single runs, an empty store; and here PostgreSQL is reached through
Docker Desktop's VM, so every statement crosses the VM boundary. Two runs fell below their neighbours with WAL
stalls — insert at N=2 (529.6 records/s, below N=1's 1,197.6; IO:WALSync 46 %, LWLock:WALWrite 42 %, max 174 ms) and
post+key at N=16 (2,227.5, below N=8's 2,294.3; LWLock:WALWrite 28 %, max 292 ms) — and are best read as noise until
re-run.

**The web service costs no throughput.** Doing the same work at the same concurrency, HTTP reached 0.87–1.33 of the
direct rate (0.99, 1.01 for the baseline's work at N=8, 32; 1.10, 0.89, 0.87 for combined at N=8, 16, 32; tr 1.05,
0.97), with the same PostgreSQL time per record both ways (e.g. 0.674 and 0.672 ms at N=8). The service's own work —
0.18–0.32 ms of CPU per record — runs on cores the database does not use, and adds under a millisecond to a message
(p50 within 0.7 ms, except post+key at N=16). Only combined at N=16–32 is direct faster, by 12–15 %. The ceilings of the HTTP
runs are those of the store's write pattern, not of Jetty, JSON or the service.

**The write pattern: a transaction per record, statements with Java between them.** At N=1 the insert alone (the
insert function and `record_index`, one statement) writes 1,197.6 records/s; with the service's post-processing
(read-back, hash and its `UPDATE`, `__PROV_*` relations: 4.9 more rows per record, 900 B more WAL) 494.4 — 2.4 times
the time per record, PostgreSQL 0.18 → 0.54 ms per record. And the writers' backends spend 66–72 % of their samples
`Client:IdleInTransaction` with the post-processing on (23–33 % for the insert alone): inside the record's
transaction, waiting for the client's next statement. Against `post` (494.4, keyless): the read-back without its
access-control join +8.8 %, the hash without its `UPDATE` +12.8 %, no hash +13.8 %, async relations +33.6 %,
all together (`combined`) +88.5 % (932.1); the Idempotency-Key costs nothing (post+key 509.1).

**Where it saturates.** With N writers, B=1 (D2), every preset still grows at N=64 but less than linearly beyond 16:
insert 1,198 → 7,987 (N=16) → 12,589 records/s (N=64), post+key 509 → 5,273, combined 932 → 9,363. At N=64
PostgreSQL uses 4.9–5.7 of the VM's 16 cores: not CPU. What rises with N is the WAL: `LWLock:WALWrite` 38 % of the
insert's samples at N=64, 21 % of combined's — every record's commit flushes the WAL, and the commits queue for the WAL
write lock. With post-processing the backends stay mostly idle in their transactions (58–76 %).

**What grouping saves** (D3, N=16). Records per transaction: insert 7,987 → 24,178 (B=10) → 28,787 (B=100); the same
inserts as one JDBC batch 30,627 and **63,234 records/s** (B=100), PostgreSQL then at 8.2 cores with `CPU` its top wait
(61 %): that is the database's own ceiling for these inserts on this machine, about five times the per-record-commit
rate. With the post-processing, grouping helps less, since each record keeps its round trips: post+key 2,228 → 3,586
→ 3,354 (+61 %, +51 %), combined 5,496 → 8,054 → 9,373 (+47 %, +71 %); idle-in-transaction 71–81 %.

**Transport** (D4): insert 1,160.9 and 7,270.7 records/s (N=1, 16), post+key 444.2 and 2,693.4, combined 762.3 and
5,183.5; 13.2 rows per record.

**Relation writers** (D5, combined, N=32): one, two, four writers 7,394, 7,530, 6,300 records/s, the writers' own lag
5.8–7.5 ms on average, at most 84 ms, the queue drained at the window's end: one or two writers suffice.

**What I would change in the store**, from these results:

1. Fewer round trips per record: the post-processing as SQL inside the record's statement — the insert function
   writing its `__PROV_*` rows and returning the record (no read-back), the hash computed from what it returns — would
   bring a record near the insert layer's cost (2.4 times less time per record at N=1).
2. Several records per transaction in the service: an entry commits alone today; committing a message's B entries
   together gave +47–71 % (combined) and up to ×3.6 for the inserts, by sparing a WAL flush per record.
3. WAL commit contention (`LWLock:WALWrite` up to 38 %): `commit_delay`/`commit_siblings`, or `synchronous_commit = off`
   where losing the last few hundred milliseconds on a crash is acceptable — not measured here.
4. A bulk path (one JDBC batch of composed inserts, relations asynchronous) for loads: 63,234 records/s on this machine.
5. Measure on a Linux host with PostgreSQL on a local socket: here every statement crosses Docker Desktop's VM, which
   inflates the idle-in-transaction time the post-processing shows.

## JSONL mode

For comparison: the same records' **bindings serialised as JSON lines and appended to files of the local file
system** — no store, no service, no index, no relation, nothing read back. A floor: what writing the records down
costs at all, beside what the database (direct mode) and the service (HTTP mode) add to it.

### Design

- **The records** are the other modes' (`Family`: same templates, weights, composites of 3, unique values,
  references to ids allocated earlier). Each line is a record's **complete bindings** (`Family.bindings`): `isA`, its
  `ID`, its input values, and its output variables bound to ids the harness allocates — what the store's insert
  function allocates (`Family.OUTPUTS`, the catalogue's ioMap outputs). A composite is one line with its
  `__elements`: its own `ID`, each element's, its shared outputs one id per composite (in place of the tokens), the
  others one per element. About 330 bytes per template record for fs, 230 for tr.
- **Writes**: N writer threads; each serialises B records (Jackson), appends them in one `FileChannel.write` and, with
  `FSYNC=batch` (default), forces them to disk (`FileChannel.force(false)`) before the next: B records as one durable
  unit, as a transaction's commit. `FSYNC=off` leaves them to the OS's cache. `FILES=writer` (default): a file per
  writer; `FILES=shared`: one file, the appends and their force serialised.
- **Durability, honestly**: `force` is fdatasync on Linux and, on macOS, `fcntl(F_FULLFSYNC)` (measured on the M4 Max:
  4.1 ms, against 22 µs for a plain `fsync`; see the M4 Max results) — a flush of the drive's own cache. The files are on the host's file system (on the Mac: APFS on the internal SSD), where
  PostgreSQL's WAL is on the Docker VM's virtual disk (pg_test_fsync). Same records, different disks: compare the shapes,
  and read the absolute figures with that in mind.
- **Files**: `JSONL_DIR` (default `target/jsonl`) may hold nothing but the mode's own `provbench-<family>-<writer>.jsonl`
  (the guard: `Guard.jsonlDirectory`, tested in `GuardTest`); the run deletes those before it starts and after its
  checks (gigabytes at full speed with `FSYNC=off`), never anything else. No database, no Docker: the JSONL mode is part
  of the default (HTTP) build and needs neither.
- **Checks** (a run that fails one is FAIL): the files hold exactly the bytes written and one line per entry (warm-up
  and window), every 997th line and the last parse as JSON with an `isA`.
- **Measured**: records/s (template records, a composite counting 1 + 3) and batches/s, MB/s and bytes per record,
  latency per batch (serialisation, write and force) p50/p95/p99/max, the harness JVM's CPU.

### Running it

```bash
make bench.jsonl.run FAMILY=fs N=16 B=1 FSYNC=batch FILES=writer
```

```bash
make bench.jsonl.matrix
```

`bench.jsonl.plan` prints the plan; `bench.jsonl.smoke` is the short check. Defaults: warm-up 3 s, measured 10 s
(nothing to start or reset). Results: `results/<date>-jsonl.csv` and `.md`, `-environment.md` (with the JSONL
directory's file system). `make bench.direct.compare` adds a table **"Three ways to write the same records"** to
`results/<date>-direct-compare.md`: at the same family, N and B, JSONL (`FSYNC=batch`, a file per writer), direct
`insert`, `post+key`, `combined`, HTTP `baseline`, `combined` — reports of the same machine only.

### The plan and its budget

| phase | runs | what varies |
|---|---:|---|
| J1. one writer, fs, B=1 | 2 | FSYNC = batch, off |
| J2. writers, fs, B=1, a force per record, a file each | 6 | N = 2, 4, 8, 16, 32, 64 |
| J3. one shared file, fs, B=1 | 2 | N = 16, 64 |
| J4. records per force, fs, N=16 | 4 | B = 10, 100 with FSYNC=batch; B = 1, 100 with FSYNC=off |
| J5. transport family, B=1 | 2 | N = 1, 16 |

16 runs × (3 s warm-up + 10 s measured + ~3 s to clear, count and delete) ≈ 4.3 min (the smoke run's 4 runs at 2 s + 5 s
took 0.5 min here). At N=64 with `FSYNC=off` the files grow at hundreds of MB/s: the run deletes them, but leave a few GB
free.

### Smoke run (cloud session, smoke only)

Linux VM (4 vCPU Xeon, ext4 on a virtual disk), Java 21, warm-up 2 s, measured 5 s: **smoke figures, not results**.
`make bench.jsonl.smoke LABEL=smoke-cloud`: 4 runs, 4 ok (`results/2026-10-31-jsonl-smoke-cloud.*`).

| fam | N | B | fsync | files | records/s | batches/s | MB/s | B/rec | p50 ms/batch | p99 ms/batch | CPU % | status |
|---|---:|---:|---|---|---:|---:|---:|---:|---:|---:|---:|---|
| fs | 2 | 1 | batch | writer | 4762.3 | 3802.4 | 1.49 | 329 | 0.428 | 1.723 | 57 | ok |
| tr | 2 | 1 | batch | writer | 7175.3 | 5000.5 | 1.59 | 233 | 0.35 | 1.022 | 62 | ok |
| fs | 2 | 1 | off | writer | 179626.8 | 143877.7 | 58.83 | 343 | 0.004 | 0.093 | 200 | ok |
| fs | 2 | 10 | batch | shared | 18525.5 | 1493.8 | 5.91 | 335 | 1.134 | 4.845 | 73 | ok |

Three ways on this VM, N=2, B=1 (`results/2026-10-31-direct-smoke-cloud-compare.md`; the HTTP runs N=2, POOL=4), records/s:

| fam | JSONL, a force per record | direct insert | direct post+key | direct combined | HTTP baseline | HTTP combined |
|---|---:|---:|---:|---:|---:|---:|
| fs | 4762.3 | 748.4 | 288.4 | 419.7 | 145.2 | 179.0 |
| tr | 7175.3 | 787.2 | 328.8 | 559.7 | 144.1 | 206.3 |

Read as smoke (the three were run minutes apart on a shared VM, the JSONL files on its virtual disk, PostgreSQL's WAL
in a container on the same disk): appending a record's bindings and forcing them to disk is about 6–9 times as fast as
the store's insert alone, and 30–50 times as fast as a record through the service; without the force, 180,000
records/s at N=2, the serialisation the only cost left.

### Results on the M4 Max (JSONL)

`make bench.jsonl.matrix` on 2026-10-31: 16 runs in 4.3 min, **16 ok, 0 failed** (warm-up 3 s, measured 10 s; the
smoke run before it, `results/2026-10-31-jsonl-smoke-mac.*`, 4/4 ok). Files on the internal SSD (APFS). Full tables:
`results/2026-10-31-jsonl.md` and `.csv`. One run each, copied from the CSV.

**On macOS a force is a full flush of the drive.** `FileChannel.force` issues `fcntl(F_FULLFSYNC)` on macOS (measured
on this Mac with Java 17.0.5 and 19.0.1: 4.1 ms per 331-byte append and force, exactly `F_FULLFSYNC`'s 4.1 ms from C;
plain `fsync` takes 22 µs). That flushes the SSD's own cache — more than PostgreSQL's commit does here: its WAL
`fdatasync` in the Docker VM takes 92 µs (`pg_test_fsync`), so it does not reach the drive's cache, and PostgreSQL's
default `wal_sync_method` on macOS does not use `F_FULLFSYNC` either (only `fsync_writethrough` does). With
`FSYNC=batch` the JSONL rows below therefore measure a stricter durability than the database's, and the drive's flush
serialises them: one writer 283 records/s, and no more than ~530 at any N.

| phase | fam | N | B | fsync | files | records/s | MB/s | B/rec | p50 ms/batch | p99 ms/batch | CPU % |
|---|---|---:|---:|---|---|---:|---:|---:|---:|---:|---:|
| J1 | fs | 1 | 1 | batch | writer | 282.9 | 0.08 | 314 | 4.118 | 6.648 | 5 |
| J1 | fs | 1 | 1 | off | writer | 371991.5 | 120.35 | 339 | 0.002 | 0.005 | 100 |
| J2 | fs | 2 | 1 | batch | writer | 266.5 | 0.08 | 314 | 8.415 | 16.938 | 4 |
| J2 | fs | 4 | 1 | batch | writer | 328.9 | 0.1 | 314 | 12.578 | 37.289 | 5 |
| J2 | fs | 8 | 1 | batch | writer | 434.4 | 0.13 | 317 | 21.059 | 55.76 | 8 |
| J2 | fs | 16 | 1 | batch | writer | 532.6 | 0.16 | 316 | 32.816 | 100.189 | 13 |
| J2 | fs | 32 | 1 | batch | writer | 473.0 | 0.14 | 317 | 68.299 | 429.749 | 15 |
| J2 | fs | 64 | 1 | batch | writer | 521.5 | 0.16 | 317 | 102.394 | 910.571 | 20 |
| J3 | fs | 16 | 1 | batch | shared | 286.6 | 0.09 | 313 | 63.828 | 203.534 | 4 |
| J3 | fs | 64 | 1 | batch | shared | 280.1 | 0.08 | 315 | 243.817 | 936.519 | 4 |
| J4 | fs | 16 | 10 | batch | writer | 5069.2 | 1.58 | 327 | 35.756 | 100.294 | 16 |
| J4 | fs | 16 | 100 | batch | writer | 38980.1 | 12.42 | 334 | 41.775 | 178.218 | 21 |
| J4 | fs | 16 | 1 | off | writer | 693221.9 | 225.96 | 342 | 0.02 | 0.164 | 1245 |
| J4 | fs | 16 | 100 | off | writer | 3948557.2 | 1307.9 | 347 | 0.224 | 1.5 | 1306 |
| J5 | tr | 1 | 1 | batch | writer | 349.9 | 0.07 | 222 | 4.104 | 5.806 | 3 |
| J5 | tr | 16 | 1 | batch | writer | 512.9 | 0.11 | 225 | 36.332 | 153.539 | 9 |

#### Three ways to write the same records

`make bench.direct.compare HTTP_RESULTS=results/2026-10-31.csv,results/2026-10-31-jitfix.csv,results/2026-10-31-postfix.csv`
(`results/2026-10-31-direct-compare.md`). The HTTP files are read in that order and the last match wins, so every HTTP
figure below comes from a run with the JIT fix: `-postfix` (the whole HTTP matrix again, 2026-10-31, 47 runs in 38.2
min, all ok) or, at N=32, `-jitfix`. Only runs with the default relation writers are paired, and no smoke report.
fs unless stated, records/s; JSONL with a force per batch and a file per writer; HTTP with POOL ≥ N.

| fam | N | B | JSONL | direct insert | direct post+key | direct combined | HTTP baseline | HTTP combined | JSONL B/rec |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| fs | 1 | 1 | 282.9 | 1197.6 | 509.1 | 932.1 | 491.6 | 883.2 | 314 |
| fs | 2 | 1 | 266.5 | 529.6 | 965.1 | 1738.9 | 993.2 | 1546.5 | 314 |
| fs | 4 | 1 | 328.9 | 3201.3 | 1404.3 | 2676.6 | 1541.3 | 2204.0 | 314 |
| fs | 8 | 1 | 434.4 | 6049.7 | 2294.3 | 3330.0 | 2305.9 | 3653.0 | 317 |
| fs | 16 | 1 | 532.6 | 7987.4 | 2227.5 | 5496.4 | 2889.7 | 4020.2 | 316 |
| fs | 32 | 1 | 473.0 | 9371.1 | 3767.7 | 7530.4 | 3795.7 | 6579.0 | 317 |
| fs | 64 | 1 | 521.5 | 12589.0 | 5272.8 | 9363.2 | | | 317 |
| fs | 16 | 10 | 5069.2 | 24178.3 | 3586.2 | 8053.9 | | | 327 |
| fs | 16 | 100 | 38980.1 | 28786.8 | 3354.2 | 9372.8 | | | 334 |
| tr | 1 | 1 | 349.9 | 1160.9 | 444.2 | 762.3 | | | 222 |
| tr | 16 | 1 | 512.9 | 7270.7 | 2693.4 | 5183.5 | 2897.2 | 5024.7 | 225 |

Empty HTTP cells: not run at that N and B (the HTTP plan stops at N=32 and varies B at N=8; at N=1 its tr runs had no
pool). HTTP combined at N=16 is 4,020.2 with a pool of 32 here, 5,003.7 with a pool of 16 in the same matrix, and
4,907.3 in the `-jitfix` runs: one run each, the spread of the measurement at that load.

Three readings. JSONL with a force per record is the *slowest* way on this Mac, not the fastest: each force is a 4.1 ms
`F_FULLFSYNC` that PostgreSQL's commit does not pay here (above), and the drive serialises them (~530 records/s at most).
Grouping restores it: a force per 100 records gives 38,980 records/s, and without a force JSONL writes 0.69 million
records/s at N=16, 3.9 million in batches of 100 — serialisation is all that is left. Direct and HTTP doing the same work
are within run-to-run noise of each other (HTTP/direct 0.73–1.30). And the store's own insert, without the per-record
post-processing, is 2.4 to 3.6 times as fast as the full write.

## For developers: how to write provenance with the toolbox

What the three benchmarks say to a developer choosing how an application records provenance through ProvToolbox's
template store. All figures: fs family, M4 Max (16 cores), PostgreSQL 16 in Docker Desktop, the template library with
the JIT fix, one run each — read differences under about 10 % as noise (two runs of the same HTTP configuration gave
4,020 and 5,004 records/s). An empty store: tables of millions of rows are not measured.

### The options

| | **1. The template service (HTTP)** | **2. In-process, direct JDBC** | **3. JSONL capture to files** |
|---|---|---|---|
| what it is | `POST provapi/statements` to a deployed service (generated from `prov-template-archetype`), one or several records per message | the generated catalogue and `service-templates-core` in your JVM, writing the same SQL on your own connections | each record's bindings appended as a JSON line to a local file, forced to disk per batch |
| you get | everything: validation, records with ids, `PROV-Hash`, access control, idempotency, `__PROV_*` relations for navigation and explanations, activity counting, any client language | the same records and SQL; post-processing optional per layer; several records per transaction; JDBC batches | a durable, ordered capture — no store, no ids from a database, no relations, no queries, no access control |
| records/s, one writer | 492 (all on) – 883 (switches) | 509 (all on) – 932 (switches); insert only 1,198 | 283 forcing each record; 372,000 without forcing |
| records/s, best measured | 3,796 (all on, 32 clients) – 6,579 (switches, 32) | 5,273 (all on, 64) – 9,363 (switches, 64); 9,373 (switches, 100 per transaction); insert only 12,589 (64), 63,234 (one JDBC batch of 100) | 38,980 (16 writers, a force per 100); 3.9 million without forcing |
| p99 per write at 16 writers | 19.7 ms (all on), 10.1 ms (switches) | 21.5 ms per transaction (all on), 9.1 ms (switches) | 100 ms forcing each record |
| costs | a service to deploy and secure; its JVM uses 0.2–0.3 ms of CPU per record (on cores the database does not use) | coupling to the generated classes and schema; Java only; access control is yours | a later load into the store to make it queryable; on macOS a forced write is a full drive flush (4.1 ms) |

### What to do

1. **Default to the service.** Doing the same work, it is as fast as writing from your own JVM: HTTP reached 0.73–1.30
   of the direct rate at every concurrency measured, with the same PostgreSQL time per record. HTTP, JSON and Jetty are
   not what limits the store; the write pattern is (below). Expect about 2,300 records/s with 8 clients and 2,900 with
   16, everything on, on a machine like this one.
2. **Configure a pool, sized to your concurrent writers** (`jdbc.pool.size` ≥ clients). Without one, 16 clients share
   one connection: 506 records/s, p99 172 ms, and relations written seconds late (up to 9.6 s) by a fire-and-forget
   executor with no back-pressure. A pool larger than the number of clients buys nothing.
3. **Turn on asynchronous relations** (`relations.write=async`) when readers can wait a few milliseconds for a record's
   `__PROV_*` rows: +97 % with one client, +19 % with 8 (HTTP, `-postfix`); commit-to-visible 1–10 ms on average, the
   queue drained, never full. One or two writer threads suffice.
4. **Keep the hash, the access-controlled read-back and idempotency.** With 8 clients they cost +10 % (hash), +3 %
   (access control) and nothing measurable (idempotency) — at the noise level, against the guarantees they give.
   Together with async relations (`combined`) the gain is +50 % at 8 clients, +219 % with one.
5. **Send concurrently, not in big messages.** Records per message save HTTP work but not commits — each entry is its
   own transaction in the service: B = 10, 100 gave 2,251 and 2,198 records/s against 2,260 at B=1 (8 clients, a pool of 8). Eight
   to 32 concurrent clients is where the store scales.
6. **Embed (option 2) for bulk loads, not for speed per se.** What it adds is control of the transaction: 100 records
   per transaction raise `combined` from 5,496 to 9,373 records/s, and the insert alone, as a JDBC batch of 100,
   reaches 63,234 records/s — PostgreSQL then CPU-bound at 8 cores, its ceiling here. Skip the post-processing only if
   you do not need what it writes (hash, relations) or will compute it later.
7. **Use JSONL (option 3) as a buffer, not as the store**: to capture provenance where the store is out of reach, or
   bursts faster than it ingests, then load it. Force per batch, not per record: on macOS a force is a full drive flush
   (4.1 ms: 283 records/s), on Linux an `fdatasync`. It records bindings only: no ids from the store, no relations, no
   queries until loaded.

### Where the store's limit is, and what the toolbox could change

Every record is a transaction of 5–7 statements: the insert (template row, its entities and activities,
`record_index`), the read-back, the hash `UPDATE`, one `INSERT` per relation kind — with the client between them. The
database's backends spend 66–76 % of their time idle inside those transactions, waiting for the next statement, and
every commit flushes the WAL (`LWLock:WALWrite` up to 38 % at 64 writers). PostgreSQL never used more than 5.7 of its
16 cores at one record per transaction. In order of what it would buy:

1. **Fewer round trips per record**: the post-processing as SQL inside the insert (the function writing its
   `__PROV_*` rows and returning the record, the hash from what it returns): towards the insert layer's cost, 2.4 times
   less time per record.
2. **A message's entries in one transaction** in the service: +47–71 % (`combined`), up to ×3.6 for the inserts.
3. **WAL commit settings** (`commit_delay`, or `synchronous_commit = off` where losing the last moments on a crash is
   acceptable): not measured.
4. **A bulk endpoint** taking JSONL or many records, written as JDBC batches with asynchronous relations.

Done on the branch already: the composite inserts no longer JIT-compiled (`SET jit = off`), which had taken 82–86 % of
the database's time; `combined` with one client doubled (648.4 → 1,340.8 records/s).

### HTTP matrix with the JIT fix

The guide's HTTP figures come from `results/2026-10-31-postfix.*` (the matrix of [Results on the M4 Max](#results-on-the-m4-max)
again, after the JIT fix; 47 runs, all ok). The variants against the baseline:

| variant | N=1, POOL=0 records/s | vs baseline | N=8, POOL=8 records/s | vs baseline |
|---|---:|---:|---:|---:|
| baseline | 420.9 | — | 2259.7 | — |
| noacl | 447.3 | +6.3 % | 2323.8 | +2.8 % |
| acl-index | 457.3 | +8.6 % | | |
| nohash | 515.7 | +22.5 % | 2475.0 | +9.5 % |
| hash-header | 517.8 | +23.0 % | | |
| async | 829.7 | +97.1 % | 2691.4 | +19.1 % |
| nokey | 440.6 | +4.7 % | 1967.7 | −12.9 % |
| key-noindex | 432.2 | +2.7 % | | |
| nojit | 452.5 | +7.5 % | 2111.4 | −6.6 % |
| combined | 1340.8 | +218.6 % | 3379.9 | +49.6 % |

With the fix in, `nojit` changes nothing by construction: its +7.5 % and −6.6 % (and `nokey`'s −12.9 %, a run with a
215 ms stall) show the noise of single runs. Clients (POOL=16), baseline and combined: N=1 491.6 and 883.2, N=2 993.2
and 1,546.5, N=4 1,541.3 and 2,204.0, N=8 2,305.9 and 3,653.0, N=16 2,884.0 and 5,003.7, N=32 2,896.8 and 5,115.3 (32
clients on 16 connections). Pool (N=16): 0, 4, 8, 16, 32 give 506.0, 1,590.1, 2,186.5, 2,884.0, 2,889.7 (baseline).
Records per message (N=8, POOL=8): baseline 2,250.6 and 2,198.1 at B = 10, 100; combined 3,475.8 and 3,897.6. Transport:
baseline 390.3 and 2,897.2, combined 1,262.2 and 5,024.7 (N=1 POOL=0; N=16 POOL=16).
