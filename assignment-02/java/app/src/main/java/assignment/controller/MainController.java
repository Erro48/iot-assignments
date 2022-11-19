package assignment.controller;

public interface MainController {
    
    /**
     * Updates the currentWaterLevel
     * @param waterLevel
     */
    void addWaterLevelRecord(int waterLevel);

    /**
     * Changes the COM port
     * @param port
     *          The serial port
     */
    void setPort(String port);
    
    
    void sendMotorAngle(int angle);
    
    
    boolean isManual();
    
    
    void setManual(boolean value);
}
