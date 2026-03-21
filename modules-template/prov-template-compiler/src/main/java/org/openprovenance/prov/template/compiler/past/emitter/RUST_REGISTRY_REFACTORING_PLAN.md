# Refactoring Plan: Replace Hardcoded Names with a Method/Type Registry

## Motivation

`Rust.java` contains many hardcoded Java class names and method names tested as string
literals (e.g. `"get"`, `"insert"`, `"HashMap"`, `"push"`, `"forEach"`).  Every time a new
collection type or library method needs special Rust handling, multiple scattered `if` blocks
must be edited.  This plan replaces those with a declarative registry of type and method
metadata consulted at emit time.

---

## 1. Current Hardcoded Knowledge — Full Catalogue

### Category A — Type classification

Six boolean helper methods, each with their own hard-wired name lists:

| Method | Hardcoded names |
|---|---|
| `isMap` | `"past.util.Map"`, `"past.util.HashMap"` |
| `isList` | `"past.util.List"`, `"past.util.LinkedList"`, `"past.util.ArrayList"` |
| `isStringType` | `"String"` |
| `isPrimitiveType` | `"int"`,`"Integer"`,`"long"`,`"Long"`,`"float"`,`"Float"`,`"double"`,`"Double"`, `"boolean"`,`"Boolean"`,`"byte"`,`"Byte"`,`"short"`,`"Short"`,`"char"`,`"Character"` |
| `isFunctionType` | `"past.lang.Function"`, `"java.util.function.Function"`, `"Function"` |
| `isClassType` | `"past.lang.Class"`, `"Class"` |

### Category B — Type-to-Rust mapping

`convertCommonType(String)` switch (~20 entries):
`"String"`→`"String"`, `"int"/"Integer"`→`"i32"`, `"long"/"Long"`→`"i64"`,
`"float"/"Float"`→`"f32"`, `"double"/"Double"`→`"f64"`, `"boolean"/"Boolean"`→`"bool"`,
`"Void"/"void"`→`"()"`, `"Object"`→`"Box<dyn std::any::Any>"`, etc.

### Category C — Method call semantics (scattered across `OBJECT_METHOD_CALL` and `OPERATOR_VARIABLE`)

| String tested | Location | Decision driven |
|---|---|---|
| `"forEach"` | OBJECT_METHOD_CALL | Emit `iter_mut().for_each(\|…\| { … })` |
| `"get"` (inner object) | OBJECT_METHOD_CALL | Detect chained map get → unwrap receiver |
| `"insert"` | OBJECT_METHOD_CALL | Switch inner `.get(` to `.get_mut(` |
| `"contains_key"`, `"get"` | OBJECT_METHOD_CALL arg loop | Key argument needs `&T` (borrow+unwrap) |
| `"get"` (result) | OBJECT_METHOD_CALL | Append `.copied()` to result |
| `"push"` (x2) | OBJECT_METHOD_CALL + OPERATOR_VARIABLE | Arguments need `.clone()` |
| `"push"` on self list field | OBJECT_METHOD_CALL | Receiver needs `.as_mut().unwrap()`; args need `Box::new(…)` |
| `"insert"` (arg 0) | OPERATOR_VARIABLE | Key argument needs `.to_string()` (owned String) |
| `"forEach"` | OPERATOR_VARIABLE | Same `iter_mut` transform |

### Category D — Mutation detection (`expressionContainsMutatingCall`)

`"forEach"` and `"add"` are tested by name to decide whether a method implies `&mut self`.

### Existing partial registry: `methodNameConversion` map

```java
put("add",        "push");
put("size",       "len");
put("isEmpty",    "is_empty");
put("toString",   "to_string");
put("containsKey","contains_key");
put("put",        "insert");
```

This covers simple renames only; none of the argument/receiver/result transforms are here.

---

## 2. Proposed Design

Three new classes in the `emitter` package (or sub-package `emitter.registry`).

### 2.1 `RustTypeSpec`

Everything the emitter needs to know about a PAST type:

```java
class RustTypeSpec {

    enum Category {
        MAP,        // HashMap — key/value operations
        LIST,       // Vec — push/iter
        STRING,     // String / &str
        PRIMITIVE,  // Copy types — no .clone() needed
        VOID,       // ()
        FUNCTION,   // functional interface / closure
        CLASS_REF,  // Java Class<T> → &'static str
        OBJECT,     // java.lang.Object → Box<dyn std::any::Any>
        TRAIT,      // known PAST interface → dyn Trait
        STRUCT      // user-defined struct
    }

    final Category    category;
    final String      rustName;   // Rust base type, e.g. "HashMap", "Vec", "i32"
    final boolean     isCopy;     // implements Copy → .clone() not needed on push/pass
    final Set<String> javaNames;  // all fully-qualified + simple names mapping here
}
```

Replaces: `isMap`, `isList`, `isStringType`, `isPrimitiveType`, `isFunctionType`,
`isClassType`, and the `convertCommonType` switch.

### 2.2 `RustMethodSpec`

Everything the emitter must do when converting a particular method call:

```java
class RustMethodSpec {

    final String javaName;
    final String rustName;          // after snake_case conversion; may equal javaName

    final boolean mutatesReceiver;  // true → contributes to &mut self detection

    // What to do to the receiver expression before the "."
    enum ReceiverTransform {
        NONE,
        AS_MUT_UNWRAP   // Option<Vec<…>> self-field: prepend .as_mut().unwrap()
    }
    final ReceiverTransform receiverTransform;

    // What to do when the receiver is itself a chained .get() call
    enum ChainedGetBehavior {
        NONE,
        SWITCH_TO_GET_MUT,   // replace .get( with .get_mut( in the receiver string
        UNWRAP_AND_CHAIN     // append .unwrap() to the receiver before the outer call
    }
    final ChainedGetBehavior chainedGetBehavior;

    // Per-argument handling (last entry is repeated for varargs)
    enum ArgTreatment {
        PASS_BY_VALUE,    // emit as-is
        PASS_BY_REF,      // prepend &
        KEY_BORROW,       // &expr  +  .unwrap() when expr is Option<T> local field
        VALUE_UNWRAP,     // .unwrap() when expr is Option<T> local field
        OWNED_STRING,     // .to_string() to convert &str key to owned String
        CLONE,            // .clone() — value may be used again after this call
        BOX_CLONE         // Box::new(expr.clone()) — for Vec<Box<dyn Any>> push
    }
    final List<ArgTreatment> argTreatments;

    // What to append after the closing ")" of the call
    enum ResultTransform {
        NONE,
        COPIED    // .copied() — HashMap::get returns Option<&T>; .copied() → Option<T>
    }
    final ResultTransform resultTransform;
}
```

### 2.3 `RustMethodRegistry`

Top-level lookup table:

```java
class RustMethodRegistry {

    // Primary index: PAST fully-qualified type name → type spec
    Map<String, RustTypeSpec> typesByJavaName;

    // Method index: (Category, java method name) → method spec
    // Used when the receiver type is known
    Map<Category, Map<String, RustMethodSpec>> methodsByCategory;

    // Global fallback: java method name → method spec
    // Used for mutation detection when receiver type is unknown
    Map<String, RustMethodSpec> globalMethods;

    // --- Query API ---

    RustTypeSpec   resolveType(TypeName tn);

    /** Replaces isMap(tn), isList(tn), isStringType(tn), isPrimitiveType(tn), … */
    boolean        isCategory(TypeName tn, Category cat);

    /** Returns null if unknown — caller falls back to generic emit path */
    RustMethodSpec resolveMethod(TypeName receiverType, String javaMethodName);

    /** For mutation detection where receiver type may be unavailable */
    RustMethodSpec resolveMethodGlobal(String javaMethodName);
}
```

### 2.4 Registry contents (abbreviated static initializer)

```
MAP  ("past.util.Map", "past.util.HashMap", "HashMap"):
  get(key)          → "get",         NOT mutates, NONE receiver,         KEY_BORROW arg,           COPIED result
  containsKey(key)  → "contains_key",NOT mutates, NONE receiver,         KEY_BORROW arg,           NONE result
  put(key, val)     → "insert",      MUTATES,     NONE receiver,         OWNED_STRING + VALUE_UNWRAP args
  chained get.get   →                             SWITCH_TO_GET_MUT / UNWRAP_AND_CHAIN (see §3)

LIST ("past.util.List", "past.util.ArrayList", "past.util.LinkedList"):
  add(elem)         → "push",        MUTATES,     AS_MUT_UNWRAP receiver, BOX_CLONE arg
  size()            → "len"
  isEmpty()         → "is_empty"
  forEach(lambda)   → special form: SpecialForm.FOR_EACH  (see §3 open question 4)

STRING ("java.lang.String", "String"):
  toString()        → "to_string"
```

---

## 3. How the Emitter Changes After Refactoring

### Type classification

```java
// Before:
if (isMap(fieldType)) { … }
if (isList(fieldType)) { … }
if (isStringType(fieldType)) { … }

// After:
if (registry.isCategory(fieldType, MAP)) { … }
if (registry.isCategory(fieldType, LIST)) { … }
if (registry.isCategory(fieldType, STRING)) { … }
```

The six boolean helpers and the `convertCommonType` switch are deleted.

### `OBJECT_METHOD_CALL` — from nested ifs to spec-driven dispatch

```java
// After:
TypeName receiverType = traitReceiverType(mc);   // already computed
RustMethodSpec spec = registry.resolveMethod(receiverType, mc.methodName);

if (spec != null && spec.chainedGetBehavior != NONE && mc.object instanceof MethodCall) {
    // handle chained map access (structural check kept explicit — see §4 Q1)
    applyChainedGetBehavior(spec, …);
} else if (spec != null) {
    convertedObject = spec.receiverTransform.apply(convertedObject);
    result.append(convertedObject).append(".").append(spec.rustName).append("(");
    for (int i = 0; i < args.size(); i++) {
        result.append(spec.argTreatment(i).convert(args.get(i), this));
    }
    result.append(")");
    result.append(spec.resultTransform.suffix());
} else {
    // generic fallback (trait calls, unknown methods, etc.)
    …
}
```

### Mutation detection

```java
// Before:
if ("forEach".equals(mc.methodName) || "add".equals(mc.methodName)) return true;

// After:
RustMethodSpec spec = registry.resolveMethodGlobal(mc.methodName);
return spec != null && spec.mutatesReceiver;
```

### `methodNameConversion` map

Deleted.  The Rust name for every method is `RustMethodSpec.rustName`.  The `sanitizeName`
fallback for methods not in the registry continues to use `toSnakeCase`.

---

## 4. Migration Strategy

### Phase 1 — Build the registry alongside existing code (no behavior change)

Create `RustTypeSpec`, `RustMethodSpec`, `RustMethodRegistry` as new files.  Populate the
registry with all known entries.  Wire `registry.isCategory()` as a thin wrapper that
delegates to the existing boolean helpers.  Add unit tests that compare the registry answer
against the boolean helper for a representative set of type names.

### Phase 2 — Replace type classification

Delete `isMap`, `isList`, `isStringType`, `isPrimitiveType`, `isFunctionType`, `isClassType`.
Replace every call site with `registry.isCategory(…)`.  Replace the `convertCommonType`
switch with `registry.resolveType(tn).rustName`.  Regenerate all target Rust files and diff
the output — it must be byte-for-byte identical before proceeding.

### Phase 3 — Replace method call dispatch

Introduce `registry.resolveMethod(receiverType, methodName)` in `OBJECT_METHOD_CALL` and
`OPERATOR_VARIABLE`.  Guard the spec-driven path with a non-null spec; fall through to
existing code when null.  Migrate one method family at a time:

1. `get` / `containsKey`
2. `put` / `insert`
3. `add` / `push`
4. `forEach`

Regenerate and diff after each step.

### Phase 4 — Replace mutation detection

Replace `expressionContainsMutatingCall` string comparisons with
`registry.resolveMethodGlobal(name).mutatesReceiver`.

### Phase 5 — Delete dead code

Remove `methodNameConversion` map, the six boolean helpers, and all remaining inline string
comparisons against method/type names.  Compile; run full regeneration diff.

---

## 5. Open Questions

1. **Chained HashMap get** — `map.get(outerKey).get(innerKey)` is a structural AST pattern
   (the receiver is itself a `MethodCall`), not just a name match.  The `ChainedGetBehavior`
   flag on `RustMethodSpec` keeps the knowledge declarative, but the AST shape check in
   `OBJECT_METHOD_CALL` must remain as explicit code.  Accept this as a documented special
   case, or introduce a general "method chain" concept in the spec?

2. **Registry location** — static Java initializer (simpler, compile-time safe) vs. external
   YAML/JSON file (extensible without recompile, risk of load-path issues at generation time).
   Recommendation: static initializer for now; externalize only if the number of library
   mappings grows significantly.

3. **Unknown receiver type** — when `inferredType` is null on the receiver, `resolveMethod`
   returns null and the emitter falls back to the current behavior.  This is acceptable short-
   term.  Long-term, consider a strict mode that logs a warning on every unresolved call for
   debugging incomplete type inference coverage.

4. **`forEach` lambda emission** — this generates a multi-line closure body, which is
   fundamentally different from the single-expression argument treatment of other methods.
   Represent it as a `SpecialForm.FOR_EACH` enum value on `RustMethodSpec`, handled by a
   dedicated branch rather than the generic `ArgTreatment` loop.

5. **Relationship to the existing `TypeRegistry`** — `TypeRegistry` (from the PAST type-
   checking phase) holds method *signatures* for type inference.  `RustMethodRegistry` holds
   Rust *emission semantics* for those same methods.  They are complementary and should remain
   separate; however, the two could share the same method name keys to simplify joint lookup.
