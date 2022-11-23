package assignment.view;

public interface MainView {

    /**
     * Add a record to the chart
     * @param waterLevel
     */
    void addData(int waterLevel);
    
    /**
     * Resets the chart
     */
    void deleteData();
    
    /**
     * Show on the view an error
     * @param message
     *          The error message
     */
    void showError(String message);
    
    /**
     * Changes the text of the control button 
     * @param manual
     *          The mode manual/auto
     */
    void setButtonText(boolean manual);
}
