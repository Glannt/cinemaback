package com.dotnt.cinemaback.models;


import com.dotnt.cinemaback.constants.CinemaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity(name = "Cinema")
@Table(name = "cinema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends AbstractEntity<UUID>{
    private String name;

    @Enumerated(EnumType.STRING)
    private CinemaStatus status;
    private String addressId;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<Hall> hall;
}
