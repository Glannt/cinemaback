package com.dotnt.cinemaback.models;

import com.dotnt.cinemaback.constants.Gender;
import com.dotnt.cinemaback.constants.UserStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity<UUID>
//        implements UserDetails
        {

    @Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;


    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "url_avatar")
    private String avatarUrl;

    @Column(name = "status")
    private UserStatus status;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserHasRole> userHasRoles;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return userHasRoles.stream().map(UserHasRole::getRole)
//                .map(role -> new SimpleGrantedAuthority(role.getName()))
//                .collect(Collectors.toSet());
//    }
//
//    @Override
//    public String getUsername() {
//        return "";
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
//    }
}
