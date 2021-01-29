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
            const e2 = pf.newEntity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "e1", "ex"));
            const activity = pf.newActivity$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "a1", "ex"));
            const agent = pf.newAgent$org_openprovenance_prov_model_QualifiedName(pf.newQualifiedName$java_lang_String$java_lang_String$java_lang_String("http://ex.org/", "ag1", "ex"));
            const wgb = pf.newWasGeneratedBy$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName$org_openprovenance_prov_model_QualifiedName(null, e.getId(), activity.getId());
            pf.addType$org_openprovenance_prov_model_HasType$org_openprovenance_prov_model_Type(e, pf.newType(pf.getName().newProvQualifiedName("SoftwareAgent"), pf.getName().PROV_QUALIFIED_NAME));
            activity.setStartTime(pf.newTimeNow());
            console.log(e);
            console.log(e.getKind());
            const doc = pf.newDocument$();
            /* add */ (doc.getStatementOrBundle().push(e) > 0);
            /* add */ (doc.getStatementOrBundle().push(activity) > 0);
            /* add */ (doc.getStatementOrBundle().push(agent) > 0);
            /* add */ (doc.getStatementOrBundle().push(wgb) > 0);
            const ns = new org.openprovenance.prov.model.Namespace();
            ns.addKnownNamespaces();
            ns.register("ex", "http://ex.org/");
            doc.setNamespace(ns);
            const docString = new org.openprovenance.prov.json.ProvSerialiser(pf).serialiseDocument$org_openprovenance_prov_model_Document(doc);
            console.log(docString);
            const doc2 = new org.openprovenance.prov.json.ProvDeserialiser(pf).deserialiseDocument$java_lang_String(docString);
            console.log(doc2);
            const doc3 = pf.newDocument$();
            doc3.setNamespace(ns);
            (doc3.getStatementOrBundle().push(e) > 0);
            console.log(/* equals */ ((o1, o2) => { if (o1 && o1.equals) {
                return o1.equals(o2);
            }
            else {
                return o1 === o2;
            } })(doc, doc2));

            console.log(e.equals(e))
            console.log(doc.equals(doc3))
            console.log(doc.equals(e))
        }
    }
    quickstart.QuickStart = QuickStart;
    QuickStart["__class"] = "quickstart.QuickStart";
})(quickstart || (quickstart = {}));
quickstart.QuickStart.main(null);
