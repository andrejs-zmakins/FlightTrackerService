package flights.flighttracker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AirportControllerTest.class,
        FlightControllerTest.class,
        AirportControllerEndpointTest.class,
        FlightTrackerApplicationTests.class})
public class AllTests {

}
