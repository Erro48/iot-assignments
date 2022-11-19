package assignment;

import java.util.Optional;

import assignment.controller.MainController;
import assignment.view.MainView;
import javafx.application.Platform;

class MainControllerImpl implements MainController {

    private final MainView view;
    private SerialListener listener;
    private final int rate;
    private boolean manualControl = false;
    
    public MainControllerImpl(final MainView view, final String port, final int rate) {
        this.view = view;
        this.rate = rate;
        this.listener = new SerialListener(port, rate);
        this.listener.start();
    }

    @Override
    public void addWaterLevelRecord(final int waterLevel) {
        this.view.addData(waterLevel);
    }


    @Override
    public void setPort(final String port) {
        this.listener.closeChannel();
        this.view.deleteData();
        this.listener = new SerialListener(port, this.rate);
        this.listener.start();
    }

    @Override
    public void sendMotorAngle(final int angle) {
        if (this.manualControl) {
            //move
            this.listener.sendMessage(String.valueOf(angle));
        }
    }

    @Override
    public boolean isManual() {
        return this.manualControl;
    }

    @Override
    public void setManual(boolean value) {
        this.manualControl = value;
    }

    
    class SerialListener extends Thread {
        
        private Optional<CommChannel> channel = Optional.empty();

        public SerialListener(final String port, final int rate) {
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
                        String message = this.channel.get().receiveMsg();   
                        Platform.runLater(() -> addWaterLevelRecord(Integer.parseInt(message)));
                    } catch (InterruptedException | NumberFormatException e) {
                        System.err.println("Cannot cast the message to a number");
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