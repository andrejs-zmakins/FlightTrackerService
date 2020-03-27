package flights.flighttracker;

import flights.flighttracker.airport.Airport;
import flights.flighttracker.airport.AirportRepository;
import flights.flighttracker.flights.Flight;
import flights.flighttracker.flights.FlightController;
import flights.flighttracker.flights.FlightRepository;
import flights.flighttracker.flights.FlightService;
import flights.flighttracker.flights.Flights;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests for the <code>FlightController</code> class.
 * 
 * @see FlightController
 * @author Sigita Livina
 */

public class FlightControllerTest extends FlightTrackerApplicationTests {

	@Mock
	private FlightRepository flightRepository;

	@Mock
	private AirportRepository airportRepository;

	@MockBean
	private FlightService flightService;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@Mock
	private FlightController controller;

	List<Flight> flights;
	
	List<Flight> incorrectFlights;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

		List<Airport> airportList = new LinkedList<Airport>() {
			{
				add(new Airport(1, "Heathrow", "LHR"));
			}
		};

		Mockito.when(airportRepository.findAll()).thenReturn(airportList);

		List<Flight> dummyDepartingFlights = new ArrayList<>();

		List<Flight> dummyArrivingFlights = new ArrayList<>();

		Date date = new Date();

		Flight flight1 = new Flight();
		flight1.setFlightNumber("31");
		flight1.setAirlineName("Delta Air Lines");
		flight1.setDepartureAirport("Heathrow");
		flight1.setDepartureAirportIata("LHR");
		flight1.setArrivalAirport("Hartsfield-jackson Atlanta International");
		flight1.setArrivalAirportIata("ATL");
		flight1.setFlightDate(date);
		flight1.setStatus("scheduled");

		Flight flight2 = new Flight();
		flight2.setFlightNumber("4045");
		flight2.setAirlineName("Virgin Atlantic");
		flight2.setDepartureAirport("Heathrow");
		flight2.setDepartureAirportIata("LHR");
		flight2.setArrivalAirport("Hartsfield-jackson Atlanta International");
		flight2.setArrivalAirportIata("ATL");
		flight2.setFlightDate(date);
		flight2.setStatus("scheduled");

		Flight flight3 = new Flight();
		flight3.setFlightNumber("5438");
		flight3.setAirlineName("Finnair");
		flight3.setDepartureAirport("Logan International");
		flight3.setDepartureAirportIata("BOS");
		flight3.setArrivalAirport("Heathrow");
		flight3.setArrivalAirportIata("LHR");
		flight3.setFlightDate(date);
		flight3.setStatus("scheduled");

		Flight flight4 = new Flight();
		flight4.setFlightNumber("1307");
		flight4.setAirlineName("British Airways");
		flight4.setDepartureAirport("Dyce");
		flight4.setDepartureAirportIata("ABZ");
		flight4.setArrivalAirport("Heathrow");
		flight4.setArrivalAirportIata("LHR");
		flight4.setFlightDate(date);
		flight4.setStatus("scheduled");
		
		

		dummyDepartingFlights.add(flight1);
		dummyDepartingFlights.add(flight2);
		dummyArrivingFlights.add(flight3);
		dummyArrivingFlights.add(flight4);

		Flights departingflights = new Flights();

		departingflights.setData(dummyDepartingFlights);

		Flights arrivingflights = new Flights();

		arrivingflights.setData(dummyArrivingFlights);

		flights = new ArrayList<>();

		flights.addAll(departingflights.getFlights());
		flights.addAll(arrivingflights.getFlights());
		
		incorrectFlights = new ArrayList<>();
		
		Flight flightIncorrect = new Flight();
		flightIncorrect.setFlightNumber("1307");
		flightIncorrect.setAirlineName("British Airways");
		flightIncorrect.setDepartureAirport("Dyce");
		flightIncorrect.setDepartureAirportIata("ABZtry");
		flightIncorrect.setArrivalAirport("Heathrow");
		flightIncorrect.setArrivalAirportIata("LHRterter");
		flightIncorrect.setFlightDate(date);
		flightIncorrect.setStatus("scheduled");
		
		incorrectFlights = new ArrayList<>();
		
		incorrectFlights.add(flightIncorrect);

	}

	@Test
	public void testGetFlights() throws Exception {
		Mockito.when(flightService.getAllFlights()).thenReturn(flights);
		mockMvc.perform(get("/flights")).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("flightNumber")));

	}

	@Test
	public void testSaveOrUpdateFlights() throws Exception {
		Mockito.when(flightService.getAllFlights()).thenReturn(flights);
		mockMvc.perform(MockMvcRequestBuilders.post("/flights/save")).andExpect(status().isCreated())
				.andExpect(content().contentType("application/json"));
		// check for update
		mockMvc.perform(MockMvcRequestBuilders.post("/flights/save")).andExpect(status().isCreated())
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
		Mockito.when(flightService.getFlightsForAirport(Mockito.anyString())).thenReturn(flights);
		mockMvc.perform(get("/flights/LHR")).andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(content().string(org.hamcrest.Matchers.containsString("flightNumber")));
	}

	@Test
	public void testGetFlightsWithInvalidApiKey() throws Exception {
		flightService.setApiKey("invalidApiKey");
		mockMvc.perform(get("/flights")).andExpect(status().isInternalServerError())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("No flights received")));
	}

	@Test
	public void testSaveFlightsWithInvalidApiKey() throws Exception {
		flightService.setApiKey("invalidApiKey");
		mockMvc.perform(MockMvcRequestBuilders.post("/flights/save")).andExpect(status().isInternalServerError())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("No flights received")));
	}

	@Test
	public void testGetFlightsForAirportIataWithInvalidApiKey() throws Exception {
		flightService.setApiKey("invalidApiKey");
		mockMvc.perform(get("/flights/LHR")).andExpect(status().isInternalServerError())
				.andExpect(content().string(org.hamcrest.Matchers.containsString("No flights received")));
	}
	
	@Test
	public void testSaveFlightsException() throws Throwable {
		Mockito.when(flightService.getAllFlights()).thenReturn(incorrectFlights);
		mockMvc.perform(MockMvcRequestBuilders.post("/flights/save")).andExpect(status().isBadRequest())
		.andExpect(content().string(org.hamcrest.Matchers.containsString("Error while saving flights")));

	}

}
