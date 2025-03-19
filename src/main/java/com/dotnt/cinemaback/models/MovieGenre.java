package com.dotnt.cinemaback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "MovieGenres")
@Table(name = "movie_genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieGenre extends AbstractEntity<UUID> {


    @ManyToOne
//    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
//    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;

    // Có thể thêm các cột khác sau này
    private Integer priority;
}
