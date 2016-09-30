package com.confinale.awesomo.repository;

import com.confinale.awesomo.domain.Subsidiary;

import com.confinale.awesomo.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Subsidiary entity.
 */
@SuppressWarnings("unused")
public interface SubsidiaryRepository extends JpaRepository<Subsidiary,Long> {
    Optional<Subsidiary> findOneByParentId(Long parentId);
}
