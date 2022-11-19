package assignment;


import assignment.view.MainViewImpl;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class ChartGuiController {

    @FXML
    private LineChart<String, Number> chart;
    
    @FXML
    private Label wlLabel;
    
    
    @FXML
    public void initialize() {        
        //defining a series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Water level");
        
        new MainControllerImpl(new MainViewImpl(series, wlLabel), "COM3", 9600);

        chart.getData().add(series); 
    }
    

}
