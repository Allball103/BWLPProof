// Defines the Customer class
public class Customer{

    // Constructor
    // Sets the itemsinCart to 0, gives them a random impatience factor,
    Customer(){
        itemsInCart = 0;
        impatienceFactor = (int)Math.random() * 10;
        finishTime = 0;
        currentEvent = EventLoop.Event.CUSTOMER_ARRIVES_IN_STORE;
    }

    // Number of items customer has
    int itemsInCart;

    // Percentage chance the customer leaves
    int impatienceFactor;

    //the current event the customer has in the priority queue
    EventLoop.Event currentEvent;

    //the time that the current event will finish
    private int finishTime;

    // Getters
    int getItemsInCart(){
        return itemsInCart;
    }

    int getImpatienceFactor(){
        return impatienceFactor;
    }

    int getFinishTime() { return finishTime;}

    // Setters
    public void setImpatienceFactor(int impatienceFactor) {
        this.impatienceFactor = impatienceFactor;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public void setFinishTime(int finishTime) {this.finishTime = finishTime;}


    //increments items in cart
    //stretch goal: add type of item to cart
    public void putItemsInCart(int numItems /*(also has type of item as a param for stretch goal)*/){
        setItemsInCart(getItemsInCart() +1);
    }

    //leaveStore() and joinLine() moved to Store.java
}
