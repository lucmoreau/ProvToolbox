/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json_1) {
                /**
                 * A Gson deserializer adapter for PROV-JSON Decode a PROV-JSON structure and
                 * produces a {@link Document}
                 *
                 * @author Trung Dong Huynh &lt;trungdong@donggiang.com&gt;
                 * @param {org.openprovenance.prov.model.ProvFactory} pf
                 * @class
                 */
                class ProvDocumentDeserializer {
                    constructor(pf) {
                        if (this.documentNamespace === undefined) {
                            this.documentNamespace = null;
                        }
                        if (this.currentNamespace === undefined) {
                            this.currentNamespace = null;
                        }
                        if (this.pf === undefined) {
                            this.pf = null;
                        }
                        if (this.name === undefined) {
                            this.name = null;
                        }
                        if (this.vconv === undefined) {
                            this.vconv = null;
                        }
                        this.pf = pf;
                        this.name = pf.getName();
                        this.vconv = new org.openprovenance.prov.model.ValueConverter(pf);
                    }
                    stringToQualifiedName(namespace, val, pf, flag) {
                        return namespace.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(val, pf, flag);
                    }
                    /**
                     *
                     * @param {com.google.gson.JsonElement} json
                     * @param {*} typeOfT
                     * @param {*} context
                     * @return {*}
                     */
                    deserialize(json, typeOfT, context) {
                        const provJSONDoc = json.getAsJsonObject();
                        this.currentNamespace = this.decodePrefixes(provJSONDoc);
                        this.documentNamespace = this.currentNamespace;
                        const statements = this.decodeBundle(provJSONDoc);
                        const doc = this.pf.newDocument$();
                        doc.setNamespace(this.currentNamespace);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(doc.getStatementOrBundle(), statements);
                        return doc;
                    }
                    /*private*/ decodePrefixes(bundleStructure) {
                        const ns = new org.openprovenance.prov.model.Namespace();
                        ns.addKnownNamespaces();
                        const prefixes = this.getObjectAndRemove(bundleStructure, ProvDocumentDeserializer.PROV_JSON_PREFIX);
                        if (prefixes != null) {
                            {
                                let array131 = prefixes.keySet();
                                for (let index130 = 0; index130 < array131.length; index130++) {
                                    let prefix = array131[index130];
                                    {
                                        const uri = prefixes.get(prefix).toString();
                                        if (prefix === ("default")) {
                                            ns.registerDefault(uri);
                                        }
                                        else {
                                            ns.register(prefix, uri);
                                        }
                                    }
                                }
                            }
                        }
                        return ns;
                    }
                    /*private*/ decodeBundle(bundleStructure) {
                        const statements = ([]);
                        {
                            let array133 = bundleStructure.keySet();
                            for (let index132 = 0; index132 < array133.length; index132++) {
                                let statementType = array133[index132];
                                {
                                    const statementMap = bundleStructure.get(statementType).getAsJsonObject();
                                    /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(statements, this.decodeElements(statementType, statementMap));
                                }
                            }
                        }
                        return statements;
                    }
                    /*private*/ decodeElements(statementType, statementMap) {
                        const statements = ([]);
                        {
                            let array135 = statementMap.keySet();
                            for (let index134 = 0; index134 < array135.length; index134++) {
                                let idStr = array135[index134];
                                {
                                    const value = statementMap.get(idStr);
                                    if (value.isJsonArray()) {
                                        const elements = value.getAsJsonArray();
                                        const iter = elements.iterator();
                                        while ((iter.hasNext())) {
                                            {
                                                const attributeMap = iter.next().getAsJsonObject();
                                                const statement = this.decodeStatement(statementType, idStr, attributeMap);
                                                /* add */ (statements.push(statement) > 0);
                                            }
                                        }
                                        ;
                                    }
                                    else {
                                        const attributeMap = value.getAsJsonObject();
                                        const statement = this.decodeStatement(statementType, idStr, attributeMap);
                                        /* add */ (statements.push(statement) > 0);
                                    }
                                }
                            }
                        }
                        return statements;
                    }
                    /*private*/ decodeStatement(statementType, idStr, attributeMap) {
                        let statement;
                        let id;
                        const provStatement = org.openprovenance.prov.json.ProvJSONStatement[statementType];
                        switch ((provStatement)) {
                            case org.openprovenance.prov.json.ProvJSONStatement.bundle:
                                {
                                    id = null;
                                    break;
                                }
                                ;
                            default:
                                {
                                    if ( /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(idStr, "_:")) {
                                        id = null;
                                    }
                                    else {
                                        id = this.stringToQualifiedName(this.currentNamespace, idStr, this.pf, false);
                                    }
                                }
                                ;
                        }
                        switch ((provStatement)) {
                            case org.openprovenance.prov.json.ProvJSONStatement.entity:
                                statement = this.pf.newEntity$org_openprovenance_prov_model_QualifiedName(id);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.activity:
                                const startTime = this.optionalTime("prov:startTime", attributeMap);
                                const endTime = this.optionalTime("prov:endTime", attributeMap);
                                const a = this.pf.newActivity$org_openprovenance_prov_model_QualifiedName(id);
                                if (startTime != null) {
                                    a.setStartTime(startTime);
                                }
                                if (endTime != null) {
                                    a.setEndTime(endTime);
                                }
                                statement = a;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.agent:
                                statement = this.pf.newAgent$org_openprovenance_prov_model_QualifiedName(id);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.alternateOf:
                                const alternate1 = this.optionalQualifiedName("prov:alternate1", attributeMap);
                                const alternate2 = this.optionalQualifiedName("prov:alternate2", attributeMap);
                                statement = this.pf.newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(alternate1, alternate2);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasAssociatedWith:
                                let activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                let agent = this.optionalQualifiedName("prov:agent", attributeMap);
                                const plan = this.optionalQualifiedName("prov:plan", attributeMap);
                                const wAW = this.pf.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, agent);
                                if (plan != null)
                                    wAW.setPlan(plan);
                                statement = wAW;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasAttributedTo:
                                let entity = this.optionalQualifiedName("prov:entity", attributeMap);
                                agent = this.optionalQualifiedName("prov:agent", attributeMap);
                                statement = this.pf.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.bundle:
                                const ns = this.decodePrefixes(attributeMap);
                                this.currentNamespace = ns;
                                this.currentNamespace.setParent(this.documentNamespace);
                                id = this.stringToQualifiedName(this.currentNamespace, idStr, this.pf, false);
                                const statements = this.decodeBundle(attributeMap);
                                const namedBundle = this.pf.newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, ns, statements);
                                statement = namedBundle;
                                this.currentNamespace = this.documentNamespace;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasInformedBy:
                                const informed = this.optionalQualifiedName("prov:informed", attributeMap);
                                const informant = this.optionalQualifiedName("prov:informant", attributeMap);
                                statement = this.pf.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, informed, informant);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.actedOnBehalfOf:
                                const delegate = this.optionalQualifiedName("prov:delegate", attributeMap);
                                const responsible = this.optionalQualifiedName("prov:responsible", attributeMap);
                                activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                statement = this.pf.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible, activity);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasDerivedFrom:
                                const generatedEntity = this.optionalQualifiedName("prov:generatedEntity", attributeMap);
                                const usedEntity = this.optionalQualifiedName("prov:usedEntity", attributeMap);
                                activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                const generation = this.optionalQualifiedName("prov:generation", attributeMap);
                                const usage = this.optionalQualifiedName("prov:usage", attributeMap);
                                statement = this.pf.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, generatedEntity, usedEntity, activity, generation, usage, null);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasEndedBy:
                                activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                let trigger = this.optionalQualifiedName("prov:trigger", attributeMap);
                                const ender = this.optionalQualifiedName("prov:ender", attributeMap);
                                let time = this.optionalTime("prov:time", attributeMap);
                                const wEB = this.pf.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                                if (ender != null)
                                    wEB.setEnder(ender);
                                if (time != null) {
                                    wEB.setTime(time);
                                }
                                statement = wEB;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasGeneratedBy:
                                entity = this.optionalQualifiedName("prov:entity", attributeMap);
                                activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                time = this.optionalTime("prov:time", attributeMap);
                                const wGB = this.pf.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, ([]));
                                statement = wGB;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasInfluencedBy:
                                const influencee = this.anyRef("prov:influencee", attributeMap);
                                const influencer = this.anyRef("prov:influencer", attributeMap);
                                statement = this.pf.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasInvalidatedBy:
                                entity = this.optionalQualifiedName("prov:entity", attributeMap);
                                activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                time = this.optionalTime("prov:time", attributeMap);
                                const wIB = this.pf.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
                                if (time != null) {
                                    wIB.setTime(time);
                                }
                                statement = wIB;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.hadMember:
                                const collection = this.optionalQualifiedName("prov:collection", attributeMap);
                                const entities = this.optionalQualifiedNames("prov:entity", attributeMap);
                                const membership = this.pf.newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(collection, entities);
                                statement = membership;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.mentionOf:
                                let specificEntity = this.optionalQualifiedName("prov:specificEntity", attributeMap);
                                let generalEntity = this.optionalQualifiedName("prov:generalEntity", attributeMap);
                                const bundle = this.optionalQualifiedName("prov:bundle", attributeMap);
                                statement = this.pf.newMentionOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specificEntity, generalEntity, bundle);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.specializationOf:
                                specificEntity = this.optionalQualifiedName("prov:specificEntity", attributeMap);
                                generalEntity = this.optionalQualifiedName("prov:generalEntity", attributeMap);
                                statement = this.pf.newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specificEntity, generalEntity);
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.wasStartedBy:
                                activity = this.optionalQualifiedName("prov:activity", attributeMap);
                                trigger = this.optionalQualifiedName("prov:trigger", attributeMap);
                                const starter = this.optionalQualifiedName("prov:starter", attributeMap);
                                time = this.optionalTime("prov:time", attributeMap);
                                const wSB = this.pf.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                                if (starter != null)
                                    wSB.setStarter(starter);
                                if (time != null) {
                                    wSB.setTime(time);
                                }
                                statement = wSB;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.used:
                                const activity2 = this.optionalQualifiedName("prov:activity", attributeMap);
                                const entity2 = this.optionalQualifiedName("prov:entity", attributeMap);
                                time = this.optionalTime("prov:time", attributeMap);
                                const wUB = this.pf.newUsed$org_openprovenance_prov_model_QualifiedName(id);
                                wUB.setActivity(activity2);
                                if (entity2 != null)
                                    wUB.setEntity(entity2);
                                if (time != null) {
                                    wUB.setTime(time);
                                }
                                statement = wUB;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.derivedByInsertionFrom:
                                let before = this.optionalQualifiedName("prov:before", attributeMap);
                                let after = this.optionalQualifiedName("prov:after", attributeMap);
                                let keyEntitySet = this.optionalEntrySet("prov:key-entity-set", attributeMap);
                                const dBIF = this.pf.newDerivedByInsertionFrom(id, after, before, keyEntitySet, null);
                                statement = dBIF;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.derivedByRemovalFrom:
                                before = this.optionalQualifiedName("prov:before", attributeMap);
                                after = this.optionalQualifiedName("prov:after", attributeMap);
                                const keys = this.optionalKeySet("prov:key-set", attributeMap);
                                const dBRF = this.pf.newDerivedByRemovalFrom(id, after, before, keys, null);
                                statement = dBRF;
                                break;
                            case org.openprovenance.prov.json.ProvJSONStatement.hadDictionaryMember:
                                const dictionary = this.optionalQualifiedName("prov:dictionary", attributeMap);
                                keyEntitySet = this.optionalEntrySet("prov:key-entity-set", attributeMap);
                                const hDM = this.pf.newDictionaryMembership(dictionary, keyEntitySet);
                                statement = hDM;
                                break;
                            default:
                                statement = null;
                                break;
                        }
                        let values = this.popMultiValAttribute("prov:type", attributeMap);
                        if (!(values.length == 0)) {
                            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) {
                                const types = statement.getType();
                                for (let index136 = 0; index136 < values.length; index136++) {
                                    let value = values[index136];
                                    {
                                        /* add */ (types.push(this.decodeAttributeValue(value, this.name.PROV_TYPE)) > 0);
                                    }
                                }
                            }
                            else {
                                throw Object.defineProperty(new Error("prov:type is not allowed in a " + statementType + "statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        values = this.popMultiValAttribute("prov:label", attributeMap);
                        if (!(values.length == 0)) {
                            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) {
                                const labels = statement.getLabel();
                                for (let index137 = 0; index137 < values.length; index137++) {
                                    let value = values[index137];
                                    {
                                        /* add */ (labels.push(this.decodeInternationalizedString(value)) > 0);
                                    }
                                }
                            }
                            else {
                                throw Object.defineProperty(new Error("prov:label is not allowed in a " + statementType + "statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        values = this.popMultiValAttribute("prov:location", attributeMap);
                        if (!(values.length == 0)) {
                            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0)) {
                                const locations = statement.getLocation();
                                for (let index138 = 0; index138 < values.length; index138++) {
                                    let value = values[index138];
                                    {
                                        /* add */ (locations.push(this.decodeAttributeValue(value, this.name.PROV_LOCATION)) > 0);
                                    }
                                }
                            }
                            else {
                                throw Object.defineProperty(new Error("prov:location is not allowed in a " + statementType + "statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        values = this.popMultiValAttribute("prov:role", attributeMap);
                        if (!(values.length == 0)) {
                            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasRole") >= 0)) {
                                const roles = statement.getRole();
                                for (let index139 = 0; index139 < values.length; index139++) {
                                    let value = values[index139];
                                    {
                                        /* add */ (roles.push(this.decodeAttributeValue(value, this.name.PROV_ROLE)) > 0);
                                    }
                                }
                            }
                            else {
                                throw Object.defineProperty(new Error("prov:role is not allowed in a " + statementType + "statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        values = this.popMultiValAttribute("prov:value", attributeMap);
                        if (!(values.length == 0)) {
                            if ( /* size */values.length > 1) {
                                throw Object.defineProperty(new Error("Only one instance of prov:value is allowed in a statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasValue") >= 0)) {
                                statement.setValue(this.decodeAttributeValue(/* get */ values[0], this.name.PROV_VALUE));
                            }
                            else {
                                throw Object.defineProperty(new Error("prov:value is not allowed in a " + statementType + "statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        if (provStatement !== org.openprovenance.prov.json.ProvJSONStatement.bundle && !(attributeMap.keySet().length == 0)) {
                            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasOther") >= 0)) {
                                const ll = statement.getOther();
                                const attributes = ll;
                                {
                                    let array141 = attributeMap.keySet();
                                    for (let index140 = 0; index140 < array141.length; index140++) {
                                        let key = array141[index140];
                                        {
                                            const attributeName = this.stringToQualifiedName(this.currentNamespace, key, this.pf, false);
                                            const element = attributeMap.get(key);
                                            values = this.pickMultiValues(element);
                                            for (let index142 = 0; index142 < values.length; index142++) {
                                                let value = values[index142];
                                                {
                                                    const attr = this.decodeAttributeValue(value, attributeName);
                                                    /* add */ (attributes.push(attr) > 0);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                throw Object.defineProperty(new Error("Arbitrary attributes are not allowed in a " + statementType + "statement - id: " + idStr), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                            }
                        }
                        return statement;
                    }
                    /*private*/ decodeInternationalizedString(element) {
                        const iString = this.pf.newInternationalizedString$java_lang_String("s");
                        if (element.isJsonPrimitive()) {
                            iString.setValue(element.toString());
                        }
                        else {
                            const struct = element.getAsJsonObject();
                            const value = struct.get("$").getAsJsonPrimitive().getAsString();
                            iString.setValue(value);
                            if (struct.has("lang")) {
                                const lang = struct.get("lang").getAsJsonPrimitive().toString();
                                iString.setLang(lang);
                            }
                        }
                        return iString;
                    }
                    /*private*/ decodeAttributeValue(element, elementName) {
                        if (element.isJsonPrimitive()) {
                            const o = this.decodeJSONPrimitive(element.getAsJsonPrimitive().getAsString());
                            const type = this.vconv.getXsdType(o);
                            return this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, o, type);
                        }
                        else {
                            const struct = element.getAsJsonObject();
                            const value = struct.get("$").getAsJsonPrimitive().getAsString();
                            if (struct.has("lang")) {
                                const lang = struct.get("lang").getAsJsonPrimitive().getAsString();
                                const iString = this.pf.newInternationalizedString$java_lang_String$java_lang_String(value, lang);
                                return this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, iString, this.name.PROV_LANG_STRING);
                            }
                            else if (struct.has("type")) {
                                const datatypeAsString = struct.get("type").getAsJsonPrimitive().getAsString();
                                const xsdType = this.stringToQualifiedName(this.currentNamespace, datatypeAsString, this.pf, false);
                                if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                                    return o1.equals(o2);
                                }
                                else {
                                    return o1 === o2;
                                } })(xsdType, this.name.PROV_QUALIFIED_NAME)) {
                                    return this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, this.stringToQualifiedName(this.currentNamespace, value, this.pf, false), xsdType);
                                }
                                else {
                                    return this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, xsdType);
                                }
                            }
                            else {
                                const iString = this.pf.getObjectFactory().createInternationalizedString();
                                iString.setValue(value);
                                return this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, iString, this.name.PROV_LANG_STRING);
                            }
                        }
                    }
                    /*private*/ decodeJSONPrimitive(value) {
                        if (value === "true") {
                            return true;
                        }
                        else if (value === "false") {
                            return false;
                        }
                        try {
                            return /* parseInt */ parseInt(value);
                        }
                        catch (ex1) {
                            try {
                                return /* parseDouble */ parseFloat(value);
                            }
                            catch (ex2) {
                                return value;
                            }
                        }
                    }
                    /*private*/ popMultiValAttribute(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName)) {
                            const element = attributeMap.remove(attributeName);
                            return this.pickMultiValues(element);
                        }
                        else
                            return /* emptyList */ [];
                    }
                    /*private*/ pickMultiValues(element) {
                        if (element.isJsonArray()) {
                            const array = element.getAsJsonArray();
                            const elements = ([]);
                            const iter = array.iterator();
                            while ((iter.hasNext())) {
                                {
                                    /* add */ (elements.push(iter.next()) > 0);
                                }
                            }
                            ;
                            return elements;
                        }
                        else {
                            return /* singletonList */ [element];
                        }
                    }
                    /*private*/ popString(jo, memberName) {
                        return jo.remove(memberName).getAsJsonPrimitive().getAsString();
                    }
                    /*private*/ qualifiedName(attributeName, attributeMap) {
                        return this.stringToQualifiedName(this.currentNamespace, this.popString(attributeMap, attributeName), this.pf, false);
                    }
                    /*private*/ anyRef(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName))
                            return this.stringToQualifiedName(this.currentNamespace, this.popString(attributeMap, attributeName), this.pf, false);
                        else
                            return null;
                    }
                    /*private*/ optionalQualifiedName(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName))
                            return this.qualifiedName(attributeName, attributeMap);
                        else
                            return null;
                    }
                    /*private*/ optionalTime(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName))
                            return this.pf.newISOTime(this.popString(attributeMap, attributeName));
                        else
                            return null;
                    }
                    /*private*/ qualifiedNames(attributeName, attributeMap) {
                        const results = ([]);
                        const elements = this.popMultiValAttribute(attributeName, attributeMap);
                        for (let index143 = 0; index143 < elements.length; index143++) {
                            let element = elements[index143];
                            {
                                /* add */ (results.push(this.stringToQualifiedName(this.currentNamespace, element.getAsJsonPrimitive().getAsString(), this.pf, false)) > 0);
                            }
                        }
                        return results;
                    }
                    /*private*/ optionalQualifiedNames(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName))
                            return this.qualifiedNames(attributeName, attributeMap);
                        else
                            return null;
                    }
                    /*private*/ entrySet(attributeName, attributeMap) {
                        const results = ([]);
                        const kESElement = attributeMap.remove(attributeName);
                        if (kESElement.isJsonArray()) {
                            const elements = this.pickMultiValues(kESElement);
                            for (let index144 = 0; index144 < elements.length; index144++) {
                                let element = elements[index144];
                                {
                                    const item = element.getAsJsonObject();
                                    const pair = this.pf.newEntry(this.decodeAttributeValue(item.remove("key"), this.name.PROV_KEY), this.stringToQualifiedName(this.currentNamespace, this.popString(item, "$"), this.pf, false));
                                    /* add */ (results.push(pair) > 0);
                                }
                            }
                        }
                        else if (kESElement.isJsonObject()) {
                            const dictionary = kESElement.getAsJsonObject();
                            const keyDatatype = dictionary.remove("$key-datatype").getAsJsonPrimitive().getAsString();
                            const datatype = this.stringToQualifiedName(this.currentNamespace, keyDatatype, this.pf, false);
                            {
                                let array146 = dictionary.keySet();
                                for (let index145 = 0; index145 < array146.length; index145++) {
                                    let entryKey = array146[index145];
                                    {
                                        const entryValue = dictionary.get(entryKey);
                                        const pair = this.decodeDictionaryEntry(datatype, entryKey, entryValue);
                                        /* add */ (results.push(pair) > 0);
                                    }
                                }
                            }
                        }
                        return results;
                    }
                    decodeDictionaryEntry(datatype, entryKey, entryValue) {
                        let kk;
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(datatype, this.name.PROV_QUALIFIED_NAME)) {
                            kk = this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.name.PROV_KEY, this.stringToQualifiedName(this.currentNamespace, entryKey, this.pf, false), datatype);
                        }
                        else {
                            kk = this.pf.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.name.PROV_KEY, entryKey, datatype);
                        }
                        const pair = this.pf.newEntry(kk, this.stringToQualifiedName(this.currentNamespace, entryValue.getAsJsonPrimitive().getAsString(), this.pf, false));
                        return pair;
                    }
                    /*private*/ optionalEntrySet(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName))
                            return this.entrySet(attributeName, attributeMap);
                        else
                            return null;
                    }
                    /*private*/ keySet(attributeName, attributeMap) {
                        const results = ([]);
                        const elements = this.popMultiValAttribute(attributeName, attributeMap);
                        for (let index147 = 0; index147 < elements.length; index147++) {
                            let element = elements[index147];
                            {
                                const key = this.decodeAttributeValue(element, this.name.PROV_KEY);
                                /* add */ (results.push(key) > 0);
                            }
                        }
                        return results;
                    }
                    /*private*/ optionalKeySet(attributeName, attributeMap) {
                        if (attributeMap.has(attributeName))
                            return this.keySet(attributeName, attributeMap);
                        else
                            return null;
                    }
                    /*private*/ getObjectAndRemove(jsonObject, memberName) {
                        if (jsonObject.has(memberName)) {
                            const result = jsonObject.getAsJsonObject$java_lang_String(memberName);
                            jsonObject.remove(memberName);
                            return result;
                        }
                        else
                            return null;
                    }
                }
                ProvDocumentDeserializer.PROV_JSON_PREFIX = "prefix";
                json_1.ProvDocumentDeserializer = ProvDocumentDeserializer;
                ProvDocumentDeserializer["__class"] = "org.openprovenance.prov.json.ProvDocumentDeserializer";
                ProvDocumentDeserializer["__interfaces"] = ["com.google.gson.JsonDeserializer"];
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
