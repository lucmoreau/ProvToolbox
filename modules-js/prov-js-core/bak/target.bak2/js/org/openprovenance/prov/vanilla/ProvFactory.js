/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class ProvFactory extends org.openprovenance.prov.model.ProvFactory {
                    constructor(of, mc) {
                        if (((of != null && (of.constructor != null && of.constructor["__interfaces"] != null && of.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ObjectFactory") >= 0)) || of === null) && ((mc != null && (mc.constructor != null && mc.constructor["__interfaces"] != null && mc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ModelConstructor") >= 0)) || mc === null)) {
                            let __args = arguments;
                            super(of);
                            if (this.mc === undefined) {
                                this.mc = null;
                            }
                            if (this.ac === undefined) {
                                this.ac = null;
                            }
                            this.mc = mc;
                            this.ac = mc;
                        }
                        else if (((of != null && (of.constructor != null && of.constructor["__interfaces"] != null && of.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ObjectFactory") >= 0)) || of === null) && mc === undefined) {
                            let __args = arguments;
                            super(of);
                            if (this.mc === undefined) {
                                this.mc = null;
                            }
                            if (this.ac === undefined) {
                                this.ac = null;
                            }
                            this.mc = new org.openprovenance.prov.vanilla.ModelConstructor();
                            this.ac = this.mc;
                        }
                        else if (of === undefined && mc === undefined) {
                            let __args = arguments;
                            super(null);
                            if (this.mc === undefined) {
                                this.mc = null;
                            }
                            if (this.ac === undefined) {
                                this.ac = null;
                            }
                            this.mc = new org.openprovenance.prov.vanilla.ModelConstructor();
                            this.ac = this.mc;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static logger_$LI$() { if (ProvFactory.logger == null) {
                        ProvFactory.logger = org.apache.log4j.Logger.getLogger(ProvFactory);
                    } return ProvFactory.logger; }
                    static oFactory_$LI$() { if (ProvFactory.oFactory == null) {
                        ProvFactory.oFactory = new ProvFactory();
                    } return ProvFactory.oFactory; }
                    static getFactory() {
                        return ProvFactory.oFactory_$LI$();
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getSerializer() {
                        return null;
                    }
                    newAttribute(namespace, localName, prefix, value, type) {
                        if (((typeof namespace === 'string') || namespace === null) && ((typeof localName === 'string') || localName === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                            return super.newAttribute(namespace, localName, prefix, value, type);
                        }
                        else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((localName != null) || localName === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                            return this.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix);
                        }
                        else if (((typeof namespace === 'number') || namespace === null) && ((localName != null) || localName === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                            return this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type) {
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(elementName, this.getName().PROV_LOCATION)) {
                            return this.newLocation(value, type);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(elementName, this.getName().PROV_TYPE)) {
                            return this.newType(value, type);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(elementName, this.getName().PROV_VALUE)) {
                            return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(elementName, this.getName().PROV_ROLE)) {
                            return this.newRole(value, type);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(elementName, this.getName().PROV_LABEL)) {
                            return this.newLabel(value, type);
                        }
                        if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                            return o1.equals(o2);
                        }
                        else {
                            return o1 === o2;
                        } })(elementName, this.getName().PROV_KEY)) {
                            Object.defineProperty(new Error("key not there yet"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                        return this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type);
                    }
                    newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(kind, value, type) {
                        switch ((kind)) {
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                                return this.newType(value, type);
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                                return this.newLabel(value, type);
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                                return this.newRole(value, type);
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                                return this.newLocation(value, type);
                            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                                return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type);
                        }
                        throw Object.defineProperty(new Error("Should never be here, unknown " + kind), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix) {
                        return this.mc['newQualifiedName$java_lang_String$java_lang_String$java_lang_String'](namespace, local, prefix);
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag) {
                        return this.mc['newQualifiedName$java_lang_String$java_lang_String$java_lang_String'](namespace, local, prefix);
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
                        else if (((namespace != null && namespace instanceof javax.xml.namespace.QName) || namespace === null) && local === undefined && prefix === undefined && flag === undefined) {
                            return this.newQualifiedName$javax_xml_namespace_QName(namespace);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newInternationalizedString$java_lang_String$java_lang_String(s, lang) {
                        const res = this.ac['newInternationalizedString$java_lang_String$java_lang_String'](s, lang);
                        return res;
                    }
                    /**
                     *
                     * @param {string} s
                     * @param {string} lang
                     * @return {*}
                     */
                    newInternationalizedString(s, lang) {
                        if (((typeof s === 'string') || s === null) && ((typeof lang === 'string') || lang === null)) {
                            return this.newInternationalizedString$java_lang_String$java_lang_String(s, lang);
                        }
                        else if (((typeof s === 'string') || s === null) && lang === undefined) {
                            return this.newInternationalizedString$java_lang_String(s);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newInternationalizedString$java_lang_String(s) {
                        const res = this.ac['newInternationalizedString$java_lang_String'](s);
                        return res;
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newType(value, type) {
                        if (value == null)
                            return null;
                        return this.ac.newType(value, type);
                    }
                    newOther(namespace, local, prefix, value, type) {
                        if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                            return super.newOther(namespace, local, prefix, value, type);
                        }
                        else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((local != null) || local === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                            return this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type) {
                        return this.ac.newOther(elementName, value, type);
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newLocation(value, type) {
                        if (value == null)
                            return null;
                        const res = this.ac.newLocation(value, type);
                        return res;
                    }
                    newActivity(id, startTime, endTime, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((startTime != null && startTime instanceof javax.xml.datatype.XMLGregorianCalendar) || startTime === null) && ((endTime != null && endTime instanceof javax.xml.datatype.XMLGregorianCalendar) || endTime === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return super.newActivity(id, startTime, endTime, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof startTime === 'string') || startTime === null) && endTime === undefined && attributes === undefined) {
                            return this.newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, startTime);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                            return this.newActivity$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                            return this.newActivity$org_openprovenance_prov_model_Activity(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newActivity$org_openprovenance_prov_model_QualifiedName(a) {
                        const res = this.mc.newActivity(a, null, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(q, label) {
                        const attr = this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL, this.newInternationalizedString$java_lang_String(label), this.getName().PROV_LANG_STRING);
                        const attributes = ([]);
                        /* add */ (attributes.push(attr) > 0);
                        const res = this.newActivity$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(q, null, null, attributes);
                        return res;
                    }
                    newEntity$org_openprovenance_prov_model_QualifiedName(a) {
                        const res = this.mc.newEntity(a, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, label) {
                        const attrs = ([]);
                        /* add */ (attrs.push(this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL, this.newInternationalizedString$java_lang_String(label), this.getName().XSD_STRING)) > 0);
                        const res = this.mc.newEntity(id, attrs);
                        return res;
                    }
                    /**
                     * Creates a new {@link org.openprovenance.prov.model.Entity} with provided identifier and label
                     * @param {*} id a {@link org.openprovenance.prov.model.QualifiedName} for the entity
                     * @param {string} label a String for the label property (see {@link HasLabel#getLabel()}
                     * @return {*} an object of type {@link org.openprovenance.prov.model.Entity}
                     */
                    newEntity(id, label) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof label === 'string') || label === null)) {
                            return this.newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, label);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((label != null && (label instanceof Array)) || label === null)) {
                            return this.newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, label);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && label === undefined) {
                            return this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || id === null) && label === undefined) {
                            return this.newEntity$org_openprovenance_prov_model_Entity(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes) {
                        const res = this.mc.newEntity(id, attributes);
                        return res;
                    }
                    newDocument$() {
                        const res = this.mc.newDocument(null, null, null);
                        return res;
                    }
                    /**
                     *
                     * @param {*} o
                     * @param {*} type
                     * @return {*}
                     */
                    newKey(o, type) {
                        throw Object.defineProperty(new Error("newKey not supported"), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                    }
                    newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type) {
                        if (value == null)
                            return null;
                        return this.ac.newValue(value, type);
                    }
                    newValue(value, type) {
                        if (((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                            return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type);
                        }
                        else if (((typeof value === 'string') || value === null) && type === undefined) {
                            return this.newValue$java_lang_String(value);
                        }
                        else if (((typeof value === 'number') || value === null) && type === undefined) {
                            return this.newValue$int(value);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newAgent$org_openprovenance_prov_model_QualifiedName(ag) {
                        const res = this.mc.newAgent(ag, ([]));
                        return res;
                    }
                    newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes) {
                        const res = this.mc.newAgent(id, attributes);
                        return res;
                    }
                    /**
                     * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier and attributes
                     * @param {*} id a {@link org.openprovenance.prov.model.QualifiedName} for the agent
                     * @param {*[]} attributes a collection of {@link Attribute} for the agent
                     * @return {*} an object of type {@link org.openprovenance.prov.model.Agent}
                     */
                    newAgent(id, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof attributes === 'string') || attributes === null)) {
                            return this.newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && attributes === undefined) {
                            return this.newAgent$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || id === null) && attributes === undefined) {
                            return this.newAgent$org_openprovenance_prov_model_Agent(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(ag, label) {
                        const res = this.newAgent$org_openprovenance_prov_model_QualifiedName(ag);
                        if (label != null) /* add */
                            (res.getLabel().push(this.newInternationalizedString$java_lang_String(label)) > 0);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName(id) {
                        const res = this.mc.newUsed(id, null, null, null, ([]));
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, aid, role, eid) {
                        const attributes = ([]);
                        if (role != null) /* add */
                            (attributes.push(this.newRole(role, this.getName().XSD_STRING)) > 0);
                        const res = this.mc.newUsed(id, aid, eid, null, attributes);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, entity) {
                        const res = this.mc.newUsed(id, activity, entity, null, ([]));
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(activity, entity) {
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(null, activity, entity);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, entity, time, attributes) {
                        const res = this.mc.newUsed(id, activity, entity, time, attributes);
                        return res;
                    }
                    newUsed(id, activity, entity, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, entity, time, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((typeof entity === 'string') || entity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                            return this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, activity, entity, time);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && attributes === undefined) {
                            return this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar(id, activity, entity, time);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && time === undefined && attributes === undefined) {
                            return this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, entity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && entity === undefined && time === undefined && attributes === undefined) {
                            return this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && activity === undefined && entity === undefined && time === undefined && attributes === undefined) {
                            return this.newUsed$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || id === null) && activity === undefined && entity === undefined && time === undefined && attributes === undefined) {
                            return this.newUsed$org_openprovenance_prov_model_Used(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar(id, activity, entity, time) {
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, entity, time, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    /**
                     *
                     * @param {*} value
                     * @param {*} type
                     * @return {*}
                     */
                    newRole(value, type) {
                        if (value == null)
                            return null;
                        return this.ac.newRole(value, type);
                    }
                    newLabel(value, type) {
                        if (value == null)
                            return null;
                        return this.ac.newLabel(value, type);
                    }
                    newWasGeneratedBy(id, entity, activity, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return super.newWasGeneratedBy(id, entity, activity, time, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, activity, time);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || time === null) && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, entity, activity, time);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || id === null) && ((typeof entity === 'string') || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, entity, activity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, eid, role, aid) {
                        const attributes = ([]);
                        if (role != null) /* add */
                            (attributes.push(this.newRole(role, this.getName().XSD_STRING)) > 0);
                        const res = this.mc.newWasGeneratedBy(id, eid, aid, null, attributes);
                        return res;
                    }
                    newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, eid, role, aid) {
                        const attributes = ([]);
                        if (role != null) /* add */
                            (attributes.push(this.newRole(role, this.getName().XSD_STRING)) > 0);
                        const res = this.mc.newWasInvalidatedBy(id, eid, aid, null, attributes);
                        return res;
                    }
                    newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity) {
                        const res = this.mc.newWasInvalidatedBy(id, entity, activity, null, ([]));
                        return res;
                    }
                    newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes) {
                        const res = this.mc.newWasInvalidatedBy(id, entity, activity, time, attributes);
                        res.setTime(time);
                        return res;
                    }
                    newWasInvalidatedBy(id, entity, activity, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                            return this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, activity, time);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                            return this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && activity === undefined && time === undefined && attributes === undefined) {
                            return this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                            return this.newWasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, agent) {
                        const res = this.mc.newWasAssociatedWith(id, activity, agent, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a, ag, plan, attributes) {
                        const res = this.mc.newWasAssociatedWith(id, a, ag, plan, attributes);
                        return res;
                    }
                    newWasAssociatedWith(id, a, ag, plan, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a, ag, plan, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && attributes === undefined) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag, plan);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && plan === undefined && attributes === undefined) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag, plan) {
                        const res = this.mc.newWasAssociatedWith(id, a, ag, plan, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(u) {
                        const u1 = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getAgent(), u.getPlan());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        return u1;
                    }
                    newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent) {
                        const res = this.mc.newWasAttributedTo(id, entity, agent, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity, agent) {
                        const res = this.mc.newWasAttributedTo(null, entity, agent, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, entity, agent, attributes) {
                        const res = this.mc.newWasAttributedTo(id, entity, agent, attributes);
                        return res;
                    }
                    newWasAttributedTo(id, entity, agent, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, entity, agent, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && attributes === undefined) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && agent === undefined && attributes === undefined) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || id === null) && entity === undefined && agent === undefined && attributes === undefined) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific, general) {
                        const res = this.mc.newSpecializationOf(specific, general);
                        return res;
                    }
                    /**
                     *
                     * @param {*} specific
                     * @param {*} general
                     * @return {*}
                     */
                    newSpecializationOf(specific, general) {
                        if (((specific != null && (specific.constructor != null && specific.constructor["__interfaces"] != null && specific.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || specific === null) && ((general != null && (general.constructor != null && general.constructor["__interfaces"] != null && general.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || general === null)) {
                            return this.newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific, general);
                        }
                        else if (((specific != null && (specific.constructor != null && specific.constructor["__interfaces"] != null && specific.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || specific === null) && general === undefined) {
                            return this.newSpecializationOf$org_openprovenance_prov_model_SpecializationOf(specific);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1, entity2) {
                        const res = this.mc.newAlternateOf(entity1, entity2);
                        return res;
                    }
                    /**
                     * A factory method to create an instance of an alternate {@link org.openprovenance.prov.model.AlternateOf}
                     * @param {*} entity1 an identifier for the first {@link org.openprovenance.prov.model.Entity}
                     * @param {*} entity2 an identifier for the second {@link org.openprovenance.prov.model.Entity}
                     * @return {*} an instance of {@link org.openprovenance.prov.model.AlternateOf}
                     */
                    newAlternateOf(entity1, entity2) {
                        if (((entity1 != null && (entity1.constructor != null && entity1.constructor["__interfaces"] != null && entity1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity1 === null) && ((entity2 != null && (entity2.constructor != null && entity2.constructor["__interfaces"] != null && entity2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity2 === null)) {
                            return this.newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1, entity2);
                        }
                        else if (((entity1 != null && (entity1.constructor != null && entity1.constructor["__interfaces"] != null && entity1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || entity1 === null) && entity2 === undefined) {
                            return this.newAlternateOf$org_openprovenance_prov_model_AlternateOf(entity1);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, e2, e1) {
                        const res = this.mc.newWasDerivedFrom(id, e2, e1, null, null, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(e2, e1) {
                        const res = this.mc.newWasDerivedFrom(null, e2, e1, null, null, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, e2, e1, a, gen, use, attributes) {
                        const res = this.mc.newWasDerivedFrom(id, e2, e1, a, gen, use, attributes);
                        return res;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} e2
                     * @param {*} e1
                     * @param {*} a
                     * @param {*} gen
                     * @param {*} use
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newWasDerivedFrom(id, e2, e1, a, gen, use, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((e2 != null && (e2.constructor != null && e2.constructor["__interfaces"] != null && e2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e2 === null) && ((e1 != null && (e1.constructor != null && e1.constructor["__interfaces"] != null && e1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e1 === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((gen != null && (gen.constructor != null && gen.constructor["__interfaces"] != null && gen.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || gen === null) && ((use != null && (use.constructor != null && use.constructor["__interfaces"] != null && use.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || use === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, e2, e1, a, gen, use, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((e2 != null && (e2.constructor != null && e2.constructor["__interfaces"] != null && e2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e2 === null) && ((e1 != null && (e1.constructor != null && e1.constructor["__interfaces"] != null && e1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e1 === null) && a === undefined && gen === undefined && use === undefined && attributes === undefined) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, e2, e1);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((e2 != null && (e2.constructor != null && e2.constructor["__interfaces"] != null && e2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e2 === null) && e1 === undefined && a === undefined && gen === undefined && use === undefined && attributes === undefined) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, e2);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || id === null) && e2 === undefined && e1 === undefined && a === undefined && gen === undefined && use === undefined && attributes === undefined) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps, as, ags, lks) {
                        const res = this.newDocument$();
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ps);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), as);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ags);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), lks);
                        return res;
                    }
                    newDocument(ps, as, ags, lks) {
                        if (((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                            return this.newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps, as, ags, lks);
                        }
                        else if (((ps != null && ps instanceof Array && (ps.length == 0 || ps[0] == null || (ps[0] != null && (ps[0].constructor != null && ps[0].constructor["__interfaces"] != null && ps[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)))) || ps === null) && ((as != null && as instanceof Array && (as.length == 0 || as[0] == null || (as[0] != null && (as[0].constructor != null && as[0].constructor["__interfaces"] != null && as[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)))) || as === null) && ((ags != null && ags instanceof Array && (ags.length == 0 || ags[0] == null || (ags[0] != null && (ags[0].constructor != null && ags[0].constructor["__interfaces"] != null && ags[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)))) || ags === null) && ((lks != null && lks instanceof Array && (lks.length == 0 || lks[0] == null || (lks[0] != null && (lks[0].constructor != null && lks[0].constructor["__interfaces"] != null && lks[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Statement") >= 0)))) || lks === null)) {
                            return super.newDocument(ps, as, ags, lks);
                        }
                        else if (((ps != null && ps instanceof org.openprovenance.prov.model.Namespace) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && lks === undefined) {
                            return this.newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(ps, as, ags);
                        }
                        else if (((ps != null && (ps.constructor != null && ps.constructor["__interfaces"] != null && ps.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || ps === null) && as === undefined && ags === undefined && lks === undefined) {
                            return this.newDocument$org_openprovenance_prov_model_Document(ps);
                        }
                        else if (ps === undefined && as === undefined && ags === undefined && lks === undefined) {
                            return this.newDocument$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newDocument$org_openprovenance_prov_model_Document(graph) {
                        const res = this.newDocument$();
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), graph.getStatementOrBundle());
                        if (graph.getNamespace() != null) {
                            res.setNamespace(new org.openprovenance.prov.model.Namespace(graph.getNamespace()));
                        }
                        return res;
                    }
                    newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, informed, informant) {
                        const res = this.mc.newWasInformedBy(id, informed, informant, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, informed, informant, attributes) {
                        const res = this.mc.newWasInformedBy(id, informed, informant, attributes);
                        return res;
                    }
                    newWasInformedBy(id, informed, informant, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((informed != null && (informed.constructor != null && informed.constructor["__interfaces"] != null && informed.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informed === null) && ((informant != null && (informant.constructor != null && informant.constructor["__interfaces"] != null && informant.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informant === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, informed, informant, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((informed != null && (informed.constructor != null && informed.constructor["__interfaces"] != null && informed.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informed === null) && ((informant != null && (informant.constructor != null && informant.constructor["__interfaces"] != null && informant.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informant === null) && attributes === undefined) {
                            return this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, informed, informant);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || id === null) && informed === undefined && informant === undefined && attributes === undefined) {
                            return this.newWasInformedBy$org_openprovenance_prov_model_WasInformedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer) {
                        const res = this.mc.newWasInfluencedBy(id, influencee, influencer, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, influencee, influencer, attributes) {
                        const res = this.mc.newWasInfluencedBy(id, influencee, influencer, attributes);
                        return res;
                    }
                    newWasInfluencedBy(id, influencee, influencer, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((influencee != null && (influencee.constructor != null && influencee.constructor["__interfaces"] != null && influencee.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencee === null) && ((influencer != null && (influencer.constructor != null && influencer.constructor["__interfaces"] != null && influencer.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencer === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, influencee, influencer, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((influencee != null && (influencee.constructor != null && influencee.constructor["__interfaces"] != null && influencee.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencee === null) && ((influencer != null && (influencer.constructor != null && influencer.constructor["__interfaces"] != null && influencer.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencer === null) && attributes === undefined) {
                            return this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || id === null) && influencee === undefined && influencer === undefined && attributes === undefined) {
                            return this.newWasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newHadMember$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName_A(collection, ...entities) {
                        const res = new org.openprovenance.prov.vanilla.HadMember(collection, /* asList */ entities.slice(0));
                        return res;
                    }
                    newHadMember(collection, ...entities) {
                        if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entities != null && entities instanceof Array && (entities.length == 0 || entities[0] == null || (entities[0] != null && (entities[0].constructor != null && entities[0].constructor["__interfaces"] != null && entities[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)))) || entities === null)) {
                            return this.newHadMember$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName_A(collection, ...entities);
                        }
                        else if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entities != null && (entities instanceof Array)) || entities === null)) {
                            return this.newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(collection, entities);
                        }
                        else if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || collection === null) && entities === undefined || entities.length === 0) {
                            return this.newHadMember$org_openprovenance_prov_model_HadMember(collection);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(c, e) {
                        const ll = ([]);
                        if (e != null) {
                            for (let index240 = 0; index240 < e.length; index240++) {
                                let q = e[index240];
                                {
                                    /* add */ (ll.push(q) > 0);
                                }
                            }
                        }
                        const res = new org.openprovenance.prov.vanilla.HadMember(c, ll);
                        return res;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid, eid) {
                        const res = this.mc.newWasStartedBy(id, aid, eid, null, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, starter) {
                        const res = this.mc.newWasStartedBy(id, activity, trigger, starter, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, starter, time, attributes) {
                        const res = this.mc.newWasStartedBy(id, activity, trigger, starter, time, attributes);
                        return res;
                    }
                    newWasStartedBy(id, activity, trigger, starter, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((starter != null && (starter.constructor != null && starter.constructor["__interfaces"] != null && starter.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || starter === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, starter, time, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((starter != null && (starter.constructor != null && starter.constructor["__interfaces"] != null && starter.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || starter === null) && time === undefined && attributes === undefined) {
                            return this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, starter);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && starter === undefined && time === undefined && attributes === undefined) {
                            return this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && activity === undefined && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                            return this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || id === null) && activity === undefined && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                            return this.newWasStartedBy$org_openprovenance_prov_model_WasStartedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid, eid) {
                        const res = this.mc.newWasEndedBy(id, aid, eid, null, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, ender) {
                        const res = this.mc.newWasEndedBy(id, activity, trigger, ender, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, ender, time, attributes) {
                        const res = this.mc.newWasEndedBy(id, activity, trigger, ender, time, attributes);
                        return res;
                    }
                    newWasEndedBy(id, activity, trigger, ender, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((ender != null && (ender.constructor != null && ender.constructor["__interfaces"] != null && ender.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ender === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, ender, time, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((ender != null && (ender.constructor != null && ender.constructor["__interfaces"] != null && ender.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ender === null) && time === undefined && attributes === undefined) {
                            return this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, ender);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ender === undefined && time === undefined && attributes === undefined) {
                            return this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && activity === undefined && trigger === undefined && ender === undefined && time === undefined && attributes === undefined) {
                            return this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || id === null) && activity === undefined && trigger === undefined && ender === undefined && time === undefined && attributes === undefined) {
                            return this.newWasEndedBy$org_openprovenance_prov_model_WasEndedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(namespace, statements, bundles) {
                        const res = this.newDocument$();
                        res.setNamespace(namespace);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), statements);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), bundles);
                        return res;
                    }
                    newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible, activity) {
                        const res = this.mc.newActedOnBehalfOf(id, delegate, responsible, activity, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, delegate, responsible, activity, attributes) {
                        const res = this.mc.newActedOnBehalfOf(id, delegate, responsible, activity, attributes);
                        return res;
                    }
                    newActedOnBehalfOf(id, delegate, responsible, activity, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, delegate, responsible, activity, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && attributes === undefined) {
                            return this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible, activity);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && activity === undefined && attributes === undefined) {
                            return this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || id === null) && delegate === undefined && responsible === undefined && activity === undefined && attributes === undefined) {
                            return this.newActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible) {
                        const res = this.mc.newActedOnBehalfOf(id, delegate, responsible, null, java.util.Collections.EMPTY_LIST);
                        return res;
                    }
                    newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, statements) {
                        return this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, null, statements);
                    }
                    newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, namespace, statements) {
                        const res = this.mc.newNamedBundle(id, namespace, statements);
                        return res;
                    }
                    newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id, ps, as, ags, lks) {
                        const attr = ([]);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(attr, ps);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(attr, as);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(attr, ags);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(attr, lks);
                        return this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attr);
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*[]} ps
                     * @param {*[]} as
                     * @param {*[]} ags
                     * @param {*[]} lks
                     * @return {*}
                     */
                    newNamedBundle(id, ps, as, ags, lks) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                            return this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id, ps, as, ags, lks);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && ps instanceof org.openprovenance.prov.model.Namespace) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ags === undefined && lks === undefined) {
                            return this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, ps, as);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && (ps instanceof Array)) || ps === null) && as === undefined && ags === undefined && lks === undefined) {
                            return this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, ps);
                        }
                        else
                            throw new Error('invalid overload');
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
                        const res = new org.openprovenance.prov.vanilla.QualifiedHadMember(id, c, e, attributes);
                        return res;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} specific
                     * @param {*} general
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newQualifiedSpecializationOf(id, specific, general, attributes) {
                        const res = new org.openprovenance.prov.vanilla.QualifiedSpecializationOf(id, specific, general, attributes);
                        return res;
                    }
                    /**
                     *
                     * @param {*} id
                     * @param {*} alt1
                     * @param {*} alt2
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newQualifiedAlternateOf(id, alt1, alt2, attributes) {
                        const res = new org.openprovenance.prov.vanilla.QualifiedAlternateOf(id, alt1, alt2, attributes);
                        return res;
                    }
                }
                vanilla.ProvFactory = ProvFactory;
                ProvFactory["__class"] = "org.openprovenance.prov.vanilla.ProvFactory";
                ProvFactory["__interfaces"] = ["org.openprovenance.prov.model.AtomConstructor", "org.openprovenance.prov.model.LiteralConstructor", "org.openprovenance.prov.model.ModelConstructorExtension", "org.openprovenance.prov.model.ModelConstructor"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
