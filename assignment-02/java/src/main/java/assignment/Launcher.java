/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package assignment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Sample JavaFX application with GUI declared in FXML file.
 */
public class Launcher extends Application {

    @Override
    public final void start(final Stage primaryStage) throws Exception {
        final Scene root = (Scene)FXMLLoader.load(getClass().getResource("/MainGui.fxml"));
        
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest((e) -> { 
           Platform.exit();
           System.exit(0);       
        });
        
        primaryStage.setTitle("Water level App");
        primaryStage.setScene(root);
        primaryStage.show();
    }

}
