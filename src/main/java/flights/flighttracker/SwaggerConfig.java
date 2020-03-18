package flights.flighttracker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Swagger config file.
 * @author Andrejs Zmakins
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final Contact DEFAULT_CONTACT = new Contact(
            "Andrejs Zmakins", "http://www.accenture.com", "example@example.com");

    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Flight Tracker API",
            "Track flights coming from specified airports.",
            "1.0",
            "urn:tos",
            DEFAULT_CONTACT,
            "Copyright, 2020", // You can do absolutely nothing with this software. ;-)
            "http://www.example.com");

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Arrays.asList("application/json",
                    "application/xml"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
