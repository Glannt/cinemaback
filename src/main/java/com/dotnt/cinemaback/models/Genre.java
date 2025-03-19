package com.dotnt.cinemaback.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "Genres")
@Table(name = "genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Genre extends AbstractEntity<UUID> {

    //    @Column(nullable = false, unique = true)
    private String name; // Ví dụ: "Action", "Comedy", "Horror"

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MovieGenre> movieGenres;
}
