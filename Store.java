import java.util.ArrayList;

// Defines the Store class
public class Store{

    // The cashiers available
    //ArrayList<Cashier> cashiers;
    private ArrayList<Cashier> cashiers = new ArrayList<Cashier>();

    // The lines the grocery store has (normal)
    ArrayList<ArrayList<Customer>> lines = new ArrayList<ArrayList<Customer>>();

    // Single line that goes to others
    public ArrayList<Customer> airportLine;

    // 15 items or fewer line
    public ArrayList<Customer> fifteenOrLess;

    // Time of the day
    int time;

    // How busy it is
    int busyness;

    // Number of Cashiers
    int numCashiers;

    // The day of the week
    enum dayOfWeek {
        Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    }

    // Getters
    ArrayList<ArrayList<Customer>> getLines(){
        return lines;
    }

    ArrayList<Customer> getAirportLine(){
        return airportLine;
    }

    ArrayList<Customer> getFifteenOrLess(){ return fifteenOrLess; }

    int getTime(){
        return time;
    }

    int getBusyness(){
        return busyness;
    }

    // Setters
    public void setLines(ArrayList<ArrayList<Customer>> lines) { this.lines = lines; }

    public void setAirportLine(ArrayList<Customer> airportLine) {
        this.airportLine = airportLine;
    }

    public void setFifteenOrLess(ArrayList<Customer> fiveOrLess) {
        this.fifteenOrLess = fiveOrLess;
    }

    public void setTime(int time) {
        this.time = time;
    }

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
            cashiers.add(c);
        }
    }

    //customer joins the airport line
    public void joinLine(Customer c){
        //choose which line customer c joins
        if(c.itemsInCart <= 15){
            fifteenOrLess.add(c);
        } else {
            airportLine.add(c);
        }
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
