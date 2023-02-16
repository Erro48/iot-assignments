package roomservice.verticles;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

public class SerialVerticle extends AbstractVerticle implements SerialPortEventListener{

    private static final int QUEUE_SIZE = 100;
    
    private SerialPort serialPort;
    private BlockingQueue<String> queue;
    private StringBuffer currentMsg = new StringBuffer("");
    
    public SerialVerticle(final String port, final int rate) throws SerialPortException {
        queue = new ArrayBlockingQueue<String>(QUEUE_SIZE);
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
        if (event.isRXCHAR()) {
            try {
                String msg = serialPort.readString(event.getEventValue());
                msg = msg.replaceAll("\r", "");
                vertx.eventBus().publish("serial.rx", msg);
                boolean goAhead = true;
                while(goAhead) {
                    String msg2 = currentMsg.toString();
                    int index = msg2.indexOf("\n");
                    if (index >= 0) {
                        queue.put(msg2.substring(0, index));
                        currentMsg = new StringBuffer("");
                        if (index + 1 < msg2.length()) {
                            vertx.eventBus().publish("serial.rx", msg2.substring(index + 1));
                        }
                    } else {
                        goAhead = false;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error in receiving string from COM-port: " + ex);
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
