package flights.flighttracker.flights;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "FlightController")
@Validated
@RestController
public class FlightController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String NO_FLIGHTS_RECEIVED = "No flights received. Check, if you have corret API key.";

	private final FlightRepository flightRepository;

	private final FlightService flightService;

	public FlightController(FlightRepository flightRepository, FlightService flightService) {
		this.flightRepository = flightRepository;
		this.flightService = flightService;
	}

	@ApiOperation(value = "View a list of flights retrieved from http://api.aviationstack.com/v1/flights API, for airports stored in the database, if those are departure or arrival airports for the flights", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "Unauthorized|Must provide valid API key.") })
	@GetMapping("/flights")
	public ResponseEntity<Object> getFlights() {
		log.info("List all flights request received.");

		List<Flight> receivedFlights = flightService.getAllFlights();

		if (receivedFlights.isEmpty()) {
			return new ResponseEntity<>(NO_FLIGHTS_RECEIVED, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(receivedFlights, HttpStatus.OK);

	}

	@ApiOperation(value = "View a list of flights retrieved from http://api.aviationstack.com/v1/flights API, where specified aiport IATA is in flight's departure or arrival aiport", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 500, message = "Internal Server Error|Check API key or IATA code for the airport.") })
	@GetMapping("/flights/{iata}")
	public ResponseEntity<Object> getFlightsForAirport(@PathVariable String iata) {

		log.info("List flights related to specific airport request received.");

		List<Flight> receivedFlights = flightService.getFlightsForAirport(iata);

		if (receivedFlights.isEmpty()) {
			return new ResponseEntity<>(NO_FLIGHTS_RECEIVED, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(receivedFlights, HttpStatus.OK);

	}

	@ApiOperation(value = "Save a list of flights retrieved from http://api.aviationstack.com/v1/flights API, for airports stored in the database, if those are departure or arrival airports for the flights", response = Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Success|Created/Updated flights"),
			@ApiResponse(code = 500, message = "Internal Server Error|Check API key.") })
	@PostMapping("/flights/save")
	public ResponseEntity<Object> saveFlights() {
		log.info("Save flights post request received.");

		List<Flight> savedFlights = new ArrayList<>();
		List<Flight> receivedFlights = flightService.getAllFlights();
		Flight savedFlight;

		if (receivedFlights.isEmpty()) {
			log.error("There are no flights received. Check, if you have correct API key.");
			return new ResponseEntity<>(NO_FLIGHTS_RECEIVED, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		for (Flight flight : receivedFlights) {
			try {
				Flight existingFlight = flightRepository.findByUniqueContraint(flight.getFlightDate(),
						flight.getFlightNumber(), flight.getAirlineName(), flight.getDepartureAirport());
				if (existingFlight == null) {

					savedFlight = flightRepository.save(flight);
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
			} catch (DataIntegrityViolationException e) {
				log.error(e.getMessage());
			}
		}

		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Number of flights saved is: ");
		messageBuilder.append(savedFlights.size());
		String message = messageBuilder.toString();
		log.info(message);

		return new ResponseEntity<>(savedFlights, HttpStatus.CREATED);

	}

	@ApiOperation(value = "Get list of flights from database", response = Iterable.class)
	@ApiResponse(code = 200, message = "Success|OK")
	@GetMapping("/savedFlights")
	public List<Flight> getSavedFlights() {
		log.info("List saved flights request received.");
		return flightRepository.findAll();
	}

}
