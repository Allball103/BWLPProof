package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import java.awt.*;

public class EventLoop extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Welcome to the Grocery Store Simulator");

        Label label = new Label("Number of Cashiers: ");
        
        ComboBox comboBox = new ComboBox();

        comboBox.getItems().add("1 Cashier");
        comboBox.getItems().add("2 Cashiers");
        comboBox.getItems().add("3 Cashiers");

        // Set default value to 1 Cashier
        comboBox.getSelectionModel().selectFirst();

        HBox hbox = new HBox(label, comboBox);

        Scene scene = new Scene(hbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}