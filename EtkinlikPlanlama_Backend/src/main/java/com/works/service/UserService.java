package com.works.service;

import com.works.dto.UserLoginRequestDto;
import com.works.dto.UserRegisterRequestDto;
import com.works.dto.UserResponseDto;
import com.works.entity.User;
import com.works.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;
    final ModelMapper model;
    final HttpServletRequest request;

    public ResponseEntity register(UserRegisterRequestDto userRegisterRequestDto) {
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(userRegisterRequestDto.getEmail());
        if (optionalUser.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Bu e-posta adresi zaten kullanımda."));
        }

        User user = model.map(userRegisterRequestDto, User.class);
        String hashPassword = org.mindrot.jbcrypt.BCrypt.hashpw(user.getPassword(), org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPassword(hashPassword);
        user.setEnabled(true);
        userRepository.save(user);

        UserResponseDto responseDto = model.map(user, UserResponseDto.class);
        return ResponseEntity.ok().body(responseDto);
    }

    public ResponseEntity login(UserLoginRequestDto userLoginRequestDto) {
        Optional<User> optionalUser = userRepository.findByEnabledTrueAndEmailIgnoreCase(userLoginRequestDto.getEmail());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean isMatch = BCrypt.checkpw(userLoginRequestDto.getPassword(), user.getPassword());

            if (isMatch) {
                UserResponseDto userResponseDto = model.map(user, UserResponseDto.class);
                request.getSession().setAttribute("user", userResponseDto);
                return ResponseEntity.ok().body(userResponseDto);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "E-posta veya şifre hatalı veya hesap aktif değil."));
    }

    public ResponseEntity logout() {
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(Map.of("success", true, "message", "Oturum kapatıldı."));
    }
}
