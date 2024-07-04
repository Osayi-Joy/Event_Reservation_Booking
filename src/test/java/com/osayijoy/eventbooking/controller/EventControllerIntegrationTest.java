package com.osayijoy.eventbooking.controller;


/**
 * @author Joy Osayi
 * @createdOn Jul-02(Tue)-2024
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.enums.Category;
import com.osayijoy.eventbooking.repository.EventRepository;
import com.osayijoy.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EventControllerIntegrationTest extends TestHelper{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper;
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
        eventRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testCreateEvent() throws Exception {

        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        EventRequestDTO eventRequestDto = new EventRequestDTO("Sample Event", LocalDateTime.now(), 500, "Sample Description", Category.CONCERT);
        String requestBody = objectMapper.writeValueAsString(eventRequestDto);

        mockMvc.perform(post(EVENT_API_VI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_ACCESS_TOKEN)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Sample Event"))
                .andExpect(jsonPath("$.data.description").value("Sample Description"));
    }

    @Test
    void testUpdateEvent() throws Exception {

        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long id = createEvent(ADMIN_ACCESS_TOKEN);

        EventRequestDTO eventRequestDto = new EventRequestDTO("Updated Event", LocalDateTime.now(), 500, "Updated Description", Category.CONFERENCE);
        String requestBody = objectMapper.writeValueAsString(eventRequestDto);

        mockMvc.perform(put(EVENT_API_VI.concat(String.valueOf(id)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_ACCESS_TOKEN)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Updated Event"))
                .andExpect(jsonPath("$.data.description").value("Updated Description"));
    }



    @Test
    void testSearchEvents() throws Exception {


        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);

        ACCESS_TOKEN = login(USERNAME);


        mockMvc.perform(get(EVENT_API_VI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void testDeleteEvent() throws Exception {
        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long id = createEvent(ADMIN_ACCESS_TOKEN);

        mockMvc.perform(delete(EVENT_API_VI.concat(String.valueOf(id)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ADMIN_ACCESS_TOKEN))
                .andExpect(status().isOk());
    }


    @Test
    void testGetEventById() throws Exception {

        createUser(ADMIN_EMAIL);

        ADMIN_ACCESS_TOKEN = login(ADMIN_EMAIL);

        Long id = createEvent(ADMIN_ACCESS_TOKEN);

        createUser(USERNAME);

        ACCESS_TOKEN = login(USERNAME);

        mockMvc.perform(get(EVENT_API_VI.concat(String.valueOf(id)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Sample Event"))
                .andExpect(jsonPath("$.data.description").value("Sample Description"));
    }
}
