package flights.flighttracker;

import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportRepository;
import flights.flighttracker.flights.FlightService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Flight Service test.
 * @author Andrejs Zmakins
 */

public class FlightServiceTest {

    @Mock
    private AirportRepository airportRepository;


    private FlightService flightService;


    @Before
    public void setup() throws IOException {
        List<Airport> airportList = new LinkedList<Airport>(){{
            add(new Airport(1, "Heathrow", "LHR"));
        }};

        AtomicInteger idCount = new AtomicInteger(1);

        MockitoAnnotations.initMocks(this);

        Mockito.when(airportRepository.findAll()).thenReturn(airportList);

        Mockito.when(airportRepository.findById(Mockito.anyInt())).thenAnswer(
                invocation -> {
                    int id = invocation.getArgument(0, Integer.class);
                    return airportList.stream().filter(a -> a.getId() == id).findAny().orElse(null);
                }
        );
        Mockito.when(airportRepository.save(Mockito.any(Airport.class))).thenAnswer(
                invocation -> {
                    Airport airport = invocation.getArgument(0, Airport.class);
                    airport.setId(idCount.incrementAndGet());
                    airportList.add(airport);
                    return airport;
                }
        );

        InputStream input = FlightServiceTest.class.getClassLoader().getResourceAsStream("test.properties");

        Properties props = new Properties();
        props.load(input);
        String apiKey = props.getProperty("aviationStack.apiKey");

        flightService = new FlightService(airportRepository);

        flightService.setApiKey(apiKey);
    }


    @Test
    public void testGetAllFlights() {
        Assert.isTrue(flightService.getAllFlights() != null, "Some data must be returned");
    }


    @Test
    public void testGetFlightsForAirport() {
        Assert.isTrue(flightService.getFlightsForAirport("LHR") != null,
                "Some data must be returned");
    }
}
