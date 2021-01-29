package com.google.gson;

import def.js.Array;

import jsweet.util.Lang;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static def.js.Globals.undefined;
import static jsweet.util.Lang.*;

public class JsonObject extends JsonElement {
    public JsonObject() {
        this(new def.js.Object());
    }

    public JsonObject(def.js.Object o) {
        super(o);
    }

    private def.js.Object asObject() {
        return object(o);
    }

    public void add(String property, JsonElement value) {
        if (value == null) {
            value = JsonNull.INSTANCE;
        }

        asObject().$set(property, value.o);
    }

    public JsonElement remove(String property) {
        JsonElement jsonElement = get(property);
        asObject().$delete(property);
        return jsonElement;
    }

    public void addProperty(String property, String value) {
        asObject().$set(property, string(value));
    }

    public void addProperty(String property, Number value) {
        asObject().$set(property, number(value));
    }

    public void addProperty(String property, Boolean value) {
        asObject().$set(property, bool(value));
    }

    public Set<String> keySet() {
        return new HashSet<>(Arrays.asList(Lang.array(Lang.<Array<String>> any(def.js.Object.keys(o)))));
    }

    public int size() {
        return keySet().size();
    }

    public boolean has(String memberName) {
        return Lang.$strict(asObject().$get(memberName) == undefined);
    }

    public JsonElement get(String memberName) {
        return new JsonElement(asObject().$get(memberName));
    }

    public JsonPrimitive getAsJsonPrimitive(String memberName) {
        return get(memberName).getAsJsonPrimitive();
    }

    public JsonArray getAsJsonArray(String memberName) {
        return get(memberName).getAsJsonArray();
    }

    public JsonObject getAsJsonObject(String memberName) {
        return get(memberName).getAsJsonObject();
    }
}
