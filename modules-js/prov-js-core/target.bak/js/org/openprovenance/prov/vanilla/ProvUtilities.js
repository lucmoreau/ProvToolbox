/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class ProvUtilities extends org.openprovenance.prov.model.ProvUtilities {
                    static QualifiedName_PROV_TYPE_$LI$() { if (ProvUtilities.QualifiedName_PROV_TYPE == null) {
                        ProvUtilities.QualifiedName_PROV_TYPE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_TYPE;
                    } return ProvUtilities.QualifiedName_PROV_TYPE; }
                    static QualifiedName_PROV_LABEL_$LI$() { if (ProvUtilities.QualifiedName_PROV_LABEL == null) {
                        ProvUtilities.QualifiedName_PROV_LABEL = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LABEL;
                    } return ProvUtilities.QualifiedName_PROV_LABEL; }
                    static QualifiedName_PROV_VALUE_$LI$() { if (ProvUtilities.QualifiedName_PROV_VALUE == null) {
                        ProvUtilities.QualifiedName_PROV_VALUE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_VALUE;
                    } return ProvUtilities.QualifiedName_PROV_VALUE; }
                    static QualifiedName_PROV_LOCATION_$LI$() { if (ProvUtilities.QualifiedName_PROV_LOCATION == null) {
                        ProvUtilities.QualifiedName_PROV_LOCATION = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LOCATION;
                    } return ProvUtilities.QualifiedName_PROV_LOCATION; }
                    static QualifiedName_PROV_ROLE_$LI$() { if (ProvUtilities.QualifiedName_PROV_ROLE == null) {
                        ProvUtilities.QualifiedName_PROV_ROLE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_ROLE;
                    } return ProvUtilities.QualifiedName_PROV_ROLE; }
                    static PROV_LABEL_URI_$LI$() { if (ProvUtilities.PROV_LABEL_URI == null) {
                        ProvUtilities.PROV_LABEL_URI = ProvUtilities.QualifiedName_PROV_LABEL_$LI$().getUri();
                    } return ProvUtilities.PROV_LABEL_URI; }
                    static PROV_TYPE_URI_$LI$() { if (ProvUtilities.PROV_TYPE_URI == null) {
                        ProvUtilities.PROV_TYPE_URI = ProvUtilities.QualifiedName_PROV_TYPE_$LI$().getUri();
                    } return ProvUtilities.PROV_TYPE_URI; }
                    static PROV_LOCATION_URI_$LI$() { if (ProvUtilities.PROV_LOCATION_URI == null) {
                        ProvUtilities.PROV_LOCATION_URI = ProvUtilities.QualifiedName_PROV_LOCATION_$LI$().getUri();
                    } return ProvUtilities.PROV_LOCATION_URI; }
                    static PROV_ROLE_URI_$LI$() { if (ProvUtilities.PROV_ROLE_URI == null) {
                        ProvUtilities.PROV_ROLE_URI = ProvUtilities.QualifiedName_PROV_ROLE_$LI$().getUri();
                    } return ProvUtilities.PROV_ROLE_URI; }
                    static PROV_VALUE_URI_$LI$() { if (ProvUtilities.PROV_VALUE_URI == null) {
                        ProvUtilities.PROV_VALUE_URI = ProvUtilities.QualifiedName_PROV_VALUE_$LI$().getUri();
                    } return ProvUtilities.PROV_VALUE_URI; }
                    split$java_util_Collection(attributes) {
                        const labels = ([]);
                        const types = ([]);
                        const values = ([]);
                        const locations = ([]);
                        const roles = ([]);
                        const others = ({});
                        this.split$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Map(attributes, labels, types, values, locations, roles, others);
                        const result = ({});
                        /* put */ ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(result, ProvUtilities.QualifiedName_PROV_LABEL_$LI$(), labels.slice(0));
                        /* put */ ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(result, ProvUtilities.QualifiedName_PROV_TYPE_$LI$(), types.slice(0));
                        /* put */ ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(result, ProvUtilities.QualifiedName_PROV_VALUE_$LI$(), values.slice(0));
                        /* put */ ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(result, ProvUtilities.QualifiedName_PROV_LOCATION_$LI$(), locations.slice(0));
                        /* put */ ((m, k, v) => { if (m.entries == null)
                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                m.entries[i].value = v;
                                return;
                            } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(result, ProvUtilities.QualifiedName_PROV_ROLE_$LI$(), roles.slice(0));
                        {
                            let array231 = /* entrySet */ ((m) => { if (m.entries == null)
                                m.entries = []; return m.entries; })(others);
                            for (let index230 = 0; index230 < array231.length; index230++) {
                                let entry = array231[index230];
                                {
                                    /* put */ ((m, k, v) => { if (m.entries == null)
                                        m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                        if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                            m.entries[i].value = v;
                                            return;
                                        } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(result, entry.getKey(), entry.getValue().slice(0));
                                }
                            }
                        }
                        return result;
                    }
                    split$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Map(attributes, labels, types, values, locations, roles, others) {
                        for (let index232 = 0; index232 < attributes.length; index232++) {
                            let attribute = attributes[index232];
                            {
                                switch ((attribute.getKind())) {
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                                        /* add */ (types.push(attribute) > 0);
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                                        /* add */ (labels.push(attribute) > 0);
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                                        /* add */ (roles.push(attribute) > 0);
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                                        /* add */ (locations.push(attribute) > 0);
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                                        /* add */ (values.push(attribute) > 0);
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_KEY:
                                        break;
                                    case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                                        const other = attribute;
                                        const name = other.getElementName();
                                        let some = ((m, k) => { if (m.entries == null)
                                            m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                            if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                return m.entries[i].value;
                                            } return null; })(others, name);
                                        if (some == null) {
                                            some = ([]);
                                            /* put */ ((m, k, v) => { if (m.entries == null)
                                                m.entries = []; for (let i = 0; i < m.entries.length; i++)
                                                if (m.entries[i].key == null && k == null || m.entries[i].key.equals != null && m.entries[i].key.equals(k) || m.entries[i].key === k) {
                                                    m.entries[i].value = v;
                                                    return;
                                                } m.entries.push({ key: k, value: v, getKey: function () { return this.key; }, getValue: function () { return this.value; } }); })(others, name, some);
                                        }
                                        /* add */ (some.push(other) > 0);
                                        break;
                                }
                            }
                        }
                    }
                    split(attributes, labels, types, values, locations, roles, others) {
                        if (((attributes != null && (attributes instanceof Array)) || attributes === null) && ((labels != null && (labels instanceof Array)) || labels === null) && ((types != null && (types instanceof Array)) || types === null) && ((values != null && (values instanceof Array)) || values === null) && ((locations != null && (locations instanceof Array)) || locations === null) && ((roles != null && (roles instanceof Array)) || roles === null) && ((others != null && (others instanceof Object)) || others === null)) {
                            return this.split$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Map(attributes, labels, types, values, locations, roles, others);
                        }
                        else if (((attributes != null && (attributes instanceof Array)) || attributes === null) && labels === undefined && types === undefined && values === undefined && locations === undefined && roles === undefined && others === undefined) {
                            return this.split$java_util_Collection(attributes);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    distribute(qn, attributes, labels, values, locations, types, roles, others) {
                        const uri = qn.getUri();
                        if (ProvUtilities.PROV_LABEL_URI_$LI$() === uri) {
                            for (let index233 = 0; index233 < attributes.length; index233++) {
                                let attr = attributes[index233];
                                {
                                    const ls = attr.getValue();
                                    /* add */ (labels.push(ls) > 0);
                                }
                            }
                            return;
                        }
                        if (ProvUtilities.PROV_TYPE_URI_$LI$() === uri) {
                            for (let index234 = 0; index234 < attributes.length; index234++) {
                                let attr = attributes[index234];
                                {
                                    /* add */ (types.push(attr) > 0);
                                }
                            }
                            return;
                        }
                        if (ProvUtilities.PROV_LOCATION_URI_$LI$() === uri) {
                            for (let index235 = 0; index235 < attributes.length; index235++) {
                                let attr = attributes[index235];
                                {
                                    /* add */ (locations.push(attr) > 0);
                                }
                            }
                            return;
                        }
                        if (ProvUtilities.PROV_VALUE_URI_$LI$() === uri) {
                            for (let index236 = 0; index236 < attributes.length; index236++) {
                                let attr = attributes[index236];
                                {
                                    /* add */ (values.push(attr) > 0);
                                }
                            }
                            return;
                        }
                        if (ProvUtilities.PROV_ROLE_URI_$LI$() === uri) {
                            for (let index237 = 0; index237 < attributes.length; index237++) {
                                let attr = attributes[index237];
                                {
                                    /* add */ (roles.push(attr) > 0);
                                }
                            }
                            return;
                        }
                        for (let index238 = 0; index238 < attributes.length; index238++) {
                            let attr = attributes[index238];
                            {
                                /* add */ (others.push(attr) > 0);
                            }
                        }
                    }
                    populateAttributes(attributes, label, location, type, role, other, value) {
                        let foundValue = false;
                        if (attributes != null) {
                            for (let index239 = 0; index239 < attributes.length; index239++) {
                                let attribute = attributes[index239];
                                {
                                    switch ((attribute.getKind())) {
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                                            /* add */ (type.push(attribute) > 0);
                                            break;
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                                            /* add */ (label.push(attribute.getValue()) > 0);
                                            break;
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                                            /* add */ (role.push(attribute) > 0);
                                            break;
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                                            /* add */ (location.push(attribute) > 0);
                                            break;
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                                            if (!foundValue) {
                                                foundValue = true;
                                                value[0] = attribute;
                                            }
                                            break;
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_KEY:
                                            break;
                                        case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                                            /* add */ (other.push(attribute) > 0);
                                    }
                                }
                            }
                        }
                    }
                }
                vanilla.ProvUtilities = ProvUtilities;
                ProvUtilities["__class"] = "org.openprovenance.prov.vanilla.ProvUtilities";
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
