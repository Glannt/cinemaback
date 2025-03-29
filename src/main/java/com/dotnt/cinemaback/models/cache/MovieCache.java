package com.dotnt.cinemaback.models.cache;


import com.dotnt.cinemaback.models.Movie;
import lombok.Data;

@Data
public class MovieCache {
    private Long version;
    private Movie movie;

    public MovieCache withClone(Movie movie) {
        this.movie = movie;
        return this;
    }

    public MovieCache withVersion(Long version) {
        this.version = version;
        return this;
    }
}
