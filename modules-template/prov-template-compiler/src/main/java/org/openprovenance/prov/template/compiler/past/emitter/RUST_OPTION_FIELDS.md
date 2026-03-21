# Optional Fields in the Rust Emitter

## The Core Rule

Every PAST `Field` carries an optional `initialiser` expression.  The Rust emitter uses that
single fact to decide how the field is typed in the generated struct:

| PAST field         | Rust struct field  |
|--------------------|--------------------|
| Has `initialiser`  | `T`                |
| No `initialiser`   | `Option<T>`        |

Fields **without** an initialiser are mutable, data-bearing slots that get filled in at
runtime (e.g. `transforming_inputs.path = Some(path.to_string())`).  Making them `Option<T>`
lets the struct be created with `new()` before all values are known and signals clearly which
slots are still vacant.

Fields **with** an initialiser represent constants or defaults that are known at construction
time.  They are emitted as plain `T` and set directly in the `Self { … }` literal.

---

## How the Rule Is Applied During Emission

### 1. Struct field declarations (`emit()`)

In `emit()`, each field's Rust type is chosen by checking `field.initialiser`:

```java
if (field.initialiser == null) {
    // Option<T> — no known initial value
    sb.append("Option<").append(convertTypeToRust(field.type)).append(">");
} else {
    // T — initialiser provides a concrete value
    sb.append(convertTypeToRust(field.type));
}
```

### 2. Constructor (`Self { … }` literal)

Fields without an initialiser default to `None` in `new()`.  Fields with an initialiser use
`convertWithType(field.initialiser, field.type)` to render the value.

### 3. Assignments in method bodies — `needsSomeWrap`

When a method body assigns to a non-self struct field (e.g.
`transforming_inputs.path = path`), the LHS is `Option<T>` (no initialiser), so the RHS must
be lifted into `Some(…)`.  The emitter detects this via `isNonSelfFieldAccess(lhs)` and sets
the `needsSomeWrap` flag:

```java
boolean needsSomeWrap = isNonSelfFieldAccess(assignment.leftHandExpression)
        && !expressionProducesOption(assignment.value);
if (needsSomeWrap) {
    rhs = "Some(" + rhs + ")";
}
```

`needsSomeWrap` is suppressed (`expressionProducesOption` returns `true`) in two cases:

* **Chained HashMap get** — `map.get(outer).get(inner)` already returns `Option<T>` after the
  emitter appends `.copied()`.
* **Cross-class field access that is itself `Option<T>`** — see section 4 below.

### 4. Cross-class field accesses — `isLocalFieldAccessProducingOption`

A workflow method may copy a field from one struct into another:

```rust
filtering_inputs.file = transforming_outputs.transformed_file;
```

Here `transforming_outputs.transformed_file` is already `Option<i32>` (the field has no
initialiser in `FileTransformingOutputs`).  If the emitter blindly applied `needsSomeWrap` it
would generate `Some(transforming_outputs.transformed_file)`, which is
`Option<Option<i32>>` — a type mismatch.

The fix is `isLocalFieldAccessProducingOption(Expression)`:

1. Confirms the expression is an `OBJECT_ACCESSOR` on a `LOCAL_VARIABLE` (not `self`).
2. Reads `objVar.inferredType` (populated by the PAST TypeInferrer) to obtain the class name
   of the local variable.
3. Looks that class up in the **`classRegistry`** (see section 5).
4. Finds the accessed field by name and returns `field.initialiser == null`.

When this returns `true`, `expressionProducesOption` returns `true`, and `needsSomeWrap` is
suppressed — the assignment is emitted without a `Some(…)` wrapper.

### 5. The `classRegistry` and `discoverClass`

`isLocalFieldAccessProducingOption` needs to inspect classes other than the one currently
being emitted.  The emitter maintains:

```java
private final Map<String, Class> classRegistry = new HashMap<>();
```

populated by the public method:

```java
public void discoverClass(Class clazz) {
    classRegistry.put(toPascalCase(clazz.name), clazz);
}
```

**Callers must invoke `discoverClass(clazz)` for every class in the compilation unit during
the same first pass that calls `discoverTraits(clazz)`.**  If a class is not registered, the
method falls back to returning `true` (assumes the field is `Option<T>`), which is correct for
all current workflow output structs (none of their data fields have initialisers).

---

## HashMap Arguments — `convertOptionArg` and `convertHashMapKeyArg`

When a local struct field is used as a HashMap key or value argument, its `Option<T>` must be
unwrapped to `T` (or `&T` for keys):

```java
// key: &T
private String convertHashMapKeyArg(Expression arg) {
    return "&" + convertOptionArg(arg);
}

// value: T
private String convertOptionArg(Expression arg) {
    if (isLocalFieldAccessProducingOption(arg)) {
        return convert(arg) + ".unwrap()";
    }
    return convert(arg);
}
```

`convertOptionArg` uses `isLocalFieldAccessProducingOption` (not the raw
`isLocalFieldAccess`) so that **initialized fields** — which are plain `T` — are passed as-is
without a spurious `.unwrap()`.

---

## Summary of Helper Methods

| Method | Purpose |
|--------|---------|
| `isNonSelfFieldAccess(expr)` | LHS is a non-self struct field → needs `Some()` wrap |
| `expressionProducesOption(expr)` | RHS already yields `Option<T>` → suppress `Some()` wrap |
| `isLocalFieldAccess(expr)` | Structural check: OBJECT_ACCESSOR on LOCAL_VARIABLE ≠ self |
| `isLocalFieldAccessProducingOption(expr)` | Principled check: field has no initialiser → `Option<T>` |
| `convertOptionArg(arg)` | Unwrap `Option<T>` → `T` for HashMap values (only when needed) |
| `convertHashMapKeyArg(arg)` | Borrow + unwrap `Option<T>` → `&T` for HashMap keys |

---

## Decision Flow for an Assignment RHS

```
assignment: lhs = rhs

isNonSelfFieldAccess(lhs)?
  NO  → emit as-is (self fields are not Option-wrapped at assignment time)
  YES → expressionProducesOption(rhs)?
          YES → emit:  lhs = rhs          (rhs is already Option<T>)
          NO  → emit:  lhs = Some(rhs)    (lift T into Option<T>)
                  + append .to_string() if rhs is &str and lhs needs String
```
