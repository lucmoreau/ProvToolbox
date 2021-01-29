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
            console.log(e);
            console.log(e.getKind());
            const doc: org.openprovenance.prov.model.Document = pf.newDocument$();
            /* add */(doc.getStatementOrBundle().push(e)>0);
            const ns: org.openprovenance.prov.model.Namespace = new org.openprovenance.prov.model.Namespace();
            ns.addKnownNamespaces();
            ns.register("ex", "http://ex.org/");
            doc.setNamespace(ns);
            console.log(new org.openprovenance.prov.json.ProvSerialiser(pf).serialiseDocument$org_openprovenance_prov_model_Document(doc));
        }
    }
    QuickStart["__class"] = "quickstart.QuickStart";

}


quickstart.QuickStart.main(null);
