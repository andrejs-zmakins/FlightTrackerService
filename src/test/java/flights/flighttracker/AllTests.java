package flights.flighttracker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
		AllAirportControllerTests.class, 
		FlightControllerTest.class, 
		FlightTrackerApplicationTests.class,
		FlightServiceTest.class })
public class AllTests {

}
