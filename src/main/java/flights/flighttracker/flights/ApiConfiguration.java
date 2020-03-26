package flights.flighttracker.flights;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

	@Value("${apiKey}")
	private String apiKey;

	@Bean
	public String getApiKey() {
		return apiKey;
	}
}