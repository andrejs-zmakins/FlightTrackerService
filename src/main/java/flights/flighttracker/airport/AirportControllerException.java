package flights.flighttracker.airport;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for airport controller errors.
 * @author Andrejs Zmakins
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AirportControllerException extends RuntimeException {
    public AirportControllerException(String message) {
        super(message);
    }
}
