package com.dotnt.cinemaback.models;


import com.dotnt.cinemaback.constants.enums.CinemaStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Cinema")
@Table(name = "cinema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cinema extends AbstractEntity<UUID> {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private CinemaStatus status;
    @NotNull(message = "Address is required")
    private String address;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
//    @Singular
//    @JsonManagedReference
    private List<Hall> halls = new ArrayList<>();
}
