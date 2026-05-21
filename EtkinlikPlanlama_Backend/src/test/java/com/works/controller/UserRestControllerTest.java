package com.works.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.works.dto.UserRegisterRequestDto;
import com.works.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.context.annotation.Import;
import com.works.CacheTestConfig;

@Import(CacheTestConfig.class)
@WebMvcTest(UserRestController.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegister_Success() throws Exception {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setName("Burak");
        requestDto.setSurname("Senol");
        requestDto.setEmail("burak@gmail.com");
        requestDto.setEnabled(true);
        requestDto.setPassword("123456");

        Mockito.when(userService.register(any(UserRegisterRequestDto.class)))
                .thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testRegister_PasswordTooShort() throws Exception {
        UserRegisterRequestDto requestDto = new UserRegisterRequestDto();
        requestDto.setName("Burak");
        requestDto.setSurname("Senol");
        requestDto.setEmail("burak@gmail.com");
        requestDto.setEnabled(true);
        requestDto.setPassword("123");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
