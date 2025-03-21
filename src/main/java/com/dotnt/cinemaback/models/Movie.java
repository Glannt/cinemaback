package com.dotnt.cinemaback.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity(name = "Movie")
@Table(name = "movie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie extends AbstractEntity<UUID> {
    @NotNull(message = "Title is required")
    private String title = "";
    @NotNull(message = "Description is required")
    private String description = "";
    @NotNull(message = "Duration is required")
    private Integer duration;
    @NotNull(message = "Director is required")
    private String director = "";
    private boolean adult = false;
    //    @NotNull(message = "Cast is required")
//    private String cast;

    @NotNull(message = "Release date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NotNull(message = "Status is required")
    private String status = "";
    @NotNull(message = "Rating is required")
    private Double rating = 0.0;
    @NotNull(message = "Price is required")
    private Double price = 0.0;
    private boolean allowToShow = true;


    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShowTime> showTimes;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MovieImage> movieImages;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenre> movieGenres;


}
