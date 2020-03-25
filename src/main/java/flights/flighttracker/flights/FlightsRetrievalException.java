package flights.flighttracker.flights;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class FlightsRetrievalException extends RuntimeException {
	public FlightsRetrievalException(String message) {
		super(message);
	}
}
