package assignment;


import assignment.controller.MainController;
import assignment.view.MainViewImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import jssc.SerialPortList;

public class ChartGuiController {

    @FXML
    private LineChart<String, Number> chart;
    
    @FXML
    private Label wlLabel;
    
    @FXML
    private ComboBox<String> comSelector;
    
    @FXML
    private Slider controlSlider;
    
    @FXML
    private Button controlButton;
    
    
    private MainController controller;
    
    @FXML
    public void initialize() {        
        //defining a series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Water level");
        
        /* detect serial ports */
        String[] portNames = SerialPortList.getPortNames();
        
        if (portNames.length > 0) {
            controller = new MainControllerImpl(new MainViewImpl(series, wlLabel), portNames[0], 9600);
            comSelector.setItems(FXCollections.observableArrayList(portNames));
            comSelector.setValue(portNames[0]);
            comSelector.setOnAction(e -> controller.setPort(comSelector.getValue()));
        }

        
        controlButton.setOnAction(e -> {
            if (!controller.isManual()) {
                controller.setManual(true);
                controlButton.setText("Revoke control");
            }
            else {
                controller.setManual(false);
                controlButton.setText("Take control");    
            }
        });
        
        controlSlider.valueProperty().addListener((obsValue, oldNum, newNum) -> controller.sendMotorAngle(newNum.intValue()));
        
        chart.getData().add(series); 
    }
    

}
