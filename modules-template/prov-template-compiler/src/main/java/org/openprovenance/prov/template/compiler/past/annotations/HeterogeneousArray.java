package org.openprovenance.prov.template.compiler.past.annotations;

/**
 * Annotation for heterogeneous (mixed-type) array handling in Rust.
 *
 * <h2>Usage 1: Array Initializers</h2>
 * Mark array initializers that contain heterogeneous types.
 * Generates {@code Vec<Value>} with wrapped elements.
 *
 * <pre>
 * ArrayInitialiser array = new ArrayInitialiser(
 *     new Constant("hello"),
 *     new Constant(42),
 *     new Constant(true)
 * ).ANNOTATION(new HeterogeneousArray());
 * </pre>
 *
 * Generated Rust:
 * <pre>
 * vec![
 *     Value::String("hello".to_string()),
 *     Value::Int(42),
 *     Value::Bool(true),
 * ]
 * </pre>
 *
 * <h2>Usage 2: Array Accessors</h2>
 * Mark array accessors with expected type for automatic unwrapping.
 * Generates {@code .as_type()} call returning {@code Option<T>}.
 *
 * <pre>
 * ArrayAccessor accessor = new ArrayAccessor(array, index)
 *     .ANNOTATION(new HeterogeneousArray("String"));
 * </pre>
 *
 * Generated Rust:
 * <pre>
 * array[index].as_string()  // Returns Option&lt;&amp;String&gt;
 * </pre>
 *
 * @see RustAnnotation
 */
public class HeterogeneousArray extends RustAnnotation {
    public static final String NAME = "HeterogeneousArray";

    private String expectedType;  // "String", "Int", "Float", "Bool" for array accessors

    /**
     * Constructor for array initializers (no expected type)
     */
    public HeterogeneousArray() {
        super(NAME);
        this.expectedType = null;
    }

    /**
     * Constructor for array accessors with expected type
     * @param expectedType The expected type: "String", "Int", "Float", "Bool"
     */
    public HeterogeneousArray(String expectedType) {
        super(NAME);
        this.expectedType = expectedType;
    }

    /**
     * Get the expected type for array accessor unwrapping
     * @return Expected type name, or null if not specified
     */
    public String getExpectedType() {
        return expectedType;
    }
}
