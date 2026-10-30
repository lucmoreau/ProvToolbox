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
- **JIT compiles every composite insert.** The generated composite functions (`insert_<t>_composite_and_linker`,
  `…_array`) make the planner estimate 25 million rows (the default 1000 rows of set-returning functions, joined),
  over `jit_above_cost`, so PostgreSQL 16 (JIT on by default) JIT-compiles the statement at every call: ~20 ms of the
  composite's ~23 ms in the cloud session (EXPLAIN ANALYZE: `JIT … Emission 19.2 ms, Total 21.9 ms`), 83 % of the
  database's time in the smoke runs, though a composite is 1 message in 12 (fs). Hence the `nojit` variant. Whether the
  deployed PostgreSQL has JIT on (it is a build option) decides whether this matters there; `ROWS` on the generated
  functions, or `jit = off` for the store, would remove it.
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

To be filled from `make bench.matrix` on the target machine: macOS, Apple M4 Max (16 cores, 48 GB), Docker Desktop (VM
16 CPUs, ~7.6 GB), Java 17–19. Copy the machine table and the run table from `results/<date>.md`; no figure here
before that run.

### Machine and software

| | |
|---|---|
| CPU, cores, RAM | |
| storage | |
| Docker VM (CPUs, RAM) | |
| pg_test_fsync (fdatasync, one 8 kB write) | |
| PostgreSQL | |
| Java, service JVM flags | |
| ProvToolbox commit | |

### A. Each variant alone (fs, N=1, B=1, POOL=0)

| variant | msg/s | records/s | rows/rec | WAL B/rec | p50 ms | p99 ms | svc ms/rec | PG ms/rec | lag p95 ms | vs baseline |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| baseline | | | | | | | | | | |
| noacl | | | | | | | | | | |
| acl-index | | | | | | | | | | |
| nohash | | | | | | | | | | |
| hash-header | | | | | | | | | | |
| async | | | | | | | | | | |
| nokey | | | | | | | | | | |
| key-noindex | | | | | | | | | | |
| nojit | | | | | | | | | | |
| combined | | | | | | | | | | |

### B. Under concurrency (fs, N=8, B=1, POOL=8)

| variant | msg/s | records/s | p50 ms | p99 ms | svc CPU % | PG CPU % | lag p95 ms |
|---|---:|---:|---:|---:|---:|---:|---:|
| baseline | | | | | | | |
| noacl | | | | | | | |
| nohash | | | | | | | |
| async | | | | | | | |
| nokey | | | | | | | |
| nojit | | | | | | | |
| combined | | | | | | | |

### C. Clients (fs, POOL=16)

| N | baseline records/s | baseline p99 ms | combined records/s | combined p99 ms | PG CPU % (combined) |
|---:|---:|---:|---:|---:|---:|
| 1 | | | | | |
| 2 | | | | | |
| 4 | | | | | |
| 8 | | | | | |
| 16 | | | | | |
| 32 | | | | | |

### D. Pool size (fs, N=16)

| POOL | baseline records/s | combined records/s | combined p99 ms |
|---:|---:|---:|---:|
| 0 | | | |
| 4 | | | |
| 8 | | | |
| 16 | | | |
| 32 | | | |

### E. Records per message (fs, N=8, POOL=8)

| B | baseline records/s | combined records/s | p50 ms per message |
|---:|---:|---:|---:|
| 1 | | | |
| 10 | | | |
| 100 | | | |

### F. Transport family

| variant | N | POOL | records/s | p99 ms | rows/rec |
|---|---:|---:|---:|---:|---:|
| baseline | 1 | 0 | | | |
| combined | 1 | 0 | | | |
| baseline | 16 | 16 | | | |
| combined | 16 | 16 | | | |

### G. Relation writers (fs, combined, N=16, POOL=16)

| writers | records/s | lag p50 ms | lag p95 ms | drain s |
|---:|---:|---:|---:|---:|
| 1 | | | | |
| 2 | | | | |
| 4 | | | | |

### What each part costs

(From A and B: records/s and ms per record against the baseline, per variant; to be written from the results.)
