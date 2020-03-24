package flights.flighttracker.flights;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import flights.flighttracker.FlightTrackerApplication;
import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportRepository;

@Service
public class FlightService {

	private static final Logger log = LoggerFactory.getLogger(FlightService.class);
	
	private static final String API_URI_FLIGHTS = "http://api.aviationstack.com/v1/flights?access_key=";
	
	@Autowired
	private String getApiKey;
	
	private final AirportRepository airportRepository;

    public FlightService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Scheduled(cron = "${cronExpression}")
	@Cacheable("flights")
	public List<Flight> getAllFlights() {
		log.info("Scheduled task using cron jobs");
		List<Flight> flights=new ArrayList<>();
		List<String> iataList=getAiportIatas();
		
		for (String iata:iataList) {
			restTemplate = new RestTemplate();
			final String uriDeparting = API_URI_FLIGHTS+getApiKey+"&dep_iata="+iata;
			Flights departing = restTemplate.getForObject(uriDeparting, Flights.class);
			flights.addAll(departing.getFlights());
			
			final String uriArriving = API_URI_FLIGHTS+getApiKey+"&arr_iata="+iata;
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
	
	public List<Flight> getFlightsForAirport(String iata) {
		List<Flight> flights=new ArrayList<>();

		restTemplate = new RestTemplate();
		
		final String uriDeparting = API_URI_FLIGHTS+getApiKey+"&dep_iata="+iata;
		Flights departing = restTemplate.getForObject(uriDeparting, Flights.class);
		flights.addAll(departing.getFlights());
		
		final String uriArriving = API_URI_FLIGHTS+getApiKey+"&arr_iata="+iata;
		Flights arriving = restTemplate.getForObject(uriArriving, Flights.class);
		flights.addAll(arriving.getFlights());

		return flights;

	}
	
	public List<String> getAiportIatas() {
		List<Airport> airports = airportRepository.findAll();
		List<String> iataList = new ArrayList<>();
		for (Airport airport : airports) {
			iataList.add(airport.getIata());
		}
		return iataList;
	}
	
}
