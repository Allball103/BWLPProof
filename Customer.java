// Defines the Customer class
public class Customer{
    // Number of items customer has
    int itemsInCart;

    // Percentage chance the customer leaves
    int impatienceFactor;

    // Getters
    int getItemsInCart(){
        return itemsInCart;
    }

    int getImpatienceFactor(){
        return impatienceFactor;
    }

    // Setters
    public void setImpatienceFactor(int impatienceFactor) {
        this.impatienceFactor = impatienceFactor;
    }

    public void setItemsInCart(int itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    //increments items in cart
    //stretch goal: add type of item to cart
    public void putItemsInCart(int numItems /*(also has type of item as a param for stretch goal)*/){
        setItemsInCart(getItemsInCart() +1);
    }

    //leaveStore() and joinLine() moved to Store.java
}
