package com.room7.moneygement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;



import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AttendanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCheckAttendance() throws Exception {

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("date", LocalDate.now().toString());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/attendance/check")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        String content = result.getResponse().getContentAsString();
        assertTrue(content.contains("출석체크가 성공적으로 완료되었습니다."));
    }


    @Test
    void testCheckRewardEligibility() throws Exception {
        // Prepare request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", 123L); // Assuming userId for testing


        mockMvc.perform(MockMvcRequestBuilders.post("/attendance/reward/eligible")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Reward eligible."));
    }

    @Test
    void testGetAllAttendance() throws Exception {
        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/attendance/all")
                        .param("userId", "123")) // Assuming userId for testing
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()); // Assuming it returns an array of objects
    }

    @Test
    void testIsAttend() throws Exception {
        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/attendance/isAttend/{userId}", 123L)) // Assuming userId for testing
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("true")); // Assuming the response is true or false
    }
}