package com.google.gson;

public interface JsonDeserializationContext {
    <T> T deserialize(JsonElement json, Class<T> typeOfT) throws JsonParseException;
}
