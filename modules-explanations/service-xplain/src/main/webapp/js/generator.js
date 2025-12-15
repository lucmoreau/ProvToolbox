function exists(x) {
    return (x && x!=="");
}

const preModifiers = 'pre-modifiers';
const postModifiers = 'post-modifiers';
Blockly.JavaScript['noun_phrase'] = function(block) {
    var value_determiner = Blockly.JavaScript.valueToCode(block, 'DETERMINER', Blockly.JavaScript.ORDER_ATOMIC);
    var value_head = Blockly.JavaScript.valueToCode(block, 'HEAD', Blockly.JavaScript.ORDER_ATOMIC);
    var value_modifiers = Blockly.JavaScript.valueToCode(block, 'MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_pre_modifiers = Blockly.JavaScript.valueToCode(block, 'PRE_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_complements = Blockly.JavaScript.valueToCode(block, 'COMPLEMENTS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_specifier = Blockly.JavaScript.valueToCode(block, 'SPECIFIER', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="noun_phrase";
    if (exists(value_determiner)) phrase['determiner']=JSON.parse(value_determiner);
    if (exists(value_head)) phrase['head']=JSON.parse(value_head);
    if (exists(value_features)) phrase['features']=JSON.parse(value_features);
    if (exists(value_complements)) phrase['complements']=JSON.parse(value_complements);
    if (exists(value_pre_modifiers)) phrase[preModifiers]=JSON.parse(value_pre_modifiers);
    if (exists(value_post_modifiers)) phrase[postModifiers]=JSON.parse(value_post_modifiers);
    if (exists(value_specifier)) phrase['specifier']=JSON.parse(value_specifier);
    if (exists(value_modifiers)) phrase['modifiers']=JSON.parse(value_modifiers);
    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['config'] = function(block) {
    var value_profile = Blockly.JavaScript.valueToCode(block, 'PROFILE', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="config";
    if (exists(value_profile)) phrase.config=JSON.parse(value_profile);
    console.log(phrase);
    var code=JSON.stringify(phrase);
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
    //return code;
};


Blockly.JavaScript['funcall'] = function(block) {
    var value_object = Blockly.JavaScript.valueToCode(block, 'OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
    var value_function = Blockly.JavaScript.valueToCode(block, 'FUNCTION', Blockly.JavaScript.ORDER_ATOMIC);
    var value_field = Blockly.JavaScript.valueToCode(block, 'FIELD', Blockly.JavaScript.ORDER_ATOMIC);
    var value_property = Blockly.JavaScript.valueToCode(block, 'PROPERTY', Blockly.JavaScript.ORDER_ATOMIC);
    var value_arg1 = Blockly.JavaScript.valueToCode(block, 'ARG1', Blockly.JavaScript.ORDER_ATOMIC);
    var value_arg2 = Blockly.JavaScript.valueToCode(block, 'ARG2', Blockly.JavaScript.ORDER_ATOMIC);
    var value_args = Blockly.JavaScript.valueToCode(block, 'ARGS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_key = Blockly.JavaScript.valueToCode(block, 'KEY', Blockly.JavaScript.ORDER_ATOMIC);
    var value_optional = Blockly.JavaScript.valueToCode(block, 'OPTIONAL', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.

    var phrase = {};
    //var args={};
    phrase["type"]="@funcall";
    if (exists(value_object))   phrase["@object"]  =JSON.parse(value_object);
    if (exists(value_function)) phrase["@function"]=JSON.parse(value_function);
    if (exists(value_field))    phrase["@field"]   =JSON.parse(value_field);
    if (exists(value_property)) phrase["@property"]=JSON.parse(value_property);
    if (exists(value_arg1))     phrase["@arg1"]    =JSON.parse(value_arg1);
    if (exists(value_arg2))     phrase["@arg2"]    =JSON.parse(value_arg2);
    if (exists(value_key))      phrase["@key"]     =JSON.parse(value_key);
    if (exists(value_optional)) phrase["@optional"]=JSON.parse(value_optional);

    //phrase["args"]=args;
    if (exists(value_post_modifiers)) phrase[postModifiers]=JSON.parse(value_post_modifiers);
    if (exists(value_features))       phrase['features']=JSON.parse(value_features);

    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};



Blockly.JavaScript['features'] = function(block) {
    var checkbox_tense_tick = block.getFieldValue('TENSE_TICK') === 'TRUE';
    var dropdown_tense_choice = block.getFieldValue('TENSE_CHOICE');
    var checkbox_voice_tick = block.getFieldValue('VOICE_TICK') === 'TRUE';
    var dropdown_voice_choice = block.getFieldValue('VOICE_CHOICE');
    var checkbox_number_tick = block.getFieldValue('NUMBER_TICK') === 'TRUE';
    var dropdown_number_choice = block.getFieldValue('NUMBER_CHOICE');
    var checkbox_participle_tick = block.getFieldValue('PARTICIPLE_TICK') === 'TRUE';
    var dropdown_participle_choice = block.getFieldValue('PARTICIPLE_CHOICE');
    var checkbox_possessive_tick = block.getFieldValue('POSSESSIVE_TICK') === 'TRUE';
    var checkbox_pronominal_tick = block.getFieldValue('PRONOMINAL_TICK') === 'TRUE';

    var checkbox_markup_element_tick = block.getFieldValue('MARKUP_ELEMENT_TICK') === 'TRUE';
    var checkbox_markup_attributes_tick = block.getFieldValue('MARKUP_ATTRIBUTES_TICK') === 'TRUE';
    var checkbox_head_markup_element_tick = block.getFieldValue('HEAD_MARKUP_ELEMENT_TICK') === 'TRUE';
    var checkbox_head_markup_attributes_tick = block.getFieldValue('HEAD_MARKUP_ATTRIBUTES_TICK') === 'TRUE';


    var markup_element = Blockly.JavaScript.valueToCode(block, 'MARKUP_ELEMENT', Blockly.JavaScript.ORDER_ATOMIC);
    var markup_attributes = Blockly.JavaScript.valueToCode(block, 'MARKUP_ATTRIBUTES', Blockly.JavaScript.ORDER_ATOMIC);
    var head_markup_element = Blockly.JavaScript.valueToCode(block, 'HEAD_MARKUP_ELEMENT', Blockly.JavaScript.ORDER_ATOMIC);
    var head_markup_attributes = Blockly.JavaScript.valueToCode(block, 'HEAD_MARKUP_ATTRIBUTES', Blockly.JavaScript.ORDER_ATOMIC);


    //var statements_map = Blockly.JavaScript.statementToCode(block, 'MAP');

    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="features";
    if (checkbox_tense_tick) {
        if (dropdown_tense_choice === "PRESENT") phrase["tense"] = "present";
        if (dropdown_tense_choice === "PAST") phrase["tense"] = "past";
        if (dropdown_tense_choice === "FUTURE") phrase["tense"] = "future";
    }
    if (checkbox_voice_tick) {
        phrase["passive"] = "" + (dropdown_voice_choice === "PASSIVE");
    }
    if (checkbox_number_tick) {
        if (dropdown_number_choice === "SINGULAR") phrase["number"] = "singular";
        if (dropdown_number_choice === "PLURAL") phrase["number"] = "plural";
    }
    if (checkbox_participle_tick) {
        if (dropdown_participle_choice === "PAST_PARTICIPLE") phrase["form"] = "pastParticiple";
        if (dropdown_participle_choice === "PRESENT_PARTICIPLE") phrase["form"] = "presentParticiple";
    }
    if (checkbox_possessive_tick) {
        phrase["possessive"]="true";
    }
    if (checkbox_pronominal_tick) {
        phrase["pronominal"]="true";
    }


    if (checkbox_markup_element_tick) {
        if (exists(markup_element)) phrase["markup_element"]=JSON.parse(markup_element)
    }
    if (checkbox_markup_attributes_tick) {
        if (exists(markup_attributes)) phrase["markup_attributes"]=JSON.parse(markup_attributes)
    }
    if (checkbox_head_markup_element_tick) {
        if (exists(head_markup_element)) phrase["head_markup_element"]=JSON.parse(head_markup_element)
    }
    if (checkbox_head_markup_attributes_tick) {
        if (exists(head_markup_attributes)) phrase["head_markup_attributes"]=JSON.parse(head_markup_attributes)
    }


    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};


Blockly.JavaScript['preposition_phrase'] = function(block) {
    var value_preposition = Blockly.JavaScript.valueToCode(block, 'PREPOSITION', Blockly.JavaScript.ORDER_ATOMIC);
    var value_noun = Blockly.JavaScript.valueToCode(block, 'NOUN', Blockly.JavaScript.ORDER_ATOMIC);
    var value_complements = Blockly.JavaScript.valueToCode(block, 'COMPLEMENTS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_specifier = Blockly.JavaScript.valueToCode(block, 'SPECIFIER', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="preposition_phrase";
    if (exists(value_preposition)) phrase['preposition']=JSON.parse(value_preposition);
    if (exists(value_noun)) phrase['noun']=JSON.parse(value_noun);
    if (exists(value_complements)) phrase['complements']=value_complements;
    if (exists(value_specifier)) phrase['specifier']=value_specifier;
    if (exists(value_features)) phrase['features']=JSON.parse(value_features);
    var code=JSON.stringify(phrase);

    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['verb_phrase'] = function(block) {
    var value_head = Blockly.JavaScript.valueToCode(block, 'HEAD', Blockly.JavaScript.ORDER_ATOMIC);
    var value_direct_object = Blockly.JavaScript.valueToCode(block, 'DIRECT_OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
    var value_indirect_object = Blockly.JavaScript.valueToCode(block, 'INDIRECT_OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
    var value_complements = Blockly.JavaScript.valueToCode(block, 'COMPLEMENTS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_modifiers = Blockly.JavaScript.valueToCode(block, 'MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_pre_modifiers = Blockly.JavaScript.valueToCode(block, 'PRE_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="verb_phrase";
    if (exists(value_head)) phrase['head']=JSON.parse(value_head);
    if (exists(value_direct_object))   phrase['object']=JSON.parse(value_direct_object);
    if (exists(value_indirect_object)) phrase['indirect_object']=JSON.parse(value_indirect_object);
    if (exists(value_complements))     phrase['complements']=JSON.parse(value_complements);
    if (exists(value_features)) phrase['features']=JSON.parse(value_features);
    if (exists(value_modifiers)) phrase['modifiers']=JSON.parse(value_modifiers);
    if (exists(value_pre_modifiers)) phrase[preModifiers]=JSON.parse(value_pre_modifiers);
    if (exists(value_post_modifiers)) phrase[postModifiers]=JSON.parse(value_post_modifiers);
    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['adjective_phrase'] = function(block) {
    var value_head = Blockly.JavaScript.valueToCode(block, 'HEAD', Blockly.JavaScript.ORDER_ATOMIC);
    var value_modifiers = Blockly.JavaScript.valueToCode(block, 'MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_pre_modifiers = Blockly.JavaScript.valueToCode(block, 'PRE_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="adjective_phrase";
    if (exists(value_head)) phrase['head']=JSON.parse(value_head);
    if (exists(value_features)) phrase['features']=JSON.parse(value_features);
    if (exists(value_modifiers)) phrase['modifiers']=JSON.parse(value_modifiers);
    if (exists(value_pre_modifiers)) phrase[preModifiers]=JSON.parse(value_pre_modifiers);
    if (exists(value_post_modifiers)) phrase[postModifiers]=JSON.parse(value_post_modifiers);
    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['adverb_phrase'] = function(block) {
    var value_head = Blockly.JavaScript.valueToCode(block, 'HEAD', Blockly.JavaScript.ORDER_ATOMIC);
    var value_modifiers = Blockly.JavaScript.valueToCode(block, 'MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_pre_modifiers = Blockly.JavaScript.valueToCode(block, 'PRE_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="adverb_phrase";
    if (exists(value_head)) phrase['head']=JSON.parse(value_head);
    if (exists(value_features)) phrase['features']=JSON.parse(value_features);
    if (exists(value_modifiers)) phrase['modifiers']=JSON.parse(value_modifiers);
    if (exists(value_pre_modifiers)) phrase[preModifiers]=JSON.parse(value_pre_modifiers);
    if (exists(value_post_modifiers)) phrase[postModifiers]=JSON.parse(value_post_modifiers);
    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['coordinated_phrase'] = function(block) {
    var value_head = Blockly.JavaScript.valueToCode(block, 'HEAD', Blockly.JavaScript.ORDER_ATOMIC);
    var value_conjunction = Blockly.JavaScript.valueToCode(block, 'CONJUNCTION', Blockly.JavaScript.ORDER_ATOMIC);
    var value_coordinates = Blockly.JavaScript.valueToCode(block, 'COORDINATES', Blockly.JavaScript.ORDER_ATOMIC);
    var value_phrase_iterator = Blockly.JavaScript.valueToCode(block, 'PHRASE_ITERATOR', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="coordinated_phrase";
    if (exists(value_head)) phrase['head']=JSON.parse(value_head);
    if (exists(value_features)) phrase['features']=JSON.parse(value_features);
    if (exists(value_conjunction)) phrase['conjunction']=JSON.parse(value_conjunction);
    if (exists(value_coordinates)) phrase['coordinates']=JSON.parse(value_coordinates);
    if (exists(value_phrase_iterator)) phrase['@iterator']=JSON.parse(value_phrase_iterator);
    if (exists(value_post_modifiers)) phrase[postModifiers]=JSON.parse(value_post_modifiers);
    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['phrase_iterator'] = function(block) {
    var value_variable = Blockly.JavaScript.valueToCode(block, 'VARIABLE', Blockly.JavaScript.ORDER_ATOMIC);
    var value_until_string = Blockly.JavaScript.valueToCode(block, 'UNTIL_STRING', Blockly.JavaScript.ORDER_ATOMIC);
    var value_from = Blockly.JavaScript.valueToCode(block, 'FROM', Blockly.JavaScript.ORDER_ATOMIC);
    var value_flatten = Blockly.JavaScript.valueToCode(block, 'FLATTEN', Blockly.JavaScript.ORDER_ATOMIC);
    var value_clause = Blockly.JavaScript.valueToCode(block, 'CLAUSE', Blockly.JavaScript.ORDER_ATOMIC);
    var value_property = Blockly.JavaScript.valueToCode(block, 'PROPERTY', Blockly.JavaScript.ORDER_ATOMIC);
    var value_element = Blockly.JavaScript.valueToCode(block, 'ELEMENT', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="@iterator";
    if (exists(value_variable))     phrase['@variable']     =JSON.parse(value_variable);
    if (exists(value_until_string)) phrase['@until_string'] =JSON.parse(value_until_string);
    if (exists(value_from))         phrase['@from']         =JSON.parse(value_from);
    if (exists(value_flatten))      phrase['@flatten']      =JSON.parse(value_flatten);
    if (exists(value_clause))       phrase['@clause']       =JSON.parse(value_clause);
    if (exists(value_property))     phrase['@property']     =JSON.parse(value_property);
    if (exists(value_element))      phrase['@element']      =JSON.parse(value_element);
    var code=JSON.stringify(phrase);

    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['clause'] = function(block) {
    var value_subject = Blockly.JavaScript.valueToCode(block, 'SUBJECT', Blockly.JavaScript.ORDER_ATOMIC);
    var value_verb = Blockly.JavaScript.valueToCode(block, 'VERB', Blockly.JavaScript.ORDER_ATOMIC);
    var value_direct_object = Blockly.JavaScript.valueToCode(block, 'DIRECT_OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
    var value_indirect_object = Blockly.JavaScript.valueToCode(block, 'INDIRECT_OBJECT', Blockly.JavaScript.ORDER_ATOMIC);
    var value_complements = Blockly.JavaScript.valueToCode(block, 'COMPLEMENTS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_modifiers = Blockly.JavaScript.valueToCode(block, 'MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_front_modifiers = Blockly.JavaScript.valueToCode(block, 'FRONT_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_pre_modifiers = Blockly.JavaScript.valueToCode(block, 'PRE_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_post_modifiers = Blockly.JavaScript.valueToCode(block, 'POST_MODIFIERS', Blockly.JavaScript.ORDER_ATOMIC);
    var value_complementiser = Blockly.JavaScript.valueToCode(block, 'COMPLEMENTISER', Blockly.JavaScript.ORDER_ATOMIC);
    var value_features = Blockly.JavaScript.valueToCode(block, 'FEATURES', Blockly.JavaScript.ORDER_ATOMIC);
    // TODO: Assemble JavaScript into code variable.
    var phrase = {};
    phrase["type"]="clause";
    if (exists(value_subject))         phrase['subject']=JSON.parse(value_subject);
    if (exists(value_verb))            phrase['verb']=JSON.parse(value_verb);
    if (exists(value_direct_object))   phrase['object']=JSON.parse(value_direct_object);
    if (exists(value_indirect_object)) phrase['indirect_object']=JSON.parse(value_indirect_object);
    if (exists(value_complements))     phrase['complements']=JSON.parse(value_complements);
    if (exists(value_modifiers))       phrase['modifiers']=JSON.parse(value_modifiers);
    if (exists(value_front_modifiers)) phrase['front-modifiers']=JSON.parse(value_front_modifiers);
    if (exists(value_pre_modifiers))   phrase['pre-modifiers']=JSON.parse(value_pre_modifiers);
    if (exists(value_post_modifiers))  phrase['post-modifiers']=JSON.parse(value_post_modifiers);
    if (exists(value_complementiser))  phrase['complementiser']=JSON.parse(value_complementiser);
    if (exists(value_features))        phrase['features']=JSON.parse(value_features);
    var code=JSON.stringify(phrase);
    // TODO: Change ORDER_NONE to the correct strength.
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

/*
function wrapInArrayIfNecessary (v) {
    if (Array.isArray()) {
        return v;
    } else {
        var tmp=[];
        tmp.push(v);
        return tmp;
    }
}
 */


Blockly.JavaScript['text'] = function(block) {
    var textValue = block.getFieldValue('TEXT');
    var code = '"' + textValue + '"';
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

Blockly.JavaScript['lists_create_with'] = function(block) {
    // Create a list with any number of elements of any type.
    var elements = new Array(block.itemCount_);
    for (var i = 0; i < block.itemCount_; i++) {
        elements[i] = Blockly.JavaScript.valueToCode(block, 'ADD' + i,
            Blockly.JavaScript.ORDER_NONE) || 'null';
        elements[i] = JSON.parse(elements[i])

    }
    //var code = '[' + elements.join(', ') + ']';
    var code=JSON.stringify(elements);
    return [code, Blockly.JavaScript.ORDER_ATOMIC];
};
