# Rust Emitter — Method & Type Registry

> **Location:** `org.openprovenance.prov.template.compiler.past.emitter.registry`
> **JSON source:** `src/main/resources/…/emitter/registry/rust-registry.json`
> **Plan:** `RUST_REGISTRY_REFACTORING_PLAN.md`

---

## 1. Purpose

`Rust.java` translates a PAST (Program Abstract Syntax Tree) into Rust source code.
Historically, every Java-to-Rust type mapping and every method-name translation was a
hardcoded `if`/`switch` or a `HashMap<String,String>` literal in `Rust.java`.

The **registry** externalises those mappings into a JSON file (`rust-registry.json`) that
can be extended without touching `Rust.java`.  The registry provides two orthogonal
services:

| Service | Registry class | Replaces |
|---------|---------------|---------|
| Type classification & Rust name | `RustMethodRegistry.isCategory()`, `rustName()`, `isCopy()` | `isMap()`, `isList()`, `isPrimitiveType()`, `convertCommonType()`, … |
| Method-call emission metadata | `RustMethodRegistry.resolveMethod()`, `resolveMethodGlobal()` | `methodNameConversion` map, hardcoded `"insert".equals(…)`, `"push".equals(…)`, … |

---

## 2. Class Overview

```
registry/
├── RustTypeCategory.java    — enum: MAP, LIST, STRING, PRIMITIVE, VOID,
│                              FUNCTION, CLASS_REF, OBJECT, TRAIT, STRUCT, OTHER
├── RustTypeSpec.java        — immutable: javaNames, category, rustName, copy
├── ArgTreatment.java        — enum: how to convert one argument position
├── ChainedGetBehavior.java  — enum: how to handle receiver that is a .get() call
├── ReceiverTransform.java   — enum: how to transform the receiver before the call
├── ResultTransform.java     — enum: what suffix to add after closing ')'
├── RustMethodSpec.java      — immutable: all metadata for one Java method
└── RustMethodRegistry.java  — central registry; factory + query API
```

### 2.1 `RustTypeSpec`

| Field | Type | Meaning |
|-------|------|---------|
| `javaNames` | `Set<String>` | All Java names that map to this type (fully qualified or simple) |
| `category` | `RustTypeCategory` | Broad classification used for dispatch |
| `rustName` | `String` | The Rust type name to emit (e.g. `"i32"`, `"Vec"`) |
| `copy` | `boolean` | Whether the type has Rust `Copy` semantics (skip `.clone()`) |

### 2.2 `RustMethodSpec`

| Field | Type | Meaning |
|-------|------|---------|
| `javaName` | `String` | Java method name (as in PAST, e.g. `"containsKey"`) |
| `rustName` | `String` | Rust name to emit (e.g. `"contains_key"`) |
| `mutatesReceiver` | `boolean` | Drives `&mut self` and `expressionContainsMutatingCall` |
| `receiverTransform` | `ReceiverTransform` | `NONE` or `AS_MUT_UNWRAP` (Option<Vec<T>> fields) |
| `chainedGetBehavior` | `ChainedGetBehavior` | `NONE`, `UNWRAP_AND_CHAIN`, `SWITCH_TO_GET_MUT` |
| `argTreatments` | `List<ArgTreatment>` | Per-position treatment; last entry repeats (varargs) |
| `resultTransform` | `ResultTransform` | `NONE` or `COPIED` (appends `.copied()`) |

#### ArgTreatment values

| Value | Rust emission | Typical use |
|-------|--------------|-------------|
| `PASS_BY_VALUE` | `convertCallArg(arg)` | Default; passes by value or `&mut` for maps |
| `PASS_BY_REF` | `&convertCallArg(arg)` | Borrow without unwrapping |
| `KEY_BORROW` | `&convertOptionArg(arg)` | HashMap key: borrow + unwrap Option |
| `VALUE_UNWRAP` | `convertOptionArg(arg)` | HashMap value: unwrap Option |
| `OWNED_STRING` | `convertOwnedStringArg(arg)` | String literal → owned `String` via `.to_string()` |
| `CLONE` | `convertCallArg(arg) + ".clone()"` | Skip clone for Copy types |
| `BOX_CLONE` | `Box::new(… .clone())` | Push into `Vec<Box<dyn Any>>`; skip clone for Copy |

#### ChainedGetBehavior values

When the receiver is itself a chained `.get(key)` call (i.e. `map.get(k1).method(…)`):

| Value | Effect |
|-------|--------|
| `NONE` | Not expected to appear chained; no special handling |
| `UNWRAP_AND_CHAIN` | Unwrap the Option, then emit the outer call normally |
| `SWITCH_TO_GET_MUT` | Replace `.get(` with `.get_mut(` then unwrap (needed for `insert`) |

#### ReceiverTransform values

| Value | Appended to receiver | Use case |
|-------|---------------------|---------|
| `NONE` | _(nothing)_ | Ordinary receiver |
| `AS_MUT_UNWRAP` | `.as_mut().unwrap()` | `self.field` is `Option<Vec<T>>` — reach the inner `Vec` |

#### ResultTransform values

| Value | Appended after `)` | Use case |
|-------|-------------------|---------|
| `NONE` | _(nothing)_ | Most methods |
| `COPIED` | `.copied()` | `HashMap::get()` returns `Option<&T>`; `.copied()` → `Option<T>` |

---

## 3. JSON Structure (`rust-registry.json`)

```jsonc
{
  "types": [
    {
      "javaNames": ["past.util.Map", "past.util.HashMap", "HashMap"],
      "category": "MAP",
      "rustName": "HashMap",
      "copy": false
    }
    // … more type entries
  ],

  "methodsByCategory": {
    "MAP": [
      {
        "javaName": "get",
        "rustName": "get",
        "mutatesReceiver": false,
        "receiverTransform": "NONE",
        "chainedGetBehavior": "UNWRAP_AND_CHAIN",
        "argTreatments": ["KEY_BORROW"],
        "resultTransform": "COPIED"
      }
      // … more MAP methods
    ],
    "LIST": [ /* … */ ]
  },

  "globalMethods": [
    {
      "javaName": "toString",
      "rustName": "to_string",
      "mutatesReceiver": false,
      "receiverTransform": "NONE",
      "chainedGetBehavior": "NONE",
      "argTreatments": [],
      "resultTransform": "NONE"
    }
  ]
}
```

**Comments in JSON:** Jackson does not support `//` comments natively.  Use a
`"_comment"` field instead; `@JsonIgnoreProperties(ignoreUnknown = true)` on
`RustMethodSpec` silently discards these.

---

## 4. Loading the Registry

### From classpath (production)

```java
// Loaded once, stored as a static field in Rust.java
private static final RustMethodRegistry METHOD_REGISTRY;
static {
    RustMethodRegistry r;
    try {
        r = RustMethodRegistry.loadFromClasspath();
    } catch (Exception e) {
        System.err.println("[Rust emitter] WARNING: …" + e.getMessage());
        r = RustMethodRegistry.builder().build(); // empty — graceful degradation
    }
    METHOD_REGISTRY = r;
}
```

`loadFromClasspath()` opens `rust-registry.json` via
`RustMethodRegistry.class.getResourceAsStream("rust-registry.json")`.
The resource file must be placed in:

```
src/main/resources/
  org/openprovenance/prov/template/compiler/past/emitter/registry/
    rust-registry.json
```

The `module-info.java` must `opens` the registry package so Jackson can access it
reflectively:

```java
opens org.openprovenance.prov.template.compiler.past.emitter.registry;
```

### Programmatic (unit tests)

```java
RustMethodRegistry r = RustMethodRegistry.builder()
    .type(new RustTypeSpec(Set.of("MyStruct"), RustTypeCategory.STRUCT, "MyStruct", false))
    .method(RustTypeCategory.STRUCT, new RustMethodSpec(
        "doThing", "do_thing", false,
        ReceiverTransform.NONE, ChainedGetBehavior.NONE,
        List.of(ArgTreatment.PASS_BY_VALUE), ResultTransform.NONE))
    .globalMethod(new RustMethodSpec(
        "globalOp", "global_op", true,
        ReceiverTransform.NONE, ChainedGetBehavior.NONE,
        List.of(), ResultTransform.NONE))
    .build();
```

Builder-only registries are isolated from JSON content, making tests predictable.

---

## 5. Query API

```java
// Type classification
registry.isCategory(typeName, RustTypeCategory.MAP)      // replaces isMap()
registry.isCategory(typeName, RustTypeCategory.PRIMITIVE) // replaces isPrimitiveType()
registry.isCopy(typeName)                                 // skip .clone() for Copy types

// Type name conversion
registry.rustName("int")        // → "i32"   (by simple Java name)
registry.rustName(typeName)     // → "i32"   (by TypeName object)

// Method lookup
registry.resolveMethod(RustTypeCategory.MAP, "get")       // category-specific
registry.resolveMethod(receiverTypeName, "get")            // convenience overload
registry.resolveMethodGlobal("add")                       // global + all categories
```

`resolveMethodGlobal` checks global methods first, then scans all category-specific
entries — useful when the receiver type is unknown or when detecting mutation.

---

## 6. How `Rust.java` Uses the Registry

### 6.1 Type-classification helpers (Phase 2 — deprecated wrappers)

The six boolean helpers and `convertCommonType` are now `@Deprecated` one-liners that
delegate to `METHOD_REGISTRY`.  They will be deleted in Phase 5.

### 6.2 Method name translation (`sanitizeName`, Phase 3)

`sanitizeName` now checks the registry **first** (via `resolveMethodGlobal`), returning
`spec.rustName` when found.  The legacy `methodNameConversion` `HashMap` is a fallback
pending Phase 5 cleanup.

### 6.3 `OBJECT_METHOD_CALL` — chained-get pattern (Phase 3)

When the receiver is itself a `.get(key)` call, the registry `ChainedGetBehavior`
determines whether to switch to `.get_mut(`, and per-argument `ArgTreatment` replaces
the previous `"contains_key".equals(callMethodName)` / `"insert".equals(callMethodName)`
guards.  `ResultTransform.COPIED` drives the `.copied()` suffix.

### 6.4 `OBJECT_METHOD_CALL` — push/add pattern (Phase 3)

`ReceiverTransform.AS_MUT_UNWRAP` replaces the `"push".equals(callMethodName)` check
for appending `.as_mut().unwrap()` to `Option<Vec<T>>` self-fields.  Per-argument
`ArgTreatment` (typically `CLONE`) replaces the `isPrimitiveType` guard and the
`Box::new(…)` wrap is applied when both `rt == AS_MUT_UNWRAP` and the field is an
option-list field.

### 6.5 `OPERATOR_VARIABLE` (Phase 3)

Per-argument `ArgTreatment` from `resolveMethodGlobal(mc.methodName)` replaces the
`"insert".equals(callMethodName) && i == 0` (→ `OWNED_STRING`) and
`"push".equals(…) && !isPrimitive(…)` (→ `CLONE`) guards.

### 6.6 `expressionContainsMutatingCall` (Phase 4)

`spec.mutatesReceiver` from `resolveMethodGlobal(mc.methodName)` replaces the hardcoded
`"forEach".equals(…) || "add".equals(…)` checks.  This automatically extends to any
future mutating method registered in the JSON.

---

## 7. Extending the Registry

### Adding a new type

Add an entry to the `"types"` array in `rust-registry.json`:

```json
{
  "javaNames": ["past.util.Set", "HashSet"],
  "category": "SET",
  "rustName": "HashSet",
  "copy": false
}
```

Then add `SET` to the `RustTypeCategory` enum.

### Adding a new method

Add an entry to the appropriate `"methodsByCategory"` section (or `"globalMethods"` for
receiver-type-agnostic methods):

```json
{
  "javaName": "remove",
  "rustName": "remove",
  "mutatesReceiver": true,
  "receiverTransform": "NONE",
  "chainedGetBehavior": "NONE",
  "argTreatments": ["KEY_BORROW"],
  "resultTransform": "NONE"
}
```

No Java code changes are needed for common cases — `sanitizeName`, the dispatch blocks,
and `expressionContainsMutatingCall` all use the registry automatically.

---

## 8. Phase Roadmap

| Phase | Status | Description |
|-------|--------|-------------|
| 1 | ✅ Done | Create registry classes and JSON; write 36 unit tests |
| 2 | ✅ Done | Redirect boolean helpers and `convertCommonType` to registry |
| 3 | ✅ Done | Registry-driven dispatch in `OBJECT_METHOD_CALL` and `OPERATOR_VARIABLE` |
| 4 | ✅ Done | Registry-driven mutation detection (`expressionContainsMutatingCall`) |
| 5 | ⏳ Future | Delete dead code: `methodNameConversion`, deprecated helper methods |
