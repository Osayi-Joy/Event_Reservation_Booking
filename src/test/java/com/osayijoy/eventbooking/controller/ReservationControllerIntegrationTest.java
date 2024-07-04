package com.osayijoy.eventbooking.controller;

import com.osayijoy.eventbooking.repository.ReservationRepository;
import com.osayijoy.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.osayijoy.eventbooking.utils.constants.Constants.RESERVATION_API_V1;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Joy Osayi
 * @createdOn Jul-02(Tue)-2024
 */
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ReservationControllerIntegrationTest extends TestHelper{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;



    private static String ACCESS_TOKEN="";
    private static String ADMIN_ACCESS_TOKEN = "";
    private static final String USERNAME = "osayijoy17@gmail.com";
    private static final String ADMIN_EMAIL = "osayijoy@musulasoft.com";


    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @BeforeEach
    void checkup(){
        reservationRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateReservation() throws Exception {

        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long eventId = createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);

        ACCESS_TOKEN = login(USERNAME);

        int attendeesCount = 5;

        mockMvc.perform(post(RESERVATION_API_V1)
                        .param("eventId", eventId.toString())
                        .param("email", USERNAME)
                        .param("attendeesCount", String.valueOf(attendeesCount))
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userEmail").value(USERNAME))
                .andExpect(jsonPath("$.data.eventId").value(eventId))
                .andExpect(jsonPath("$.data.attendeesCount").value(attendeesCount));
    }

    @Test
    void testGetReservationsByUser() throws Exception {
        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long eventId = createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);

        ACCESS_TOKEN = login(USERNAME);

        int attendeesCount = 5;

        createReservation(USERNAME, eventId, attendeesCount, ACCESS_TOKEN);

        mockMvc.perform(get(RESERVATION_API_V1 + "/user/" + USERNAME)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").exists())
                .andExpect(jsonPath("$.data.content[0].eventId").value(eventId))
                .andExpect(jsonPath("$.data.content[0].userEmail").value(USERNAME))
                .andExpect(jsonPath("$.data.content[0].attendeesCount").value(attendeesCount));
    }


    @Test
    void testGetReservationsByEvent() throws Exception {

        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long eventId = createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);

        ACCESS_TOKEN = login(USERNAME);

        int attendeesCount = 5;

        createReservation(USERNAME, eventId, attendeesCount, ACCESS_TOKEN);

        mockMvc.perform(get(RESERVATION_API_V1 + "/event/" + eventId)
                        .header("Authorization", "Bearer " + ADMIN_ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].id").exists())
                .andExpect(jsonPath("$.data.content[0].eventId").exists())
                .andExpect(jsonPath("$.data.content[0].userEmail").value(USERNAME))
                .andExpect(jsonPath("$.data.content[0].attendeesCount").value(attendeesCount));
    }

    @Test
    void testCancelReservation() throws Exception {

        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long eventId = createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);

        ACCESS_TOKEN = login(USERNAME);

        int attendeesCount = 5;

        Long reservationId = createReservation(USERNAME, eventId, attendeesCount, ACCESS_TOKEN);


        mockMvc.perform(delete(RESERVATION_API_V1 + "/" + reservationId)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservation() throws Exception {

        createUser(ADMIN_EMAIL);
        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long eventId = createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);
        ACCESS_TOKEN = login(USERNAME);

        int attendeesCount = 5;
        createReservation(USERNAME, eventId, attendeesCount, ACCESS_TOKEN);

        mockMvc.perform(get(RESERVATION_API_V1 + "/event/" + eventId + "/" + USERNAME)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.eventId").value(eventId))
                .andExpect(jsonPath("$.data.userEmail").value(USERNAME));
    }
}
