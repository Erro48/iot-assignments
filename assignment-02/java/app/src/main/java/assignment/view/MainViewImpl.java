package assignment.view;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MainViewImpl implements MainView {

    private static final String LABEL_TEXT = "Water level: ";
    private static final String BUTTON_TAKE_TEXT = "Take control";
    private static final String BUTTON_REVOKE_TEXT = "Revoke control";
    private static final int MAX_X_VALUES = 30;
    
    private static final int WL1 = 1500;
    private static final int WL2 = 800;
    private static final int WL_MAX = 500;
    
    
    private final XYChart.Series<String, Number> data;
    private final Label label;
    private final Button button;
    private final Circle alarmLed;
    
    public MainViewImpl(final XYChart.Series<String, Number> data, final Label label, final Button button,
    					final Circle alarmLed) {
        this.data = data;
        this.label = label;
        this.button = button;
        this.alarmLed = alarmLed;
    }
    
    @Override
    public void addData(final int waterLevel) {
       this.label.setText(LABEL_TEXT + String.valueOf(-waterLevel));
       
       this.handleAlarm(-waterLevel);
       
       this.alarmLed.setStyle(BUTTON_REVOKE_TEXT);
       
       if (this.data.getData().size() > MAX_X_VALUES) {
           this.data.getData().remove(0);
       }
       
       this.data.getData().add(new XYChart.Data<String, Number>(new SimpleDateFormat("HH:mm:ss")
               .format(new Timestamp(System.currentTimeMillis())), waterLevel));
    }

    @Override
    public void deleteData() {
        this.data.getData().clear();
    }

	@Override
	public void showError(final String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.CLOSE);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.CLOSE) {
		    alert.close();
		}
	}
	
	@Override
	public void setButtonText(boolean manual) {
		if (manual)
			this.button.setText(BUTTON_REVOKE_TEXT);
		else this.button.setText(BUTTON_TAKE_TEXT);	
	}
	
	private void handleAlarm(final int waterValue) {
		if (waterValue > WL1)
			alarmLed.setFill(Color.GREEN);
		else if (waterValue < WL1 && waterValue > WL2)
			alarmLed.setFill(Color.ORANGE);
		else if (waterValue < WL2 && waterValue > WL_MAX)
			alarmLed.setFill(Color.RED);
	}

}
