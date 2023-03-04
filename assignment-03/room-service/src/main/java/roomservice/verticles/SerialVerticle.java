package roomservice.verticles;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialVerticle extends AbstractVerticle implements SerialPortEventListener{

    private SerialPort serialPort;
    private StringBuilder message = new StringBuilder();
    private Boolean receivingMessage = false;
    
    public SerialVerticle(final String port, final int rate) throws SerialPortException {
        serialPort = new SerialPort(port);
        serialPort.openPort();
        serialPort.setParams(rate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | 
                SerialPort.FLOWCONTROL_RTSCTS_OUT);
        serialPort.addEventListener(this);
    }
    
    @Override
    public void start() {
        final EventBus eventBus = vertx.eventBus();
        eventBus.consumer("serial.tx", message -> {
           this.sendMsg(message.body().toString()); 
        });
    }

    public void sendMsg(final String msg) {
        char[] array = (msg+"\n").toCharArray();
        byte[] bytes = new byte[array.length];

        for (int i = 0; i < array.length; i++){
            bytes[i] = (byte) array[i];
        }
        try {
            synchronized (serialPort) {
                serialPort.writeBytes(bytes);
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    @Override
    public void serialEvent(final SerialPortEvent event) {
        /* if there are bytes received in the input buffer */
        if(event.isRXCHAR() && event.getEventValue() > 0){
            try {
                final byte buffer[] = serialPort.readBytes();
                for (byte b: buffer) {
                    if (b == '>') {
                        receivingMessage = true;
                        message.setLength(0);
                    }
                    else if (receivingMessage == true) {
                        if (b == '\r') {
                            receivingMessage = false;
                            final String toProcess = message.toString();
                        	vertx.eventBus().publish("serial.rx", toProcess);
                        }
                        else {
                            message.append((char)b);
                        }
                    }
                }                
            }
            catch (SerialPortException ex) {
            	ex.printStackTrace();
            }
        }
    }
    
    @Override
    public void stop() {
        try {
            if (serialPort != null) {
                serialPort.removeEventListener();
                serialPort.closePort();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }  
    }
    
}
