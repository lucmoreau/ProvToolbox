/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace com.google.gson {
    export class JsonObject extends com.google.gson.JsonElement {
        public constructor(o?: any) {
            if (((o != null && o instanceof <any>Object) || o === null)) {
                let __args = arguments;
                super(o);
            } else if (o === undefined) {
                let __args = arguments;
                {
                    let __args = arguments;
                    let o: any = <Object>new Object();
                    super(o);
                }
            } else throw new Error('invalid overload');
        }

        /*private*/ asObject(): Object {
            return this.o;
        }

        public add(property: string, value: com.google.gson.JsonElement) {
            if (value == null){
                value = com.google.gson.JsonNull.INSTANCE_$LI$();
            }
            this.asObject()[property] = value.o;
        }

        public remove(property: string): com.google.gson.JsonElement {
            const jsonElement: com.google.gson.JsonElement = this.get(property);
            delete this.asObject()[property];
            return jsonElement;
        }

        public addProperty$java_lang_String$java_lang_String(property: string, value: string) {
            this.asObject()[property] = (value);
        }

        public addProperty(property?: any, value?: any) {
            if (((typeof property === 'string') || property === null) && ((typeof value === 'string') || value === null)) {
                return <any>this.addProperty$java_lang_String$java_lang_String(property, value);
            } else if (((typeof property === 'string') || property === null) && ((typeof value === 'number') || value === null)) {
                return <any>this.addProperty$java_lang_String$java_lang_Number(property, value);
            } else if (((typeof property === 'string') || property === null) && ((typeof value === 'boolean') || value === null)) {
                return <any>this.addProperty$java_lang_String$java_lang_Boolean(property, value);
            } else throw new Error('invalid overload');
        }

        public addProperty$java_lang_String$java_lang_Number(property: string, value: number) {
            this.asObject()[property] = (value);
        }

        public addProperty$java_lang_String$java_lang_Boolean(property: string, value: boolean) {
            this.asObject()[property] = (value);
        }

        public keySet(): Array<string> {
            return <any>(/* asList */((<any>(Object.keys(this.o)))).slice(0).slice(0));
        }

        public size(): number {
            return /* size */(<number>this.keySet().length);
        }

        public has(memberName: string): boolean {
            return this.asObject()[memberName] === undefined;
        }

        public get(memberName: string): com.google.gson.JsonElement {
            return new com.google.gson.JsonElement(<any>(this.asObject()[memberName]));
        }

        public getAsJsonPrimitive$java_lang_String(memberName: string): com.google.gson.JsonPrimitive {
            return this.get(memberName).getAsJsonPrimitive();
        }

        public getAsJsonPrimitive(memberName?: any): any {
            if (((typeof memberName === 'string') || memberName === null)) {
                return <any>this.getAsJsonPrimitive$java_lang_String(memberName);
            } else if (memberName === undefined) {
                return super.getAsJsonPrimitive();
            } else throw new Error('invalid overload');
        }

        public getAsJsonArray$java_lang_String(memberName: string): com.google.gson.JsonArray {
            return this.get(memberName).getAsJsonArray();
        }

        public getAsJsonArray(memberName?: any): any {
            if (((typeof memberName === 'string') || memberName === null)) {
                return <any>this.getAsJsonArray$java_lang_String(memberName);
            } else if (memberName === undefined) {
                return super.getAsJsonArray();
            } else throw new Error('invalid overload');
        }

        public getAsJsonObject$java_lang_String(memberName: string): JsonObject {
            return this.get(memberName).getAsJsonObject();
        }

        public getAsJsonObject(memberName?: any): any {
            if (((typeof memberName === 'string') || memberName === null)) {
                return <any>this.getAsJsonObject$java_lang_String(memberName);
            } else if (memberName === undefined) {
                return super.getAsJsonObject();
            } else throw new Error('invalid overload');
        }
    }
    JsonObject["__class"] = "com.google.gson.JsonObject";

}

