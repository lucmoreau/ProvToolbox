/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace quickstart {
    /**
     * This class is used within the webapp/index.html file.
     * @class
     */
    export class QuickStart {
        public static main(args: string[]) {
            const l: Array<string> = <any>([]);
            /* add */(l.push("Hello")>0);
            /* add */(l.push("world")>0);
            const a: Array<string> = <any>(new Array<any>());
            a.push("Hello", "world");
            $("#target").text(/* toString */('['+l.join(', ')+']'));
            console.log(a.toString());
            const nodeList: HTMLCollectionOf<HTMLDivElement> = document.getElementsByTagName("div");
            for(let index241=0; index241 < nodeList.length; index241++) {
                let element = nodeList[index241];
                {
                    element.innerText = "Hello again in vanilla JS";
                }
            }
            const pf: org.openprovenance.prov.vanilla.ProvFactory = new org.openprovenance.prov.vanilla.ProvFactory();
            const e: org.openprovenance.prov.model.Entity = pf.newEntity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "e1", "ex"));
            const activity: org.openprovenance.prov.model.Activity = pf.newActivity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "a1", "ex"));
            const agent: org.openprovenance.prov.model.Agent = pf.newAgent$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "ag1", "ex"));
            const wgb: org.openprovenance.prov.model.WasGeneratedBy = pf.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(null, e.getId(), activity.getId());
            pf.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(e, pf.newType(pf.getName().newProvQualifiedName("SoftwareAgent"), pf.getName().PROV_QUALIFIED_NAME));
            activity.setStartTime(pf.newTimeNow());
            console.log(e);
            console.log(e.getKind());
            const doc: org.openprovenance.prov.model.Document = pf.newDocument$();
            /* add */(doc.getStatementOrBundle().push(e)>0);
            /* add */(doc.getStatementOrBundle().push(activity)>0);
            /* add */(doc.getStatementOrBundle().push(agent)>0);
            /* add */(doc.getStatementOrBundle().push(wgb)>0);
            const ns: org.openprovenance.prov.model.Namespace = new org.openprovenance.prov.model.Namespace();
            ns.addKnownNamespaces();
            ns.register("ex", "http://ex.org/");
            doc.setNamespace(ns);
            const docString: string = new org.openprovenance.prov.json.ProvSerialiser(pf).serialiseDocument$org_openprovenance_prov_model_Document(doc);
            console.log(docString);
            const doc2: org.openprovenance.prov.model.Document = new org.openprovenance.prov.json.ProvDeserialiser(pf).deserialiseDocument$java_lang_String(docString);
            console.log(doc2);
            console.log(/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(doc,doc2)));
        }
    }
    QuickStart["__class"] = "quickstart.QuickStart";

}


quickstart.QuickStart.main(null);
