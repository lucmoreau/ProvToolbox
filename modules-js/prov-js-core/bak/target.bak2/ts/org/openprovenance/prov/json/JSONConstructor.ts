/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.json {
    /**
     * @author Trung Dong Huynh
     * 
     * Constructing a JSON structure from a Document that
     * follows the PROV-JSON representation.
     * @param {org.openprovenance.prov.model.Name} name
     * @class
     */
    export class JSONConstructor implements org.openprovenance.prov.model.ModelConstructor, org.openprovenance.prov.model.ModelConstructorExtension {
        /*private*/ documentNamespace: org.openprovenance.prov.model.Namespace;

        /*private*/ currentNamespace: org.openprovenance.prov.model.Namespace;

        /*private*/ documentBundles: any;

        /*private*/ documentRecords: Array<JSONConstructor.JsonProvRecord>;

        /*private*/ currentRecords: Array<JSONConstructor.JsonProvRecord>;

        /*private*/ name: org.openprovenance.prov.model.Name;

        public constructor(name: org.openprovenance.prov.model.Name) {
            this.documentNamespace = null;
            this.currentNamespace = null;
            this.documentBundles = <any>({});
            this.documentRecords = <any>([]);
            this.currentRecords = this.documentRecords;
            if (this.name === undefined) { this.name = null; }
            this.name = name;
        }

        public getJSONStructure$(): any {
            const document: any = this.getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(this.documentRecords, this.documentNamespace);
            if (!/* isEmpty */(Object.keys(this.documentBundles).length == 0))/* put */(document["bundle"] = this.documentBundles);
            return document;
        }

        public getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(records: Array<JSONConstructor.JsonProvRecord>, namespace: org.openprovenance.prov.model.Namespace): any {
            const bundle: any = <any>({});
            const prefixes: any = <any>(((o) => { let r = {}; for(let p in o) r[p]=o[p]; return r; })(namespace.getPrefixes()));
            if (namespace.getDefaultNamespace() != null){
                /* put */(prefixes["default"] = namespace.getDefaultNamespace());
            }
            if (!/* isEmpty */(Object.keys(prefixes).length == 0))/* put */(bundle["prefix"] = prefixes);
            for(let index121=0; index121 < records.length; index121++) {
                let o = records[index121];
                {
                    if (o == null)continue;
                    const record: JSONConstructor.JsonProvRecord = <JSONConstructor.JsonProvRecord>o;
                    const type: string = record.type;
                    let structure: any = <any><any>/* get */((m,k) => m[k]===undefined?null:m[k])(bundle, type);
                    if (structure == null){
                        structure = <any>({});
                        /* put */(bundle[type] = structure);
                    }
                    const hash: any = <any>({});
                    const tuples: Array<any[]> = <Array<any[]>><any>record.attributes;
                    for(let index122=0; index122 < tuples.length; index122++) {
                        let tuple = tuples[index122];
                        {
                            const attribute: any = tuple[0];
                            const value: any = tuple[1];
                            if (/* containsKey */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return true; } return false; })(<any>hash, attribute)){
                                const existing: any = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>hash, attribute);
                                if (existing != null && (existing instanceof Array)){
                                    const values: Array<any> = <Array<any>><any>existing;
                                    /* add */(values.push(value)>0);
                                } else {
                                    const values: Array<any> = <any>([]);
                                    /* add */(values.push(existing)>0);
                                    /* add */(values.push(value)>0);
                                    /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>hash, attribute, values);
                                }
                            } else {
                                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>hash, attribute, value);
                            }
                        }
                    }
                    if (/* containsKey */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return true; } return false; })(<any>structure, record.id)){
                        const existing: any = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>structure, record.id);
                        if (existing != null && (existing instanceof Array)){
                            const values: Array<any> = <Array<any>><any>existing;
                            /* add */(values.push(hash)>0);
                        } else {
                            const values: Array<any> = <any>([]);
                            /* add */(values.push(existing)>0);
                            /* add */(values.push(hash)>0);
                            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>structure, record.id, values);
                        }
                    } else /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>structure, record.id, hash);
                }
            }
            return bundle;
        }

        public getJSONStructure(records?: any, namespace?: any): any {
            if (((records != null && (records instanceof Array)) || records === null) && ((namespace != null && namespace instanceof <any>org.openprovenance.prov.model.Namespace) || namespace === null)) {
                return <any>this.getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(records, namespace);
            } else if (records === undefined && namespace === undefined) {
                return <any>this.getJSONStructure$();
            } else throw new Error('invalid overload');
        }

        static countMap: any; public static countMap_$LI$(): any { if (JSONConstructor.countMap == null) { JSONConstructor.countMap = <any>({}); }  return JSONConstructor.countMap; }

        static getBlankID(type: string): string {
            if (!/* containsKey */JSONConstructor.countMap_$LI$().hasOwnProperty(type)){
                /* put */(JSONConstructor.countMap_$LI$()[type] = 0);
            }
            let count: number = /* get */((m,k) => m[k]===undefined?null:m[k])(JSONConstructor.countMap_$LI$(), type);
            count += 1;
            /* put */(JSONConstructor.countMap_$LI$()[type] = count);
            return "_:" + type + count;
        }

        tuple(o1: any, o2: any): any[] {
            const tuple: any[] = [o1, o2];
            return tuple;
        }

        static qnU: org.openprovenance.prov.model.QualifiedNameUtils; public static qnU_$LI$(): org.openprovenance.prov.model.QualifiedNameUtils { if (JSONConstructor.qnU == null) { JSONConstructor.qnU = new org.openprovenance.prov.model.QualifiedNameUtils(); }  return JSONConstructor.qnU; }

        public qualifiedNameToString(namespace: org.openprovenance.prov.model.Namespace, id: org.openprovenance.prov.model.QualifiedName): string {
            let tmp: javax.xml.namespace.QName;
            const prefix: string = id.getPrefix();
            const local: string = id.getLocalPart();
            if (prefix == null){
                tmp = new javax.xml.namespace.QName(id.getNamespaceURI(), JSONConstructor.qnU_$LI$().unescapeProvLocalName(local));
            } else {
                tmp = new javax.xml.namespace.QName(id.getNamespaceURI(), JSONConstructor.qnU_$LI$().unescapeProvLocalName(local), prefix);
            }
            return namespace.qualifiedNameToString$javax_xml_namespace_QName(tmp);
        }

        typedLiteral(value: string, datatype: string, lang: string): any {
            if (datatype === "xsd:string" && lang == null)return value;
            if (datatype === "xsd:double")return /* parseDouble */parseFloat(value);
            if (datatype === "xsd:int")return /* parseInt */parseInt(value);
            if (datatype === "xsd:boolean")return javaemul.internal.BooleanHelper.parseBoolean(value);
            const result: any = <any>({});
            /* put */(result["$"] = value);
            if (datatype != null){
                /* put */(result["type"] = datatype);
            }
            if (lang != null){
                /* put */(result["lang"] = lang);
            }
            return result;
        }

        convertValueToString(value: any, convertedValue: any): string {
            if (typeof convertedValue === 'string'){
                return <string>convertedValue;
            }
            if (convertedValue != null && (convertedValue.constructor != null && convertedValue.constructor["__interfaces"] != null && convertedValue.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0))return this.qualifiedNameToString(this.currentNamespace, <org.openprovenance.prov.model.QualifiedName><any>convertedValue);
            if (convertedValue != null && (convertedValue.constructor != null && convertedValue.constructor["__interfaces"] != null && convertedValue.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                const iStr: org.openprovenance.prov.model.LangString = <org.openprovenance.prov.model.LangString><any>convertedValue;
                return iStr.getValue();
            }
            if (convertedValue != null && convertedValue instanceof <any>Array && (convertedValue.length == 0 || convertedValue[0] == null ||typeof convertedValue[0] === 'number')){
                return <string>value;
            }
            return <string>value;
        }

        convertTypedValue(value: any, type: org.openprovenance.prov.model.QualifiedName): any {
            const datatype: string = this.qualifiedNameToString(this.currentNamespace, type);
            let result: any;
            if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)){
                result = this.typedLiteral(this.qualifiedNameToString(this.currentNamespace, <org.openprovenance.prov.model.QualifiedName><any>value), datatype, null);
            } else if (value != null && (value.constructor != null && value.constructor["__interfaces"] != null && value.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                const iStr: org.openprovenance.prov.model.LangString = <org.openprovenance.prov.model.LangString><any>value;
                const lang: string = iStr.getLang();
                if (lang != null){
                    result = this.typedLiteral(iStr.getValue(), null, lang);
                } else {
                    result = iStr.getValue();
                }
            } else {
                result = this.typedLiteral(value.toString(), datatype, null);
            }
            return result;
        }

        convertAttribute(attr: org.openprovenance.prov.model.Attribute): any[] {
            const attrName: string = this.qualifiedNameToString(this.currentNamespace, attr.getElementName());
            const value: any = attr.getValue();
            const type: org.openprovenance.prov.model.QualifiedName = attr.getType();
            const attrValue: any = this.convertTypedValue(value, type);
            return this.tuple(attrName, attrValue);
        }

        convertAttributes(attrs: Array<org.openprovenance.prov.model.Attribute>): Array<any[]> {
            const result: Array<any[]> = <any>([]);
            if (attrs != null)for(let index123=0; index123 < attrs.length; index123++) {
                let attr = attrs[index123];
                {
                    /* add */(result.push(this.convertAttribute(attr))>0);
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
        public newEntity(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Entity {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "entity", this.qualifiedNameToString(this.currentNamespace, id), attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newActivity(id: org.openprovenance.prov.model.QualifiedName, startTime: javax.xml.datatype.XMLGregorianCalendar, endTime: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Activity {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (startTime != null){
                /* add */(attrs.push(this.tuple("prov:startTime", startTime.toXMLFormat()))>0);
            }
            if (endTime != null){
                /* add */(attrs.push(this.tuple("prov:endTime", endTime.toXMLFormat()))>0);
            }
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "activity", this.qualifiedNameToString(this.currentNamespace, id), attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {*} id
         * @param {*[]} attributes
         * @return {*}
         */
        public newAgent(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Agent {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "agent", this.qualifiedNameToString(this.currentNamespace, id), attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newUsed(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Used {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (activity != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity)))>0);
            if (entity != null)/* add */(attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, entity)))>0);
            if (time != null)/* add */(attrs.push(this.tuple("prov:time", time.toXMLFormat()))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("u");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "used", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasGeneratedBy(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasGeneratedBy {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (entity != null)/* add */(attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, entity)))>0);
            if (activity != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity)))>0);
            if (time != null)/* add */(attrs.push(this.tuple("prov:time", time.toXMLFormat()))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wGB");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasGeneratedBy", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasInvalidatedBy(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInvalidatedBy {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (entity != null)/* add */(attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, entity)))>0);
            if (activity != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity)))>0);
            if (time != null)/* add */(attrs.push(this.tuple("prov:time", time.toXMLFormat()))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wIB");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasInvalidatedBy", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasStartedBy(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasStartedBy {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (activity != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity)))>0);
            if (trigger != null)/* add */(attrs.push(this.tuple("prov:trigger", this.qualifiedNameToString(this.currentNamespace, trigger)))>0);
            if (starter != null)/* add */(attrs.push(this.tuple("prov:starter", this.qualifiedNameToString(this.currentNamespace, starter)))>0);
            if (time != null)/* add */(attrs.push(this.tuple("prov:time", time.toXMLFormat()))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wSB");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasStartedBy", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasEndedBy(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasEndedBy {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (activity != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity)))>0);
            if (trigger != null)/* add */(attrs.push(this.tuple("prov:trigger", this.qualifiedNameToString(this.currentNamespace, trigger)))>0);
            if (ender != null)/* add */(attrs.push(this.tuple("prov:ender", this.qualifiedNameToString(this.currentNamespace, ender)))>0);
            if (time != null)/* add */(attrs.push(this.tuple("prov:time", time.toXMLFormat()))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wEB");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasEndedBy", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasDerivedFrom(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, generation: org.openprovenance.prov.model.QualifiedName, usage: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasDerivedFrom {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (e2 != null)/* add */(attrs.push(this.tuple("prov:generatedEntity", this.qualifiedNameToString(this.currentNamespace, e2)))>0);
            if (e1 != null)/* add */(attrs.push(this.tuple("prov:usedEntity", this.qualifiedNameToString(this.currentNamespace, e1)))>0);
            if (activity != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, activity)))>0);
            if (generation != null)/* add */(attrs.push(this.tuple("prov:generation", this.qualifiedNameToString(this.currentNamespace, generation)))>0);
            if (usage != null)/* add */(attrs.push(this.tuple("prov:usage", this.qualifiedNameToString(this.currentNamespace, usage)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wDF");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasDerivedFrom", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasAssociatedWith(id: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, ag: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAssociatedWith {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (a != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, a)))>0);
            if (ag != null)/* add */(attrs.push(this.tuple("prov:agent", this.qualifiedNameToString(this.currentNamespace, ag)))>0);
            if (plan != null)/* add */(attrs.push(this.tuple("prov:plan", this.qualifiedNameToString(this.currentNamespace, plan)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wAW");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasAssociatedWith", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasAttributedTo(id: org.openprovenance.prov.model.QualifiedName, e: org.openprovenance.prov.model.QualifiedName, ag: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAttributedTo {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (e != null)/* add */(attrs.push(this.tuple("prov:entity", this.qualifiedNameToString(this.currentNamespace, e)))>0);
            if (ag != null)/* add */(attrs.push(this.tuple("prov:agent", this.qualifiedNameToString(this.currentNamespace, ag)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("wAT");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasAttributedTo", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newActedOnBehalfOf(id: org.openprovenance.prov.model.QualifiedName, ag2: org.openprovenance.prov.model.QualifiedName, ag1: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.ActedOnBehalfOf {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (ag2 != null)/* add */(attrs.push(this.tuple("prov:delegate", this.qualifiedNameToString(this.currentNamespace, ag2)))>0);
            if (ag1 != null)/* add */(attrs.push(this.tuple("prov:responsible", this.qualifiedNameToString(this.currentNamespace, ag1)))>0);
            if (a != null)/* add */(attrs.push(this.tuple("prov:activity", this.qualifiedNameToString(this.currentNamespace, a)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("aOBO");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "actedOnBehalfOf", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasInformedBy(id: org.openprovenance.prov.model.QualifiedName, a2: org.openprovenance.prov.model.QualifiedName, a1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInformedBy {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (a2 != null)/* add */(attrs.push(this.tuple("prov:informed", this.qualifiedNameToString(this.currentNamespace, a2)))>0);
            if (a1 != null)/* add */(attrs.push(this.tuple("prov:informant", this.qualifiedNameToString(this.currentNamespace, a1)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("Infm");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasInformedBy", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newWasInfluencedBy(id: org.openprovenance.prov.model.QualifiedName, a2: org.openprovenance.prov.model.QualifiedName, a1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInfluencedBy {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (a2 != null)/* add */(attrs.push(this.tuple("prov:influencee", this.qualifiedNameToString(this.currentNamespace, a2)))>0);
            if (a1 != null)/* add */(attrs.push(this.tuple("prov:influencer", this.qualifiedNameToString(this.currentNamespace, a1)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("Infl");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "wasInfluencedBy", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {*} e1
         * @param {*} e2
         * @return {*}
         */
        public newAlternateOf(e1: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf {
            const attrs: Array<any[]> = <any>([]);
            if (e2 != null)/* add */(attrs.push(this.tuple("prov:alternate2", this.qualifiedNameToString(this.currentNamespace, e2)))>0);
            if (e1 != null)/* add */(attrs.push(this.tuple("prov:alternate1", this.qualifiedNameToString(this.currentNamespace, e1)))>0);
            const recordID: string = JSONConstructor.getBlankID("aO");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "alternateOf", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {*} e2
         * @param {*} e1
         * @return {*}
         */
        public newSpecializationOf(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf {
            const attrs: Array<any[]> = <any>([]);
            if (e2 != null)/* add */(attrs.push(this.tuple("prov:specificEntity", this.qualifiedNameToString(this.currentNamespace, e2)))>0);
            if (e1 != null)/* add */(attrs.push(this.tuple("prov:generalEntity", this.qualifiedNameToString(this.currentNamespace, e1)))>0);
            const recordID: string = JSONConstructor.getBlankID("sO");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "specializationOf", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {*} e2
         * @param {*} e1
         * @param {*} b
         * @return {*}
         */
        public newMentionOf(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, b: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.MentionOf {
            const attrs: Array<any[]> = <any>([]);
            if (e2 != null)/* add */(attrs.push(this.tuple("prov:specificEntity", this.qualifiedNameToString(this.currentNamespace, e2)))>0);
            if (e1 != null)/* add */(attrs.push(this.tuple("prov:generalEntity", this.qualifiedNameToString(this.currentNamespace, e1)))>0);
            if (b != null)/* add */(attrs.push(this.tuple("prov:bundle", this.qualifiedNameToString(this.currentNamespace, b)))>0);
            const recordID: string = JSONConstructor.getBlankID("mO");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "mentionOf", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {*} c
         * @param {*[]} e
         * @return {*}
         */
        public newHadMember(c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>): org.openprovenance.prov.model.HadMember {
            const attrs: Array<any[]> = <any>([]);
            if (c != null)/* add */(attrs.push(this.tuple("prov:collection", this.qualifiedNameToString(this.currentNamespace, c)))>0);
            if (e != null && !/* isEmpty */(e.length == 0)){
                const entityList: Array<string> = <any>([]);
                for(let index124=0; index124 < e.length; index124++) {
                    let entity = e[index124];
                    /* add */(entityList.push(this.qualifiedNameToString(this.currentNamespace, entity))>0)
                }
                /* add */(attrs.push(this.tuple("prov:entity", entityList))>0);
            }
            const recordID: string = JSONConstructor.getBlankID("hM");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "hadMember", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespaces
         * @param {*[]} statements
         * @param {*[]} bundles
         * @return {*}
         */
        public newDocument(namespaces: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>, bundles: Array<org.openprovenance.prov.model.Bundle>): org.openprovenance.prov.model.Document {
            return null;
        }

        /**
         * 
         * @param {*} id
         * @param {org.openprovenance.prov.model.Namespace} namespaces
         * @param {*[]} statements
         * @return {*}
         */
        public newNamedBundle(id: org.openprovenance.prov.model.QualifiedName, namespaces: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            const bundle: any = this.getJSONStructure$java_util_List$org_openprovenance_prov_model_Namespace(this.currentRecords, this.currentNamespace);
            /* put */(this.documentBundles[this.qualifiedNameToString(this.currentNamespace, id)] = bundle);
            this.currentRecords = this.documentRecords;
            this.currentNamespace = this.documentNamespace;
            return null;
        }

        /**
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespace
         */
        public startDocument(namespace: org.openprovenance.prov.model.Namespace) {
            this.documentNamespace = namespace;
            this.currentNamespace = this.documentNamespace;
        }

        /**
         * 
         * @param {*} bundleId
         * @param {org.openprovenance.prov.model.Namespace} namespaces
         */
        public startBundle(bundleId: org.openprovenance.prov.model.QualifiedName, namespaces: org.openprovenance.prov.model.Namespace) {
            this.currentNamespace = namespaces;
            this.currentRecords = <any>([]);
        }

        encodeKeyEntitySet(keyEntitySet: Array<org.openprovenance.prov.model.Entry>): any {
            let isAllKeyOfSameDatatype: boolean = true;
            const firstKey: org.openprovenance.prov.model.Key = /* get */keyEntitySet[0].getKey();
            const firstKeyClass: org.openprovenance.prov.model.QualifiedName = firstKey.getType();
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(this.name.PROV_LANG_STRING,firstKeyClass))){
                isAllKeyOfSameDatatype = false;
            }
            if (isAllKeyOfSameDatatype){
                for(let index125=0; index125 < keyEntitySet.length; index125++) {
                    let pair = keyEntitySet[index125];
                    {
                        const keyClass: org.openprovenance.prov.model.QualifiedName = pair.getKey().getType();
                        if (keyClass !== firstKeyClass){
                            isAllKeyOfSameDatatype = false;
                            break;
                        }
                    }
                }
            }
            if (isAllKeyOfSameDatatype){
                const dictionary: any = <any>({});
                const keyDatatype: string = this.qualifiedNameToString(this.currentNamespace, /* get */keyEntitySet[0].getKey().getType());
                /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>dictionary, "$key-datatype", keyDatatype);
                for(let index126=0; index126 < keyEntitySet.length; index126++) {
                    let pair = keyEntitySet[index126];
                    {
                        const key: string = this.convertValueToString(pair.getKey().getValue(), pair.getKey().getConvertedValue());
                        const entity: string = this.qualifiedNameToString(this.currentNamespace, pair.getEntity());
                        /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>dictionary, key, entity);
                    }
                }
                return dictionary;
            }
            const values: Array<any> = <any>([]);
            for(let index127=0; index127 < keyEntitySet.length; index127++) {
                let pair = keyEntitySet[index127];
                {
                    const entity: any = this.qualifiedNameToString(this.currentNamespace, pair.getEntity());
                    const item: any = <any>({});
                    /* put */(item["$"] = entity);
                    const key: org.openprovenance.prov.model.Key = pair.getKey();
                    /* put */(item["key"] = this.convertTypedValue(key.getValue(), key.getType()));
                    /* add */(values.push(item)>0);
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
        public newDerivedByInsertionFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByInsertionFrom {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (after != null)/* add */(attrs.push(this.tuple("prov:after", this.qualifiedNameToString(this.currentNamespace, after)))>0);
            if (before != null)/* add */(attrs.push(this.tuple("prov:before", this.qualifiedNameToString(this.currentNamespace, before)))>0);
            if (keyEntitySet != null && !/* isEmpty */(keyEntitySet.length == 0)){
                /* add */(attrs.push(this.tuple("prov:key-entity-set", this.encodeKeyEntitySet(keyEntitySet)))>0);
            }
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("dBIF");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "derivedByInsertionFrom", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newDerivedByRemovalFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keys: Array<org.openprovenance.prov.model.Key>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByRemovalFrom {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (after != null)/* add */(attrs.push(this.tuple("prov:after", this.qualifiedNameToString(this.currentNamespace, after)))>0);
            if (before != null)/* add */(attrs.push(this.tuple("prov:before", this.qualifiedNameToString(this.currentNamespace, before)))>0);
            if (keys != null && !/* isEmpty */(keys.length == 0)){
                const values: Array<any> = <any>([]);
                for(let index128=0; index128 < keys.length; index128++) {
                    let key = keys[index128];
                    {
                        /* add */(values.push(this.convertTypedValue(key.getValue(), key.getType()))>0);
                    }
                }
                /* add */(attrs.push(this.tuple("prov:key-set", values))>0);
            }
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("dBRF");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "derivedByRemovalFrom", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        /**
         * 
         * @param {*} dict
         * @param {*[]} keyEntitySet
         * @return {*}
         */
        public newDictionaryMembership(dict: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>): org.openprovenance.prov.model.DictionaryMembership {
            const attrs: Array<any[]> = <any>([]);
            if (dict != null)/* add */(attrs.push(this.tuple("prov:dictionary", this.qualifiedNameToString(this.currentNamespace, dict)))>0);
            if (keyEntitySet != null && !/* isEmpty */(keyEntitySet.length == 0)){
                /* add */(attrs.push(this.tuple("prov:key-entity-set", this.encodeKeyEntitySet(keyEntitySet)))>0);
            }
            const recordID: string = JSONConstructor.getBlankID("hDM");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "hadDictionaryMember", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace: string, local: string, prefix: string): org.openprovenance.prov.model.QualifiedName {
            return null;
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace: string, local: string, prefix: string, flag: org.openprovenance.prov.model.ProvUtilities.BuildFlag): org.openprovenance.prov.model.QualifiedName {
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
        public newQualifiedName(namespace?: any, local?: any, prefix?: any, flag?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof flag === 'number') || flag === null)) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag);
            } else if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && flag === undefined) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
            } else throw new Error('invalid overload');
        }

        /**
         * 
         * @param {*} id
         * @param {*} e2
         * @param {*} e1
         * @param {*[]} attributes
         * @return {*}
         */
        public newQualifiedAlternateOf(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedAlternateOf {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (e2 != null)/* add */(attrs.push(this.tuple("prov:alternate2", this.qualifiedNameToString(this.currentNamespace, e2)))>0);
            if (e1 != null)/* add */(attrs.push(this.tuple("prov:alternate1", this.qualifiedNameToString(this.currentNamespace, e1)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("a0");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "provext:alternateOf", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newQualifiedSpecializationOf(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedSpecializationOf {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (e2 != null)/* add */(attrs.push(this.tuple("prov:specificEntity", this.qualifiedNameToString(this.currentNamespace, e2)))>0);
            if (e1 != null)/* add */(attrs.push(this.tuple("prov:generalEntity", this.qualifiedNameToString(this.currentNamespace, e1)))>0);
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("s0");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "provext:specializationOf", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
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
        public newQualifiedHadMember(id: org.openprovenance.prov.model.QualifiedName, c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedHadMember {
            const attrs: Array<any[]> = this.convertAttributes(attributes);
            if (c != null)/* add */(attrs.push(this.tuple("prov:collection", this.qualifiedNameToString(this.currentNamespace, c)))>0);
            if (e != null && !/* isEmpty */(e.length == 0)){
                const entityList: Array<string> = <any>([]);
                for(let index129=0; index129 < e.length; index129++) {
                    let entity = e[index129];
                    /* add */(entityList.push(this.qualifiedNameToString(this.currentNamespace, entity))>0)
                }
                /* add */(attrs.push(this.tuple("prov:entity", entityList))>0);
            }
            const recordID: string = (id != null) ? this.qualifiedNameToString(this.currentNamespace, id) : JSONConstructor.getBlankID("hM");
            const record: JSONConstructor.JsonProvRecord = new JSONConstructor.JsonProvRecord(this, "provext:hadMember", recordID, attrs);
            /* add */(this.currentRecords.push(record)>0);
            return null;
        }
    }
    JSONConstructor["__class"] = "org.openprovenance.prov.json.JSONConstructor";
    JSONConstructor["__interfaces"] = ["org.openprovenance.prov.model.ModelConstructorExtension","org.openprovenance.prov.model.ModelConstructor"];



    export namespace JSONConstructor {

        export class JsonProvRecord {
            public __parent: any;
            type: string;

            id: string;

            attributes: Array<any[]>;

            public constructor(__parent: any, type: string, id: string, attributes: Array<any[]>) {
                this.__parent = __parent;
                if (this.type === undefined) { this.type = null; }
                if (this.id === undefined) { this.id = null; }
                if (this.attributes === undefined) { this.attributes = null; }
                this.type = type;
                this.id = id;
                this.attributes = attributes;
            }
        }
        JsonProvRecord["__class"] = "org.openprovenance.prov.json.JSONConstructor.JsonProvRecord";

    }

}

