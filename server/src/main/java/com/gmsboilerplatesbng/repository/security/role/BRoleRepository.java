package com.gmsboilerplatesbng.repository.security.role;

import com.gmsboilerplatesbng.domain.secuirty.role.BRole;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "role", path = "role")
public interface BRoleRepository extends PagingAndSortingRepository<BRole, Long> {
}