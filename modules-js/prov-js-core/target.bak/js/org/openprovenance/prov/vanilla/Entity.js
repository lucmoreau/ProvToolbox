/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var prov;
        (function (prov) {
            var vanilla;
            (function (vanilla) {
                class Entity {
                    constructor(id, attributes) {
                      //  console.log(id)
                      //  console.log(attributes)
                        if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                            let __args = arguments;
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.value = null;
                            this.setId(id);
                            this.location = ([]);
                            this.type = ([]);
                            this.other = ([]);
                            const valueHolder = [null];
                            valueHolder[0] = null;
                            Entity.u_$LI$().populateAttributes(attributes, this.labels, this.location, this.type, ([]), this.other, valueHolder);
                            this.value = valueHolder[0];
                        }
                        else if (id === undefined && attributes === undefined) {
                            let __args = arguments;
                            if (this.id === undefined) {
                                this.id = null;
                            }
                            this.labels = ([]);
                            this.location = ([]);
                            this.other = ([]);
                            this.type = ([]);
                            this.value = null;
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    static QUALIFIED_NAME_XSD_STRING_$LI$() { if (Entity.QUALIFIED_NAME_XSD_STRING == null) {
                        Entity.QUALIFIED_NAME_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING;
                    } return Entity.QUALIFIED_NAME_XSD_STRING; }
                    static u_$LI$() { if (Entity.u == null) {
                        Entity.u = new org.openprovenance.prov.vanilla.ProvUtilities();
                    } return Entity.u; }
                    setValue(o) {
                        this.value = o;
                    }
                    getValue() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.value, null);
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getId() {
                        return /* orElse */ ((v, v2) => v == null ? v2 : v)(this.id, null);
                    }
                    /**
                     *
                     * @return {org.openprovenance.prov.model.StatementOrBundle.Kind}
                     */
                    getKind() {
                        return StatementOrBundle.Kind.PROV_ENTITY;
                    }
                    /**
                     *
                     * @param {*} value
                     */
                    setId(value) {
                        this.id = value;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getLabel() {
                        return this.labels;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getLocation() {
                        return this.location;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getType() {
                        return this.type;
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getOther() {
                        return this.other;
                    }
                    equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Entity)) {
                            equalsBuilder.appendSuper(false);
                            return;
                        }
                        if (this === object) {
                            return;
                        }
                        const that = object;
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getId(), that.getId());
                        equalsBuilder.append$java_lang_Object$java_lang_Object(this.getIndexedAttributes(), that.getIndexedAttributes());
                    }
                    equals(object, equalsBuilder) {
                        if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                            return this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
                        }
                        else if (((object != null) || object === null) && equalsBuilder === undefined) {
                            return this.equals$java_lang_Object(object);
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    equals$java_lang_Object(object) {
                        if (!(object != null && object instanceof org.openprovenance.prov.vanilla.Entity)) {
                            return false;
                        }
                        if (this === object) {
                            return true;
                        }
                        const equalsBuilder = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
                        this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
                        return equalsBuilder.isEquals();
                    }
                    hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder) {
                        hashCodeBuilder.append$java_lang_Object(this.getId());
                        hashCodeBuilder.append$java_lang_Object(this.getIndexedAttributes());
                    }
                    hashCode(hashCodeBuilder) {
                        if (((hashCodeBuilder != null && hashCodeBuilder instanceof org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) || hashCodeBuilder === null)) {
                            return this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
                        }
                        else if (hashCodeBuilder === undefined) {
                            return this.hashCode$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    hashCode$() {
                        const hashCodeBuilder = new org.openprovenance.apache.commons.lang.builder.HashCodeBuilder();
                        this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
                        return hashCodeBuilder.toHashCode();
                    }
                    toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder) {
                        {
                            let theId;
                            theId = this.getId();
                            toStringBuilder.append$java_lang_String$java_lang_Object("id", theId);
                        }
                        ;
                        {
                            let theAttributes;
                            theAttributes = this.getIndexedAttributes();
                            toStringBuilder.append$java_lang_String$java_lang_Object("attributes", theAttributes);
                        }
                        ;
                    }
                    toString(toStringBuilder) {
                        if (((toStringBuilder != null && toStringBuilder instanceof org.openprovenance.apache.commons.lang.builder.ToStringBuilder) || toStringBuilder === null)) {
                            return this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
                        }
                        else if (toStringBuilder === undefined) {
                            return this.toString$();
                        }
                        else
                            throw new Error('invalid overload');
                    }
                    toString$() {
                        const toStringBuilder = new org.openprovenance.apache.commons.lang.builder.ToStringBuilder(this);
                        this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
                        return toStringBuilder.toString();
                    }
                    /**
                     *
                     * @return {*[]}
                     */
                    getAttributes() {
                        const result = ([]);//LUC remove streams
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getLabel().map((s) => new org.openprovenance.prov.vanilla.Label(Entity.QUALIFIED_NAME_XSD_STRING_$LI$(), s)));
                        if ( /* isPresent */(this.value != null)) /* add */
                            (result.push(this.getValue()) > 0);
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getType());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getLocation());
                        /* addAll */ ((l1, l2) => l1.push.apply(l1, l2))(result, this.getOther().map((o) => o));
                        return result;
                    }
                    setIndexedAttributes(qn, attributes) {
                        const values_discard = ([]);
                        const roles_discard = ([]);
                        Entity.u_$LI$().distribute(qn, attributes, this.getLabel(), values_discard, this.getLocation(), this.getType(), roles_discard, this.getOther());
                        if (!(values_discard.length == 0))
                            this.value = (v => { if (v == null)
                                throw new Error('value is null'); return v; })(/* get */ values_discard[0]);
                    }
                    /**
                     *
                     * @return {*}
                     */
                    getIndexedAttributes() {
                        return Entity.u_$LI$().split$java_util_Collection(this.getAttributes());
                    }
                }
                vanilla.Entity = Entity;
                Entity["__class"] = "org.openprovenance.prov.vanilla.Entity";
                Entity["__interfaces"] = ["org.openprovenance.prov.model.HasOther", "org.openprovenance.prov.model.HasType", "org.openprovenance.prov.model.HasValue", "org.openprovenance.prov.model.HasLocation", "org.openprovenance.prov.model.StatementOrBundle", "org.openprovenance.apache.commons.lang.builder.ToString", "org.openprovenance.prov.model.Statement", "org.openprovenance.prov.model.Entity", "org.openprovenance.prov.model.Element", "org.openprovenance.prov.vanilla.HasAttributes", "org.openprovenance.apache.commons.lang.builder.HashCode", "org.openprovenance.prov.model.HasLabel", "org.openprovenance.apache.commons.lang.builder.Equals", "org.openprovenance.prov.model.Identifiable"];
            })(vanilla = prov.vanilla || (prov.vanilla = {}));
        })(prov = openprovenance.prov || (openprovenance.prov = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
