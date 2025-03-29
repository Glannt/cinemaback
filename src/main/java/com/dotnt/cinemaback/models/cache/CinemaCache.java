package com.dotnt.cinemaback.models.cache;

import com.dotnt.cinemaback.models.Cinema;
import lombok.Data;

@Data
public class CinemaCache {
    private Long version;
    private Cinema cinema;

    public CinemaCache withClone(Cinema cinema) {
        this.cinema = cinema;
        return this;
    }

    public CinemaCache withVersion(Long version) {
        this.version = version;
        return this;
    }
}
