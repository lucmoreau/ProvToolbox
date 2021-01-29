/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.vanilla {
    export class ProvUtilities extends org.openprovenance.prov.model.ProvUtilities {
        static QualifiedName_PROV_TYPE: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_TYPE_$LI$(): org.openprovenance.prov.model.QualifiedName { if (ProvUtilities.QualifiedName_PROV_TYPE == null) { ProvUtilities.QualifiedName_PROV_TYPE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_TYPE; }  return ProvUtilities.QualifiedName_PROV_TYPE; }

        static QualifiedName_PROV_LABEL: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_LABEL_$LI$(): org.openprovenance.prov.model.QualifiedName { if (ProvUtilities.QualifiedName_PROV_LABEL == null) { ProvUtilities.QualifiedName_PROV_LABEL = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LABEL; }  return ProvUtilities.QualifiedName_PROV_LABEL; }

        static QualifiedName_PROV_VALUE: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_VALUE_$LI$(): org.openprovenance.prov.model.QualifiedName { if (ProvUtilities.QualifiedName_PROV_VALUE == null) { ProvUtilities.QualifiedName_PROV_VALUE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_VALUE; }  return ProvUtilities.QualifiedName_PROV_VALUE; }

        static QualifiedName_PROV_LOCATION: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_LOCATION_$LI$(): org.openprovenance.prov.model.QualifiedName { if (ProvUtilities.QualifiedName_PROV_LOCATION == null) { ProvUtilities.QualifiedName_PROV_LOCATION = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_LOCATION; }  return ProvUtilities.QualifiedName_PROV_LOCATION; }

        static QualifiedName_PROV_ROLE: org.openprovenance.prov.model.QualifiedName; public static QualifiedName_PROV_ROLE_$LI$(): org.openprovenance.prov.model.QualifiedName { if (ProvUtilities.QualifiedName_PROV_ROLE == null) { ProvUtilities.QualifiedName_PROV_ROLE = org.openprovenance.prov.vanilla.ProvFactory.getFactory().getName().PROV_ROLE; }  return ProvUtilities.QualifiedName_PROV_ROLE; }

        public static PROV_LABEL_URI: string; public static PROV_LABEL_URI_$LI$(): string { if (ProvUtilities.PROV_LABEL_URI == null) { ProvUtilities.PROV_LABEL_URI = ProvUtilities.QualifiedName_PROV_LABEL_$LI$().getUri(); }  return ProvUtilities.PROV_LABEL_URI; }

        public static PROV_TYPE_URI: string; public static PROV_TYPE_URI_$LI$(): string { if (ProvUtilities.PROV_TYPE_URI == null) { ProvUtilities.PROV_TYPE_URI = ProvUtilities.QualifiedName_PROV_TYPE_$LI$().getUri(); }  return ProvUtilities.PROV_TYPE_URI; }

        public static PROV_LOCATION_URI: string; public static PROV_LOCATION_URI_$LI$(): string { if (ProvUtilities.PROV_LOCATION_URI == null) { ProvUtilities.PROV_LOCATION_URI = ProvUtilities.QualifiedName_PROV_LOCATION_$LI$().getUri(); }  return ProvUtilities.PROV_LOCATION_URI; }

        public static PROV_ROLE_URI: string; public static PROV_ROLE_URI_$LI$(): string { if (ProvUtilities.PROV_ROLE_URI == null) { ProvUtilities.PROV_ROLE_URI = ProvUtilities.QualifiedName_PROV_ROLE_$LI$().getUri(); }  return ProvUtilities.PROV_ROLE_URI; }

        public static PROV_VALUE_URI: string; public static PROV_VALUE_URI_$LI$(): string { if (ProvUtilities.PROV_VALUE_URI == null) { ProvUtilities.PROV_VALUE_URI = ProvUtilities.QualifiedName_PROV_VALUE_$LI$().getUri(); }  return ProvUtilities.PROV_VALUE_URI; }

        split$java_util_Collection(attributes: Array<org.openprovenance.prov.model.Attribute>): any {
            const labels: Array<org.openprovenance.prov.vanilla.Label> = <any>([]);
            const types: Array<org.openprovenance.prov.vanilla.Type> = <any>([]);
            const values: Array<org.openprovenance.prov.vanilla.Value> = <any>([]);
            const locations: Array<org.openprovenance.prov.vanilla.Location> = <any>([]);
            const roles: Array<org.openprovenance.prov.vanilla.Role> = <any>([]);
            const others: any = <any>({});
            this.split$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Map(attributes, labels, types, values, locations, roles, others);
            const result: any = <any>({});
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>result, ProvUtilities.QualifiedName_PROV_LABEL_$LI$(), labels.slice(0));
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>result, ProvUtilities.QualifiedName_PROV_TYPE_$LI$(), types.slice(0));
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>result, ProvUtilities.QualifiedName_PROV_VALUE_$LI$(), values.slice(0));
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>result, ProvUtilities.QualifiedName_PROV_LOCATION_$LI$(), locations.slice(0));
            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>result, ProvUtilities.QualifiedName_PROV_ROLE_$LI$(), roles.slice(0));
            {
                let array231 = /* entrySet */((m) => { if(m.entries==null) m.entries=[]; return m.entries; })(<any>others);
                for(let index230=0; index230 < array231.length; index230++) {
                    let entry = array231[index230];
                    {
                        /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>result, entry.getKey(), entry.getValue().slice(0));
                    }
                }
            }
            return result;
        }

        public split$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Map(attributes: Array<org.openprovenance.prov.model.Attribute>, labels: Array<org.openprovenance.prov.vanilla.Label>, types: Array<org.openprovenance.prov.vanilla.Type>, values: Array<org.openprovenance.prov.vanilla.Value>, locations: Array<org.openprovenance.prov.vanilla.Location>, roles: Array<org.openprovenance.prov.vanilla.Role>, others: any) {
            for(let index232=0; index232 < attributes.length; index232++) {
                let attribute = attributes[index232];
                {
                    switch((attribute.getKind())) {
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                        /* add */(types.push(<org.openprovenance.prov.vanilla.Type><any>attribute)>0);
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                        /* add */(labels.push(<org.openprovenance.prov.vanilla.Label><any>attribute)>0);
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                        /* add */(roles.push(<org.openprovenance.prov.vanilla.Role><any>attribute)>0);
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                        /* add */(locations.push(<org.openprovenance.prov.vanilla.Location><any>attribute)>0);
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                        /* add */(values.push(<org.openprovenance.prov.vanilla.Value><any>attribute)>0);
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_KEY:
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                        const other: org.openprovenance.prov.vanilla.Other = <org.openprovenance.prov.vanilla.Other><any>attribute;
                        const name: org.openprovenance.prov.model.QualifiedName = other.getElementName();
                        let some: Array<org.openprovenance.prov.vanilla.Other> = /* get */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>others, name);
                        if (some == null){
                            some = <any>([]);
                            /* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>others, name, some);
                        }
                        /* add */(some.push(other)>0);
                        break;
                    }
                }
            }
        }

        public split(attributes?: any, labels?: any, types?: any, values?: any, locations?: any, roles?: any, others?: any) {
            if (((attributes != null && (attributes instanceof Array)) || attributes === null) && ((labels != null && (labels instanceof Array)) || labels === null) && ((types != null && (types instanceof Array)) || types === null) && ((values != null && (values instanceof Array)) || values === null) && ((locations != null && (locations instanceof Array)) || locations === null) && ((roles != null && (roles instanceof Array)) || roles === null) && ((others != null && (others instanceof Object)) || others === null)) {
                return <any>this.split$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Map(attributes, labels, types, values, locations, roles, others);
            } else if (((attributes != null && (attributes instanceof Array)) || attributes === null) && labels === undefined && types === undefined && values === undefined && locations === undefined && roles === undefined && others === undefined) {
                return <any>this.split$java_util_Collection(attributes);
            } else throw new Error('invalid overload');
        }

        public distribute(qn: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>, labels: Array<org.openprovenance.prov.model.LangString>, values: Array<org.openprovenance.prov.model.Value>, locations: Array<org.openprovenance.prov.model.Location>, types: Array<org.openprovenance.prov.model.Type>, roles: Array<org.openprovenance.prov.model.Role>, others: Array<org.openprovenance.prov.model.Other>) {
            const uri: string = qn.getUri();
            if (ProvUtilities.PROV_LABEL_URI_$LI$() === uri){
                for(let index233=0; index233 < attributes.length; index233++) {
                    let attr = attributes[index233];
                    {
                        const ls: org.openprovenance.prov.model.LangString = <org.openprovenance.prov.model.LangString><any>attr.getValue();
                        /* add */(labels.push(ls)>0);
                    }
                }
                return;
            }
            if (ProvUtilities.PROV_TYPE_URI_$LI$() === uri){
                for(let index234=0; index234 < attributes.length; index234++) {
                    let attr = attributes[index234];
                    {
                        /* add */(types.push(<org.openprovenance.prov.model.Type><any>attr)>0);
                    }
                }
                return;
            }
            if (ProvUtilities.PROV_LOCATION_URI_$LI$() === uri){
                for(let index235=0; index235 < attributes.length; index235++) {
                    let attr = attributes[index235];
                    {
                        /* add */(locations.push(<org.openprovenance.prov.model.Location><any>attr)>0);
                    }
                }
                return;
            }
            if (ProvUtilities.PROV_VALUE_URI_$LI$() === uri){
                for(let index236=0; index236 < attributes.length; index236++) {
                    let attr = attributes[index236];
                    {
                        /* add */(values.push(<org.openprovenance.prov.model.Value><any>attr)>0);
                    }
                }
                return;
            }
            if (ProvUtilities.PROV_ROLE_URI_$LI$() === uri){
                for(let index237=0; index237 < attributes.length; index237++) {
                    let attr = attributes[index237];
                    {
                        /* add */(roles.push(<org.openprovenance.prov.model.Role><any>attr)>0);
                    }
                }
                return;
            }
            for(let index238=0; index238 < attributes.length; index238++) {
                let attr = attributes[index238];
                {
                    /* add */(others.push(<org.openprovenance.prov.model.Other><any>attr)>0);
                }
            }
        }

        populateAttributes(attributes: Array<org.openprovenance.prov.model.Attribute>, label: Array<org.openprovenance.prov.model.LangString>, location: Array<org.openprovenance.prov.model.Location>, type: Array<org.openprovenance.prov.model.Type>, role: Array<org.openprovenance.prov.model.Role>, other: Array<org.openprovenance.prov.model.Other>, value: org.openprovenance.prov.model.Value[]) {
            let foundValue: boolean = false;
            if (attributes != null){
                for(let index239=0; index239 < attributes.length; index239++) {
                    let attribute = attributes[index239];
                    {
                        switch((attribute.getKind())) {
                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                            /* add */(type.push(<org.openprovenance.prov.model.Type><any>attribute)>0);
                            break;
                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                            /* add */(label.push(<org.openprovenance.prov.model.LangString><any>(<org.openprovenance.prov.model.Label><any>attribute).getValue())>0);
                            break;
                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                            /* add */(role.push(<org.openprovenance.prov.model.Role><any>attribute)>0);
                            break;
                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                            /* add */(location.push(<org.openprovenance.prov.model.Location><any>attribute)>0);
                            break;
                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                            if (!foundValue){
                                foundValue = true;
                                value[0] = <org.openprovenance.prov.model.Value><any>attribute;
                            }
                            break;
                        case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_KEY:
                            break;
                        case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                            /* add */(other.push(<org.openprovenance.prov.model.Other><any>attribute)>0);
                        }
                    }
                }
            }
        }
    }
    ProvUtilities["__class"] = "org.openprovenance.prov.vanilla.ProvUtilities";

}

