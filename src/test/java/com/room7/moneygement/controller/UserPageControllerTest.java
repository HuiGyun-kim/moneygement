package com.room7.moneygement.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.room7.moneygement.dto.PasswordChangeDTO;
import com.room7.moneygement.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserPageControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService userService;

    @Test
    public void testChangePassword() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
        passwordChangeDto.setCurrentPassword("oldPassword");
        passwordChangeDto.setNewPassword("newPassword");

        when(userService.changePassword(any(), any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/change-pw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"currentPassword\":\"oldPassword\",\"newPassword\":\"newPassword\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAccount() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        Map<String, String> credentials = new HashMap<>();
        credentials.put("password", "userPassword");

        when(userService.checkPassword(any(), any())).thenReturn(true);

        mockMvc.perform(post("/api/delete-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"userPassword\"}"))
                .andExpect(status().isOk());
    }
}