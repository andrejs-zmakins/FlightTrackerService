package flights.flighttracker.flights;

import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import flights.flighttracker.airport.Airport;

@RestController
public class FlightController {
	private final static Pattern IATA_CODE_PATTERN = Pattern.compile("[A-Z]{3}");

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/flights")
	public List<Flight> allFlights() {
		log.info("List all flights request received");

		final String uri = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef";
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		List<Flight> flights = response.getFlights();

		return flights;
	}
	
	@GetMapping("/landed-flights")
	public List<Flight> listLandededFlights() {
		log.info("List landed flights request received");

		final String uri = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef&flight_status=landed";
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		List<Flight> flights = response.getFlights();

		return flights;
	}

}
