package roomservice;

import java.time.Duration;
import java.util.Calendar;

/**
 * Logs and records for how much time a status remains valid
 */
public interface TimeLogger {

    /**
     * Start the recording of a specific event
     * @param e
     *          The event
     */
    void startRecording(Event e);
  
    /** 
     * Stop the recording of a specific event. If an event has not been started yet nothing happens.
     * @param e
     *          The event
     */
    void stopRecording(Event e);
    
    /**
     * Retrieves the recording log of the event
     * @param e
     *          The event
     * @return
     *          A {@link Duration} object with the desired data
     */
    Duration getTimeOf(Event e);
    
    
    /**
     * @return
     *          The current hour 0-23
     */
    default int getCurrentHourTime() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);            
    }
}
