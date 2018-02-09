package com.gms.service.security.permission;

import com.gms.domain.security.permission.BPermission;
import com.gms.repository.security.permission.BPermissionRepository;
import com.gms.service.db.QueryService;
import com.gms.util.constant.BPermissionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * PermissionService
 *
 * @author Asiel Leal Celdeiro | lealceldeiro@gmail.com
 *
 * @version 0.1
 * Dec 12, 2017
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {

    private final BPermissionRepository repository;

    private final QueryService queryService;

    //region default permissions
    public Boolean createDefaultPermissions() {
        Boolean ok = true;
        final BPermissionConst[] constPermissions = BPermissionConst.values();
        for (BPermissionConst p : constPermissions) {
            ok = ok && repository.save(
                    new com.gms.domain.security.permission.BPermission(
                            p.toString(), p.toString().replaceAll("__", " ").replaceAll("_", " ")
                    )
            ) != null;
        }
        return ok;
    }
    //endregion

    /**
     * Return the list of permissions associated to some role(s). This (theses) role(s) is(are) the one(s) associated to
     * a user over an entity. Some conditions must be met in order to return the permissions:
     * - The following properties from the user must be true: <code>enabled</code>, <code>accountNonExpired</code>,
     * <code>accountNonLocked</code> and <code>credentialsNonExpired</code>
     * - The following properties from the role must be true: <code>enabled</code>
     */
    @SuppressWarnings("unchecked")
    public List<BPermission> findPermissionsByUserIdAndEntityId(long userId, long entityId) {
        String sql = "" +
                "SELECT p.* " +
                "from" +
                "  bauthorization auth" +
                "  INNER JOIN euser u ON auth.user_id = u.id" +
                "  INNER JOIN eowned_entity e ON auth.entity_id = e.id" +
                "  INNER JOIN brole r ON auth.role_id = r.id" +
                "  INNER JOIN brole_bpermission rp ON r.id = rp.brole_id" +
                "  INNER JOIN bpermission p ON rp.bpermission_id = p.id " +
                "WHERE" +
                "  auth.entity_id = :entityId" +
                "  AND auth.user_id = :userId" +
                "  AND u.enabled = TRUE" +                                                          // by default users are not enabled
                "  AND (u.account_non_expired = TRUE OR u.account_non_expired IS NULL)" +            // by default users' account are not expired
                "  AND (u.account_non_locked = TRUE OR u.account_non_locked IS NULL)" +              // by default users are not locked
                "  AND (u.credentials_non_expired = TRUE OR u.credentials_non_expired IS NULL)" +    // by default users' credentials are not expired
                "  AND r.enabled = TRUE";                                                           // by default roles are not enabled

        final Query query = queryService.createNativeQuery(sql, BPermission.class);
        query.setParameter("userId", userId).setParameter("entityId", entityId);

        return query.getResultList();
    }
}
