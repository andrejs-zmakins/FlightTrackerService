package flights.flighttracker.flights;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Integer> {

	@Query(value = "SELECT * FROM flight WHERE flight_date=:flightDate AND flight_number=:flightNumber AND airline=:airline AND departure_airport=:departureAirport", nativeQuery = true)
	Flight findByUniqueContraint(@Param("flightDate") Date flightDate, @Param("flightNumber") String flightNumber,
			@Param("airline") String airline, @Param("departureAirport") String departureAirport);

}
