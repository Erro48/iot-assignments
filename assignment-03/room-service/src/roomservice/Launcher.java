package roomservice;

import io.vertx.core.Vertx;
import jssc.SerialPortException;
import roomservice.verticles.*;

public class Launcher {
    
    private final static String MQTT_BROKER = "broker.mqtt-dashboard.com";
    private final static String MQTT_TOPIC = "roomservice/test";
    private final static int HTTP_PORT = 8888;
    
    public static void main(String[] args) throws SerialPortException {
        final Vertx vertx = Vertx.vertx();
        
        String serialPort = "COM2";
        int rate = 9600;
        
        if (args.length != 2) {
            System.out.println("Incorrect parameters, default parameters will be used");
            System.out.println("1 arg: SerialPort, 2 arg: Serial port rate");
        } else {
            serialPort = args[0];
            rate = Integer.parseInt(args[1]);
        }
        
        vertx.deployVerticle(new MqttVerticle(MQTT_BROKER, MQTT_TOPIC));
        vertx.deployVerticle(new SerialVerticle(serialPort, rate));
        vertx.deployVerticle(new HttpVerticle(HTTP_PORT));
        vertx.deployVerticle(new TimerVerticle());
        vertx.deployVerticle(new SmartRoomVerticle());
    }

}
