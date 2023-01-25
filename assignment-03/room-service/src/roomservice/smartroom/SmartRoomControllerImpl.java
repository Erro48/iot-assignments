package roomservice.smartroom;

public class SmartRoomControllerImpl implements SmartRoomController {

    private final CommChannel channel;

    public SmartRoomControllerImpl(final CommChannel channel) {
        this.channel = channel;
    }
    
    @Override
    public void rollTo(final int percentage) {
        System.out.println("Controller: roll to " + percentage);
        this.channel.sendMsg(String.valueOf(percentage));
    }

    @Override
    public void turnLight(final boolean status) {
        System.out.println("Controller: turn " + (status ? "on" : "off"));
        this.channel.sendMsg(String.valueOf(status));
    }

}
