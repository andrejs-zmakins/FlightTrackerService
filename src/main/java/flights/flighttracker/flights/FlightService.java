package flights.flighttracker.flights;

import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {

	private static final Logger log = LoggerFactory.getLogger(FlightService.class);

	private static final String API_URI_FLIGHTS = "http://api.aviationstack.com/v1/flights?access_key=";

	private String apiKey;

	private final AirportRepository airportRepository;

	@Autowired
	public FlightService(AirportRepository airportRepository) {
		this.airportRepository = airportRepository;
	}

	@Cacheable("flights")
	public List<Flight> getAllFlights() {
		log.info("Retrieving data from External API");
		List<Flight> flights = new ArrayList<>();
		List<String> iataList = getAirportIatas();
		RestTemplate restTemplate = new RestTemplate();

		try {
			for (String iata : iataList) {
				final String uriDeparting = API_URI_FLIGHTS + apiKey + "&dep_iata=" + iata;
				Flights departing = restTemplate.getForObject(uriDeparting, Flights.class);
				flights.addAll(departing.getFlights());

				final String uriArriving = API_URI_FLIGHTS + apiKey + "&arr_iata=" + iata;
				Flights arriving = restTemplate.getForObject(uriArriving, Flights.class);
				flights.addAll(arriving.getFlights());
			}
		} catch (RuntimeException e) {
			throw new FlightsRetrievalException(e.getMessage());
		}
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Number of flights retrieved is: ");
		messageBuilder.append(flights.size());
		String message = messageBuilder.toString();
		log.info(message);
		
		return flights;

	}

	public List<Flight> getFlightsForAirport(String iata) {
		List<Flight> flights = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		try {
			final String uriDeparting = API_URI_FLIGHTS + apiKey + "&dep_iata=" + iata;
			Flights departing = restTemplate.getForObject(uriDeparting, Flights.class);
			flights.addAll(departing.getFlights());

			final String uriArriving = API_URI_FLIGHTS + apiKey + "&arr_iata=" + iata;
			Flights arriving = restTemplate.getForObject(uriArriving, Flights.class);
			flights.addAll(arriving.getFlights());
		} catch (RuntimeException e) {
			throw new FlightsRetrievalException(e.getMessage());
		}
		return flights;
	}

	private List<String> getAirportIatas() {
		List<Airport> airports = airportRepository.findAll();
		List<String> iataList = new ArrayList<>();
		for (Airport airport : airports) {
			iataList.add(airport.getIata());
		}
		return iataList;
	}

	@Resource(name = "getApiKey")
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
