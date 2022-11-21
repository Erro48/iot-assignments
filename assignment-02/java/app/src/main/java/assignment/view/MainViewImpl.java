package assignment.view;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class MainViewImpl implements MainView {

    private static final String LABEL_TEXT = "Water level: ";
    private static final String BUTTON_TAKE_TEXT = "Take control";
    private static final String BUTTON_REVOKE_TEXT = "Revoke control";
    private static final int MAX_X_VALUES = 30;
    
    private final XYChart.Series<String, Number> data;
    private final Label label;
    private final Button button;
    
    public MainViewImpl(final XYChart.Series<String, Number> data, final Label label, final Button button) {
        this.data = data;
        this.label = label;
        this.button = button;
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

}
