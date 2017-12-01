package com.gmsboilerplatesbng.domain.security.permission;

import com.gmsboilerplatesbng.domain.GmsEntity;
import com.gmsboilerplatesbng.domain.security.role.BRole;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@Entity
public class BPermission extends GmsEntity {

    /**
     * Name to be used for authenticating the user
     */
    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String name;

    /**
     * Label to be shown to the final user
     */
    @NotNull
    @NotBlank
    @Column(unique = true, nullable = false)
    private final String label;

    @ManyToMany(mappedBy = "permissions")
    Set<BRole> roles = new HashSet<>();
}
