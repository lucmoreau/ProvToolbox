# j4ts-json
A JSweet implementation for https://github.com/google/gson json handler


example:
```
package test;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.*;

public class Test2 {
    private final int num;
    private final String msg;

    public Test2(int num, String msg) {
        this.num = num;
        this.msg = msg;
    }

    public static class TestDeserializer implements JsonDeserializer<Test2> {
        @Override
        public Test2 deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonArray()) {
                JsonArray asJsonArray = json.getAsJsonArray();
                if (asJsonArray.size() >= 2)
                    return new Test2(context.deserialize(asJsonArray.get(0), Integer.class), context.deserialize(asJsonArray.get(1), String.class));
            }
            throw new JsonParseException("not a valid Test");
        }
    }

    public static class TestSerializer implements JsonSerializer<Test2> {
        @Override
        public JsonElement serialize(Test2 src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(src.msg, src.num);
            return jsonObject;
        }

        // currently need this hack :'(
        public JsonElement serialize(Test2 src, Object typeOfSrc, JsonSerializationContext context) {
            return serialize(src, (Type) typeOfSrc, context);
        }
    }
    public static void main(String[] args) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Test2.class, new TestDeserializer())
                .registerTypeAdapter(Test2.class, new TestSerializer())
                .create();

        Test2 test = gson.fromJson("[5, \"test\"]", Test2.class);

        String s = gson.toJson(test);
        System.err.println(s);

        if (!Objects.equals("{\"test\":5}", s))
            throw new JsonParseException("Not equal :(");

        Map<?, ?> listSerializable = gson.fromJson("{\"a\": 1.1, \"b\": \"x\", \"c\": []}", Map.class);
        List<?> values = new LinkedList<>(listSerializable.values());
        String s1 = gson.toJson(values);
        System.err.println(s1);

        if (!Objects.equals("[1.1,\"x\",[]]", s1))
            throw new JsonParseException("Not equal :(");
    }
}

```

add maven dependency to your repo, and you can use it java such as javascript side
```
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.8.5</version>
</dependency>
```
