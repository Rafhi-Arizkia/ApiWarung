package com.api.warung.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @Email
    @NotEmpty(message = "Required email for login")
    private String email;
    @NotEmpty(message = "Required password for login")
    private String password;
}
