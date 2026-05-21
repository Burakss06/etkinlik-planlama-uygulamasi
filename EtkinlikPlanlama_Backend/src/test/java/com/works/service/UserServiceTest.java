package com.works.service;

import com.works.dto.UserRegisterRequestDto;
import com.works.entity.User;
import com.works.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    public void testRegister_Success() {
        UserRegisterRequestDto dto = new UserRegisterRequestDto();
        dto.setName("Burak");
        dto.setSurname("Senol");
        dto.setEmail("burak@gmail.com");
        dto.setEnabled(true);
        dto.setPassword("123456");

        when(userRepository.findByEmailIgnoreCase(dto.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity response = userService.register(dto);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegister_DuplicateEmail() {
        UserRegisterRequestDto dto = new UserRegisterRequestDto();
        dto.setName("Burak");
        dto.setSurname("Senol");
        dto.setEmail("burak@gmail.com");
        dto.setEnabled(true);
        dto.setPassword("123456");

        User existingUser = new User();
        existingUser.setEmail("burak@gmail.com");

        when(userRepository.findByEmailIgnoreCase(dto.getEmail())).thenReturn(Optional.of(existingUser));

        ResponseEntity response = userService.register(dto);
        assertEquals(400, response.getStatusCode().value());
    }
}
