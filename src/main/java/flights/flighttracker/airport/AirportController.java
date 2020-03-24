package flights.flighttracker.airport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Controller for Airport endpoint.
 * @author Andrejs Zmakins
 */
@Api(value = "AirportController", description = "REST APIs related to Airport entities")
@RestController
public class AirportController {

    private final static Pattern IATA_CODE_PATTERN = Pattern.compile("[A-Z]{3}");

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final AirportRepository airportRepository;

    public AirportController(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }


    @ApiOperation(value = "Get list of airports to observe",
        response = Iterable.class,
        tags = "AirportController")
    @ApiResponse(code = 200, message = "Success|OK")
    @GetMapping("/airports")
    public List<Airport> listAirports() {
        log.info("List Airports request received");
        return airportRepository.findAll();
    }


    @ApiOperation(value = "Get airport by ID",
        response = Airport.class,
        tags = "AirportController")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 404, message = "Airport with such ID not found!") })
    @GetMapping("/airports/{id}")
    public Airport getAirportById(@PathVariable int id) {
        log.info("Get Airport by ID request received");

        Airport airport = airportRepository.findById(id).orElse(null);

        if (airport == null)
        {
            throw new AirportNotFoundException("Airport entity with ID#" + id + " not found!");
        }

        return airport;
    }


    @ApiOperation(value = "Delete airport airport by ID", tags = "AirportController")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 404, message = "Airport with such ID not found!") })
    // Query parameter
    @DeleteMapping("/airports")
    public void deleteAirportQueryParam(@RequestParam Integer id) {
        deleteAirportById(id);
    }


    @ApiOperation(value = "Delete airport airport by ID", tags = "AirportController")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Success|OK"),
        @ApiResponse(code = 404, message = "Airport with such ID not found!")
    })
    // Path parameter
    @DeleteMapping("/airports/{id}")
    public void deleteAirportPathParam(@PathVariable Integer id) {
        deleteAirportById(id);
    }


    private void deleteAirportById(Integer id) {
        try {
            airportRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new AirportNotFoundException("Airport entity with ID#" + id + " not found!");
        }
    }


    @ApiOperation(value = "Register airport",
        response = ResponseEntity.class,
        tags = "AirportController")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Success|OK"),
        @ApiResponse(code = 400, message = "Bad Request|Incorrect data provided"),
        @ApiResponse(code = 500, message = "Internal server error")})
    @PostMapping("/airports")
    public ResponseEntity<Object> createAirport(@RequestBody Airport airport) {

        if (airport.getIata() == null || airport.getName() == null) {
            log.warn("Attempt to register an airport with incomplete data");
            throw new AirportCreationException("Necessary data not provided!");
        }

        if (!IATA_CODE_PATTERN.matcher(airport.getIata()).matches()) {
            log.warn("Attempt to register an airport with incorrect data");
            throw new AirportCreationException("IATA code format incorrect!");
        }

        if (airportRepository.findAll().stream().anyMatch(ap -> airport.getIata().equals(ap.getIata()))) {
            log.warn("Attempt to register an airport twice");
            throw new AirportCreationException("IATA code already registered!");
        }

        Airport savedAirport = airportRepository.save(airport);

        if (savedAirport == null) {
            log.error("Cannot save airport to database!");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedAirport.getId()).toUri();

            log.info("Airport ID {} created: {}, {}", savedAirport.getId(), savedAirport.getName(), savedAirport.getIata());

            return ResponseEntity.created(location).build();
        }
    }
}
