package assignment;

import java.util.Optional;

import assignment.controller.MainController;
import assignment.view.MainView;
import javafx.application.Platform;

class MainControllerImpl implements MainController {

    private final MainView view;
    private SerialHandler handler;
    private final int rate;
    private boolean manualControl = false;
    
    public MainControllerImpl(final MainView view, final String port, final int rate) {
        this.view = view;
        this.rate = rate;
        this.handler = new SerialHandler(port, rate);
        this.handler.start();
    }

    @Override
    public void addWaterLevelRecord(final int waterLevel) {
        this.view.addData(-waterLevel);
    }


    @Override
    public void setPort(final String port) {
        this.handler.closeChannel();
        this.view.deleteData();
        this.handler = new SerialHandler(port, this.rate);
        this.handler.start();
    }

    @Override
    public void sendMotorAngle(final int angle) {
        if (this.manualControl) {
            //move
            this.handler.sendMessage(String.valueOf(angle));
        }
    }
    
	@Override
	public void toggleMode() {
		this.manualControl = !this.manualControl;
		this.view.setButtonText(manualControl);
	    this.handler.sendMessage(String.valueOf(this.manualControl));
	}

    @Override
    public void handleError(final String message) {
    	if (message.startsWith("[01]")) {
    		this.view.setButtonText(false);
    		this.view.showError(message.substring(4));
    		this.manualControl = false;
    		return;
    	}
    	
    	this.view.showError(message);
    }
    
    class SerialHandler extends Thread {
        
        private Optional<CommChannel> channel = Optional.empty();

        public SerialHandler(final String port, final int rate) {
            try {
                this.channel = Optional.of(new SerialCommChannel(port, rate));
            } catch (Exception e) {
                Platform.runLater(() -> handleError("Cannot create a CommChannel at port: " + port));
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
                        System.out.println(message);
                        if (message.startsWith("*"))
                        	Platform.runLater(() -> handleError(message.substring(1)));
                        else
                        	Platform.runLater(() -> { 
	                        		try {
	                        			addWaterLevelRecord(Integer.parseInt(message));
	                            			
	                        		} catch(NumberFormatException e) {
	                        			
	                        		}
                        		});
                    } catch (InterruptedException | NumberFormatException e) {
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