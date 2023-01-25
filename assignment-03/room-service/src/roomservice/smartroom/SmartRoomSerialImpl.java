package roomservice.smartroom;

import java.security.InvalidParameterException;
import java.util.Optional;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;
import jssc.SerialPortException;
import roomservice.Event;
import roomservice.MessageSource;
import roomservice.TimeLogger;
import roomservice.TimeLoggerImpl;

public class SmartRoomSerialImpl implements SmartRoom {

    private static final int MORNING_HOUR = 8;
    private static final int EVENING_HOUR = 19;
    private static final int DARK_ROOM_LIMIT = 120;
    private static final String SERIAL_COM = "COM2";
    private static final int SERIAL_RATE = 9600;

    private boolean lightOn;
    private int rollPercentage;
    private SmartRoomController controller;
    private final TimeLogger timer;

    public SmartRoomSerialImpl() throws SerialPortException {
        this.lightOn = false;
        this.rollPercentage = 0;
        this.timer = new TimeLoggerImpl();
        final SerialHandler sh = new SerialHandler(SERIAL_COM, SERIAL_RATE);
        this.controller = new SmartRoomControllerImpl(sh.getChannel());
        Vertx.vertx().deployVerticle(new RestAPI());
        sh.start();
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

            if (isDark(data.getLight()) && someoneEntering(data.getPir())) {
                this.setLight(true);
            }

            if (this.timer.getCurrentHourTime() == MORNING_HOUR && this.timer.getCurrentHourTime() < EVENING_HOUR 
                    && someoneEntering(data.getPir())) {
                this.setRollPercentage(0);
            }

            if (this.timer.getCurrentHourTime() == EVENING_HOUR && this.timer.getCurrentHourTime() < MORNING_HOUR
                    && someoneEntering(data.getPir())) {
                this.setRollPercentage(100);
            }   
        });
    }

    private void setRollPercentage(final int percentage) {
        if (percentage > 100 || percentage < 0) {
            throw new InvalidParameterException("The number must be a valid percentage");
        }

        if (this.rollPercentage != percentage) {
            this.controller.rollTo(percentage);
            this.rollPercentage = percentage;
        }
    }

    private void setLight(final boolean status) {
        if (this.lightOn != status) {
            this.controller.turnLight(status);
            this.lightOn = status;
        }
    }

    private boolean isDark(final int light) {
        return light < DARK_ROOM_LIMIT;
    }

    private boolean someoneEntering(final boolean pir) {
        return pir;
    }

    private class RestAPI extends AbstractVerticle {
        private static final int PORT = 8888;

        @Override
        public void start() {
            // Create the HTTP server
            final Router router = Router.router(vertx);

            /* TODO remove */
            router.route().handler(CorsHandler.create("*")
                    .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                    .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                    .allowedMethod(io.vertx.core.http.HttpMethod.OPTIONS)
                    .allowedHeader("Access-Control-Request-Method")
                    .allowedHeader("Access-Control-Allow-Credentials")
                    .allowedHeader("Access-Control-Allow-Origin")
                    .allowedHeader("Access-Control-Allow-Headers")
                    .allowedHeader("Content-Type"));


            router.get("/light")
            .respond( ctx -> Future.succeededFuture(lightOn));

            router.get("/roll")
            .respond( ctx -> Future.succeededFuture(rollPercentage));

            router.get("/lighttime")
            .respond( ctx -> Future.succeededFuture(timer.getTimeOf(Event.LIGHT_ON).toSeconds()));

            router.put("/light")
            .handler( ctx -> {
                final String status = ctx.request().getParam("status", "false");
                controller.turnLight(Boolean.parseBoolean(status));
                ctx.response().end();
            });

            router.put("/roll")
            .handler( ctx -> {
                final String percentage = ctx.request().getParam("percentage", "0");
                controller.rollTo(Integer.parseInt(percentage));
                ctx.response().end();
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

    /**
     * The SerialHandler is responsible for the communication with Arduino 
     */
    private class SerialHandler extends Thread {

        private Optional<CommChannel> channel = Optional.empty();

        public SerialHandler(final String port, final int rate) {
            try {
                this.channel = Optional.of(new SerialCommChannel(port, rate));
            } catch (Exception e) {
                System.err.println("Cannot create a CommChannel at port: " + port);
            } 
            this.setDaemon(true);
        }

        @Override
        public void run() {
            while(true && this.channel.isPresent()) {
                synchronized (this.channel.get()) {
                    if (this.channel.get().isMsgAvailable()) {
                        try {
                            final String message = this.channel.get().receiveMsg();
                            System.out.println("ARDUINO -- Received: " + message);

                            if (message.startsWith("l")) {
                                lightOn = Boolean.parseBoolean(message.substring(1));
                                this.log("Light state changed");
                            } else if (message.startsWith("r")) {
                                rollPercentage = Integer.parseInt(message.substring(1));
                                this.log("Roller blinds state changed");
                            }

                        } catch (InterruptedException | NumberFormatException e) {
                            System.err.println("Unknown message");
                        }   
                    }    
                }
            }

        }

        private void log(final String message) {
            System.out.println("ARDUINO -- " + message);
        }

        public CommChannel getChannel() {
            return this.channel.isPresent() ? this.channel.get() : null;
        }
    } 

}

