package com.osayijoy.eventbooking.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.osayijoy.eventbooking.dto.request.Credentials;
import com.osayijoy.eventbooking.dto.request.EventRequestDTO;
import com.osayijoy.eventbooking.dto.request.UserRequestDto;
import com.osayijoy.eventbooking.enums.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.osayijoy.eventbooking.utils.constants.Constants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Joy Osayi
 * @createdOn Jul-01(Mon)-2024
 */

import java.time.LocalDateTime;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class TestHelper {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    public void createUser(String username) throws Exception {
        UserRequestDto userRequestDto =
                new UserRequestDto("Joy Osayi", username, "password");
        String requestBody = objectMapper.writeValueAsString(userRequestDto);

        mockMvc.perform(post(USER_API_VI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();
    }

    public String login(String username) throws Exception {
        Credentials credentials = new Credentials(username, "password");
        String requestBody = objectMapper.writeValueAsString(credentials);

        MvcResult result = mockMvc.perform(post(AUTHENTICATION_API_VI.concat("login"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        return jsonNode.get("data").get("accessToken").asText();
    }



    public Long createEvent(String token) throws Exception {
        EventRequestDTO eventRequestDto = new EventRequestDTO("Sample Event", LocalDateTime.now(), 500, "Sample Description", Category.CONCERT);
        String requestBody = objectMapper.writeValueAsString(eventRequestDto);

        MvcResult result = mockMvc.perform(post(EVENT_API_VI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("Sample Event"))
                .andExpect(jsonPath("$.data.description").value("Sample Description"))
                .andReturn();


        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        return jsonNode.get("data").get("id").asLong();
    }

    public Long createReservation(String email, Long eventId, int attendeesCount, String token) throws Exception {
        MvcResult result = mockMvc.perform(post(RESERVATION_API_V1)
                        .param("eventId", eventId.toString())
                        .param("email", email)
                        .param("attendeesCount", String.valueOf(attendeesCount))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.userEmail").value(email))
                .andExpect(jsonPath("$.data.eventId").value(eventId))
                .andExpect(jsonPath("$.data.attendeesCount").value(attendeesCount))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        return jsonNode.get("data").get("id").asLong();
    }
}
