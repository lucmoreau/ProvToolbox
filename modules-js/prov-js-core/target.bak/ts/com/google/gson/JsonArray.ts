/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace com.google.gson {
    export class JsonArray extends com.google.gson.JsonElement {
        public constructor(array?: any) {
            if (((array != null && array instanceof <any>Array) || array === null)) {
                let __args = arguments;
                super(array);
            } else if (((typeof array === 'number') || array === null)) {
                let __args = arguments;
                let capacity: any = __args[0];
                {
                    let __args = arguments;
                    let array: any = new Array<any>(capacity);
                    super(array);
                }
            } else if (array === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    let array: any = new Array<any>();
                    super(array);
                }
            } else throw new Error('invalid overload');
        }

        /*private*/ asArray(): Array<any> {
            return <any>((<any>this.o));
        }

        public add$java_lang_Boolean(boolVal: boolean) {
            this.asArray().push((boolVal));
        }

        public add(boolVal?: any) {
            if (((typeof boolVal === 'boolean') || boolVal === null)) {
                return <any>this.add$java_lang_Boolean(boolVal);
            } else if (((typeof boolVal === 'number') || boolVal === null)) {
                return <any>this.add$java_lang_Number(boolVal);
            } else if (((typeof boolVal === 'string') || boolVal === null)) {
                return <any>this.add$java_lang_String(boolVal);
            } else if (((boolVal != null && boolVal instanceof <any>com.google.gson.JsonElement) || boolVal === null)) {
                return <any>this.add$com_google_gson_JsonElement(boolVal);
            } else throw new Error('invalid overload');
        }

        public add$java_lang_Number(numberVal: number) {
            this.asArray().push((numberVal));
        }

        public add$java_lang_String(stringVal: string) {
            this.asArray().push((stringVal));
        }

        public add$com_google_gson_JsonElement(element: com.google.gson.JsonElement) {
            if (element == null){
                element = com.google.gson.JsonNull.INSTANCE_$LI$();
            }
            this.asArray().push(element.o);
        }

        public set(index: number, element: com.google.gson.JsonElement): com.google.gson.JsonElement {
            const othO: any = this.asArray()[index];
            this.asArray()[index] = element.o;
            return new com.google.gson.JsonElement(othO);
        }

        public remove$com_google_gson_JsonElement(element: com.google.gson.JsonElement): boolean {
            for(let i: number = 0; i < this.asArray().length; ++i) {{
                if (this.asArray()[i] === element.o){
                    this.remove$int(i);
                    return true;
                }
            };}
            return false;
        }

        public remove(element?: any): any {
            if (((element != null && element instanceof <any>com.google.gson.JsonElement) || element === null)) {
                return <any>this.remove$com_google_gson_JsonElement(element);
            } else if (((typeof element === 'number') || element === null)) {
                return <any>this.remove$int(element);
            } else throw new Error('invalid overload');
        }

        public remove$int(index: number): com.google.gson.JsonElement {
            const splice: Array<any> = this.asArray().splice(index, 1);
            return new com.google.gson.JsonElement(splice[0]);
        }

        public contains(element: com.google.gson.JsonElement): boolean {
            for(let i: number = 0; i < this.asArray().length; ++i) {{
                if (this.asArray()[i] === element.o){
                    return true;
                }
            };}
            return false;
        }

        public size(): number {
            return this.asArray().length;
        }

        public iterator(): any {
            return new JsonArray.JsonArray$0(this);
        }

        public get(i: number): com.google.gson.JsonElement {
            return new com.google.gson.JsonElement(this.asArray()[i]);
        }
    }
    JsonArray["__class"] = "com.google.gson.JsonArray";
    JsonArray["__interfaces"] = ["java.lang.Iterable"];



    export namespace JsonArray {

        export class JsonArray$0 {
            public __parent: any;
            a: Array<any>;

            pos: number;

            /**
             * 
             * @return {boolean}
             */
            public hasNext(): boolean {
                return this.pos < this.a.length;
            }

            /**
             * 
             * @return {com.google.gson.JsonElement}
             */
            public next(): com.google.gson.JsonElement {
                return new com.google.gson.JsonElement(this.a[this.pos++]);
            }

            /**
             * 
             */
            public remove() {
                this.a.splice(--this.pos, 1);
            }

            constructor(__parent: any) {
                this.__parent = __parent;
                this.a = this.__parent.asArray();
                if (this.pos === undefined) { this.pos = 0; }
            }
        }
        JsonArray$0["__interfaces"] = ["java.util.Iterator"];


    }

}

