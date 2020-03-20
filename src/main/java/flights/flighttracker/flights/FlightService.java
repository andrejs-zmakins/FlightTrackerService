package flights.flighttracker.flights;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FlightService {

	private static final Logger log = LoggerFactory.getLogger(FlightService.class);

	
	public List<Flight> getAllFlights(List<String> iataList) {
		List<Flight> flights=new ArrayList<>();
		for (String iata:iataList) {
			RestTemplate restTemplate = new RestTemplate();
			final String uriDeparting = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef&dep_iata="+iata;
			Flights departing = restTemplate.getForObject(uriDeparting, Flights.class);
			flights.addAll(departing.getFlights());
			
			final String uriArriving = "http://api.aviationstack.com/v1/flights?access_key=7fd294be1d151dce29930f2c8c85abef&arr_iata="+iata;
			Flights arriving = restTemplate.getForObject(uriArriving, Flights.class);
			flights.addAll(arriving.getFlights());
			
		}
		StringBuilder messageBuilder= new StringBuilder();
		messageBuilder.append("Number of flights retrieved is: ");
		if (flights.isEmpty()) {
			log.error("There are no flights");
		}else {
			messageBuilder.append(flights.size());
			String message= messageBuilder.toString();
			log.info(message);
		}
		return flights;

	}
	
}
