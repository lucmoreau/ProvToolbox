/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace com.google.gson {
    export class JsonElement {
        o: any;

        public constructor(o: any) {
            if (this.o === undefined) { this.o = null; }
            this.o = o;
        }

        public isJsonArray(): boolean {
            return Array.isArray(this.o);
        }

        public isJsonObject(): boolean {
            return !this.isJsonNull() && ("object" === typeof this.o);
        }

        public isJsonPrimitive(): boolean {
            switch((typeof this.o)) {
            case "boolean":
            case "number":
            case "string":
                return true;
            }
            return false;
        }

        public isJsonNull(): boolean {
            return this.o === null;
        }

        public getAsJsonObject(): com.google.gson.JsonObject {
            if (this.isJsonObject()){
                return (this != null && this instanceof <any>com.google.gson.JsonObject) ? <any>((<any>this)) : new com.google.gson.JsonObject(this.o);
            } else {
                throw Object.defineProperty(new Error("Not a JSON Object: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }

        public getAsJsonArray(): com.google.gson.JsonArray {
            if (this.isJsonArray()){
                return (this != null && this instanceof <any>com.google.gson.JsonArray) ? <any>((<any>this)) : new com.google.gson.JsonArray(<any>((<any>this.o)));
            } else {
                throw Object.defineProperty(new Error("Not a JSON Array: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }

        public getAsJsonPrimitive(): com.google.gson.JsonPrimitive {
            if (this.isJsonPrimitive()){
                if (this != null && this instanceof <any>com.google.gson.JsonPrimitive)return <any>((<any>this));
                switch((typeof this.o)) {
                case "boolean":
                    return new com.google.gson.JsonPrimitive(<any>((<any>this.o)));
                case "number":
                    return new com.google.gson.JsonPrimitive(<any>((<any>this.o)));
                case "string":
                    return new com.google.gson.JsonPrimitive(<any>((<any>this.o)));
                }
                throw Object.defineProperty(new Error("Not a JSON Primitive: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            } else {
                throw Object.defineProperty(new Error("Not a JSON Primitive: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }

        public getAsJsonNull(): com.google.gson.JsonNull {
            if (this.isJsonNull()){
                return <com.google.gson.JsonNull>this;
            } else {
                throw Object.defineProperty(new Error("Not a JSON Null: " + this), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.IllegalStateException','java.lang.Object','java.lang.RuntimeException','java.lang.Exception'] });
            }
        }

        public toString(): string {
            return JSON.stringify(this.o);
        }
    }
    JsonElement["__class"] = "com.google.gson.JsonElement";

}

