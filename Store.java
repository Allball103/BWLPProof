import java.util.ArrayList;

// Defines the Store class
public class Store{
    // The lines the grocery store has (normal)
    ArrayList<ArrayList<Customer>> lines;

    // Single line that goes to others
    ArrayList<Customer> airportLine;

    // 15 items or fewer line
    ArrayList<Customer> fiveOrLess;

    // Time of the day
    int time;

    // How busy it is
    int busyness;

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

    ArrayList<Customer> getFiveOrLess(){
        return fiveOrLess;
    }

    int getTime(){
        return time;
    }

    int getBusyness(){
        return busyness;
    }

    // Setters
    public void setLines(ArrayList<ArrayList<Customer>> lines) {
        this.lines = lines;
    }

    public void setAirportLine(ArrayList<Customer> airportLine) {
        this.airportLine = airportLine;
    }

    public void setFiveOrLess(ArrayList<Customer> fiveOrLess) {
        this.fiveOrLess = fiveOrLess;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setBusyness(int busyness) {
        this.busyness = busyness;
    }
}
