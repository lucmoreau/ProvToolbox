


# ProvToolbox TODO

- T25
- T24
- T23
- T22
- T21
- T20 Streaming progress for `/templates/viz` (and other long-running endpoints)

  The viz request currently runs three stages sequentially behind a single
  synchronous `POST /provapi/templates/viz` returning `image/svg+xml`:
  1. **SQL traversal** — `recursiveTraversal(...)` in `TemplateQuery.generateViz`,
     i.e. `backwardtraversal_star_typed`.
  2. **PROV-document construction** — currently bundled inside
     `TemplatesToDot.convert(...)`.
  3. **Graphviz rendering** — also inside `TemplatesToDot.convert(...)`, writes
     SVG to the response `OutputStream`.

  The browser today (`forms.html:submitNavigate`) shows a single
  "Loading…" spinner for the entire span, which is uninformative when graphs
  push into multi-second renders.

  **Server-side outline**:
  - Introduce a `ProgressListener` interface in `service-templates-core` with a
    minimal vocabulary: `started(stage)`, `done(stage, durationMs)`,
    `failed(stage, throwable)`, `detail(stage, message)`. Default no-op
    implementation so existing callers compile unchanged.
  - Split `TemplatesToDot.convert(...)` so PROV-document construction and
    Graphviz invocation are observable as separate stages (either two methods
    or one method that takes the listener and brackets each phase).
  - Thread the listener through `TemplateService.getTemplatesViz` →
    `TemplateLogic.generateViz` → `TemplateQuery.generateViz` →
    `TemplatesToDot`.
  - Add a sibling endpoint at `POST /templates/viz/stream` returning
    `text/event-stream`. Use JAX-RS `SseEventSink` + `Sse`, suspend the
    response, run the work on a managed executor, and have the
    `SseProgressListener` translate listener callbacks into events. Emit the
    final SVG as a `result` event (string-escaped JSON, or base64). Keep the
    existing synchronous endpoint as-is for backwards compatibility.
  - Operational hygiene: `Cache-Control: no-store`, `X-Accel-Buffering: no`,
    per-request timeout, cancel the in-flight task on client disconnect.

  **Client-side outline** (in `forms.html`):
  - Replace the `XMLHttpRequest` in `submitNavigate` with `fetch` +
    `ReadableStream` (POST keeps the JSON config) — or `EventSource` if the
    config is moved to a query string.
  - Replace the single spinner with a three-row stage panel inside
    `#navigator_div` that flips each row from spinner → tick + duration on
    each `stage:done` event; show an error indicator on `stage:error`.
  - On the `result` event, swap the panel for the SVG and call the existing
    `rewriteImageHrefs`, `wireNavigatorNodeEvents`, `updateMinimap`, and
    enable `navigator_save` — exactly as `xhr.onload` does today.
  - Track and abort the active stream when a new `submitNavigate` click
    arrives, so stale events don't paint over a fresh request.
  - Fall back to the legacy endpoint when the streaming one is unavailable.

  **Phased rollout**:
  1. Add `ProgressListener` and the `TemplatesToDot` split, with a
     `LoggingProgressListener` so per-stage timings appear in the server log.
     No user-visible change.
  2. Add the SSE endpoint; keep the synchronous one.
  3. Client-side stage panel and stream consumption.
  4. Once stable, retire (or document as fallback) the synchronous endpoint.

  **Reuse**: the `ProgressListener` and the SSE transport are deliberately
  generic. Every long-running endpoint (document conversion, template
  expansion, bulk import, large search, export pipelines, anything currently
  fronted by Quartz) can adopt the same pattern by accepting an optional
  listener and exposing a `/stream` variant. Detail follows.

  ### `ProgressListener` — proposed methods

  Keep the interface small. Five callbacks cover the cases this codebase
  needs; everything except `started`/`done` is defaulted to a no-op so
  implementations pick only what they care about.

  | Method | When | Payload |
  |---|---|---|
  | `started(stage)` | Stage begins | stage name (e.g. `viz.sql`, `viz.prov-build`, `viz.render`) |
  | `done(stage, durationMs)` | Stage completes normally | total elapsed time |
  | `failed(stage, throwable)` | Stage threw | error class + message; stream closes after |
  | `detail(stage, message)` | Optional sub-event during a stage | free-form, e.g. `"SQL returned 1 079 rows"`, `"PROV doc has 314 statements"`, `"SVG: 184 KB"` |
  | `progress(stage, fraction)` | Optional, only for stages that can self-report a percentage | 0.0 – 1.0 |

  Consider adding `isCancelled()` so cooperative cancellation flows through
  the same channel — natural fit, since the SSE transport already observes
  client disconnect.

  Stage names should be **hierarchical** strings (`viz.sql`, `viz.prov-build`,
  `viz.render`, or `import.parse`, `import.validate`, `import.store`). That
  gives clients a stable pattern to render against without baking
  endpoint-specific logic into the UI.

  ### Reusability across the server

  The listener interface and the SSE transport live in the core service
  module; the business logic in any module just accepts an optional listener.
  Natural adopters:

  - **`/templates/viz`** — this task (three stages).
  - **Document conversion / serialization** (`/documents/...`) — accept-driven
    format conversion can take seconds for large bundles.
  - **Template expansion** (CSV → PROV) — parse, expand per row, serialize.
  - **Bulk record import** — parse, validate, insert, index; per-row
    sub-progress via `detail`.
  - **Large search / `/templates/records`** — row-count reporting via `detail`.
  - **Anything fronted by Quartz today** (`JobDeleteDocumentResource`, etc.)
    — currently invisible to clients; the same listener can drive both
    SSE-to-browser and metrics-to-Prometheus.
  - **Export pipelines** — zip, JSON-LD bundling, CSV dump.

  Three things make broad rollout practical:

  1. **One interface, several adapters.** Provide a small zoo of standard
     implementations and let the endpoint pick at request time:
     - `NoOpProgressListener` — production default for non-streaming callers.
     - `LoggingProgressListener` — drops into existing log4j config; useful
       for diagnosing slow endpoints with zero client work.
     - `SseProgressListener` — wraps `SseEventSink`, the focus of this task.
     - `MetricsProgressListener` — feeds Micrometer/Prometheus
       `stage.duration` histograms partitioned by stage name.
     - `CompositeProgressListener` — fan-out: SSE *and* metrics *and* log
       simultaneously.
  2. **Conventions, not framework.** A `Stages` constants file or per-feature
     enum (`VizStages.SQL`, `VizStages.PROV_BUILD`, `VizStages.RENDER`)
     keeps stage names typo-free and discoverable without forcing every
     consumer to depend on an enum.
  3. **Endpoint pairing.** Keep the existing synchronous endpoint and add a
     `/stream` sibling. The two share the same business method; only the
     listener changes. Old clients keep working, new clients opt in.

  Bottom line: the surface area the rest of the server has to absorb is one
  interface and one extra method parameter. The transport layer (SSE today,
  WebSocket or progress polling later if ever wanted) is fully isolated
  behind the listener — adding a new transport doesn't touch business logic,
  and adding a new endpoint to the system gets staged progress and metrics
  for free.

- T18 add creation date to record_index table
- T17 Update the viz so that overlay template is near the template it overlays
- T16 Have a place for template explanations to be displayed when hovering over a template
- T15 backward_traversal*: to return the semantic type of template, and to add it in the viz, and select icon accordingly: use semanticType map in TemplateQuery
- T10 explanation for book templates
- T11 clean up Makefile in archetype
- T12 archetype to start a template library
- T13 Docker container for provenance service


-  T2 template-compiler:
    Revisit TableConfigurator so that generic type is per method, rather than class level, allowing unnecessary casts.
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

- T3 script.sh in prov-template-compiler needs to point to the proper library

- T4 should not insert delegation if delegate/responsible empty
-INSERT INTO
__PROV_DELEGATION (delegate, delegate_rel, responsible, responsible_rel, activity, activity_rel, template, template_id, rel) VALUES
(,'agent',,'agent',1517,'activity','plead_filtering',152,'--0--');

- T5 PC1FullTest.java in prov-dot no longer working

- T6 upgrade scala version

- T7 templateIT fails when service is usign mongo.

- T8 DeleteJob/Redis issue
        
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

# Done
* T1 template-compiler: 
updated CompilerCommon to use the new code generation pattern for the Common Bean, and removed the old code.  

* T14 template expansion with association without agent but with plan, does not include association.

- T9 rust code generation:
    ```angular2html
       --> src/org/openprovenance/templates/catalogue/transport/integrator/bean_completer2.rs:510:42
        |
    510 |             bean.count = Some(bean.count + 1);
        |                               ---------- ^ - {integer}
        |                               |
        |                               std::option::Option<i32>
        |
    note: the foreign item type `std::option::Option<i32>` doesn't implement `Add<{integer}>`
       --> /Users/luc/.rustup/toolchains/stable-aarch64-apple-darwin/lib/rustlib/src/rust/library/core/src/option.rs:599:1
        |
    
    ```
  - T19 update navigation display to visualise atype icons, and fallback on template icons if atype icons not available
