package com.confinale.awesomo.repository;

import com.confinale.awesomo.domain.WorkLog;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the WorkLog entity.
 */
@SuppressWarnings("unused")
public interface WorkLogRepository extends JpaRepository<WorkLog,Long> {

    @Query("select workLog from WorkLog workLog where workLog.user.login = ?#{principal.username}")
    List<WorkLog> findByUserIsCurrentUser();

}
