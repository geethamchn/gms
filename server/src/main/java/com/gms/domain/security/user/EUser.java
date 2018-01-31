package com.gms.domain.security.user;

import com.gms.domain.GmsEntity;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashSet;

/**
 * EUser
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"authorities", "password"})
@Entity
public class EUser extends GmsEntity implements UserDetails {

    @NotNull
    @NotBlank
    @Column(unique = true)
    private final String username;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private final String email;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    @NotBlank
    private final String lastName;

    @NotNull
    @NotBlank
    private final String password;

    @Getter(AccessLevel.NONE)
    private Boolean enabled;

    @Getter(AccessLevel.NONE)
    private Boolean emailVerified;

    @Getter(AccessLevel.NONE)
    private Boolean accountNonExpired;

    @Getter(AccessLevel.NONE)
    private Boolean accountNonLocked;

    @Getter(AccessLevel.NONE)
    private Boolean credentialsNonExpired;

    // user authorities are handled via jjwt, this attribute is kept for compatibility with Spring Security
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Getter(AccessLevel.NONE)
    private HashSet<GrantedAuthority> authorities = null;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // by default: true
        return accountNonExpired == null || accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        // by default: true
        return accountNonLocked == null || accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // by default: true
        return credentialsNonExpired == null || credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        // by default: false
        return enabled != null && enabled;
    }

    public boolean isEmailVerified() {
        return emailVerified != null && emailVerified;
    }
}
