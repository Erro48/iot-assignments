package roomservice.smartroom;

import roomservice.MessageSource;

/**
 * The SmartRoom interface
 */
public interface SmartRoom {
    
    /**
     * Set a message source for this SmartRoom. The {@link MessageSource} is 
     * the object that provides sensor data.
     * @param ms
     *          the source
     */
    void setInputDataFrom(MessageSource ms);
}
