package flights.flighttracker.airport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
public class AirportController {

    private final static Pattern IATA_CODE_PATTERN = Pattern.compile("[A-Z]{3}");

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private final AirportDaoService airportDaoService;


    public AirportController(AirportDaoService airportDaoService) {
        this.airportDaoService = airportDaoService;
    }


    @GetMapping("/airports")
    public List<Airport> listAirports() {
        log.info("List Airports request received");
        return airportDaoService.findAll();
    }


    @GetMapping("/airports/{id}")
    public Airport getAirportById(@PathVariable int id) {
        log.info("Get Airport by ID request received");
        return airportDaoService.findById(id);
    }


    @PostMapping("/airports")
    public ResponseEntity<Object> createAirport(@RequestBody Airport airport) {

        if (airport.getIata() == null || airport.getName() == null) {
            throw new AirportCreationException("Necessary data not provided!");
        }

        if (!IATA_CODE_PATTERN.matcher(airport.getIata()).matches()) {
            throw new AirportCreationException("IATA code format incorrect!");
        }

        Airport savedAirport = airportDaoService.save(airport);

        if (savedAirport == null) {
            // TODO: Reason for that? DB error? IATA already exists? Etc?

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
