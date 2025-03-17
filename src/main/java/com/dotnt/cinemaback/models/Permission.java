package com.dotnt.cinemaback.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity(name = "Permission")
@Table(name = "permission")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Permission extends AbstractEntity<UUID> {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "permission")
    private Set<RoleHasPermission> roleHasPermissions;
}
