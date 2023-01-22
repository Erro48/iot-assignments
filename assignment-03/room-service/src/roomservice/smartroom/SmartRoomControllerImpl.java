package roomservice.smartroom;

import java.util.Optional;

public class SmartRoomControllerImpl implements SmartRoomController {


    public SmartRoomControllerImpl() {
    }
    
    @Override
    public void rollTo(int percentage) {
        // TODO Auto-generated method stub

    }

    @Override
    public void turnLight(boolean status) {
        // TODO Auto-generated method stub
    }

    
    class SerialHandler extends Thread {
        
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
                if (this.channel.get().isMsgAvailable()) {
                    try {

                    } catch (NumberFormatException e) {
                        System.err.println("Unknown message");
                    }   
                }    
            }
            
        }
        
        public void closeChannel() {
            this.channel.ifPresent(channel -> SerialCommChannel.class.cast(channel).close());
        }

        public void sendMessage(final String message) {
            this.channel.ifPresent(channel -> channel.sendMsg(message));
        }
    }
}
