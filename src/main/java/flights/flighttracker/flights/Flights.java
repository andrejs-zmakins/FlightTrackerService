package flights.flighttracker.flights;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flights {

	@JsonProperty("data")
	List<Flight> data;

	protected Flights() {
		//do nothing because default constructor required by JPA
	}

	public List<Flight> getFlights() {
		return data;
	}


}
