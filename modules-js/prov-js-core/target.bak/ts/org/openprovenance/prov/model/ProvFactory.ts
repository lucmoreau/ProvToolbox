/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * A stateless factory for PROV objects.
     * @param {*} of
     * @class
     */
    export abstract class ProvFactory implements org.openprovenance.prov.model.LiteralConstructor, org.openprovenance.prov.model.ModelConstructor, org.openprovenance.prov.model.ModelConstructorExtension {
        public static packageList: string = "org.openprovenance.prov.xml:org.openprovenance.prov.xml.validation";

        static fileName: string = "toolbox.properties";

        static toolboxVersion: string; public static toolboxVersion_$LI$(): string { if (ProvFactory.toolboxVersion == null) { ProvFactory.toolboxVersion = /* getProperty */((m,k) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { return m.entries[i].value; } return null; })(<any>ProvFactory.getPropertiesFromClasspath(ProvFactory.fileName), "toolbox.version"); }  return ProvFactory.toolboxVersion; }

        static getPropertiesFromClasspath(propFileName: string): any {
            const props: any = <any>new Object();
            const inputStream: { str: string, cursor: number } = ProvFactory.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream == null){
                return null;
            }
            try {
                props.load(inputStream);
            } catch(ee) {
                return null;
            }
            return props;
        }

        public static printURI(u: java.net.URI): string {
            return u.toString();
        }

        dataFactory: javax.xml.datatype.DatatypeFactory;

        of: org.openprovenance.prov.model.ObjectFactory;

        public constructor(of: org.openprovenance.prov.model.ObjectFactory) {
            if (this.dataFactory === undefined) { this.dataFactory = null; }
            if (this.of === undefined) { this.of = null; }
            this.name = null;
            this.util = new org.openprovenance.prov.model.ProvUtilities();
            this.of = of;
            this.init();
        }

        public addAttribute(a: org.openprovenance.prov.model.HasOther, o: org.openprovenance.prov.model.Other) {
            /* add */(a.getOther().push(o)>0);
        }

        public addAttributes$org_openprovenance_prov_model_ActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(from: org.openprovenance.prov.model.ActedOnBehalfOf, to: org.openprovenance.prov.model.ActedOnBehalfOf): org.openprovenance.prov.model.ActedOnBehalfOf {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes(from?: any, to?: any): any {
            if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_ActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_Activity$org_openprovenance_prov_model_Activity(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_Agent$org_openprovenance_prov_model_Agent(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_Entity$org_openprovenance_prov_model_Entity(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_Used$org_openprovenance_prov_model_Used(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasEndedBy$org_openprovenance_prov_model_WasEndedBy(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasInformedBy$org_openprovenance_prov_model_WasInformedBy(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(from, to);
            } else if (((from != null && (from.constructor != null && from.constructor["__interfaces"] != null && from.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || from === null) && ((to != null && (to.constructor != null && to.constructor["__interfaces"] != null && to.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || to === null)) {
                return <any>this.addAttributes$org_openprovenance_prov_model_WasStartedBy$org_openprovenance_prov_model_WasStartedBy(from, to);
            } else throw new Error('invalid overload');
        }

        public addAttributes$org_openprovenance_prov_model_Activity$org_openprovenance_prov_model_Activity(from: org.openprovenance.prov.model.Activity, to: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.Activity {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_Agent$org_openprovenance_prov_model_Agent(from: org.openprovenance.prov.model.Agent, to: org.openprovenance.prov.model.Agent): org.openprovenance.prov.model.Agent {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_Entity$org_openprovenance_prov_model_Entity(from: org.openprovenance.prov.model.Entity, to: org.openprovenance.prov.model.Entity): org.openprovenance.prov.model.Entity {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_Used$org_openprovenance_prov_model_Used(from: org.openprovenance.prov.model.Used, to: org.openprovenance.prov.model.Used): org.openprovenance.prov.model.Used {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(from: org.openprovenance.prov.model.WasAssociatedWith, to: org.openprovenance.prov.model.WasAssociatedWith): org.openprovenance.prov.model.WasAssociatedWith {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(from: org.openprovenance.prov.model.WasAttributedTo, to: org.openprovenance.prov.model.WasAttributedTo): org.openprovenance.prov.model.WasAttributedTo {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(from: org.openprovenance.prov.model.WasDerivedFrom, to: org.openprovenance.prov.model.WasDerivedFrom): org.openprovenance.prov.model.WasDerivedFrom {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasEndedBy$org_openprovenance_prov_model_WasEndedBy(from: org.openprovenance.prov.model.WasEndedBy, to: org.openprovenance.prov.model.WasEndedBy): org.openprovenance.prov.model.WasEndedBy {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(from: org.openprovenance.prov.model.WasGeneratedBy, to: org.openprovenance.prov.model.WasGeneratedBy): org.openprovenance.prov.model.WasGeneratedBy {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(from: org.openprovenance.prov.model.WasInfluencedBy, to: org.openprovenance.prov.model.WasInfluencedBy): org.openprovenance.prov.model.WasInfluencedBy {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasInformedBy$org_openprovenance_prov_model_WasInformedBy(from: org.openprovenance.prov.model.WasInformedBy, to: org.openprovenance.prov.model.WasInformedBy): org.openprovenance.prov.model.WasInformedBy {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(from: org.openprovenance.prov.model.WasInvalidatedBy, to: org.openprovenance.prov.model.WasInvalidatedBy): org.openprovenance.prov.model.WasInvalidatedBy {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addAttributes$org_openprovenance_prov_model_WasStartedBy$org_openprovenance_prov_model_WasStartedBy(from: org.openprovenance.prov.model.WasStartedBy, to: org.openprovenance.prov.model.WasStartedBy): org.openprovenance.prov.model.WasStartedBy {
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLabel(), from.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getType(), from.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getLocation(), from.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getRole(), from.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(to.getOther(), from.getOther());
            return to;
        }

        public addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String(a: org.openprovenance.prov.model.HasLabel, label: string) {
            /* add */(a.getLabel().push(this.newInternationalizedString$java_lang_String(label))>0);
        }

        public addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String$java_lang_String(a: org.openprovenance.prov.model.HasLabel, label: string, language: string) {
            /* add */(a.getLabel().push(this.newInternationalizedString$java_lang_String$java_lang_String(label, language))>0);
        }

        public addLabel(a?: any, label?: any, language?: any) {
            if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) || a === null) && ((typeof label === 'string') || label === null) && ((typeof language === 'string') || language === null)) {
                return <any>this.addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String$java_lang_String(a, label, language);
            } else if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) || a === null) && ((typeof label === 'string') || label === null) && language === undefined) {
                return <any>this.addLabel$org_openprovenance_prov_model_HasLabel$java_lang_String(a, label);
            } else throw new Error('invalid overload');
        }

        public addPrimarySourceType(a: org.openprovenance.prov.model.HasType) {
            /* add */(a.getType().push(this.newType(this.getName().PROV_PRIMARY_SOURCE, this.getName().PROV_QUALIFIED_NAME))>0);
        }

        public addQuotationType(a: org.openprovenance.prov.model.HasType) {
            /* add */(a.getType().push(this.newType(this.getName().PROV_QUOTATION, this.getName().PROV_QUALIFIED_NAME))>0);
        }

        public addRevisionType(a: org.openprovenance.prov.model.HasType) {
            /* add */(a.getType().push(this.newType(this.getName().PROV_REVISION, this.getName().PROV_QUALIFIED_NAME))>0);
        }

        public addBundleType(a: org.openprovenance.prov.model.HasType) {
            /* add */(a.getType().push(this.newType(this.getName().PROV_BUNDLE, this.getName().PROV_QUALIFIED_NAME))>0);
        }

        public addRole(a: org.openprovenance.prov.model.HasRole, role: org.openprovenance.prov.model.Role) {
            if (role != null){
                /* add */(a.getRole().push(role)>0);
            }
        }

        public addType$org_openprovenance_prov_model_HasType$java_lang_Object$org_openprovenance_prov_model_QualifiedName(a: org.openprovenance.prov.model.HasType, o: any, type: org.openprovenance.prov.model.QualifiedName) {
            /* add */(a.getType().push(this.newType(o, type))>0);
        }

        public addType(a?: any, o?: any, type?: any) {
            if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) || a === null) && ((o != null) || o === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return <any>this.addType$org_openprovenance_prov_model_HasType$java_lang_Object$org_openprovenance_prov_model_QualifiedName(a, o, type);
            } else if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) || a === null) && ((o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Type") >= 0)) || o === null) && type === undefined) {
                return <any>this.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(a, o);
            } else if (((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) || a === null) && ((o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || o === null) && type === undefined) {
                return <any>this.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_QualifiedName(a, o);
            } else throw new Error('invalid overload');
        }

        public addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(a: org.openprovenance.prov.model.HasType, type: org.openprovenance.prov.model.Type) {
            /* add */(a.getType().push(type)>0);
        }

        public addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_QualifiedName(a: org.openprovenance.prov.model.HasType, type: org.openprovenance.prov.model.QualifiedName) {
            /* add */(a.getType().push(this.newType(type, this.getName().PROV_QUALIFIED_NAME))>0);
        }

        public base64Decoding(s: string): number[] {
            return java.util.Base64.getDecoder().decode(s);
        }

        public base64Encoding(b: number[]): string {
            return java.util.Base64.getEncoder().encodeToString(b);
        }

        public getLabel(e: org.openprovenance.prov.model.HasLabel): string {
            const labels: Array<org.openprovenance.prov.model.LangString> = e.getLabel();
            if ((labels == null) || (/* isEmpty */(labels.length == 0)))return null;
            if (e != null && (e.constructor != null && e.constructor["__interfaces"] != null && e.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0))return /* get */labels[0].getValue();
            return "pFact: label TODO";
        }

        /*private*/ name: org.openprovenance.prov.model.Name;

        public getName(): org.openprovenance.prov.model.Name {
            if (this.name == null){
                this.name = new org.openprovenance.prov.model.Name(this);
            }
            return this.name;
        }

        public getObjectFactory(): org.openprovenance.prov.model.ObjectFactory {
            return this.of;
        }

        public getPackageList(): string {
            return ProvFactory.packageList;
        }

        public abstract getSerializer(): org.openprovenance.prov.model.ProvSerialiser;

        public getRole(e: org.openprovenance.prov.model.HasOther): string {
            return "pFact: role TODO";
        }

        public getType(e: org.openprovenance.prov.model.HasOther): Array<org.openprovenance.prov.model.Type> {
            if (e != null && (e.constructor != null && e.constructor["__interfaces"] != null && e.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0))return (<org.openprovenance.prov.model.HasType><any>e).getType();
            const res: Array<org.openprovenance.prov.model.Type> = <any>([]);
            /* add */(res.push(this.newType("pFact: type TODO", this.getName().XSD_STRING))>0);
            return res;
        }

        public getVersion(): string {
            return ProvFactory.toolboxVersion_$LI$();
        }

        public hexDecoding(s: string): number[] {
            const byteArray: number[] = new java.math.BigInteger(s, 16).toByteArray();
            if (byteArray[0] === 0){
                const output: number[] = (s => { let a=[]; while(s-->0) a.push(0); return a; })(byteArray.length - 1);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(byteArray, 1, output, 0, output.length);
                return output;
            }
            return byteArray;
        }

        public hexEncoding(b: number[]): string {
            const bigInteger: java.math.BigInteger = new java.math.BigInteger(1, b);
            return bigInteger.toString(16);
        }

        init() {
            try {
                this.dataFactory = javax.xml.datatype.DatatypeFactory.newInstance();
            } catch(ex) {
                throw Object.defineProperty(new Error(ex.message), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }

        public newActedOnBehalfOf$org_openprovenance_prov_model_ActedOnBehalfOf(u: org.openprovenance.prov.model.ActedOnBehalfOf): org.openprovenance.prov.model.ActedOnBehalfOf {
            const u1: org.openprovenance.prov.model.ActedOnBehalfOf = this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(u.getId(), u.getDelegate(), u.getResponsible(), u.getActivity(), null);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            return u1;
        }

        public newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.ActedOnBehalfOf {
            const res: org.openprovenance.prov.model.ActedOnBehalfOf = this.of.createActedOnBehalfOf();
            res.setId(id);
            res.setActivity(activity);
            res.setDelegate(delegate);
            res.setResponsible(responsible);
            return res;
        }

        public newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, delegate: org.openprovenance.prov.model.QualifiedName, responsible: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.ActedOnBehalfOf {
            const res: org.openprovenance.prov.model.ActedOnBehalfOf = this.of.createActedOnBehalfOf();
            res.setId(id);
            res.setActivity(activity);
            res.setDelegate(delegate);
            res.setResponsible(responsible);
            this.setAttributes(res, attributes);
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
            const res: org.openprovenance.prov.model.ActedOnBehalfOf = this.newActedOnBehalfOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, delegate, responsible, null, null);
            return res;
        }

        public newActivity$org_openprovenance_prov_model_Activity(a: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.Activity {
            const res: org.openprovenance.prov.model.Activity = this.newActivity$org_openprovenance_prov_model_QualifiedName(a.getId());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getType(), a.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getLabel(), a.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getLocation(), a.getLocation());
            res.setStartTime(a.getStartTime());
            res.setEndTime(a.getEndTime());
            return res;
        }

        public newActivity$org_openprovenance_prov_model_QualifiedName(a: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Activity {
            const res: org.openprovenance.prov.model.Activity = this.of.createActivity();
            res.setId(a);
            return res;
        }

        public newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(q: org.openprovenance.prov.model.QualifiedName, label: string): org.openprovenance.prov.model.Activity {
            const res: org.openprovenance.prov.model.Activity = this.newActivity$org_openprovenance_prov_model_QualifiedName(q);
            if (label != null)/* add */(res.getLabel().push(this.newInternationalizedString$java_lang_String(label))>0);
            return res;
        }

        public newActivity$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, startTime: javax.xml.datatype.XMLGregorianCalendar, endTime: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Activity {
            const res: org.openprovenance.prov.model.Activity = this.newActivity$org_openprovenance_prov_model_QualifiedName(id);
            res.setStartTime(startTime);
            res.setEndTime(endTime);
            this.setAttributes(res, attributes);
            return res;
        }

        public newActivity(id?: any, startTime?: any, endTime?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((startTime != null && startTime instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || startTime === null) && ((endTime != null && endTime instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || endTime === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newActivity$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, startTime, endTime, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof startTime === 'string') || startTime === null) && endTime === undefined && attributes === undefined) {
                return <any>this.newActivity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, startTime);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                return <any>this.newActivity$org_openprovenance_prov_model_Activity(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && startTime === undefined && endTime === undefined && attributes === undefined) {
                return <any>this.newActivity$org_openprovenance_prov_model_QualifiedName(id);
            } else throw new Error('invalid overload');
        }

        public newAgent$org_openprovenance_prov_model_Agent(a: org.openprovenance.prov.model.Agent): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.newAgent$org_openprovenance_prov_model_QualifiedName(a.getId());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getType(), a.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getLabel(), a.getLabel());
            return res;
        }

        public newAgent$org_openprovenance_prov_model_QualifiedName(ag: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.of.createAgent();
            res.setId(ag);
            return res;
        }

        public newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.newAgent$org_openprovenance_prov_model_QualifiedName(id);
            this.setAttributes(res, attributes);
            return res;
        }

        /**
         * Creates a new {@link Agent} with provided identifier and attributes
         * @param {*} id a {@link QualifiedName} for the agent
         * @param {*[]} attributes a collection of {@link Attribute} for the agent
         * @return {*} an object of type {@link Agent}
         */
        public newAgent(id?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newAgent$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof attributes === 'string') || attributes === null)) {
                return <any>this.newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || id === null) && attributes === undefined) {
                return <any>this.newAgent$org_openprovenance_prov_model_Agent(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && attributes === undefined) {
                return <any>this.newAgent$org_openprovenance_prov_model_QualifiedName(id);
            } else throw new Error('invalid overload');
        }

        public newAgent$org_openprovenance_prov_model_QualifiedName$java_lang_String(ag: org.openprovenance.prov.model.QualifiedName, label: string): org.openprovenance.prov.model.Agent {
            const res: org.openprovenance.prov.model.Agent = this.newAgent$org_openprovenance_prov_model_QualifiedName(ag);
            if (label != null)/* add */(res.getLabel().push(this.newInternationalizedString$java_lang_String(label))>0);
            return res;
        }

        public newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1: org.openprovenance.prov.model.QualifiedName, entity2: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.AlternateOf {
            const res: org.openprovenance.prov.model.AlternateOf = this.of.createAlternateOf();
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
        public newAlternateOf(entity1?: any, entity2?: any): any {
            if (((entity1 != null && (entity1.constructor != null && entity1.constructor["__interfaces"] != null && entity1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity1 === null) && ((entity2 != null && (entity2.constructor != null && entity2.constructor["__interfaces"] != null && entity2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity2 === null)) {
                return <any>this.newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(entity1, entity2);
            } else if (((entity1 != null && (entity1.constructor != null && entity1.constructor["__interfaces"] != null && entity1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || entity1 === null) && entity2 === undefined) {
                return <any>this.newAlternateOf$org_openprovenance_prov_model_AlternateOf(entity1);
            } else throw new Error('invalid overload');
        }

        public newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName: org.openprovenance.prov.model.QualifiedName, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }

        public newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(kind: org.openprovenance.prov.model.Attribute.AttributeKind, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }

        public newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace: string, localName: string, prefix: string, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Attribute {
            let res: org.openprovenance.prov.model.Attribute;
            res = this.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, localName, prefix), value, type);
            return res;
        }

        public newAttribute(namespace?: any, localName?: any, prefix?: any, value?: any, type?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof localName === 'string') || localName === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return <any>this.newAttribute$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix, value, type);
            } else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((localName != null) || localName === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                return <any>this.newAttribute$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix);
            } else if (((typeof namespace === 'number') || namespace === null) && ((localName != null) || localName === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                return <any>this.newAttribute$org_openprovenance_prov_model_Attribute_AttributeKind$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, localName, prefix);
            } else throw new Error('invalid overload');
        }

        public newDerivedByInsertionFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keyEntitySet: Array<org.openprovenance.prov.model.Entry>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByInsertionFrom {
            const res: org.openprovenance.prov.model.DerivedByInsertionFrom = this.of.createDerivedByInsertionFrom();
            res.setId(id);
            res.setNewDictionary(after);
            res.setOldDictionary(before);
            if (keyEntitySet != null)/* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getKeyEntityPair(), keyEntitySet);
            this.setAttributes(res, attributes);
            return res;
        }

        public newDerivedByRemovalFrom(id: org.openprovenance.prov.model.QualifiedName, after: org.openprovenance.prov.model.QualifiedName, before: org.openprovenance.prov.model.QualifiedName, keys: Array<org.openprovenance.prov.model.Key>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.DerivedByRemovalFrom {
            const res: org.openprovenance.prov.model.DerivedByRemovalFrom = this.of.createDerivedByRemovalFrom();
            res.setId(id);
            res.setNewDictionary(after);
            res.setOldDictionary(before);
            if (keys != null)/* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getKey(), keys);
            this.setAttributes(res, attributes);
            return res;
        }

        public newDictionaryMembership(dict: org.openprovenance.prov.model.QualifiedName, entitySet: Array<org.openprovenance.prov.model.Entry>): org.openprovenance.prov.model.DictionaryMembership {
            const res: org.openprovenance.prov.model.DictionaryMembership = this.of.createDictionaryMembership();
            res.setDictionary(dict);
            if (entitySet != null)/* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getKeyEntityPair(), entitySet);
            return res;
        }

        public newDocument$(): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.of.createDocument();
            return res;
        }

        public newDocument$org_openprovenance_prov_model_Activity_A$org_openprovenance_prov_model_Entity_A$org_openprovenance_prov_model_Agent_A$org_openprovenance_prov_model_Statement_A(ps: org.openprovenance.prov.model.Activity[], as: org.openprovenance.prov.model.Entity[], ags: org.openprovenance.prov.model.Agent[], lks: org.openprovenance.prov.model.Statement[]): org.openprovenance.prov.model.Document {
            return this.newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(((ps == null) ? null : /* asList */ps.slice(0)), ((as == null) ? null : /* asList */as.slice(0)), ((ags == null) ? null : /* asList */ags.slice(0)), ((lks == null) ? null : /* asList */lks.slice(0)));
        }

        public newDocument(ps?: any, as?: any, ags?: any, lks?: any): any {
            if (((ps != null && ps instanceof <any>Array && (ps.length == 0 || ps[0] == null ||(ps[0] != null && (ps[0].constructor != null && ps[0].constructor["__interfaces"] != null && ps[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)))) || ps === null) && ((as != null && as instanceof <any>Array && (as.length == 0 || as[0] == null ||(as[0] != null && (as[0].constructor != null && as[0].constructor["__interfaces"] != null && as[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)))) || as === null) && ((ags != null && ags instanceof <any>Array && (ags.length == 0 || ags[0] == null ||(ags[0] != null && (ags[0].constructor != null && ags[0].constructor["__interfaces"] != null && ags[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)))) || ags === null) && ((lks != null && lks instanceof <any>Array && (lks.length == 0 || lks[0] == null ||(lks[0] != null && (lks[0].constructor != null && lks[0].constructor["__interfaces"] != null && lks[0].constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Statement") >= 0)))) || lks === null)) {
                return <any>this.newDocument$org_openprovenance_prov_model_Activity_A$org_openprovenance_prov_model_Entity_A$org_openprovenance_prov_model_Agent_A$org_openprovenance_prov_model_Statement_A(ps, as, ags, lks);
            } else if (((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                return <any>this.newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps, as, ags, lks);
            } else if (((ps != null && ps instanceof <any>org.openprovenance.prov.model.Namespace) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && lks === undefined) {
                return <any>this.newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(ps, as, ags);
            } else if (((ps != null && (ps.constructor != null && ps.constructor["__interfaces"] != null && ps.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Document") >= 0)) || ps === null) && as === undefined && ags === undefined && lks === undefined) {
                return <any>this.newDocument$org_openprovenance_prov_model_Document(ps);
            } else if (ps === undefined && as === undefined && ags === undefined && lks === undefined) {
                return <any>this.newDocument$();
            } else throw new Error('invalid overload');
        }

        public newDocument$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(ps: Array<org.openprovenance.prov.model.Activity>, as: Array<org.openprovenance.prov.model.Entity>, ags: Array<org.openprovenance.prov.model.Agent>, lks: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.of.createDocument();
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ps);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), as);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), ags);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), lks);
            return res;
        }

        public newDocument$org_openprovenance_prov_model_Document(graph: org.openprovenance.prov.model.Document): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.of.createDocument();
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), graph.getStatementOrBundle());
            if (graph.getNamespace() != null){
                res.setNamespace(new org.openprovenance.prov.model.Namespace(graph.getNamespace()));
            }
            return res;
        }

        public newDocument$org_openprovenance_prov_model_Namespace$java_util_Collection$java_util_Collection(namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>, bundles: Array<org.openprovenance.prov.model.Bundle>): org.openprovenance.prov.model.Document {
            const res: org.openprovenance.prov.model.Document = this.of.createDocument();
            res.setNamespace(namespace);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), statements);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatementOrBundle(), bundles);
            return res;
        }

        public newDuration$int(durationInMilliSeconds: number): javax.xml.datatype.Duration {
            const dur: javax.xml.datatype.Duration = this.dataFactory.newDuration(durationInMilliSeconds);
            return dur;
        }

        public newDuration$java_lang_String(lexicalRepresentation: string): javax.xml.datatype.Duration {
            const dur: javax.xml.datatype.Duration = this.dataFactory.newDuration(lexicalRepresentation);
            return dur;
        }

        public newDuration(lexicalRepresentation?: any): any {
            if (((typeof lexicalRepresentation === 'string') || lexicalRepresentation === null)) {
                return <any>this.newDuration$java_lang_String(lexicalRepresentation);
            } else if (((typeof lexicalRepresentation === 'number') || lexicalRepresentation === null)) {
                return <any>this.newDuration$int(lexicalRepresentation);
            } else throw new Error('invalid overload');
        }

        public newEntity$org_openprovenance_prov_model_Entity(e: org.openprovenance.prov.model.Entity): org.openprovenance.prov.model.Entity {
            const res: org.openprovenance.prov.model.Entity = this.newEntity$org_openprovenance_prov_model_QualifiedName(e.getId());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getOther(), e.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getType(), e.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getLabel(), e.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getLocation(), e.getLocation());
            return res;
        }

        public newEntity$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Entity {
            const res: org.openprovenance.prov.model.Entity = this.of.createEntity();
            res.setId(id);
            return res;
        }

        public newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Entity {
            const res: org.openprovenance.prov.model.Entity = this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
            this.setAttributes(res, attributes);
            return res;
        }

        /**
         * Creates a new {@link Entity} with provided identifier and attributes
         * @param {*} id a {@link QualifiedName} for the entity
         * @param {*[]} attributes a collection of {@link Attribute} for the entity
         * @return {*} an object of type {@link Entity}
         */
        public newEntity(id?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newEntity$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((typeof attributes === 'string') || attributes === null)) {
                return <any>this.newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || id === null) && attributes === undefined) {
                return <any>this.newEntity$org_openprovenance_prov_model_Entity(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && attributes === undefined) {
                return <any>this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
            } else throw new Error('invalid overload');
        }

        public newEntity$org_openprovenance_prov_model_QualifiedName$java_lang_String(id: org.openprovenance.prov.model.QualifiedName, label: string): org.openprovenance.prov.model.Entity {
            const res: org.openprovenance.prov.model.Entity = this.newEntity$org_openprovenance_prov_model_QualifiedName(id);
            if (label != null)/* add */(res.getLabel().push(this.newInternationalizedString$java_lang_String(label))>0);
            return res;
        }

        /**
         * Factory method for Key-entity entries. Key-entity entries are used to identify the members of a dictionary.
         * @param {*} key indexing the entity in the dictionary
         * @param {*} entity a {@link QualifiedName} denoting an entity
         * @return {*} an instance of {@link Entry}
         */
        public newEntry(key: org.openprovenance.prov.model.Key, entity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Entry {
            const res: org.openprovenance.prov.model.Entry = this.of.createEntry();
            res.setKey(key);
            res.setEntity(entity);
            return res;
        }

        public newGDay(day: number): javax.xml.datatype.XMLGregorianCalendar {
            const cal: javax.xml.datatype.XMLGregorianCalendar = this.dataFactory.newXMLGregorianCalendar();
            cal.setDay(day);
            return cal;
        }

        public newGMonth(month: number): javax.xml.datatype.XMLGregorianCalendar {
            const cal: javax.xml.datatype.XMLGregorianCalendar = this.dataFactory.newXMLGregorianCalendar();
            cal.setMonth(month);
            return cal;
        }

        public newGMonthDay(month: number, day: number): javax.xml.datatype.XMLGregorianCalendar {
            const cal: javax.xml.datatype.XMLGregorianCalendar = this.dataFactory.newXMLGregorianCalendar();
            cal.setMonth(month);
            cal.setDay(day);
            return cal;
        }

        public newGYear(year: number): javax.xml.datatype.XMLGregorianCalendar {
            const cal: javax.xml.datatype.XMLGregorianCalendar = this.dataFactory.newXMLGregorianCalendar();
            cal.setYear(year);
            return cal;
        }

        public newHadMember$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName_A(collection: org.openprovenance.prov.model.QualifiedName, ...entities: org.openprovenance.prov.model.QualifiedName[]): org.openprovenance.prov.model.HadMember {
            const res: org.openprovenance.prov.model.HadMember = this.of.createHadMember();
            res.setCollection(collection);
            if (entities != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getEntity(), /* asList */entities.slice(0));
            }
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
                for(let index196=0; index196 < e.length; index196++) {
                    let q = e[index196];
                    {
                        /* add */(ll.push(q)>0);
                    }
                }
            }
            const res: org.openprovenance.prov.model.HadMember = this.of.createHadMember();
            res.setCollection(c);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getEntity(), ll);
            return res;
        }

        public newInternationalizedString$java_lang_String(s: string): org.openprovenance.prov.model.LangString {
            const res: org.openprovenance.prov.model.LangString = this.of.createInternationalizedString();
            res.setValue(s);
            return res;
        }

        public newInternationalizedString$java_lang_String$java_lang_String(s: string, lang: string): org.openprovenance.prov.model.LangString {
            const res: org.openprovenance.prov.model.LangString = this.of.createInternationalizedString();
            res.setValue(s);
            res.setLang(lang);
            return res;
        }

        public newInternationalizedString(s?: any, lang?: any): any {
            if (((typeof s === 'string') || s === null) && ((typeof lang === 'string') || lang === null)) {
                return <any>this.newInternationalizedString$java_lang_String$java_lang_String(s, lang);
            } else if (((typeof s === 'string') || s === null) && lang === undefined) {
                return <any>this.newInternationalizedString$java_lang_String(s);
            } else throw new Error('invalid overload');
        }

        public newISOTime(time: string): javax.xml.datatype.XMLGregorianCalendar {
            return this.newTime(/* getTime */(new Date(javax.xml.bind.DatatypeConverter.parseDateTime(time).getTime())));
        }

        public abstract newKey(o: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Key;

        public newLocation(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Location {
            const res: org.openprovenance.prov.model.Location = this.of.createLocation();
            res.setType(type);
            res.setValueFromObject(value);
            return res;
        }

        public newMentionOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(infra: org.openprovenance.prov.model.QualifiedName, supra: org.openprovenance.prov.model.QualifiedName, bundle: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.MentionOf {
            const res: org.openprovenance.prov.model.MentionOf = this.of.createMentionOf();
            res.setSpecificEntity(infra);
            res.setBundle(bundle);
            res.setGeneralEntity(supra);
            return res;
        }

        public newMentionOf(infra?: any, supra?: any, bundle?: any): any {
            if (((infra != null && (infra.constructor != null && infra.constructor["__interfaces"] != null && infra.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || infra === null) && ((supra != null && (supra.constructor != null && supra.constructor["__interfaces"] != null && supra.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || supra === null) && ((bundle != null && (bundle.constructor != null && bundle.constructor["__interfaces"] != null && bundle.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || bundle === null)) {
                return <any>this.newMentionOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(infra, supra, bundle);
            } else if (((infra != null && (infra.constructor != null && infra.constructor["__interfaces"] != null && infra.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || infra === null) && supra === undefined && bundle === undefined) {
                return <any>this.newMentionOf$org_openprovenance_prov_model_MentionOf(infra);
            } else throw new Error('invalid overload');
        }

        public newMentionOf$org_openprovenance_prov_model_MentionOf(r: org.openprovenance.prov.model.MentionOf): org.openprovenance.prov.model.MentionOf {
            const res: org.openprovenance.prov.model.MentionOf = this.of.createMentionOf();
            res.setSpecificEntity(r.getSpecificEntity());
            res.setBundle(r.getBundle());
            res.setGeneralEntity(r.getGeneralEntity());
            return res;
        }

        public newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, ps: Array<org.openprovenance.prov.model.Activity>, as: Array<org.openprovenance.prov.model.Entity>, ags: Array<org.openprovenance.prov.model.Agent>, lks: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            const res: org.openprovenance.prov.model.Bundle = this.of.createNamedBundle();
            res.setId(id);
            if (ps != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), ps);
            }
            if (as != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), as);
            }
            if (ags != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), ags);
            }
            if (lks != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), lks);
            }
            return res;
        }

        public newNamedBundle(id?: any, ps?: any, as?: any, ags?: any, lks?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && (ps instanceof Array)) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ((ags != null && (ags instanceof Array)) || ags === null) && ((lks != null && (lks instanceof Array)) || lks === null)) {
                return <any>this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection$java_util_Collection$java_util_Collection$java_util_Collection(id, ps, as, ags, lks);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && ps instanceof <any>org.openprovenance.prov.model.Namespace) || ps === null) && ((as != null && (as instanceof Array)) || as === null) && ags === undefined && lks === undefined) {
                return <any>this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id, ps, as);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((ps != null && (ps instanceof Array)) || ps === null) && as === undefined && ags === undefined && lks === undefined) {
                return <any>this.newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, ps);
            } else throw new Error('invalid overload');
        }

        public newNamedBundle$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, lks: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            const res: org.openprovenance.prov.model.Bundle = this.of.createNamedBundle();
            res.setId(id);
            if (lks != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), lks);
            }
            return res;
        }

        public newNamedBundle$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Namespace$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, namespace: org.openprovenance.prov.model.Namespace, statements: Array<org.openprovenance.prov.model.Statement>): org.openprovenance.prov.model.Bundle {
            const res: org.openprovenance.prov.model.Bundle = this.of.createNamedBundle();
            res.setId(id);
            res.setNamespace(namespace);
            if (statements != null){
                /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getStatement(), statements);
            }
            return res;
        }

        public newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName: org.openprovenance.prov.model.QualifiedName, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Other {
            if (value == null)return null;
            const res: org.openprovenance.prov.model.Other = this.of.createOther();
            res.setType(type);
            res.setValueFromObject(value);
            res.setElementName(elementName);
            return res;
        }

        public newOther$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace: string, local: string, prefix: string, value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Other {
            const elementName: org.openprovenance.prov.model.QualifiedName = this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
            return this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(elementName, value, type);
        }

        public newOther(namespace?: any, local?: any, prefix?: any, value?: any, type?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return <any>this.newOther$java_lang_String$java_lang_String$java_lang_String$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix, value, type);
            } else if (((namespace != null && (namespace.constructor != null && namespace.constructor["__interfaces"] != null && namespace.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || namespace === null) && ((local != null) || local === null) && ((prefix != null && (prefix.constructor != null && prefix.constructor["__interfaces"] != null && prefix.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || prefix === null) && value === undefined && type === undefined) {
                return <any>this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(namespace, local, prefix);
            } else throw new Error('invalid overload');
        }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace: string, local: string, prefix: string): org.openprovenance.prov.model.QualifiedName { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }

        public newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace: string, local: string, prefix: string, flag: org.openprovenance.prov.model.ProvUtilities.BuildFlag): org.openprovenance.prov.model.QualifiedName { throw new Error('cannot invoke abstract overloaded method... check your argument(s) type(s)'); }

        public newQualifiedName(namespace?: any, local?: any, prefix?: any, flag?: any): any {
            if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && ((typeof flag === 'number') || flag === null)) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String$org_openprovenance_prov_model_ProvUtilities_BuildFlag(namespace, local, prefix, flag);
            } else if (((typeof namespace === 'string') || namespace === null) && ((typeof local === 'string') || local === null) && ((typeof prefix === 'string') || prefix === null) && flag === undefined) {
                return <any>this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(namespace, local, prefix);
            } else if (((namespace != null && namespace instanceof <any>javax.xml.namespace.QName) || namespace === null) && local === undefined && prefix === undefined && flag === undefined) {
                return <any>this.newQualifiedName$javax_xml_namespace_QName(namespace);
            } else throw new Error('invalid overload');
        }

        public newQualifiedName$javax_xml_namespace_QName(qname: javax.xml.namespace.QName): org.openprovenance.prov.model.QualifiedName {
            return this.newQualifiedName$java_lang_String$java_lang_String$java_lang_String(qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix());
        }

        public newRole(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Role {
            if (value == null)return null;
            const res: org.openprovenance.prov.model.Role = this.of.createRole();
            res.setType(type);
            res.setValueFromObject(value);
            return res;
        }

        public newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific: org.openprovenance.prov.model.QualifiedName, general: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.SpecializationOf {
            const res: org.openprovenance.prov.model.SpecializationOf = this.of.createSpecializationOf();
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
        public newSpecializationOf(specific?: any, general?: any): any {
            if (((specific != null && (specific.constructor != null && specific.constructor["__interfaces"] != null && specific.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || specific === null) && ((general != null && (general.constructor != null && general.constructor["__interfaces"] != null && general.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || general === null)) {
                return <any>this.newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(specific, general);
            } else if (((specific != null && (specific.constructor != null && specific.constructor["__interfaces"] != null && specific.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || specific === null) && general === undefined) {
                return <any>this.newSpecializationOf$org_openprovenance_prov_model_SpecializationOf(specific);
            } else throw new Error('invalid overload');
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
            const res: org.openprovenance.prov.model.extension.QualifiedSpecializationOf = this.of.createQualifiedSpecializationOf();
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
        public newQualifiedAlternateOf(id: org.openprovenance.prov.model.QualifiedName, alt1: org.openprovenance.prov.model.QualifiedName, alt2: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedAlternateOf {
            const res: org.openprovenance.prov.model.extension.QualifiedAlternateOf = this.of.createQualifiedAlternateOf();
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
        public newQualifiedHadMember(id: org.openprovenance.prov.model.QualifiedName, c: org.openprovenance.prov.model.QualifiedName, e: Array<org.openprovenance.prov.model.QualifiedName>, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.extension.QualifiedHadMember {
            const ll: Array<org.openprovenance.prov.model.QualifiedName> = <any>([]);
            if (e != null){
                for(let index197=0; index197 < e.length; index197++) {
                    let q = e[index197];
                    {
                        /* add */(ll.push(q)>0);
                    }
                }
            }
            const res: org.openprovenance.prov.model.extension.QualifiedHadMember = this.of.createQualifiedHadMember();
            res.setId(id);
            res.setCollection(c);
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(res.getEntity(), e);
            return res;
        }

        public newTime(date: Date): javax.xml.datatype.XMLGregorianCalendar {
            const gc: Date = new Date();
            /* setTime */gc.setTime(date.getTime());
            return this.newXMLGregorianCalendar(gc);
        }

        public newTimeNow(): javax.xml.datatype.XMLGregorianCalendar {
            return this.newTime(new Date());
        }

        public newType(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Type {
            if (value == null)return null;
            const res: org.openprovenance.prov.model.Type = this.of.createType();
            res.setType(type);
            res.setValueFromObject(value);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.of.createUsed();
            res.setId(id);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, role: string, eid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName(id);
            res.setActivity(aid);
            if (role != null)this.addRole(res, this.newRole(role, this.getName().XSD_STRING));
            res.setEntity(eid);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName(id);
            res.setActivity(activity);
            res.setEntity(entity);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName(<org.openprovenance.prov.model.QualifiedName><any>null);
            res.setActivity(activity);
            res.setEntity(entity);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.Used {
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, activity, null, entity);
            res.setTime(time);
            this.setAttributes(res, attributes);
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
            const res: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, activity, null, entity);
            res.setTime(time);
            return res;
        }

        public newUsed$org_openprovenance_prov_model_Used(u: org.openprovenance.prov.model.Used): org.openprovenance.prov.model.Used {
            const u1: org.openprovenance.prov.model.Used = this.newUsed$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getEntity());
            u1.setTime(u.getTime());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            return u1;
        }

        public newValue$java_lang_String(value: string): org.openprovenance.prov.model.Value {
            return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, this.getName().XSD_STRING);
        }

        public newValue$int(value: number): org.openprovenance.prov.model.Value {
            return this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, this.getName().XSD_INT);
        }

        public newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value: any, type: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.Value {
            if (value == null)return null;
            const res: org.openprovenance.prov.model.Value = this.of.createValue();
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
        public newValue(value?: any, type?: any): any {
            if (((value != null) || value === null) && ((type != null && (type.constructor != null && type.constructor["__interfaces"] != null && type.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || type === null)) {
                return <any>this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(value, type);
            } else if (((typeof value === 'string') || value === null) && type === undefined) {
                return <any>this.newValue$java_lang_String(value);
            } else if (((typeof value === 'number') || value === null) && type === undefined) {
                return <any>this.newValue$int(value);
            } else throw new Error('invalid overload');
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAssociatedWith {
            return this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, <org.openprovenance.prov.model.QualifiedName><any>null, <org.openprovenance.prov.model.QualifiedName><any>null);
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAssociatedWith {
            const res: org.openprovenance.prov.model.WasAssociatedWith = this.of.createWasAssociatedWith();
            res.setId(id);
            res.setActivity(activity);
            res.setAgent(agent);
            return res;
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, ag: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAssociatedWith {
            const res: org.openprovenance.prov.model.WasAssociatedWith = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
            res.setPlan(plan);
            this.setAttributes(res, attributes);
            return res;
        }

        public newWasAssociatedWith(id?: any, a?: any, ag?: any, plan?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a, ag, plan, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && ((plan != null && (plan.constructor != null && plan.constructor["__interfaces"] != null && plan.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || plan === null) && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag, plan);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a != null && (a.constructor != null && a.constructor["__interfaces"] != null && a.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a === null) && ((ag != null && (ag.constructor != null && ag.constructor["__interfaces"] != null && ag.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || ag === null) && plan === undefined && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName(id);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || id === null) && a === undefined && ag === undefined && plan === undefined && attributes === undefined) {
                return <any>this.newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(id);
            } else throw new Error('invalid overload');
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.QualifiedName, ag: org.openprovenance.prov.model.QualifiedName, plan: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAssociatedWith {
            const res: org.openprovenance.prov.model.WasAssociatedWith = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a, ag);
            res.setPlan(plan);
            return res;
        }

        public newWasAssociatedWith$org_openprovenance_prov_model_WasAssociatedWith(u: org.openprovenance.prov.model.WasAssociatedWith): org.openprovenance.prov.model.WasAssociatedWith {
            const u1: org.openprovenance.prov.model.WasAssociatedWith = this.newWasAssociatedWith$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getAgent());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            u1.setPlan(u.getPlan());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
            return u1;
        }

        public newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasAttributedTo {
            const res: org.openprovenance.prov.model.WasAttributedTo = this.of.createWasAttributedTo();
            res.setId(id);
            res.setEntity(entity);
            res.setAgent(agent);
            return res;
        }

        public newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, agent: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasAttributedTo {
            const res: org.openprovenance.prov.model.WasAttributedTo = this.of.createWasAttributedTo();
            res.setId(id);
            res.setEntity(entity);
            res.setAgent(agent);
            this.setAttributes(res, attributes);
            return res;
        }

        public newWasAttributedTo(id?: any, entity?: any, agent?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, entity, agent, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((agent != null && (agent.constructor != null && agent.constructor["__interfaces"] != null && agent.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || agent === null) && attributes === undefined) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, agent);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || id === null) && entity === undefined && agent === undefined && attributes === undefined) {
                return <any>this.newWasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(id);
            } else throw new Error('invalid overload');
        }

        public newWasAttributedTo$org_openprovenance_prov_model_WasAttributedTo(u: org.openprovenance.prov.model.WasAttributedTo): org.openprovenance.prov.model.WasAttributedTo {
            const u1: org.openprovenance.prov.model.WasAttributedTo = this.newWasAttributedTo$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getEntity(), u.getAgent());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            return u1;
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasDerivedFrom {
            const res: org.openprovenance.prov.model.WasDerivedFrom = this.of.createWasDerivedFrom();
            res.setId(id);
            res.setUsedEntity(e1);
            res.setGeneratedEntity(e2);
            return res;
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(e2: org.openprovenance.prov.model.QualifiedName, e1: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasDerivedFrom {
            const res: org.openprovenance.prov.model.WasDerivedFrom = this.of.createWasDerivedFrom();
            res.setUsedEntity(e1);
            res.setGeneratedEntity(e2);
            return res;
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, aid1: org.openprovenance.prov.model.QualifiedName, aid2: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, did1: org.openprovenance.prov.model.QualifiedName, did2: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasDerivedFrom {
            const res: org.openprovenance.prov.model.WasDerivedFrom = this.of.createWasDerivedFrom();
            res.setId(id);
            res.setUsedEntity(aid2);
            res.setGeneratedEntity(aid1);
            res.setActivity(aid);
            res.setGeneration(did1);
            res.setUsage(did2);
            this.setAttributes(res, attributes);
            return res;
        }

        public newWasDerivedFrom(id?: any, aid1?: any, aid2?: any, aid?: any, did1?: any, did2?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((aid1 != null && (aid1.constructor != null && aid1.constructor["__interfaces"] != null && aid1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid1 === null) && ((aid2 != null && (aid2.constructor != null && aid2.constructor["__interfaces"] != null && aid2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid2 === null) && ((aid != null && (aid.constructor != null && aid.constructor["__interfaces"] != null && aid.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid === null) && ((did1 != null && (did1.constructor != null && did1.constructor["__interfaces"] != null && did1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || did1 === null) && ((did2 != null && (did2.constructor != null && did2.constructor["__interfaces"] != null && did2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || did2 === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, aid1, aid2, aid, did1, did2, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((aid1 != null && (aid1.constructor != null && aid1.constructor["__interfaces"] != null && aid1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid1 === null) && ((aid2 != null && (aid2.constructor != null && aid2.constructor["__interfaces"] != null && aid2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid2 === null) && aid === undefined && did1 === undefined && did2 === undefined && attributes === undefined) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid1, aid2);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((aid1 != null && (aid1.constructor != null && aid1.constructor["__interfaces"] != null && aid1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || aid1 === null) && aid2 === undefined && aid === undefined && did1 === undefined && did2 === undefined && attributes === undefined) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, aid1);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || id === null) && aid1 === undefined && aid2 === undefined && aid === undefined && did1 === undefined && did2 === undefined && attributes === undefined) {
                return <any>this.newWasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(id);
            } else throw new Error('invalid overload');
        }

        public newWasDerivedFrom$org_openprovenance_prov_model_WasDerivedFrom(d: org.openprovenance.prov.model.WasDerivedFrom): org.openprovenance.prov.model.WasDerivedFrom {
            const wdf: org.openprovenance.prov.model.WasDerivedFrom = this.newWasDerivedFrom$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(d.getId(), d.getGeneratedEntity(), d.getUsedEntity());
            wdf.setActivity(d.getActivity());
            wdf.setGeneration(d.getGeneration());
            wdf.setUsage(d.getUsage());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wdf.getOther(), d.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wdf.getType(), d.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wdf.getLabel(), d.getLabel());
            return wdf;
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.of.createWasEndedBy();
            res.setId(id);
            return res;
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.of.createWasEndedBy();
            res.setId(id);
            res.setActivity(activity);
            res.setTrigger(trigger);
            return res;
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
            res.setEnder(ender);
            return res;
        }

        public newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, ender: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasEndedBy {
            const res: org.openprovenance.prov.model.WasEndedBy = this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
            res.setTime(time);
            res.setEnder(ender);
            this.setAttributes(res, attributes);
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

        public newWasEndedBy$org_openprovenance_prov_model_WasEndedBy(u: org.openprovenance.prov.model.WasEndedBy): org.openprovenance.prov.model.WasEndedBy {
            const u1: org.openprovenance.prov.model.WasEndedBy = this.newWasEndedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getTrigger());
            u1.setEnder(u.getEnder());
            u1.setTime(u.getTime());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u1.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            return u1;
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(a: org.openprovenance.prov.model.Entity, role: string, p: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.WasGeneratedBy {
            return this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(<org.openprovenance.prov.model.QualifiedName><any>null, a, role, p);
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasGeneratedBy {
            const res: org.openprovenance.prov.model.WasGeneratedBy = this.of.createWasGeneratedBy();
            res.setId(id);
            return res;
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id: org.openprovenance.prov.model.QualifiedName, a: org.openprovenance.prov.model.Entity, role: string, p: org.openprovenance.prov.model.Activity): org.openprovenance.prov.model.WasGeneratedBy {
            const res: org.openprovenance.prov.model.WasGeneratedBy = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a.getId(), p.getId());
            if (role != null)this.addRole(res, this.newRole(role, this.getName().XSD_STRING));
            return res;
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, role: string, pid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasGeneratedBy {
            const res: org.openprovenance.prov.model.WasGeneratedBy = this.of.createWasGeneratedBy();
            res.setId(id);
            res.setActivity(pid);
            res.setEntity(aid);
            if (role != null)this.addRole(res, this.newRole(role, this.getName().XSD_STRING));
            return res;
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasGeneratedBy {
            const res: org.openprovenance.prov.model.WasGeneratedBy = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, null, activity);
            return res;
        }

        public newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasGeneratedBy {
            const res: org.openprovenance.prov.model.WasGeneratedBy = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, null, activity);
            res.setTime(time);
            this.setAttributes(res, attributes);
            return res;
        }

        public newWasGeneratedBy(id?: any, entity?: any, activity?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || time === null) && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_Entity$java_lang_String$org_openprovenance_prov_model_Activity(id, entity, activity, time);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((typeof activity === 'string') || activity === null) && ((time != null && (time.constructor != null && time.constructor["__interfaces"] != null && time.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || time === null) && attributes === undefined) {
                return <any>this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(id, entity, activity, time);
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

        public newWasGeneratedBy$org_openprovenance_prov_model_WasGeneratedBy(g: org.openprovenance.prov.model.WasGeneratedBy): org.openprovenance.prov.model.WasGeneratedBy {
            const wgb: org.openprovenance.prov.model.WasGeneratedBy = this.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_lang_String$org_openprovenance_prov_model_QualifiedName(g.getId(), g.getEntity(), null, g.getActivity());
            wgb.setId(g.getId());
            wgb.setTime(g.getTime());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wgb.getOther(), g.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wgb.getRole(), g.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wgb.getType(), g.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wgb.getLabel(), g.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wgb.getLocation(), g.getLocation());
            return wgb;
        }

        public newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, influencee: org.openprovenance.prov.model.QualifiedName, influencer: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInfluencedBy {
            const res: org.openprovenance.prov.model.WasInfluencedBy = this.of.createWasInfluencedBy();
            res.setId(id);
            res.setInfluencee(influencee);
            res.setInfluencer(influencer);
            return res;
        }

        public newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, influencee: org.openprovenance.prov.model.QualifiedName, influencer: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInfluencedBy {
            const res: org.openprovenance.prov.model.WasInfluencedBy = this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, influencee, influencer);
            this.setAttributes(res, attributes);
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

        public newWasInfluencedBy$org_openprovenance_prov_model_WasInfluencedBy(__in: org.openprovenance.prov.model.WasInfluencedBy): org.openprovenance.prov.model.WasInfluencedBy {
            const out: org.openprovenance.prov.model.WasInfluencedBy = this.newWasInfluencedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(__in.getId(), __in.getInfluencee(), __in.getInfluencer());
            out.setId(__in.getId());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(out.getOther(), __in.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(out.getType(), __in.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(out.getLabel(), __in.getLabel());
            return out;
        }

        public newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, informed: org.openprovenance.prov.model.QualifiedName, informant: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInformedBy {
            const res: org.openprovenance.prov.model.WasInformedBy = this.of.createWasInformedBy();
            res.setId(id);
            res.setInformed(informed);
            res.setInformant(informant);
            return res;
        }

        public newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, a2: org.openprovenance.prov.model.QualifiedName, a1: org.openprovenance.prov.model.QualifiedName, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInformedBy {
            const res: org.openprovenance.prov.model.WasInformedBy = this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a2, a1);
            this.setAttributes(res, attributes);
            return res;
        }

        public newWasInformedBy(id?: any, a2?: any, a1?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a2 != null && (a2.constructor != null && a2.constructor["__interfaces"] != null && a2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a2 === null) && ((a1 != null && (a1.constructor != null && a1.constructor["__interfaces"] != null && a1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a1 === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$java_util_Collection(id, a2, a1, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((a2 != null && (a2.constructor != null && a2.constructor["__interfaces"] != null && a2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a2 === null) && ((a1 != null && (a1.constructor != null && a1.constructor["__interfaces"] != null && a1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || a1 === null) && attributes === undefined) {
                return <any>this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, a2, a1);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || id === null) && a2 === undefined && a1 === undefined && attributes === undefined) {
                return <any>this.newWasInformedBy$org_openprovenance_prov_model_WasInformedBy(id);
            } else throw new Error('invalid overload');
        }

        public newWasInformedBy$org_openprovenance_prov_model_WasInformedBy(d: org.openprovenance.prov.model.WasInformedBy): org.openprovenance.prov.model.WasInformedBy {
            const wtb: org.openprovenance.prov.model.WasInformedBy = this.newWasInformedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(d.getId(), d.getInformed(), d.getInformant());
            wtb.setId(d.getId());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wtb.getOther(), d.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wtb.getType(), d.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(wtb.getLabel(), d.getLabel());
            return wtb;
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(eid: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInvalidatedBy {
            const res: org.openprovenance.prov.model.WasInvalidatedBy = this.of.createWasInvalidatedBy();
            res.setEntity(eid);
            res.setActivity(aid);
            return res;
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasInvalidatedBy {
            const res: org.openprovenance.prov.model.WasInvalidatedBy = this.of.createWasInvalidatedBy();
            res.setId(id);
            res.setEntity(entity);
            res.setActivity(activity);
            return res;
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, entity: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasInvalidatedBy {
            const res: org.openprovenance.prov.model.WasInvalidatedBy = this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
            res.setTime(time);
            this.setAttributes(res, attributes);
            return res;
        }

        public newWasInvalidatedBy(id?: any, entity?: any, activity?: any, time?: any, attributes?: any): any {
            if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && ((time != null && time instanceof <any>javax.xml.datatype.XMLGregorianCalendar) || time === null) && ((attributes != null && (attributes instanceof Array)) || attributes === null)) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id, entity, activity, time, attributes);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && ((activity != null && (activity.constructor != null && activity.constructor["__interfaces"] != null && activity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || activity === null) && time === undefined && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity, activity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || id === null) && ((entity != null && (entity.constructor != null && entity.constructor["__interfaces"] != null && entity.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedName") >= 0)) || entity === null) && activity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, entity);
            } else if (((id != null && (id.constructor != null && id.constructor["__interfaces"] != null && id.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || id === null) && entity === undefined && activity === undefined && time === undefined && attributes === undefined) {
                return <any>this.newWasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(id);
            } else throw new Error('invalid overload');
        }

        public newWasInvalidatedBy$org_openprovenance_prov_model_WasInvalidatedBy(u: org.openprovenance.prov.model.WasInvalidatedBy): org.openprovenance.prov.model.WasInvalidatedBy {
            const u1: org.openprovenance.prov.model.WasInvalidatedBy = this.newWasInvalidatedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getEntity(), u.getActivity());
            u1.setTime(u.getTime());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u.getRole(), u.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
            return u1;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.of.createWasStartedBy();
            res.setId(id);
            return res;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, aid: org.openprovenance.prov.model.QualifiedName, eid: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.of.createWasStartedBy();
            res.setId(id);
            res.setActivity(aid);
            res.setTrigger(eid);
            return res;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
            res.setStarter(starter);
            return res;
        }

        public newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$javax_xml_datatype_XMLGregorianCalendar$java_util_Collection(id: org.openprovenance.prov.model.QualifiedName, activity: org.openprovenance.prov.model.QualifiedName, trigger: org.openprovenance.prov.model.QualifiedName, starter: org.openprovenance.prov.model.QualifiedName, time: javax.xml.datatype.XMLGregorianCalendar, attributes: Array<org.openprovenance.prov.model.Attribute>): org.openprovenance.prov.model.WasStartedBy {
            const res: org.openprovenance.prov.model.WasStartedBy = this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(id, activity, trigger);
            res.setTime(time);
            res.setStarter(starter);
            this.setAttributes(res, attributes);
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

        public newWasStartedBy$org_openprovenance_prov_model_WasStartedBy(u: org.openprovenance.prov.model.WasStartedBy): org.openprovenance.prov.model.WasStartedBy {
            const u1: org.openprovenance.prov.model.WasStartedBy = this.newWasStartedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(u.getId(), u.getActivity(), u.getTrigger());
            u1.setStarter(u.getStarter());
            u1.setTime(u.getTime());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getType(), u.getType());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getRole(), u.getRole());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLabel(), u.getLabel());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getLocation(), u.getLocation());
            /* addAll */((l1, l2) => l1.push.apply(l1, l2))(u1.getOther(), u.getOther());
            return u1;
        }

        public newXMLGregorianCalendar(gc: Date): javax.xml.datatype.XMLGregorianCalendar {
            return this.dataFactory.newXMLGregorianCalendar(gc);
        }

        public newYear(year: number): javax.xml.datatype.XMLGregorianCalendar {
            const res: javax.xml.datatype.XMLGregorianCalendar = this.dataFactory.newXMLGregorianCalendar();
            res.setYear(year);
            return res;
        }

        public getAttributes(statement: org.openprovenance.prov.model.Statement): Array<org.openprovenance.prov.model.Attribute> {
            const result: Array<org.openprovenance.prov.model.Attribute> = <any>([]);
            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0))/* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, (<org.openprovenance.prov.model.HasType><any>statement).getType());
            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0))/* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, (<org.openprovenance.prov.model.HasLocation><any>statement).getLocation());
            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasRole") >= 0))/* addAll */((l1, l2) => l1.push.apply(l1, l2))(result, (<org.openprovenance.prov.model.HasRole><any>statement).getRole());
            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasValue") >= 0)){
                const val: org.openprovenance.prov.model.Value = (<org.openprovenance.prov.model.HasValue><any>statement).getValue();
                if (val != null){
                    /* add */(result.push(val)>0);
                }
            }
            if (statement != null && (statement.constructor != null && statement.constructor["__interfaces"] != null && statement.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasOther") >= 0)){
                {
                    let array199 = (<org.openprovenance.prov.model.HasOther><any>statement).getOther();
                    for(let index198=0; index198 < array199.length; index198++) {
                        let o = array199[index198];
                        {
                            /* add */(result.push(<org.openprovenance.prov.model.Attribute><any>o)>0);
                        }
                    }
                }
            }
            return result;
        }

        public setAttributes(res: org.openprovenance.prov.model.HasOther, attributes: Array<org.openprovenance.prov.model.Attribute>) {
            if (attributes == null)return;
            if (/* isEmpty */(attributes.length == 0))return;
            const typ: org.openprovenance.prov.model.HasType = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasType") >= 0)) ? <org.openprovenance.prov.model.HasType><any>res : null;
            const loc: org.openprovenance.prov.model.HasLocation = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLocation") >= 0)) ? <org.openprovenance.prov.model.HasLocation><any>res : null;
            const lab: org.openprovenance.prov.model.HasLabel = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasLabel") >= 0)) ? <org.openprovenance.prov.model.HasLabel><any>res : null;
            const aval: org.openprovenance.prov.model.HasValue = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasValue") >= 0)) ? <org.openprovenance.prov.model.HasValue><any>res : null;
            const rol: org.openprovenance.prov.model.HasRole = (res != null && (res.constructor != null && res.constructor["__interfaces"] != null && res.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HasRole") >= 0)) ? <org.openprovenance.prov.model.HasRole><any>res : null;
            for(let index200=0; index200 < attributes.length; index200++) {
                let attr = attributes[index200];
                {
                    let aValue: any = attr.getValue();
                    const vconv: org.openprovenance.prov.model.ValueConverter = new org.openprovenance.prov.model.ValueConverter(this);
                    if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(this.getName().RDF_LITERAL,attr.getType())) && (typeof aValue === 'string')){
                        aValue = vconv.convertToJava(attr.getType(), <string>aValue);
                    }
                    switch((attr.getKind())) {
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LABEL:
                        if (lab != null){
                            if (aValue != null && (aValue.constructor != null && aValue.constructor["__interfaces"] != null && aValue.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.LangString") >= 0)){
                                /* add */(lab.getLabel().push(<org.openprovenance.prov.model.LangString><any>aValue)>0);
                            } else {
                                /* add */(lab.getLabel().push(this.newInternationalizedString$java_lang_String(aValue.toString()))>0);
                            }
                        }
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_LOCATION:
                        if (loc != null){
                            /* add */(loc.getLocation().push(this.newLocation(aValue, attr.getType()))>0);
                        }
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_ROLE:
                        if (rol != null){
                            /* add */(rol.getRole().push(this.newRole(aValue, attr.getType()))>0);
                        }
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_TYPE:
                        if (typ != null){
                            /* add */(typ.getType().push(this.newType(aValue, attr.getType()))>0);
                        }
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.PROV_VALUE:
                        if (aval != null){
                            aval.setValue(this.newValue$java_lang_Object$org_openprovenance_prov_model_QualifiedName(aValue, attr.getType()));
                        }
                        break;
                    case org.openprovenance.prov.model.Attribute.AttributeKind.OTHER:
                        /* add */(res.getOther().push(this.newOther$org_openprovenance_prov_model_QualifiedName$java_lang_Object$org_openprovenance_prov_model_QualifiedName(attr.getElementName(), aValue, attr.getType()))>0);
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
        public startBundle(bundleId: org.openprovenance.prov.model.QualifiedName, namespaces: org.openprovenance.prov.model.Namespace) {
        }

        /**
         * 
         * @param {org.openprovenance.prov.model.Namespace} namespace
         */
        public startDocument(namespace: org.openprovenance.prov.model.Namespace) {
        }

        public newNamespace$org_openprovenance_prov_model_Namespace(ns: org.openprovenance.prov.model.Namespace): org.openprovenance.prov.model.Namespace {
            return new org.openprovenance.prov.model.Namespace(ns);
        }

        public newNamespace(ns?: any): any {
            if (((ns != null && ns instanceof <any>org.openprovenance.prov.model.Namespace) || ns === null)) {
                return <any>this.newNamespace$org_openprovenance_prov_model_Namespace(ns);
            } else if (ns === undefined) {
                return <any>this.newNamespace$();
            } else throw new Error('invalid overload');
        }

        public newNamespace$(): org.openprovenance.prov.model.Namespace {
            return new org.openprovenance.prov.model.Namespace();
        }

        public newAlternateOf$org_openprovenance_prov_model_AlternateOf(s: org.openprovenance.prov.model.AlternateOf): org.openprovenance.prov.model.AlternateOf {
            const res: org.openprovenance.prov.model.AlternateOf = this.newAlternateOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(s.getAlternate1(), s.getAlternate2());
            return res;
        }

        public newSpecializationOf$org_openprovenance_prov_model_SpecializationOf(s: org.openprovenance.prov.model.SpecializationOf): org.openprovenance.prov.model.SpecializationOf {
            const res: org.openprovenance.prov.model.SpecializationOf = this.newSpecializationOf$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(s.getSpecificEntity(), s.getGeneralEntity());
            return res;
        }

        public newHadMember$org_openprovenance_prov_model_HadMember(s: org.openprovenance.prov.model.HadMember): org.openprovenance.prov.model.HadMember {
            const res: org.openprovenance.prov.model.HadMember = this.newHadMember$org_openprovenance_prov_model_QualifiedName$java_util_Collection(s.getCollection(), s.getEntity());
            return res;
        }

        util: org.openprovenance.prov.model.ProvUtilities;

        public newStatement<T extends org.openprovenance.prov.model.Statement>(s: T): T {
            return <T><any>this.util.doAction$org_openprovenance_prov_model_StatementOrBundle$org_openprovenance_prov_model_StatementActionValue(s, new ProvFactory.Cloner(this));
        }
    }
    ProvFactory["__class"] = "org.openprovenance.prov.model.ProvFactory";
    ProvFactory["__interfaces"] = ["org.openprovenance.prov.model.LiteralConstructor","org.openprovenance.prov.model.ModelConstructorExtension","org.openprovenance.prov.model.ModelConstructor"];



    export namespace ProvFactory {

        export class Cloner implements org.openprovenance.prov.model.StatementActionValue {
            public __parent: any;
            public doAction$org_openprovenance_prov_model_Activity(s: org.openprovenance.prov.model.Activity): any {
                return this.__parent.newActivity(s);
            }

            public doAction$org_openprovenance_prov_model_Used(s: org.openprovenance.prov.model.Used): any {
                return this.__parent.newUsed(s);
            }

            public doAction$org_openprovenance_prov_model_WasStartedBy(s: org.openprovenance.prov.model.WasStartedBy): any {
                return this.__parent.newWasStartedBy(s);
            }

            public doAction$org_openprovenance_prov_model_Agent(s: org.openprovenance.prov.model.Agent): any {
                return this.__parent.newAgent(s);
            }

            public doAction$org_openprovenance_prov_model_AlternateOf(s: org.openprovenance.prov.model.AlternateOf): any {
                return this.__parent.newAlternateOf(s);
            }

            public doAction$org_openprovenance_prov_model_WasAssociatedWith(s: org.openprovenance.prov.model.WasAssociatedWith): any {
                return this.__parent.newWasAssociatedWith(s);
            }

            public doAction$org_openprovenance_prov_model_WasAttributedTo(s: org.openprovenance.prov.model.WasAttributedTo): any {
                return this.__parent.newWasAttributedTo(s);
            }

            public doAction$org_openprovenance_prov_model_WasInfluencedBy(s: org.openprovenance.prov.model.WasInfluencedBy): any {
                return this.__parent.newWasInfluencedBy(s);
            }

            public doAction$org_openprovenance_prov_model_ActedOnBehalfOf(s: org.openprovenance.prov.model.ActedOnBehalfOf): any {
                return this.__parent.newActedOnBehalfOf(s);
            }

            public doAction$org_openprovenance_prov_model_WasDerivedFrom(s: org.openprovenance.prov.model.WasDerivedFrom): any {
                return this.__parent.newWasDerivedFrom(s);
            }

            public doAction$org_openprovenance_prov_model_DictionaryMembership(s: org.openprovenance.prov.model.DictionaryMembership): any {
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }

            public doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(s: org.openprovenance.prov.model.DerivedByRemovalFrom): any {
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }

            public doAction$org_openprovenance_prov_model_WasEndedBy(s: org.openprovenance.prov.model.WasEndedBy): any {
                return this.__parent.newWasEndedBy(s);
            }

            public doAction$org_openprovenance_prov_model_Entity(s: org.openprovenance.prov.model.Entity): any {
                return this.__parent.newEntity(s);
            }

            public doAction$org_openprovenance_prov_model_WasGeneratedBy(s: org.openprovenance.prov.model.WasGeneratedBy): any {
                return this.__parent.newWasGeneratedBy(s);
            }

            public doAction$org_openprovenance_prov_model_WasInvalidatedBy(s: org.openprovenance.prov.model.WasInvalidatedBy): any {
                return this.__parent.newWasInvalidatedBy(s);
            }

            public doAction$org_openprovenance_prov_model_HadMember(s: org.openprovenance.prov.model.HadMember): any {
                return this.__parent.newHadMember(s);
            }

            public doAction$org_openprovenance_prov_model_MentionOf(s: org.openprovenance.prov.model.MentionOf): any {
                return this.__parent.newMentionOf(s);
            }

            public doAction$org_openprovenance_prov_model_SpecializationOf(s: org.openprovenance.prov.model.SpecializationOf): any {
                return this.__parent.newSpecializationOf(s);
            }

            public doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(s: org.openprovenance.prov.model.extension.QualifiedSpecializationOf): any {
                return this.__parent.newQualifiedSpecializationOf(s.getId(), s.getSpecificEntity(), s.getGeneralEntity(), this.__parent.getAttributes(s));
            }

            public doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(s: org.openprovenance.prov.model.extension.QualifiedHadMember): any {
                return this.__parent.newQualifiedHadMember(s.getId(), s.getCollection(), s.getEntity(), this.__parent.getAttributes(s));
            }

            public doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(s: org.openprovenance.prov.model.DerivedByInsertionFrom): any {
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }

            public doAction$org_openprovenance_prov_model_WasInformedBy(s: org.openprovenance.prov.model.WasInformedBy): any {
                return this.__parent.newWasInformedBy(s);
            }

            public doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(s: org.openprovenance.prov.model.Bundle, provUtilities: org.openprovenance.prov.model.ProvUtilities): any {
                throw Object.defineProperty(new Error(), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.UnsupportedOperationException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }

            /**
             * 
             * @param {*} s
             * @param {org.openprovenance.prov.model.ProvUtilities} provUtilities
             * @return {*}
             */
            public doAction(s?: any, provUtilities?: any): any {
                if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) || s === null) && ((provUtilities != null && provUtilities instanceof <any>org.openprovenance.prov.model.ProvUtilities) || provUtilities === null)) {
                    return <any>this.doAction$org_openprovenance_prov_model_Bundle$org_openprovenance_prov_model_ProvUtilities(s, provUtilities);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Activity") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_Activity(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Used") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_Used(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasStartedBy") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasStartedBy(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Agent") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_Agent(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.AlternateOf") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_AlternateOf(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAssociatedWith") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasAssociatedWith(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasAttributedTo") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasAttributedTo(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInfluencedBy") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasInfluencedBy(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.ActedOnBehalfOf") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_ActedOnBehalfOf(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasDerivedFrom") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasDerivedFrom(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DictionaryMembership") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_DictionaryMembership(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByRemovalFrom") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_DerivedByRemovalFrom(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasEndedBy") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasEndedBy(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Entity") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_Entity(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasGeneratedBy") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasGeneratedBy(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInvalidatedBy") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasInvalidatedBy(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.HadMember") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_HadMember(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.MentionOf") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_MentionOf(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedSpecializationOf") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedSpecializationOf(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.SpecializationOf") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_SpecializationOf(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedHadMember") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedHadMember(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.DerivedByInsertionFrom") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_DerivedByInsertionFrom(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.WasInformedBy") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_WasInformedBy(s);
                } else if (((s != null && (s.constructor != null && s.constructor["__interfaces"] != null && s.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.extension.QualifiedAlternateOf") >= 0)) || s === null) && provUtilities === undefined) {
                    return <any>this.doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(s);
                } else throw new Error('invalid overload');
            }

            public doAction$org_openprovenance_prov_model_extension_QualifiedAlternateOf(s: org.openprovenance.prov.model.extension.QualifiedAlternateOf): any {
                return this.__parent.newQualifiedAlternateOf(s.getId(), s.getAlternate1(), s.getAlternate2(), this.__parent.getAttributes(s));
            }

            constructor(__parent: any) {
                this.__parent = __parent;
            }
        }
        Cloner["__class"] = "org.openprovenance.prov.model.ProvFactory.Cloner";
        Cloner["__interfaces"] = ["org.openprovenance.prov.model.StatementActionValue"];


    }

}

