package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class EventLoop extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("HBox Experiment 1");

        TextField textField = new TextField();

        HBox hbox = new HBox(textField);

        Scene scene = new Scene(hbox, 200, 100);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}