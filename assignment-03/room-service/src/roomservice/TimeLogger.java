package roomservice;

import java.time.Duration;

public interface TimeLogger {

    void startRecording(Event e);
    
    void stopRecording(Event e);
    
    Duration getTimeOf(Event e);
}

enum Event {
    LIGHT_ON;
}