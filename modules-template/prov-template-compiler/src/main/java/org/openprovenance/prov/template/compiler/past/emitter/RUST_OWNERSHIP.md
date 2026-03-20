# Rust Emitter — Ownership and Borrowing Principles

This document records the decisions made in `Rust.java` for translating Java
parameter passing and iteration into idiomatic (and compilable) Rust.  It is
intended as a reference when extending the emitter or debugging type mismatches
in generated code.

---

## 1. General Principle

Java has uniform reference semantics: every object is accessed through an
implicit pointer.  Rust distinguishes sharply between:

| Mode | Rust syntax | Meaning |
|------|-------------|---------|
| Owned value | `T` | Caller transfers ownership; callee may move or drop it |
| Shared borrow | `&T` | Read-only view; caller retains ownership |
| Mutable borrow | `&mut T` | Read-write view; caller retains ownership |

When generating Rust from PAST the emitter must pick one of these three modes
for every parameter and every argument.  The rules below govern that choice.

---

## 2. String

### Background

`String` is a heap-allocated, owned string type.  `&str` is a borrowed string
slice.  In idiomatic Rust:

- **Function parameters that only read a string** should accept `&str`; this
  allows the caller to pass a `&str` literal directly without allocation.
- **Struct fields that store a string** must be `String`; an owned value must be
  stored.
- **`HashMap<String, …>` keys** must be `String` (owned); the key is moved into
  the map on `insert`.

### Emitter rules

| Site | Generated type | Converter |
|------|---------------|-----------|
| Method parameter (non-trait impl) | `&str` | `convertTypeToRustParam` — `String` ClassName → `"&str"` |
| Method parameter (trait impl) | `Option<&str>` | `convertTypeToRustTraitParam` — `String` → `"Option<&str>"` |
| Struct field | `String` | `convertTypeToRust` — unmodified |
| Field initialiser / RHS expression | `"value".to_string()` | `convertConstant(c, targetType)` — appends `.to_string()` when target is `String` |
| `HashMap::insert` key argument | `"key".to_string()` | `convertOwnedStringArg` — appends `.to_string()` on string constants |

### Rationale

Callers of generated methods always have an owned `String` or a `&str`
literal; accepting `&str` avoids an allocation at every call site.  When the
string must be *stored* (map key, struct field) it must be owned, so
`.to_string()` is appended at the point where the `&str` value is written into
an owning context.

---

## 3. Struct / Bean types

### Background

Generated PAST structs (beans, inputs, outputs) can be large.  They derive
`Debug`, `Clone`, `Serialize`, and `Deserialize`.  Fields without an
initialiser are wrapped in `Option<T>`.

### Parameter passing

| Site | Generated type | Notes |
|------|---------------|-------|
| Trait method parameter (`inTrait=true`) | `&T` | `convertTypeToRustTraitParam` — non-primitive, non-collection types become `"&" + T` |
| Struct method parameter (`inTrait=false`) | `T` (by value) | `convertTypeToRustParam` falls through to `convertTypeToRust`, returning bare `T` |

**Why the asymmetry?**  Trait methods are always read-only (they observe the
bean and return a new output struct).  They should therefore borrow.  Struct
impl methods (`pub fn`) may move the parameter into a `Vec` (e.g.
`add_elements`), so they need ownership.

> **Important consequence:** if you add a new struct-impl method whose
> parameter should be read-only, you may want to annotate or override the
> parameter type manually; the emitter cannot currently distinguish read-only
> from move-into-collection at the parameter level.

### Iterating over a `Vec<T>` inside a borrowed struct

When a `for` loop iterates over a field of a locally-borrowed struct
(e.g. `bean: &CompositeInputs`, iterating over `bean.elements`), moving the
`Vec` out of the borrow is illegal.

The emitter (ITERATOR case in `emitStatement`) detects this pattern via
`isLocalFieldAccess(collection)` and generates:

```rust
for item in bean.elements.iter().cloned() { … }
```

`.iter()` yields `&T`; `.cloned()` calls `T::clone()` to produce owned `T`
values.  This is safe because all generated structs derive `Clone`.  The
resulting loop variable is `T` (owned), which satisfies by-value method
signatures such as `process_…_map(bean: T, …)`.

### Struct fields are `Option<T>`

Every struct field without an initialiser is wrapped in `Option<T>`.  When
such a field is read:

- As a **HashMap key** (`contains_key`, `get`): `&field.unwrap()` — borrow
  the unwrapped `T` reference.  Helper: `convertHashMapKeyArg`.
- As a **HashMap value** (`insert`): `field.unwrap()` — take ownership of
  the `Copy` value.  Helper: `convertOptionArg`.
- On the **left-hand side** of an assignment: the RHS must produce
  `Option<T>`, either via `Some(rhs)` wrapping or directly when the RHS
  already evaluates to `Option<T>` (see §4).

---

## 4. HashMap

### Background

`HashMap<K, V>` is not `Copy`.  In Rust, passing a `HashMap` by value moves
it; to allow the caller to keep using the map the callee must borrow it.
Because generated `_map` methods both read from and write into the map (via
`get_mut` / `insert`), the borrow must be *mutable*.

### Parameter type

Both `convertTypeToRustParam` and `convertTypeToRustTraitParam` detect map
types via `isMap(tn)` and emit `&mut HashMap<…>`.

| Site | Generated type |
|------|---------------|
| Any method parameter whose type is `Map` or `HashMap` | `&mut HashMap<K, V>` |

### Call sites

When a local `HashMap` variable is passed as an argument the emitter
(`convertCallArg`) checks `arg.inferredType` and, if the argument is a local
variable with a map type, prefixes the argument with `&mut`:

```rust
self.process_…_map(in1, &mut map)   // generated
```

### Key argument types

`HashMap<String, …>` keys are `String` (owned).  The outer map is populated
via `insert` whose key arguments must be owned strings:

```rust
map.insert("transformed_file".to_string(), HashMap::new());
```

This is handled by `convertOwnedStringArg`, which appends `.to_string()` to
string constants when they appear as the first (key) argument of `insert`.

The **inner** map (`HashMap<i32, i32>`) has `i32` (Copy) keys.  When the key
comes from an `Option<i32>` bean field, the emitter calls
`convertHashMapKeyArg` which produces `&field.unwrap()`:

```rust
inner_map.contains_key(&bean.transformed_file.unwrap())
inner_map.get(&bean.transformed_file.unwrap()).copied()
inner_map.insert(bean.transformed_file.unwrap(), value.unwrap())
```

### Return type from `HashMap::get`

`HashMap::get(&K)` returns `Option<&V>`.  For `Copy` value types (e.g.
`i32`), `.copied()` converts `Option<&V>` to `Option<V>`.  The emitter
appends `.copied()` automatically when the outer method is `get`:

```rust
out.field = map.get("key").unwrap().get(&bean.field.unwrap()).copied();
```

Because `.copied()` already returns `Option<T>`, the assignment does **not**
need additional `Some(…)` wrapping.  `expressionProducesOption` detects this
pattern and suppresses the wrap in the ASSIGNMENT case of `emitStatement`.

### Mutability for nested access

`HashMap::get` returns a shared reference `Option<&V>`.  To call `insert` on
the inner map the emitter needs a **mutable** reference.  In the
`OBJECT_METHOD_CALL` path, when the call is `insert` and the object is a
chained `.get(…)`, the emitter replaces `.get(` with `.get_mut(` before
appending `.unwrap()`:

```rust
map.get_mut("key").unwrap().insert(k, v)
```

---

## 5. Summary table

| Type | Parameter (struct impl) | Parameter (trait impl) | Struct field |
|------|------------------------|----------------------|-------------|
| `String` | `&str` | `Option<&str>` | `String` |
| Primitive (`i32`, `bool`, …) | `T` (by value) | `Option<T>` | `Option<T>` |
| Struct / bean | `T` (by value) | `&T` | `Option<T>` |
| `Vec<T>` | `T` (by value, emitter default) | `&Vec<T>` | `Vec<T>` |
| `HashMap<K,V>` | `&mut HashMap<K,V>` | `&mut HashMap<K,V>` | `HashMap<K,V>` |

---

## 6. Helper methods in `Rust.java`

| Method | Purpose |
|--------|---------|
| `convertTypeToRustParam(TypeName)` | Parameter type for struct impl methods |
| `convertTypeToRustTraitParam(TypeName)` | Parameter type for trait impl methods |
| `convertHashMapKeyArg(Expression)` | `&expr.unwrap()` for Option field used as map key |
| `convertOptionArg(Expression)` | `expr.unwrap()` for Option field used by value |
| `convertOwnedStringArg(Expression)` | `"str".to_string()` for string constant map key |
| `convertCallArg(Expression)` | `&mut var` when local HashMap is passed as argument |
| `isLocalFieldAccess(Expression)` | True when expr is `localVar.field` (non-self) |
| `isNonSelfFieldAccess(Expression)` | Alias used for LHS `Some()` wrapping decisions |
| `expressionProducesOption(Expression)` | True when expr already returns `Option<T>` |
