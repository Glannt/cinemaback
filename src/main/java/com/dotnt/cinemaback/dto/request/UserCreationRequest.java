package com.dotnt.cinemaback.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserCreationRequest {


    @Size(min = 4, message = "EMAIL_INVALID")
    @Email
    private String email;

    @Size(min = 6, message = "INVALID_PASSWORD")
    private String password;

    private String firstName;
    private String lastName;

//    @DobConstraint(min = 10, message = "INVALID_DOB")
    private LocalDate dob;

//    @PhoneNumberConstraint(min= 10, message = "INVALID_PN")
    private String phoneNumber;

//    private String address;



}
