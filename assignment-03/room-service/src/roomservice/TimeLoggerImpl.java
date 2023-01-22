package roomservice;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TimeLoggerImpl implements TimeLogger {

    private final Map<Event, Duration> recordings;
    private final Set<Event> stopped;
    private Instant timestamp;
    
    
    public TimeLoggerImpl() {
        this.recordings = new HashMap<>();
        this.stopped = new HashSet<>();
        this.timestamp = Instant.now();
    }
    
    @Override
    public void startRecording(final Event e) {
        final Instant i = Instant.now();
        this.updateDurations(this.computeDeltaSeconds(i));
        this.timestamp = i;
        if (this.recordings.containsKey(e)) {
            this.stopped.remove(e);
        } else {
            this.recordings.put(e, Duration.ofSeconds(0));
        }
    }

    @Override
    public void stopRecording(final Event e) {
        final Instant i = Instant.now();
        this.updateDurations(this.computeDeltaSeconds(i));
        this.timestamp = i;
        this.stopped.add(e);
    }

    @Override
    public Duration getTimeOf(final Event e) {
        return this.recordings.get(e);
    }
    
    private long computeDeltaSeconds(final Instant newInstant) {
        return newInstant.minus(this.timestamp.getEpochSecond(), ChronoUnit.SECONDS).getEpochSecond();
    }
    
    private void updateDurations(final long deltaSeconds) {
        this.recordings.keySet().stream()
                        .filter(k -> !this.stopped.contains(k))
                        .forEach(k -> this.recordings.put(k, this.recordings.get(k).plusSeconds(deltaSeconds)));
    }

}
