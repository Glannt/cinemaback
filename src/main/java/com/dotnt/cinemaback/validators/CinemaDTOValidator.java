package com.dotnt.cinemaback.validators;

import com.dotnt.cinemaback.dto.CinemaDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CinemaDTOValidator implements ConstraintValidator<ValidCinemaDTO, CinemaDTO> {

    @Override
    public boolean isValid(CinemaDTO cinemaDTO, ConstraintValidatorContext context) {
        // Add your custom validation logic here
        if (cinemaDTO == null) {
            return false;
        }
        // Example validation: check if a required field is not null
        return cinemaDTO.getName() != null && !cinemaDTO.getName().isEmpty();
    }
}