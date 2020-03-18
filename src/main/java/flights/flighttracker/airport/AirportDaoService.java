package flights.flighttracker.airport;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * DAO service for Airport.
 * @author Andrejs Zmakins
 */
@Component
public class AirportDaoService {

    // TODO: Work with the real database!
    private static List<Airport> airportList = new LinkedList<Airport>(){{
        add(new Airport(1, "Schiphol", "AMS"));
    }};

    private static int airportCount = 1;


    public Airport save(Airport airport) {

        if (airport.getId() == null) {
            airport.setId(++airportCount);
        }

        airportList.add(airport);

        return airport;
    }


    public List<Airport> findAll() {
        return Collections.unmodifiableList(airportList);
    }


    public Airport findById(int id) {
        for (Airport airport : airportList) {
            if (airport.getId() == id) {
                return airport;
            }
        }

        return null;
    }
}
