package com.osayijoy.eventbooking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.osayijoy.eventbooking.dto.request.Credentials;
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

import static com.osayijoy.eventbooking.utils.constants.Constants.AUTHENTICATION_API_VI;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Joy Osayi
 * @createdOn Jun-30(Sun)-2024
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest extends TestHelper{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private static String ACCESS_TOKEN="";
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



    @Test
    void testLogin() throws Exception {

        createUser(USERNAME);

        Credentials credentials = new Credentials("osayijoy17@gmail.com", "password");
        String requestBody = objectMapper.writeValueAsString(credentials);

        mockMvc.perform(post(AUTHENTICATION_API_VI.concat("login"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }
}

