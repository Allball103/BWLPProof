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
import java.text.DecimalFormat;
import org.omg.CORBA.Current;

import java.util.PriorityQueue;
import java.awt.*;
import java.util.PriorityQueue;
import java.util.Timer;
import java.time.Clock;


public class EventLoop extends Application {

    enum Event
    {
        CUSTOMER_SPAWNS, CUSTOMER_ARRIVES_IN_STORE, CUSTOMER_READY_FOR_CHECKOUT, CUSTOMER_FINISHES_CHECKOUT, CUSTOMER_ABANDONS_LINE;
    }

    String cashierNum;
    String arrivalInt;
    String itemsNum;
    //keeps track of the current time at the store
    double CurrentTime = 0;
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
            int itemsNumber = (int)comboBoxItems.getValue();


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

            double overallStartTime = CurrentTime;
            double ustomerStartTime = CurrentTime;
            double customerCheckOutTime = CurrentTime;

            double doubleDist = store.customerDistribution(arrivalNumber);
            double items = store.customerDistribution(itemsNumber);
            //casting is a problem here; need to somehow remove decimal points without rounding or something?
            //int dist = (int) doubleDist;

            Customer cust = new Customer(CurrentTime, doubleDist, items);
            pQueue.add(cust);
            double customerStartTime = CurrentTime;
            // RUN SIMULATION CODE HERE //

            while(!stop){
                //this adds a customer to the store
                /*if(CurrentTime == (dist) ){ //System.currentTimeMillis() - customerStartTime == (arrivalNumber * 1000)){
                    Customer c = new Customer(CurrentTime); //
                    pQueue.add(c);
                    customerStartTime = CurrentTime;
                    System.out.println("Added a new Customer.");
                    doubleDist = store.customerDistribution(arrivalNumber);
                    dist = (int) doubleDist;
                }*/
                //this changes events
                if(!pQueue.isEmpty()) {
                    //if ((pQueue.peek().getFinishTime()) == System.currentTimeMillis()) {
                    if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_SPAWNS) {
                        //Set current store time to the Finish Time of the next event
                        CurrentTime = pQueue.peek().getFinishTime();
                        //Create a new customer that arrives at store
                        Customer c = pQueue.poll();
                        c.setCurrentEvent(Event.CUSTOMER_ARRIVES_IN_STORE);
                        c.setFinishTime(CurrentTime + c.getItemsInCart());
                        pQueue.add(c);
                        System.out.println("Added a new Customer.");
                        //Also must figure out when the next customer spawns.
                        doubleDist = store.customerDistribution(arrivalNumber);
                        items = store.customerDistribution(arrivalNumber);
                        //dist = (int) doubleDist;
                        Customer c2 = new Customer(CurrentTime, doubleDist, items);
                        pQueue.add(c2);
                        customerStartTime = CurrentTime;

                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ARRIVES_IN_STORE) {
                        //Set current store time to the Finish Time of the next event
                        CurrentTime = pQueue.peek().getFinishTime();
                        //Create a new customer that's ready for checkout
                        Customer c = pQueue.poll();
                        c.setCurrentEvent(Event.CUSTOMER_READY_FOR_CHECKOUT);
                        //Set finish time based on when a cashier will be ready
                        boolean openCashier = false;
                        for (int i = 0; i < store.getNumCashiers(); i++) {
                            if (store.getCashiers().get(i).available && openCashier == false) {
                                openCashier = true;
                            }
                        }
                        if(openCashier == true) {
                            c.setFinishTime(CurrentTime);
                        } else {
                            //Need to determine when there will be an empty register... NO IDEA how to do this
                            c.setFinishTime(CurrentTime + 10);
                        }
                        store.joinLine(c);
                        pQueue.add(c);
                        System.out.println("Customer ready for checkout");
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_READY_FOR_CHECKOUT) {
                        //Set current store time to the Finish Time of the next event
                        CurrentTime = pQueue.peek().getFinishTime();
                        //Create a new customer that's ready to go to a cashier
                        Customer c = pQueue.poll();
                        c.setCurrentEvent(Event.CUSTOMER_FINISHES_CHECKOUT);
                        boolean notInLine = true;
                        int cashierSpeed = 1;
                        for (int i = 0; i < store.getNumCashiers(); i++) {
                            if (store.getCashiers().get(i).available && notInLine) {
                                store.getCheckingOut().add(i, c);
                                store.getCashiers().get(i).setAvailable(false);
                                store.leaveLine(c);
                                c.setRegisterNum(i);
                                notInLine = false;
                            }
                        }
                        //Finish time = current time + items in customers cart * cashier's check out speed
                        c.setFinishTime(CurrentTime + c.getItemsInCart() * store.getCashiers().get(c.getRegisterNum()).getCheckOutSpeed());
                        pQueue.add(c);
                        System.out.println("Transitioned Customer");
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_FINISHES_CHECKOUT) {
                        CurrentTime = pQueue.peek().getFinishTime();
                        Customer c = pQueue.poll();
                        // should maybe add try catch
                        store.getCheckingOut().set(c.getRegisterNum(), null);
                        System.out.println("Customer checked out and left store");
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ABANDONS_LINE) {
                        CurrentTime = pQueue.peek().getFinishTime();
                        Customer c = pQueue.poll();

                    }
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    //System.out.println(df.format(CurrentTime));
                    //prints current time
                    System.out.println("Current Time: "+ df.format(CurrentTime));
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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