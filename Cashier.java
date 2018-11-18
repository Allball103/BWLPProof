// Defines the Cashier class
public class Cashier{

    public Cashier(){
        checkOutSpeed = (int)Math.random() * 10 + 1;
        available = true;
    }

    // How fast the cashier can check out items
    int checkOutSpeed;

    // If there is a customer at their register
    boolean available;

    // Getters
    int getCheckOutSpeed(){
        return checkOutSpeed;
    }

    boolean getAvailable(){
        return available;
    }

    // Setters
    public void setCheckOutSpeed(int checkOutSpeed) {
        this.checkOutSpeed = checkOutSpeed;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    //determines the time it will take the cashier to check the current customer out
    public int checkout(Customer c){
        //stretch goal; different items will have different check out speeds
        int time = c.itemsInCart * checkOutSpeed;
        return time;
    }

    //stretch goal
    public void scanItem(){}
}
