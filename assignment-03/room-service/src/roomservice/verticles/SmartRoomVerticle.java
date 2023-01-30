package roomservice.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import roomservice.RoomData;

/**
 * Central verticle that manages the state of the room
 */
public class SmartRoomVerticle extends AbstractVerticle {

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
            System.err.println("Invalid JSON input data!");
        }
    }
    
    /**
     * Handle serial line requests sent by arduino
     * @param msg
     */
    private void handleSerialRequests(final String msg) {
        switch(msg.charAt(0)) {
            case 'L':    
                this.setLight(Boolean.parseBoolean(msg.substring(1)));
            break;
            case 'R':
                this.setRollPercentage(Integer.parseInt(msg.substring(1)));
            break;
            default: 
                System.err.println("Unknown message from serial line");
        }
    }
    
    private void setLight(final boolean status) {
        if (this.light != status) {
            this.light = status;
            this.sendData(String.valueOf(this.light));
        }
    }
    
    private void setRollPercentage(final int percentage) {
        if (this.rollPercentage != percentage) {
            this.rollPercentage = percentage;
            this.sendData(String.valueOf(this.rollPercentage));
        }
    }
    
    /** 
     * Send data to the Serial Line
     * @param msg
     */
    private void sendData(final String msg) {
        vertx.eventBus().send("serial.tx", msg);
    }
    
}