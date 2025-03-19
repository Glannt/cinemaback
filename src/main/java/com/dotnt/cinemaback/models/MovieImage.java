package com.dotnt.cinemaback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "MovieImage")
@Table(name = "movie_image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieImage extends AbstractEntity<UUID> {
    private String url;
    private String alt;
    private String title;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}
