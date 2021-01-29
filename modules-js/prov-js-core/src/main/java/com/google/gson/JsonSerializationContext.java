package com.google.gson;

public interface JsonSerializationContext {
    JsonElement serialize(Object src);

    JsonElement serialize(Object src, Class<?> typeOfSrc);
}
