package roomservice.verticles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class TimerVerticle extends AbstractVerticle {
    
    private static final String TIMER_ADDRESS = "timer.";
    private final Map<String, Instant> timestamps;
    private final Map<String, List<Period>> recordings;
    
    public TimerVerticle() {
        this.timestamps = new HashMap<>();
        this.recordings = new HashMap<>();
    }
    
    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        
        eventBus.addInboundInterceptor(ctx -> {
            final String address = ctx.message().address();
            if (!address.startsWith(TIMER_ADDRESS)) {
                ctx.next();
                return;    
            }
            
            final JsonObject json = (JsonObject) ctx.message().body();
            switch((String)json.getValue("request")) {
                case "start":
                    this.handleStartRecording(address.substring(TIMER_ADDRESS.length()));
                break;
                case "stop":
                    this.handleStopRecording(address.substring(TIMER_ADDRESS.length()));
                break;
            
            }
            ctx.next();
        });
    }
    
    private void handleStartRecording(final String event) {
        final Instant now = Instant.now();
        this.timestamps.putIfAbsent(event, now);
    }
    
    private void handleStopRecording(final String event) {
        final Period period = new Period(this.timestamps.get(event), Instant.now());
        this.timestamps.remove(event);
        final List<Period> history = this.recordings.getOrDefault(event, new ArrayList<>());
        history.add(period);
    }
}

class Period {
   
    private final Instant from;
    private final Instant to;
    
    public Period(final Instant from, final Instant to) {
        this.to = to;
        this.from = from;
    }
    
    public Instant getFrom() {
        return this.from;
    }
    
    public Instant getTo() {
        return this.to;
    }
}
