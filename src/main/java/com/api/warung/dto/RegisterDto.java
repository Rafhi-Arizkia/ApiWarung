package com.api.warung.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    @NotEmpty(message = "Required Username")
    private String userName;
    @Email
    @NotEmpty(message = "Required Email")
    private String userEmail;
    @NotEmpty(message = "Required Address")
    private String userAddress;
    @NotNull(message = "Required Number Phone")
    private Long numberPhone;
    @NotEmpty(message = "Required password")
    private String password;

}
