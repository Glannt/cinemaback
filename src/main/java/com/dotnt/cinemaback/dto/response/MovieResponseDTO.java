package com.dotnt.cinemaback.dto.response;

import com.dotnt.cinemaback.models.MovieImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private Integer duration;
    private String director;
    //    private List<String> cast;
    private List<String> genres;
    private String releaseDate;
    //    private String poster;
//    private String trailer;
    private List<MovieImage> images;
    private String status;
}