package flights.flighttracker.flights;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightService {

	private static final Logger log = LoggerFactory.getLogger(FlightService.class);

	
	public List<Flight> getAllFlights() {
		final String uri = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef";
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		List<Flight> flights = response.getFlights();

		log.info(flights.toString());
		
		return flights;

	}
	
	public List<Flight> getAllFlightsExhange() {
		final String uri = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<Flights> response = restTemplate.exchange(uri, HttpMethod.GET, null, Flights.class);
		List<Flight> flights = response.getBody().getFlights();

		log.info(flights.toString());
		
		return flights;

	}
}
