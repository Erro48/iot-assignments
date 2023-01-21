package roomservice;

import java.util.function.Consumer;

public interface MessageSource {
    
    void start();
    
    void setMessageHandler(Consumer<String> handler);
}
