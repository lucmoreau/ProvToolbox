/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class ProvFactory extends org.openprovenance.prov.model.ProvFactory implements org.openprovenance.prov.model.LiteralConstructor, org.openprovenance.prov.model.ModelConstructor, org.openprovenance.prov.model.ModelConstructorExtension, org.openprovenance.prov.model.AtomConstructor {
        static logger: org.apache.log4j.Logger; public static logger_$LI$(): org.apache.log4j.Logger { if (ProvFactory.logger == null) { ProvFactory.logger = org.apache.log4j.Logger.getLogger(ProvFactory); }  return ProvFactory.logger; }

        static oFactory: ProvFactory; public static oFactory_$LI$(): ProvFactory { if (ProvFactory.oFactory == null) { ProvFactory.oFactory = new ProvFactory(); }  return ProvFactory.oFactory; }

        public static getFactory(): ProvFactory {
            return ProvFactory.oFactory_$LI$();
        }

        mc: org.openprovenance.prov.model.ModelConstructor;

        ac: org.openprovenance.prov.model.AtomConstructor;

        public constructor(of?: any, mc?: any) {
            if (((of != null && (of.constructor != null && of.constructor["__interfaces"] != null && of.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ObjectFactory") >= 0)) || of === null) && ((mc != null && (mc.constructor != null && mc.constructor["__interfaces"] != null && mc.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ModelConstructor") >= 0)) || mc === null)) {
                let __args = arguments;
                super(of);
                if (this.mc === undefined) { this.mc = null; } 
                if (this.ac === undefined) { this.ac = null; } 
                this.mc = mc;
                this.ac = <org.openprovenance.prov.model.AtomConstructor><any>mc;
            } else if (((of != null && (of.constructor != null && of.constructor["__interfaces"] != null && of.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ObjectFactory") >= 0)) || of === null) && mc === undefined) {
                let __args = arguments;
                super(of);
                if (this.mc === undefined) { this.mc = null; } 
                if (this.ac === undefined) { this.ac = null; } 
                this.mc = new org.openprovenance.prov.vanilla.ModelConstructor();
                this.ac = <org.openprovenance.prov.model.AtomConstructor><any>this.mc;
            } else if (of === undefined && mc === undefined) {
                let __args = arguments;
                super(null);
                if (this.mc === undefined) { this.mc = null; } 
                if (this.ac === undefined) { this.ac = null; } 
                this.mc = new org.openprovenance.prov.vanilla.ModelConstructor();
                this.ac = <org.openprovenance.prov.model.AtomConstructor><any>this.mc;
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @return {*}
         */
        public getSerializer(): org.openprovenance.prov.model.ProvSerialiser {
            return null;
        }

        public newAttribute(namespace?: any, localName?: any, prefix?: any, value?: any, type?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof localName === 'string') || localName === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return super.newAttribute(namespace, localName, prefix, value, type);
            } else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((localName != null) || localName === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                return <any>this.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix);
            } else if (((typeof namespace === 'number') || namespace === null) && ((localName != null) || localName === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                return <any>this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix);
            } else throw new Error('invalid overload');
        }

        public newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName: org.openprovenance.prov.model.QualifiedName, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute {
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(elementName,this.getName().PROV_LOCATION))){
                return this.newLocation(value, type);
            }
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(elementName,this.getName().PROV_TYPE))){
                return this.newType(value, type);
            }
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(elementName,this.getName().PROV_VALUE))){
                return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type);
            }
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(elementName,this.getName().PROV_ROLE))){
                return this.newRole(value, type);
            }
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(elementName,this.getName().PROV_LABEL))){
                return this.newLabel(value, type);
            }
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(elementName,this.getName().PROV_KEY))){
                Object.defineProperty(new Error("key not there yet"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
            return this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type);
        }

        public newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(kind: org.openprovenance.prov.model.Attribute.AttributeKind, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute {
            switch((kind)) {
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
            throw Object.defineProperty(new Error("Should never be here, unknown " + kind), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace: string, local: string, prefix: string): org.openprovenance.prov.model.QualifiedName {
            return this.mc['newQualifiedName$java_lang_String$java_lang_String$java_lang_String'](namespace, local, prefix);
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace: string, local: string, prefix: string, flag: org.openprovenance.prov.vanilla.ProvUtilities.BuildFlag): org.openprovenance.prov.model.QualifiedName {
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
        public newQualifiedName(namespace?: any, local?: any, prefix?: any, flag?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof flag === 'number') || flag === null)) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag);
            } else if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && flag === undefined) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
            } else if (((namespace != null && namespace instanceof <any>javax.xml.namespace.QName) || namespace === null) && local === undefined && prefix === undefined && flag === undefined) {
                return <any>this.newQualifiedName$javax_xml_namespace_QName(namespace);
            } else throw new Error('invalid overload');
        }

        public newInternationalizedString$java_lang_String$java_lang_String(s: string, lang: string): org.openprovenance.prov.model.LangString {
            const res: org.openprovenance.prov.model.LangString = this.ac['newInternationalizedString$java_lang_String$java_lang_String'](s, lang);
            return res;
        }

        /**
         * 
         * @param {string} s
         * @param {string} lang
         * @return {*}
         */
        public newInternationalizedString(s?: any, lang?: any): any {
            if (((typeof s === 'string') || s === null) && ((typeof lang === 'string') || lang === null)) {
                return <any>this.newInternationalizedString$java_lang_String$java_lang_String(s, lang);
            } else if (((typeof s === 'string') || s === null) && lang === undefined) {
                return <any>this.newInternationalizedString$java_lang_String(s);
            } else throw new Error('invalid overload');
        }

        public newInternationalizedString$java_lang_String(s: string): org.openprovenance.prov.model.LangString {
            const res: org.openprovenance.prov.model.LangString = this.ac['newInternationalizedString$java_lang_String'](s);
            return res;
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newType(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Type {
            if (value == null)return null;
            return this.ac.newType(value, type);
        }

        public newOther(namespace?: any, local?: any, prefix?: any, value?: any, type?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return super.newOther(namespace, local, prefix, value, type);
            } else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((local != null) || local === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                return <any>this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix);
            } else throw new Error('invalid overload');
        }

        public newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName: org.openprovenance.prov.model.QualifiedName, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Other {
            return this.ac.newOther(elementName, value, type);
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newLocation(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Location {
            if (value == null)return null;
            const res: org.openprovenance.prov.model.Location = this.ac.newLocation(value, type);
            return res;
        }

        public newActivity(id?: any, startTime?: any, endTime?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((startTime != null && startTime instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || startTime === null) && ((endTime != null && endTime instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || endTime === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return super.newActivity(id, startTime, endTime, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof startTime === 'string') || startTime === null) && endTime === undefined && attributes === undefined) {
                return <any>this.newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, startTime);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                return <any>this.newActivity$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                return <any>this.newActivity$org_openprovenance_prov_model_Activity(id);
            } else throw new Error('invalid overload');
        }

        public newActivity$org_openprovenance_prov_model_QualifiedName(a: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Activity {
            const res: org.openprovenance.prov.model.Activity = this.mc.newActivity(a, null, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(q: org.openprovenance.prov.model.QualifiedName, label: string): org.openprovenance.prov.model.Activity {
            const attr: org.openprovenance.prov.model.Attribute = this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL, this.newInternationalizedString$java_lang_String(label), this.getName().PROV_LANG_STRING);
            const attributes: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            /* add */(attributes.push(attr)>0);
            const res: org.openprovenance.prov.model.Activity = this.newActivity$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(q, null, null, attributes);
            return res;
        }

        public newEntity$org_openprovenance_prov_model_QualifiedName(a: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Entity {
            const res: org.openprovenance.prov.model.Entity = this.mc.newEntity(a, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id: org.openprovenance.prov.model.QualifiedName, label: string): org.openprovenance.prov.model.Entity {
            const attrs: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            /* add */(attrs.push(this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL, this.newInternationalizedString$java_lang_String(label), this.getName().XSD_STRING))>0);
            const res: org.openprovenance.prov.model.Entity = this.mc.newEntity(id, attrs);
            return res;
        }

        /**
         * Creates a new {@link org.openprovenance.prov.model.Entity} with provided identifier and label
         * @param {*} id a {@link org.openprovenance.prov.model.QualifiedName} for the entity
         * @param {string} label a String for the label property (see {@link HasLabel#getLabel()}
         * @return {*} an object of type {@link org.openprovenance.prov.model.Entity}
         */
        public newEntity(id?: any, label?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof label === 'string') || label === null)) {
                return <any>this.newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, label);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((label != null && (label instanceof Array)) || label === null)) {
                return <any>this.newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, label);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && label === undefined) {
                return <any>this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || id === null) && label === undefined) {
                return <any>this.newEntity$org_openprovenance_prov_model_Entity(id);
            } else throw new Error('invalid overload');
        }

        public newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Entity {
            const res: org.openprovenance.prov.model.Entity = this.mc.newEntity(id, attributes);
            return res;
        }

        public newDocument$(): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.mc.newDocument(null, null, null);
            return res;
        }

        /**
         * 
         * @param {*} o
         * @param {*} type
         * @return {*}
         */
        public newKey(o: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Key {
            throw Object.defineProperty(new Error("newKey not supported"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
        }

        public newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Value {
            if (value == null)return null;
            return this.ac.newValue(value, type);
        }

        public newValue(value?: any, type?: any): any {
            if (((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return <any>this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type);
            } else if (((typeof value === 'string') || value === null) && type === undefined) {
                return <any>this.newValue$java_lang_String(value);
            } else if (((typeof value === 'number') || value === null) && type === undefined) {
                return <any>this.newValue$int(value);
            } else throw new Error('invalid overload');
        }

        public newAgent$org_openprovenance_prov_model_QualifiedName(ag: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.mc.newAgent(ag, <any>([]));
            return res;
        }

        public newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.mc.newAgent(id, attributes);
            return res;
        }

        /**
         * Creates a new {@link org.openprovenance.prov.model.Agent} with provided identifier and attributes
         * @param {*} id a {@link org.openprovenance.prov.model.QualifiedName} for the agent
         * @param {*[]} attributes a collection of {@link Attribute} for the agent
         * @return {*} an object of type {@link org.openprovenance.prov.model.Agent}
         */
        public newAgent(id?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof attributes === 'string') || attributes === null)) {
                return <any>this.newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && attributes === undefined) {
                return <any>this.newAgent$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || id === null) && attributes === undefined) {
                return <any>this.newAgent$org_openprovenance_prov_model_Agent(id);
            } else throw new Error('invalid overload');
        }

        public newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(ag: org.openprovenance.prov.model.QualifiedName, label: string): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.newAgent$org_openprovenance_prov_model_QualifiedName(ag);
            if (label != null)/* add */(res.getLabel().push(this.newInternationalizedString$java_lang_String(label))>0);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.mc.newUsed(id, null, null, null, <any>([]));
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, role: string, eid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const attributes: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            if (role != null)/* add */(attributes.push(this.newRole(role, this.getName().XSD_STRING))>0);
            const res: org.openprovenance.prov.model.Used = this.mc.newUsed(id, aid, eid, null, attributes);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.mc.newUsed(id, activity, entity, null, <any>([]));
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(<org.openprovenance.prov.model.QualifiedName><any>null, activity, entity);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.mc.newUsed(id, activity, entity, time, attributes);
            return res;
        }

        public newUsed(id?: any, activity?: any, entity?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, entity, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((typeof entity === 'string') || entity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                return <any>this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, activity, entity, time);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && attributes === undefined) {
                return <any>this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar(id, activity, entity, time);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && time === undefined && attributes === undefined) {
                return <any>this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, entity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && entity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && activity === undefined && entity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newUsed$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || id === null) && activity === undefined && entity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newUsed$org_openprovenance_prov_model_Used(id);
            } else throw new Error('invalid overload');
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, entity, time, java.util.Collections.EMPTY_LIST);
            return res;
        }

        /**
         * 
         * @param {*} value
         * @param {*} type
         * @return {*}
         */
        public newRole(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Role {
            if (value == null)return null;
            return this.ac.newRole(value, type);
        }

        public newLabel(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Label {
            if (value == null)return null;
            return this.ac.newLabel(value, type);
        }

        public newWasGeneratedBy(id?: any, entity?: any, activity?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return super.newWasGeneratedBy(id, entity, activity, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, activity, time);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || time === null) && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, entity, activity, time);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || id === null) && ((typeof entity === 'string') || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, entity, activity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(id);
            } else throw new Error('invalid overload');
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, eid: org.openprovenance.prov.model.QualifiedName, role: string, aid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasGeneratedBy {
            const attributes: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            if (role != null)/* add */(attributes.push(this.newRole(role, this.getName().XSD_STRING))>0);
            const res: org.openprovenance.prov.model.WasGeneratedBy = this.mc.newWasGeneratedBy(id, eid, aid, null, attributes);
            return res;
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, eid: org.openprovenance.prov.model.QualifiedName, role: string, aid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInvalidatedBy {
            const attributes: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            if (role != null)/* add */(attributes.push(this.newRole(role, this.getName().XSD_STRING))>0);
            const res: org.openprovenance.prov.model.WasInvalidatedBy = this.mc.newWasInvalidatedBy(id, eid, aid, null, attributes);
            return res;
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInvalidatedBy {
            const res: org.openprovenance.prov.model.WasInvalidatedBy = this.mc.newWasInvalidatedBy(id, entity, activity, null, <any>([]));
            return res;
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInvalidatedBy {
            const res: org.openprovenance.prov.model.WasInvalidatedBy = this.mc.newWasInvalidatedBy(id, entity, activity, time, attributes);
            res.setTime(time);
            return res;
        }

        public newWasInvalidatedBy(id?: any, entity?: any, activity?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, activity, time);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && activity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(id);
            } else throw new Error('invalid overload');
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAssociatedWith {
            const res: org.openprovenance.prov.model.WasAssociatedWith = this.mc.newWasAssociatedWith(id, activity, agent, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, ag: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAssociatedWith {
            const res: org.openprovenance.prov.model.WasAssociatedWith = this.mc.newWasAssociatedWith(id, a, ag, plan, attributes);
            return res;
        }

        public newWasAssociatedWith(id?: any, a?: any, ag?: any, plan?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a, ag, plan, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag, plan);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && plan === undefined && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName(id);
            } else throw new Error('invalid overload');
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, ag: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAssociatedWith {
            const res: org.openprovenance.prov.model.WasAssociatedWith = this.mc.newWasAssociatedWith(id, a, ag, plan, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(u: org.openprovenance.prov.model.WasAssociatedWith): org.openprovenance.prov.model.WasAssociatedWith {
            const u1: org.openprovenance.prov.model.WasAssociatedWith = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getAgent(), u.getPlan());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            return u1;
        }

        public newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAttributedTo {
            const res: org.openprovenance.prov.model.WasAttributedTo = this.mc.newWasAttributedTo(id, entity, agent, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAttributedTo {
            const res: org.openprovenance.prov.model.WasAttributedTo = this.mc.newWasAttributedTo(null, entity, agent, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAttributedTo {
            const res: org.openprovenance.prov.model.WasAttributedTo = this.mc.newWasAttributedTo(id, entity, agent, attributes);
            return res;
        }

        public newWasAttributedTo(id?: any, entity?: any, agent?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, entity, agent, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && attributes === undefined) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && agent === undefined && attributes === undefined) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || id === null) && entity === undefined && agent === undefined && attributes === undefined) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(id);
            } else throw new Error('invalid overload');
        }

        public newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific: org.openprovenance.prov.model.QualifiedName, general: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf {
            const res: org.openprovenance.prov.model.SpecializationOf = this.mc.newSpecializationOf(specific, general);
            return res;
        }

        /**
         * 
         * @param {*} specific
         * @param {*} general
         * @return {*}
         */
        public newSpecializationOf(specific?: any, general?: any): any {
            if (((specific != null && (specific.constructor != null && specific.constructor["__interfaces"] != null && specific.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || specific === null) && ((general != null && (general.constructor != null && general.constructor["__interfaces"] != null && general.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || general === null)) {
                return <any>this.newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific, general);
            } else if (((specific != null && (specific.constructor != null && specific.constructor["__interfaces"] != null && specific.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || specific === null) && general === undefined) {
                return <any>this.newSpecializationOf$org_openprovenance_prov_model_SpecializationOf(specific);
            } else throw new Error('invalid overload');
        }

        public newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1: org.openprovenance.prov.model.QualifiedName, entity2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf {
            const res: org.openprovenance.prov.model.AlternateOf = this.mc.newAlternateOf(entity1, entity2);
            return res;
        }

        /**
         * A factory method to create an instance of an alternate {@link org.openprovenance.prov.model.AlternateOf}
         * @param {*} entity1 an identifier for the first {@link org.openprovenance.prov.model.Entity}
         * @param {*} entity2 an identifier for the second {@link org.openprovenance.prov.model.Entity}
         * @return {*} an instance of {@link org.openprovenance.prov.model.AlternateOf}
         */
        public newAlternateOf(entity1?: any, entity2?: any): any {
            if (((entity1 != null && (entity1.constructor != null && entity1.constructor["__interfaces"] != null && entity1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity1 === null) && ((entity2 != null && (entity2.constructor != null && entity2.constructor["__interfaces"] != null && entity2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity2 === null)) {
                return <any>this.newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1, entity2);
            } else if (((entity1 != null && (entity1.constructor != null && entity1.constructor["__interfaces"] != null && entity1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || entity1 === null) && entity2 === undefined) {
                return <any>this.newAlternateOf$org_openprovenance_prov_model_AlternateOf(entity1);
            } else throw new Error('invalid overload');
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasDerivedFrom {
            const res: org.openprovenance.prov.model.WasDerivedFrom = this.mc.newWasDerivedFrom(id, e2, e1, null, null, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasDerivedFrom {
            const res: org.openprovenance.prov.model.WasDerivedFrom = this.mc.newWasDerivedFrom(null, e2, e1, null, null, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, gen: org.openprovenance.prov.model.QualifiedName, use: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasDerivedFrom {
            const res: org.openprovenance.prov.model.WasDerivedFrom = this.mc.newWasDerivedFrom(id, e2, e1, a, gen, use, attributes);
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
        public newWasDerivedFrom(id?: any, e2?: any, e1?: any, a?: any, gen?: any, use?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((e2 != null && (e2.constructor != null && e2.constructor["__interfaces"] != null && e2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e2 === null) && ((e1 != null && (e1.constructor != null && e1.constructor["__interfaces"] != null && e1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e1 === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((gen != null && (gen.constructor != null && gen.constructor["__interfaces"] != null && gen.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || gen === null) && ((use != null && (use.constructor != null && use.constructor["__interfaces"] != null && use.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || use === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, e2, e1, a, gen, use, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((e2 != null && (e2.constructor != null && e2.constructor["__interfaces"] != null && e2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e2 === null) && ((e1 != null && (e1.constructor != null && e1.constructor["__interfaces"] != null && e1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e1 === null) && a === undefined && gen === undefined && use === undefined && attributes === undefined) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, e2, e1);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((e2 != null && (e2.constructor != null && e2.constructor["__interfaces"] != null && e2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || e2 === null) && e1 === undefined && a === undefined && gen === undefined && use === undefined && attributes === undefined) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, e2);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || id === null) && e2 === undefined && e1 === undefined && a === undefined && gen === undefined && use === undefined && attributes === undefined) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(id);
            } else throw new Error('invalid overload');
        }

        public newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps: Array<org.openprovenance.prov.model.Activity>, as: Array<org.openprovenance.prov.model.Entity>, ags: Array<org.openprovenance.prov.model.Agent>, lks: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.newDocument$();
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ps);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), as);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ags);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), lks);
            return res;
        }

        public newDocument(ps?: any, as?: any, ags?: any, lks?: any): any {
            if (((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                return <any>this.newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps, as, ags, lks);
            } else if (((ps != null && ps instanceof <any>Array && (ps.length == 0 || ps[0] == null ||(ps[0] != null && (ps[0].constructor != null && ps[0].constructor["__interfaces"] != null && ps[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)))) || ps === null) && ((as != null && as instanceof <any>Array && (as.length == 0 || as[0] == null ||(as[0] != null && (as[0].constructor != null && as[0].constructor["__interfaces"] != null && as[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)))) || as === null) && ((ags != null && ags instanceof <any>Array && (ags.length == 0 || ags[0] == null ||(ags[0] != null && (ags[0].constructor != null && ags[0].constructor["__interfaces"] != null && ags[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)))) || ags === null) && ((lks != null && lks instanceof <any>Array && (lks.length == 0 || lks[0] == null ||(lks[0] != null && (lks[0].constructor != null && lks[0].constructor["__interfaces"] != null && lks[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Statement") >= 0)))) || lks === null)) {
                return super.newDocument(ps, as, ags, lks);
            } else if (((ps != null && ps instanceof <any>org.openprovenance.prov.model.Namespace) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && lks === undefined) {
                return <any>this.newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(ps, as, ags);
            } else if (((ps != null && (ps.constructor != null && ps.constructor["__interfaces"] != null && ps.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || ps === null) && as === undefined && ags === undefined && lks === undefined) {
                return <any>this.newDocument$org_openprovenance_prov_model_Document(ps);
            } else if (ps === undefined && as === undefined && ags === undefined && lks === undefined) {
                return <any>this.newDocument$();
            } else throw new Error('invalid overload');
        }

        public newDocument$org_openprovenance_prov_model_Document(graph: org.openprovenance.prov.model.Document): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.newDocument$();
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), graph.getStatementOrBundle());
            if (graph.getNamespace() != null){
                res.setNamespace(new org.openprovenance.prov.model.Namespace(graph.getNamespace()));
            }
            return res;
        }

        public newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, informed: org.openprovenance.prov.model.QualifiedName, informant: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInformedBy {
            const res: org.openprovenance.prov.model.WasInformedBy = this.mc.newWasInformedBy(id, informed, informant, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, informed: org.openprovenance.prov.model.QualifiedName, informant: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInformedBy {
            const res: org.openprovenance.prov.model.WasInformedBy = this.mc.newWasInformedBy(id, informed, informant, attributes);
            return res;
        }

        public newWasInformedBy(id?: any, informed?: any, informant?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((informed != null && (informed.constructor != null && informed.constructor["__interfaces"] != null && informed.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informed === null) && ((informant != null && (informant.constructor != null && informant.constructor["__interfaces"] != null && informant.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informant === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, informed, informant, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((informed != null && (informed.constructor != null && informed.constructor["__interfaces"] != null && informed.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informed === null) && ((informant != null && (informant.constructor != null && informant.constructor["__interfaces"] != null && informant.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || informant === null) && attributes === undefined) {
                return <any>this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, informed, informant);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || id === null) && informed === undefined && informant === undefined && attributes === undefined) {
                return <any>this.newWasInformedBy$org_openprovenance_prov_model_WasInformedBy(id);
            } else throw new Error('invalid overload');
        }

        public newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, influencee: org.openprovenance.prov.model.QualifiedName, influencer: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInfluencedBy {
            const res: org.openprovenance.prov.model.WasInfluencedBy = this.mc.newWasInfluencedBy(id, influencee, influencer, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, influencee: org.openprovenance.prov.model.QualifiedName, influencer: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInfluencedBy {
            const res: org.openprovenance.prov.model.WasInfluencedBy = this.mc.newWasInfluencedBy(id, influencee, influencer, attributes);
            return res;
        }

        public newWasInfluencedBy(id?: any, influencee?: any, influencer?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((influencee != null && (influencee.constructor != null && influencee.constructor["__interfaces"] != null && influencee.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencee === null) && ((influencer != null && (influencer.constructor != null && influencer.constructor["__interfaces"] != null && influencer.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencer === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, influencee, influencer, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((influencee != null && (influencee.constructor != null && influencee.constructor["__interfaces"] != null && influencee.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencee === null) && ((influencer != null && (influencer.constructor != null && influencer.constructor["__interfaces"] != null && influencer.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || influencer === null) && attributes === undefined) {
                return <any>this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || id === null) && influencee === undefined && influencer === undefined && attributes === undefined) {
                return <any>this.newWasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(id);
            } else throw new Error('invalid overload');
        }

        public newHadMember$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName_A(collection: org.openprovenance.prov.model.QualifiedName, ...entities: org.openprovenance.prov.model.QualifiedName[]): org.openprovenance.prov.model.HadMember {
            const res: org.openprovenance.prov.model.HadMember = new org.openprovenance.prov.vanilla.HadMember(collection, /* asList */entities.slice(0));
            return res;
        }

        public newHadMember(collection?: any, ...entities: any[]): any {
            if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entities != null && entities instanceof <any>Array && (entities.length == 0 || entities[0] == null ||(entities[0] != null && (entities[0].constructor != null && entities[0].constructor["__interfaces"] != null && entities[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)))) || entities === null)) {
                return <any>this.newHadMember$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName_A(collection, ...entities);
            } else if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || collection === null) && ((entities != null && (entities instanceof Array)) || entities === null)) {
                return <any>this.newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(collection, <any>entities);
            } else if (((collection != null && (collection.constructor != null && collection.constructor["__interfaces"] != null && collection.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || collection === null) && entities === undefined || entities.length === 0) {
                return <any>this.newHadMember$org_openprovenance_prov_model_HadMember(<any>collection);
            } else throw new Error('invalid overload');
        }

        public newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>): org.openprovenance.prov.model.HadMember {
            const ll: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
            if (e != null){
                for(let index240=0; index240 < e.length; index240++) {
                    let q = e[index240];
                    {
                        /* add */(ll.push(q)>0);
                    }
                }
            }
            const res: org.openprovenance.prov.model.HadMember = new org.openprovenance.prov.vanilla.HadMember(c, ll);
            return res;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, eid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.mc.newWasStartedBy(id, aid, eid, null, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.mc.newWasStartedBy(id, activity, trigger, starter, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.mc.newWasStartedBy(id, activity, trigger, starter, time, attributes);
            return res;
        }

        public newWasStartedBy(id?: any, activity?: any, trigger?: any, starter?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((starter != null && (starter.constructor != null && starter.constructor["__interfaces"] != null && starter.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || starter === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, starter, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((starter != null && (starter.constructor != null && starter.constructor["__interfaces"] != null && starter.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || starter === null) && time === undefined && attributes === undefined) {
                return <any>this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, starter);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && starter === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && activity === undefined && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || id === null) && activity === undefined && trigger === undefined && starter === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasStartedBy$org_openprovenance_prov_model_WasStartedBy(id);
            } else throw new Error('invalid overload');
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, eid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.mc.newWasEndedBy(id, aid, eid, null, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.mc.newWasEndedBy(id, activity, trigger, ender, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.mc.newWasEndedBy(id, activity, trigger, ender, time, attributes);
            return res;
        }

        public newWasEndedBy(id?: any, activity?: any, trigger?: any, ender?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((ender != null && (ender.constructor != null && ender.constructor["__interfaces"] != null && ender.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ender === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, activity, trigger, ender, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ((ender != null && (ender.constructor != null && ender.constructor["__interfaces"] != null && ender.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ender === null) && time === undefined && attributes === undefined) {
                return <any>this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger, ender);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((trigger != null && (trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || trigger === null) && ender === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && activity === undefined && trigger === undefined && ender === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || id === null) && activity === undefined && trigger === undefined && ender === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasEndedBy$org_openprovenance_prov_model_WasEndedBy(id);
            } else throw new Error('invalid overload');
        }

        public newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>, bundles: Array<org.openprovenance.prov.model.Bundle>): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.newDocument$();
            res.setNamespace(namespace);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), statements);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), bundles);
            return res;
        }

        public newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.ActedOnBehalfOf {
            const res: org.openprovenance.prov.model.ActedOnBehalfOf = this.mc.newActedOnBehalfOf(id, delegate, responsible, activity, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.ActedOnBehalfOf {
            const res: org.openprovenance.prov.model.ActedOnBehalfOf = this.mc.newActedOnBehalfOf(id, delegate, responsible, activity, attributes);
            return res;
        }

        public newActedOnBehalfOf(id?: any, delegate?: any, responsible?: any, activity?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, delegate, responsible, activity, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && attributes === undefined) {
                return <any>this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible, activity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((delegate != null && (delegate.constructor != null && delegate.constructor["__interfaces"] != null && delegate.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || delegate === null) && ((responsible != null && (responsible.constructor != null && responsible.constructor["__interfaces"] != null && responsible.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || responsible === null) && activity === undefined && attributes === undefined) {
                return <any>this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, delegate, responsible);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || id === null) && delegate === undefined && responsible === undefined && activity === undefined && attributes === undefined) {
                return <any>this.newActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(id);
            } else throw new Error('invalid overload');
        }

        public newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.ActedOnBehalfOf {
            const res: org.openprovenance.prov.model.ActedOnBehalfOf = this.mc.newActedOnBehalfOf(id, delegate, responsible, null, java.util.Collections.EMPTY_LIST);
            return res;
        }

        public newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, statements: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            return this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, null, statements);
        }

        public newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            const res: org.openprovenance.prov.model.Bundle = this.mc.newNamedBundle(id, namespace, statements);
            return res;
        }

        public newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, ps: Array<org.openprovenance.prov.model.Activity>, as: Array<org.openprovenance.prov.model.Entity>, ags: Array<org.openprovenance.prov.model.Agent>, lks: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            const attr: Array<org.openprovenance.prov.model.Statement> = <any>([]);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(attr, ps);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(attr, as);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(attr, ags);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(attr, lks);
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
        public newNamedBundle(id?: any, ps?: any, as?: any, ags?: any, lks?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                return <any>this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id, ps, as, ags, lks);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && ps instanceof <any>org.openprovenance.prov.model.Namespace) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ags === undefined && lks === undefined) {
                return <any>this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, ps, as);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && (ps instanceof Array)) || ps === null) && as === undefined && ags === undefined && lks === undefined) {
                return <any>this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, ps);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} id
         * @param {*} c
         * @param {*[]} e
         * @param {*[]} attributes
         * @return {*}
         */
        public newQualifiedHadMember(id: org.openprovenance.prov.model.QualifiedName, c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedHadMember {
            const res: org.openprovenance.prov.vanilla.QualifiedHadMember = new org.openprovenance.prov.vanilla.QualifiedHadMember(id, c, e, attributes);
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
        public newQualifiedSpecializationOf(id: org.openprovenance.prov.model.QualifiedName, specific: org.openprovenance.prov.model.QualifiedName, general: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedSpecializationOf {
            const res: org.openprovenance.prov.vanilla.QualifiedSpecializationOf = new org.openprovenance.prov.vanilla.QualifiedSpecializationOf(id, specific, general, attributes);
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
        public newQualifiedAlternateOf(id: org.openprovenance.prov.model.QualifiedName, alt1: org.openprovenance.prov.model.QualifiedName, alt2: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedAlternateOf {
            const res: org.openprovenance.prov.model.extension.QualifiedAlternateOf = new org.openprovenance.prov.vanilla.QualifiedAlternateOf(id, alt1, alt2, attributes);
            return res;
        }
    }
    ProvFactory["__class"] = "org.openprovenance.prov.vanilla.ProvFactory";
    ProvFactory["__interfaces"] = ["org.openprovenance.prov.model.AtomConstructor","org.openprovenance.prov.model.LiteralConstructor","org.openprovenance.prov.model.ModelConstructorExtension","org.openprovenance.prov.model.ModelConstructor"];


}

