/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var quickstart;
(function (quickstart) {
    /**
     * This class is used within the webapp/index.html file.
     * @class
     */
    class QuickStart {
        static main(args) {
            const l = ([]);
            /* add */ (l.push("Hello") > 0);
            /* add */ (l.push("world") > 0);
            const a = (new Array());
            a.push("Hello", "world");
            $("#target").text(/* toString */ ('[' + l.join(', ') + ']'));
            console.log(a.toString());
            const nodeList = document.getElementsByTagName("div");
            for (let index241 = 0; index241 < nodeList.length; index241++) {
                let element = nodeList[index241];
                {
                    element.innerText = "Hello again in vanilla JS";
                }
            }
            const pf = new org.openprovenance.prov.vanilla.ProvFactory();
            const e = pf.newEntity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "e1", "ex"));
            console.log(e);
            console.log(e.getKind());
            const doc = pf.newDocument$();
            /* add */ (doc.getStatementOrBundle().push(e) > 0);
            const ns = new org.openprovenance.prov.model.Namespace();
            ns.addKnownNamespaces();
            ns.register("ex", "http://ex.org/");
            doc.setNamespace(ns);
            console.log(new org.openprovenance.prov.json.ProvSerialiser(pf).serialiseDocument$org_openprovenance_prov_model_Document(doc));
        }
    }
    quickstart.QuickStart = QuickStart;
    QuickStart["__class"] = "quickstart.QuickStart";
})(quickstart || (quickstart = {}));
quickstart.QuickStart.main(null);
