package assignment.view;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class MainViewImpl implements MainView {

    private static final String LABEL_TEXT = "Water level: ";
    private static final int MAX_X_VALUES = 30;
    
    private final XYChart.Series<String, Number> data;
    private final Label label;
    
    public MainViewImpl(final XYChart.Series<String, Number> data, final Label label) {
        this.data = data;
        this.label = label;
    }
    
    @Override
    public void addData(final int waterLevel) {
       this.label.setText(LABEL_TEXT + String.valueOf(waterLevel));
       
       if (this.data.getData().size() > MAX_X_VALUES) {
           this.data.getData().remove(0);
       }
       
       this.data.getData().add(new XYChart.Data<String, Number>(new SimpleDateFormat("HH:mm:ss")
               .format(new Timestamp(System.currentTimeMillis())), waterLevel));
    }

}
