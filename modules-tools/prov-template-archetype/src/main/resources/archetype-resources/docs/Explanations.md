
# Template based generation of explanations

## 1. Approach

### 1.1. Overview

Explanation Plans (or x-plans) are a way to generate natural language explanations from instances of provenance templates. Each x-plan is a JSON object describing what to extract from a provenance graph and how to express it as a natural language sentence. All x-plans for a given domain are collected in a *library*, a JSON index file that the template service loads at startup to produce explanations for every matching provenance relation.

The pipeline proceeds through four stages, illustrated below:

![NLG pipeline](nlg-pipeline.png)

Starting from a provenance template and a set of bindings, *template instantiation* produces a concrete provenance graph. The x-plan *query* is then evaluated against that graph, yielding a set of variable bindings for each matched relation. Those bindings are combined with the x-plan's sentence specification and any associated dictionary entries to *construct an abstract syntax tree* — a language-independent, compositional representation of the sentence. Finally, *realisation* converts the abstract syntax tree into surface text using [simpleNLG](https://github.com/simplenlg/simplenlg), a Java library for surface realisation that handles morphology, agreement, and word order.

The files involved fall into four categories, described in the sections below: the library index, x-plan files, dictionary files, and profile files.

This approach follows a traditional, symbolic NLG pipeline: every step is rule-based and deterministic — given the same provenance graph and x-plans, the same sentences are always produced. This is in contrast to LLM-based approaches, which rely on recent machine learning techniques to generate natural language and may produce varying outputs for the same input. The deterministic nature of this pipeline is a deliberate design choice: it allows explanations to be generated quickly and cheaply without any dependency on a language model or external API. At the same time, the generated sentences are not a dead end — they can themselves be used as structured, factual input to further prompt an AI, combining the reliability of symbolic generation with the flexibility of large language models for downstream reasoning or elaboration.

### 1.2. Library File

A library file is a JSON object that acts as the entry point for a collection of x-plans. It has the following properties:

- `name`: a short identifier for the library (e.g. `"provbasic"` for the PROV basic library, `"fs"` for the file-system example).
- `templates`: a list of x-plan filenames contained in the same directory, each defining one explanation pattern (x-plans are defined in Section 1.3).
- `dictionaries` *(optional)*: a list of dictionary filenames that map ontology type URIs to NLG phrase structures, allowing types of entities or activities to be mapped to customised phrased components.
- `profiles` *(optional)*: a list of profile filenames that define how named roles are realised grammatically in different contexts (pronoun, possessive, noun phrase), allowing the generator to vary references to the same role across sentences (e.g. *"the borrower"*, *"their"*, *"you"*).

Example — `xplain/nlg/fs/fs-template-library.json`:

```json
{
    "name": "fs",
    "templates": ["fs-generic-responsibility.json"],
    "dictionaries": ["fs-dictionary.json"],
    "profiles": ["fs-profile.json"]
}
```

The `provbasic` library, which covers the core PROV relations, requires no dictionaries or profiles:

`xplain/nlg/provbasic/provbasic.json`:

```json
{
    "name": "provbasic",
    "templates": [
        "actedonbehalfof1.json", "activity1.json", "agent1.json",
        "alternate1.json", "association1.json", "attribution1.json",
        "derivation1.json", "end1.json", "entity1.json",
        "generation1.json", "invalidation1.json", "specialization1.json",
        "start1.json", "usage1.json", "member1.json"
    ]
}
```

### 1.3. X-Plan Files

Each x-plan file is a JSON object with the following properties:

- `query`: specifies which provenance relations to retrieve from the instantiated graph. It may be a single string or a list of clause strings forming a template query. Each clause binds a variable to a PROV type (e.g. `prov:Entity`, `prov:WasDerivedFrom`); `join`, `left join`, and `optional join` lines express how variables relate to one another; `where` lines add type filters.
- `sentence`: specifies the natural language clause to generate for each query result. It is a JSON tree of typed phrase objects (`clause`, `noun_phrase`, `verb_phrase`, `preposition_phrase`, `adjective_phrase`, etc.). Leaf values are either literal strings or `@funcall` objects that dynamically retrieve a value from the query result (e.g. the local name of an entity's URI, a plural test) or look up a domain term in a dictionary via `lookup-type`.
- `context`: maps namespace prefixes to their full URIs, scoped to this x-plan, as in PROV documents.
- `select` *(optional, not currently used)*: identifies the focus of the generated sentence.

A JSON Schema for x-plan files and library descriptors is provided at
`src/main/resources/schema/xplan-schema.json`.  It validates both forms (x-plan and
library) and mirrors the phrase-type hierarchy described in §2.1.  Run
`make schema.validate` to check all x-plan files in the repository against the schema.

Example — `xplain/nlg/provbasic/entity1.json`:

```json
{
    "select": {"ent": {"@type": "prov:Entity"}},
    "query": "select * from ent a prov:Entity",
    "sentence": {
        "type": "clause",
        "subject": {
            "type": "noun_phrase",
            "head": {
                "type": "@funcall", 
                "@object": "ent", 
                "@field": "id", 
                "@function": "localname"
            },
            "features": {
                "number": {
                    "type": "@funcall", 
                    "@object": "ent", 
                    "@field": "id", 
                    "@function": "pluralp"
                }
            }
        },
        "verb": "be",
        "object": {"type": "noun_phrase", "head": "entity", "determiner": "a"},
        "features": {"tense": "present"}
    },
    "context": {"ex": "http://example.org/#"}
}
```

### 1.4. Dictionary Files

A dictionary file maps domain-specific ontology type URIs to NLG phrase structures, allowing the sentence generator to realise the *kind* of an entity or activity in natural language. It has three top-level properties:

- `dictionary`: maps type URIs (e.g. `plead:DataFile`, `plead:FittingData`) to phrase objects. Noun phrases are used for entity types; verb phrases are used for activity types. These entries are retrieved by `@funcall` nodes of type `lookup-type` in x-plan sentence trees.
- `snippets` *(optional)*: named reusable phrase fragments whose keys are prefixed with `##` (e.g. `"##borrower-possessive"`). Snippets can be referenced by name from within dictionary entries, avoiding repetition of complex phrase structures.
- `context`: maps namespace prefixes to their full URIs, scoped to this dictionary.

Example — `xplain/nlg/fs/fs-dictionary.json`.

### 1.5. Profile Files

A profile file defines how named roles (e.g. a borrower, a company) should be realised grammatically across different contexts. Each key is a role URI, and its value is a map of named variants (e.g. first-person pronoun, second-person pronoun, noun, possessive), each specifying a set of SimpleNLG `features` (`pronominal`, `possessive`, `person`, `gender`). At runtime, the sentence generator selects a variant according to the grammatical context, enabling a reference to vary between, for example, *"the borrower"*, *"their"*, or *"you"* depending on who the explanation is addressed to.

Example — `xplain/nlg/fs/fs-profile.json`.

## 2. Details

## 2.1. NLG Abstract Syntax

The `sentence` field of an x-plan is a JSON tree that encodes a sentence as a hierarchy of *phrase nodes*. Each node is a JSON object with a `type` property that determines its role, plus type-specific properties for its constituents. The tree is evaluated at runtime to produce a [simpleNLG](https://github.com/simplenlg/simplenlg) `NLGElement`, which is then realised as surface text.

Leaf values in the tree are either **literal strings** (for fixed words) or **`@funcall` objects** (for values derived dynamically from query results or dictionary lookups — see [§ Dynamic values](#dynamic-values-funcall)).

The supported node types are summarised below.  The full machine-readable definition is the JSON Schema at `src/main/resources/schema/xplan-schema.json`, which mirrors this type hierarchy directly and can be used to validate x-plan files (see §1.3).

| Node type | Role |
|---|---|
| `paragraph` | Container for a sequence of phrases forming a multi-sentence explanation |
| `clause` | A full clause; the root of a single-sentence tree |
| `coordinated_phrase` | Two or more coordinated phrases joined by a conjunction (e.g. *and*) |
| `noun_phrase` | A noun phrase: subject, object, or complement of a preposition |
| `verb_phrase` | A verb phrase used inline as the `verb` of a clause |
| `preposition_phrase` | A prepositional phrase: modifier, front-modifier, or complement |
| `adjective_phrase` | An adjectival modifier on a noun phrase |
| `adverb_phrase` | An adverbial modifier on a verb or clause |
| `@funcall` | Dynamic leaf — computes a value at runtime from query bindings or dictionary lookups |
| `string` | A literal string phrase wrapping a fixed word or phrase |

Not every property slot accepts every node type. The property tables below use the following **named type sets** to express which node types are allowed in each position:

| Type set | Permitted node types | Typical use |
|---|---|---|
| **`Phrase`** | Any node type in the table above, plus `@iterator` | General-purpose slot that places no restriction on what kind of phrase it contains (modifier arrays, complements, etc.) |
| **`NounForm`** | `noun_phrase` \| `coordinated_phrase` \| `@funcall` \| `string` | Slots that fill a noun position: the `subject` of a clause, the `noun` of a preposition phrase, the `specifier` of a noun phrase |
| **`VerbForm`** | `verb_phrase` \| `coordinated_phrase` \| `@funcall` \| `string` | Slots that fill a verb position: the `verb` of a clause |
| **`HeadForm`** | `@funcall` \| `string` | Slots that supply the lexical head word of a phrase: the `head` of `noun_phrase`, `adjective_phrase`, `adverb_phrase` |
| **`Head`** | Literal `string` only | The `head` of `verb_phrase` and the `conjunction` of `coordinated_phrase` — fixed words, not computed nodes |

---

### paragraph

A `paragraph` is a top-level container that wraps a sequence of clauses into a multi-sentence explanation. It is used when a single provenance relation warrants more than one sentence.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"paragraph"` | yes | Node discriminator |
| `items` | array of **Phrase** | yes | The phrases forming the paragraph, realised in order |
| `properties` | object | no | Metadata (currently unused; pass `{}`) |

**Example** (`plead.cs-6b.json` — abbreviated):
```json
{
  "type": "paragraph",
  "items": [
    { "type": "clause", "subject": { ... }, "verb": { ... }, "object": { ... }, "complements": [ ... ] }
  ],
  "properties": {}
}
```

---

### clause

Represents a full clause (subject + verb ± object ± modifiers). The root of a single-sentence tree, or an item within a `paragraph` or `complements` array.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"clause"` | yes | Node discriminator |
| `subject` | **NounForm** | no | The grammatical subject (`noun_phrase`, `coordinated_phrase`, `@funcall`, or `string`). Omit for passive clauses where the subject is suppressed. May be a snippet reference string (e.g. `"##company"`) |
| `verb` | **VerbForm** | yes | The main verb: a bare `string` (base form), `verb_phrase`, `coordinated_phrase`, or a `lookup-type` `@funcall` returning a verb_phrase from the dictionary |
| `object` | **Phrase** | no | The grammatical object — any phrase node |
| `indirect_object` | **Phrase** | no | An indirect object — any phrase node (often a `preposition_phrase`, e.g. *"because of the reason"*) |
| `complements` | array of **Phrase** | no | Clausal or prepositional complements of the whole clause. A `clause` complement introduces a subordinate clause via `complementiser` (e.g. *"because …"*, *"which …"*, *"that …"*) |
| `modifiers` | array of **Phrase** | no | Adjunct phrases after the verb and object (e.g. *"at time T"*, *"from X"*) |
| `pre-modifiers` | array of **Phrase** | no | Phrases placed before the subject |
| `post-modifiers` | array of **Phrase** | no | Phrases placed after the verb phrase and all other elements (e.g. passive adjuncts such as *"with method M"*, *"from file F"*) |
| `front-modifiers` | array of **Phrase** | no | Phrases fronted before the subject (e.g. *"on behalf of …"*) |
| `complementiser` | string | no | Subordinating conjunction introducing this clause when it appears as a complement (e.g. `"because"`, `"that"`, `"which"`, `"because of"`). Present only on nested clause nodes inside `complements` arrays |
| `features` | features object | no | Grammatical features: tense, voice (see [§ Features](#features)) |

**Active clause** (`actedonbehalfof1.json`):
```json
{
  "type": "clause",
  "subject": { "type": "noun_phrase",
               "head": { "type": "@funcall", "@object": "aobo", "@field": "delegate", "@function": "localname" },
               "features": { "number": { "type": "@funcall", "@object": "aobo", "@field": "delegate", "@function": "pluralp" } } },
  "verb": "act",
  "modifiers": [ { "type": "preposition_phrase", "preposition": "on behalf of",
                   "noun": { "type": "@funcall", "@object": "aobo", "@field": "responsible", "@function": "localname" } } ],
  "features": { "tense": "past" }
}
```
*Realises as:* **X acted on behalf of Y.**

**Passive clause** (`association1.json` — subject suppressed):
```json
{
  "type": "clause",
  "object": { "type": "noun_phrase", "head": { "type": "@funcall", "@object": "waw", "@field": "activity", "@function": "localname" } },
  "verb": "associate",
  "modifiers": [ { "type": "preposition_phrase", "preposition": "with",
                   "noun": { "type": "@funcall", "@object": "waw", "@field": "agent", "@function": "localname" } } ],
  "features": { "tense": "past", "passive": "true" }
}
```
*Realises as:* **X was associated with Y.**

**Clause with subordinate complement** (`plead.cs-6b.json` — abbreviated):
```json
{
  "type": "clause",
  "subject": { "type": "noun_phrase", "determiner": "The", "head": "recommendation",
               "post-modifiers": [ { "type": "preposition_phrase", "preposition": "of",
                                     "noun": { "type": "noun_phrase", "determiner": "the", "head": "system" } } ] },
  "verb": { "type": "verb_phrase", "head": "was",
            "post-modifiers": [ { "type": "preposition_phrase", "preposition": "to", "noun": "refuse" } ] },
  "object": { "type": "noun_phrase", "head": "application", "specifier": "##borrower-possessive" },
  "complements": [
    { "type": "clause", "complementiser": "because",
      "indirect_object": { "type": "preposition_phrase", "preposition": "of",
                           "noun": { "type": "@funcall", "@object": "recommendation",
                                     "@property": "pl:reason", "@function": "lookup-type", "@arg1": "noun_phrase" } } }
  ],
  "features": { "tense": "past" }
}
```
*Realises as:* **The recommendation of the system was to refuse the borrower's application because of [reason].**

---

### noun_phrase

Represents a noun phrase. Used for subjects, objects, and complements of prepositions.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"noun_phrase"` | yes | Node discriminator |
| `head` | **HeadForm** | yes | The head noun: a literal `string` or a `@funcall` producing a string or full phrase |
| `determiner` | string | no | Determiner, e.g. `"a"`, `"the"` |
| `specifier` | **NounForm** | no | A pre-built snippet or possessive phrase inserted before the head (`noun_phrase`, `coordinated_phrase`, `@funcall`, or `string`; snippet refs are strings prefixed with `##`, e.g. `"##borrower-possessive"`) |
| `modifiers` | array of **Phrase** | no | Mid-position modifiers |
| `pre-modifiers` | array of **Phrase** | no | Phrases placed before the head (adjectives, participial phrases, etc.) |
| `post-modifiers` | array of **Phrase** | no | Phrases placed after the head (e.g. `preposition_phrase` *"of the activity"*, `adjective_phrase` post-modifier, `@funcall` identity suffix) |
| `complements` | array of **Phrase** | no | Relative clauses or other complements modifying the noun, each introduced by a `complementiser` (e.g. *"the data **that the company excluded**"*) |
| `features` | features object | no | Grammatical features, primarily `number` |

**Post-modifier example** (`end2.json`):
```json
{
  "type": "noun_phrase",
  "head": "end",
  "determiner": "the",
  "post-modifiers": [ { "type": "preposition_phrase", "preposition": "of",
                        "noun": { "type": "@funcall", "@object": "web", "@field": "activity", "@function": "localname" } } ],
  "features": { "number": "singular" }
}
```
*Realises as:* **the end of X.**

**Relative clause complement example** (`exclusion1.json`):
```json
{
  "type": "noun_phrase",
  "determiner": "the",
  "head": "data",
  "complements": [
    { "type": "clause", "complementiser": "that",
      "subject": "##company",
      "verb": "exclude",
      "features": { "tense": "past" },
      "complements": [ { "type": "preposition_phrase", "preposition": "for the processing of",
                         "noun": { "type": "noun_phrase", "head": "loan application", "specifier": "##borrower-possessive" } } ] }
  ]
}
```
*Realises as:* **the data that the company excluded for the processing of the borrower's loan application.**

---

### verb_phrase

An inline verb phrase used as the `verb` of a clause. Allows constructing compound or modal verb constructions that go beyond a simple base-form string.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"verb_phrase"` | yes | Node discriminator |
| `head` | **Head** (string) | yes | The main verb or auxiliary (e.g. `"was"`, `"have"`, `"select"`) — a literal string only |
| `object` | **Phrase** | no | The direct object of this verb phrase |
| `indirect_object` | **Phrase** | no | An indirect object |
| `modifiers` | array of **Phrase** | no | Mid-position modifiers |
| `pre-modifiers` | array of **Phrase** | no | Phrases placed before the verb head |
| `post-modifiers` | array of **Phrase** | no | Phrases placed after the verb head (e.g. a `to`-infinitive `preposition_phrase`: *"was **to refuse**"*, or an `adverb_phrase`) |
| `complements` | array of **Phrase** | no | Complements of the verb phrase |
| `features` | features object | no | Grammatical features: tense, modal, form, perfect, passive |

**Example** (`plead.cs-6b.json`):
```json
{
  "type": "verb_phrase",
  "head": "was",
  "post-modifiers": [
    { "type": "preposition_phrase", "preposition": "to", "noun": "refuse" }
  ]
}
```
*Realises as (within clause):* **was to refuse.**

---

### preposition_phrase

Represents a prepositional phrase. Used as a modifier, front-modifier, complement, or the `noun` slot of another prepositional phrase.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"preposition_phrase"` | yes | Node discriminator |
| `preposition` | string | yes | The preposition string, e.g. `"from"`, `"at"`, `"on behalf of"`, `"generated by"`, `"for the processing of"` |
| `noun` | **NounForm** | yes | The complement of the preposition: `noun_phrase`, `coordinated_phrase`, `@funcall`, or a literal `string` |
| `complements` | array of **Phrase** | no | Additional complements of the prepositional phrase |
| `specifier` | `noun_phrase` | no | A specifier for the phrase |
| `features` | features object | no | Grammatical features |

---

### adjective_phrase

Represents an adjectival modifier on a noun phrase.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"adjective_phrase"` | yes | Node discriminator |
| `head` | **HeadForm** | yes | The adjective head: a literal `string` or a `@funcall` |
| `modifiers` | array of **Phrase** | no | Mid-position modifiers |
| `pre-modifiers` | array of **Phrase** | no | Phrases placed before the head |
| `post-modifiers` | array of **Phrase** | no | Further modifiers after the head (e.g. `preposition_phrase` *"approved **by the manager**"*) |
| `features` | features object | no | Grammatical features |

---

### adverb_phrase

Represents an adverbial modifier. Used as a `post-modifier` on a verb phrase, or returned by a dictionary `lookup-type` to modify a clause. Appears in dictionaries mapping boolean or categorical attribute values to adverbs.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"adverb_phrase"` | yes | Node discriminator |
| `head` | **HeadForm** | yes | The adverb head: a literal `string` or a `@funcall` (e.g. `"positively"`, `"negatively"`, `"hence"`) |
| `modifiers` | array of **Phrase** | no | Mid-position modifiers |
| `pre-modifiers` | array of **Phrase** | no | Phrases placed before the head |
| `post-modifiers` | array of **Phrase** | no | Phrases placed after the head |
| `features` | features object | no | Grammatical features |

**Example** (`collabmap-dictionary.json`):
```json
"collabmap:yes": { "type": "adverb_phrase", "head": "positively" },
"collabmap:no":  { "type": "adverb_phrase", "head": "negatively" }
```
These entries are returned by a `lookup-type` @funcall and inserted as adverbial modifiers in the enclosing clause.

---

### coordinated_phrase

Joins two or more phrases with a coordinating conjunction. Supports both a static list of coordinates and a dynamic iterator over query results.

| Property | Type | Required | Description |
|---|---|---|---|
| `type` | `"coordinated_phrase"` | yes | Node discriminator |
| `conjunction` | string | yes | The coordinating conjunction, e.g. `"and"`, `"or"` |
| `coordinates` | array of **Phrase** | one of `coordinates` or `@iterator` | Static list of phrases to coordinate — any phrase type |
| `@iterator` | @iterator object | one of `coordinates` or `@iterator` | Dynamic iteration over query result values to build the coordinates at runtime (see below) |
| `post-modifiers` | array of **Phrase** | no | Phrases placed after the coordinated phrase |
| `features` | features object | no | Grammatical features (e.g. `negated`) |

**Static example**:
```json
{
  "type": "coordinated_phrase",
  "conjunction": "and",
  "coordinates": [
    { "type": "clause", ... },
    { "type": "clause", ... }
  ]
}
```

**Dynamic example** (`exclusion1.json` — abbreviated):
```json
{
  "type": "coordinated_phrase",
  "conjunction": "and",
  "@iterator": {
    "type": "@iterator",
    "@variable": "@values",
    "@element": { "type": "@funcall", "@clause": "coordinates", "@function": "difference-lookup-attribute", "@args": [ ... ] }
  }
}
```

The `@iterator` object iterates over a set of values bound by the query and instantiates `@element` for each, collecting the results as the `coordinates`.

| @iterator property | Description |
|---|---|
| `@variable` | Comma-separated query variable name(s) to iterate over, or `"@values"` for the full result set |
| `@element` | The phrase template (any **Phrase**) to instantiate for each value |
| `@clause` | Target slot for each instantiated element (e.g. `"coordinates"` to fill a `coordinated_phrase`) |
| `@property` | Property name to iterate over (used when iterating over attribute values rather than variable bindings) |
| `@flatten` | `"true"` — flatten a nested array of coordinates before collecting |
| `@from` | Start index (integer string) for slicing the iteration range |
| `@until` | End index (integer string, exclusive) for slicing the iteration range |
| `@iterator` | A nested `@iterator` object for two-level iteration |

---

### Dynamic values: @funcall

A `@funcall` object is a leaf node that computes its value at runtime from query result bindings, dictionary lookups, or profile data. It may appear wherever a phrase, string, or features value is expected.

| Property | Description |
|---|---|
| `@function` | The function to invoke (see table below) |
| `@object` | Query variable to read from (e.g. `"ent"`, `"recommendation"`) |
| `@field` | Named **structural field** of a PROV relation — see table below. Exactly one of `@field` or `@property` must be present. |
| `@property` | Namespace-qualified **typed attribute** of a PROV statement (e.g. `"prov:type"`, `"fs:filename"`). The prefix must be declared in the x-plan `context`. Exactly one of `@field` or `@property` must be present. |
| `@arg1` | First function argument (semantics are function-dependent) |
| `@arg2` | Second function argument (semantics are function-dependent) |
| `@args` | Array of arguments for functions that take more than two (alternative to `@arg1`/`@arg2`) |
| `@key` | When set, the @funcall replaces the value at the named key in its containing object rather than replacing the whole node (used by `profile-features` to inject a features object) |
| `@optional` | `"true"` — silently omit the enclosing phrase if this value is absent in the query result |

#### `@field` vs `@property`

These two keys select **what data** is extracted from the query variable bound to `@object`. They are mutually exclusive.

**`@field`** accesses a structural role of a PROV relation — the roles defined by the PROV data model such as identifiers, referenced entities, activities, agents, or timestamps:

| `@field` value | Available on | Description |
|---|---|---|
| `id` | all | The URI identifier of the PROV element |
| `entity` | `WasGeneratedBy`, `WasAttributedTo`, `Used`, `WasInvalidatedBy` | The entity role |
| `activity` | `WasGeneratedBy`, `Used`, `WasAssociatedWith`, `WasDerivedFrom`, `WasStartedBy`, `WasEndedBy` | The activity role |
| `agent` | `WasAssociatedWith`, `WasAttributedTo`, `ActedOnBehalfOf` | The agent role |
| `plan` | `WasAssociatedWith` | The `prov:hadPlan` entity — the method, algorithm, or script used |
| `generatedEntity` | `WasDerivedFrom` | The derived (output) entity |
| `usedEntity` | `WasDerivedFrom` | The source (input) entity |
| `delegate` | `ActedOnBehalfOf` | The delegating agent |
| `responsible` | `ActedOnBehalfOf` | The responsible agent |
| `collection` | `HadMember` | The collection entity |
| `time` | `WasGeneratedBy`, `Used`, `WasStartedBy`, `WasEndedBy` | Timestamp |

**`@property`** accesses a **typed attribute** declared on a PROV entity or activity — the key-value pairs in brackets in PROV-N notation, such as `entity(e1, [prov:type = 'X', fs:filename = "foo.txt"])`. The property name is written as a prefixed URI (`prefix:localname`), and the prefix must be in the x-plan `context`:

```json
{ "type": "@funcall", "@object": "file2",
  "@property": "fs:filename", "@function": "string" }
```

```json
{ "type": "@funcall", "@object": "recommendation",
  "@property": "pl:reason", "@function": "lookup-type", "@arg1": "noun_phrase" }
```

The `@property` path returns the **value** of the attribute (a string literal, URI, or typed value). The `@field` path returns a **QualifiedName** (for referenced elements like `activity`, `agent`, `plan`) or the full ID URI (for `id`).

#### Built-in functions

| Function | Input via | Returns | Description |
|---|---|---|---|
| `localname` | `@field` | string | The local name of a URI (e.g. `file:1549` → `"1549"`). Typical use: head noun or verb |
| `identity` | `@field` or `@property` | string | Full string representation: prefixed URI for `@field` (e.g. `"file:1549"`), or the literal value for `@property` (e.g. `"myfile.txt.gz"`) |
| `string` | `@property` | string | The unwrapped string value of a typed literal attribute (e.g. `"myfile.txt.gz"` from `fs:filename = "myfile.txt.gz" %% xsd:string`) |
| `pluralp` | `@field` | `"plural"` \| `"singular"` | Heuristic plural test on the local name of `@field`. Used as the value of `features.number` |
| `timestring` | `@field` | string | Formats an ISO timestamp as a readable time string. Typically paired with `@optional: "true"` |
| `noun+localname` | `@field` | string | Produces `"<arg1> (<localname>)"` — e.g. `"file (1549)"`. `@arg1` is the role label |
| `noun+identity` | `@field` | string | Produces `"<arg1> (<prefixed-id>)"` — e.g. `"file (file:1549)"`, `"engineer (ag:41)"`. `@arg1` is the role label. Use `@arg1: ""` to emit only the parenthesised id as a post-modifier |
| `lookup-type` | `@property` | noun_phrase \| verb_phrase \| adverb_phrase | Looks up the value of `@property` in the dictionary; `@arg1` is the expected output type, `@arg2` is an optional comma-separated namespace prefix filter |
| `lookup-attribute` | `@property` | phrase | Looks up an attribute value in the dictionary |
| `profile-features` | — | features object | Returns the grammatical features object for a named role from the active profile; `@arg1` is the role URI. Used via `@key: "features"` to inject features into a snippet noun phrase |
| `markup-for-id` | `@field` | object | Returns HTML markup attributes (`data-id`, `class`) for in-browser navigation. Used with `head_markup_attributes` in features. `@arg1` is the CSS class (typically `"provelement"`) |
| `flatten` | `@property` | array | Flattens a nested array of attributes. Used as input to iterator-based @funcalls |

---

### Features

The `features` object controls grammatical morphology. It may appear on `clause`, `noun_phrase`, or `verb_phrase` nodes. The `"type": "features"` discriminator is optional but may be present.

| Key | Values | Applies to | Description |
|---|---|---|---|
| `tense` | `"past"`, `"present"` | clause, verb_phrase | Verb tense. PROV relations use `"past"`; definitional sentences use `"present"`. |
| `passive` | `"true"`, `"false"` | clause | Voice. When `"true"`, simpleNLG promotes the logical `object` to surface subject ("The file was compressed…") and moves the logical `subject` to a "by…" phrase automatically. Set `subject` to the logical agent and `object` to the patient; add other adjuncts as `post-modifiers` rather than `complements` to ensure correct word order. |
| `number` | `"singular"`, `"plural"`, or a `pluralp` @funcall | noun_phrase | Agreement number. A `pluralp` @funcall determines it at runtime. |
| `modal` | string, e.g. `"would"` | verb_phrase | Modal auxiliary to prefix the verb (e.g. *"would have"*). |
| `form` | see below | verb_phrase | Explicit verb form, overriding default morphological inflection. |

The supported `form` values correspond directly to the simpleNLG `Form` enum:

| `form` value | simpleNLG `Form` | Effect |
|---|---|---|
| `"bareInfinitive"` | `BARE_INFINITIVE` | Base form without *to* (e.g. *select*, *generate*). Use on `verb_phrase` nodes inside a `coordinated_phrase` that is the verb of an infinitive complement clause, to prevent unwanted 3rd-person conjugation |
| `"gerund"` | `GERUND` | Gerund / noun form (e.g. *selecting*) |
| `"imperative"` | `IMPERATIVE` | Imperative mood (e.g. *select!*) |
| `"infinitive"` | `INFINITIVE` | Full infinitive including *to* (e.g. *to select*). Do not combine with a `complementiser: "to"` clause, which already adds *to* |
| `"normal"` | `NORMAL` | Base form, same as bare infinitive in most contexts |
| `"pastParticiple"` | `PAST_PARTICIPLE` | Past participle (e.g. *selected*, *generated*) |
| `"presentParticiple"` | `PRESENT_PARTICIPLE` | Present participle / gerund form (e.g. *selecting*) |
| `perfect` | `true` | verb_phrase | Renders the verb in perfect aspect (e.g. *"has derived"*). |
| `pronominal` | `"true"`, `"false"` | noun_phrase | Whether to realise as a pronoun. Set by `profile-features`. |
| `possessive` | `"true"`, `"false"` | noun_phrase | Whether to realise in possessive form. Set by `profile-features`. |
| `person` | `"first"`, `"second"`, `"third"` | noun_phrase | Grammatical person. Set by `profile-features`. |
| `gender` | `"masculine"`, `"feminine"` | noun_phrase | Grammatical gender. Set by `profile-features`. |
| `negated` | boolean | clause, verb_phrase, coordinated_phrase | Negates the phrase. On a `clause` or `coordinated_phrase`, the negation is passed down to the underlying verb phrase. Negating a `coordinated_phrase` negates all coordinates. |
| `head_markup_element` | string, e.g. `"span"` | noun_phrase | HTML element to wrap the realised head noun for in-browser rendering. |
| `head_markup_attributes` | @funcall \| object | noun_phrase | HTML attributes on the `head_markup_element`, typically produced by a `markup-for-id` @funcall. |

---

### Missing or unsupported constructs

| Construct | Status | Notes |
|---|---|---|
| Interrogative / question forms | Not supported | No mood feature beyond tense and passive |
| Coreference / pronoun substitution in x-plans | Partially supported | Profiles (§ 1.5) and `profile-features` support pronoun variants in dictionary snippets; however, there is no mechanism to invoke a profile directly from within an x-plan sentence tree without going through a snippet reference (`specifier`) |

## 2.2 The Syntax of Queries

The `query` field of an x-plan is written in **ProvQL**, a declarative language designed to navigate provenance graphs produced by template instantiation. ProvQL has a relational feel — variables are bound to PROV relations and nodes, and joins navigate between them — but its relations are the PROV relations rather than database tables.

A query is either a single string (for simple queries) or a JSON array of strings that are concatenated in order (for multi-line queries). The result is a set of *variable bindings*: one row per match, each row providing the values that the `sentence` tree uses to fill in `@funcall` nodes.

---

### Overall structure

A query consists of the following clauses, in order:

```
[prefix <prefix> <uri>]*
select *
from <var> a <ProvType>
[from <var> a <ProvType> [join | left join | optional join <lhs>.<field> = <rhs>.<field>]]*
[where <condition> [and|or <condition>]*]
[group by <var>[, <var>]* aggregate <var>[, <var>]* with <AggFunc>]
```

All clauses after `select *` are optional except the first `from`.

---

### Prefix declarations

```
prefix <prefix> <uri>
```

Declares a namespace prefix used in type names and attribute values elsewhere in the query. Must appear before `select`. There is no terminating punctuation.

```
prefix ln <https://plead-project.org/ns/loan#>
prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>
```

---

### select

```
select *
```

Currently only wildcard selection is supported. All bound variables and their fields are returned.

---

### from

```
from <var> a <ProvType>
```

Binds variable `<var>` to every instance of `<ProvType>` in the provenance graph. The keyword `a` is shorthand for `rdf:type`, borrowed from Turtle notation. Multiple `from` clauses are evaluated in sequence; each one extends the set of bound variables.

The first `from` clause appears on the same line as `select *`:

```
select * from ent a prov:Entity
```

Subsequent `from` clauses are joined to previously bound variables (see [§ join](#join) below):

```
select * from der a prov:WasDerivedFrom
from decision a prov:Entity
 join der.generatedEntity = decision.id
```

---

### join

```
join <lhs>.<field> = <rhs>.<field>
```

Constrains the variable introduced by the immediately preceding `from` clause by equating one of its fields with a field of an already-bound variable.

**Binding rule:** The left-hand side `<lhs>` **must refer to a variable that is already bound** by a prior `from` clause. The right-hand side `<rhs>` is the variable being newly introduced (the one from the immediately preceding `from`). This restriction ensures that the join is always driven by a known value and never requires a cross-product scan.

```
from der1 a prov:WasDerivedFrom        -- der1 is now bound
from file2 a prov:Entity
 join der1.generatedEntity = file2.id  -- LHS der1 is bound; binds file2
from file1 a prov:Entity
 join der1.usedEntity = file1.id       -- LHS der1 is bound; binds file1
from act0 a prov:Activity
 join der1.activity = act0.id          -- LHS der1 is bound; binds act0
```

The already-bound variable may equally well appear on the left as the subject of a previously bound entity, as long as the constraint is satisfied:

```
from wat1 a prov:WasAttributedTo
 join file2.id = wat1.entity           -- LHS file2 (bound earlier); binds wat1
```

---

### left join

```
left join <lhs>.<field> = <rhs>.<field>
```

Identical to `join` but optional: if no match exists for `<rhs>`, the row is kept with `<rhs>` unbound (null). Used when a role within a relation may be absent but the relation itself is guaranteed to be present.

```
from aobo1 a prov:ActedOnBehalfOf
 left join engineer.id = aobo1.delegate  -- optional; aobo1 may not match
```

The same LHS-bound rule applies. Note: if `<rhs>` is left unbound by a `left join`, any subsequent `join` or `left join` that references `<rhs>` as its LHS will fail. Use `optional join` instead when the unbound variable may propagate.

---

### optional join

```
optional join <lhs>.<field> = <rhs>.<field>
```

Like `left join`, but with null-safe propagation. If no match exists for `<rhs>`, the row is kept with `<rhs>` unbound. Unlike `left join`, any subsequent `optional join` whose LHS references an unbound variable will also leave its own RHS variable unbound rather than failing. This makes it safe to chain optional navigation steps:

```
from waw a prov:WasAssociatedWith
 optional join act.id = waw.activity   -- waw may be absent entirely
from agent a prov:Agent
 optional join waw.agent = agent.id    -- safe even if waw is unbound
from plan a prov:Entity
 optional join waw.plan = plan.id      -- safe even if waw is unbound
```

If `waw` is unbound (no `WasAssociatedWith` exists), then `agent` and `plan` are also unbound, and all three `@optional`-marked `@funcall` nodes in the sentence tree suppress their phrases. Use `optional join` whenever the relation itself may be absent or when null must propagate through a chain.

---

### where

```
where <condition> [and <condition>]* [or <condition>]*
```

Filters the result set after all joins have been applied. Two condition forms are supported:

**Type constraint** — tests whether a PROV node carries a specific type attribute. The `>=` operator means *is an instance of, or a subtype of*, supporting type hierarchy subsumption:

```
where file2[prov:type] >= 'plead:DataFile'
  or  file2[prov:type] >= 'plead:AccuracyScore'
```

**Field equality** — tests whether a field equals a literal value:

```
where decision.id >= 'app:applications/437/decision'
```

Attribute access uses bracket notation `var[namespace:property]`; relational field access uses dot notation `var.field`.

---

### group by / aggregate

```
group by <var>[, <var>]* aggregate <var>[, <var>]* with <AggFunc>
```

Groups rows by the listed variables and collects the values of the aggregated variables into a sequence or count. Used when multiple matches (e.g. a collection of entities derived through a chain) should produce a single sentence rather than one sentence per row.

| Aggregate function | Description |
|---|---|
| `Seq` | Collects values into an ordered sequence |
| `Count` | Counts the number of distinct values |

```
group by recommendation, features aggregate data with Seq
group by pipeline aggregate ancestor, act with Count
```

---

### PROV relation fields

Each PROV type bound in a `from` clause exposes a fixed set of fields accessible via dot notation in joins and `where` conditions.

| PROV type | Fields |
|---|---|
| `prov:Entity` | `id` |
| `prov:Activity` | `id`, `startTime`, `endTime` |
| `prov:Agent` | `id` |
| `prov:WasDerivedFrom` | `generatedEntity`, `usedEntity`, `activity` |
| `prov:WasGeneratedBy` | `entity`, `activity`, `time` |
| `prov:Used` | `activity`, `entity`, `time` |
| `prov:WasInvalidatedBy` | `entity`, `activity`, `time` |
| `prov:WasStartedBy` | `activity`, `trigger`, `starter`, `time` |
| `prov:WasEndedBy` | `activity`, `trigger`, `ender`, `time` |
| `prov:WasAttributedTo` | `entity`, `agent` |
| `prov:WasAssociatedWith` | `activity`, `agent` |
| `prov:ActedOnBehalfOf` | `delegate`, `responsible` |
| `provext:HadMember` | `collection`, `element` |
| `provext:AlternateOf` | `alternate1`, `alternate2` |
| `provext:SpecializationOf` | `specificEntity`, `generalEntity` |
| `provext:WasDerivedFromStar` | Same fields as `prov:WasDerivedFrom` (reflexive-transitive closure) |
| `provext:WasDerivedFromPlus` | Same fields as `prov:WasDerivedFrom` (transitive closure, excluding identity) |

In addition, all node types support attribute access via `var[namespace:property]` for arbitrary RDF properties (most commonly `prov:type`).

---

### Examples

**Minimal — single relation:**
```
select * from ent a prov:Entity
```

**Two-step derivation:**
```
select * from der a prov:WasDerivedFrom
from decision a prov:Entity
 join der.generatedEntity = decision.id
from application a prov:Entity
 join der.usedEntity = application.id
```
*Binds each derivation relation together with the entity it generated and the entity it used.*

**Multi-step chain with type filter and optional responsibility:**
```
prefix plead <https://plead-project.org/ns/plead#>
prefix sk    <https://scikit-learn.org/stable/modules/generated/sklearn.>
select * from der1 a prov:WasDerivedFrom
from file2 a prov:Entity
 join der1.generatedEntity = file2.id
from file1 a prov:Entity
 join der1.usedEntity = file1.id
from act0 a prov:Activity
 join der1.activity = act0.id
from wat1 a prov:WasAttributedTo
 join file2.id = wat1.entity
from engineer a prov:Agent
 join wat1.agent = engineer.id
from aobo1 a prov:ActedOnBehalfOf
 left join engineer.id = aobo1.delegate
where file2[prov:type] >= 'plead:DataFile'
  or  file2[prov:type] >= 'plead:AccuracyScore'
  or  file2[prov:type] >= 'sk:Pipeline'
```

**Transitive closure with aggregation:**
```
prefix sk <https://scikit-learn.org/stable/modules/generated/sklearn.>
prefix ln <https://plead-project.org/ns/loan#>
select * from pipeline a prov:Entity
from wdf a provext:WasDerivedFromStar
 join pipeline.id = wdf.generatedEntity
from ancestor a prov:Entity
 join wdf.usedEntity = ancestor.id
from wgb a prov:WasGeneratedBy
 join ancestor.id = wgb.entity
from act a prov:Activity
 join wgb.activity = act.id
where pipeline[prov:type] >= 'sk:pipeline.Pipeline'
  and ancestor[prov:type] >= 'ln:File'
group by pipeline aggregate ancestor, act with Seq
```
*For each approved pipeline, collects all ancestor files and the activities that generated them into sequences, producing one sentence per pipeline.*

## 2.3. Contexts and Namespaces

## 2.4. Dictionary and Dictionary Lookups

## 3. Configuration

Explanations x-plans, dictionaries, and profiles need to be in files on the file system and accessible to the Template Service.  
In this archetype, some explanation files are already included in the [ProvToolbox: modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/resources/xplain/nlg](src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/resources/xplain/nlg)(../__rootArtifactId__-service/src/main/resources/xplain/nlg)
`src/main/xplain/nlg` folder, 


There is also a configuration file `template-config.json` (/Users/luc/IdeaProjects/ProvToolbox/modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/src/test/config/template-config.json), acting as general configuration file, that needs to be found by the service.
The `template-config.json` file is read by the service when deployed. It has the following structure with the last two properties
`nlg.xplan.library` and `nlg.xplan.selection` being used to configure the x-plan library and the selection of x-plans to use from that library.

```json
{
    "catalogue.package": "${pom.catalogue.package}",
    "jdbc.url": "${pom.jdbc.url}",
    "sql.initializer": "/META-INF/resources/webjars/${pom.the.template.library}/${pom.the.template.library.version}/sql/prov-template-library${pom.the.template.sublibrary}.sql",
    "nlg.xplan.library": "${pom.nlg.xplan.library}",
    "nlg.xplan.selection": ${pom.nlg.xplan.selection}
}
```
Given this is an archetype, the values of these properties need to be instantiated the archetype is itself instantiated.



File

/Users/luc/IdeaProjects/ProvToolbox/modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/pom.xml

```xml
<pom.nlg.xplan.library>/xplain/nlg/provbasic/provbasic.json</pom.nlg.xplan.library> <!-- /xplain/nlg/xplans-config.json -->
<pom.nlg.xplan.selection>["actedonbehalfof1", "activity1", "agent1", "alternate1", "association1", "attribution1", "derivation1", "end1", "entity1", "generation1", "invalidation1", "specialization1", "start1", "usage1", "member1"]</pom.nlg.xplan.selection>
```

Folder [ProvToolbox: modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/resources/xplain/nlg](/Users/luc/IdeaProjects/ProvToolbox/modules-tools/prov-template-archetype/src/main/resources/archetype-resources/__rootArtifactId__-service/src/main/resources/xplain/nlg) for example xplan libraries.

## 4. Bank of Examples

Each example below shows a complete x-plan: the target sentence, the provenance model it assumes, the ProvQL query, and the NLG sentence tree with annotations on the constructs used.

---

### 4.1. Awk column selection

**Target sentence**

> Engineer (ag:41) applied awk script (fs:some-script-id) to select some columns of file (file:14) and generate file (file:1549).

**Provenance model**

```
file2  prov:wasDerivedFrom  file1  (via activity act, relation der)
act    prov:wasAssociatedWith  agent  (relation waw, with plan)
act    prov:type  fs:SelectingData
```

**Key constructs illustrated**

| Construct | Where used |
|---|---|
| Active past-tense clause with subject | Main clause: *Engineer (ag:41) applied …* |
| `noun+identity` for agent | Subject: *Engineer (ag:41)* |
| `noun+identity` for activity (method id) | Object: *awk script (fs:some-script-id)* |
| `noun+identity` for files | File references: *file (file:14)*, *file (file:1549)* |
| Infinitive complement clause (`complementiser: "to"`) | *… to [select … and generate …]* |
| `coordinated_phrase` of two `verb_phrase` nodes | *select … **and** generate …* |
| `"form": "bareInfinitive"` on verb_phrase coordinates | Prevents 3rd-person conjugation inside infinitive complement |
| `"number": "plural"` on noun_phrase | *some **columns*** |
| `markup-for-id` for in-browser navigation | `<span>` wrapping each file identity |

**Query** — starts from `prov:WasDerivedFrom`, navigates to both files and the activity, then to the associated agent. The `where` clause restricts to activities of type `fs:SelectingData`. All joins respect the LHS-bound rule.

```
prefix fs <http://openprovenance.org/ns/fs#>
select * from der a prov:WasDerivedFrom
from file2 a prov:Entity
 join der.generatedEntity = file2.id
from file1 a prov:Entity
 join der.usedEntity = file1.id
from act a prov:Activity
 join der.activity = act.id
from waw a prov:WasAssociatedWith
 join act.id = waw.activity
from agent a prov:Agent
 join waw.agent = agent.id
from plan a prov:Entity
 join waw.plan = plan.id
where act[prov:type] >= 'fs:SelectingData'
```

**Sentence tree**

```
clause  [tense: past]
├── subject: noun_phrase
│     └── head: noun+identity(agent.id, "engineer")  → "Engineer (ag:41)"
├── verb: "apply"                                      → "applied"
├── object: noun_phrase
│     └── head: noun+identity(plan.id, "awk script")  → "awk script (fs:some-script-id)"
└── complements:
    └── clause  [complementiser: "to"]                 → "to …"
        └── verb: coordinated_phrase  [conjunction: "and"]
            ├── verb_phrase  [form: bareInfinitive]
            │   ├── head: "select"
            │   └── object: noun_phrase  [number: plural]
            │         ├── determiner: "some"
            │         ├── head: "columns"
            │         └── post-modifiers:
            │             └── preposition_phrase "of"
            │                 └── noun: noun+identity(file1.id, "file")  → "file (file:14)"
            │                          [head_markup_element: span]
            └── verb_phrase  [form: bareInfinitive]
                ├── head: "generate"
                └── object: noun+identity(file2.id, "file")              → "file (file:1549)"
                           [head_markup_element: span]
```

**File:** [`xplain/nlg/fs/fs-awk-column-selection.json`](../__rootArtifactId__-service/src/main/resources/xplain/nlg/fs/fs-awk-column-selection.json)

```json
{
    "select": { "act": { "@type": "prov:Activity" }, "plan": { "@type": "prov:Entity" } },

    "query": [
        "prefix fs <http://openprovenance.org/ns/fs#>",
        "select * from der a prov:WasDerivedFrom",
        "from file2 a prov:Entity",
        " join der.generatedEntity = file2.id",
        "from file1 a prov:Entity",
        " join der.usedEntity = file1.id",
        "from act a prov:Activity",
        " join der.activity = act.id",
        "from waw a prov:WasAssociatedWith",
        " join act.id = waw.activity",
        "from agent a prov:Agent",
        " join waw.agent = agent.id",
        "from plan a prov:Entity",
        " join waw.plan = plan.id",
        "where act[prov:type] >= 'fs:SelectingData'"
    ],

    "sentence": {
        "type": "clause",
        "subject": {
            "type": "noun_phrase",
            "head": {
                "type": "@funcall",
                "@object": "agent",
                "@field": "id",
                "@function": "noun+identity",
                "@arg1": "engineer"
            }
        },
        "verb": "apply",
        "object": {
            "type": "noun_phrase",
            "head": {
                "type": "@funcall",
                "@object": "plan",
                "@field": "id",
                "@function": "noun+identity",
                "@arg1": "awk script",
                "features": {
                    "head_markup_element": "span",
                    "head_markup_attributes": {
                        "type": "@funcall",
                        "@object": "plan",
                        "@function": "markup-for-id",
                        "@field": "id",
                        "@arg1": "provelement"
                    }
                }
            }
        },
        "complements": [
            {
                "type": "clause",
                "complementiser": "to",
                "verb": {
                    "type": "coordinated_phrase",
                    "conjunction": "and",
                    "coordinates": [
                        {
                            "type": "verb_phrase",
                            "head": "select",
                            "object": {
                                "type": "noun_phrase",
                                "determiner": "some",
                                "head": "columns",
                                "features": { "number": "plural" },
                                "post-modifiers": [ {
                                    "type": "preposition_phrase",
                                    "preposition": "of",
                                    "noun": {
                                        "type": "@funcall",
                                        "@object": "file1",
                                        "@field": "id",
                                        "@function": "noun+identity",
                                        "@arg1": "file",
                                        "features": {
                                            "head_markup_element": "span",
                                            "head_markup_attributes": {
                                                "type": "@funcall",
                                                "@object": "file1",
                                                "@function": "markup-for-id",
                                                "@field": "id",
                                                "@arg1": "provelement"
                                            }
                                        }
                                    }
                                } ]
                            },
                            "features": { "form": "bareInfinitive" }
                        },
                        {
                            "type": "verb_phrase",
                            "head": "generate",
                            "object": {
                                "type": "@funcall",
                                "@object": "file2",
                                "@field": "id",
                                "@function": "noun+identity",
                                "@arg1": "file",
                                "features": {
                                    "head_markup_element": "span",
                                    "head_markup_attributes": {
                                        "type": "@funcall",
                                        "@object": "file2",
                                        "@function": "markup-for-id",
                                        "@field": "id",
                                        "@arg1": "provelement"
                                    }
                                }
                            },
                            "features": { "form": "bareInfinitive" }
                        }
                    ]
                }
            }
        ],
        "features": { "type": "features", "tense": "past" }
    },

    "context": {
        "ex": "http://example.org/#",
        "fs": "http://openprovenance.org/ns/fs#"
    }
}
```

---

### 4.2. File compression

**Target sentence**

> The file myfile.txt.gz (file:1550) was compressed from file (file:3) with compression method (method:3) by agent (ag:12).

The agent, method, and even the entire `WasAssociatedWith` relation are optional — if the provenance does not record them the corresponding phrases are silently omitted:

> The file ffff (file:1556) was compressed from file (file:1) by agent (ag:7). *(no method/plan)*

> The file ffff (file:1556) was compressed from file (file:1) with compression method (method:3). *(no agent)*

> The file ffff (file:1556) was compressed from file (file:1). *(no WasAssociatedWith at all)*

**Provenance model**

```
file:1550  prov:wasDerivedFrom  file:3  (via activity act:6082, relation der)
act:6082   prov:wasAssociatedWith  ag:12  (relation waw, with plan method:3)   -- entire relation, agent, and plan are all optional
act:6082   prov:type  fs:TransformingData
file:1550  prov:type  fs:DataFile
file:1550  fs:filename  "myfile.txt.gz"
```

**Key constructs illustrated**

| Construct | Where used |
|---|---|
| Passive clause | *The file … **was compressed** …* |
| `subject` = logical agent → automatic "by …" phrase | *… by agent (ag:12)* |
| `object` = patient → surface subject in passive | *The file myfile.txt.gz (file:1550)* |
| `@property: "fs:filename"` + `@function: "string"` | Filename head of subject NP: *myfile.txt.gz* |
| `noun+identity` with `@arg1: ""` as post-modifier | Parenthesised id after filename: *(file:1550)* |
| `"number": "singular"` on object NP | Correct agreement: *was* (not *were*) |
| `waw.plan` field — `prov:hadPlan` of `WasAssociatedWith` | Method identity: *compression method (method:3)* |
| `adjective_phrase` pre-modifier on `noun_phrase` | *compression* method, *file* myfile.txt.gz |
| `post-modifiers` at clause level for passive adjuncts | Ensures adjuncts follow "was compressed", not precede it |
| `optional join` for waw, agent, and plan | Makes the entire `WasAssociatedWith` relation, and the agent and plan roles within it, all optional in the query |
| `@optional: "true"` on `@funcall` head | Silently omits the enclosing `preposition_phrase` when the variable is unbound |

**Query** — starts from `prov:WasDerivedFrom`, navigates to both files and the activity, then optionally to the associated agent and plan. Filtered to activities of type `fs:TransformingData`.

```
prefix fs <http://openprovenance.org/ns/fs#>
select * from der a prov:WasDerivedFrom
from file2 a prov:Entity
 join der.generatedEntity = file2.id
from file1 a prov:Entity
 join der.usedEntity = file1.id
from act a prov:Activity
 join der.activity = act.id
from waw a prov:WasAssociatedWith
 optional join act.id = waw.activity
from agent a prov:Agent
 optional join waw.agent = agent.id
from plan a prov:Entity
 optional join waw.plan = plan.id
where act[prov:type] >= 'fs:TransformingData'
```

`optional join` is used for `waw`, `agent`, and `plan`. When `optional join` finds no match for `waw` (i.e. no `WasAssociatedWith` exists for the activity), the variables `waw`, `agent`, and `plan` all become unbound — nulls propagate through the chain rather than filtering the row out. When `waw` is found but lacks an agent or plan role, only those variables are unbound. `@optional: "true"` on each sentence-tree `@funcall` that references an unbound variable then suppresses the enclosing phrase.

**Sentence tree**

```
clause  [tense: past, passive: true]
├── subject: noun_phrase                                   → "by agent (ag:12)" if bound, else omitted
│     └── head: noun+identity(agent.id, "agent")  [@optional]
├── object: noun_phrase  [number: singular]                → surface subject "The file myfile.txt.gz (file:1550)"
│     ├── determiner: "The"
│     ├── pre-modifiers: [adjective_phrase "file"]
│     ├── head: string(@property fs:filename)              → "myfile.txt.gz"  [head_markup_element: span]
│     └── post-modifiers:
│           └── adjective_phrase
│               └── head: noun+identity(file2.id, "")      → " (file:1550)"
├── verb: "compress"                                       → "was compressed"
└── post-modifiers:
    ├── preposition_phrase "from"
    │     └── noun: noun_phrase
    │           └── head: noun+identity(file1.id, "file")  → "file (file:3)"
    └── preposition_phrase "with"                          → omitted if plan unbound
          └── noun: noun_phrase
                ├── pre-modifiers: [adjective_phrase "compression"]
                └── head: noun+identity(plan.id, "method") [@optional] → "compression method (method:3)"
```

When `agent` is unbound the `subject` noun phrase resolves to nothing, so simpleNLG produces no "by …" phrase. When `plan` is unbound the `@optional` on its `@funcall` head causes the enclosing `preposition_phrase` to be dropped entirely.

**File:** [`xplain/nlg/fs/fs-file-transforming.json`](../__rootArtifactId__-service/src/main/resources/xplain/nlg/fs/fs-file-transforming.json)

```json
{
    "select": { "act": { "@type": "prov:Activity" }, "plan": { "@type": "prov:Entity" } },

    "query": [
        "prefix fs <http://openprovenance.org/ns/fs#>",
        "select * from der a prov:WasDerivedFrom",
        "from file2 a prov:Entity",
        " join der.generatedEntity = file2.id",
        "from file1 a prov:Entity",
        " join der.usedEntity = file1.id",
        "from act a prov:Activity",
        " join der.activity = act.id",
        "from waw a prov:WasAssociatedWith",
        " optional join act.id = waw.activity",
        "from agent a prov:Agent",
        " optional join waw.agent = agent.id",
        "from plan a prov:Entity",
        " optional join waw.plan = plan.id",
        "where act[prov:type] >= 'fs:TransformingData'"
    ],

    "sentence": {
        "type": "clause",
        "subject": {
            "type": "noun_phrase",
            "head": {
                "type": "@funcall",
                "@object": "agent",
                "@field": "id",
                "@function": "noun+identity",
                "@arg1": "agent",
                "@optional": "true"
            }
        },
        "object": {
            "type": "noun_phrase",
            "determiner": "The",
            "pre-modifiers": [
                { "type": "adjective_phrase", "head": "file" }
            ],
            "head": {
                "type": "@funcall",
                "@object": "file2",
                "@property": "fs:filename",
                "@function": "string",
                "features": {
                    "head_markup_element": "span",
                    "head_markup_attributes": {
                        "type": "@funcall",
                        "@object": "file2",
                        "@function": "markup-for-id",
                        "@field": "id",
                        "@arg1": "provelement"
                    }
                }
            },
            "post-modifiers": [
                {
                    "type": "adjective_phrase",
                    "head": {
                        "type": "@funcall",
                        "@object": "file2",
                        "@field": "id",
                        "@function": "noun+identity",
                        "@arg1": ""
                    }
                }
            ],
            "features": { "number": "singular" }
        },
        "verb": "compress",
        "post-modifiers": [
            {
                "type": "preposition_phrase",
                "preposition": "from",
                "noun": {
                    "type": "noun_phrase",
                    "head": {
                        "type": "@funcall",
                        "@object": "file1",
                        "@field": "id",
                        "@function": "noun+identity",
                        "@arg1": "file",
                        "@optional": "true",
                        "features": {
                            "head_markup_element": "span",
                            "head_markup_attributes": {
                                "type": "@funcall",
                                "@object": "file1",
                                "@function": "markup-for-id",
                                "@field": "id",
                                "@arg1": "provelement"
                            }
                        }
                    }
                }
            },
            {
                "type": "preposition_phrase",
                "preposition": "with",
                "noun": {
                    "type": "noun_phrase",
                    "pre-modifiers": [
                        { "type": "adjective_phrase", "head": "compression" }
                    ],
                    "head": {
                        "type": "@funcall",
                        "@object": "plan",
                        "@field": "id",
                        "@function": "noun+identity",
                        "@arg1": "method",
                        "@optional": "true"
                    }
                }
            }
        ],
        "features": {
            "tense": "past",
            "passive": "true"
        }
    },

    "context": {
        "ex": "http://example.org/#",
        "fs": "http://openprovenance.org/ns/fs#"
    }
}
```

### 4.3. File registration with specialization

**Target sentence**

> A CSV file (file:1588) is registered alongside first version (file:1589) by agent (ag:9).

This example is distinctive in that it must navigate a `prov:SpecializationOf` relation to distinguish the *file entity* (the general, stable identifier) from its *first version entity* (the specific instance created at registration time).

**Provenance model**

```
spec              prov:SpecializationOf  —  spec.generalEntity = file:1588, spec.specificEntity = file:1589
file:1588         prov:type  trs:CSV
wgb               prov:WasGeneratedBy   —  wgb.entity = file:1588, wgb.activity = act:6119
act:6119          prov:type  phys:RegisteringEntity
waw               prov:WasAssociatedWith  —  waw.activity = act:6119, waw.agent = ag:9   (optional)
```

**Key constructs illustrated**

| Construct | Where used |
|---|---|
| `provext:SpecializationOf` as scan type | Entry point to distinguish general entity from version |
| `spec.generalEntity` / `spec.specificEntity` fields | Navigate from spec to file and version |
| `@property: "prov:type"` + `@function: "localname"` as `adjective_phrase` pre-modifier | Extracts *CSV* from `trs:CSV` type attribute |
| `preposition: "alongside"` | *alongside first version (file:1589)* |
| Literal `adjective_phrase` head `"first"` as pre-modifier on a noun phrase | *first* version |
| `present` tense passive | *is registered* |
| `provext` prefix in `context` | Required for `provext:SpecializationOf` type lookup |

**Query note:** The scan begins from `provext:SpecializationOf` (not `prov:Entity`), because the query needs to bind both the general entity (file) and the specific entity (version) simultaneously. An agent is optional via `optional join`.

```
prefix trs <https://openprovenance.org/transport/ns/#>
prefix phys <http://openprovenance.org/ns/phys#>
select * from spec a provext:SpecializationOf
from file a prov:Entity
 join spec.generalEntity = file.id
from version a prov:Entity
 join spec.specificEntity = version.id
from wgb a prov:WasGeneratedBy
 join file.id = wgb.entity
from act a prov:Activity
 join wgb.activity = act.id
from waw a prov:WasAssociatedWith
 optional join act.id = waw.activity
from agent a prov:Agent
 optional join waw.agent = agent.id
where act[prov:type] >= 'phys:RegisteringEntity'
```

**Output:** `A CSV file (file:1588) is registered alongside first version (file:1589) by agent (ag:9).`

**File:** [`xplain/nlg/fs/fs-file-init.json`](../__rootArtifactId__-service/src/main/resources/xplain/nlg/fs/fs-file-init.json)

---

### 4.4. File splitting

**Target sentence** (single sentence)

> The file (file:2) was split into training_set.csv (file:1586) and validation_set.csv (file:1587) with splitting method (method:3) by agent (ag:5).

This example illustrates a *one-to-many* derivation: one input file is split into multiple output files, each recorded as a separate `prov:WasDerivedFrom` relation. Rather than emitting one sentence per output file, a `group by` clause collapses all output files into a single row, and an `@iterator` over the aggregated sequence builds a coordinated noun phrase naming all outputs in a single sentence.

**Provenance model**

```
file:1586  prov:wasDerivedFrom  file:2  (via act:6118, der1)   fs:filename = "training_set.csv"
file:1587  prov:wasDerivedFrom  file:2  (via act:6118, der2)   fs:filename = "validation_set.csv"
act:6118   prov:type  fs:SplittingData
act:6118   prov:wasAssociatedWith  ag:5  plan method:3   (optional)
```

**Key constructs illustrated**

| Construct | Where used |
|---|---|
| `group by file1, act, waw, agent, plan aggregate file2 with Seq` | Collapses both `WasDerivedFrom` rows into one result row; `file2` becomes a sequence |
| `coordinated_phrase` with `@iterator` as `noun` of `preposition_phrase` | Builds *"training_set.csv (file:1586) and validation_set.csv (file:1587)"* inside *into …* |
| `"@variable": "file2"` on `@iterator` | Iterates over each element of the aggregated `file2` sequence |
| `"@clause": "coordinates"` | Directs each instantiated element into the `coordinates` list of the `coordinated_phrase` |
| `preposition: "into"` | *into training_set.csv … and validation_set.csv …* |
| Input file (file1) as the passive surface subject | *The file (file:2) was split …* |
| `noun+identity(file1.id, "file")` | Renders the unnamed source file as *file (file:2)* |

**Query**
```
prefix fs <http://openprovenance.org/ns/fs#>
select * from der a prov:WasDerivedFrom
from file2 a prov:Entity
 join der.generatedEntity = file2.id
from file1 a prov:Entity
 join der.usedEntity = file1.id
from act a prov:Activity
 join der.activity = act.id
from waw a prov:WasAssociatedWith
 optional join act.id = waw.activity
from agent a prov:Agent
 optional join waw.agent = agent.id
from plan a prov:Entity
 optional join waw.plan = plan.id
where act[prov:type] >= 'fs:SplittingData'
group by file1, act, waw, agent, plan aggregate file2 with Seq
```

The `der` variable is not included in `group by` — it is silently dropped, so the two `WasDerivedFrom` rows that share the same `(file1, act, waw, agent, plan)` key are merged into one result row with `file2 = [file:1586, file:1587]`.

**Sentence tree (abbreviated)**
```
clause  [tense: past, passive: true]
├── subject: noun+identity(agent.id, "agent") [@optional]   → "by agent (ag:5)"
├── object: noun_phrase "The file (file:2)"                  → surface subject
│     └── head: noun+identity(file1.id, "file")
├── verb: "split"                                            → "was split"
└── post-modifiers:
    ├── preposition_phrase "into"
    │     └── noun: coordinated_phrase [conjunction: "and"]
    │           @iterator  @variable: "file2"  @clause: "coordinates"
    │           @element: noun_phrase
    │               head: string(@property fs:filename)      → "training_set.csv" / "validation_set.csv"
    │               post-modifiers: noun+identity(file2.id, "")
    └── preposition_phrase "with"                            → omitted if plan unbound
          └── noun: "splitting method (method:3)" [@optional]
```

**Output:** `The file (file:2) was split into training_set.csv (file:1586) and validation_set.csv (file:1587) with splitting method (method:3) by agent (ag:5).`

**File:** [`xplain/nlg/fs/fs-file-splitting.json`](../__rootArtifactId__-service/src/main/resources/xplain/nlg/fs/fs-file-splitting.json)

---

### 4.5. Model fitting

**Target sentence**

> The model pipeline (file:1590) was fitted from data (file:3) with fitting method (method:100) by agent (ag:22).

This example shows how the same `WasDerivedFrom`-based template is reused for a machine-learning fitting step, producing a *model* (of type `fs:Pipeline`) from a *data* file.

**Provenance model**

```
file:1590  prov:wasDerivedFrom  file:3  (via act:6120)   fs:filename = "pipeline", prov:type = fs:Pipeline
act:6120   prov:type  fs:FittingData
act:6120   prov:wasAssociatedWith  ag:22  plan method:100   (optional)
```

**Key constructs illustrated**

| Construct | Where used |
|---|---|
| `where act[prov:type] >= 'fs:FittingData'` | Filters to fitting activities only |
| `adjective_phrase` pre-modifier `"model"` on object NP | *model* pipeline (file:1590) |
| `@arg1: "data"` on source file `noun+identity` | *data* (file:3) — labels an unnamed entity with a domain term |
| `verb: "fit"` in past tense passive | simpleNLG realises as *was fitted* |
| `adjective_phrase` pre-modifier `"fitting"` | *fitting* method |

**`@arg1` as a domain label:** When the source file entity has no `fs:filename` property the `noun+identity` function renders only the qualified name. Providing `"@arg1": "data"` prepends the label *data*, yielding *data (file:3)* — a clean domain-meaningful phrase even for bare entity nodes.

**Output:** `The model pipeline (file:1590) was fitted from data (file:3) with fitting method (method:100) by agent (ag:22).`

**File:** [`xplain/nlg/fs/fs-file-fitting.json`](../__rootArtifactId__-service/src/main/resources/xplain/nlg/fs/fs-file-fitting.json)

---

## 5. Rules for Writing X-Plans

This section distils what is known about x-plan authoring into a set of concrete rules. They are written for an AI assistant that does not have access to the source code and must rely on this document alone to generate correct x-plans for new provenance.

---

### 5.1. Query rules

**R-Q1 — Start from a PROV relation, not from an entity.**
Every query begins with `select * from <rel> a prov:<RelationType>`. Navigate outward from that relation to entities, activities, agents, and plans using `from` + `join`. Do not start from an entity and try to reach a relation.

**R-Q2 — The left-hand side of every `join` or `left join` must already be bound.**
The LHS must reference a variable introduced by an earlier `from` clause. The RHS introduces or constrains the new variable. Correct: `join der.usedEntity = file1.id`. Wrong: `join file1.id = der.usedEntity`.

**R-Q2b — Use `optional join` to make a PROV relation, or a role within one, optional.**
When a PROV relation or one of its roles may be absent from the provenance, use `optional join` instead of `join`. If no match is found the introduced variable is left unbound rather than filtering the row out, and subsequent `optional join` clauses whose LHS references that variable will propagate the null rather than crash:

```
from waw a prov:WasAssociatedWith
 optional join act.id = waw.activity    ← waw itself is optional
from agent a prov:Agent
 optional join waw.agent = agent.id     ← agent is optional; null-safe if waw is unbound
from plan a prov:Entity
 optional join waw.plan = plan.id       ← plan is optional; null-safe if waw is unbound
```

`left join` is also available (it makes an individual role optional when the relation is guaranteed to be present) but does **not** propagate null through a chain: if a `left join` yields an unbound variable, a subsequent `left join` or `join` that references it will fail. Use `optional join` whenever the variable being introduced may itself be unbound at a later step.

**R-Q3 — Use PROV role names as `@field` values, not PROV-N property URIs.**
In join expressions and `@funcall` nodes the field names come from the PROV data model: `id`, `activity`, `agent`, `entity`, `plan`, `generatedEntity`, `usedEntity`, `delegate`, `responsible`, `collection`, `time`. See the `@field` reference table in § 2.1.

**R-Q4 — Declare every namespace prefix used in `where` clauses and `@property` values.**
Add a `prefix` line in the query for each namespace appearing in a `where` type filter. Add the same mappings to the x-plan `context` object so that `@property` lookups can resolve them at sentence-generation time.

**R-Q5 — Access the plan (method/algorithm/script) via `waw.plan`.**
The plan entity is a role of `prov:WasAssociatedWith`, not a field of the activity. Reach it with: `from plan a prov:Entity` / ` join waw.plan = plan.id`. There is no direct plan field on an activity.

**R-Q6 — Declare focus variables in the `select` object.**
The `select` object at the top of the x-plan declares the primary variable(s) that drive sentence generation (typically the activity or entity that is the focus of the explanation). Variables listed here must also be bound by the query.

---

### 5.2. `@funcall` placement rules

**R-F1 — `@funcall` cannot be placed directly as the `object` of a `clause`. Wrap it in a `noun_phrase`.**
`clause.object` uses standard Jackson polymorphic type resolution, which does not include `@funcall` as a registered `Phrase` subtype. Always wrap:
```json
"object": { "type": "noun_phrase", "head": { "type": "@funcall", ... } }
```
`@funcall` can appear as the `head` of a `noun_phrase`, the `noun` of a `preposition_phrase`, or the `object` of a `verb_phrase` (those fields use custom deserializers that accept `@funcall`).

**R-F2 — Choose exactly one of `@field` or `@property`; never both.**
`@field` reads a structural PROV role (an id, a referenced agent, an activity, a plan, etc.). `@property` reads a typed attribute declared on the statement in brackets (e.g. `fs:filename`, `prov:type`). They are mutually exclusive in one `@funcall`.

**R-F3 — Use `@function: "string"` to extract typed literal attributes.**
When `@property` points to a typed literal (e.g. `fs:filename = "myfile.txt.gz" %% xsd:string`), use `"string"` to unwrap the plain value. `"identity"` may include type annotation artefacts.

**R-F4 — Use `noun+identity` with `@field: "id"` to display a PROV element as `"role (prefix:local)"`.**
```json
{ "type": "@funcall", "@object": "agent", "@field": "id",
  "@function": "noun+identity", "@arg1": "engineer" }
```
Result: `"engineer (ag:41)"`. The `@arg1` string is the human-readable role label prepended to the parenthesised identifier.

**R-F5 — To append a parenthesised id after a filename head, use `noun+identity` with `@arg1: ""` as an `adjective_phrase` post-modifier.**
When the NP head is a filename (from `@property` + `string`) and you also want the element id shown in parentheses, add:
```json
"post-modifiers": [{
    "type": "adjective_phrase",
    "head": { "type": "@funcall", "@object": "file2", "@field": "id",
              "@function": "noun+identity", "@arg1": "" }
}]
```
`noun+identity` with an empty `@arg1` produces `" (file:1550)"` (leading space), which simpleNLG appends cleanly after the head noun.

**R-F6 — Every `@property` prefix must be declared in the x-plan `context`.**
A missing prefix causes a `NullPointerException` at runtime. If a prefix appears in `@property` values in the sentence tree, add it to `context` even if it is already declared as a query `prefix` line.

**R-F7 — Use `@optional: "true"` on a `@funcall` head to silently suppress the enclosing phrase when the variable is unbound.**
When a query variable may be unbound (because it was introduced via `left join` or `optional join`), mark the `@funcall` that reads it with `"@optional": "true"`. The effect propagates upward: the `@funcall` returns nothing, the `noun_phrase` it heads resolves to nothing, and the `preposition_phrase` containing that noun is omitted from the realised sentence. Place `@optional` on the `head` of the innermost noun phrase, not on the preposition phrase itself.

---

### 5.3. Passive voice rules

**R-P1 — In a passive clause, `subject` is the logical agent and `object` is the patient.**
simpleNLG promotes the logical `object` to surface subject and appends the logical `subject` as a "by …" phrase automatically. To produce *"The file was compressed by the agent"*:

| Field | Role | Surface result |
|---|---|---|
| `subject` | logical agent | *"… by agent (ag:12)"* — generated automatically |
| `object` | patient | *"The file myfile.txt.gz was compressed"* |
| `features` | `{ "tense": "past", "passive": "true" }` | |

**R-P2 — Add `"number": "singular"` to the object NP whenever the head is a dynamic string.**
simpleNLG cannot infer grammatical number from a `@funcall`-generated head. Without this, the auxiliary defaults to plural (*"were compressed"* instead of *"was compressed"*).

**R-P3 — Use `post-modifiers` at the clause level for prepositional adjuncts in passive sentences.**
Using `complements` in passive mode causes simpleNLG to place the prepositional phrases *before* the verb group. Using `post-modifiers` places them correctly after *"was compressed"*.

**R-P4 — Do not use `"using"` as a preposition value.**
simpleNLG recognises *"using"* as a verb stem and conjugates it to *"uses"*. Use `"with"` instead (*"with compression method …"*).

---

### 5.4. Infinitive complement and coordinated verb rules

**R-I1 — Form an infinitive complement with `complementiser: "to"` on a child `clause`.**
```json
{ "type": "clause", "complementiser": "to", "verb": { ... } }
```
The parent clause verb drives tense/agreement; the child clause supplies the infinitive.

**R-I2 — Set `"form": "bareInfinitive"` on every `verb_phrase` inside a `coordinated_phrase` that is the verb of an infinitive complement clause.**
Without this simpleNLG conjugates each coordinate in the third person (*"selects"*, *"generates"*). `"bareInfinitive"` suppresses conjugation. Do not use `"infinitive"` here — that adds a redundant *"to"* because the parent clause's `complementiser` already supplies it.

---

### 5.5. Noun phrase construction rules

**R-N1 — Wrap a `@funcall` head in a `noun_phrase` whenever you need a determiner, modifiers, or features.**
A bare `@funcall` used directly produces a string. To add `determiner`, `pre-modifiers`, `post-modifiers`, or features such as `number` or markup, it must be the `head` field of a `noun_phrase`.

**R-N2 — Pre-modifiers precede the head; post-modifiers follow it.**
simpleNLG realises: `[determiner] [pre-modifiers] [head] [post-modifiers]`. Use `pre-modifiers` for descriptive adjectives (*"compression method"*, *"awk script"*, *"file myfile.txt.gz"*) and `post-modifiers` for trailing elements such as a parenthesised id.

**R-N3 — Declare `"number"` explicitly on any NP whose head is produced by `@funcall`.**
Dynamic string heads have no inherent number. Set `"number": "singular"` or `"number": "plural"` whenever verb agreement or determiner form depends on it.

**R-N4 — Apply `head_markup_element` + `head_markup_attributes` to make entity references navigable.**
Use `markup-for-id` with `@field: "id"` (never `@property`) and `@arg1: "provelement"`. Place these features on the `@funcall` node that produces the displayed text:
```json
"features": {
    "head_markup_element": "span",
    "head_markup_attributes": {
        "type": "@funcall", "@object": "ent",
        "@function": "markup-for-id", "@field": "id", "@arg1": "provelement"
    }
}
```

---

### 5.7. Join types: `join`, `left join`, and `optional join`

ProvQL provides three join operators. Choosing the right one depends on whether a matched row is required and whether a null result must propagate safely through subsequent steps.

| Syntax | AST node | Semantics |
|---|---|---|
| `join a.x = b.y` | `Join` | Inner join — both sides required; no match drops the row |
| `left join a.x = b.y` | `LeftJoin` | Left outer join — right variable becomes null if no match; does **not** propagate null safely through further joins |
| `optional join a.x = b.y` | `LeftHashJoin` | Left outer join — right variable becomes null if no match; null **propagates safely** through any subsequent `optional join` that references it |

**When to use each:**

- Use `join` for required navigation steps (e.g. from a derivation to the activity that performed it).
- Use `left join` only when the relation being joined against is guaranteed to exist and only an optional *role within* it (such as a plan or an agent) may be absent, and no further `optional join` depends on it.
- Use `optional join` whenever the relation itself may be absent, or when a null result must propagate through a chain of optional steps.

**Example — the `WasAssociatedWith` subgraph is entirely optional:**

```
from waw a prov:WasAssociatedWith
 optional join act.id = waw.activity    ← waw may not exist at all
from agent a prov:Agent
 optional join waw.agent = agent.id     ← waw may be null; null propagates to agent
from plan a prov:Entity
 optional join waw.plan = plan.id       ← waw may be null; null propagates to plan
```

If no `WasAssociatedWith` exists for the activity, `waw`, `agent`, and `plan` are all left unbound. The `@optional: "true"` markers in the sentence tree then suppress the corresponding phrases. The root constraint (R-Q2) is still respected: the LHS of each join (`act.id`, `waw.agent`, `waw.plan`) references a variable that was already introduced by an earlier `from` clause.

---

### 5.6. General authoring checklist

Before running an x-plan, verify each item:

- [ ] Every namespace used in `where` filters or `@property` values is declared in both the query `prefix` lines and the x-plan `context` block.
- [ ] Every `join`, `left join`, and `optional join` LHS references a variable already bound by a preceding `from` clause.
- [ ] No `@funcall` is placed directly as `clause.object` — it is always inside a `noun_phrase`.
- [ ] Passive clauses use `subject` for the logical agent and `object` for the patient; prepositional adjuncts are `post-modifiers`, not `complements`.
- [ ] `"form": "bareInfinitive"` is set on every `verb_phrase` coordinate inside an infinitive `coordinated_phrase`.
- [ ] `"number"` is explicit on any NP whose head is produced by `@funcall`.
- [ ] `@property` prefixes are present in the `context`; `@field` values are PROV role names, not property URIs.
- [ ] Every `@funcall` that reads a variable introduced by `left join` or `optional join` carries `"@optional": "true"`.
- [ ] If the variable introduced by a join may itself be used as the LHS of a further optional step, use `optional join` (not `left join`) so that null propagates safely through the chain.

