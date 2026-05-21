package com.works.controller;

import com.works.dto.UserLoginRequestDto;
import com.works.dto.UserRegisterRequestDto;
import com.works.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserRestController {
    final UserService userService;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return userService.register(userRegisterRequestDto);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return userService.login(userLoginRequestDto);
    }

    @GetMapping("logout")
    public ResponseEntity logout() {
        return userService.logout();
    }

    @GetMapping("control")
    public ResponseEntity control() {
        return ResponseEntity.ok(java.util.Map.of("success", true, "message", "Oturum aktif."));
    }
}
