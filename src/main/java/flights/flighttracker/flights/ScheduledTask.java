package flights.flighttracker.flights;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTask {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private FlightService flightService;

	@Scheduled(cron = "${cronExpression}")
	public void updateCache() {
		log.info("Scheduled job running");
		
		try {
			flightService.getAllFlights();
		} catch (Exception e) {
			log.error("Cannot retrieve flights from external API.");
		}
		
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append("Flights in cache: ");

		if (cacheManager.getCache("flights").getNativeCache().toString().isEmpty()) {
			log.error("There are no flights in cache.");
		} else {
			messageBuilder.append(cacheManager.getCache("flights").getNativeCache().toString());
			String message = messageBuilder.toString();
			log.info(message);
		}
	}
}
