Blockly.defineBlocksWithJsonArray(

        [
            {
                "type": "noun_phrase",
                "message0": "Noun Phrase %1 determiner %2 head %3 modifiers %4 pre-modifiers %5 post-modifiers %6 specifier %7 complements %8 features %9",
                "args0": [
                    {
                        "type": "input_dummy"
                    },
                    {
                        "type": "input_value",
                        "name": "DETERMINER",
                        "check": "String",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "HEAD",
                        "check": [
                            "String",
                            "Funcall"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "PRE_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "POST_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "SPECIFIER",
                        "check": [
                            "String",
                            "Funcall",
                            "NounPhrase"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "COMPLEMENTS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "FEATURES",
                        "check": "Features",
                        "align": "RIGHT"
                    }
                ],
                "inputsInline": false,
                "output": "NounPhrase",
                "colour": 65,
                "tooltip": "This is a noun phrase",
                "helpUrl": ""
            },
        {
            "type": "funcall",
            "message0": "Funcall %1 @object %2 @function %3 @field %4 @property %5 @arg1 %6 @arg2 %7 @key %8 @args %9 @optional %10 @post_modifiers %11 @features %12",
            "args0": [
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_value",
                    "name": "OBJECT",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FUNCTION",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FIELD",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "PROPERTY",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "ARG1",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "ARG2",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "KEY",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "ARGS",
                    "check": [
                        "Funcall",
                        "Phrase"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "OPTIONAL",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "POST_MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FEATURES",
                    "check": "Features",
                    "align": "RIGHT"
                }
            ],
            "inputsInline": false,
            "output": "Funcall",
            "colour": 330,
            "tooltip": "",
            "helpUrl": ""
        },

            {
                "type": "features",
                "message0": "Features %1 %2 %3 %4 %5 %6 %7 %8 %9 number %10 %11 %12 %13 %14 possessive %15 %16 pronominal %17 %18 markup_element %19 %20 markup_attributes %21 %22 head_markup_element %23 %24 head_markup_attributes %25 %26",
                "args0": [
                    {
                        "type": "input_dummy"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "TENSE_TICK",
                        "checked": false
                    },
                    {
                        "type": "field_dropdown",
                        "name": "TENSE_CHOICE",
                        "options": [
                            [
                                "present tense",
                                "PRESENT"
                            ],
                            [
                                "past tense",
                                "PAST"
                            ],
                            [
                                "future tense",
                                "FUTURE"
                            ]
                        ]
                    },
                    {
                        "type": "input_dummy",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "VOICE_TICK",
                        "checked": false
                    },
                    {
                        "type": "field_dropdown",
                        "name": "VOICE_CHOICE",
                        "options": [
                            [
                                "passive voice",
                                "PASSIVE"
                            ],
                            [
                                "active voice",
                                "ACTIVE"
                            ]
                        ]
                    },
                    {
                        "type": "input_dummy",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "NUMBER_TICK",
                        "checked": false
                    },
                    {
                        "type": "field_dropdown",
                        "name": "NUMBER_CHOICE",
                        "options": [
                            [
                                "singular",
                                "SINGULAR"
                            ],
                            [
                                "plural",
                                "PLURAL"
                            ]
                        ]
                    },
                    {
                        "type": "input_dummy",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "PARTICIPLE_TICK",
                        "checked": false
                    },
                    {
                        "type": "field_dropdown",
                        "name": "PARTICIPLE_CHOICE",
                        "options": [
                            [
                                "past participle",
                                "PAST_PARTICIPLE"
                            ],
                            [
                                "present participle",
                                "PRESENT_PARTICIPLE"
                            ]
                        ]
                    },
                    {
                        "type": "input_dummy",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "POSSESSIVE_TICK",
                        "checked": false
                    },
                    {
                        "type": "input_dummy",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "PRONOMINAL_TICK",
                        "checked": false
                    },
                    {
                        "type": "input_dummy",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "MARKUP_ELEMENT_TICK",
                        "checked": false
                    },
                    {
                        "type": "input_value",
                        "name": "MARKUP_ELEMENT",
                        "check": "String",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "MARKUP_ATTRIBUTES_TICK",
                        "checked": false
                    },
                    {
                        "type": "input_value",
                        "name": "MARKUP_ATTRIBUTES",
                        "check": [
                            "String",
                            "Funcall"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "HEAD_MARKUP_ELEMENT_TICK",
                        "checked": false
                    },
                    {
                        "type": "input_value",
                        "name": "HEAD_MARKUP_ELEMENT",
                        "check": "String",
                        "align": "RIGHT"
                    },
                    {
                        "type": "field_checkbox",
                        "name": "HEAD_MARKUP_ATTRIBUTES_TICK",
                        "checked": false
                    },
                    {
                        "type": "input_value",
                        "name": "HEAD_MARKUP_ATTRIBUTES",
                        "check": [
                            "String",
                            "Funcall"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_statement",
                        "name": "MAP",
                        "check": "Map"
                    }
                ],
                "output": "Features",
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            },
        {
            "type": "preposition_phrase",
            "message0": "Preposition phrase %1 preposition %2 noun %3 complements %4 specifier %5 features %6",
            "args0": [
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_value",
                    "name": "PREPOSITION",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "NOUN",
                    "check": [
                        "String",
                        "NounPhrase",
                        "Funcall"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "COMPLEMENTS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "SPECIFIER",
                    "check": [
                        "String",
                        "NounPhrase",
                        "Funcall",
                        "CoordinatedPhrase"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FEATURES",
                    "check": "Features",
                    "align": "RIGHT"
                }
            ],
            "output": "PrepositionPhrase",
            "colour": 160,
            "tooltip": "",
            "helpUrl": ""
        },
        {
            "type": "verb_phrase",
            "message0": "Verb Phrase %1 head %2 direct object %3 indirect object %4 complements %5 modifiers %6 pre-modifiers %7 post-modifiers %8 features %9",
            "args0": [
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_value",
                    "name": "HEAD",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "DIRECT_OBJECT",
                    "check": [
                        "String",
                        "NounPhrase",
                        "Funcall",
                        "CoordinatedPhrase"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "INDIRECT_OBJECT",
                    "check": [
                        "String",
                        "NounPhrase",
                        "Funcall",
                        "CoordinatedPhrase"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "COMPLEMENTS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "PRE_MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "POST_MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FEATURES",
                    "check": "Features",
                    "align": "RIGHT"
                }
            ],
            "output": "VerbPhrase",
            "colour": 210,
            "tooltip": "",
            "helpUrl": ""
        },
        {
            "type": "adjective_phrase",
            "message0": "Adjective Phrase %1 head %2 modifiers %3 pre-modifiers %4 post-modifiers %5 features %6",
            "args0": [
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_value",
                    "name": "HEAD",
                    "check": [
                        "String",
                        "Funcall"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "PRE_MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "POST_MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FEATURES",
                    "check": "Features",
                    "align": "RIGHT"
                }
            ],
            "output": "AdjectivePhrase",
            "colour": 255,
            "tooltip": "",
            "helpUrl": ""
        },
        {
            "type": "coordinated_phrase",
            "message0": "Coordinated Phrase %1 head %2 conjunction %3 coordinates %4 iterator %5 post-modifiers %6 features %7",
            "args0": [
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_value",
                    "name": "HEAD",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "CONJUNCTION",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "COORDINATES",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "PHRASE_ITERATOR",
                    "check": "PhraseIterator",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "POST_MODIFIERS",
                    "check": "Array",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FEATURES",
                    "check": "Features",
                    "align": "RIGHT"
                }
            ],
            "inputsInline": false,
            "output": "CoordinatedPhrase",
            "colour": 290,
            "tooltip": "",
            "helpUrl": ""
        },
            {
                "type": "adverb_phrase",
                "message0": "adverb-phrase %1 head %2 modifiers %3 pre-modifiers %4 post-modifiers %5",
                "args0": [
                    {
                        "type": "input_dummy"
                    },
                    {
                        "type": "input_value",
                        "name": "head",
                        "check": [
                            "String",
                            "Funcall"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "PRE_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "POST_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    }
                ],
                "output": "AdverbPhrase",
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            },
        {
            "type": "phrase_iterator",
            "message0": "Phrase Iterator %1 @variable %2 @until_string %3 @from %4 @flatten %5 @clause %6 @property %7 @element %8 @iterator %9",
            "args0": [
                {
                    "type": "input_dummy"
                },
                {
                    "type": "input_value",
                    "name": "VARIABLE",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "UNTIL_STRING",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FROM",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "FLATTEN",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "CLAUSE",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "PROPERTY",
                    "check": "String",
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "ELEMENT",
                    "check": [
                        "String",
                        "NounPhrase",
                        "Funcall"
                    ],
                    "align": "RIGHT"
                },
                {
                    "type": "input_value",
                    "name": "ITERATOR",
                    "check": "PhraseIterator",
                    "align": "RIGHT"
                }
            ],
            "output": "PhraseIterator",
            "colour": 75,
            "tooltip": "",
            "helpUrl": ""
        },
            {
                "type": "clause",
                "message0": "Clause %1 subject %2 verb %3 direct object %4 indirect object %5 complements %6 modifiers %7 front-modifiers %8 pre-modifiers %9 post-modifiers %10 complementiser %11 features %12",
                "args0": [
                    {
                        "type": "input_dummy"
                    },
                    {
                        "type": "input_value",
                        "name": "SUBJECT",
                        "check": [
                            "String",
                            "NounPhrase",
                            "VerbPhrase",
                            "Clause",
                            "Funcall",
                            "CoordinatedPhrase"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "VERB",
                        "check": [
                            "String",
                            "VerbPhrase",
                            "Funcall",
                            "CoordinatedPhrase"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "DIRECT_OBJECT",
                        "check": [
                            "String",
                            "NounPhrase",
                            "VerbPhrase",
                            "Clause",
                            "Funcall",
                            "CoordinatedPhrase"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "INDIRECT_OBJECT",
                        "check": [
                            "String",
                            "NounPhrase",
                            "VerbPhrase",
                            "Clause",
                            "Funcall",
                            "CoordinatedPhrase",
                            "PrepositionPhrase"
                        ],
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "COMPLEMENTS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "FRONT_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "PRE_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "POST_MODIFIERS",
                        "check": "Array",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "COMPLEMENTISER",
                        "check": "String",
                        "align": "RIGHT"
                    },
                    {
                        "type": "input_value",
                        "name": "FEATURES",
                        "check": "Features",
                        "align": "RIGHT"
                    }
                ],
                "output": null,
                "colour": 20,
                "tooltip": "",
                "helpUrl": ""
            },
        {
            "type": "map",
            "message0": "key %1 value %2",
            "args0": [
                {
                    "type": "input_value",
                    "name": "KEY",
                    "check": "String"
                },
                {
                    "type": "input_value",
                    "name": "VALUE"
                }
            ],
            "inputsInline": true,
            "previousStatement": "Map",
            "nextStatement": "Map",
            "colour": 330,
            "tooltip": "",
            "helpUrl": ""
        },
            {
                "type": "config",
                "message0": "profile %1",
                "args0": [
                    {
                        "type": "input_value",
                        "name": "PROFILE",
                        "check": "String",
                        "align": "RIGHT"
                    }
                ],
                "output": "Config",
                "colour": 230,
                "tooltip": "",
                "helpUrl": ""
            }]
);

