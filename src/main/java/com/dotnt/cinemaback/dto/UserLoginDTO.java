package com.dotnt.cinemaback.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginDTO {
    @JsonProperty("email")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    public boolean isPasswordBlank() {
        return password == null || password.trim().isEmpty();
    }
}
