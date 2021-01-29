package com.google.gson;

import def.js.Array;
import jsweet.util.Lang;

import java.util.Iterator;

import static jsweet.util.Lang.*;

public class JsonArray extends JsonElement implements Iterable<JsonElement> {
    public JsonArray(Array<?> array) {
        super(array);
    }

    public JsonArray() {
        this(new Array<>());
    }

    public JsonArray(int capacity) {
        this(new Array<>(capacity));
    }

    private Array<Object> asArray() {
        return Lang.any(o);
    }

    public void add(Boolean boolVal) {
        asArray().push(bool(boolVal));
    }

    public void add(Number numberVal) {
        asArray().push(number(numberVal));
    }

    public void add(String stringVal) {
        asArray().push(string(stringVal));
    }

    public void add(JsonElement element) {
        if (element == null) {
            element = JsonNull.INSTANCE;
        }

        asArray().push(element.o);
    }

    public JsonElement set(int index, JsonElement element) {
        Object othO = asArray().$get(index);
        asArray().$set(index, element.o);
        return new JsonElement(othO);
    }

    public boolean remove(JsonElement element) {
        for (int i = 0; i < asArray().length; ++i) {
            if ($strict(asArray().$get(i) == element.o)) {
                remove(i);
                return true;
            }
        }
        return false;
    }
    public JsonElement remove(int index) {
        Array<Object> splice = asArray().splice(index, 1);
        return new JsonElement(splice.$get(0));
    }

    public boolean contains(JsonElement element) {
        for (int i = 0; i < asArray().length; ++i) {
            if ($strict(asArray().$get(i) == element.o)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return asArray().length;
    }

    public Iterator<JsonElement> iterator() {
        return new Iterator<JsonElement>() {
            Array<Object> a = asArray();
            private int pos;
            @Override
            public boolean hasNext() {
                return pos < a.length;
            }

            @Override
            public JsonElement next() {
                return new JsonElement(a.$get(pos++));
            }

            @Override
            public void remove() {
                a.splice(--pos, 1);
            }
        };
    }

    public JsonElement get(int i) {
        return new JsonElement(asArray().$get(i));
    }
}
