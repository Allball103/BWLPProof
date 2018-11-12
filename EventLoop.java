package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import java.awt.*;

public class EventLoop extends Application {

    String cashierNum;
    String arrivalInt;
    String itemsNum;


    @Override
    public void start(Stage primaryStage) throws Exception {

        // App window title
        primaryStage.setTitle("Welcome to the Grocery Store Simulator");

        // Number of Cashiers //////////////////////////////////////////////////////////////////////////////////////////
        Label labelCash = new Label("Number of Cashiers: ");

        // Number of Cashiers Dropdown
        ComboBox comboBoxCash = new ComboBox();
        comboBoxCash.getItems().add("1 Cashier");
        comboBoxCash.getItems().add("2 Cashiers");
        comboBoxCash.getItems().add("3 Cashiers");
        comboBoxCash.getSelectionModel().selectFirst();

        // Mean Customer Arrival Interval //////////////////////////////////////////////////////////////////////////////
        Label labelArrive = new Label("Mean Customer Arrival Interval: ");

        ComboBox comboBoxArrive = new ComboBox();
        for (int i = 0; i < 11; i++) {
            comboBoxArrive.getItems().add(i);
        }
        comboBoxArrive.getSelectionModel().selectFirst();

        // Average Number of Items /////////////////////////////////////////////////////////////////////////////////////
        Label labelItems = new Label("Average Number of Items: ");

        ComboBox comboBoxItems = new ComboBox();
        for (int t = 0; t < 101; t++) {
            comboBoxItems.getItems().add(t);
        }
        comboBoxItems.getSelectionModel().selectFirst();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Button buttonReset = new Button("Reset Simulation");
        Button buttonStart = new Button("Start Simulation");

        HBox hbox1 = new HBox(labelCash, comboBoxCash);
        HBox hbox2 = new HBox(labelArrive, comboBoxArrive);
        HBox hbox3 = new HBox(labelItems, comboBoxItems);
        HBox hbox4 = new HBox(buttonReset, buttonStart);

        buttonStart.setOnAction(actionEvent ->  {

            cashierNum = comboBoxCash.getValue().toString();
            arrivalInt = comboBoxArrive.getValue().toString();
            itemsNum = comboBoxItems.getValue().toString();


            System.out.println("Number of Cashiers: " + cashierNum);
            System.out.println("Mean Customer Arrival Interval: " + arrivalInt);
            System.out.println("Average Number of Items: " +  itemsNum);


            // RUN SIMULATION CODE HERE //

        });

        VBox vbox = new VBox(hbox1, hbox2, hbox3, hbox4);

        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}