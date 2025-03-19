package com.dotnt.cinemaback.models;


import com.dotnt.cinemaback.constants.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "HallHasSeat")
@Table(name = "hall_seat")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HallHasSeat extends AbstractEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    @JsonBackReference
    private Hall hall;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
//    @JsonBackReference
//    @JsonIgnore
    private Seat seat;

    private SeatStatus status;
}