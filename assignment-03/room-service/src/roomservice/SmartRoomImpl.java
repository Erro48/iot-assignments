package roomservice;


import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class SmartRoomImpl implements SmartRoom {

    private boolean lightOn;
    private int rollPercentage;
    private SmartRoomController controller;
 
    public SmartRoomImpl() {
        this.lightOn = false;
        this.rollPercentage = 0;
        this.startRestServer();
    }
    
    @Override
    public void setInputDataFrom(final MessageSource ms) {
        ms.setMessageHandler(message -> {
            try {
                System.out.println("MESSAGE RECEIVED IN SmartRoom: " + message);
                //final RoomData data = new RoomData(message);  
                //this.controller.turnLight(false);
            } catch (Exception e) {
                e.printStackTrace();
            }    
        });
    }

    @Override
    public void setSmartRoomController(SmartRoomController c) {
        this.controller = c;
    }
    
    private void startRestServer() {
        // Create the HTTP server
        final Vertx vertx = Vertx.vertx();
        final Router router = Router.router(vertx);
        
        router.get("/light")
               .respond( ctx -> Future.succeededFuture(this.lightOn));
        
        router.get("/roll")
               .respond( ctx -> Future.succeededFuture(this.rollPercentage));

        vertx.createHttpServer()
            // Handle every request using the router
            .requestHandler(router)
            // Start listening
            .listen(8888)
            // Print the port
            .onSuccess(server -> {
                System.out.println("HTTP server started on port " + server.actualPort());
            })
            .onFailure(event -> {
                System.out.println("Failed to start HTTP server:" + event.getMessage());
            });
    }

}


class RoomData {
    
    private final static String LIGHT_KEY = "light";
    private final static String PIR_KEY = "pir";
    
    private final int light;
    private final boolean pir;
    
    public RoomData(final String jsonData) throws Exception {
        Object obj = Json.decodeValue(jsonData);
        if (!JsonObject.class.isInstance(obj)) {
            throw new Exception();
        }
        JsonObject json = (JsonObject)obj;
        
        if (json.containsKey(LIGHT_KEY)) {
            this.light = json.getInteger(LIGHT_KEY);
        } else this.light = 0;
        
        if (json.containsKey(PIR_KEY)) {
            this.pir = json.getBoolean(PIR_KEY);
        } else this.pir = false;
    }
    
    public RoomData(final int light, final boolean pir) {
        this.light = light;
        this.pir = pir;
    }
    
    public int getLight() {
        return this.light;
    }
    
    public boolean getPir() {
        return this.pir;
    }
}
