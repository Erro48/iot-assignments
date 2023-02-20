package roomservice.verticles;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import roomservice.RoomData;

/**
 * Central verticle that manages the state of the room
 */
public class SmartRoomVerticle extends AbstractVerticle {

	private final Logger logger = LoggerFactory.getLogger(SmartRoomVerticle.class);
    private boolean light;
    private int rollPercentage;
    
    public SmartRoomVerticle() {
        this.light = false;
        this.rollPercentage = 0;
    }
    
    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        
        eventBus.consumer("mqtt", msg -> this.handleRawData((String)msg.body()));
        eventBus.consumer("serial.rx", msg -> this.handleSerialRequests((String)msg.body()));
        
        /* HTTP Api */
        eventBus.consumer("request.light", msg -> msg.reply(String.valueOf(this.light)));
        eventBus.consumer("request.rollpercentage", msg -> msg.reply(String.valueOf(rollPercentage)));
        
        eventBus.consumer("controller.light", msg -> this.setLight(Boolean.parseBoolean((String)msg.body())));
        eventBus.consumer("controller.rollpercentage", msg -> this.setRollPercentage(Integer.parseInt((String)msg.body())));
    }
    
    /**
     * Handle sensor data and compute the next state
     * @param msg, 
     *          JSON object with sensor data
     */
    private void handleRawData(final String msg) {
        try {
            final RoomData data = new RoomData(msg);
            /* 
             * Get the data and handle it 
             */
        } catch (Exception e) {
           this.logger.warn("Invalid JSON input data!");
        }
    }
    
    /**
     * Handle serial line requests sent by arduino
     * @param msg
     */
    private void handleSerialRequests(final String msg) {
        //this.logger.info("Serial message received: {}", msg);
        switch(msg.charAt(0)) {
            case 'L':    
                this.setLight(Boolean.parseBoolean(msg.substring(1)));
            break;
            case 'M':
                this.setRollPercentage(Integer.parseInt(msg.substring(1)));
            break;
            default:
            	this.logger.warn("Unknown message from serial line");
        }
    }
    
    private void setLight(final boolean status) {
        if (this.light != status) {
            this.light = status;
            this.sendData("L" + String.valueOf(this.light));
            /* Start recording */
            if (this.light) {
                this.startRecord("light");
            } else this.stopRecord("light");
        }
    }
    
    private void setRollPercentage(final int percentage) {
        if (this.rollPercentage != percentage) {
            this.rollPercentage = percentage;
            this.sendData("M" + String.valueOf(this.rollPercentage));
        }
    }
    
    /** 
     * Send data to the Serial Line
     * @param msg
     */
    private void sendData(final String msg) {
        vertx.eventBus().send("serial.tx", msg);
    }
    
    private void startRecord(final String event) {
        final EventBus eventBus = vertx.eventBus();
        eventBus.send("timer", new JsonObject(Map.of("event", event, "request", "start")));
    }
    
    private void stopRecord(final String event) {
        final EventBus eventBus = vertx.eventBus();
        eventBus.send("timer", new JsonObject(Map.of("event", event, "request", "stop")));
    }
}
