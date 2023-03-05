package roomservice.verticles;

import java.time.LocalDateTime;
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
    private final static int LIGHT_THRESHOLD = 100; 
    
    private boolean isFirstEntrance;
    private boolean roomOccupied;
    private boolean light;
    private int rollPercentage;
    
    public SmartRoomVerticle() {
        this.light = false;
        this.rollPercentage = 0;
        this.roomOccupied = false;
        this.isFirstEntrance = true;
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
            final LocalDateTime now = LocalDateTime.now();
    
            /* Someone exits the room */
            if (this.roomOccupied && this.roomOccupied != data.isSomeoneIn()) {
            	this.setLight(false);
            }
            
            /* If someone enters and the room is dark */
            if(data.isSomeoneIn() && this.isDark(data.getLight())) {
            	this.setLight(true);
            }
            
            /* If someone enters after 8:00 the first time */
            if (this.isFirstEntrance && data.isSomeoneIn() && now.getHour() >= 8 && now.getHour() < 19) {
            	this.setRollPercentage(0);
            	this.isFirstEntrance = false;
            }
            
            /* Roller blinds are unrolled at 19 if no one is in the room */
            if (!data.isSomeoneIn() && now.getHour() >= 19 && now.getHour() < 8) {
            	this.setRollPercentage(100);
            	this.isFirstEntrance = true;
            }
            
            this.roomOccupied = data.isSomeoneIn();
            
        } catch (Exception e) {
           this.logger.warn("Invalid JSON input data!");
        }
    }
    
    /**
     * Handle serial line requests sent by arduino
     * @param msg
     */
    private void handleSerialRequests(final String msg) {
        switch(msg.charAt(0)) {
            case 'L':    
                this.setLight(!this.light);
            break;
            case 'M':
            	int newRoll = this.rollPercentage + Integer.parseInt(msg.substring(1));
                if (newRoll > 100)
                	newRoll = 100;
                else if (newRoll < 0)
                	newRoll = 0;
            	this.setRollPercentage(newRoll);
            break;
            default:
            	this.logger.warn("Unknown message from serial line");
        }
    }
    
    private boolean isDark(final int light) {
    	return light <= LIGHT_THRESHOLD;
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
