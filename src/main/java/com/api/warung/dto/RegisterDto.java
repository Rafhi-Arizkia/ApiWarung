package com.api.warung.dto;

import jakarta.validation.constraints.*;
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
    @Pattern(regexp = "62\\d{11,12}", message = "Invalid phone number")
    private Long numberPhone;
    @NotEmpty(message = "Required password")
    private String password;

}
