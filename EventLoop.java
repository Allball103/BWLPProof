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

import java.util.PriorityQueue;
import java.awt.*;
import java.util.PriorityQueue;
import java.util.Timer;
import java.time.Clock;


public class EventLoop extends Application {

    enum Event
    {
        CUSTOMER_ARRIVES_IN_STORE, CUSTOMER_READY_FOR_CHECKOUT, CUSTOMER_FINISHES_CHECKOUT, CUSTOMER_ABANDONS_LINE;
    }

    String cashierNum;
    String arrivalInt;
    String itemsNum;
    PriorityQueue<Customer> pQueue = new PriorityQueue<Customer>((c1,c2) -> {
        if(c1.getFinishTime() < c2.getFinishTime()){
            return -1;
        } if(c1.getFinishTime() == c2.getFinishTime()) {
            return 0;
        } else {
            return 1;
        }
    }); //need to make a comparable by the time the event will finish for the given customer

        // could maybe use this code later for limiting the amount of time the system runs
//    int remainingTime = 1000;
//    long timeout = System.currentTimeMillis() + (remainingTime * 1000);
//    long currentTime = System.currentTimeMillis();
//    while ((int)currentTime > (int)timeout) {
//        Thread.sleep(1000);
//        System.out.println("You have : " + (timeout - System.currentTimeMillis()) / 1000 + " seconds left");
//    }
    boolean stop = false;
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

            int arrivalNumber = (int)comboBoxArrive.getValue();


            System.out.println("Number of Cashiers: " + cashierNum);
            System.out.println("Mean Customer Arrival Interval: " + arrivalInt);
            System.out.println("Average Number of Items: " +  itemsNum);

            Store store = new Store();

            //sets number of cashiers based on dropdown selection
            switch(comboBoxCash.getValue().toString()) {
                case "1 Cashier":
                    store.setNumCashiers(1);
                    store.cashierCreator(1);
                case "2 Cashiers":
                    store.setNumCashiers(2);
                    store.cashierCreator(2);
                case "3 Cashiers":
                    store.setNumCashiers(3);
                    store.cashierCreator(3);
            }
            long customerStartTime = System.currentTimeMillis();



            // RUN SIMULATION CODE HERE //

            while(!stop){
                if(System.currentTimeMillis() - customerStartTime == (arrivalNumber * 1000)){
                    Customer c = new Customer(); //
                    pQueue.add(c);
                    customerStartTime = System.currentTimeMillis();
                }

            }

        });

        VBox vbox = new VBox(hbox1, hbox2, hbox3, hbox4);

        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}