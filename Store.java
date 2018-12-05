import java.util.ArrayList;

// Defines the Store class
public class Store{

    // The cashiers available
    //ArrayList<Cashier> cashiers;
    private Cashier cashiers[] = new Cashier[3];

    // Customers checking out
    private Customer checkingOut[] = new Customer[3];

    // Single line that goes to others
    private ArrayList<Customer> airportLine = new ArrayList<Customer>();

    // Number of Cashiers
    private int numCashiers;

    // Getters
    Cashier[] getCashiers() { return cashiers; }

    Customer[] getCheckingOut() { return checkingOut; }

    ArrayList<Customer> getAirportLine(){
        return airportLine;
    }

    int getNumCashiers(){ return numCashiers; }

    // Setters
    public void setAirportLine(ArrayList<Customer> airportLine) {
        this.airportLine = airportLine;
    }

    public void setCheckingOut(Customer[] checkingOut) { this.checkingOut = checkingOut; }

    public void setCashiers(Cashier[] cashiers) { this.cashiers = cashiers; }

    public void setNumCashiers(int numCashiers){ this.numCashiers = numCashiers;}

    // create the list of cashiers
    public void cashierCreator(int numCashiers){
        for(int i = 0; i < numCashiers; i++){
            Cashier c = new Cashier();
            cashiers[i] = c;
        }
    }

    //customer joins the airport line
    public void joinLine(Customer c){
        //choose which line customer c joins
        airportLine.add(c);
    }

    //customer leaves lines
    public void leaveLine(){
        airportLine.remove(0);
    }

    //I'm gonna be honest i have no idea what this does but it's what he gave us
    public double customerDistribution(int chance){
        double u,x;
        u = (Math.random());
        x = -chance * Math.log(1.0 - u);
        return x;
    }
}
