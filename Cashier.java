import java.util.ArrayList;

// Defines the Cashier class
public class Cashier{

    public Cashier(){
        checkOutSpeed = (int)Math.random() * 10 + 1;
        available = true;
        timeAvailable = 0;
    }

    // The people who this cashier will be checking out
    private ArrayList<Customer> futureCheckouts = new ArrayList<>();

    // How fast the cashier can check out items
    int checkOutSpeed;

    // If there is a customer at their register
    boolean available;

    // If they aren't available, this is the time that they WILL be
    private double timeAvailable;

    // Getters
    ArrayList<Customer> getFutureCheckouts() {return futureCheckouts; }

    int getCheckOutSpeed(){
        return checkOutSpeed;
    }

    boolean getAvailable(){
        return available;
    }

    double getTimeAvailable() {return timeAvailable; }

    // Setters
    public void setFutureCheckouts(ArrayList<Customer> futureCheckouts) {this.futureCheckouts = futureCheckouts; }

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

    // calculate how long their future line will take to checkout
    public int checkoutAllLine(Customer c){
        int finishTime = 0;
        for (int i = 0; i < futureCheckouts.size(); i++) {
            finishTime += futureCheckouts.get(i).itemsInCart*checkOutSpeed;
        }
        return finishTime;
    }

}
