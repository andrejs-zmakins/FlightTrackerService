package flights.flighttracker.flights;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration{

    // passing the key which you set in application.properties
    @Value("${apiKey}")
    private String apiKey;

   // getting the value from that key which you set in application.properties
    @Bean
    public String getApiKey() {
        return apiKey;
    }
}