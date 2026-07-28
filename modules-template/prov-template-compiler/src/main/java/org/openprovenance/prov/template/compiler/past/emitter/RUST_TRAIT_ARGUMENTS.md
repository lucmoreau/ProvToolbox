# Rust Emitter — How Trait Arguments Are Handled

This document explains every Rust construct the emitter uses when a PAST type
is a *known trait* (one registered in `knownTraits`, e.g. `InputOutputProcessor`).
It covers the four key keywords — `impl`, `Box`, `dyn`, `'static` — their
individual meanings, and why each one is needed in this code-generation context.

---

## 1. Background: traits are not types in the usual sense

In Java an interface is just a reference type.  You can write:

```java
InputOutputProcessor p = new BeanLocalEnactor2();
```

and pass `p` anywhere `InputOutputProcessor` is expected.

In Rust a trait is a *contract*, not a concrete type.  The compiler must always
know the exact memory layout of every value.  A bare trait name is not a type
that can appear in a position where a concrete, sized value is required.  Two
different constructs cover the two different use cases:

| Use case | Rust construct | Dispatch |
|----------|---------------|----------|
| "any concrete type that implements the trait, decided at compile time" | `impl Trait` | static (monomorphised) |
| "some concrete type that implements the trait, decided at runtime" | `dyn Trait` | dynamic (vtable) |

---

## 2. `impl Trait` — static dispatch in parameter position

```rust
pub fn new(template_invoker: impl InputOutputProcessor + 'static, …) -> Self
```

### What it means

`impl Trait` in a parameter position is syntactic sugar for a *generic
parameter* bounded by that trait:

```rust
// these two are equivalent
pub fn new<T: InputOutputProcessor + 'static>(template_invoker: T, …) -> Self

pub fn new(template_invoker: impl InputOutputProcessor + 'static, …) -> Self
```

The compiler generates a separate, specialised copy of `new` for every
concrete type `T` that is actually passed.  No vtable is used; all method calls
on `template_invoker` inside `new` are resolved at compile time.

### Why the emitter uses it here

The constructor receives a concrete value from the caller.  The caller always
knows the exact type (e.g. `BeanLocalEnactor2`).  Using `impl Trait`:

- Avoids allocating a vtable at the call site.
- Keeps the constructor ergonomic — the caller writes `Workflow::new(enactor, …)`
  without wrapping in `Box`.
- Is idiomatic for "accept any implementor by value".

### The `+ 'static` bound

`Box<dyn Trait>` silently expands to `Box<dyn Trait + 'static>`.  This means
the stored trait object must not contain references shorter than the program
lifetime.  If the parameter were typed as `impl Trait` without `+ 'static`, the
compiler cannot prove the passed-in value satisfies that bound and emits
**E0310** ("the parameter type may not live long enough").

Adding `+ 'static` to the parameter signals: *"the concrete type you pass must
own all its data."*  All generated concrete types (`BeanLocalEnactor2`, etc.)
are fully-owned structs with no borrowed fields, so they trivially satisfy this.

```
impl Trait + 'static
 │      │         └─ no non-static borrows inside the value
 │      └─────────── the concrete type implements this trait
 └────────────────── accepted by value, monomorphised at compile time
```

---

## 3. `Box` — heap allocation for owned, sized indirection

```rust
template_invoker: Option<Box<dyn InputOutputProcessor>>,
```

### What it means

`Box<T>` is Rust's simplest heap-allocated smart pointer.  It owns the `T`,
frees it when dropped, and dereferences transparently.  Its size is always one
pointer word, regardless of what `T` is.

### Why it is needed here

A struct field must have a known, fixed size.  `dyn Trait` (see §4) has no
known size — different implementors can be arbitrarily large.  Putting the value
behind a `Box` gives the field a fixed size (one pointer) while the actual
object lives on the heap.

```
Stack frame of PleadWorkflow
┌──────────────────────────────────────┐
│ template_instantiation: Option<Box>  │  ← fixed size: one word (ptr) + tag
│   └─ ptr ──────────────────────────────────────────────┐
│ …                                    │                  │
└──────────────────────────────────────┘    Heap          ▼
                                        ┌─────────────────────────┐
                                        │  BeanLocalEnactor2 data │
                                        │  vtable ptr → …         │
                                        └─────────────────────────┘
```

### Constructor body

Because the constructor accepts `impl Trait + 'static` (an owned concrete
value), the emitter wraps it in `Box::new` before storing:

```rust
template_invoker: Some(Box::new(template_invoker)),
```

`Box::new(v)` moves `v` onto the heap and returns a `Box` owning it.

---

## 4. `dyn Trait` — dynamic dispatch through a vtable

```rust
Option<Box<dyn InputOutputProcessor>>
```

### What it means

`dyn Trait` is a *trait object*.  At runtime it is a fat pointer: a data
pointer to the concrete value on the heap plus a vtable pointer.  Method calls
on a `dyn Trait` reference go through the vtable (one indirection), like a
virtual call in Java.

```
Fat pointer (two words)
┌──────────────┬──────────────┐
│  data ptr    │  vtable ptr  │
└──────┬───────┴──────┬───────┘
       │              │
       ▼              ▼
  BeanLocal…    ┌───────────────────────┐
  instance      │ process_inputs: fn…   │
                │ process_outputs: fn…  │
                │ drop: fn…             │
                └───────────────────────┘
```

### Why the emitter uses it for struct fields

The struct must store *some* implementor of `InputOutputProcessor` without
knowing which one at compile time (different workflows could be wired to
different processors).  `dyn Trait` is the only way to store heterogeneous
concrete types behind a single field type.

`Box<dyn Trait>` = owned heap allocation + vtable.  This is the standard
pattern for "store a trait object for later use".

### `dyn` is mandatory (Rust 2021)

Before Rust 2021 edition, `Box<Trait>` was accepted as shorthand.  Since Rust
2021 the `dyn` keyword is mandatory to make dynamic dispatch explicit:

```rust
Box<InputOutputProcessor>       // ← E0782: missing `dyn`
Box<dyn InputOutputProcessor>   // ← correct
```

---

## 5. End-to-end flow through the emitter

The table below shows what code is emitted at each site and which method in
`Rust.java` is responsible.

| Site | Generated Rust | Emitter method |
|------|---------------|----------------|
| Struct field declaration | `template_invoker: Option<Box<dyn InputOutputProcessor>>` | struct field loop in `emit()` — `isKnownTrait` → `"Box<dyn " + convertTypeToRust(…) + ">"` then wrapped in `Option<…>` |
| Constructor parameter | `template_invoker: impl InputOutputProcessor + 'static` | `convertTypeToRustParam` — `isKnownTrait` → `"impl " + name + " + 'static"` |
| Constructor body | `template_invoker: Some(Box::new(template_invoker))` | `emitConstructor` — `isKnownTrait(matchingField.type)` → wraps base rhs in `"Box::new(…)"` before `Some(…)` |
| Method call on stored value | `self.template_invoker.process_…(…)` | normal method call emission — `Box<dyn T>` auto-derefs to `T` |

---

## 6. Complete generated example

```rust
use crate::…::input_output_processor::InputOutputProcessor;

#[derive(Debug, Clone)]
pub struct PleadWorkflow {
    //                          ┌── heap-allocate (unknown size)
    //                          │        ┌── dynamic dispatch (vtable)
    //                          ▼        ▼
    template_instantiation: Option<Box<dyn InputOutputProcessor>>,
    inputs:  Option<Vec<Box<dyn std::any::Any>>>,
    outputs: Option<Vec<Box<dyn std::any::Any>>>,
}

impl PleadWorkflow {
    //                     ┌── static dispatch (monomorphised)
    //                     │                          ┌── must own all data
    //                     ▼                          ▼
    pub fn new(template_instantiation: impl InputOutputProcessor + 'static,
               inputs:  Vec<Box<dyn std::any::Any>>,
               outputs: Vec<Box<dyn std::any::Any>>) -> Self {
        Self {
            //                  ┌── move to heap, erase concrete type
            //                  ▼
            template_instantiation: Some(Box::new(template_instantiation)),
            inputs:  Some(inputs),
            outputs: Some(outputs),
        }
    }

    pub fn workflow(&self, …) {
        // Box<dyn T> auto-derefs — no explicit unwrap needed
        let out = self.template_instantiation.process_file_transforming_inputs(inputs);
    }
}
```

---

## 7. Design alternatives considered

### A. Store as a generic type parameter

```rust
pub struct PleadWorkflow<T: InputOutputProcessor> {
    template_instantiation: Option<T>,
}
```

**Pros:** zero-cost (no vtable, no heap allocation beyond the struct itself).
**Cons:** `T` propagates to every method, every `impl` block, and every struct
that contains a `PleadWorkflow`.  `Clone` requires `T: Clone`.  Generated code
becomes significantly more complex.

### B. Store as `Arc<dyn Trait>`

```rust
template_instantiation: Option<Arc<dyn InputOutputProcessor>>,
```

**Pros:** cheaply cloneable (reference counted), shareable across threads.
**Cons:** reference counting overhead; `Arc` implies potential shared ownership,
which is not the semantics here (the workflow owns its processor exclusively).

### C. Store as `&'a dyn Trait` with struct lifetime

```rust
pub struct PleadWorkflow<'a> {
    template_instantiation: Option<&'a dyn InputOutputProcessor>,
}
```

**Pros:** no heap allocation for the reference itself.
**Cons:** the `'a` lifetime parameter cascades to every method and every
containing struct.  The processor must outlive the workflow at the call site,
which is hard to guarantee in generated code.

### Chosen approach: `Box<dyn Trait + 'static>`

`Box<dyn Trait>` (= `Box<dyn Trait + 'static>`) is the correct default for
generated workflow code:
- No lifetime parameters on the struct.
- Single, clear ownership: the workflow owns its processor.
- Caller ergonomics: pass any owned concrete type; `Box::new` is inserted by
  the constructor, invisible to the caller.
- All generated concrete types are `'static`, so the bound is always satisfied.
