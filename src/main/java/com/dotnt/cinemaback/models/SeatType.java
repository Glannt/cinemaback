package com.dotnt.cinemaback.models;

import com.dotnt.cinemaback.constants.enums.ESeatType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "Seat Type")
@Table(name = "seat_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatType extends AbstractEntity<UUID> {
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true)
    private ESeatType name;
    private String description;
    private Double price;
    @OneToMany(mappedBy = "seatType")
    private List<Seat> seats;

}
