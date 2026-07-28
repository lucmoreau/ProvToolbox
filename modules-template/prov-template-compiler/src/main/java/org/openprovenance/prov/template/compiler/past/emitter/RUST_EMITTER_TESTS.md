# Rust Emitter Unit Tests

**File:** `src/test/java/org/openprovenance/prov/template/compiler/test/RustEmitterTest.java`

Unit tests for the `Rust.java` code generator. Each test constructs a minimal PAST tree
programmatically and asserts that specific fragments appear (or do not appear) in the
generated Rust source.

---

## How to invoke the emitter in a test

```java
private String emit(Class clazz) {
    Rust rust = new Rust();
    rust.discoverClass(clazz);          // register class for cross-class field lookup
    return rust.emit(clazz).toString(); // generate Rust source
}
```

`discoverClass` must be called before `emit` when the class's fields need to be
looked up during emission (e.g. to decide Option wrapping for field accesses).

For multi-class scenarios where one class's methods access fields of another class,
call `discoverClass` on every class that will be referenced, using the same `Rust`
instance for all of them.

---

## PAST construction patterns used in tests

### Variables

```java
// Local variable (emits as snake_cased name, e.g. myVar → my_var)
Variable local = new Variable("myVar", Variable.VariableKind.LOCAL_VARIABLE);

// Field variable (emits as self.snake_cased_name)
Variable field = new Variable("myField", Variable.VariableKind.FIELD_VARIABLE);

// With inferred type (used for Copy-type detection in .clone() suppression)
Variable intVar = new Variable("n", LOCAL_VARIABLE);
intVar.inferredType = ClassName._int;   // i32 is Copy → no .clone()
```

### Method calls

| PAST form | Rust emitter kind | Usage |
|-----------|-------------------|-------|
| `new MethodCall(Variable, name, args)` | `OPERATOR_VARIABLE` | `var.method(args)` — most method calls on local/field vars |
| `new MethodCall(MethodCall, name, args)` | `OBJECT_METHOD_CALL` | `expr.method(args)` — chained calls |
| `new MethodCall(ClassName, name, args)` | `STATIC_METHOD_CALL` | `Type::method(args)` |
| `MethodCall.CONSTRUCTOR_CALL(TypeName, args)` | `CONSTRUCTOR_CALL` | `Type::new(args)` |
| `new MethodCall(Variable, accessorName)` | `OBJECT_ACCESSOR` | `var.field` — field/property accessor, no args |

### Classes, methods and fields

```java
// Minimal class
Class clazz = new Class("MyStruct").MODIFIERS(Modifier.PUBLIC)
        .FIELDS(field1, field2)
        .METHODS(method1);

// Field without initialiser → Option<T> in Rust
Field optField = new Field("name", ClassName.STRING);

// Field with initialiser → plain T in Rust
Field plainField = new Field("count", ClassName._int);
plainField.initialiser = new Constant(0);

// Method
Method m = new Method("doWork")
        .MODIFIERS(Modifier.PUBLIC)
        .RETURNS(ClassName.VOID)
        .BODY(stmt1, stmt2);
```

---

## Test groups and what they verify

### Group 1 — Struct layout (7 tests)

| Test | Assertion |
|------|-----------|
| `simpleClass_emitsStructKeyword` | Output contains `pub struct MyStruct` |
| `fieldWithoutInitializer_emittedAsOptionString` | `Option<String>` present; bare `: String,` absent |
| `fieldWithInitializer_emittedAsPlainString` | Bare `: String,` present; `Option<String>` absent |
| `intField_emittedAsI32` | `: i32,` present |
| `uninitializedIntField_emittedAsOptionI32` | `Option<i32>` present |
| `fieldNameIsSnakedCased` | `myFieldName` → `my_field_name:` |
| `classNameIsPascalCased` | `myStruct` → `struct MyStruct` |

**Key rule:** A PAST field with no `initialiser` is wrapped in `Option<T>`; one with an
`initialiser` is plain `T`. See `RUST_OPTION_FIELDS.md` for the full design.

### Group 2 — Method name translation (5 tests)

All use `OPERATOR_VARIABLE` on a local variable. The registry's `resolveMethodGlobal`
searches category-specific entries as a fallback, so `add` (LIST), `containsKey` (MAP),
etc. are all reachable.

| Java name | Rust name | Arg treatment |
|-----------|-----------|---------------|
| `add` | `push` | `CLONE` — appends `.clone()` unless arg type is Copy |
| `size` | `len` | — |
| `isEmpty` | `is_empty` | — |
| `toString` | `to_string` | — |

Tests:
- `methodCall_add_translatedToPush` — output contains `my_list.push(`
- `methodCall_add_appendsClone` — output contains `item.clone()`
- `methodCall_add_copyType_noClone` — with `inferredType = ClassName._int`, no `.clone()`
- `methodCall_size_translatedToLen` — output contains `my_list.len()`
- `methodCall_isEmpty_translatedToIsEmpty` — output contains `my_list.is_empty()`
- `methodCall_toString_translatedToToString` — output contains `my_obj.to_string()`

### Group 3 — HashMap argument treatment (3 tests)

| Method | Arg[0] treatment | Arg[1] treatment |
|--------|-----------------|-----------------|
| `containsKey` | `KEY_BORROW` → `&key` | — |
| `put` | `OWNED_STRING` → `"k".to_string()` | `VALUE_UNWRAP` → `.unwrap()` if Option field |

Tests:
- `hashMap_containsKey_keyIsBorrowed` — output contains `contains_key(&key)`
- `hashMap_put_firstArgOwnedString` — output contains `"myKey".to_string()`
- `hashMap_put_secondArgPassedThrough` — local var (not a field access) passes through
  without `.unwrap()`

### Group 4 — Chained-get dispatch (2 tests)

Triggered when `mc.object instanceof MethodCall` and `innerMc.methodName == "get"`.
The outer method's `chainedGetBehavior` drives the transformation.

| Pattern | `ChainedGetBehavior` | Emitted Rust |
|---------|----------------------|--------------|
| `map.get(k1).get(k2)` | `UNWRAP_AND_CHAIN` | `my_map.get(&k1).unwrap().get(&k2).copied()` |
| `map.get(k1).put(k2, v)` | `SWITCH_TO_GET_MUT` | `my_map.get_mut(&k1).unwrap().insert(…)` |

PAST construction:
```java
MethodCall innerGet = new MethodCall(mapVar, "get", List.of(k1Var));    // OPERATOR_VARIABLE
MethodCall outerGet = new MethodCall(innerGet, "get", List.of(k2Var));  // OBJECT_METHOD_CALL
```

Tests:
- `chainedGet_get_unwrapsAndAddsCopied` — `.unwrap()` and `.copied()` present; `get(&k2)` present
- `chainedGet_put_switchesToGetMut` — `get_mut(` present; `.get(&k1)` absent; `insert(` present

### Group 5 — Mutation detection → `&mut self` (3 tests)

`modifiesSelf(method)` inspects the method body for `mutatesReceiver = true` calls on
self-owned fields.

| PAST pattern | Triggers `&mut self`? |
|---|---|
| `MethodCall(FIELD_VARIABLE, "add", args)` via `OPERATOR_VARIABLE` | Yes |
| `MethodCall(FIELD_VARIABLE, "size", args)` | No |
| `MethodCall(LOCAL_VARIABLE, "add", args)` | No — only field variables trigger it |

Tests:
- `methodWithFieldListAdd_emitsMutSelf` — output contains `&mut self`
- `methodWithoutMutation_emitsSharedSelf` — output contains `&self`, not `&mut self`
- `methodWithLocalListAdd_doesNotEmitMutSelf` — local var `add` does not trigger mutation

### Group 6 — Control flow (4 tests)

| Test | What it checks |
|------|---------------|
| `ifStatement_emitsIfBlock` | `if flag {` present; `push(` inside the body |
| `ifStatement_withElse_emitsIfElseBlock` | `if flag {` and `} else {` both present |
| `lastReturnStatement_emittedAsImplicitReturn` | Last `Return` node → no `return` keyword (Rust implicit return) |
| `nonLastReturnStatement_emittedWithReturnKeyword` | `Return` inside `if` body → `return 0;` |

### Group 7 — Static calls and constructors (2 tests)

| PAST | Emitted Rust |
|------|-------------|
| `new MethodCall(ClassName.HASHMAP, "new", List.of())` | `HashMap::new()` |
| `MethodCall.CONSTRUCTOR_CALL(myType, List.of(arg))` | `MyType::new(arg)` |

### Group 8 — Variable conversion (2 tests)

| Variable kind | Emitted form |
|---|---|
| `FIELD_VARIABLE("myField")` | `self.my_field` |
| `LOCAL_VARIABLE("myLocal")` | `my_local` |

### Group 9 — Iterator / for-each (3 tests)

| PAST collection expression | Emitted Rust |
|---------------------------|-------------|
| `LOCAL_VARIABLE("items")` | `for item in items {` |
| `OBJECT_ACCESSOR(LOCAL_VARIABLE("bean"), "myItems")` | `for item in bean.my_items.iter().cloned() {` |
| `FIELD_VARIABLE("myItems")` | `for item in self.my_items {` (no `.iter().cloned()`) |

**Why `.iter().cloned()` for local field access but not for self-fields?**
The emitter adds `.iter().cloned()` only when `isLocalFieldAccess(collection)` is true —
i.e. the collection is an `OBJECT_ACCESSOR` call on a `LOCAL_VARIABLE` (not `self`).
This avoids moving out of a borrowed local struct. When iterating over `self.field`,
the borrow rules differ and `.iter().cloned()` is not inserted.

---

## Adding new tests

1. Identify the PAST node type you want to test (see `past/` package).
2. Build a minimal `Class` with the relevant field/method/statement.
3. Call `emit(clazz)` and assert on output fragments.
4. If the test involves cross-class field lookups (e.g. `.unwrap()` suppression based on
   field initialisers in another class), call `discoverClass` on each referenced class
   before `emit`.

For registry-driven behaviour, see `RUST_REGISTRY.md`.
For Option-field rules, see `RUST_OPTION_FIELDS.md`.
