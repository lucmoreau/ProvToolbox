# Activity of the provenance store

How much is the store doing? The dashboard's *Activity* tab answers with the number of
messages (and records, and failures) over the last minute, hour, day and week, refreshed
periodically. This note gives the design, the table, and what another service writing
to the same store (chronicle) has to do to show up in the same tab.

## Design in one paragraph

Nothing is written per message. `ActivityLogger.count(kind)` on the request path is a
`currentTimeMillis`, two concurrent-map lookups and a `LongAdder` increment: no lock, no
I/O, no allocation once the bucket and the kind exist. The counts are aggregated in
memory **per time bucket** (10 s) **and per kind**, and a single daemon thread flushes
them every 10 s as **one batched upsert** to PostgreSQL, over a connection of its own so
it neither queues behind the request threads nor holds them up. However many messages
arrive, at most one row per bucket and per kind reaches the table; a quiet bucket
produces no row. The upsert *adds* to the stored count, so a bucket flushed twice, or
written by two services, loses nothing. If the database is away, the counts stay in
memory (bounded) and go out when it is back. Rows older than the retention (400 days)
are pruned once an hour.

Why not derive the figures from `record_index.created_at`, which every record already
carries? It would answer "records per window" with just an index, but not messages
(one POST carries many records), not failures, and not chronicle's own kinds. The
activity table is generic: anything a service wants to count, at the same cost.

## The table

Created idempotently at service start (`JdbcActivitySink.DDL`); every service may run it.

```sql
CREATE TABLE IF NOT EXISTS activity_log
(
  bucket timestamp with time zone NOT NULL,   -- start of the time bucket (UTC)
  kind   text NOT NULL,                        -- what is counted, e.g. ps.statements
  count  bigint NOT NULL,                      -- occurrences in that bucket
  PRIMARY KEY (bucket, kind)
);

CREATE TABLE IF NOT EXISTS activity_kind       -- the human description of each kind
(
  kind        text PRIMARY KEY,
  description text NOT NULL,
  updated_at  timestamp with time zone NOT NULL DEFAULT NOW()
);
```

`kind` is the column that says what type of information a row counts. The convention is
`<service>.<what>`, lower case, dotted:

| kind                    | counted by          | meaning |
|-------------------------|---------------------|---------|
| `ps.statements`         | the template service | messages posted to `/statements`, one per request |
| `ps.records`            | the template service | template records those messages wrote |
| `ps.statements.failed`  | the template service | messages whose processing failed |
| `chronicle.<what>`      | chronicle            | whatever chronicle decides to count |

A write is

```sql
INSERT INTO activity_log (bucket, kind, count) VALUES (?, ?, ?)
ON CONFLICT (bucket, kind) DO UPDATE SET count = activity_log.count + EXCLUDED.count;
```

Sizing: with 10 s buckets and continuous activity, a kind costs 8,640 rows a day, about
3 M rows a year, 50 bytes each; a store that is quiet most of the time costs much less,
since empty buckets have no row. Widen the buckets (`TPL_ACTIVITY_BUCKET_SECONDS=60`) or
shorten the retention to trade precision for size.

## Reading it

Two endpoints on every service built on `service-templates-core`:

- `GET provapi/activity/summary.json`: per kind, the counts over the last minute, hour,
  day and week, the description, the last bucket seen. The service flushes its own
  counters before answering, so its figures are exact as of the call; another service's
  are at most one of its flush periods behind.
- `GET provapi/activity/series.json?window=minute|hour|day|week`: the counts per kind
  and per step (10 s, 1 min, 30 min, 6 h) over that window, for the chart.

Both are sums over whole buckets, so a window is aligned on the bucket width, not on the
instant of the call. The query is one range scan of the primary key over the last week.

The dashboard's tab (`forms.html#tab_activity`) calls both every *N* seconds while it is
on screen and the page visible (*N* from its field, default 10, or `?refresh=N`), never
overlapping two refreshes, and draws a table and a stacked bar chart per kind.

## Configuration

Environment variables or system properties, read once at start:

| variable                      | meaning | default |
|-------------------------------|---------|---------|
| `TPL_ACTIVITY_FLUSH_SECONDS`  | period of the flushing thread; `0` disables the logger | `10` |
| `TPL_ACTIVITY_BUCKET_SECONDS` | width of a bucket | `10` |
| `TPL_ACTIVITY_RETENTION_DAYS` | rows older than this are deleted; `0` never prunes | `400` |

Without a store (`jdbc.url` absent) the logger is `ActivityLogger.NOOP` and the
endpoints answer `enabled: false`.

## Counting something else (chronicle)

A service that extends `TemplateService` already has the logger, the tables and the
endpoints. To count its own kinds:

```java
// once, at start-up (the description reaches activity_kind at the next flush)
getActivityLogger().register("chronicle.events", "events ingested by chronicle");

// on the request path
getActivityLogger().count("chronicle.events");
getActivityLogger().count("chronicle.edges", edges.size());
```

Code without a handle on the service uses `ActivityLogger.shared()`, which the service
installs at start (and which is a no-op until then). A service that does not extend
`TemplateService` builds its own:

```java
ActivityLogger activity = ActivityLogger.fromEnvironment(
        new JdbcActivitySink(() -> openConnection(), jdbcUrl));   // starts the flushing thread
```

A service in another language only needs the DDL and the upsert above, with its own
buckets aligned on a multiple of 10 s; the dashboard shows whatever kinds the table holds.

## Code

- `activity/ActivityLogger.java`: the counters, the buckets, the flushing thread, the fold-back on outage.
- `activity/ActivitySink.java`, `activity/JdbcActivitySink.java`: persistence (DDL, upsert, describe, prune).
- `activity/ActivityQuery.java`: the summary and series queries and the windows.
- `TemplateService.java`: the three `ps.*` kinds, the two endpoints, `newActivityLogger` to override.
- `src/test/.../activity/ActivityLoggerTest.java`: bucketing, outage, descriptions, pruning, without a database.
- `src/test/.../activity/JdbcActivitySinkTest.java`: the DDL, the adding upsert, the summary and series queries and pruning against a real PostgreSQL; runs only with `-DTPL_ACTIVITY_TEST_JDBC_URL=jdbc:postgresql://host:5432/db?user=u&password=p`, skipped otherwise.
