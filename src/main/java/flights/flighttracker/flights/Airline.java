package flights.flighttracker.flights;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Airline {
	
	String name;
	String iata;
	String icao;
	
	public Airline() {
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		return "Airline [name=" + name + ", iata=" + iata + ", icao=" + icao + "]";
	}

}