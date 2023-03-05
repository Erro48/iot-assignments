package roomservice;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class RoomData {

    private final static String LIGHT_KEY = "light";
    private final static String PIR_KEY = "pir";

    private final int light;
    private final boolean someoneIn;

    public RoomData(final String jsonData) throws Exception {
        Object obj = Json.decodeValue(jsonData);
        if (!JsonObject.class.isInstance(obj)) {
            throw new Exception();
        }
        JsonObject json = (JsonObject)obj;

        if (json.containsKey(LIGHT_KEY)) {
            this.light = json.getInteger(LIGHT_KEY);
        } else this.light = 0;

        if (json.containsKey(PIR_KEY)) {
            this.someoneIn = json.getBoolean(PIR_KEY);
        } else this.someoneIn = false;
    }

    public RoomData(final int light, final boolean pir) {
        this.light = light;
        this.someoneIn = pir;
    }

    public int getLight() {
        return this.light;
    }

    public boolean isSomeoneIn() {
        return this.someoneIn;
    }
}