package com.dotnt.cinemaback.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Builder
public class SignupResponse implements Serializable {
    private String fullName;
    private String email;
    private LocalDate dob;
    Set<String> roles;
}
