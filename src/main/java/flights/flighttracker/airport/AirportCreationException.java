package flights.flighttracker.airport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for airport creation errors.
 * @author Andrejs Zmakins
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AirportCreationException extends RuntimeException {
    public AirportCreationException(String message) {
        super(message);
    }
}
