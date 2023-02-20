package roomservice.verticles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class TimerVerticle extends AbstractVerticle {

    private static final String TIMER_ADDRESS = "timer";
    private final Map<String, LocalDateTime> timestamps;
    private final Map<String, List<Period>> recordings;

    public TimerVerticle() {
        this.timestamps = new HashMap<>();
        this.recordings = new HashMap<>();
    }

    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();

        eventBus.consumer(TIMER_ADDRESS, msg -> {
            final JsonObject json = (JsonObject)msg.body();
            final String event = json.getString("event");
            final String request = json.getString("request");

            switch(request) {
            case "start":
                this.handleStartRecording(event);
                break;
            case "stop":
                this.handleStopRecording(event);
                break;
            }
        });


        eventBus.consumer("request.history", msg -> {
            final String key = (String)msg.body();
            this.recordings.get(key).stream()
                    .map(period -> period.toJson().toString())
                    .reduce((a, b) -> a += ", "+b)
                    .map(string -> "{" + string + "}")
                    .ifPresent(json -> msg.reply(json));
        });

    }

    private void handleStartRecording(final String event) {
        final LocalDateTime now = LocalDateTime.now();
        this.timestamps.putIfAbsent(event, now);
    }

    private void handleStopRecording(final String event) {
        final Period period = new Period(this.timestamps.get(event), LocalDateTime.now());
        this.timestamps.remove(event);
        final List<Period> history = this.recordings.getOrDefault(event, new ArrayList<>());
        history.add(period);
        this.recordings.put(event, history);
    }
}

@VertxGen
@DataObject
class Period {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public Period(final LocalDateTime from, final LocalDateTime to) {
        this.to = to;
        this.from = from;
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to;
    }

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        json.put("from", from.format(DateTimeFormatter.ISO_DATE_TIME).toString());
        json.put("to", to.format(DateTimeFormatter.ISO_DATE_TIME).toString());
        return json;
    }
}
