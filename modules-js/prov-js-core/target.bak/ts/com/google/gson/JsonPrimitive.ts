/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace com.google.gson {
    export class JsonPrimitive extends com.google.gson.JsonElement {
        public constructor(value?: any) {
            if (((typeof value === 'boolean') || value === null)) {
                let __args = arguments;
                super((value));
            } else if (((typeof value === 'number') || value === null)) {
                let __args = arguments;
                super((value));
            } else if (((typeof value === 'string') || value === null)) {
                let __args = arguments;
                super((value));
            } else throw new Error('invalid overload');
        }

        setValue(primitive: any) {
            const obj: Object = primitive;
            new com.google.gson.JsonElement(obj).getAsJsonPrimitive();
            this.o = obj;
        }

        public isBoolean(): boolean {
            return "boolean" === typeof this.o;
        }

        public getAsBoolean(): boolean {
            return this.isBoolean() ? <any>((<any>this.o)) : javaemul.internal.BooleanHelper.parseBoolean(this.getAsString());
        }

        public isNumber(): boolean {
            return "number" === typeof this.o;
        }

        public getAsNumber(): number {
            return this.isNumber() ? <any>((<any>this.o)) : this.getAsDouble();
        }

        public isString(): boolean {
            return "string" === typeof this.o;
        }

        public getAsString(): string {
            return (this.o).toString();
        }

        public getAsDouble(): number {
            return this.isNumber() ? <any>((<any>this.o)) : /* parseDouble */parseFloat(this.getAsString());
        }
    }
    JsonPrimitive["__class"] = "com.google.gson.JsonPrimitive";

}

