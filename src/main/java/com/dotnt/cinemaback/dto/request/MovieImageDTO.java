package com.dotnt.cinemaback.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
public class MovieImageDTO {

    private UUID movieId;

    @Size(min = 5, max = 200, message = "Image's name")
    private String url;

    private String title;
}
