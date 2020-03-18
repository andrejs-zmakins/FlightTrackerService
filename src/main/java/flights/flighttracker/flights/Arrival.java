package flights.flighttracker.flights;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Arrival {
	String airport;
	String timezone;
	String iata;
	String icao;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	Date actual;

	public Arrival() {

	}

	public String getAirport() {
		return airport;
	}

	public void setAirport(String airport) {
		this.airport = airport;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimeZone(String timezone) {
		this.timezone = timezone;
	}

	public String getIata() {
		return iata;
	}

	public void setIata(String iata) {
		this.iata = iata;
	}

	public String getIcao() {
		return icao;
	}

	public void setIcao(String icao) {
		this.icao = icao;
	}

	public Date getActual() {
		return actual;
	}

	public void setActual(Date actual) {
		this.actual = actual;
	}

	@Override
	public String toString() {
		return "Arrival [airport=" + airport + "timezone=" + timezone + ", iata=" + iata + ", icao=" + icao
				+ ", actual=" + actual + "]";
	}

}
