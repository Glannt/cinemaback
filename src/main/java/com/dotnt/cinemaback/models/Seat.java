package com.dotnt.cinemaback.models;

import com.dotnt.cinemaback.constants.SeatType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "Seat")
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends AbstractEntity<UUID>{
    private String name;

    @Enumerated(EnumType.STRING)
    private SeatType type;

    @OneToMany(mappedBy = "seat")
    private Set<HallHasSeat> hallHasSeats;
}
