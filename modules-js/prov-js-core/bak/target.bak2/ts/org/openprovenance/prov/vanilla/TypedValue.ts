/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class TypedValue implements org.openprovenance.prov.model.TypedValue, org.openprovenance.apache.commons.lang.builder.Equals, org.openprovenance.apache.commons.lang.builder.ToString, org.openprovenance.apache.commons.lang.builder.HashCode {
        static QualifiedName_XSD_STRING: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_XSD_STRING_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_XSD_STRING == null) { TypedValue.QualifiedName_XSD_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_STRING; }  return TypedValue.QualifiedName_XSD_STRING; }

        static QualifiedName_PROV_TYPE: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_TYPE_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_TYPE == null) { TypedValue.QualifiedName_PROV_TYPE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_TYPE; }  return TypedValue.QualifiedName_PROV_TYPE; }

        static QualifiedName_PROV_LABEL: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_LABEL_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_LABEL == null) { TypedValue.QualifiedName_PROV_LABEL = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LABEL; }  return TypedValue.QualifiedName_PROV_LABEL; }

        static QualifiedName_PROV_VALUE: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_VALUE_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_VALUE == null) { TypedValue.QualifiedName_PROV_VALUE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_VALUE; }  return TypedValue.QualifiedName_PROV_VALUE; }

        static QualifiedName_PROV_LOCATION: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_LOCATION_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_LOCATION == null) { TypedValue.QualifiedName_PROV_LOCATION = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LOCATION; }  return TypedValue.QualifiedName_PROV_LOCATION; }

        static QualifiedName_PROV_ROLE: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_ROLE_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_ROLE == null) { TypedValue.QualifiedName_PROV_ROLE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_ROLE; }  return TypedValue.QualifiedName_PROV_ROLE; }

        static QualifiedName_XSD_HEX_BINARY: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_XSD_HEX_BINARY_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_XSD_HEX_BINARY == null) { TypedValue.QualifiedName_XSD_HEX_BINARY = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_HEX_BINARY; }  return TypedValue.QualifiedName_XSD_HEX_BINARY; }

        static QualifiedName_XSD_BASE64_BINARY: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_XSD_BASE64_BINARY_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_XSD_BASE64_BINARY == null) { TypedValue.QualifiedName_XSD_BASE64_BINARY = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().XSD_BASE64_BINARY; }  return TypedValue.QualifiedName_XSD_BASE64_BINARY; }

        static QualifiedName_PROV_QUALIFIEDNAME: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_QUALIFIEDNAME_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_QUALIFIEDNAME == null) { TypedValue.QualifiedName_PROV_QUALIFIEDNAME = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_QUALIFIED_NAME; }  return TypedValue.QualifiedName_PROV_QUALIFIEDNAME; }

        static QualifiedName_PROV_LANG_STRING: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_LANG_STRING_$LI$(): org.openprovenance.prov.model.QualifiedName { if (TypedValue.QualifiedName_PROV_LANG_STRING == null) { TypedValue.QualifiedName_PROV_LANG_STRING = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LANG_STRING; }  return TypedValue.QualifiedName_PROV_LANG_STRING; }

        public static castToStringOrLangStringOrQualifiedName(value: any, type: org.openprovenance.prov.model.QualifiedName): any {
            return (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) ? value : ((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) ? value : ((/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(TypedValue.QualifiedName_XSD_STRING_$LI$(),type))) ? new org.openprovenance.prov.vanilla.LangString(value.toString(), null) : value.toString()));
        }

        value: any;

        type: org.openprovenance.prov.model.QualifiedName;

        valueAsJavaObject: any;

        public constructor(type?: any, value?: any, _dummy?: any, ignore?: any) {
            if (((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null) && ((_dummy != null) || _dummy === null) && ((ignore != null) || ignore === null)) {
                let __args = arguments;
                if (this.value === undefined) { this.value = null; } 
                if (this.type === undefined) { this.type = null; } 
                if (this.valueAsJavaObject === undefined) { this.valueAsJavaObject = null; } 
                this.type = type;
                this.setValueFromObject$java_lang_Object(value);
            } else if (((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null) && ((value != null) || value === null) && _dummy === undefined && ignore === undefined) {
                let __args = arguments;
                if (this.value === undefined) { this.value = null; } 
                if (this.type === undefined) { this.type = null; } 
                if (this.valueAsJavaObject === undefined) { this.valueAsJavaObject = null; } 
                this.type = type;
                this.value = value;
            } else if (type === undefined && value === undefined && _dummy === undefined && ignore === undefined) {
                let __args = arguments;
                if (this.value === undefined) { this.value = null; } 
                if (this.type === undefined) { this.type = null; } 
                if (this.valueAsJavaObject === undefined) { this.valueAsJavaObject = null; } 
            } else throw new Error('invalid overload');
        }

        public convertValueToObject(vconv: org.openprovenance.prov.model.ValueConverter): any {
            if (this.valueAsJavaObject == null){
                if (typeof this.value === 'string'){
                    this.valueAsJavaObject = vconv.convertToJava(this.getType(), <string>this.value);
                } else {
                    this.valueAsJavaObject = this.value;
                }
            }
            return this.valueAsJavaObject;
        }

        /**
         * 
         * @return {*}
         */
        public getType(): org.openprovenance.prov.model.QualifiedName {
            return this.type;
        }

        /**
         * 
         * @return {*}
         */
        public getValue(): any {
            return this.value;
        }

        /**
         * 
         * @return {*}
         */
        public getConvertedValue(): any {
            return this.valueAsJavaObject;
        }

        public getQualifiedName(kind: org.openprovenance.prov.model.Attribute.AttributeKind): org.openprovenance.prov.model.QualifiedName {
            switch((kind)) {
            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                return TypedValue.QualifiedName_PROV_TYPE_$LI$();
            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                return TypedValue.QualifiedName_PROV_LABEL_$LI$();
            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                return TypedValue.QualifiedName_PROV_VALUE_$LI$();
            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                return TypedValue.QualifiedName_PROV_LOCATION_$LI$();
            case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                return TypedValue.QualifiedName_PROV_ROLE_$LI$();
            case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
            default:
                return null;
            }
        }

        public getAttributeKind(q: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute.AttributeKind {
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(q,TypedValue.QualifiedName_PROV_TYPE_$LI$())))return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE;
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(q,TypedValue.QualifiedName_PROV_LABEL_$LI$())))return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL;
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(q,TypedValue.QualifiedName_PROV_VALUE_$LI$())))return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE;
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(q,TypedValue.QualifiedName_PROV_LOCATION_$LI$())))return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION;
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(q,TypedValue.QualifiedName_PROV_ROLE_$LI$())))return org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE;
            return org.openprovenance.prov.model.Attribute.AttributeKind.OTHER;
        }

        /**
         * 
         * @param {*} value
         */
        public setType(value: org.openprovenance.prov.model.QualifiedName) {
            this.type = value;
        }

        public setValue$org_openprovenance_prov_model_LangString(value: org.openprovenance.prov.model.LangString) {
            this.value = value;
        }

        /**
         * 
         * @param {*} value
         */
        public setValue(value?: any) {
            if (((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)) || value === null)) {
                return <any>this.setValue$org_openprovenance_prov_model_LangString(value);
            } else if (((value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || value === null)) {
                return <any>this.setValue$org_openprovenance_prov_model_QualifiedName(value);
            } else if (((typeof value === 'string') || value === null)) {
                return <any>this.setValue$java_lang_String(value);
            } else throw new Error('invalid overload');
        }

        public setValue$org_openprovenance_prov_model_QualifiedName(value: org.openprovenance.prov.model.QualifiedName) {
            this.value = value;
        }

        public setValue$java_lang_String(value: string) {
            if ((this.type != null) && /* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(TypedValue.QualifiedName_PROV_QUALIFIEDNAME_$LI$(),this.type))){
                console.info("HACK: deserializing value " + value + "type " + this.type);
                const ns: org.openprovenance.prov.model.Namespace = null;
                this.setValue$org_openprovenance_prov_model_QualifiedName(ns.stringToQualifiedName$java_lang_String$org_openprovenance_prov_model_ProvFactory$boolean(value, org.openprovenance.prov.vanilla.ProvFactory.getFactory(), false));
                throw Object.defineProperty(new Error("we should not be here with " + value), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            } else {
                this.value = value;
            }
        }

        public setValueFromObject$java_lang_Object(anObject: any) {
            if ((anObject != null) && (this.value == null)){
                if (anObject != null && (anObject.constructor != null && anObject.constructor["__interfaces"] != null && anObject.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)){
                    this.setValue$org_openprovenance_prov_model_QualifiedName(<org.openprovenance.prov.model.QualifiedName><any>anObject);
                } else if (anObject != null && (anObject.constructor != null && anObject.constructor["__interfaces"] != null && anObject.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                    this.setValue$org_openprovenance_prov_model_LangString(<org.openprovenance.prov.model.LangString><any>anObject);
                } else if (anObject != null && anObject instanceof <any>Array && (anObject.length == 0 || anObject[0] == null ||typeof anObject[0] === 'number')){
                    this.setValueFromObject$byte_A(<number[]>anObject);
                } else if (anObject != null && (anObject.constructor != null && anObject.constructor["__interfaces"] != null && anObject.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)){
                    this.setValueFromObject$org_w3c_dom_Node(<org.w3c.dom.Node><any>anObject);
                } else {
                    this.setValue$java_lang_String(anObject.toString());
                }
            }
            this.valueAsJavaObject = anObject;
        }

        public setValueFromObject$byte_A(bytes: number[]) {
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(this.type,TypedValue.QualifiedName_XSD_BASE64_BINARY_$LI$()))){
                this.setValue$java_lang_String(org.openprovenance.prov.vanilla.ProvFactory.getFactory().base64Encoding(bytes));
            } else if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(this.type,TypedValue.QualifiedName_XSD_HEX_BINARY_$LI$()))){
                this.setValue$java_lang_String(org.openprovenance.prov.vanilla.ProvFactory.getFactory().hexEncoding(bytes));
            }
        }

        /**
         * Converts a byte array in base64 or hexadecimal according to specified type.
         * 
         * @param {byte[]} bytes array of bytes to convert
         * @private
         */
        public setValueFromObject(bytes?: any) {
            if (((bytes != null && bytes instanceof <any>Array && (bytes.length == 0 || bytes[0] == null ||(typeof bytes[0] === 'number'))) || bytes === null)) {
                return <any>this.setValueFromObject$byte_A(bytes);
            } else if (((bytes != null && (bytes.constructor != null && bytes.constructor["__interfaces"] != null && bytes.constructor["__interfaces"].indexOf("org.w3c.dom.Node") >= 0)) || bytes === null)) {
                return <any>this.setValueFromObject$org_w3c_dom_Node(bytes);
            } else if (((bytes != null) || bytes === null)) {
                return <any>this.setValueFromObject$java_lang_Object(bytes);
            } else throw new Error('invalid overload');
        }

        /*private*/ setValueFromObject$org_w3c_dom_Node(n: org.w3c.dom.Node) {
            org.openprovenance.prov.model.DOMProcessing.trimNode$org_w3c_dom_Node(n);
            try {
                this.setValue$java_lang_String(org.openprovenance.prov.model.DOMProcessing.writeToString(n));
            } catch(__e) {
                if(__e != null && __e instanceof <any>javax.xml.transform.TransformerConfigurationException) {
                    const e: javax.xml.transform.TransformerConfigurationException = <javax.xml.transform.TransformerConfigurationException>__e;
                    this.setValue$java_lang_String(n.toString());

                }
                if(__e != null && __e instanceof <any>javax.xml.transform.TransformerException) {
                    const e: javax.xml.transform.TransformerException = <javax.xml.transform.TransformerException>__e;
                    this.setValue$java_lang_String(n.toString());

                }
            }
        }

        public equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object: any, equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder) {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.TypedValue)){
                equalsBuilder.appendSuper(false);
                return;
            }
            if (this === object){
                return;
            }
            const that: TypedValue = (<TypedValue>object);
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getValue(), that.getValue());
            equalsBuilder.append$java_lang_Object$java_lang_Object(this.getType(), that.getType());
        }

        public equals(object?: any, equalsBuilder?: any) {
            if (((object != null) || object === null) && ((equalsBuilder != null && equalsBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.EqualsBuilder) || equalsBuilder === null)) {
                return <any>this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            } else if (((object != null) || object === null) && equalsBuilder === undefined) {
                return <any>this.equals$java_lang_Object(object);
            } else throw new Error('invalid overload');
        }

        public equals$java_lang_Object(object: any): boolean {
            if (!(object != null && object instanceof <any>org.openprovenance.prov.vanilla.TypedValue)){
                return false;
            }
            if (this === object){
                return true;
            }
            const equalsBuilder: org.openprovenance.apache.commons.lang.builder.EqualsBuilder = new org.openprovenance.apache.commons.lang.builder.EqualsBuilder();
            this.equals$java_lang_Object$org_openprovenance_apache_commons_lang_builder_EqualsBuilder(object, equalsBuilder);
            return equalsBuilder.isEquals();
        }

        public hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder: org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) {
            hashCodeBuilder.append$java_lang_Object(this.getValue());
            hashCodeBuilder.append$java_lang_Object(this.getType());
        }

        public hashCode(hashCodeBuilder?: any) {
            if (((hashCodeBuilder != null && hashCodeBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.HashCodeBuilder) || hashCodeBuilder === null)) {
                return <any>this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
            } else if (hashCodeBuilder === undefined) {
                return <any>this.hashCode$();
            } else throw new Error('invalid overload');
        }

        public hashCode$(): number {
            const hashCodeBuilder: org.openprovenance.apache.commons.lang.builder.HashCodeBuilder = new org.openprovenance.apache.commons.lang.builder.HashCodeBuilder();
            this.hashCode$org_openprovenance_apache_commons_lang_builder_HashCodeBuilder(hashCodeBuilder);
            return hashCodeBuilder.toHashCode();
        }

        public toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder: org.openprovenance.apache.commons.lang.builder.ToStringBuilder) {
            {
                let theValue: any;
                theValue = this.getValue();
                toStringBuilder.append$java_lang_String$java_lang_Object("value", theValue);
                toStringBuilder.append$java_lang_String$java_lang_Object("DEBUG_type1", (<any>theValue.constructor));
            };
            {
                let theType: org.openprovenance.prov.model.QualifiedName;
                theType = this.getType();
                toStringBuilder.append$java_lang_String$java_lang_Object("type", theType);
            };
        }

        public toString(toStringBuilder?: any) {
            if (((toStringBuilder != null && toStringBuilder instanceof <any>org.openprovenance.apache.commons.lang.builder.ToStringBuilder) || toStringBuilder === null)) {
                return <any>this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
            } else if (toStringBuilder === undefined) {
                return <any>this.toString$();
            } else throw new Error('invalid overload');
        }

        public toString$(): string {
            const toStringBuilder: org.openprovenance.apache.commons.lang.builder.ToStringBuilder = new org.openprovenance.apache.commons.lang.builder.ToStringBuilder(this);
            this.toString$org_openprovenance_apache_commons_lang_builder_ToStringBuilder(toStringBuilder);
            return toStringBuilder.toString();
        }

        public getLanguageString(): string {
            if (this.getValue() != null && (this.getValue().constructor != null && this.getValue().constructor["__interfaces"] != null && this.getValue().constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                return (<org.openprovenance.prov.model.LangString><any>this.getValue()).getLang();
            }
            return null;
        }

        public getTypeString(): string {
            if ((/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(TypedValue.QualifiedName_XSD_STRING_$LI$(),this.type))) || (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(TypedValue.QualifiedName_PROV_LANG_STRING_$LI$(),this.type)))){
                return null;
            }
            return TypedValue.prnt(this.type);
        }

        static prnt(qn: org.openprovenance.prov.model.QualifiedName): string {
            return qn.getPrefix() + ":" + qn.getLocalPart();
        }
    }
    TypedValue["__class"] = "org.openprovenance.prov.vanilla.TypedValue";
    TypedValue["__interfaces"] = ["org.openprovenance.prov.model.TypedValue","org.openprovenance.apache.commons.lang.builder.HashCode","org.openprovenance.apache.commons.lang.builder.Equals","org.openprovenance.apache.commons.lang.builder.ToString"];


}

