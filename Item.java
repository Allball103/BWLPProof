// Defines the Item class
public class Item{
    // Cost of item
    int cost;

    // How fast the item is checked out
    int checkOutTime;

    // How likely it is to be an impulse purchase
    int impulseValue;

    // Getters
    int getCost(){
        return cost;
    }

    int getCheckOutTime(){
        return checkOutTime;
    }

    int getImpulseValue(){
        return impulseValue;
    }

    // Setters
    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setCheckOutTime(int checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public void setImpulseValue(int impulseValue) {
        this.impulseValue = impulseValue;
    }
}
