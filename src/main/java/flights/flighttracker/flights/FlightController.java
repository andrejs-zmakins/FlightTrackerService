package flights.flighttracker.flights;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final FlightRepository flightRepository;

	private final FlightService flightService;

	@Autowired
	private CacheManager cacheManager;

	public FlightController(FlightRepository flightRepository, FlightService flightService) {
		this.flightRepository = flightRepository;
		this.flightService = flightService;
	}

	@GetMapping("/flights")
	public List<Flight> getFlights() {
		log.info("List all flights request received.");
		return flightService.getAllFlights();

	}

	@GetMapping("/flights/{iata}")
	public List<Flight> getFlightsForAirport(@PathVariable String iata) {

		log.info("List flights related to specific airport request received.");

		return flightService.getFlightsForAirport(iata);

	}

	@PostMapping("/flights/save")
	public List<Flight> saveFlights() {
		log.info("Save flights post request received.");
		List<Flight> receivedFlights = flightService.getAllFlights();

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
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Number of flights saved is: ");
		if (savedFlights.isEmpty()) {
			log.error("There are no flights saved.");
		} else {
			messageBuilder.append(savedFlights.size());
			String message = messageBuilder.toString();
			log.info(message);
		}
		return savedFlights;

	}

	@GetMapping("/savedFlights")
	public List<Flight> getSavedFlights() {
		log.info("List saved flights request received.");
		return flightRepository.findAll();
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

}
