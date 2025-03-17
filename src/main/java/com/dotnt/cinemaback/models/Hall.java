package com.dotnt.cinemaback.models;


import com.dotnt.cinemaback.constants.CinemaStatus;
import com.dotnt.cinemaback.constants.ProjectionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;


@Entity(name = "Hall")
@Table(name = "hall")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hall extends AbstractEntity<UUID>{
    private String name;

    private int seatCount;

    @Enumerated(EnumType.STRING)
    private ProjectionType projectionType;

    @Enumerated(EnumType.STRING)
    private CinemaStatus status;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    @OneToMany(mappedBy = "hall")
    private Set<HallHasSeat> hallHasSeats;
}
