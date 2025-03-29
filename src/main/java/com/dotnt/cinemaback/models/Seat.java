package com.dotnt.cinemaback.models;

import com.dotnt.cinemaback.constants.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Seat")
@Table(name = "seat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends AbstractEntity<UUID> {
    @Column(name = "seat_row")
    private String row;  // e.g., "A", "B", "C"

    //    @Column(nullable = false)
    private Integer number;  // e.g., 1, 2, 3

//    @Column(nullable = false)
//    private Double price;

    //    @Column(nullable = false)
    private SeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_type_id", nullable = false)
    private SeatType seatType;

    @OneToMany(mappedBy = "seat")
    @JsonBackReference
    @JsonIgnore
    private Set<HallHasSeat> hallHasSeats;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookedSeat> bookedSeats = new ArrayList<>();
}
