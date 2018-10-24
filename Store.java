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
}
