package com.google.gson;

import def.js.JSON;
import jsweet.util.Lang;

//import java.util.Objects;

import static jsweet.util.Lang.typeof;
import static jsweet.util.Lang.object;

public class JsonElement {
    protected Object o;

    public JsonElement(Object o) {
        this.o = o;
    }

    public boolean isJsonArray() {
        return def.js.Array.isArray(o);
    }

    public boolean isJsonObject() {
        return !isJsonNull() && "object".equals(typeof(o));
    }

    public boolean isJsonPrimitive() {
        switch (typeof(o)) {
            case "boolean":
            case "number":
            case "string":
                return true;
        }
        return false;
    }

    public boolean isJsonNull() {
        return Lang.$strict(o == null);
    }

    public JsonObject getAsJsonObject() {
        if (this.isJsonObject()) {
            return this instanceof JsonObject ? Lang.any(this) : new JsonObject(object(o));
        } else {
            throw new IllegalStateException("Not a JSON Object: " + this);
        }
    }

    public JsonArray getAsJsonArray() {
        if (this.isJsonArray()) {
            return this instanceof JsonArray ? Lang.any(this) : new JsonArray(Lang.<def.js.Array<Object>> any(o));
        } else {
            throw new IllegalStateException("Not a JSON Array: " + this);
        }
    }

    public JsonPrimitive getAsJsonPrimitive() {
        if (this.isJsonPrimitive()) {
            if (this instanceof JsonPrimitive)
                return Lang.any(this);
            switch (typeof(o)) {
                case "boolean":
                    return new JsonPrimitive(Lang.<Boolean> any(o));
                case "number":
                    return new JsonPrimitive(Lang.<Number> any(o));
                case "string":
                    return new JsonPrimitive(Lang.<String> any(o));
            }
            throw new IllegalStateException("Not a JSON Primitive: " + this);
        } else {
            throw new IllegalStateException("Not a JSON Primitive: " + this);
        }
    }

    public JsonNull getAsJsonNull() {
        if (this.isJsonNull()) {
            return (JsonNull)this;
        } else {
            throw new IllegalStateException("Not a JSON Null: " + this);
        }
    }

    public String toString() {
        return JSON.stringify(o);
    }
}
