package flights.flighttracker.flights;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flights {

	@JsonProperty("data")
	List<Flight> data;

	public Flights() {

	}

	public List<Flight> getFlights() {
		return data;
	}

	public void setFlights(List<Flight> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Response [flights=" + data + "]";
	}

}
