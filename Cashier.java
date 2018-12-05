// Defines the Cashier class
public class Cashier{

    public Cashier(){
        checkOutSpeed = (int)Math.random() * 10 + 1;
        available = true;
        timeAvailable = 0;
    }

    // How fast the cashier can check out items
    int checkOutSpeed;

    // If there is a customer at their register
    boolean available;

    // If they aren't available, this is the time that they WILL be
    private double timeAvailable;

    // Getters
    int getCheckOutSpeed(){
        return checkOutSpeed;
    }

    boolean getAvailable(){
        return available;
    }

    double getTimeAvailable() {return timeAvailable; }
    // Setters
    public void setCheckOutSpeed(int checkOutSpeed) {
        this.checkOutSpeed = checkOutSpeed;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setTimeAvailable(double timeAvailable) { this.timeAvailable = timeAvailable; }
    //determines the time it will take the cashier to check the current customer out
    public int checkout(Customer c){
        //stretch goal; different items will have different check out speeds
        int time = c.itemsInCart * checkOutSpeed;
        return time;
    }
}
