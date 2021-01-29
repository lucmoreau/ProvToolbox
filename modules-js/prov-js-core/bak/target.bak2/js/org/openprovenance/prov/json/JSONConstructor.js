/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var json;
            (function (json) {
                /**
                 * @author Trung Dong Huynh
                 *
                 * Constructing a JSON structure from a Document that
                 * follows the PROV-JSON representation.
                 * @param {org.openprovenance.prov.model.Name} name
                 * @class
                 */
                class JSONConstructor {
                    constructor(name) {
                        this.documentNamespace = null;
                        this.currentNamespace = null;
                        this.documentBundles = ({});
                        this.documentRecords = ([]);
                        this.currentRecords = this.documentRecords;
                        if (this.name === undefined) {
                            this.name = null;
                        }
                        this.name = name;
                    }
                    getJSONStructure$() {
                        const document = this.getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(this.documentRecords, this.documentNamespace);
                        console.log("document")
                        console.log(document)
                        console.log(JSON.stringify(document))
                        if (!(Object.keys(this.documentBundles).length == 0)) /* put */
                            (document["bundle"] = this.documentBundles);
                        return document;
                    }
                    getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(records, namespace) {
                        const bundle = ({});
                        const prefixes = (((o) => { let r = {}; for (let p in o)
                            r[p] = o[p]; return r; })(namespace.getPrefixes()));
                        if (namespace.getDefaultNamespace() != null) {
                            /* put */ (prefixes["default"] = namespace.getDefaultNamespace());
                        }
                        if (!(Object.keys(prefixes).length == 0)) {/* put */
                            (bundle["prefix"] = prefixes);
                            console.log("luc3");
                            console.log(prefixes)
                        }
                        for (let index121 = 0; index121 < records.length; index121++) {
                            let o = records[index121];
                            console.log("luc4 ")
                            console.log(o)
                            {
                                if (o == null)
                                    continue;
                                const record = o;
                                const type = record.type;
                                let structure = ((m, k) => m[k] === undefined ? null : m[k])(bundle, type);
                                if (structure == null) {
                                    structure = ({});
                                    /* put */ (bundle[type] = structure);
                                }
                                const hash = ({});
                                const tuples = record.attributes;
                                console.log("luc44")
                                console.log(tuples)
                                for (let index122 = 0; index122 < tuples.length; index122++) {
                                    let tuple = tuples[index122];
                                    {
                                        const attribute = tuple[0];
                                        const value = tuple[1];
                                        if ( /* containsKey */((m, k) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                return true;
                                            } return false; })(hash, attribute)) {
                                            const existing = ((m, k) => { if (m.entries == null)
                                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                    return m.entries[i].value;
                                                } return null; })(hash, attribute);
                                            if (existing != null && (existing instanceof Array)) {
                                                const values = existing;
                                                /* add */ (values.push(value) > 0);
                                            }
                                            else {
                                                const values = ([]);
                                                /* add */ (values.push(existing) > 0);
                                                /* add */ (values.push(value) > 0);
                                                /* put */ ((m, k, v) => { if (m.entries == null)
                                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                        m.entries[i].value = v;
                                                        return;
                                                    } console.log("foo4 " + k); m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(hash, attribute, values);
                                            }
                                        }
                                        else {
                                            /* put */ ((m, k, v) => { if (m.entries == null)
                                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                    m.entries[i].value = v;
                                                    return;
                                                } console.log("foo3 " + k); m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(hash, attribute, value);
                                        }
                                    }
                                }
                                console.log("luc5")
                                console.log(record.id)
                                console.log(structure)

                                if ( /* containsKey */((m, k) => { if (m.entries == null)
                                    m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                    if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                        return true;
                                    } return false; })(structure, record.id)) {
                                    console.log("Luc 2")
                                    const existing = ((m, k) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            return m.entries[i].value;
                                        } return null; })(structure, record.id);
                                    if (existing != null && (existing instanceof Array)) {
                                        const values = existing;
                                        /* add */ (values.push(hash) > 0);
                                    }
                                    else {
                                        const values = ([]);
                                        /* add */ (values.push(existing) > 0);
                                        /* add */ (values.push(hash) > 0);
                                        /* put */ ((m, k, v) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                m.entries[i].value = v;
                                                return;
                                            } console.log("foo1 " + k);m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(structure, record.id, values);
                                    }
                                }
                                else /* put */ {
                                    ((m, k, v) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            m.entries[i].value = v;
                                            return;
                                        } console.log("foo2 " + k); m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(structure, record.id, hash);
                                    console.log(structure.entries)
                                }
                                console.log("luc10")
                                console.log(structure.entries)

                            }
                        }
                        console.log(bundle)
                        return bundle;
                    }
                    getJSONStructure(records, namespace) {
                        if (((records != null && (records instanceof Array)) || records === null) && ((namespace != null && namespace instanceof org.openprovenance.prov.model.Namespace) || namespace === null)) {
                            return this.getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(records, namespace);
                        }
                        else if (records === undefined && namespace === undefined) {
                            return this.getJSONStructure$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static countMap_$LI$() { if (JSONConstructor.countMap == null) {
                        JSONConstructor.countMap = ({});
                    } return JSONConstructor.countMap; }
                    static getBlankID(type) {
                        if (!JSONConstructor.countMap_$LI$().hasOwnProperty(type)) {
                            /* put */ (JSONConstructor.countMap_$LI$()[type] = 0);
                        }
                        let count = ((m, k) => m[k] === undefined ? null : m[k])(JSONConstructor.countMap_$LI$(), type);
                        count += 1;
                        /* put */ (JSONConstructor.countMap_$LI$()[type] = count);
                        return "_:" + type + count;
                    }
                    tuple(o1, o2) {
                        const tuple = [o1, o2];
                        return tuple;
                    }
                    static qnU_$LI$() { if (JSONConstructor.qnU == null) {
                        JSONConstructor.qnU = new org.openprovenance.prov.model.QualifiedNameUtils();
                    } return JSONConstructor.qnU; }
                    qualifiedNameToString(namespace, id) {
                        let tmp;
                        const prefix = id.getPrefix();
                        const local = id.getLocalPart();
                        if (prefix == null) {
                            tmp = new javax.xml.namespace.QName(id.getNamespaceURI(), JSONConstructor.qnU_$LI$().unescapeProvLocalName(local));
                        }
                        else {
                            tmp = new javax.xml.namespace.QName(id.getNamespaceURI(), JSONConstructor.qnU_$LI$().unescapeProvLocalName(local), prefix);
                        }
                        return namespace.qualifiedNameToString$javax_xml_namespace_QName(tmp);
                    }
                    typedLiteral(value, datatype, lang) {
                        if (datatype === "xsd:string" && lang == null)
                            return value;
                        if (datatype === "xsd:double")
                            return /* parseDouble */ parseFloat(value);
                        if (datatype === "xsd:int")
                            return /* parseInt */ parseInt(value);
                        if (datatype === "xsd:boolean")
                            return javaemul.internal.BooleanHelper.parseBoolean(value);
                        const result = ({});
                        /* put */ (result["$"] = value);
                        if (datatype != null) {
                            /* put */ (result["type"] = datatype);
                        }
                        if (lang != null) {
                            /* put */ (result["lang"] = lang);
                        }
                        return result;
                    }
                    convertValueToString(value, convertedValue) {
                        if (typeof convertedValue === 'string') {
                            return convertedValue;
                        }
                        if (convertedValue != null && (convertedValue.constructor != null && convertedValue.constructor["__interfaces"] != null && convertedValue.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0))
                            return this.qualifiedNameToString(this.currentNamespace, convertedValue);
                        if (convertedValue != null && (convertedValue.constructor != null && convertedValue.constructor["__interfaces"] != null && convertedValue.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                            const iStr = convertedValue;
                            return iStr.getValue();
                        }
                        if (convertedValue != null && convertedValue instanceof Array && (convertedValue.length == 0 || convertedValue[0] == null || typeof convertedValue[0] === 'number')) {
                            return value;
                        }
                        return value;
                    }
                    convertTypedValue(value, type) {
                        const datatype = this.qualifiedNameToString(this.currentNamespace, type);
                        let result;
                        if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) {
                            result = this.typedLiteral(this.qualifiedNameToString(this.currentNamespace, value), datatype, null);
                        }
                        else if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                            const iStr = value;
                            const lang = iStr.getLang();
                            if (lang != null) {
                                result = this.typedLiteral(iStr.getValue(), null, lang);
                            }
                            else {
                                result = iStr.getValue();
                            }
                        }
                        else {
                            result = this.typedLiteral(value.toString(), datatype, null);
                        }
                        return result;
                    }
                    convertAttribute(attr) {
                        const attrName = this.qualifiedNameToString(this.currentNamespace, attr.getElementName());
                        const value = attr.getValue();
                        const type = attr.getType();
                        const attrValue = this.convertTypedValue(value, type);
                        return this.tuple(attrName, attrValue);
                    }
                    convertAttributes(attrs) {
                        const result = ([]);
                        if (attrs != null)
                            for (let index123 = 0; index123 < attrs.length; index123++) {
                                let attr = attrs[index123];
                                {
                                    /* add */ (result.push(this.convertAttribute(attr)) > 0);
                                }
                            }
                        return result;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newEntity(id, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        const record = new JSONConstructor.JsonProvRecord(this, "entity", this.qualifiedNameToString(this.currentNamespace, id), attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {javax.xml.datatype.XMLGregorianCalendar} startTime
                     * @param {javax.xml.datatype.XMLGregorianCalendar} endTime
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newActivity(id, startTime, endTime, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (startTime != null) {
                            /* add */ (attrs.push(this.tuple("prov:startTime", startTime.toXMLFormat())) > 0);
                        }
                        if (endTime != null) {
                            /* add */ (attrs.push(this.tuple("prov:endTime", endTime.toXMLFormat())) > 0);
                        }
                        const record = new JSONConstructor.JsonProvRecord(this, "activity", this.qualifiedNameToString(this.currentNamespace, id), attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newAgent(id, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        const record = new JSONConstructor.JsonProvRecord(this, "agent", this.qualifiedNameToString(this.currentNamespace, id), attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} activity
                     * @param {*} entity
                     * @param {javax.xml.datatype.XMLGregorianCalendar} time
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newUsed(id, activity, entity, time, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (activity != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity))) > 0);
                        if (entity != null) /* add */
                            (attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, entity))) > 0);
                        if (time != null) /* add */
                            (attrs.push(this.tuple("prov:time", time.toXMLFormat())) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("u");
                        const record = new JSONConstructor.JsonProvRecord(this, "used", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} entity
                     * @param {*} activity
                     * @param {javax.xml.datatype.XMLGregorianCalendar} time
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasGeneratedBy(id, entity, activity, time, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (entity != null) /* add */
                            (attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, entity))) > 0);
                        if (activity != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity))) > 0);
                        if (time != null) /* add */
                            (attrs.push(this.tuple("prov:time", time.toXMLFormat())) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wGB");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasGeneratedBy", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} entity
                     * @param {*} activity
                     * @param {javax.xml.datatype.XMLGregorianCalendar} time
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasInvalidatedBy(id, entity, activity, time, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (entity != null) /* add */
                            (attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, entity))) > 0);
                        if (activity != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity))) > 0);
                        if (time != null) /* add */
                            (attrs.push(this.tuple("prov:time", time.toXMLFormat())) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wIB");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasInvalidatedBy", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} activity
                     * @param {*} trigger
                     * @param {*} starter
                     * @param {javax.xml.datatype.XMLGregorianCalendar} time
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasStartedBy(id, activity, trigger, starter, time, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (activity != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity))) > 0);
                        if (trigger != null) /* add */
                            (attrs.push(this.tuple("prov:trigger", this.qualifiedNameToString(this.currentNamespace, trigger))) > 0);
                        if (starter != null) /* add */
                            (attrs.push(this.tuple("prov:starter", this.qualifiedNameToString(this.currentNamespace, starter))) > 0);
                        if (time != null) /* add */
                            (attrs.push(this.tuple("prov:time", time.toXMLFormat())) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wSB");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasStartedBy", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} activity
                     * @param {*} trigger
                     * @param {*} ender
                     * @param {javax.xml.datatype.XMLGregorianCalendar} time
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasEndedBy(id, activity, trigger, ender, time, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (activity != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity))) > 0);
                        if (trigger != null) /* add */
                            (attrs.push(this.tuple("prov:trigger", this.qualifiedNameToString(this.currentNamespace, trigger))) > 0);
                        if (ender != null) /* add */
                            (attrs.push(this.tuple("prov:ender", this.qualifiedNameToString(this.currentNamespace, ender))) > 0);
                        if (time != null) /* add */
                            (attrs.push(this.tuple("prov:time", time.toXMLFormat())) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wEB");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasEndedBy", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} e2
                     * @param {*} e1
                     * @param {*} activity
                     * @param {*} generation
                     * @param {*} usage
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasDerivedFrom(id, e2, e1, activity, generation, usage, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (e2 != null) /* add */
                            (attrs.push(this.tuple("prov:generatedEntity", this.qualifiedNameToString(this.currentNamespace, e2))) > 0);
                        if (e1 != null) /* add */
                            (attrs.push(this.tuple("prov:usedEntity", this.qualifiedNameToString(this.currentNamespace, e1))) > 0);
                        if (activity != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity))) > 0);
                        if (generation != null) /* add */
                            (attrs.push(this.tuple("prov:generation", this.qualifiedNameToString(this.currentNamespace, generation))) > 0);
                        if (usage != null) /* add */
                            (attrs.push(this.tuple("prov:usage", this.qualifiedNameToString(this.currentNamespace, usage))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wDF");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasDerivedFrom", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} a
                     * @param {*} ag
                     * @param {*} plan
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasAssociatedWith(id, a, ag, plan, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (a != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, a))) > 0);
                        if (ag != null) /* add */
                            (attrs.push(this.tuple("prov:agent", this.qualifiedNameToString(this.currentNamespace, ag))) > 0);
                        if (plan != null) /* add */
                            (attrs.push(this.tuple("prov:plan", this.qualifiedNameToString(this.currentNamespace, plan))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wAW");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasAssociatedWith", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} e
                     * @param {*} ag
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasAttributedTo(id, e, ag, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (e != null) /* add */
                            (attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, e))) > 0);
                        if (ag != null) /* add */
                            (attrs.push(this.tuple("prov:agent", this.qualifiedNameToString(this.currentNamespace, ag))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wAT");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasAttributedTo", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} ag2
                     * @param {*} ag1
                     * @param {*} a
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newActedOnBehalfOf(id, ag2, ag1, a, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (ag2 != null) /* add */
                            (attrs.push(this.tuple("prov:delegate", this.qualifiedNameToString(this.currentNamespace, ag2))) > 0);
                        if (ag1 != null) /* add */
                            (attrs.push(this.tuple("prov:responsible", this.qualifiedNameToString(this.currentNamespace, ag1))) > 0);
                        if (a != null) /* add */
                            (attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, a))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("aOBO");
                        const record = new JSONConstructor.JsonProvRecord(this, "actedOnBehalfOf", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} a2
                     * @param {*} a1
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasInformedBy(id, a2, a1, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (a2 != null) /* add */
                            (attrs.push(this.tuple("prov:informed", this.qualifiedNameToString(this.currentNamespace, a2))) > 0);
                        if (a1 != null) /* add */
                            (attrs.push(this.tuple("prov:informant", this.qualifiedNameToString(this.currentNamespace, a1))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("Infm");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasInformedBy", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} a2
                     * @param {*} a1
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasInfluencedBy(id, a2, a1, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (a2 != null) /* add */
                            (attrs.push(this.tuple("prov:influencee", this.qualifiedNameToString(this.currentNamespace, a2))) > 0);
                        if (a1 != null) /* add */
                            (attrs.push(this.tuple("prov:influencer", this.qualifiedNameToString(this.currentNamespace, a1))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("Infl");
                        const record = new JSONConstructor.JsonProvRecord(this, "wasInfluencedBy", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} e1
                     * @param {*} e2
                     * @return {*}
                     */
                    newAlternateOf(e1, e2) {
                        const attrs = ([]);
                        if (e2 != null) /* add */
                            (attrs.push(this.tuple("prov:alternate2", this.qualifiedNameToString(this.currentNamespace, e2))) > 0);
                        if (e1 != null) /* add */
                            (attrs.push(this.tuple("prov:alternate1", this.qualifiedNameToString(this.currentNamespace, e1))) > 0);
                        const recordID = JSONConstructor.getBlankID("aO");
                        const record = new JSONConstructor.JsonProvRecord(this, "alternateOf", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} e2
                     * @param {*} e1
                     * @return {*}
                     */
                    newSpecializationOf(e2, e1) {
                        const attrs = ([]);
                        if (e2 != null) /* add */
                            (attrs.push(this.tuple("prov:specificEntity", this.qualifiedNameToString(this.currentNamespace, e2))) > 0);
                        if (e1 != null) /* add */
                            (attrs.push(this.tuple("prov:generalEntity", this.qualifiedNameToString(this.currentNamespace, e1))) > 0);
                        const recordID = JSONConstructor.getBlankID("sO");
                        const record = new JSONConstructor.JsonProvRecord(this, "specializationOf", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} e2
                     * @param {*} e1
                     * @param {*} b
                     * @return {*}
                     */
                    newMentionOf(e2, e1, b) {
                        const attrs = ([]);
                        if (e2 != null) /* add */
                            (attrs.push(this.tuple("prov:specificEntity", this.qualifiedNameToString(this.currentNamespace, e2))) > 0);
                        if (e1 != null) /* add */
                            (attrs.push(this.tuple("prov:generalEntity", this.qualifiedNameToString(this.currentNamespace, e1))) > 0);
                        if (b != null) /* add */
                            (attrs.push(this.tuple("prov:bundle", this.qualifiedNameToString(this.currentNamespace, b))) > 0);
                        const recordID = JSONConstructor.getBlankID("mO");
                        const record = new JSONConstructor.JsonProvRecord(this, "mentionOf", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} c
                     * @param {*[]} e
                     * @return {*}
                     */
                    newHadMember(c, e) {
                        const attrs = ([]);
                        if (c != null) /* add */
                            (attrs.push(this.tuple("prov:collection", this.qualifiedNameToString(this.currentNamespace, c))) > 0);
                        if (e != null && !(e.length == 0)) {
                            const entityList = ([]);
                            for (let index124 = 0; index124 < e.length; index124++) {
                                let entity = e[index124];
                                /* add */ (entityList.push(this.qualifiedNameToString(this.currentNamespace, entity)) > 0);
                            }
                            /* add */ (attrs.push(this.tuple("prov:entity", entityList)) > 0);
                        }
                        const recordID = JSONConstructor.getBlankID("hM");
                        const record = new JSONConstructor.JsonProvRecord(this, "hadMember", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespaces
                     * @param {*[]} statements
                     * @param {*[]} bundles
                     * @return {*}
                     */
                    newDocument(namespaces, statements, bundles) {
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {org.openprovenance.prov.model.Namespace} namespaces
                     * @param {*[]} statements
                     * @return {*}
                     */
                    newNamedBundle(id, namespaces, statements) {
                        const bundle = this.getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(this.currentRecords, this.currentNamespace);
                        /* put */ (this.documentBundles[this.qualifiedNameToString(this.currentNamespace, id)] = bundle);
                        this.currentRecords = this.documentRecords;
                        this.currentNamespace = this.documentNamespace;
                        return null;
                    }
                    /**
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespace
                     */
                    startDocument(namespace) {
                        this.documentNamespace = namespace;
                        this.currentNamespace = this.documentNamespace;
                    }
                    /**
                     *
                     * @param {*} bundleId
                     * @param {org.openprovenance.prov.model.Namespace} namespaces
                     */
                    startBundle(bundleId, namespaces) {
                        this.currentNamespace = namespaces;
                        this.currentRecords = ([]);
                    }
                    encodeKeyEntitySet(keyEntitySet) {
                        let isAllKeyOfSameDatatype = true;
                        const firstKey = keyEntitySet[0].getKey();
                        const firstKeyClass = firstKey.getType();
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(this.name.PROV_LANG_STRING, firstKeyClass)) {
                            isAllKeyOfSameDatatype = false;
                        }
                        if (isAllKeyOfSameDatatype) {
                            for (let index125 = 0; index125 < keyEntitySet.length; index125++) {
                                let pair = keyEntitySet[index125];
                                {
                                    const keyClass = pair.getKey().getType();
                                    if (keyClass !== firstKeyClass) {
                                        isAllKeyOfSameDatatype = false;
                                        break;
                                    }
                                }
                            }
                        }
                        if (isAllKeyOfSameDatatype) {
                            const dictionary = ({});
                            const keyDatatype = this.qualifiedNameToString(this.currentNamespace, /* get */ keyEntitySet[0].getKey().getType());
                            /* put */ ((m, k, v) => { if (m.entries == null)
                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                    m.entries[i].value = v;
                                    return;
                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(dictionary, "$key-datatype", keyDatatype);
                            for (let index126 = 0; index126 < keyEntitySet.length; index126++) {
                                let pair = keyEntitySet[index126];
                                {
                                    const key = this.convertValueToString(pair.getKey().getValue(), pair.getKey().getConvertedValue());
                                    const entity = this.qualifiedNameToString(this.currentNamespace, pair.getEntity());
                                    /* put */ ((m, k, v) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            m.entries[i].value = v;
                                            return;
                                        } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(dictionary, key, entity);
                                }
                            }
                            return dictionary;
                        }
                        const values = ([]);
                        for (let index127 = 0; index127 < keyEntitySet.length; index127++) {
                            let pair = keyEntitySet[index127];
                            {
                                const entity = this.qualifiedNameToString(this.currentNamespace, pair.getEntity());
                                const item = ({});
                                /* put */ (item["$"] = entity);
                                const key = pair.getKey();
                                /* put */ (item["key"] = this.convertTypedValue(key.getValue(), key.getType()));
                                /* add */ (values.push(item) > 0);
                            }
                        }
                        return values;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} after
                     * @param {*} before
                     * @param {*[]} keyEntitySet
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newDerivedByInsertionFrom(id, after, before, keyEntitySet, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (after != null) /* add */
                            (attrs.push(this.tuple("prov:after", this.qualifiedNameToString(this.currentNamespace, after))) > 0);
                        if (before != null) /* add */
                            (attrs.push(this.tuple("prov:before", this.qualifiedNameToString(this.currentNamespace, before))) > 0);
                        if (keyEntitySet != null && !(keyEntitySet.length == 0)) {
                            /* add */ (attrs.push(this.tuple("prov:key-entity-set", this.encodeKeyEntitySet(keyEntitySet))) > 0);
                        }
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("dBIF");
                        const record = new JSONConstructor.JsonProvRecord(this, "derivedByInsertionFrom", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} after
                     * @param {*} before
                     * @param {*[]} keys
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newDerivedByRemovalFrom(id, after, before, keys, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (after != null) /* add */
                            (attrs.push(this.tuple("prov:after", this.qualifiedNameToString(this.currentNamespace, after))) > 0);
                        if (before != null) /* add */
                            (attrs.push(this.tuple("prov:before", this.qualifiedNameToString(this.currentNamespace, before))) > 0);
                        if (keys != null && !(keys.length == 0)) {
                            const values = ([]);
                            for (let index128 = 0; index128 < keys.length; index128++) {
                                let key = keys[index128];
                                {
                                    /* add */ (values.push(this.convertTypedValue(key.getValue(), key.getType())) > 0);
                                }
                            }
                            /* add */ (attrs.push(this.tuple("prov:key-set", values)) > 0);
                        }
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("dBRF");
                        const record = new JSONConstructor.JsonProvRecord(this, "derivedByRemovalFrom", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} dict
                     * @param {*[]} keyEntitySet
                     * @return {*}
                     */
                    newDictionaryMembership(dict, keyEntitySet) {
                        const attrs = ([]);
                        if (dict != null) /* add */
                            (attrs.push(this.tuple("prov:dictionary", this.qualifiedNameToString(this.currentNamespace, dict))) > 0);
                        if (keyEntitySet != null && !(keyEntitySet.length == 0)) {
                            /* add */ (attrs.push(this.tuple("prov:key-entity-set", this.encodeKeyEntitySet(keyEntitySet))) > 0);
                        }
                        const recordID = JSONConstructor.getBlankID("hDM");
                        const record = new JSONConstructor.JsonProvRecord(this, "hadDictionaryMember", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix) {
                        return null;
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag) {
                        return null;
                    }
                    /**
                     *
                     * @param {string} namespace
                     * @param {string} local
                     * @param {string} prefix
                     * @param {org.openprovenance.prov.model.ProvUtilities.BuildFlag} flag
                     * @return {*}
                     */
                    newQualifiedName(namespace, local, prefix, flag) {
                        if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof flag === 'number') || flag === null)) {
                            return this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag);
                        }
                        else if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && flag === undefined) {
                            return this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} e2
                     * @param {*} e1
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newQualifiedAlternateOf(id, e2, e1, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (e2 != null) /* add */
                            (attrs.push(this.tuple("prov:alternate2", this.qualifiedNameToString(this.currentNamespace, e2))) > 0);
                        if (e1 != null) /* add */
                            (attrs.push(this.tuple("prov:alternate1", this.qualifiedNameToString(this.currentNamespace, e1))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("a0");
                        const record = new JSONConstructor.JsonProvRecord(this, "provext:alternateOf", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} e2
                     * @param {*} e1
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newQualifiedSpecializationOf(id, e2, e1, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (e2 != null) /* add */
                            (attrs.push(this.tuple("prov:specificEntity", this.qualifiedNameToString(this.currentNamespace, e2))) > 0);
                        if (e1 != null) /* add */
                            (attrs.push(this.tuple("prov:generalEntity", this.qualifiedNameToString(this.currentNamespace, e1))) > 0);
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("s0");
                        const record = new JSONConstructor.JsonProvRecord(this, "provext:specializationOf", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} c
                     * @param {*[]} e
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newQualifiedHadMember(id, c, e, attributes) {
                        const attrs = this.convertAttributes(attributes);
                        if (c != null) /* add */
                            (attrs.push(this.tuple("prov:collection", this.qualifiedNameToString(this.currentNamespace, c))) > 0);
                        if (e != null && !(e.length == 0)) {
                            const entityList = ([]);
                            for (let index129 = 0; index129 < e.length; index129++) {
                                let entity = e[index129];
                                /* add */ (entityList.push(this.qualifiedNameToString(this.currentNamespace, entity)) > 0);
                            }
                            /* add */ (attrs.push(this.tuple("prov:entity", entityList)) > 0);
                        }
                        const recordID = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("hM");
                        const record = new JSONConstructor.JsonProvRecord(this, "provext:hadMember", recordID, attrs);
                        /* add */ (this.currentRecords.push(record) > 0);
                        return null;
                    }
                }
                json.JSONConstructor = JSONConstructor;
                JSONConstructor["__class"] = "org.openprovenance.prov.json.JSONConstructor";
                JSONConstructor["__interfaces"] = ["org.openprovenance.prov.model.ModelConstructorExtension", "org.openprovenance.prov.model.ModelConstructor"];
                (function (JSONConstructor) {
                    class JsonProvRecord {
                        constructor(__parent, type, id, attributes) {
                            this.__parent = __parent;
                            if (this.type === undefined) {
                                this.type = null;
                            }
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            if (this.attributes === undefined) {
                                this.attributes = null;
                            }
                            this.type = type;
                            this.id = id;
                            this.attributes = attributes;
                        }
                    }
                    JSONConstructor.JsonProvRecord = JsonProvRecord;
                    JsonProvRecord["__class"] = "org.openprovenance.prov.json.JSONConstructor.JsonProvRecord";
                })(JSONConstructor = json.JSONConstructor || (json.JSONConstructor = {}));
            })(json = prov.json || (prov.json = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
