package flights.flighttracker.flights;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTask {

	private static final Logger log = LoggerFactory.getLogger(FlightController.class);

	@Scheduled(cron = "0 0/3 * * * *")
	public void getFlights() {
		final String uri = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef&flight_status=landed&arr_iata=RIX";
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		List<Flight> flights = response.getFlights();

		log.info(flights.toString());

	}
}
