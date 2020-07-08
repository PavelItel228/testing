package kpi.prject.testing.testing.repository;

import kpi.prject.testing.testing.entity.Archive;
import kpi.prject.testing.testing.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchiveRepository extends JpaRepository<Archive ,Long> {
    Optional<Archive> findFirstByReportOrderByIdDesc(Report report);
}
