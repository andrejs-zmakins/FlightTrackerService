package flights.flighttracker;

import flights.flighttracker.airport.AirportController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Test for <code>AirportController</code>.
 *
 * Numbers in method names are necessary to enforce test order and save some time on mock mvc instantiation.
 * @see AirportController
 * @author Andrejs Zmakins
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AirportControllerEndpointTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    public void test1GetAllAirports() throws Exception {
        mockMvc.perform( get("/airports") )
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));

    }


    @Test
    public void test2GetAirportById() throws Exception {
        mockMvc.perform( get("/airports/1") )
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }


    @Test
    public void test3DeleteAirportPathParam() throws Exception {
        mockMvc.perform( delete("/airports/1") )
                .andExpect(status().isOk());
    }


    @Test
    public void test4DeleteAirportQueryParam() throws Exception {
        mockMvc.perform( delete("/airports/?id=2") )
                .andExpect(status().isOk());
    }


    @Test
    public void test5DeleteAirportFailurePathParam() throws Exception {
        mockMvc.perform( delete("/airports/123") )
                .andExpect(status().isBadRequest());
    }


    @Test
    public void test6DeleteAirportFailureQueryParam() throws Exception {
        mockMvc.perform( delete("/airports/?id=234") )
                .andExpect(status().isBadRequest());
    }
}
