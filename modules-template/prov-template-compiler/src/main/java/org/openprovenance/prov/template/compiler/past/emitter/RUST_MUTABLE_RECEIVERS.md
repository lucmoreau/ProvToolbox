# Rust Emitter — Mutable Receivers and Mutable First Parameters

This document covers how `Rust.java` handles methods that mutate `self` or
that receive and return a mutable reference to a bean parameter.  Two
orthogonal annotation-driven mechanisms are described:

| Annotation | Mutates | Receiver | First param |
|-----------|---------|----------|-------------|
| `MutableReceiver` | `self` fields | `&mut self` | unchanged |
| `MutableFirstParam` | bean parameter | `&self` | `&'a mut T` |

Read `RUST_OWNERSHIP.md` first for the general ownership model.

---

## Part A — `MutableReceiver`: methods that mutate `self`

---

## 1. The Problem

Java methods implicitly mutate `this` whenever they write to an instance
field.  In Rust, writing to a field requires the receiver to be `&mut self`.
The PAST emitter already detects some mutation patterns automatically
(e.g. `self.items.push(…)`) and emits `&mut self`.  However, this
auto-detection cannot cover every case.  The `MutableReceiver` annotation
is the explicit escape hatch.

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
have `MutableReceiver` registered in its map; see §11 for the registration
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

## 4. Return Value in `MutableReceiver` Methods

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

## Part B — `MutableFirstParam`: methods that mutate a bean parameter

---

## 9. The Problem

The redesigned `BeanMerger` pattern calls for methods that:

1. receive an **existing** bean (already allocated by the caller),
2. copy fields from an input or output bean **into** it, and
3. return the same bean to the caller.

The natural Java signature is:

```java
CommonBean process(CommonBean bean, InputBean inputBean);
```

In Rust, the required pattern is:

```rust
fn process_bean_inputs<'a>(&self, bean: &'a mut CommonBean, input_bean: &InputBean) -> &'a mut CommonBean
```

Three specific problems arise without special handling:

| Error | Cause | Fix needed |
|-------|-------|-----------|
| E0594 — cannot assign through `&CommonBean` | Default PAST trait param → `&T` (shared borrow) | First param must be `&'a mut T` |
| E0507 — cannot move out of shared reference | Accessing `input_bean.time` for a non-Copy type | Append `.clone()` to field reads from `&T` params |
| E0308 — mismatched types at call site | Passing `bean` where `&mut T` expected | Inject `&mut` / `&` at call sites |

A plain `mut T` (owned, consuming) first parameter would also compile, but
was explicitly rejected: it forces the caller to surrender ownership and
prevents in-place mutation without a clone.

A `&mut T` first parameter with a **lifetime-annotated return** (`-> &'a mut T`)
is the chosen design — the method modifies the bean in place and hands the
same mutable reference back, with no intermediate allocation.

---

## 10. The `MutableFirstParam` Annotation

**Package:** `org.openprovenance.prov.template.compiler.past.annotations`

```java
public class MutableFirstParam extends RustAnnotation {
    public static final String NAME = "rust:@mutablefirstparam";
}
```

Like `MutableReceiver`, it is a `RustAnnotation` subclass and is silently
ignored by all non-Rust emitters.

### Applying the annotation in PAST builders

```java
// Interface (trait) declaration
Method mspec_inI = METHOD(PROCESS_METHOD_NAME)
        .MODIFIERS(Modifier.PUBLIC, Modifier.ABSTRACT)
        .PARAMETER(className, BEAN_VAR)
        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
        .ANNOTATIONS(MutableFirstParam.NAME)
        .RETURNS(className);

// Implementation method
Method mspec_in = METHOD(PROCESS_METHOD_NAME)
        .MODIFIERS(Modifier.PUBLIC, Modifier.FINAL)
        .PARAMETER(className, BEAN_VAR)
        .PARAMETER(inputClassName, INPUT_BEAN_VAR)
        .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
        .RETURNS(className);
```

Both the trait declaration and the implementing method must carry the
annotation so that both code paths (`emitTraitMethod` and
`emitMethod(method, inTrait=true)`) produce matching signatures.

---

## 10a. Lifetime Parameter `<'a>`

When `MutableFirstParam` is active, the method signature requires an
explicit lifetime to tie the mutable reference input to the mutable
reference output:

```rust
fn process_bean_inputs<'a>(&self, bean: &'a mut CommonBean, input_bean: &InputBean) -> &'a mut CommonBean
//                    ^^^^                 ^^                                           ^^
//              lifetime declared      first param                              return type
```

Without `<'a>`, the Rust compiler cannot infer which input reference the
returned reference is derived from (E0261 / lifetime elision ambiguity when
multiple reference parameters exist).

**In `emitTraitMethod`** (trait definition):

```java
boolean traitMFP = hasMutableFirstParam(method);
if (traitMFP) {
    sb.append("<'a>");   // injected before the parameter list
}
```

**In `emitMethod(method, inTrait=true)`** (trait implementation):

```java
currentMethodMutableFirstParam = hasMutableFirstParam(method);
if (currentMethodMutableFirstParam && inTrait) {
    sb.append("<'a>");
}
```

`<'a>` is only injected when `inTrait == true`.  Methods in a plain
`impl Struct {}` block (non-trait) do not use the `&'a mut T` pattern and
do not need the lifetime parameter.

---

## 10b. Parameter Types

When `MutableFirstParam` is active and `inTrait == true`:

| Parameter position | Emitted type | Rationale |
|-------------------|-------------|-----------|
| 0 (first) | `&'a mut T` | Must be mutable so fields can be written; lifetime ties return to input |
| 1..n (rest) | `&T` | Read-only: only field values are read, never mutated |

**In `emitTraitMethod`:**

```java
String rustParamType = (mutableFirst && i == 0)
        ? "&'a mut " + convertTypeToRustParam(param.type)
        : convertTypeToRustTraitParam(param.type);
```

Note: `convertTypeToRustParam` (not `convertTypeToRustTraitParam`) is used
for the first param to get the bare type name `T` (not `&T`), then `&'a mut`
is prepended explicitly.

**In `emitMethod(method, inTrait=true)`:**

```java
if (currentMethodMutableFirstParam && inTrait && i == 0) {
    sb.append(snakeName).append(": &'a mut ").append(convertTypeToRustParam(paramType));
    currentMethodFirstParamName = param.name;  // raw Java name, for downstream use
} else {
    sb.append(snakeName).append(": ")
            .append(inTrait ? convertTypeToRustTraitParam(paramType) : convertTypeToRustParam(paramType));
    if (currentMethodMutableFirstParam && inTrait && i > 0) {
        currentMethodRefParamNames.add(param.name);  // raw Java name, for .clone() detection
    }
}
```

---

## 10c. Return Type

When `MutableFirstParam` is active and `inTrait == true`, the return type
becomes `&'a mut T` instead of `T`:

```rust
// Without MutableFirstParam:
fn process_bean_inputs(&self, bean: FileInitBean, ...) -> FileInitBean

// With MutableFirstParam:
fn process_bean_inputs<'a>(&self, bean: &'a mut FileInitBean, ...) -> &'a mut FileInitBean
```

**In both `emitTraitMethod` and `emitMethod`:**

```java
if (traitMFP /* or currentMethodMutableFirstParam && inTrait */) {
    sb.append(" -> &'a mut ").append(convertTypeToRust(method.returnType));
} else {
    sb.append(" -> ").append(convertTypeToRust(method.returnType));
}
```

The last statement in the method body returns `bean` (the first param
variable) by its raw name — since `bean` is already `&'a mut T`, no
`.clone()` is needed; the reference itself is returned.

---

## 10d. Field Reads from `&T` Reference Parameters — `.clone()`

Within a `MutableFirstParam` impl method, parameters 1..n are `&T`
(shared references).  Moving a non-Copy field out of a shared reference
causes E0507:

```rust
// Fails:
bean.time = input_bean.time;   // E0507: cannot move out of `input_bean` (a &T)

// Correct:
bean.time = input_bean.time.clone();
```

The emitter detects this automatically.  During `emitMethod(m, inTrait=true)`,
the raw names of parameters 1..n are collected in `currentMethodRefParamNames`.
In the `OBJECT_ACCESSOR` conversion path, whenever the object of the field
access is a variable whose raw name is in that set, `.clone()` is appended:

```java
} else if (mc.object instanceof Variable) {
    result.append(convert(mc.object)).append(".").append(sanitizeName(toSnakeCase(mc.methodName)));
    if (currentMethodMutableFirstParam
            && currentMethodRefParamNames.contains(((Variable) mc.object).name)) {
        result.append(".clone()");
    }
```

The first parameter (tracked separately as `currentMethodFirstParamName`)
is **not** in `currentMethodRefParamNames`, so `bean.time` on the LHS of an
assignment does not get `.clone()`.

`.clone()` is always emitted for non-Copy types; it is a no-op for Copy
types (e.g. `i32`), so it is safe to emit unconditionally.

---

## 10e. Call-Site Argument Injection (`&mut` / `&`)

When a `MutableFirstParam` method is called on `self` (i.e. via
`this.process(bean, composee)` in the composite loop), the call site must
pass:
- `&mut bean` for argument 0
- `&composee` for arguments 1+

Without this, the Rust compiler sees:
```
E0308: expected `&mut FileTransformingInputs1`, found `FileTransformingInputs1`
```

The emitter detects such call sites via `calledMethodHasMutableFirstParam`:

```java
private boolean calledMethodHasMutableFirstParam(MethodCall mc) {
    if (currentClass == null) return false;
    if (!(mc.object instanceof Variable)) return false;
    if (!"this".equals(((Variable) mc.object).name)) return false;
    int argCount = mc.arguments == null ? 0 : mc.arguments.size();
    for (Method m : currentClass.methods) {
        if (m.name.equals(mc.methodName)
                && m.parameters != null && m.parameters.size() == argCount
                && hasMutableFirstParam(m)) {
            return true;
        }
    }
    return false;
}
```

In the `OPERATOR_VARIABLE` emission loop:

```java
boolean calleeMFP = calledMethodHasMutableFirstParam(mc);
for (int i = 0; i < mc.arguments.size(); i++) {
    Expression arg = mc.arguments.get(i);
    if (calleeMFP && i == 0) {
        result.append("&mut ").append(convert(arg));
    } else if (calleeMFP && i > 0) {
        result.append("&").append(convert(arg));
    } else {
        // standard ArgTreatment logic …
    }
}
```

---

## 10f. Two Separate Code Paths

`Rust.java` has two distinct paths for emitting method signatures:

| Path | Trigger | `inTrait` |
|------|---------|-----------|
| `emitTraitMethod(method)` | Class is an interface (`isInterface == true`) | N/A (implicit) |
| `emitMethod(method, true)` | Method has `@Override` annotation, inside `emitTraitImplementations` | `true` |
| `emitMethod(method, false)` | Regular struct method, not annotated with `@Override` | `false` |

**Both `emitTraitMethod` and `emitMethod(m, true)` must produce identical
signatures** — otherwise the Rust compiler reports E0053 (method in trait
has incompatible type with the implementation).

`MutableFirstParam` handling was added to both paths independently.  If you
see a `<'a>` in the trait but not the impl (or vice versa), check that both
code paths carry the `hasMutableFirstParam` check.

---

## 11. Registration Requirement

`AnnotationConverter` maintains a static map from annotation name string to
`PastAnnotation` instance.  **All Rust annotations must be registered there**,
otherwise `Method.ANNOTATIONS(NAME)` silently stores `null` and the annotation
is never applied:

```java
// AnnotationConverter.java
map.put(MutableReceiver.NAME,    new MutableReceiver());
map.put(MutableFirstParam.NAME,  new MutableFirstParam());
```

If you add a new `RustAnnotation` subclass and it is not being applied,
check `AnnotationConverter` first.

---

## 12. State Variables Driven by Annotations

| Variable | Set by | Cleared by | Used by |
|----------|--------|-----------|---------|
| `currentMethodConsumesSelf` | `emitMethod`, from `consumesSelf(method)` | next `emitMethod` | RETURN handler (`.clone()`, `.clone().unwrap()`); `convertCallArg` |
| `currentMethodMutableFirstParam` | `emitMethod`, from `hasMutableFirstParam(method)` | next `emitMethod` | `<'a>` lifetime, `&'a mut T` first param, `&'a mut T` return type, `.clone()` on ref-param field reads |
| `currentMethodFirstParamName` | `emitMethod` param loop (i==0, MutableFirstParam) | next `emitMethod` | (reserved; used by `isMutableFirstParamReturn`) |
| `currentMethodRefParamNames` | `emitMethod` param loop (i>0, MutableFirstParam) | next `emitMethod` | OBJECT_ACCESSOR `.clone()` detection |

---

## 13. End-to-End BeanMerger Examples

### Simple template — `MutableReceiver` (old design, still present)

```
PAST (CompilerBeanMerger.java — commented-out MutableReceiver branch)
──────────────────────────────────────────────────────────────────────
METHOD("mergeIntoPackingBean")
  .ANNOTATIONS(MutableReceiver.NAME)   → &mut self
  .RETURNS(mergerClass)
  .BODY(
    ASSIGNMENT(METHOD_CALL(this, beanLocalVar), VARIABLE(BEAN_VAR)),
    RETURN(VARIABLE("this")))

Generated Rust:
pub fn merge_into_packing_bean(&mut self, bean: PackingBean) -> BeanMerger {
    self.bean__packing_bean = Some(bean);
    self.clone()
}
```

### Simple template — `MutableFirstParam` (current design)

```
PAST (CompilerBeanMerger.java)
──────────────────────────────
METHOD(PROCESS_METHOD_NAME)
  .ANNOTATIONS(OverrideAnnotation.NAME, MutableFirstParam.NAME)
  .PARAMETER(className, BEAN_VAR)
  .PARAMETER(inputClassName, INPUT_BEAN_VAR)
  .RETURNS(className)
  .BODY(
    ASSIGNMENT(METHOD_CALL(VARIABLE(BEAN_VAR), key),       // bean.time = input_bean.time
               METHOD_CALL(VARIABLE(INPUT_BEAN_VAR), key)),
    RETURN(VARIABLE(BEAN_VAR)))                            // bean
```

Generated Rust (trait definition in `bean_merger_interface.rs`):

```rust
pub trait BeanMergerInterface {
    fn process_file_init_bean_file_init_inputs<'a>(
        &self,
        bean: &'a mut FileInitBean,
        input_bean: &FileInitInputs,
    ) -> &'a mut FileInitBean;
    …
}
```

Generated Rust (implementation in `bean_merger.rs`):

```rust
impl BeanMergerInterface for BeanMerger {
    fn process_file_init_bean_file_init_inputs<'a>(
        &self,
        bean: &'a mut FileInitBean,
        input_bean: &FileInitInputs,
    ) -> &'a mut FileInitBean {
        bean.time = input_bean.time.clone();
        bean.r#type = input_bean.r#type.clone();
        bean.location = input_bean.location.clone();
        bean
    }
}
```

### Composite template — `MutableFirstParam` composite loop

The composite bean merger iterates over the elements of a composite input
and delegates each one to the simple-template method:

```
PAST (CompilerBeanMerger.java — composite branch)
──────────────────────────────────────────────────
mspec_in.BODY(
    DEFINITION(_int, VARIABLE("count"), CONSTANT(0)),
    ITERATOR(
            PARAMETER("composee", inComposeeClass),
            METHOD_CALL(VARIABLE(INPUT_COMPOSITE_VAR), ELEMENTS))
            .BODY(
                    DEFINITION(beanComposeeClass, VARIABLE(BEAN_VAR),
                               METHOD_CALL(METHOD_CALL(VARIABLE(COMPOSITE_VAR), ELEMENTS),
                                           "get", List.of(VARIABLE("count")))),
                    METHOD_CALL(VARIABLE("this"), PROCESS_METHOD_NAME,
                                List.of(VARIABLE(BEAN_VAR), VARIABLE("composee"))),
                    ASSIGNMENT(VARIABLE("count"), BINARY_OP(VARIABLE("count"), "+", CONSTANT(1)))),
    RETURN(VARIABLE(COMPOSITE_VAR))
)
```

Generated Rust:

```rust
fn process_file_transforming_composite_bean_file_transforming_composite_inputs<'a>(
    &self,
    composite: &'a mut FileTransformingCompositeBean,
    input_composite: &FileTransformingCompositeInputs,
) -> &'a mut FileTransformingCompositeBean {
    let mut count = 0;
    for composee in input_composite.elements.clone().iter().cloned() {
        let mut bean = composite.elements[count as usize].clone();
        self.process_file_transforming_bean_file_transforming_inputs_1(&mut bean, &composee);
        count = count + 1;
    }
    composite
}
```

Note: `&mut bean` and `&composee` at the call site are injected automatically
by the `calledMethodHasMutableFirstParam` check.

---

## 14. Summary of Emitter Decision Points

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
| Method has `MutableFirstParam` annotation (trait def) | `hasMutableFirstParam` in `emitTraitMethod` | `<'a>`, `&'a mut T` first param, `&'a mut T` return |
| Method has `MutableFirstParam` annotation (trait impl) | `hasMutableFirstParam` in `emitMethod(m, true)` | `<'a>`, `&'a mut T` first param, `&T` rest, `&'a mut T` return |
| Field read from `&T` ref param in `MutableFirstParam` method | `currentMethodRefParamNames` in OBJECT_ACCESSOR | `.clone()` appended |
| Call to `MutableFirstParam` method on `self` | `calledMethodHasMutableFirstParam` in OPERATOR_VARIABLE | `&mut arg0`, `&arg1`, … |

---

## 15. Helper Method Reference

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
| `hasMutableFirstParam(Method)` | `Rust.java` | True when method carries `MutableFirstParam` annotation |
| `calledMethodHasMutableFirstParam(MethodCall)` | `Rust.java` | True when call is to a `MutableFirstParam` method on `this` in the current class |
| `isMutableFirstParamReturn(Expression)` | `Rust.java` | True when expression is the first param variable (reserved; `&'a mut T` return is implicit) |

---

## 16. Unit Tests

`MutableReceiver` and `MutableFirstParam` are covered in
`RustEmitterTest.java`:

| Test group | Tests | What is covered |
|-----------|-------|----------------|
| Group 13 — MutableReceiver | `mutableReceiver_emitsMutSelf` | `&mut self` receiver |
| | `mutableReceiver_returnSelf_appendsClone` | `self.clone()` on `return this` |
| | `mutableReceiver_returnOptionField_appendsCloneUnwrap` | `.clone().unwrap()` on Option field return |
| | `mutableReceiver_assignToOptionSubField_asMutUnwrap` | `.as_mut().unwrap()` on two-level LHS |
| Group 14 — MutableFirstParam | `mutableFirstParam_traitDef_hasLifetimeParam` | `<'a>` in trait definition |
| | `mutableFirstParam_traitDef_firstParamIsMutableBorrow` | `bean: &'a mut T` in trait |
| | `mutableFirstParam_traitDef_subsequentParamIsSharedBorrow` | `input_bean: &T` in trait |
| | `mutableFirstParam_traitDef_returnTypeIsMutableBorrow` | `-> &'a mut T` in trait |
| | `mutableFirstParam_impl_hasLifetimeParam` | `<'a>` in trait impl |
| | `mutableFirstParam_impl_firstParamIsMutableBorrow` | `bean: &'a mut T` in impl |
| | `mutableFirstParam_impl_subsequentParamIsSharedBorrow` | `input_bean: &T` in impl |
| | `mutableFirstParam_impl_returnTypeIsMutableBorrow` | `-> &'a mut T` in impl |
| | `mutableFirstParam_fieldReadFromRefParam_appendsClone` | `.clone()` on `&T` ref-param field reads |
| | `mutableFirstParam_callSite_firstArgGetsMutRef_secondArgGetsRef` | `&mut arg0`, `&arg1` at call site |
