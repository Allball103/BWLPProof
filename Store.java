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

    // How busy it is
    int busyness;

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

    public void setBusyness(int busyness) {
        this.busyness = busyness;
    }

    public void setNumCashiers(int numCashiers){ this.numCashiers = numCashiers;}

    // create the list of cashiers
    public void cashierCreator(int numCashiers){

        //if(cashiers.isEmpty()) {
        //cashiers.clear();
        //}
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

    // NEEDS FIXING (i think)
    //customer leaves lines
    public void leaveLine(Customer c){
        airportLine.remove(0);
    }

    //deletes a customer
    public void leaveStore(Customer c){
        //might need more here? IDK how java works
        c = null;
    }

    //I'm gonna be honest i have no idea what this does but it's what he gave us
    public double customerDistribution(int chance){
        double u,x;
        u = (Math.random());
        x = -chance * Math.log(1.0 - u);
        return x;

//every second has a 1/customerChance chance to spawn a customer, with chance being the average time between spawns
//        if((int) (Math.random() * customerChance) == 0){
//            //spawn customer
//            return true;
//        } else {
//            return false;
//        }
    }

    public void openStore(){}

    public void closeStore(){}
}
