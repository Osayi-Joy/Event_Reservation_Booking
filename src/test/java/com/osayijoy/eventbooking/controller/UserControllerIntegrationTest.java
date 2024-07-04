package com.osayijoy.eventbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osayijoy.eventbooking.dto.request.UserRequestDto;
import com.osayijoy.eventbooking.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.osayijoy.eventbooking.utils.constants.Constants.USER_API_VI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Joy Osayi
 * @createdOn Jul-01(Mon)-2024
 */
@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIntegrationTest extends TestHelper{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private static String ACCESS_TOKEN="";
    private static String ADMIN_ACCESS_TOKEN="";
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
        userRepository.deleteAll();
    }



    @Test
    void testCreateUser() throws Exception {
        UserRequestDto userRequestDto =
                new UserRequestDto("Alice Smith", "alice.smith@example.com", "password");
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        mockMvc.perform(post(USER_API_VI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Alice Smith"))
                .andExpect(jsonPath("$.data.email").value("alice.smith@example.com"));
    }

    @Test
    void testUpdateUser() throws Exception {
        createUser(USERNAME);
        ACCESS_TOKEN = login(USERNAME);
        UserRequestDto userRequestDto = new UserRequestDto("Joy Osayi Updated", "osayijoy17@gmail.com", "newpassword");
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        mockMvc.perform(put(USER_API_VI.concat("/osayijoy17@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Joy Osayi Updated"))
                .andExpect(jsonPath("$.data.email").value("osayijoy17@gmail.com"));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        createUser(USERNAME);
        ACCESS_TOKEN = login(USERNAME);
        mockMvc.perform(get(USER_API_VI.concat("/osayijoy17@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Joy Osayi"))
                .andExpect(jsonPath("$.data.email").value("osayijoy17@gmail.com"));
    }

    @Test
    void testGetUsers() throws Exception {
        createUser(USERNAME);
        createUser(ADMIN_EMAIL);
        ACCESS_TOKEN = login(ADMIN_EMAIL);
        mockMvc.perform(get(USER_API_VI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].name").value("Joy Osayi"))
                .andExpect(jsonPath("$.data.content[0].email").value("osayijoy17@gmail.com"));
    }

    @Test
    void testDeleteUser() throws Exception {
        createUser(USERNAME);
        ACCESS_TOKEN = login(USERNAME);
        mockMvc.perform(delete(USER_API_VI.concat("osayijoy17@gmail.com"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + ACCESS_TOKEN))
                .andExpect(status().isNoContent());
    }
}
