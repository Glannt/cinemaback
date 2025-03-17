package com.dotnt.cinemaback.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "Role")
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role extends AbstractEntity<UUID> {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "role")
    private Set<UserHasRole> userHasRoles;

    @OneToMany(mappedBy = "role")
    private Set<RoleHasPermission> roleHasPermissions;


}
