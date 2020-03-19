package flights.flighttracker;

import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportController;
import flights.flighttracker.airport.AirportCreationException;
import flights.flighttracker.airport.AirportRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.Assert;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Tests for the <code>AirportController</code> class.
 * @see AirportController
 * @author Andrejs Zmakins
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(ServletUriComponentsBuilder.class)
public class AirportControllerTest {

    @Mock
    private AirportRepository airportRepository;

    @Test
    public void test() throws URISyntaxException {
        // Set up mock database
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

        // Suppress errors with servlet things
        PowerMockito.mockStatic(ServletUriComponentsBuilder.class);

        ServletUriComponentsBuilder uriComponentsBuilderMock = Mockito.mock(ServletUriComponentsBuilder.class);
        BDDMockito.given(ServletUriComponentsBuilder.fromCurrentRequest()).willReturn(uriComponentsBuilderMock);
        Mockito.when(uriComponentsBuilderMock.path(Mockito.anyString())).thenReturn(uriComponentsBuilderMock);
        UriComponents uriComponentsMock = Mockito.mock(UriComponents.class);
        Mockito.when(uriComponentsBuilderMock.buildAndExpand(Mockito.anyInt())).thenReturn(uriComponentsMock);
        Mockito.when(uriComponentsMock.toUri()).thenReturn(new URI("http://www.example.com"));


        AirportController controller = new AirportController(airportRepository);

        // Should be fine
        controller.createAirport(new Airport("Schiphol", "AMS"));

        Assert.state(airportRepository.findAll().size() == 2, "Airport not added!");

        // Four letter IATA
        try {
            controller.createAirport(new Airport("Qwerty", "QWER"));
            Assert.isTrue(false, "No exception on incorrect IATA!");
        } catch (AirportCreationException e) {
            // Should go here
        }

        // Two letter IATA
        try {
            controller.createAirport(new Airport("Qwerty", "QW"));
            Assert.isTrue(false, "No exception on incorrect IATA!");
        } catch (AirportCreationException e) {
            // Should go here
        }

        // Lowercase IATA
        try {
            controller.createAirport(new Airport("Qwerty", "qwe"));
            Assert.isTrue(false, "No exception on incorrect IATA!");
        } catch (AirportCreationException e) {
            // Should go here
        }

        // No name
        try {
            controller.createAirport(new Airport(null, "qwe"));
            Assert.isTrue(false, "No exception on null airport name!");
        } catch (AirportCreationException e) {
            // Should go here
        }

        // No IATA
        try {
            controller.createAirport(new Airport("Qwerty", null));
            Assert.isTrue(false, "No exception on IATA == null!");
        } catch (AirportCreationException e) {
            // Should go here
        }

        Assert.state(airportRepository.findAll().size() == 2,
                "None of the above should have resulted in insertion of an entry!");
    }
}
