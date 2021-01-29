/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var model;
            (function (model) {
                /**
                 * A stateless factory for PROV objects.
                 * @param {*} of
                 * @class
                 */
                class ProvFactory {
                    constructor(of) {
                        if (this.dataFactory === undefined) {
                            this.dataFactory = null;
                        }
                        if (this.of === undefined) {
                            this.of = null;
                        }
                        this.name = null;
                        this.util = new org.openprovenance.prov.model.ProvUtilities();
                        this.of = of;
                        this.init();
                    }
                    static toolboxVersion_$LI$() { if (ProvFactory.toolboxVersion == null) {
                        ProvFactory.toolboxVersion = /* getProperty */ ((m, k) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                return m.entries[i].value;
                            } return null; })(ProvFactory.getPropertiesFromClasspath(ProvFactory.fileName), "toolbox.version");
                    } return ProvFactory.toolboxVersion; }
                    static getPropertiesFromClasspath(propFileName) {
                        const props = new Object();
                        const inputStream = ProvFactory.getClassLoader().getResourceAsStream(propFileName);
                        if (inputStream == null) {
                            return null;
                        }
                        try {
                            props.load(inputStream);
                        }
                        catch (ee) {
                            return null;
                        }
                        return props;
                    }
                    static printURI(u) {
                        return u.toString();
                    }
                    addAttribute(a, o) {
                        /* add */ (a.getOther().push(o) > 0);
                    }
                    addAttributes$org_openprovenance_prov_model_ActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes(from, to) {
                        if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_ActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_Activity$org_openprovenance_prov_model_Activity(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_Agent$org_openprovenance_prov_model_Agent(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_Entity$org_openprovenance_prov_model_Entity(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_Used$org_openprovenance_prov_model_Used(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasEndedBy$org_openprovenance_prov_model_WasEndedBy(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasInformedBy$org_openprovenance_prov_model_WasInformedBy(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(from, to);
                        }
                        else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || to === null)) {
                            return this.addAttributes$org_openprovenance_prov_model_WasStartedBy$org_openprovenance_prov_model_WasStartedBy(from, to);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    addAttributes$org_openprovenance_prov_model_Activity$org_openprovenance_prov_model_Activity(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_Agent$org_openprovenance_prov_model_Agent(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_Entity$org_openprovenance_prov_model_Entity(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_Used$org_openprovenance_prov_model_Used(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasEndedBy$org_openprovenance_prov_model_WasEndedBy(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasInformedBy$org_openprovenance_prov_model_WasInformedBy(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addAttributes$org_openprovenance_prov_model_WasStartedBy$org_openprovenance_prov_model_WasStartedBy(from, to) {
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
                        return to;
                    }
                    addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String(a, label) {
                        /* add */ (a.getLabel().push(this.newInternationalizedString$java_lang_String(label)) > 0);
                    }
                    addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String$java_lang_String(a, label, language) {
                        /* add */ (a.getLabel().push(this.newInternationalizedString$java_lang_String$java_lang_String(label, language)) > 0);
                    }
                    addLabel(a, label, language) {
                        if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) || a === null) && ((typeof label === 'string') || label === null) && ((typeof language === 'string') || language === null)) {
                            return this.addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String$java_lang_String(a, label, language);
                        }
                        else if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) || a === null) && ((typeof label === 'string') || label === null) && language === undefined) {
                            return this.addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String(a, label);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    addPrimarySourceType(a) {
                        /* add */ (a.getType().push(this.newType(this.getName().PROV_PRIMARY_SOURCE, this.getName().PROV_QUALIFIED_NAME)) > 0);
                    }
                    addQuotationType(a) {
                        /* add */ (a.getType().push(this.newType(this.getName().PROV_QUOTATION, this.getName().PROV_QUALIFIED_NAME)) > 0);
                    }
                    addRevisionType(a) {
                        /* add */ (a.getType().push(this.newType(this.getName().PROV_REVISION, this.getName().PROV_QUALIFIED_NAME)) > 0);
                    }
                    addBundleType(a) {
                        /* add */ (a.getType().push(this.newType(this.getName().PROV_BUNDLE, this.getName().PROV_QUALIFIED_NAME)) > 0);
                    }
                    addRole(a, role) {
                        if (role != null) {
                            /* add */ (a.getRole().push(role) > 0);
                        }
                    }
                    addType$org_openprovenance_prov_model_HasType$java_lang_Object$org_openprovenance_prov_model_QualifiedName(a, o, type) {
                        /* add */ (a.getType().push(this.newType(o, type)) > 0);
                    }
                    addType(a, o, type) {
                        if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) || a === null) && ((o != null) || o === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                            return this.addType$org_openprovenance_prov_model_HasType$java_lang_Object$org_openprovenance_prov_model_QualifiedName(a, o, type);
                        }
                        else if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) || a === null) && ((o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Type") >= 0)) || o === null) && type === undefined) {
                            return this.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(a, o);
                        }
                        else if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) || a === null) && ((o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || o === null) && type === undefined) {
                            return this.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_QualifiedName(a, o);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(a, type) {
                        /* add */ (a.getType().push(type) > 0);
                    }
                    addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_QualifiedName(a, type) {
                        /* add */ (a.getType().push(this.newType(type, this.getName().PROV_QUALIFIED_NAME)) > 0);
                    }
                    base64Decoding(s) {
                        return java.util.Base64.getDecoder().decode(s);
                    }
                    base64Encoding(b) {
                        return java.util.Base64.getEncoder().encodeToString(b);
                    }
                    getLabel(e) {
                        const labels = e.getLabel();
                        if ((labels == null) || ( /* isEmpty */(labels.length == 0)))
                            return null;
                        if (e != null && (e.constructor != null && e.constructor["__interfaces"] != null && e.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0))
                            return /* get */ labels[0].getValue();
                        return "pFact: label TODO";
                    }
                    getName() {
                        if (this.name == null) {
                            this.name = new org.openprovenance.prov.model.Name(this);
                        }
                        return this.name;
                    }
                    getObjectFactory() {
                        return this.of;
                    }
                    getPackageList() {
                        return ProvFactory.packageList;
                    }
                    getRole(e) {
                        return "pFact: role TODO";
                    }
                    getType(e) {
                        if (e != null && (e.constructor != null && e.constructor["__interfaces"] != null && e.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0))
                            return e.getType();
                        const res = ([]);
                        /* add */ (res.push(this.newType("pFact: type TODO", this.getName().XSD_STRING)) > 0);
                        return res;
                    }
                    getVersion() {
                        return ProvFactory.toolboxVersion_$LI$();
                    }
                    hexDecoding(s) {
                        const byteArray = new java.math.BigInteger(s, 16).toByteArray();
                        if (byteArray[0] === 0) {
                            const output = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(byteArray.length - 1);
                            /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                while (--size >= 0)
                                    dstPts[dstOff++] = srcPts[srcOff++];
                            }
                            else {
                                let tmp = srcPts.slice(srcOff, srcOff + size);
                                for (let i = 0; i < size; i++)
                                    dstPts[dstOff++] = tmp[i];
                            } })(byteArray, 1, output, 0, output.length);
                            return output;
                        }
                        return byteArray;
                    }
                    hexEncoding(b) {
                        const bigInteger = new java.math.BigInteger(1, b);
                        return bigInteger.toString(16);
                    }
                    init() {
                        try {
                            this.dataFactory = javax.xml.datatype.DatatypeFactory.newInstance();
                        }
                        catch (ex) {
                            throw Object.defineProperty(new Error(ex.message), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                    }
                    newActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(u) {
                        const u1 = this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(u.getId(), u.getDelegate(), u.getResponsible(), u.getActivity(), null);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        return u1;
                    }
                    newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible, activity) {
                        const res = this.of.createActedOnBehalfOf();
                        res.setId(id);
                        res.setActivity(activity);
                        res.setDelegate(delegate);
                        res.setResponsible(responsible);
                        return res;
                    }
                    newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, delegate, responsible, activity, attributes) {
                        const res = this.of.createActedOnBehalfOf();
                        res.setId(id);
                        res.setActivity(activity);
                        res.setDelegate(delegate);
                        res.setResponsible(responsible);
                        this.setAttributes(res, attributes);
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
                        const res = this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, delegate, responsible, null, null);
                        return res;
                    }
                    newActivity$org_openprovenance_prov_model_Activity(a) {
                        const res = this.newActivity$org_openprovenance_prov_model_QualifiedName(a.getId());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getType(), a.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getLabel(), a.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getLocation(), a.getLocation());
                        res.setStartTime(a.getStartTime());
                        res.setEndTime(a.getEndTime());
                        return res;
                    }
                    newActivity$org_openprovenance_prov_model_QualifiedName(a) {
                        const res = this.of.createActivity();
                        res.setId(a);
                        return res;
                    }
                    newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(q, label) {
                        const res = this.newActivity$org_openprovenance_prov_model_QualifiedName(q);
                        if (label != null) /* add */
                            (res.getLabel().push(this.newInternationalizedString$java_lang_String(label)) > 0);
                        return res;
                    }
                    newActivity$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, startTime, endTime, attributes) {
                        const res = this.newActivity$org_openprovenance_prov_model_QualifiedName(id);
                        res.setStartTime(startTime);
                        res.setEndTime(endTime);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newActivity(id, startTime, endTime, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((startTime != null && startTime instanceof javax.xml.datatype.XMLGregorianCalendar) || startTime === null) && ((endTime != null && endTime instanceof javax.xml.datatype.XMLGregorianCalendar) || endTime === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newActivity$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, startTime, endTime, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof startTime === 'string') || startTime === null) && endTime === undefined && attributes === undefined) {
                            return this.newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, startTime);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                            return this.newActivity$org_openprovenance_prov_model_Activity(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                            return this.newActivity$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newAgent$org_openprovenance_prov_model_Agent(a) {
                        const res = this.newAgent$org_openprovenance_prov_model_QualifiedName(a.getId());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getType(), a.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getLabel(), a.getLabel());
                        return res;
                    }
                    newAgent$org_openprovenance_prov_model_QualifiedName(ag) {
                        const res = this.of.createAgent();
                        res.setId(ag);
                        return res;
                    }
                    newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes) {
                        const res = this.newAgent$org_openprovenance_prov_model_QualifiedName(id);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    /**
                     * Creates a new {@link Agent} with provided identifier and attributes
                     * @param {*} id a {@link QualifiedName} for the agent
                     * @param {*[]} attributes a collection of {@link Attribute} for the agent
                     * @return {*} an object of type {@link Agent}
                     */
                    newAgent(id, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof attributes === 'string') || attributes === null)) {
                            return this.newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || id === null) && attributes === undefined) {
                            return this.newAgent$org_openprovenance_prov_model_Agent(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && attributes === undefined) {
                            return this.newAgent$org_openprovenance_prov_model_QualifiedName(id);
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
                    newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1, entity2) {
                        const res = this.of.createAlternateOf();
                        res.setAlternate1(entity1);
                        res.setAlternate2(entity2);
                        return res;
                    }
                    /**
                     * A factory method to create an instance of an alternate {@link AlternateOf}
                     * @param {*} entity1 an identifier for the first {@link Entity}
                     * @param {*} entity2 an identifier for the second {@link Entity}
                     * @return {*} an instance of {@link AlternateOf}
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
                    newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type) { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }
                    newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(kind, value, type) { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }
                    newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix, value, type) {
                        let res;
                        res = this.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, localName, prefix), value, type);
                        return res;
                    }
                    newAttribute(namespace, localName, prefix, value, type) {
                        if (((typeof namespace === 'string') || namespace === null) && ((typeof localName === 'string') || localName === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                            return this.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix, value, type);
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
                    newDerivedByInsertionFrom(id, after, before, keyEntitySet, attributes) {
                        const res = this.of.createDerivedByInsertionFrom();
                        res.setId(id);
                        res.setNewDictionary(after);
                        res.setOldDictionary(before);
                        if (keyEntitySet != null) /* addAll */
                            ((l1, l2) => l1.push.apply(l1, l2))(res.getKeyEntityPair(), keyEntitySet);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newDerivedByRemovalFrom(id, after, before, keys, attributes) {
                        const res = this.of.createDerivedByRemovalFrom();
                        res.setId(id);
                        res.setNewDictionary(after);
                        res.setOldDictionary(before);
                        if (keys != null) /* addAll */
                            ((l1, l2) => l1.push.apply(l1, l2))(res.getKey(), keys);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newDictionaryMembership(dict, entitySet) {
                        const res = this.of.createDictionaryMembership();
                        res.setDictionary(dict);
                        if (entitySet != null) /* addAll */
                            ((l1, l2) => l1.push.apply(l1, l2))(res.getKeyEntityPair(), entitySet);
                        return res;
                    }
                    newDocument$() {
                        const res = this.of.createDocument();
                        return res;
                    }
                    newDocument$org_openprovenance_prov_model_Activity_A$org_openprovenance_prov_model_Entity_A$org_openprovenance_prov_model_Agent_A$org_openprovenance_prov_model_Statement_A(ps, as, ags, lks) {
                        return this.newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(((ps == null) ? null : /* asList */ ps.slice(0)), ((as == null) ? null : /* asList */ as.slice(0)), ((ags == null) ? null : /* asList */ ags.slice(0)), ((lks == null) ? null : /* asList */ lks.slice(0)));
                    }
                    newDocument(ps, as, ags, lks) {
                        if (((ps != null && ps instanceof Array && (ps.length == 0 || ps[0] == null || (ps[0] != null && (ps[0].constructor != null && ps[0].constructor["__interfaces"] != null && ps[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)))) || ps === null) && ((as != null && as instanceof Array && (as.length == 0 || as[0] == null || (as[0] != null && (as[0].constructor != null && as[0].constructor["__interfaces"] != null && as[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)))) || as === null) && ((ags != null && ags instanceof Array && (ags.length == 0 || ags[0] == null || (ags[0] != null && (ags[0].constructor != null && ags[0].constructor["__interfaces"] != null && ags[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)))) || ags === null) && ((lks != null && lks instanceof Array && (lks.length == 0 || lks[0] == null || (lks[0] != null && (lks[0].constructor != null && lks[0].constructor["__interfaces"] != null && lks[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Statement") >= 0)))) || lks === null)) {
                            return this.newDocument$org_openprovenance_prov_model_Activity_A$org_openprovenance_prov_model_Entity_A$org_openprovenance_prov_model_Agent_A$org_openprovenance_prov_model_Statement_A(ps, as, ags, lks);
                        }
                        else if (((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                            return this.newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps, as, ags, lks);
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
                    newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps, as, ags, lks) {
                        const res = this.of.createDocument();
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ps);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), as);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ags);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), lks);
                        return res;
                    }
                    newDocument$org_openprovenance_prov_model_Document(graph) {
                        const res = this.of.createDocument();
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), graph.getStatementOrBundle());
                        if (graph.getNamespace() != null) {
                            res.setNamespace(new org.openprovenance.prov.model.Namespace(graph.getNamespace()));
                        }
                        return res;
                    }
                    newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(namespace, statements, bundles) {
                        const res = this.of.createDocument();
                        res.setNamespace(namespace);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), statements);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), bundles);
                        return res;
                    }
                    newDuration$int(durationInMilliSeconds) {
                        const dur = this.dataFactory.newDuration(durationInMilliSeconds);
                        return dur;
                    }
                    newDuration$java_lang_String(lexicalRepresentation) {
                        const dur = this.dataFactory.newDuration(lexicalRepresentation);
                        return dur;
                    }
                    newDuration(lexicalRepresentation) {
                        if (((typeof lexicalRepresentation === 'string') || lexicalRepresentation === null)) {
                            return this.newDuration$java_lang_String(lexicalRepresentation);
                        }
                        else if (((typeof lexicalRepresentation === 'number') || lexicalRepresentation === null)) {
                            return this.newDuration$int(lexicalRepresentation);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newEntity$org_openprovenance_prov_model_Entity(e) {
                        const res = this.newEntity$org_openprovenance_prov_model_QualifiedName(e.getId());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getOther(), e.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getType(), e.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getLabel(), e.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getLocation(), e.getLocation());
                        return res;
                    }
                    newEntity$org_openprovenance_prov_model_QualifiedName(id) {
                        const res = this.of.createEntity();
                        res.setId(id);
                        return res;
                    }
                    newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes) {
                        const res = this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    /**
                     * Creates a new {@link Entity} with provided identifier and attributes
                     * @param {*} id a {@link QualifiedName} for the entity
                     * @param {*[]} attributes a collection of {@link Attribute} for the entity
                     * @return {*} an object of type {@link Entity}
                     */
                    newEntity(id, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof attributes === 'string') || attributes === null)) {
                            return this.newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || id === null) && attributes === undefined) {
                            return this.newEntity$org_openprovenance_prov_model_Entity(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && attributes === undefined) {
                            return this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, label) {
                        const res = this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
                        if (label != null) /* add */
                            (res.getLabel().push(this.newInternationalizedString$java_lang_String(label)) > 0);
                        return res;
                    }
                    /**
                     * Factory method for Key-entity entries. Key-entity entries are used to identify the members of a dictionary.
                     * @param {*} key indexing the entity in the dictionary
                     * @param {*} entity a {@link QualifiedName} denoting an entity
                     * @return {*} an instance of {@link Entry}
                     */
                    newEntry(key, entity) {
                        const res = this.of.createEntry();
                        res.setKey(key);
                        res.setEntity(entity);
                        return res;
                    }
                    newGDay(day) {
                        const cal = this.dataFactory.newXMLGregorianCalendar();
                        cal.setDay(day);
                        return cal;
                    }
                    newGMonth(month) {
                        const cal = this.dataFactory.newXMLGregorianCalendar();
                        cal.setMonth(month);
                        return cal;
                    }
                    newGMonthDay(month, day) {
                        const cal = this.dataFactory.newXMLGregorianCalendar();
                        cal.setMonth(month);
                        cal.setDay(day);
                        return cal;
                    }
                    newGYear(year) {
                        const cal = this.dataFactory.newXMLGregorianCalendar();
                        cal.setYear(year);
                        return cal;
                    }
                    newHadMember$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName_A(collection, ...entities) {
                        const res = this.of.createHadMember();
                        res.setCollection(collection);
                        if (entities != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getEntity(), /* asList */ entities.slice(0));
                        }
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
                            for (let index196 = 0; index196 < e.length; index196++) {
                                let q = e[index196];
                                {
                                    /* add */ (ll.push(q) > 0);
                                }
                            }
                        }
                        const res = this.of.createHadMember();
                        res.setCollection(c);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getEntity(), ll);
                        return res;
                    }
                    newInternationalizedString$java_lang_String(s) {
                        const res = this.of.createInternationalizedString();
                        res.setValue(s);
                        return res;
                    }
                    newInternationalizedString$java_lang_String$java_lang_String(s, lang) {
                        const res = this.of.createInternationalizedString();
                        res.setValue(s);
                        res.setLang(lang);
                        return res;
                    }
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
                    newISOTime(time) {
                        return time; // LUC
                        //return this.newTime(/* getTime */ (new Date(javax.xml.bind.DatatypeConverter.parseDateTime(time).getTime())));
                    }
                    newLocation(value, type) {
                        const res = this.of.createLocation();
                        res.setType(type);
                        res.setValueFromObject(value);
                        return res;
                    }
                    newMentionOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(infra, supra, bundle) {
                        const res = this.of.createMentionOf();
                        res.setSpecificEntity(infra);
                        res.setBundle(bundle);
                        res.setGeneralEntity(supra);
                        return res;
                    }
                    newMentionOf(infra, supra, bundle) {
                        if (((infra != null && (infra.constructor != null && infra.constructor["__interfaces"] != null && infra.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || infra === null) && ((supra != null && (supra.constructor != null && supra.constructor["__interfaces"] != null && supra.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || supra === null) && ((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || bundle === null)) {
                            return this.newMentionOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(infra, supra, bundle);
                        }
                        else if (((infra != null && (infra.constructor != null && infra.constructor["__interfaces"] != null && infra.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || infra === null) && supra === undefined && bundle === undefined) {
                            return this.newMentionOf$org_openprovenance_prov_model_MentionOf(infra);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newMentionOf$org_openprovenance_prov_model_MentionOf(r) {
                        const res = this.of.createMentionOf();
                        res.setSpecificEntity(r.getSpecificEntity());
                        res.setBundle(r.getBundle());
                        res.setGeneralEntity(r.getGeneralEntity());
                        return res;
                    }
                    newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id, ps, as, ags, lks) {
                        const res = this.of.createNamedBundle();
                        res.setId(id);
                        if (ps != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), ps);
                        }
                        if (as != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), as);
                        }
                        if (ags != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), ags);
                        }
                        if (lks != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), lks);
                        }
                        return res;
                    }
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
                    newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, lks) {
                        const res = this.of.createNamedBundle();
                        res.setId(id);
                        if (lks != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), lks);
                        }
                        return res;
                    }
                    newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, namespace, statements) {
                        const res = this.of.createNamedBundle();
                        res.setId(id);
                        res.setNamespace(namespace);
                        if (statements != null) {
                            /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), statements);
                        }
                        return res;
                    }
                    newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type) {
                        if (value == null)
                            return null;
                        const res = this.of.createOther();
                        res.setType(type);
                        res.setValueFromObject(value);
                        res.setElementName(elementName);
                        return res;
                    }
                    newOther$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, value, type) {
                        const elementName = this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
                        return this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type);
                    }
                    newOther(namespace, local, prefix, value, type) {
                        if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                            return this.newOther$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, value, type);
                        }
                        else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((local != null) || local === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                            return this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix) { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }
                    newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag) { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }
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
                    newQualifiedName$javax_xml_namespace_QName(qname) {
                        return this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
                    }
                    newRole(value, type) {
                        if (value == null)
                            return null;
                        const res = this.of.createRole();
                        res.setType(type);
                        res.setValueFromObject(value);
                        return res;
                    }
                    newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific, general) {
                        const res = this.of.createSpecializationOf();
                        res.setSpecificEntity(specific);
                        res.setGeneralEntity(general);
                        return res;
                    }
                    /**
                     * A factory method to create an instance of a specialization {@link SpecializationOf}
                     * @param {*} specific an identifier ({@link QualifiedName}) for the specific {@link Entity}
                     * @param {*} general an identifier  ({@link QualifiedName}) for the general {@link Entity}
                     * @return {*} an instance of {@link SpecializationOf}
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
                    /**
                     *
                     * @param {*} id
                     * @param {*} specific
                     * @param {*} general
                     * @param {*[]} attributes
                     * @return {*}
                     */
                    newQualifiedSpecializationOf(id, specific, general, attributes) {
                        const res = this.of.createQualifiedSpecializationOf();
                        res.setId(id);
                        res.setSpecificEntity(specific);
                        res.setGeneralEntity(general);
                        this.setAttributes(res, attributes);
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
                        const res = this.of.createQualifiedAlternateOf();
                        res.setId(id);
                        res.setAlternate1(alt1);
                        res.setAlternate2(alt2);
                        this.setAttributes(res, attributes);
                        return res;
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
                        const ll = ([]);
                        if (e != null) {
                            for (let index197 = 0; index197 < e.length; index197++) {
                                let q = e[index197];
                                {
                                    /* add */ (ll.push(q) > 0);
                                }
                            }
                        }
                        const res = this.of.createQualifiedHadMember();
                        res.setId(id);
                        res.setCollection(c);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(res.getEntity(), e);
                        return res;
                    }
                    newTime(date) {
                        const gc = new Date();
                        /* setTime */ gc.setTime(date.getTime());
                        return this.newXMLGregorianCalendar(gc);
                    }
                    newTimeNow() {
                        return this.newTime(new Date());
                    }
                    newType(value, type) {
                        if (value == null)
                            return null;
                        const res = this.of.createType();
                        res.setType(type);
                        res.setValueFromObject(value);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName(id) {
                        const res = this.of.createUsed();
                        res.setId(id);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, aid, role, eid) {
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName(id);
                        res.setActivity(aid);
                        if (role != null)
                            this.addRole(res, this.newRole(role, this.getName().XSD_STRING));
                        res.setEntity(eid);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, entity) {
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName(id);
                        res.setActivity(activity);
                        res.setEntity(entity);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(activity, entity) {
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName(null);
                        res.setActivity(activity);
                        res.setEntity(entity);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, entity, time, attributes) {
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, activity, null, entity);
                        res.setTime(time);
                        this.setAttributes(res, attributes);
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
                        const res = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, activity, null, entity);
                        res.setTime(time);
                        return res;
                    }
                    newUsed$org_openprovenance_prov_model_Used(u) {
                        const u1 = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getEntity());
                        u1.setTime(u.getTime());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        return u1;
                    }
                    newValue$java_lang_String(value) {
                        return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, this.getName().XSD_STRING);
                    }
                    newValue$int(value) {
                        return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, this.getName().XSD_INT);
                    }
                    newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type) {
                        if (value == null)
                            return null;
                        const res = this.of.createValue();
                        res.setType(type);
                        res.setValueFromObject(value);
                        return res;
                    }
                    /**
                     * Factory method to create an instance of the PROV-DM prov:value attribute (see {@link Value}).
                     * Use class {@link Name} for predefined {@link QualifiedName}s for the common types.
                     * @param {*} value an {@link Object}
                     * @param {*} type a {@link QualifiedName} to denote the type of value
                     * @return {*} a new {@link Value}
                     */
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
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName(id) {
                        return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, null, null);
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, agent) {
                        const res = this.of.createWasAssociatedWith();
                        res.setId(id);
                        res.setActivity(activity);
                        res.setAgent(agent);
                        return res;
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a, ag, plan, attributes) {
                        const res = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
                        res.setPlan(plan);
                        this.setAttributes(res, attributes);
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
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName(id);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                            return this.newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag, plan) {
                        const res = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
                        res.setPlan(plan);
                        return res;
                    }
                    newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(u) {
                        const u1 = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getAgent());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        u1.setPlan(u.getPlan());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
                        return u1;
                    }
                    newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent) {
                        const res = this.of.createWasAttributedTo();
                        res.setId(id);
                        res.setEntity(entity);
                        res.setAgent(agent);
                        return res;
                    }
                    newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, entity, agent, attributes) {
                        const res = this.of.createWasAttributedTo();
                        res.setId(id);
                        res.setEntity(entity);
                        res.setAgent(agent);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newWasAttributedTo(id, entity, agent, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, entity, agent, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && attributes === undefined) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || id === null) && entity === undefined && agent === undefined && attributes === undefined) {
                            return this.newWasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(u) {
                        const u1 = this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getEntity(), u.getAgent());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        return u1;
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, e2, e1) {
                        const res = this.of.createWasDerivedFrom();
                        res.setId(id);
                        res.setUsedEntity(e1);
                        res.setGeneratedEntity(e2);
                        return res;
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(e2, e1) {
                        const res = this.of.createWasDerivedFrom();
                        res.setUsedEntity(e1);
                        res.setGeneratedEntity(e2);
                        return res;
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, aid1, aid2, aid, did1, did2, attributes) {
                        const res = this.of.createWasDerivedFrom();
                        res.setId(id);
                        res.setUsedEntity(aid2);
                        res.setGeneratedEntity(aid1);
                        res.setActivity(aid);
                        res.setGeneration(did1);
                        res.setUsage(did2);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newWasDerivedFrom(id, aid1, aid2, aid, did1, did2, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((aid1 != null && (aid1.constructor != null && aid1.constructor["__interfaces"] != null && aid1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid1 === null) && ((aid2 != null && (aid2.constructor != null && aid2.constructor["__interfaces"] != null && aid2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid2 === null) && ((aid != null && (aid.constructor != null && aid.constructor["__interfaces"] != null && aid.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid === null) && ((did1 != null && (did1.constructor != null && did1.constructor["__interfaces"] != null && did1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || did1 === null) && ((did2 != null && (did2.constructor != null && did2.constructor["__interfaces"] != null && did2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || did2 === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, aid1, aid2, aid, did1, did2, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((aid1 != null && (aid1.constructor != null && aid1.constructor["__interfaces"] != null && aid1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid1 === null) && ((aid2 != null && (aid2.constructor != null && aid2.constructor["__interfaces"] != null && aid2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid2 === null) && aid === undefined && did1 === undefined && did2 === undefined && attributes === undefined) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid1, aid2);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((aid1 != null && (aid1.constructor != null && aid1.constructor["__interfaces"] != null && aid1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid1 === null) && aid2 === undefined && aid === undefined && did1 === undefined && did2 === undefined && attributes === undefined) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid1);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || id === null) && aid1 === undefined && aid2 === undefined && aid === undefined && did1 === undefined && did2 === undefined && attributes === undefined) {
                            return this.newWasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(d) {
                        const wdf = this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(d.getId(), d.getGeneratedEntity(), d.getUsedEntity());
                        wdf.setActivity(d.getActivity());
                        wdf.setGeneration(d.getGeneration());
                        wdf.setUsage(d.getUsage());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wdf.getOther(), d.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wdf.getType(), d.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wdf.getLabel(), d.getLabel());
                        return wdf;
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName(id) {
                        const res = this.of.createWasEndedBy();
                        res.setId(id);
                        return res;
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger) {
                        const res = this.of.createWasEndedBy();
                        res.setId(id);
                        res.setActivity(activity);
                        res.setTrigger(trigger);
                        return res;
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, ender) {
                        const res = this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                        res.setEnder(ender);
                        return res;
                    }
                    newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, ender, time, attributes) {
                        const res = this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                        res.setTime(time);
                        res.setEnder(ender);
                        this.setAttributes(res, attributes);
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
                    newWasEndedBy$org_openprovenance_prov_model_WasEndedBy(u) {
                        const u1 = this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getTrigger());
                        u1.setEnder(u.getEnder());
                        u1.setTime(u.getTime());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u1.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        return u1;
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(a, role, p) {
                        return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(null, a, role, p);
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName(id) {
                        const res = this.of.createWasGeneratedBy();
                        res.setId(id);
                        return res;
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, a, role, p) {
                        const res = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a.getId(), p.getId());
                        if (role != null)
                            this.addRole(res, this.newRole(role, this.getName().XSD_STRING));
                        return res;
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, aid, role, pid) {
                        const res = this.of.createWasGeneratedBy();
                        res.setId(id);
                        res.setActivity(pid);
                        res.setEntity(aid);
                        if (role != null)
                            this.addRole(res, this.newRole(role, this.getName().XSD_STRING));
                        return res;
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity) {
                        const res = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, null, activity);
                        return res;
                    }
                    newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes) {
                        const res = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, null, activity);
                        res.setTime(time);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newWasGeneratedBy(id, entity, activity, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || time === null) && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, entity, activity, time);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, activity, time);
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
                    newWasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(g) {
                        const wgb = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(g.getId(), g.getEntity(), null, g.getActivity());
                        wgb.setId(g.getId());
                        wgb.setTime(g.getTime());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wgb.getOther(), g.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wgb.getRole(), g.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wgb.getType(), g.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wgb.getLabel(), g.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wgb.getLocation(), g.getLocation());
                        return wgb;
                    }
                    newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer) {
                        const res = this.of.createWasInfluencedBy();
                        res.setId(id);
                        res.setInfluencee(influencee);
                        res.setInfluencer(influencer);
                        return res;
                    }
                    newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, influencee, influencer, attributes) {
                        const res = this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer);
                        this.setAttributes(res, attributes);
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
                    newWasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(__in) {
                        const out = this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(__in.getId(), __in.getInfluencee(), __in.getInfluencer());
                        out.setId(__in.getId());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(out.getOther(), __in.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(out.getType(), __in.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(out.getLabel(), __in.getLabel());
                        return out;
                    }
                    newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, informed, informant) {
                        const res = this.of.createWasInformedBy();
                        res.setId(id);
                        res.setInformed(informed);
                        res.setInformant(informant);
                        return res;
                    }
                    newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a2, a1, attributes) {
                        const res = this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a2, a1);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newWasInformedBy(id, a2, a1, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a2 != null && (a2.constructor != null && a2.constructor["__interfaces"] != null && a2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a2 === null) && ((a1 != null && (a1.constructor != null && a1.constructor["__interfaces"] != null && a1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a1 === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a2, a1, attributes);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a2 != null && (a2.constructor != null && a2.constructor["__interfaces"] != null && a2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a2 === null) && ((a1 != null && (a1.constructor != null && a1.constructor["__interfaces"] != null && a1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a1 === null) && attributes === undefined) {
                            return this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a2, a1);
                        }
                        else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || id === null) && a2 === undefined && a1 === undefined && attributes === undefined) {
                            return this.newWasInformedBy$org_openprovenance_prov_model_WasInformedBy(id);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newWasInformedBy$org_openprovenance_prov_model_WasInformedBy(d) {
                        const wtb = this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(d.getId(), d.getInformed(), d.getInformant());
                        wtb.setId(d.getId());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wtb.getOther(), d.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wtb.getType(), d.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(wtb.getLabel(), d.getLabel());
                        return wtb;
                    }
                    newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(eid, aid) {
                        const res = this.of.createWasInvalidatedBy();
                        res.setEntity(eid);
                        res.setActivity(aid);
                        return res;
                    }
                    newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity) {
                        const res = this.of.createWasInvalidatedBy();
                        res.setId(id);
                        res.setEntity(entity);
                        res.setActivity(activity);
                        return res;
                    }
                    newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes) {
                        const res = this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
                        res.setTime(time);
                        this.setAttributes(res, attributes);
                        return res;
                    }
                    newWasInvalidatedBy(id, entity, activity, time, attributes) {
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            return this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes);
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
                    newWasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(u) {
                        const u1 = this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getEntity(), u.getActivity());
                        u1.setTime(u.getTime());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u.getRole(), u.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
                        return u1;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName(id) {
                        const res = this.of.createWasStartedBy();
                        res.setId(id);
                        return res;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid, eid) {
                        const res = this.of.createWasStartedBy();
                        res.setId(id);
                        res.setActivity(aid);
                        res.setTrigger(eid);
                        return res;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, starter) {
                        const res = this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                        res.setStarter(starter);
                        return res;
                    }
                    newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, starter, time, attributes) {
                        const res = this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
                        res.setTime(time);
                        res.setStarter(starter);
                        this.setAttributes(res, attributes);
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
                    newWasStartedBy$org_openprovenance_prov_model_WasStartedBy(u) {
                        const u1 = this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getTrigger());
                        u1.setStarter(u.getStarter());
                        u1.setTime(u.getTime());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
                        return u1;
                    }
                    newXMLGregorianCalendar(gc) {
                        return this.dataFactory.newXMLGregorianCalendar(gc);
                    }
                    newYear(year) {
                        const res = this.dataFactory.newXMLGregorianCalendar();
                        res.setYear(year);
                        return res;
                    }
                    getAttributes(statement) {
                        const result = ([]);
                        if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) /* addAll */
                            ((l1, l2) => l1.push.apply(l1, l2))(result, statement.getType());
                        if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0)) /* addAll */
                            ((l1, l2) => l1.push.apply(l1, l2))(result, statement.getLocation());
                        if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasRole") >= 0)) /* addAll */
                            ((l1, l2) => l1.push.apply(l1, l2))(result, statement.getRole());
                        if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasValue") >= 0)) {
                            const val = statement.getValue();
                            if (val != null) {
                                /* add */ (result.push(val) > 0);
                            }
                        }
                        if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasOther") >= 0)) {
                            {
                                let array199 = statement.getOther();
                                for (let index198 = 0; index198 < array199.length; index198++) {
                                    let o = array199[index198];
                                    {
                                        /* add */ (result.push(o) > 0);
                                    }
                                }
                            }
                        }
                        return result;
                    }
                    setAttributes(res, attributes) {
                        if (attributes == null)
                            return;
                        if ( /* isEmpty */(attributes.length == 0))
                            return;
                        const typ = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) ? res : null;
                        const loc = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0)) ? res : null;
                        const lab = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) ? res : null;
                        const aval = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasValue") >= 0)) ? res : null;
                        const rol = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasRole") >= 0)) ? res : null;
                        for (let index200 = 0; index200 < attributes.length; index200++) {
                            let attr = attributes[index200];
                            {
                                let aValue = attr.getValue();
                                const vconv = new org.openprovenance.prov.model.ValueConverter(this);
                                if ( /* equals */((o1, o2) => { if (o1 && o1.equals) {
                                    return o1.equals(o2);
                                }
                                else {
                                    return o1 === o2;
                                } })(this.getName().RDF_LITERAL, attr.getType()) && (typeof aValue === 'string')) {
                                    aValue = vconv.convertToJava(attr.getType(), aValue);
                                }
                                switch ((attr.getKind())) {
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                                        if (lab != null) {
                                            if (aValue != null && (aValue.constructor != null && aValue.constructor["__interfaces"] != null && aValue.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) {
                                                /* add */ (lab.getLabel().push(aValue) > 0);
                                            }
                                            else {
                                                /* add */ (lab.getLabel().push(this.newInternationalizedString$java_lang_String(aValue.toString())) > 0);
                                            }
                                        }
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                                        if (loc != null) {
                                            /* add */ (loc.getLocation().push(this.newLocation(aValue, attr.getType())) > 0);
                                        }
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                                        if (rol != null) {
                                            /* add */ (rol.getRole().push(this.newRole(aValue, attr.getType())) > 0);
                                        }
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                                        if (typ != null) {
                                            /* add */ (typ.getType().push(this.newType(aValue, attr.getType())) > 0);
                                        }
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                                        if (aval != null) {
                                            aval.setValue(this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(aValue, attr.getType()));
                                        }
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                                        /* add */ (res.getOther().push(this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(attr.getElementName(), aValue, attr.getType())) > 0);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }
                    /**
                     *
                     * @param {*} bundleId
                     * @param {org.openprovenance.prov.model.Namespace} namespaces
                     */
                    startBundle(bundleId, namespaces) {
                    }
                    /**
                     *
                     * @param {org.openprovenance.prov.model.Namespace} namespace
                     */
                    startDocument(namespace) {
                    }
                    newNamespace$org_openprovenance_prov_model_Namespace(ns) {
                        return new org.openprovenance.prov.model.Namespace(ns);
                    }
                    newNamespace(ns) {
                        if (((ns != null && ns instanceof org.openprovenance.prov.model.Namespace) || ns === null)) {
                            return this.newNamespace$org_openprovenance_prov_model_Namespace(ns);
                        }
                        else if (ns === undefined) {
                            return this.newNamespace$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    newNamespace$() {
                        return new org.openprovenance.prov.model.Namespace();
                    }
                    newAlternateOf$org_openprovenance_prov_model_AlternateOf(s) {
                        const res = this.newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(s.getAlternate1(), s.getAlternate2());
                        return res;
                    }
                    newSpecializationOf$org_openprovenance_prov_model_SpecializationOf(s) {
                        const res = this.newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(s.getSpecificEntity(), s.getGeneralEntity());
                        return res;
                    }
                    newHadMember$org_openprovenance_prov_model_HadMember(s) {
                        const res = this.newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(s.getCollection(), s.getEntity());
                        return res;
                    }
                    newStatement(s) {
                        return this.util.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, new ProvFactory.Cloner(this));
                    }
                }
                ProvFactory.packageList = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";
                ProvFactory.fileName = "toolbox.properties";
                model.ProvFactory = ProvFactory;
                ProvFactory["__class"] = "org.openprovenance.prov.model.ProvFactory";
                ProvFactory["__interfaces"] = ["org.openprovenance.prov.model.LiteralConstructor", "org.openprovenance.prov.model.ModelConstructorExtension", "org.openprovenance.prov.model.ModelConstructor"];
                (function (ProvFactory) {
                    class Cloner {
                        constructor(__parent) {
                            this.__parent = __parent;
                        }
                        doAction$org_openprovenance_prov_model_Activity(s) {
                            return this.__parent.newActivity(s);
                        }
                        doAction$org_openprovenance_prov_model_Used(s) {
                            return this.__parent.newUsed(s);
                        }
                        doAction$org_openprovenance_prov_model_WasStartedBy(s) {
                            return this.__parent.newWasStartedBy(s);
                        }
                        doAction$org_openprovenance_prov_model_Agent(s) {
                            return this.__parent.newAgent(s);
                        }
                        doAction$org_openprovenance_prov_model_AlternateOf(s) {
                            return this.__parent.newAlternateOf(s);
                        }
                        doAction$org_openprovenance_prov_model_WasAssociatedWith(s) {
                            return this.__parent.newWasAssociatedWith(s);
                        }
                        doAction$org_openprovenance_prov_model_WasAttributedTo(s) {
                            return this.__parent.newWasAttributedTo(s);
                        }
                        doAction$org_openprovenance_prov_model_WasInfluencedBy(s) {
                            return this.__parent.newWasInfluencedBy(s);
                        }
                        doAction$org_openprovenance_prov_model_ActedOnBehalfOf(s) {
                            return this.__parent.newActedOnBehalfOf(s);
                        }
                        doAction$org_openprovenance_prov_model_WasDerivedFrom(s) {
                            return this.__parent.newWasDerivedFrom(s);
                        }
                        doAction$org_openprovenance_prov_model_DictionaryMembership(s) {
                            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                        doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(s) {
                            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                        doAction$org_openprovenance_prov_model_WasEndedBy(s) {
                            return this.__parent.newWasEndedBy(s);
                        }
                        doAction$org_openprovenance_prov_model_Entity(s) {
                            return this.__parent.newEntity(s);
                        }
                        doAction$org_openprovenance_prov_model_WasGeneratedBy(s) {
                            return this.__parent.newWasGeneratedBy(s);
                        }
                        doAction$org_openprovenance_prov_model_WasInvalidatedBy(s) {
                            return this.__parent.newWasInvalidatedBy(s);
                        }
                        doAction$org_openprovenance_prov_model_HadMember(s) {
                            return this.__parent.newHadMember(s);
                        }
                        doAction$org_openprovenance_prov_model_MentionOf(s) {
                            return this.__parent.newMentionOf(s);
                        }
                        doAction$org_openprovenance_prov_model_SpecializationOf(s) {
                            return this.__parent.newSpecializationOf(s);
                        }
                        doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(s) {
                            return this.__parent.newQualifiedSpecializationOf(s.getId(), s.getSpecificEntity(), s.getGeneralEntity(), this.__parent.getAttributes(s));
                        }
                        doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(s) {
                            return this.__parent.newQualifiedHadMember(s.getId(), s.getCollection(), s.getEntity(), this.__parent.getAttributes(s));
                        }
                        doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(s) {
                            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                        doAction$org_openprovenance_prov_model_WasInformedBy(s) {
                            return this.__parent.newWasInformedBy(s);
                        }
                        doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(s, provUtilities) {
                            throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable', 'java.lang.UnsupportedOperationException', 'java.lang.Object', 'java.lang.RuntimeException', 'java.lang.Exception'] });
                        }
                        /**
                         *
                         * @param {*} s
                         * @param {org.openprovenance.prov.model.ProvUtilities} provUtilities
                         * @return {*}
                         */
                        doAction(s, provUtilities) {
                            if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || s === null) && ((provUtilities != null && provUtilities instanceof org.openprovenance.prov.model.ProvUtilities) || provUtilities === null)) {
                                return this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(s, provUtilities);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_Activity(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_Used(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasStartedBy(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_Agent(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_AlternateOf(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasAssociatedWith(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasAttributedTo(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasInfluencedBy(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasDerivedFrom(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_DictionaryMembership(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasEndedBy(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_Entity(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasGeneratedBy(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_HadMember(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_MentionOf(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_SpecializationOf(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_WasInformedBy(s);
                            }
                            else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || s === null) && provUtilities === undefined) {
                                return this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(s);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(s) {
                            return this.__parent.newQualifiedAlternateOf(s.getId(), s.getAlternate1(), s.getAlternate2(), this.__parent.getAttributes(s));
                        }
                    }
                    ProvFactory.Cloner = Cloner;
                    Cloner["__class"] = "org.openprovenance.prov.model.ProvFactory.Cloner";
                    Cloner["__interfaces"] = ["org.openprovenance.prov.model.StatementActionValue"];
                })(ProvFactory = model.ProvFactory || (model.ProvFactory = {}));
            })(model = prov.model || (prov.model = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
