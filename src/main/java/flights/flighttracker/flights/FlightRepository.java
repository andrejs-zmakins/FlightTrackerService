package flights.flighttracker.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FlightRepository extends JpaRepository<Flight, Integer> {
	
	Flight findByFlightDateAndFlightNumberAndAirlineNameAndDepartureAirport(Date flight_date, String flight_number, String airline, String departure_airport);

}
