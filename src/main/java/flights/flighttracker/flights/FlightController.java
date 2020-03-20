package flights.flighttracker.flights;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportRepository;

@RestController
public class FlightController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private AirportRepository airportRepository;

	@Autowired
	private FlightService flightService;
	
	private static final String API_URI = "http://api.aviationstack.com/v1/flights?access_key=";



	@GetMapping("/flights")
	public List<Flight> getFlights(@RequestParam(defaultValue = "7fd294be1d151dce29930f2c8c85abef") String apiKey) {
		log.info("List all flights request received.");

		final String uri = API_URI + apiKey;
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		
		return response.getFlights();

	}

	@GetMapping("/flights/{iata}")
	public List<Flight> getFlightsForAirport(
			@RequestParam(defaultValue = "7fd294be1d151dce29930f2c8c85abef") String apiKey, @PathVariable String iata) {
		log.info("List flights related to specific airport request received.");
		List<Flight> flights = new ArrayList<>();

		RestTemplate restTemplate = new RestTemplate();

		final String uriDeparting = API_URI + apiKey + "&dep_iata="
				+ iata;
		Flights departingFlights = restTemplate.getForObject(uriDeparting, Flights.class);
		flights.addAll(departingFlights.getFlights());

		final String uriArriving =  API_URI + apiKey + "&arr_iata=";
		Flights arrivingFlights = restTemplate.getForObject(uriArriving, Flights.class);
		flights.addAll(arrivingFlights.getFlights());

		return flights;

	}

	@PostMapping("/flights/save")
	public List<Flight> saveFlights() {
		log.info("Save flights post request received.");
		List<Airport> airports = airportRepository.findAll();
		List<String> iataList = new ArrayList<>();
		for (Airport airport : airports) {
			iataList.add(airport.getIata());
		}

		List<Flight> receivedFlights = flightService.getAllFlights(iataList);

		List<Flight> savedFlights = new ArrayList<>();
		for (Flight flight : receivedFlights) {
			Flight existingFlight = flightRepository.findByFlightDateAndFlightNumberAndAirlineNameAndDepartureAirport(
					flight.getFlightDate(), flight.getFlightNumber(), flight.getAirlineName(),
					flight.getDepartureAirport());
			if (existingFlight == null) {
				Flight savedFlight = flightRepository.save(flight);
				savedFlights.add(savedFlight);
			} else {
				existingFlight.setStatus(flight.getStatus());
				existingFlight.setDepartureActualTime(flight.getDepartureActualTime());
				existingFlight.setArrivalActualTime(flight.getArrivalAirport());
				existingFlight.setArrivalAirportIata(flight.getArrivalAirportIata());
				existingFlight.setArrivalActualTime(flight.getArrivalActualTime());
				flightRepository.save(existingFlight);
				savedFlights.add(existingFlight);
			}
		}
		StringBuilder messageBuilder= new StringBuilder();
		messageBuilder.append("Number of flights saved is: ");
		if (savedFlights.isEmpty()) {
			log.error("There are no flights saved.");
		}else {
			messageBuilder.append(savedFlights.size());
			String message= messageBuilder.toString();
			log.info(message);
		}
		return savedFlights;

	}

	@GetMapping("/savedFlights")
	public List<Flight> getSavedFlights() {
		log.info("List saved flights request received.");
		return flightRepository.findAll();
	}

}
