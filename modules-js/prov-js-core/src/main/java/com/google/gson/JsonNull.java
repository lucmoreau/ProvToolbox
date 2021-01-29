package com.google.gson;

public class JsonNull extends JsonElement {
    public static JsonNull INSTANCE = new JsonNull();
    private JsonNull() {
        super(null);
    }
}
