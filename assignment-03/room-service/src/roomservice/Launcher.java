package roomservice;

import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import jssc.SerialPortException;
import roomservice.smartroom.SmartRoom;
import roomservice.smartroom.SmartRoomSerialImpl;

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
        MessageSource input = new MqttMessageSourceImpl("esiot/test");
        final SmartRoom room = new SmartRoomSerialImpl();
        Vertx.vertx().deployVerticle((Verticle)input);
        room.setInputDataFrom(input);
    }

}
