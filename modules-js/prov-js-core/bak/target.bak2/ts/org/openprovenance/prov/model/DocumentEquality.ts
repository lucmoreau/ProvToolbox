/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * @author Trung Dong Huynh &lt;tdh@ecs.soton.ac.uk&gt;
     * @author lavm
     * Comparator Class
     * @param {boolean} mergeDuplicates
     * @param {java.io.PrintStream} out
     * @class
     */
    export class DocumentEquality {
        static logger: org.apache.log4j.Logger; public static logger_$LI$(): org.apache.log4j.Logger { if (DocumentEquality.logger == null) { DocumentEquality.logger = org.apache.log4j.Logger.getLogger(DocumentEquality); }  return DocumentEquality.logger; }

        /*private*/ mergeDuplicates: boolean;

        /*private*/ out: java.io.PrintStream;

        public constructor(mergeDuplicates?: any, out?: any) {
            if (((typeof mergeDuplicates === 'boolean') || mergeDuplicates === null) && ((out != null && out instanceof <any>java.io.PrintStream) || out === null)) {
                let __args = arguments;
                if (this.mergeDuplicates === undefined) { this.mergeDuplicates = false; } 
                if (this.out === undefined) { this.out = null; } 
                this.mergeDuplicates = mergeDuplicates;
                this.out = out;
            } else if (mergeDuplicates === undefined && out === undefined) {
                let __args = arguments;
                if (this.mergeDuplicates === undefined) { this.mergeDuplicates = false; } 
                if (this.out === undefined) { this.out = null; } 
                this.mergeDuplicates = false;
                this.out = null;
            } else throw new Error('invalid overload');
        }

        public log(s: any) {
            if (this.out != null){
                this.out.println(s);
            } else {
                DocumentEquality.logger_$LI$().debug(s);
            }
        }

        /*private*/ collectionEqual(c1: Array<any>, c2: Array<any>): boolean {
            let bag1: Array<any>;
            let bag2: Array<any>;
            if (this.mergeDuplicates){
                bag1 = <any>(c1.slice(0));
                bag2 = <any>(c2.slice(0));
            } else {
                bag1 = <any>(new org.apache.commons.collections4.bag.HashBag(c1));
                bag2 = <any>(new org.apache.commons.collections4.bag.HashBag(c2));
            }
            return /* equals */((a1, a2) => { if(a1==null && a2==null) return true; if(a1==null || a2==null) return false; if(a1.length != a2.length) return false; for(let i = 0; i < a1.length; i++) { if(<any>a1[i] != <any>a2[i]) return false; } return true; })(bag1, bag2);
        }

        /*private*/ statementEqual(r1: org.openprovenance.prov.model.StatementOrBundle, r2: org.openprovenance.prov.model.StatementOrBundle): boolean {
            if ((r1 != null && (r1.constructor != null && r1.constructor["__interfaces"] != null && r1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0)) && (r2 != null && (r2.constructor != null && r2.constructor["__interfaces"] != null && r2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.Bundle") >= 0))){
                const b1: org.openprovenance.prov.model.Bundle = <org.openprovenance.prov.model.Bundle><any>r1;
                const b2: org.openprovenance.prov.model.Bundle = <org.openprovenance.prov.model.Bundle><any>r2;
                if (!/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(b1.getId(),b2.getId())))return false;
                const stmts1: Array<org.openprovenance.prov.model.Statement> = b1.getStatement();
                const stmts2: Array<org.openprovenance.prov.model.Statement> = b2.getStatement();
                return this.collectionEqual(stmts1, stmts2);
            }
            const class1: any = (<any>r1.constructor);
            const class2: any = (<any>r2.constructor);
            if (class1 !== class2){
                if (!(((r1 != null && (r1.constructor != null && r1.constructor["__interfaces"] != null && r1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.UnqualifiedRelation") >= 0)) && (r2 != null && (r2.constructor != null && r2.constructor["__interfaces"] != null && r2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedRelation") >= 0))) || ((r2 != null && (r2.constructor != null && r2.constructor["__interfaces"] != null && r2.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.UnqualifiedRelation") >= 0)) && (r1 != null && (r1.constructor != null && r1.constructor["__interfaces"] != null && r1.constructor["__interfaces"].indexOf("org.openprovenance.prov.model.QualifiedRelation") >= 0))))){
                    return false;
                }
            }
            if (/* equals */(<any>((o1: any, o2: any) => { if (o1 && o1.equals) { return o1.equals(o2); } else { return o1 === o2; } })(r1,r2))){
                return true;
            }
            const allMethods: { owner: any, name: string, fn : Function }[] = /* getDeclaredMethods */(c => Object.getOwnPropertyNames(c.prototype).filter(n => typeof c.prototype[n] == 'function').map(n => ({owner:c,name:n,fn:c.prototype[n]}) ) )(class1);
            for(let index148=0; index148 < allMethods.length; index148++) {
                let m = allMethods[index148];
                {
                    const methodName: string = /* getName */m.name;
                    if (/* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(methodName, "get") && (!(methodName === ("getAll")))){
                        try {
                            const attr1: any = /* invoke */m.fn.apply(r1);
                            const attr2: any = /* invoke */m.fn.apply(r2);
                            if (attr1 == null && attr2 == null)continue;
                            if (/* equals */(<any>((o1: any, o2: any) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(attr1,attr2)))continue;
                            if ((attr1 != null && (attr1 instanceof Array)) && (attr2 != null && (attr2 instanceof Array)))if (this.collectionEqual(<Array<any>><any>attr1, <Array<any>><any>attr2))continue;
                            if (((<any>attr1.constructor).isArray()) && (<any>attr2.constructor).isArray())if (this.collectionEqual(java.util.Set.of<any>(<any[]>attr1), java.util.Set.of<any>(<any[]>attr2)))continue;
                            return false;
                        } catch(e) {
                            this.log(e);
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        /*private*/ statementListEqual(stmts1: Array<org.openprovenance.prov.model.StatementOrBundle>, stmts2: Array<org.openprovenance.prov.model.StatementOrBundle>): boolean {
            if (/* size */(<number>stmts1.length) !== /* size */(<number>stmts2.length)){
                return false;
            }
            const list1: Array<org.openprovenance.prov.model.StatementOrBundle> = <any>(stmts1.slice(0));
            const list2: Array<org.openprovenance.prov.model.StatementOrBundle> = <any>(stmts2.slice(0));
            for(let index149=0; index149 < list1.length; index149++) {
                let stmt1 = list1[index149];
                {
                    let found: org.openprovenance.prov.model.StatementOrBundle = null;
                    for(let index150=0; index150 < list2.length; index150++) {
                        let stmt2 = list2[index150];
                        {
                            if (this.statementEqual(stmt1, stmt2)){
                                found = stmt2;
                                break;
                            }
                        }
                    }
                    if (found == null){
                        this.log("---------------------------");
                        this.log("Cannot find the following statement in the second document");
                        this.log("---------------------------");
                        this.log("statement: " + stmt1);
                        this.log("---------------------------");
                        this.log("list of statements: ");
                        this.log(stmts2);
                        this.log("---------------------------");
                        return false;
                    }
                    /* remove */(a => { let index = a.indexOf(found); if(index>=0) { a.splice(index, 1); return true; } else { return false; }})(list2);
                }
            }
            return true;
        }

        public check(d1: org.openprovenance.prov.model.Document, d2: org.openprovenance.prov.model.Document): boolean {
            return this.statementListEqual(d1.getStatementOrBundle(), d2.getStatementOrBundle());
        }
    }
    DocumentEquality["__class"] = "org.openprovenance.prov.model.DocumentEquality";

}

