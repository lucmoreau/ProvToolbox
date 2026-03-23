# Rust Emitter — Mutable Receivers

This document covers how `Rust.java` handles methods that mutate `self`,
focusing on the `MutableReceiver` annotation, the generated `&mut self`
receiver, and all downstream consequences in generated code.  Read
`RUST_OWNERSHIP.md` first for the general ownership model.

---

## 1. The Problem

Java methods implicitly mutate `this` whenever they write to an instance
field.  In Rust, writing to a field requires the receiver to be `&mut self`.
The PAST emitter already detects some mutation patterns automatically
(e.g. `self.items.push(…)`) and emits `&mut self`.  However, this
auto-detection cannot cover every case.  The `MutableReceiver` annotation
is the explicit escape hatch.

The primary use-case is the generated `BeanMerger` class.  Its methods:

- accept an input or output bean,
- copy fields from that bean into `self.bean__*` (an `Option<T>` field), and
- return the accumulated result.

Each of these operations mutates `self`, yet the mutation may be too indirect
for the auto-detector to catch reliably.

---

## 2. The `MutableReceiver` Annotation

**Package:** `org.openprovenance.prov.template.compiler.past.annotations`

```java
public class MutableReceiver extends RustAnnotation {
    public static final String NAME = "rust:@mutablereceiver";
}
```

It is a `RustAnnotation` subclass, meaning it is silently ignored by all
non-Rust emitters (Java, Python, JavaScript).

### Applying the annotation in PAST builders

```java
Method m = METHOD("processBean")
        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
        .ANNOTATIONS(MutableReceiver.NAME)   // <-- opt-in to &mut self
        .RETURNS(commonBeanClass);
```

`Method.ANNOTATIONS(String... names)` calls
`AnnotationConverter.toAnnotation(name)` for each name.  The converter must
have `MutableReceiver` registered in its map; see §6 for the registration
requirement.

---

## 3. Receiver Emission: `&mut self`

`Rust.java` has two predicates that together drive the receiver:

| Predicate | Detects | Emits |
|-----------|---------|-------|
| `modifiesSelf(method)` | Body contains a push/add onto a self List field, or method has `StatefulProcessor` annotation | `&mut self` |
| `consumesSelf(method)` | Method has `MutableReceiver` annotation | `&mut self` |

In `emitMethod`, the receiver is:

```java
if (!method.modifiers.contains(Modifier.STATIC)) {
    sb.append("&");
    if (consumesSelf(method) || modifiesSelf(method)) {
        sb.append("mut ");
    }
    sb.append("self");
    …
}
```

`currentMethodConsumesSelf` is set to the result of `consumesSelf(method)`
and kept for the duration of the method body — it gates several downstream
decisions described below.

### Why `&mut self`, not `mut self` (consuming)?

A consuming receiver (`mut self`) would own `self` for the call and prevent
calling the method again in a loop:

```rust
// This would fail to compile — self is moved on the first iteration:
for composee in output_bean.elements.iter().cloned() {
    self.merge_into_packing_bean(PackingBean::new());  // E0382: use of moved value
    self.process_packing_outputs(composee);
}
```

`&mut self` avoids the move while still allowing field mutation.  The caller
always retains ownership of `self`.

---

## 4. Return Value

### 4a. Returning `self`

A method with `&mut self` cannot simply return `self` — that would move out
of the mutable borrow, giving `&BeanMerger` where `BeanMerger` is expected
(E0308).  The fix is to return `self.clone()`:

```rust
pub fn merge_into_packing_bean(&mut self, bean: PackingBean) -> BeanMerger {
    self.bean__packing_bean = Some(bean);
    self.clone()       // <— emitter appends .clone() automatically
}
```

In `emitStatement` RETURN case, when `currentMethodConsumesSelf` is true and
`isSelfVariableReturn(ret.expression)` is true (i.e. the expression is the
`this`/`self` variable), the emitter appends `.clone()`:

```java
if (currentMethodConsumesSelf && isSelfVariableReturn(ret.expression)) {
    retExpr += ".clone()";
}
```

### 4b. Returning a self `Option<T>` field

When the return expression is a self field access (e.g. `this.myBean`) and
the field is `Option<T>` (no initialiser), the raw `self.my_bean` would be
`Option<T>` while the declared return type is `T`.  The emitter appends
`.clone().unwrap()`:

```rust
pub fn process_packing_inputs(&mut self, input_bean: PackingInputs) -> PackingBean {
    …
    self.bean__packing_bean.clone().unwrap()   // <— .clone().unwrap() appended
}
```

The check uses `isSelfOptionFieldAccess(ret.expression)`, which returns true
when the expression is an OBJECT_ACCESSOR on `this`/`self` for a field with
no initialiser (looked up via `isOptionField`).

`.clone()` is needed because `&mut self` does not allow moving a field out
of the struct — we must clone first to get an owned copy, then `.unwrap()`
to strip the `Option`.

---

## 5. Writing to a Nested `Option<T>` Field (LHS Assignment)

### Problem

Copying a field from an input bean into a self Option field's sub-field:

```java
// PAST
ASSIGNMENT(
    METHOD_CALL(METHOD_CALL(VARIABLE("this"), "myBean"), "time"),
    METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), "time"))
```

The LHS is `this.myBean.time`, a two-level accessor.  In Rust, `self.my_bean`
is `Option<MyBean>`, so `self.my_bean.time` does not compile (E0609):
`Option<MyBean>` has no field `time`.

### Fix — `convertLH`

`convertLH` handles left-hand-side conversion.  When it encounters a
two-level OBJECT_ACCESSOR chain where the intermediate field is an
`Option<T>` self-field, it inserts `.as_mut().unwrap()`:

```
OBJECT_ACCESSOR(OBJECT_ACCESSOR(this, "myBean"), "time")
  ↓ convertLH
self.my_bean.as_mut().unwrap().time
```

Detection:

```java
if (mc.operatorKind == OBJECT_ACCESSOR && mc.object instanceof MethodCall) {
    MethodCall inner = (MethodCall) mc.object;
    if (inner.operatorKind == OBJECT_ACCESSOR && inner.object instanceof Variable) {
        Variable v = (Variable) inner.object;
        if (("this".equals(v.name) || "self".equals(v.name)) && isOptionField(inner.methodName)) {
            return convert(inner) + ".as_mut().unwrap()." + sanitizeName(toSnakeCase(mc.methodName));
        }
    }
}
```

`.as_mut()` converts `&mut Option<T>` to `Option<&mut T>`, avoiding a move.
`.unwrap()` yields `&mut T`, whose fields are then directly assignable.

---

## 6. Self `Option<T>` Field as a By-Value Argument

### Problem

When a self `Option<T>` field is passed as a by-value argument (e.g. to
`add_elements`), the argument type is `Option<T>` while the method expects
`T` (E0308).

### Fix — `convertCallArg`

`convertCallArg` is invoked for every `PASS_BY_VALUE` argument.  It now
checks `isSelfOptionFieldAccess(arg)` and, if true, appends `.clone().unwrap()`:

```java
if (isSelfOptionFieldAccess(arg)) {
    return convert(arg) + ".clone().unwrap()";
}
```

Generated:

```rust
self.bean__packing_composite_bean.as_mut().unwrap()
    .add_elements(self.bean__packing_bean.clone().unwrap());
//                                        ^^^^^^^^^^^^^^^ unwrap the Option
```

`.clone()` is mandatory because the self borrow is still active — moving a
field out of `&mut self` is not allowed.

---

## 7. Vec Index Access: `__elements.get(i)` → `[i as usize].clone()`

This problem arises in the composite BeanMerger's output-processing loop,
where `elements.get(count)` is used to retrieve the i-th element of a Vec.

### Problem

`Vec::get(usize)` returns `Option<&T>`.  The ASSIGNMENT handler wraps the
RHS in `Some(…)` to produce the `Option<T>` expected by the LHS field,
giving `Some(Option<&T>)` — a type mismatch (E0277/E0308).  Additionally,
the argument type mismatch (`i32` vs `usize`) causes E0308.

### Fix — Vec index early return in `OBJECT_METHOD_CALL`

When the outer method is `get`, the argument is a single integer, and the
receiver is an OBJECT_ACCESSOR for the PAST field named `__elements`, the
emitter uses index notation instead:

```java
if ("get".equals(mc.methodName) && mc.arguments.size() == 1
        && mc.object instanceof MethodCall
        && ((MethodCall) mc.object).operatorKind == OBJECT_ACCESSOR
        && "__elements".equals(((MethodCall) mc.object).methodName)) {
    result.append(convertedObject)
          .append("[").append(convert(mc.arguments.get(0))).append(" as usize].clone()");
    return result.toString();
}
```

Generated:

```rust
// Before fix:
self.bean__packing_bean = Some(self.bean__packing_composite_bean
    .as_ref().unwrap().elements.get(&count));   // Option<&T>, type mismatch

// After fix:
self.bean__packing_bean = Some(self.bean__packing_composite_bean
    .as_ref().unwrap().elements[count as usize].clone());  // T, correct
```

Index notation (`vec[i]`) returns `T` directly (panicking if out of bounds,
which is acceptable in generated code where the sizes are guaranteed).
`.clone()` produces an owned `T` without moving the element out of the Vec,
which would invalidate the Vec.

### No spurious `.unwrap()` on field access after Vec index

The OBJECT_ACCESSOR case adds `.unwrap()` after any `get`-named MethodCall
on the assumption that `get` returns `Option<&T>`.  After the Vec index fix,
the converted string already ends with `.clone()` (returning `T`), so no
`.unwrap()` is needed.

The helper `isVecIndexAccessGet(MethodCall mc)` identifies this pattern:

```java
private boolean isVecIndexAccessGet(MethodCall mc) {
    return "get".equals(mc.methodName)
        && mc.arguments != null && mc.arguments.size() == 1
        && mc.object instanceof MethodCall
        && ((MethodCall) mc.object).operatorKind == OBJECT_ACCESSOR
        && "__elements".equals(((MethodCall) mc.object).methodName);
}
```

In the OBJECT_ACCESSOR case the guard is:

```java
if ("get".equals(mc2.methodName) && !isVecIndexAccessGet(mc2)) {
    inner += ".unwrap()";   // HashMap::get — still needs .unwrap()
}
```

---

## 8. OBJECT_METHOD_CALL Method Name Snake-Casing

The OPERATOR_VARIABLE path applied `toSnakeCase` to method names, but the
OBJECT_METHOD_CALL path did not.  This caused camelCase names such as
`addElements` to appear verbatim in generated Rust (E0599).

Both emission points in OBJECT_METHOD_CALL now use `toSnakeCase(callMethodName)`:

```java
result.append(convertedObject).append(".")
      .append(toSnakeCase(callMethodName)).append("(");
```

This applies to the main emission line and to the chained-get early-return
path.

---

## 9. Registration Requirement for `MutableReceiver`

`AnnotationConverter` maintains a static map from annotation name string to
`PastAnnotation` instance.  **`MutableReceiver` must be registered there**,
otherwise `Method.ANNOTATIONS(MutableReceiver.NAME)` silently stores `null`
and the annotation is never applied:

```java
// AnnotationConverter.java
map.put(MutableReceiver.NAME, new MutableReceiver());
```

If you add a new `RustAnnotation` subclass and it is not being applied,
check `AnnotationConverter` first.

---

## 10. How It All Fits Together — BeanMerger Example

The following shows the full chain for the simple-template `merge_into` /
`process` method pair:

```
PAST (CompilerBeanMerger.java)
──────────────────────────────
METHOD("mergeIntoPackingBean")
  .ANNOTATIONS(MutableReceiver.NAME)   → &mut self
  .RETURNS(mergerClass)
  .BODY(
    ASSIGNMENT(METHOD_CALL(this, beanLocalVar),          // this.bean__packing_bean = Some(bean)
               VARIABLE(BEAN_VAR)),
    RETURN(VARIABLE("this")))                             // self.clone()

METHOD("processPackingInputs")
  .ANNOTATIONS(MutableReceiver.NAME)   → &mut self
  .RETURNS(commonBeanClass)
  .BODY(
    ASSIGNMENT(                                           // this.bean__packing_bean.item = input_bean.item
      METHOD_CALL(METHOD_CALL(this, beanLocalVar), "item"),         // LHS: as_mut().unwrap()
      METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), "item")),
    RETURN(METHOD_CALL(this, beanLocalVar)))              // self.bean__packing_bean.clone().unwrap()
```

Generated Rust:

```rust
pub fn merge_into_packing_bean(&mut self, bean: PackingBean) -> BeanMerger {
    self.bean__packing_bean = Some(bean);
    self.clone()
}

pub fn process_packing_inputs(&mut self, input_bean: PackingInputs) -> PackingBean {
    self.bean__packing_bean.as_mut().unwrap().item = input_bean.item;
    self.bean__packing_bean.clone().unwrap()
}
```

---

## 11. Summary of Emitter Decision Points

| Situation | Detection | Generated Rust |
|-----------|-----------|---------------|
| Method has `MutableReceiver` annotation | `consumesSelf(method)` | `&mut self` receiver |
| `return this` in `MutableReceiver` method | `isSelfVariableReturn` | `self.clone()` |
| `return this.field` (Option, no init) in `MutableReceiver` method | `isSelfOptionFieldAccess` | `self.field.clone().unwrap()` |
| LHS is `this.optField.subField` | Two-level OBJECT_ACCESSOR in `convertLH`, `isOptionField` | `self.opt_field.as_mut().unwrap().sub_field` |
| Arg is `this.optField` (Option, no init) passed by value | `isSelfOptionFieldAccess` in `convertCallArg` | `self.opt_field.clone().unwrap()` |
| `__elements.get(i)` via OBJECT_METHOD_CALL | `isVecIndexAccessGet` early return | `elements[i as usize].clone()` |
| Field access on `__elements.get(i)` result | `isVecIndexAccessGet` guard in OBJECT_ACCESSOR | no spurious `.unwrap()` |
| camelCase method name in OBJECT_METHOD_CALL | `toSnakeCase(callMethodName)` at emission | `add_elements(…)` |

---

## 12. Helper Method Reference

| Method | File | Purpose |
|--------|------|---------|
| `consumesSelf(Method)` | `Rust.java` | True when method has `MutableReceiver` annotation |
| `modifiesSelf(Method)` | `Rust.java` | True when body mutates a self List field or has `StatefulProcessor` |
| `isSelfVariableReturn(Expression)` | `Rust.java` | True when expression is the `this`/`self` variable |
| `isSelfOptionFieldAccess(Expression)` | `Rust.java` | True when expr is `this.field` for a field with no initialiser |
| `isOptionField(String fieldName)` | `Rust.java` | True when named field in `currentClass` has `initialiser == null` |
| `isVecIndexAccessGet(MethodCall)` | `Rust.java` | True when `mc` is `__elements.get(i)` pattern |
| `convertLH(Expression)` | `Rust.java` | LHS conversion; inserts `.as_mut().unwrap()` for two-level Option chain |
| `convertCallArg(Expression)` | `Rust.java` | By-value argument conversion; appends `.clone().unwrap()` for self Option fields |
