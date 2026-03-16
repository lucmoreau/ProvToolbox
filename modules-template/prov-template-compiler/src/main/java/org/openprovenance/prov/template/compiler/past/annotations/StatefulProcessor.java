package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * Rust annotation to mark a method as requiring mutable self (&mut self).
 *
 * By default, Rust methods are generated with immutable self (&self) for stateless processors.
 * Use this annotation when the method needs to mutate internal state.
 *
 * Usage in PAST code:
 * <pre>
 * method.annotation.add(new StatefulProcessor());
 * </pre>
 *
 * This will generate:
 * <pre>
 * fn process(&mut self, ...) -> T  // instead of &self
 * </pre>
 */
public class StatefulProcessor extends RustAnnotation {
    public static final String NAME = "StatefulProcessor";

    public StatefulProcessor() {
        super(NAME);
    }
}
