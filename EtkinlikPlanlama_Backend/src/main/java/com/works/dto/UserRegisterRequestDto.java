package com.works.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequestDto {
    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String surname;

    @Email
    @NotEmpty
    @NotNull
    private String email;

    private boolean enabled;

    @Size(min = 6)
    @NotEmpty
    @NotNull
    private String password;

}
