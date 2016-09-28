package com.confinale.awesomo.repository;

import com.confinale.awesomo.domain.Subsidiary;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Subsidiary entity.
 */
@SuppressWarnings("unused")
public interface SubsidiaryRepository extends JpaRepository<Subsidiary,Long> {

}
