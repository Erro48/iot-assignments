package roomservice.smartroom;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import roomservice.Event;
import roomservice.MessageSource;
import roomservice.TimeLogger;
import roomservice.TimeLoggerImpl;

public class SmartRoomImpl implements SmartRoom {
    
    private static final int DARK_ROOM_LIMIT = 120;
    
    private SmartRoomMode mode;
    private boolean lightOn;
    private int rollPercentage;
    private SmartRoomController controller;
    private final TimeLogger timer;

    public SmartRoomImpl() {
        this.lightOn = false;
        this.rollPercentage = 0;
        this.timer = new TimeLoggerImpl();
        new RestAPI().start();
        this.mode = SmartRoomMode.AUTOMATIC;
    }

    @Override
    public void setInputDataFrom(final MessageSource ms) {
        ms.setMessageHandler(message -> {
            RoomData data = new RoomData(0, false);
            try {
                 data = new RoomData(message);  
            } catch (Exception e) {
                System.err.println("The JSON object received is not valid");
            }
            
            if (this.mode == SmartRoomMode.AUTOMATIC) {
                if (isDark(data.getLight()) && someoneEntering(data.getPir())) {
                    this.controller.turnLight(true);
                }
                
                if (this.timer.getCurrentHourTime() >= 8 && this.timer.getCurrentHourTime() < 19 
                        && someoneEntering(data.getPir())) {
                    this.controller.rollTo(0);
                }
                
                if (this.timer.getCurrentHourTime() >= 19 && this.timer.getCurrentHourTime() < 8
                        && someoneEntering(data.getPir())) {
                    this.controller.rollTo(100);
                }   
            }
        });
    }
    
    private boolean isDark(final int light) {
        return light < DARK_ROOM_LIMIT;
    }
    
    private boolean someoneEntering(final boolean pir) {
        return pir;
    }

    @Override
    public void setSmartRoomController(final SmartRoomController c) {
        this.controller = c;
    }

    private class RestAPI extends AbstractVerticle {
        private static final int PORT = 8888;

        
        @Override
        public void start() {
            // Create the HTTP server
            final Vertx vertx = Vertx.vertx();
            final Router router = Router.router(vertx);

            router.get("/light")
                    .respond( ctx -> Future.succeededFuture(lightOn));

            router.get("/roll")
                    .respond( ctx -> Future.succeededFuture(rollPercentage));

            router.get("/lighttime")
                    .respond( ctx -> Future.succeededFuture(timer.getTimeOf(Event.LIGHT_ON).toSeconds()));

            router.post("/light")
                    .respond( ctx -> {
                        final String status = ctx.request().getParam("status", "0");
                        controller.turnLight(Boolean.parseBoolean(status));
                        return Future.succeededFuture();
                    });

            router.post("/roll")
                    .respond( ctx -> {
                        final String percentage = ctx.request().getParam("percentage", "0");
                        controller.rollTo(Integer.parseInt(percentage));
                        return Future.succeededFuture();
                    });

            vertx.createHttpServer()
                    // Handle every request using the router
                    .requestHandler(router)
                    // Start listening
                    .listen(PORT)
                    // Print the port
                    .onSuccess(server -> {
                        System.out.println("HTTP server started on port " + server.actualPort());
                    })
                    .onFailure(event -> {
                        System.out.println("Failed to start HTTP server:" + event.getMessage());
                    });
        }
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
