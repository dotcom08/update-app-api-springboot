package com.example.app.repositories;

import com.example.app.models.UpdateLog;
import com.example.app.models.UpdateStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateLogRepository extends JpaRepository<UpdateLog, Long> {

    List<UpdateLog> findByUserIdOrderByUpdateDateDesc(String userId);

    @Query("SELECT COUNT(ul) FROM UpdateLog ul WHERE ul.toVersion = :version AND ul.status = com.example.app.models.UpdateStatus.SUCCESS")
    Long countSuccessfulUpdatesToVersion(@Param("version") String version);

    @Query("SELECT COUNT(ul) FROM UpdateLog ul WHERE ul.toVersion = :version AND ul.status = :status")
    Long countByToVersionAndStatus(@Param("version") String version, @Param("status") UpdateStatus status);
}
