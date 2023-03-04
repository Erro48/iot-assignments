package roomservice.verticles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class TimerVerticle extends AbstractVerticle {

    private static final String TIMER_ADDRESS = "timer";
    private static final int MAX_SIZE = 200;
    private final Map<String, List<Period>> recordings;

    public TimerVerticle() {
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
            final JsonObject jsonmsg = (JsonObject)msg.body();
            final String key = jsonmsg.getString("name");
            final int size;
            if (!jsonmsg.getString("parameter").isEmpty()) {
                size = Integer.parseInt(jsonmsg.getString("parameter"));
            } else {
                size = MAX_SIZE;
            }
            if (!this.recordings.containsKey(key)) {
                msg.reply("{ \"history\": [] }");
                return;
            }
            this.recordings.get(key).stream()
                    .map(period -> period.toJson().toString())
                    .sorted(Comparator.reverseOrder())
                    .limit(size)
                    .reduce((a, b) -> a += ", "+b)
                    .map(string -> "{ \"history\": [" + string + "] }")
                    .ifPresent(json -> msg.reply(json));
        });

    }

    private void handleStartRecording(final String event) {
        final LocalDateTime now = LocalDateTime.now();
        final List<Period> history = this.recordings.getOrDefault(event, new ArrayList<>());
        final Period period = new Period(now); 
        history.add(period);
        this.recordings.put(event, history);
    }

    private void handleStopRecording(final String event) {
        final List<Period> history = this.recordings.getOrDefault(event, new ArrayList<>());
        history.get(history.size()-1).setTo(LocalDateTime.now());
        this.recordings.put(event, history);
    }
}

@VertxGen
@DataObject
class Period {

    private final LocalDateTime from;
    private Optional<LocalDateTime> to;

    public Period(final LocalDateTime from, final LocalDateTime to) {
        this.to = Optional.of(to);
        this.from = from;
    }
    
    public Period(final LocalDateTime from) {
        this.from = from;
        this.to = Optional.empty();
    }
    
    public void setTo(final LocalDateTime to) {
        this.to = Optional.of(to);
    }

    public LocalDateTime getFrom() {
        return this.from;
    }

    public LocalDateTime getTo() {
        return this.to.isPresent() ? this.to.get() : LocalDateTime.MAX;
    }

    public JsonObject toJson() {
        final JsonObject json = new JsonObject();
        json.put("from", from.format(DateTimeFormatter.ISO_DATE_TIME).toString());
        this.to.ifPresent(date -> json.put("to", date.format(DateTimeFormatter.ISO_DATE_TIME).toString()));
        return json;
    }
}
