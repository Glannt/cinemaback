package com.dotnt.cinemaback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "UserHasRole")
@Table(name = "user_role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserHasRole extends AbstractEntity<UUID>{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

   
}
