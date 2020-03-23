package flights.flighttracker.flights;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
@ApiModel(description="List of Flights")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flights {

	@JsonProperty("data")
	List<Flight> data;

	public Flights() {
		//do nothing because default constructor required by JPA
	}

	public List<Flight> getFlights() {
		return data;
	}

	public List<Flight> getData() {
		return data;
	}

	public void setData(List<Flight> data) {
		this.data = data;
	}

}
