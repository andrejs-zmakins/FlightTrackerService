package flights.flighttracker;

import flights.flighttracker.flights.FlightRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Tests for the <code>FlightController</code> class.
 * @see FlightController
 * @author Sigita Livina
 */

public class FlightControllerTest extends FlightTrackerApplicationTests {

    @Mock
    private FlightRepository flightRepository;
    
    @Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

    @Test
    public void testGetFlights() throws Exception {
			mockMvc.perform(get("/flights")).andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("flightNumber")));
    }
    
    @Test
    public void testSaveOrUpdateFlights() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
			      .post("/flights/save"))
				  .andExpect(status().isOk())
				  .andExpect(content().contentType("application/json"));
		//check for update
		mockMvc.perform(MockMvcRequestBuilders
			      .post("/flights/save"))
				  .andExpect(status().isOk())
				  .andExpect(content().contentType("application/json"));
    }
    	
    @Test
    public void testGetSavedFlights() throws Exception {
			mockMvc.perform(get("/savedFlights")).andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("flightNumber")));
    }
    
    @Test
    public void testGetFlightsForAirportIata() throws Exception {
			mockMvc.perform(get("/flights/LHR")).andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(content().string(org.hamcrest.Matchers.containsString("flightNumber")));
    }
    
}
