package roomservice;


public class Launcher {
    /**
     *  1. Receive MQTT messages with raw data
     *  2. Parse data to values
     *  3. Modify smart room state
     *  4. Send values to arduino to control
     *  5. Provide data with http to the dashboard
     */
    public static void main(String[] args) {
        MessageSource input = new MqttMessageSourceImpl("esiot/test");
        final SmartRoom room = new SmartRoomImpl();
        room.setInputDataFrom(input);
        input.start();
    }

}
