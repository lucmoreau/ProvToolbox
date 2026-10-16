package org.openprovenance.prov.service.core.readers;

/**
 * Request body of the template <em>slicer</em>: the two anchors delimiting a slice.
 *
 * <p>The inherited {@link TemplatesVizConfig} fields carry the <b>upstream</b> anchor —
 * {@code id}, {@code template}, and an <em>input</em> variable in {@code property} (the
 * seed shape of a forward search) — together with the shared {@code style} and
 * {@code parameters}.  The fields declared here carry the <b>downstream</b> anchor, whose
 * {@code downstreamProperty} is an <em>output</em> variable (the seed shape of a backward
 * search).
 *
 * <p>The slice is the set of template connections lying on a path from the downstream
 * output back to the upstream input.
 *
 * <p>Both {@code property} and {@code downstreamProperty} are optional: left unset, the
 * search starts from every variable of that anchor's template — which is what a source
 * template, whose entities have no predecessor and hence no input to name, requires.
 *
 * <p>Beyond the keys {@link TemplatesVizConfig} already recognises, {@code parameters}
 * accepts {@code paths}: {@code "one"} narrows the result to the shortest single path
 * between the anchors, while {@code "all"} (the default) returns the whole slice.
 */
public class TemplatesSliceConfig extends TemplatesVizConfig {
    public Integer downstreamId;
    public String downstreamTemplate;
    public String downstreamProperty;
}
