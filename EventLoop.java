import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.PriorityQueue;
import java.util.Random;

public class EventLoop extends Application {

    enum Event
    {
        CUSTOMER_SPAWNS, CUSTOMER_ARRIVES_IN_STORE, CUSTOMER_READY_FOR_CHECKOUT, CUSTOMER_FINISHES_CHECKOUT, CUSTOMER_ABANDONS_LINE;
    }

    double CurrentTime = 0;

    PriorityQueue<Customer> pQueue = new PriorityQueue<Customer>((c1,c2) -> {
        if(Math.abs(c1.getFinishTime() - c2.getFinishTime()) < .0001){
            if(c1.getCurrentEvent() == Event.CUSTOMER_FINISHES_CHECKOUT){
                return 1;
            } else if(c2.getCurrentEvent() == Event.CUSTOMER_FINISHES_CHECKOUT){
                return -1;
            } else if(c1.getCurrentEvent() == Event.CUSTOMER_READY_FOR_CHECKOUT){
                return 1;
            } else if(c2.getCurrentEvent() == Event.CUSTOMER_READY_FOR_CHECKOUT){
                return -1;
            } else {
                return 0;
            }
        } else if(c1.getFinishTime() < c2.getFinishTime()) {
            return -1;
        } else {
            return 1;
        }
    }); //need to make a comparable by the time the event will finish for the given customer

    private Store store;

    int arrivalNumber;
    int itemsNumber;
    int cashierNum;
    int runTime;

    double doubleDist;
    double items;

    //casting is a problem here; need to somehow remove decimal points without rounding or something?
    //int dist = (int) doubleDist;

    int custIdCounter;

    boolean firstTime = true;

    // Flag that is switched when the user clicks START to enter the simulation while loop
    private boolean start = false;

    // start with 0 customers in store and processed in store for GUI label
    private int custProcessed = 0;
    private int custCount = 0;


    private final Label custLabel = new Label(Integer.toString(custCount) + " customers currently in store");
    private final Label airportLabel = new Label(" 0 customers currently in airport line");
    private final Label processedLabel = new Label(Integer.toString(custProcessed) + " customers processed");

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void simulationThread() {

        if(!pQueue.isEmpty() && start) {

            if (firstTime) {
                System.out.println("Arrival Int: " + arrivalNumber);
                System.out.println("Items Int: " + itemsNumber);
                System.out.println("Cashiers: " + cashierNum);
            }

            firstTime = false;

            //This event is for when the customer is going to arrive in the store.
            //There will always be exactly one of these events in the queue, as it creates itself.
            //After a customer arrives, creates a customer arrives in store event w/ that customer
            if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_SPAWNS) {
                //Set current store time to the Finish Time of the next event
                CurrentTime = pQueue.peek().getFinishTime();
                //Create a new customer that arrives at store
                Customer c = pQueue.poll();
                c.setCurrentEvent(Event.CUSTOMER_ARRIVES_IN_STORE);
                c.setFinishTime(CurrentTime + (c.getItemsInCart()));
                pQueue.add(c);
                System.out.println("Added a new Customer with ID: " + c.getId());
                custCount += 1;
                //Also must figure out when the next customer spawns.
                doubleDist = store.customerDistribution(arrivalNumber);
                items = store.customerDistribution(arrivalNumber);
                // customer id
                custIdCounter+=1;
                //customer defaults to customer spawns event
                Customer c2 = new Customer(CurrentTime, doubleDist, items, custIdCounter);
                pQueue.add(c2);
                //This is the event for the customer wandering around the store before getting in line.
                //Creates a ready for checkout class when they get in line
            } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ARRIVES_IN_STORE) {
                //Set current store time to the Finish Time of the next event
                CurrentTime = pQueue.peek().getFinishTime();
                //Create a new customer that's ready for checkout
                Customer c = pQueue.poll();
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
                    store.joinLine(c);
                    c.setCurrentEvent(Event.CUSTOMER_READY_FOR_CHECKOUT);
                    pQueue.add(c);
                    System.out.println("Customer" + c.getId() + " ready for checkout 1");
                } else {
                    //Need to determine when there will be an empty register.
                    //Default to the first cashier as the lowest
                    double lowest = store.getCashiers()[0].getTimeAvailable();
                    int cNum = 0;
                    //If there's multiple cashiers, find the lowest time
                    for (int i = 1; i < store.getNumCashiers() ; i++) {
                        System.out.println(store.getNumCashiers());
                        //System.out.println("hey y'all");
                        if (store.getCashiers()[i].getTimeAvailable() < lowest) {
                            lowest = store.getCashiers()[i].getTimeAvailable();
                            cNum = i;
                        }
                    }
                    //Set the customer's finish time to the lowest cashier availability,
                    //AND set that cashier's availability time to after that customer will be done.
                    if(lowest < (10- c.impatienceFactor) + CurrentTime) {
                        c.setCurrentEvent(Event.CUSTOMER_READY_FOR_CHECKOUT);
                        c.setFinishTime(lowest +0.01);
                        store.getCashiers()[cNum].setTimeAvailable(lowest +0.01+ store.getCashiers()[cNum].checkout(c));
                        store.joinLine(c);
                        pQueue.add(c);
                        System.out.println("Customer " + c.getId() + " ready for checkout 2");
                    } else {
                        c.setCurrentEvent(Event.CUSTOMER_ABANDONS_LINE);
                        c.setFinishTime((10 - c.impatienceFactor) + CurrentTime);
                        store.joinLine(c);
                        pQueue.add(c);
                        System.out.println("Customer " + c.getId() + " prepares to leave line");
                    }
                }

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
                        store.leaveLine();
                        c.setRegisterNum(i);
                        notInLine = false;
                    }
                }
                // This is for if the customer got into the cashiers line because they are available
                if(!notInLine){
                    //Set customer finish time based on current time and the checkout cashier method
                    c.setFinishTime(CurrentTime + store.getCashiers()[c.getRegisterNum()].checkout(c));
                    //Set cashier time available to this customer finish time
                    store.getCashiers()[c.getRegisterNum()].setTimeAvailable(c.getFinishTime());
                    pQueue.add(c);
                    System.out.println("Transitioned Customer " + c.getId());
                } else{
                    //This is the "dumb" abandon line, where the customer waits a long time before getting
                    //frustrated and leaving because it took too long
                    //pQueue.add(c);
                    System.out.println("Customer "+ c.getId()+" abandons the line and leaves the store.");
                    custCount -= 1;
                }
                //Customer finishes at the cashier and leaves the store.
            } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_FINISHES_CHECKOUT) {
                CurrentTime = pQueue.peek().getFinishTime();
                Customer c = pQueue.poll();
                // should maybe add try catch
                store.getCheckingOut()[c.getRegisterNum()] = null;
                store.getCashiers()[c.getRegisterNum()].setAvailable(true);
                //error handling code
                System.out.println("Customer " + c.getId() + " checked out and left store");
                custProcessed += 1;
                custCount -= 1;
                //Customer abandons line due to impatience and leaves the store.
                //This is the "smart" abandon line, where the customer decides they're going to leave the line
                //far in advance.
            } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ABANDONS_LINE) {
                CurrentTime = pQueue.peek().getFinishTime();
                Customer c = pQueue.poll();
                System.out.println("Customer "+ c.getId()+" abandons the line and leaves the store");
                custCount -= 1;
            }
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            //System.out.println(df.format(CurrentTime));
            //prints current time
            System.out.println("Current Time: "+ df.format(CurrentTime));

            custLabel.setText(Integer.toString(custCount) + " customers currently in store");
            airportLabel.setText(Integer.toString(store.getAirportLine().size()) + " customers currently in airport line");
            processedLabel.setText(Integer.toString(custProcessed) + " customers processed");

        }
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Welcome to the Grocery Store Simulator");

        // Number of Cashiers //////////////////////////////////////////////////////////////////////////////////////////
        Label labelCash = new Label("Number of Cashiers: ");

        // Number of Cashiers Dropdown
        ComboBox comboBoxCash = new ComboBox();
        for (int i = 1; i < 4; i++) {
            comboBoxCash.getItems().add(i);
        }

        comboBoxCash.getSelectionModel().selectFirst();

        // Mean Customer Arrival Interval //////////////////////////////////////////////////////////////////////////////
        Label labelArrive = new Label("Mean Customer Arrival Interval: ");

        ComboBox comboBoxArrive = new ComboBox();
        for (int i = 1; i < 11; i++) {
            comboBoxArrive.getItems().add(i);
        }
        comboBoxArrive.getSelectionModel().selectFirst();

        Label labelTime = new Label("Simulation Run Time (minutes): ");
        ComboBox comboBoxTime = new ComboBox();
        for (int i = 1; i < 11; i++) {
            comboBoxTime.getItems().add(i);
        }
        comboBoxTime.getSelectionModel().selectFirst();


        // Average Number of Items /////////////////////////////////////////////////////////////////////////////////////
        Label labelItems = new Label("Average Number of Items: ");

        ComboBox comboBoxItems = new ComboBox();
        for (int t = 1; t < 101; t++) {
            comboBoxItems.getItems().add(t);
        }
        comboBoxItems.getSelectionModel().selectFirst();

        Label labelDivider1 = new Label("---------------------------------------------------");
        Label labelDivider2 = new Label("---------------------------------------------------");


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Button buttonStart = new Button("Start Simulation");
        Button buttonStop = new Button("Stop Simulation / Reset");


        HBox hbox1 = new HBox(labelCash, comboBoxCash);
        HBox hbox2 = new HBox(labelArrive, comboBoxArrive);
        HBox hbox3 = new HBox(labelItems, comboBoxItems);
        HBox hbox4 = new HBox(labelTime, comboBoxTime);
        HBox hbox5 = new HBox(labelDivider1);
        HBox hbox6 = new HBox(buttonStart, buttonStop);
        HBox hbox7 = new HBox(labelDivider2);
        HBox hbox8 = new HBox(custLabel);
        HBox hbox9 = new HBox(airportLabel);
        HBox hbox10 = new HBox(processedLabel);


        hbox1.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);
        hbox3.setAlignment(Pos.CENTER);
        hbox4.setAlignment(Pos.CENTER);
        hbox5.setAlignment(Pos.CENTER);
        hbox6.setAlignment(Pos.CENTER);
        hbox7.setAlignment(Pos.CENTER);
        hbox8.setAlignment(Pos.CENTER);
        hbox9.setAlignment(Pos.CENTER);
        hbox10.setAlignment(Pos.CENTER);


        buttonStart.setOnAction(actionEvent -> {

            // Initialize our store
            store = new Store();

            //sets simulation parameters based on dropdown selections after user has clicked start
            arrivalNumber = (int)comboBoxArrive.getValue();
            itemsNumber = (int)comboBoxItems.getValue();
            cashierNum = (int)comboBoxCash.getValue();
            runTime = (int)comboBoxTime.getValue();

            // generates values from inputs to use in formula for arrival and items distributions
            doubleDist = store.customerDistribution(arrivalNumber);
            items = store.customerDistribution(itemsNumber);

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

            // sets our customer counter to start at 1
            custIdCounter = 1;

            // generate the first customer to initialize the simulation and add to pQueue
            Customer cust = new Customer(CurrentTime, doubleDist, items, custIdCounter);
            pQueue.add(cust);

            // switch flag to start running simulation now that parameters are set
            start = true;
        });

        buttonStop.setOnAction(actionEvent -> {
            start = false;
            custCount = 0;
            custProcessed = 0;
            pQueue.clear();
            store.getAirportLine().clear();

            custLabel.setText(Integer.toString(custCount) + " customers currently in store");
            airportLabel.setText(Integer.toString(store.getAirportLine().size()) + " customers currently in airport line");
            processedLabel.setText(Integer.toString(custProcessed) + " customers processed");

        });


        VBox vbox = new VBox(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, hbox7, hbox8, hbox9, hbox10);

        Scene scene = new Scene(vbox, 300, 250);


        /////////////////////////////////////////////////////////////////////

        // longrunning operation runs on different thread
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                Runnable updater = new Runnable() {

                    @Override
                    public void run() {
                        simulationThread();
                    }
                };

                ///// WHILE LOOP /////////////
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }

                    // UI update is run on the Application thread
                    Platform.runLater(updater);
                }
            }
        });

        thread.setDaemon(true);
        thread.start();

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
