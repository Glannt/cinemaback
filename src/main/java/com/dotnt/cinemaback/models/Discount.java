package com.dotnt.cinemaback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity(name = "Discount")
@Table(name = "discount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount extends AbstractEntity<UUID> {
    private String name;

    private String code;

    private double discountPercent;

    private boolean active;

    private LocalDate expire;

    @OneToMany(mappedBy = "discount")
    private List<Ticket> tickets;
}
