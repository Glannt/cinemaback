package com.dotnt.cinemaback.models;


import com.dotnt.cinemaback.constants.enums.CinemaStatus;
import com.dotnt.cinemaback.constants.enums.ProjectionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Entity(name = "Hall")
@Table(name = "hall")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall extends AbstractEntity<UUID> {
    @NotNull(message = "Name is required")
    private String name;

    private int seatCount;

    @NotNull(message = "Projection type is required")
    @Enumerated(EnumType.STRING)
    private ProjectionType projectionType;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private CinemaStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cinema_id", nullable = false)
    @JsonBackReference
    @JsonIgnore
    private Cinema cinema;

    @OneToMany(mappedBy = "hall")
//    @Singular
//    @JsonManagedReference
    private Set<HallHasSeat> hallHasSeats = new HashSet<>();

    @OneToMany(mappedBy = "hall")
//    @JsonManagedReference
    private Set<ShowTime> showTimes = new HashSet<>();
}