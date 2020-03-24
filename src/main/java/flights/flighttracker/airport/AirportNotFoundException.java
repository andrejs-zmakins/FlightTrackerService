package flights.flighttracker.airport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Airport not found exception.
 * @author Andrejs Zmakins
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AirportNotFoundException extends RuntimeException {
    public AirportNotFoundException(String message) {
        super(message);
    }
}
