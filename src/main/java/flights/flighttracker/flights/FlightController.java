package flights.flighttracker.flights;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "FlightController")
@RestController
public class FlightController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private final FlightRepository flightRepository;

	private final FlightService flightService;

	@Autowired
	public FlightController(FlightRepository flightRepository, FlightService flightService) {
		this.flightRepository = flightRepository;
		this.flightService = flightService;
	}

	@ApiOperation(value = "View a list of flights retrieved from http://api.aviationstack.com/v1/flights API, for airports stored in the database, if those are departure or arrival airports for the flights", response=Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 401, message = "Unauthorized|Must provide valid API key.") })
	@GetMapping("/flights")
	public ResponseEntity<Object> getFlights() {
		log.info("List all flights request received.");
		List<Flight> receivedFlights = flightService.getAllFlights();
		if (receivedFlights.isEmpty()) {
			log.error("There are no flights received. Check, if you have correct API key.");
			return new ResponseEntity<>("No flights received. Check, if you have corret API key.",
					HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<>(receivedFlights, HttpStatus.OK);

	}

	@ApiOperation(value = "View a list of flights retrieved from http://api.aviationstack.com/v1/flights API, where specified aiport IATA is in flight's departure or arrival aiport", response=Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success|OK"),
			@ApiResponse(code = 400, message = "Bad Request|Check API key or IATA code for the airport.") })
	@GetMapping("/flights/{iata}")
	public ResponseEntity<Object> getFlightsForAirport(@PathVariable String iata) {

		log.info("List flights related to specific airport request received.");
		List<Flight> receivedFlights = flightService.getFlightsForAirport(iata);
		if (receivedFlights.isEmpty()) {
			log.error(
					"There are no flights received. Check, if you have correct API key or have provided valid IATA code");
			return new ResponseEntity<>(
					"No flights received. Check, if you have corret API key or have provided valid IATA code.",
					HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(receivedFlights, HttpStatus.OK);

	}

	@ApiOperation(value = "Save a list of flights retrieved from http://api.aviationstack.com/v1/flights API, for airports stored in the database, if those are departure or arrival airports for the flights", response=Iterable.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Success|Created/Updated flights"),
			@ApiResponse(code = 401, message = "Unauthorized|Must provide valid API key.") })
	@PostMapping("/flights/save")
	public ResponseEntity<Object> saveFlights() {
		log.info("Save flights post request received.");

		List<Flight> savedFlights = new ArrayList<>();

		List<Flight> receivedFlights = flightService.getAllFlights();
		if (receivedFlights.isEmpty()) {
			log.error("There are no flights received. Check, if you have correct API key.");
			return new ResponseEntity<>("No flights received. Check, if you have corret API key.",
					HttpStatus.UNAUTHORIZED);
		}

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
