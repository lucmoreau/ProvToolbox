package org.openprovenance.prov.service.core.progress.sse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.sse.Sse;
import jakarta.ws.rs.sse.SseEventSink;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openprovenance.prov.service.core.progress.ProgressListener;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * {@link ProgressListener} that turns each callback into a Server-Sent Event.
 *
 * <p>Events use two names:
 * <ul>
 *   <li>{@code stage} — per-stage lifecycle ({@code started}, {@code done},
 *       {@code failed}, {@code detail}, {@code progress}).  Payload is a JSON
 *       object with at least a {@code stage} and {@code status} field.</li>
 *   <li>{@code result} — emitted by {@link #result(String, byte[])} once the
 *       work has finished; carries the binary artefact (e.g. SVG) as a
 *       base64 payload alongside the originating media type.</li>
 *   <li>{@code error} — emitted by {@link #error(Throwable)} when the work
 *       throws.  Distinct from a {@code stage}/{@code failed} event because
 *       it terminates the stream; clients should treat it as fatal.</li>
 * </ul>
 *
 * <p>The listener never closes the {@link SseEventSink}; that is the
 * responsibility of the endpoint that owns it.  Calls that arrive after the
 * sink has been closed are silently dropped, so a disconnected client cannot
 * cause the in-flight work to throw via this channel.
 */
public final class SseProgressListener implements ProgressListener {

    private static final Logger logger = LogManager.getLogger(SseProgressListener.class);

    private final SseEventSink sink;
    private final Sse sse;
    private final ObjectMapper om;

    public SseProgressListener(SseEventSink sink, Sse sse) {
        this(sink, sse, new ObjectMapper());
    }

    public SseProgressListener(SseEventSink sink, Sse sse, ObjectMapper om) {
        this.sink = sink;
        this.sse  = sse;
        this.om   = om;
    }

    // ── ProgressListener ─────────────────────────────────────────────────

    @Override public void started(String stage)                { sendStage(stage, "started",  null, null); }
    @Override public void done(String stage, long durationMs)  { sendStage(stage, "done",     "durationMs", durationMs); }
    @Override public void failed(String stage, Throwable error){ sendStage(stage, "failed",   "error", error.toString()); }
    @Override public void detail(String stage, String message) { sendStage(stage, "detail",   "message", message); }
    @Override public void progress(String stage, double fraction) { sendStage(stage, "progress", "fraction", fraction); }

    // ── Terminal events ──────────────────────────────────────────────────

    /**
     * Send the final result of the work as a {@code result} event.
     * The byte payload is base64-encoded to survive JSON transport.
     */
    public void result(String mediaType, byte[] payload) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("mediaType", mediaType);
        data.put("base64",    Base64.getEncoder().encodeToString(payload));
        send("result", data);
    }

    /**
     * Send a terminal {@code error} event.  Use when the work itself threw —
     * a per-stage {@code failed} has already been emitted by the bracketing
     * code, but the client also needs a stream-level signal to give up.
     */
    public void error(Throwable error) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("error", error.toString());
        send("error", data);
    }

    // ── Internals ────────────────────────────────────────────────────────

    private void sendStage(String stage, String status, String extraKey, Object extraVal) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("stage",  stage);
        data.put("status", status);
        if (extraKey != null) data.put(extraKey, extraVal);
        send("stage", data);
    }

    private void send(String eventName, Map<String, Object> payload) {
        if (sink.isClosed()) return;
        try {
            String json = om.writeValueAsString(payload);
            // Block until the event is flushed.  SseEventSink.send() is
            // asynchronous and returns a CompletionStage; if we don't await
            // it, a fast-following sink.close() in the endpoint's `finally`
            // can race ahead of the actual write and discard queued events.
            sink.send(sse.newEventBuilder().name(eventName).data(json).build())
                .toCompletableFuture().join();
        } catch (JsonProcessingException e) {
            // Should not happen for a Map of primitives/Strings, but keep
            // the work thread alive even if it does.
            logger.warn("SseProgressListener: failed to serialise {} event: {}", eventName, e.toString());
        } catch (RuntimeException e) {
            // Most likely the sink was closed between isClosed() and send().
            // Promote to WARN so silent drops are visible if they recur.
            logger.warn("SseProgressListener: send dropped for {} event: {}", eventName, e.toString());
        }
    }
}
