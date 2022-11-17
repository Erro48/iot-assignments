package assignment;



class MainControllerImpl implements MainController {


    public MainControllerImpl(final String port, final int rate) {

    }

    @Override
    public void setWaterLevel(int waterLevel) {
        // TODO Auto-generated method stub
        
    }


    class SerialListener extends Thread {
        
        private final CommChannel channel;

        public SerialListener(final String port, final int rate) throws Exception {
            this.channel = new SerialCommChannel(port, rate);
        }

        @Override
        public void run() {
            
        }

    }

}