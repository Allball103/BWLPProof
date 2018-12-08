/*
Customer Class
Class Contributors: Liam, Bill
 */


// Defines the Customer class
public class Customer{

    // Constructor
    //creates customer using the exponential dist
    Customer(double currTime, double dist, double items, int custId){
        enterTime = currTime;
        //add 1 to guarantee they have at least one item
        itemsInCart = ((int) items) + 1;
        impatienceFactor = ((int)Math.random() * 10) + 1;
        finishTime = currTime + dist;
        currentEvent = EventLoop.Event.CUSTOMER_SPAWNS;
        id = custId;
    }

    //customer id
    private int id;

    //time that they entered the store
    private double enterTime;

    // Number of items customer has
    int itemsInCart;

    // Percentage chance the customer leaves
    int impatienceFactor;

    //the current event the customer has in the priority queue
    private EventLoop.Event currentEvent;

    //the time that the current event will finish
    private double finishTime;

    // register they are at for checking out
    private int registerNum;

    // Getters
    int getId() { return  id; }

    double getEnterTime() { return enterTime; }

    int getItemsInCart(){
        return itemsInCart;
    }

    int getImpatienceFactor(){
        return impatienceFactor;
    }

    double getFinishTime() { return finishTime;}

    EventLoop.Event getCurrentEvent(){return currentEvent;}

    public int getRegisterNum() { return registerNum; }

    // Setters
    public void setId(int id){ this.id = id; }

    public void setEnterTime(double currTime) {this.enterTime = currTime;}

    public void setImpatienceFactor(int impatienceFactor) {
        this.impatienceFactor = impatienceFactor;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public void setFinishTime(double finishTime) {this.finishTime = finishTime;}

    public void setCurrentEvent(EventLoop.Event newEvent) {this.currentEvent = newEvent;}

    public void setRegisterNum(int registerNum){this.registerNum = registerNum;}
}
