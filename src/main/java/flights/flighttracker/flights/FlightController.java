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

	@GetMapping("/flights")
	public List<Flight> getFlights(@RequestParam(defaultValue = "7fd294be1d151dce29930f2c8c85abef") String apiKey) {
		log.info("List all flights request received");

		final String uri = "http://api.aviationstack.com/v1/flights?access_key=" + apiKey;
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		List<Flight> receivedFlights = response.getFlights();
		return receivedFlights;

	}
	
	@GetMapping("/flights/{dept_iata}")
	public List<Flight> getFlights(@RequestParam(defaultValue = "7fd294be1d151dce29930f2c8c85abef") String apiKey, @PathVariable String dept_iata) {
		log.info("List all flights request received");

		final String uri = "http://api.aviationstack.com/v1/flights?access_key=" + apiKey +"&dept_iata="+dept_iata;
		RestTemplate restTemplate = new RestTemplate();

		Flights response = restTemplate.getForObject(uri, Flights.class);
		List<Flight> receivedFlights = response.getFlights();
		return receivedFlights;

	}

	@PostMapping("/flights/save")
	public List<Flight> saveFlights() {
		List<Flight> receivedFlights = flightService.getAllFlights();

		List<Airport> airports = airportRepository.findAll();
		List<String> iataList = new ArrayList<>();
		for (Airport airport : airports) {
			iataList.add(airport.getIata());
		}

		List<Flight> savedFlights = new ArrayList<>();
		for (Flight flight : receivedFlights) {
			Flight existingFlight = flightRepository.findByFlightDateAndFlightNumberAndAirlineNameAndDepartureAirport(
					flight.getFlightDate(), flight.getFlightNumber(), flight.getAirlineName(),
					flight.getDepartureAirport());
			if (existingFlight == null) {
				if (iataList.contains(flight.getDepartureAirportIata())
						|| iataList.contains(flight.getArrivalAirportIata())) {
					Flight savedFlight = flightRepository.save(flight);
					savedFlights.add(savedFlight);
				}
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
		return savedFlights;

	}

	@GetMapping("/savedFlights")
	public List<Flight> getSavedFlights() {
		log.info("List saved flights request received");

		List<Flight> flights = flightRepository.findAll();

		return flights;
	}

}
