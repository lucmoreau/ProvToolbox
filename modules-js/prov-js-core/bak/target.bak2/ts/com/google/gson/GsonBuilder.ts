/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace com.google.gson {
    export class GsonBuilder {
        serializers: any;

        deserializers: any;

        public registerTypeAdapter(type: any, o: any): GsonBuilder {
            if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("com.google.gson.JsonSerializer") >= 0))/* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.serializers, type, <com.google.gson.JsonSerializer<any>><any>o); else if (o != null && (o.constructor != null && o.constructor["__interfaces"] != null && o.constructor["__interfaces"].indexOf("com.google.gson.JsonDeserializer") >= 0))/* put */((m,k,v) => { if(m.entries==null) m.entries=[]; for(let i=0;i<m.entries.length;i++) if(m.entries[i].key==null && k==null || m.entries[i].key.equals!=null && m.entries[i].key.equals(k) || m.entries[i].key===k) { m.entries[i].value=v; return; } m.entries.push({key:k,value:v,getKey: function() { return this.key }, getValue: function() { return this.value }}); })(<any>this.deserializers, type, <com.google.gson.JsonDeserializer<any>><any>o); else throw Object.defineProperty(new Error("Unrecognized type adapter"), '__classes', { configurable: true, value: ['java.lang.Throwable','java.lang.Object','java.lang.RuntimeException','java.lang.IllegalArgumentException','java.lang.Exception'] });
            return this;
        }

        public create(): com.google.gson.Gson {
            return new com.google.gson.Gson(this.serializers, this.deserializers);
        }

        public setPrettyPrinting(): GsonBuilder {
            return this;
        }

        constructor() {
            this.serializers = <any>({});
            this.deserializers = <any>({});
        }
    }
    GsonBuilder["__class"] = "com.google.gson.GsonBuilder";

}

