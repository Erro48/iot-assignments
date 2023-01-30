package roomservice.verticles;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

public class TimerVerticle extends AbstractVerticle {
    
    private static final String TIMER_ADDRESS = "timer.";
    
    public TimerVerticle() {
        
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
        
    }
    
    private void handleStopRecording(final String event) {
        
    }
}
