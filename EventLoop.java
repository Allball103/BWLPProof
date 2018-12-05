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
        for (int i = 1; i < 4; i++) {
            comboBoxCash.getItems().add(i);
        }
//        comboBoxCash.getItems().add("1 Cashier");
//        comboBoxCash.getItems().add("2 Cashiers");
//        comboBoxCash.getItems().add("3 Cashiers");
        comboBoxCash.getSelectionModel().selectFirst();

        // Mean Customer Arrival Interval //////////////////////////////////////////////////////////////////////////////
        Label labelArrive = new Label("Mean Customer Arrival Interval: ");

        ComboBox comboBoxArrive = new ComboBox();
        for (int i = 1; i < 11; i++) {
            comboBoxArrive.getItems().add(i);
        }
        comboBoxArrive.getSelectionModel().selectFirst();

        // Average Number of Items /////////////////////////////////////////////////////////////////////////////////////
        Label labelItems = new Label("Average Number of Items: ");

        ComboBox comboBoxItems = new ComboBox();
        for (int t = 1; t < 101; t++) {
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

            primaryStage.close();

            cashierNum = comboBoxCash.getValue().toString();
            arrivalInt = comboBoxArrive.getValue().toString();
            itemsNum = comboBoxItems.getValue().toString();

            int arrivalNumber = (int)comboBoxArrive.getValue();
            int itemsNumber = (int)comboBoxItems.getValue();
            int cashierNum = (int)comboBoxCash.getValue();

            System.out.println("Number of Cashiers: " + cashierNum);
            System.out.println("Mean Customer Arrival Interval: " + arrivalInt);
            System.out.println("Average Number of Items: " +  itemsNum);

            Store store = new Store();

            //sets number of cashiers based on dropdown selection
            switch(cashierNum) {
                case 1:
                    store.setNumCashiers(1);
                    store.cashierCreator(1);
                    break;
                case 2:
                    store.setNumCashiers(2);
                    store.cashierCreator(2);
                    break;
                case 3:
                    store.setNumCashiers(3);
                    store.cashierCreator(3);
                    break;
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
                    //This event is for when the customer is going to arrive in the store.
                    //There will always be exactly one of these events in the queue, as it creates itself.
                    //After a customer arrives, creates a customer arrives in store event w/ that customr
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
                        //customer defaults to customer spawns event
                        Customer c2 = new Customer(CurrentTime, doubleDist, items);
                        pQueue.add(c2);
                        customerStartTime = CurrentTime;
                    //This is the event for the customer wandering around the store before getting in line.
                    //Creates a ready for checkout class when they get in line
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ARRIVES_IN_STORE) {
                        //Set current store time to the Finish Time of the next event
                        CurrentTime = pQueue.peek().getFinishTime();
                        //Create a new customer that's ready for checkout
                        Customer c = pQueue.poll();
                        c.setCurrentEvent(Event.CUSTOMER_READY_FOR_CHECKOUT);
                        //Set finish time based on when a cashier will be ready
                        boolean openCashier = false;
                        for (int i = 0; i < (store.getNumCashiers()); i++) {
                            if (store.getCashiers()[i].available) {
                                openCashier = true;
                            }
                        }
                        //If there's a cashier open, go right to them! Otherwise, find the closest time available.
                        if(openCashier == true) {
                            c.setFinishTime(CurrentTime);
                        } else {
                            //Need to determine when there will be an empty register.
                            //Default to the first cashier as the lowest
                            double lowest = store.getCashiers()[0].getTimeAvailable();
                            int cNum = 0;
                            //If there's multiple cashiers, find the lowest time
                            for (int i = 1; i < store.getNumCashiers(); i++) {
                                System.out.println(store.getNumCashiers());
                                System.out.println("hey y'all");
                                if (store.getCashiers()[i].getTimeAvailable() < lowest) {
                                    lowest = store.getCashiers()[i].getTimeAvailable();
                                    cNum = i;
                                }
                            }
                            //Set the customer's finish time to the lowest cashier availability,
                            //AND set that cashier's availability time to after that customer will be done.
                            c.setFinishTime(lowest);
                            store.getCashiers()[cNum].setTimeAvailable(CurrentTime + store.getCashiers()[cNum].checkout(c));
                        }
                        store.joinLine(c);
                        pQueue.add(c);
                        System.out.println("Customer ready for checkout");
                    //Customer is in line, awaiting a cashier.
                    //Creates either a finishes checkout event (when customer is at the cashier)
                    //or an abandons line event for when the customer gives up and leaves the store.
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_READY_FOR_CHECKOUT) {
                        //Set current store time to the Finish Time of the next event
                        CurrentTime = pQueue.peek().getFinishTime();
                        //Create a new customer that's ready to go to a cashier
                        Customer c = pQueue.poll();
                        c.setCurrentEvent(Event.CUSTOMER_FINISHES_CHECKOUT);
                        boolean notInLine = true;
                        for (int i = 0; i < store.getNumCashiers() ; i++) {
                            if (store.getCashiers()[i].available && notInLine) {
                                store.getCheckingOut()[i] = c;
                                store.getCashiers()[i].setAvailable(false);
                                store.leaveLine(c);
                                c.setRegisterNum(i);
                                notInLine = false;
                            }
                        }
                        // This is for if the customer got into the cashiers line becaus they are availible
                        if(!notInLine){
                            //Set customer finish time based on current time and the checkout cashier method
                            c.setFinishTime(CurrentTime + store.getCashiers()[c.getRegisterNum()].checkout(c));
                            //Set cashier time available to this customer finish time
                            store.getCashiers()[c.getRegisterNum()].setTimeAvailable(c.getFinishTime());
                            pQueue.add(c);
                            System.out.println("Transitioned Customer");
                        } else{ //If the cashier was not available, need a way to tell the customer to "continue to wait"
                                // could also not matter since the finish time is going to be set to something different
                                // once there is a cashier available. This might still need work.
                            c.setFinishTime(CurrentTime + 1);
                            pQueue.add(c);
                            System.out.println("Customer Still Waiting for Cashier");
                        }
                    //Customer finishes at the cashier and leaves the store.
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_FINISHES_CHECKOUT) {
                        CurrentTime = pQueue.peek().getFinishTime();
                        Customer c = pQueue.poll();
                        // should maybe add try catch
                        store.getCheckingOut()[c.getRegisterNum()] = null;
                        store.getCashiers()[c.getRegisterNum()].setAvailable(true);
                        //error handling code
                        System.out.println("Customer checked out and left store");
                    //Customer abandons line due to impatience and leaves the store.
                    //Currently does nothing....
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