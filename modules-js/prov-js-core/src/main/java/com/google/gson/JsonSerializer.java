package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T> {
    JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context);
}
