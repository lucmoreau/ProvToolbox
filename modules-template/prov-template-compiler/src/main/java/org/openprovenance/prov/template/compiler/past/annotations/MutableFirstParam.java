package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * Rust-only annotation that changes how the first non-self parameter of a trait
 * method is emitted.
 *
 * <p>Without this annotation the emitter applies {@code convertTypeToRustTraitParam},
 * which turns a bean type {@code T} into {@code &T} (shared borrow).  That is correct
 * for read-only observer methods, but breaks for the BeanMerger pattern where the
 * method receives a bean, writes input/output fields into it, and returns the updated
 * bean.  In that pattern:
 *
 * <ul>
 *   <li>The first parameter must be <em>owned and mutable</em> ({@code mut T}), so
 *       that fields can be written directly.</li>
 *   <li>Subsequent parameters remain {@code &T} (read-only borrows).</li>
 *   <li>The return type is the same {@code T}, which is simply the modified first
 *       parameter — no {@code .clone()} or lifetime annotation is needed.</li>
 * </ul>
 *
 * <p>Applying this annotation also causes the emitter to append {@code .clone()} when
 * reading a field from any {@code &T} parameter (parameters 1..n), because moving a
 * field out of a shared reference requires cloning for non-Copy types.
 *
 * <p>Example — without annotation (broken):
 * <pre>{@code
 * fn process_file_init_bean_file_init_inputs(&self, bean: &FileInitBean, input_bean: &FileInitInputs) -> FileInitBean {
 *     bean.time = input_bean.time;   // E0594: cannot assign through &FileInitBean
 *     bean                           // E0308: &FileInitBean ≠ FileInitBean
 * }
 * }</pre>
 *
 * <p>Example — with annotation (correct):
 * <pre>{@code
 * fn process_file_init_bean_file_init_inputs(&self, mut bean: FileInitBean, input_bean: &FileInitInputs) -> FileInitBean {
 *     bean.time = input_bean.time.clone();
 *     bean
 * }
 * }</pre>
 *
 * @see MutableReceiver  for the analogous annotation that makes the <em>self</em>
 *                        receiver {@code &mut self} instead of {@code &self}.
 */
public class MutableFirstParam extends RustAnnotation {

    public static final String NAME = "rust:@mutablefirstparam";

    public MutableFirstParam() {
        super(NAME);
    }
}
