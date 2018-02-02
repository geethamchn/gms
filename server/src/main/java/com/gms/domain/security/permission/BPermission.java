package com.gms.domain.security.permission;

import com.gms.domain.GmsEntity;
import com.gms.domain.security.role.BRole;
import com.gms.util.i18n.CodeI18N;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * BPermission
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Data
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = "roles")
@Entity
public class BPermission extends GmsEntity {

    /**
     * Name to be used for authenticating the user.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Column(unique = true, nullable = false, length = 255)
    private final String name;

    /**
     * Label to be shown to the final user.
     */
    @NotNull(message = CodeI18N.FIELD_NOT_NULL)
    @NotBlank(message = CodeI18N.FIELD_NOT_BLANK)
    @Size(max = 255, message = CodeI18N.FIELD_SIZE)
    @Column(unique = true, nullable = false, length = 255)
    private final String label;

    /**
     * Roles in which the permission is being used.
     */
    @ManyToMany(mappedBy = "permissions")
    private Set<BRole> roles = new HashSet<>();
}
