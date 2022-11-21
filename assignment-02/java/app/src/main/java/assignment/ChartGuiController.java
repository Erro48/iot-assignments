package assignment;


import assignment.controller.MainController;
import assignment.view.MainViewImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import jssc.SerialPortList;

public class ChartGuiController {

    private static final int MOTOR_STEP = 10;
    
    @FXML
    private LineChart<String, Number> chart;
    
    @FXML
    private NumberAxis yAxis;
    
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
        
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
        	@Override
        	public String toString(Number value) {
        		return String.valueOf(-value.intValue());	
        	}
        });      	
        
        
        /* detect serial ports */
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length > 0) {
            controller = new MainControllerImpl(new MainViewImpl(series, wlLabel, controlButton), portNames[0], 9600);
            comSelector.setItems(FXCollections.observableArrayList(portNames));
            comSelector.setValue(portNames[0]);
            comSelector.setOnAction(e -> controller.setPort(comSelector.getValue()));
        }

        controlButton.setOnAction(e -> controller.toggleMode());
        
        controlSlider.valueProperty().addListener((obsValue, oldNum, newNum) -> {
            if (newNum.intValue() % MOTOR_STEP == 0 && newNum.intValue() != oldNum.intValue()) {
                controller.sendMotorAngle(newNum.intValue());       
            }
        });
        
        chart.getData().add(series); 
    }
    

}
