package com.dotnt.cinemaback.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity(name = "RoleHasPermission")
@Table(name = "role_permission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoleHasPermission extends AbstractEntity<UUID> {

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
