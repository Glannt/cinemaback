package com.dotnt.cinemaback.validators;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CinemaDTOValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCinemaDTO {
    String message() default "Invalid CinemaDTO";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}