package roomservice;

import io.vertx.core.Vertx;
import jssc.SerialPortException;
import roomservice.verticles.MqttVerticle;
import roomservice.verticles.SerialVerticle;

public class Launcher {
    /**
     *  1. Receive MQTT messages with raw data
     *  2. Parse data to values
     *  3. Modify smart room state
     *  4. Send values to arduino to control
     *  5. Provide data with http to the dashboard
     * @throws SerialPortException 
     */
    public static void main(String[] args) throws SerialPortException {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MqttVerticle("broker.mqtt-dashboard.com", "roomservice/test"));
        vertx.deployVerticle(new SerialVerticle("COM2", 9600));
    }

}
