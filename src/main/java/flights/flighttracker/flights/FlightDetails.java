package flights.flighttracker.flights;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightDetails {

	String number;
	String iata;
	String icao;

	public FlightDetails() {

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	@Override
	public String toString() {
		return "FlightDetails [number=" + number + ", iata=" + iata + ", icao=" + icao + "]";
	}

}
