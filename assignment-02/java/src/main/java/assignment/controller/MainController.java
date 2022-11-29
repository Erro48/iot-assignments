package assignment.controller;

public interface MainController {
    
    /**
     * Updates the currentWaterLevel
     * @param waterLevel
     */
    void addWaterLevelRecord(float waterLevel);

    /**
     * Changes the COM port
     * @param port
     *          The serial port
     */
    void setPort(String port);
    
    /** 
     * Moves the servo
     * @param angle
     *          The new angle
     */
    void sendMotorAngle(int angle);
    
    /**
     * Switch to manual/auto mode
     */
    void toggleMode();
    
    /**
     * Handle error messages sent by Arduino
     * @param message
     *          The error message with an optional code [01]
     */
    void handleError(String message);
}
