package flights.flighttracker.flights;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Details about the flight.")
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints=
		@UniqueConstraint(columnNames={"flight_date", "flight_number", "airline", "departure_airport"}))
@Entity(name = "Flight")
public class Flight {
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Integer id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonProperty("flight_date")
	@Column(name = "flight_date")
	private Date flightDate;

	@JsonProperty("flight_status")
	@Column(name = "flight_status")
	private String status;

	@Column(name = "flight_number")
	private String flightNumber;

	@Column(name = "airline")
	private String airlineName;

	@Column(name = "departure_airport")
	private String departureAirport;

	@Column(name = "departure_airport_iata")
	private String departureAirportIata;

	@Column(name = "departure_actual_time")
	private String departureActualTime;

	@Column(name = "arrival_airport")
	private String arrivalAirport;

	@Column(name = "arrival_airport_iata")
	private String arrivalAirportIata;

	@Column(name = "arrival_actual_time")
	private String arrivalActualTime;

	protected Flight() {
		//do nothing because default constructor needed by JPA
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getAirlineName() {
		return airlineName;
	}

	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}

	public String getDepartureAirport() {
		return departureAirport;
	}

	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}

	public String getDepartureAirportIata() {
		return departureAirportIata;
	}

	public void setDepartureAirportIata(String departureAirportIata) {
		this.departureAirportIata = departureAirportIata;
	}

	public String getDepartureActualTime() {
		return departureActualTime;
	}

	public void setDepartureActualTime(String departureActualTime) {
		this.departureActualTime = departureActualTime;
	}

	public String getArrivalAirport() {
		return arrivalAirport;
	}

	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}

	public String getArrivalAirportIata() {
		return arrivalAirportIata;
	}

	public void setArrivalAirportIata(String arrivalAirportIata) {
		this.arrivalAirportIata = arrivalAirportIata;
	}

	public String getArrivalActualTime() {
		return arrivalActualTime;
	}

	public void setArrivalActualTime(String arrivalActualTime) {
		this.arrivalActualTime = arrivalActualTime;
	}
	
	

	@JsonProperty("arrival")
	private void unpackNestedArrival(Map<String, Object> arrival) {
		this.arrivalAirport = (String) arrival.get("airport");
		this.arrivalAirportIata = (String) arrival.get("iata");
		this.arrivalActualTime = (String) arrival.get("actual");
	}

	@JsonProperty("departure")
	private void unpackNestedDeparture(Map<String, Object> departure) {
		this.departureAirport = (String) departure.get("airport");
		this.departureAirportIata = (String) departure.get("iata");
		this.departureActualTime = (String) departure.get("actual");
	}

	@JsonProperty("flight")
	private void unpackNestedFlight(Map<String, Object> flight) {
		this.flightNumber = (String) flight.get("number");
	}

	@JsonProperty("airline")
	private void unpackNestedAirline(Map<String, Object> airline) {
		this.airlineName = (String) airline.get("name");
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", flightDate=" + flightDate + ", status=" + status + ", flightNumber="
				+ flightNumber + ", airlineName=" + airlineName + ", departureAirport=" + departureAirport
				+ ", departureAirportIata=" + departureAirportIata + ", departureActualTime=" + departureActualTime
				+ ", arrivalAirport=" + arrivalAirport + ", arrivalAirportIata=" + arrivalAirportIata
				+ ", arrivalActualTime=" + arrivalActualTime + "]";
	}

}
