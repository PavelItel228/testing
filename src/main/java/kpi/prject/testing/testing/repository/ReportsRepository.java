package kpi.prject.testing.testing.repository;

import kpi.prject.testing.testing.entity.Report;
import kpi.prject.testing.testing.entity.User;
import kpi.prject.testing.testing.entity.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportsRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByName(String name);
    //@Query("select r from Report r where owner = ?1 and name like %?2%")
    Optional<Page<Report>> findByOwnerAndNameContaining(User user ,String name, Pageable page);
    //@Query("select r from Report r join User u where u = ?1 and r.status = ?2 and r.name like %?3%")
    Page<Report> findAllByInspectorsAndStatusAndNameContaining(User inspector, ReportStatus status, String name, Pageable pageable);
}
