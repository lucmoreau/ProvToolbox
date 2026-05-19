package org.openprovenance.prov.service.core.progress;

/**
 * {@link ProgressListener} that discards every event.
 *
 * <p>Used as the safe default by every call site that takes a listener.  Hold
 * the singleton in {@link #INSTANCE} rather than allocating fresh instances.
 */
public final class NoOpProgressListener implements ProgressListener {

    public static final ProgressListener INSTANCE = new NoOpProgressListener();

    private NoOpProgressListener() { }

    @Override public void started(String stage)                { }
    @Override public void done(String stage, long durationMs)  { }
}
