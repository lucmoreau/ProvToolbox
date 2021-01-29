package com.google.gson;


import static jsweet.util.Lang.*;

public class JsonPrimitive extends JsonElement {
    public JsonPrimitive(Boolean value) {
        super(bool(value));
    }

    public JsonPrimitive(Number value) {
        super(number(value));
    }

    public JsonPrimitive(String value) {
        super(string(value));
    }

    void setValue(Object primitive) {
        def.js.Object obj = object(primitive);
        new JsonElement(obj).getAsJsonPrimitive(); // checker
        o = obj;
    }

    public boolean isBoolean() {
        return "boolean".equals(typeof(o));
    }

    public boolean getAsBoolean() {
        return this.isBoolean() ? any(o) : Boolean.parseBoolean(this.getAsString());
    }

    public boolean isNumber() {
        return "number".equals(typeof(o));
    }

    public Number getAsNumber() {
        return this.isNumber() ? any(o) : getAsDouble();
    }

    public boolean isString() {
        return "string".equals(typeof(o));
    }

    public String getAsString() {
        return object(o).toString();
    }

    public double getAsDouble() {
        return this.isNumber() ? any(o) : Double.parseDouble(getAsString());
    }
}
