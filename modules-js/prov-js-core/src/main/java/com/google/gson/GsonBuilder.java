package com.google.gson;

import java.util.HashMap;
import java.util.Map;

public final class GsonBuilder {
    Map<Class<?>, JsonSerializer<?>> serializers = new HashMap<>();
    Map<Class<?>, JsonDeserializer<?>> deserializers = new HashMap<>();

    public GsonBuilder registerTypeAdapter(Class<?> type, Object o) {
        if (o instanceof JsonSerializer<?>)
            serializers.put(type, (JsonSerializer<?>) o);
        else if (o instanceof JsonDeserializer<?>)
            deserializers.put(type, (JsonDeserializer<?>) o);
        else
            throw new IllegalArgumentException("Unrecognized type adapter");

        return this;
    }
    public Gson create() {
        return new Gson(serializers, deserializers);
    }

    public GsonBuilder setPrettyPrinting() {
        return this;
    }
}