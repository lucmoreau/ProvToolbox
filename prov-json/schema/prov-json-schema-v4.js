{
  "$schema": "http://json-schema.org/draft-04/schema#",
  "type": "object",
  "allOf": [
    { "$ref": "#/definitions/bundle" },
    { "properties": {
        "bundle": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/bundle" }
        }
      }
    }
  ],
  "definitions": {
    "literal-simple": {
      "title": "Simple data types",
      "oneOf": [
        { "type": "string" },
        { "type": "boolean" },
        { "type": "number" }
      ]
    },
    "literal-typed": {
      "type": "object",
      "title": "A typed literal with value and data type",
      "properties": {
        "$": { "$ref": "#/definitions/literal-simple" },
        "type": { "type": "string", "format": "uri" }
      },
      "required": ["$", "type"],
      "additionalProperties": false
    },
    "literal-international-string": {
      "type": "object",
      "title": "An international string with langtag",
      "properties": {
        "$": { "type": "string" },
        "type": { "type": "string", "format": "uri" },
        "lang": { "type": "string" }
      },
      "required": ["$", "type"],
      "additionalProperties": false
    },
    "literal-complex": {
      "title": "A complex (typed) literal",
      "anyOf": [
        { "$ref": "#/definitions/literal-typed" },
        { "$ref": "#/definitions/literal-international-string" }
      ]
    },
    "literal-single": {
      "title": "A single literal",
      "oneOf": [
        { "$ref": "#/definitions/literal-simple" },
        { "$ref": "#/definitions/literal-complex" }
      ]
    },
    "literal-array": {
      "title": "An array of single literals",
      "type": "array",
      "items": { "$ref": "#/definitions/literal-single" }
    },
    "literal": {
      "title": "A general literal - can be a single value or an array of single literals",
      "oneOf": [
        { "$ref": "#/definitions/literal-single" },
        { "$ref": "#/definitions/literal-array" }
      ]
    },
    "entity": {
      "type": "object",
      "title": "An entity",
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "agent": { "$ref": "#/definitions/entity" },
    "activity": {
      "type": "object",
      "title": "An activity",
      "properties": {
        "prov:startTime": {"type": "string", "format": "date-time"},
        "prov:endTime": {"type": "string", "format": "date-time"}
      },
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "generation": {
      "type": "object",
      "properties": {
        "prov:entity": {"type": "string", "format": "uri"},
        "prov:activity": {"type": "string", "format": "uri"},
        "prov:time": {"type": "string", "format": "date-time"}
      },
      "required": ["prov:entity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "usage": {
      "type": "object",
      "properties": {
        "prov:entity": {"type": "string", "format": "uri"},
        "prov:activity": {"type": "string", "format": "uri"},
        "prov:time": {"type": "string", "format": "date-time"}
      },
      "required": ["prov:activity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "start": {
      "type": "object",
      "properties":{
        "prov:activity": {"type": "string", "format": "uri"},
        "prov:trigger": {"type": "string", "format": "uri"},
        "prov:starter": {"type": "string", "format": "uri"},
        "prov:time": {"type": "string", "format": "date-time"}
      },
      "required": ["prov:activity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "end": {
      "type": "object",
      "properties":{
        "prov:activity": {"type": "string", "format": "uri"},
        "prov:trigger": {"type": "string", "format": "uri"},
        "prov:ender": {"type": "string", "format": "uri"},
        "prov:time": {"type": "string", "format": "date-time"}
      },
      "required": ["prov:activity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "communication": {
      "type": "object",
      "properties":{
        "prov:informed": { "type": "string", "format": "uri" },
        "prov:informant": { "type": "string", "format": "uri" }
      },
      "required": ["prov:informed", "prov:informant"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "invalidation": {
      "type": "object",
      "properties":{
         "prov:entity": {"type": "string", "format": "uri" },
         "prov:activity": {"type": "string", "format": "uri" },
         "prov:time": {"type": "string", "format": "date-time"}
      },
      "required": ["prov:entity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "derivation": {
      "type": "object",
      "properties":{
        "prov:generatedEntity": {"type": "string", "format": "uri" },
        "prov:usedEntity": {"type": "string", "format": "uri" },
        "prov:activity": {"type": "string", "format": "uri"},
        "prov:generation": {"type": "string", "format": "uri"},
        "prov:usage": {"type": "string", "format": "uri"}
      },
      "required": ["prov:generatedEntity", "prov:usedEntity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "attribution": {
      "type": "object",
      "properties":{
        "prov:entity": {"type": "string", "format": "uri" },
        "prov:agent": {"type": "string", "format": "uri" }
      },
      "required": ["prov:entity", "prov:agent"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "association": {
      "type": "object",
      "properties": {
        "prov:activity": {"type": "string", "format": "uri" },
        "prov:agent": {"type": "string", "format": "uri" },
        "prov:plan": {"type": "string", "format": "uri" }
      },
      "required": ["prov:activity"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "delegation": {
      "type": "object",
      "properties":{
        "prov:delegate": {"type": "string", "format": "uri" },
        "prov:responsible": {"type": "string", "format": "uri" },
        "prov:activity": {"type": "string", "format": "uri"}
      },
      "required": ["prov:delegate", "prov:responsible"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "influence": {
      "type": "object",
      "properties": {
        "prov:influencer": {"type": "string", "format": "uri" },
        "prov:influencee": {"type": "string", "format": "uri" }
      },
      "required": ["prov:influencer", "prov:influencee"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "specialization": {
      "type": "object",
      "properties": {
        "prov:specificEntity": {"type": "string", "format": "uri" },
        "prov:generalEntity": {"type": "string", "format": "uri" }
      },
      "required": ["prov:specificEntity", "prov:generalEntity"],
      "additionalProperties": false
    },
    "alternate": {
      "type": "object",
      "properties": {
        "prov:alternate1": {"type": "string", "format": "uri" },
        "prov:alternate2": {"type": "string", "format": "uri" }
      },
      "required": ["prov:alternate1", "prov:alternate2"],
      "additionalProperties": false
    },
    "membership": {
      "type": "object",
      "properties": {
        "prov:collection": {"type": "string", "format": "uri" },
        "prov:entity": {
          "oneOf": [
            { "type": "string", "format": "uri" },
            { "type": "array",
              "items": { "type": "string", "format": "uri" }
            }
          ]
        }
      },
      "required": ["prov:collection", "prov:entity"],
      "additionalProperties": false
    },
    "key-entity-set-standard": {
      "type": "array",
      "items": {
        "type": "object",
        "properties": {
          "key": { "$ref": "#/definitions/literal-single" },
          "$": {"type": "string", "format": "uri" }
        },
        "required": ["key", "$"],
        "additionalProperties": false
      },
      "minItems": 1
    },
    "key-entity-set-compact": {
      "type":"object",
      "properties": {
        "$key-datatype": { "type": "string", "format": "uri" }
      },
      "required": ["$key-datatype"],
      "additionalProperties": { "type": "string", "format": "uri" }
    },
    "key-entity-set": {
      "title": "A set of key-entity pairs (for dictionary relations)",
      "oneOf": [
        { "$ref": "#/definitions/key-entity-set-standard" },
        { "$ref": "#/definitions/key-entity-set-compact" }
      ]
    },
    "dictionary-membership": {
      "type":"object",
      "properties": {
        "prov:dictionary": {"type": "string", "format": "uri" },
        "prov:key-entity-set": { "$ref": "#/definitions/key-entity-set" }
      },
      "required": ["prov:dictionary", "prov:key-entity-set"],
      "additionalProperties": false
    },
    "dictionary-insertion": {
      "type":"object",
      "properties": {
        "prov:after": {"type": "string", "format": "uri" },
        "prov:before": {"type": "string", "format": "uri" },
        "prov:key-entity-set": { "$ref": "#/definitions/key-entity-set" }
      },
      "required": ["prov:after", "prov:before", "prov:key-entity-set"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "dictionary-removal": {
      "type":"object",
      "properties": {
        "prov:after": {"type": "string", "format": "uri" },
        "prov:before": {"type": "string", "format": "uri" },
        "prov:key-set": {
          "type": "array",
          "items": { "$ref": "#/definitions/literal-single" },
          "minItems": 1
        }
      },
      "required": ["prov:after", "prov:before", "prov:key-set"],
      "additionalProperties": { "$ref": "#/definitions/literal" }
    },
    "mention": {
      "type": "object",
      "properties": {
        "prov:specificEntity": {"type": "string", "format": "uri" },
        "prov:generalEntity": {"type": "string", "format": "uri" },
        "prov:bundle": {"type": "string", "format": "uri" }
      },
      "required": ["prov:specificEntity", "prov:generalEntity", "prov:bundle"],
      "additionalProperties": false
    },
    "bundle": {
      "type": "object",
      "properties": {
        "prefix": {
          "type": "object",
          "description": "A map of prefixes and URIs",
          "patternProperties": {
            "^[a-zA-Z0-9_\\-]+$": {
              "type" : "string",
              "format": "uri"
            }
          },
          "additionalProperties": false
        },
        "entity": {
          "type": "object",
          "description": "Map of entities by ids. TODO: use patternProperties instead of additionalProperties as the keys are required to be valid ids.",
          "additionalProperties": { "$ref": "#/definitions/entity" }
        },
        "agent": {
          "type": "object",
          "description": "Map of ids of agents",
          "additionalProperties": { "$ref": "#/definitions/agent" }
        },
        "activity":{
          "type": "object",
          "description": "Map of activities by ids",
          "additionalProperties": { "$ref": "#/definitions/activity" }
        },
        "wasGeneratedBy": {
           "type": "object",
           "additionalProperties": { "$ref": "#/definitions/generation" }
        },
        "used": {
           "type": "object",
           "additionalProperties": { "$ref": "#/definitions/usage" }
         },
         "wasStartedBy": {
           "type":"object",
           "additionalProperties": { "$ref": "#/definitions/start" }
         },
         "wasEndedBy": {
           "type":"object",
           "additionalProperties": { "$ref": "#/definitions/end" }
         },
         "wasInformedBy": {
           "type": "object",
           "additionalProperties": { "$ref": "#/definitions/communication" }
        },
        "wasInvalidatedBy": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/invalidation" }
        },
        "wasDerivedFrom": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/derivation" }
        },
        "wasAttributedTo": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/attribution" }
        },
        "wasAssociatedWith": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/association" }
        },
        "actedOnBehalfOf": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/delegation" }
        },
        "wasInfluencedBy": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/influence" }
        },
        "specializationOf": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/specialization" }
        },
        "alternateOf": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/alternate" }
        },
        "hadMember": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/membership" }
        },
        "hadDictionaryMember": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/dictionary-membership" }
        },
        "derivedByInsertionFrom": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/dictionary-insertion" }
        },
        "derivedByRemovalFrom": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/dictionary-removal" }
        },
        "mentionOf": {
          "type": "object",
          "additionalProperties": { "$ref": "#/definitions/mention" }
        }
      }
    }
  }
}