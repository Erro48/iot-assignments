package roomservice.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import roomservice.RoomData;

/**
 * Central verticle that manages the state of the room
 */
public class SmartRoomVerticle extends AbstractVerticle {

    private final boolean light;
    private final int rollPercentage;
    
    public SmartRoomVerticle() {
        this.light = false;
        this.rollPercentage = 0;
    }
    
    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        
        eventBus.consumer("mqtt", message -> handleInputMessage(message));
    }
    
    private void handleInputMessage(final Message<Object> msg) {
        try {
            final RoomData data = new RoomData((String)msg.body());
            
        } catch (Exception e) {
            System.err.println("Invalid JSON input data!");
        }
    }
    
}
