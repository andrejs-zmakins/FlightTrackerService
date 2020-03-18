package flights.flighttracker;

import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportDaoService;
import flights.flighttracker.airport.AirportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AirportDaoServiceCommandLineRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AirportDaoServiceCommandLineRunner.class);


    @Autowired
    private AirportDaoService airportDaoService;


    @Autowired
    private AirportRepository airportRepository;


    private void saveEntity(Airport airport) {
        //airportDaoService.save(airport);
        airportRepository.save(airport);
        log.info("Airport saved: {}", airport);
    }


    @Override
    public void run(String... args) throws Exception {
        saveEntity(new Airport("Heathrow", "LHR"));
    }
}
