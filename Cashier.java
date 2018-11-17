// Defines the Cashier class
public class Cashier{

    public Cashier(){
        checkOutSpeed = (int)Math.random() * 10;
        customerAtRegister = false;
    }

    // How fast the cashier can check out items
    int checkOutSpeed;

    // If there is a customer at their register
    boolean customerAtRegister;

    // Getters
    int getCheckOutSpeed(){
        return checkOutSpeed;
    }

    boolean getCustomerAtRegister(){
        return customerAtRegister;
    }

    // Setters
    public void setCheckOutSpeed(int checkOutSpeed) {
        this.checkOutSpeed = checkOutSpeed;
    }

    public void setCustomerAtRegister(boolean customerAtRegister) {
        this.customerAtRegister = customerAtRegister;
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
