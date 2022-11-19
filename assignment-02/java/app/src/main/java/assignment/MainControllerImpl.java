package assignment;

import assignment.controller.MainController;
import assignment.view.MainView;
import javafx.application.Platform;

class MainControllerImpl implements MainController {

    private final MainView view;
    
    public MainControllerImpl(final MainView view, final String port, final int rate) {
        this.view = view;
        try {
            new SerialListener(port, rate).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addWaterLevelRecord(int waterLevel) {
        this.view.addData(waterLevel);
    }


    class SerialListener extends Thread {
        
        private final CommChannel channel;

        public SerialListener(final String port, final int rate) throws Exception {
            this.channel = new SerialCommChannel(port, rate);
            this.setDaemon(true);
        }

        @Override
        public void run() {
            while(true) {
                if (this.channel.isMsgAvailable()) {
                    try {
                        String message = this.channel.receiveMsg();
                        Platform.runLater(() -> addWaterLevelRecord(Integer.parseInt(message)));
                    } catch (InterruptedException | NumberFormatException e) {
                        e.printStackTrace();
                    }   
                }    
            }
            
        }

    }

}