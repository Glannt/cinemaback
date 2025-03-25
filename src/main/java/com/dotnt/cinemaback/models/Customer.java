package com.dotnt.cinemaback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "Customer")
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends AbstractEntity<UUID> {
    private Integer loyaltyPoints;
    private boolean marketingOptIn;
}
