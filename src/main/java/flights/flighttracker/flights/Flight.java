package flights.flighttracker.flights;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("flight_date")
	Date flightDate;

	@JsonProperty("flight_status")
	String status;

	@JsonProperty("airline")
	Airline airline;

	@JsonProperty("arrival")
	Arrival arrival;

	@JsonProperty("departure")
	Departure departure;

	@JsonProperty("flight")
	FlightDetails flightDetails;

	public Flight() {
	}

	public Date getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(Date flightDate) {
		this.flightDate = flightDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Airline getAirline() {
		return airline;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public Arrival getArrival() {
		return arrival;
	}

	public void setArrival(Arrival arrival) {
		this.arrival = arrival;
	}

	public Departure getDeparture() {
		return departure;
	}

	public void setDeparture(Departure departure) {
		this.departure = departure;
	}

	public FlightDetails getFlightDetails() {
		return flightDetails;
	}

	public void setFlightDetails(FlightDetails flightDetails) {
		this.flightDetails = flightDetails;
	}

	@Override
	public String toString() {
		return "Flight [flightDate=" + flightDate + ", status=" + status + ", airline=" + airline + ", arrival="
				+ arrival + ", departure=" + departure + ", flightDetails=" + flightDetails + "]";
	}

}
