import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
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
    Integer runTime;

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

        Label labelDivider = new Label("---------------------------------------------------");


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Button buttonStart = new Button("Start Simulation");

        HBox hbox1 = new HBox(labelCash, comboBoxCash);
        HBox hbox2 = new HBox(labelArrive, comboBoxArrive);
        HBox hbox3 = new HBox(labelItems, comboBoxItems);
        HBox hbox4 = new HBox(labelTime, comboBoxTime);
        HBox hbox5 = new HBox(labelDivider);
        HBox hbox6 = new HBox(buttonStart);

        hbox1.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);
        hbox3.setAlignment(Pos.CENTER);
        hbox4.setAlignment(Pos.CENTER);
        hbox5.setAlignment(Pos.CENTER);
        hbox6.setAlignment(Pos.CENTER);

        buttonStart.setOnAction(actionEvent ->  {

            int arrivalNumber = (int)comboBoxArrive.getValue();
            int itemsNumber = (int)comboBoxItems.getValue();
            int cashierNum = (int)comboBoxCash.getValue();

            int runTime = (int)comboBoxTime.getValue();


            System.out.println("Number of Cashiers: " + cashierNum);
            System.out.println("Mean Customer Arrival Interval: " + arrivalNumber);
            System.out.println("Average Number of Items: " +  itemsNumber);
            System.out.println("Run time: " + runTime);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Start Simulation Run");
            alert.setHeaderText("We will run the store simulation with the following settings...");
            alert.setContentText("Number of Cashiers: " + Integer.toString(cashierNum) +
                    "\n Arrival Interval: " + Integer.toString(arrivalNumber) +
                    "\n Average Items: " + Integer.toString(itemsNumber) +
                    "\n Simulation Run Time: " + Integer.toString(runTime));


            alert.showAndWait();

            primaryStage.close();

            primaryStage.setTitle("Running Simulation...");
            Label runningLabel = new Label("Running Simulation...");
            HBox hboxRun = new HBox(runningLabel);

            hboxRun.setAlignment(Pos.CENTER);

            VBox vboxRun = new VBox(hboxRun);
            Scene sceneRun = new Scene(vboxRun, 300, 300);

            primaryStage.setScene(sceneRun);

            primaryStage.show();

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

            double doubleDist = store.customerDistribution(arrivalNumber);
            double items = store.customerDistribution(itemsNumber);
            //casting is a problem here; need to somehow remove decimal points without rounding or something?
            //int dist = (int) doubleDist;

            int custIdCounter = 1;
            Customer cust = new Customer(CurrentTime, doubleDist, items, custIdCounter);
            pQueue.add(cust);

            // RUN SIMULATION CODE HERE //

            long simulationStartTime = System.currentTimeMillis();
            long simulationEndTime = simulationStartTime + (runTime * 60000);
            System.out.println("Simulation Start Time: 0");

            int customersProcessed = 0;

            while(System.currentTimeMillis() < simulationEndTime){

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
                            if(lowest < (20- c.impatienceFactor) + CurrentTime) {
                                c.setCurrentEvent(Event.CUSTOMER_READY_FOR_CHECKOUT);
                                c.setFinishTime(lowest);
                                store.getCashiers()[cNum].setTimeAvailable(lowest + store.getCashiers()[cNum].checkout(c));
                                store.joinLine(c);
                                pQueue.add(c);
                                System.out.println("Customer " + c.getId() + " ready for checkout 2");
                            } else {
                                c.setCurrentEvent(Event.CUSTOMER_ABANDONS_LINE);
                                c.setFinishTime((20 - c.impatienceFactor) + CurrentTime);
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
                        customersProcessed += 1;
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
                            System.out.println("CUSTOMER SHOULD NEVER BE HERE " + c.getId());
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
                    //Customer abandons line due to impatience and leaves the store.
                    //Currently does nothing....
                    } else if (pQueue.peek().getCurrentEvent() == Event.CUSTOMER_ABANDONS_LINE) {
                        CurrentTime = pQueue.peek().getFinishTime();
                        Customer c = pQueue.poll();
                        System.out.println("Customer " + c.getId() +  " peaces out");

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

            try {
                Writer fileWriter = new FileWriter("results.txt", true);
                fileWriter.write("\n" + Integer.toString(cashierNum) + "               " +
                        Integer.toString(arrivalNumber) + "                   " + Integer.toString(itemsNumber)
                        + "               " + Integer.toString(runTime) + "                   " + Integer.toString(customersProcessed));

                fileWriter.close();
            }

            catch(IOException e) {
                e.printStackTrace();
            }
            System.out.println("Customers processed: " + customersProcessed);

            primaryStage.close();

            primaryStage.setTitle("Simulation Complete");
            Label finishedLabel = new Label("Simulation Finished...");
            Label processedLabel = new Label("Processed: " + Integer.toString(customersProcessed) + " customers");

            HBox finishedHbox = new HBox(finishedLabel);
            HBox processedHbox = new HBox(processedLabel);

            finishedHbox.setAlignment(Pos.CENTER);
            processedHbox.setAlignment(Pos.CENTER);

            VBox vbox2 = new VBox(finishedHbox, processedHbox);

            Scene scene2 = new Scene(vbox2, 300, 300);

            primaryStage.setScene(scene2);
            primaryStage.show();
        });

        // This is displayed when program starts. Just the standard settings menu

        VBox vbox = new VBox(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6);

        Scene scene = new Scene(vbox, 300, 300);

        primaryStage.setScene(scene);
        primaryStage.show();



    }



    public static void main(String[] args) { launch(args); }
}