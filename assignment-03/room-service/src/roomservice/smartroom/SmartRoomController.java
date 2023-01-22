package roomservice.smartroom;

/**
 * The controller that controls the actuators (e.g Arduino)
 */
public interface SmartRoomController {

    /**
     * Roll the rollerblinds to a specific percentage
     * @param percentage
     *          The amount
     */
    void rollTo(int percentage);
    
    /** 
     * Turn on/off the lights
     * @param status
     *          
     */
    void turnLight(boolean status);
}
