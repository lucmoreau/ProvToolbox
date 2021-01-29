package com.google.gson;

import def.js.JSON;
import jsweet.util.Lang;
import org.openprovenance.prov.model.Document;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Gson implements JsonSerializationContext, JsonDeserializationContext {
    private final Map<Class<?>, JsonSerializer<?>> serializers;
    private final Map<Class<?>, JsonDeserializer<?>> deserializers;

    Gson(Map<Class<?>, JsonSerializer<?>> serializers, Map<Class<?>, JsonDeserializer<?>> deserializers) {
        this.serializers = serializers;
        this.deserializers = deserializers;
    }

    public String toJson(Object src) {
        return toJson(serialize(src));
    }

    public String toJson(JsonElement src) {
        return src.toString();
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return fromJson(new JsonElement(JSON.parse(json)), classOfT);
    }

    public <T> T fromJson(JsonElement json, Class<T> classOfT) {
        return deserialize(json,classOfT);
    }

    @Override
    public JsonElement serialize(Object src) {
        Class<?> clazz = src.getClass();

        if (serializers.get(clazz) == null) {
            if (src instanceof Map<?, ?>) {
                clazz = Map.class;
            }
            if (src instanceof Iterable<?>) {
                clazz = Iterable.class;
            }
        }

        return serialize(src, clazz);
    }

    @Override
    public JsonElement serialize(Object src, Class<?> typeOfSrc) {
        JsonSerializer<?> jsonSerializer = serializers.get(typeOfSrc);
        if (jsonSerializer != null) {
            return jsonSerializer.serialize(Lang.any(src), Lang.any(typeOfSrc), this);
        }

        if (src instanceof JsonElement) {
            return (JsonElement) src;
        }

        if (Map.class == typeOfSrc || HashMap.class == typeOfSrc) {
            Map<?, ?> l = Lang.any(src);
            JsonObject obj = new JsonObject();

          //  l.keySet().forEach(kv -> {
          //      obj.add(kv.toString(), serialize(l.get(kv)));
           // });

            l.entrySet().forEach(kv -> {
                obj.add(kv.getKey().toString(), serialize(kv.getValue()));
            });
            return obj;
        }


        if (Iterable.class == typeOfSrc) {
            Iterable<?> l = Lang.any(src);
            JsonArray array = new JsonArray();
            for (Object o : l) {
                array.add(serialize(o));
            }
            return array;
        }


        return new JsonElement(src);
    }

    private Object deserialize_default(JsonElement je) throws JsonParseException {
        if (je.isJsonArray())
            return deserialize(je, List.class);
        if (je.isJsonObject())
            return deserialize(je, Map.class);
        if (je.isJsonNull())
            return deserialize(je, Object.class);
        if (je.isJsonPrimitive()) {
            JsonPrimitive jp = je.getAsJsonPrimitive();
            if (jp.isBoolean())
                return deserialize(je, Boolean.class);
            if (jp.isNumber())
                return deserialize(je, Number.class);
            if (jp.isString())
                return deserialize(je, String.class);
        }
        throw new IllegalStateException("Unrecognizable json element");
    }

    @Override
    public <T> T deserialize(JsonElement je, Class<T> classOfT) throws JsonParseException {
        JsonDeserializer<?> jsonDeserializer = deserializers.get(classOfT);
        if (jsonDeserializer != null)
            return Lang.any(jsonDeserializer.deserialize(je, Lang.any(classOfT), this));

        if (Object.class == classOfT)
            return Lang.any(je.o);
        if (String.class == classOfT)
            return Lang.any(je.o.toString());

        if (JsonElement.class == classOfT) {
            return Lang.any(je);
        }
        if (JsonArray.class == classOfT) {
            return Lang.any(je.getAsJsonArray());
        }
        if (JsonNull.class == classOfT) {
            if (je.isJsonNull())
                return Lang.any(JsonNull.INSTANCE);

            throw new IllegalStateException("Not a JSON null: " + je.toString());
        }
        if (JsonObject.class == classOfT) {
            return Lang.any(je.getAsJsonObject());
        }
        if (JsonPrimitive.class == classOfT) {
            return Lang.any(je.getAsJsonPrimitive());
        }

        if (Number.class == classOfT ||
                Float.class == classOfT ||
                Integer.class == classOfT ||
                Double.class == classOfT ||
                Long.class == classOfT)
            return Lang.any(je.getAsJsonPrimitive().getAsDouble());

        if (Boolean.class == classOfT)
            return Lang.any(je.getAsJsonPrimitive().getAsBoolean());

        if (List.class == classOfT || ArrayList.class == classOfT) {
            JsonArray ja = je.getAsJsonArray();
            List<Object> res = new ArrayList<>(ja.size());
            for (int i = 0; i < ja.size(); ++i)
                res.add(deserialize_default(ja.get(i)));
            return Lang.any(res);
        }

        if (Map.class == classOfT || HashMap.class == classOfT) {
            JsonObject ja = je.getAsJsonObject();
            HashMap<String, Object> res = new HashMap<>();
            for (String key : ja.keySet())
                res.put(key, deserialize_default(ja.get(key)));
            return Lang.any(res);
        }

        return Lang.any(je.o);
    }

    public Object fromJson(Object buf, Class class1) {
        return null;
    }

    public void toJson(Document doc, Object writer) {
    }
}
