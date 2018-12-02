// Defines the Customer class
public class Customer{

    // Constructor
    // Sets the itemsinCart to 0, gives them a random impatience factor,
    Customer(double currTime){
        enterTime = currTime;
        itemsInCart = (int)Math.random() * 100;
        impatienceFactor = (int)Math.random() * 10;
        finishTime = currTime + 5;
        currentEvent = EventLoop.Event.CUSTOMER_SPAWNS;
    }

    //creates customer using the exponential dist
    Customer(double currTime, double dist, double items){
        enterTime = currTime;
        itemsInCart = (int) items;
        impatienceFactor = (int)Math.random() * 10;
        finishTime = currTime + dist;
        currentEvent = EventLoop.Event.CUSTOMER_SPAWNS;
    }

    public Customer(int itemsInCart, int impatienceFactor, EventLoop.Event currentEvent) {
        this.itemsInCart = itemsInCart;
        this.impatienceFactor = impatienceFactor;
        this.currentEvent = currentEvent;
    }

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

    //increments items in cart
    //stretch goal: add type of item to cart
    public void putItemsInCart(int numItems /*(also has type of item as a param for stretch goal)*/){
        setItemsInCart(getItemsInCart() +1);
    }



    //leaveStore() and joinLine() moved to Store.java
}
