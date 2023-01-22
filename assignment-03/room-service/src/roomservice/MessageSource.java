package roomservice;

import java.util.function.Consumer;

public interface MessageSource {
    
    /**
     * Start the service
     */
    void start();
    
    /**
     * Setup an handler/callback executed every time a new Message arrives
     * @param handler
     *          The callback consumer
     */
    void setMessageHandler(Consumer<String> handler);
}
