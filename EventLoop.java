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

            long overallStartTime = System.currentTimeMillis();
            long customerStartTime = System.currentTimeMillis();
            long customerCheckOutTime = System.currentTimeMillis();

            double doubleDist = store.customerDistribution(arrivalNumber);
            //casting is a problem here; need to somehow remove decimal points without rounding or something?
            int dist = (int) doubleDist;
            // RUN SIMULATION CODE HERE //

            while(!stop){
                //this adds a customer to the store
                if(System.currentTimeMillis() - customerStartTime == (dist*1000) ){ //System.currentTimeMillis() - customerStartTime == (arrivalNumber * 1000)){
                    Customer c = new Customer(System.currentTimeMillis()); //
                    pQueue.add(c);
                    customerStartTime = System.currentTimeMillis();
                    System.out.println("Added a new Customer.");
                    doubleDist = store.customerDistribution(arrivalNumber);
                    dist = (int) doubleDist;
                }
                //this changes events
                if(!pQueue.isEmpty()) {
                    if ((pQueue.peek().getFinishTime()) == System.currentTimeMillis()) {
                        if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ARRIVES_IN_STORE) {
                            Customer c = pQueue.poll();
                            c.setCurrentEvent(Event.CUSTOMER_READY_FOR_CHECKOUT);
                            c.setFinishTime(System.currentTimeMillis() + 6000);
                            store.joinLine(c);
                            pQueue.add(c);
                            System.out.println("Customer ready for checkout");
                        } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_READY_FOR_CHECKOUT) {
                            Customer c = pQueue.poll();
                            c.setCurrentEvent(Event.CUSTOMER_FINISHES_CHECKOUT);
                            c.setFinishTime(System.currentTimeMillis() + 4000);
                            boolean notInLine = true;
                            for(int i = 0; i < store.getNumCashiers(); i++){
                                if(store.getCashiers().get(i).available && notInLine){
                                    store.getCheckingOut().add(i, c);
                                    store.getCashiers().get(i).setAvailable(false);
                                    store.leaveLine(c);
                                    c.setRegisterNum(i);
                                    notInLine = false;
                                }
                            }
                            pQueue.add(c);
                            System.out.println("Transitioned Customer");
                        } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_FINISHES_CHECKOUT) {
                            Customer c = pQueue.poll();
                            // should maybe add try catch
                            store.getCheckingOut().set(c.getRegisterNum(), null);
                            System.out.println("Customer checked out and left store");
                        } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ABANDONS_LINE) {
                            Customer c = pQueue.poll();
                            

                        }
                    }
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