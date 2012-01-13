{
  "title":"Provenance Container",
  "type":"object",
  
  "properties":{
    "prefix":{
      "type":"object",
      "description":"",
      "patternProperties": {
        "^[a-zA-Z0-9_\\-]+$": {
          "type" : "string",
          "format": "uri"
        }
      },
      "additionalProperties":false
    },
    "entity":{
      "type":"object",
      "description":"Map of entities by ids. TODO: use patternProperties instead of additionalProperties as the keys are required to be valid ids.",
      "additionalProperties":{
        "type":"object",
        "title":"entity",
        "additionalProperties":{}
      }
    },
    "activity":{
      "type":"object",
      "description":"Map of activities by ids",
      "additionalProperties":{
        "type":"object",
        "title":"activity",
        "properties":{
          "prov:startTime": {"type": "string", "format": "date-time"},
          "prov:endTime": {"type": "string", "format": "date-time"}
        },
        "additionalProperties":{}
      }
    },
    "agent":{
      "type":"array",
      "description":"List of ids of entities that are assert as agents",
      "items":{
        "type":"string",
        "format": "uri"
      }
    },
    "note":{
      "type":"object",
      "description":"Map of notes by ids",
      "additionalProperties":{
        "type":"object",
        "title":"note",
        "additionalProperties":{}
      }
    },
    "account": {
      "type":"object",
      "description":"Map of accounts by ids",
      "additionalProperties": {
        "type":"object",
        "title":"account",
        "properties":{
          "asserter": {"type": "string", "format": "uri", "required": true},
          "entity":{"$ref":"#/properties/entity"},
          "activity":{"$ref":"#/properties/activity"},
          "agent":{"$ref":"#/properties/agent"},
          "note":{"$ref":"#/properties/note"},
          "wasGeneratedBy":{"$ref":"#/properties/wasGeneratedBy"},
          "used":{"$ref":"#/properties/used"},
          "wasAssociatedWith":{"$ref":"#/properties/wasAssociatedWith"},
          "wasStartedBy":{"$ref":"#/properties/wasStartedBy"},
          "wasEndedby":{"$ref":"#/properties/wasEndedby"},
          "actedOnBehalfOf":{"$ref":"#/properties/actedOnBehalfOf"},
          "wasDerivedFrom":{"$ref":"#/properties/wasDerivedFrom"},
          "alternateOf":{"$ref":"#/properties/alternateOf"},
          "specializationOf":{"$ref":"#/properties/specializationOf"},
          "hasAnnotation":{"$ref":"#/properties/hasAnnotation"}
        },
        "additionalProperties": false
      }
    },
    "wasGeneratedBy": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:entity": {"type": "string", "format": "uri", "required":true},
          "prov:activity": {"type": "string", "format": "uri", "required":true},
          "prov:time": {"type": "string", "format": "date-time"}
        },
        "additionalProperties":{}
      }
    },
    "used": {"$ref":"#/properties/wasGeneratedBy"},
    "wasAssociatedWith": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:activity": {"type": "string", "format": "uri", "required":true},
          "prov:agent": {"type": "string", "format": "uri", "required":true},
          "prov:plan": {"type": "string", "format": "uri", "required":false}
        },
        "additionalProperties":{}
      }
    },
    "wasStartedBy": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:activity": {"type": "string", "format": "uri", "required":true},
          "prov:agent": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties":{}
      }
    },
    "wasEndedby": {"$ref":"#/properties/wasStartedBy"},
    "actedOnBehalfOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:subordinate": {"type": "string", "format": "uri", "required":true},
          "prov:responsible": {"type": "string", "format": "uri", "required":true},
          "prov:activity": {"type": "string", "format": "uri"}
        },
        "additionalProperties":{}
      }
    },
    "wasDerivedFrom": {
      "type":"object",
      "decription":"PROV-DM requires that activity, generation, and usage must be present at the same time with one another, hence the 'dependencies' requirements below. However, the requirement for 'prov:steps' can not be described as 'prov:steps' could also appear as 'steps' or 'anyprefix:steps'",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:generatedEntity": {"type": "string", "format": "uri", "required":true},
          "prov:usedEntity": {"type": "string", "format": "uri", "required":true},
          "prov:activity": {"type": "string", "format": "uri"},
          "prov:generation": {"type": "string", "format": "uri"},
          "prov:usage": {"type": "string", "format": "uri"}
        },
        "additionalProperties":{},
        "dependencies": {
          "prov:activity": ["prov:generation", "prov:usage"],
          "prov:generation": ["prov:activity", "prov:usage"],
          "prov:usage": ["prov:activity", "prov:generation"]
        }
      }
    },
    "alternateOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:entity": {"type": "string", "format": "uri", "required":true},
          "prov:alternate": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties": {}
      }
    },
    "specializationOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:entity": {"type": "string", "format": "uri", "required":true},
          "prov:specialization": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties": {}
      }
    },
    "hasAnnotation": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:annotated": {"type": "string", "format": "uri", "required":true},
          "prov:note": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties": false
      }
    }
    
  },
  "additionalProperties": false
}
