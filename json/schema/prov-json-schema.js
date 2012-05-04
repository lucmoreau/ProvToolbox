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
    "wasStartedBy": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:activity": {"type": "string", "format": "uri", "required":true},
          "prov:trigger": {"type": "string", "format": "uri", "required":false},
          "prov:time": {"type": "string", "format": "date-time"}
        },
        "additionalProperties":{}
      }
    },
    "wasEndedBy": {"$ref":"#/properties/wasStartedBy"},
    "wasInvalidatedBy": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:started": {"type": "string", "format": "uri", "required":true},
          "prov:starter": {"type": "string", "format": "uri", "required":true},
        },
        "additionalProperties":{}
      }
    },
    "wasInformedBy": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:informed": {"type": "string", "format": "uri", "required":true},
          "prov:informant": {"type": "string", "format": "uri", "required":true},
        },
        "additionalProperties":{}
      }
    },
    "wasStartedByActivity": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:informed": {"type": "string", "format": "uri", "required":true},
          "prov:informant": {"type": "string", "format": "uri", "required":true},
        },
        "additionalProperties":{}
      }
    },
    "agent":{
      "type":"object",
      "description":"Map of ids of agents",
      "additionalProperties":{
        "type":"object",
        "title":"entity",
        "additionalProperties":{}
      }
    },
    "wasAttributedTo": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:entity": {"type": "string", "format": "uri", "required":true},
          "prov:agent": {"type": "string", "format": "uri", "required":true},
        },
        "additionalProperties":{}
      }
    },
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
        "additionalProperties":{}
      }
    },
    "wasRevisionOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:newer": {"type": "string", "format": "uri", "required":true},
          "prov:older": {"type": "string", "format": "uri", "required":true},
          "prov:responsibility": {"type": "string", "format": "uri"}
        },
        "additionalProperties":{}
      }
    },
    "wasQuotedFrom": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:quote": {"type": "string", "format": "uri", "required":true},
          "prov:original": {"type": "string", "format": "uri", "required":true},
          "prov:quoterAgent": {"type": "string", "format": "uri"},
          "prov:originalAgent": {"type": "string", "format": "uri"}
        },
        "additionalProperties":{}
      }
    },
    "hadOriginalSource": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:derived": {"type": "string", "format": "uri", "required":true},
          "prov:source": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties":{}
      }
    },
    "tracedTo": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:entity": {"type": "string", "format": "uri", "required":true},
          "prov:ancestor": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties":{}
      }
    },
    "specializationOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:specializedEntity": {"type": "string", "format": "uri", "required":true},
          "prov:generalEntity": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties": {}
      }
    },
    "alternateOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:alternate1": {"type": "string", "format": "uri", "required":true},
          "prov:alternate2": {"type": "string", "format": "uri", "required":true}
        },
        "additionalProperties": {}
      }
    },
    "derivedByInsertionFrom": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:after": {"type": "string", "format": "uri", "required":true},
          "prov:before": {"type": "string", "format": "uri", "required":true},
          "prov:key-entity-set": {"type": "array", "items":{}, "required":true}
        },
        "additionalProperties":{}
      }
    },
    "derivedByRemovalFrom": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:after": {"type": "string", "format": "uri", "required":true},
          "prov:before": {"type": "string", "format": "uri", "required":true},
          "prov:key-set": {"type": "array", "items":{}, "required":true}
        },
        "additionalProperties": {}
      }
    },
    "memberOf": {
      "type":"object",
      "additionalProperties":{
        "type":"object",
        "properties":{
          "prov:after": {"type": "string", "format": "uri", "required":true},
          "prov:key-entity-set": {"type": "array", "items":{}, "required":true},
          "prov:complete": {"type": "boolean", "required":false}
        },
        "additionalProperties": {}
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
